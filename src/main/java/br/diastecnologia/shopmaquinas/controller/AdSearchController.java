package br.diastecnologia.shopmaquinas.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.diastecnologia.shopmaquinas.bean.Brand;
import br.diastecnologia.shopmaquinas.bean.Model;
import br.diastecnologia.shopmaquinas.bean.Subtype;
import br.diastecnologia.shopmaquinas.bean.Type;
import br.diastecnologia.shopmaquinas.daos.AdDao;
import br.diastecnologia.shopmaquinas.session.SessionBean;

@Controller
public class AdSearchController {

	@Inject
	protected Result result;
	
	@Inject
	protected SessionBean session;
	
	@Inject
	private AdDao adDao;
	
	@Get
	@Path("/getGroups")
	public void getTypes( @Named("typeId")int typeId ){
		List<Type> groups = adDao.types().where( t-> t.getParentId() == typeId ).collect(Collectors.toList());
		result.use( Results.json() ).from( groups ).recursive().serialize();
	}
	
	@Get
	@Path("/getCategories")
	public void getSubtypes( @Named("groupId")int groupId ){
		List<Subtype> categories = adDao.subtypes().where( t-> t.getParentId() == groupId ).collect(Collectors.toList());
		result.use( Results.json() ).from( categories ).recursive().serialize();
	}
	
	@Get
	@Path("/getBrands")
	public void getBrands( @Named("categoryId")int categoryId ){
		List<Brand> brands = adDao.brands().where( t-> t.getParentId() == categoryId ).collect(Collectors.toList());
		result.use( Results.json() ).from( brands ).recursive().serialize();
	}
	
	@Get
	@Path("/getModels")
	public void getModels( @Named("brandId")int brandId ){
		List<Model> models = adDao.models().where( t-> t.getParentId() == brandId ).collect(Collectors.toList());
		result.use( Results.json() ).from( models ).recursive().serialize();
	}
	
}
