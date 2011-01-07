
<?php

	include ("../common/functions.php");
	
	db_utility::db_connect();
	
	$offset=1;
	if (isset($_GET['metric1']))
		$offset=$_GET['metric1'];
	
	
	//$string4 = "google.visualization.Query.setResponse({version:'0.5',reqId:'0',status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"value\",type:'number'}],rows: [{c:[{v:\"WMT\"},{v:2010,f:\"2010\"},{v:1.32,f:\"1\"}]}]}});";
	$string3 = "google.visualization.Query.setResponse({version:'0.5',reqId:'0',status:'ok',table:{cols: [";
	$string3 = $string3."{id:'ticker',label:\"ticker\",type:'string'},";
	$string3 = $string3."{id:'calyear',label:\"calyear\",type:'number'},";
	$string3 = $string3."{id:'value',label:\"eps\",type:'number'}";
	
	$string3 = $string3."],rows: [";
	
	$query2 = "select ticker from company where groups like '%sandp%' order by ticker limit 50 offset ".$offset;
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
	
	
	
	$query1 = "select ticker,value,calyear from eps_est_and_act where ticker in";
	
	$query1 = $query1.$inclause;
	
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
	
	
	
	for ($j=0;$j<mysql_num_rows($result1);$j++)
	//for ($j=0;$j<1;$j++)
	{
		if ($j==0)
			$tmpstring="";
		else 
			$tmpstring=",";
		$row1 = mysql_fetch_array($result1);
		$tmpstring = $tmpstring."{c:[{v:\"".$row1['ticker']."\"},";
		$tmpstring = $tmpstring."{v:".$row1['calyear'].",f:\"".$row1['calyear']."\"},";
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
