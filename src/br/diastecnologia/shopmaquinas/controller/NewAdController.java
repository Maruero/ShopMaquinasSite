package br.diastecnologia.shopmaquinas.controller;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.JsonResponse;
import br.diastecnologia.shopmaquinas.dao.AdDao;
import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.ContractUtils;
import br.diastecnologia.shopmaquinas.utils.FileUtils;

@Controller
public class NewAdController {

	@Inject
	private Result result;
	
	@Inject
	private SessionBean session;
	
	@Inject
	private FileUtils fileUtils;
	
	private AdDao adDao = new AdDao();
	
	@Get("/anunciar/novo-anuncio")
	public void newAd(){
		Contract contract = ContractUtils.getValidContract(session.getUser().getPerson());
		if( contract == null ){
			result.include("ErrorMessage", "Somente anunciantes ativos podem anunciar. Para anunciar contrate um de nossos planos.");
			result.redirectTo( HomeController.class ).index();
		}
		result.include("Contract", contract);
	}
	
	@Post("/anunciar/novo-anuncio")
	public void saveAd(Ad ad){
		try{
			ad.setPerson(session.getUser().getPerson());
			ad = adDao.saveAd(ad, session.getUploadedImages());
			result.redirectTo(AdDetailsController.class).getAdDetails( ad.getAdID() );
		}catch(Exception ex){
			result.include("ErrorMessage", ex.getMessage());
		}
	}
	
	@Post("/anunciar/salvar-imagem")
	public void saveImage( UploadedFile image ){
		try{
			if( session.getUploadedImages() == null ){
				session.setUploadedImages( new ArrayList<String>() );
			}
			
			String path = fileUtils.saveFile(image);
			session.getUploadedImages().add(path);
			
			result.use( Results.json() ).from( new JsonResponse("Imagem carregada com sucesso" , path) ).recursive().serialize();
		}catch(Exception ex){
			result.use( Results.json() ).from( new JsonResponse( JsonResponseCode.ERROR, ex.getMessage() ));
		}
	}
	
}