<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	if (isset($_REQUEST['userid']))
	{
		$user_id = $_REQUEST['userid'];	
		
	}
	else 
	{
		die("No ticker parameter provided.");
	}
	
	
	
	$query = "select tasks.name,entities.ticker,users.username from alerts,schedules,entities,users,tasks";
	$query .= " where tasks.id=schedules.task_id AND alerts.schedule_id=schedules.id AND alerts.entity_id=entities.id AND alerts.user_id=users.id AND alerts.user_id=".$user_id;
	$query .= " limit 30";
	
	
	$q=mysql_query($query)  or die("ERROR: Failed Query of " . $query1);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
		
	
	
	print("google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:".json_encode($output)."});");
	mysql_close();
	
	


?>




