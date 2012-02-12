<?php
$q=$_GET["q"];
$metricid=$_GET["m"];

$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("findata") or die(mysql_error());
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
  
  $output = 1;


function getChildren($groupid) {
	
	global $metricid;
	$haschildren = false;
	
	$sql="SELECT description,id,parent_id,1 as type,'' as full_name,'' as country FROM entity_groups where id=".$groupid;
		
	$result = mysql_query($sql);
	$e=mysql_fetch_assoc($result);
	$output[]=$e;
	
	
	
	
	
	
	$sql="SELECT description,id,parent_id,1 as type,'' as full_name,'' as country FROM entity_groups where parent_id=".$groupid;
	$sql.=" order by description ";

	
	$result = mysql_query($sql);
	
	while($e=mysql_fetch_assoc($result))
	{
			
			$tmp = getChildren($e['id']);
			if (!empty($tmp))
			{
				foreach ($tmp as $item)
				{
					$output[] = $item;
					$haschildren = true;
				}
			}
	}
	
	
	$sql= "select ticker as description,entities.id,entity_group_id as parent_id,2 as type,full_name,countries.name as country ";
	$sql.= "from entities ";
	$sql.= "JOIN entities_entity_groups on entities.id=entities_entity_groups.entity_id ";
	//$sql.= "LEFT join countries on countries.id=entities.country_id ";
	$sql.= " LEFT join countries_entities on countries_entities.entity_id=entities.id ";
	$sql.= " LEFT join countries on countries_entities.country_id=countries.id ";
	if (!empty($metricid))
		$sql.= "JOIN entities_metrics on entities_metrics.entity_id = entities.id ";
	$sql.= " where entities_entity_groups.entity_group_id=".$groupid;
	if (!empty($metricid))
		$sql.= " and entities_metrics.metric_id =".$metricid;
	//OFP 2/12/2012 - Had to add the following for USDXCD which was an example of an entity associated
	//with multiple countries and would result in invalid duplicate entires being returned in this query.
	$sql.= " and (countries_entities.default_country=1 OR countries_entities.default_country is null)";
	$sql.= " order by description ";
	
	//echo $sql;
	
	$result = mysql_query($sql);
	
	if ($result != null)
		while($e=mysql_fetch_assoc($result))
		{
				$output[]=$e;
				$haschildren = true;
		}
	
	if ($haschildren == false)
	{
		//if this node has no children, remove it.
		array_pop($output);
		
		
	}
		
	return($output);
	
	
	 //echo "<BR> schedule id: ".$q."<BR>";
	
	//echo "<table border='1'>";
	
	
	
	
}


$output = getChildren($q);
		
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
