<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../../</tiles:putAttribute>
	
	<tiles:putAttribute name="pageName">Detalhes do produto</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details">
			<a href="#" onclick="return setFavorite(${ad.adID});" class="btn-favourite"><span class="icon-star"></span></a>
			<div class="gallery-block">
			
				<div id="carousel-generic1" class="carousel slide" data-ride="carousel" data-interval="false">
					<div class="carousel-inner" role="listbox">
						<div class="item active image-holder">
							<c:choose>
								<c:when test="${fn:length(ad.images) gt 0}">
									<c:forEach items="${ad.images}" var="image">
										<c:if test="${image.indexOf('mini') == -1 }">
											<img src="../../..${image}" alt="image description" width="400" height="341" data-name="${image}" style="display:none;">
										</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<img src="../../resources/images/sem-imagem-d.jpg" alt="image description" width="400" height="341" data-name="${image}" style="display:none;">
								</c:otherwise>						
							</c:choose>
							
						</div>
					</div>
				
					<ol class="carousel-indicators photos-list" style="width:400px;">
						<c:choose>
							<c:when test="${fn:length(ad.images) gt 0}">
								<c:forEach items="${ad.images}" var="image">
									<c:if test="${image.indexOf('mini') != -1 }">
										<li>
											<a href="#"><img id="mini-image-link" src="../../..${image}" alt="image description" width="95" height="81" onclick="return showImage(this);" data-name="${image}"></a>
										</li>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li>
									<a href="#"><img id="mini-image-link" src="../../resources/images/mini-sem-imagem-d.jpg" alt="image description" width="95" height="81" onclick="return showImage(this);" data-name="${image}"></a>
								</li>
							</c:otherwise>
						</c:choose>
					</ol>
				</div>
			</div>
			<div class="description">
				<h3>${ad.description}</h3>
				<ul class="info">
				
					<c:forEach var="prop" items="${ad.adPropertyValues}">
						
						<c:if test="${prop.adProperty.visible}">
							<li>
								<strong class="title" style="text-align:right;">${prop.adProperty.description}:</strong>
								<c:choose>
									<c:when test="${prop.value == 'true' }">
										<span class="value price">Sim</span>
									</c:when>
									<c:otherwise>
										<span class="value price">${prop.value}</span>
									</c:otherwise>
								</c:choose>
							</li>
						</c:if>
						
					</c:forEach>
					
				</ul>
				<div class="text-wrap">
					<strong class="title">Descrição:</strong>
					<p>${ad.longDescription}</p>
					<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal1">fazer proposta</button>
					<button type="button" style="float:right;" class="btn btn-danger" data-toggle="modal" data-target="#myModal1">anunciante</button>
				</div>
			</div>
		</article>
	
	</tiles:putAttribute>
	
	<tiles:putAttribute name="afterFooter">
		<div class="modal modal1 fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<button type="button" class="close" data-dismiss="modal"><span class="icon-close"></span></button>
					<div class="row" style="height:525px;">
						<div class="col-md-6">
							<section class="advertiser-block">
								<h2>anunciante</h2>
								<div class="block-holder">
									<div class="details">
									
										<c:if test="${ ad.person.personType == 'COMPANY' }">
											<c:choose>
												<c:when test="${ad.person.firstImage != null}">
													<div class="img-holder">
														<a href="javascript:;"><img src="../../..${ad.person.firstImage}" alt="image description" width="100" height="133"></a>
													</div>
												</c:when>
												<c:otherwise>
													<div class="img-holder">
														<a href="javascript:;"><img src="../../resources/images/sem-imagem-x1.jpg" alt="image description" width="100" height="133"></a>
													</div>
												</c:otherwise>
											</c:choose> 
										</c:if>
										
										 
										<div class="address-holder">
											<h3>${ad.person.firstname} ${ad.person.lastname}</h3>
											<dl>
												<dt>Tel - </dt>
												<dd>${ad.person.phone}</dd>
												<dt>Local - </dt>
												<dd>${ad.person.address.street}, ${ad.person.address.number} ${ad.person.address.city}/${ad.person.address.uf}</dd>
											</dl>
										</div>
									</div>
									
									 
									<div class="other-product">
										<h3>Outros Anúncios:</h3>
										<ul class="feature-list">
											<c:forEach items="${otherAds}" var="ad">
												<li>
													<div class="holder">
														<div class="img-holder">
															<img src="${ad.image}" alt="image description" width="108" height="77">
															<div class="hover-content">
																<a href="${ad.adID}" class="btn-more">saiba mais</a>
																<a href="#" onclick="setFavorite(${ad.adID})" class="btn-favroite icon-star"></a>
															</div>									
														</div>
														<strong class="title"><a href="#">${ad.description}</a></strong>
														<span class="counter">ano ${ad.year}</span>
													</div>
												</li>
											</c:forEach>
										</ul>
									</div>
									
								</div>
							</section>
						</div>
						<div class="col-md-6">
							<section class="contact-form">
								<h2>fazer proposta</h2>
								<form role="form" class="form" id="proposal-group" style="padding-right:15px;">
									<input type="hidden" id="proposal-adID" value="${ad.adID}">
									<div class="form-group">
										<label for="name">Nome*</label>
										<input type="text" class="form-control" id="proposal-name" requiredLength="1" requiredMessage="O campo nome é obrigatório">
									</div>
									<div class="form-group">
										<label for="Email">e-mail*</label>
										<input type="email" class="form-control" id="proposal-email" requiredLength="1" requiredMessage="O campo e-mail é obrigatório">
									</div>
									<div class="form-group">
										<label for="phone">Telefone*</label>
										<input type="tel" class="form-control" id="proposal-phone" requiredLength="1" requiredMessage="O campo telefone é obrigatório">
									</div>
									<div class="form-group">
										<label for="phone">mensagem*</label>
										<textarea style="min-height:170px !important;" class="form-control" maxlength="1900" id="proposal-text" rows="3" requiredLength="1" requiredMessage="O campo mensagem é obrigatória"></textarea>
									</div>
									<a type="submit" id="send-proposal-button" class="btn btn-danger" onclick="sendProposal();">enviar</a>
								</form>
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
