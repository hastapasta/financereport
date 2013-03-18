<div class="tasks none">

	<?php echo $this->Form->create('Task',array('controller'=>'tasks','action'=>'action_process','class'=>'recordForm')) ?>
	<?php echo $this->Form->hidden('Task.action_value',array('id'=>'actionValue')); ?>
	<?php echo $this->element('check_uncheck_control')?>
	
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php echo $this->Paginator->sort('name');?></th>
			<th><?php echo $this->Paginator->sort('description');?></th>
			<th><?php echo $this->Paginator->sort('use_group_for_reading');?></th>
			<th><?php echo $this->Paginator->sort('eps_est_priority');?></th>
			<th><?php echo $this->Paginator->sort('source');?></th>
			<th><?php echo $this->Paginator->sort('metric_id');?></th>
			<th class="actions"><?php //__('Actions');?></th>
			<th class="actions"><?php echo __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j = 0;
	foreach ($tasks as $task):
		$class = null;
		if ($i++ % 2 == 0) {
			$class = ' class="altrow"';
		}
	?>
	<tr<?php echo $class;?>>
		<td><?php echo $task['Task']['id']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['name']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['description']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['use_group_for_reading']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['eps_est_priority']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['source']; ?>&nbsp;</td>
		<td><?php echo $task['Metric']['name']; ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View'), array('action' => 'view', $task['Task']['id'])); ?>
		</td>
		<td style="text-align: center">
		<?php
			$j++;
			echo $this->Form->checkbox('Task'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$task['Task']['id']));
		?>
		</td>
	</tr>
<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit')?>
		<td class="actions_button" colspan="5" style="text-align: right"><?php echo $this->Html->link(__('Edit'),'#',array('class'=>'editButton')); ?>

		<?php echo $this->Html->link(__('Delete'),'#', array('class'=>'deleteButton')); ?>
		</td>
	</tr>
	</table>
	<?php echo $this->Form->end(); ?>
<p><?php
echo $this->Paginator->counter(array(
	'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%')
));
?></p>

<?php echo $this->element('paginate'); ?></div>