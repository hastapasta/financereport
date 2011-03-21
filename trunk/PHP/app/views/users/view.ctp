<div class="users view">
<h2><?php  __('Actions');?></h2>
<?php echo $this->element('actions',array('title'=>'User')); ?>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Username'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['username']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Password'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['password']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Email'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['email']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Group'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $this->Html->link($user['Group']['name'], array('controller' => 'groups', 'action' => 'view', $user['Group']['id'])); ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Created'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['created']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Modified'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $user['User']['modified']; ?>
	&nbsp;</dd>
</dl>
</div>
