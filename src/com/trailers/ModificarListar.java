package com.trailers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.fasterxml.jackson.databind.ObjectMapper;

import clases.DB;
import clases.Trailer;

@WebServlet("/traileres/modificar/listar")
public class ModificarListar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ModificarListar() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(404);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		DB DB = new DB();

		List<Trailer> trailers = DB.scan(Trailer.class, new DynamoDBScanExpression());

		for (int i = 0; i < trailers.size(); i++) {
			trailers.get(i).setEstado(DB.getEstadoTrailer(trailers.get(i).getPatente(),false));
		} // for

		response.setContentType("application/json");
		response.getWriter().print(new ObjectMapper().writeValueAsString(trailers));
		response.getWriter().close();

	}

}
