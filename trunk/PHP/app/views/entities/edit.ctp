<div class="entities form">
<?php echo $this->element('actions'); ?>
<?php echo $this->Form->create('Entity');
for($i = 0; $i < sizeof($this->data['Entity']); $i++){
?>
<fieldset><legend><?php __('Edit Entity'); ?></legend> <?php
echo $this->Form->input('Entity.'.$i.'.id',array('type'=>'hidden'));
echo $this->Form->input('Entity.'.$i.'.ticker');
echo $this->Form->input('Entity.'.$i.'.shares_outstanding');
echo $this->Form->input('Entity.'.$i.'.begin_fiscal_calendar');
echo $this->Form->input('Entity.'.$i.'.last_reported_quarter');
echo $this->Form->input('Entity.'.$i.'.next_report_date');
echo $this->Form->input('Entity.'.$i.'.groups');
echo $this->Form->input('Entity.'.$i.'.actual_fiscal_year_end');
echo $this->Form->input('Entity.'.$i.'.full_name');
echo $this->Form->input('Entity.'.$i.'.depricated');
?></fieldset>
<?php } 
echo $this->Form->end(__('Submit', true));?>
</div>
