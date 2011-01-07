<HTML>
<meta http-equiv="refresh" content="86400;url=data_grab.php?Data_Set=Initial_UE_Claims&insert=1">
<!-- 
hour:3600
day:86400
week:604800 
-->



<?php
/*version 2: allows for multiple passes to accomodate dynamic urls */
/* also added code to handle div tags */

include("../common/functions.php");



$Data_Set = $_GET["Data_Set"];
$bool_insert = $_GET["insert"];


if (substr($Data_Set,0,3) == 'URL')
{
echo "<BR>Here A<BR>";


echo "parameter:" . $Data_Set . "<br>";

$data_value = get_value($Data_Set);
  
  /*Here you need code if this is the final iteration with the actual data value or if 
  this is the dynamic url and another iteration is needed to get the actual data value*/
  
  //Should probably add a rule to check that the data_set is valid 
 $Data_Set = substr($Data_Set,4,strlen($Data_Set) - 4);  
 

  
  
  	$update = "UPDATE extract_info SET url_dynamic='". $data_value ."' where data_set='".$Data_Set."'";
  	
  	echo "<br>" . $update . "<br>";
  
  	if (mysql_query($update))
  		echo "Value updated in db.";  			
  	else
  	 die(mysql_error());
	
 }
  
 
 
 echo "parameter:" . $Data_Set . "<br>";

$data_value = get_value($Data_Set);



$insert = "INSERT INTO fact_data (data_set,value,date_collected) VALUES ('" . $Data_Set . "','" . $data_value . "',NOW())";
 
 echo "<br>" . $insert . "<br>";
  
   if ($bool_insert != 0)
  {
  	if (mysql_query($insert))
  		echo "Value inserted in db.";  			
  	else
  	 die(mysql_error());
  	}
	
	






?>
</HTML>