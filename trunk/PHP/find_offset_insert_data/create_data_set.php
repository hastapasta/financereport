<html>
<head>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="animatedcollapse.js">

/***********************************************
* Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
***********************************************/

</script>


<script type="text/javascript">


animatedcollapse.addDiv('dataset','fade=1,height=280px')

animatedcollapse.addDiv('bodytable','fade=1,height=740px')

animatedcollapse.addDiv('colheadtable','fade=1,height=740px')

animatedcollapse.addDiv('rowheadtable','fade=1,height=740px')

animatedcollapse.addDiv('scheduletable','fade=1,height=100px')

animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
	//$: Access to jQuery
	//divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
	//state: "block" or "none", depending on state
}

animatedcollapse.init()

</script>
</head>

<?php
//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());
	include("functions.php");

	db_utility::db_connect();
	
	/*schema_arrays::$table_job_info = array (
	array ("field" => "data_set","type" => "varchar", "length" =>  45),
	array ("field" => "url_static","type" => "varchar", "length" =>  200),
    array ("field" => "pre_process_func_name","type" => "varchar", "length" =>  45),
    array ("field" => "post_process_func_name","type" => "varchar", "length" =>  45),
    array ("field" => "pre_no_data_check_func","type" => "varchar", "length" =>  45),
    array ("field" => "pre_job_process_func_name","type" => "varchar", "length" =>  45),
    array ("field" => "post_job_process_func_name","type" => "varchar", "length" =>  45),
    array ("field" => "source","type" => "varchar", "length" =>  20),
    array ("field" => "data_group","type" => "varchar", "length" =>  25)
	);
	
	schema_arrays::$table_extract_table = array (
    array ("field" => "table_count","type" => "int", "length" =>  11),
    array ("field" => "top_corner_row","type" => "int", "length" =>  11),
    array ("field" => "number_of_columns","type" => "int", "length" =>  11),
    array ("field" => "column_th_tags","type" => "boolean", "length" =>  1),
    array ("field" => "rowsofdata","type" => "int", "length" =>  11),
    array ("field" => "rowinterval","type" => "int", "length" =>  11),
    array ("field" => "end_data_marker","type" => "varchar", "length" =>  20),
    array ("field" => "column1","type" => "int", "length" =>  11),
    array ("field" => "column2","type" => "int", "length" =>  11),
    array ("field" => "column3","type" => "int", "length" =>  11),
    array ("field" => "column4","type" => "int", "length" =>  11),
    array ("field" => "column5","type" => "int", "length" =>  11),
    array ("field" => "column6","type" => "int", "length" =>  11)
    );
    
    schema_arrays::$table_schedule = array (
    array ("field" => "Repeat_Type","type" => "int", "length" =>  5),
    array ("field" => "companygroup","type" => "varchar", "length" =>  40)
    );*/
   
	
	
	
	
		
	echo "	<body> ";
	echo "NOTES: <BR>";
	echo "+Commas and ".htmlspecialchars("&nbsp;")." are replaced before inserting into the db.<BR>";
	echo "+Preserve value means that the existing value in the form won't be overwritten from the db when \"Refresh Fields\" is clicked.<BR>";
	echo "+\"Initial Before Unique Code\" search is done before any table tag searches.<BR>";
	
	parse_table_structure(false);
	
	?>
	
<!--<form name="UpdateValues" action="regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=form" method=POST > -->
<!--  
<a href="#" rel="toggle[functions]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('functions')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('functions')">Slide Up</a>

<div id="functions" style="width: 400px; background: #FFFFFF">

This is some text.
</div>
-->

<form name="InsertDataSet" action="InsertDataSet.php" method=POST > 	
					<table>
						<tr><td></td><td>
							<!-- <select name="data_set" onchange="alert(this.value);" onchange="UpdateField(this.selectedIndex);"> -->
							<select name="data_set_copy" onchange="UpdateField(this.selectedIndex);">
								<?php
									$query2 = "select distinct Data_Set from job_info";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									for ($j=0;$j<mysql_num_rows($result2);$j++)
									{
										$row2 = mysql_fetch_array($result2);
										echo "<option value=\"".$row2['Data_Set']."\">".$row2['Data_Set']."</option>\n";
									}
								
								?>
								<!-- <option value="custom_eps_chart">Custom EPS Chart</option> -->
								
							</select>	</td>
							<!-- have to change the insertdata action afterwards since the url requires parameters -->
						<td>
							<input type="submit" value="Refresh Fields" name="submit_update" 
							onclick="InsertDataSet.action='regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=form'; return true" >
						</td>
							</tr>
						</table>
					
<a href="#" rel="toggle[dataset]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a>

Job Properties:
<div id="dataset" style="background: #FFFFFF">
							<table>
							<tr><td>Preserve<br>Value</td></tr>
