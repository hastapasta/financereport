<?php
require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

//$taskid = $_GET['taskid'];




?>

<!DOCTYPE html>
<html>
<head>

<?php IncFunc::jQuery();?>

<script type='text/javascript'>
	$(function(){
		$('#dialog').dialog({autoOpen:false, title : "HELP"});
		$('.help').click(function(){
			$('#dialog').dialog('open')
		});
	});
</script>

<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<link rel="stylesheet" href="../../site/includes/style.css"	type="text/css" />
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget()?>
<script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['table']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
    //added cache buster url parameter for ie.
    
            echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2eh2.jsp?randNum=' + new Date().getTime();";
            
     ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
    var query2;
    var query3;
    var query4;
 


    



    function sendAndDraw() {

      var chart = document.getElementById('table1');
      chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      var chart2 = document.getElementById('table2');
      chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      var chart3 = document.getElementById('table3');
      chart3.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      var chart4 = document.getElementById('table4');
      chart4.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
      
  	  
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
      var timeframe = document.getElementById('timeframe');
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true)
      {
          //taskid='0';
          timeframe.value='week';
          firstpass=false;
      }
      
      var options = {};
      options['height'] = 400;
      options['width'] = 1000;
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '&entitygroupid=3&metricid=1&timeframe='+timeframe.value + '&order=DESC';\n";

     	echo "queryString2 = '&entitygroupid=5&metricid=1&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	echo "queryString3 = '&entitygroupid=4&metricid=11&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	echo "queryString4 = '&entitygroupid=1008&metricid=11&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

 

      //alert(dataSourceUrl + queryString1);

      //alert(dataSourceUrl + queryString2);

      //alert(dataSourceUrl + queryString3);
      
      var container1 = document.getElementById('table1');
      var container2 = document.getElementById('table2');
      var container3 = document.getElementById('table3');
      var container4 = document.getElementById('table4');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      var tableChart2 = new google.visualization.Table(container2);
      var tableChart3 = new google.visualization.Table(container3);
      var tableChart4 = new google.visualization.Table(container4);

      google.visualization.events.addListener(tableChart1, 'select', function(event){
			var row = tableChart1.getSelection();
			alert( "you selected row " + row[0].row + " of second table");
       });
		
      google.visualization.events.addListener(tableChart2, 'select', function(event){
	  		var row = tableChart2.getSelection();
			alert( "you selected row " + row[0].row + " of third table");
   	   });
      google.visualization.events.addListener(tableChart3, 'select', function(event){
	  		var row = tableChart3.getSelection();
			alert( "you selected row " + row[0].row + " of fourth table");
   	  });

      google.visualization.events.addListener(tableChart4, 'select', function(event){
	  		var row = tableChart4.getSelection();
			alert( "you selected row " + row[0].row + " of 1st table");
 	  });

	  //alert(dataSourceUrl + queryString3);   	  
	  
	  if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString1)}
      if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString2)}
      if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString3)}
      if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString4)}
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1);
      queryWrapper1.sendAndDraw();

      query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2);
      queryWrapper2.sendAndDraw();

      query3 && query3.abort();
      query3 = new google.visualization.Query(dataSourceUrl + queryString3);
      query3.setTimeout(120);
      var queryWrapper3 = new QueryWrapper(query3, tableChart3, options, container3);
      queryWrapper3.sendAndDraw();

      query4 && query4.abort();
      query4 = new google.visualization.Query(dataSourceUrl + queryString4);
      query4.setTimeout(120);
      var queryWrapper4 = new QueryWrapper(query4, tableChart4, options, container4);
      queryWrapper4.sendAndDraw();
    }

  </script>
</head>

<body style="text-align:left;">
<div id="jq-siteContain" >
<?php 
	IncFunc::header2("charts"); 
	//echo "<div id=\"yuipadding\" style=\"paddin>";
	IncFunc::yuiDropDownMenu();
	//echo "</div>";

?>




<br/>
<div id="pf-form" style="text-align:left;font-size:1.5em;">

<BR>
Time Frame: <BR>
<select id="timeframe" style="background-color: #FFFFFF">
	<option value="year">Last Year</option>
	<option value="month">Last Month</option>
	<option value="week">Last Week</option>
	<option value="day">Last Day</option>
	<option value="hour">Last Hour</option>	
	
	<!-- <option value="Custom">Custom</option> -->
</select> <BR>
<BR>
<BR>

<input type="button" style="color: #000000;background-color: #FFFFFF" value="Update Tables"
	onclick="sendAndDraw();return false;"> <br />
<br />
<br />
</div><!-- pf-form -->



<div id="tmp1" style="float: left;margin-bottom: 20px">

<!-- Included for Dialog -->



<div id="tmp1A" style="font-size: small">Forex Gainers/Losers:
	<!-- <span class='help' style='cursor:pointer'>
		?
	</span>
	<div id="dialog" title="Basic dialog">
		<p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
	</div> -->
</div>
<div id="table1" style="color: #000;"> </div>
</div>

<div id="tmp2" style="float: left;clear: left;margin-bottom: 20px">
<div id="tmp2A" style="font-size: small">Global Equity Indexes Gainers/Losers:</div>
<div id="table2" style="color: #000;"> </div>
</div>

<div id="tmp3" style="float: left;clear: left;margin-bottom: 20px">
<div id="tmp3A" style="font-size: small">Commodity Futures Gainers/Losers:</div>
<div id="table3" style="color: #000;"> </div>
</div>

<div id="tmp4" style="float: left;clear: left;margin-bottom: 20px">
<div id="tmp4A" style="font-size: small">Equity Futures Gainers/Losers:</div>
<div id="table4" style="color: #000;"> </div>
</div>




</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
