<?php

/*
 * To Do: I want to change the code to pass around the entity_ids instead of ticker strings.
 * 
 * 
 */

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");


db_utility::db_connect();

date_default_timezone_set('America/Phoenix');

/*
 * Initialize page default values
 */


$alertid="";
$percent=false;
//$begindate = "";
//$enddate = "";
$gran="day";
$metricid="0";
$noflash = false;
$showprops = true;
$type = 1;


/*
 * Initialize URL values
 */


if (isset($_GET['gran']))
	$gran= $_GET['gran'];


if (isset($_GET['a']))
	$alertid = $_GET['a'];
elseif (isset ($_GET['e']))
	$entityid = $_GET['e'];
elseif (isset ($_GET['t']))
	$ticker = $_GET['t'];
elseif (isset($_GET['eg']))
	$eg = $_GET['eg'];

if (empty($ticker)) {
	
	$sql2 = "select distinct ticker from entities ";
	if (!empty($alertid)) {
		$sql2.=" join alerts on alerts.entity_id=entities.id ";
		$sql2.=" where alerts.id=".$alertid;
		$tmperror = "No alerts were found.";
	}
	elseif (!empty($entityid)) {
		$sql2.=" where entities.id in (".$entityid.") ";
		$tmperror = "No entity ids were found.";
	}
	elseif (!empty($eg)) {
		$sql2.=" join entities_entity_groups on entities.id=entities_entity_groups.entity_id ";
		$sql2.=" where entities_entity_groups.entity_group_id=".$eg;
		$tmperror = "Entity group contains no members or doesn't exist.";
	}
	
	$result2 = mysql_query($sql2);
	$count=0;
	while ($row1 = mysql_fetch_array($result2)) {
		$ticker.=$row1['ticker'].",";
		$count++;
	}
	
	if ($count==0) {
		$error = $tmperror;
	}
		
	$ticker=substr($ticker,0,strlen($ticker)-1);
	
	
}

if (isset($_GET['m']))
	$metricid = $_GET['m'];

if (isset($_GET['type']))
	$type=urldecode($_GET['type']);

/*
 * read a parameter that will force the code to not use flash components.
*/

if (isset($_GET['noflash']))
	if (strcasecmp('true', $_GET['noflash'])==0)
		$noflash=true;

if (isset($_GET['showprops']))
	if (strcasecmp('false', $_GET['showprops'])==0)
		$showprops=false;

if (isset($_GET['percent']))
	if (strcasecmp('true', $_GET['percent'])==0)
		$percent=true;

if (isset($_GET['begindate']) && !empty($_GET['begindate']))
	$urlbegindate=$_GET['begindate'];

if (isset($_GET['enddate']) && !empty($_GET['enddate']))
	$urlenddate=$_GET['enddate'];




/*
 * Initialize date values. 
 * 
 * This goes here at the end since the logic of the date values (including when
 * no dates are passed) depends on previously initialized values.
 */

if (!empty($urlenddate))
	$enddate = $urlenddate;
else {
	/*Check which environment we are in because test/dev may not have the latest data.*/
	if (strpos(php_uname('n'),'prod') !== false) {
		$enddate = date('Y-m-d H:i:s');
	}
	else {
		$enddate = date('Y-m-d',strtotime('2012-03-01'));
	}
}

//echo "<br>enddate 1:".$enddate;

if (!empty($urlbegindate)) {
	$begindate = $urlbegindate;
	//echo "urlbegindate";
}
else {
	/*
	 * Only show one month's worth of data by default if granularity is minute;
	 */
	//echo "<br>pagedefault begindate";
	if ($gran == 'minute') {
		$newdate = strtotime ( '-1 month' , strtotime ( $enddate ) ) ;
		$begindate = date ( 'Y-m-d H:i:s' , $newdate );
	}
	else {
		$newdate = strtotime ( '-3 month' , strtotime ( $enddate ) ) ;
		$begindate = date('Y-m-d H:i:s', $newdate);
	}
}

/*
 * Convert y-m-d to seconds.
 * 
 * !!Note: javascript uses milliseconds and php uses seconds.
 */

if (strpos($begindate,"-") == true)
	$begindate = strtotime($begindate) * 1000;

