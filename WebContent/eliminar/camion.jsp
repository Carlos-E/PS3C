<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%
	if (session.getAttribute("rol") == null) {
		response.sendError(400, "Acceso incorrecto"); //cambiar
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Modificar Camion</title>
<% session.setAttribute("pagina", "Eliminar Camion"); %>
<jsp:include page="/head.jsp" />

</head>
<body class="fondo">
		<!-- Header -->
		<div class="container-fluid">
			<jsp:include page="/header.jsp" />
		</div>

		<!--  Barra de navegacion -->
		<div class="container-fluid">
			<jsp:include page="/navbar.jsp" />
		</div>
	<br><br>
		<div class="container">
			<%@ page import="com.logica.*"%>
			<%@ page import="clases.*"%>
			<%@ page import="com.logica.*"%>
			<%@ page import="java.util.ArrayList"%>
			<%
				ArrayList<camion> listaCamion = ControladorBD.escanearTabla("camiones");
			%>
			<%
				if (session.getAttribute("busca") != "camion") {
			%>
			<form id="form" name="form" action="/buscar" method="post"
				class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-sm-2" for="camiones">
						Camiones </label>
					<div class="col-sm-9">
						<select class="form-control" id="subject" name="placaE"
							tabindex="4">
							<%
								for (int i = 0; i < listaCamion.size(); i++) {
							%>
							<option value="<%out.print(listaCamion.get(i).getPlaca());%>">
								<%
									out.print(listaCamion.get(i).getPlaca());
								%>
							</option>
							<%
								}
							%>
						</select>
					</div>
				</div>
				<!--          
            <input name="placa" id="placa" type="text" placeholder="Placa del camion">
            -->
				<div class="col-sm-2"></div>
				<button type="submit" name="submit" class="btn btn-primary">Buscar</button>
				<!-- 
             <input name="submit" id="buscar" type="submit" value="Buscar">                 
             -->
			</form>

			<%
				} else {
					camion camion = new camion();
					camion = (camion) com.logica.ControladorBD.getItem("camiones", "placa",
							session.getAttribute("obj").toString());
					%>
					<form id="form" name="form" action="/eliminarCamion" method="post">
						<div class="form-horizontal">
							<%
					if (camion.getTipo().equals("camion")) {
			
						//Nombre de los campos del form
								String[] inputs = { "capacidad", "espacio" };
								String[] values = { camion.getCapacidad(), camion.getEspacio() };
								com.logica.Dibujar.inputs(out, inputs, values);
					%>
					<%
						}
					%>
					<%
						//Nombre de los campos del form
							String[] inputs = { "estado", "conductor" };
							String[] values = { camion.getEstado(), camion.getUsuario() };
							com.logica.Dibujar.inputs(out, inputs, values);
					%>
				</div>
				<div class="col-sm-2"></div>
				<div class="form-vertical">
					<button name="submit" id="submit" type="submit"
						class="btn btn-primary">Eliminar</button>
						<button name="submit" id="submit" type="submit" class="btn btn-danger" formaction="/cancelar">Cancelar</button>
				</div>
			</form>
			<div class="col-sm-2"></div>
			<%
				}
			%>
	</div><br>
		<div class="container-fluid">
	<jsp:include page="/footer.jsp" />
	</div>
</body>
</html>