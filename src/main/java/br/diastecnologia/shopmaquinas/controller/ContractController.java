package br.diastecnologia.shopmaquinas.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class ContractController {

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	protected AdDao dao;
	
	@Get("/contratos")
	public void contracts(){
		if( session.getUser() == null){
			result.include("ErrorMessage", "Você precisa estar logado para acessar essa página!");
			result.redirectTo(HomeController.class).index();
			return;
		}
		
		Person person = dao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
		//Person person = dao.getEM().merge(session.getUser().getPerson());
		
		for( Contract contract : person.getContracts() ){
			System.out.println( contract.getAds().size() );
		}
		
		result.include("restricted", true);
		result.include("contracts", person.getContracts());
	}
	
}
