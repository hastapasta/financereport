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

include("../common/functions.php");
/* input values */
$parse_external_url = 1;
//$url_val = 'http://bloomberg.econoday.com/byshoweventfull.asp?fid=441986&cust=bloomberg&year=2010#top';
//$url_val = 'http://www.bls.gov/lau/home.htm';
$url_val = $_COOKIE['web_page'];

if (isset($_COOKIE['form_properties']))
{
	$form_properties = $_COOKIE['form_properties'];
}

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



function parse_table_structure_old($bDisplay)
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

/*if ($frame == 'parent')
{
?>

<frameset cols="50%,*">
 	<frame src="http://localhost/regex_test/regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=display" name=table_structure_display>
  <frame src="http://localhost/regex_test/regex_table_structure.php?offset=<?php	echo $seek_offset;?>&frame=form" name=table_structure_form>
<!--  <frame src=<?=$PHP_SELF?>?frame=whc_left#here name=whc_left>
  <frame src=<?=$PHP_SELF?>?frame=whc_right name=whc_right> -->
 </frameset>
 
<?php



}*/

echo "	<body> ";
echo "<BR>processing offset :".$seek_offset."<BR>";

parse_table_structure(true);





/*
	Count the number of open table tags prior to the offset location
*/
///<([^<]*444[^>]*)>/i




		
		
		
	
	

	
	








?>

<A HREF="create_data_set.php">Create Data Set</A>
</body>
</html>
