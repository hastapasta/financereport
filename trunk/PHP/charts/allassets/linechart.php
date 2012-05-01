<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$alertid="";

if (isset($_GET['a']))
	$alertid = $_GET['a'];
elseif (isset ($_GET['e']))
	$entityid = $_GET['e'];
elseif (isset ($_GET['t']))
	$ticker = $_GET['t'];
elseif (isset($_GET['eg']))
	$eg = $_GET['eg'];
else
	$error =  "No alert id,entity id,ticker or entity group parameter in url. Unable to render chart.";
	//die("No alert id,entity id  or ticker parameter in url. Unable to render chart.");
	
$gran="day";
if (isset($_GET['gran']))
	$gran= $_GET['gran'];
	
$metricid="0";
if (isset($_GET['m']))
	$metricid = $_GET['m'];
	
/*
 * OFP 4/30/2012 - This is a little confusing. "eg" is the entitygroup for 
 * the entities to use in the chart. "group" is the entitygroup to use to populate
 * the ajax auto complete control.
 * 
 */
	

	
$percent=false;
if (isset($_GET['percent']))
	if (strcasecmp('true', $_GET['percent'])==0)
		$percent=true;
	
if (!empty($alertid)) {
	$sql1 = "select entity_id from alerts where id=".$alertid;
	$result1 = mysql_query($sql1);
	$row1 = mysql_fetch_array($result1);
	
	
	$entityid = $row1['entity_id'];
}


if (!empty($eg)) {
	
	$sql2 = "select entities.id from entities ";
	$sql2.=" join entities_entity_groups on entities.id=entities_entity_groups.entity_id ";
	$sql2.=" where entities_entity_groups.entity_group_id=".$eg;
	
	
	$result2 = mysql_query($sql2);
	$count = 0;
	while ($row1 = mysql_fetch_array($result2)) {
		$entityid.=$row1['id'].",";	
		$count++;
	}
	
	if ($count==0)
		$error = "Entity group ".$eg." has no members.";
	$entityid=substr($entityid,0,strlen($entityid)-1);
	
	
	
}

if (!empty($ticker)) {
	$tickerarray = split(",",$ticker);
	$sql1 = "select id from entities where ticker in (";
	foreach ($tickerarray as $tmp) {
		$sql1.="'".$tmp."',";
	}
	$sql1 = substr($sql1,0,strlen($sql1)-1);
	$sql1.=")";
	
	$result1 = mysql_query($sql1);
	$count=0;
	while ($row1 = mysql_fetch_array($result1)) {
		$entityid.=$row1['id'].",";	
		$count++;
	}
	if ($count==0)
		$error = "Ticker(s) ".$ticker." not found.";
	$entityid=substr($entityid,0,strlen($entityid)-1);
}

if (empty($begindate)) {
	$begindate="2011-01-01";
	if ($gran=='minute') {		
		$date = date('Y-m-d');
		//$date = "2012-03-01";
		$newdate = strtotime ( '-1 month' , strtotime ( $date ) ) ;
		$begindate = date ( 'Y-m-d' , $newdate );
		
	}
	
	
	
	
}

$enddate="";

if (isset($_GET['begindate']) && !empty($_GET['begindate']))
	$begindate=$_GET['begindate'];
	
if (isset($_GET['enddate']) && !empty($_GET['enddate']))
	$enddate=$_GET['enddate'];
	
	
	
	

$type="1";
if (isset($_GET['type']))
	$type=urldecode($_GET['type']);
	
if ($type==1) {
	$title = "Line Chart - All Assets";
	$inputprompt = "entity name (stock ticker, equity index, currency cross, etc)";
}
else if ($type==2) {
	$title = "Line Chart - Foreign Exchange";
	$inputprompt = "currency cross";
}
else if ($type==3) {
	$title = "Line Chart - Commodities";
	$inputprompt = "commodity";
}
else if ($type==4) {
	$title = "Line Chart - Equities";
	$inputprompt = "stock ticker";
}
else if ($type==5) {
	$title = "Line Chart - Bond Yields";
	$inputprompt = "bond";
}
	

$entityid = str_replace(' ',',',$entityid);
$entities = split(",",$entityid);


	
$sql = "select * from entities where id in (".$entityid.")";
$result = mysql_query($sql);
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
    <?php //IncFunc::yuiDropDownJavaScript(); ?>
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

