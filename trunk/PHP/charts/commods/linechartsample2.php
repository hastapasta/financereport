<?php
//require_once 'init2.php';





?>
<html>
<head>
	<?php IncFunc::icon();?>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
		//alert('here .5');

 
        google.load('visualization', '1', {packages: ['linechart']});
        google.setOnLoadCallback(loadChart);
        line_chart = null;
        var options = {};

        

       /* function init() {
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
          };*/

              
    
        
        function loadChart() {
        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;

         	options['title'] = 'Cotton Futures';
         	options['height'] = 600;
         	options['width'] = 800;
         	//options['legend'] = 'none';
         http://localhost:8080/JSPDataSource/mysqldatasource4.jsp?entityid=660&begindate=01-01-2011
 			
           
            //var query = new google.visualization.Query('http://www.pikefin.com/devjsp/JSPDataSource/mysqldatasource1.jsp?entityid=660,661&begindate=01-01-2011');
        	var query = new google.visualization.Query('http://www.pikefin.com/devjsp/JSPDataSource/mysqldatasource4.jsp?entityid=660&begindate=01-01-2011');
            
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
                   
                    
                    line_chart.draw(res.getDataTable(), options);
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>

  

    <div id="chart-div"></div>

 
    

  
</body>
</html>
