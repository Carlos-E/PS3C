<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="com.logica.*"%>
<%@ page import="clases.*"%>
<%
	if (session.getAttribute("rol") == null) {
		//response.sendError(400, "Acceso incorrecto"); //cambiar
		response.sendRedirect("/error.jsp");
	}
	session.setAttribute("pagina", "Modificar Trailer");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>
	<%
		out.print(session.getAttribute("pagina").toString());
	%>
</title>
<jsp:include page="/head.jsp" />
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
		ArrayList<Usuario> listaUsuarios = ControladorBD.escanearTabla("usuarios");
		ArrayList<Vehiculo> listaVehiculos = ControladorBD.escanearTabla("vehiculos");
		ArrayList<Usuario> listaConductor = new ArrayList<Usuario>();
		ArrayList<Empresa> listaEmpresas = ControladorBD.escanearTabla("empresas");
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if (listaUsuarios.get(i).getRol().equals("conductor")) {
				if (!ControladorBD.estaOcupado(listaUsuarios.get(i).getNombre(), "null")) {
					listaConductor.add(listaUsuarios.get(i));
				}
			}
		}
	%>
	<main class="col-xs-12 col-sm-8 col-lg-9 col-xl-10 pt-3 pl-4 ml-auto"> <!--  HEADER --> <jsp:include page="/header.jsp" /> <!--  ./HEADER --> <section class="row">
	<div class="col-md-12 col-lg-12">
		<div class="card mb-4">
			<!-- INICIO CONTAINER -->

			<div class="card-block" id="buscar-form">
				<h3 class="card-title">
					<%
						out.print(session.getAttribute("pagina").toString());
					%>
				</h3>
				<form class="form" name="form" method="post">
					<div class="form-group row">
						<label class="col-md-3 col-form-label">Seleccione el trailer</label>
						<div class="col-md-9">
							<select class="custom-select form-control" id="select">
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button id="buscar" type="button" class="btn btn-primary btn-md float-right">Buscar</button>
						<button type="button" data-toggle="modal" data-target="#myModal" class="btn btn-danger btn-md float-right">Cancelar</button>
					</div>
				</form>
			</div>

			<div class="card-block" id="form" hidden="true">
				<h3 class="card-title">
					<%
						out.print(session.getAttribute("pagina").toString());
					%>
				</h3>
				<form id="form2" class="form" action="/traileres/modificar" method="post">
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Patente</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="patente" placeholder="patente" id="patente" readonly>
						</div>

					</div>
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Estado</label>
						<div class="col-md-4">
							<select class="custom-select" name="estado" id="estado" required>
								<option value="" selected>seleccionar...</option>
								<option value="disponible">disponible</option>
								<option value="asignado">asignado</option>
							</select>
						</div>
						<label class="col-md-2 col-form-label text-capitalize">Tipo</label>
						<div class="col-md-4">
							<select class="custom-select" name="tipo" id="tipo" required>
								<option value="" selected>seleccionar...</option>
								<option value="rabon (1 eje)">rabon (1 eje)</option>
								<option value="torton (2 ejes)">torton (2 ejes)</option>
								<option value="caja cerrada de 53 pies">caja cerrada de 53 pies</option>
								<option value="caja cerrada de 48 pies">caja cerrada de 48 pies</option>
								<option value="full / doble semiremolque">full / doble semiremolque</option>
								<option value="caja refrigerada">caja refrigerada</option>
								<option value="plataforma">plataforma</option>
								<option value="autotanque / pipa">autotanque / pipa</option>
								<option value="autotanque para asfalto / granel">autotanque para asfalto / granel</option>
								<option value="jaula a granel / granelera">jaula a granel / granelera</option>
								<option value="jaula ganadera">jaula ganadera</option>
								<option value="jaula enlonada / cortina">jaula enlonada / cortina</option>
								<option value="low boy / cama baja">low boy / cama baja</option>
								<option value="madrina / porta vehiculos">madrina / porta vehículos</option>
								<option value="tolva">tolva</option>
							</select>
						</div>
					</div>

					
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Empresa</label>
						<div class="col-md-4">
							<!-- 							<input class="form-control" type="text" name="empresa" placeholder="empresa" id="empresa" required>
 -->
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
						<label class="col-md-2 col-form-label text-capitalize">Cami&oacute;n</label>
						<div class="col-md-4">
							<!-- 							<input class="form-control" type="text" name="vehiculo" placeholder="vehiculo" id="vehiculo" required>
 -->
							<select class="form-control" name="vehiculo" id="vehiculo" required>
															<option value="" selected>Seleccionar...</option>
															<option value="ninguno" >ninguno</option>
							
								<%
									for (int i = 0; i < listaVehiculos.size(); i++) {
										if (!ControladorBD.estaOcupado("null", listaVehiculos.get(i).getPlaca())
												&& listaVehiculos.get(i).getTipo().equals("remolque")) {
											ControladorBD.actualizarValor("vehiculos", "placa", listaVehiculos.get(i).getPlaca(), "estado",
													"Asignado");
											
								%>
								<option value="<%out.print(listaVehiculos.get(i).getPlaca());%>">
									<%
										out.print(listaVehiculos.get(i).getPlaca());
									%>
								</option>
								<%
									}
									}
								%>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<label class="col-md-2 col-form-label text-capitalize">Peso maximo</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="peso" placeholder="peso" id="peso" required>
						</div>
						<label class="col-md-2 col-form-label text-capitalize">Espacio</label>
						<div class="col-md-4">
							<input class="form-control" type="text" name="espacio" placeholder="espacio" id="espacio" required>
						</div>
					</div>
					<div class="modal-footer">
						<button id="submit" type="submit" class="btn btn-primary btn-md float-right">Modificar</button>
						<button id="atras" type="button" data-target="#" class="btn btn-danger btn-md float-right">Atras</button>
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
					<form name="form" action="/cancelar" method="post">
						<button type="submit" class="btn btn-danger btn-md float-right">Cancelar</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--  FOOTER CON SCRIPTS -->
	<jsp:include page="/footer.jsp" />
	<!-- /FIN -->
	<script>
		$(document).ready(
				function() {
					var lista;
					$.ajax({
						url : "/scanTable",
						data : {
							tabla : 'trailers'
						},
						type : "POST",
						dataType : "json",
					}).done(
							function(response) {
								console.log(response);
								lista = response;
								$(response).each(
										function() {
											let value = this.patente;
											let text = this.patente;
											$('#select').append(
													$("<option>").attr('value',
															value).text(text));
										});
							}).fail(function(xhr, status, errorThrown) {
						alert("Algo ha salido mal");
						console.log('Failed Request To Servlet /scanTable')
					}).always(function(xhr, status) {
					});
					$('#buscar').click(function() {
						let selectedIndex = $('#select').prop('selectedIndex');
						console.log(lista[selectedIndex]);
						let objeto = lista[selectedIndex];
						$('#patente').val(objeto.patente);
						$('#tipo').val(objeto.tipo);
						$('#estado').val(objeto.estado);
						$('#peso').val(objeto.peso);
						$('#espacio').val(objeto.espacio);
						$('#empresa').val(objeto.empresa);
						$('#vehiculo').val(objeto.vehiculo);
						$('#buscar-form').hide();
						$('#form').removeAttr('hidden');
						$('#form').show();
					});
					$('#atras').click(function() {
						$('#buscar-form').show();
						$('#form').hide();
					});
				});
	</script>
	
</body>
</html>