-->
</style>
   
    <script type="text/javascript">
    	$(document).ready(function() {
        	$("#checkbox1").attr("checked", <?php echo ($percent == true ? 'true' : 'false');?>); 
    	});
    	$(function(){
    		$( "#a_c" ).autocomplete({
    			source: function( request, response ) {
					$.ajax({
						url: "../../site/includes/getTicker.php",
						dataType: "json",
						data: {
							maxRows: 12,
							term: request.term
							<?php echo ", group: '".$entitygroup."'\n"; ?>
					
						},
						success: function( data ) {
							//console.log(data);
							response( $.map( data, function( item ) {
								//alert(item.id);
								return {
									
									value: item.ticker,
									label: item.ticker,
									id: item.id,
									full_name: item.full_name
			
								}

							}));
						}
					});
				},
    			minLength: 1,
    			select: function( event, ui ) {
    				if(ui.item)	{    
        				
        				redraw(ui.item.id);
        				<?php
        						//echo "window.location.replace(\"";
        						//echo IncFunc::$PHP_ROOT_PATH;
        						//echo "/charts/allassets/linechart.php?type=";
        						//echo $type;
        						/*
        						 * There are other javascript enocde functions: escape() and encodeURIComponent()
        						 */
        						//echo "&e=\"+encodeURI(ui.item.id)+\"&percent=\"+encodeURI($(\"#checkbox1\").is(':checked')));";
        						
        				?>
    				}
    			}
    		});
    		$("#a_c").keypress(function (e,ui) { 
        		if (e.which == 13) { 
            		//yourFunction($('#yourAutoComplete').val()); return false;
            		value = $("#a_c").val();
            		redraw(value);
      
					<?php
						//echo "window.location.replace(\"";
						//echo IncFunc::$PHP_ROOT_PATH;
						//echo "/charts/allassets/linechart.php?title=";
						//echo urlencode($title);
						/*
						 * There are other javascript enocde functions: escape() and encodeURIComponent()
						 */
						//echo "&t=\"+encodeURI(value));";
						
					?>
		
        			//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name); 
            	} 
        	}); 
        	$('#checkbox1').click(function () {
				//alert('clicked');
				//alert($("#checkbox1").is(':checked'));
				<?php echo "redraw('".$entityid."');\n"; ?>
        	});
        });

        function redraw(value) {
            //alert('in redraw');
        	<?php
				echo "window.location.replace(\"";
				echo IncFunc::$PHP_ROOT_PATH;
				echo "/charts/allassets/linechart.php?&gran=".$gran;
				//echo urlencode($title);
				/*
				 * There are other javascript enocde functions: escape() and encodeURIComponent()
				 */
				//echo "&e=\"+encodeURI(value));";
				echo "&e=\"+encodeURI(value)+\"&percent=\"+encodeURI($(\"#checkbox1\").is(':checked')));";
					
			?>


        }

        
    
		var count = 0;
	
        //google.load('visualization', '1', {packages: ['linechart']});
        google.load('visualization', '1', {packages: ['annotatedtimeline']});
        google.setOnLoadCallback(function() { sendAndDraw(null) });
        var firstpass = true;
        var options = {};
        var query1;

        
        function sendAndDraw(id,ticker,fullname) {
            

            /*var chart2 = document.getElementById('orgchart2');
            chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
            
        	
           
            var queryString1;

            
            var options = {};

         	options.scaleType='allmaximixed';
         	options.scaleType='maximized';
         	options.wmode='opaque';
         	options.thickness=2;
         	options.legendPosition='newRow';

        	/*
        	* metric id of zero means use the default.
        	*/
			<?php 
			if (sizeof($entities) > 1) {
			//if ($percent == true) {
				echo "var queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15multiple.jsp?begindate=".$begindate;
				if (!empty($enddate))
					echo "&enddate=".$enddate;
				/* OFP 4/29/2012 - Right now multiple entities doesn't support anything other
				 * than the default metric id.
				 */
				echo "&alertid=0&metricid=0";
				
			}
			else {
				echo "var queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15single.jsp?begindate=".$begindate;
				if (!empty($enddate))
					echo "&enddate=".$enddate;
				echo "&alertid=0&metricid=".$metricid;
			}
			
			if ($percent==true)
				echo "&percent=true";
			else
				echo "&percent=false";
				
			if ($gran=="minute")
				echo "&gran=minute";
			else 
				echo "&gran=day";
				
			echo "';\n";
			
			if ($percent==true)
				echo "options.allValuesSuffix='%';\n";
			
			?>
        	title = document.getElementById('chart-title');
			if(id != undefined){
				queryPath += '&entityid=' + escape(id); 

		
				title.innerHTML = ticker + " - " + fullname;
				//options.title = ticker + " - " + fullname;

				
			}
			else
			{
				queryPath += <?php echo "'&entityid=".$entityid."';"; ?>
				
				
				<?php 
					while ($row = mysql_fetch_array($result)) {
						$fullname = str_replace("'","&#39;",$row['full_name']);
						$fullname = str_replace("€","&#128;",$fullname);
						echo "title.innerHTML+='".$row['ticker']." - ".$fullname."<BR>';";
						
					}

					
				?>
			}

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            var lineChart1 = new google.visualization.AnnotatedTimeLine(container1);
            //var tableChart2 = new google.visualization.Table(container2);
            
         	//alert(queryPath);
            if (window.console && window.console.firebug) {console.log(queryPath)}

            
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
<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 20px 0 0 0;font-size: medium;font-weight:bold;"><?php echo strtoupper($title); ?></div>



    <div id="pf-form">   
    <BR><BR>
    Enter <?php echo $inputprompt;?>:
    <BR>
  	<input type='text' id='a_c' style='z-index:3;clear:both;float:left;' />
  	<div style='z-index:3;margin-bottom: 10px;float:left;clear:both;margin-top: 10px;' >
  	Y axis values in terms of % change: 
  	<input type='checkbox' id='checkbox1' />
  	</div>
  	<div style="font-size:30;margin: 10px 0 20px 0;float:left;clear:both;" id="chart-title"></div>
	</div>
	
	<div style="font-size:20;clear:both;" id="charttabs">
	
	
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
	<ul id="tabs">
	<li id="tab1"><a href=<?php echo "\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?".$serverquery."\""?>>Day Granularity</a></li>
	<li id="tab2"><a href=<?php echo "\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?".$serverquery."&gran=minute\""?>>Quarter Hour Granularity</a></li>
	</ul>
	</div>
    <div id="chart-div" style="margin: 30px 0 20px 0;width:800px;height:600px;float:left;clear: both;"></div>

 
</div> <!--  pf-body -->

</div> <!--  siteContain -->  
<?php 
	}
?>
</body>
</html>
