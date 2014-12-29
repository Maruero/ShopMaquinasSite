$(document).ready(function() {
	
	formatDates();
	processError();
	showFirst();
	setMasks();
	
	$(':input').setMask();
	$('input[data-mask]').each(function() {
		  $(this).setMask($(this).data('mask'));
	});
	
	loadOptions();
	bindEvents();
});

function bindEvents(){
	$('#logon-password[data-id="login-password"]').keypress(function (e) {
	  if (e.which == 13) {
	    $('#login-button').click();
	    return false;    
	  }
	});
}

function processError(){
	var errorMessage = $("#errorMessage").html();
	if( errorMessage.length > 0 ){
		openRedPopup("Aten&ccedil;&atilde;o: ", errorMessage);
	}
}

function register(prefixPath){
	
	if(!prefixPath){
		prefixPath = "";
	}
	
	window.location = prefixPath + "contrato/novo-contrato";
	return false;
}

function checarSenha(){
	var password = $("#password").val();
	var passwordConfirmation = $("#password-confirmation").val();
	var equals = (password == passwordConfirmation);
	if( !equals){
		$("#password").addClass("input-error");
		$("#password-confirmation").addClass("input-error");
		$("#register-button").hide();
		openRedPopup("Aten&ccedil;&atilde;o: ", "Senha e confirma&ccedil;&atilde;o de senha informados n&atilde;o conferem!");
	}else{
		$("#register-button").show();
		$("#password").removeClass("input-error");
		$("#password-confirmation").removeClass("input-error");
	}
}

function checarCpf(input){
	
	var cpf = $("#input-cpf").val();
	var cnpj = $("#input-cnpj").val();
	
	var goon = false;
	
	if( cpf && valida_cpf() ){
		goon = true;
	}else if( cnpj && valida_cnpj() ){
		goon = true;
	}else{
		openRedPopup("Aten&ccedil;&atilde;o: ", "Documento inv&aacute;lido.");
		$(input).addClass("input-error");
	}
	
	if( goon ){
		$.ajax({
			url: 'checar-cpf',
			data: {
				'cpf' : $(input).val()
			},
			success: function (data){
				if(data.long > 0 ){
					$(input).addClass("input-error");
					$("#register-button").hide();
					openRedPopup("Aten&ccedil;&atilde;o: ", "O documento informado j&aacute; est&aacute; cadastrado na Shop M&aacute;quinas!");
				}else{
					$("#register-button").show();
					$(input).removeClass("input-error");
				}
		}
	});
	}
}

/** ---------------- LOGIN ----------------**/
var loginNextPage = ".";

function openLogin(prefixPath){
	
	if(!prefixPath){
		prefixPath = "";
	}
	
	var username = $("#user-logged").html();
	
	if( username.length < 1){
		loginNextPage = prefixPath + 'area-do-cliente';
		$("#popup-login-opener").click();
	}else{
		$.ajax({
			url: prefixPath + 'sair',
			method: 'POST',
			success: function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					$("#user-logged").html('');
					$("#login-label").html('login');
					
					var restricted = $("#page-restricted").html();
					if( restricted ){
						var pathPrefix = $("#pathPrefix").html();
						window.location = pathPrefix;
					}
				}
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data );
			}
		});
	}
	return false;
}

function login(prefixPath){
	
	if(!prefixPath){
		prefixPath = "";
	}
	
	$("#login-button").html("Carregando...");
	
	if(validate("login-group")){
		$.ajax({
			url: prefixPath + 'logon',
			method: 'POST',
			data : {
				'username' : $("#logon-username").val(),
				'password' : $("#logon-password").val()
			},
			success: function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					
					$("#user-logged").html(data.jsonResponse.data);
					$("#login-label").html('sair');
					if( loginNextPage != "." ){
						window.location = loginNextPage;
					}
					$("#popup-login-opener").click();
				}
				$("#login-button").html("Entrar");
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				$("#login-button").html("Entrar");
			}
		});
	}else{
		$("#login-button").html("Entrar");
	}
	return false;
}



