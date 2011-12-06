<?php

include ("../site/includes/sitecommon.php");

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en"  dir=ltr>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<?php IncFunc::jQuery();?>


<script>
$(document).ready(function() {
<?php 
$text = "";
if(isset($_POST['submit_msg'])) {
	$text = $_POST['textarea1'];
	echo " $(\"#textarea1\").val(\"".$text."\");";
}




?>

});
</script>
</head>

<body style="font-family:arial;font-size:10pt">

<table width="100%"><tr><td style="padding-left:10px;padding-right:10px;">

<p>
<?php 
include ("../common/functions.php");
if (isset($_POST['val1'])) {
	db_utility::setDatabase("translate");
	db_utility::db_connect();
	$query1 = "insert into fact_data (text_original,text_translate,date_collected) values ";
	$query1.= "('".str_replace('\'','\\\'',$_POST['val1'])."','".str_replace('\'','\\\'',$_POST['val2'])."',NOW())";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	echo "New translation was published.<BR>";
	echo "val1: ".$_POST['val1']."<BR>";
	echo "val2: ".$_POST['val2']."<BR>";
}



?>

<p>
<p>
<div class="goog-trans-section" lang="es">
<div  class="goog-trans-control"></div>
<div id="translated">
<?php echo $text; ?>
</div>
</div>
</td></tr></table>

<script>
<?php echo "var originalText='".$text."';"; ?>
function googleSectionalElementInit() {
  new google.translate.SectionalElement({
    sectionalNodeClassName: 'goog-trans-section',
    controlNodeClassName: 'goog-trans-control',
    background: '#f4fa58'
  }, 'google_sectional_element');
}

function publish() {
	val1 = document.getElementById("val1");

	originalText = originalText.replace(/(\r\n|\n|\r)/gm,"");
	val1.value = originalText;
	//alert(val1.value);
	

	val2 = document.getElementById("val2");
	transval = document.getElementById("translated");
	
	tmptext = strip(transval.innerHTML);
	tmptext = tmptext.replace(/(\r\n|\n|\r)/gm,"");
	//alert(tmptext);
	val2.value = tmptext;

	//alert("submitting");
	document.forms["publishform"].submit();
	
	
}

function strip(html) {
   var tmp = document.createElement("DIV");
   tmp.innerHTML = html;
   return tmp.textContent||tmp.innerText;
}


</script>
<script src="//translate.google.com/translate_a/element.js?cb=googleSectionalElementInit&ug=section&hl=en"></script>


	
<form action="translate3.php" method=POST > 
<textarea name="textarea1" id="textarea1" rows="5" cols="50">
</textarea>
<input type="submit" value="Submit" name="submit_msg" >
</form>


<input type="button" style="float: left;clear: both;color: #000000;background-color: #FFFFFF" value="Publish"
	onclick="publish();return false;">
	
<form name="publishform" action="translate3.php" method=POST>
<input type="hidden" name="val1" id="val1" value="">
<input type="hidden" name="val2" id="val2" value="">
</form>



</body></html>