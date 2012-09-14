<div class="groups none">
<?php //echo $this->element('actions',array('title'=>'Groups')); ?>
<?php echo $form->create('Group',array('controller'=>'groups','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $form->hidden('Group.action_value',array('id'=>'actionValue')); ?>
<?php echo $this->element('check_uncheck_control')?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo $this->Paginator->sort('id');?></th>
		<th><?php echo $this->Paginator->sort('name');?></th>
		<th><?php echo $this->Paginator->sort('created');?></th>
		<th><?php echo $this->Paginator->sort('modified');?></th>
		<th class="actions"><?php //__('Actions');?></th>
		<th class="actions"><?php __('Edit/Delete');?></th>
	</tr>
	<?php
	$i = 0;
	$j = 0;
	foreach ($groups as $group):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
		<td><?php echo $group['Group']['id']; ?>&nbsp;</td>
		<td><?php echo $group['Group']['name']; ?>&nbsp;</td>
		<td><?php echo $group['Group']['created']; ?>&nbsp;</td>
		<td><?php echo $group['Group']['modified']; ?>&nbsp;</td>
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $group['Group']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('Group'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$group['Group']['id']));
		?></td>
	</tr>
	<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit')?>
		<td class="actions_button" colspan="2" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

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