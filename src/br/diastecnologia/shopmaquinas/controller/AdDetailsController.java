package br.diastecnologia.shopmaquinas.controller;

import java.util.Calendar;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.JsonResponse;
import br.diastecnologia.shopmaquinas.bean.Message;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.dao.AdDao;
import br.diastecnologia.shopmaquinas.email.EmailConfiguration;
import br.diastecnologia.shopmaquinas.email.EmailSender;
import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class AdDetailsController{

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	@Named("EmailSender")
	private EmailSender emailSender;
	
	@Inject
	@Named("EmailConfiguration")
	private EmailConfiguration emailConfiguration;
	
	private AdDao adDao = new AdDao();
	
	
	@Get
	@Path("/anuncios/detalhes-do-anuncio/{adID}")
	public void getAdDetails( @Named("adID") Integer adID ){
		Optional<Ad> adOptional = adDao.ads().where( a-> a.getAdID() == adID ).findFirst();
		Ad adToShow = null;
		if( adOptional.isPresent()){
			adToShow = adOptional.get();
		}else{
			result.redirectTo( AdDetailsController.class).notFound();
			return;
		}
		
		result.include("ad", adToShow);
	}
	
	@Get("/anuncios/nao-encontrado")
	public void notFound( ){
		
	}
	
	@Post("/anuncios/salvar-proposta")
	public void saveProposal( @Named("adID") int adID, @Named("text") String text, @Named("person") Person person){
		JsonResponse response = new JsonResponse("Proposta realizada com sucesso.");
		try{
			
			Ad ad = adDao.getAd(adID);
			String body = emailConfiguration.getProposalText(person, ad.getPerson(), ad, text);
			
			Message message = new Message();
			message.setAd(ad);
			message.setDate( Calendar.getInstance().getTime() );
			message.setFromPerson( person );
			message.setToPerson( ad.getPerson() );
			message.setText( "Proposta: " + text );
			
			adDao.beginTransaction();
			
			if( person.getPersonID() < 1 ){
				adDao.em.persist(person);
			}
			
			adDao.em.persist(message);
			adDao.commitTransaction();
			
			emailSender.SendEmail(emailConfiguration.getProposalSubject(), body, body, person, ad.getPerson() );
			
		}catch(Exception ex){
			
			try{
				if( adDao.isActiveTransaction() ){
					adDao.rollbackTransaction();
				}
			}catch(Exception ex1){}
			
			response.setCode( JsonResponseCode.ERROR.toString() );
			response.setMessage( "Houve um problema ao realizar a proposta: " + ex.getMessage() );
		}
		result.use( Results.json() ).from( response ).recursive().serialize();
	}
	
}
