<table>
<tr>
<td>
<table class="searchTable" cellspacing="0">
	<?php 
		echo $this->Form->create('Alert',array('controller'=>'alerts','name' => 'SearchForm',
												'action'=>'index','class'=>'filterForm'));
		//echo $this->Form->hidden('action_value_search',array('id'=>'actionValueSearch','value'=>'3'));
		$tickerdefault = "";
		$fullnamedefault = "";
		$taskdefault="";
		$metricdefault="";
		$timeeventdefault="";
		$userdefault = "";
		$checkboxdefault=false;
		$checkboxdefault1=false;
		$fired = false;
		//$filtervalues = $this->data;
		$filtervalues = $this->Session->read('alertfiltervalues');
		$initial = false;
	
		if ($filtervalues != null){
			
			
				//if (!empty($filtervalues['Alert'])){
					
					$tickerdefault = $filtervalues['Entity']['ticker'];
					//$taskdefault = $filtervalues['Alert']['task_id'];
					$metricdefault = $filtervalues['Alert']['metric_id'];
					$fullnamedefault = $filtervalues['Entity']['full_name'];
					
					$timeeventdefault = $filtervalues['Alert']['time_event_id'];
					$userdefault = $filtervalues['Alert']['user_id'];
					if ($filtervalues['Alert']['filtersenabled']=='1'){
						$checkboxdefault=true;
					}
					if ($filtervalues['Alert']['filtersdisabled']=='1'){
						$checkboxdefault1=true;
					}
					if ($filtervalues['Alert']['filtersinitial']=='1'){
						$initial=true;
					}
					if($filtervalues['Alert']['filtersfired'] == '1')
						$fired = true;
					
				//}
		}
	?>
	<!-- <tr>
		<td><?php 
		
		//echo $this->Form->input('task_id',array('label'=>'Task Name:','options' => $this->getVar('task_names2'),'selected'=>$taskdefault)); ?>
		</td>
	</tr> -->
	<tr>
		<td><?php 
		
		echo $this->Form->input('metric_id',array('label'=>'Metric:','options' => $this->getVar('metricnames'),'selected'=>$metricdefault,'empty'=>'All')); ?>
		</td>
	</tr>
	<?php if($this->Session->read('Auth.User.group_id') == 1):?>
	<tr>
		<td>
			
			<?php echo $this->Form->input('user_id', array('label'=>'User Name:', 'options'=>$this->getVar('users'), 'value'=>$userdefault,'empty'=>'All' ));?>
		</td>
	</tr>
	<?php endif;?>
	
	<tr>
		<td><?php echo $this->Form->input('Entity.ticker',array('label'=>'Ticker:','type'=>'text','value'=>$tickerdefault)); ?></td>
	</tr>
	<tr>
		<td><?php echo $this->Form->input('Entity.full_name',array('label'=>'Description:','type'=>'text','value'=>$fullnamedefault)); ?></td>
	</tr>
	
	<tr>
		<td><?php echo $this->Form->input('time_event_id',array('label'=>'Observation Period:','options' => $this->getVar('time_event_names'),'value'=>$timeeventdefault,'empty'=>'All')); ?></td>
	</tr>
	<tr>
		<td><?php echo $this->Form->label('Disabled:'); ?></td>
		<td><?php echo $this->Form->checkbox('filtersdisabled', array('label'=>'Disabled','value' => '0','checked'=>$checkboxdefault1)); ?></td>
	</tr>

	<tr>
		<td><?php echo $this->Form->label('Fired:');?></td>
		<td><?php echo $this->Form->checkbox('filtersfired', array('value' => '0','checked'=>$fired)); ?></td>
	</tr>
	<?php if($this->Session->read('Auth.User.group_id') == 1):?>
	<tr>
		<td><?php echo $this->Form->label('Zero Initial Value:');?></td>
		<td><?php echo $this->Form->checkbox('filtersinitial', array('value' => '0','checked'=>$initial)); ?></td>
	</tr>
	<?php endif;?>
	
	<tr>

		<td style="border-top:1px solid black;"><?php echo $this->Form->label('Filters Enabled:');?></td>
		<td style="border-top:1px solid black;"><?php echo $this->Form->checkbox('filtersenabled', array('value' => '0','checked'=>$checkboxdefault)); ?></td>
	</tr>
	<tr>
		<td><?php echo $this->Form->end(__('Refresh List', true)); ?></td>
	</tr>
</table>

</td>
<td>

<div class="alerts index" style='width:95%'>
<?php //echo $this->element('actions',array('title'=>'Alerts')); ?> 
<?php
	echo $this->Form->create('Alert',array('controller'=>'alerts','action'=>'action_process','class'=>'recordForm'));
	echo $this->Form->hidden('Alert.action_value',array('id'=>'actionValue'));
?>
<?php echo $this->element('check_uncheck_control')?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo $this->Paginator->sort('id');?></th>
		<th><?php echo $this->Paginator->sort('Task Name','Task.name');?></th>
		<th><?php echo $this->Paginator->sort('Ticker','Entity.ticker');?></th>
		<th><?php echo $this->Paginator->sort('Description','Entity.full_name');?></th>
		<th><?php echo $this->Paginator->sort('Observation Period','TimeEvent.name');?></th>
		<th><?php echo $this->Paginator->sort('limit_value');?></th>
		<th><?php echo $this->Paginator->sort('Calendar Year','calyear');?></th>
		<th><?php echo $this->Paginator->sort('notification_count');?></th>
		<th><?php echo $this->Paginator->sort('disabled');?></th>
		<th><?php echo $this->Paginator->sort('fired');?></th>
		<th><?php echo $this->Paginator->sort('user');?></th>
		<th class="actions"><?php //__('Actions');?></th>
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
	
		<td><?php echo $alert['Alert']['id']; ?>&nbsp;</td>
		<td><?php if(isset($alert['Task']['name'])){
			echo $alert['Task']['name'];
		} ?>&nbsp;</td>
		<td><?php echo $alert['Entity']['ticker']; ?>&nbsp;</td>
		<td><?php echo $alert['Entity']['full_name']; ?>&nbsp;</td>
		<td><?php echo $alert['TimeEvent']['name']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['limit_value']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['calyear']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['notification_count']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['disabled']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['fired'];?>&nbsp;</td>
		<td><?php echo $alert['User']['username']; ?>&nbsp;</td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $alert['Alert']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $this->Form->checkbox('Alert'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$alert['Alert']['id']));
		?></td>
	</tr>
	<?php endforeach;
	}
	?>
	<tr>
		<?php echo $this->element('pagelimit');?>
		<td class="actions_button" colspan="10" style="text-align: right">
		<?php echo $this->Html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

		<?php echo $this->Html->link(__('Delete', true),'#', array('class'=>'deleteButton')); ?>
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
</div>

</td>
</tr>
</table>


<?php 	
	
	
	//echo $this->Form->checkbox('filtersenabled', array('value' => 'test'));
	/*if ($this->Session->read('FilterEnabled') == null || $this->Session->read('FilterEnabled') == false)
		echo $this->Form->checkbox('filtersenabled', array('value' => '1','checked'=>false));
	else
		echo $this->Form->checkbox('filtersenabled', array('value' => '0','checked'=>true));*/
	
	

	
	//echo $this->Form->end(__('Filter', true));
	//echo $html->link(__('Filter', true),'#', array('class'=>'filterButton'));
	//echo $this->Form->end();
	?> 


