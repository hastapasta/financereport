<?php
require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$entitygroupid = $_GET['entitygroupid'];
$begindate = $_GET['begindate'];
$enddate = $_GET['enddate'];



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
<style type="text/css">
  #rangeDemoStart, #rangeDemoFinish {
    background-image:url("calendar.png");
    background-position:right center;
    background-repeat:no-repeat; }
</style>
<?php IncFunc::jQuery();?>
<?php IncFunc::jQueryDatePicker();?>
<?php IncFunc::generalDateFunctions();?>

<script>
   
	$(document).ready(function() {
		var oneDay = 24*60*60*1000;
		var td = new Date();
		td.setDate(td.getDate() - 1);
		td.setMonth(td.getMonth() - 1);
		td.setMonth(td.getMonth() - 12);
		//var fromDay = td.setDay(td.getDay() - 1); 
	  rangeDemoFormat = "%e-%b-%Y %H:%i:%s";
	  rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});
	 $("#rangeDemoToday").click( function(e) {
	      $("#rangeDemoFinish").val(rangeDemoConv.format(new Date())).change(); } );
	  $("#rangeDemoClear").click( function(e) {
	      $("#rangeDemoStart").val("").change();
	      $("#rangeDemoFinish").val("").change(); } );
	  $("#rangeDemoStart").AnyTime_picker({format:rangeDemoFormat});
	  $("#rangeDemoFinish").AnyTime_picker({format:rangeDemoFormat});

	  //t1 = new Date();
	  //t1.setTime(1313290898000);
	  //t1 = Date.parse(1313290898000);
	  //alert(t1);

	  <?php 
	  if (!empty($begindate) && !empty($enddate)) {
	  		
	  	echo "t1 = new Date();\n";
	  	echo "t1.setTime(".$begindate.");";
	  	//echo "alert(rangeDemoConv.format(t1));";
	  	echo "$(\"#rangeDemoStart\").
	  		AnyTime_noPicker().\n
			  	val(rangeDemoConv.format(t1)).\n
			    AnyTime_picker(\n
			              { 
			                format: rangeDemoFormat
			               
			              } );\n";
	  	if (strtoupper($enddate) == 'CURRENT') {
	  		echo "t1 = new Date();\n";
	  	}
	  	else {
	  		echo "t1.setTime(".$enddate.");";
	  	}
	  	
	  	 	echo "$(\"#rangeDemoFinish\").
	  		AnyTime_noPicker().\n
			  	val(rangeDemoConv.format(t1)).\n
			    AnyTime_picker(\n
			              { 
			                format: rangeDemoFormat
			               
			              } );\n";
	  	 	
	  	 echo "sendAndDraw();\n";
	
	  }
	  
	  		
	  ?>

				
	});
	$(function(){

		 $("#timeframe").change( function(e) {
			 /* Called when the 'preset' drop down box is changed. */
		    	enddate = new Date();
		    	begindate = new Date();
		    	var tmp = $("#timeframe").val();
		    	if (tmp == 'year')
		        	begindate.setMonth(enddate.getMonth() - 12);
		    	else if (tmp == 'custom1')
			    	begindate = Date.parseExact("1/20/2011", "M/d/yyyy"); 
		    	else if (tmp == 'month')
		        	begindate.setMonth(enddate.getMonth() - 1);
		    	else if (tmp == 'week')
		        	begindate.setDate(enddate.getDate() - 7);
		    	else if (tmp == 'day')
		        	begindate.setDate(enddate.getDate() - 1);
		    	else //tmp should == hour
		        	begindate = new Date(enddate - (3600 * 1000));
	        
		    	$("#rangeDemoStart").
			  	AnyTime_noPicker().
			  	//removeAttr("disabled").
			  	val(rangeDemoConv.format(begindate)).
			    AnyTime_picker(
			              { //earliest: dayEarlier,
			                format: rangeDemoFormat
			                //latest: ninetyDaysLater
			              } );
		    	$("#rangeDemoFinish").
			  	AnyTime_noPicker().
			  	//removeAttr("disabled").
			  	val(rangeDemoConv.format(enddate)).
			    AnyTime_picker(
			              { //earliest: dayEarlier,
			                format: rangeDemoFormat
			                //latest: ninetyDaysLater
			              } );
		        	
		        	    
		        	    
		    });
		
		
		/*$('#dialog').dialog({autoOpen:false, title : "HELP"});
		$('.help').click(function(){
			$('#dialog').dialog('open')
		});*/
		
	});

	function generateURL() {
		var rangeDemoStart = document.getElementById('rangeDemoStart');
	    var rangeDemoFinish = document.getElementById('rangeDemoFinish');
	    query = window.location.search;
		query += '&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime();


		var url = location.href;
		var url_parts = url.split('?');
		var main_url = url_parts[0]; 		
		alert(main_url + query);
	}
