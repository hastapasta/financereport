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
<html>
<head>
    <title>Complete visualization example</title>
    <link rel="stylesheet" href="../../site/includes/style.css"	type="text/css" />
    <?php IncFunc::yuiDropDownJavaScript(); ?>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
	

 
        google.load('visualization', '1', {packages: ['linechart']});
        google.setOnLoadCallback(loadChart);
        line_chart = null;
        var options = {};

        

        
        function loadChart() {
        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;

         	<?php //echo "options['title']='".$row['full_name']."';";?>
         	
         	//options['title'] = 'Cotton Futures';
         	options['height'] = 600;
         	options['width'] = 900;
         	options['fontSize'] = 1;
         	options['curveType'] = 'function';
         	options['pointSize'] = 0;
         	//options['legend'] = 'none';
         
 			
           
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
                     var title = document.getElementById('chart-title');
					 <?php echo "title.innerHTML ='".$row['ticker']."';"; ?> 
                     var description = document.getElementById('chart-description');
                     <?php echo "description.innerHTML ='".$row['full_name']."';"; ?> 
                     
                    
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

  
	<div style="font-size:20" id="chart-title"></div>
	<BR/>
	<div style="font-size:15" id="chart-description"></div>
    <div id="chart-div"></div>

 
    

</div> <!--  siteContain -->  
</body>
</html>
