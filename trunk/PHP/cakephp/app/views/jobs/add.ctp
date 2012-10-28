<div class="jobs form">
<?php echo $form->create('Job');?>
	<fieldset>
 		<legend><?php __('Add Job');?></legend>
	<?php
		echo $form->input('data_set');
		echo $form->input('extract_key');
		echo $form->input('extract_key_colhead');
		echo $form->input('extract_key_rowhead');
		echo $form->input('pre_process_func_name');
		echo $form->input('post_process_func_name');
		echo $form->input('pre_nodata_check_func');
		echo $form->input('pre_job_process_func_name');
		echo $form->input('post_job_process_func_name');
		echo $form->input('table_extraction');
		echo $form->input('source');
		echo $form->input('custom_insert');
		echo $form->input('data_group');
		echo $form->input('input_source');
		echo $form->input('data_set_alias');
		echo $form->input('url_static');
		echo $form->input('url_dynamic');
		echo $form->input('multiple_tables');
	?>
	</fieldset>
<?php echo $form->end('Submit');?>
</div>
