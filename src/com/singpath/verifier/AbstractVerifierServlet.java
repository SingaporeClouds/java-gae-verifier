package com.singpath.verifier;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public abstract class AbstractVerifierServlet extends HttpServlet {
	
	Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// make it do the same thing as doGet
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userStr = req.getParameter("jsonrequest");
		PrintWriter writer = resp.getWriter();
		logger.info("userStr:" + userStr);

		// convert the request parameter userStr into a manipulatable JSON object
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(userStr);			
		} catch (JSONException e) {
			logErrors(e, writer, "converting userStr to JSON");
			return;
		}
		
		// process the request
		try {
			handleRequest(jsonObj, writer);
		} catch (Exception ex) {
			logErrors(ex, writer, "handling request");
		}
	}
	
	private void logErrors(Exception ex, PrintWriter writer, String context) {
		HashMap<String, String> em = new HashMap<String, String>(); // error messages
		em.put("errors", ex.toString());
		
		logger.info("error in "+context+": "+ ex);
		writer.println(new JSONObject(em).toString());
	}
	
	protected abstract void handleRequest(JSONObject jsonObj, PrintWriter writer) throws Exception;

}
