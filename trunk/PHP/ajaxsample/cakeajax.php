<?php
$q=$_GET["q"];

function wl($the_string)
{
	$fi = fopen( 'logfile2.txt', 'a+');
	$the_string = $the_string."\n";
	fputs( $fi, $the_string, strlen($the_string) );
	fclose( $fi );
}

//wl("in picklist1.php");

//$con = mysql_connect('localhost', 'peter', 'abc123');
$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("findata") or die(mysql_error());
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("findata", $con);

$sql="SELECT entities.ticker,entities.id FROM entities,entity_groups,entities_entity_groups,entity_groups_tasks,schedules";
$sql.=" where entities.id=entities_entity_groups.entity_id and entity_groups.id=entities_entity_groups.entity_group_id and entity_groups.id=entity_groups_tasks.entity_group_id";
$sql.=" and entity_groups_tasks.task_id=schedules.task_id and schedules.id=".$q;

/*select ticker from entities,entity_groups,entities_entity_groups,entity_groups_tasks,schedules where 
 entity_groups_tasks.task_id=schedules.task_id and entity_groups.id=entity_groups_tasks.entity_group_id and 
 entities_entity_groups.entity_group_id=entity_groups.id and entities.id=entities_entity_groups.entity_id and schedules.id=72*/



$result = mysql_query($sql);

 //echo "<BR> schedule id: ".$q."<BR>";

//echo "<table border='1'>";


while($e=mysql_fetch_assoc($result))
{
		$output[]=$e;
}
		
		
print(json_encode($output));
 

/*while($row = mysql_fetch_array($result))
  {

  echo "<tr>";
  echo "<td>" . $row['ticker'] . "</td>";
  echo "</tr>";
  }
echo "</table>";*/

mysql_close($con);
?> 
