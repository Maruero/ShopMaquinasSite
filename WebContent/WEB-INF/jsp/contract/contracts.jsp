<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">./</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Venda</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details">
			<div class="description">
				<h3>Seus contratos</h3>
				
				<c:forEach items="${contracts}" var="contract">
					<ul class="info">
						<li>
							<strong class="title">${contract.contractDefinition.name}</strong>
							<span class="value price">Ativo</span>
						</li>
						<li>
							<strong class="title">Data da contratação</strong>
							<span class="value date-label">${contract.startDate}</span>
						</li>
						<li>
							<strong class="title">Anúncios cadastrados</strong>
							<span class="value">
							
								<c:forEach items="${contract.ads}" var="ad">
									<p>${ad.description}<a href="anuncios/detalhes-do-anuncio/${ad.adID}"><span class="value price">Exibir</span></a><br></p>	
								</c:forEach>
							
							</span>
						</li>
						
					</ul>
					
					<div class="text-wrap">
						<button type="button" class="btn btn-danger" href="anunciar/novo-anuncio" onClick="return restricted(this);">Novo anúncio</button>
					</div>
				</c:forEach>
				
			</div>
		</article>
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
