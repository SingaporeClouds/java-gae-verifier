package com.singpath.jsptest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

import net.sf.jsptest.HtmlTestCase;

/**
 * This class is not thread safe.  Do not attempt to run methods on it concurrently.
 * 
 * @author masotime
 */
public class JSPTester extends HtmlTestCase {
	
	StringBuilder jspBuilder = new StringBuilder();
	
	public JSPTester() throws Exception {	
		super.setUp();
	}
	
	public JSPTester(String code) throws Exception {
		this();
		addCode(code);
	}
	
	public void addCode(String code) {
		jspBuilder.append(code);
	}
	
	public void build() throws Exception {
		
		// have to create a real file in the filesystem :(
		File f = new File("index.jsp");
		f.createNewFile();
		Writer w = new PrintWriter(f);
		w.write(jspBuilder.toString());
		w.close();
		
		// run tests... hardcoded for now, I think we have a problem Houston
		String expectedFruit = "guava";

		setRequestParameter("fruit", "guava");
		request("/index.jsp","GET");
		String expectedDate = new Date().toString();

		page().shouldHaveTitle("Hello Pineapples");
		page().shouldContain("Today is: "+expectedDate);
		page().shouldContain("The request parameter 'fruit' has a value of " + expectedFruit);
		page().shouldContainElement("//TABLE/TR/TD/P/B");
		element("//TABLE/TR/TD/P/B").shouldContain("embedded");
		
		// clean up		
		f.delete();

		super.tearDown(); // important!

	}

}
