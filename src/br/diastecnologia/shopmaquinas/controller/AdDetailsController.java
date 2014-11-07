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
import br.diastecnologia.shopmaquinas.dao.AdDao;
import br.diastecnologia.shopmaquinas.email.EmailConfiguration;
import br.diastecnologia.shopmaquinas.email.EmailSender;
import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.CurrencyUtils;

@Controller
public class AdDetailsController {

	@Inject
	private Result result;
	
	@Inject
	@Named("EmailSender")
	private EmailSender emailSender;
	
	@Inject
	@Named("EmailConfiguration")
	private EmailConfiguration emailConfiguration;
	
	@Inject
	private SessionBean session;
	
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
	public void saveProposal( @Named("adID") int adID, @Named("price") double price, @Named("text") String text){
		JsonResponse response = new JsonResponse("Proposta realizada com sucesso.");
		try{
			
			Ad ad = adDao.getAd(adID);
			String body = emailConfiguration.getProposalText(session.getUser().getPerson(), ad.getPerson(), ad, price, text);
			
			Message message = new Message();
			message.setAd(ad);
			message.setDate( Calendar.getInstance().getTime() );
			message.setFromPerson( session.getUser().getPerson() );
			message.setToPerson( ad.getPerson() );
			message.setText( "Proposta: " + CurrencyUtils.toString(price) + " - " + text );
			
			adDao.beginTransaction();
			adDao.em.persist(message);
			adDao.commitTransaction();
			
			emailSender.SendEmail("Proposta recebida", body, body, ad.getPerson() );
			emailSender.SendEmail("Proposta recebida", body, body, session.getUser().getPerson() );
			
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
