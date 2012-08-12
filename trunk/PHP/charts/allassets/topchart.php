<?php
require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();

//$taskid = $_GET['taskid'];
$begindate = $_GET['begindate'];
$enddate = $_GET['enddate'];




?>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">
  #rangeDemoStart, #rangeDemoFinish {
    background-image:url("../../site/includes/images/calendar.png");
    background-position:right center;
    background-repeat:no-repeat; }
</style>


<?php IncFunc::jQuery();?>
<?php IncFunc::jQueryDatePicker();?>
<?php IncFunc::generalDateFunctions();?>

<script>

	<? IncFunc::googleTableWorkaround(); ?>
   
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
	  	echo "t1.setTime(".$enddate.");";
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
		<?php IncFunc::jqueryTimeFrame();?>
		 /*$("#timeframe").change( function(e) {
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
		        	
		        	    
		        	    
		    });*/
		
		
		/*$('#dialog').dialog({autoOpen:false, title : "HELP"});
		$('.help').click(function(){
			$('#dialog').dialog('open')
		});*/
		
	});

	function generateURL() {
		var rangeDemoStart = document.getElementById('rangeDemoStart');
	    var rangeDemoFinish = document.getElementById('rangeDemoFinish');
	    query = window.location.search;
		query += '?begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime();
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
<?php IncFunc::googleGadget();?>
<?php IncFunc::googleAnalytics();?>
<script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['table']});
    //google.setOnLoadCallback(function() { sendAndDraw('') });
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
 

	function genericClickHandler(localTableChart,localQueryWrapper) {

		var row = localTableChart.getSelection();
		
		//var test5 = queryWrapper2;

		var dt = localQueryWrapper.currentDataTable;
		var val = dt.getValue(row[0].row,8);
		var metric = dt.getValue(row[0].row,9);
		<?php //echo "window.location.href = \"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\";";?>
		<?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?m=\" + metric +\"&e=\" + val + \"&title=All Assets Indivdual Line Charts\");";?>

	}
    



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
      //var timeframe = document.getElementById('timeframe');
      var rangeDemoStart = document.getElementById('rangeDemoStart');
      var rangeDemoFinish = document.getElementById('rangeDemoFinish');
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true) {
          //taskid='0';
          //timeframe = 'week';
          firstpass=false;
      }
      
      var options = {};
      options['height'] = 400;
      options['width'] = 1000;
      
      //alert (Date.parse(rangeDemoStart.value).getTime());



     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '&entitygroupid=3&metricid=1&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&order=DESC';\n";

     	echo "queryString2 = '&entitygroupid=5&metricid=1&begindate='+ (Date.parse(rangeDemoStart.value)).getTime()  + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&order=DESC';\n";
     	
     	echo "queryString3 = '&entitygroupid=4&metricid=11&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&order=DESC';\n";
     	
     	echo "queryString4 = '&entitygroupid=1008&metricid=11&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&order=DESC';\n";
     	
     	?>
    	 

 

      //alert(dataSourceUrl + queryString1);

      //alert(dataSourceUrl + queryString2);

      //alert(dataSourceUrl + queryString3);
      
      var container1 = document.getElementById('table1');
      var container2 = document.getElementById('table2');
      var container3 = document.getElementById('table3');
      var container4 = document.getElementById('table4');

      /*wrapper = new google.visualization.ChartWrapper({
    	    chartType: 'ColumnChart',
    	    dataTable: [['Germany', 'USA', 'Brazil', 'Canada', 'France', 'RU'],
    	                [700, 300, 400, 500, 600, 800]],
    	    options: {'title': 'Countries'},
    	    containerId: 'visualization'
    	  });*/
     
      
      var tableChart1 = new google.visualization.Table(container1);
      var tableChart2 = new google.visualization.Table(container2);
      var tableChart3 = new google.visualization.Table(container3);
      var tableChart4 = new google.visualization.Table(container4);
	  
	  if (window.console) {console.log(dataSourceUrl + queryString1)}
      if (window.console) {console.log(dataSourceUrl + queryString2)}
      if (window.console) {console.log(dataSourceUrl + queryString3)}
      if (window.console) {console.log(dataSourceUrl + queryString4)}
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1,[8,9],3);

      //var y = null;

      google.visualization.events.addListener(tableChart1, 'select', function(event){
    	  genericClickHandler(tableChart1,queryWrapper1);

     });
      queryWrapper1.sendAndDraw();

      query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2,[8,9,],3);

      google.visualization.events.addListener(tableChart2, 'select', function(event){
          genericClickHandler(tableChart2,queryWrapper2);
 	   });
      queryWrapper2.sendAndDraw();

      query3 && query3.abort();
      query3 = new google.visualization.Query(dataSourceUrl + queryString3);
      query3.setTimeout(120);
      var queryWrapper3 = new QueryWrapper(query3, tableChart3, options, container3,[8,9],3);
      google.visualization.events.addListener(tableChart3, 'select', function(event){
          genericClickHandler(tableChart3,queryWrapper3);
 	   });
      queryWrapper3.sendAndDraw();

      query4 && query4.abort();
      query4 = new google.visualization.Query(dataSourceUrl + queryString4);
      query4.setTimeout(120);
      var queryWrapper4 = new QueryWrapper(query4, tableChart4, options, container4,[8,9],3);
      google.visualization.events.addListener(tableChart4, 'select', function(event){
          genericClickHandler(tableChart4,queryWrapper4);
 	   });
      queryWrapper4.sendAndDraw();
    }

  </script>
