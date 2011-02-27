<div class="tasks form"><?php echo $this->Form->create('Task');?>
<fieldset><legend><?php __('Add Task'); ?></legend> <?php
echo $this->Form->input('name');
echo $this->Form->input('description');
?></fieldset>
<?php echo $this->Form->end(__('Submit', true));?></div>
<div class="actions">
<h3><?php __('Actions'); ?></h3>
<ul>

	<li><?php echo $this->Html->link(__('List Tasks', true), array('action' => 'index'));?></li>
</ul>
</div>
