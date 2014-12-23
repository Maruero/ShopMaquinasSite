<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">./</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Venda</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details">
			<div class="description side-by-side">
				<h3>Seus contratos</h3>
				
				<c:forEach items="${contracts}" var="contract">
					<ul class="info">
						<li>
							<strong class="title">N�mero do contrato</strong>
							<span class=""><fmt:formatNumber type="number" pattern="000000" value="${contract.contractID}" /></span>
						</li>
						<li>
							<strong class="title">Tipo do contrato</strong>
							<span>${contract.contractDefinition.name}</span>
						</li>
						<li>
							<strong class="title">Situa��o</strong>
							<span class="value price">
								
								<c:choose>
									<c:when test="${contract.contractStatus == 'CANCELED'}">
										Cancelado
									</c:when>
									<c:when test="${contract.contractStatus == 'NOT_PAID'}">
										Aguardando pagamento
									</c:when>
									<c:when test="${contract.contractStatus == 'ACTIVE'}">
										Ativo
									</c:when>
									<c:when test="${contract.contractStatus == 'EXPIRED'}">
										Expirado
									</c:when>
								</c:choose>
							
							</span>
						</li>
						<li>
							<strong class="title">Data da contrata��o</strong>
							<span class="value date-label">
								<fmt:formatDate value="${contract.startDate}" pattern="dd/MM/yyyy" />
							</span>
						</li>
						
						<c:if test="${contract.endDate != null}">
							<li>
								<strong class="title">Data de expira��o</strong>
								<span class="value date-label">
									<fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy" />
								</span>
							</li>
						</c:if>
						
						<li>
							<strong class="title">An�ncios cadastrados</strong>
							<span class="value">
							
								<c:forEach items="${contract.ads}" var="ad">
									<p>${ad.description}<a href="anuncios/detalhes-do-anuncio/${ad.adID}"><span class="value price">Exibir</span></a><br></p>	
								</c:forEach>
							
							</span>
						</li>
						
					</ul>
					
					<div class="text-wrap">
					
						<c:choose>
							<c:when test="${contract.contractStatus == 'ACTIVE'}">
								<button type="button" class="btn btn-danger" href="anunciar/novo-anuncio" onClick="return restricted(this);">Novo an�ncio</button>
							</c:when>
							<c:when test="${contract.contractStatus == 'EXPIRED'}">
								<button data-error-message="Este contrato est� expirado." type="button" class="btn btn-danger" href="anunciar/novo-anuncio" onClick="return restricted(this);">Novo an�ncio</button>
							</c:when>
							<c:when test="${contract.contractStatus == 'NOT_PAID'}">
								<button data-error-message="Ainda n�o identificamos o pagamento desse contrato." type="button" class="btn btn-danger" href="anunciar/novo-anuncio" onClick="return restricted(this);">Novo an�ncio</button>
							</c:when>
							<c:otherwise>
								<button data-error-message="Este contrato est� inativo." type="button" class="btn btn-danger" href="anunciar/novo-anuncio" onClick="return restricted(this);">Novo an�ncio</button>
							</c:otherwise>
						</c:choose>
					
						
					</div>
				</c:forEach>
				
			</div>
			<div class="description side-by-side">
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
						<c:forEach items="${contracts}" var="contract">
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
		</article>
		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="scripts">
		<script type="text/javascript" src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js">
		</script>		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
