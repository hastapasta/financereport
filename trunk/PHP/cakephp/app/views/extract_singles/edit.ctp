<div class="extractSingles form">
	<?php 
		echo $this->Form->create('ExtractSingle');
		for($i = 0; $i < sizeof($this->data['ExtractSingle']); $i++){
	?>
	<fieldset>
 		<legend><?php __('Edit Extract Single'); ?></legend>
	<?php
		echo $this->Form->input('ExtractSingle.'.$i.'.id');
		echo $this->Form->input('ExtractSingle.'.$i.'.obs_Data_Set');
		echo $this->Form->input('ExtractSingle.'.$i.'.Table_Count');
		echo $this->Form->input('ExtractSingle.'.$i.'.Row_Count');
		echo $this->Form->input('ExtractSingle.'.$i.'.Cell_Count');
		echo $this->Form->input('ExtractSingle.'.$i.'.Div_Count');
		echo $this->Form->input('ExtractSingle.'.$i.'.Before_Unique_Code');
		echo $this->Form->input('ExtractSingle.'.$i.'.After_Unique_Code');
		echo $this->Form->input('ExtractSingle.'.$i.'.Initial_Bef_Unique_Code');
		echo $this->Form->input('ExtractSingle.'.$i.'.is_csv_format');
	?>
	</fieldset>
<?php } 
	echo $this->Form->end(__('Submit', true));
?>
</div>