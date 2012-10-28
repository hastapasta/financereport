<div class="extractTables form">

<?php echo $this->Form->create('ExtractTable');
	for($i = 0; $i < sizeof($this->data['ExtractTable']); $i++){
?>
	<fieldset>
 		<legend><?php __('Edit ExtractTable');?></legend>
	<?php
		echo $form->input('ExtractTable.'.$i.'.id');
		echo $form->input('ExtractTable.'.$i.'.Description');
		echo $form->input('ExtractTable.'.$i.'.table_count');
		echo $form->input('ExtractTable.'.$i.'.top_corner_row');
		echo $form->input('ExtractTable.'.$i.'.number_of_columns');
		echo $form->input('ExtractTable.'.$i.'.column_th_tags');
		echo $form->input('ExtractTable.'.$i.'.column1');
		echo $form->input('ExtractTable.'.$i.'.column2');
		echo $form->input('ExtractTable.'.$i.'.column3');
		echo $form->input('ExtractTable.'.$i.'.column4');
		echo $form->input('ExtractTable.'.$i.'.column5');
		echo $form->input('ExtractTable.'.$i.'.column6');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col1');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col1');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col2');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col2');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col3');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col3');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col4');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col4');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col5');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col5');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col6');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col6');
		echo $form->input('ExtractTable.'.$i.'.rowsofdata');
		echo $form->input('ExtractTable.'.$i.'.rowinterval');
		echo $form->input('ExtractTable.'.$i.'.end_data_marker');
		echo $form->input('ExtractTable.'.$i.'.column7');
		echo $form->input('ExtractTable.'.$i.'.column8');
		echo $form->input('ExtractTable.'.$i.'.column9');
		echo $form->input('ExtractTable.'.$i.'.column10');
		echo $form->input('ExtractTable.'.$i.'.column11');
		echo $form->input('ExtractTable.'.$i.'.column12');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col7');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col7');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col8');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col8');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col9');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col9');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col10');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col10');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col11');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col11');
		echo $form->input('ExtractTable.'.$i.'.bef_code_col12');
		echo $form->input('ExtractTable.'.$i.'.aft_code_col12');
	?>
	</fieldset>
	<?php }?>
<?php echo $form->end('Submit');?>
</div>
