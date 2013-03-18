<div class="jobs form">
<?php echo $this->Form->create('Job');?>
	<fieldset>
 		<legend><?php echo __('Add Job');?></legend>
	<?php
		echo $this->Form->input('data_set');
		echo $this->Form->input('extract_key');
		echo $this->Form->input('extract_key_colhead');
		echo $this->Form->input('extract_key_rowhead');
		echo $this->Form->input('pre_process_func_name');
		echo $this->Form->input('post_process_func_name');
		echo $this->Form->input('pre_nodata_check_func');
		echo $this->Form->input('pre_job_process_func_name');
		echo $this->Form->input('post_job_process_func_name');
		echo $this->Form->input('table_extraction');
		echo $this->Form->input('source');
		echo $this->Form->input('custom_insert');
		echo $this->Form->input('data_group');
		echo $this->Form->input('input_source');
		echo $this->Form->input('data_set_alias');
		echo $this->Form->input('url_static');
		echo $this->Form->input('url_dynamic');
		echo $this->Form->input('multiple_tables');
	?>
	</fieldset>
<?php echo $this->Form->end('Submit');?>
</div>