/** ---------------- SEND PROPOSAL ----------------**/
function sendProposal(){
	
	if( validate("proposal-group") ){
		
		$("#send-proposal-button").html('enviando...');
		
		$.ajax({
			url: '../salvar-proposta',
			method: 'POST',
			data: {
				'adID' : $("#proposal-adID").val(),
				'person.firstname' : $("#proposal-name").val(),
				'person.phone' : $("#proposal-phone").val(),
				'person.email' :  $("#proposal-email").val(),
				'text' : $("#proposal-text").val()
			},
			success: function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					openGreenPopup("Sucesso!", data.jsonResponse.message );
				}
				$("#send-proposal-button").html('enviar');
				
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				$("#send-proposal-button").html('enviar');
			}
		});
		
	}
	
	return false;
}



/** ---------------- COMMONS ----------------**/

/** ===============================================================
 * Função usada para alterar o formato das datas no DOM.
 ==================================================================**/
function formatDates(){
	$('.date-label').each(function(date){
		var date = $(this).html();
		if( date != null && date.split('-').length == 3){
			date = date.split(' ')[0];
			date = date.split('-')[2] + '/' + date.split('-')[1] + '/' + date.split('-')[0]; 
		}
		$(this).html(date);
	});
}

/** ===============================================================
 * Função para validar se o usuário está logado
 ==================================================================**/

function restricted(link){
	
	var errorMessage = $(link).attr("data-error-message");
	if(errorMessage){
		openRedPopup("Aten&ccedil;&atilde;o!", errorMessage);
		return false;
	}
	
	var href = $(link).attr("href");
	
	var username = $("#user-logged").html();
	if( username.length < 1){
		loginNextPage = href;
		$("#popup-login-opener").click();
	}else{
		window.location = href;
	}
	
	return false;
	
}

/** ===============================================================
 * Função para validar valores obrigatórios
 ==================================================================**/

function validate(group){
	var response = true;
	$( "#" + group + " [requiredLength]").each(function (element){
		var qtd = $(this).attr("requiredLength");
		if( $(this).val().length < qtd){
			if( response ){
				openRedPopup("Aten&ccedil;&atilde;o!", $(this).attr("requiredMessage"));
				response = false;
			}
		}
	});
	return response;
}

/** ===============================================================
 * Funções para controlar a abertura de popups.
 ==================================================================**/

function openRedPopup( title, message ){
	$("#h2-popup").removeClass("green");
	$("#h2-popup").addClass("red");
	
	$("#div-label-popup").show();
	$("#div-content-popup").hide();
	
	openPopup(title, message);
}

function openGreenPopup( title, message ){
	$("#h2-popup").addClass("green");
	$("#h2-popup").removeClass("red");
	
	$("#div-label-popup").show();
	$("#div-content-popup").hide();
	
	openPopup(title, message);
}

function openPopup( title, message ){
	$("#h2-popup").html('');
	$("#label-popup").html('');
	
	$("#h2-popup").html(title);
	$("#label-popup").html(message);
	$("#popup-opener").click();
}




/** ===============================================================
 * Criação de anúncios
 ==================================================================**/

function sendImage(button){
	
	var imageNum = $(button).attr("data-image-num");
	$("#input-send-image").val( $("input[data-image-num='"+imageNum+"']").val() );
	$("#form-send-image").attr("target", "iframeImage" + imageNum);
	$("#form-send-image").submit();
	
}


function saveAd(){
	if( validate("form-new-ad")){
		if( $('#form-new-ad').attr('data-update') != "true"){
			$('#form-new-ad').attr('action', 'novo-anuncio');
		}else{
			$('#form-new-ad').attr('action', 'salvar-anuncio');	
		}
		$('#form-new-ad').prop('target', '');
		$('#form-new-ad').submit();
	}
	return false;
}

function previewAd(){
	if( validate("form-new-ad")){
		$('#form-new-ad').attr('action', 'novo-anuncio/visualizar');
		$('#form-new-ad').prop('target', 'about:blank');
		$('#form-new-ad').submit();
	}
	return false;
}

