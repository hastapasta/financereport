<div class="jobs form">
<?php echo $this->Form->create('Job');?>
	<?php echo $this->Form->create('Group');
		for($i = 0; $i < sizeof($this->data['Job']); $i++){
	?>
	<fieldset>
 		<legend><?php __('Edit Job');?></legend>
	<?php
		echo $this->Form->input('id');
		echo $this->Form->input('Job.'.$i.'.id');
		echo $this->Form->input('Job.'.$i.'.data_set');
		echo $this->Form->input('Job.'.$i.'.extract_key');
		echo $this->Form->input('Job.'.$i.'.extract_key_colhead');
		echo $this->Form->input('Job.'.$i.'.extract_key_rowhead');
		echo $this->Form->input('Job.'.$i.'.pre_process_func_name');
		echo $this->Form->input('Job.'.$i.'.post_process_func_name');
		echo $this->Form->input('Job.'.$i.'.pre_nodata_check_func');
		echo $this->Form->input('Job.'.$i.'.pre_job_process_func_name');
		echo $this->Form->input('Job.'.$i.'.post_job_process_func_name');
		echo $this->Form->input('Job.'.$i.'.table_extraction');
		echo $this->Form->input('Job.'.$i.'.source');
		echo $this->Form->input('Job.'.$i.'.custom_insert');
		echo $this->Form->input('Job.'.$i.'.data_group');
		echo $this->Form->input('Job.'.$i.'.input_source');
		echo $this->Form->input('Job.'.$i.'.data_set_alias');
		echo $this->Form->input('Job.'.$i.'.url_static');
		echo $this->Form->input('Job.'.$i.'.url_dynamic');
		echo $this->Form->input('Job.'.$i.'.multiple_tables');
	?>
	</fieldset>
	<?php }?>
<?php echo $this->Form->end('Submit');?>
</div>