if (strpos($enddate,"-") == true)
	$enddate = strtotime($enddate) * 1000;


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
	

	<?php IncFunc::dyGraphs();?>
	<?php IncFunc::checkFlash();?>
	<?php IncFunc::icon();?>
    <?php IncFunc::title();?>
    <?php IncFunc::linkStyleCSS();?> 
    <?php IncFunc::jQuery();?>   
    <?php IncFunc::jQueryDatePicker();?>
    <?php IncFunc::generalDateFunctions();?>
    <?php IncFunc::generateUrlCode(); ?>
    <?php IncFunc::googleGadget(); ?>
    <style type="text/css">
<!--
#tabs {
	border-bottom: .5em solid #0F1923;
	margin: 0;
	padding: 0;
	font-size: 1.5em;
}
#tabs li { 
	display:inline; 
	border-top: .1em solid #000000;
	border-left: .1em solid #000000;
	border-right: .1em solid #000000;
}
#tabs li a {
	text-decoration: none;
	padding: 0.25em 1em;
	color: #fff;
}
#page1 #tabs li#tab1 a, #page2 #tabs li#tab2 a, #page3 #tabs li#tab3 a, .page4 li#tab4 a {
	padding: 0.25em 1em;
	background-color: #0F1923;
	color: #fff;
}
#AnyTime--rangeDemoStart {
	z-index:30;
}
#AnyTime--rangeDemoFinish {
	z-index:30;
}

.dygraph-legend{
	background-color: black !important;
	left:500px !important;
	width:294Px !important;
}
.ui-autocomplete {
		max-height: 200px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
		/* add padding to account for vertical scrollbar */
		padding-right: 20px;
	}
	/* IE 6 doesn't support max-height
	 * we use height instead, but this forces the menu to always be this tall
	 */
	* html .ui-autocomplete {
		height: 100px;
	}


