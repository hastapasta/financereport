<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();



$return = array();	

$sql = "select name,id ";
$sql.=" from countries ";
$sql.=" where name like '" . $_REQUEST['term']. "%' "; 


$result = mysql_query($sql);

while( $row = mysql_fetch_assoc($result))
{
	$return[] = $row;
}
 
echo json_encode($return);

?>