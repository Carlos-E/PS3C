<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="com.logica.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="clases.*"%>
<%@ page import="com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression"%>
<%
	if (session.getAttribute("rol") == null) {
		response.sendRedirect("/login.jsp");
	}
	session.setAttribute("pagina", "Crear envíos");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="es">

<head>
<jsp:include page="/head.jsp" />
<title>
	<%
		out.print(session.getAttribute("pagina").toString());
	%>
</title>
</head>

<body>
	<!-- INICIO -->
	<div class="container-fluid" id="wrapper">
		<div class="row">
			<!-- INICIO NAVBAR -->
			<jsp:include page="/navbar.jsp" />
			<!--  ./NAVBAR -->
		</div>
	</div>
	<%
		DB DB = new DB();
		List<Usuario> listaUsuarios = DB.scan(Usuario.class, new DynamoDBScanExpression());
		List<Usuario> listaClientes = new ArrayList<Usuario>();
		List<Empresa> listaEmpresas = DB.scan(Empresa.class, new DynamoDBScanExpression());
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if (listaUsuarios.get(i).getRol().equals("cliente")) {
				if (!DB.estaOcupado(listaUsuarios.get(i).getNombre(), "null")) {
					listaClientes.add(listaUsuarios.get(i));
				}
			}
		}
	%>
	<main class="col-xs-12 col-sm-8 col-lg-9 col-xl-10 pt-3 pl-4 ml-auto"> <!--  HEADER --> <jsp:include page="/header.jsp" /> <!--  ./HEADER --> <section class="row">
	<div class="col-md-12 col-lg-12">
		<div class="card mb-4">
			<!-- INICIO CONTAINER -->

			<div class="card-block">
				<h3 class="card-title">
					<%
						out.print(session.getAttribute("pagina").toString());
					%>
				</h3>

				<form id="myForm" class="form" action="/envios/crear" method="post">
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize" >Peso(Kg)</label>
						<div class="col-md-4">
							<input class="form-control" pattern="^\s*(?=.*[1-9])\d*(?:\.\d{1,2})?\s*$" name="peso" id="peso" placeholder="en kilogramos" required>
						</div>
						<label class="col-md-2 col-form-label text-capitalize">Espacio(m<sup>3</sup>)</label>
						<div class="col-md-4">
							<input class="form-control" pattern="^\s*(?=.*[1-9])\d*(?:\.\d{1,2})?\s*$" name="espacio" id="espacio" placeholder="en metros c&uacute;bicos" required>
						</div>
					</div>
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Origen</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="origen" placeholder="origen" id="origen" required>
						</div>
						<label class="col-md-2 col-form-label text-capitalize">Destino</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="destino" placeholder="destino" id="destino" required>
						</div>
					</div>
					<div class="form-group row">
						<%
							if (session.getAttribute("rol").equals("admin") || session.getAttribute("rol").equals("empleado")) {
						%>
						<label class="col-md-2 col-form-label text-capitalize">Cliente</label>
						<div class="col-md-4">
							<select class="form-control" name="cliente" id="cliente" required>
								<option value="" selected>Seleccionar...</option>
								<%
									for (int i = 0; i < listaClientes.size(); i++) {
								%>
								<option value="<%out.print(listaClientes.get(i).getUsuario());%>">
									<%
										out.print(listaClientes.get(i).getNombre());
									%>
								</option>
								<%
									}
								%>
							</select>
						</div>
						<%
							}
						%>
						<label class="col-md-2 col-form-label text-capitalize">Empresa</label>
						<div class="col-md-4">
							<select class="form-control" name="empresa" id="empresa" required>
								<option value="" selected>Seleccionar...</option>
								<%
									for (int i = 0; i < listaEmpresas.size(); i++) {
								%>
								<option value="<%out.print(listaEmpresas.get(i).getNit());%>">
									<%
										out.print(listaEmpresas.get(i).getNombre());
									%>
								</option>
								<%
									}
								%>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Tipo</label>
						<div class="col-md-4">
							<select class="custom-select" name="tipo" required>
								<option value="" selected>Seleccionar...</option>
								<option value="normal">normal</option>
								<option value="ligero">ligero</option>
								<option value="fragil">fragil</option>
								<option value="perecedero">perecedero</option>
							</select>
						</div>
						<label class="col-md-2 col-form-label text-capitalize">Descripci&oacute;n</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="descripcion" placeholder="descripci&oacute;n" required>
						</div>						
					</div>
					
					<input type="text" id="destinoLatLong" name="destinoLatLong" style="display: none">
					<input type="text" id="origenLatLong" name="origenLatLong" style="display: none">
					
					<div class="modal-footer">
						<button type="reset" class="btn btn-secondary float-left"><i class="fas fa-eraser fa-lg"></i></button>
						<button id="submit" type="submit" class="btn btn-primary btn-md float-right">Confirmar</button>
						<button type="button" data-toggle="modal" data-target="#myModal" class="btn btn-danger btn-md float-right">Cancelar</button>
					</div>
				</form>
			</div>

			<!-- /FIN CONTAINER -->
		</div>
	</div>
	</section> </main>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Confirmaci&oacute;n</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">Desea cancelar?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
					<form action="/cancelar" method="post">
						<button type="submit" class="btn btn-danger btn-md float-right">Cancelar</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!--  INICIO FOOTER CON SCRIPTS -->
	<jsp:include page="/footer.jsp" />
	<!--  /FIN FOOTER CON SCRIPTS -->
	<!-- /FIN -->

	<script src="/js/calculo-ruta.js"></script>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDsQwNmnSYTDtkrlXKeKnfP0x8TNwVJ2uI&libraries=places&callback=initMap"></script>
	<script type="text/javascript">
		google.maps.event.addDomListener(window, 'load', intilize);
		function intilize() {
			var autocomplete = new google.maps.places.Autocomplete(document
					.getElementById("destino"));
			google.maps.event
					.addListener(
							autocomplete,
							'place_changed',
							function() {
								var place = autocomplete.getPlace();
								var latlon = place.geometry.location.lat()
										+ ',' + place.geometry.location.lng();

								document.getElementById('destinoLatLong').value = latlon;
							});
		};
	</script>
	<script type="text/javascript">
		google.maps.event.addDomListener(window, 'load', intilize);
		function intilize() {
			var autocomplete = new google.maps.places.Autocomplete(document
					.getElementById("origen"));
			google.maps.event
					.addListener(
							autocomplete,
							'place_changed',
							function() {
								var place = autocomplete.getPlace();
								var latlon = place.geometry.location.lat()
										+ "," + place.geometry.location.lng();
								
								document.getElementById('origenLatLong').value = latlon;
							});
		};
	</script>

</body>

</html>