<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

if (isset($_GET['a']))
	$alertid = $_GET['a'];
else
	die("No alert id parameter in url. Unable to render chart.");
	
$sql = "select entities.* from entities,alerts where alerts.entity_id=entities.id AND alerts.id=".$alertid;
$result = mysql_query($sql);

$row = mysql_fetch_array($result);




?>
<!DOCTYPE html>
<html>
<head>
	<?php IncFunc::icon();?>
    <title>Complete visualization example</title>
    <link rel="stylesheet" href="../../site/includes/style.css"	type="text/css" />
    
    
    
    
    <link rel="stylesheet" href="../../site/includes/jquery-ui-1.8.11.custom.css" type="text/css" />
    <script type="text/javascript" src="../../site/includes/jquery-1.5.1.js"></script>
    <script type="text/javascript" src="../../site/includes/jquery-ui-1.8.11.custom.min.js"></script>
    
    
    <?php IncFunc::yuiDropDownJavaScript(); ?>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">

    	$(function(){
    		$( "#a_c" ).autocomplete({
    			source: function( request, response ) {
					$.ajax({
						url: "getTicker.php",
						dataType: "json",
						data: {
							maxRows: 12,
							term: request.term
						},
						success: function( data ) {
							//console.log(data);
							response( $.map( data, function( item ) {
								return {
									label: item.ticker,
									value: item.ticker
								}
							}));
						}
					});
				},
    			minLength: 1,
    			select: function( event, ui ) {
    				if(ui.item)
    				{
        				loadChart("",  ui.item.value);
    				}
    			}
    		});
        });
    
		var count = 0;
	
        //google.load('visualization', '1', {packages: ['linechart']});
        google.load('visualization', '1', {packages: ['corechart']});
        google.setOnLoadCallback(loadChart);
        line_chart = null;
       
        var options = {};

        

        
        function loadChart(event, ticker) {

      		count++;
         	var str = '' + count;

         	document.getElementById('chart-div').innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

         	<?php //echo "options['title']='".$row['full_name']."';";?>
         	var options = {};
         	//options['title'] = 'Cotton Futures';
         	options['height'] = 600;
         	options['width'] = 900;
         	options['fontSize'] = 15;
         	options['curveType'] = 'function';
         	options['pointSize'] = 0;
			options.interpolateNulls = true;
         	<?php echo "options.title='".$row['ticker']." - ".$row['full_name']."';"; ?>
         	//options['legend'] = 'none';

         	//options['vAxis'] = {title: "Vertical Title"};
         	//options['hAxis'] = {title : "Horizontal Title"};

 			var queryPath = '<?php echo IncFunc::$JSP_ROOT_PATH;?>/mysqldatasource1.jsp?entityid=<?php echo $row[id];?>&begindate=2011-01-01';

			if(ticker != undefined){
				queryPath += '&ticker=' + escape(ticker); 	
			}
 			alert(queryPath);
           	var query = new google.visualization.Query(queryPath);
           	       	  	 
            <?php echo "var query = new google.visualization.Query('".IncFunc::$JSP_ROOT_PATH."/mysqldatasource1.jsp?entityid=".$row['id']."&begindate=2011-01-01');"; ?>
            
            
            //alert('here 2');
            
            query.setQuery(str);
            //alert('here 3');
           // alert('here 2');
          //  alert('metric1: ' + metric1);
            query.send(
           	function(res)
           	{
				//alert('here 2.05');
                if(res.isError())
                {
				  //alert('here 2.06');
                    alert(res.getDetailedMessage());
                }
                else
                {
                    //alert('here 2.07');
                    if(line_chart === null)
                    {
		     			//alert('here 2.1');
                    	 line_chart = new google.visualization.LineChart(document.getElementById('chart-div'));
                    	 //alert('here 2.5');
                   	}
		   			//alert('here 2.7');
                    //column_chart.draw(res.getDataTable(), {'height': 600, 'width': 800});
                    // var title = document.getElementById('chart-title');
					 <?php //echo "title.innerHTML ='".$row['ticker']."';"; ?> 
                     //var description = document.getElementById('chart-description');
                     <?php //echo "description.innerHTML ='".$row['full_name']."';"; ?> 
                     
                    
                     line_chart.draw(res.getDataTable(), options);
                   
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>
<div id="jq-siteContain">

<?php 
	IncFunc::header1("charts"); 
	IncFunc::yuiDropDownMenu();

?>

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
