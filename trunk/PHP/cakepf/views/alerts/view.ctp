<div class="alerts view">
<h2><?php  __('Alert');?></h2>
<dl>
<?php $this->set('jsIncludes',array('google'));   // this will link to /js/google.js ?>
<?php $i = 0; $class = ' class="altrow"';?>

	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Task Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php if(isset($alert['Schedule']['Task']['name']))
	echo $alert['Schedule']['Task']['name'];
	?> &nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('User'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['User']['username']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Frequency'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['frequency']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Limit Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['limit_value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Limit Adjustment'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['limit_adjustment']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Fact Data Key'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['fact_data_key']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['ticker']; ?>
	<?php echo "<div id='tickerval' style=\"display: none\">".$alert['Entity']['ticker']."</div>"?>
	<?php echo "<div id='taskidval' style=\"display: none\">".$alert['Schedule']['task_id']."</div>"?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Alert Count'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['alert_count']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Disabled'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['disabled']; ?>
	&nbsp;</dd>
</dl>
<div id="chart-div"></div>
</div>

	<?php echo $this->element('actions'); ?>
