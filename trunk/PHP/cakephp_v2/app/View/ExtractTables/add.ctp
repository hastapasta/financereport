<div class="extractTables form">
<?php echo $this->Form->create('ExtractTable');?>
	<fieldset>
 		<legend><?php echo __('Add ExtractTable');?></legend>
	<?php
		echo $this->Form->input('Description');
		echo $this->Form->input('table_count');
		echo $this->Form->input('top_corner_row');
		echo $this->Form->input('number_of_columns');
		echo $this->Form->input('column_th_tags');
		echo $this->Form->input('column1');
		echo $this->Form->input('column2');
		echo $this->Form->input('column3');
		echo $this->Form->input('column4');
		echo $this->Form->input('column5');
		echo $this->Form->input('column6');
		echo $this->Form->input('bef_code_col1');
		echo $this->Form->input('aft_code_col1');
		echo $this->Form->input('bef_code_col2');
		echo $this->Form->input('aft_code_col2');
		echo $this->Form->input('bef_code_col3');
		echo $this->Form->input('aft_code_col3');
		echo $this->Form->input('bef_code_col4');
		echo $this->Form->input('aft_code_col4');
		echo $this->Form->input('bef_code_col5');
		echo $this->Form->input('aft_code_col5');
		echo $this->Form->input('bef_code_col6');
		echo $this->Form->input('aft_code_col6');
		echo $this->Form->input('rowsofdata');
		echo $this->Form->input('rowinterval');
		echo $this->Form->input('end_data_marker');
		echo $this->Form->input('column7');
		echo $this->Form->input('column8');
		echo $this->Form->input('column9');
		echo $this->Form->input('column10');
		echo $this->Form->input('column11');
		echo $this->Form->input('column12');
		echo $this->Form->input('bef_code_col7');
		echo $this->Form->input('aft_code_col7');
		echo $this->Form->input('bef_code_col8');
		echo $this->Form->input('aft_code_col8');
		echo $this->Form->input('bef_code_col9');
		echo $this->Form->input('aft_code_col9');
		echo $this->Form->input('bef_code_col10');
		echo $this->Form->input('aft_code_col10');
		echo $this->Form->input('bef_code_col11');
		echo $this->Form->input('aft_code_col11');
		echo $this->Form->input('bef_code_col12');
		echo $this->Form->input('aft_code_col12');
	?>
	</fieldset>
<?php echo $this->Form->end('Submit');?>
</div>

