<!-- <div class="jobs index"> -->
<div class="jobs none">
<?php echo $this->Form->create('Job',array('controller'=>'groups','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $this->Form->hidden('Job.action_value',array('id'=>'actionValue')); ?>
<?php echo $this->element('check_uncheck_control')?>

<table cellpadding="0" cellspacing="0">
<tr>
	<th><?php echo $this->Paginator->sort('id');?></th>
	<th><?php echo $this->Paginator->sort('data_set');?></th>
	<th><?php echo $this->Paginator->sort('extract_key');?></th>
	<th><?php echo $this->Paginator->sort('extract_key_colhead');?></th>
	<th><?php echo $this->Paginator->sort('extract_key_rowhead');?></th>
	<th class="actions"><?php //__('Actions');?></th>
	<th class="actions"><?php echo __('Edit/Delete');?></th>

</tr>
<?php
$i = 0;
$j = 0;
foreach ($jobs as $job):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
?>
	<tr<?php echo $class;?>>
		<td>
			<?php echo $job['Job']['id']; ?>
		</td>
		<td>
			<?php echo $job['Job']['data_set']; ?>
		</td>
		<td>
			<?php echo $job['Job']['extract_key']; ?>
		</td>
		<td>
			<?php echo $job['Job']['extract_key_colhead']; ?>
		</td>
		<td>
			<?php echo $job['Job']['extract_key_rowhead']; ?>
		</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View'), array('action' => 'view', $job['Job']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $this->Form->checkbox('Job'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$job['Job']['id']));
		?></td>
	</tr>

<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit')?>
		<td class="actions_button" colspan="8" style="text-align: right">
		<?php echo $this->Html->link(__('Edit'),'#',array('class'=>'editButton')); ?>
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

