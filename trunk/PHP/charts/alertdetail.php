<?php
require_once '../common/functions.php';

db_utility::db_connect();

if (isset($_GET['alertid']))
	$alertid = $_GET['alertid'];
else
	die("no alertid parameter in url");



?>
<html>
<head>
  
</head>
<body>
<?php 
  		$query1 .= "select alerts.*,entities.*,time_events.*,fd1.*,fd2.*,schedules.*,users.* ";
  		$query1 .= "from alerts ";
  		$query1 .= "LEFT JOIN entities ON alerts.entity_id = entities.id ";
  		$query1 .= "LEFT JOIN time_events ON alerts.time_event_id = time_events.id ";
  		$query1 .= "LEFT JOIN fact_data as fd1 ON alerts.initial_fact_data_id = fd1.id ";
  		$query1 .= "LEFT JOIN fact_data as fd2 ON alerts.initial_fact_data_id = fd2.id ";
  		$query1 .= "LEFT JOIN schedules ON alerts.schedule_id = schedules.id ";
  		$query1 .= "LEFT JOIN users ON alerts.user_id = users.id ";
  		$query1 .= "where alerts.id=".$alertid;
  		
  		
  		
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			while ($col = current($row1))
			  {
			  		next($row1);
			        echo key($row1).'<br />';
			        next($row1);
			   }
			
			
			//$row1 = mysql_fetch_array($result1);
			//echo "<option value=\"".$row1['id']."\">".$row1['username']."</option>";
			
		}
?>

   
 


    

  
</body>
</html>