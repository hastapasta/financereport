<div class="extractTables form">

<?php echo $this->Form->create('ExtractTable');
	for($i = 0; $i < sizeof($this->request->data['ExtractTable']); $i++){
?>
	<fieldset>
 		<legend><?php echo __('Edit ExtractTable');?></legend>
	<?php
		echo $this->Form->input('ExtractTable.'.$i.'.id');
		echo $this->Form->input('ExtractTable.'.$i.'.Description');
		echo $this->Form->input('ExtractTable.'.$i.'.table_count');
		echo $this->Form->input('ExtractTable.'.$i.'.top_corner_row');
		echo $this->Form->input('ExtractTable.'.$i.'.number_of_columns');
		echo $this->Form->input('ExtractTable.'.$i.'.column_th_tags');
		echo $this->Form->input('ExtractTable.'.$i.'.column1');
		echo $this->Form->input('ExtractTable.'.$i.'.column2');
		echo $this->Form->input('ExtractTable.'.$i.'.column3');
		echo $this->Form->input('ExtractTable.'.$i.'.column4');
		echo $this->Form->input('ExtractTable.'.$i.'.column5');
		echo $this->Form->input('ExtractTable.'.$i.'.column6');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col1');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col1');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col2');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col2');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col3');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col3');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col4');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col4');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col5');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col5');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col6');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col6');
		echo $this->Form->input('ExtractTable.'.$i.'.rowsofdata');
		echo $this->Form->input('ExtractTable.'.$i.'.rowinterval');
		echo $this->Form->input('ExtractTable.'.$i.'.end_data_marker');
		echo $this->Form->input('ExtractTable.'.$i.'.column7');
		echo $this->Form->input('ExtractTable.'.$i.'.column8');
		echo $this->Form->input('ExtractTable.'.$i.'.column9');
		echo $this->Form->input('ExtractTable.'.$i.'.column10');
		echo $this->Form->input('ExtractTable.'.$i.'.column11');
		echo $this->Form->input('ExtractTable.'.$i.'.column12');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col7');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col7');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col8');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col8');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col9');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col9');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col10');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col10');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col11');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col11');
		echo $this->Form->input('ExtractTable.'.$i.'.bef_code_col12');
		echo $this->Form->input('ExtractTable.'.$i.'.aft_code_col12');
	?>
	</fieldset>
	<?php }?>
<?php echo $this->Form->end('Submit');?>
</div>
