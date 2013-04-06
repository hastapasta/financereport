
<div style='clear:both'></div>
<?php
	$currpage = isset($currentpage)?$currentpage :"";
	$sub = 0;
	$userprops = $this->getVar('user_props');
?>
<div class='main_tab'>

 	<?php
 		$class = array();
 		//$list = array('chart');
		//if($currpage == 'charts' && in_array($this->request->params['action'], $list)){ $class=array('class'=>'active_tab'); $sub=7;}
		if($currpage == 'charts'){ $class=array('class'=>'active_tab'); $sub=7;}
 			echo $this->Html->link(__('Charts'), array('controller' => 'charts','action' => 'chart'), $class);

 	?>
 	<?php
	 	$class = array();
		if($currpage == 'alerts'){ $class=array('class'=>'active_tab'); $sub=3;}
	 	echo $this->Html->link(__('Alerts'), array('controller' => 'alerts', 'action' => 'index'), $class);
 	?>

 	 <?php
	 	$class = array();
		if($currpage == 'entities'){ $class=array('class'=>'active_tab'); $sub=5;}
	 	echo $this->Html->link(__('Entities'), array('controller' => 'entities', 'action' => 'index'), $class);
 	?>

	 <?php
 		$class = array();

 		if ($userprops['User']['group_id'] == 1){
			if($currpage == 'entity_groups'){ $class=array('class'=>'active_tab'); $sub=6;}
	 		echo $this->Html->link(__('Entity Groups'), array('controller' => 'entity_groups', 'action' => 'index'), $class);
 		}
 	?>

	 <?php
 		$class = array();
 		if ($userprops['User']['group_id'] == 1){
			if($currpage == 'schedules') { $class=array('class'=>'active_tab'); $sub=4;}
	 		echo $this->Html->link(__('Schedules'), array('controller' => 'schedules', 'action' => 'index'), $class);
 		}
 	?>

	 <?php
	 	$class = array();
	 	if ($userprops['User']['group_id'] == 1){
			if($currpage == 'tasks'){ $class=array('class'=>'active_tab'); $sub=8;}
	 		echo $this->Html->link(__('Tasks'), array('controller' => 'tasks', 'action' => 'index'), $class);
	 	}
 	?>
	 <?php
	 	$class = array();
	 	if ($userprops['User']['group_id'] == 1){
			if($currpage == 'jobs'){ $class=array('class'=>'active_tab'); $sub=9;}
	 		echo $this->Html->link(__('Jobs'), array('controller' => 'jobs', 'action' => 'index'), $class);
	 	}
 	?>
	 <?php
	 	$class = array();
	 	if ($userprops['User']['group_id'] == 1){
			if($currpage == 'extract_tables'){ $class=array('class'=>'active_tab'); $sub=10;}
	 		echo $this->Html->link(__('Extract Tables'), array('controller' => 'extract_tables', 'action' => 'index'), $class);
	 	}
 	?>
	 <?php
	 	$class = array();
	 	if ($userprops['User']['group_id'] == 1){
			if($currpage == 'extract_singles'){ $class=array('class'=>'active_tab'); $sub=11;}
	 		echo $this->Html->link(__('Extract Singles'), array('controller' => 'extract_singles', 'action' => 'index'), $class);
	 	}
 	?>

 	<?php
		$class = array();
		//$list = array('login', 'logout', 'chart');
		//if($currpage == 'users' && !in_array($this->request->params['action'], $list)) { $class=array('class'=>'active_tab'); $sub=1;}
		if ($userprops['User']['group_id'] == 1){
			if($currpage == 'users'){ $class=array('class'=>'active_tab'); $sub=1;}
	 			echo $this->Html->link(__('Users'), array('controller'=>'users','action' => 'index'), $class);
		}
		else
		{
			if($currpage == 'users'){ $class=array('class'=>'active_tab'); $sub=1;}
				echo $this->Html->link(__('Change Password'), array('controller'=>'users','action' => 'change_password'), $class);
		}

 	 ?>
 	 <?php
 		$class = array();
 		if ($userprops['User']['group_id'] == 1){
			if($currpage == 'groups'){ $class=array('class'=>'active_tab'); $sub=2;}
	 		echo $this->Html->link(__('Groups'), array('controller' => 'groups', 'action' => 'index'), $class);
 		}
 	?>
	<?php echo $this->Html->link(__('Log Out'),array('controller' => 'users','action' => 'logout'), array('class'=>'logout')); ?>
</div>
<div class='sub_tab'>
	<?php
		switch($sub){
			case 1:
				if ($userprops['User']['group_id'] == 1){
					echo $this->Html->link(__('List'), array('controller'=>'users','action' => 'index'));
					echo $this->Html->link(__('Add'), array('controller'=>'users','action' => 'add'));
				}
				break;
			case 2:
				echo $this->Html->link(__('List'), array('controller'=>'groups','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'groups','action' => 'add'));
				break;
			case 3:
				echo $this->Html->link(__('List'), array('controller'=>'alerts','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'alerts','action' => 'add'));
				echo $this->Html->link(__('Logs'),'http://www.pikefin.com/php/viewlogs/parent.php',array('target'=>'blank'));
				break;
			case 4:
				echo $this->Html->link(__('List'), array('controller'=>'schedules','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'schedules','action' => 'add'));
				echo $this->Html->link("Turn Off All Schedules", array('action'=>'turnoff'));
				echo $this->Html->link(__('Set Default Configration'), array('controller' => 'schedules', 'action' => 'set_default_schedules'));
				break;
			case 5:
				echo $this->Html->link(__('List'), array('controller'=>'entities','action' => 'index'));
				if ($userprops['User']['group_id'] == 1) {
					echo $this->Html->link(__('Add'), array('controller'=>'entities','action' => 'add'));
				}
				break;
			case 6:
				echo $this->Html->link(__('List'), array('controller'=>'entity_groups','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'entity_groups','action' => 'add'));
				break;
			case 7:
				/*echo "<a href='#'>Main</a>";
				echo "<a href='#'>Approaching Fired</a>";
				echo "<a href='#'>Task Execution Times</a>";*/
				echo $this->Html->link(__('Main'), array('controller'=>'charts','action' => 'chart'));
				echo $this->Html->link(__('Approaching Fired'), array('controller'=>'charts','action' => 'charta'));
				if ($userprops['User']['group_id'] == 1) {
					echo $this->Html->link(__('Task Execution Times'), array('controller'=>'charts','action' => 'chartb'));
					echo $this->Html->link(__('Task Execution Times Detail'), array('controller'=>'charts','action' => 'charte'));

				}
				break;
			case 8:
				echo $this->Html->link(__('List'), array('controller'=>'tasks','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'tasks','action' => 'add'));
				break;
			case 9:
				echo $this->Html->link(__('List'), array('controller'=>'jobs','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'jobs','action' => 'add'));
				break;
			case 10:
				echo $this->Html->link(__('List'), array('controller'=>'extract_tables','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'extract_tables','action' => 'add'));
				break;
			case 11:
				echo $this->Html->link(__('List'), array('controller'=>'extract_singles','action' => 'index'));
				echo $this->Html->link(__('Add'), array('controller'=>'extract_singles','action' => 'add'));
				break;
		}
	?>
</div>