<?php
//require_once 'init2.php';

/* OFP 11/20/2011 - I don't know why we're still passing task ids into the datasource. Seems redundant. We don't
 * have multiple tasks gathering GDP or equity index data (yet). 
 */

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$alertid="";

//if (isset($_GET['countryid']))
$countryid = $_GET['countryid'];
//else
//	$error =  "No country id in url. Unable to render chart.";
	//die("No alert id,entity id  or ticker parameter in url. Unable to render chart.");
	
$gdptype = "cp";
if (isset($_GET['gdptype']) && !empty($_GET['gdptype']))
	$gdptype = $_GET['gdptype'];
	
	

if (isset($_GET['begindate']) && !empty($_GET['begindate']))
	$begindate=$_GET['begindate'];
else 
	$begindate='01-Jan-2007';
	
if (isset($_GET['enddate']) && !empty($_GET['enddate']))
	$enddate=$_GET['enddate'];
else 
	$enddate='01-Jan-2016';
	
	
	
	

$title="";
if (isset($_GET['title']))
	$title=urldecode($_GET['title']);

if (!empty($countryid)) {
	$sql = "select name from countries where id=".$countryid;
	$result = mysql_query($sql);
	$row = mysql_fetch_array($result);
	$countryname = $row['name'];
		
		
	$sql = "select entities.id ";
	$sql .= " from entities ";
	$sql .= " join entities_entity_groups on entities_entity_groups.entity_id=entities.id ";
	$sql .= " join countries_entities on countries_entities.entity_id = entities.id ";
	$sql .= " join countries on countries_entities.country_id=countries.id ";
	$sql .= " where  ";
	$sql .= " countries.id=".$countryid;
	$sql .= " and entities_entity_groups.entity_group_id=101037 ";
	//$sql .= " limit 7 ";
	
	//echo $sql;
	
	$result = mysql_query($sql);
	$count = 0;
	while ($row= mysql_fetch_array($result)) {
		$entities.=$row['id'].",";
		$metrics.="1,";
		$tasks.="0,";
		$count++;
	}
	
	//if ($count == 0) {
	//	$error = "No equity index data for the country: ".$countryname;
	//}
	//$entities = substr($entities,0,strlen($entities)-1);
	//$metrics.="12,13";
	//$metrics.="30,29";
}


if ($gdptype=="ppp") {
	$metrics.="12,13";
	$tasks.="30,29";
}
else { //we'll default to current prices gdp
	$metrics.="2,3";
	$tasks.="22,33";
}

if (!empty($countryid)) {
	$sql = "select entities.id from entities ";
	$sql.= " join countries_entities on countries_entities.entity_id=entities.id ";
	$sql.= " where ticker='macro' and country_id=".$countryid;
	
	//echo $sql;
	
	$result = mysql_query($sql);
	$row = mysql_fetch_array($result);
	$entities.=$row['id'].",".$row['id'];
}





//$entityid = str_replace(' ',',',$entityid);
//$entities = split(",",$entityid);


	
//$sql = "select * from entities where id in (".$entityid.")";
//$result = mysql_query($sql);
//$row = mysql_fetch_array($result);
	


$entitygroup = 'all';
if (isset($_GET['group']))
	$entitygroup= $_GET['group'];



