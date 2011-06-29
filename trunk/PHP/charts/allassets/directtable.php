<?php
require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$entitygroupid = $_GET['entitygroupid'];

$timeframe="hour";
if (isset($_GET['timeframe']))
	$timeframe = strtolower($_GET['timeframe']);
	
$order = "ASC";
if (isset($_GET['order']))
	$order = $_GET['order'];

$title="";
if (isset($_GET['title']))
	$title=urldecode($_GET['title']);
	
$metricid="1";
if (isset($_GET['metricid']))
	$metricid=$_GET['metricid'];




?>

<!DOCTYPE html>
<html>
<head>
<?php IncFunc::jQuery();?>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<link rel="stylesheet" href="../../site/includes/style.css"	type="text/css" />
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget(); ?>

<script type="text/javascript">

	$(document).ready(function(){
	   // Your code here
		$("#timeframe").val(<?php echo "'".$timeframe."'"?>);
		
	 });
    google.load('visualization', '1', {'packages' : ['table']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
            echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2eh2.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
   // var query2;
 


    



    function sendAndDraw() {

      var chart = document.getElementById('table1');
      chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      /*var chart2 = document.getElementById('orgchart2');
      chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
      
  	
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
      var timeframe = document.getElementById('timeframe').value;
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true)
      {
          //taskid='0';
          <?php if (!empty($timeframe)) echo "var timeframe='".$timeframe."';";?>
          firstpass=false;
      }
      
      
      var options = {};
      options['height'] = 600;
      options['width'] = 800;
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '?order=".$order."&entitygroupid=".$entitygroupid."&timeframe='+timeframe + '&metricid=".$metricid."&order=ASC';\n";

     //	echo "queryString2 = '?taskid=".$taskid."&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

     	if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString1)}

      
      var container1 = document.getElementById('table1');
      //var container2 = document.getElementById('orgchart2');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      //var tableChart2 = new google.visualization.Table(container2);
      
      //alert(dataSourceUrl + queryString1);
     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1);
      queryWrapper1.sendAndDraw();

      /*query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2);
      queryWrapper2.sendAndDraw();*/
    }

  </script>
 
 

  
</head>

<body>
<div id="jq-siteContain" >
<?php 
	IncFunc::header2("charts"); 
	IncFunc::yuiDropDownMenu();

?>




<br/>
<div id="chartTitle" style="font-size: medium;font-weight:bold;"><u><?php echo $title ?></u></div>
<div id="pf-form" style="text-align:left;font-size:1.5em;">

<BR>
Time Frame: <BR>
<select id="timeframe" style="background-color: #FFFFFF">
	<option value="hour">Last Hour</option>
	<option value="day">Last Day</option>
	<option value="week">Last Week</option>
	<option value="month">Last Month</option>
	<option value="year">Last Year</option>
	<!-- <option value="Custom">Custom</option> -->
</select> <BR>
<BR>


<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Table"	onclick="sendAndDraw();return false;"> <br />
<br />
<br />
</div><!-- pf-form -->

<div id="tmp1" style="float: left;margin-bottom: 20px">



  

	<div id="table1" style="color: #000;"> </div>


</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
