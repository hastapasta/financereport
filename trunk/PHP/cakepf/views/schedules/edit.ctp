<div class="schedules form">
<?php echo $this->Form->create('Schedule');?>
	<fieldset>
 		<legend><?php __('Edit Schedule'); ?></legend>
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

		<li><?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $this->Form->value('Schedule.id')), null, sprintf(__('Are you sure you want to delete # %s?', true), $this->Form->value('Schedule.id'))); ?></li>
		<li><?php echo $this->Html->link(__('List Schedules', true), array('action' => 'index'));?></li>
	</ul>
</div>
