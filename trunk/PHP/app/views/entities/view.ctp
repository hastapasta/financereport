<div class="entities view">
<?php //echo $this->element('actions',array('title'=>'Entity'))
$userprops = $this->getVar('user_props');
?>
<div style='clear:both'></div>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['ticker']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Shares Outstanding'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['shares_outstanding']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Begin Fiscal Calendar'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['begin_fiscal_calendar']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Last Reported Quarter'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['last_reported_quarter']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Next Report Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['next_report_date']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Actual Fiscal Year End'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['actual_fiscal_year_end']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Full Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['full_name']; ?>
	&nbsp;</dd>
</dl>
</div>


