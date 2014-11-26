package br.diastecnologia.shopmaquinas.controller;

import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.ContractDefinition;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class UserController{
	
	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private AdDao dao;
	
	@Get("/contrato/cadastro")
	public void register(){
		if( session.getUser() != null && session.getUser().getPerson() != null ){
			result.include("update", true);
			result.include("person", session.getUser().getPerson() );
		}
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
	@Transactional
	public void saveNewRegister( @Named("person")Person person, @Named("user")User user, @Named("contractDefinitionID")int contractDefinitionID, @Named("companyID")int companyID)
	{
		try{
			ContractDefinition def = dao.contractDefinitions().where( c-> c.getContractDefinitionID() == contractDefinitionID).findFirst().get();
			Contract contract = new Contract();
			contract.setContractDefinition(def);
			contract.setStartDate( Calendar.getInstance().getTime() );
			
			person.setContracts( Arrays.asList(contract) );
			user.setPerson( person );
			user.setUsername( person.getDocuments().get( 0 ).getDocumentNumber() );
			
			dao.getEM().persist( person.getContracts().get( 0 ));
			dao.getEM().persist( person.getDocuments().get( 0 ));
			dao.getEM().persist( person.getAddress());
			dao.getEM().persist( person );
			dao.getEM().persist( user );
			
			result.include("update", true);
			result.include("message", "Novo cadastro salvo com sucesso!");
			
		}catch(Exception ex){
			result.include("errorMessage", ex.getMessage() );
			result.redirectTo( UserController.class ).register();
			return;
		}
	}
	
	@Post("/contrato/salvar-contrato")
	@Transactional
	public void saveRegister( @Named("person")Person person)
	{
		try{
			dao.getEM().persist( person.getDocuments().get( 0 ));
			dao.getEM().merge( person.getAddress());
			dao.getEM().merge( person );
			
			result.include("update", true);
			result.include("message", "Cadastro salvo com sucesso!");
			
		}catch(Exception ex){
			result.include("errorMessage", ex.getMessage() );
			result.redirectTo( UserController.class ).register();
			return;
		}
	}
	
}
