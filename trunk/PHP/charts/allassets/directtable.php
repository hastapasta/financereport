<?php
require_once '../../common/functions.php';
require_once("../../site/includes/sitecommon.php");

db_utility::db_connect();

$entitygroupid = $_GET['entitygroupid'];
$begindate = $_GET['begindate'];
$enddate = $_GET['enddate'];



$timeframe="CURRENT_MONTH";
if (isset($_GET['timeframe']))
	$timeframe = strtolower($_GET['timeframe']);
	
$order = "ASC";
if (isset($_GET['order']))
	$order = $_GET['order'];

/*$title="";
if (isset($_GET['title']))
	$title=urldecode($_GET['title']);*/
	
$title="";
if ($entitygroupid==3) {
	$title = "Global Forex";
}
else if ($entitygroupid==4) {
	$title = "Commodity Futures";
}
else if ($entitygroupid==5) {
	$title = "Global Equity Indexes";
}
else if ($entitygroupid==1008) {
	$title = "Global Equity Index Futures";
}
else if ($entitygroupid==101023) {
	$title = "Global Sovereign Bonds";
}
else if ($entitygroupid==101024) {
	$title = "Global Credit Default Swaps";
}
else if ($entitygroupid==1) {
	$title = "S&P 500 Performance";
}
else if ($entitygroupid==101027) {
	$title = "Benchmark Global Equity Indexes";
}
	
$showRowNumber='false';
if (isset($_GET['showrow']))
	$showRowNumber=$_GET['showrow'];
	
