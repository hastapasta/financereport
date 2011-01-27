<div class="entities form">
<?php echo $this->Form->create('Entity');?>
	<fieldset>
 		<legend><?php __('Edit Entity'); ?></legend>
	<?php
		echo $this->Form->input('id');
		echo $this->Form->input('ticker');
		echo $this->Form->input('shares_outstanding');
		echo $this->Form->input('begin_fiscal_calendar');
		echo $this->Form->input('last_reported_quarter');
		echo $this->Form->input('next_report_date');
		echo $this->Form->input('groups');
		echo $this->Form->input('actual_fiscal_year_end');
		echo $this->Form->input('full_name');
		echo $this->Form->input('depricated');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>

		<li><?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $this->Form->value('Entity.id')), null, sprintf(__('Are you sure you want to delete # %s?', true), $this->Form->value('Entity.id'))); ?></li>
		<li><?php echo $this->Html->link(__('List Entities', true), array('action' => 'index'));?></li>
		<li><?php echo $this->Html->link(__('List Alerts', true), array('controller' => 'alerts', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Alert', true), array('controller' => 'alerts', 'action' => 'add')); ?> </li>
		<li><?php echo $this->Html->link(__('List Entity Groups', true), array('controller' => 'entity_groups', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Entity Group', true), array('controller' => 'entity_groups', 'action' => 'add')); ?> </li>
		<li><?php echo $this->Html->link(__('List Fact Data', true), array('controller' => 'fact_data', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Fact Datum', true), array('controller' => 'fact_data', 'action' => 'add')); ?> </li>
	</ul>
</div>