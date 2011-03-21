<div class="entities index">
<h2><?php __('Actions');?></h2>
<?php 
	echo $this->element('actions',array('title'=>'Entities')); 
	echo $form->create('Entity',array('controller'=>'entities','action'=>'action_process','class'=>'recordForm')); 
	echo $form->hidden('Entity.action_value',array('id'=>'actionValue')); 
?>
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
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j=0;
	foreach ($entities as $entity):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
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
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $entity['Entity']['id'])); ?></td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('Entity'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$entity['Entity']['id']));
		?></td>
	</tr>
	<?php endforeach; ?>
	<tr>
		<td class="actions_button" colspan="13" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

		<?php echo $html->link(__('Delete', true),'#', array('class'=>'deleteButton')); ?>
		</td>
	</tr>
</table>
<p><?php
echo $this->Paginator->counter(array(
	'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
));
?></p>
<?php echo $this->element('paginate'); ?>
</div>
