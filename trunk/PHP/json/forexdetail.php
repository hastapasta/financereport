<?php

	mysql_connect("localhost","root","madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	//$query="SELECT * FROM company WHERE ticker='".$_REQUEST['ticker']."'";
	
	if (isset($_REQUEST['ticker']))
	{
		$ticker = $_REQUEST['ticker'];	
		
	}
	else 
	{
		die("No ticker parameter provided.");
	}
	
	$query="select AL2.ticker,AL2.full_name, AL1.value as current_value,AL1.date_collected current_dc,
	AL4.value AS hourly_value, AL4.date_collected AS hourly_dc,  (AL1.value - AL4.value)/AL4.value AS hourly_changeval,
	AL5.value AS daily_value, AL5.date_collected AS daily_dc, (AL1.value - AL5.value)/AL5.value AS daily_changeval,
	AL6.value AS monthly_value, AL6.date_collected AS monthly_dc, (AL1.value - AL6.value)/AL6.value AS monthly_changeval,
	AL7.value AS yearly_value, AL7.date_collected AS yearly_dc, (AL1.value - AL7.value)/AL7.value AS yearly_changeval,
	AL8.value AS alltime_value, AL8.date_collected AS alltime_dc, (AL1.value - AL8.value)/AL8.value AS alltime_changeval
from fact_data AL1,entities AL2,
(select max(batch) as mx from entities, fact_data where fact_data.entity_id=entities.id AND ticker='".$ticker."') AL3,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='HOURLY' AND fact_data.id=alerts.fact_data_key) AL4,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='DAILY' AND fact_data.id=alerts.fact_data_key) AL5,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='MONTHLY' AND fact_data.id=alerts.fact_data_key) AL6,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='YEARLY' AND fact_data.id=alerts.fact_data_key) AL7,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='ALLTIME' AND fact_data.id=alerts.fact_data_key) AL8


 where AL1.batch=AL3.mx AND AL2.id=AL1.entity_id and AL2.ticker='".$ticker."' AND AL4.ticker=AL2.ticker AND AL5.ticker=AL2.ticker AND AL6.ticker=AL2.ticker AND AL7.ticker=AL2.ticker AND AL8.ticker=AL2.ticker";
	
	
	/*$query="select AL1.value as current_value,AL1.date_collected current_dc,
	AL4.value AS hourly_value, AL4.date_collected AS hourly_dc, (AL1.value - AL4.value)/AL4.value AS hourly_changeval,
	AL5.value AS daily_value, AL5.date_collected AS daily_dc, (AL1.value - AL5.value)/AL5.value AS hourly_changeval,
	AL6.value AS monthly_value, AL6.date_collected AS monthly_dc, (AL1.value - AL6.value)/AL6.value AS hourly_changeval,
	AL7.value AS yearly_value, AL7.date_collected AS yearly_dc, (AL1.value - AL7.value)/AL7.value AS hourly_changeval,
	AL8.value AS alltime_value, AL8.date_collected AS alltime_dc, (AL1.value - AL8.value)/AL8.value AS hourly_changeval
from fact_data AL1,entities AL2,
(select max(batch) as mx from entities, fact_data where fact_data.entity_id=entities.id AND ticker='".$ticker."') AL3,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='HOURLY' AND fact_data.id=alerts.fact_data_key) AL4,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='DAILY' AND fact_data.id=alerts.fact_data_key) AL5,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='MONTHLY' AND fact_data.id=alerts.fact_data_key) AL6,
(select entities.ticker,fact_data.value,fact_daSELECT * FROM `eps_est_and_act_next_4qs` WHERE 1ta.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='YEARLY' AND fact_data.id=alerts.fact_data_key) AL7,
(select entities.ticker,fact_data.value,fact_data.date_collected from alerts,entities,fact_data where alerts.user_id=4 and alerts.entity_id=entities.id and entities.ticker='".$ticker."' and alerts.frequency='ALLTIME' AND fact_data.id=alerts.fact_data_key) AL8


 where AL1.batch=AL3.mx AND AL2.id=AL1.entity_id and AL2.ticker='".$ticker."' AND AL4.ticker=AL2.ticker AND AL5.ticker=AL2.ticker AND AL6.ticker=AL2.ticker AND AL7.ticker=AL2.ticker AND AL8.ticker=AL2.ticker";*/
	
	
	/*$output[0]['change_hour'] = sigdigits(($row2['value']-$hourly['value'])/$hourly['value'],3);
	$output[0]['change_day'] = sigdigits(($row2['value']-$daily['value'])/$daily['value'],3);
	//$output[0]['change_week'] = sigdigits(($row2['value']-$weekly['value'])/$weekly['value'],3);
	$output[0]['change_month'] = sigdigits(($row2['value']-$monthly['value'])/$monthly['value'],3);
	$output[0]['change_year'] = sigdigits(($row2['value']-$yearly['value'])/$yearly['value'],3);
	$output[0]['change_alltime'] = sigdigits(($row2['value']-$alltime['value'])/$alltime['value'],3);	*/
	$q=mysql_query($query)  or die("ERROR: Failed Query of " . $query1);
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;
		
		
	for ($i=0;$i<sizeof($output);$i++)
	{
		
		$output[$i]['current_value'] = sigdigits($output[$i]['current_value'],3);
		$output[$i]['hourly_value'] = sigdigits($output[$i]['hourly_value'],3);
		$output[$i]['daily_value'] = sigdigits($output[$i]['daily_value'],3);
		$output[$i]['monthly_value'] = sigdigits($output[$i]['monthly_value'],3);
		$output[$i]['yearly_value'] = sigdigits($output[$i]['yearly_value'],3);
		$output[$i]['alltime_value'] = sigdigits($output[$i]['alltime_value'],3);
		$output[$i]['hourly_changeval'] = sigdigits($output[$i]['hourly_changeval']*100,3)."%";
		$output[$i]['daily_changeval'] = sigdigits($output[$i]['daily_changeval']*100,3)."%";
		$output[$i]['monthly_changeval'] = sigdigits($output[$i]['monthly_changeval']*100,3)."%";
		$output[$i]['yearly_changeval'] = sigdigits($output[$i]['yearly_changeval']*100,3)."%";
		$output[$i]['alltime_changeval'] = sigdigits($output[$i]['alltime_changeval']*100,3)."%";
		
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




