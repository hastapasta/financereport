<!--
You are free to copy and use this sample in accordance with the terms of the
Apache license (http://www.apache.org/licenses/LICENSE-2.0.html)
-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>
      Google Visualization API Sample
    </title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript" src="querywrapper.js"></script>
    <script type="text/javascript">
      google.load('visualization', '1', {packages: ['corechart']});
      google.setOnLoadCallback(function() { sendAndDraw('') });
      var dataSourceUrl = 'http://localhost/PHP/gadgetsamples/echodatasource6.php?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
      var query;

      function sendAndDraw(queryString) {
          var container = document.getElementById('visualization');
          var orgChart = new google.visualization.BarChart(container);
          query && query.abort();
          alert(dataSourceUrl + queryString);
          query = new google.visualization.Query(dataSourceUrl + queryString);
          alert("here 1");
          var queryWrapper = new QueryWrapper(query, orgChart, {'size': 'large'}, container);
          queryWrapper.sendAndDraw();
          alert ("here 2");
        }
    </script>
    <script type="text/javascript">
      /*function drawVisualization() {
        // Create and populate the data table.
        var data = new google.visualization.DataTable();
        var raw_data = [['Austria', 1336060, 1538156, 1576579, 1600652, 1968113, 1901067],
                        ['Bulgaria', 400361, 366849, 440514, 434552, 393032, 517206],
                        ['Denmark', 1001582, 1119450, 993360, 1004163, 979198, 916965],
                        ['Greece', 997974, 941795, 930593, 897127, 1080887, 1056036]];
        
        var years = [2003, 2004, 2005, 2006, 2007, 2008];
                        
        data.addColumn('string', 'Year');
        for (var i = 0; i  < raw_data.length; ++i) {
          data.addColumn('number', raw_data[i][0]);    
        }
        
        data.addRows(years.length);
      
        for (var j = 0; j < years.length; ++j) {    
          data.setValue(j, 0, years[j].toString());    
        }
        for (var i = 0; i  < raw_data.length; ++i) {
          for (var j = 1; j  < raw_data[i].length; ++j) {
            data.setValue(j-1, i+1, raw_data[i][j]);    
          }
        }
        
        // Create and draw the visualization.
        new google.visualization.BarChart(document.getElementById('visualization')).
            draw(data,
                 {title:"Yearly Coffee Consumption by Country",
                  width:600, height:400,
                  vAxis: {title: "Year"},
                  hAxis: {title: "Cups"}}
            );
      }
      

      google.setOnLoadCallback(drawVisualization);*/
    </script>
  </head>
  <body style="font-family: Arial;border: 0 none;">
  <form action="">
  <br/><br/>
  <select onChange="sendAndDraw(this.value)">
    <option value="?code=1">1</option>
    <option value="?code=2">2</option>
  </select>
	</form>
<br/>
    <div id="visualization" style="width: 600px; height: 400px;"></div>
  </body>
</html>
​