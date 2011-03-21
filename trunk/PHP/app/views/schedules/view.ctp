<div class="schedules view">
<h2><?php  __('Actions');?></h2>
<?php echo $this->element('actions',array('title'=>'Schedule')); ?>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Obsolete Data Set'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php  echo $schedule['Schedule']['obsolete_data_set']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Task'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Task']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Start Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['Start_Date']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Repeat Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['RepeatType']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Last Run'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_last_run']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Run Once'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_run_once']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Priority'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['priority']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Companygroup'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_companygroup']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Configure Notification'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['configure_notification']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Referencegroup'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $schedule['Schedule']['obsolete_referencegroup']; ?>
	&nbsp;</dd>
</dl>
</div>
