<?php
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
?>
<HTML>
	<BODY>
				
		<table>

<?php
				
				if(isset($_POST['submit_msg'])) 
				{
					$selected_data_set = $_POST['data_set'];
					
				$query1 = "select data_set,ticker,value from test_fact_data where data_set='".$selected_data_set."'";
				
				$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
				
				for ($j=0;$j<mysql_num_rows($result1);$j++)
				//for ($j=0;$j<2;$j++)
				{
					
					$row1 = mysql_fetch_array($result1);
					
					echo "<TR>";
					echo "<TD>".$row1[data_set]."</TD>";
					echo "<TD>".$row1[ticker]."</TD>";
					echo "<TD>".$row1[value]."</TD>";
					echo "</TR>";
					
					
					
					
				}
			}

?>
				</table>
				<form action="DisplayDataSet.php" method=POST > 
					<table>
						<tr><td>
							<select name="data_set">
								<?php
									$query2 = "select distinct Data_Set from extract_info";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									for ($j=0;$j<mysql_num_rows($result2);$j++)
									{
										$row2 = mysql_fetch_array($result2);
										echo "<option value=\"".$row2[Data_Set]."\">".$row2[Data_Set]."</option>";
									}
								
								?>
								<option value="custom_eps_chart">Custom EPS Chart</option>
								
							</select>
							</td></tr>
							<tr><td><input type="submit" value="Submit" name="submit_msg" ></td></tr>
					</table>
				</form>

	
	
	</BODY>
</HTML>