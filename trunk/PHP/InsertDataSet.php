<HTML>
	<?php
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
	
	$data_set_input = $_POST['data_set_input'];
	$table_count = $_POST['tables'];
	$cell_count = $_POST['cell'];
	$row_count = $_POST['row'];
	$div_count = $_POST['div'];
	$initial_before_code = $_POST['initial_before'];
	$before_code = $_POST['before'];
	$after_code = $_POST['after'];
	
	
	
	
	?>
	
	<BODY>
		PLACEHOLDER
	</BODY>
</HTML>

<!--
<tr><td>Data Set: </td><td><input type="text" name="data_set_input" size=30 maxlength="30"</td></tr>
<?php

echo "<tr><td>Table Count: </td><td><input type=\"text\" name=\"tables\" size=5 maxlength=\"5\" value=\"".$global_table_count."\"</td></tr>";
echo "<tr><td>Row Count: </td><td><input type=\"text\" name=\"cells\" size=5 maxlength=\"5\" value=\"".$global_row_count."\"</td></tr>";
echo "<tr><td>Cell Count: </td><td><input type=\"text\" name=\"rows\" size=5 maxlength=\"5\" value=\"".$global_cell_count."\"</td></tr>";
echo "<tr><td>Div Count: </td><td><input type=\"text\" name=\"divs\" size=5 maxlength=\"5\" value=\"".$global_div_count."\"</td></tr>";
?>
							<tr><td>Initial Before Unique Code: </td><td><input type="text" name="initial_before" size=30 maxlength="30"</td></tr>
							<tr><td>Before Unique Code: </td><td><input type="text" name="before" size=30 maxlength="30"</td></tr>
							<tr><td>After Unique Code: </td><td><input type="text" name="after" size=30 maxlength="30"</td></
							-->