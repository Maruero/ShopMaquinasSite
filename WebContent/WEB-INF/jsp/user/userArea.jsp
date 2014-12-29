<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">./</tiles:putAttribute>
	<tiles:putAttribute name="pageName">�rea do Cliente</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details">
									
			<div class="row">
				<div class="col-xs-6 description side-by-side">
					<h3>Seus dados</h3>
					<ul class="info">
						<li>
							<c:choose>
								<c:when test="${person.personType == 'HUMAN'}">
									<strong class="small-title">CPF:</strong>	
								</c:when>
								<c:otherwise>
									<strong class="small-title">CNPJ:</strong>
								</c:otherwise>
							</c:choose>
							
							<span class="value price">${person.firstDocument.documentNumber}</span>
						</li>
						<li>
							<strong class="small-title">Email:</strong>
							<span class="value price">${person.email}</span>
						</li>
						<li>
							<c:choose>
								<c:when test="${person.personType == 'HUMAN'}">
									<strong class="small-title">Nome:</strong>	
								</c:when>
								<c:otherwise>
									<strong class="small-title">Nome Fantasia:</strong>
								</c:otherwise>
							</c:choose>
							<span class="value price">${person.firstname}</span>
						</li>
						<li>
							<c:choose>
								<c:when test="${person.personType == 'HUMAN'}">
									<strong class="small-title">Sobrenome:</strong>	
								</c:when>
								<c:otherwise>
									<strong class="small-title">Raz�o social:</strong>
								</c:otherwise>
							</c:choose>
							<span class="value price">${person.lastname}</span>
						</li>
						<li>
							<strong class="small-title">Telefone:</strong>
							<span class="value price">${person.phone}</span>
						</li>
						<li>
							<strong class="small-title">Endere�o:</strong>
							<span class="value price">
								${person.address.street} <br> 
								${person.address.number} <br>
								${person.address.complement} <br>
								${person.address.neighborhood} <br>
								${person.address.city} <br>
								${person.address.uf} <br>
								${person.address.cep} <br>
							</span>
						</li>
					</ul>
					<div class="text-wrap" style="width:200px;">
						<button type="button" class="btn btn-danger" style="float:left;" data-toggle="modal" data-target="#myModal22">Trocar senha</button>
						<a class="btn btn-danger" style="float:right;" href="contrato/cadastro">Editar</a>
					</div>
				</div>
				<div class="col-xs-6 description side-by-side">
					<h3>Suas faturas</h3>
					<table style="width:440px;">
					<colgroup>
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
					</colgroup>
					<thead>
						<tr>
							<th>#</th>
							<th>Vencimento</th>
							<th>Valor</th>
							<th>Situa��o<th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${person.contracts}" var="contract">
							<c:forEach items="${contract.billings}" var="billing">
								<tr>
									<td>
										<fmt:formatNumber type="number" pattern="000" value="${contract.contractID}" />
										<fmt:formatNumber type="number" pattern="000" value="${billing.billingID}" />
									</td>
									<td class="date">
										<fmt:formatDate value="${billing.dueDate}" pattern="dd/MM/yyyy" />
									</td>
									<td>R$ <fmt:formatNumber type="number" pattern="#,##0.00" value="${billing.amount}" /></td>
									<td>
										<c:choose>
											<c:when test="${billing.status == 'CANCELLED'}">
												Cancelado
											</c:when>
											<c:when test="${billing.status == 'PENDING'}">
												Pendente
												<div class="text-wrap">
													<button type="button" class="btn btn-danger" href="#" onClick="return gerarBoleto(${billing.billingID});">Boleto</button>
												</div>
											</c:when>
											<c:when test="${billing.status == 'PAID'}">
												Pago
											</c:when>
											<c:otherwise>
												Outro
											</c:otherwise>
										</c:choose>
										
									</td>
								</tr>
							</c:forEach>
						</c:forEach>
					</tbody>
					</table>
				</div>
			</div>
			
		</article>
		
		<article class="new-contract description product-details">
		</article>
		
		<article class="description product-details">
									
			<div class="row">
				<div class="col-xs-6 description side-by-side">
					<h3>An�ncios ativos</h3>
					<table style="width:440px;">
						<colgroup>
							<col style="width:5%;">
							<col style="width:20%;">
							<col style="width:5%;">
						</colgroup>
						<thead>
							<tr>
								<th>Data</th>
								<th>Descri��o</th>
								<th>A��es</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="ad" items="${activeAds}">
								<tr>
									<td><fmt:formatDate value="${ad.startDate}" pattern="dd/MM/yyyy" /></td>
									<td>${ad.description}</td>
									<td>
										<a href="anunciar/editar-anuncio?adID=${ad.adID}"><span class="glyphicon glyphicon-pencil"> </span></a>
										<a href="#" onClick="return removeAd(${ad.adID});"><span class="glyphicon glyphicon-remove"> </span></a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="col-xs-6 description side-by-side">
					<h3>Mensagens</h3>
					
					<table style="width:440px;">
						<colgroup>
							<col style="width:5%;">
							<col style="width:20%;">
							<col style="width:5%;">
						</colgroup>
						<thead>
							<tr>
								<th>Data</th>
								<th>Descri��o</th>
								<th>A��es</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="message" items="${messages}">
								<tr>
									<td><fmt:formatDate value="${message.date}" pattern="dd/MM/yyyy" /></td>
									<td>${message.description}</td>
									<td>
										<a href="#" onClick="return openMessage(${message.messageID});">
											<c:choose>
												<c:when test="${message.status == 'NEW' }">
													<span id="${message.messageID}" class="glyphicon glyphicon-envelope"> </span>	
												</c:when>
												<c:otherwise>
													<span class="glyphicon glyphicon-ok"> </span>
												</c:otherwise>
											</c:choose>
											
										</a>
										<a href="#" onClick="return removeMessage(${message.messageID});"><span class="glyphicon glyphicon-remove"> </span></a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
				</div>
			</div>
			
		</article>
	
	</tiles:putAttribute>
	
	<tiles:putAttribute name="scripts">
		<script type="text/javascript" src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js">
		</script>		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="afterFooter">
		<div class="modal modal1 fade" id="myModal22" tabindex="-1" role="dialog" aria-labelledby="myModalLabel22" aria-hidden="true">
			<div class="modal-dialog modal-dialog-popup">
				<div class="modal-content">
<!-- 					<button type="button" class="close" data-dismiss="modal"><span class="icon-close"></span></button> -->
					<div class="row">
						<div class="col-md-12">
							<section class="contact-form">
								<h2 id="h3-popup">Trocar senha</h2>
								<div role="form" class="form" id="proposal-group">
									<div class="form-group">
										<label for="name">Senha atual*</label>
										<input type="password" class="form-control" id="old-password" requiredLength="1" requiredMessage="O campo nome � obrigat�rio">
									</div>
									<div class="form-group">
										<label for="Email">Nova senha*</label>
										<input type="password" class="form-control" id="new-password" requiredLength="1" requiredMessage="O campo e-mail � obrigat�rio">
									</div>
									<div class="form-group">
										<label for="phone">Confirma��o da nova senha*</label>
										<input type="password" class="form-control" id="new-repassword" requiredLength="1" requiredMessage="O campo telefone � obrigat�rio">
									</div>
									<a type="submit" id="change-password-button" class="btn btn-danger" onclick="changePassword();">trocar</a>
								</div>
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>