-->
</style>
   
    <script type="text/javascript">
		
    
	    function split( val ) {
			return val.split( /,\s*/ );
		}
		function extractLast( term ) {
			return split( term ).pop();
		}
    	$(document).ready(function() {
    		<?php 
    		if ($noflash == true) 
    			echo "document.bFlash = false;\n";
    		else
    			echo "document.bFlash = isFlashEnabled();\n";

    		?>
    		document.firstpass = true;

    		<?php
    	    	echo "document.type=".$type.";\n";
    	    ?>

    	    document.entityGroup="all";
    	    if (document.type==1) {
    	    	$('#chartTitle').html("Line Chart - All Assets");
    	    	$('#inputPrompt').html("comma separated list of entity names (stock ticker, equity index, currency cross, etc)");
    	    }
    	    else if (document.type==2) {
    	    	$('#chartTitle').html("Line Chart - Foreign Exchange");
    	    	$('#inputPrompt').html("comma separated list of currency crosses");
    	    	document.entityGroup = 3;
    	    }
    	    else if (document.type==3) {
    	    	$('#chartTitle').html("Line Chart - Commodities");
    	    	$('#inputPrompt').html("comma separated list of commodities");
    	    	document.entityGroup = 4;
    	    }
    	   	else if (document.type==4) {
    	    	$('#chartTitle').html("Line Chart - Equities");
    	    	$('#inputPrompt').html("comma separated list of stock tickers");
    	    	document.entityGroup = 1;
    	    }
    	    else if (document.type==5) {
    	    	$('#chartTitle').html("Line Chart - Bond Yields");
    	    	$('#inputPrompt').html("comma separated list of bonds");
    	    	document.entityGroup = 101023;
    	    }
    	    else if (document.type==6) {
    	    	$('#chartTitle').html("Line Chart - Credit Default Swaps");
    	    	$('#inputPrompt').html("comma separated list of credit default swaps");
    	    	document.entityGroup = 101024;
    	    }
        	$("#jquerytabs").tabs({
            	//select is fired too early, need to use show.
            	
            	show: function(event,ui) {
            		if (document.firstpass == false)
						sendAndDraw();

            	}
        	});
        	
        	$("#checkbox1").attr("checked", <?php echo ($percent == true ? 'true' : 'false');?>);
        	rangeDemoFormat = "%e-%b-%Y %H:%i:%s";
        	rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});
        	 $("#rangeDemoClear").click( function(e) {
        	      $("#rangeDemoStart").val("").change();
        	      $("#rangeDemoFinish").val("").change(); } );
        	  $("#rangeDemoStart").AnyTime_picker({format:rangeDemoFormat,placement: "popup"});
        	  $("#rangeDemoFinish").AnyTime_picker({format:rangeDemoFormat,placement: "popup"});
        	  $("#a_c").val('<?php echo $ticker; ?>');
        	  t1 = new Date();
        	  t1.setTime("<?php echo $begindate;?>");
        	  $("#rangeDemoStart").
        	  	AnyTime_noPicker().
        	  	val(rangeDemoConv.format(t1)).
				        	AnyTime_picker(
				        	{
				        		format: rangeDemoFormat
				        	
				        	} );
	        	t2 = new Date();
				t2.setTime("<?php echo $enddate; ?>");
				        
			    $("#rangeDemoFinish").
			    	AnyTime_noPicker().
			    	val(rangeDemoConv.format(t2)).
			    	AnyTime_picker(
			       {
			       	format: rangeDemoFormat
				        	
					} );
		      <?php  	
		      /* If $ticker is set then url value (either e,t,eg or a) was passed in. */
		      //echo "//ticker: ".$ticker."\n";
				if (!empty($ticker)) {
					echo "google.setOnLoadCallback(function() { sendAndDraw(null) });\n";
				}
				        	
				?>
        	  	
				        	
				        	
                	
                	
			document.firstpass = false;
        	
    	});
    	$(function(){
    		$( "#a_c" )
    			.bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
				})
    			.autocomplete({
    			source: function( request, response ) {
					$.ajax({
						url: "../../site/ajax/getTicker.php",
						dataType: "json",
						data: {
							/*
							These are url parameters.
							*/
							maxRows: 12,
							term: extractLast(request.term),
							group: document.entityGroup
					
						},
						success: function( data ) {
							//console.log(data);
							response( $.map( data, function( item ) {

								return {
									
									value: item.ticker,
									label: item.ticker,
									id: item.id,
									full_name: item.full_name
			
								};
							

							}));
						}
					});
				},
    			minLength: 1,
    			/*select: function( event, ui ) {
    				if(ui.item)	{    
        				
        				//redraw(ui.item.id);
        				window.id = ui.item.id;
        				<?php
        						//echo "window.location.replace(\"";
        						//echo IncFunc::$PHP_ROOT_PATH;
        						//echo "/charts/allassets/linechart.php?type=";
        						//echo $type;
        						//*
        						// * There are other javascript enocde functions: escape() and encodeURIComponent()
        						 //*
        						//echo "&e=\"+encodeURI(ui.item.id)+\"&percent=\"+encodeURI($(\"#checkbox1\").is(':checked')));";
        						
        				?>
    				}
    			}*/
    			focus: function() {
					// prevent value inserted on focus
					return false;
				},
    			select: function( event, ui ) {
        			var q
					var terms = split( this.value );
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push( ui.item.value );
					// add placeholder to get the comma-and-space at the end
					terms.push( "" );
					this.value = terms.join( ", " );
					return false;
				}
    		});
    		$("#a_c").keypress(function (e,ui) { 
        		if (e.which == 13) { 
            		//yourFunction($('#yourAutoComplete').val()); return false;
            		value = $("#a_c").val();
            		window.id = ui.item.id;
      
            	} 
        	}); 
        	$('#refreshChart').click(function () {

				sendAndDraw();
        	});

        	$( "#accordion" ).accordion({
    			collapsible: true,
    			//active: true,
    			autoHeight: true
    		});

    		<?php 
    			if ($showprops == false)
    				echo "$('#accordion').accordion('activate',false);";
    		
    		?>

    		
        	
        	//$('.ui-accordion .ui-accordion-content').css("overflow:hidden !important;");
    		
        });


    	function generateURL() {
    
    		var newquery = "";


    		var cleantickers = $('#a_c').val().trim();

       		if (cleantickers.charAt(cleantickers.length - 1) == ',')
                cleantickers = cleantickers.substr(0,cleantickers.length-1);

    		newquery += "t=" + encodeURIComponent(cleantickers);

    		if ($('#checkbox1').prop('checked')) {
				newquery += '&percent=true';
			}
			else {
				newquery += '&percent=false';
			}

    		if ($('#jquerytabs').tabs('option','selected')==0) {
				newquery += '&gran=day';
			}
			else {
				newquery += '&gran=minute';
			}

			if (document.type) {
				newquery += '&type=' + document.type;
			}

			if ($('#accordion').accordion('option','active')===false)
				newquery += '&showprops=false';

    		newquery += '&begindate=' + (Date.parse($('#rangeDemoStart').val())).getTime();
			newquery += '&enddate=' + (Date.parse($('#rangeDemoFinish').val())).getTime();


    		var url = location.href;
    		var url_parts = url.split('?');
    		var main_url = url_parts[0]; 		
    		//alert(main_url + '?' + newquery);
    		return(main_url + '?' + newquery);
    	}

    
		//var count = 0;

        google.load('visualization', '1', {packages: ['annotatedtimeline']});
    
        var options = {};
        var query1;

        
        function sendAndDraw() {

            var queryString1;

            
            var options = {};

         	options.scaleType='allmaximixed';
         	options.scaleType='maximized';
         	options.wmode='opaque';
         	options.thickness=2;
         	options.legendPosition='newRow';


            var queryPath = '';


            var tickers_no_spaces = $('#a_c').val().replace(/\s/g, "");

            if (tickers_no_spaces.charAt(tickers_no_spaces.length - 1) == ',')
                tickers_no_spaces = tickers_no_spaces.substr(0,tickers_no_spaces.length-1);

            if ((tickers_no_spaces.split(',')).length > 1) {
                <?php echo "queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15multiple.jsp';";?>
            }
            else {
            	<?php echo "queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15single.jsp';"; ?>
            }

        	/*
        	* metric id of zero means use the default.
        	*/

			queryPath += '?metricid=0';

			if ($('#checkbox1').prop('checked')) {
				queryPath += '&percent=true';
				options.allValuesSuffix='%';
			}
			else {
				queryPath += '&percent=false';
				options.allValuesSuffix='';
			}


			if ($('#jquerytabs').tabs('option','selected')==0) {
				queryPath += '&gran=day';
			}
			else {
				queryPath += '&gran=minute';
			}

			queryPath += '&begindate=' + (Date.parse($('#rangeDemoStart').val())).getTime();
			queryPath += '&enddate=' + (Date.parse($('#rangeDemoFinish').val())).getTime();
			
        	title = document.getElementById('chart-title');

        	var cleantickers = $('#a_c').val().trim();

        	 if (cleantickers.charAt(cleantickers.length - 1) == ',')
                 cleantickers = cleantickers.substr(0,cleantickers.length-1);
        	
        	

        	queryPath += '&tickers=' + encodeURIComponent(cleantickers);

        	var url = "../../site/ajax/getEntityId.php?tickers=" + encodeURIComponent(cleantickers);
        	//alert(url);
        	$('#chart-legend').empty();
            $.ajax({
				url: url,
				dataType: "json",
				success: function(data) {
					$.map( data, function( item ) {
	
					var tmptext = item.ticker;
					if (item.full_name) {
						tmptext += " - " + item.full_name;
					}
					$('<li>').text(tmptext).appendTo('#chart-legend');
				

				});
					
				}

            });


			$('.charts').each(function(index,item) {
				//item.css('z-index',0);
				$(item).css('z-index',0);
				$(item).empty();
			});

			

			var chartelement = $('#chart-div' + ($('#jquerytabs').tabs('option','selected')+1));
			
			chartelement.css('z-index',100);		
            chartelement.html("<img src=\"../../site/images/spinner4-bluey.gif\" />");

            //var container1 = document.getElementById('chart-div1');
      

            var lineChart1 = new google.visualization.AnnotatedTimeLine(chartelement[0]);
     
            
         	//alert(queryPath);
            if (window.console) {
                console.log(queryPath);
            }

            
            query1 && query1.abort();


            query1 = new google.visualization.Query(queryPath);
            query1.setTimeout(120);

          
            if (document.bFlash == true) {

            	var queryWrapper1 = new QueryWrapper(query1, lineChart1, options, chartelement[0]);
 
                queryWrapper1.sendAndDraw();
    			
    		} else {

    			//var charta = document.getElementById('chart-div');
    			var dygraphWrapper = new DyGraphWrapper(query1, chartelement[0]);
				dygraphWrapper.sendAndDraw();
    			
        		//query1.send(handleQueryDygraph);
        		chartelement.css("width", "800px").css("height", "500px");

    			
    		}

          }

        
       
    </script>
