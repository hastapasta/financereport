<div class="alerts index">
<h2><?php __('Alerts');?></h2>
<script type="text/javascript">

	

	</script> <?php
	//debug($this,true);
	echo $form->create('Alert',array('controller'=>'alerts','action'=>'action_process','class'=>'recordForm'));
	echo $form->hidden('Alert.action_value',array('id'=>'actionValue'));
	?>
<table cellpadding="0" cellspacing="0">
	<tr>
		
		<th><?php echo $this->Paginator->sort('Schedule Name','Schedule.Task.name');?></th>
		<th><?php echo $this->Paginator->sort('Ticker','Entity.ticker');?></th>
		<th><?php echo $this->Paginator->sort('Description','Entity.full_name');?></th>
		<th><?php echo $this->Paginator->sort('frequency');?></th>
		<th><?php echo $this->Paginator->sort('limit_value');?></th>
		<th><?php echo $this->Paginator->sort('limit_adjustment');?></th>
		
		<th><?php echo $this->Paginator->sort('alert_count');?></th>
		<th><?php echo $this->Paginator->sort('disabled');?></th>
		<th><?php echo $this->Paginator->sort('user');?></th>
		<th class="actions"><?php __('Actions');?></th>
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php

	if (1==1)
	{
		$i = 0;
		$j = 0;
		//debug($alerts);
		foreach ($alerts as $alert):
		$class = null;
		if ($i++ % 2 == 0) {
			$class = ' class="altrow"';
		}
		//debug($alert);


		?>


	<tr <?php echo $class;?>>
	
		<td><?php if(isset($alert['Schedule']['Task']['name'])){
			echo $alert['Schedule']['Task']['name'];
		} ?>&nbsp;</td>
		<td><?php echo $alert['Entity']['ticker']; ?>&nbsp;</td>
		<td><?php echo $alert['Entity']['full_name']; ?>&nbsp;</td>
	
		<td><?php echo $alert['Alert']['frequency']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['limit_value']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['limit_adjustment']; ?>&nbsp;</td>

		
		<td><?php echo $alert['Alert']['alert_count']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['disabled']; ?>&nbsp;</td>
		<td><?php echo $alert['User']['username']; ?>&nbsp;</td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $alert['Alert']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('Alert'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$alert['Alert']['id']));
		?></td>
	</tr>
	<?php endforeach;
	}
	?>
	<tr>
		<td class="actions_button" colspan="14" style="text-align: right">
		<?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

		<?php echo $html->link(__('Delete', true),'#', array('class'=>'deleteButton')); ?>
		</td>
	</tr>
</table>
<p><?php 
echo $this->Form->end();
echo $this->Paginator->counter(array(
		'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
));
?></p>

<?php echo $this->element('paginate'); ?>

<?php 
	echo $this->Form->create('Alert',array('controller'=>'alerts','name' => 'SearchForm','action'=>'action_process','class'=>'filterForm'));
	echo $this->Form->hidden('action_value',array('id'=>'actionValue','value'=>'3'));
	
	$tickerdefault = "";
	$scheduledefault="";
	$frequencydefault="";
	$checkboxdefault=false;
	
	if ($this->Session->read('FilterValues') != null)
	{
			$filtervalues = $this->Session->read('FilterValues');
			//debug($filtervalues);
			if (!empty($filtervalues['Alert']))
			{

				$tickerdefault = $filtervalues['Entity']['ticker'];
				$scheduledefault = $filtervalues['Alert']['schedule_id'];
				$frequencydefault = $filtervalues['Alert']['frequency'];
				if ($filtervalues['Alert']['filtersenabled']=='1')
				{
					$checkboxdefault=true;
				}
			}
		
	}
	echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','options' => $this->getVar('task_names2'),'selected'=>$scheduledefault));
	echo $this->Form->input('Entity.ticker',array('label'=>'Ticker','type'=>'text','value'=>$tickerdefault));
	echo $this->Form->input('frequency',array('label'=>'Frequency','options' => $this->getVar('frequencies'),'selected'=>$frequencydefault));
	echo ' Filters enabled:';
	echo $this->Form->checkbox('filtersenabled', array('value' => '0','checked'=>$checkboxdefault));
	//echo $this->Form->checkbox('filtersenabled', array('value' => 'test'));
	/*if ($this->Session->read('FilterEnabled') == null || $this->Session->read('FilterEnabled') == false)
		echo $this->Form->checkbox('filtersenabled', array('value' => '1','checked'=>false));
	else
		echo $this->Form->checkbox('filtersenabled', array('value' => '0','checked'=>true));*/
	echo $this->Form->end(__('Set Filters', true));
	

	
	//echo $this->Form->end(__('Filter', true));
	//echo $html->link(__('Filter', true),'#', array('class'=>'filterButton'));
	//echo $this->Form->end();
	?> 


</div>



<?php echo $this->element('actions'); ?>

