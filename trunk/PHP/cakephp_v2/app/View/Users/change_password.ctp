<div class="users none">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('User',array('action'=>'change_password'));?>	 
<fieldset>
	<legend><?php echo __('Change Password'); ?></legend> 
	<?php
		echo $this->Form->input('User.old_password',array('type'=>'password'));
		echo $this->Form->input('User.new_password',array('type'=>'password'));
		echo $this->Form->input('User.confirm_password',array('type'=>'password'));
	?>
</fieldset>
<?php echo $this->Form->end(__('Submit'));?></div>
