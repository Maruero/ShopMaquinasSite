<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">

	<tiles:putAttribute name="pathPrefix">./</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Resultado da busca</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
		<ul class="feature-list">
			<c:forEach items="${ads}" var="ad">
				<li>
					<div class="holder">
						<div class="img-holder">
							<img src="${ad.image}" alt="image description" width="108" height="77">
							<div class="hover-content">
								<a href="anuncios/detalhes-do-anuncio/${ad.adID}" class="btn-more">saiba mais</a>
								<a href="#" class="btn-favroite icon-star"></a>
							</div>									
						</div>
						<strong class="title"><a href="#">${ad.description}</a></strong>
						<span class="counter">ano ${ad.year}</span>
					</div>
				</li>
			</c:forEach>
		</ul>
	</tiles:putAttribute>
	
	<tiles:putAttribute name="secondContent">
		&nbsp;
	</tiles:putAttribute>
	
	<!-- POPUPs e demais recursos para colocar escondido. -->
	<tiles:putAttribute name="afterFooter">
		&nbsp;
	</tiles:putAttribute>
	
</tiles:insertDefinition>