<div class="schedules index">
<h2><?php __('Actions');?></h2>
<?php echo $this->element('actions',array('title'=>'Schedules')); ?>
<?php echo $form->create('Schedule',array('controller'=>'schedules','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $form->hidden('Schedule.action_value',array('id'=>'actionValue')); ?>
<table cellpadding="0" cellspacing="0">
	<tr>

		<th><?php echo $this->Paginator->sort('task name'); ?></th>
		<th><?php echo $this->Paginator->sort('repeat type');?></th>
		<th><?php //echo $this->Paginator->sort('id');?></th>
		<th><?php //echo $this->Paginator->sort('run_once');?></th>
		<th><?php echo $this->Paginator->sort('priority');?></th>
		<th><?php echo $this->Paginator->sort('entitygroup');?></th>
		<th><?php //echo $this->Paginator->sort('configure_notification');?></th>
		<th><?php //echo $this->Paginator->sort('referencegroup');?></th>
		<th class="actions"><?php __('Actions');?></th>
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j = 0;
	//debug($schedules,true);
	foreach ($schedules as $schedule):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>

		<td><?php echo $schedule['Task']['name']; ?>&nbsp;</td>
		<td><?php echo $schedule['RepeatType']['description']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['id']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['run_once']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['priority']; ?>&nbsp;</td>
		<td><?php echo $schedule['Task']['EntityGroup'][0]['name']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['configure_notification']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['referencegroup']; ?>&nbsp;</td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $schedule['Schedule']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('Schedule'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$schedule['Schedule']['id']));
		?></td>
	</tr>
	<?php endforeach; ?>
	<tr>
		<td class="actions_button" colspan="11" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

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
