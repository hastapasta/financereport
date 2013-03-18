<div class="schedules view">
<?php //echo $this->element('actions',array('title'=>'Schedule')); ?>
<div style='clear:both'></div>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Obsolete Data Set'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php  echo $schedule['Schedule']['obsolete_data_set']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Task'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Task']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Task Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $this->Html->link($schedule['Task']['id'], array('controller' => 'tasks', 'action' => 'view', $schedule['Task']['id'])); ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Start Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['Start_Date']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Repeat Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['RepeatType']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Verify Mode'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['verify_mode']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Last Run'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_last_run']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Run Once'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_run_once']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Priority'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['priority']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Configure Notification'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['configure_notification']; ?>
	&nbsp;</dd>
</dl>
</div>
