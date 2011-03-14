
<?php

	include ("../common/functions.php");
	
	db_utility::db_connect();
	
	$offset=1;
	if (isset($_GET['metric1']))
		$offset=$_GET['metric1'];
	
	
	//$string4 = "google.visualization.Query.setResponse({version:'0.5',reqId:'0',status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"value\",type:'number'}],rows: [{c:[{v:\"WMT\"},{v:2010,f:\"2010\"},{v:1.32,f:\"1\"}]}]}});";
	$string3 = "google.visualization.Query.setResponse({version:'0.5',reqId:'0',status:'ok',table:{cols: [";
	$string3 = $string3."{id:'ticker',label:\"ticker\",type:'string'},";
	$string3 = $string3."{id:'calyear',label:\"calyear\",type:'date'},";
	$string3 = $string3."{id:'value',label:\"eps\",type:'number'}";
	
	$string3 = $string3."],rows: [";
	
	$query2 = "select ticker from entities,entities_entity_groups,entity_groups where entity_groups.name='sandp' and entities_entity_groups.entity_group_id=entity_groups.id and entities_entity_groups.entity_id=entities.id order by ticker limit 10";
	//$query2 = "select ticker from company where groups like '%sandp%' order by ticker limit 50 offset ".$offset;
	$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
	
	$inclause = "(";
	
	for($k=0;$k<mysql_num_rows($result2);$k++)
	{
		$row2 = mysql_fetch_array($result2);
		if ($k!=0)
			$inclause = $inclause.",";
			
		$inclause = $inclause."'".$row2['ticker']."'";
		
			
		
	}
	
	$inclause = $inclause.")";
	
	
	
	//$query1 = "select ticker,value,calyear,calquarter,calyear || calquarter as yearquarter from table_eps_est_and_act,entities where entities.id=eps_est_and_act.entity_id AND ticker in ";
	$query1 = "select ticker,value,calyear,calquarter from table_eps_est_and_act where ticker in ";
	
	$query1 = $query1.$inclause;
	
	$query1 = $query1." order by calquarter,ticker";
	
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	//Sample Formatting:
	/*
	 * google.visualization.Query.setResponse({version:'0.5',reqId:'0',status:'ok',
	 * table:{cols: [{id:'ticker',label:"ticker",type:'string'},{id:'calyear',label:"calyear",type:'number'},{id:'value',label:"value",type:'number'}],
	 * rows: [{c:[{v:"WMT"},{v:2010,f:"2010"},{v:1.32,f:"1"}]},
	 * 		  {c:[{v:"WMT"},{v:2011,f:"2011"},{v:0.97,f:"1"}]},
	 *        {c:[{v:"WMT"},{v:2012,f:"2012"},{v:1.223,f:"1"}]}]}});
	 */
	
	/*
	 * Loop through the column headers
	 */
	
	/*
	 * Loop through the values
	 */
	echo $string3;
	
	function writedata1($result1)
	{
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		//for ($j=0;$j<1;$j++)
		{
			if ($j==0)
				$tmpstring="";
			else 
				$tmpstring=",";
			$row1 = mysql_fetch_array($result1);
			$tmpstring = $tmpstring."{c:[{v:\"".$row1['ticker']."\"},";
			
			if ($row1['calquarter']=="1")
			{
				$month = "2";
				$day = "31";
			}
			else if ($row1['calquarter']=="2")
			{
				$month="5";
				$day = "30";
			}
			else if ($row1['calquarter']=="3")
			{
				$month="8";
				$day = "30";
			}
			else if ($row1['calquarter']=="4")
			{
				$month="11";
				$day = "31";
			}
			
			
			
			
			
			
			//$tmpstring = $tmpstring."{v:".$row1['yearquarter'].",f:\"".$row1['calyear']."\"},";
			$tmpstring.="{v:\"Date(".$row1['calyear'].",".$month.",".$day.")\",\"f\":null},";
			$tmpstring = $tmpstring."{v:".$row1['value'].",f:\""."1"."\"}]}";
			
			echo($tmpstring);
		
			
			
			/*echo "<option value=\"".$row1[$column_name]."\"";
			if (isset($_POST['views']))
			{
				if ($row1[$column_name] == $_POST['views'])
					echo "selected";
			}
			echo ">".$row1[$column_name]."</option>";*/
		}
	}
	$test = array("a"=>1,"b"=>2,"c"=>3);
	
	function writedata2($result1)
	{
		$tickerarray = array();
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		//for ($j=0;$j<1;$j++)
		{
			
			if ($j==0)
				$tmpstring="";
			else 
				$tmpstring=",";
			$row1 = mysql_fetch_array($result1);
			$tmpstring = $tmpstring."{c:[{v:\"".$row1['ticker']."\"},";
			
			if ($row1['calquarter']=="1")
			{
				$month = "2";
				$day = "31";
			}
			else if ($row1['calquarter']=="2")
			{
				$month="5";
				$day = "30";
			}
			else if ($row1['calquarter']=="3")
			{
				$month="8";
				$day = "30";
			}
			else if ($row1['calquarter']=="4")
			{
				$month="11";
				$day = "31";
			}
			
			$tmpstring.="{v:\"Date(".$row1['calyear'].",".$month.",".$day.")\",\"f\":null},";
			//$tmpstring = $tmpstring."{v:".$row1['yearquarter'].",f:\"".$row1['yearquarter']."\"},";
			
			if (array_key_exists($row1['ticker'],$tickerarray) && !($tickerarray[$row1['ticker']]==0))
			{
				$growth = ($row1['value'] - $tickerarray[$row1['ticker']]) / $tickerarray[$row1['ticker']];
				$growth = round($growth,2);
				$tmpstring = $tmpstring."{v:".$growth.",f:\""."1"."\"}]}";
			}
			else 
			{
				$tmpstring = $tmpstring."{v:0,f:\""."1"."\"}]}";
				//array_push($tickerarray,$row1['ticker'],$row1['value']);
				$tickerarray[$row1['ticker']] = $row1['value'];
			}
			
			
			echo($tmpstring);
		
			
			
			/*echo "<option value=\"".$row1[$column_name]."\"";
			if (isset($_POST['views']))
			{
				if ($row1[$column_name] == $_POST['views'])
					echo "selected";
			}
			echo ">".$row1[$column_name]."</option>";*/
		}
	}
	
	writedata2($result1);
	
	

	echo "]}});";



//$string = "id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"value\",type:'number'}],rows: [{c:[{v:\"JPM\"},{v:2010,f:\"2010\"},{v:1.32,f:\"1\"}]},{c:[{v:\"JPM\"},{v:2011,f:\"2011\"},{v:0.97,f:\"1\"}]},{c:[{v:\"JPM\"},{v:2012,f:\"2012\"},{v:1.223,f:\"1\"}]}]}});";
//$string2 = '[{"id":"ticker","label":"ticker","type":"string"}]';
//$string3 = '{"value2":4570,"date_collected2":"2011-01-06 15:35:57","value":4950,"date_collected":"2010-11-05 13:41:26","ticker":"USDPYG","frequency":"YEARLY","changeval":"-8.497%"}';
//echo("google.visualization.Query.setResponse(".$string.");");
//echo $string;
//var_dump(json_decode($string2,TRUE));





?>
<html>
<body>
</body>
</html>
