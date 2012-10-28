<div class="extractSingles form">
<?php echo $this->Form->create('ExtractSingle');?>
	<fieldset>
 		<legend><?php __('Add Extract Single'); ?></legend>
	<?php
		echo $this->Form->input('obs_Data_Set');
		echo $this->Form->input('Table_Count');
		echo $this->Form->input('Row_Count');
		echo $this->Form->input('Cell_Count');
		echo $this->Form->input('Div_Count');
		echo $this->Form->input('Before_Unique_Code');
		echo $this->Form->input('After_Unique_Code');
		echo $this->Form->input('Initial_Bef_Unique_Code');
		echo $this->Form->input('is_csv_format');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>