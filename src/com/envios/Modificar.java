package com.envios;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import clases.DB;
import clases.Email;
import clases.Envio;
import clases.Trailer;
import clases.Usuario;
import clases.Vehiculo;

@WebServlet("/envios/modificar")
public class Modificar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Modificar() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/404.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		DB DB = new DB();

		// Buscar el objeto en la base de datos e instanciarlo
		Envio envio = new DB().load(Envio.class, request.getParameter("cliente"), request.getParameter("fecha"));
		// Si no encontro nada, soltar mensaje de error y recargar pagina
		if (envio == null) {
			response.setStatus(200);
			response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("title", "Operaci&oacute;n fallida");
					put("message", "Envio no encontrado");
				}
			}));
			return;
		}


		envio.setOrigen(request.getParameter("origen"));
		envio.setDestino(request.getParameter("destino"));
		envio.setOrigenLatLong(request.getParameter("origenLatLong"));
		envio.setDestinoLatLong(request.getParameter("destinoLatLong"));
		envio.setEmpresa(request.getParameter("empresa"));

		envio.setTipo(request.getParameter("tipo"));
		envio.setDescripcion(request.getParameter("descripcion"));

		String placaVehiculo = request.getParameter("camion");
		String patenteTrailer = request.getParameter("trailer");

		// No asignado hasta que se demuestre lo contrario
		if (!((placaVehiculo != null && placaVehiculo.equals(""))
				|| (patenteTrailer != null && patenteTrailer.equals("")))) {
			envio.setCamion("ninguno");
			envio.setTrailer("ninguno");
		}
		envio.setEstado("no asignado");

		if (placaVehiculo != null && !placaVehiculo.equals("ninguno") && !placaVehiculo.equals("")) {
			System.out.println("Se selecciono un Camion");

			Vehiculo vehiculo = DB.load(Vehiculo.class, placaVehiculo.toLowerCase());

			envio.setCamion(vehiculo.getPlaca());
			envio.setEstado("asignado");

		} else if (patenteTrailer != null && !patenteTrailer.equals("ninguno") && !patenteTrailer.equals("")) {
			System.out.println("Se selecciono un Trailer");

			Trailer trailer = DB.load(Trailer.class, patenteTrailer.toLowerCase());

			envio.setCamion(trailer.getCamion());
			envio.setTrailer(trailer.getPatente());
			envio.setEstado("asignado");
		}

		if (envio.getTrailer().equals("ninguno") && !envio.getCamion().equals("ninguno")) {
			
			envio.setEstado("asignado");

			Vehiculo vehiculo = DB.load(Vehiculo.class, envio.getCamion());
			
			if ((vehiculo.getPesoMax() - DB.getPesoVehiculo(vehiculo.getPlaca())+envio.getPeso()) < Double
					.valueOf(request.getParameter("peso"))) {
				response.setStatus(200);
				response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("title", "Operaci&oacute;n fallida");
						put("message", "Peso muy grande para el camion: " + vehiculo.getPlaca());
					}
				}));
				return;
			}

			if (((vehiculo.getEspacioMax() - DB.getEspacioVehiculo(vehiculo.getPlaca())+envio.getEspacio()) < Double
					.valueOf(request.getParameter("espacio")))) {
				response.setStatus(200);
				response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("title", "Operaci&oacute;n fallida");
						put("message", "Espacio muy grande para el camion: " + vehiculo.getPlaca());
					}
				}));
				return;
			}

		} else if (!envio.getTrailer().equals("ninguno")) {
			
			envio.setEstado("asignado");

			Trailer trailer = DB.load(Trailer.class, envio.getTrailer());

			if ((trailer.getPesoMax() - DB.getPesoTrailer(trailer.getPatente())+envio.getPeso()) < Double
					.valueOf(request.getParameter("peso"))) {
				response.setStatus(200);
				response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("title", "Operaci&oacute;n fallida");
						put("message", "Peso muy grande para el trailer: " + trailer.getPatente());
					}
				}));
				return;
			}

			if (((trailer.getEspacioMax() - DB.getEspacioTrailer(trailer.getPatente())+envio.getEspacio()) < Double
					.valueOf(request.getParameter("espacio")))) {
				response.setStatus(200);
				response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("title", "Operaci&oacute;n fallida");
						put("message", "Espacio muy grande para el trailer: " + trailer.getPatente());
					}
				}));
				return;
			}
		}

		envio.setEspacio(Double.valueOf(request.getParameter("espacio")));
		envio.setPeso(Double.valueOf(request.getParameter("peso")));
		
		DB.save(envio);

		if (envio.getEstado().equals("asignado")) {
			new Thread(() -> {
				new Email(DB.load(Usuario.class, envio.getUsuario()).getCorreo(), "PS3C - Envío Asignado",
						"Su envío ha sido asignado correctamente y pronto sera recogido.", envio);
			}).start();
		}

		response.setStatus(200);
		response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("title", "Operaci&oacuten exitosa");
				put("message", "Envío actualizado");
			}
		}));
		return;
	}
}
