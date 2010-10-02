<?php
$timestamp=$_GET["timestamp"];

//$con = mysql_connect('localhost', 'peter', 'abc123');
/*$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("mydb") or die(mysql_error());
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("mydb", $con);

$sql="SELECT * FROM company WHERE ticker = '".$q."'";

$result = mysql_query($sql);*/

 echo "<BR> time: ".$timestamp."<BR>";

/*echo "<table border='1'>
<tr>
<th>company".$q."</th>
<th>shares outstanding</th>
<th>begin fiscal quarter</th>
<th>groupssss</th>
</tr>";


 

while($row = mysql_fetch_array($result))
  {

  echo "<tr>";
  echo "<td>" . $row['ticker'] . "</td>";
  echo "<td>" . $row['shares_outstanding'] . "</td>";
  echo "<td>" . $row['begin_fiscal_calendar'] . "</td>";
  echo "<td>" . $row['groups'] . "</td>";
  echo "</tr>";
  }
echo "</table>";

mysql_close($con);*/
?> 