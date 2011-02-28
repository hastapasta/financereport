<div class="alerts view">
<h2><?php  __('Alert');?></h2>
<dl>
<?php $this->set('jsIncludes',array('google'));   // this will link to /js/google.js ?>
<?php $i = 0; $class = ' class="altrow"';?>
<?php 
	//debug($alert);
	$table = 'Alert';
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
	
	$table = 'TimeEvent';
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
	
	
	
	$table = 'Entity';
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
	
	$table = 'Schedule';
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
		
		
	
	}
?>
	
	

</dl>
<div id="chart-div"></div>
</div>

	<?php echo $this->element('actions'); ?>
