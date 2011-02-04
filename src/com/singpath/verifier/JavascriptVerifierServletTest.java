package com.singpath.verifier;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JavascriptVerifierServletTest {

	private final JavascriptVerifierServlet inst = new JavascriptVerifierServlet();

	private static void assertJSONHasKey(String exp_key, String exp_val, String act_str)
		throws JSONException {
		JSONObject json = new JSONObject(act_str);
		assertTrue(json.has(exp_key));
		assertEquals(exp_val, json.getString(exp_key));
	}

	@Test
	public void testParseJsSimple() throws Exception {
		assertJSONHasKey("solved", "true",
			inst.parseJs("", "assert_equal(1,1);"));
		assertJSONHasKey("solved", "true",
			inst.parseJs(
				"function add(a, b) { return a+b; }",
				"assert_equal(add(1,2),3);"));
	}

	@Test
	public void testParseJsWhitespace() throws Exception {
		assertJSONHasKey("solved", "true",
			inst.parseJs(
				"function add(a, b) {\n"
				+ "	return a+b;\n"
				+ "}",
				"assert_equal(add(1,2),3);"));
	}

}
