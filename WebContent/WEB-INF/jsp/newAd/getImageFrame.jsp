<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			.holder .btn{
				display: none;
				position: absolute;
				left: 25px;
				top: 37px;
				font: 17px/18px "akzidenz-grotesk_bq_condensMd", Arial, Helvetica, sans-serif;
				text-transform: uppercase;
				padding: 3px 4px;
				color: #fff;
				background: #9c010d;
				vertical-align: top;
				font-size: 11px;
				font-weight: 700;
				line-height: 12px;
				letter-spacing: 0;
			}
			.holder:hover .btn{
				display: block;
			}
		</style>
	</head>
	<body style="min-width:90px;min-height:77px;">
		
		<c:choose>
			<c:when test="${ limit }">
				<script type="text/javascript">
			
					parent.openRedPopup('Imagem descartada', 'Por favor, verifique o tamanho da imagem.');
					parent.decrementImageSentCount();
			
				</script>
			</c:when>
			<c:otherwise>
				<div class="holder">
					<img src="${path}" width="90" height="77"/>
					<div class="hover-content">
						<form action="remover-imagem" method="post">
							<input type="hidden" name="imageName" value="${path}">
							<button class="btn">Apagar</button>
						</form>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		
		
		
	</body>
</html>