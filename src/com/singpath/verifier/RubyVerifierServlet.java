package com.singpath.verifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

//import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class RubyVerifierServlet extends HttpServlet{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		RubyVerifierServlet instance = new RubyVerifierServlet();
		try
		{
			System.out.println(instance.parseRuby("puts \"Howdy!\"\na=1\nb=2\n", "org.junit.Assert.assertEquals(a,2)\norg.junit.Assert.assertEquals(b,2)"));
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		*/
		ScriptEngine jruby = new ScriptEngineManager().getEngineByName("jruby");
		try
		{
			jruby.eval("require 'test/unit'\nputs \"Howdy!\"\na=1\nputs a\nassert_equal(a,1)");
			//System.out.println(jruby.getBindings(ScriptContext.ENGINE_SCOPE).get("a"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException{
		//String script = req.getParameter("script");
		//String tests = req.getParameter("tests");
		String userStr = req.getParameter("jsonrequest");
		String script = null;
		String tests = null;


		try
		{
			if(userStr != null)
			{
				JSONObject jsonObj = new JSONObject(userStr);

				script = jsonObj.getString("solution");
				tests = jsonObj.getString("tests");
			}
			else
			{
				script = req.getParameter("script");
				tests = req.getParameter("tests");
			}

			resp.getWriter().println(this.parseRuby(script, tests));
		}
		catch(Exception e)
		{
			logger.info("error in doPost:" + e);
			HashMap<String, String> em = new HashMap<String, String>();
			em.put("errors", e.toString());
			resp.getWriter().println(new JSONObject(em).toString());
		}
	}

	public String parseRuby(String script, String tests) throws Exception
	{
		StringBuffer strResult = new StringBuffer();

		ByteArrayOutputStream bufStream;
		PrintStream bufferedOut;

		bufStream = new ByteArrayOutputStream();
		try
		{
			bufferedOut = new PrintStream(bufStream, true, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}

		RubyInstanceConfig config = new RubyInstanceConfig();
		config.setOutput(bufferedOut);
		config.setError(bufferedOut);
		//config.setOutput(System.out);
		//config.setError(System.out);

		Ruby runtime = JavaEmbedUtils.initialize(new ArrayList(), config);
		RubyRuntimeAdapter evaler = JavaEmbedUtils.newRuntimeAdapter();
		//evaler.eval(runtime, "include Java");
		evaler.eval(runtime, script);
		String buf = new String(bufStream.toByteArray(), "UTF-8");
		bufStream.reset();

		String[] testscripts = tests.split("\n");
		boolean solved = true;

		ArrayList<JSONObject> testResults = new ArrayList<JSONObject>();

		for(String testscript : testscripts)
		{
			if(testscript.trim().equals(""))
				continue;
			try
			{
				//evaler.eval(runtime, "include Java\n" + script + "\n" + testscript);
				String theCode = script+"\n\n";
				theCode += "require 'test/unit'\n";
				theCode += "extend Test::Unit::Assertions \n";
				//theCode += "assert_equal(5, 5)\n";
				theCode += testscript+"\n";

				System.out.println("Will execute this code.");
				System.out.println(theCode);

				evaler.eval(runtime, theCode);

			}
			catch(Throwable e)
			{
				System.out.println("Error caught");
								System.out.println(e.getMessage());
				HashMap<String, Object> resulthash = new HashMap<String, Object>();
				solved = false;


				//String failS = e.getMessage();
				//failS = failS.substring(failS.indexOf("expected"));
				//failS = failS.substring(0, failS.indexOf(";"));
				//failS = failS.replace("expected:<", "");
				//failS = failS.replace("> but was:<", ",");
				//failS = failS.replace(">", "");
				//String[] ss = failS.split(",");
				resulthash.put("expected", "");
				//resulthash.put("received", ss[1]);
				resulthash.put("received", e.getMessage());
								resulthash.put("call", testscript);
				resulthash.put("correct", false);
				testResults.add(new JSONObject(resulthash));

				continue;
			}
			System.out.println("No error thrown for "+testscript);
			HashMap<String, Object> resulthash = new HashMap<String, Object>();
			//resulthash.put("expected", value);
			//resulthash.put("received", valueincode);
			//resulthash.put("call", key);
			resulthash.put("call", testscript);
			resulthash.put("correct", true);
			testResults.add(new JSONObject(resulthash));
		}
		HashMap resultjson = new HashMap();
		resultjson.put("solved", solved);
		resultjson.put("results", testResults);
		resultjson.put("printed", buf);
		JSONObject json = new JSONObject(resultjson);
		strResult.append(json.toString());


		return strResult.toString();
	}

}