<?php 

	for($i = 0; $i < sizeof(schema_arrays::$table_job_info); ++$i)
	{
		$field = schema_arrays::$table_job_info[$i]['field'];
		$type = schema_arrays::$table_job_info[$i]['type'];
		$length = schema_arrays::$table_job_info[$i]['length'];
		
		
		echo "<tr><td><input type=\"checkbox\" name=\"ck_".$field."\" /></td>\n";
		if ($length>5 && $length<40)
			$size=$length;
		else if ($length >= 40)
			$size=40;
		echo "<td>".$field.": </td><td><input type=\"text\" name=\"".$field."\" size=".$size." maxlength=\"".$length."\" ></td></tr>\n";
		
	}
?>
							
</table>
</div>
<BR>
<BR>
<a href="#" rel="toggle[bodytable]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a>

Table body:
<div id="bodytable" style="background: #FFFFFF;display:none">

<table>
<tr><td>Preserve<br>Value</td></tr>
<?php 




	for($i = 0; $i < sizeof(schema_arrays::$table_extract_table); ++$i)
	{
		$field = schema_arrays::$table_extract_table[$i]['field'];
		$type = schema_arrays::$table_extract_table[$i]['type'];
		$length = schema_arrays::$table_extract_table[$i]['length'];
		
		
		echo "<tr><td><input type=\"checkbox\" name=\"ck_".$field."\" /></td>\n";
		if ($length>5 && $length<40)
			$size=$length;
		else if ($length >= 40)
			$size=40;
		echo "<td>".$field.": </td><td><input type=\"text\" name=\"body_".$field."\" size=".$size." maxlength=\"".$length."\" ></td></tr>\n";
		
		
		
		
		
	}


?>
</table>
</div>
<BR>
<BR>

<a href="#" rel="toggle[colheadtable]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a>
Column Headers:

<div id="colheadtable" style="background: #FFFFFF;display:none">

<table>
<tr><td>Preserve<br>Value</td></tr>
<?php 




	for($i = 0; $i < sizeof(schema_arrays::$table_extract_table); ++$i)
	{
		$field = schema_arrays::$table_extract_table[$i]['field'];
		$type = schema_arrays::$table_extract_table[$i]['type'];
		$length = schema_arrays::$table_extract_table[$i]['length'];
		
		
		echo "<tr><td><input type=\"checkbox\" name=\"ck_".$field."\" /></td>\n";
		if ($length>5 && $length<40)
			$size=$length;
		else if ($length >= 40)
			$size=40;
		echo "<td>".$field.": </td><td><input type=\"text\" name=\"colhead_".$field."\" size=".$size." maxlength=\"".$length."\" ></td></tr>\n";
		
		
		
		
		
	}


?>
</table>
</div>
<BR>
<BR>
<a href="#" rel="toggle[rowheadtable]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a>

Row Headers:
<div id="rowheadtable" style="background: #FFFFFF;display:none">

<table>
<tr><td>Preserve<br>Value</td></tr>
<?php 

	for($i = 0; $i < sizeof(schema_arrays::$table_extract_table); ++$i)
	{
		$field = schema_arrays::$table_extract_table[$i]['field'];
		$type = schema_arrays::$table_extract_table[$i]['type'];
		$length = schema_arrays::$table_extract_table[$i]['length'];
		
		
		echo "<tr><td><input type=\"checkbox\" name=\"ck_".$field."\" /></td>\n";
		if ($length>5 && $length<40)
			$size=$length;
		else if ($length >= 40)
			$size=40;
		echo "<td>".$field.": </td><td><input type=\"text\" name=\"rowhead_".$field."\" size=".$size." maxlength=\"".$length."\" ></td></tr>\n";		
		
	}
?>
</table>
</div>
<BR>
<BR>
<a href="#" rel="toggle[scheduletable]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a>

Schedule Properties:
<div id="scheduletable" style="background: #FFFFFF;display:none">

<table>
<tr><td>Preserve<br>Value</td></tr>
<?php 

	for($i = 0; $i < sizeof(schema_arrays::$table_schedule); ++$i)
	{
		$field = schema_arrays::$table_schedule[$i]['field'];
		$type = schema_arrays::$table_schedule[$i]['type'];
		$length = schema_arrays::$table_schedule[$i]['length'];
		
		
		echo "<tr><td><input type=\"checkbox\" name=\"ck_".$field."\" /></td>\n";
		if ($length>5 && $length<40)
			$size=$length;
		else if ($length >= 40)
			$size=40;
		echo "<td>".$field.": </td><td><input type=\"text\" name=".$field." size=".$size." maxlength=\"".$length."\" ></td></tr>\n";		
		
	}
?>
</table>
</div>
<table>

					

										
							<tr><td><input type="submit" value="Submit" name="submit_msg" ></td></tr>
						
					</table>
				</form>
	
<?php