$metricid="1";
if (isset($_GET['metricid']))
	$metricid=$_GET['metricid'];




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


	/* JQuery plugin example */
	
	<?php IncFunc::jqueryTimeFrameNew();?>
	
	$(function(){

		jQuery.fn.selText = function() {
		    var obj = this[0];
		    if ($.browser.msie) {
		        var range = obj.offsetParent.createTextRange();
		        range.moveToElementText(obj);
		        range.select();
		    } 
			//webkit is chrome (& safari too technically but safari is being handled differently here)
		    else if ($.browser.mozilla || $.browser.opera || $.browser.webkit) {
		        var selection = obj.ownerDocument.defaultView.getSelection();
		        var range = obj.ownerDocument.createRange();
		        range.selectNodeContents(obj);
		        selection.removeAllRanges();
		        selection.addRange(range);
		    } else if ($.browser.safari) {
		        var selection = obj.ownerDocument.defaultView.getSelection();
		        selection.setBaseAndExtent(obj, 0, obj, 1);
		    }
		    return this;
		};
				

		
	
		$( "input:button").button();
		$( "input:button").css("padding",0);

		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 150,
			width: 350,
			modal: true,
			open: function() {
				//alert(generateURL());
				$( "#urllabel" ).html(generateURL());
				$(this).selText().addClass("selected");
				
			},
			buttons: {
				/*"CopyClipboard": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );
					

				
				},*/
				Close: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				//allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});

		$( "#generateurl" )
			.button()
			.click(function() {
				//tmp = $("#generateurl").attr("name");
				//alert(tmp);
				$( "#dialog-form" ).dialog( "open" );
			});
		
	});

	<?php IncFunc::googleTableWorkaround(); ?>

	$(document).ready(function() {

		
		
		var oneDay = 24*60*60*1000;
		var td = new Date();
		td.setDate(td.getDate() - 1);
		td.setMonth(td.getMonth() - 1);
		td.setMonth(td.getMonth() - 12);
		//var fromDay = td.setDay(td.getDay() - 1); 
	 /* rangeDemoFormat = "%e-%b-%Y %H:%i:%s";
	  rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});
	  $("#rangeDemoClear").click( function(e) {
	      $("#rangeDemoStart").val("").change();
	      $("#rangeDemoFinish").val("").change(); } );
	  $("#rangeDemoStart").AnyTime_picker({format:rangeDemoFormat});
	  $("#rangeDemoFinish").AnyTime_picker({format:rangeDemoFormat});*/

	 
	
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
	  else if (!empty($timeframe)) {
		echo  "$(\"#timeframe\").val('".strtoupper($timeframe)."');\n";
		//echo  "$(\"#timeframe\").val('');\n";
		
		/*
		 * Trigger has to appear in file after the event handler function!
		 */
		echo "$(\"#timeframe\").trigger('change');\n";
		echo "sendAndDraw();\n";

	  } 
	  ?>
	
	  
	  		
	 

				
	});

	

	function generateURL() {
		var rangeDemoStart = document.getElementById('rangeDemoStart');
	    var rangeDemoFinish = document.getElementById('rangeDemoFinish');
	    query = window.location.search;
	    newquery = '';

		
		
		/*
		* Strip out the original begindate and endate url parameters
		*/
		query = query.substr(1,query.length);
		query_parts = query.split('&');
		
		for (i=0;i<query_parts.length;i++) {
			if ((query_parts[i].substr(0,9) != 'begindate') && (query_parts[i].substr(0,7) != 'enddate'))
				newquery += '&' + query_parts[i];
		}
		newquery += '&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime();
		newquery += '&type=1';
		newquery = newquery.substr(1,newquery.length);
		


		var url = location.href;
		var url_parts = url.split('?');
		var main_url = url_parts[0]; 		
		//alert(main_url + '?' + newquery);
		return(main_url + '?' + newquery);
	}
</script>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<?php IncFunc::linkStyleCSS();?>
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget(); ?>

<script>

	/*$(document).ready(function(){
	   // Your code here
		$("#timeframe").val(<?php echo "'".$timeframe."'"?>);
		
	 });*/
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
		var entityid = dt.getValue(row[0].row,8);
		var metricid = dt.getValue(row[0].row,9);
		
		<?php //echo "window.location.href = \"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\";";?>
		<?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + entityid + \"&type=1&m=\" + metricid);";?>

	}
 


    



    function sendAndDraw() {

      console.log('here 1');
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
      <?php 
      if ($showRowNumber!='false')
      	echo "options['showRowNumber'] = true;\n";
      
      ?>
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '?order=".$order."&entitygroupid=".$entitygroupid."&begindate='+ (Date.parse(rangeDemoStart.value)).getTime() + '&enddate=' + (Date.parse(rangeDemoFinish.value)).getTime() + '&metricid=".$metricid."&order=ASC';\n";

     //	echo "queryString2 = '?taskid=".$taskid."&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

 

      
      var container1 = document.getElementById('table1');
      //var container2 = document.getElementById('orgchart2');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      //var tableChart2 = new google.visualization.Table(container2);
      

      //alert(dataSourceUrl + queryString1);
      if (window.console) {console.log(dataSourceUrl + queryString1);}
     
      
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
 
 

<?php IncFunc::googleAnalytics();?>
</head>

<body>
<div id="jq-siteContain" >
<?php 
	IncFunc::header1("charts"); 
?>
<div id="pf-body">
<?php 
	IncFunc::apycomDropDownMenu();
?>

<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 70px 0 0 0;font-size: medium;font-weight:bold;"><?php echo strtoupper($title); ?></div>

<div id="pf-form">

<BR>
Time Frame:&nbsp;&nbsp;
Start: <input type="text" id="rangeDemoStart" size="23" />
&nbsp;Finish: <input type="text" id="rangeDemoFinish" size="23" />
<input type="button" id="rangeDemoClear" value="clear" />

<!-- <div id="displaycustom"></div> -->

<BR>
&nbsp;(Preset Time Frames: 
<?php IncFunc::dateSelectNew();?> )<BR>
<BR>


<input type="button" value="Display Table"	onclick="sendAndDraw();return false;"> <br />
<!-- <input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Table"	onclick="sendAndDraw();return false;"> <br /> -->
<br />
</div><!-- pf-form -->

<div id="tmp1" style="float: left;margin-bottom: 20px">



  

	<div id="table1" style="color: #000;"> </div>
<br>
<div style="font-size: 1.5em;">
<input type="button"  id="generateurl" value="Generate URL"	> <br />
</div>
</div> <!--  pf-body -->

</div> <!--  font-black -->


</div> <!--  siteContain -->  

<div id="dialog-form" title="Url">
    <div id="urllabel" class="selected"></div>
	<form>
	<fieldset>
		
	</fieldset>
	</form>
</div>
</body>
</html>
