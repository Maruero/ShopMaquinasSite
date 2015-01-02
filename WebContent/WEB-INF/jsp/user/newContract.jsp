<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Cadastramento</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="new-contract">
									
			<h3>Para que você possa cadastrar anúncios no site é necessário que contrate um de nossos planos.<h3>
			<h3 class="not-center">Escolha a opção desejada:</h3>
			
		</article>
		
		<article class="new-contract product-details">
			<div class="row">
			
				<c:forEach items="${definitions}" var="def">
					<div class="col-xs-6">
						<h3 class="not-center">${def.description}</h3>
						<ul class="info">
							<c:forEach items="${def.contractDefinitionPropertyValues}" var="prop">
								<li>
									<strong class="title">${prop.contractDefinitionProperty.description}</strong>
									<span class="value price">${prop.value}</span>
								</li>	
							</c:forEach>
						</ul>
						<form action="escolher-contrato" method="GET">
							<input type="hidden" name="contractDefinitionID" value="${def.contractDefinitionID}" />
							<div class="text-wrap">
								<input type="submit" class="btn btn-danger" style="margin-left: 100px;" value="Contratar"/>
							</div>
						</form>
					</div>
				</c:forEach>

			</div>
		</article>
	
	</tiles:putAttribute>
	
</tiles:insertDefinition>