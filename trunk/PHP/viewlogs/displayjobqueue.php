<?php
require_once '../common/functions.php';

db_utility::db_connect();
?>
<html>
<body>
<table border="10">
<?php 
	$sql = "select * from job_queue ";
	$fields = array('id','start_time','task_id','status','priority','queued_time');
	db_utility::maketable($sql,$fields);

?>
</table>





</body>

</html>