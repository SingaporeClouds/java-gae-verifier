package com.singpath.verifier;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class PHPVerifierServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private URLConnectionReader Resultreader = new URLConnectionReader();

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// String script = req.getParameter("script");
		// String tests = req.getParameter("tests");
		String userStr = req.getParameter("jsonrequest");
		String script = null;
		String tests = null;

		try {
			if (userStr != null) {
				JSONObject jsonObj = new JSONObject(userStr);

				script = jsonObj.getString("solution");
				tests = jsonObj.getString("tests");
			} else {
				script = req.getParameter("script");
				tests = req.getParameter("tests");
			}
			resp.getWriter().println(this.parsePHP(script, tests));
		} catch (Exception e) {
			logger.info("error in doPost:" + e);
			HashMap<String, String> em = new HashMap<String, String>();
			em.put("errors", e.toString());
			resp.getWriter().println(new JSONObject(em).toString());
		}
	}

	public String parsePHP(String script, String tests) throws Exception {
		StringBuffer strResult = new StringBuffer();
		String[] testscripts = tests.split("\n");
		boolean solved = true;

		ArrayList<JSONObject> testResults = new ArrayList<JSONObject>();

		for (String testscript : testscripts) {
			System.out.println("Evaluating test " + testscript);

			if (testscript.trim().equals(""))
				continue;
			String a = testscript.substring(testscript.indexOf("(") + 1,
					testscript.indexOf(","));
			String b = testscript.substring(testscript.indexOf(",") + 1,
					testscript.indexOf(")"));
			//System.out.println(a+"..."+b);
			//String result = Resultreader.read("http://localhost:8888/script.php?a=" + a + "&b=" + b);
			String result = Resultreader.read("http://wgx731lotrepls.appspot.com/script.php?a=" + a + "&b=" + b);
			System.out.println(result);
			if (result.equals("pass")) {
				HashMap<String, Object> resulthash = new HashMap<String, Object>();
				// resulthash.put("expected", value);
				// resulthash.put("received", valueincode);
				// resulthash.put("call", key);
				resulthash.put("call", testscript);
				resulthash.put("correct", true);
				testResults.add(new JSONObject(resulthash));
			} else {
				System.out
				.println("Caught an error for test " + testscript);
				HashMap<String, Object> resulthash = new HashMap<String, Object>();
				solved = false;

				String failS = result;
				failS = failS.replace("expected:<", "");
				failS = failS.replace("> but was:<", "MYSPLIT");
				failS = failS.replace(">", "MYSPLIT");
				String[] ss = failS.split("MYSPLIT");
				resulthash.put("expected", ss[0]);
				resulthash.put("received", ss[1]);
				resulthash.put("call", testscript);
				resulthash.put("correct", false);
				testResults.add(new JSONObject(resulthash));

				continue;
			}
			if (testscript.indexOf("assert") == -1)
				continue;
		}
		HashMap resultjson = new HashMap();
		resultjson.put("solved", solved);
		resultjson.put("results", testResults);
		JSONObject json = new JSONObject(resultjson);
		strResult.append(json.toString());

		return strResult.toString();
	}

	public static void print(Object o) {
		System.out.println(o);
	}
}
