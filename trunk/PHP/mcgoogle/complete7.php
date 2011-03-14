<?php
//require_once 'init2.php';





?>
<html>
<head>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
		alert('here .5');

 
        google.load('visualization', '1', {packages: ['table']});
        google.setOnLoadCallback(loadChart);
        column_chart = null;
        var options = {};

        

        function init() {
            gadgetHelper = new google.visualization.GadgetHelper();
            var container = document.getElementById('chart');
            container.style.width = document.body.clientWidth;
            container.style.height = document.body.clientHeight;
            columnChart = new google.visualization.ColumnChart(container);
            options['title'] = prefs.getString('chartTitle');
            options['titleX'] = prefs.getString('labelx');
            options['titleY'] = prefs.getString('labely');
            options['isStacked'] = prefs.getBool('stacked');
            options['is3D'] = prefs.getBool('3d');
            var legendOptions = ['right', 'left', 'top', 'bottom', 'none'];
            options['legend'] = legendOptions[prefs.getInt('legend')];
            if (prefs.getString('min') != '') {
              options['min'] = prefs.getInt('min');
            }
            if (prefs.getString('max') != '') {
              options['max'] = prefs.getInt('max');
            }
            var query = gadgetHelper.createQueryFromPrefs(prefs);
            query.send(responseHandler);
          };

              
    
        
        function loadChart() {
        	
         	alert('here 1');
            var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;

         	options['title'] = 'Test Chart Title';
         	options['height'] = 600;
         	options['width'] = 800;
         
 			
           
            var query = new google.visualization.Query('http://www.pikefin.com/phpdev/ajaxsample/echodatasource2.php');
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
                    if(column_chart === null)
                    {
		     			//alert('here 2.1');
                    	 column_chart = new google.visualization.ColumnChart(document.getElementById('chart-div'));
                    	 //alert('here 2.5');
                   	}
		   			//alert('here 2.7');
                    //column_chart.draw(res.getDataTable(), {'height': 600, 'width': 800});
                    column_chart.draw(res.getDataTable(), options);
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
