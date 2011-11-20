<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

$alertid="";

if (isset($_GET['countryid']))
	$countryid = $_GET['countryid'];
else
	$error =  "No country id in url. Unable to render chart.";
	//die("No alert id,entity id  or ticker parameter in url. Unable to render chart.");
	




$begindate="2011-01-01";
$enddate="";

if (isset($_GET['begindate']) && !empty($_GET['begindate']))
	$begindate=$_GET['begindate'];
	
if (isset($_GET['enddate']) && !empty($_GET['enddate']))
	$enddate=$_GET['enddate'];
	
	
	
	

$title="";
if (isset($_GET['title']))
	$title=urldecode($_GET['title']);
	
$sql = "select name from countries where id=".$countryid;
$result = mysql_query($sql);
$row = mysql_fetch_array($result);
$countryname = $row['name'];
	
	
$sql = "select entities.id ";
$sql .= " from entities,entities_entity_groups ";
$sql .= " where entities.id=entities_entity_groups.entity_id ";
$sql .= " and entities.country_id=".$countryid;
$sql .= " and entities_entity_groups.entity_group_id=5";

$result = mysql_query($sql);
$count = 0;
while ($row= mysql_fetch_array($result)) {
	$entities.=$row['id'].",";
	$metrics.="1,";
	$tasks.="0,";
	$count++;
}

if ($count == 0) {
	$error = "No equity index data for the country: ".$countryname;
}
//$entities = substr($entities,0,strlen($entities)-1);
//$metrics.="12,13";
//$metrics.="30,29";
$metrics.="2,3";
$tasks.="22,33";

$sql = "select id from entities where ticker='macro' and country_id=".$countryid;
$result = mysql_query($sql);
$row = mysql_fetch_array($result);
$entities.=$row['id'].",".$row['id'];

echo $entities;




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
    <?php //IncFunc::yuiDropDownJavaScript(); ?>
    <?php IncFunc::googleGadget(); ?>
   
    <script type="text/javascript">

    	$(function(){
    		$( "#a_c" ).autocomplete({
    			source: function( request, response ) {
					$.ajax({
						url: "../../site/includes/getCountry.php",
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
        				
        				//alert(ui.item.id);
        				//sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name);
        				<?php
        						echo "window.location.replace(\"";
        						echo IncFunc::$PHP_ROOT_PATH;
        						echo "/charts/countries/linechart.php";
        				
        						/*
        						 * There are other javascript enocde functions: escape() and encodeURIComponent()
        						 */
        						echo "?countryid=\"+encodeURI(ui.item.id));";
        						
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
						echo "/charts/allassets/linechart.php?title=";
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
	
        google.load('visualization', '1', {packages: ['corechart']});
        //google.load('visualization', '1', {packages: ['annotatedtimeline']});
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
         	options.interpolateNulls = true;
         	options['curveType'] = 'function';
         	options.hAxis= {};
         	options.hAxis.slantedText = true;
         	options.hAxis.slantedTextAngle = 45;
         	options['width'] = 1000;
         	<?php echo "options.title='Comparison of GDP Growth (Actual and Estimated) to Equity Indexes for Country: ".$countryname."';";?>
         	
            




        	
        	
        	/*
        	* metric id of zero means use the default.
        	*/
			<?php 

			echo "var queryPath = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource15multiplecountry.jsp?begindate=".$begindate;
			if (!empty($enddate))
				echo "&enddate=".$enddate;
			echo "&entityid=".$entities;
			echo "&countryid=".$countryid;
			echo "&taskid=".$tasks;
			echo "&metricid=".$metrics."';";
	
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

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            //var lineChart1 = new google.visualization.AnnotatedTimeLine(container1);
            var lineChart1 = new google.visualization.LineChart(container1);
        
            
         
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
</head>
<body>
<div id="jq-siteContain">

<?php 
	IncFunc::header2("charts"); 
	IncFunc::yuiDropDownMenu();

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

    <div id="pf-form" style="margin:20px 0 0 0;font-size:15px;">
    Enter country name:
    <BR>
  	<input type='text' id='a_c' style='z-index:3' /><br/>
  	<BR>
  
	<div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div>
	</div>
	
	

	
	<div style="font-size:20" id="chart-description"></div>
    <div id="chart-div" style="margin: 30px 0 20px 0;width:800px;height:600px"></div>

 
    

</div> <!--  siteContain -->  
<?php 
	}
?>
</body>
</html>
