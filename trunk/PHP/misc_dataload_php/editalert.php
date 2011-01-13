<?php
	//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());
	include ("../common/functions.php");
	db_utility::db_connect();
?>
<HTML>
	<BODY>
	
				


<?php

				if (isset($_GET['pkey']))
				{
					$primary_key = $_GET['pkey'];
				}
				else 
				{
					die("No pkey parameter set. Terminating.");
				}
				
				if (isset($_GET['currentvalue']))
				{
					$currentvalue = $_GET['currentvalue'];
				}
				
			
				if(isset($_POST['submit_msg'])) 
				{
					
					
				$query1 = "update notify set limit_adjustment=".$_POST['limit_increase']." where primary_key=".$primary_key;
				
				
				if (mysql_query($query1))
  					echo "Limit Value Updated.<BR>";  			
  				else
  	 				die(mysql_error());
  	 				}
  	 	?>
					</table>
				
			

INCREASE ALERT LIMIT
			
				<?php 
				echo "<form action=\"editalert.php?pkey=".$primary_key."&currentvalue=".$currentvalue."\" method=POST > ";
				?>
				
					<table>
						
							
								<?php
									$query2 = "select schedule,schedule.data_set,email,frequency,limit_value,notify.ticker,fact_data_key,value,limit_adjustment,date_collected from notify,schedule,fact_data where notify.primary_key=".$primary_key." and notify.schedule=schedule.primary_key and notify.fact_data_key=fact_data.primary_key";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									$row2 = mysql_fetch_array($result2);
									//<input type=\"text\" name=\"".$field."\" size=".$size." maxlength=\"".$length."\" >
									//echo "<TR><TD>Type: </TD><TD><input type=\"text\" name=\"type\" size=20 value=\"".$row2['type']."\" ></TD></TR>";
									if (!empty($row2['ticker']))
									{
										echo "<TR><TD>Ticker: </TD><TD>".$row2['ticker']."</TD></TR>";
										$query3 = "select full_name from company where ticker='".$row2['ticker']."'";
										$result3 = mysql_query($query3) or die("Failed Query of " . $query3);
										$row3 = mysql_fetch_array($result3);
										
										echo "<TR><TD>Ticker Description: </TD><TD>".$row3['full_name']."</TD></TR>";
										
									}
									echo "<TR><TD>Data Set: </TD><TD>".$row2['data_set']."</TD></TR>";
									echo "<TR><TD>Email: </TD><TD>".$row2['email']."</TD></TR>";

									$beginvalue = $row2['value'];
									
									echo "<TR><TD>Begin Value: </TD><TD>".$beginvalue."</TD></TR>";
									echo "<TR><TD>Current Value: </TD><TD>".$currentvalue."</TD></TR>";
									
									$percentdiff = round((($currentvalue - $beginvalue)/$beginvalue) * 100,2);
									
									echo "<TR><TD>Percent Difference: </TD><TD>".$percentdiff."%</TD></TR>";
									
									
									echo "<TR><TD>Frequency: </TD><TD>".$row2['frequency']."</TD></TR>";
									echo "<TR><TD>Begin Time: </TD><TD>".$row2['date_collected']."</TD></TR>";
									
							
									echo "<TR><TD>Base Limit Value: </TD><TD>".$row2['limit_value']."</TD></TR>";
									echo "<TR><TD>Limit Value Adjustment: </TD><TD>".$row2['limit_adjustment']."</TD></TR>";
									
									echo "<TR><TD>Increase Limit Value Adjustment To: </TD><TD><input type=\"text\" name=\"limit_increase\" size=20 value=\"".$row2['limit_adjustment']."\" ></TD></TR>";
									
								
								?>

							<tr><td><input type="submit" value="Submit" name="submit_msg" ></td></tr>
					</table>
				</form>

				
				
	
			

		
	
	</BODY>
</HTML>