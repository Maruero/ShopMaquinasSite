<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="pt">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Shop Maquinas</title>
		
		<link type="text/css" rel="stylesheet" href="<tiles:getAsString name="pathPrefix"/>resources/css/bootstrap.min.css">
		<link type="text/css" rel="stylesheet" href="<tiles:getAsString name="pathPrefix"/>resources/css/all_v1.css">
		
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
			<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		<tiles:insertAttribute name="head" />
	</head>
	<body>
	
		<span style="display:none;" id="pathPrefix"><tiles:getAsString name="pathPrefix"/></span>
		<span style="display:none;" id="errorMessage">${ErrorMessage}</span>
	
		<div id="wrapper">
			<div class="container">
				<div class="row">
					<header id="header">
						<div class="row">
							<div class="bg-stretch">
								<img src="<tiles:getAsString name="pathPrefix"/>resources/images/header-bg.jpg" alt="image description" width="1170" height="212">
							</div>
							<div class="col-xs-9">
								<div class="header-left">
									<div class="header-top">
										<div class="logo">
											<a href="<tiles:getAsString name="pathPrefix"/>"><img src="<tiles:getAsString name="pathPrefix"/>resources/images/logo.png" alt="Shop Maquinas" width="178" height="140"></a>
										</div>
										<span class="slogan">"Agora ficou fácil comprar e vender máquinas e implementos agrícolas"</span>
									</div>
									<nav id="nav" class="navbar navbar-default" role="navigation">
										<div class="container-fluid">
											<div class="collapse navbar-collapse">
												<ul class="nav navbar-nav">
													<li><a href="<tiles:getAsString name="pathPrefix"/>">home</a></li>
													<li><a href="#">compre</a></li>
													<li><a href="<tiles:getAsString name="pathPrefix"/>contratos" onClick="return restricted(this);">venda</a></li>
													<li><a href="<tiles:getAsString name="pathPrefix"/>area-do-cliente" onClick="return restricted(this);">Área do cliente</a></li>
												</ul>
											</div>
										</div>
									</nav>
								</div>
							</div>
							<div class="col-xs-3">
								<div class="header-right">
									<span id="user-logged" style="display:none">${session.user.username}</span>
									<span id="page-restricted" style="display:none">${restricted}</span>
									<a href="#" class="btn btn-default"><span class="icon-star"></span>Favoritos</a>
									<a href="#" class="btn btn-default" onclick="return openLogin('<tiles:getAsString name="pathPrefix"/>');"><span class="icon-user"></span>
									<span id="login-label">
										<c:choose>
											<c:when test="${session.user.username != null}">
												Sair
											</c:when>
											<c:otherwise>
												Login
											</c:otherwise>
										</c:choose>
									</span></a>
								</div>
							</div>
						</div>
					</header>
					<main id="main">
						
						<section class="featured-block">
							<header class="heading-wrap text-center">
								<div class="bg-stretch">
									<img src="<tiles:getAsString name="pathPrefix"/>resources/images/bg-heading01.jpg" alt="image description" width="1170" height="80">
								</div>
								<h1><tiles:insertAttribute name="pageName" /></h1>
							</header>
							<div class="row">
								<div class="col-xs-2">
									<aside id="sidebar">
										<section class="widget filter-form">
											<h2>Busca</h2>
											<form action="<tiles:getAsString name="pathPrefix"/>buscar-anuncions" role="form">
												<fieldset>
													<div class="form-group">
														<input class="form-control" type="text" name="description" placeholder="Digite..">
													</div>
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
														<button type="submit" class="btn btn-danger">Buscar</button>
													</div>
												</fieldset>
											</form>
											<a href="<tiles:getAsString name="pathPrefix"/>busca-detalhada" class="link-refine"><span>+</span> Busca Detalhada</a>
										</section>
									</aside>
								</div>
								<div class="col-xs-10">
								
									<tiles:insertAttribute name="content" />
								
								</div>
							</div>
						</section>
						
						<tiles:insertAttribute name="secondContent"/>
						
					</main>
					<footer id="footer">
						<div class="row">
							<div class="bg-stretch">
								<img src="<tiles:getAsString name="pathPrefix"/>resources/images/bg-footer.jpg" alt="image description" width="1170" height="229">
							</div>
							<div class="col-xs-8">
								<div class="footer-left">
									<div class="box">
										<h4>Compre</h4>
										<ul>
											<li>
												<a href="#">Busca detalhada</a>
											</li>
											<li>
												<a href="#">Busca por categoria</a>
											</li>
											<li>
												<a href="#">Catalogo de produtos</a>
											</li>
											<li>
												<a href="#">Destaques</a>
											</li>
											<li>
												<a href="#">Banco de pedidos</a>
											</li>
											<li>
												<a href="#">Área do cliente</a>
											</li>
										</ul>
									</div>
									<div class="box">
										<h4>Venda</h4>
										<ul>
											<li>
												<a href="#">Anunciar</a>
											</li>
											<li>
												<a href="#">Adesão de empresas</a>
											</li>
										</ul>
									</div>
									<div class="box">
										<h4>acessórios</h4>
										<ul>
											<li>
												<a href="#">Destaques</a>
											</li>
											<li>
												<a href="#">Anunciar</a>
											</li>
											<li>
												<a href="#">Adesão de empresas</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="col-xs-4">
								<div class="footer-right">
									<div class="logo">
										<a href="#"><img src="<tiles:getAsString name="pathPrefix"/>resources/images/logo.png" alt="Shop Maquinas" width="178" height="140"></a>
									</div>
									<ul class="social-networks">
										<li><a href="javascript:;"><span class="icon-twitter"></span></a></li>
										<li><a href="javascript:;"><span class="icon-facebook"></span></a></li>
										<li><a href="javascript:;"><span class="icon-feed"></span></a></li>
									</ul>
								</div>
							</div>
						</div>
					</footer>
					
					<tiles:insertAttribute name="afterFooter" />
					
					<a href="#" id="popup-opener" data-toggle="modal" data-target="#myModal10" style="display:none;"></a>
					<div class="modal modal1 fade" id="myModal10" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true" style="z-index:10000;">
						<div class="modal-dialog modal-dialog-popup">
							<div class="modal-content">
								<div class="row">
									<div class="col-md-12">
										<section class="contact-form">
											<h2 id="h2-popup">Aguarde</h2>
											<form class="form" role="form">
												<div class="form-group" id="div-label-popup">
													<label for="name" id="label-popup">Carregando</label>
												</div>
												<div class="form-group" id="div-content-popup">
												</div>
												<a type="submit" class="btn btn-danger" data-toggle="modal" data-target="#myModal10">fechar</a>
											</form>
										</section>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<a href="#" id="popup-login-opener" data-toggle="modal" data-target="#myModal3" style="display:none;" ></a>
					<div class="modal modal1 fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel3" aria-hidden="true" style="z-index:999;">
						<div class="modal-dialog modal-dialog-popup">
							<div class="modal-content">
								<div class="row">
									<div class="col-md-12">
										<section class="contact-form">
											<h2 id="h2-popup">Login</h2>
											<form class="form" role="form" id="login-group">
												<div class="form-group">
													<label for="name" id="label-popup">CPF/CNPJ</label>
													<input type="text" class="form-control" id="logon-username" name="username" requiredLength="1" alt="99999999999999" requiredMessage="O campo CPF/CNPJ é obrigatório!">
												</div>
												<div class="form-group">
													<label for="name" id="label-popup">Senha</label>
													<input type="password" class="form-control" data-id="login-password" id="logon-password" name="password" requiredLength="1" requiredMessage="O campo senha é obrigatório!">
												</div>
												<a type="submit" class="btn btn-danger left" onclick="return register('<tiles:getAsString name="pathPrefix"/>');">cadastre-se</a>
												<a type="submit" class="btn btn-danger" onclick="return login('<tiles:getAsString name="pathPrefix"/>');" id="login-button">entrar</a>
											</form>
										</section>
									</div>
								</div>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<tiles:getAsString name="pathPrefix"/>resources/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="<tiles:getAsString name="pathPrefix"/>resources/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<tiles:getAsString name="pathPrefix"/>resources/js/shopmaquinas_v1.js"></script>
		<script type="text/javascript" src="<tiles:getAsString name="pathPrefix"/>resources/js/meiomask.js"></script>
		
		<tiles:insertAttribute name="scripts" />
	</body>
</html>