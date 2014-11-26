package br.diastecnologia.shopmaquinas.utils;

import java.util.List;

import br.diastecnologia.shopmaquinas.bean.AdProperty;
import br.diastecnologia.shopmaquinas.bean.AdPropertyValue;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.enums.AdProperties;


public class AdPropertyUtils {

	private AdDao dao;
	private AdPropertyValue highlightedPropery;
	private AdProperty imageProperty;
	private List<AdProperty> props;
	
	private static AdPropertyUtils instance;
	
	private AdPropertyUtils(AdDao dao){
		this.dao = dao;
	}
	
	public static AdPropertyUtils getInstance(AdDao dao){
		if( instance == null ){
			instance = new AdPropertyUtils(dao);
			instance.init();
		}
		return instance;
	}
	
	public void init()
	{
		props = dao.adProperties().toList();
		for( AdProperty prop : props){
			dao.getEM().detach(prop);
		}
		
		AdProperty prop = props.stream().filter( p-> p.getName().equals( AdProperties.HIGHLIGHTED.toString() )).findFirst().get();
		highlightedPropery = new AdPropertyValue();
		highlightedPropery.setAdProperty( prop );
		highlightedPropery.setValue( "true" );
		
		imageProperty = props.stream().filter( p-> p.getName().equals( AdProperties.IMAGE.toString() )).findFirst().get();
	}
	
	public AdPropertyValue getHighlightProperty(){
		return highlightedPropery;
	}
	
	public AdProperty getImageProperty(){
		return imageProperty;
	}
	
	public List<AdProperty> listProperties(){
		return props;
	}
	
	public AdProperty getAdProperty(String name){
		return props.stream().filter( p-> p.getName().equals( name )).findFirst().get();
	}
	
	
}