<?php
//require_once 'init2.php';

include ("../../site/includes/sitecommon.php");



?>
<html>
<head>
	<?php IncFunc::icon();?>
    <title>Complete visualization example</title>
    <link rel="stylesheet" href="/PHP/site/includes/style.css" type="text/css" />
  <?php IncFunc::yuiDropDownJavaScript(); ?>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
		//alert('here .5');

 
        google.load('visualization', '1', {'packages': ['motionchart']});
        google.setOnLoadCallback(loadChart);
        motion_chart = null;

        function showState() {
		
		var state = motion_chart.getState();
		alert(state);
        }
    
        
        function loadChart() {
        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;
        
            //var str = 'select ticker,calyear,value where ((calyear=2010 or calyear=2011 or calyear=2012) and ticker=\'WMT\') group by ticker, calyear limit 30 format calyear "%d"';
           // alert('count:' + count);
 			
           
            //var query = new google.visualization.Query('http://localhost:8080/JSPDataSource/mysqldatasource7.jsp?taskid=22');
            
            <?php echo "var query = new google.visualization.Query('".IncFunc::$JSP_ROOT_PATH."/mysqldatasource7.jsp?taskid=22');"; ?>
  
            
            query.setQuery(str);

            var options = {};

            options['height'] = 600;
            options['width'] = 800;

            /*options['state'] =	'{"iconKeySettings":[],';  
            options['state'] +='"stateVersion":3,';  
            options['state'] +='"time":"notime",';  
            options['state'] +='"xAxisOption":"Time",';  
            options['state'] +='"playDuration":15,';  
            options['state'] +='"iconType":"BUBBLE",';  
            options['state'] +='"sizeOption":"_NOTHING",';  
            options['state'] +='"xZoomedDataMin":null,';  
            options['state'] +='"xZoomedIn":false,';  
            options['state'] +='"duration":{"multiplier":1,"timeUnit":"none"},';  
            options['state'] +='"yZoomedDataMin":null,';  
            options['state'] +='"xLambda":1,';  
            options['state'] +='"colorOption":"_NOTHING",';  
            options['state'] +='"nonSelectedAlpha":0.4,';  
            options['state'] +='"dimensions":{"iconDimensions":[]},';  
            options['state'] +='"yZoomedIn":false,';  
            options['state'] +='"yAxisOption":"pctchange",';  
            options['state'] +='"yLambda":1,';  
            options['state'] +='"yZoomedDataMax":null,';  
            options['state'] +='"showTrails":true,';  
            options['state'] +='"xZoomedDataMax":null};';*/

            options['state'] = '{"showTrails":false,"iconType":"VBAR","orderedByY":false,"dimensions":{"iconDimensions":["dim0"]},"yZoomedDataMin":-7,"xZoomedDataMin":0,"xZoomedIn":false,"yAxisOption":"2","xLambda":1,"duration":{"timeUnit":"Y","multiplier":1},"time":"2015","yLambda":1,"xZoomedDataMax":182,"iconKeySettings":[],"colorOption":"_UNIQUE_COLOR","orderedByX":true,"yZoomedIn":false,"playDuration":15000,"sizeOption":"_UNISIZE","xAxisOption":"2","uniColorForNonSelected":false,"nonSelectedAlpha":0.4,"yZoomedDataMax":120}';
            	            

            
        
            query.send(
           	function(res)
           	{
				//alert('here 2.05');
                if(res.isError())
                {
				  // alert('here 2.06');
                    alert(res.getDetailedMessage());
                }
                else
                {
                    if(motion_chart === null)
                    {
		     			//alert('here 2.1');
                    	 motion_chart = new google.visualization.MotionChart(document.getElementById('chart-div'));
                    	 //alert('here 2.5');
                   	}
		   			//alert('here 2.7');
                    //motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800, 'interpolateNulls': false});
                    motion_chart.draw(res.getDataTable(), options);
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

    <div id="chart-div" style="margin-top:20px"></div>
    
	<!--  <input type="button" value="Display Chart" onclick="showState();return false;"> -->
 
</div> <!--  siteContain -->
</body>
</html>
