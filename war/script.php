<?php
function assertEquals($t,$r){
	if ($t == $r){
		return "true";
	}
	else{
		throw new Exception("expected:<".$r."> but was:<".$t.">");
	}
}
$script = $_POST['solution'];
$test = $_POST['tests'];
if (isset($script)){
	eval($script);
}
try{
	echo eval("return ".$test);
}
catch (Exception $e) {
	echo $e->getMessage();
}
?>