<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	$rows=10;
	if (isset($_REQUEST['rows']))
	{
	
		//$query=$queryweaker;
		$rows=$_REQUEST['rows'];
		
	}
	
	$frequency="YEARLY";
	if (isset($_REQUEST['freq']))
	{
		$frequency=$_REQUEST['freq'];
	}
	
	
		
	/*$query = "SELECT AL3.value2, AL3.date_collected2, AL1.value, AL1.date_collected, AL2.ticker, AL2.frequency, ".
	"(AL3.value2 - AL1.value)/AL1.value2 AS changeval FROM fact_data AL1, notify AL2, ".
	"(SELECT D2AL2.value AS value2, D2AL2.date_collected AS date_collected2, D2AL1.ticker AS ticker from fact_data D2AL2, ". 
	"(select max(batch) mx, ticker from fact_data where data_set like '%xrateorg%' group by ticker) as D2AL1  ".
	"where data_set like '%xrateorg%' and D2AL2.batch=D2AL1.mx and D2AL1.ticker=D2AL2.ticker) AL3 ".
	"WHERE (AL1.primary_key=AL2.fact_data_key AND AL3.ticker=AL1.ticker)  AND ".
	"(AL1.data_set LIKE '%xrateorg%' AND AL1.ticker LIKE 'USD%' AND AL2.frequency='".$frequency."' AND AL2.email='opike@yahoo.com') ORDER BY  7 {order} limit ".$rows;*/
	
	$query = "SELECT AL3.value2, AL3.date_collected2, AL1.value, AL1.date_collected, AL4.ticker, AL2.frequency, ".
	"(AL3.value2 - AL1.value)/AL1.value AS changeval FROM fact_data AL1, alerts AL2,entities AL4, ".
	"(SELECT D2AL2.value AS value2, D2AL2.date_collected AS date_collected2, D2AL2.entity_id from fact_data D2AL2, ". 
	"(select max(batch) mx from fact_data where fact_data.task_id=1) as D2AL1  ".
	"where task_id=1 and D2AL2.batch=D2AL1.mx) AL3 ".
	"WHERE (AL1.id=AL2.fact_data_key AND AL3.entity_id=AL1.entity_id AND AL1.entity_id=AL4.id  AND ".
	"AL1.task_id=1 AND AL4.ticker LIKE 'USD%' AND AL2.frequency='".$frequency."' AND AL2.user_id=4) ORDER BY  7 {order} limit ".$rows;
	

	
	
	//$query=$querystronger;
	if (isset($_REQUEST['type']) && ($_REQUEST['type'] == 'weaker'))
	{
	
		//$query=$queryweaker;
		$query = str_replace('{order}', 'DESC',$query);
		
	}
	else 
	{
		$query = str_replace('{order}', 'ASC', $query);
	}

	$q=mysql_query($query);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
		
	for ($i=0;$i<sizeof($output);$i++)
	{
		
		$output[$i]['value2'] = sigdigits($output[$i]['value2'],3);
		$output[$i]['value'] = sigdigits($output[$i]['value'],3);
		$output[$i]['changeval'] = sigdigits($output[$i]['changeval']*100,3)."%";
		
	}
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


