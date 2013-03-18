<?php $group = $this->Session->read('Auth.User.group_id'); ?>
<div class="actions">
<?php //if(isset($title)):?>
<h3><?php //__($title); ?></h3>
<?php //else:?>
<h3><?php //__('Actions'); ?></h3>
<?php //endif; ?>
<ul id="topAction">
<?php if($group == 1): ?>
	<li><?php echo $this->Html->link(__('List Users'), array('controller'=>'users','action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New User'), array('controller'=>'users','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Groups'), array('controller' => 'groups', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Group'), array('controller' => 'groups', 'action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Alerts'), array('controller' => 'alerts', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Alert'), array('controller' => 'alerts', 'action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Schedules'), array('controller' => 'schedules', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Schedule'), array('controller' => 'schedules','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('List Entities'), array('controller' => 'entities', 'action' => 'index')); ?></li>
	<li><?php echo $this->Html->link(__('New Entity'), array('controller' => 'entities','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('Charts'), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Log Out'),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
	<?php if($group == 2): ?>
	<li><?php echo $this->Html->link(__('List Alerts'), array('controller' => 'alerts', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Alert'), array('controller' => 'alerts', 'action' => 'add')); ?>
	</li>
	<li><?php echo $this->Html->link(__('List Schedules'), array('controller' => 'schedules', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Schedule'), array('controller' => 'schedules','action' => 'add')); ?></li>
	<li><?php echo $this->Html->link(__('Charts'), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Log Out'),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
	<?php if($group == 3): ?>
	<li><?php echo $this->Html->link(__('List Alerts'), array('controller' => 'alerts', 'action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Alert'), array('controller' => 'alerts', 'action' => 'add')); ?>
	</li>	
	<li><?php echo $this->Html->link(__('Charts'), array('controller' => 'users','action' => 'chart')); ?></li>
	<li><?php echo $this->Html->link(__('Change Password'), array('controller' => 'users', 'action' => 'change_password')); ?></li>	
	<li><?php echo $this->Html->link(__('Log Out'),array('controller' => 'users','action' => 'logout')); ?></li>
	<?php endif; ?>
</ul>
</div>
