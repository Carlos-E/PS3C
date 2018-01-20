<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%
	if (session.getAttribute("rol") == null) {
		response.sendError(400, "Acceso incorrecto"); //cambiar
	}
%>
<%@ page import="com.logica.*"%>
<%@ page import="clases.*"%>
<%
	usuario usuario = new usuario();
	usuario = (usuario) com.logica.ControladorBD.getItem("usuarios", "usuario",
			session.getAttribute("username").toString());
%>

<nav class="navbar navbar-inverse" role="navigation">
	<div class="container-fluid">

		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-animations">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/index.jsp">
				<span class="glyphicon glyphicon-home"></span>
				Inicio
			</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-animations" data-hover="dropdown" data-animations="pulse fadeInUp pulse fadeInUp">
			<ul class="nav navbar-nav">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Env&iacuteos
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li>
							<a href="/envios/estadoDelEnvio.jsp">Listar</a>
						</li>
						<li>
							<a href="/envios/realizarNuevoEnvio.jsp">Crear</a>
						</li>
						<li>
							<a href="/modificarDatos/mercancia.jsp">Modificar</a>
						</li>

						<li class="divider"></li>
						<li>
							<a href="/eliminar/mercancia.jsp">Eliminar</a>
						</li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Usuarios
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li>
							<a href="/disponibilidad/usuario.jsp">Listar</a>
						</li>
						<li>
							<a href="/agregar/empleado.jsp">Crear</a>
						</li>
						<li>
							<a href="/modificarDatos/usuario.jsp">Modificar</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="/eliminar/usuario.jsp">Eliminar</a>
						</li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Camiones
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li>
							<a href="/disponibilidad/camiones.jsp">Listar</a>
						</li>
						<li>
							<a href="/agregar/camion.jsp">Crear</a>
						</li>
						<li>
							<a href="/modificarDatos/camion.jsp">Modificar</a>
						</li>
						<li class="divider"></li>

						<li>
							<a href="/asignar/destino-camion.jsp">Asignar Ruta</a>
						</li>

						<li class="divider"></li>
						<li>
							<a href="/eliminar/camion.jsp">Eliminar</a>
						</li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Traileres
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li>
							<a href="/disponibilidad/trailers.jsp">Listar</a>
						</li>
						<li>
							<a href="/agregar/trailer.jsp">Crear</a>
						</li>
						<li>
							<a href="/modificarDatos/trailer.jsp">Modificar</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="/asignar/trailer-camion.jsp">Asignar Cami&oacute;n</a>
						</li>

						<li class="divider"></li>
						<li>
							<a href="/eliminar/trailer.jsp">Eliminar</a>
						</li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Empresas
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li>
							<a href="/disponibilidad/empresa.jsp">Listar</a>
						</li>
						<li>
							<a href="/agregar/empresa.jsp">Crear</a>
						</li>
						<li>
							<a href="/modificarDatos/empresa.jsp">Modificar</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="/eliminar/empresa.jsp">Eliminar</a>
						</li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						Gestion
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdownhover-bottom" role="menu" style="">
						<li class="dropdown">
							<a href="#">
								Reportes
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu dropdownhover-right">
								<li>
									<a href="/chequeo/reportarProblema.jsp">Generar</a>
								</li>
								<li>
									<a href="/envios/ultimosReportes.jsp">Listar</a>
								</li>
							</ul>
						</li>
						<li class="divider"></li>
						<li class="dropdown">
							<a href="#">
								Chequeo de mercancia
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu dropdownhover-right">
								<li>
									<a href="/chequeo/chequearCarga.jsp">Chequear Carga</a>
								</li>
								<li>
									<a href="/chequeo/chequearDescarga.jsp">Chequear Descarga</a>
								</li>
							</ul>
						</li>
						<li class="divider"></li>
						<li class="dropdown">
							<a href="#">
								Asignaciones
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu dropdownhover-right">
								<li class="dropdown">
									<a href="#">
										Mercancia
										<span class="caret"></span>
									</a>
									<ul class="dropdown-menu dropdownhover-right">
										<li>
											<a href="/asignar/mercancia-camion.jsp">Cami&oacute;n</a>
										</li>
										<li>
											<a href="/asignar/mercancia-trailer.jsp">Trailer</a>
										</li>
									</ul>
								</li>
								<li class="divider"></li>
								<li>
									<a href="/asignar/destino-camion.jsp">Destino a Cami&oacute;n</a>
								</li>
								<li>
									<a href="/asignar/trailer-camion.jsp">Trailer a Cami&oacute;n</a>
								</li>
							</ul>
						</li>
					</ul>
				</li>

				<!-- MAPA -->
				<li>
					<a href="/chequeo/mapeoDeMercancia.jsp" class="dropdown-toggle">
						<span class="glyphicon glyphicon-globe"></span>
					</a>
				</li>

			</ul>
			<form class="navbar-form navbar-right" action="/logout" method="post">
				<button type="submit" class="btn btn-danger">
					<span class="glyphicon glyphicon-log-out"></span>
					  Cerrar Sesi&oacuten
				</button>

			</form>

		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>

<br>
<br>
