<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../</tiles:putAttribute>
	<tiles:putAttribute name="pageName">
		Dados do Usu�rio
	</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details contact-form">
			<c:choose>
				<c:when test="${ !update }">
					<form class="form" role="form" id="register-form" action="salvar-novo-contrato" method="POST" id="form-new-ad" onSubmit="return validate('register-form');">
				</c:when>
				<c:otherwise>
					<form class="form" role="form" id="register-form" action="salvar-dados" method="POST" id="form-new-ad" onSubmit="return validate('register-form');">
				</c:otherwise>
			</c:choose>
				<input type="hidden" name="contractDefinitionID" value="${contractDefinitionID}"/>
			
				<div class="description" style="float:left; min-width:450px;">
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<input type="hidden" name="person.personType" value="COMPANY"/>
								<input type="hidden" name="person.documents[0].documentType" value="CNPJ"/>
								<label for="name">CNPJ*</label>
								
								<c:choose>
									<c:when test="${update}">
										<input id="input-cnpj" onblur="checarCpf(this);" type="text" class="form-control" placeholder="CNPJ -  somente n�meros" name="person.documents[0].documentNumber" value="${person.documents[0].documentNumber}" alt="99999999999999" requiredLength="14" requiredMessage="CNPJ inv�lido."/>
									</c:when>
									<c:otherwise>
										<input id="input-cnpj" onblur="checarCpf(this);" type="text" class="form-control" placeholder="CNPJ -  somente n�meros" name="person.documents[0].documentNumber" value="" alt="99999999999999" requiredLength="14" requiredMessage="CNPJ inv�lido."/>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="person.personType" value="HUMAN"/>
								<input type="hidden" name="person.documents[0].documentType" value="CPF"/>
								<label for="name">CPF*</label>
								
									<c:choose>
										<c:when test="${update}">
											<input id="input-cpf" onblur="checarCpf(this);" type="text" class="form-control" placeholder="CPF -  somente n�meros" name="person.documents[0].documentNumber" value="${person.documents[0].documentNumber}" alt="99999999999" requiredLength="11" requiredMessage="CPF inv�lido."/>
										</c:when>
										<c:otherwise>
											<input id="input-cpf" onblur="checarCpf(this);" type="text" class="form-control" placeholder="CPF -  somente n�meros" name="person.documents[0].documentNumber" value="" alt="99999999999" requiredLength="11" requiredMessage="CPF inv�lido."/>
										</c:otherwise>
									</c:choose>
								
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<label for="name">Email*</label>
						<input type="text" id="input-email" class="form-control" placeholder="E-mail" name="person.email" value="${person.email}" requiredLength="1" requiredMessage="O e-mail � obrigat�rio"/>
					</div>
					
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<label for="name">Nome fantasia*</label>
								<input type="text" class="form-control" placeholder="Nome fantasia" name="person.firstname" value="${person.firstname}" requiredLength="1" requiredMessage="O nome fantasia � obrigat�rio"/>
							</c:when>
							<c:otherwise>
								<label for="name">Nome*</label>
								<input type="text" class="form-control" placeholder="Nome" name="person.firstname" value="${person.firstname}" requiredLength="1" requiredMessage="O nome � obrigat�rio"/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<label for="name">Raz�o Social*</label>
								<input type="text" class="form-control" placeholder="Sobrenome" name="person.lastname" value="${person.lastname}" requiredLength="1" requiredMessage="A raz�o social � obrigat�ria"/>
							</c:when>
							<c:otherwise>
								<label for="name">Sobrenome*</label>
								<input type="text" class="form-control" placeholder="Sobrenome" name="person.lastname" value="${person.lastname}" requiredLength="1" requiredMessage="O sobrenome � obrigat�rio"/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<c:if test="${ !update }">
						<div class="form-group">
							<label for="name">Senha*</label>
							<input type="password" class="form-control" name="user.password" placeholder="Senha" id="password" value="" requiredLength="1" requiredMessage="Senha � obrigat�rio."/>
						</div>
						
						<div class="form-group">
							<label for="name">Confirma��o de senha*</label>
							<input onblur="checarSenha();" type="password" class="form-control" name="" placeholder="Confirma��o de senha" id="password-confirmation" value="" requiredLength="1" requiredMessage="Confirma��o de senha � obrigat�rio."/><br/>
						</div>
					</c:if>
					
					<c:if test="${!isCompanyContract}">
						<div class="form-group">
							<label for="name">Sexo*</label>
						</div>
						<div class="form-group" style="">
							<input type="radio" style="margin-bottom:10px;" name="person.gender" value="MALE" id="male" checked="checked"/><label for="male">Masculino</label>
						</div>
						<div class="form-group" style="">
							<input type="radio" style="margin-bottom:10px;" name="person.gender" id="female" value="FEMALE" /> <label for="female">Feminino</label>
						</div>
					</c:if>
					
				</div>
				<div class="description">
					<div class="form-group">
						<label for="name">Telefone*</label>
						<input type="text" class="form-control" name="person.phone" placeholder="Telefone" id="celular" value="${person.phone}" requiredLength="1" requiredMessage="Telefone inv�lido."/><br/>
					</div>
					<div class="form-group">
						<label for="name">Endere�o*</label>
					</div>
					<div class="form-group" style="display:inline-block;padding-bottom:40px;">
						<input type="text" class="form-control" placeholder="CEP - somente n�meros" name="person.address.cep" id="cep" value="${person.address.cep}" alt="99999999" requiredLength="8" requiredMessage="CEP inv�lido." style="display:inline-block; width:180px;"/> 
						<button class="btn btn-danger" onclick="return buscarCEP();" type="button" style="display:inline-block; margin:0 40px;">Pesquisar</button>
					</div>
					
					<div class="form-group">
						<input type="text" class="form-control address-input" name="person.address.street" id="logradouro" value="${person.address.street}" placeholder="Logradouro" requiredLength="1" requiredMessage="Logradouro � obrigat�rio."/><br/>
						<input type="text" class="form-control" name="person.address.number" id="numero" value="${person.address.number}" placeholder="N�mero" requiredLength="1" requiredMessage="N�mero � obrigat�rio."/><br/>
						<input type="text" class="form-control" name="person.address.complement" id="complemento" value="${person.address.complement}" placeholder="Complemento" /><br/>
						<input type="text" class="form-control address-input" name="person.address.neighborhood" id="bairro" value="${person.address.neighborhood}" placeholder="Bairro" requiredLength="1" requiredMessage="Bairro � obrigat�rio."/><br/>
						<input type="text" class="form-control address-input" name="person.address.city" id="cidade" value="${person.address.city}" placeholder="Cidade" requiredLength="1" requiredMessage="Cidade � obrigat�rio."/><br/>
						<input type="text" class="form-control address-input" name="person.address.uf" placeholder="UF" id="uf" alt="**" value="${person.address.uf}" requiredLength="2" requiredMessage="UF � obrigat�rio."/><br/>
						
						
					</div>
					
				</div>
					
			</form>
				<c:if test="${isCompanyContract}">
					<div class="description form" style="float:left; min-width:450px;">
						<div class="form-group">
							<label for="name">Imagem carregada</label>
							<div class="form-control" style="min-height:110px;overflow:auto;">
								<iframe name="iframeImage1" width="100" height="100" frameBorder="0">
								</iframe>
							</div>
						</div>
					</div>
					<div class="description form">
						<div class="form-group">
							<label for="name">Imagem</label>
							<div>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage1">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
							</div>
						</div>
					</div>
				
				</c:if>
				<div class="description" style="margin-top:15px;">
					<div class="text-wrap" style="width:835px;">
						<input id="register-button" type="submit" class="btn btn-danger" style="float:right;" value="Salvar" onclick="$('#register-form').submit();"/>
					</div>
				</div>
						
		</article>
		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="scripts">
		<c:if test="${ update }">
			<script type="text/javascript">
				$(document).ready(function(){
					$("#input-cnpj").attr('readonly', 'readonly');
					$("#input-cpf").attr('readonly', 'readonly');
					$("#input-cnpj").attr('onblur', '');
					$("#input-cpf").attr('onblur', '');
					$("#input-email").attr('readonly', 'readonly');
				});
			</script>		
		</c:if>
		&nbsp;
	</tiles:putAttribute>
	
</tiles:insertDefinition>
