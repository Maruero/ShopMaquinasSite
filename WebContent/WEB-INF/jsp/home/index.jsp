<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Shop Maquinas</title>
		</head>
	<body>
		<c:forEach var="ad" items="${ads}">
			<c:forEach var="prop" items="${ad.adPropertyValues}">
				${prop.value}
				<br/>
			</c:forEach>
			--------------------------------------------------------------
		</c:forEach>
	</body>
</html>