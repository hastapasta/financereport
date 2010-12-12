<?php
$frame = $_GET['frame'];

if ($frame == 'parent')
{
?>


<frameset rows="30%,*">
	<frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=form" name=form>
<frameset cols="50%,*">
 	<frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=internaldata" name=internaldata>
  <frame src="http://win-d2sjsg6emdd/dev/VerifyData.php?frame=webpage" name=webpage>
 </frameset>
</frameset>

<?php
}
else if ($frame == 'internaldata')
{
?>
<html>
	<body>
<table>
<?php
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
	if(isset($_POST['submit_internal'])) 
	{
		
		$selected_data_set = $_POST['data_set'];
		$selected_ticker = $_POST['ticker'];
		
		if (substr($selected_data_set,0,5) == "table")
		{
			$query1 = 	"SELECT view_fact_data.data_set, ticker, value, url_static, fiscalquarter, fiscalyear
		FROM view_fact_data, extract_table
		WHERE view_fact_data.data_set = extract_table.Data_Set
		AND view_fact_data.data_set = '".$selected_data_set."' 
		AND view_fact_data.ticker = '".$selected_ticker."'";
		}
		else
		{
		$query1 = 
		"SELECT view_fact_data.data_set, ticker, value, url_static, fiscalquarter, fiscalyear
		FROM view_fact_data, extract_info
		WHERE view_fact_data.data_set = extract_info.Data_Set
		AND view_fact_data.data_set = '".$selected_data_set."' 
		AND view_fact_data.ticker = '".$selected_ticker."'";
		}

		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		//for ($j=0;$j<2;$j++)
		{
		
			$row1 = mysql_fetch_array($result1);
			
			echo "<TR>";
			echo "<TD>".($j+1)."</TD>";
			echo "<TD>".$row1[data_set]."</TD>";
			echo "<TD><A HREF=\"".$row1[url_static].$row1[ticker]."\" target=webpage>".$row1[ticker]."</A></TD>";
			echo "<TD>".$row1[value]."</TD>";
			echo "<TD>".$row1[fiscalyear]."</TD>";
			echo "<TD>".$row1[fiscalquarter]."</TD>";
			echo "</TR>";
		}
	
	
}


?>
</table>
</body>
</html>


<?php

}
else if ($frame == 'webpage')
{
	
?>
	
THIS IS THE WEBPAGE DATA FRAME

<?php
}
else if ($frame == 'form')
{
	mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("mydb") or die(mysql_error());
?>
<html>
	<script type="text/javascript">
	var companyArray;
	function loadfunc()
{
	<?php
	


$sql="SELECT * FROM company order by ticker";

$result = mysql_query($sql);

echo "companyArray = new Array();";
$i = 0;
while($row = mysql_fetch_array($result))
  {

  echo "companyArray[".$i."]=\"".$row[ticker]."\";";
  $i++;
  }
?>

}
function test1()
{
	
	//var x=document.getElementById('company');
	var z=document.getElementById('ticker');
	
	var a=document.getElementById('tables');
	
	var tmp = a.value.toUpperCase();
	
	for(var i=z.options.length-1;i>=0;i--)
	{
		z.remove(i);
	}

	
	
	for (var y=0;y<companyArray.length;y=y+1)
	{
		//z.add(x.options[y].text,x.options[y].text);
		if (companyArray[y].indexOf(tmp) == 0)
		{
			var elOptNew = document.createElement('option');

    	elOptNew.text = companyArray[y];
    	elOptNew.value = companyArray[y];
    	z.add(elOptNew);
    }

		//z.add("test","test");
	}
}
</script>
	<body onload="loadfunc();">
<table border="1">
<tr>
	<td>
<form name="SelectFactData" action="VerifyData.php?frame=internaldata" method=POST target=internaldata> 	
					<table border="1">
						<tr><td>
							<!-- <select name="data_set" onchange="alert(this.value);" onchange="UpdateField(this.selectedIndex);"> -->
							<select name="data_set">
								<?php
									$query2 = "select distinct data_set from view_fact_data order by data_set";
				
									$result2 = mysql_query($query2) or die("Failed Query of " . $query2);
									
									for ($j=0;$j<mysql_num_rows($result2);$j++)
									{
										$row2 = mysql_fetch_array($result2);
										echo "<option value=\"".$row2[data_set]."\">".$row2[data_set]."</option>";
									}
								
								?>
							</select>
						</td>
						<td>
							<input type="text" name="tables" size=30 maxlength="30" onkeyup="test1()">
						</td>
						<td>
							<select name="ticker" size=5></select>
						</td>
						<td>
							<input type="submit" value="Submit" name="submit_internal" 
							onclick="SelectFactData.action='VerifyData.php?frame=internaldata'; return true" >
						</td></tr>
							</table>
							
</form>
</td>
</tr>
</table>
</body>
</html>

<?php
}
?>
