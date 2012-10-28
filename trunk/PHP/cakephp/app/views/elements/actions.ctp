<?php $group = $session->read('Auth.User.group_id'); ?>
<div class="actions">
<?php //if(isset($title)):?>
<h3><?php //__($title); ?></h3>
<?php //else:?>
<h3><?php //__('Actions'); ?></h3>
<?php //endif; ?>
<ul id="topAction">
<?php if($group == 1): ?>
	<li><?php echo $this->Html->link(__('List Users', true), array('controller'=>'users','action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New User', true), array('controller'=>'users','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Groups', true), array('controller' => 'groups', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Group', true), array('controller' => 'groups', 'action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Alert', true), array('controller' => 'alerts', 'action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Schedules', true), array('controller' => 'schedules', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Schedule', true), array('controller' => 'schedules','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Entities', true), array('controller' => 'entities', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Entity', true), array('controller' => 'entities','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('Charts', true), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Log Out',true),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
	<?php if($group == 2): ?>
	<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Alert', true), array('controller' => 'alerts', 'action' => 'add')); ?>
	</li>
	<li><?php echo $this->Html->link(__('List Schedules', true), array('controller' => 'schedules', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Schedule', true), array('controller' => 'schedules','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('Charts', true), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Log Out',true),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
	<?php if($group == 3): ?>
	<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Alert', true), array('controller' => 'alerts', 'action' => 'add')); ?>
	</li>	
	<li><?php echo $this->Html->link(__('Charts', true), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Change Password', true), array('controller' => 'users', 'action' => 'change_password')); ?></li>	
	<li><?php echo $this->Html->link(__('Log Out',true),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
</ul>
</div>
