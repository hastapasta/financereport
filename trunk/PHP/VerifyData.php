<?php
$frame = $_GET['frame'];

if ($frame == 'parent')
{
?>


<frameset rows="20%,*">
	<frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=form" name=form>
<frameset cols="50%,*">
 	<frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=internaldata" name=internaldata>
  <frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=webpage" name=webpage>
 </frameset>
</frameset>

<?php
}
else if ($frame == 'internaldata')
{
?>
<table>
<?php
	if(isset($_POST['submit_internal'])) 
	{
		mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
		mysql_select_db("mydb") or die(mysql_error());
		$selected_data_set = $_POST['data_set'];
		
		$query1 = //"select data_set,ticker,value,static_url from view_fact_data,extract_info where view_fact_data.data_set=extract_info.Data_Set AND data_set='".$selected_data_set."'";
		"SELECT view_fact_data.data_set, ticker, value, url_static
		FROM view_fact_data, extract_info
		WHERE view_fact_data.data_set = extract_info.Data_Set
		AND view_fact_data.data_set = '".$selected_data_set."'";

		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		//for ($j=0;$j<2;$j++)
		{
		
			$row1 = mysql_fetch_array($result1);
			
			echo "<TR>";
			echo "<TD>".($j+1)."</TD>";
			echo "<TD>".$row1[data_set]."</TD>";
			echo "<TD><A HREF=\"".$row1[url_static].$row1[ticker]."\" target=webpage>".$row1[ticker]."</A></TD>";
			echo "<TD>".$row1[value]."</TD>";
			echo "</TR>";
		}
	
	
}

?>
</table>


<?php

}
else if ($frame == 'webpage')
{
	
?>
	
THIS IS THE WEBPAGE DATA FRAME

<?php
}
else if ($frame == 'form')
{
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
?>

<form name="SelectFactData" action="VerifyData.php?frame=internaldata" method=POST target=internaldata> 	
					<table>
						<tr><td>
							<!-- <select name="data_set" onchange="alert(this.value);" onchange="UpdateField(this.selectedIndex);"> -->
							<select name="data_set">
								<?php
									$query2 = "select distinct data_set from view_fact_data order by data_set";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									for ($j=0;$j<mysql_num_rows($result2);$j++)
									{
										$row2 = mysql_fetch_array($result2);
										echo "<option value=\"".$row2[data_set]."\">".$row2[data_set]."</option>";
									}
								
								?>
							</select>
						</td>
						<td>
							<input type="submit" value="Submit" name="submit_internal" 
							onclick="SelectFactData.action='VerifyData.php?frame=internaldata'; return true" >
						</td></tr>
							</table>
							
</form>

<?php
}
?>
