<div class="groups form">
<?php echo $this->element('actions'); ?>
<?php echo $this->Form->create('Group');?>
<fieldset><legend><?php __('Add Group'); ?></legend> <?php
echo $this->Form->input('name');
?></fieldset>
<?php echo $this->Form->end(__('Submit', true));?></div>