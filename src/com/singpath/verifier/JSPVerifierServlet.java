package com.singpath.verifier;

import java.io.PrintWriter;

import org.json.JSONObject;

import com.singpath.jsptest.JSPTester;

@SuppressWarnings("serial")
public class JSPVerifierServlet extends AbstractVerifierServlet {

	@Override
	protected void handleRequest(JSONObject jsonObj, PrintWriter writer) {
		writer.write("Hello world! The jsonObj is "+jsonObj);
		
		// hardcode
		String jspFile;
		jspFile = "<HTML><HEAD><TITLE>Hello Pineapples</TITLE>";
		jspFile += "</HEAD><BODY><H1>Hello World</H1>";
		jspFile += "<TABLE><TR><TD><P>This is an <B>embedded</B> table</P></TD></TR></TABLE>";
		jspFile += "<P>Today is: <%= new java.util.Date().toString() %></P>";
		jspFile += "<P>The request parameter 'fruit' has a value of <%= request.getParameter(\"fruit\") %></BODY></HTML>";
		
		try {
			JSPTester tester = new JSPTester(jspFile);
			tester.build();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}
