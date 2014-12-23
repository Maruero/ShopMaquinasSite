<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">

	<tiles:putAttribute name="pageName">Máquinas em Destaque</tiles:putAttribute>
	
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
	
		<section class="utility-block">
			<header class="heading-wrap text-center">
				<div class="bg-stretch">
					<img src="resources/images/bg-heading02.jpg" alt="image description" width="1170" height="77">
				</div>
				<h1>utilidades</h1>
			</header>
			<div class="four-block">
				<div class="row">
					<div class="col-xs-3">
						<section class="gallery-holder">
							<h2><a href="#">Previsão de preciptação</a></h2>
							<div class="holder">
								<div id="carousel-generic1" class="carousel slide" data-ride="carousel" data-interval="false">
									<div class="carousel-inner" role="listbox">
										<div class="item active">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.24hr.png" alt="image description" width="800" height="620">
											</a>
										</div>
										<div class="item">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.48hr.png" alt="image description" width="800" height="620">
											</a>
										</div>
										<div class="item">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.72hr.png" alt="image description" width="800" height="620">
											</a>
										</div>
										<div class="item">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.96hr.png" alt="image description" width="800" height="620">	
											</a>
										</div>
										<div class="item">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.120hr.png" alt="image description" width="800" height="620">
											</a>
										</div>
										<div class="item">
											<a href="#" data-toggle="modal" data-target="#myModal2">
												<img src="http://wxmaps.org/pix/sa3.144hr.png" alt="image description" width="800" height="620">
											</a>
										</div>
									</div>
									<ol class="carousel-indicators">
										<li data-target="#carousel-generic1" data-slide-to="0" class="active">
											<img src="http://wxmaps.org/pix/sa3.24hr.png" alt="image description" width="113" height="87">
										</li>
										<li data-target="#carousel-generic1" data-slide-to="1" class="active">
											<img src="http://wxmaps.org/pix/sa3.48hr.png" alt="image description" width="113" height="87">
										</li>
										<li data-target="#carousel-generic1" data-slide-to="2" class="active">
											<img src="http://wxmaps.org/pix/sa3.72hr.png" alt="image description" width="113" height="87">
										</li>
										<li data-target="#carousel-generic1" data-slide-to="3" class="active">
											<img src="http://wxmaps.org/pix/sa3.96hr.png" alt="image description" width="113" height="87">
										</li>
										<li data-target="#carousel-generic1" data-slide-to="4" class="active">
											<img src="http://wxmaps.org/pix/sa3.120hr.png" alt="image description" width="113" height="87">
										</li>
										<li data-target="#carousel-generic1" data-slide-to="5" class="active">
											<img src="http://wxmaps.org/pix/sa3.144hr.png" alt="image description" width="113" height="87">
										</li>
										
									</ol>
								</div>
							</div>
						</section>
					</div>
					<div class="col-xs-3">
						<section class="stock-block">
							<h2><a href="#">Bolsa de valores</a></h2>
							<div class="holder">
								<iframe id="frmPreview" frameborder="0" scrolling="no" src="http://www.agrolink.com.br/clientes/selos/selo.aspx?servico=cotacoes&uf=9830,9833,9834&p=9,11,1090,17&l=-1&esp=-1&cor=azul&w=266&h=178" width="266" height="178" style="border:none;"></iframe>
								
							</div>
						</section>
						<section class="stock-block">
							<h2>Notícias</h2>
							<div class="holder">
								<iframe id="frmPreview" frameborder="0" scrolling="no" src="http://www.agrolink.com.br/clientes/selos/selo.aspx?servico=noticias&uf=9830,9833,9834&p=9,11,1090,17&l=-1&esp=1379,3218,4394,3508,3175&cor=verde&w=300&h=200" width="300" height="200" style="border:none;"></iframe>
							</div>
						</section>
					</div>
					<div class="col-xs-3">
						<section class="notice-holder">
							<h2>Bolsa de valores</h2>
							<div class="holder">
								<iframe id="frmPreview" frameborder="0" scrolling="no" src="http://www.agrolink.com.br/clientes/selos/selo.aspx?servico=financas&uf=9830,9833,9834&p=9,11,1090,17&l=-1&esp=-1&cor=azul&w=200&h=115" width="200" height="115" style="border:none;"></iframe>
							</div>
						</section>
					</div>
					<div class="col-xs-3">
						<div class="ad-holder">
							<img src="resources/images/img06.png" alt="image description" width="257" height="306">
							<a class="btn-ad" href="contratos" onClick="return restricted(this);">clique aqui</a>
						</div>
					</div>
				</div>
			</div>
		</section>
	
	</tiles:putAttribute>
	
	<!-- POPUPs e demais recursos para colocar escondido. -->
	<tiles:putAttribute name="afterFooter">
	
		<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModal2" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<button type="button" class="close" data-dismiss="modal"><span class="icon-close"></span></button>
					<div id="carousel-generic2" class="carousel slide" data-ride="carousel" data-interval="false">
						<div class="carousel-inner" role="listbox">
							<div class="item active">
								<img src="http://wxmaps.org/pix/sa3.24hr.png" alt="image description" width="800" height="600">
							</div>
							<div class="item">
								<img src="http://wxmaps.org/pix/sa3.48hr.png" alt="image description" width="800" height="600">
							</div>
							<div class="item">
								<img src="http://wxmaps.org/pix/sa3.72hr.png" alt="image description" width="800" height="600">
							</div>
							<div class="item">
								<img src="http://wxmaps.org/pix/sa3.96hr.png" alt="image description" width="800" height="600">
							</div>
							<div class="item">
								<img src="http://wxmaps.org/pix/sa3.120hr.png" alt="image description" width="800" height="600">
							</div>
							<div class="item">
								<img src="http://wxmaps.org/pix/sa3.144hr.png" alt="image description" width="800" height="600">
							</div>
						</div>
						<ol class="carousel-indicators">
							<li data-target="#carousel-generic2" data-slide-to="0" class="active">
								<img src="http://wxmaps.org/pix/sa3.24hr.png" alt="image description" width="113" height="87">
							</li>
							<li data-target="#carousel-generic2" data-slide-to="1">
								<img src="http://wxmaps.org/pix/sa3.48hr.png" alt="image description" width="113" height="87">
							</li>
							<li data-target="#carousel-generic2" data-slide-to="2">
								<img src="http://wxmaps.org/pix/sa3.72hr.png" alt="image description" width="113" height="87">
							</li>
							<li data-target="#carousel-generic2" data-slide-to="3">
								<img src="http://wxmaps.org/pix/sa3.96hr.png" alt="image description" width="113" height="87">
							</li>
							<li data-target="#carousel-generic2" data-slide-to="4">
								<img src="http://wxmaps.org/pix/sa3.120hr.png" alt="image description" width="113" height="87">
							</li>
							<li data-target="#carousel-generic2" data-slide-to="5">
								<img src="http://wxmaps.org/pix/sa3.144hr.png" alt="image description" width="113" height="87">
							</li>
						</ol>
						<a class="left carousel-control" href="#carousel-generic2" role="button" data-slide="prev">Previous</a>
						<a class="right carousel-control" href="#carousel-generic2" role="button" data-slide="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	
	</tiles:putAttribute>
	
</tiles:insertDefinition>