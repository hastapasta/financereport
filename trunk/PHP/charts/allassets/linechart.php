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
else
	die("No alert id or entity id parameter in url. Unable to render chart.");
	
if (!empty($alertid))
{
	$sql1 = "select entity_id from alerts where id=".$alertid;
	$result1 = mysql_query($sql1);
	$row1 = mysql_fetch_array($result1);
	
	
	$entityid = $row1['entity_id'];
}

$title="";
if (isset($_GET['title']))
	$title=urldecode($_GET['title']);
	

	

	
$sql = "select * from entities where id=".$entityid;
$result = mysql_query($sql);
$row = mysql_fetch_array($result);
	


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
    <?php IncFunc::yuiDropDownJavaScript(); ?>
    <?php IncFunc::googleGadget(); ?>
   
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
    				if(ui.item)
    				{
        				//loadChart("",  ui.item.value);
        				//alert('value:' + ui.item.value);
        				//var tmp[] = split(ui.item.value,'|');
        				//$( "#a_c" ).val(ui.item.value.split("|")[1]);
        				//sendAndDraw(ui.item.value.split("|")[0],ui.item.value.split("|")[1],ui.item.value.split("|")[2]);
        				sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name);
    				}
    			}
    		});
        });

        
    
		var count = 0;
	
        //google.load('visualization', '1', {packages: ['linechart']});
        google.load('visualization', '1', {packages: ['corechart']});
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
         	options['height'] = 600;
         	options['width'] = 900;
         	options['fontSize'] = 15;
         	//options['curveType'] = 'function';
         	options['pointSize'] = 0;
			options.interpolateNulls = true;
			options.backgroundColor = {};
			options.backgroundColor.fill = 'white';
			options.backgroundColor.stroke = '#000';
         	options.backgroundColor.strokeWidth = 8;
         	
            




        	var queryPath = '<?php echo IncFunc::$JSP_ROOT_PATH;?>mysqldatasource1.jsp?begindate=2011-01-01&metricid=1';

			if(id != undefined){
				queryPath += '&entityid=' + escape(id); 
				options.title = ticker + " - " + fullname;

				//alert(ticker);
			}
			else
			{
				queryPath += <?php echo "'&entityid=".$entityid."';"; ?>
				<?php echo "options.title='".$row['ticker']." - ".$row['full_name']."';"; ?>
			}

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            //alert(queryPath);

 
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            var lineChart1 = new google.visualization.LineChart(container1);
            //var tableChart2 = new google.visualization.Table(container2);
           
            
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
	IncFunc::header1("charts"); 
	IncFunc::yuiDropDownMenu();

?>
<div id="chartTitle" style="margin: 20px 0 0 300px;font-size: medium;font-weight:bold;"><u><?php echo $title ?></u></div>

    <div id="pf-form" style="margin:20px 0 0 0;font-size:15px;">
    Enter entity name (stock ticker, equity index, currency cross, etc):
    <BR>
  	<input type='text' id='a_c' /><br/>
  
  
	<!-- <div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div> -->
	</div>
	<BR>
	
	<div style="font-size:20" id="chart-description"></div>
    <div id="chart-div" style="margin: 10px 0 20px 0;"></div>

 
    

</div> <!--  siteContain -->  
</body>
</html>
