<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$alertid="";

if (isset($_GET['a']))
	$alertid = $_GET['a'];
elseif (isset ($_GET['e']))
	$entityid = $_GET['e'];
elseif (isset ($_GET['t']))
	$ticker = $_GET['t'];
else
	$error =  "No alert id,entity id  or ticker parameter in url. Unable to render chart.";
	//die("No alert id,entity id  or ticker parameter in url. Unable to render chart.");
	
$metricid="0";
if (isset($_GET['m']))
	$metricid = $_GET['m'];
	
if (!empty($alertid))
{
	$sql1 = "select entity_id from alerts where id=".$alertid;
	$result1 = mysql_query($sql1);
	$row1 = mysql_fetch_array($result1);
	
	
	$entityid = $row1['entity_id'];
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

/*
 * Want to do date arithmetic here. Set begin date to 3 months back from current date by default.
 */
$date = date('Y-m-j');
$newdate = strtotime ( '-3 month' , strtotime ( $date ) ) ;
$begindate = date ( 'Y-m-j' , $newdate );

//$begindate="2011-01-01";
$enddate="";

if (isset($_GET['begindate']) && !empty($_GET['begindate']))
	$begindate=$_GET['begindate'];
	
if (isset($_GET['enddate']) && !empty($_GET['enddate']))
	$enddate=$_GET['enddate'];
	
	
	
	
$type="1";
if (isset($_GET['type']))
	$type=urldecode($_GET['type']);
	
if ($type==1)
	$title = "Line Chart - All Assets";
else if ($type==2)
	$title = "Line Chart - Foreign Exchange";
else if ($type==3)
	$title = "Line Chart - Commodities";
else if ($type==4)
	$title = "Line Chart - Equities";
	

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
        				
        				//alert(ui.item.id);
        				//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name);
        				<?php
        						echo "window.location.replace(\"";
        						echo IncFunc::$PHP_ROOT_PATH;
        						echo "/charts/allassets/linechart15min.php?type=";
        						echo $type;
        						/*
        						 * There are other javascript enocde functions: escape() and encodeURIComponent()
        						 */
        						echo "&e=\"+encodeURI(ui.item.id));";
        						
        				?>
    				}
    			}
    		});
    		$("#a_c").keypress(function (e,ui) { 
        		if (e.which == 13) { 
            		//yourFunction($('#yourAutoComplete').val()); return false;
            		value = $("#a_c").val();
      
					<?php
						echo "window.location.replace(\"";
						echo IncFunc::$PHP_ROOT_PATH;
						echo "/charts/allassets/linechart15min.php?title=";
						echo urlencode($title);
						/*
						 * There are other javascript enocde functions: escape() and encodeURIComponent()
						 */
						echo "&t=\"+encodeURI(value));";
						
					?>
					
					
					
					
        			//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name); 
            	} 
        	}); 
        });

        
    
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
         	options.legendPosition='newRow';
         	options.dateFormat='HH:mm:ss MMMM dd, yyyy';
         	
            




        	
        	
        	/*
        	* metric id of zero means use the default.
        	*/
			<?php 
			if (sizeof($entities) > 1) {
				echo "var queryPath = '".Environment::getJSPPath(true)."mysqldatasource15multiple.jsp?begindate=".$begindate;
				if (!empty($enddate))
					echo "&enddate=".$enddate;
				echo "&alertid=0&metricid=0';\n";
			}
			else {
				echo "var queryPath = '".Environment::getJSPPath(true)."mysqldatasource15.jsp?granularity=minute&begindate=".$begindate;
				if (!empty($enddate))
					echo "&enddate=".$enddate;
					echo "&alertid=0&metricid=".$metricid."';\n";
			}
			
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
						
						echo "title.innerHTML+='".$row['ticker']." - ".str_replace("'","&#39;",$row['full_name'])."<BR>';";
						
					}

					
				?>
			}

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            var lineChart1 = new google.visualization.AnnotatedTimeLine(container1);
            //var tableChart2 = new google.visualization.Table(container2);
            
         
            if (window.console && window.console.firebug) 
                console.log(queryPath);

            
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
<body id="page2">
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
    Enter entity name (stock ticker, equity index, currency cross, etc):
    <BR>
  	<input type='text' id='a_c' style='z-index:3' /><br/>
  	<BR>
  
	<div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div>
	<BR>
	</div>
	
	<div style="font-size:20" id="charttabs";>
	<ul id="tabs">
	<li id="tab1"><a href=<?php echo "\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?".$_SERVER['QUERY_STRING']."\""?>>Day Granularity</a></li>
	<li id="tab2"><a href=<?php echo "\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart15min.php?".$_SERVER['QUERY_STRING']."\""?>>Quarter Hour Granularity</a></li>
	</ul>
	</div>
    <div id="chart-div" style="margin: 30px 0 20px 0;width:800px;height:600px"></div>

 
</div> <!--  pf-body --> 

</div> <!--  siteContain -->  
<?php 
	}
?>
</body>
</html>
