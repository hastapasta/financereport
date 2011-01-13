<?php
require_once 'init2.php';





?>
<html>
<head>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;


 
        google.load('visualization', '1', {'packages': ['motionchart']});
        google.setOnLoadCallback(loadChart);
        motion_chart = null;
    
        
        function loadChart() {
        	
         	alert('here 1');
            var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;
            //var str = 'select ticker,calyear,value where ((calyear=2010 or calyear=2011 or calyear=2012) and ticker=\'WMT\') group by ticker, calyear limit 30 format calyear "%d"';
           // alert('count:' + count);
 			
           
            var query = new google.visualization.Query('returndata.php?metric1=' + metric1);
            //alert(str);
            
            query.setQuery(str);
           // alert('here 2');
          //  alert('metric1: ' + metric1);
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
                    motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800});
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>

    <p>This is a more complete example showing how you can build queries to provide interesting data visualizations.</p>
    <div class="chart-options">
        <select id="metric-1" onchange="window.location.reload()">
            <option value="1">Companies 1-50</option>
            <option value="51">Companies 51-100</option>
            <option value="101">Companies 101-150</option>
            <option value="151">Companies 151-200</option>
            <option value="201">Companies 201-250</option>
            <option value="251">Companies 251-300</option>
            <option value="301">Companies 301-350</option>
            <option value="351">Companies 351-400</option>
            <option value="401">Companies 401-450</option>
            <option value="451">Companies 451-500</option>
        </select>

    </div>
    <div id="chart-div"></div>
    

  
</body>
</html>