function incrementImageSentCount(){
	var val = $("#imageSentCount").val();
	if( !val || val == "" ){
		val = 1;
	}
	
	val += 1;
	$("#imageSentCount").val(val)
}


function hideNotMiniImageLink(){
	$(".photos-list li").hide();
	$(".photos-list img[src*='mini']").parent().parent().show();
}

function showFirst(){
	$("#mini-image-link").click();
}

function showImage(elem){
	var imageSrc = $(elem).attr('data-name');
	imageSrc = imageSrc.replace('mini-', '');
	$(".image-holder img").hide();
	$(".image-holder img[data-name='"+imageSrc+"']").show();
	
	return false;
}

function putSymbol(elem){
	var val = $(elem).val();
	if( val.split(' ').length < 2){
		//val = 'R$ ' + val;
	}
	$(elem).val(val);
}

/** ===============================================================
 * SELECT's
 ==================================================================**/

var typeJson = [
	{
		'id' : '3',
		'name' : 'Implementos'
	},
	{
		'id' : '4',
		'name' : 'Industriais'
	},
	{
		'id' : '2',
		'name' : 'M&aacute;quinas'
	},
	{
		'id' : '1',
		'name' : 'Pesados'
	},
];

function loadOptions(){
	
	var prefix = $("#pathPrefix").html();
	
	$('#group-select').prop('disabled', true);
	$('#category-select').prop('disabled', true);
	$('#brand-select').prop('disabled', true);
	$('#model-select').prop('disabled', true);
	
	$('#group-home-select').prop('disabled', true);
	$('#category-home-select').prop('disabled', true);
	$('#brand-home-select').prop('disabled', true);
	$('#model-home-select').prop('disabled', true);
	
	$("#type-select").html('<option value="" data-id="0">Selecione</option>');
	$(typeJson).each(function (option){
		$('#type-select').append(
			'<option value="'+this.name+'" data-id="'+this.id+'">'+this.name+'</option>'
		);
	});
	
	$("#type-select").on('change', function (){
		var typeId = $("#type-select option:selected").attr('data-id');
		var url = prefix + 'getGroups?typeId=' +typeId;
		var target = '#group-select';
		load(url, target);
	});
	
	$("#group-select").on('change', function (){
		var groupId = $("#group-select option:selected").attr('data-id');
		var url = prefix + 'getCategories?groupId=' +groupId;
		var target = '#category-select';
		load(url, target);
	});
	
	$("#category-select").on('change', function (){
		var categoryId = $("#category-select option:selected").attr('data-id');
		var url = prefix + 'getBrands?categoryId=' +categoryId;
		var target = '#brand-select';
		load(url, target);
	});
	
	$("#brand-select").on('change', function (){
		var brandId = $("#brand-select option:selected").attr('data-id');
		var url = prefix + 'getModels?brandId=' +brandId;
		var target = '#model-select';
		load(url, target);
	});
	
	$(typeJson).each(function (option){
		$('#type-home-select').append(
			'<option value="'+this.name+'" data-id="'+this.id+'">'+this.name+'</option>'
		);
	});
	
	$("#type-home-select").on('change', function (){
		var typeId = $("#type-home-select option:selected").attr('data-id');
		var url = prefix + 'getGroups?typeId=' +typeId;
		var target = '#group-home-select';
		load(url, target);
	});
	
	$("#group-home-select").on('change', function (){
		var groupId = $("#group-home-select option:selected").attr('data-id');
		var url = prefix + 'getCategories?groupId=' +groupId;
		var target = '#category-home-select';
		load(url, target);
	});
	
	$("#category-home-select").on('change', function (){
		var categoryId = $("#category-home-select option:selected").attr('data-id');
		var url = prefix + 'getBrands?categoryId=' +categoryId;
		var target = '#brand-home-select';
		load(url, target);
	});
	
	$("#brand-home-select").on('change', function (){
		var brandId = $("#brand-home-select option:selected").attr('data-id');
		var url = prefix + 'getModels?brandId=' +brandId;
		var target = '#model-home-select';
		load(url, target);
	});
	
}

