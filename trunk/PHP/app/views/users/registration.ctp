<h2>Registration</h2>
<?php echo  $this->Javascript->link('validation_rules');?>
<?php
echo $this->Form->create('User', array('action' =>'registration','onsubmit'=>'return validate();'))."\n";
echo $this->Form->input('User.email',array('class'=>'validEmail'))."\n";
echo $this->Form->input('User.username',array('class'=>'notEmpty'))."\n";
echo $this->Form->input('User.password',array('class'=>'notEmpty'))."\n";
echo $this->Form->input('User.confirm_password',array('type'=>'password','class'=>'notEmpty'))."\n";
echo $this->Form->submit('Submit')."\n";
echo $this->Form->end(null)."\n";
?>