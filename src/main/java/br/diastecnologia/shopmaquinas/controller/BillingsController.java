package br.diastecnologia.shopmaquinas.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.SenderDocument;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.direct.checkout.BoletoCheckout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.enums.PaymentMode;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.SessionService;
import br.com.uol.pagseguro.service.TransactionService;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Billing;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.enums.BillingStatus;
import br.diastecnologia.shopmaquinas.enums.PersonType;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class BillingsController {

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private AdDao adDao;
	
	private Integer billingDays = 0;
	
	private boolean production = !PagSeguroConfig.isSandboxEnvironment();
	
	private static Logger logger = Logger.getLogger("billings");
	
	@Post
	@Transactional
	@Path("/contrato/prorrogar-contrato")
	public void addBilling(@Named("contractID") int contractID ){
		Contract contract = adDao.contracts().filter( c-> c.getContractID() == contractID ).findFirst().get();
		
		double amount = contract.getContractDefinition().getContractDefinitionPropertyValues().stream().filter( p-> p.getContractDefinitionProperty().getName().equals("PRICE")).findFirst().get().getDoubleValue();
		Billing billing = new Billing();
		billing.setAmount(amount);
		billing.setContract(contract);
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.add( Calendar.DAY_OF_MONTH, billingDays);
		billing.setDueDate(dueDate.getTime());
		billing.setStatus(BillingStatus.PENDING);
		
		contract.setBillings(new ArrayList<Billing>());
		contract.getBillings().add(billing);
		
		result.include("ErrorMessage", "Foi criada uma nova fatura para o contrato, assim que identificarmos o pagamento dela o seu contrato terá a data de expiração atualizada.");
		result.redirectTo( ContractController.class ).contracts();
		return;
	}
	
	@Get
	@Path("/gerar-boleto")
	public void getBilling( @Named("payer") String payer, @Named("billingID") int billingID ){
		
		Optional<Billing> bilOp = adDao.billings().where( b-> b.getBillingID() == billingID ).findFirst();
		if( !bilOp.isPresent() ){
			result.include("ErrorMessage", "Houve um problema durante a geração do seu contrato.");
			result.redirectTo( ContractController.class ).contracts();
			return;
		}
		Billing bil = bilOp.get();
		
		BoletoCheckout request = getRequest(bil.getBillingID(), bil.getContract().getPerson(), bil.getAmount());
		request.getSender().setHash(payer);
		
		if( !production ){
			request.getSender().setEmail("marcelo@sandbox.pagseguro.com.br");
			BigDecimal amount = request.getItems().get( 0 ).getAmount();
			amount = amount.add( new BigDecimal("-1.00"));
			request.getItems().get( 0 ).setAmount(amount);
		}
		
		try {
            final AccountCredentials accountCredentials = PagSeguroConfig.getAccountCredentials();

            SessionService.createSession(accountCredentials);
            final Transaction transaction = TransactionService.createTransaction(accountCredentials, request);

            if (transaction != null) {
            	logger.info("Transaction Code - Default Mode: " + transaction.getCode());
            	this.result.redirectTo(transaction.getPaymentLink());
            	return;
            }else{
            	result.include("ErrorMessage", "Houve um problema durante a geração do seu contrato.");
        		result.redirectTo( ContractController.class ).contracts();
     			return;
            }
        } catch (PagSeguroServiceException e) {
        	logger.info(e.getMessage());

        	result.include("ErrorMessage", "Houve um problema durante a geração do seu contrato: " + e.getMessage());
    		result.redirectTo( ContractController.class ).contracts();
        	
			return;
        }
	}
	
	@Post
	@Transactional(rollbackOn=Exception.class)
	@Path("/pagseguro-notification")
	public void receiveNotification( @Named("notificationCode") String notificationCode ){
		try{
			final AccountCredentials accountCredentials = PagSeguroConfig.getAccountCredentials();
			Transaction trans = NotificationService.checkTransaction(accountCredentials, notificationCode);
			
			logger.info(trans.getReference());
			logger.info(trans.getStatus() + "\n");
			
			int billingID = Integer.parseInt(trans.getReference().split("-")[1]);
			Optional<Billing> billingOp = adDao.billings().where( b-> b.getBillingID() == billingID ).findFirst();
			if( billingOp.isPresent()){
				Billing billing = billingOp.get();
				if( trans.getStatus() == TransactionStatus.PAID ){
					billing.setStatus( BillingStatus.PAID );
					
					Calendar expiration = Calendar.getInstance();
					if( billing.getContract().getEndDate() != null ){
						expiration.setTime(billing.getContract().getEndDate());
					}
					expiration.add( Calendar.MONTH, 1);
					billing.getContract().setEndDate( expiration.getTime() );
					for( Ad ad: billing.getContract().getAds()){
						if( ad.getEndDate().after(ad.getStartDate())){
							ad.setEndDate(expiration.getTime());
						}
					}
					
				}else if( trans.getStatus() == TransactionStatus.CANCELLED ){
					billing.setStatus( BillingStatus.CANCELLED );
				}
			}else{
				logger.info("Fatura não encontrada.");
			}
			
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		this.result.use( Results.json() ).from( "OK" ).serialize();
	}
	
	private BoletoCheckout getRequest( int billingID, Person person , double amount){
		BoletoCheckout request = new BoletoCheckout();
		
		request.setPaymentMode(PaymentMode.DEFAULT);
        request.setCurrency(Currency.BRL);
        
        request.setReceiverEmail("contato@shopmaquinas.com.br");

        request.setNotificationURL("http://www.shopmaquinas.com.br/pagseguro-notification");

        request.setReference("TST-" + billingID);

        String phone = person.getPhone().replace('(', ' ').replace(')', ' ').replace('-', ' ').replaceAll(" ", "");
        
        Sender buyer = new Sender();
        buyer.setName( person.getFirstname() + " " + person.getLastname() );
        buyer.setPhone( new Phone( phone.substring(0,2), phone.substring(2)));
        buyer.setDocuments( new ArrayList<SenderDocument>() );
        buyer.getDocuments().add( getSenderDocument(person) );
        buyer.setEmail(person.getEmail());
        request.setSender(buyer);
        
        request.setShippingAddress(getSenderAddress(person));

        request.setShippingCost(new BigDecimal("0.00"));

        NumberFormat f = NumberFormat.getInstance(Locale.US);
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
        
        request.addItem(new Item("1", //
                "Assinatura", //
                Integer.valueOf(1), //
                new BigDecimal(f.format(amount)),
                0l,
                new BigDecimal("0.00")));
		
		return request;
	}
	
	private Address getSenderAddress( Person person ){
		if( person == null || person.getAddress() == null ){
			return null;
		}
		
		Address add = new Address();
		
		add.setCountry("BRA");
		add.setCity( person.getAddress().getCity());
		add.setState( person.getAddress().getUf());
		add.setStreet( person.getAddress().getStreet());
		add.setNumber( person.getAddress().getNumber());
		add.setDistrict( person.getAddress().getNeighborhood());
		add.setPostalCode( person.getAddress().getCep());
		add.setComplement( person.getAddress().getComplement());
		
		return add;
	}
	
	private SenderDocument getSenderDocument( Person person ){
		if( person == null || person.getDocuments() == null || person.getDocuments().size() < 1 ){
			return null;
		}
		
		try{
			String value = person.getDocuments().get( 0 ).getDocumentNumber();
			
			SenderDocument doc = new SenderDocument();
			if( person.getPersonType() == PersonType.HUMAN ){
				
				doc.setType( DocumentType.CPF );
				doc.setValue( 
						value.substring( 0 , 3 ) + "." +
						value.substring( 3 , 6 ) + "." +
						value.substring( 6 , 9 ) + "-" +
						value.substring( 9 , 11 )
				);
			}else{
				doc.setType( DocumentType.CNPJ );
				doc.setValue( 
						value.substring( 0 , 2 ) + "." +
						value.substring( 2 , 5 ) + "." +
						value.substring( 5 , 8 ) + "/" +
						value.substring( 8 , 12 ) + "-" +
						value.substring( 12 , 14 )
				);
			}
			
			return doc;
		}catch(Exception ex){
			return null;
		}
	}
	
	
}