</script>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<?php IncFunc::linkStyleCSS();?>
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget(); ?>

<script type="text/javascript">

	$(document).ready(function(){
	   // Your code here
		$("#timeframe").val(<?php echo "'".$timeframe."'"?>);
		
	 });
    google.load('visualization', '1', {'packages' : ['table']});
    //google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
            echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2eh2.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
   // var query2;
   
   function genericClickHandler(localTableChart,localQueryWrapper) {

		var row = localTableChart.getSelection();
		
		//var test5 = queryWrapper2;

		var dt = localQueryWrapper.currentDataTable;
		var val = dt.getValue(row[0].row,8);
		<?php //echo "window.location.href = \"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\";";?>
		<?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\");";?>

	}
 


    



    function sendAndDraw() {

      var chart = document.getElementById('table1');
      chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      /*var chart2 = document.getElementById('orgchart2');
      chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
      
  	
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
      //var timeframe = document.getElementById('timeframe').value;
      var rangeDemoStart = document.getElementById('rangeDemoStart');
      var rangeDemoFinish = document.getElementById('rangeDemoFinish');
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true)
      {
          //taskid='0';
          <?php //if (!empty($timeframe)) echo "var timeframe='".$timeframe."';";?>
          firstpass=false;
      }
      
      
      var options = {};
      options['height'] = 600;
      options['width'] = 800;
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '?order=".$order."&entitygroupid=".$entitygroupid."&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&metricid=".$metricid."&order=ASC';\n";

     //	echo "queryString2 = '?taskid=".$taskid."&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

 

      
      var container1 = document.getElementById('table1');
      //var container2 = document.getElementById('orgchart2');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      //var tableChart2 = new google.visualization.Table(container2);
      
  
      
      if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString1)}
     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      /*
      * The [8] parameter is what is used to hide the data column index.
      */
      var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1,[8,9],3);
      google.visualization.events.addListener(tableChart1, 'select', function(event){
    	  genericClickHandler(tableChart1,queryWrapper1);

     });
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
<div id="pf-body">




<br/>
<div id="chartTitle" style="font-size: medium;font-weight:bold;"><u><?php echo $title ?></u></div>
<div id="pf-form">

<BR>
Time Frame:&nbsp;&nbsp;
Start: <input type="text" id="rangeDemoStart" size="22" />
&nbsp;Finish: <input type="text" id="rangeDemoFinish" size="22" />
<!-- <input type="button" id="rangeDemoToday" value="today" /> -->
<input type="button" id="rangeDemoClear" value="clear" />

<div id="displaycustom"></div>


(Preset Time Frames: 
<select id="timeframe" style="background-color: #FFFFFF">
	<option value="year">Last Year</option>
	<option value="month">Last Month</option>
	<option value="week">Last Week</option>
	<option value="day">Last Day</option>
	<option value="hour">Last Hour</option>	
	<option value="custom1">Begin Data Collection (1/20/2011)</option>
	
	
	<!-- <option value="Custom">Custom</option> -->
</select> )<BR>
<BR>


<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Table"	onclick="sendAndDraw();return false;"> <br />
<br />
</div><!-- pf-form -->

<div id="tmp1" style="float: left;margin-bottom: 20px">



  

	<div id="table1" style="color: #000;"> </div>
<br>
<input type="button" style="float: left;clear: both;color: #000000;background-color: #FFFFFF" value="Generate URL"
	onclick="generateURL();return false;"> <br />
</div> <!--  pf-body -->

</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
