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
	

$entityid = str_replace(' ',',',$entityid);


	
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
         	
            




        	
        	
        	/*
        	* metric id of zero means use the default.
        	*/
        	var queryPath = '<?php echo IncFunc::$JSP_ROOT_PATH;?>mysqldatasource15multiple.jsp?begindate=2011-01-01&alertid=0&metricid=0';
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
						echo "title.innerHTML+='".$row['ticker']." - ".$row['full_name']."<BR>';";
					}

					
				?>
			}

			var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
			
            
            var container1 = document.getElementById('chart-div');
            //var container2 = document.getElementById('orgchart2');
           
            
            var lineChart1 = new google.visualization.AnnotatedTimeLine(container1);
            //var tableChart2 = new google.visualization.Table(container2);
            
         
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
<div id="chartTitle" style="margin: 20px 0 0 0;font-size: medium;font-weight:bold;"><u><?php echo $title ?></u></div>

    <div id="pf-form" style="margin:20px 0 0 0;font-size:15px;">
    Enter entity name (stock ticker, equity index, currency cross, etc):
    <BR>
  	<input type='text' id='a_c' style='z-index:3' /><br/>
  	<BR>
  
	<div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div>
	</div>
	
	

	
	<div style="font-size:20" id="chart-description"></div>
    <div id="chart-div" style="margin: 30px 0 20px 0;width:800px;height:600px"></div>

 
    

</div> <!--  siteContain -->  
</body>
</html>
