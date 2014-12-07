package br.diastecnologia.shopmaquinas.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.JsonResponse;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.AdPropertyUtils;

@Controller
public class HomeController implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private AdDao adDao;
	
	@Get("/")
	public void index(){
		List<Ad> ads = adDao.listAds( Arrays.asList(AdPropertyUtils.getInstance(adDao).getHighlightProperty()), null, 1);
		result.include("ads", ads);
	}
	
	@Post("/logon")
	public void logon( @Named("username") String username, @Named("password") String password){
		JsonResponse response = new JsonResponse("Login realizado com sucesso.");
		
		Optional<User> user = adDao.users().where( 
				u-> u.getUsername().equals(username)
				&& u.getPassword().equals( password )).findFirst();
		
		if( !user.isPresent() ){
			response.setCode( JsonResponseCode.ERROR.toString() );
			response.setMessage("Usu�rio e/ou senha inv�lidos!");
		}else{
			User userLogged = user.get();
			response.setData( userLogged.getUsername() );
			session.setUser( userLogged );
		}
		
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
	
	@Post("/sair")
	public void logout(){
		session.setUser( null );
		
		JsonResponse response = new JsonResponse("Logout realizado com sucesso.");
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
}
