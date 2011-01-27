<div class="schedules form">
<?php echo $this->Form->create('Schedule');?>
	<fieldset>
 		<legend><?php __('Add Schedule'); ?></legend>
	<?php
		echo $this->Form->input('obsolete_data_set');
		echo $this->Form->input('task');
		echo $this->Form->input('Start_Date');
		echo $this->Form->input('Repeat_Type');
		echo $this->Form->input('last_run');
		echo $this->Form->input('id');
		echo $this->Form->input('run_once');
		echo $this->Form->input('priority');
		echo $this->Form->input('companygroup');
		echo $this->Form->input('configure_notification');
		echo $this->Form->input('referencegroup');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>

		<li><?php echo $this->Html->link(__('List Users', true), array('controller' => 'users', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Groups', true), array('controller' => 'groups', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Schedules', true), array('controller' => 'schedules', 'action' => 'index')); ?> </li>
	</ul>
</div>


