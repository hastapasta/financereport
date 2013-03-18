<div class="alerts view">
<fieldset>
 		<legend><?php echo __('Alerts Created ('.sizeof($alerts).')'); ?></legend>
<?php 
	for($j = 0; $j < sizeof($alerts); $j++ ):
	$alert = $alerts[$j];
?>
<div style='clear:both'></div>

<dl>
<?php $i = 0; $class = ' class="altrow"';?>

	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['ticker']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Description'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['full_name']; ?>
	<?php //echo "<div id='tickerval' style=\"display: none\">".$alert['Entity']['ticker']."</div>"?>
	<?php //echo "<div id='taskidval' style=\"display: none\">".$alert['Schedule']['task_id']."</div>"?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Task Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php if(isset($alert['Task']['name']))
	echo $alert['Task']['name'];
	?> &nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('User'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['User']['username']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Observation Period'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['TimeEvent']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Limit Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['limit_value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Calendar Year'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['calyear']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Initial Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Current Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Initial Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Current Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Auto Reset Fired'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['auto_reset_fired']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Auto Reset Period'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['auto_reset_period']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Email Alert'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['email_alert']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Twitter Alert'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['twitter_alert']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Notification Count'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['notification_count']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Fired'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['fired']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Disabled'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['disabled']; ?>
	&nbsp;</dd>
</dl>

<div style="width:100%; border-top:2px solid black;margin-bottom:15px;margin-top:15px;"></div>
<?php endfor; ?>
</fieldset>
</div>

