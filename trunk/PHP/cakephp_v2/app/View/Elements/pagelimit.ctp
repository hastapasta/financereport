<td colspan="4" align='left'>
	<script type='text/javascript'>
		$(function(){
			$("#PagePageLimit").change(function(){
				var path = "<?php echo $this->request->base; ?>/pagelimit/change/" + $(this).val();
				window.location = path;
			});
		});
	</script>
	<?php 
		$selected_pagesize = ($this->Session->read('Page.pagesize'));
		echo $this->Form->input('Page.PageLimit', array('label'=>'Items per Page:&nbsp&nbsp', 'div'=>array('id'=>'PageLimitLabel'),  'options'=>array(5=>5,10=>10,15=>15,20=>20),'value'=>$selected_pagesize, 'selected'=>$selected_pagesize));
	?>
</td>