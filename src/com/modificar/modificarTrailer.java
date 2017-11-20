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

import clases.camion;

/**
 * Servlet implementation class modificarTrailer
 */
@WebServlet("/modificarTrailer")
public class modificarTrailer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	clases.trailer trailer = new clases.trailer();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modificarTrailer() {
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
		String patente = session.getAttribute("obj").toString();
		trailer = (clases.trailer) ControladorBD.getItem("trailers", "trailer", patente);
		String capacidad = request.getParameter("capacidad").toLowerCase();
		String espacio = request.getParameter("espacio").toLowerCase();
		String estado = request.getParameter("estado").toLowerCase();
		String camion = request.getParameter("camion").toLowerCase();
		String tipo = request.getParameter("tipo").toLowerCase();
		boolean cambio = false;
		if (!trailer.getCapacidad().equals(capacidad)) {
			trailer.setCapacidad(capacidad);
			ControladorBD.actualizarValor("trailers", "patente", patente, "capacidad", capacidad);
			cambio = true;
		}
		if (!trailer.getEspacio().equals(espacio)) {
			trailer.setEspacio(espacio);
			ControladorBD.actualizarValor("trailers", "patente", patente, "espacio", espacio);
			cambio = true;
		}
		if (!trailer.getEstado().equals(estado)) {
			trailer.setEstado(estado);
			ControladorBD.actualizarValor("trailers", "patente", patente, "estado", estado);
			cambio = true;
		}
		if (!trailer.getCamion().equals(camion)) {
			trailer.setCamion(camion);
			ControladorBD.actualizarValor("trailers", "patente", patente, "camion", camion);
			cambio = true;
		}
		if (!trailer.getTipo().equals(tipo)) {
			trailer.setTipo(tipo);
			ControladorBD.actualizarValor("trailers", "patente", patente, "tipo", tipo);
			cambio = true;
		}
		if (cambio) {
			session.setAttribute("busca", "ninguno");
			System.out.println("algo se cambio");
			PrintWriter out = response.getWriter();
			String nextURL = request.getContextPath() + "/modificar/trailer.jsp";
			com.logica.Dibujar.mensaje(out, "Operacion Exitosa", nextURL);
			//response.sendRedirect("index.jsp");
		} else {
			System.out.println("no se cambio nada");
		}
	}

}
