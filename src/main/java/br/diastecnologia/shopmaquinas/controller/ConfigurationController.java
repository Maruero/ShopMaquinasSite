package br.diastecnologia.shopmaquinas.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.environment.Property;
import br.diastecnologia.shopmaquinas.bean.Brand;
import br.diastecnologia.shopmaquinas.bean.Category;
import br.diastecnologia.shopmaquinas.bean.Model;
import br.diastecnologia.shopmaquinas.bean.Subtype;
import br.diastecnologia.shopmaquinas.bean.Type;
import br.diastecnologia.shopmaquinas.daos.AdDao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class ConfigurationController {

	@Inject
	private AdDao adDao;
	
	@Inject
	@Property("configuration.categoryies.path")
	private String categoriesPath;
	
	@Get("/configurar-categorias")
	@Transactional
	public void addCategories() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(categoriesPath));
		String content = reader.readLine();
		reader.close();
		
 		List<br.diastecnologia.options.beans.Category> cats = new Gson().fromJson(content, new TypeToken<List<br.diastecnologia.options.beans.Category>>(){
		}.getType());
		
		for( br.diastecnologia.options.beans.Category cat : cats){
			
			Category category = new Category();
			category.setName(cat.getName());
			
			adDao.getEM().persist(category);
			
			for( br.diastecnologia.options.beans.Type t : cat.getTypes() ){
				
				Type type = new Type();
				type.setParentId(category.getId());
				type.setName( t.getName() );
				
				adDao.getEM().persist(type);
				
				for( br.diastecnologia.options.beans.Subtype s : t.getSubtypes() ){
					
					Subtype subtype = new Subtype();
					subtype.setParentId(type.getId());
					subtype.setName( s.getName() );
					
					adDao.getEM().persist(subtype);
					
					for( br.diastecnologia.options.beans.Brand b : s.getBrands() ){
						
						Brand brand = new Brand();
						brand.setParentId(subtype.getId());
						brand.setName( b.getName() );
						
						adDao.getEM().persist(brand);
						
						for( br.diastecnologia.options.beans.Model m : b.getModels() ){
							
							Model model = new Model();
							model.setParentId(brand.getId());
							model.setName( m.getName() );
							
							adDao.getEM().persist(model);
						}
					}
				}
			}
		}
	}
	
}
