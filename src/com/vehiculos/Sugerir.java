package com.vehiculos;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import clases.DB;
import clases.Vehiculo;

@WebServlet("/vehiculos/sugerir/carlos")
public class Sugerir extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Sugerir() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		DB DB = new DB();

		double pesoEnvio = Double.valueOf(request.getParameter("pesoEnvio"));
		double espacioEnvio = Double.valueOf(request.getParameter("espacioEnvio"));
		double relacion = 0.0;

		System.out.println("pesoEnvio: " + pesoEnvio);
		System.out.println("espacioEnvio: " + espacioEnvio);

		List<Vehiculo> lista = new ArrayList<Vehiculo>(DB.scan(Vehiculo.class, new DynamoDBScanExpression()));
		Iterator<Vehiculo> iterator;

		iterator = lista.iterator();
		while (iterator.hasNext()) {
			Vehiculo objeto = iterator.next();

			double pesoOcupadoVehiculo = DB.getPesoVehiculo(objeto.getPlaca());
			double espacioOcupadoVehiculo = DB.getEspacioVehiculo(objeto.getPlaca());

			if (objeto.getTipo().equals("remolque")) {
				iterator.remove();
				continue;
			}

			if (objeto.getPesoMax() == pesoOcupadoVehiculo) {
				iterator.remove();
				continue;
			}

			if (objeto.getEspacioMax() == espacioOcupadoVehiculo) {
				iterator.remove();
				continue;
			}

			if (objeto.getEspacioMax() == 0.0 || objeto.getPesoMax() == 0.0) {
				iterator.remove();
				continue;
			}
		}

		// organizacion de la las listad dependiendo el criterio
		if (pesoEnvio < espacioEnvio) {
			lista.sort(Comparator.comparing(Vehiculo::getPesoMax));
			Collections.reverse(lista);
		} else {
			lista.sort(Comparator.comparing(Vehiculo::getEspacioMax));
			Collections.reverse(lista);
		}

		List<Map<String, Object>> seleccionados = new ArrayList<Map<String, Object>>();

		iterator = lista.iterator();
		while (iterator.hasNext()) {
			Vehiculo objeto = iterator.next();

			double pesoOcupadoVehiculo = DB.getPesoVehiculo(objeto.getPlaca());
			double espacioOcupadoVehiculo = DB.getEspacioVehiculo(objeto.getPlaca());

			Map<String, Object> seleccionado = new HashMap<String, Object>();

			seleccionado.put("placa", objeto.getPlaca());
			seleccionado.put("pesoMax", objeto.getPesoMax());
			seleccionado.put("pesoOcupado", pesoOcupadoVehiculo);
			seleccionado.put("pesoDisponible", objeto.getPesoMax() - pesoOcupadoVehiculo);
			seleccionado.put("espacioMax", objeto.getEspacioMax());
			seleccionado.put("espacioOcupado", espacioOcupadoVehiculo);
			seleccionado.put("espacioDisponible", objeto.getEspacioMax() - espacioOcupadoVehiculo);

			seleccionados.add(seleccionado);
		}

		List<HashMap<String, Object>> distribuciones = new ArrayList<HashMap<String, Object>>();

		if (pesoEnvio < espacioEnvio) {

			System.out.println("pesoEnvio < espacioEnvio");

			relacion = espacioEnvio / pesoEnvio;

			System.out.println("Relacion: " + relacion);

			espacioEnvio = pesoEnvio * relacion;

			for (int i = 0; i < seleccionados.size(); i++) {

				HashMap<String, Object> distribucion = new HashMap<String, Object>();

				double espacioDisponible = (Double) seleccionados.get(i).get("pesoDisponible");
				double pesoDisponible = (Double) seleccionados.get(i).get("espacioDisponible");

				if (pesoDisponible >= pesoEnvio) {
					// soporta
					System.out.println(seleccionados.get(i).get("placa") + " soporta peso");

					if (espacioDisponible >= espacioEnvio) {
						// cabe y soporta todo el envio restante
						System.out.println(seleccionados.get(i).get("placa") + " y cabe");

						distribucion.put("placa", seleccionados.get(i).get("placa"));
						distribucion.put("asignarPeso", pesoEnvio);
						distribucion.put("asignarEspacio", espacioEnvio);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - pesoEnvio;
						espacioEnvio = espacioEnvio - espacioEnvio;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);
						break;
					} else {
						// cabe menos y soporta todo
						System.out.println(seleccionados.get(i).get("placa") + " y cabe menos");

						distribucion.put("placa", seleccionados.get(i).get("placa"));
						distribucion.put("asignarPeso", espacioDisponible / relacion);
						distribucion.put("asignarEspacio", espacioDisponible);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - espacioDisponible / relacion;
						espacioEnvio = espacioEnvio - espacioDisponible;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);

					}

				} else {

					// cabe y soporta todo el envio restante
					System.out.println(seleccionados.get(i).get("placa") + " sampale todo");

					distribucion.put("placa", seleccionados.get(i).get("placa"));
					distribucion.put("asignarPeso", pesoDisponible);
					distribucion.put("asignarEspacio", espacioDisponible);

					System.out.println("Peso antes de la operacion: " + pesoEnvio);
					System.out.println("Espacio antes de la operacion: " + espacioEnvio);

					pesoEnvio = pesoEnvio - pesoDisponible;

					espacioEnvio = espacioEnvio - espacioDisponible;

					System.out.println("Peso restante del envio: " + pesoEnvio);
					System.out.println("Espacio restante del envio: " + espacioEnvio);

					distribuciones.add(distribucion);

				}

			}

			System.out.println("Peso restante del envio: " + pesoEnvio);
			System.out.println("Espacio restante del envio: " + espacioEnvio);

			if (pesoEnvio > 0 || espacioEnvio > 0) {
				distribuciones = new ArrayList<HashMap<String, Object>>();
			}

		} else if (pesoEnvio > espacioEnvio) {

			System.out.println("pesoEnvio > espacioEnvio");

			relacion = pesoEnvio / espacioEnvio;

			System.out.println("Relacion: " + relacion);

			pesoEnvio = espacioEnvio * relacion;

			for (int i = 0; i < seleccionados.size(); i++) {

				HashMap<String, Object> distribucion = new HashMap<String, Object>();

				double espacioDisponible = (Double) seleccionados.get(i).get("pesoDisponible");
				double pesoDisponible = (Double) seleccionados.get(i).get("espacioDisponible");

				if (espacioDisponible >= espacioEnvio) {
					// soporta
					System.out.println(seleccionados.get(i).get("placa") + " soporta peso");

					if (pesoDisponible >= pesoEnvio) {
						// cabe y soporta todo el envio restante
						System.out.println(seleccionados.get(i).get("placa") + " y cabe");

						distribucion.put("placa", seleccionados.get(i).get("placa"));
						distribucion.put("asignarPeso", pesoEnvio);
						distribucion.put("asignarEspacio", espacioEnvio);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - pesoEnvio;

						espacioEnvio = espacioEnvio - espacioEnvio;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);
						break;
					} else {
						// cabe menos y soporta todo
						System.out.println(seleccionados.get(i).get("placa") + " y cabe menos");

						distribucion.put("placa", seleccionados.get(i).get("placa"));

						distribucion.put("asignarPeso", pesoDisponible);
						distribucion.put("asignarEspacio", pesoDisponible / relacion);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - pesoDisponible;
						espacioEnvio = espacioEnvio - pesoDisponible / relacion;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);

					}

				} else {

					// cabe y soporta todo el envio restante
					System.out.println(seleccionados.get(i).get("placa") + " sampale todo");

					distribucion.put("placa", seleccionados.get(i).get("placa"));
					distribucion.put("asignarPeso", pesoDisponible);
					distribucion.put("asignarEspacio", espacioDisponible);

					System.out.println("Peso antes de la operacion: " + pesoEnvio);
					System.out.println("Espacio antes de la operacion: " + espacioEnvio);

					pesoEnvio = pesoEnvio - pesoDisponible;

					espacioEnvio = espacioEnvio - espacioDisponible;

					System.out.println("Peso restante del envio: " + pesoEnvio);
					System.out.println("Espacio restante del envio: " + espacioEnvio);

					distribuciones.add(distribucion);

				}

			}

			System.out.println("Peso restante del envio: " + pesoEnvio);
			System.out.println("Espacio restante del envio: " + espacioEnvio);

			if (pesoEnvio > 0 || espacioEnvio > 0) {
				distribuciones = new ArrayList<HashMap<String, Object>>();
			}
		} else if (pesoEnvio == espacioEnvio) {

			System.out.println("pesoEnvio == espacioEnvio");

			relacion = 1;

			System.out.println("Relacion: " + relacion);

			for (int i = 0; i < seleccionados.size(); i++) {

				HashMap<String, Object> distribucion = new HashMap<String, Object>();

				double espacioDisponible = (Double) seleccionados.get(i).get("pesoDisponible");
				double pesoDisponible = (Double) seleccionados.get(i).get("espacioDisponible");

				if (espacioDisponible >= espacioEnvio) {
					// soporta
					System.out.println(seleccionados.get(i).get("placa") + " soporta peso");

					if (pesoDisponible >= pesoEnvio) {
						// cabe y soporta todo el envio restante
						System.out.println(seleccionados.get(i).get("placa") + " y cabe");

						distribucion.put("placa", seleccionados.get(i).get("placa"));
						distribucion.put("asignarPeso", pesoEnvio);
						distribucion.put("asignarEspacio", espacioEnvio);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - pesoEnvio;

						espacioEnvio = espacioEnvio - espacioEnvio;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);
						break;
					} else {
						// cabe menos y soporta todo
						System.out.println(seleccionados.get(i).get("placa") + " y cabe menos");

						distribucion.put("placa", seleccionados.get(i).get("placa"));

						distribucion.put("asignarPeso", pesoDisponible);
						distribucion.put("asignarEspacio", pesoDisponible / relacion);

						System.out.println("Peso antes de la operacion: " + pesoEnvio);
						System.out.println("Espacio antes de la operacion: " + espacioEnvio);

						pesoEnvio = pesoEnvio - pesoDisponible;
						espacioEnvio = espacioEnvio - pesoDisponible / relacion;

						System.out.println("Peso restante del envio: " + pesoEnvio);
						System.out.println("Espacio restante del envio: " + espacioEnvio);

						distribuciones.add(distribucion);

					}

				} else {

					// cabe y soporta todo el envio restante
					System.out.println(seleccionados.get(i).get("placa") + " sampale todo");

					distribucion.put("placa", seleccionados.get(i).get("placa"));
					distribucion.put("asignarPeso", pesoDisponible);
					distribucion.put("asignarEspacio", espacioDisponible);

					System.out.println("Peso antes de la operacion: " + pesoEnvio);
					System.out.println("Espacio antes de la operacion: " + espacioEnvio);

					pesoEnvio = pesoEnvio - pesoDisponible;

					espacioEnvio = espacioEnvio - espacioDisponible;

					System.out.println("Peso restante del envio: " + pesoEnvio);
					System.out.println("Espacio restante del envio: " + espacioEnvio);

					distribuciones.add(distribucion);
				}

			}

			System.out.println("Peso restante del envio: " + pesoEnvio);
			System.out.println("Espacio restante del envio: " + espacioEnvio);

			if (pesoEnvio > 0 || espacioEnvio > 0) {
				distribuciones = new ArrayList<HashMap<String, Object>>();
			}

		}

		DecimalFormat df = new DecimalFormat("#.##");
		// Poner como lo implemento puche
		for (int i = 0; i < distribuciones.size(); i++) {
			distribuciones.get(i).put("id", distribuciones.get(i).get("placa"));
			distribuciones.get(i).remove("placa");
			distribuciones.get(i).put("pesoAAsignar", df.format(Double.valueOf(distribuciones.get(i).get("asignarPeso").toString())));
			distribuciones.get(i).remove("asignarPeso");
			distribuciones.get(i).put("espacioAAsignar",df.format(Double.valueOf(distribuciones.get(i).get("asignarEspacio").toString())));
			distribuciones.get(i).remove("asignarEspacio");
		}

		response.getWriter().print(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writer()
				.writeValueAsString(distribuciones));
		response.getWriter().close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/404.jsp");
	}
}
