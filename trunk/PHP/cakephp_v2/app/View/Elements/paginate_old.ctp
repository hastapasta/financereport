<div class="paging"><?php if(!$this->Paginator->hasPrev()):?> <span
	class="disabled"><< first</span> <?php else: ?> <?php echo $this->Paginator->first('<< first ', array('title'=>'First'), null, array('class'=>'disabled'));?>
&nbsp; <?php endif; ?> <?php echo $this->Paginator->prev('< previous', array('title'=>'Previous'), null, array('class'=>'disabled'));?>
&nbsp; <?php echo $this->Paginator->numbers(array('separator'=>'    '));?> <?php echo $this->Paginator->next(' next >', array('title'=>'Next'), null, array('class' => 'disabled'));?>
&nbsp; <?php if(!$this->Paginator->hasNext()): ?> <span class="disabled"> last
>></span> <?php else: ?> <?php echo $this->Paginator->last(' last >>', array('title'=>'Last'), null, array('class'=>'disabled'));?>
<?php endif; ?></div>
