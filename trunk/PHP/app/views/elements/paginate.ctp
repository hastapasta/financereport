<div class="paging">
<?php if(!$paginator->hasPrev()):?> 
<span class="disabled"><< first</span> 
<?php else: ?> <?php echo $paginator->first('<< first ', array('title'=>'First'), null, array('class'=>'disabled'));?>
&nbsp; <?php endif; ?> 
<?php echo $paginator->prev('< previous', array('title'=>'Previous'), null, array('class'=>'disabled'));?>
&nbsp; 
<?php echo $paginator->numbers(array('separator'=>'    '));?> <?php echo $paginator->next(' next >', array('title'=>'Next'), null, array('class' => 'disabled'));?>
&nbsp; 
<?php if(!$paginator->hasNext()): ?> <span class="disabled"> last
>></span>
 <?php else: ?> <?php echo $paginator->last(' last >>', array('title'=>'Last'), null, array('class'=>'disabled'));?>
<?php endif; ?>




</div>
