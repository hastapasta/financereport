<?php
$q=$_GET["q"];

function wl($the_string)
{
	$fi = fopen( 'logfile2.txt', 'a+');
	$the_string = $the_string."\n";
	fputs( $fi, $the_string, strlen($the_string) );
	fclose( $fi );
}

wl("in picklist1.php");

//$con = mysql_connect('localhost', 'peter', 'abc123');
$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("findata") or die(mysql_error());
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("findata", $con);

$sql="SELECT entities.ticker FROM entities,entity_groups,join_entity_groups_tasks where entity_groups.entity_id=entities.id and join_entity_groups_tasks.entity_group_name=entity_groups.name and join_entity_groups_tasks.task_id=".$q;

$result = mysql_query($sql);

 echo "<BR> schedule id: ".$q."<BR>";

echo "<table border='1'>";



 

while($row = mysql_fetch_array($result))
  {

  echo "<tr>";
  echo "<td>" . $row['entities.ticker'] . "</td>";
  echo "</tr>";
  }
echo "</table>";

mysql_close($con);
?> 