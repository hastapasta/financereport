<HTML>
	<BODY>
	<?php
	//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());

	include ("../common/functions.php");
	db_utility::db_connect();
	
	function getmaxprimarykey($tablename)
	{
		$select = 'select primary_key from '.$tablename.' order by primary_key DESC';
		
		$results = mysql_query($select) or die("Failed Query of " . $select);
		$firstrow = mysql_fetch_array($results);
		return($firstrow['primary_key']);
		
		
		
	}
	
	function check_dataset_exists($dataset)
	{
		$select = 'select count(data_set) from job_info where data_set=\''.$dataset.'\'';
		$results = mysql_query($select) or die("Failed Query of " . $select);
		$firstrow = mysql_fetch_array($results);
		if ($firstrow['count(data_set)'] > 0)
			die("Data_set name already exists. Terminating.");
	}
	
	function insertarrayintodb($array,$tablename)
	{
		$fields = "";
		$values = "";
		
		for ($i=0;$i<sizeof($array);++$i)
			{
				$val = $array[$i]['value'];
				
				if (!(is_null($val)) && !(empty($val)))
				{
				
					if (!empty($fields))
					{
						$fields = $fields.", ";
						$values = $values.", ";
					}
						
					$fields=$fields.$array[$i]['field'];
					
					
					
					if ($array[$i]['type']=='int')
					{
						$values = $values.$val;
					}
					elseif ($array[$i]['type']=='varchar')
					{
						$values = $values."'".$val."'";
					}
					elseif ($array[$i]['type']=='boolean')
					{
						$values = $values.$val;
					}
					else 
					{
						die("Error, database field type not found. Terminating.");
					}
				}
				
				
			}
		
			$insert = "insert into ".$tablename." (".$fields.") values (".$values.")";
			if (mysql_query($insert))
  				echo "<BR><BR>Row inserted into table ".$tablename."<BR>";  			
  			else
  	 			die(mysql_error());
			
	}
	

	

	
	if (!isset($_POST['data_set']))
	{
		die("Required field data_set is not set. Terminating.");
	}
	
	if (!isset($_POST['body_table_count']) || !isset($_POST['colhead_table_count']) || !isset($_POST['rowhead_table_count']))
	{
		die("All of the required fields of body_table_count, colhead_table_count and rowhead_table_count are not set. Terminating.");
	}
	
	check_dataset_exists($_POST['data_set']);
	
	for ($i=0;$i<sizeof(schema_arrays::$table_extract_table);++$i)
	{
		
		schema_arrays::$table_extract_table[$i]['value'] = $_POST['body_'.schema_arrays::$table_extract_table[$i]['field']];
		
	}
	insertarrayintodb(schema_arrays::$table_extract_table,'extract_table');
	/*
	 * I'm hoping that the primary_key is always the max from autoincement.
	 */
	
	$bodyprimarykey = getmaxprimarykey('extract_table');
	
	for ($i=0;$i<sizeof(schema_arrays::$table_extract_table);++$i)
	{
		
		schema_arrays::$table_extract_table[$i]['value'] = $_POST['colhead_'.schema_arrays::$table_extract_table[$i]['field']];
		
	}
	insertarrayintodb(schema_arrays::$table_extract_table,'extract_table');
	$colheadprimarykey = getmaxprimarykey('extract_table');
	
	for ($i=0;$i<sizeof(schema_arrays::$table_extract_table);++$i)
	{
		
		schema_arrays::$table_extract_table[$i]['value'] = $_POST['rowhead_'.schema_arrays::$table_extract_table[$i]['field']];
		
	}
	insertarrayintodb(schema_arrays::$table_extract_table,'extract_table');
	$rowheadprimarykey = getmaxprimarykey('extract_table');
	
	for ($i=0;$i<sizeof(schema_arrays::$table_job_info);++$i)
	{
		if (isset($_POST[schema_arrays::$table_job_info[$i]['field']]))
			schema_arrays::$table_job_info[$i]['value'] = $_POST[schema_arrays::$table_job_info[$i]['field']];
		else
			schema_arrays::$table_job_info[$i]['value']="";
		
	}
	
	schema_arrays::$table_job_info[] = array('field' => 'extract_key','type' => 'int', 'length' => 11, 'value' => $bodyprimarykey);
	schema_arrays::$table_job_info[] = array('field' => 'extract_key_colhead','type' => 'int', 'length' => 11, 'value' => $colheadprimarykey);
	schema_arrays::$table_job_info[] = array('field' => 'extract_key_rowhead','type' => 'int', 'length' => 11, 'value' => $rowheadprimarykey);
	
	insertarrayintodb(schema_arrays::$table_job_info,'job_info');
	
	for ($i=0;$i<sizeof(schema_arrays::$table_schedule);++$i)
	{
		
		schema_arrays::$table_schedule[$i]['value'] = $_POST[schema_arrays::$table_schedule[$i]['field']];
		
	}
	
	
	
	schema_arrays::$table_schedule[] = array('field' => 'data_set','type' => 'varchar', 'length' => 45, 'value' => $_POST['data_set']);

	insertarrayintodb(schema_arrays::$table_schedule,'schedule');
	
	
	
	

  	
	
	
	?>
	

	</BODY>
</HTML>

