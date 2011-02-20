<?php
function assertEquals($test,$result){
	if ($test == $result){
		return true;
	}
	else{
		throw new Exception("expected:<".$test."> but was:<".$result.">");
	}
}
$a = $_GET['a'];
$b = $_GET['b'];
try {
    assertEquals($a,$b);
    echo "pass";
} catch (Exception $e) {
    echo $e->getMessage(), "\n";
}
?>