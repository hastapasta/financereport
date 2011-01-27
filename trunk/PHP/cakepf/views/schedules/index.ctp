<div class="schedules index">
	<h2><?php __('Schedules');?></h2>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php //echo $this->Paginator->sort('obsolete_data_set');?></th>
			<th><?php //echo $this->Paginator->sort('name');?></th>
			<th><?php echo $this->Paginator->sort('name', array('model' => 'Task')); ?></th>

			
			<th><?php //echo $this->Paginator->sort('task_id');?></th>
			<th><?php echo $this->Paginator->sort('Start_Date');?></th>
			<th><?php echo $this->Paginator->sort('repeat_type_id');?></th>
			<th><?php echo $this->Paginator->sort('last_run');?></th>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php //echo $this->Paginator->sort('run_once');?></th>
			<th><?php echo $this->Paginator->sort('priority');?></th>
			<th><?php echo $this->Paginator->sort('companygroup');?></th>
			<th><?php echo $this->Paginator->sort('configure_notification');?></th>
			<th><?php //echo $this->Paginator->sort('referencegroup');?></th>
			<th class="actions"><?php __('Actions');?></th>
	</tr>
	<?php
	$i = 0;
	foreach ($schedules as $schedule):
		$class = null;
		if ($i++ % 2 == 0) {
			$class = ' class="altrow"';
		}
	?>
	<tr<?php echo $class;?>>
		<td><?php //echo $schedule['Schedule']['obsolete_data_set']; ?>&nbsp;</td>
		<td><?php echo $schedule['Task']['name']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['task_id']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['Start_Date']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['repeat_type_id']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['last_run']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['id']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['run_once']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['priority']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['companygroup']; ?>&nbsp;</td>
		<td><?php echo $schedule['Schedule']['configure_notification']; ?>&nbsp;</td>
		<td><?php //echo $schedule['Schedule']['referencegroup']; ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View', true), array('action' => 'view', $schedule['Schedule']['id'])); ?>
			<?php echo $this->Html->link(__('Edit', true), array('action' => 'edit', $schedule['Schedule']['id'])); ?>
			<?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $schedule['Schedule']['id']), null, sprintf(__('Are you sure you want to delete # %s?', true), $schedule['Schedule']['id'])); ?>
		</td>
	</tr>
<?php endforeach; ?>
	</table>
	<p>
	<?php
	echo $this->Paginator->counter(array(
	'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
	));
	?>	</p>

	<div class="paging">
		<?php echo $this->Paginator->prev('<< ' . __('previous', true), array(), null, array('class'=>'disabled'));?>
	 | 	<?php echo $this->Paginator->numbers();?>
 |
		<?php echo $this->Paginator->next(__('next', true) . ' >>', array(), null, array('class' => 'disabled'));?>
	</div>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('New Schedule', true), array('action' => 'add')); ?></li>
		<li><?php echo $this->Html->link(__('List Users', true), array('controller' => 'users', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Groups', true), array('controller' => 'groups', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?> </li>
	</ul>
</div>
