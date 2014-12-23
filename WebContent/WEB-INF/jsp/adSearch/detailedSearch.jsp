<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">./</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Busca detalhada</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="">
			<div class="description side-by-side contact-form">
				<form class="form" action="<tiles:getAsString name="pathPrefix"/>busca-avancada" role="form" onsubmit="return validateAdvancedSearch();">
					<fieldset>
						<div class="form-group">
							<label>Categorias</label>
							<input type="hidden" name="adPropertyValues[0].adProperty.adPropertyID" value="1" />
							<select id="type-home-select" name="adPropertyValues[0].value" class="form-control">
								<option value="">Tipo</option>
							</select>
						</div>
						<div class="form-group">
							<input type="hidden" name="adPropertyValues[1].adProperty.adPropertyID" value="2" />
							<select id="group-home-select" name="adPropertyValues[1].value" class="form-control">
								<option value="">Grupo</option>
							</select>
						</div>
						<div class="form-group">
							<input type="hidden" name="adPropertyValues[2].adProperty.adPropertyID" value="3" />
							<select id="category-home-select" name="adPropertyValues[2].value" class="form-control">
								<option value="">Categoria</option>
							</select>
						</div>
						<div class="form-group">
							<input type="hidden" name="adPropertyValues[3].adProperty.adPropertyID" value="4" />
							<select id="brand-home-select" name="adPropertyValues[3].value" class="form-control">
								<option value="">Marca</option>
							</select>
						</div>
						<div class="form-group">
							<input type="hidden" name="adPropertyValues[4].adProperty.adPropertyID" value="5" />
							<select id="model-home-select" name="adPropertyValues[4].value" class="form-control">
								<option value="">Modelo</option>
							</select>
						</div>
						
						<div class="form-group">
							<label>Cor</label>
							
							<input type="hidden" name="adPropertyValues[5].adProperty.name" value="COLOR"/>
							<input type="text" class="form-control" name="adPropertyValues[5].value" placeholder="Cor" />
						</div>
						<div class="form-group">
							<label for="name">Ano</label>
						</div>
						<div class="form-group">
							<input type="hidden" name="otherProperties[4].adProperty.name" value="YEAR_MIN"/>
							<input type="hidden" name="otherProperties[4].adProperty.adPropertyID" value="11"/>
							<input type="text" id="year-min" class="form-control two-in-a-row" name="otherProperties[4].value" alt="9999" placeholder="Ano mínimo"/>
							
							<input type="hidden" name="otherProperties[5].adProperty.name" value="YEAR_MAX"/>
							<input type="hidden" name="otherProperties[5].adProperty.adPropertyID" value="11"/>
							<input type="text" id="year-max" class="form-control two-in-a-row" name="otherProperties[5].value" alt="9999" placeholder="Ano máximo"/>
						</div>
						
						<div class="form-group">
							<label for="name">horas/kilometragem</label>
						</div>
						<div class="form-group">
							<input type="hidden" name="otherProperties[0].adProperty.name" value="HOURS_MIN" />
							<input type="hidden" name="otherProperties[0].adProperty.adPropertyID" value="15"/>
							<input type="text" id="hour-min" class="form-control two-in-a-row" name="otherProperties[0].value" alt="999999" placeholder="Horas/Km mínimo"/>
							
							<input type="hidden" name="otherProperties[1].adProperty.name" value="HOURS_MAX" />
							<input type="hidden" name="otherProperties[1].adProperty.adPropertyID" value="15"/>
							<input type="text" id="hour-max" class="form-control two-in-a-row" name="otherProperties[1].value" alt="999999" placeholder="Horas/Km máximo"/>
						</div>
						
						
						<div class="form-group">
							<label for="name">Valor</label>
						</div>
						<div class="form-group">
							<input type="hidden" name="otherProperties[2].adProperty.name" value="PRICE_MIN" />
							<input type="hidden" name="otherProperties[2].adProperty.adPropertyID" value="9"/>
							<input type="text" id="price-min" onblur="putSymbol(this);" class="form-control two-in-a-row" name="otherProperties[2].value" alt="decimal" placeholder="Valor mínimo"/>
							
							<input type="hidden" name="otherProperties[3].adProperty.name" value="PRICE_MAX" />
							<input type="hidden" name="otherProperties[3].adProperty.adPropertyID" value="9"/>
							<input type="text" id="price-max" onblur="putSymbol(this);" class="form-control two-in-a-row" name="otherProperties[3].value" alt="decimal" placeholder="Valor máximo"/>
						</div>
						
						<div class="form-group">
							<label for="name">Propriedades</label>
						</div>
						
						<div class="form-group" style="text-align:center;">
						
							<input type="hidden" name="adPropertyValues[8].adProperty.name" value="UNIQUE_OWNER"/>
							<input type="hidden" name="adPropertyValues[8].adProperty.adPropertyID" value="13"/>
							<input type="checkbox" name="adPropertyValues[8].value" value="true"/> Único dono
						</div>
						
						<div class="form-group" style="text-align:center;"> 
						
							<input type="hidden" name="adPropertyValues[9].adProperty.name" value="EXCHANGE"/>
							<input type="hidden" name="adPropertyValues[9].adProperty.adPropertyID" value="14"/>
							<input type="checkbox" name="adPropertyValues[9].value" value="true"/> Aceita troca
						</div>
						
						<div class="form-group">
							<button type="submit" class="btn btn-danger">Buscar</button>
						</div>
					</fieldset>
				</form>
			</div>
		</article>
		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="scripts">
		<script type="text/javascript">
			$("#sidebar").remove();
		</script>		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
