<div class="groups none">
<?php echo $form->create('EntityGroup',array('controller'=>'entity_groups','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $form->hidden('EntityGroup.action_value',array('id'=>'actionValue')); ?>
<?php echo $this->element('check_uncheck_control')?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo $this->Paginator->sort('id');?></th>
		<th><?php echo $this->Paginator->sort('name');?></th>
		<th><?php echo $this->Paginator->sort('Parent Entity Group', 'parent_id')?></th>
		<th><?php echo $this->Paginator->sort('Type', 'entitygrouptype')?></th>
		<th class="actions"><?php //__('Actions');?></th>
		
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j = 0;
	foreach ($entityGroups as $group):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
		<td><?php echo $group['EntityGroup']['id']; ?>&nbsp;</td>
		<td><?php echo $group['EntityGroup']['name']; ?>&nbsp;</td>
		<td>
			<?php
				if($group['EntityGroup']['parent_id'] != 0) 
				echo $entityList[$group['EntityGroup']['parent_id']];
				else
				echo "Root";
			?></td>
		<td><?php echo $group['EntityGroup']['type']?></td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $group['EntityGroup']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('EntityGroup'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$group['EntityGroup']['id']));
		?></td>
		
	</tr>
	<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit')?>
		<td class="actions_button" colspan="0" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

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