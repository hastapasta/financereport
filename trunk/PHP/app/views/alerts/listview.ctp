<div class="alerts view">
<?php 
	for($j = 0; $j < sizeof($alerts); $j++ ):
	$alert = $alerts[$j];
?>
<div style='clear:both'></div>

<dl>
<?php $i = 0; $class = ' class="altrow"';?>

	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['ticker']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Description'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['full_name']; ?>
	<?php //echo "<div id='tickerval' style=\"display: none\">".$alert['Entity']['ticker']."</div>"?>
	<?php //echo "<div id='taskidval' style=\"display: none\">".$alert['Schedule']['task_id']."</div>"?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Task Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php if(isset($alert['Task']['name']))
	echo $alert['Task']['name'];
	?> &nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('User'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['User']['username']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Observation Period'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['TimeEvent']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Limit Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['limit_value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Initial Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Current Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Initial Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Current Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Auto Reset Fired'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['auto_reset_fired']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Auto Reset Period'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['auto_reset_period']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Email Alert'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['email_alert']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Twitter Alert'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['twitter_alert']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Notification Count'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['notification_count']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Fired'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['fired']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Disabled'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['disabled']; ?>
	&nbsp;</dd>
</dl>

<div style="width:100%; border-top:2px solid black;margin-bottom:15px;margin-top:15px;"></div>
<?php endfor; ?>
</div>