function validateAdvancedSearch(){
	
	var yearMin = $("#year-min").val();
	var yearMax = $("#year-max").val();
	
	if( yearMin > yearMax ){
		openRedPopup("Aten&ccedil;&atilde;o!", "Por favor, verifique os valores informados com m&iacute;nimo e m&aacute;ximo para o Ano.");
		return false;
	}
	
	var hourMin = $("#hour-min").val();
	var hourMax = $("#hour-max").val();
	
	if( hourMin > hourMax ){
		openRedPopup("Aten&ccedil;&atilde;o!", "Por favor, verifique os valores informados com m&iacute;nimo e m&aacute;ximo para o Horas/Km.");
		return false;
	}
	
	var priceMin = $("#price-min").val();
	var priceMax = $("#price-max").val();
	
	if( priceMin > priceMax ){
		openRedPopup("Aten&ccedil;&atilde;o!", "Por favor, verifique os valores informados com m&iacute;nimo e m&aacute;ximo para o Pre&ccedil;o.");
		return false;
	}
	
	if( priceMin == "0,00" && priceMin == priceMax ){
		$("#price-min").attr('disabled', 'disabled');
		$("#price-max").attr('disabled', 'disabled');
	}
	
	return true;
}

function load( url , target ){
	$('' + target + ' option').html('Carregando');
	$.ajax({
		url : url,
		success: function(data){
			$('' + target).prop('disabled', false);
			$('' + target).html('<option value="" data-id="">Selecione</option>');
			$(data.list).each(function(option){
				$('' + target).append(
					'<option value="'+this.name+'" data-id="'+this.id+'">'+this.name+'</option>'
				);
			});
			$('' + target).trigger('optionsChanged');
		},
		error: function(data){
			
		}
	});
}



/*--------------------------- NOVO USUÁRIO -------------------------------*/
function setMasks() {
	$(':input').setMask();
	$('input[data-mask]').each(function() {
	  $(this).setMask($(this).data('mask'));
	});
	
	$('#celular').setMask("(99) 9999-99999");
	$('#celular').on('blur', function(event) {
	    var target, phone, element;
	    target = (event.currentTarget) ? event.currentTarget : event.srcElement;
	    phone = target.value.replace(/\D/g, '');
	    element = $(target);
	    element.unsetMask();
	    if(phone.length > 10) {
	        element.setMask("(99) 99999-9999");
	    } else {
	        element.setMask("(99) 9999-99999");
	    }
	});
	
	$('#proposal-phone').setMask("(99) 9999-99999");
	$('#proposal-phone').on('blur', function(event) {
	    var target, phone, element;
	    target = (event.currentTarget) ? event.currentTarget : event.srcElement;
	    phone = target.value.replace(/\D/g, '');
	    element = $(target);
	    element.unsetMask();
	    if(phone.length > 10) {
	        element.setMask("(99) 99999-9999");
	    } else {
	        element.setMask("(99) 9999-99999");
	    }
	});
}

function buscarCEP(){
	
	$('.address-input').val('Carregando...');
	
	$.ajax({
		url: 'http://cep.republicavirtual.com.br/web_cep.php',
		dataType: "jsonp",
		data: {
			'cep' : $('#cep').val(),
			'formato' : 'jsonp',
			'callback' : 'receiveCEP'
		},
		success: function(data){
			alert('success:' + data);
		}
	});
	return false;
}

function receiveCEP(cep){
	if( cep.uf == "" ){
		openRedPopup("Aten&ccedil;&atilde;o!", "Cep n&atilde;o encontrado");
	}else{			
		$("#uf").val(cep.uf);
		$("#cidade").val(cep.cidade);
		$("#bairro").val(cep.bairro);
		$("#logradouro").val(cep.tipo_logradouro + ' ' + cep.logradouro);
	}
	
}

/** ===============================================================
 * BILLINGS
 ==================================================================**/

