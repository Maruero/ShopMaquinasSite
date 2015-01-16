package br.diastecnologia.shopmaquinas.daos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;

import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.AdProperty;
import br.diastecnologia.shopmaquinas.bean.AdPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Billing;
import br.diastecnologia.shopmaquinas.bean.Brand;
import br.diastecnologia.shopmaquinas.bean.Category;
import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.ContractDefinition;
import br.diastecnologia.shopmaquinas.bean.ContractDefinitionProperty;
import br.diastecnologia.shopmaquinas.bean.ContractDefinitionPropertyValue;
import br.diastecnologia.shopmaquinas.bean.Document;
import br.diastecnologia.shopmaquinas.bean.Image;
import br.diastecnologia.shopmaquinas.bean.Message;
import br.diastecnologia.shopmaquinas.bean.Model;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.bean.Subtype;
import br.diastecnologia.shopmaquinas.bean.Type;
import br.diastecnologia.shopmaquinas.bean.User;
import br.diastecnologia.shopmaquinas.bean.UserToken;
import br.diastecnologia.shopmaquinas.utils.AdPropertyUtils;

@RequestScoped
public class AdDao {
		
	protected int adPageSize = 100;
	
	@Inject
	private EntityManagerFactory entityManagerFactory;
	
	@Inject
	private EntityManager em;
	
	protected boolean activeTransaction;
	private JinqJPAStreamProvider streams;
	
	public List<Ad> listAds( List<AdPropertyValue> props , String text, int pageNumber ){
		
		long skip = (pageNumber -1) * adPageSize;
		
		List<Ad> ads = new ArrayList<Ad>();
		
		JinqStream<Ad> stream = ads().where( a-> 
			a.getEndDate() == null || 
			Calendar.getInstance().getTime().before( a.getEndDate())
		);
		
		if( props != null && props.size() > 0 ){
			for( AdPropertyValue prop : props ){
				stream = stream.where( a -> a.getAdPropertyValues().stream().filter( 
						p-> p.getAdPropertyID() == prop.getAdPropertyID() && 
					    p.getValue().equals( prop.getValue() )
					).count() > 0
				);
			}
		}
		
		if( text != null && text.length() > 0 ){
			String[] textSearch = text.toUpperCase().split(" ");
			
			for( String search : textSearch ){
				stream = stream.where( a -> a.getAdPropertyValues().stream().filter( 
						p-> p.getValue().toUpperCase().contains( search )
					).count() > 0
				);
			}
		}
		
		stream = stream.skip(skip).limit(adPageSize);
		ads = stream.toList();
		
		return ads;
	}
	
	public List<Ad> listAds( List<AdPropertyValue> props , List<AdPropertyValue> otherProps, int pageNumber ){
		
		long skip = (pageNumber -1) * adPageSize;
		
		List<Ad> ads = new ArrayList<Ad>();
		
		JinqStream<Ad> stream = ads().where( a-> 
			a.getEndDate() == null || 
			Calendar.getInstance().getTime().before( a.getEndDate())
		);
		
		if( props != null && props.size() > 0 ){
			for( AdPropertyValue prop : props ){
				stream = stream.where( a -> a.getAdPropertyValues().stream().filter( 
						p-> p.getAdPropertyID() == prop.getAdPropertyID() && 
					    p.getValue().equals( prop.getValue() )
					).count() > 0
				);
			}
		}
		
		if( otherProps != null && otherProps.size() > 0 ){
			for( AdPropertyValue prop : otherProps ){
				
				boolean min = true;
				if( prop.getAdProperty().getName().indexOf("MAX") != -1){
					min = false;
				}
				
				if( min ){
					stream = stream.where( a -> a.getAdPropertyValues().stream().filter( 
							p-> p.getAdPropertyID() == prop.getAdPropertyID() && 
						    p.getDoubleValue() >= prop.getDoubleValue()
						).count() > 0
					);
				}else{
					stream = stream.where( a -> a.getAdPropertyValues().stream().filter( 
							p-> p.getAdPropertyID() == prop.getAdPropertyID() && 
						    p.getDoubleValue() <= prop.getDoubleValue()
						).count() > 0
					);
				}
			}
		}
		
		stream = stream.skip(skip).limit(adPageSize);
		ads = stream.toList();
		
		return ads;
	}
	
	public Ad getAd( int adID ){
		Optional<Ad> ad = ads().filter( a->a.getAdID() == adID ).findFirst();
		if( ad.isPresent() ){
			return ad.get();
		}else{
			return null;
		}
	}
	
	public Ad getDetachedAd(int adID){
		Ad ad = getAd(adID);
		if( ad != null ){
			em.detach(ad);
			
			ad.setContract(null);
			ad.setPerson(null);
		}
		return ad;
	}
	
