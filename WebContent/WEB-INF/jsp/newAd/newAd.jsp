<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="shopmaquinas.template">
	
	<tiles:putAttribute name="pathPrefix">../</tiles:putAttribute>
	<tiles:putAttribute name="pageName">Novo Anúncio</tiles:putAttribute>
	
	<tiles:putAttribute name="content">
	
		<article class="product-details contact-form">
			<form role="form" action="novo-anuncio" method="POST" id="form-new-ad">
				<input type="hidden" name="ad.contract.contractID" value="${Contract.contractID}"/>
				<input type="hidden" name="ad.adPropertyValues[11].adProperty.name" value="HIGHLIGHTED"/>
				<input type="hidden" name="ad.adPropertyValues[11].value" value="true"/>
				<input type="hidden" name="outro" id="imageSentCount" value="" requiredLength="1" requiredMessage="O envio de pelo menos uma imagem é obrigatório."/>
				
			
				<div class="description" style="float:left; min-width:450px;padding:7px 60px 0 0;">
					<div class="form-group">
						<label for="name">Tipo*</label>
						
						<input type="hidden" name="ad.adPropertyValues[0].adProperty.name" value="TYPE"/>
						<select class="form-control" name="ad.adPropertyValues[0].value" requiredLength="1" requiredMessage="Por favor, informe o tipo." id="type-select">
							<option value="">Selecione</option>
							
							<c:forEach items="${types}" var="type">
								<option value="${type.name}" data-id="${type.id}">${type.name}</option>
							</c:forEach>
						</select>
					</div>
					
					<div class="form-group">
						<label for="name">Grupo*</label>
						
						<input type="hidden" name="ad.adPropertyValues[1].adProperty.name" value="GROUP"/>
						<select class="form-control" name="ad.adPropertyValues[1].value" requiredLength="1" requiredMessage="Por favor, informe o grupo." id="group-select">
							<option value="">Selecione</option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="name">Categoria*</label>
						
						<input type="hidden" name="ad.adPropertyValues[2].adProperty.name" value="CATEGORY"/>
						<select class="form-control" name="ad.adPropertyValues[2].value" requiredLength="1" requiredMessage="Por favor, informe a categoria." id="category-select">
							<option value="">Selecione</option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="name">Marca*</label>
						
						<input type="hidden" name="ad.adPropertyValues[3].adProperty.name" value="BRAND"/>
						<select class="form-control" name="ad.adPropertyValues[3].value" requiredLength="1" requiredMessage="Por favor, informe a marca." id="brand-select">
							<option value="">Selecione</option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="name">Modelo*</label>
						
						<input type="hidden" name="ad.adPropertyValues[4].adProperty.name" value="MODEL"/>
						<select class="form-control" name="ad.adPropertyValues[4].value" requiredLength="1" requiredMessage="Por favor, informe o modelo." id="model-select">
							<option value="">Selecione</option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="name">Cor*</label>
						
						<input type="hidden" name="ad.adPropertyValues[5].adProperty.name" value="COLOR"/>
						<input type="text" class="form-control" name="ad.adPropertyValues[5].value" requiredLength="1" requiredMessage="Por favor, informe a cor." />					
					</div>
					
					<div class="form-group">
						<label for="name">Ano*</label>
						
						<input type="hidden" name="ad.adPropertyValues[6].adProperty.name" value="YEAR"/>
						<input type="text" class="form-control" name="ad.adPropertyValues[6].value" alt="9999" requiredLength="1" requiredMessage="Por favor, informe o ano de fabricação."/>
					</div>
					
					<div class="form-group">
						<label for="name">Descrição</label>
						
						<input type="hidden" name="ad.adPropertyValues[7].adProperty.name" value="LONG_DESCRIPTION"/>
						<textarea class="form-control" id="message" rows="3" name="ad.adPropertyValues[7].value"></textarea>
					</div>
				</div>
				<div class="description">
					<div class="form-group">
						<label for="name">Propriedades</label>
					</div>
					
					<div class="form-group" style="text-align:center;">
					
						<input type="hidden" name="ad.adPropertyValues[8].adProperty.name" value="UNIQUE_OWNER"/>
						<input type="checkbox" name="ad.adPropertyValues[8].value" value="true"/> Único dono
					</div>
					
					<div class="form-group" style="text-align:center;"> 
					
						<input type="hidden" name="ad.adPropertyValues[9].adProperty.name" value="EXCHANGE"/>
						<input type="checkbox" name="ad.adPropertyValues[9].value" value="true"/> Aceita troca
					</div>
					
					 
					
					<div class="form-group">
						<label for="name">horas/kilometragem*</label>
						
						<input type="hidden" name="otherProperties[0].adProperty.name" value="HOURS" />
						<input type="text" class="form-control" name="otherProperties[0].value" alt="999999" requiredLength="1" requiredMessage="Por favor, informe a quantidade de horas de uso."/>
					</div>
					
					
					<div class="form-group">
						<label for="name">Valor*</label>
						
						<input type="hidden" name="otherProperties[1].adProperty.name" value="PRICE" />
						<input type="text" onblur="putSymbol(this);" class="form-control" name="otherProperties[1].value" alt="decimal" requiredLength="1" requiredMessage="Por favor, informe o preço."/>
					</div>
					
					
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
				
			</form>
			<div class="form-group">
				<label for="name">Imagens</label>
				<div>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage1">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage2">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage3">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage4">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage5">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage6">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();"  style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage7">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();" style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
					<form action="salvar-imagem" method="post" enctype='multipart/form-data' id="form-send-image" target="iframeImage8">
						<input type="file" data-image-num="1" name="image" style="display:inline-block">
						<input type="submit" onclick="incrementImageSentCount();" style="display:inline-block" data-image-num="1" value="enviar"/>
					</form>
				</div>
			</div>
			<div class="text-wrap" style="width:830px;">
				<button type="button" class="btn btn-danger" style="float:left;" onclick="return previewAd();">Visualizar</button>
				<button type="button" class="btn btn-danger" style="float:right;" onclick="return saveAd();">Salvar</button>
			</div>
		</article>
		
	</tiles:putAttribute>
	
</tiles:insertDefinition>
