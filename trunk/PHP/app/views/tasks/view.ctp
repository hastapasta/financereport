<div class="tasks view">
<h2><?php  __('Task');?></h2>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $task['Task']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $task['Task']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Description'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $task['Task']['description']; ?>
	&nbsp;</dd>
</dl>
</div>
<div class="actions">
<h3><?php __('Actions'); ?></h3>
<ul>
	<li><?php echo $this->Html->link(__('Edit Task', true), array('action' => 'edit', $task['Task']['id'])); ?>
	</li>
	<li><?php echo $this->Html->link(__('Delete Task', true), array('action' => 'delete', $task['Task']['id']), null, sprintf(__('Are you sure you want to delete # %s?', true), $task['Task']['id'])); ?>
	</li>
	<li><?php echo $this->Html->link(__('List Tasks', true), array('action' => 'index')); ?>
	</li>
	<li><?php echo $this->Html->link(__('New Task', true), array('action' => 'add')); ?>
	</li>
</ul>
</div>
