<html>
<head>

<script type="text/javascript">
	var companyArray;
	function loadfunc()
{
	<?php
	

	$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
	mysql_select_db("findata") or die(mysql_error());
	
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("findata", $con);

$sql="SELECT * FROM company order by ticker";

$result = mysql_query($sql);

echo "companyArray = new Array();";
$i = 0;
while($row = mysql_fetch_array($result))
  {

  echo "companyArray[".$i."]=\"".$row['ticker']."\";";
  $i++;
  }
?>

}
function showUser(str)
{
if (str=="")
  {
  document.getElementById("txtHint").innerHTML="";
  return;
  } 
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5  
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
    }
  }
var currentTime = new Date()
xmlhttp.open("POST","picklist1.php?q="+str+"&timestamp="+currentTime,true);
xmlhttp.send();
}

function test()
{

if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5  
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    document.getElementById("txtDate").innerHTML=xmlhttp.responseText;
    }
  }
var currentTime = new Date();
xmlhttp.open("GET","picklist2.php?timestamp="+currentTime,true);
xmlhttp.send();
}

function test1()
{

	//var x=document.getElementById('company');
	var z=document.getElementById('company2');
	
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
	    	z.appendChild(elOptNew);
	    
    	}

		//z.add("test","test");
	}

}
</script>
</head>
<body onLoad="loadfunc();">
	<?php
$con = mysql_connect("127.0.0.1:3306", "root", "madmax1.") or die(mysql_error());
mysql_select_db("findata") or die(mysql_error());
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("findata", $con);

$sql="SELECT * FROM company order by ticker";

$result = mysql_query($sql);

/*//echo "<form><select name=\"company\" size=".mysql_num_rows($result)." onchange=\"test(\")>";
echo "<form><select name=\"company\" size=10 onchange=\"test()\">";
while($row = mysql_fetch_array($result))
  {

  echo "<option value=\"".$row[ticker]."\">".$row[ticker]."</option>";
  }
  
 echo "</select></form>";*/
 




?>

Enter text in the text box to filter the list of companies:
<form>
<input type="text" name="tables" id=tables size=30 maxlength="30" onkeyup="test1()">
</select>
</form>

<form><select name="company2" id=company2 size=10 onchange="test()">
</select>
</form>

<form>
<select name="users" onchange="showUser(this.value)">
<option value="">Select a ticker:</option>
<option value="GE">GE</option>
<option value="MSFT">MSFT</option>
<option value="TRV">TRV</option>
<option value="INTC">INTC</option>
</select>
</form>
<div id="txtCompany"><b>Company info will be listed here.</b></div>
<br>

<br />


Enter text in the text box to make an ajax call to get the current date/time:
<form>
<input type="text" name="tables1" size=30 maxlength="30" onkeyup="test()">
</select>
</form>

<div id="txtDate"><b>Date/time info will be listed here.</b></div>
</body>
</html> 