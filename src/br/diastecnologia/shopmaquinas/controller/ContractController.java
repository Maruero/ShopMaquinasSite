package br.diastecnologia.shopmaquinas.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class ContractController {

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Get("/contratos")
	public void contracts(){
		if( session.getUser() == null){
			result.include("ErrorMessage", "Você precisa estar logado para acessar essa página!");
			result.redirectTo(HomeController.class).index();
			return;
		}
		
		result.include("restricted", true);
		result.include("contracts", session.getUser().getPerson().getContracts());
	}
	
}
