<div class="schedules none">
	<table>
		<tr>
			<td style="width: 10%;">
				<table class="searchTable" cellspacing="0"> <?php 
				  echo $this->Form->create( 'Schedule',
							array(
									'controller'=>'schedules',
									'name' => 'SearchForm',
									'action'=>'index',
									'class'=>'filterForm'
							)
					);
					$obsolete_data_set= "";
					$repeat_type_id="";
					$checkboxdefault=false;
					$checkBoxNotDefault=false;
					$verify_mode ="";
					$taskId = "";
					$filtervalues = $this->Session->read('schedulefiltervalues');
					$initial = false;
					//print_r($filtervalues);exit;
					// Initialize the form values to the previously set values.
					if ($filtervalues != null){
						$timeeventdefault = $filtervalues['Schedule']['repeat_type_id'];
						if ($filtervalues['Schedule']['filtersenabled'] == '1') {
							$checkboxdefault=true;
						}
						if ($filtervalues['Schedule']['notrepeattype'] == '1') {
						  $checkBoxNotDefault=true;
						}
						if (!empty($filtervalues['Schedule']['obsolete_data_set'])){
							$obsolete_data_set=$filtervalues['Schedule']['obsolete_data_set'];
						}
						if (!empty($filtervalues['Schedule']['repeat_type_id'])){
							$repeat_type_id=$filtervalues['Schedule']['repeat_type_id'];
						}
						if (!empty($filtervalues['Schedule']['task_id'])){
						  $taskId=$filtervalues['Schedule']['task_id'];
						}
						if (isset($filtervalues['Schedule']['custom_verify_mode']) &&
								$filtervalues['Schedule']['custom_verify_mode'] != '') {
							$verify_mode = $filtervalues['Schedule']['custom_verify_mode'];
						}
					} ?>
					<tr>
						<td> <?php 
						  echo $this->Form->input('obsolete_data_set',
								array('label'=>'Task Name:','type'=>'text',
										'value'=>$obsolete_data_set)); ?>
						</td>
					</tr>
					<tr>
						<td> <?php 
						  echo $this->Form->input('task_id',
								array('label'=>'Task Id:','type'=>'text',
										'value'=>$taskId)); ?>
						</td>
					</tr>
					<tr>
						<td border="1"> <?php 
						  echo $this->Form->input('repeat_type_id',
 								array('label'=>'Repeat Type:',
 										'options' => $this->getVar('repeatTypeId'),
 										'selected'=>$repeat_type_id,'empty'=>'All')); ?>
						</td>
						<td border="1"> <?php 
						  echo $this->Form->label('Not');
						  echo $this->Form->checkbox('notrepeattype',
								array('label'=>'Not','value' => '0',
										'checked'=>$checkBoxNotDefault,
						         'style'=>'margin-top:4px;margin-left:6px;')); ?>
						</td>
					</tr>
					<tr>
						<!--<td> <?php 
						echo $this->Form->label('Verify Mode:'); ?>
						</td>-->
						<td> <?php 
						  echo $this->Form->input('custom_verify_mode', 
    						array('label' => 'Verify Mode',
    								'options'=>$this->getVar('verifyMode'),
    								'selected'=>$verify_mode,'empty'=>'All')); ?>
						</td>
					</tr>
					<tr>
						<td style="border-top: 1px solid black;"> <?php 
						  echo $this->Form->label('Filters Enabled:');?>
						</td>
						<td style="border-top: 1px solid black;"> <?php 
						  echo $this->Form->checkbox('filtersenabled', 
  						array('value' => '0','checked'=>$checkboxdefault)); ?>
						</td>
					</tr>
					<tr>
						<td><?php 
						  echo $this->Form->end(__('Refresh List')); ?>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<div class="alerts index" style='width: 95%'> <?php 
					echo $this->Form->create('Schedule',array('controller'=>'schedules',
					    'action'=>'action_process','class'=>'recordForm'));
					echo $this->Form->hidden('Schedule.action_value',
					    array('id'=>'actionValue')); 
					echo $this->element('check_uncheck_control'); ?>
					<table cellpadding="0" cellspacing="0">
						<tr>
							<th> <?php 
							  echo $this->Paginator->sort('Schedule Id','Schedule.id'); ?>
							</th>
							<th> <?php 
							  echo $this->Paginator->sort('Task Name','Task.name'); ?>
							</th>
							<th> <?php 
							  echo $this->Paginator->sort('Task Id','Schedule.task_id'); ?>
							</th>
							<th> <?php 
							  echo $this->Paginator->sort('Repeat Type',
							      'RepeatType.description');?>
							</th>
							<th> <?php 
							  echo $this->Paginator->sort('Verify Mode',
							      'Schedule.verify_mode');?>		
							<th> <?php 
							  echo $this->Paginator->sort('Priority','Schedule.priority');?>
							</th>
							<th> <?php echo __('Entity Group');?>
							</th>
							<th class="actions">
							</th>
							<th class="actions"> <?php echo __('Edit/Delete');?>
							</th>
						</tr> <?php
            	$i = 0;
            	$j = 0;
            	//debug($schedules);exit;
            	foreach ($schedules as $schedule):
            	$class = null;
            	if ($i++ % 2 == 0) {
            		$class = ' class="altrow"';
            	}	?>
						<tr <?php echo $class;?>>
							<td><?php echo $schedule['Schedule']['id']; ?>&nbsp;</td>
							<td><?php echo $schedule['Task']['name']; ?>&nbsp;</td>
							<td><?php echo $schedule['Task']['id']; ?>&nbsp;</td>
							<td><?php
							  echo $schedule['RepeatType']['description']; ?>
							  &nbsp;
							</td>
							<td><?php 
							  echo $schedule['Schedule']['verify_mode']; ?>
							  &nbsp;
							</td>
							<td><?php echo $schedule['Schedule']['priority']; ?>&nbsp;</td>
							<td><?php 
							  echo $schedule['Task']['EntityGroup'][0]['name']; ?>
							  &nbsp;
							</td>
							<td class="actions"><?php
							  echo $this->Html->link(__('View'), 
							    array('action' => 'view', $schedule['Schedule']['id'])); ?>
							</td>
							<td style="text-align: center"> <?php
		            $j++;
		            echo $this->Form->checkbox('Schedule'.$j, array('label'=>false, 
		                'type'=>'checkbox', 
		                'value'=>$schedule['Schedule']['id'])); ?>
							</td>
						</tr>
						<?php endforeach; ?>
						<tr>
							<?php echo $this->element('pagelimit');?>
							<td class="actions_button" colspan="7"
							 style="text-align: right"> <?php 
							 echo $this->Html->link(__('Edit'),'#',
							     array('class'=>'editButton')); 
							 echo $this->Html->link(__('Delete'),'#', 
							     array('class'=>'deleteButton')); ?>
							</td>
						</tr>
					</table>
					<?php echo $this->Form->end(); ?>
					<p> <?php
            echo $this->Paginator->counter(array(
	            'format' => __('Page %page% of %pages%, showing %current% 
	                records out of %count% total, starting on record %start%,
	                 ending on %end%', true)));?>
					</p>
					<?php echo $this->element('paginate'); ?>
				</div>
			</td>
		</tr>
	</table>
</div>