function gerarBoleto(billingID){
	
	var payer = PagSeguroDirectPayment.getSenderHash();	
	if( payer ){
		window.open("gerar-boleto?billingID=" + billingID + "&payer=" + payer, "_blank", "toolbar=yes, scrollbars=yes, resizable=yes, top=0, left=0, width=700, height=500");
	}else{
		openRedPopup("Aten&ccedil;&atilde;o!", "Por favor, aguarde o total carregamento da p&aacute;gina antes de acionar esse bot&atilde;o." );
	}

	return false;
}

function valida_cnpj(){
	 
    var pri = $("#input-cnpj").val().substring(0,2);
    var seg = $("#input-cnpj").val().substring(2,5);
    var ter = $("#input-cnpj").val().substring(5,8);
    var qua = $("#input-cnpj").val().substring(8,12);
    var qui = $("#input-cnpj").val().substring(12,14);

    var i;
    var numero;
    var situacao = '';

    numero = (pri+seg+ter+qua+qui);

    var s = numero;


    var c = s.substr(0,12);
    var dv = s.substr(12,2);
    var d1 = 0;

    for (i = 0; i < 12; i++){
       d1 += c.charAt(11-i)*(2+(i % 8));
    }

    if (d1 == 0){
       var result = "falso";
    }
       d1 = 11 - (d1 % 11);

    if (d1 > 9) d1 = 0;

       if (dv.charAt(0) != d1){
          var result = "falso";
       }

    d1 *= 2;
    for (i = 0; i < 12; i++){
       d1 += c.charAt(11-i)*(2+((i+1) % 8));
    }

    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;

       if (dv.charAt(1) != d1){
          var result = "falso";
       }


    if (result == "falso") {
       return false;
    }
    
    return true;
}


function valida_cpf(){

    var pri = $("#input-cpf").val().substring(0,3);
    var seg = $("#input-cpf").val().substring(3,6);
    var ter = $("#input-cpf").val().substring(6,9);
    var qua = $("#input-cpf").val().substring(9,11);

    var i;
    var numero;

    numero = (pri+seg+ter+qua);

    var s = numero;
    var c = s.substr(0,9);
    var dv = s.substr(9,2);
    var d1 = 0;

    for (i = 0; i < 9; i++){
       d1 += c.charAt(i)*(10-i);
    }

    if (d1 == 0){
       var result = "falso";
    }

    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;

    if (dv.charAt(0) != d1){
       var result = "falso";
    }

    d1 *= 2;
    for (i = 0; i < 9; i++){
       d1 += c.charAt(i)*(11-i);
    }

    d1 = 11 - (d1 % 11);
    if (d1 > 9) d1 = 0;

    if (dv.charAt(1) != d1){
       var result = "falso";
    }


    if (result == "falso") {
       return false;
    }
    
    return true;
}


/****************************** AREA DO CLIENTE **************************/
function removeMessage( messageID ){
	
	var remove = confirm('Deseja realmente remover a mensagem?');
	if( remove ){
		$.ajax({
			url : 'excluir-mensagem',
			method: 'POST',
			data : {
				messageID : messageID
			},
			success : function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					openGreenPopup("Sucesso!", data.jsonResponse.message );
					window.location.reload();
				}
			},
			error : function(jqXHR, textStatus, errorThrown ){
				openRedPopup("Aten&ccedil;&atilde;o!", textStatus );
			}
		});
		
	}
	return false;
}

function openMessage(messageID ){
	$.ajax({
		url : 'obter-mensagem',
		data : {
			messageID : messageID
		},
		success : function(data){
			if( data.jsonResponse.code == "ERROR"){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
			}else{
				
				$("#div-label-popup").hide();
				$("#div-content-popup").show();
				$("#div-content-popup").html('');
				
				$("#div-content-popup").append(
					"<strong>Nome: </strong>" + data.jsonResponse.data.senderName +
					"<br><strong>Telefone: </strong>" + data.jsonResponse.data.senderPhone +
					"<br><strong>Email: </strong>" + data.jsonResponse.data.senderEmail +
					"<br><strong>Anuncio: </strong>" + data.jsonResponse.data.adDescription +
					"<br><br>" + data.jsonResponse.data.text
				);
				
				openPopup('Proposta', '');
				
				$(".glyphicon[id='"+messageID+"']").removeClass("glyphicon-envelope");
				$(".glyphicon[id='"+messageID+"']").addClass("glyphicon-ok");
				
			}
		},
		error : function(jqXHR, textStatus, errorThrown ){
			openRedPopup("Aten&ccedil;&atilde;o!", textStatus );
		}
	});
	
	return false;
}

