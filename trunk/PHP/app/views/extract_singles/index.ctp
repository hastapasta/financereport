<div class="groups none">
	<?php // Create Form For Edit/Delete Extract Single Table Record
		echo $this->Form->create('ExtractSingle',array('controller'=>'extract_singles',
														'action'=>'action_process',
														'class'=>'recordForm')); 
		echo $this->Form->hidden('ExtractSingle.action_value',array('id'=>'actionValue')); 

		//Include A Element For Check Or Uncheck All Checkbox For Edit Or Delete
		echo $this->element('check_uncheck_control');
	?>
	<table cellpadding="0" cellspacing="0">
		<tr>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php echo $this->Paginator->sort('obs_Data_Set');?></th>
			<th><?php echo $this->Paginator->sort('Table_Count');?></th>
			<th><?php echo $this->Paginator->sort('Row_Count');?></th>
			<th><?php echo $this->Paginator->sort('Cell_Count');?></th>
			<th><?php echo $this->Paginator->sort('Div_Count');?></th>
			<th><?php echo $this->Paginator->sort('Before_Unique_Code');?></th>
			<th><?php echo $this->Paginator->sort('After_Unique_Code');?></th>
			<th><?php echo $this->Paginator->sort('Initial_Bef_Unique_Code');?></th>
			<th><?php echo $this->Paginator->sort('is_csv_format');?></th>
			<th class="actions"><?php __('Edit/Delete');?></th>
		</tr>
		<?php
			$i = 0;
			$j = 0;
			foreach ($extractSingles as $extractSingle):	// Loop For Display Record Of Extract_single Table
				$class = null;
				if ($i++ % 2 == 0) {
					$class = ' class="altrow"';
				}
		?>
		<tr <?php echo $class;?>>
			<td><?php echo $extractSingle['ExtractSingle']['id']; ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['obs_Data_Set']; ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['Table_Count']; ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['Row_Count']; ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['Cell_Count']; ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['Div_Count']; ?>&nbsp;</td>
			<td><?php echo htmlspecialchars($extractSingle['ExtractSingle']['Before_Unique_Code']); ?>&nbsp;</td>
			<td><?php echo htmlspecialchars($extractSingle['ExtractSingle']['After_Unique_Code']); ?>&nbsp;</td>
			<td><?php echo htmlspecialchars($extractSingle['ExtractSingle']['Initial_Bef_Unique_Code']); ?>&nbsp;</td>
			<td><?php echo $extractSingle['ExtractSingle']['is_csv_format']; ?>&nbsp;</td>
			<td class="actions">
				<?php 
					echo $this->Html->link(__('View', true), array('action' => 'view', 
																	$extractSingle['ExtractSingle']['id'])); 
				?>
			</td>
			<td style="text-align: center">
				<?php
					$j++;
					echo $this->Form->checkbox('ExtractSingle'.$j, array('label'=>false, 'type'=>'checkbox', 
																		'value'=>$extractSingle['ExtractSingle']['id']));
				?>
			</td>
			
		</tr>
		<?php endforeach; ?>
		<tr>
			<?php echo $this->element('pagelimit')?>
			<td class="actions_button" colspan="0" style="text-align: right">
				<?php 
					echo $this->Html->link(__('Edit', true),'#',array('class'=>'editButton')); 
					echo $this->Html->link(__('Delete', true),'#', array('class'=>'deleteButton')); 
				?>
			</td>
		</tr>
	</table>
		<?php echo $this->Form->end(); ?>
	<p>
		<?php
			echo $this->Paginator->counter(array(
				'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
			));
		?>
	</p>

	<?php echo $this->element('paginate'); ?>
</div>
