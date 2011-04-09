<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$entitygroup = "all";
if (isset($_GET['group']))
	$entitygroup = $_GET['group'];


$return = array();	

$sql = "select entities.ticker,entities.id,entities.full_name ";
$sql.=" from entities ";
$sql.=" join entities_entity_groups on entities.id = entities_entity_groups.entity_id " ;
$sql.=" join entity_groups on entities_entity_groups.entity_group_id=entity_groups.id ";
$sql.=" where ticker like '" . $_REQUEST['term']. "%' "; 
if (!($entitygroup == 'all'))
	$sql.=" AND entity_groups.id=".$entitygroup;



$result = mysql_query($sql);

while( $row = mysql_fetch_assoc($result))
{
	$return[] = $row;
}
 
echo json_encode($return);

?>