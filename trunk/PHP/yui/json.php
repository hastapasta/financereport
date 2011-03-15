<html>
<head>

<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/yahoo/yahoo-min.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/json/json-min.js"></script>




<script type="text/javascript">
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
			//document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
			var data = YAHOO.lang.JSON.parse(xmlhttp.responseText);
	
			alert(xmlhttp.responseText);
			alert(data);
			alert(data[10].ticker);
		}
	}
	
	var currentTime = new Date()
	//alert("here");
	//xmlhttp.open("POST","http://localhost/PHP/ajaxsample/cakeajax.php?q="+str+"&timestamp="+currentTime,true);
	xmlhttp.open("POST","http://localhost/PHP/ajaxsample/cakeajax.php?q=12&timestamp="+currentTime,true);
	xmlhttp.send();
}
</script>
</head>


<body>

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



</body>


</html>