<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Dados do Novo Usuário</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details contact-form">
			<form class="form" role="form" id="register-form" action="salvar-novo-contrato" method="POST" id="form-new-ad" onSubmit="return validate('register-form');">
				<input type="hidden" name="contractDefinitionID" value="${contractDefinitionID}"/>
			
				<div class="description" style="float:left; min-width:450px;">
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<input type="hidden" name="person.personType" value="COMPANY"/>
								<input type="hidden" name="person.documents[0].documentType" value="CNPJ"/>
								<label for="name">CNPJ*</label>
								<input type="text" class="form-control" placeholder="CNPJ -  somente números" name="person.documents[0].documentNumber" value="" alt="99999999999999" requiredLength="14" requiredMessage="CNPJ inválido."/>
							</c:when>
							<c:otherwise>
							<input type="hidden" name="person.personType" value="HUMAN"/>
							<input type="hidden" name="person.documents[0].documentType" value="CPF"/>
								<label for="name">CPF*</label>
								<input type="text" class="form-control" placeholder="CPF -  somente números" name="person.documents[0].documentNumber" value="" alt="99999999999" requiredLength="11" requiredMessage="CPF inválido."/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<label for="name">Email*</label>
						<input type="text" class="form-control" placeholder="E-mail" name="person.email" value="" requiredLength="1" requiredMessage="O e-mail é obrigatório"/>
					</div>
					
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<label for="name">Nome fantasia*</label>
								<input type="text" class="form-control" placeholder="Nome fantasia" name="person.firstname" value="" requiredLength="1" requiredMessage="O nome fantasia é obrigatório"/>
							</c:when>
							<c:otherwise>
								<label for="name">Nome*</label>
								<input type="text" class="form-control" placeholder="Nome" name="person.firstname" value="" requiredLength="1" requiredMessage="O nome é obrigatório"/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<c:choose>
							<c:when test="${isCompanyContract}">
								<label for="name">Razão Social*</label>
								<input type="text" class="form-control" placeholder="Sobrenome" name="person.lastname" value="" requiredLength="1" requiredMessage="A razão social é obrigatória"/>
							</c:when>
							<c:otherwise>
								<label for="name">Sobrenome*</label>
								<input type="text" class="form-control" placeholder="Sobrenome" name="person.lastname" value="" requiredLength="1" requiredMessage="O sobrenome é obrigatório"/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<label for="name">Senha*</label>
						<input type="password" class="form-control" name="user.password" placeholder="Senha" id="password" value="" requiredLength="1" requiredMessage="Senha é obrigatório."/>
					</div>
					
					<div class="form-group">
						<label for="name">Confirmação de senha*</label>
						<input type="password" class="form-control" name="" placeholder="Confirmação de senha" id="password-confirmation" value="" requiredLength="1" requiredMessage="Confirmação de senha é obrigatório."/><br/>
					</div>
					
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
						<input type="text" class="form-control" name="person.phone" placeholder="Telefone" id="celular" value="" requiredLength="1" requiredMessage="Telefone inválido."/><br/>
					</div>
					<div class="form-group">
						<label for="name">Endereço*</label>
					</div>
					<div class="form-group" style="display:inline-block;padding-bottom:40px;">
						<input type="text" class="form-control" placeholder="CEP - somente números" name="person.address.cep" id="cep" value="" alt="99999999" requiredLength="8" requiredMessage="CEP inválido." style="display:inline-block; width:180px;"/> 
						<button class="btn btn-danger" onclick="return buscarCEP();" type="button" style="display:inline-block; margin:0 40px;">Pesquisar</button>
					</div>
					
					<div class="form-group">
						<input type="text" class="form-control address-input" name="person.address.street" id="logradouro" value="" placeholder="Logradouro" requiredLength="1" requiredMessage="Logradouro é obrigatório."/><br/>
						<input type="text" class="form-control" name="person.address.number" id="numero" value="" placeholder="Número" requiredLength="1" requiredMessage="Número é obrigatório."/><br/>
						<input type="text" class="form-control" name="person.address.complement" id="complemento" value="" placeholder="Complemento" /><br/>
						<input type="text" class="form-control address-input" name="person.address.neighborhood" id="bairro" value="" placeholder="Bairro" requiredLength="1" requiredMessage="Bairro é obrigatório."/><br/>
						<input type="text" class="form-control address-input" name="person.address.city" id="cidade" value="" placeholder="Cidade" requiredLength="1" requiredMessage="Cidade é obrigatório."/><br/>
						<input type="text" class="form-control address-input" name="person.address.uf" placeholder="UF" id="uf" alt="**" value="" requiredLength="2" requiredMessage="UF é obrigatório."/><br/>
						
						
					</div>
					
				</div>
					
			</form>
				<c:if test="${isCompanyContract}">
					<div class="description form" style="float:left; min-width:450px;">
						<div class="form-group">
							<label for="name">Imagens carregadas</label>
							<div class="form-control" style="min-height:110px;overflow:auto;">
								<iframe name="iframeImage1" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage2" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage3" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage4" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage5" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage6" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage7" width="100" height="100" frameBorder="0">
								</iframe>
								<iframe name="iframeImage8" width="100" height="100" frameBorder="0">
								</iframe>
							</div>
						</div>
					</div>
					<div class="description form">
						<div class="form-group">
							<label for="name">Imagens</label>
							<div>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage1">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage2">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage3">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage4">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage5">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage6">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage7">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();" style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
								<form action="../anunciar/salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage8">
									<input type="file" data-image-num="1" name="image" style="display:inline-block">
									<input type="submit" onclick="incrementImageSentCount();" style="display:inline-block" data-image-num="1" value="enviar"/>
								</form>
							</div>
						</div>
					</div>
				
				</c:if>
				<div class="description" style="margin-top:15px;">
					<div class="text-wrap" style="width:835px;">
						<input type="submit" class="btn btn-danger" style="float:right;" value="Salvar" onclick="$('#register-form').submit();"/>
					</div>
				</div>
						
		</article>
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
