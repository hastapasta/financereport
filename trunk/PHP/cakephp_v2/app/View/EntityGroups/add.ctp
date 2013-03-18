<div class="groups form">
<?php echo $this->Form->create('EntityGroup');?>
<fieldset><legend><?php echo __('Add Entity Group'); ?></legend> <?php
echo $this->Form->input('name');
echo $this->Form->input('parent_id', array('options'=>$parents, 'type'=>'select','empty'=>array('0'=>'Root')));
echo $this->Form->input('description');
echo $this->Form->input('type');
?></fieldset>
<?php echo $this->Form->end(__('Submit'));?></div>