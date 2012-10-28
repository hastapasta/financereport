<?php
//require_once 'init2.php';

require_once ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';

$cache = false;
if (isset($_GET['cache']))
	if (strcasecmp('true', $_GET['cache'])==0)
		$cache=true;
		
$gdptype = 'cp';
if (isset($_GET['gdptype']))
	$gdptype = $_GET['gdptype'];
	
$metricids = "2,3";
if (strcasecmp('ppp', $gdptype)==0) {
	$metricids="12,13";
}



?>
<!DOCTYPE html>
<html>
<head>
    <?php IncFunc::jQuery();?>   
	<?php IncFunc::icon();?>
    <?php IncFunc::title();?>
    <?php IncFunc::linkStyleCSS();?>
    <?php IncFunc::checkFlash();?>
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

        $(function(){

    		$( "input:button").button();
    		$( "input:button").css("padding",0);
  
    		
    	});

		function viewDataTable() {
    		
        	<?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/gdp/gdpdatatable.php?abs=true\");";?>
    	}


   	    function loadChart() {

   	    	if (isFlashEnabled() == true)
        		$('#chart-div').html("<img src=\"../../site/images/spinner3-black.gif\" />");
   	    	else {
   	   	    	$('#chart-div').html("This chart requires Flash.");
   	   	    	$('#chart-div').css("font-size","2.0em");
   	   	    	$('#chart-div').css("background-color", "rgb(192, 0, 0)");
   	   	    	$('#chart-div').css("width","250px");
   	   	    	return;
   	    	}

        	//document.getElementById('chart-div').innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;
        
         	<?php 
          		if ($cache==true) 
            		echo "var dataSourceUrl='".IncFunc::$PHP_ROOT_PATH."/json/gdpmotion.html';";
     			else {
					echo "var dataSourceUrl = '".Environment::getJSPPath(true)."/mysqldatasource7.jsp?metricids=".$metricids."';";
      			}
          	?>

          	if (window.console) {console.log(dataSourceUrl)}
            
            var query = new google.visualization.Query(dataSourceUrl);
  
            
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
                if(res.isError()) {
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
?>
<div id="pf-body">
<?php 
	IncFunc::apycomDropDownMenu();
?>
<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 50px 0 0 0;font-size: medium;font-weight:bold;"><?php echo strtoupper('GDP GROWTH (CUMULATIVE)'); ?></div>

    <div id="chart-div" style="margin-top:70px"></div>
    
	<!-- <input type="button" value="Display Chart" onclick="showState();return false;"> -->
	
<div style="font-size: 1.5em;float: left;margin-bottom: 20px;margin-top:50px;">
<input type="button"  value="View Data Table"	onclick="viewDataTable();return false;"> <br />
</div>
 
</div>
</div> <!--  siteContain -->
</body>
</html>
