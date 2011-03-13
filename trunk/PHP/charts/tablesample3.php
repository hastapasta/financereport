<?php
require_once '../common/functions.php';

db_utility::db_connect();




?>
<html>
<head>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
        alert('here 0');
		var count = 0;
		alert('here .5');
		

        google.load('visualization', '1', {packages: ['table']});
        google.setOnLoadCallback(loadChart);
        table_chart = null;
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

        	var chart1 = document.getElementById('chart-div');
            chart1.innertHTML="<img src='http://www.google.com/ig/images/spinner.gif' />";
        	
         	//alert('here 1');
            var timeeventid = document.getElementById('timeeventid').value;
            var userid = document.getElementById('users').value;
            var taskid = document.getElementById('tasks').value;
            
      		count++;
         	var str = '' + count;

         	options['title'] = 'Test Chart Title';
         	options['height'] = 400;
         	options['width'] = 1000;
         	options['showfilters'] = true;
         
 			
            //var queryurl = 'http://www.pikefin.com/testjsp/JSPDataSource/mysqldatasource.jsp?frequency=' + frequency;
            <?php 
            echo "var queryurl = '".db_utility::$datasourceurl."mysqldatasource.jsp?timeeventid=' + timeeventid;";
            ?>
            queryurl += '&userid=' + userid;
            queryurl += '&taskid=' + taskid;

            alert(queryurl);
          
            var query = new google.visualization.Query(queryurl);
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
                    if(table_chart === null)
                    {
		     			//alert('here 2.1');
                    	 table_chart = new google.visualization.Table(document.getElementById('chart-div'));
                    	 //alert('here 2.5');
                   	}
		   			//alert('here 2.7');
                    //column_chart.draw(res.getDataTable(), {'height': 600, 'width': 800});
 

                    
                    table_chart.draw(res.getDataTable(), options);
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>


    <div class="chart-options">
       <BR><BR>
         Time Events: <BR><BR>
    
         <select id="timeeventid" onchange="window.location.reload()">
         <option value="all">all</option>
        <?php 
        $query1 = "select id,name from time_events";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['name']."</option>";
			
		}
        
        ?>
        </select>
        
        <BR><BR>
         Users: <BR><BR>
         <select id="users" onchange="window.location.reload()">
         <option value="all">all</option>
        <?php 
        $query1 = "select id,username from users";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['username']."</option>";
			
		}
        
        ?>
        </select>
        
              <BR><BR>
         Task names: <BR><BR>
         <select id="tasks" onchange="window.location.reload()">
         <option value="all">all</option>
        <?php 
        $query1 = "select id,name from tasks";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['name']."</option>";
			
		}
        
        ?>
        </select>
        
       
        

    </div>
    <BR>
    <div id="chart-div"></div>


    

  
</body>
</html>
