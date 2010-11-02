<html>
	<body>


<?php

include ("functions.php");

	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
	
	$query1 = "show full tables in mydb where table_type like 'VIEW'";
	$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
	
if (isset($_POST['submitviewchange']))
{

	//test creation of view with temporary name
	$viewname = $_COOKIE['viewname'];
	$viewcode = $_POST['viewcode'];
	$search = "`".$viewname."`";
	echo $search."<BR>";
	$replace= "`temp_".$viewname."`";
	$tempviewcode = str_replace($search,$replace,$viewcode);
	$result4 = mysql_query($tempviewcode) or die("Failed Query of " . $tempviewcode);
	echo "TESTED CREATION OF VIEW...<BR>";
	
	//if succssfull delete old view and temporary view
	$query3 = "drop view ".$viewname;
	$result3 = mysql_query($query3) or die("Failed Query of " . $query3);
	echo "DELETED OLD VIEW...<BR>";
	
	$query3 = "drop view temp_".$viewname;
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
	<tr><td><select name="views" onChange="this.form.submit();">
	<?php
	
	for ($j=0;$j<mysql_num_rows($result1);$j++)
	{
		$row1 = mysql_fetch_array($result1);
		echo "<option value=\"".$row1[Tables_in_mydb]."\"";
		if ($row1[Tables_in_mydb] == $_POST[views])
			echo "selected";
		echo ">".$row1[Tables_in_mydb]."</option>";
	}
	
	
	?>
</select></td></td>
	<tr><td><input type="hidden" value="Submit" name="Submitform" /></td></tr>
</form>
<form name="editview" active="editview.php" method=POST>

	<tr><td><textarea name="viewcode" cols="80" rows="15">
		<?php
		
		if(isset($_POST['Submitform'])) 
		{
			$query2 = "show create view ".$_POST[views];
			$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
			$row2 = mysql_fetch_array($result2);
			echo $row2['Create View'];
			setcookie('viewname', $_POST[views] ,mktime (0, 0, 0, 12, 31, 2015));
			
		}
		?>
		</textarea></td></tr>
	<tr><td><input type="submit" value="Submit" name="submitviewchange" /></td></tr>
</table>
</form>
</body>
</html>