function removeAd( adID ){
	
	var remove = confirm('Deseja realmente remove-lo?');
	if( remove ){
		$.ajax({
			url : 'excluir-anuncio',
			method: 'POST',
			data : {
				adID : adID
			},
			success : function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					openGreenPopup("Sucesso!", data.jsonResponse.message );
					window.location.reload();
				}
			},
			error : function(jqXHR, textStatus, errorThrown ){
				openRedPopup("Aten&ccedil;&atilde;o!", textStatus );
			}
		});
		
	}
	return false;
}


function changePassword(){
	
	var oldPassword = $("#old-password").val();
	var newPassword = $("#new-password").val();
	var newRePassword = $("#new-repassword").val();
	
	if( newPassword != newRePassword ){
		openRedPopup("Aten&ccedil;&atilde;o: ", "Senha e confirma&ccedil;&atilde;o de senha informados n&atilde;o conferem!");
	}else if( oldPassword.length < 1 ){
		openRedPopup("Aten&ccedil;&atilde;o: ", "Senha atual &eacute; obrigat&oacute;ria!");
	}else if( newPassword.length < 1 ){
		openRedPopup("Aten&ccedil;&atilde;o: ", "Nova senha &eacute; obrigat&oacute;ria!");
	}else{
		
		$("#change-password-button").html('trocando...');
		
		$.ajax({
			url : 'trocar-senha',
			method : 'POST',
			data: {
				password: oldPassword,
				newPassword: newPassword
			},
			success: function(data){
				if( data.jsonResponse.code == "ERROR"){
					openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
				}else{
					openGreenPopup("Sucesso!", data.jsonResponse.message );
				}
				$("#change-password-button").html('trocar');
			},
			error : function(jqXHR, textStatus, errorThrown ){
				openRedPopup("Aten&ccedil;&atilde;o!", textStatus );
				$("#change-password-button").html('trocar');
			}
		});
		
	}
	
	return false;
}


function getAd( adID ){
	$.ajax({
		url: '../anuncios/detalhes-do-anuncio-json',
		data:{
			'adID' : adID
		},
		success: function(data){
			loadAdToEdit(data);
		},
		error : function(jqXHR, textStatus, errorThrown ){
			openRedPopup("Aten&ccedil;&atilde;o!", textStatus );
			$("#change-password-button").html('trocar');
		}
	});
}

function loadAdToEdit( data ){
	var notInputProperties = ["TYPE", "GROUP", "CATEGORY", "BRAND", "MODEL"];
	var notInputValues = []; 
	
	$(data.jsonResponse.data.adPropertyValues).each(function(prop){
		if( $.inArray( this.adProperty.name, notInputProperties) == -1 ){
			$("[data-prop-name='"+ this.adProperty.name +"']").val( this.value );
		}else{
			notInputValues.push(this.value);
		}
	});
	
	bindEventsToLoadAd(notInputValues);
}

function bindEventsToLoadAd( values ){
	
	$("#group-select").on("optionsChanged", function(){
		if( values.length > 3 ){
			$("#group-select").val(values[0]).change();
			values = values.slice(1, 10);
		}
	});
	$("#category-select").on("optionsChanged", function(){
		if( values.length > 2 ){
			$("#category-select").val(values[0]).change();
			values = values.slice(1, 10);
		}
	});
	$("#brand-select").on("optionsChanged", function(){
		if( values.length > 1 ){
			$("#brand-select").val(values[0]).change();
			values = values.slice(1, 10);
		}
	});
	$("#model-select").on("optionsChanged", function(){
		if( values.length > 0 ){
			$("#model-select").val(values[0]).change();
			values = values.slice(1, 10);
		}
	});
	
	$("#type-select").val(values[0]).change();
	values = values.slice(1, 10);
}

