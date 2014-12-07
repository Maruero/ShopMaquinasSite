$(document).ready(function() {
	
	formatDates();
	processError();
	hideNotMiniImageLink();
	setMasks();
	
	$(':input').setMask();
	$('input[data-mask]').each(function() {
		  $(this).setMask($(this).data('mask'));
	});
	
	loadOptions();
	
});

function processError(){
	var errorMessage = $("#errorMessage").html();
	if( errorMessage.length > 0 ){
		openRedPopup("Atenção: ", errorMessage);
	}
}

function register(prefixPath){
	
	if(!prefixPath){
		prefixPath = "";
	}
	
	window.location = prefixPath + "contrato/novo-contrato";
	return false;
}

/** ---------------- LOGIN ----------------**/
var loginNextPage = ".";

function openLogin(prefixPath){
	
	if(!prefixPath){
		prefixPath = "";
	}
	
	var username = $("#user-logged").html();
	
	if( username.length < 1){
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
						pathPrefix = $("#pathPrefix").html();
						window.location = pathPrefix;
					}
				}
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
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
				'username' : $("#username").val(),
				'password' : $("#password").val()
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
	}
	return false;
}



/** ---------------- SEND PROPOSAL ----------------**/
function sendProposal(){
	
	if( validate("proposal-group") ){
		
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
				
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
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
	
	openPopup(title, message);
}

function openGreenPopup( title, message ){
	$("#h2-popup").addClass("green");
	$("#h2-popup").removeClass("red");
	
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
		$('#form-new-ad').attr('action', 'novo-anuncio');
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

function showImage(elem){
	var imageSrc = $(elem).attr('src');
	imageSrc = imageSrc.replace('mini-', '');
	$(".image-holder img").attr('src', imageSrc);
	
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
		'id' : '',
		'name' : 'Selecione'
	},
	{
		'id' : '1',
		'name' : 'Ve&iacute;culos'
	},
	{
		'id' : '2',
		'name' : 'M&aacute;quinas'
	},
	{
		'id' : '3',
		'name' : 'Implementos'
	},
	{
		'id' : '4',
		'name' : 'Industriais'
	}
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
	
	$("#type-select").html('');
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
		alert("Cep não encontrado");
	}else{			
		$("#uf").val(cep.uf);
		$("#cidade").val(cep.cidade);
		$("#bairro").val(cep.bairro);
		$("#logradouro").val(cep.tipo_logradouro + ' ' + cep.logradouro);
	}
	
}

