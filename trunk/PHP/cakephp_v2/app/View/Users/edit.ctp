<div class="users form">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('User');	 
if ($this->getVar('administrator') == true)
{
	for($i = 0; $i < sizeof($this->request->data['User']); $i++)
	{
	?>
<fieldset><legend><?php echo __('Edit User '.$i); ?></legend> <?php
echo $this->Form->hidden('User.'.$i.'.id');
echo $this->Form->input('User.'.$i.'.username');
echo $this->Form->input('User.'.$i.'.password');
echo $this->Form->input('User.'.$i.'.email');
echo $this->Form->input('User.'.$i.'.max_alerts');
echo $this->Form->input('User.'.$i.'.group_id');
?></fieldset>
<?php }
 }
 else 
 {
 for($i = 0; $i < sizeof($this->request->data['User']); $i++)
 {
 	/*
 	 * Need to add more security here. Techincally someone could overwrite the hidden id parameter
 	 * and modify another users password.
 	 * 
 	 */
	?>
<fieldset><legend><?php echo __('Edit User '.$i); ?></legend> <?php
echo $this->Form->hidden('User.'.$i.'.id');
echo $this->Form->input('User.'.$i.'.username',array('disabled' => true));
echo $this->Form->input('User.'.$i.'.password');
echo $this->Form->input('User.'.$i.'.email',array('disabled' => true));
//echo $this->Form->input('User.'.$i.'.max_alerts');
//echo $this->Form->input('User.'.$i.'.group_id',array('disabled' => true));
?></fieldset>
<?php }
 	
 }
 
 ?> 

<?php echo $this->Form->end(__('Submit'));?></div>
