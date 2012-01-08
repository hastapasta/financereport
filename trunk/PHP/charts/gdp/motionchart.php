<?php
//require_once 'init2.php';

include ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';





?>
<!DOCTYPE html>
<html>
<head>
    <?php IncFunc::jQuery();?>   
	<?php IncFunc::icon();?>
    <?php IncFunc::title();?>
    <?php IncFunc::linkStyleCSS();?>
 	<?php //IncFunc::yuiDropDownJavaScript(); ?>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
		//alert('here .5');

 
        google.load('visualization', '1', {'packages': ['motionchart']});
        google.setOnLoadCallback(loadChart);
        motion_chart = null;
        var advsettings = null;
		<?php
		/*
		 * NOTE!! You have to be careful there are no extra newlines in the file pointed to by $advfilename.
		 */
        $advfilename="";
        $advsettings='{"showTrails":false,"iconType":"VBAR","orderedByY":false,"dimensions":{"iconDimensions":["dim0"]},"yZoomedDataMin":-7,"xZoomedDataMin":0,"xZoomedIn":false,"yAxisOption":"2","xLambda":1,"duration":{"timeUnit":"Y","multiplier":1},"time":"2015","yLambda":1,"xZoomedDataMax":182,"iconKeySettings":[],"colorOption":"_UNIQUE_COLOR","orderedByX":true,"yZoomedIn":false,"playDuration":15000,"sizeOption":"_UNISIZE","xAxisOption":"2","uniColorForNonSelected":false,"nonSelectedAlpha":0.4,"yZoomedDataMax":120}';
        if (isset($_GET['advfilename'])) {
        	$advfilename = $_GET['advfilename'];
          	$advsettings = file_get_contents('../../json/'.$advfilename.'.html', FILE_USE_INCLUDE_PATH);
       
        
        }
        ?>

        function showState() {
		
		var state = motion_chart.getState();
		alert(state);
        }


   	    function loadChart() {

        	  document.getElementById('chart-div').innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;
        
            //var str = 'select ticker,calyear,value where ((calyear=2010 or calyear=2011 or calyear=2012) and ticker=\'WMT\') group by ticker, calyear limit 30 format calyear "%d"';
           // alert('count:' + count);
 			
           
            //var query = new google.visualization.Query('http://localhost:8080/JSPDataSource/mysqldatasource7.jsp?taskid=22');
            
          	<?php 
          	//echo "var datasourceurl='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource7.jsp?metricid=2';";
          	echo "var datasourceurl='".IncFunc::$PHP_ROOT_PATH."/json/gdpmotion.html';";
          	?>

          	if (window.console && window.console.firebug) {console.log(datasourceurl)}
            
            var query = new google.visualization.Query(datasourceurl);
  
            
            query.setQuery(str);

            var options = {};

            options['height'] = 600;
            options['width'] = 800;
            options.wmode='opaque';

            options.state = {};

            <?php 
            echo "options['state']='".$advsettings."';\n";
            ?>
            //alert(options['state']);
         
			/*if (!advsettings) {
          		options['state'] = '{"showTrails":false,"iconType":"VBAR","orderedByY":false,"dimensions":{"iconDimensions":["dim0"]},"yZoomedDataMin":-7,"xZoomedDataMin":0,"xZoomedIn":false,"yAxisOption":"2","xLambda":1,"duration":{"timeUnit":"Y","multiplier":1},"time":"2015","yLambda":1,"xZoomedDataMax":182,"iconKeySettings":[],"colorOption":"_UNIQUE_COLOR","orderedByX":true,"yZoomedIn":false,"playDuration":15000,"sizeOption":"_UNISIZE","xAxisOption":"2","uniColorForNonSelected":false,"nonSelectedAlpha":0.4,"yZoomedDataMax":120}';
			}
          	else {
              	options['state'] = advsettings;
          	}*/
              		
          	

            //options['state'] = '{"dimensions":{"iconDimensions":["dim0"]},"duration":{"multiplier":1,"timeUnit":"Y"},"yZoomedDataMax":140,"time":"2015","iconType":"LINE","xZoomedDataMin":1230768000000,"yZoomedDataMin":0,"uniColorForNonSelected":false,"yLambda":1,"xAxisOption":"_TIME","sizeOption":"_UNISIZE","yAxisOption":"2","orderedByY":false,"iconKeySettings":[{"key":{"dim0":"China"}},{"key":{"dim0":"Russia"}},{"key":{"dim0":"India"}},{"key":{"dim0":"United States"}},{"key":{"dim0":"Brazil"}}],"xZoomedIn":false,"xZoomedDataMax":1420070400000,"playDuration":15000,"yZoomedIn":false,"xLambda":1,"colorOption":"_UNIQUE_COLOR","orderedByX":false,"nonSelectedAlpha":0,"showTrails":false}';
            	            

            
        
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
	IncFunc::header2("charts"); 
	IncFunc::yuiDropDownMenu();

?>
<div id="pf-body">

    <div id="chart-div" style="margin-top:150px"></div>
    
	<!-- <input type="button" value="Display Chart" onclick="showState();return false;"> -->
 
</div>
</div> <!--  siteContain -->
</body>
</html>
