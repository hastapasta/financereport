<div class="users index">
<?php echo $this->element('actions',array('title'=>'Users')); ?>
<?php echo $form->create('User',array('controller'=>'users','action'=>'action_process','class'=>'recordForm')) ?>
<?php echo $form->hidden('User.action_value',array('id'=>'actionValue')); ?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo $this->Paginator->sort('id');?></th>
		<th><?php echo $this->Paginator->sort('username');?></th>
		<th><?php echo $this->Paginator->sort('password');?></th>
		<th><?php echo $this->Paginator->sort('email');?></th>
		<th><?php echo $this->Paginator->sort('group_id');?></th>
		<th class="actions"><?php __('Actions');?></th>
		<th class="actions"><?php __('Edit/Delete');?></th>

	</tr>
	<?php
	$i = 0;
	$j = 0;
	foreach ($users as $user):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
		<td><?php echo $user['User']['id']; ?>&nbsp;</td>
		<td><?php echo $user['User']['username']; ?>&nbsp;</td>
		<td><?php echo $user['User']['password']; ?>&nbsp;</td>
		<td><?php echo $user['User']['email']; ?>&nbsp;</td>
		<td><?php echo $this->Html->link($user['Group']['name'], array('controller' => 'groups', 'action' => 'view', $user['Group']['id'])); ?>
		</td>
		
		<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $user['User']['id'])); ?>
		</td>
		<td style="text-align: center"><?php
		$j++;
		echo $form->checkbox('User'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$user['User']['id']));
		?></td>
	</tr>
	<?php endforeach; ?>
	<tr>
		<td class="actions_button" colspan="9" style="text-align: right"><?php echo $html->link(__('Edit', true),'#',array('class'=>'editButton')); ?>

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