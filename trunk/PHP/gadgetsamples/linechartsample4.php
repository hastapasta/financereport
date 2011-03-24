<?php
//require_once 'init2.php';





?>
<html>
<head>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript" src="querywrapper.js"></script>
    <script type="text/javascript">
		var count = 0;
		//alert('here .5');

 
        google.load('visualization', '1', {packages: ['linechart']});
        google.setOnLoadCallback(function() { sendAndDraw('') });
        line_chart = null;
        var dataSourceUrl = 'http://localhost:8080/JSPDataSource/mysqldatasource1.jsp?begindate=2011-01-01';
        var query;
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

              
    
        
          function sendAndDraw(queryString) {
              var options = {};
        	  options['height'] = 600;
           	  options['width'] = 800;
              var container = document.getElementById('chart-div');
              var orgChart = new google.visualization.LineChart(container);
              query && query.abort();
              //alert(dataSourceUrl + queryString);
              query = new google.visualization.Query(dataSourceUrl + queryString);
              var queryWrapper = new QueryWrapper(query, orgChart, options, container);
              queryWrapper.sendAndDraw();
            }
    </script>
</head>
<body>
  <form action="">
 
  <br /><br />
  <select onChange="sendAndDraw(this.value)">
    <option value="&entityid=660">660</option>
    <option value="&entityid=661">661</option>
  </select>
</form>

  

    <div id="chart-div"></div>

 
    

  
</body>
</html>
