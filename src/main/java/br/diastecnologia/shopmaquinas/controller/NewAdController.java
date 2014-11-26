package br.diastecnologia.shopmaquinas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.AdProperty;
import br.diastecnologia.shopmaquinas.bean.AdPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.AdPropertyUtils;
import br.diastecnologia.shopmaquinas.utils.ContractUtils;
import br.diastecnologia.shopmaquinas.utils.FileUtils;

@Controller
public class NewAdController{
	
	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private FileUtils fileUtils;
	
	@Inject
	private AdDao adDao;
	
	@Get("/anunciar/novo-anuncio")
	public void newAd(){
	
		Person person = adDao.getEM().merge(session.getUser().getPerson());
		
		Contract contract = ContractUtils.getValidContract(person.getContracts());
		if( contract == null ){
			result.include("ErrorMessage", "Somente anunciantes ativos podem anunciar. Para anunciar contrate um de nossos planos.");
			result.redirectTo( HomeController.class ).index();
		}
		session.setUploadedImages( new ArrayList<String>() );
		result.include("Contract", contract);
	}
	
	@Post("/anunciar/novo-anuncio")
	@Transactional
	public void saveAd(@Named("ad")Ad ad, @Named("otherProperties")List<AdPropertyValue> otherProperties){
		try{
			ad.getAdPropertyValues().addAll(otherProperties);
			
			ad.setPerson(session.getUser().getPerson());
			ad = adDao.saveAd(ad, session.getUploadedImages());
			result.redirectTo(AdDetailsController.class).getAdDetails( ad.getAdID() );
		}catch(Exception ex){
			ex.printStackTrace();
			result.include("ErrorMessage", ex.getMessage());
			result.redirectTo( NewAdController.class ).newAd();
		}
	}
	
	@Post("/anunciar/novo-anuncio/visualizar")
	public void previewAd(@Named("ad")Ad ad, @Named("otherProperties")List<AdPropertyValue> otherProperties ){
		ad.getAdPropertyValues().addAll(otherProperties);
		
		ad.setAdPropertyValues( ad.getAdPropertyValues().stream().filter( p-> p.getValue() != null ).collect(Collectors.toList()) );
		for( AdPropertyValue propValue : ad.getAdPropertyValues()){
			AdProperty prop = AdPropertyUtils.getInstance(adDao).getAdProperty( propValue.getAdProperty().getName() );
			propValue.setAdProperty(prop);
		}
		
		if( session.getUploadedImages() != null ){
			for( String image : session.getUploadedImages() ){
				AdPropertyValue prop = new AdPropertyValue();
				ad.getAdPropertyValues().add(prop);
				
				prop.setAdProperty( AdPropertyUtils.getInstance(adDao).getImageProperty() );
				prop.setValue( image );
			}
		}
		
		result.include("ad", ad);
		String to = "/WEB-INF/jsp/adDetails/getAdDetails.jsp";
		result.use( Results.page() ).forwardTo(to);
	}
	
	@Post("/anunciar/salvar-imagem")
	public void saveImage( @Named("image")UploadedFile image ){
		try{
			if( session.getUploadedImages() == null ){
				session.setUploadedImages( new ArrayList<String>() );
			}
			
			String miniPath = fileUtils.getWebDefaultFolder() + "/mini-" + fileUtils.saveFile(image);
			String path = miniPath.replaceAll("mini-", "");
			session.getUploadedImages().add(miniPath);
			session.getUploadedImages().add(path);
			
			result.include("path", miniPath);
		}catch(Exception ex){
			ex.printStackTrace();
			result.include("errorMessage", ex.getMessage());
		}
	}
	
}
