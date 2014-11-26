<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../../</tiles:putAttribute>
	
	<tiles:putAttribute name="pageName">Detalhes do produto</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details">
			<a href="#" class="btn-favourite"><span class="icon-star"></span></a>
			<div class="gallery-block">
			
				<div id="carousel-generic1" class="carousel slide" data-ride="carousel" data-interval="false">
					<div class="carousel-inner" role="listbox">
						<div class="item active">
							<img src="../../${ad.image}" alt="image description" width="400" height="341">
						</div>
					</div>
				
					<ol class="carousel-indicators photos-list">
						<c:forEach items="${ad.images}" var="image">
							<li>
								<a href="#"><img src="../../${image}" alt="image description" width="95" height="81" onclick="return showImage(this);"></a>
							</li>
						</c:forEach>
					</ol>
				</div>
			</div>
			<div class="description">
				<h3>${ad.description}</h3>
				<ul class="info">
				
					<c:forEach var="prop" items="${ad.adPropertyValues}">
						
						<c:if test="${prop.adProperty.visible}">
							<li>
								<strong class="title">${prop.adProperty.description} -</strong>
								<span class="value price">${prop.value}</span>
							</li>
						</c:if>
						
					</c:forEach>
					
				</ul>
				<div class="text-wrap">
					<strong class="title">Descrição:</strong>
					<p>${ad.longDescription}</p>
					<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal1">fazer proposta</button>
				</div>
			</div>
		</article>
	
	</tiles:putAttribute>
	
	<tiles:putAttribute name="afterFooter">
		<div class="modal modal1 fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<button type="button" class="close" data-dismiss="modal"><span class="icon-close"></span></button>
					<div class="row">
						<div class="col-md-6">
							<section class="advertiser-block">
								<h2>anunciante</h2>
								<div class="block-holder">
									<div class="details">
									
										<!-- 
										<div class="img-holder">
											<a href="#"><img src="images/img09.jpg" alt="image description" width="100" height="133"></a>
										</div>
										 -->
										 
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
									
									<!-- 
									<div class="other-product">
										<h3>Outros Produtos:</h3>
										<ul class="feature-list">
											<li>
												<div class="holder">
													<div class="img-holder">
														<img src="images/img01.jpg" alt="image description" width="108" height="77">
														<div class="hover-content">
															<a href="#" class="btn-more">detalhes</a>
														</div>									
													</div>
													<strong class="title"><a href="#">John deere 7330</a></strong>
													<span class="counter">11/11</span>
												</div>
											</li>
											<li>
												<div class="holder">
													<div class="img-holder">
														<img src="images/img01.jpg" alt="image description" width="108" height="77">
														<div class="hover-content">
															<a href="#" class="btn-more">detalhes</a>
														</div>									
													</div>
													<strong class="title"><a href="#">John deere 7330</a></strong>
													<span class="counter">11/11</span>
												</div>
											</li>
											<li>
												<div class="holder">
													<div class="img-holder">
														<img src="images/img01.jpg" alt="image description" width="108" height="77">
														<div class="hover-content">
															<a href="#" class="btn-more">detalhes</a>
														</div>									
													</div>
													<strong class="title"><a href="#">John deere 7330</a></strong>
													<span class="counter">11/11</span>
												</div>
											</li>
										</ul>
									</div>
									-->
								</holder>
							</section>
						</div>
						<div class="col-md-6">
							<section class="contact-form">
								<h2>fazer proposta</h2>
								<form role="form" id="proposal-group">
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
										<textarea class="form-control" id="proposal-text" rows="3" requiredLength="1" requiredMessage="O campo mensagem é obrigatória"></textarea>
									</div>
									<a type="submit" class="btn btn-danger" onclick="sendProposal();">enviar</a>
								</form>
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
