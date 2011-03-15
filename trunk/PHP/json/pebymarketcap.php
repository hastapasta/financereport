<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	//$query1 = "select max(batch) as mbatch from fact_data where data_set='yahoo_share_price_new'";
	
	$query1 = "select max(batch) as mbatch from fact_data where task_id=10";
	
	$result1 = mysql_query($query1) or die("ERROR: Failed Query of " . $query1);
	
	$tmp = mysql_fetch_array($result1);
	
	$maxbatch = $tmp['mbatch'];
		
	/*$query="select  t1.ticker, round(t3.value,2) as \"Q1:2011\", round(t4.value,2) as \"Q2:2011\", round(t2.value,2) as \"Q3:2011\",  round(t5.value,2) as \"Q4:2011\", round(t1.total_eps,2) as total_eps, t1.shares_outstanding, round(t1.shares_outstanding * t1.total_eps,-4) as earnings, round(t6.value,2) as share_price ".
	"from (SELECT AL1.ticker, SUM ( AL1.value ) as total_eps, AL2.shares_outstanding FROM eps_est_and_act_next_4qs AL1, entities AL2 WHERE (AL2.ticker=AL1.ticker)  GROUP BY AL1.ticker, AL2.shares_outstanding) as t1,".
	"(SELECT AL1.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1 WHERE ((AL1.calquarter=3 AND AL1.calyear=2011))) as t2,".
	"(SELECT AL1.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1 WHERE ((AL1.calquarter=1 AND AL1.calyear=2011))) as t3,".
	"(SELECT AL1.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1 WHERE ((AL1.calquarter=2 AND AL1.calyear=2011))) as t4,".
	"(SELECT AL1.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1 WHERE ((AL1.calquarter=4 AND AL1.calyear=2011))) as t5,".
	"(SELECT entities.ticker, AL1.value, AL1.date_collected FROM fact_data AL1,entities WHERE ((AL1.entity_id=entities.id AND AL1.task_id=10 AND AL1.batch=".$maxbatch."))) as t6".
	" where t1.ticker=t2.ticker AND t1.ticker=t3.ticker AND t1.ticker=t4.ticker AND t1.ticker=t5.ticker AND t1.ticker=t6.ticker ".
	"order by earnings desc limit 30";*/
	
	/*
	 * NOTE - THIS QUERY DOES NOT WORK CORRECTLY IN PHPMYADMIN!!!!!!!!!!!!!!!!!!!!!
	 * I think it's the calculations of the fields that blows up phpmyadmin.
	 * You can get it to work by converting the derived queries into views.
	 */
	
	$query="select  t1.ticker, round(t3.value,2) as \"Q1:2011\", round(t4.value,2) as \"Q2:2011\", round(t2.value,2) as \"Q3:2011\",  round(t5.value,2) as \"Q4:2011\", round(t1.total_eps,2) as total_eps, t1.shares_outstanding, round(t1.shares_outstanding * t1.total_eps,-4) as earnings, round(t6.value,2) as share_price ".
	"from (SELECT AL2.ticker, SUM ( AL1.value ) as total_eps, AL2.shares_outstanding FROM eps_est_and_act_next_4qs AL1, entities AL2 WHERE (AL2.id=AL1.entity_id)  GROUP BY AL2.ticker, AL2.shares_outstanding) as t1,".
	"(SELECT AL2.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1,entities AL2 WHERE ((AL1.calquarter=3 AND AL1.calyear=2011) AND AL2.id=AL1.entity_id)) as t2,".
	"(SELECT AL2.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1,entities AL2 WHERE ((AL1.calquarter=1 AND AL1.calyear=2011) AND AL2.id=AL1.entity_id)) as t3,".
	"(SELECT AL2.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1,entities AL2 WHERE ((AL1.calquarter=2 AND AL1.calyear=2011) AND AL2.id=AL1.entity_id)) as t4,".
	"(SELECT AL2.ticker, AL1.value FROM eps_est_and_act_next_4qs AL1,entities AL2 WHERE ((AL1.calquarter=4 AND AL1.calyear=2011) AND AL2.id=AL1.entity_id)) as t5,".
	"(SELECT AL2.ticker, AL1.value, AL1.date_collected FROM fact_data AL1,entities AL2 WHERE ((AL1.entity_id=AL2.id AND AL1.task_id=10 AND AL1.batch=".$maxbatch."))) as t6".
	" where t1.ticker=t2.ticker AND t1.ticker=t3.ticker AND t1.ticker=t4.ticker AND t1.ticker=t5.ticker AND t1.ticker=t6.ticker ".
	"order by earnings desc limit 30";

	
	
	//$query=$querystronger;
	/*if (isset($_REQUEST['type']) && ($_REQUEST['type'] == 'weaker'))
	{
	
		//$query=$queryweaker;
		$query = str_replace('{order}', 'DESC',$query);
		
	}
	else 
	{
		$query = str_replace('{order}', 'ASC', $query);
	}*/

	$q=mysql_query($query)  or die("ERROR: Failed Query of " . $query1);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
	print(json_encode($output));
	mysql_close();

?>


