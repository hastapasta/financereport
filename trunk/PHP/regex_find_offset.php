<html>
	<body>
Note: -For now you need to manually preceed any special characters (e.g. forward slashes (/), decimal points (.), etc.) with back slashes (\).<BR>
	
<?php

/*TO DO:
-Add a link that will bring up the parsed url in a separate window.
-On regex_table_structure, show the full contents of the last <TD> </TD> tags.
-Add close tag info to regex_table_structure.


/* input values */
$parse_external_url = 1;
//$url_val = 'http://bloomberg.econoday.com/byshoweventfull.asp?fid=441986&cust=bloomberg&year=2010#top';
//$url_val = 'http://www.bls.gov/lau/home.htm';
$chars_before_after = 200;
/*end input values */


function wl($the_string)
{
	$fi = fopen( 'logfile2.txt', 'w+');
	$the_string = $the_string."\n";
	fputs( $fi, $the_string, strlen($the_string) );
	fclose( $fi );
}

$html = ' 
<html>
<body>
<TABLE zzz>
	<tr>
		<td>Cell A</td>
		<td>
			<table>
				<tr>
					<td>Cell B</td>
					<td>
						<table>
							<tr>
								<td>Cell C</td>
							</tr>
							<table>blap</test>
							<!-- test -->
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
'; 


function get_data($url)
{
  $ch = curl_init();
  $timeout = 5;
  curl_setopt($ch,CURLOPT_URL,$url);
  curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
  curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
  $data = curl_exec($ch);
  curl_close($ch);
  return $data;
}

$url_val='';

if(isset($_POST['submit_msg'])) 
{


//$extract_value = htmlspecialchars($HTTP_POST_VARS['find_text']); 
$extract_value = $_POST['find_text'];
$url_val = $_POST['web_page'];
setcookie('web_page', $url_val ,mktime (0, 0, 0, 12, 31, 2015));



if ($parse_external_url == true)
{
	$returned_content = get_data($url_val);
}
else
{
	$returned_content=$html;
}

?>
<table id="myTable">
<tr>
<td></td>
</tr>
<tr><td><BR><a href="show_raw_html.php">View Raw HTML</A><BR></td></tr>
</table>


<?php

wl('test');

$done = false;
$offset = 0;
//$extract_value_regex = "/<([^<]*".$extract_value."[^>]*)>/i";
$extract_value_regex = "/".$extract_value."/i";
//$extract_value_regex = "/<([^<]*".$extract_value."[^>]*)>(?i)/";

echo "<BR>regex:<BR>".$extract_value_regex."<BR>";
$count = 0;
while (!$done)
{
	//if (preg_match("/<([^<]*444[^>]*)>/i", $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
	if (preg_match($extract_value_regex, $returned_content, $matches, PREG_OFFSET_CAPTURE, $offset))
	{
		$count++;
		$offset = $matches[0][1] + 1;
		echo "<PRE>";
		echo  htmlspecialchars(print_r($matches,true));
		echo "</PRE>";
		echo "<A HREF=\"regex_table_structure.php?offset=".$offset."&frame=parent\">Display Table Structure @ Offset ".$offset."</A><BR>";
		//display string plus amount in $chars_before_after characters before and after
		$begin = $offset - $chars_before_after;
		$length = strlen($matches[0][0]) + (2*$chars_before_after);
		echo "<BR>".$chars_before_after." Characters before and after string:<BR>";
		echo "<BR>".htmlspecialchars(substr($returned_content, $begin, $length))."<BR>";
		foreach ($matches as $match)
		{
			wl($match);
		}
	}
	else
	{
		$done = true;
	}

}
}

?>


	
			<form action="regex_find_offset.php" method=POST > 
				<table>
					<tr><td>FIND TEXT: </td>
					<td><input type="text" name="find_text" size=21 maxlength="30"></td></tr>
					<tr><td><br></td></tr>
					<tr><td>URL:</td>
<?php
if ($url_val == '')
{
	$url_val = $_COOKIE['web_page'];
}
	
echo '<td><input type="text" name="web_page" value="'. $url_val . '" size=100 maxlength="100"></td></tr>';
?>
					<tr><td><input type="submit" value="Submit" name="submit_msg" ></td></tr>
				</table>
			</form>
			
</body>

<SCRIPT LANGUAGE="JavaScript">
function changeContent(){
    var x=document.getElementById('myTable').rows;
    var y=x[0].cells;
    
<?PHP

   echo "y[0].innerHTML=\"OCCURENCES FOUND: ".$count."\"";
    
?>
}
changeContent();
</SCRIPT>


</html>	