<div class="schedules form"><?php echo $this->Form->create('Schedule');?>
<fieldset><legend><?php __('Add Schedule'); ?></legend> <?php
echo $this->Form->input('obsolete_data_set');
echo $this->Form->input('task_id');
echo $this->Form->input('Start_Date');
echo $this->Form->input('repeat_type_id');
echo $this->Form->input('last_run');
echo $this->Form->input('run_once');
echo $this->Form->input('priority');
echo $this->Form->input('companygroup');
echo $this->Form->input('configure_notification');
echo $this->Form->input('referencegroup');
?></fieldset>
<?php echo $this->Form->end(__('Submit', true));?></div>
<?php echo $this->element('actions'); ?>


