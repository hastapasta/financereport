<div class="tasks form">
<?php echo $this->Form->create('Task');?>
	<?php for($i = 0; $i < sizeof($this->data['Task']); $i++){ ?>
	<fieldset>
 		<legend><?php __('Edit Task'); ?></legend>
 	
	<?php
		echo $this->Form->input( "Task.$i.id");
		echo $this->Form->input("Task.$i.name");
		echo $this->Form->input("Task.$i.description");
		echo $this->Form->input("Task.$i.use_group_for_reading");
		echo $this->Form->input("Task.$i.eps_est_priority");
		echo $this->Form->input("Task.$i.source");
		echo $this->Form->input("Task.$i.metric_id");
	?>
	</fieldset>
	<?php }?>
	
<?php echo $this->Form->end(__('Submit', true));?>
</div>