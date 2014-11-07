package br.diastecnologia.shopmaquinas.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.dao.AdDao;
import br.diastecnologia.shopmaquinas.dao.Dao;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.AdPropertyUtils;

@Controller
public class HomeController {

	@Inject
	private Result result;
	
	@Inject
	private SessionBean session;
	
	private AdDao adDao = new AdDao();
	private Dao dao = (Dao)adDao;
	
	@Get("/")
	public void index(){
		List<Ad> ads = adDao.listAds( Arrays.asList(AdPropertyUtils.getHighlightProperty()), 1);
		result.include("ads", ads);
	}
	
	@Get("/login")
	public void login(@Named("nextPage") String nextPage ){
		session.setRedirectObject(nextPage);
	}
	
	@Post("/logon")
	public void logon( @Named("username") String username, @Named("password") String password){
		
		String nextPage = (String)session.getRedirectObject();
		if( nextPage == null || nextPage.length() < 1 ){
			nextPage = ".";
		}
		
		Optional<User> user = dao.users().where( 
				u-> u.getUsername().equals(username)
				&& u.getPassword().equals( password )).findFirst();
		
		if( !user.isPresent() ){
			result.include("ErrorMessage", "Usuário e/ou senha inválidos!");
			result.redirectTo( HomeController.class ).login(nextPage);
			return;
		}else{
			session.setUser( user.get() );
			result.redirectTo(nextPage);
			return;
		}
	}
	
}
