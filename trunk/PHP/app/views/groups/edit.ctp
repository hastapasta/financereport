<div class="groups form">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('Group');
for($i = 0; $i < sizeof($this->data['Group']); $i++){
	?>
<fieldset><legend><?php __('Edit Group'); ?></legend> <?php
echo $this->Form->hidden('Group.'.$i.'.id');
echo $this->Form->input('Group.'.$i.'.name');
?></fieldset>
<?php }?> <?php echo $this->Form->end(__('Submit', true));?></div>