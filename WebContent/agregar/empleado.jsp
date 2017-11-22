<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%
	if (session.getAttribute("rol") == null) {
		response.sendError(400, "Acceso incorrecto"); //cambiar
	}
session.setAttribute("pagina", "Agregar Usuario");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Agregar Empleado</title>

<jsp:include page="/head.jsp" />

</head>
<body  class="fondo">

	<!-- Header  -->
	<div class="container-fluid">
	<jsp:include page="/header.jsp" />
	</div>
	<!--  Container de la Barra de navegacion -->
	<div class="container-fluid">
	<jsp:include page="/navbar.jsp" />
	</div>
		<br>
		<br>
		<div class="container">
			<form id="form" name="form" action="/empleado" method="post"
				class="form-horizontal">

				<%
				//Nombre de los campos del form
				String[] inputs = { "usuario" };
				com.logica.Dibujar.inputs(out, inputs);
				String[] inputs2 = { "clave1", "clave2" };
				com.logica.Dibujar.inputsHidden(out, inputs2);
				String[] inputs3 = { "nombre", "apellidos", "correo", "direccion", "telefono", "rol" };
				com.logica.Dibujar.inputs(out, inputs3);
			%>
				<div class="col-sm-2"></div>

				<button type="submit" name="submit" class="btn btn-primary">Registrar</button>
			</form>

	</div>
		<div class="container"></div>
	<br><br><br><br><br><br><br>

	<jsp:include page="/footer.jsp" />
</body>
</html>