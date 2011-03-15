<?php
require_once '../common/functions.php';
//echo "google.visualization.Query.setResponse({version:'0.6',status:'ok',sig:'1950104442',table:{cols:[{id:'A',label:'',type:'string',pattern:''},{id:'B',label:'2011-01-26',type:'number',pattern:'#0.###############'},{id:'C',label:'2011-01-19',type:'number',pattern:'#0.###############'}],rows:[{c:[{v:'Capital paid in'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Surplus'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Other capital'},{v:0.0,f:'0'},{v:0.0,f:'0'}]}]}});";

//echo "google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"eps\",type:'number'}],rows: [{c:[{v:\"ADSK\"},{v:20091,f:\"20091\"},{v:0,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20091,f:\"20091\"},{v:800,f:\"1\"}]},{c:[{v:\"BIG\"},{v:20091,f:\"20091\"},{v:10000,f:\"1\"}]},{c:[{v:\"ADSK\"},{v:20092,f:\"20092\"},{v:-1.36,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20092,f:\"20092\"},{v:-0.56,f:\"1\"}]}]},\"version\":'0.5'});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{\"cols\":[{\"id\":\"Col1\",\"label\":\"BLAP\",\"type\":\"date\"}],\"rows\":[{\"c\":[{\"v\":\"a\"},{\"v\":\"Date(2010,10,6)\"}]},{\"c\":[{\"v\":\"b\"},{\"v\":\"Date(2010,10,7)\"}]}]}});";

if (isset($_COOKIE['counter']))
{
		$counter = $_COOKIE['counter']+1;
		setcookie('counter', $counter,mktime (0, 0, 0, 12, 31, 2015));
		
}
else
{
	$counter=0;
	setcookie('counter', $counter,mktime (0, 0, 0, 12, 31, 2015));
}

wl("-----------------------------------\n");
wl("counter: ".$counter."\n");

$reqId = 0; 
if(isset($_GET['tqx'])) 
{ $tqx = $_GET['tqx']; }

wl("reqId ".$reqId);
wl("tqx: ".$tqx);

wl($_SERVER['QUERY_STRING']."\n");




/*foreach (getallheaders() as $name => $value) {
    wl("$name: $value\n");
}*/
/*wl("Get parameters\n");
foreach ($_GET as $key => $var) {
 if (!in_array($key,$escape)) wl("$var\n");
}*/

wl("Post parameters\n");
foreach ($_POST as $key => $var) {
 if (!in_array($key,$escape)) wl("$var\n");
}

echo "google.visualization.Query.setResponse({".$tqx.",status:'ok',table:{\"cols\":[{\"id\":\"\",\"label\":\"Name\",\"pattern\":\"\",\"type\":\"string\"},{\"id\":\"\",\"label\":\"Salary\",\"pattern\":\"\",\"type\":\"number\"},{\"id\":\"\",\"label\":\"Full Time Employee\",\"pattern\":\"\",\"type\":\"boolean\"}],\"rows\":[{\"c\":[{\"v\":\"Mike\",\"f\":null},{\"v\":10000,\"f\":\"$10,000\"},{\"v\":true,\"f\":null}]},{\"c\":[{\"v\":\"Jim\",\"f\":null},{\"v\":8000,\"f\":\"$8,000\"},{\"v\":false,\"f\":null}]},{\"c\":[{\"v\":\"Alice\",\"f\":null},{\"v\":12500,\"f\":\"$12,500\"},{\"v\":true,\"f\":null}]},{\"c\":[{\"v\":\"Bob\",\"f\":null},{\"v\":7000,\"f\":\"$7,000\"},{\"v\":true,\"f\":null}]}],\"p\":null}});";

?> 