if(isset($_POST['submit_update'])) 
	{
		/* A data set was selected so the form fields need to be populated with existing values from the database. */
		$populate_ds_values = true;
		$data_set_input = $_POST['data_set_input'];
		/*$url_static = $_POST['static_url'];
		$url_dynamic = $_POST['dynamic_url'];
		$table_count = $_POST['tables'];
		$cell_count = $_POST['cells'];
		$row_count = $_POST['rows'];
		$div_count = $_POST['divs'];
		$initial_before_code = $_POST['initial_before'];
		$before_code = $_POST['before'];
		$after_code = $_POST['after'];
		$pre_func = $_POST['pre_func']
		$post_func = $_POST['post_func']*/
		$query1 = "select job_info.*,extract_info.* from job_info,extract_info where job_info.extract_key=extract_info.primary_key data_set='".$_POST['data_set_copy']."'";
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		//should only return one row since there is a database constraint on unique data sets.
		$row1 = mysql_fetch_array($result1);
		$data_set_input = $row1['data_set_copy'];
?>

<SCRIPT LANGUAGE="JavaScript">

function toggleMe(a){
	  var e=document.getElementById(a);
	  if(!e)return true;
	  if(e.style.display=="none"){
	    e.style.display="block"
	  } else {
	    e.style.display="none"
	  }
	  return true;
	}

form=document.InsertDataSet;

<?php
  if (isset($_POST['ck_data_set_input']))
  {
  	echo "form.ck_data_set_input.checked=true;";
		echo "form.data_set_input.value = '".$_POST['data_set_input']."';";
	}
	else
		echo "form.data_set_input.value='".$_POST['data_set']."';";
		
	if (isset($_POST['ck_static_url']))
  {
  	echo "form.ck_static_url.checked=true;";
		echo "form.static_url.value = '".$_POST['static_url']."';";
	}
	else
		echo "form.static_url.value='".$row1['url_static']."';";
		
		if (isset($_POST['ck_pre_process_func_name']))
  {
  	echo "form.pre_process_func_name.checked=true;";
		echo "form.pre_process_func_name.value = '".$_POST['pre_process_func_name']."';";
	}
	else
		echo "form.pre_process_func_name.value='".$row1['pre_process_func_name']."';";
		
		if (isset($_POST['ck_post_process_func_name']))
  {
  	echo "form.post_process_func_name.checked=true;";
		echo "form.post_process_func_name.value = '".$_POST['post_process_func_name']."';";
	}
	else
		echo "form.post_process_func_name.value='".$row1['post_process_func_name']."';";
		
		if (isset($_POST['ck_pre_nodata_check_func_name']))
  {
  	echo "form.ck_pre_nodata_check_func_name.checked=true;";
		echo "form.pre_nodata_check_func_name.value = '".$_POST['pre_nodata_check_func_name']."';";
	}
	else
		echo "form.pre_nodata_check_func_name.value='".$row1['pre_nodata_check_func_name']."';";
		

		
	if (isset($_POST['ck_table_count']))
  {
  	echo "form.ck_table_count.checked=true;";
		echo "form.table_count.value = '".$_POST['table_count']."';";
	}
	else
		echo "form.table_count.value='".$row1['Table_Count']."';";
		
	if (isset($_POST['ck_top_corner_row']))
  {
  	echo "form.ck_top_corner_row.checked=true;";
		echo "form.top_corner_row.value = '".$_POST['top_corner_row']."';";
	}
	else
		echo "form.top_corner_row.value='".$row1['top_corner_row']."';";
		
	if (isset($_POST['ck_cells']))
  {
  	echo "form.ck_cells.checked=true;";
		echo "form.cells.value = '".$_POST['cells']."';";
	}
	else
		echo "form.cells.value='".$row1['Cell_Count']."';";
		
	if (isset($_POST['ck_divs']))
  {
  	echo "form.ck_divs.checked=true;";
		echo "form.divs.value = '".$_POST['divs']."';";
	}
	else
		echo "form.divs.value='".$row1['Div_Count']."';";
		
	if (isset($_POST['ck_initial_before']))
  {
  	echo "form.ck_initial_before.checked=true;";
		echo "form.initial_before.value = '".$_POST['initial_before']."';";
	}
	else
		echo "form.initial_before.value='".$row1['Initial_Bef_Unique_Code']."';";
		
	if (isset($_POST['ck_before']))
  {
  	echo "form.ck_before.checked=true;";
		echo "form.before.value = '".$_POST['before']."';";
	}
	else
		echo "form.before.value='".$row1['Before_Unique_Code']."';";
		
	if (isset($_POST['ck_after']))
  {
  	echo "form.ck_after.checked=true;";
		echo "form.after.value = '".$_POST['after']."';";
	}
	else
		echo "form.after.value='".$row1['After_Unique_Code']."';";
		
	if (isset($_POST['ck_pre_func']))
  {
  	echo "form.ck_pre_func.checked=true;";
		echo "form.pre_func.value = '".$_POST['pre_func']."';";
	}
	else
		echo "form.pre_func.value='".$row1['pre_process_func_name']."';";
		
	if (isset($_POST['ck_post_func']))
  {
  	echo "form.ck_post_func.checked=true;";
		echo "form.post_func.value = '".$_POST['post_func']."';";
	}
	else
		echo "form.post_func.value='".$row1['post_process_func_name']."';";
		
		
?>

</SCRIPT>

<?php
		
		
		
	}
	

	
	








?>
</body>
</html>