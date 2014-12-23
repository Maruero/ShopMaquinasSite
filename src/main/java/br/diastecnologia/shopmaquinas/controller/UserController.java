package br.diastecnologia.shopmaquinas.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Billing;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.ContractDefinition;
import br.diastecnologia.shopmaquinas.bean.ContractDefinitionPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Image;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.enums.BillingStatus;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class UserController{
	
	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private AdDao dao;
	
	//@Inject
	//@Property("billing.days")
	private Integer billingDays = 5;
	
	@Get("/contrato/novo-contrato")
	public void newContract(){
		List<ContractDefinition> definitions = dao.contractDefinitions().toList();
		result.include("definitions", definitions);
	}
	
	@Get("/contrato/escolher-contrato")
	public void setContract( @Named("contractDefinitionID")int contractDefinitionID){
		session.setRedirectObject(contractDefinitionID);
		result.redirectTo( UserController.class ).register();
	}
	
	@Get("/contrato/cadastro")
	public void register(){
		if( session.getUser() != null && session.getUser().getPerson() != null ){
			result.include("update", true);
			result.include("person", session.getUser().getPerson() );
		}
		
		int contractDefinitionID = (Integer)session.getRedirectObject();
		session.setRedirectObject(contractDefinitionID);
		
		ContractDefinition def = dao.contractDefinitions().where( c-> c.getContractDefinitionID() == contractDefinitionID ).findFirst().get();
		ContractDefinitionPropertyValue prop = def.getContractDefinitionPropertyValues().stream().filter( 
				p -> p.getContractDefinitionProperty().getName().equals(br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty.TYPE.toString())).findFirst().get();
		boolean isCompanyContract = prop.getValue().toUpperCase().equals("EMPRESARIAL");
		
		session.setUploadedImages( new ArrayList<String>() );
		
		result.include("isCompanyContract", isCompanyContract);
		result.include("contractDefinitionID", contractDefinitionID);
	}
	
	@Get("/contrato/checar-cpf")
	public void cpfCheck(@Named("cpf") String cpf){
		long count = dao.documents().where( d-> d.getDocumentType().equals("CPF") && d.getDocumentNumber().equals(cpf)).count();
		result.use( Results.json() ).from(count).recursive().serialize();
	}
	
	@Get("/contrato/checar-usename")
	public void usernameCheck(@Named("usermane") String username){
		long count = dao.users().where( u-> u.getUsername().equals(username)).count();
		result.use( Results.json() ).from(count).recursive().serialize();
	}
	
	@Post("/contrato/salvar-novo-contrato")
	@Transactional(rollbackOn=Exception.class)
	public void saveNewRegister( @Named("person")Person person, @Named("user")User user, @Named("contractDefinitionID")int contractDefinitionID)
	{
		try{
			ContractDefinition def = dao.contractDefinitions().where( c-> c.getContractDefinitionID() == contractDefinitionID).findFirst().get();
			Contract contract = new Contract();
			contract.setPerson(person);
			contract.setContractDefinition(def);
			contract.setStartDate( Calendar.getInstance().getTime() );
			
			double amount = def.getContractDefinitionPropertyValues().stream().filter( p-> p.getContractDefinitionProperty().getName().equals("PRICE")).findFirst().get().getDoubleValue();
			Billing billing = new Billing();
			billing.setAmount(amount);
			billing.setContract(contract);
			
			Calendar dueDate = Calendar.getInstance();
			dueDate.add( Calendar.DAY_OF_MONTH, billingDays);
			billing.setDueDate(dueDate.getTime());
			billing.setStatus(BillingStatus.PENDING);
			
			contract.setBillings(new ArrayList<Billing>());
			contract.getBillings().add(billing);
			
			person.setContracts( Arrays.asList(contract) );
			user.setPerson( person );
			user.setUsername( person.getDocuments().get( 0 ).getDocumentNumber() );
			
			if(session.getUploadedImages() != null )
			{
				person.setImages(new ArrayList<Image>());
				for( String image : session.getUploadedImages() )
				{
					Image i = new Image();
					i.setPerson(person);
					i.setPath(image);
					person.getImages().add(i);
				}
			}

			String username = user.getUsername();
			String password = user.getPassword();
			
			user = dao.saveNewUser(user);
			User userLogged = dao.users().where( 
					u-> u.getUsername().equals(username)
					&& u.getPassword().equals( password )).findFirst().get();
			
			session.setUser( userLogged );
			
			result.include("update", true);
			result.include("message", "Novo cadastro salvo com sucesso!");
			result.redirectTo(ContractController.class).contracts();
			
		}catch(Exception ex){
			session.setRedirectObject(contractDefinitionID);
			result.include("errorMessage", ex.getMessage() );
			result.redirectTo( UserController.class ).register();
			return;
		}
	}
	
}
