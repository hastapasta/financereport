<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	$queryweaker="SELECT AL3.value2, AL3.date_collected2, AL1.value, AL1.date_collected, AL2.ticker, AL2.frequency, (AL3.value2 - AL1.value)/AL3.value2 AS changeval FROM fact_data AL1, notify AL2, (SELECT D2AL2.value AS value2, D2AL2.date_collected AS date_collected2, D2AL1.ticker AS ticker from fact_data D2AL2, (select max(batch) mx, ticker from fact_data where data_set like '%xrateorg%' group by ticker) as D2AL1  where data_set like '%xrateorg%' and D2AL2.batch=D2AL1.mx and D2AL1.ticker=D2AL2.ticker) AL3 WHERE (AL1.primary_key=AL2.fact_data_key AND AL3.ticker=AL1.ticker)  AND ((AL1.data_set LIKE '%xrateorg%' AND AL1.ticker LIKE 'USD%' AND AL2.frequency='YEARLY')) ORDER BY  7 DESC limit 20";

	$querystronger="SELECT AL3.value2, AL3.date_collected2, AL1.value, AL1.date_collected, AL2.ticker, AL2.frequency, (AL3.value2 - AL1.value)/AL3.value2 AS changeval FROM fact_data AL1, notify AL2, (SELECT D2AL2.value AS value2, D2AL2.date_collected AS date_collected2, D2AL1.ticker AS ticker from fact_data D2AL2, (select max(batch) mx, ticker from fact_data where data_set like '%xrateorg%' group by ticker) as D2AL1  where data_set like '%xrateorg%' and D2AL2.batch=D2AL1.mx and D2AL1.ticker=D2AL2.ticker) AL3 WHERE (AL1.primary_key=AL2.fact_data_key AND AL3.ticker=AL1.ticker)  AND ((AL1.data_set LIKE '%xrateorg%' AND AL1.ticker LIKE 'USD%' AND AL2.frequency='YEARLY')) ORDER BY  7 ASC limit 20";
	
	$query=$querystronger;
	if (isset($_REQUEST['type']))
	{
		if ($_REQUEST['type'] == 'weaker')
		{
			$query=$queryweaker;
		}
	}

	$q=mysql_query($query);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
	print(json_encode($output));
	mysql_close();

?>


