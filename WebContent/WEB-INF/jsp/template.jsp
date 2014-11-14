<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="ISO-8859-1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Shop Maquinas</title>
		<link type="text/css" rel="stylesheet" href="resources/css/bootstrap.css">
		<link type="text/css" rel="stylesheet" href="resources/css/all.css">
		
		<tiles:insertAttribute name="head" />
	</head>
	<body>
		<div id="wrapper">
			<div class="container">
				<div class="row">
					<header id="header">
						<div class="row">
							<div class="col-md-9">
								<div class="header-left">
									<div class="header-top">
										<div class="logo">
											<a href="#"><img src="resources/images/logo.png" alt="Shop Maquinas" width="178" height="140"></a>
										</div>
										<span class="slogan">"Agora ficou fácil comprar e vender sua máquina e implemento agrícola"</span>
									</div>
									<nav id="nav" class="navbar navbar-default" role="navigation">
										<div class="container-fluid">
											<div class="collapse navbar-collapse">
												<ul class="nav navbar-nav">
													<li><a href="#">home</a></li>
													<li><a href="#">compre</a></li>
													<li><a href="#">venda</a></li>
													<li><a href="#">acessórios</a></li>
												</ul>
											</div>
										</div>
									</nav>
								</div>
							</div>
							<div class="col-md-3">
								<div class="header-right">
									<a href="#" class="btn btn-default"><span class="icon-star"></span>Favoritos</a>
									<a href="#" class="btn btn-default"><span class="icon-user"></span>Login</a>
								</div>
							</div>
						</div>
					</header>
					<main id="main">
						
						<tiles:insertAttribute name="content" />
						
					</main>
					<footer id="footer">
						<div class="row">
							<div class="col-md-8">
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
							<div class="col-md-4">
								<div class="footer-right">
									<div class="logo">
										<a href="#"><img src="resources/images/logo.png" alt="Shop Maquinas" width="178" height="140"></a>
									</div>
									<ul class="social-networks">
										<li><a href="#"><span class="icon-twitter"></span></a></li>
										<li><a href="#"><span class="icon-facebook"></span></a></li>
										<li><a href="#"><span class="icon-feed"></span></a></li>
									</ul>
								</div>
							</div>
						</div>
					</footer>
					
					<tiles:insertAttribute name="afterFooter" />
					
				</div>
			</div>
		</div>
		<script type="text/javascript" src="resources/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="resources/js/bootstrap.js"></script>
		
		<tiles:insertAttribute name="scripts" />
	</body>
</html>