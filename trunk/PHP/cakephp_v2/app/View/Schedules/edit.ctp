<div class="schedules form">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('Schedule');
for($i = 0; $i < sizeof($this->request->data['Schedule']); $i++){
	?>
<fieldset><legend><?php echo __('Edit Schedule'); ?></legend> <?php

echo $this->Form->input('Schedule.'.$i.'.id', array('type' => 'hidden'));
echo $this->Form->input('Schedule.'.$i.'.task_id', array('label'=>'Task Name','options' => $this->getVar('task_names')));
echo $this->Form->input('Schedule.'.$i.'.repeat_type_id',array('label'=>'Repeat Type','options' => $this->getVar('repeat_types')));
echo $this->Form->input('Schedule.'.$i.'.verify_mode',array('label'=>'Verify Mode','options' => array(0=>"false",1=>"true")));
echo $this->Form->input('Schedule.'.$i.'.priority');
?></fieldset>
<?php } ?> <?php echo $this->Form->end(__('Submit'));?></div>
