<script type="text/javascript">
$(function(){
	$('#registered').click(function(){		
		Window.href,location="<?php echo $this->base; ?>/users/registration";
		});
});
</script>
<h2>Login</h2>
<?php
//debug($this->getVar('urlrequest'),true);
echo $this->Form->create('User', array('url' => array('controller' => 'users', 'action' =>'login')));
echo $this->Form->input('User.username');
echo $this->Form->input('User.password');
?>
<ul id="logIn">
  <li><?php echo $this->Form->button('Login',array('class'=>'registration')); ?></li>
  <li><?php echo $this->Form->button('Registration',array('class'=>'registration','type'=>'button','id'=>'registered')); ?></li>  
</ul>
<?php 
echo $this->Form->end(null);
?>

