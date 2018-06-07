package com.modificar;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.logica.ControladorBD;

import clases.Camion;
import clases.Usuario;

/**
 * Servlet implementation class modificarCamion
 */
@WebServlet("/modificarCamion")
public class modificarCamion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Camion camion = new Camion();
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modificarCamion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();	
		String placa = session.getAttribute("obj").toString();
		camion = (clases.Camion) ControladorBD.getItem("camiones", "camion", placa);
		String capacidad = request.getParameter("capacidad").toLowerCase();
		String espacio = request.getParameter("espacio").toLowerCase();
		String estado = request.getParameter("estado").toLowerCase();
		String conductor = request.getParameter("conductor").toLowerCase();
		boolean cambio = false;
		if (!camion.getCapacidad().equals(capacidad)) {
			camion.setCapacidad(capacidad);
			ControladorBD.actualizarValor("camiones", "placa", placa, "capacidad", capacidad);
			cambio = true;
		}
		if (!camion.getEspacio().equals(espacio)) {
			camion.setEspacio(espacio);
			ControladorBD.actualizarValor("camiones", "placa", placa, "espacio", espacio);
			cambio = true;
		}
		if (!camion.getEstado().equals(estado)) {
			camion.setEstado(estado);
			ControladorBD.actualizarValor("camiones", "placa", placa, "estado", estado);
			cambio = true;
		}
		if (!camion.getUsuario().equals(conductor)) {
			camion.setUsuario(conductor);
			ControladorBD.actualizarValor("camiones", "placa", placa, "conductor", conductor);
			cambio = true;
		}
		if (cambio) {
			session.setAttribute("busca", "ninguno");
			response.setContentType("text/html");

			com.logica.Dibujar.mensaje(response.getWriter(), "Operacion Exitosa", request.getSession().getAttribute("origin").toString());
		} else {
			System.out.println("no se cambio nada");
			response.setContentType("text/html");

			com.logica.Dibujar.mensaje(response.getWriter(), "No ha habido cambio", request.getSession().getAttribute("origin").toString());

		}		
	}

}
