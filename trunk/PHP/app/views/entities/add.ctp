<div class="entities form">
<?php echo $this->element('actions'); ?>
<?php echo $this->Form->create('Entity');?>
<fieldset><legend><?php __('Add Entity'); ?></legend> <?php
echo $this->Form->input('ticker');
echo $this->Form->input('shares_outstanding');
echo $this->Form->input('begin_fiscal_calendar');
echo $this->Form->input('last_reported_quarter');
echo $this->Form->input('next_report_date');
echo $this->Form->input('groups');
echo $this->Form->input('actual_fiscal_year_end');
echo $this->Form->input('full_name');
echo $this->Form->input('depricated');
?></fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>