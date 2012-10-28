<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

/*
 This script does entity id and full name lookup for ticker arguments.
*/

db_utility::db_connect();



$return = array();	

$tickers = split(",",urldecode($_REQUEST['tickers']));


$entities = '';
for ($i=0;$i<sizeof($tickers);$i++) {
	$entities.='\''.trim($tickers[$i]).'\',';
}
$entities = substr($entities,0,strlen($entities)-1);

$sql = "select entities.ticker,entities.id,entities.full_name ";
$sql.=" from entities ";
$sql.= " where ticker in (".$entities.") ";

//echo $sql;

$result = mysql_query($sql);

while( $row = mysql_fetch_assoc($result))
{
	$return[] = $row;
}
 
echo json_encode($return);

?>