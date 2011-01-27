<div class="tasks index">
	<h2><?php __('Tasks');?></h2>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php echo $this->Paginator->sort('name');?></th>
			<th><?php echo $this->Paginator->sort('description');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key1');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key2');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key3');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key4');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key5');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key6');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key7');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key8');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key9');?></th>
			<th><?php echo $this->Paginator->sort('job_primary_key10');?></th>
			<th class="actions"><?php __('Actions');?></th>
	</tr>
	<?php
	$i = 0;
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
		<td><?php echo $task['Task']['job_primary_key1']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key2']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key3']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key4']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key5']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key6']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key7']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key8']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key9']; ?>&nbsp;</td>
		<td><?php echo $task['Task']['job_primary_key10']; ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View', true), array('action' => 'view', $task['Task']['id'])); ?>
			<?php echo $this->Html->link(__('Edit', true), array('action' => 'edit', $task['Task']['id'])); ?>
			<?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $task['Task']['id']), null, sprintf(__('Are you sure you want to delete # %s?', true), $task['Task']['id'])); ?>
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
		<li><?php echo $this->Html->link(__('New Task', true), array('action' => 'add')); ?></li>
	</ul>
</div>