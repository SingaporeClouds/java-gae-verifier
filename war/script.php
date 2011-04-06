<?php
error_reporting(E_ALL);
require_once 'JSON.php';
//TODO: code clean up, add class support.
function assert_equal($t,$r){
	if ($t === $r){
		return "true";
	}
	else{
		throw new Exception("expected:<".$r."> but was:<".$t.">");
	}
}
function getTestCase(){
	global $json,$examples,$solution,$tests;
	if (isset($_POST['jsonrequest'])){
		$code = $_POST['jsonrequest'];
		if (get_magic_quotes_gpc()) $code = stripslashes($code);
		$value = $json->decode($code);
		$examples = $value['examples'];
		$solution = $value['solution'];
		$tests = $value['tests'];
	}
	else{
		$solution = $_POST['solution'];
		if (get_magic_quotes_gpc()) $solution = stripslashes($solution);
		$examples = $_POST['examples'];
		if (get_magic_quotes_gpc()) $examples = stripslashes($examples);
		$tests = $_POST['tests'];
	}	
}
function executeTestCase(){
	global $examples,$solution,$tests;
	$json = new Services_JSON();
	if (isset($solution)){
		eval($solution);
	}
	$testArray = explode("\n",$tests);
	$solved = true;
	$testResults = array();
	$count = 0;
	foreach ($testArray as $test){
		if (trim($test) == "" ) continue;
		try{
			eval("return ".$test);
			if (stristr($test, 'assert') === FALSE) continue;
		}
		catch (Exception $e) {
			$error = $e->getMessage();
			$testResult = array();
			$solved = false;
			$pattern = '|<([^>]+)>|U';
			preg_match_all($pattern, $error, $matches,PREG_SET_ORDER);
			if (count($matches)<2){
				$testResult['expected'] = $matches[0][1];
				$testResult['received'] = null;
				$testResult['call'] = $test;
				$testResult['correct'] = false;
			}
			else{
				$testResult['expected'] = $matches[0][1];
				$testResult['received'] = $matches[1][1];
				$testResult['call'] = $test;
				$testResult['correct'] = false;
			}
			$testResults[$count] = $testResult;
			$count ++;
			error_log("Caught an error for test ".$test."\r\n"."$error");
			continue;
		}
		$testResult = array();
		$testResult['call'] = $test;
		$testResult['correct'] = true;
		$testResults[$count] = $testResult;
		$count ++;	
	}
	$outputResult = array('results' => $testResults, 'solved' => $solved);
	return $json->encode($outputResult);		
}
$json = new Services_JSON(16);
$examples = null;
$tests = null;
$solution = null;
getTestCase();
if (!isset($solution) && !isset($tests)){
	 error_log("Error in Post data!",0);
}
else{
	$output = executeTestCase();
	echo $output;
}
?>