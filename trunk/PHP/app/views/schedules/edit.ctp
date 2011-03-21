<div class="schedules form">
<?php echo $this->element('actions'); ?>
<?php echo $this->Form->create('Schedule');
for($i = 0; $i < sizeof($this->data['Schedule']); $i++){
	?>
<fieldset><legend><?php __('Edit Schedule'); ?></legend> <?php

echo $this->Form->input('Schedule.'.$i.'.id', array('type' => 'hidden'));
echo $this->Form->input('Schedule.'.$i.'.task_id', array('label'=>'Task Name','options' => $this->getVar('task_names')));
echo $this->Form->input('Schedule.'.$i.'.repeat_type_id',array('label'=>'Repeat Type','options' => $this->getVar('repeat_types')));
//echo $this->Form->input('Start_Date');
//echo $this->Form->input('Repeat_Type');
//echo $this->Form->input('last_run');
//echo $this->Form->input('id');
//echo $this->Form->input('run_once');
echo $this->Form->input('Schedule.'.$i.'.priority');
//echo $this->Form->input('companygroup');
//echo $this->Form->input('configure_notification');
//echo $this->Form->input('referencegroup');
?></fieldset>
<?php } ?> <?php echo $this->Form->end(__('Submit', true));?></div>
