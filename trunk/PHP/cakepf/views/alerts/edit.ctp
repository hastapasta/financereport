<div class="alerts form">
<?php echo $this->Form->create('Alert');?>
	<fieldset>
 		<legend><?php __('Edit Alert'); ?></legend>
	<?php
		//echo $this->Form->input('id');
		//echo $this->Form->input('type');
		//debug($this->Form);
		//echo $this->Form->input('schedule');
		echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','options' => $this->getVar('task_names')));
		echo $this->Form->input('ticker');
		echo $this->Form->input('Alert.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
		//echo $this->Form->input('user');
		//echo $this->Form->input('names');
		echo $this->Form->input('Alert.frequency',array('label'=>'Frequency','options' => $this->getVar('frequencies')));
		//echo $this->Form->input('frequency');
		echo $this->Form->input('limit_value');
		//echo $this->Form->input('limit_adjustment');
		//echo $this->Form->input('fact_data_key');
		
		//echo $this->Form->input('alert_count');
		echo $this->Form->input('disabled');
		
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>

		<li><?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $this->Form->value('Alert.id')), null, sprintf(__('Are you sure you want to delete # %s?', true), $this->Form->value('Alert.id'))); ?></li>
		<li><?php echo $this->Html->link(__('List Alerts', true), array('action' => 'index'));?></li>
	</ul>
</div>
