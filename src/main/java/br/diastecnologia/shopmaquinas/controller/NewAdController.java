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
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.AdProperty;
import br.diastecnologia.shopmaquinas.bean.AdPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.enums.AdProperties;
import br.diastecnologia.shopmaquinas.session.SessionBean;
import br.diastecnologia.shopmaquinas.utils.AdPropertyUtils;
import br.diastecnologia.shopmaquinas.utils.ContractUtils;
import br.diastecnologia.shopmaquinas.utils.FileUtils;

@Controller
public class NewAdController{
	
	@Inject
	protected Result result;
	
	@Inject
	protected Validator validator;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private FileUtils fileUtils;
	
	@Inject
	private AdDao adDao;
	
	@Get("/anunciar/novo-anuncio")
	public void newAd(){
	
		Person person = adDao.persons().where( p-> p.getPersonID() == session.getUser().getPerson().getPersonID() ).findFirst().get();
		
		Contract contract = ContractUtils.getValidContract(person.getContracts());
		if( contract == null ){
			result.include("ErrorMessage", "Somente anunciantes ativos podem anunciar. Para anunciar contrate um de nossos planos.");
			result.redirectTo( ContractController.class ).contracts();
		}
		session.setUploadedImages( new ArrayList<String>() );
		result.include("Contract", contract);
		result.include("ad", new Ad());
	}
	
	@Get("/anunciar/editar-anuncio")
	public void editAd( @Named("adID") int adID ){
		
		Ad ad = adDao.getAd(adID);
		if( session.getUser() == null || session.getUser().getPerson() == null || ad.getPerson().getPersonID() != session.getUser().getPerson().getPersonID() ){
			result.include("ErrorMessage", "Sess�o expirada, comece novamente.");
			result.redirectTo(HomeController.class).index();
			return;
		}
		
		session.setUploadedImages( 
			ad.getAdPropertyValues().stream()
			.filter( p-> p.getAdProperty().getName().equals( AdProperties.IMAGE.toString() ) )
			.map( i-> i.getValue() )
			.collect( Collectors.toList() ) 
		);
		
		result.include("ad", ad);
		result.include("update", true);
		
		result.forwardTo("/WEB-INF/jsp/newAd/newAd.jsp");
	}
	
	@Post("/anunciar/novo-anuncio")
	@Transactional(rollbackOn=Exception.class)
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
	
	@Post("/anunciar/salvar-anuncio")
	@Transactional(rollbackOn=Exception.class)
	public void saveExistingAd(@Named("ad")Ad ad, @Named("otherProperties")List<AdPropertyValue> otherProperties){
		try{
			Ad exisingAd = adDao.getAd(ad.getAdID());
			adDao.getEM().detach(exisingAd);
			
			ad.getAdPropertyValues().addAll(otherProperties);
			
			for( AdPropertyValue value : exisingAd.getAdPropertyValues() ){
				adDao.getEM().remove(value);
			}
			adDao.getEM().flush();
			
			exisingAd.getAdPropertyValues().clear();
			exisingAd.getAdPropertyValues().addAll(ad.getAdPropertyValues());
			
			exisingAd = adDao.saveAd(exisingAd, session.getUploadedImages());
			result.redirectTo(AdDetailsController.class).getAdDetails( exisingAd.getAdID() );
		}catch(Exception ex){
			ex.printStackTrace();
			result.include("ErrorMessage", ex.getMessage());
			result.redirectTo( NewAdController.class ).editAd(ad.getAdID());
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
			
			long limit = 1 * 1024 * 1024; //1 MB
			if( image == null || image.getSize() >  limit ){
				result.include("limit", true);
				
				if( validator.hasErrors() ){
					validator.onErrorForwardTo(NewAdController.class).getImageFrame("limit");
					
				}else{
					result.redirectTo(NewAdController.class).getImageFrame("limit");
				}
				return;
			}else{
				result.include("limit", false);
			}
			
			String miniPath = fileUtils.getWebDefaultFolder() + "/mini-" + fileUtils.saveFile(image);
			String path = miniPath.replaceAll("mini-", "");
			session.getUploadedImages().add(miniPath);
			session.getUploadedImages().add(path);
			
			result.include("path", miniPath);
			result.redirectTo(NewAdController.class).getImageFrame(miniPath);
		}catch(Exception ex){
			ex.printStackTrace();
			result.include("errorMessage", ex.getMessage());
		}
	}
	
	@Get("/imagem-frame")
	public void getImageFrame( @Named("imageName") String imageName ){
		result.include("path", imageName);
	}
	
	@Post("/remover-imagem")
	public void deleteImage( @Named("imageName") String imageName )
	{
		if( session.getUploadedImages() != null && session.getUploadedImages().contains( imageName )){
			session.getUploadedImages().remove(imageName);
		}
		String path = imageName.replaceAll("mini-", "");
		if( session.getUploadedImages() != null && session.getUploadedImages().contains( path )){
			session.getUploadedImages().remove(path);
		}
		int index = imageName.lastIndexOf("/") +1;
		path = imageName.substring( 0 , index) + "mini-" + imageName.substring(index);
		if( session.getUploadedImages() != null && session.getUploadedImages().contains( path )){
			session.getUploadedImages().remove(path);
		}
	}
	
}
