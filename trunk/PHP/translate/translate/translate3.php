<?php

include ("../../site/includes/sitecommon.php");



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
<div id="bodystyle" style="margin: 100px">

<table width="100%"><tr><td style="padding-left:10px;padding-right:10px;">

<p>
<?php 
include ("../../common/functions.php");

db_utility::setDatabase("translate");
db_utility::db_connect();


if (isset($_POST['val1'])) {
	
	$query1 = "insert into fact_data (text_original,text_translate,date_collected,source_id) values ";
	$query1.= "('".str_replace('\'','\\\'',$_POST['val1'])."','".str_replace('\'','\\\'',$_POST['val2'])."',NOW(),".$_POST['sourceval'].")";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	
	echo "New translation was published.<BR>";
	echo "<table>";
	echo "<tr><td>original: </td><td>".$_POST['val1']."</td></tr>";
	echo "<tr><td>translation: </td><td>".$_POST['val2']."</td></tr>";
	
	$query2 = "select name from sources where id=".$_POST['sourceval'];
	$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
	$row2 = mysql_fetch_array($result2);	
	echo "<tr><td>source: </td><td>".$row2['name']."</td></tr>";
	echo "</table>";
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

function cleararea() {
	$("#textarea1").val('');
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

	sourceval = document.getElementById("sourceval");
	sourceval.value = $("#sources").val();

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
<textarea name="textarea1" id="textarea1" rows="5" cols="100">
</textarea>
<BR><BR>
<input type="submit" value="   Submit   " name="submit_msg" >
</form>
<input type="button" style="float: left;clear: both;margin-top: 30px;" value="  Clear  "
	onclick="cleararea();return false;">
<BR><BR>
<input type="button" style="float: left;clear: both;margin-top:30px;" value="  Publish  "
	onclick="publish();return false;">
	
<form name="publishform" action="translate3.php" method=POST>
<input type="hidden" name="val1" id="val1" value="">
<input type="hidden" name="val2" id="val2" value="">
<input type="hidden" name="sourceval" id="sourceval" value="">
<BR><BR>
<div id="selectid" style="float: left; clear: both;margin-top:30px;">
Source: 
<select id="sources" style="background-color: #FFFFFF">

<?php 
	$sql = "select name,id from sources ";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)	{
		$row1 = mysql_fetch_array($result);		
		echo "<option value=".$row1['id'].">".$row1['name']."</option>";
	}	
?>
</select>
</div>
</form>


</div>
</body></html>