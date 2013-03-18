<div class="groups view">
<?php //echo $this->element('actions',array('title'=>'Group')); ?>
<div style='clear:both'></div>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $group['Group']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $group['Group']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Created'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $group['Group']['created']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Modified'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $group['Group']['modified']; ?>
	&nbsp;</dd>
</dl>
</div>
