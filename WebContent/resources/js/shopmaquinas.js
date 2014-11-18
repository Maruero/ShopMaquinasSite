$(document).ready(function() {
	
	formatDates();
	processError();
	hideNotMiniImageLink();
	
	$(':input').setMask();
	$('input[data-mask]').each(function() {
		  $(this).setMask($(this).data('mask'));
	});
	
});

function processError(){
	var errorMessage = $("#errorMessage").html();
	if( errorMessage.length > 0 ){
		openRedPopup("Ocorreu um erro: ", errorMessage);
	}
}

function register(){
	alert("Em desenvolvimento!");
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
			},
			error: function(data){
				openRedPopup("Aten&ccedil;&atilde;o!", data.jsonResponse.message );
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
 * Upload de imagens
 ==================================================================**/

function sendImage(button){
	
	var imageNum = $(button).attr("data-image-num");
	$("#input-send-image").val( $("input[data-image-num='"+imageNum+"']").val() );
	$("#form-send-image").attr("target", "iframeImage" + imageNum);
	$("#form-send-image").submit();
	
}


function saveAd(){
	if( validate("form-new-ad")){
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
		val = 'R$ ' + val;
	}
	$(elem).val(val);
}
