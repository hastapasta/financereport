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

function wl($the_string)
{
	$fi = fopen( 'logfile2.txt', 'a+');
	$the_string = $the_string."\n";
	fputs( $fi, $the_string, strlen($the_string) );
	fclose( $fi );
}

function get_data($url,$form_properties)
{
	$timeout = 5;
  if ($form_properties == "")
  {
	  $ch = curl_init();
	  wl("in get data");
	  curl_setopt($ch,CURLOPT_URL,$url);
	  curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
	  curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
	  
	}
	else
	{
		define('POSTURL', $url);
		define('POSTVARS', $form_properties);
		
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



?>