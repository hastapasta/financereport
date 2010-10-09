<SCRIPT LANGUAGE="JavaScript">
//function UpdateField(index) {
//form=document.InsertDataSet;
//form.data_set_input.value = form.data_set.options[index].text
//}
</SCRIPT>
<html>


<?php

/* 4/18/2010 - I'd like to add a "preserve value" check box column for each field in the form section. 
								Then when you select a field from the data_set drop down box, those fields which aren't
								checked will be overwritten with the existing value from the data base. */

include("functions.php");
/* input values */
$parse_external_url = 1;
//$url_val = 'http://bloomberg.econoday.com/byshoweventfull.asp?fid=441986&cust=bloomberg&year=2010#top';
//$url_val = 'http://www.bls.gov/lau/home.htm';
$url_val = $_COOKIE['web_page'];
//$extract_value = '444';
/*end input values */
$global_table_count=0;
$global_row_count=0;
$global_cell_count=0;
$global_div_count=0;

$frame = $_GET['frame'];
$seek_offset = $_GET['offset'];

/*function get_data($url)
	{
  $ch = curl_init();
  $timeout = 5;
  curl_setopt($ch,CURLOPT_URL,$url);
  curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
  curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
  $data = curl_exec($ch);
  curl_close($ch);
  return $data;
	}*/



