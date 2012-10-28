
<script type='text/javascript'>
	$(function(){
		$('.check_all').click(function(){
			$('.index input:checkbox').attr('checked', 'checked');
		});
		$('.uncheck_all').click(function(){
			$('.index input:checkbox').removeAttr('checked');
		});
	});
</script>
<p style='text-align:right'>
	<a href='#' class='check_all'>Check All</a>
	/
	<a href='#' class='uncheck_all'>Uncheck All</a>
</p>
