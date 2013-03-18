<div class="schedules view">
<?php //echo $this->element('actions'); ?>
<dl>
<?php //$this->set('jsIncludes',array('google'));   // this will link to /js/google.js ?>
<?php $i = 0; $class = ' class="altrow"';?>
<?php 
	//debug($schedule);
	$table = 'Schedule';
	$keys = array_keys($schedule[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $schedule[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}
	
	$table = 'Task';
	$keys = array_keys($schedule[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $schedule[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}
	
	/*
	 * This next one is a little different because of a HABTM.
	 */
	
	
	$table = 'Job';
	
	foreach ($schedule['Task'][$table] as $job)
	{
		//$keys = array_keys($schedule['Task'][$table][0]);
		$keys = array_keys($job);
		//debug($keys,true);
	
		foreach ($keys as $key)
		{
			
	
			echo "<!-- begin -->";
			echo "<dt ";
			if ($i % 2 == 0) echo $class;
			echo ">";
			__($table.".".$key);
			echo "</dt>";
			echo "<dd ";
			if ($i++ % 2 == 0) echo $class;
			echo ">";
			//echo $schedule['Task'][$table][0][$key];
			echo $job[$key];
			echo "&nbsp;</dd>";
			echo "<!-- end -->";
			echo "\n";
			
			
		
		}
	}
	
	/*$table = 'extract_info';
	$keys = array_keys($schedule[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $schedule[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}*/
	
	
	/*$table = 'Schedule';
	$keys = array_keys($alert[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $alert[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
		
	
	}
	
	$table = 'Task';
	$keys = array_keys($alert['Schedule'][$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $alert['Schedule'][$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}
	
	$table = 'InitialFactDatum';
	$keys = array_keys($alert[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $alert[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}
	
	$table = 'CurrentFactDatum';
	$keys = array_keys($alert[$table]);
	//debug($keys,true);

	foreach ($keys as $key)
	{
		

		echo "<!-- begin -->";
		echo "<dt ";
		if ($i % 2 == 0) echo $class;
		echo ">";
		__($table.".".$key);
		echo "</dt>";
		echo "<dd ";
		if ($i++ % 2 == 0) echo $class;
		echo ">";
		echo $alert[$table][$key];
		echo "&nbsp;</dd>";
		echo "<!-- end -->";
		echo "\n";
		
		
	
	}*/
?>
	
	

</dl>
</div>