function parse_table_structure($bDisplay)
{
	global $url_val, $parse_external_url, $seek_offset;
	global $global_table_count,$global_row_count,$global_cell_count,$global_div_count;
	
	function custom_echo($bDisplay,$string)
	{
		
		if ($bDisplay == true)
			echo $string;
	}
			
	
	

	
	
	$html = "<html></html>";

if ($parse_external_url == true)
{
	$returned_content = get_data($url_val,"");
}
else
{
	$returned_content=$html;
}

	$open_table_regex="/<table[^>]*>/i";
$close_table_regex="/<\/table[^>]*>/i";
$done = false;
$count = 0;
$offset = 0;

while (!$done)
{
	if (preg_match($open_table_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
	{
		if ($seek_offset > $matches[0][1])
		//table tag is before seek_offset
		{
			
			
			$offset = $matches[0][1]+1;
			$previous_offset = $offset;
			//echo "<BR>location: ".$offset;
			custom_echo($bDisplay,"<BR>location: ".$offset);
			$count++;
		}
		else
			$done = true;
	}
	else
	{
		$done = true;
	}
}

custom_echo($bDisplay,"<BR>There are ".$count." open table tags preceeding offset ".$seek_offset.".<BR>");
$global_table_count = $count;
//echo "<BR>There are ".$count." open table tags preceeding offset ".$seek_offset.".<BR>";

if ($count != 0)
{

	$open_tr_regex = "/<tr[^>]*>/i";
	$done = false;
	$count = 0;
	$save_offset = $previous_offset;
	$offset = $previous_offset;
	
	
	while (!$done)
	{
		if (preg_match($open_tr_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
		{
			if ($seek_offset > $matches[0][1])
			//table tag is before seek_offset
			{
				
				$offset = $matches[0][1]+1;
				$previous_offset = $offset;
				//echo "<BR>location: ".$offset;
				custom_echo($bDisplay,"<BR>location: ".$offset);
				$count++;
			}
			else
				$done = true;
		}
		else
		{
			$done = true;
		}
	}

	//echo "<BR>There are ".$count." open tr tags between offset ".$save_offset." and ".$seek_offset.".<BR>";
	custom_echo($bDisplay,"<BR>There are ".$count." open tr tags between offset ".$save_offset." and ".$seek_offset.".<BR>");
	$global_row_count = $count;
}

if ($count != 0)
{

	$open_td_regex = "/<td[^>]*>/i";
	$done = false;
	$count = 0;
	$save_offset = $previous_offset;
	$offset = $previous_offset;
	
	
	while (!$done)
	{
		if (preg_match($open_td_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
		{
			if ($seek_offset > $matches[0][1])
			//table tag is before seek_offset
			{
				
				$offset = $matches[0][1]+1;
				$previous_offset = $offset;
				//echo "<BR>location: ".$offset;
				custom_echo($bDisplay,"<BR>location: ".$offset);
				$count++;
			}
			else
				$done = true;
		}
		else
		{
			$done = true;
		}
	}

	//echo "<BR>There are ".$count." open td tags preceeding offset ".$save_offset." and ".$seek_offset.".<BR>";
	custom_echo($bDisplay,"<BR>There are ".$count." open td tags preceeding offset ".$save_offset." and ".$seek_offset.".<BR>");
	$global_cell_count = $count;
}

$close_td_regex = "%</td>(?i)%";
if (preg_match($close_td_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
{
	$end = $matches[0][1]+1;
	$length = $end - $offset;
	//echo "<BR>Contents of last td open and close tags:<BR>";
	//echo "<BR>".htmlspecialchars(substr($returned_content, $offset - 1, $length + 5))."<BR>";
	custom_echo($bDisplay,"<BR>Contents of last td open and close tags:<BR>");
	custom_echo($bDisplay,"<BR>".htmlspecialchars(substr($returned_content, $offset - 1, $length + 5))."<BR>");
	
}
else
{
	echo "Issue with processing open and close td tags.";
}

if ($count != 0)
{
	
	$open_div_regex = "/<div[^>]*>/i";
	$done = false;
	$count = 0;
	$save_offset = $previous_offset;
	$offset = $previous_offset;
	
	while (!$done)
	{
		if (preg_match($open_div_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
		{
			if ($seek_offset > $matches[0][1])
			//table tag is before seek_offset
			{
				
				$offset = $matches[0][1]+1;
				$previous_offset = $offset;
				//echo "<BR>location: ".$offset;
				custom_echo($bDisplay,"<BR>location: ".$offset);
				$count++;
			}
			else
				$done = true;
		}
		else
		{
			$done = true;
		}
	}

	//echo "<BR>There are ".$count." open div tags preceeding offset ".$save_offset." and ".$seek_offset.".<BR>";
	custom_echo($bDisplay,"<BR>There are ".$count." open div tags preceeding offset ".$save_offset." and ".$seek_offset.".<BR>");
	$global_div_count=$count;
	
}

//display the contents between the last <div> tag and its corresponding </div> tag

if ($count > 0)
{
//find the close </div> tag
$close_div_regex = "%</div>(?i)%";
if (preg_match($close_div_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
{
	$end = $matches[0][1]+1;
	$length = $end - $offset;
	//echo "<BR>Contents of last div open and close tags:<BR>";
	//echo "<BR>".htmlspecialchars(substr($returned_content, $offset - 1, $length + 6))."<BR>";
	custom_echo($bDisplay,"<BR>Contents of last div open and close tags:<BR>");
	custom_echo($bDisplay,"<BR>".htmlspecialchars(substr($returned_content, $offset - 1, $length + 6))."<BR>");
	
}
else
{
	echo "<BR>Issue with processing open and close div tags.<BR>";
}
	

}
}

if ($frame == 'parent')
{
?>

<frameset cols="50%,*">
 	<frame src="http://win-d2sjsg6emdd/dev/regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=display" name=table_structure_display>
  <frame src="http://win-d2sjsg6emdd/dev/regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=form" name=table_structure_form>
<!--  <frame src=<?=$PHP_SELF?>?frame=whc_left#here name=whc_left>
  <frame src=<?=$PHP_SELF?>?frame=whc_right name=whc_right> -->
 </frameset>
 
<?php



}
else if ($frame == 'display')
{
echo "	<body> ";
echo "<BR>processing offset :".$seek_offset."<BR>";

parse_table_structure(true);





/*
	Count the number of open table tags prior to the offset location
*/
///<([^<]*444[^>]*)>/i

}
else if ($frame == 'form')
{
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
	
	
		
	echo "	<body> ";
	echo "NOTES: <BR>";
	echo "+Commas and ".htmlspecialchars("&nbsp;")." are replaced before inserting into the db.<BR>";
	echo "+Preserve value means that the existing value in the form won't be overwritten from the db when \"Refresh Fields\" is clicked.<BR>";
	echo "+\"Initial Before Unique Code\" search is done before any table tag searches.<BR>";
	
	parse_table_structure(false);
	
	?>
	
<!--<form name="UpdateValues" action="regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=form" method=POST > -->

<form name="InsertDataSet" action="InsertDataSet.php" method=POST > 	
					<table>
						<tr><td></td><td>
							<!-- <select name="data_set" onchange="alert(this.value);" onchange="UpdateField(this.selectedIndex);"> -->
							<select name="data_set" onchange="UpdateField(this.selectedIndex);">
								<?php
									$query2 = "select distinct Data_Set from extract_info";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									for ($j=0;$j<mysql_num_rows($result2);$j++)
									{
										$row2 = mysql_fetch_array($result2);
										echo "<option value=\"".$row2[Data_Set]."\">".$row2[Data_Set]."</option>";
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
					
							
							<table>
							<tr><td>Preserve<br>Value</td></tr>
							<tr><td><input type="checkbox" name="ck_data_set_input" /></td>
								<td>Data Set: </td><td><input type="text" name="data_set_input" size=30 maxlength="30"></td></tr>
<?php

echo "<tr><td><input type=\"checkbox\" name=\"ck_static_url\" /></td>";
echo "<td>Static URL: </td><td><input type=\"text\" name=\"static_url\" size=30 maxlength=\"200\" value=\"".$url_val."\"></td></tr>";

echo "<tr><td><input type=\"checkbox\" name=\"ck_dynamic_url\" /></td>";
echo "<td>Dynamic URL: </td><td><input type=\"text\" name=\"dynamic_url\" size=30 maxlength=\"200\" ></td></tr>";

echo "<tr><td><input type=\"checkbox\" name=\"ck_tables\" /></td>";
echo "<td>Table Count: </td><td><input type=\"text\" name=\"tables\" size=5 maxlength=\"5\" value=\"".$global_table_count."\"></td></tr>";

echo "<tr><td><input type=\"checkbox\" name=\"ck_rows\" /></td>";
echo "<td>Row Count: </td><td><input type=\"text\" name=\"rows\" size=5 maxlength=\"5\" value=\"".$global_row_count."\"></td></tr>";

echo "<tr><td><input type=\"checkbox\" name=\"ck_cells\" /></td>";
echo "<td>Cell Count: </td><td><input type=\"text\" name=\"cells\" size=5 maxlength=\"5\" value=\"".$global_cell_count."\"></td></tr>";

echo "<tr><td><input type=\"checkbox\" name=\"ck_divs\" /></td>";
echo "<td>Div Count: </td><td><input type=\"text\" name=\"divs\" size=5 maxlength=\"5\" value=\"".$global_div_count."\"></td></tr>";
?>
							<tr><td><input type="checkbox" name="ck_initial_before" /></td>
								<td>Initial Before Unique Code: </td><td><input type="text" name="initial_before" size=30 maxlength="30"></td></tr>
							<tr><td><input type="checkbox" name="ck_before" /></td>
								<td>Before Unique Code: </td><td><input type="text" name="before" size=30 maxlength="30"></td></tr>
							<tr><td><input type="checkbox" name="ck_after" /></td>
								<td>After Unique Code: </td><td><input type="text" name="after" size=30 maxlength="30"></td></tr>
										<tr><td><input type="checkbox" name="ck_pre_func" /></td>
								<td>Pre processing function: </td><td><input type="text" name="pre_func" size=30 maxlength="30"></td></tr>
										<tr><td><input type="checkbox" name="ck_post_func" /></td>
								<td>Post processing function: </td><td><input type="text" name="post_func" size=30 maxlength="30"></td></tr>
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
		$query1 = "select * from extract_info where data_set='".$_POST['data_set']."'";
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		//should only return one row since there is a database constraint on unique data sets.
		$row1 = mysql_fetch_array($result1);
		$data_set_input = $row1[data_set];
?>

<SCRIPT LANGUAGE="JavaScript">

form=document.InsertDataSet;

<?php
  if (isset($_POST['ck_data_set_input']))
  {
  	echo "form.ck_data_set_input.checked=true;";
		echo "form.data_set_input.value = '".$_POST[data_set_input]."';";
	}
	else
		echo "form.data_set_input.value='".$_POST['data_set']."';";
		
	if (isset($_POST['ck_static_url']))
  {
  	echo "form.ck_static_url.checked=true;";
		echo "form.static_url.value = '".$_POST[static_url]."';";
	}
	else
		echo "form.static_url.value='".$row1[url_static]."';";
		
	if (isset($_POST['ck_dynamic_url']))
  {
  	echo "form.ck_dynamic_url.checked=true;";
		echo "form.dynamic_url.value = '".$_POST[dynamic_url]."';";
	}
	else
		echo "form.dynamic_url.value='".$row1[url_dynamic]."';";
		
	if (isset($_POST['ck_tables']))
  {
  	echo "form.ck_tables.checked=true;";
		echo "form.tables.value = '".$_POST[tables]."';";
	}
	else
		echo "form.tables.value='".$row1[Table_Count]."';";
		
	if (isset($_POST['ck_rows']))
  {
  	echo "form.ck_rows.checked=true;";
		echo "form.rows.value = '".$_POST[rows]."';";
	}
	else
		echo "form.rows.value='".$row1[Row_Count]."';";
		
	if (isset($_POST['ck_cells']))
  {
  	echo "form.ck_cells.checked=true;";
		echo "form.cells.value = '".$_POST[cells]."';";
	}
	else
		echo "form.cells.value='".$row1[Cell_Count]."';";
		
	if (isset($_POST['ck_divs']))
  {
  	echo "form.ck_divs.checked=true;";
		echo "form.divs.value = '".$_POST[divs]."';";
	}
	else
		echo "form.divs.value='".$row1[Div_Count]."';";
		
	if (isset($_POST['ck_initial_before']))
  {
  	echo "form.ck_initial_before.checked=true;";
		echo "form.initial_before.value = '".$_POST[initial_before]."';";
	}
	else
		echo "form.initial_before.value='".$row1[Initial_Bef_Unique_Code]."';";
		
	if (isset($_POST['ck_before']))
  {
  	echo "form.ck_before.checked=true;";
		echo "form.before.value = '".$_POST[before]."';";
	}
	else
		echo "form.before.value='".$row1[Before_Unique_Code]."';";
		
	if (isset($_POST['ck_after']))
  {
  	echo "form.ck_after.checked=true;";
		echo "form.after.value = '".$_POST[after]."';";
	}
	else
		echo "form.after.value='".$row1[After_Unique_Code]."';";
		
	if (isset($_POST['ck_pre_func']))
  {
  	echo "form.ck_pre_func.checked=true;";
		echo "form.pre_func.value = '".$_POST[pre_func]."';";
	}
	else
		echo "form.pre_func.value='".$row1[pre_process_func_name]."';";
		
	if (isset($_POST['ck_post_func']))
  {
  	echo "form.ck_post_func.checked=true;";
		echo "form.post_func.value = '".$_POST[post_func]."';";
	}
	else
		echo "form.post_func.value='".$row1[post_process_func_name]."';";
		
		
?>

</SCRIPT>

<?php
		
		
		
	}
	
}
	
	








?>
</body>
</html>