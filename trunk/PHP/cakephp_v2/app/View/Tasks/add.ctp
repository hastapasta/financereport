<div class="tasks form">
<?php echo $this->Form->create('Task');?>
	<fieldset>
		<legend><?php echo __('Add Task'); ?></legend> 
	<?php
		echo $this->Form->input('name');
		echo $this->Form->input('description');
		echo $this->Form->input('use_group_for_reading');
		echo $this->Form->input('eps_est_priority');
		echo $this->Form->input('source');
		echo $this->Form->input('metric_id');
?></fieldset>
<?php echo $this->Form->end(__('Submit'));?></div>