	public Ad saveAd( Ad ad, List<String> images ) throws Exception{
		if( ad.getAdPropertyValues() == null ){
			ad.setAdPropertyValues(new ArrayList<AdPropertyValue>());
		}
		
		ad.setStartDate( Calendar.getInstance().getTime() );
		
		if( images != null ){
			for( String image : images ){
				AdPropertyValue prop = new AdPropertyValue();
				ad.getAdPropertyValues().add(prop);
				
				prop.setAdProperty( AdPropertyUtils.getInstance(this).getImageProperty() );
				prop.setValue( image );
			}
		}
	
		ad.setAdPropertyValues( ad.getAdPropertyValues().stream().filter( p-> p.getValue() != null ).collect(Collectors.toList()) );
		for( AdPropertyValue propValue : ad.getAdPropertyValues()){
			AdProperty prop = AdPropertyUtils.getInstance(this).getAdProperty( propValue.getAdProperty().getName() );
			propValue.setAdProperty(prop);
		}
		
		if( ad.getContract().getContractDefinition() == null ){
			Contract contract = this.contracts().where( c-> c.getContractID() == ad.getContract().getContractID() ).findFirst().get();
			ad.setContract(contract);
			ad.setEndDate(contract.getEndDate());
		}
		
		if( ad.getAdID() > 0 ){
			for( AdPropertyValue value : ad.getAdPropertyValues() ){
				if( value.getAdID() < 1 ){
					value.setAdID(ad.getAdID());
					em.persist(value);
				}else{
					em.merge(value);
				}
			}
		}else{
			em.persist(ad);
			for( AdPropertyValue value : ad.getAdPropertyValues() ){
				value.setAdID(ad.getAdID());
				em.persist(value);
			}
		}
		
		return ad;
	}

	public User saveNewUser(User user){
		if( user.getUserID() < 1 ){
			em.persist( user );
			if( user.getPerson().getDocuments() != null ){
				for( Document doc : user.getPerson().getDocuments() ){
					doc.setPersonID( user.getPerson().getPersonID() );
					em.persist( doc);
				}
			}
		}else{
			em.merge(user);
		}
		return user;
	}
	
	
	public void flush(){
		em.flush();
	}
	
	private void init(){
		if( streams == null){
			streams = new JinqJPAStreamProvider(entityManagerFactory);
		}
	}
	
	public EntityManager getEM(){
		return em;
	}
	
	public JinqStream<Ad> ads() {
		init();
		return streams.streamAll(em, Ad.class); 
	}
	
	public JinqStream<AdProperty> adProperties() {
		init();
		return streams.streamAll(em, AdProperty.class); 
	}
	
	public JinqStream<AdPropertyValue> adPropertyValues() {
		init();
		return streams.streamAll(em, AdPropertyValue.class); 
	}
	
	public JinqStream<Contract> contracts() {
		init();
		return streams.streamAll(em, Contract.class); 
	}
	
	public JinqStream<ContractDefinition> contractDefinitions() {
		init();
		return streams.streamAll(em, ContractDefinition.class); 
	}
	
	public JinqStream<ContractDefinitionProperty> contractDefinitionProperties() {
		init();
		return streams.streamAll(em, ContractDefinitionProperty.class); 
	}
	
	public JinqStream<ContractDefinitionPropertyValue> contractDefinitionPropertyValues() {
		init();
		return streams.streamAll(em, ContractDefinitionPropertyValue.class); 
	}
	
	public JinqStream<Document> documents() {
		init();
		return streams.streamAll(em, Document.class); 
	}
	
	public JinqStream<Person> persons() {
		init();
		return streams.streamAll(em, Person.class); 
	}
	
	public JinqStream<User> users() {
		init();
		return streams.streamAll(em, User.class); 
	}
	
	public JinqStream<Message> messages(){
		init();
		return streams.streamAll(em, Message.class);
	}
	
	public JinqStream<Image> images(){
		init();
		return streams.streamAll(em, Image.class);
	}
	
	public JinqStream<Category> categories(){
		init();
		return streams.streamAll(em, Category.class);
	}
	
	public JinqStream<Type> types(){
		init();
		return streams.streamAll(em, Type.class);
	}
	
	public JinqStream<Subtype> subtypes(){
		init();
		return streams.streamAll(em, Subtype.class);
	}
	
	public JinqStream<Brand> brands(){
		init();
		return streams.streamAll(em, Brand.class);
	}
	
	public JinqStream<Model> models(){
		init();
		return streams.streamAll(em, Model.class);
	}
	
	public JinqStream<Billing> billings(){
		init();
		return streams.streamAll(em, Billing.class);
	}
	
	public JinqStream<UserToken> tokens(){
		init();
		return streams.streamAll(em, UserToken.class);
	}
}
