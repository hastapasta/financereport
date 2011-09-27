<html>
<body>

<?php //include("/home/ollie/workspace/DataLoad/log4jError.log"); ?>
<?php 

error_reporting(E_ALL);

$logname = $_GET['log'];
$order = 'back';
if (!empty($_GET['order']))
	$order = $_GET['order'];



// THE URL FROM THE POST AT EE
$url = '/home/ollie/workspace/DataLoad/'.$logname;

echo "Displaying ".$url."\n";



// READ THE WEB PAGE
$txt= file_get_contents($url);


echo "<pre>";


//echo $txt;
$var=explode("\n", $txt);
if (strtoupper($order) == 'FORWARD') {
	foreach ($var as $line=>$data) {
	echo $data;
	echo "\n";
	} 
	
}
else {
	foreach (array_reverse($var) as $line=>$data) {
	echo $data;
	echo "\n";
	} 
}


echo "</pre>";



?>





</body>







</html>