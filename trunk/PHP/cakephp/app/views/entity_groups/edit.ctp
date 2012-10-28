<div class="groups form">
<?php echo $this->Form->create('EntityGroup');
for($i = 0; $i < sizeof($this->data['EntityGroup']); $i++){
	?>
<fieldset><legend><?php __('Edit Group'); ?></legend> <?php
echo $this->Form->hidden('EntityGroup.'.$i.'.id');
echo $this->Form->input('EntityGroup.'.$i.'.name');
echo $this->Form->input("EntityGroup.".$i.'.parent_id', array('options'=>$parents, 'empty'=>array('0'=>'Root'), 'type'=>'select'));
echo $this->Form->input("EntityGroup.".$i.'.description');
echo $this->Form->input("EntityGroup.".$i.'.entitygrouptype');
?></fieldset>
<?php }?> <?php echo $this->Form->end(__('Submit', true));?></div>