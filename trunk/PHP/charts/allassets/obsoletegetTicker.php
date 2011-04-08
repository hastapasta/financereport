<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();


$return = array();	

$sql = "select entities.ticker,entities.id,entities.full_name from entities where ticker like '" . $_REQUEST['term']. "%'"; 
$result = mysql_query($sql);

while( $row = mysql_fetch_assoc($result))
{
	$return[] = $row;
}
 
echo json_encode($return);

?>