<%-- 
    Document   : index
    Created on : Sep 13, 2010, 9:43:07 AM
    Author     : scboesch
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>wgx731 Verifier Test</title>
    </head>
    <body>
        <h1>wgx731 Verifier Test</h1>

        This form submits the contents of the text box as the jsonrequest to the verifier. <br>
        The test should be in the format: <br>

        {"solution": solution, "tests": tests, "examples": examples}
        
    <form id ="test_form" name="test_form" action="/javascript" method="post" >
    	Select the language before you do the test.
		<select name="language" id="language" onchange="changeAction()">
		<option value = "/javascript">javascript</option>
        <option value = "/ruby">ruby</option>
        <option value = "/java">java</option>
		</select>
		<script type="text/javascript" Language="JavaScript">
		function changeAction() {
			var dropdown = document.getElementById("language");
    		var actionvalue = dropdown.value;
    		document.getElementById("test_form").action = actionvalue;
		}
		</script>
		<br>

        jsonrequest:<br><textarea name="jsonrequest" rows="10" cols="50">
{"solution": solution, "tests": tests, "examples": examples} 
        </textarea> <br>
        <input onclick="changeAction()" type="submit" value="Submit" />

    </form>
	
	
	<div id = "footer">
	<font color="red">
	Hi, Chris.
	Wish you can see this page, I finally make it working, but actually I am not understanding how it works and how to do test. Can help me with that? Thank you.
	</font>
	</div>
    </body>

</html>