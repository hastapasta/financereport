<div class="alerts index">
	<h2><?php __('Alerts');?></h2>
	<script type="text/javascript">
	function showUser(str)
{
	if (str=="")
	{
		document.getElementById("txtHint").innerHTML="";
		return;
	} 
	
	if (window.XMLHttpRequest)
	{// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}
	else
	{// code for IE6, IE5  
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
		}
	}
	
	var currentTime = new Date()
	//alert("here");
	xmlhttp.open("POST","picklist1.php?q="+str+"&timestamp="+currentTime,true);
	xmlhttp.send();
}
	</script>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id');?></th>
			<th><?php //echo $this->Paginator->sort('type');?></th>
			<th><?php echo $this->Paginator->sort('name');?></th>
			<th><?php //echo $this->Paginator->sort('email');?></th>
			<th><?php echo $this->Paginator->sort('frequency');?></th>
			<th><?php echo $this->Paginator->sort('limit_value');?></th>
			<th><?php echo $this->Paginator->sort('limit_adjustment');?></th>
			<th><?php echo $this->Paginator->sort('fact_data_key');?></th>
			<th><?php echo $this->Paginator->sort('ticker');?></th>
			<th><?php echo $this->Paginator->sort('alert_count');?></th>
			<th><?php echo $this->Paginator->sort('disabled');?></th>
			<th><?php echo $this->Paginator->sort('user');?></th>
			<th class="actions"><?php __('Actions');?></th>
	</tr>
	<?php
	$i = 0;
	
	foreach ($alerts as $alert):
		$class = null;
		if ($i++ % 2 == 0) {
			$class = ' class="altrow"';
		}
		//debug($alert,true);
		

	?>
	
	
	<tr<?php echo $class;?>>
		<td><?php echo $alert['Alert']['id']; ?>&nbsp;</td>
		<td><?php //echo $alert['Alert']['type']; ?>&nbsp;</td>
		<td><?php echo $alert['Schedule']['Task']['name']; ?>&nbsp;</td>
		<td><?php //echo $alert['Alert']['email']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['frequency']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['limit_value']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['limit_adjustment']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['fact_data_key']; ?>&nbsp;</td>
		<td><?php echo $alert['Entity']['ticker']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['alert_count']; ?>&nbsp;</td>
		<td><?php echo $alert['Alert']['disabled']; ?>&nbsp;</td>
		<td><?php echo $alert['User']['username']; ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View', true), array('action' => 'view', $alert['Alert']['id'])); ?>
			<?php echo $this->Html->link(__('Edit', true), array('action' => 'edit', $alert['Alert']['id'])); ?>
			<?php echo $this->Html->link(__('Delete', true), array('action' => 'delete', $alert['Alert']['id']), null, sprintf(__('Are you sure you want to delete # %s?', true), $alert['Alert']['id'])); ?>
		</td>
	</tr>
<?php endforeach; ?>
	</table>
	<p>
	<?php
	echo $this->Paginator->counter(array(
	'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
	));
	?>	</p>

	<div class="paging">
		<?php echo $this->Paginator->prev('<< ' . __('previous', true), array(), null, array('class'=>'disabled'));?>
	 | 	<?php echo $this->Paginator->numbers();?>
 |
		<?php echo $this->Paginator->next(__('next', true) . ' >>', array(), null, array('class' => 'disabled'));?>
	</div>
</div>

<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('New Alert', true), array('action' => 'add')); ?></li>
		<li><?php echo $this->Html->link(__('List Users', true), array('controller' => 'users', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Groups', true), array('controller' => 'groups', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('List Schedules', true), array('controller' => 'schedules', 'action' => 'index')); ?> </li>
		
	</ul>
</div>

