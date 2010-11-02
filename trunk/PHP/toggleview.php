<html>
<script type="text/javascript">




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
else if (isset($_POST['submit_clear_stage']))
{
	$query1 = "delete from fact_data_stage";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	
	
}
?>

</script>
<body>
<table border="1">
	<tr>
		<td>
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
</td>
</tr>
<tr><br><br></tr>
<tr>
<td>
<form name="ObtainURL" action="toggleview.php" method=POST>
	<select name="data_set">
	<?php
	$query1 = "select data_set from extract_table where !(data_set like '%colhead%') and !(data_set like '%rowhead%')";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	for ($j=0;$j<mysql_num_rows($result1);$j++)
	{
		$row1 = mysql_fetch_array($result1);
		echo "<option value=\"".$row1[data_set]."\">".$row1[data_set]."</option>";
	}
	
	$query1 = "select data_set from extract_info";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	for ($j=0;$j<mysql_num_rows($result1);$j++)
	{
		$row1 = mysql_fetch_array($result1);
		echo "<option value=\"".$row1[data_set]."\">".$row1[data_set]."</option>";
	}

	?>
</select>
	<input type="submit" value="Show URL" name="show_url">
</form>
</td>
<td>
<?php
	if(isset($_POST['show_url'])) 
	{
	$dataset = $_POST['data_set'];
	if (substr($dataset,0,5) == "table")
	{
		$query1 = "select url_dynamic, url_static from extract_table where data_set='".$dataset."'";
	}
	else
	{
		$query1 = "select url_dynamic, url_static from extract_info where data_set='".$dataset."'";
	}
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	$row1 = mysql_fetch_array($result1);
	echo "<A href=\"".$row1[url_static].$row1[url_dynamic]."\" target=\"_blank\">".$row1[url_static].$row1[url_dynamic]."</A>";


}
	?>
</td>
</tr>
<tr>
	<td>
		<form name="ClearFactDataStage" action="toggleview.php" method=POST>
			<?php
			$query1 = "select count(value) from fact_data_stage";
			$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
			$row1 = mysql_fetch_array($result1);
			echo "Rows in fact_data_stage: ".$row1['count(value)'];
			?>
		
			<input type="submit" value="Clear fact_data_stage" name="submit_clear_stage">
	</form>
	</td>
	
</tr>
</table>
<script type="text/javascript">
<?php

	?>
</script>
</body>
</html>