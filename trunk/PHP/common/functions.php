<?php

/*This is legacy from before this code was converted to java.*/
//include("post_processing_functions.php");

class db_utility
{

	static private $functions_dbhost="localhost";
	static private $functions_dbport="3306";
	static private $functions_dbuser="root";
	static private $functions_dbpass="madmax1.";
	static private $functions_database="findata";
	
	
	static public function db_connect()
	{

		mysql_connect(self::$functions_dbhost.":".self::$functions_dbport,self::$functions_dbuser,self::$functions_dbpass) or die (mysql_error());
		mysql_select_db(self::$functions_database) or die(mysql_error());
	
	}
	
	static public function get_database()
	{
		return(self::$functions_database);
	}
}

class schema_arrays
{
	public static $table_job_info = array (
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
	
	public static $table_extract_table = array (
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
    array ("field" => "column6","type" => "int", "length" =>  11),
    array ("field" => "bef_code_col1","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col1","type" => "varchar", "length" =>  20),
    array ("field" => "bef_code_col2","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col2","type" => "varchar", "length" =>  20),
    array ("field" => "bef_code_col3","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col3","type" => "varchar", "length" =>  20),
    array ("field" => "bef_code_col4","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col4","type" => "varchar", "length" =>  20),
    array ("field" => "bef_code_col5","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col5","type" => "varchar", "length" =>  20),
    array ("field" => "bef_code_col6","type" => "varchar", "length" =>  20),
    array ("field" => "aft_code_col6","type" => "varchar", "length" =>  20)
    
    );
    
    public static $table_schedule = array (
    array ("field" => "Repeat_Type","type" => "int", "length" =>  5),
    array ("field" => "companygroup","type" => "varchar", "length" =>  40)
    );
}

function wl($the_string)
{
	$fi = fopen( 'logfile2.txt', 'a+');
	$the_string = $the_string."\n";
	fputs( $fi, $the_string, strlen($the_string) );
	fclose( $fi );
}



function get_data($url,$form_properties)
{

/* IF CURL_EXEC RETURNS NOTHING and no errors are being generated, check
the selinux configuration or disable selinux completely.*/

	$timeout = 5;
	wl("in get data");
	 wl("url:".$url);
  if ($form_properties == "")
  {
	  $ch = curl_init();
	  wl("no form properties");
	 
	  curl_setopt($ch,CURLOPT_URL,$url);
	  curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
	  curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
	  
	}
	else
	{
		define('POSTURL', $url);
		define('POSTVARS', $form_properties);
		wl("with form properties");
		//sample url and form properties
		//define('POSTURL', 'http://data.bls.gov/cgi-bin/surveymost');
		//define('POSTVARS', 'series_id=LNS14000000&survey=ln&format=&html_tables=&delimiter=&catalog=&print_line_length=&lines_per_page=&row_stub_key=&year=&date=&net_change_start=&net_change_end=&percent_change_start=&percent_change_end='); 
		
		$ch = curl_init(POSTURL);
		curl_setopt($ch, CURLOPT_POST      ,1);
		curl_setopt($ch, CURLOPT_POSTFIELDS    ,POSTVARS);
		curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
	  curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
		
		
		
				
				 
		}

		wl("Still here");
		curl_error($ch);
		$data = curl_exec($ch);
		wl("still in get_data");
	  curl_close($ch);
	  wl(strlen($data));
	  echo "<BR>Length of data returned from url: ".strlen($data)."<BR>";
	  return $data;
	
}

function get_value($local_data_set)
{
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("mydb") or die(mysql_error());

$query1 = "select * from extract_info where Data_Set='" . $local_data_set . "'";

echo $query1 . "<br>";

$result = mysql_query($query1) or die("Failed Query of " . $query1);

echo "# of records: " . mysql_num_rows($result);

//while($row = mysql_fetch_array($result))
  $row = mysql_fetch_array($result);
  echo $row['Data_Set'] . " " . $row['Table_Count'] . " " . $row['Row_Count'] . " " . $row['Cell_Count'] . " " . $row['Unique_Code'];
  echo "<br />";
  $tables = $row['Table_Count'];
  $rows = $row['Row_Count'];
  $cells = $row['Cell_Count'];
  $divs = $row['Div_Count'];
  //$url_data = $row['url_static'];
  $url_val = $row['url_static'];
  $post_process_logic = $row['post_process_func_name'];
  if ($row['url_dynamic'] != NULL)
  	$url_val = $url_val.$row['url_dynamic'];
  echo "<BR>URL: ".$url_val."<BR>";
  
  
  $current_offset = 0;
 
  $returned_content = get_data($url_val);
  
  /*
  3/14/2010 - Added an initial regex search
  */
  
  $initial_open_unique_code = $row['Initial_Bef_Unique_Code'];
  if ($initial_open_unique_code != NULL)
  {
  	$initial_open_unique_regex = "/" . $initial_open_unique_code . "/";
  	
  	echo "<br>initial open regex: " . htmlspecialchars($initial_open_unique_regex);
  	
  	preg_match($initial_open_unique_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	
  	$current_offset = $matches[0][1] + strlen($initial_open_unique_code);
  	
  	echo "<br>Offset after initial regex search: ".$current_offset."<br>";
  	
  }
   
  
  
  /*
  End Initial Regex Search
  */
  
  $open_table_regex="/<table[^>]*>/i";
  $previous_offset = 0;
  
  for ($i=0;$i<$tables;$i++)
  {
  	preg_match($open_table_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	$current_offset = $matches[0][1] + 1;
  	echo "<BR>table tag iteration " . $i . ",offset: " . $current_offset;
  	
  }
  
  $open_tr_regex = "/<tr[^>]*>/i";
  
  for ($i=0;$i<$rows;$i++)
  {
  	preg_match($open_tr_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	$current_offset = $matches[0][1] + 1;
  	echo "<BR>row tag iteration " . $i . ",offset: " . $current_offset;
  	
  }
  
  $open_td_regex = "/<td[^>]*>/i";
  
  for ($i=0;$i<$cells;$i++)
  {
  	preg_match($open_td_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	$current_offset = $matches[0][1] + 1;
  	echo "<BR>cell tag iteration " . $i . ",offset: " . $current_offset;
  	
  }
  
  $open_div_regex = "/<div[^>]*>/i";
  
  for ($i=0;$i<$divs;$i++)
  {
  	preg_match($open_div_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  	$current_offset = $matches[0][1] + 1;
  	echo "<BR>div tag iteration " . $i . ",offset: " . $current_offset;
  	
  }
  
  $open_unique_code = $row['Before_Unique_Code'];
  $open_unique_regex = "/" . $open_unique_code . "/";
  
  echo "<br>open regex: " . htmlspecialchars($open_unique_regex);
  
  preg_match($open_unique_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $current_offset);
  $begin_offset = $matches[0][1] + strlen($open_unique_code);
  
  echo "<br>begin offset: ". $begin_offset;
  
  $close_unique_code = $row['After_Unique_Code'];
  $close_unique_regex = "/" . $close_unique_code . "/";
  
  echo "<br>close regex: " . htmlspecialchars($close_unique_regex);
  
  preg_match($close_unique_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $begin_offset);
  $end_offset = $matches[0][1];
  
  echo "<br>end offset: ". $end_offset;
  
  $data_value = substr($returned_content, $begin_offset, $end_offset - $begin_offset);
  
  //remove the commas
  $data_value = str_replace(",","",$data_value);
  
  //If enclosed in parens, switch to negative
  if (strlen(str_replace("(","",$data_value)) < strlen($data_value))
  {
  	$data_value = str_replace("(","",$data_value);
  	$data_value = str_replace(")","",$data_value);
  	$data_value = $data_value * -1;
  }
  
  echo "<br>data value: " . htmlspecialchars($data_value);
  
  if ($post_process_logic != "")
  	$data_value = call_user_func($post_process_logic, $data_value);
  
  return ($data_value);
	
	
}

function parse_table_structure($bDisplay)
{
	global $url_val, $parse_external_url, $seek_offset,$form_properties;
	global $global_table_count,$global_row_count,$global_cell_count,$global_div_count;
	
	function custom_echo($bDisplay,$string)
	{
		
		if ($bDisplay == true)
			echo $string;
	}
			
	
	

	
	
	$html = "<html></html>";

if ($parse_external_url == true)
{
	$returned_content = get_data($url_val,$form_properties);
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

?>