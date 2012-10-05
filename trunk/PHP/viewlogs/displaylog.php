<html>
<body>

<?php //include("/home/ollie/workspace/DataLoad/log4jError.log"); ?>
<?php 

error_reporting(E_ALL);

$logname = $_GET['log'];
$order = 'back';
if (!empty($_GET['order']))
	$order = $_GET['order'];
	
$display="";
$notdisplay="";
$append="";
if(isset($_GET['submit_msg'])) {
	
	$display=$_GET['display'];
	$notdisplay=$_GET['notdisplay'];
	if (!empty($_GET['appendfiles']))
		$append=$_GET['appendfiles'];
}

//echo "display: ".$display."<BR>";
//echo $_POST['display']."<BR>";

function full_url() {
    $s = empty($_SERVER["HTTPS"]) ? '' : ($_SERVER["HTTPS"] == "on") ? "s" : "";
    $protocol = substr(strtolower($_SERVER["SERVER_PROTOCOL"]), 0, strpos(strtolower($_SERVER["SERVER_PROTOCOL"]), "/")) . $s;
    $port = ($_SERVER["SERVER_PORT"] == "80") ? "" : (":".$_SERVER["SERVER_PORT"]);
    return $protocol . "://" . $_SERVER['SERVER_NAME'] . $port . $_SERVER['REQUEST_URI'];
}

$url = full_url();

//echo 'url:'.$url;


$file = '/home/ollie/workspace/DataLoad/'.$logname;

//echo "Displaying ".$file."\n";

?>
<table>
<form action='<?php echo 'displaylog.php?'.$_SERVER['QUERY_STRING']?>' name='filterform' method='GET'>
<tr><td>Display if Contains: </td><td>
<input type='text' name='display' id='displayid' size='20' maxlength='50' value='<?php echo $display?>'>
</td></tr><tr><td> Don't Display if Contains: </td><td>
<input type='text' name='notdisplay' id='notdisplayid2' size='20' maxlength='50' value='<?php echo $notdisplay?>'>
</td></tr><tr><td>Append Files:
<input type='checkbox' name='appendfiles' <?php echo (empty($append)? '' : 'checked')?>>
</td></tr><tr><td>
<input type='submit' value='filter' name='submit_msg'>
</td></tr>
<input type='hidden' value='<?php echo $logname?>' name='log'>
</form>
</table>

<?php 




// READ THE WEB PAGE


if (!empty($append))
	$extensions = array("",".1",".2",".3");
else
	$extensions = array("");


//WEIRD BEHAVIOR WITH NESTED FOREACH LOOPS!
//foreach ($extensions as &$ext) {
for ($i=0;$i<sizeof($extensions);$i++) {
	echo $file.$extensions[$i]."<BR>";

	$txt= file_get_contents($file.$extensions[$i]);


	echo "<pre>";	
	//echo $txt;
	$var=explode("\n", $txt);
	if (strtoupper($order) == 'FORWARD') {
		//Can't have a foreach nested inside of another loop
		//foreach ($var as $line=>$data) {
		for ($j=0;$j<sizeof($var);$j++) {
			$data = $var[$j];
		
			if (!empty($display))
				if (!(strpos($data,$display,0)))
					continue;
			if (!empty($notdisplay))
				if (strpos($data,$notdisplay,0))
					continue;
			echo htmlspecialchars($data,ENT_QUOTES);
			echo "\n";
		
		} 
		
	}
	else {
		//Can't have a foreach nested inside of another loop.
		//foreach (array_reverse($var) as $line=>$data) {
		for ($k=sizeof($var)-1;$k>=0;$k--) {
			$data = $var[$k];
		
			if (!empty($display))
				if (!(strpos($data,$display,0)))
					continue;
			if (!empty($notdisplay))
				if (strpos($data,$notdisplay,0))
					continue;
			echo htmlspecialchars($data,ENT_QUOTES);
			echo "\n";
	
		} 
	}
	echo "</pre>";


}





?>





</body>







</html>