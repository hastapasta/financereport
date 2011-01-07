<html>
<!-- Ran into an issue with the onchange event for the select control. The first item
is selected by default, so if the user selects that item with the mouse, the event doesn't fire, hence
the 'select a view' option in the control. The onselect event doesn't work appropriately either and I 
couldn't find any other even that would work the way I want. Maybe this is a firefox issue... --!>
<script type="text/javascript">
function verify()
{
	//alert("here");
	if (listviews.views.value =="1")
	{
		alert("select one of the view names");
		return false;
	}
	else
		editview.forms.submit();
}


</script>
	<body>

	WARNING: This form can be used to change the code of a view but not the NAME.<BR>
	<div name="msg"></div>


<?php

include ("../common/functions.php");

	//mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	//mysql_select_db("mydb") or die(mysql_error());
	
	db_utility::db_connect();
	
	$query1 = "show full tables in ".db_utility::get_database()." where table_type like 'VIEW'";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	
if (isset($_POST['submitviewchange']))
{

	//test creation of view with temporary name
	$viewname = $_COOKIE['viewname'];
	//echo "viewname: ".$viewname."<BR>";
	$viewcode = $_POST['viewcode'];
	//echo "viewcode: ".$viewcode."<BR>";
	$search = $viewname;
	//echo "serach: ".$search."<BR>";
	$replace= "temp_".$viewname;
	//echo "replace: ".$replace."<BR>";
	$tempviewcode = str_replace($search,$replace,$viewcode);
	//echo $tempviewcode."<BR>";
	$result4 = mysql_query($tempviewcode) or die("Failed Query of " . $tempviewcode);
	echo "TESTED CREATION OF VIEW...<BR>";
	
	//if succssfull delete old view and temporary view
	$query3 = "drop view ".$viewname;
	//echo $query3."<BR>";
	$result3 = mysql_query($query3) or die("Failed Query of " . $query3);
	echo "DELETED OLD VIEW...<BR>";
	
	$query3 = "drop view temp_".$viewname;
	//echo $query3."<BR>";
	$result3 = mysql_query($query3) or die("Failed Query of " . $query3);
	echo "DELETED TEST VIEW...<BR>";
	
	//now read to create new view with old name
	
	$result4 = mysql_query($viewcode) or die("Failed Query of " . $viewcode);
	echo "output: ".$result4;
	echo "<BR>SUCCESSFULLY CREATED NEW VIEW<BR>";
	
	
	
	
	
	
}

?>

<form name="listviews" action="editview.php" method=POST>
	<table>
	<tr><td><select name="views" onchange="this.form.submit();">
	<!-- <tr><td><select name="views" onclick="myalert();"> -->
	<?php
	
	$column_name = "Tables_in_".db_utility::get_database();
	
	echo "<option value=\"1\">--select a view--</option>";
	for ($j=0;$j<mysql_num_rows($result1);$j++)
	{
		$row1 = mysql_fetch_array($result1);
		echo "<option value=\"".$row1[$column_name]."\"";
		if (isset($_POST['views']))
		{
			if ($row1[$column_name] == $_POST['views'])
				echo "selected";
		}
		echo ">".$row1[$column_name]."</option>";
	}
	
	
	?>
</select></td></td>
	<tr><td><input type="hidden" value="Submit" name="Submitform" /></td></tr>
</form>


<form name="editview" action="editview.php" onsubmit="return verify()" method=POST>

	<tr><td><textarea name="viewcode" cols="80" rows="15">
		<?php
		
		if(isset($_POST['Submitform'])) 
		{
			if ("1" != $_POST['views'])
			{
				$query2 = "show create view ".$_POST['views'];
				$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
				$row2 = mysql_fetch_array($result2);
				echo $row2['Create View'];
				setcookie('viewname', $_POST['views'] ,mktime (0, 0, 0, 12, 31, 2015));
			}
			
		}
		?>
		</textarea></td></tr>
	<tr><td><input type="submit" value="Submit" name="submitviewchange" /></td></tr>
</table>
</form>
</body>

</html>