?>
<!DOCTYPE html>
<html>
<head>
	<?php IncFunc::icon();?>
    <?php IncFunc::title();?>
    <?php IncFunc::linkStyleCSS();?> 
    <?php IncFunc::jQuery();?>   
    <?php IncFunc::jQueryDatePicker();?>
	<?php IncFunc::generalDateFunctions();?>
    <?php //IncFunc::yuiDropDownJavaScript(); ?>
    <?php IncFunc::googleGadget(); ?>
   
    <script type="text/javascript">

    	var countryid;
    	$(document).ready(function() {

        	<?php 
        	
        	if ($gdptype=="ppp") {
        		echo "$(\"#checkPPP\").attr('checked','checked');\n";
        		//echo "$('#checkPPP').checked = true;";
        	}
        	else /*if ($gdptype=="cp")*/ {
        		echo "$(\"#checkCP\").attr('checked','checked');\n";
        		//echo "$('#checkCP').checked = true;";
        	}
        	
        	if (!empty($countryid)) {
        		echo "$( \"#a_c\" ).val(\"".$countryname."\");\n";
        		echo "countryid=\"".$countryid."\";\n";
	
        	}
        	?>

        	
    		var oneDay = 24*60*60*1000;
    		var td = new Date();
    		td.setDate(td.getDate() - 1);
    		td.setMonth(td.getMonth() - 1);
    		td.setMonth(td.getMonth() - 12);
    		//var fromDay = td.setDay(td.getDay() - 1); 
    	  rangeDemoFormat = "%e-%b-%Y";
    	  rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});
    	 $("#rangeDemoToday").click( function(e) {
    	      $("#rangeDemoFinish").val(rangeDemoConv.format(new Date())).change(); } );
    	  $("#rangeDemoClear").click( function(e) {
    	      $("#rangeDemoStart").val("").change();
    	      $("#rangeDemoFinish").val("").change(); } );
    	  $("#rangeDemoStart").AnyTime_picker({format:rangeDemoFormat});
    	  $("#rangeDemoFinish").AnyTime_picker({format:rangeDemoFormat});

    	  <?php 
    			  if (!empty($begindate) && !empty($enddate)) {
    			
    			  	//echo "t1 = new Date();\n";
    			  	echo "t1 = Date.parse('".$begindate."');\n";
    			  	//echo "t1.setTime(".$begindate.");";
    			  	//echo "alert(rangeDemoConv.format(t1));";
    			  	echo "$(\"#rangeDemoStart\").
    			  		AnyTime_noPicker().\n
    					  	val(rangeDemoConv.format(t1)).\n
    					    AnyTime_picker(\n
    					              { 
    					                format: rangeDemoFormat
    					               
    					              } );\n";
    			  	//echo "t1.setTime(".$enddate.");";
    			  	echo "t1 = Date.parse('".$enddate."');\n";
    			  	 	echo "$(\"#rangeDemoFinish\").
    			  		AnyTime_noPicker().\n
    					  	val(rangeDemoConv.format(t1)).\n
    					    AnyTime_picker(\n
    					              { 
    					                format: rangeDemoFormat
    					               
    					              } );\n";
    			  	 	
    			  	
    			
    			  }
    	?>
    	});
    	$(function(){
    		$( "#a_c" ).autocomplete({
    			source: function( request, response ) {
					$.ajax({
						url: "../../site/ajax/getCountry.php",
						dataType: "json",
						data: {
							maxRows: 12,
							term: request.term
					
						},
						success: function( data ) {
							//console.log(data);
							response( $.map( data, function( item ) {
								//alert(item.id);
								return {
									
									value: item.name,
									label: item.name,
									id: item.id
			
								}

							}));
						}
					});
				},
    			minLength: 1,
    			select: function( event, ui ) {
    				if(ui.item)	{    
        				countryid = ui.item.id;
        				//alert(ui.item.id);
        				//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name);
        				/*
        				<?php
        						echo "window.location.replace(\"";
        						echo IncFunc::$PHP_ROOT_PATH;
        						echo "/charts/countries/linechart.php";
        				
        						/*
        						 * There are other javascript enocde functions: escape() and encodeURIComponent()
        						 */
        						echo "?countryid=\"+encodeURI(ui.item.id));";
        						
        				?>
        				*/
    				}
    			}
    		});
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
    			

    		/*$("#a_c").keypress(function (e,ui) { 
        		if (e.which == 13) { 
            		//yourFunction($('#yourAutoComplete').val()); return false;
            		value = $("#a_c").val();
      
					<?php
						echo "window.location.replace(\"";
						echo IncFunc::$PHP_ROOT_PATH;
						echo "/charts/allassets/linechart.php?title=";
						echo urlencode($title);
						/*
						 * There are other javascript enocde functions: escape() and encodeURIComponent()
						 */
						echo "&t=\"+encodeURI(value));";
						
					?>
					
					
					
					
        			//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name); 
            	} 
        	}); */
        });

        
    
		var count = 0;
	
        google.load('visualization', '1', {packages: ['corechart']});
        //google.load('visualization', '1', {packages: ['annotatedtimeline']});
        <?php
         if (!empty($countryid))
       	 	echo "google.setOnLoadCallback(function() { sendAndDraw(null) });\n";
       	?>
        var firstpass = true;
        var options = {};
        var query1;

        function submitForm() {
        	//value = $("#a_c").id;
        	value = countryid;

        	var checkbox;

        	
   
        	if ($("#checkPPP").attr("checked")==true) {
				if ($("#checkCP").attr("checked")==true)
					checkbox = "&gdptype=both";
				else
					checkbox = "&gdptype=ppp";
					
			}
			else {
				if ($("#checkCP").attr("checked")==true)
					checkbox = "&gdptype=cp";
				else
					checkbox = "&gdptype=none";

			}

			//datebegin = (Date.parse($("#rangeDemoStart").val())).getTime();
			datebegin = $("#rangeDemoStart").val();

			//dateend = (Date.parse($("#rangeDemoFinish").val())).getTime();
			dateend = $("#rangeDemoFinish").val();


            
			<?php
			
			echo "url=\"?title=".urlencode($title)."\";\n";
			echo "url+=\"&countryid=\"+encodeURI(value);\n";
			echo "url+=checkbox;\n";
			echo "url+=\"&begindate=\"+datebegin;\n";
			echo "url+=\"&enddate=\"+dateend;\n";

			
        	echo "window.location.replace(\"";
			echo IncFunc::$PHP_ROOT_PATH;
			echo "/charts/countries/linechart.php\"+url);\n";
			//echo urlencode($title);
			/*
			 * There are other javascript enocde functions: escape() and encodeURIComponent()
			 */
			//echo "&countryid=\"+encodeURI(value)+checkbox+\"&);";
			?>


        }

        
        function sendAndDraw(id,ticker,fullname) {

            

            /*var chart2 = document.getElementById('orgchart2');
            chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
            
        	
           
            var queryString1;

            
            var options = {};
         	//options['title'] = 'Cotton Futures';
         	/*options['height'] = 600;
         	options['width'] = 900;
         	options['fontSize'] = 15;
         	//options['curveType'] = 'function';
         	options['pointSize'] = 0;
			options.interpolateNulls = true;
			options.backgroundColor = {};
			options.backgroundColor.fill = 'white';
			options.backgroundColor.stroke = '#000';
         	options.backgroundColor.strokeWidth = 8;*/

         	options.scaleType='allmaximixed';
         	options.scaleType='maximized';
         	options.wmode='opaque';
         	options.thickness=2;
         	options.interpolateNulls = true;
         	options['curveType'] = 'function';
         	options.hAxis= {};
         	options.hAxis.slantedText = true;
         	options.hAxis.slantedTextAngle = 45;
         	options['width'] = 1000;
         	
         	<?php 
         		if ($gdptype=='ppp')
         			$gdpstring = 'PPP';
         		else if ($gdptype='cp')
         			$gdpstring = 'Current Prices';
         			
         		echo "options.title='Comparison of Cumulative GDP Growth (".$gdpstring.": Actual and Estimated) to Equity Indexes for Country: ".$countryname."';";
         	?>
         	
            




        	
        	
        	/*
        	* metric id of zero means use the default.
        	*/
			<?php 
			//echo "alert(\"".$begindate."\");\n";
			echo "var queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15multiplecountry.jsp";
			//echo "var queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15multiplecountryv2.jsp";
			echo "?begindate='+(Date.parse('".$begindate."')).getTime()+'";
			if (!empty($enddate))
				echo "&enddate='+(Date.parse('".$enddate."')).getTime()+'";
			echo "&entityid=".$entities;
			echo "&countryid=".$countryid;
			echo "&taskid=".$tasks;		
			//echo "&gdptype=.".$gdptype;
			echo "&metricid=".$metrics."';";
	
			?>

	
		
				
			<?php 
			if (!empty($countryid)) {
			?>

	        	title = document.getElementById('chart-title');
				if(id != undefined){
					//queryPath += '&entityid=' + escape(id); 
	
			
					title.innerHTML = ticker + " - " + fullname;
					//options.title = ticker + " - " + fullname;
	
					
				}
				else
				{
					//queryPath += <?php //echo "'&entityid=".$entityid."';"; ?>
					
					
					<?php 
						while ($row = mysql_fetch_array($result)) {
							
							echo "title.innerHTML+='".$row['ticker']." - ".str_replace("'","&#39;",$row['full_name'])."<BR>';";
							
						}
	
						
					?>
				}
			<?php 
			}
			?>

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            //var lineChart1 = new google.visualization.AnnotatedTimeLine(container1);
            var lineChart1 = new google.visualization.LineChart(container1);
        
            
         	//alert(queryPath);
            if (window.console) {console.log(queryPath)}

            
            query1 && query1.abort();
            query1 = new google.visualization.Query(queryPath);
            query1.setTimeout(120);
            var queryWrapper1 = new QueryWrapper(query1, lineChart1, options, container1);
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
<div id="jq-siteContain">

<?php 
	IncFunc::header2("charts"); 
	IncFunc::apycomDropDownMenu();

?>
<BR>

<?php 
	if (!empty($error)) {
		echo "<BR>";
		echo "<div id=\"error\" style=\"font-size: medium\">";
		echo "Error: ".$error;
		echo "</div>";
	}
	else {
?>
<div id="chartTitle" style="margin: 20px 0 0 0;font-size: medium;font-weight:bold;"><u><?php echo $title; ?></u></div>
<div id="pf-body">
<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 50px 0 20px 0;font-size: medium;font-weight:bold;"><?php echo strtoupper('LINE CHART: GDP VS. EQUITY INDEXES BY COUNTRY'); ?></div>

    <div id="pf-form">
    Enter country name:
    <BR>
  	<input type='text' id='a_c' style='z-index:3' /><br/>
  	<BR>
	<input type="radio" name="vehicle" value="checkCP" id="checkCP" />&nbsp;Current Prices&nbsp;&nbsp;&nbsp;<a target='_blank' href="http://en.wikipedia.org/wiki/Gross_domestic_product#Cross-border_comparison">Difference between Purchasing Power Parity and Current Prices</a>
	<BR>
  	<input type="radio" name="vehicle" value="checkPPP" id="checkPPP"/>&nbsp;Purchasing Power Parity
   	<BR>
   	Time Frame:&nbsp;&nbsp;
Start: <input type="text" id="rangeDemoStart" size="11" />
&nbsp;Finish: <input type="text" id="rangeDemoFinish" size="11" />
<!-- <input type="button" id="rangeDemoToday" value="today" /> -->
<input type="button" id="rangeDemoClear" value="clear" />
<BR>
   	<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Chart"	onclick="submitForm();return false;"> <br />
   	<BR>
<BR>
	<div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div>
	</div> <!--  pf-form -->
	
	

	
	<div style="font-size:20" id="chart-description"></div>
    <div id="chart-div" style="margin: 30px 0 20px 0;width:800px;height:600px"></div>

 
    
</div> <!--  pf-body -->
</div> <!--  siteContain -->  
<?php 
	}
?>
</body>
</html>
