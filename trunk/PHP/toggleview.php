<html>
	<body>




<?php
mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
if(isset($_POST['submit_table_switch'])) 
{
		$query1 = "select count(primary_key) from view_fact_data";
		$query2 = "select count(primary_key) from fact_data_stage";
		
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		$row1 = mysql_fetch_array($result1);
		$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
		$row2 = mysql_fetch_array($result2);
		
		//echo "<TR><TD>".$row1['count(primary_key)']."</TD>   <TD>".$row2['count(primary_key)']."</TD></TR>";
		mysql_query("DROP VIEW view_fact_data") or die("Failed to drop view_fact_data");
		
		//previous view was based off of fact_data_stage, create new view off of fact_data
		if ($row1['count(primary_key)'] == $row2['count(primary_key)'])
		{
				mysql_query("CREATE ALGORITHM = UNDEFINED VIEW `view_fact_data` AS SELECT * FROM `fact_data` ") or die("Failed to create view_fact_data");
				echo "CREATED NEW VIEW FROM fact_data";
		}
		//create new view off of fact_data_stage
		else
		{
			mysql_query("CREATE ALGORITHM = UNDEFINED VIEW `view_fact_data` AS SELECT * FROM `fact_data_stage` ") or die("Failed to create view_fact_data");
			echo "CREATED NEW VIEW FROM fact_data_stage";
		}
		
		
		
		
}


?>
<form name="SelectFactData" action="toggleview.php" method=POST > 	
					<table>
						<tr><td>
<?php
		$query1 = "select count(primary_key) from view_fact_data";
		$query2 = "select count(primary_key) from fact_data_stage";
		
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		$row1 = mysql_fetch_array($result1);
		$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
		$row2 = mysql_fetch_array($result2);
		
		//echo "<TR><TD>".$row1['count(primary_key)']."</TD>   <TD>".$row2['count(primary_key)']."</TD></TR>";
		
		if ($row1['count(primary_key)'] == $row2['count(primary_key)']) 
			echo "Current view: fact_data_stage";
		else
			echo "Current view: fact_data";
			

	

?>						
						
						</td>
						<td>
							<input type="submit" value="Toggle Fact View" name="submit_table_switch">
						</td></tr>
							</table>
							
</form>

</body>
</html>