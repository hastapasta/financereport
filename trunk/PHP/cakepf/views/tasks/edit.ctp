<div class="tasks form">
<?php echo $this->Form->create('Task');?>
	<fieldset>
 		<legend><?php __('Edit Task'); ?></legend>
	<?php
		echo $this->Form->input('id');
		echo $this->Form->input('name');
		echo $this->Form->input('description');
		echo $this->Form->input('job_primary_key1');
		echo $this->Form->input('job_primary_key2');
		echo $this->Form->input('job_primary_key3');
		echo $this->Form->input('job_primary_key4');
		echo $this->Form->input('job_primary_key5');
		echo $this->Form->input('job_primary_key6');
		echo $this->Form->input('job_primary_key7');
		echo $this->Form->input('job_primary_key8');
		echo $this->Form->input('job_primary_key9');
		echo $this->Form->input('job_primary_key10');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>

		<li><?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $this->Form->value('Task.id')), null, sprintf(__('Are you sure you want to delete # %s?', true), $this->Form->value('Task.id'))); ?></li>
		<li><?php echo $this->Html->link(__('List Tasks', true), array('action' => 'index'));?></li>
	</ul>
</div>