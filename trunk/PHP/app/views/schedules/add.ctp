<div class="schedules form">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('Schedule');?>
<fieldset><legend><?php __('Add Schedule'); ?></legend> <?php

echo $this->Form->input('task_id');
echo $this->Form->input('Start_Date');
echo $this->Form->input('repeat_type_id');
echo $this->Form->input('verify_mode');
echo $this->Form->input('priority',array('default'=>'0'));
echo $this->Form->input('configure_notification');

?></fieldset>
<?php echo $this->Form->end(__('Submit', true));?></div>


