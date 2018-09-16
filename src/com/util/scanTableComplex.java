package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;

@WebServlet("/read/all")
public class scanTableComplex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// CODIGO DE PRUEBA
	// VARIABLES
	private final static String AccessKeyID = "AKIAIE7FDFA4OUA4AEOQ";
	private final static String SecretKey = "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";
	// private final static String AccessKeyID =
	// System.getenv("AWS_ACCESS_KEY_ID") != null ?
	// System.getenv("AWS_ACCESS_KEY_ID") :
	// System.getProperty("AWS_ACCESS_KEY_ID");
	// private final static String SecretKey =
	// System.getenv("AWS_SECRET_ACCESS_KEY") != null ?
	// System.getenv("AWS_SECRET_ACCESS_KEY") :
	// System.getProperty("AWS_SECRET_ACCESS_KEY");
	// VARIABLES

	private static BasicAWSCredentials basicCreds = new BasicAWSCredentials(AccessKeyID, SecretKey);

	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1)
			.withCredentials(new AWSStaticCredentialsProvider(basicCreds)).build();

	DynamoDB dynamoDB = new DynamoDB(client);
	// CODIGO DE PRUEBA

	public scanTableComplex() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		try {

			Table table = dynamoDB.getTable(request.getParameter("table"));
			ItemCollection<ScanOutcome> result = table.scan();
			ArrayList<String> Items = new ArrayList<String>();
			Iterator<Item> iterator = result.iterator();
			while (iterator.hasNext()) {
				Items.add(iterator.next().toJSONPretty().toString());
			}

			response.getWriter().print(Items);
			response.getWriter().close();

		} catch (Exception e) {

			response.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("title", "Operaci&oacute;n fallida");
					put("message", "Ocurrio un error al intentar escanear la tabla: " + request.getParameter("tabla"));
				}
			}));
			return;
		}

	}

}
