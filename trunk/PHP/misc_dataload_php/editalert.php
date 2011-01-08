<?php
	//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());
	include ("../common/functions.php");
	db_utility::db_connect();
?>
<HTML>
	<BODY>
	INCREASE ALERT LIMIT
				
		<table>

<?php

				if (isset($_GET['pkey']))
				{
					$primary_key = $_GET['pkey'];
				}
				else 
				{
					die("No pkey parameter set. Terminating.");
				}
				
				if(isset($_POST['submit_msg'])) 
				{
					$selected_data_set = $_POST['data_set'];
					
				$query1 = "select data_set,ticker,value from fact_data where data_set='".$selected_data_set."'";
				
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
						
							
								<?php
									$query2 = "select schedule,schedule.data_set,email,frequency,limit_value,notify.ticker,fact_data_key,value,limit_adjustment,date_collected from notify,schedule,fact_data where notify.primary_key=".$primary_key." and notify.schedule=schedule.primary_key and notify.fact_data_key=fact_data.primary_key";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									$row2 = mysql_fetch_array($result2);
									//<input type=\"text\" name=\"".$field."\" size=".$size." maxlength=\"".$length."\" >
									//echo "<TR><TD>Type: </TD><TD><input type=\"text\" name=\"type\" size=20 value=\"".$row2['type']."\" ></TD></TR>";
									echo "<TR><TD>Data Set: </TD><TD>".$row2['data_set']."</TD></TR>";
									echo "<TR><TD>Email: </TD><TD>".$row2['email']."</TD></TR>";	
									echo "<TR><TD>Begin Value: </TD><TD>".$row2['value']."</TD></TR>";
									echo "<TR><TD>Current Value: </TD><TD></TD></TR>";
									echo "<TR><TD>Frequency: </TD><TD>".$row2['frequency']."</TD></TR>";
									echo "<TR><TD>Begin Time: </TD><TD>".$row2['date_collected']."</TD></TR>";
									if (!empty($row2['ticker']))
									{
										echo "<TR><TD>Ticker: </TD><TD>".$row2['ticker']."</TD></TR>";
										$query3 = "select description from company where ticker='".$row['ticker']."'";
										$result3 = mysql_query($query3) or die("Failed Query of " . $query3);
										$row3 = mysql_fetch_array($result3);
										
										echo "<TR><TD>Ticker Description: </TD><TD>".$row3['description']."</TD></TR>";
										
									}
									echo "<TR><TD>Limit Value: </TD><TD>".$row2['limit_value']."</TD></TR>";
									echo "<TR><TD>Limit Increase: </TD><TD><input type=\"text\" name=\"type\" size=20 value=\"".$row2['limit_adjustment']."\" ></TD></TR>";
									
								
								?>

							<tr><td><input type="submit" value="Submit" name="submit_msg" ></td></tr>
					</table>
				</form>

	
	
	</BODY>
</HTML>