<?php IncFunc::googleAnalytics();?>
</head>

<!-- <body style="text-align:left;"> -->
<body>
<div id="jq-siteContain" >

<?php 
	IncFunc::header2("charts"); 
	//echo "<div id=\"yuipadding\" style=\"paddin>";
	IncFunc::apycomDropDownMenu();
	//echo "</div>";

?>
<div id="pf-body">


<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 50px 0 0 0;font-size: medium;font-weight:bold;"><?php echo strtoupper('GLOBAL MARKETS DASHBOARD: FOREX, EQUITY INDEXES, COMMODITIES, EQUITY FUTURES'); ?></div>



<br/>
<div id="pf-form">


<br>
Time Frame:&nbsp;&nbsp;
Start: <input type="text" id="rangeDemoStart" size="18" /> 
&nbsp;&nbsp;Finish: <input type="text" id="rangeDemoFinish" size="18" />
<!-- <input type="button" id="rangeDemoToday" value="today" /> -->
&nbsp;&nbsp;<input type="button" id="rangeDemoClear" value="clear" />&nbsp;

<!-- <div id="displaycustom"></div> -->


(Preset Time Frames: 
<?php IncFunc::dateSelect();?>)<BR>
<BR>

<input type="button" style="color: #000000;background-color: #FFFFFF" value="Update Tables"
	onclick="sendAndDraw();return false;"> <br />
<br />
<br />
</div><!-- pf-form -->


<!-- <div style="margin: 0 200px;"> -->
<div id="pf-chartbody">
<div id="tmp1" style="float: left;padding-bottom: 20px">
<div id="tmp1A" style="font-size: small">Forex Gainers/Losers:
</div>
<div id="table1" style="color: #000;"> </div>
</div>


<div id="tmp2" style="float: left;clear: left;padding-bottom: 20px">
<div id="tmp2A" style="font-size: small">Global Equity Indexes Gainers/Losers:</div>
<div id="table2" style="color: #000;"> </div>
</div>

<div id="tmp3" style="float: left;clear: left;padding-bottom: 20px">
<div id="tmp3A" style="font-size: small">Commodity Futures Gainers/Losers:</div>
<div id="table3" style="color: #000;"> </div>
</div>

<div id="tmp4" style="float: left;clear: left;padding-bottom: 20px">
<div id="tmp4A" style="font-size: small">Equity Futures Gainers/Losers:</div>
<div id="table4" style="color: #000;"> </div>
</div>

<input type="button" style="float: left;clear: both;color: #000000;background-color: #FFFFFF" value="Generate URL"
	onclick="generateURL();return false;"> <br />

</div>


	<!-- <span class='help' style='cursor:pointer'>
		?
	</span>
	 <div id="dialog" title="Basic dialog">
		<p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
	</div> -->

</div> <!--  pf-body -->

</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
