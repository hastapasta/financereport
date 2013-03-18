<div class="entities view">
<?php //echo $this->element('actions',array('title'=>'Entity'))
$userprops = $this->getVar('user_props');
?>
<div style='clear:both'></div>
<dl>
<?php $i = 0; $class = ' class="altrow"';?>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Id'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['id']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['ticker']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Shares Outstanding'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['shares_outstanding']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Begin Fiscal Calendar'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['begin_fiscal_calendar']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Last Reported Quarter'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['last_reported_quarter']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Next Report Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['next_report_date']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Actual Fiscal Year End'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['actual_fiscal_year_end']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php echo __('Full Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $entity['Entity']['full_name']; ?>
	&nbsp;</dd>
</dl>
</div>
<div class="actions"></div>
<div class="related">
<h3><?php echo __('Related Alerts');?></h3>
<?php if (!empty($entity['Alert'])):?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo __('Id'); ?></th>
		<th><?php echo __('Type'); ?></th>
		<th><?php echo __('User'); ?></th>
		<th><?php echo __('Time Event'); ?></th>
		<th><?php echo __('Limit Value'); ?></th>
		<th><?php echo __('Fact Data Key'); ?></th>
		<th><?php echo __('Alert Count'); ?></th>
		<th><?php echo __('Disabled'); ?></th>
		<th><?php echo __('Group Id'); ?></th>
		<th><?php echo __('Entity Id'); ?></th>
		<th class="actions"><?php //__('Actions');?></th>
	</tr>
	<?php
	$i = 0;
	//debug($entity['Alert'][0],true);
	foreach ($entity['Alert'] as $alert):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
		<td><?php echo $alert['id'];?></td>
		<td><?php echo $alert['type'];?></td>
		<td><?php echo $alert['user_id'];?></td>
		<td><?php echo $alert['time_event_id'];?></td>
		<td><?php echo $alert['limit_value'];?></td>
		<td><?php echo $alert['initial_fact_data_id'];?></td>
		<td><?php echo $alert['notification_count'];?></td>
		<td><?php echo $alert['disabled'];?></td>
		<td><?php echo $alert['group_id'];?></td>
		<td><?php echo $alert['entity_id'];?></td>
		<td class="actions"><?php echo $this->Html->link(__('View'), array('controller' => 'alerts', 'action' => 'view', $alert['id'])); ?>
		<?php echo $this->Html->link(__('Edit'), array('controller' => 'alerts', 'action' => 'edit', $alert['id'])); ?>
		<?php echo $this->Html->link(__('Delete'), array('controller' => 'alerts', 'action' => 'delete', $alert['id']), null, sprintf(__('Are you sure you want to delete # %s?'), $alert['id'])); ?>
		</td>
	</tr>
	<?php endforeach; ?>
</table>
	<?php endif; ?>

<div class="actions">
<ul>
	<li><?php //echo $this->Html->link(__('New Alert'), array('controller' => 'alerts', 'action' => 'add'));?>
	</li>
</ul>
</div>
</div>
<div class="related">
<h3><?php echo __('Related Entity Groups');?></h3>
	<?php if (!empty($entity['EntityGroup'])):?>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th><?php echo __('Id'); ?></th>
		<th><?php echo __('Name'); ?></th>		
		<th class="actions"><?php //__('Actions');?></th>
	</tr>
	<?php
	$i = 0;
	foreach ($entity['EntityGroup'] as $entityGroup):
	$class = null;
	if ($i++ % 2 == 0) {
		$class = ' class="altrow"';
	}
	?>
	<tr <?php echo $class;?>>
		<td><?php echo $entityGroup['id'];?></td>
		<td><?php echo $entityGroup['name'];?></td>
		<td class="actions"><?php echo $this->Html->link(__('View'), array('controller' => 'entity_groups', 'action' => 'view', $entityGroup['id'])); ?>
		<?php if ($userprops['User']['group_id'] == 1){
				echo $this->Html->link(__('Edit'), array('controller' => 'entity_groups', 'action' => 'edit', $entityGroup['id'])); 
				echo $this->Html->link(__('Delete'), array('controller' => 'entity_groups', 'action' => 'delete', $entityGroup['id']), null, sprintf(__('Are you sure you want to delete # %s?'), $entityGroup['id']));
			} ?> 
		</td>
	</tr>
	<?php endforeach; ?>
</table>
	<?php endif; ?>



<div class="actions">
<ul>
	<li><?php //echo $this->Html->link(__('New Fact Datum'), array('controller' => 'fact_data', 'action' => 'add'));?>
	</li>
</ul>
</div>
</div>
