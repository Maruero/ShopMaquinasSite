package br.diastecnologia.shopmaquinas.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Billing;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.ContractDefinition;
import br.diastecnologia.shopmaquinas.bean.ContractDefinitionPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Image;
import br.diastecnologia.shopmaquinas.bean.JsonResponse;
import br.diastecnologia.shopmaquinas.bean.Message;
import br.diastecnologia.shopmaquinas.bean.MessageDTO;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.bean.UserToken;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.email.EmailConfiguration;
import br.diastecnologia.shopmaquinas.email.EmailSender;
import br.diastecnologia.shopmaquinas.enums.BillingStatus;
import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;
import br.diastecnologia.shopmaquinas.enums.MessageStatus;
import br.diastecnologia.shopmaquinas.enums.PersonType;
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
	private Integer billingDays = 0;
	
	@Inject
	@Named("EmailSender")
	private EmailSender emailSender;
	
	@Inject
	@Named("EmailConfiguration")
	private EmailConfiguration emailConfiguration;
	
	@Get("/contrato/novo-contrato")
	public void newContract(){
		List<ContractDefinition> definitions = dao.contractDefinitions().toList();
		
		Comparator<ContractDefinition> comparator = (ContractDefinition c1, ContractDefinition c2) -> c1.getOrder() - c2.getOrder();
		if( session.getUser() != null && session.getUser().getPerson() != null ){
			if( session.getUser().getPerson().getPersonType() == PersonType.COMPANY ){
				
				definitions = definitions.stream().filter( d->
					d.getContractDefinitionPropertyValues().stream().filter( p-> 
						p.getContractDefinitionProperty().getName().equals( br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty.TYPE.toString() )
						&& p.getValue().toUpperCase().equals("EMPRESARIAL")
						).count() > 0
					).collect(Collectors.toList());
				
			}else{
				
				definitions = definitions.stream().filter( d->
				d.getContractDefinitionPropertyValues().stream().filter( p-> 
					p.getContractDefinitionProperty().getName().equals( br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty.TYPE.toString() )
					&& p.getValue().toUpperCase().equals("PARTICULAR")
					).count() > 0
				).collect(Collectors.toList());
				
			}
		}
		
		definitions.sort(comparator);
		result.include("definitions", definitions);
	}
	
	@Get("/contrato/escolher-contrato")
	@Transactional
	public void setContract( @Named("contractDefinitionID")int contractDefinitionID){
		
		if( session.getUser() != null && session.getUser().getPerson() != null ){
		
			Person person = dao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
			
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
			
			person.getContracts().add(contract);
			
			result.redirectTo( ContractController.class ).contracts();
			return;
		}else{
			session.setRedirectObject(contractDefinitionID);
			result.redirectTo( UserController.class ).register();
			return;
		}
	}
	
	@Get("/contrato/cadastro")
	public void register(){
		if( session.getUser() != null && session.getUser().getPerson() != null ){
			
			Person person = dao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
			
			result.include("update", true);
			result.include("person", person );
			session.setUploadedImages( person.getImages().stream().map( i-> i.getPath()).collect( Collectors.toList() ) );
			session.setRedirectObject( person.getContracts().get( 0 ).getContractDefinition().getContractDefinitionID() );
		}else{
			result.include("person", new Person(true) );
			session.setUploadedImages( new ArrayList<String>() );
		}
		
		if( !session.redirectObjectIs(Integer.class)){
			result.include("ErrorMessage", "Sessão expirada, por favor, comece novamente.");
			result.redirectTo( HomeController.class ).index();
			return;
		}
		
		int contractDefinitionID = (Integer)session.getRedirectObject();
		session.setRedirectObject(contractDefinitionID);
		
		ContractDefinition def = dao.contractDefinitions().where( c-> c.getContractDefinitionID() == contractDefinitionID ).findFirst().get();
		ContractDefinitionPropertyValue prop = def.getContractDefinitionPropertyValues().stream().filter( 
				p -> p.getContractDefinitionProperty().getName().equals(br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty.TYPE.toString())).findFirst().get();
		boolean isCompanyContract = prop.getValue().toUpperCase().equals("EMPRESARIAL");
		
		result.include("isCompanyContract", isCompanyContract);
		result.include("contractDefinitionID", contractDefinitionID);
	}
	
	@Post("/trocar-senha")
	@Transactional
	public void changePassword( @Named("password") String password, @Named("newPassword") String newPassword ){
		final User user = session.getUser();
		if( session.getUser().getPassword().equals(password) ){
			
			User changedUser = dao.users().where( u-> u.getUserID() == user.getUserID() ).findFirst().get();
			changedUser.setPassword(newPassword);
			
			this.result.use( Results.json() ).from( new JsonResponse("Senha trocada com sucesso.")).recursive().serialize();
		}else{
			this.result.use( Results.json() ).from( new JsonResponse( JsonResponseCode.ERROR, "Senha atual não confere.")).recursive().serialize();
		}
	}
	
	/*
	@Post("/remover-imagem")
	public void removeImage(@Named("path") String path ){
		if( session.getUploadedImages() != null && session.getUploadedImages().contains(path)){
			session.getUploadedImages().remove(path);
		}
		this.result.use( Results.json() ).from( new JsonResponse("Imagem removida") ).recursive().serialize();
	}
	*/
	
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
	
	@Get("/area-do-cliente")
	public void userArea(){
		if( session.getUser() == null){
			result.include("ErrorMessage", "Você precisa estar logado para acessar essa página!");
			result.redirectTo(HomeController.class).index();
			return;
		}
		
		Person person = dao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
		
		Hibernate.initialize(person.getContracts());
		for( Contract contract : person.getContracts() ){
			Hibernate.initialize(contract.getAds());
		}
		
		List<Message> messages = person.getMessages().stream().filter( m-> 
			m.getStatus() == MessageStatus.NEW ||
			m.getStatus() == MessageStatus.READ
		).collect( Collectors.toList());
		
		List<Ad> ads = new ArrayList<Ad>();
		for( Contract contract : person.getContracts() ){
			for( Ad ad : contract.getAds() ){
				if( ad.getEndDate() == null || Calendar.getInstance().getTime().before( ad.getEndDate() ) ){
					ads.add(ad);
				}
			}
		}
		
		result.include("restricted", true);
		this.result.include("person", person);
		this.result.include("messages", messages);
		this.result.include("activeAds", ads);
		
	}
	
	@Get("/obter-mensagem")
	@Transactional
	public void getMessage( @Named("messageID") int messageID ){
		JsonResponse response = new JsonResponse("Mensagem obtida com sucesso.");
		try{
			Message message = dao.messages().where( m-> m.getMessageID() == messageID).findFirst().get();
			message.setStatus( MessageStatus.READ );
			response.setData( new MessageDTO(message));
		}catch(Exception ex){
			response.setCode( JsonResponseCode.ERROR.toString() );
			response.setMessage( ex.getMessage() );
		}
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
	
	@Post("/excluir-mensagem")
	@Transactional
	public void deleteMessage( @Named("messageID") int messageID ){
		JsonResponse response = new JsonResponse("Mensagem removida com sucesso.");
		try{
			Message message = dao.messages().where( m-> m.getMessageID() == messageID).findFirst().get();
			message.setStatus( MessageStatus.EXCLUDED );
		}catch(Exception ex){
			response.setCode( JsonResponseCode.ERROR.toString() );
			response.setMessage( ex.getMessage() );
		}
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
	
	@Post("/excluir-anuncio")
	@Transactional
	public void deleteAd( @Named("adID") int adID ){
		JsonResponse response = new JsonResponse("Anúncio removido com sucesso.");
		try{
			Ad ad = dao.getAd(adID);
			
			Calendar expired = Calendar.getInstance();
			expired.setTime(ad.getStartDate());
			expired.add( Calendar.DAY_OF_YEAR , -1);
			
			ad.setEndDate(expired.getTime());
		}catch(Exception ex){
			response.setCode( JsonResponseCode.ERROR.toString() );
			response.setMessage( ex.getMessage() );
		}
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
	
	@Get("/acesso-direto")
	public void directAccess(@Named("token") String token ){
		try{
			
			Optional<UserToken> userToken = dao.tokens().where( 
					t-> t.getToken().equals(token)
					&& t.getExpirationDate().after(Calendar.getInstance().getTime()
			)).findFirst();
			
			if( userToken.isPresent() ){
				session.setUser(userToken.get().getUser());
				result.redirectTo(UserController.class).userArea();
			}else{
				result.redirectTo(HomeController.class).index();
			}
		}catch(Exception ex){
			result.redirectTo(HomeController.class).index();
		}
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
			
			sendRegisterEmail(userLogged);
			
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
	
	private void sendRegisterEmail( User user ){
		
		try{
			String token = UUID.randomUUID().toString();
			Calendar expiration = Calendar.getInstance();
			expiration.add( Calendar.MONTH, 1);
			
			UserToken userToken = new UserToken();
			userToken.setToken(token);
			userToken.setUser(user);
			userToken.setExpirationDate(expiration.getTime());
			
			dao.getEM().persist(userToken);
			
			String body = emailConfiguration.getRegisterHtml(user, token);
			if( body != null ){
				emailSender.SendEmail("Cadastro realizado com sucesso.", body, user.getPerson(), null);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	@Post("/contrato/salvar-dados")
	@Transactional(rollbackOn=Exception.class)
	public void saveData( @Named("person")Person person){
		try{
			Person oldPerson = dao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
			
			oldPerson.getImages().clear();
			if(session.getUploadedImages() != null )
			{
				for( String image : session.getUploadedImages() )
				{
					Image i = new Image();
					i.setPerson(oldPerson);
					i.setPath(image);
					oldPerson.getImages().add(i);
				}
			}
			
			oldPerson.setAddress( person.getAddress() );
			oldPerson.setFirstname( person.getFirstname() );
			oldPerson.setLastname( person.getLastname() );
			oldPerson.setGender( person.getGender() );
			oldPerson.setPhone( person.getPhone() );
			
			result.include("update", true);
			result.include("message", "Novo cadastro salvo com sucesso!");
			result.redirectTo(ContractController.class).contracts();
			
		}catch(Exception ex){
			
			session.setRedirectObject( person.getContracts().get( 0 ).getContractDefinition().getContractDefinitionID() );
			
			result.include("errorMessage", ex.getMessage() );
			result.redirectTo( UserController.class ).register();
			return;
		}
	}
	
}
