package com.singpath.verifier;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class JavascriptVerifierServletTest {

	private JavascriptVerifierServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private StringWriter response_writer;
	private Map<String, String> parameters;

	@Before
	public void setUp() throws IOException {
		parameters = new HashMap<String, String>();
		servlet = new JavascriptVerifierServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		response_writer = new StringWriter();

		when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				return parameters.get((String) invocation.getArguments()[0]);
			}
		});
		when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
	}

	private static void assertJSONHasKey(String exp_key, String exp_val, String act_str)
		throws JSONException {
		JSONObject json = new JSONObject(act_str);
		assertTrue(json.has(exp_key));
		assertEquals(exp_val, json.getString(exp_key));
	}

	@Test
	public void testParseJsSimple() throws Throwable {
		assertJSONHasKey("solved", "true",
			servlet.parseJs("", "assert_equal(1,1);"));
		assertJSONHasKey("solved", "true",
			servlet.parseJs(
				"function add(a, b) { return a+b; }",
				"assert_equal(add(1,2),3);"));
	}

	@Test
	public void testParseJsWhitespace() throws Throwable {
		assertJSONHasKey("solved", "true",
			servlet.parseJs(
				"function add(a, b) {\n"
				+ "	return a+b;\n"
				+ "}",
				"assert_equal(add(1,2),3);"));
	}

	@Test
	public void testNonExistentFunction() throws Exception {
		parameters.put("script", "");
		parameters.put("tests", "assert_equal(add(1,2),3);");
		servlet.doPost(request, response);
		assertThat(response_writer.toString(),
			containsString("org.mozilla.javascript.EcmaError: ReferenceError: \\\"add\\\" is not defined."));
	}

}