<?php IncFunc::googleAnalytics();?>
</head>

<?php 

/*
 * I don't like controlling the tabs using the body tag. Should control it near the tabs
 * themselves.
 */

if ($gran=='minute')
	echo "<body id=\"page2\">";
else
	echo "<body id=\"page1\">";


?>

<div id="jq-siteContain">

<?php 
	IncFunc::header2("charts"); 
	IncFunc::yuiDropDownMenu();

?>
<div id="pf-body">
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
<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 20px 0 0 0;font-size: large;font-weight:bold;"><?php echo strtoupper($title); ?></div>



    <div id="style" style='z-index:3;margin-bottom: 10px;float:left;clear:both;margin-top: 10px;' >
  	<div id="accordion">
  	<h3><a href="#" style='font-size:1.3em;'>Chart Properties</a></h3>
  	<div> <!-- First Content -->
  	<div id="pf-form">   
    Enter <span id="inputPrompt"></span>:
    <BR>
  	<input type='text' id='a_c' style='z-index:3;float:left;' size="70" />
  	<div id="style2" style='z-index:3;margin-bottom: 10px;float:left;clear:both;margin-top: 10px;' >
  	Y axis values in terms of % change: 
  	<input type='checkbox' id='checkbox1' />
  	<BR><BR>
	Time Frame:&nbsp;&nbsp;
	Start: <input type="text" id="rangeDemoStart" style='z-index:-10' size="23" />
	&nbsp;Finish: <input type="text" id="rangeDemoFinish" size="23" />
	<!-- <input type="button" id="rangeDemoToday" value="today" /> -->
	<input type="button" id="rangeDemoClear" value="clear" />
	</div> <!--  style 2 -->
	<div style="float:left;clear:both">
	<input type="button" id="refreshChart" value="Refresh Chart" />
	</div>
	
	</div> <!-- pf-form -->
	</div> <!-- First Content -->
	</div> <!-- accordion -->
	</div> <!--  style -->
	
  	
  	<div id="chart-elements" style="border-width: 1px;border-style: solid;font-size:1.4em;margin: 10px 0 20px 0;padding: 2px 2px 2px 2px;float:left;clear:both;" >

  	
  	<div style="font-weight: bold;">Chart Items:
  	<ul id="chart-legend" style="list-style:none;">
  	</ul>
  	</div>
  	<div id="chart-title"></div>
  	</div>
	
	
	
	<?php 
		$serverquery = $_SERVER['QUERY_STRING'];
		
		parse_str($serverquery, $params);
		/*
		 * $params = array(
		 *     'mode'    => '1'
		 *     'newUser' => '1'
		 * )
		 */
		unset($params['gran']);
		$serverquery = http_build_query($params);
		
	?>

    <!-- <div id="chart-div-old" style="margin: 30px 0 20px 0;width:800px;height:600px;float:left;clear: both;"></div> -->

    <!-- <div id="chart-div2" style="margin: 30px 0 20px 0;width:800px;height:600px;float:left;clear: both;"></div> -->

	<div id="jquerytabs" style="font-size:20;clear:both;">
		<ul>
			<li><a href="#tabs-1">Day Granularity</a></li>
			<li><a href="#tabs-2">Quarter Hour Granularity</a></li>
		</ul>
		<div id="tabs-1">
			 <div id="chart-div1" class="charts" style="margin: 30px 0 20px 0;z-index:100;width:800px;height:600px;"></div>
		</div>
		<div id="tabs-2">
			<div id="chart-div2" class="charts" style="margin: 30px 0 20px 0;width:800px;height:600px;"></div>
		</div>
	
	
	</div>
	
	<div style="font-size: 1.5em;margin-bottom: 10px;margin-top:10px;">
		<input type="button"  id="generateurl" value="Generate URL"	> <br />
	</div>

 
</div> <!--  pf-body -->

</div> <!--  siteContain -->  
<?php 
	}
?>

<style>
#debug * {
    margin: 0;
    padding: 0;
    border: 0;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}
</style>

</body> 
<div id="debug">
<?php 
//print_r($GLOBALS);


?>
</div>
</html>
