<div class="entities index">
	<h2><?php __('Entities');?></h2>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php echo $this->Paginator->sort('ticker');?></th>
			<th><?php echo $this->Paginator->sort('shares_outstanding');?></th>
			<th><?php echo $this->Paginator->sort('begin_fiscal_calendar');?></th>
			<th><?php echo $this->Paginator->sort('last_reported_quarter');?></th>
			<th><?php echo $this->Paginator->sort('next_report_date');?></th>
			<th><?php echo $this->Paginator->sort('groups');?></th>
			<th><?php echo $this->Paginator->sort('actual_fiscal_year_end');?></th>
			<th><?php echo $this->Paginator->sort('full_name');?></th>
			<th><?php echo $this->Paginator->sort('depricated');?></th>
			<th class="actions"><?php __('Actions');?></th>
	</tr>
	<?php
	$i = 0;
	foreach ($entities as $entity):
		$class = null;
		if ($i++ % 2 == 0) {
			$class = ' class="altrow"';
		}
	?>
	<tr<?php echo $class;?>>
		<td><?php echo $entity['Entity']['id']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['ticker']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['shares_outstanding']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['begin_fiscal_calendar']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['last_reported_quarter']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['next_report_date']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['groups']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['actual_fiscal_year_end']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['full_name']; ?>&nbsp;</td>
		<td><?php echo $entity['Entity']['depricated']; ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View', true), array('action' => 'view', $entity['Entity']['id'])); ?>
			<?php echo $this->Html->link(__('Edit', true), array('action' => 'edit', $entity['Entity']['id'])); ?>
			<?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $entity['Entity']['id']), null, sprintf(__('Are you sure you want to delete # %s?', true), $entity['Entity']['id'])); ?>
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
		<li><?php echo $this->Html->link(__('New Entity', true), array('action' => 'add')); ?></li>
		<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Alert', true), array('controller' => 'alerts', 'action' => 'add')); ?> </li>
		<li><?php echo $this->Html->link(__('List Entity Groups', true), array('controller' => 'entity_groups', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Entity Group', true), array('controller' => 'entity_groups', 'action' => 'add')); ?> </li>
		<li><?php echo $this->Html->link(__('List Fact Data', true), array('controller' => 'fact_data', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Fact Datum', true), array('controller' => 'fact_data', 'action' => 'add')); ?> </li>
	</ul>
</div>