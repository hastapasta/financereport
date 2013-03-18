<div class="extractTables none">
<?php echo $this->Form->create('ExtractTable',array('controller'=>'extract_tables','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $this->Form->hidden('ExtractTable.action_value',array('id'=>'actionValue')); ?>
<?php echo $this->element('check_uncheck_control')?>
<table cellpadding="0" cellspacing="0">
<tr>
	<th><?php echo $this->Paginator->sort('id');?></th>
	<th><?php echo $this->Paginator->sort('Description');?></th>
	<th><?php echo $this->Paginator->sort('table_count');?></th>
	<th><?php echo $this->Paginator->sort('top_corner_row');?></th>
	<th><?php echo $this->Paginator->sort('number_of_columns');?></th>
	<th><?php echo $this->Paginator->sort('column_th_tags');?></th>
	<th class="actions"><?php echo __('Actions');?></th>
	<th class="actions"><?php echo __('Edit/Delete');?></th>
</tr>
<?php
$i = 0;
$j = 0;
foreach ($extractTables as $extractTable):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
?>
	<tr<?php echo $class;?>>
		<td>
			<?php echo $extractTable['ExtractTable']['id']; ?>
		</td>
		<td>
			<?php echo $extractTable['ExtractTable']['Description']; ?>
		</td>
		<td>
			<?php echo $extractTable['ExtractTable']['table_count']; ?>
		</td>
		<td>
			<?php echo $extractTable['ExtractTable']['top_corner_row']; ?>
		</td>
		<td>
			<?php echo $extractTable['ExtractTable']['number_of_columns']; ?>
		</td>
		<td>
			<?php echo $extractTable['ExtractTable']['column_th_tags']; ?>
		</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View'), array('action' => 'view', $extractTable['ExtractTable']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $this->Form->checkbox('ExtractTable'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$extractTable['ExtractTable']['id']));
		?></td>
	</tr>
<?php endforeach; ?>
	<tr>
		<?php echo $this->element('pagelimit')?>
		<td class="actions_button" colspan="4" style="text-align: right"><?php echo $this->Html->link(__('Edit'),'#',array('class'=>'editButton')); ?>

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
