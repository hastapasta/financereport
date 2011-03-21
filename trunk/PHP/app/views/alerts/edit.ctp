<div class="alerts form">
<?php echo $this->element('actions'); ?>
<?php echo $this->Form->create('Alert');	

$this->log('in edit view',LOG_DEBUG);



for($i = 0; $i < sizeof($this->data['Alert']); $i++){
	?>
<fieldset><legend><?php __('Edit Alert'); ?></legend> <?php
//echo $this->Form->hidden('id');
//echo $this->Form->input('type');

//echo $this->Form->input('schedule');

echo $this->Form->input('Alert.'.$i.'.id', array('type' => 'hidden'));
echo $this->Form->input('Alert.'.$i.'.schedule_id',array('label'=>'Schedule Name','options' => $this->getVar('task_names')));
$tickers1 = $this->getVar('tickers1');
//$ticker1 = $tickers['Alert.'.$i.'.entity_id'];



echo $this->Form->input('Entity.'.$i.'.ticker',array('label'=>'Ticker','default'=>$tickers1[$this->data['Alert'][$i]['entity_id']]));

echo $this->Form->input('Alert.'.$i.'.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
//echo $this->Form->input('user');
//echo $this->Form->input('names');
echo $this->Form->input('Alert.'.$i.'.frequency',array('label'=>'Frequency','options' => $this->getVar('frequencies')));
//echo $this->Form->input('frequency');
echo $this->Form->input('Alert.'.$i.'.limit_value');
echo $this->Form->input('Alert.'.$i.'.limit_adjustment');
//echo $this->Form->input('fact_data_key');

//echo $this->Form->input('alert_count');
echo $this->Form->input('Alert.'.$i.'.disabled');

?></fieldset>
<?php }?> <?php echo $this->Form->end(__('Submit', true));?></div>
