<HTML>
	<BODY>
	<?php
	//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());
	echo "this is some text";
	include ("functions.php");
	db_connect();
	
	$data_set_input = $_POST['data_set_input'];
	$url_static = $_POST['static_url'];
	$url_dynamic = $_POST['dynamic_url'];
	$table_count = $_POST['tables'];
	$cell_count = $_POST['cells'];
	$row_count = $_POST['rows'];
	$div_count = $_POST['divs'];
	$initial_before_code = $_POST['initial_before'];
	$before_code = $_POST['before'];
	$after_code = $_POST['after'];
	$pre_func = $_POST['pre_func'];
	$post_func = $_POST['post_func'];
	
	$insert = "INSERT INTO extract_info (Data_Set, Table_Count, Row_Count, Cell_Count, Div_Count, Initial_Bef_Unique_Code, Before_Unique_Code, After_Unique_Code, url_static, url_dynamic, pre_process_func_name, post_process_func_name) VALUES ('" . $data_set_input . "', '" . $table_count . "', '" . $row_count."', '".$cell_count."', '".$div_count."', '".$initial_before_code."', '".$before_code."', '".$after_code."', '".$url_static."', '".$url_dynamic."','".$pre_func."','".$post_func."')";
	
	echo "<BR>".htmlspecialchars($insert)."<BR>";
	
	if (mysql_query($insert))
  		echo "<BR><BR>Value inserted in db.<BR>";  			
  	else
  	 die(mysql_error());
  	
	
	
	?>
	

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