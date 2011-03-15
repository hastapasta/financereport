<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	if (isset($_REQUEST['entityid']))
	{
		$entity_id = $_REQUEST['entityid'];	
		
	}
	else 
	{
		die("No ticker parameter provided.");
	}
	
	if (isset($_REQUEST['taskid']))
	{
		$taskid = $_REQUEST['taskid'];	
		
	}
	else 
	{
		die("No taskid parameter provided.");
	}
	
	$query = "select value, date_collected from fact_data where entity_id=".$entity_id." AND task_id=".$taskid;
	
	
	
	
	$q=mysql_query($query)  or die("ERROR: Failed Query of " . $query1);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
		
	
	
	print(json_encode($output));
	mysql_close();
	
	function sigdigits($input,$digits)
	{
		$val = 10;
		if ($input<1)
			return(round($input,$digits));
		$tmp1 = $input;
		$tmp = ($tmp1 / (pow($val,(floor(log($tmp1,10))))));
		$tmp = round($tmp,$digits-1) * pow($val,(floor(log($tmp1,10))));
		return($tmp);
		
	}
	
	


?>




