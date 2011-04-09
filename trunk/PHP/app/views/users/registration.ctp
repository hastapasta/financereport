<h2>Registration</h2>
<?php
echo $this->Form->create('User', array('action' =>'registration','onsubmit'=>'return validate();'));
echo $this->Form->input('User.email',array('class'=>'validEmail'));
echo $this->Form->input('User.username',array('class'=>'notEmpty'));
echo $this->Form->input('User.password',array('class'=>'notEmpty'));
echo $this->Form->input('User.confirm_password',array('type'=>'password','class'=>'notEmpty'));
echo $this->Form->submit('Submit');
echo $this->Form->end(null);
?>