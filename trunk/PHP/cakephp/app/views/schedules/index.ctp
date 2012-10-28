<div class="schedules none">
<?php //echo $this->element('actions',array('title'=>'Schedules')); ?>
<table>
<tr>
<td style="width:10%;">
<table class="searchTable" cellspacing="0">
	<?php 
		echo $this->Form->create(
			'Schedule',
			array(
				'controller'=>'schedules',
				'name' => 'SearchForm',
				'action'=>'index',
				'class'=>'filterForm'
			)
		);
		$obsolete_data_set= "";
		$repeat_type_id="";
		$checkboxdefault=false;
		$verify_mode ="";
		$filtervalues = $this->Session->read('schedulefiltervalues');
		$initial = false;
		//print_r($filtervalues);exit;
		if ($filtervalues != null){

			$timeeventdefault = $filtervalues['Schedule']['repeat_type_id'];

			if ($filtervalues['Schedule']['filtersenabled']=='1'){
				$checkboxdefault=true;
			}

			if (!empty($filtervalues['Schedule']['obsolete_data_set'])){
				$obsolete_data_set=$filtervalues['Schedule']['obsolete_data_set'];
			}

			if (!empty($filtervalues['Schedule']['repeat_type_id'])){
				$repeat_type_id=$filtervalues['Schedule']['repeat_type_id'];
			}
			if (isset($filtervalues['Schedule']['custom_verify_mode']) && $filtervalues['Schedule']['custom_verify_mode'] != ''){
				$verify_mode = $filtervalues['Schedule']['custom_verify_mode'];
			}
			//if ($filtervalues['Schedule']['verify_mode']=='1'){
			//	$verify_mode=true;
			//}
		}
	?>
	<tr>
		<td>
			<?php echo $this->Form->input('obsolete_data_set',array('label'=>'Task Name:','type'=>'text','value'=>$obsolete_data_set)); ?>
		</td>
	</tr>
	<tr>
		<td>
		<?php echo $this->Form->input('repeat_type_id',array('label'=>'Repeat Type:','options' => $this->getVar('repeatTypeId'),'selected'=>$repeat_type_id,'empty'=>'All')); ?>
		</td>
	</tr>
	<tr>
		<!--<td><?php echo $this->Form->label('Verify Mode:');?></td>-->
		<td><?php echo $this->Form->input('custom_verify_mode', array('label' => 'Verify Mode','options'=>$this->getVar('varifyMode'),'selected'=>$verify_mode,'empty'=>'All')); ?></td>
		<!--<td><?php echo $this->Form->checkbox('verify_mode', array('value' => '0','checked'=>$verify_mode)); ?></td>-->
	</tr>
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
<?php echo $form->create('Schedule',array('controller'=>'schedules','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $form->hidden('Schedule.action_value',array('id'=>'actionValue')); ?>
<?php echo $this->element('check_uncheck_control')?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo $this->Paginator->sort('schedule id'); ?></th>
		<th><?php echo $this->Paginator->sort('task name'); ?></th>
		<th><?php echo $this->Paginator->sort('task id'); ?></th>
		<th><?php echo $this->Paginator->sort('repeat type');?></th>
		<th><?php echo $this->Paginator->sort('verify mode');?>
		<th><?php echo $this->Paginator->sort('priority');?></th>
		<th><?php echo $this->Paginator->sort('entitygroup');?></th>
		<th class="actions"><?php //__('Actions');?></th>
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j = 0;
	//debug($schedules);exit;
	foreach ($schedules as $schedule):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>

		<td><?php echo $schedule['Schedule']['id']; ?>&nbsp;</td>
		<td><?php echo $schedule['Task']['name']; ?>&nbsp;</td>
		<td><?php echo $schedule['Task']['id']; ?>&nbsp;</td>
		<td><?php echo $schedule['RepeatType']['description']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['verify_mode']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['priority']; ?>&nbsp;</td>
		<td><?php echo $schedule['Task']['EntityGroup'][0]['name']; ?>&nbsp;</td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $schedule['Schedule']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('Schedule'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$schedule['Schedule']['id']));
		?></td>
	</tr>
	<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit');?>
		<td class="actions_button" colspan="7" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

		<?php echo $html->link(__('Delete', true),'#', array('class'=>'deleteButton')); ?>
		</td>
	</tr>
</table>
		<?php echo $form->end(); ?>
<p><?php
echo $this->Paginator->counter(array(
	'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
));
?></p>

<?php echo $this->element('paginate'); ?></div>
</div>
</td>
</tr>
</table>