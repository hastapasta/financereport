<div class="jobs view">
	<dl><?php $i = 0; $class = ' class="altrow"';?>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Id'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['id']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Data Set'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['data_set']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Extract Key'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php 
				if ($job['Job']['table_extraction'] == 0) {
					echo $this->Html->link($job['Job']['extract_key'], array('controller' => 'extract_singles', 'action' => 'view', $job['Job']['extract_key']));
				}
				else {
					echo $this->Html->link($job['Job']['extract_key'], array('controller' => 'extract_tables', 'action' => 'view', $job['Job']['extract_key']));
				}
			
			?>
			<?php //echo $job['Job']['extract_key']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Extract Key Colhead'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php 
				if ($job['Job']['table_extraction'] != 0) {
					echo $this->Html->link($job['Job']['extract_key_colhead'], array('controller' => 'extract_tables', 'action' => 'view', $job['Job']['extract_key_colhead']));
				}
			
			
			?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Extract Key Rowhead'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php 
				if ($job['Job']['table_extraction'] != 0) {
					echo $this->Html->link($job['Job']['extract_key_rowhead'], array('controller' => 'extract_tables', 'action' => 'view', $job['Job']['extract_key_rowhead']));
				}
			
			
			?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Pre Process Func Name'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['pre_process_func_name']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Post Process Func Name'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['post_process_func_name']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Pre Nodata Check Func'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['pre_nodata_check_func']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Pre Job Process Func Name'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['pre_job_process_func_name']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Post Job Process Func Name'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['post_job_process_func_name']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Custom URL Func Name'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['custom_url_func_name']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Table Extraction'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['table_extraction']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Source'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['source']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Custom Insert'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['custom_insert']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Data Group'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['data_group']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Input Source'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['input_source']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Data Set Alias'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['data_set_alias']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Url Static'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['url_static']; ?>
			&nbsp;
		</dd>
		<dt<?php if ($i % 2 == 0) echo $class;?>><?php echo __('Multiple Tables'); ?></dt>
		<dd<?php if ($i++ % 2 == 0) echo $class;?>>
			<?php echo $job['Job']['multiple_tables']; ?>
			&nbsp;
		</dd>
	</dl>
	<br>
	
	
	<form controller="jobs" class="recordForm" id="JobActionProcessForm" method="post" action="/cakepftest/jobs/action_process" accept-charset="utf-8">
	<!--<input type="checkbox" name="data[Job][Job6]" type="checkbox" value="11168" id="JobJob19" />-->
	<?php //echo $this->Form->checkbox('Job', array('label'=>false, 'type'=>'checkbox', 'value'=>$job['Job']['id']));?>
	<?php echo $this->Html->link('Edit Job', array('controller' => 'jobs', 'action' => 'edit',$job['Job']['id']),array('class'=>'job_editbutton')); ?>
	</form>
	<?php //echo $this->Html->link('Edit Job', array('controller' => 'jobs', 'action' => 'edit',$job['Job']['id']),array('class'=>'editButton')); ?>
	<script>
		$(function() {
		<?php echo "$( \"#edit".$job['Job']['id']."\" )" ?>
				.button()
				.click(function() {
					<?php //echo "tmp = $(\"#split".$this->request->data['Entity'][$i]['id']."\").attr(\"name\");"; ?>
					//alert(tmp);
					//window.location.href = "http://www.pikefin.com";
					$('#actionValue').val('2'); 
					$('.recordForm').submit(); 
					
					//$( "#dialog-form" ).dialog( "open" );
				});
		});
	</script>
</div>

