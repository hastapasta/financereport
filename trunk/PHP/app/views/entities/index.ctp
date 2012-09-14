<?php 
	echo $this->Javascript->link('jquery-ui-1.8.12.custom.min');
	echo $this->Html->css('jquery-ui-1.8.12.custom');
	$userprops = $this->getVar('user_props');
?>
<script type='text/javascript'>
	$(function(){
		$( "#CountryName" ).autocomplete({	
			source: function( request, response ) {
				$.ajax({
					url: "<?php echo $this->base?>/entities/getCountry",
					dataType: "json",
					data: {
						maxRows: 12,
						term: request.term
					},
					success: function( data ) {
						response( $.map( data, function( item ) {
							return {
								value: item.Country.name,
								label: item.Country.name,
								id: item.Country.id
							};
						}));
					}
				});
			},
			minLength: 1,
			select: function( event, ui ) {
				if(ui.item)
				{
    				$("#CountryId").val(ui.item.id);
				}
			}
		})
		
	});
	function submitFunc()
	{

		var theForm=document.getElementById("EntityIndexForm");
		  //theForm.action = encodeURI(theForm.action);
		  theForm.ticker.value = encodeURI(theForm.ticker.value);
				
		return true;
	}
</script>

<table>
<tr>
	<td>
		<?php 
		
		$filtervalues = $this->Session->read('entityfiltervalues');
		$tickerdefault = "";
		$fullnamedefault = "";
		$countrynamedefault = "";
		$countryiddefault = "";
		$enable = 0;
		
		if ($filtervalues != null){
			
		//	print_r($filtervalues);
			//if (!empty($filtervalues['Alert'])){
			$tickerdefault = $filtervalues['Entity']['ticker'];
			//print_r($tickerdefault);
			//$taskdefault = $filtervalues['Alert']['task_id'];
			$fullnamedefault = $filtervalues['Entity']['Description'];
			$countrynamedefault = $filtervalues['Country']['name'];
			if (!empty($countrynamedefault))
				$countryiddefault = $filtervalues['Country']['id'];
			$enable = $filtervalues['Entity']['enable'];
			
			/*if ($filtervalues['Entity']['enable']=='1'){
				$checkboxdefault=true;
			}
			if ($filtervalues['Alert']['filtersdisabled']=='1'){
				$checkboxdefault1=true;
			}
			if ($filtervalues['Alert']['filtersinitial']=='1'){
				$initial=true;
			}
			if($filtervalues['Alert']['filtersfired'] == '1')
				$fired = true;*/
			//}
		}
		
		
		?>
		
		
		<table class="searchTable" cellspacing="0">
		<?php echo $this->Form->create('Entity', array('controller'=>'entities', 'name' => 'SearchForm','action'=>'index', 'type'=>'POST','class'=>'filterForm')); ?>
		 <tr>
				<td>
				<?php 
					
					//$ticker = (isset($this->data['Entity']['ticker'])) ? $this->data['Entity']['ticker'] : "";
					echo $this->Form->input('ticker', array("value"=>$tickerdefault));
				?>	
				</td>
			</tr>
			<tr>
				<td>
				<?php 
					//$name = (isset($this->data['Entity']['full_name'])) ? $this->data['Entity']['full_name'] : "";
					echo $this->Form->input('Description', array("value"=>$fullnamedefault));
				?>	
				</td>
							
			</tr>
			<tr>
				<td>
				<?php 
				   
					//$country = (isset($this->data['Entity']['country'])) ? $this->data['Entity']['country'] : "";
					//$country_id = (isset($this->data['Entity']['country_id'])) ? $this->data['Entity']['country_id'] : "";
					//var_dump($entities);
					//var_dump($countries);
					//var_dump($this);
					echo $this->Form->input('Country.name',array('label'=>'Country:','options' => $this->getVar('countries'),'selected'=>$countrynamedefault,'empty'=>'All'));
					//echo $this->Form->hidden('Country.id',array('label'=>'Country:','options' => $this->getVar('countries'),'selected'=>$countrynamedefault,'empty'=>'All'));
					//echo $this->Form->input('Country.name', array("value"=>$countrynamedefault));
					//debug($countrynamedefault);exit;
					echo $this->Form->hidden('Country.id', array("value"=>$countrynamedefault));
				?>	
				</td>
							
			</tr>
		
			<tr><td><br></td></tr>
				
			
			<tr>
				<td style="border-top:1px solid black;">
				<?php echo $this->Form->label('Filters Enabled:'); ?>
				</td>
				<td style="border-top:1px solid black;">
				<?php
					//$enable = isset($this->data['Entity']['enable']) ? $this->data['Entity']['enable'] : null;
					//echo $form->input('enable', array('type'=>'checkbox', 'checked'=> (boolean) $enable));
					echo $this->Form->checkbox('enable', array('value' => '0','checked'=>(boolean) $enable));
				?>
				</td>
			</tr>
			<tr>
				<td><?php echo $this->Form->end(__('Refresh List', true)); ?></td>
			</tr>
		</table>
		
	</td>
	<td>
		<div class="entities index" style='width:95%'>
		<?php 
			
		 
			echo $this->Form->create('Entity',array('controller'=>'entities','action'=>'action_process','class'=>'recordForm')); 
			echo $this->Form->hidden('Entity.action_value',array('id'=>'actionValue')); 
		?>
		<?php if ($userprops['User']['group_id'] == 1){ echo $this->element('check_uncheck_control'); }?>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<th><?php echo $this->Paginator->sort('id');?></th>
				<th><?php echo $this->Paginator->sort('ticker');?></th>
				<th><?php echo $this->Paginator->sort('shares_outstanding');?></th>
				<th><?php echo $this->Paginator->sort('begin_fiscal_calendar');?></th>
				<th><?php echo $this->Paginator->sort('last_reported_quarter');?></th>
				<th><?php echo $this->Paginator->sort('next_report_date');?></th>
				<th><?php //echo $this->Paginator->sort('groups');?></th>
				<th><?php echo $this->Paginator->sort('actual_fiscal_year_end');?></th>
				<th><?php echo $this->Paginator->sort('full_name');?></th>
				<th class="actions"><?php //__('Actions');?></th>
				<th class="actions"><?php if ($userprops['User']['group_id'] == 1){__('Edit/Delete');}?></th>
			</tr>
			<?php
			$i = 0;
			$j=0;
			foreach ($entities as $entity):
			$class = null;
			if ($i++ % 2 == 0) {
				$class = ' class="altrow"';
			}
			?>
			<tr <?php echo $class;?>>
				<td><?php echo $entity['Entity']['id']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['ticker']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['shares_outstanding']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['begin_fiscal_calendar']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['last_reported_quarter']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['next_report_date']; ?>&nbsp;</td>
				<td><?php //echo $entity['Entity']['groups']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['actual_fiscal_year_end']; ?>&nbsp;</td>
				<td><?php echo $entity['Entity']['full_name']; ?>&nbsp;</td>
				<td class="actions"><?php echo $this->Html->link(__('View', true), array('action' => 'view', $entity['Entity']['id'])); ?></td>
				<td style="text-align: center"><?php
				$j++;
				if ($userprops['User']['group_id'] == 1){
					echo $form->checkbox('Entity'.$j, array('label'=>false, 'type'=>'checkbox', 'value'=>$entity['Entity']['id']));
				}
				?></td>
			</tr>
			<?php endforeach; ?>
			<tr>
				<?php echo $this->element('pagelimit')?>
				<td class="actions_button" colspan="9" style="text-align: right">
				
				
				<?php 
				if ($userprops['User']['group_id'] == 1){
					echo $html->link(__('Edit', true),'#',array('class'=>'editButton'));
					echo $html->link(__('Delete', true),'#', array('class'=>'deleteButton'));
				} 
				
				?>
				</td>
			</tr>
		</table>
		<p><?php
		echo $this->Paginator->counter(array(
			'format' => __('Page %page% of %pages%, showing %current% records out of %count% total, starting on record %start%, ending on %end%', true)
		));
		?></p>
		<?php echo $this->element('paginate'); ?>
		</div>
			
	</td>
</tr>
</table>


