<?php //debug($this,true);?>
<!-- <script type="text/javascript" src="/cakepftest/js/jquery-ui-1.8.12.custom.min.js"></script> -->
<script type="text/javascript">


		var count = 0;
		//alert('here .5');
		
	/*	$(function() {
			  $("#dialog").dialog();
			});*/
		
		
		
		
        //google.load('visualization', '1', {packages: ['corechart','table']});
        //google.setOnLoadCallback(function() { sendAndDraw('') });
        
        google.load('visualization', 1, {packages: ['table']});
        google.setOnLoadCallback(function() { sendAndDraw('') });

        google.load('visualization', 1, {packages: ['corechart']});
        google.setOnLoadCallback(function() { sendAndDraw2('') });
        
        

        <?php 
        	echo "var dataSourceUrl =  jsp_root_path + '/mysqldatasource9.jsp?pholder=val';\n";
        	
        	echo "var dataSourceUrl2 = jsp_root_path + '/mysqldatasource17.jsp?pholder=val';\n";
        	
        	//echo "var dataSourceUrl3 = jsp_root_path + '/mysqldatasource18.jsp?userid=".$this->getVar('userid')."';\n";

        ?>
        var query,query2;
        var options = {};
        var options2 = {};

        function genericClickHandler(localTableChart,localQueryWrapper) {

    		var row = localTableChart.getSelection();
    		
    		//var test5 = queryWrapper2;

    		var dt = localQueryWrapper.currentDataTable;
    		var val = dt.getValue(row[0].row,11);

    		 localTableChart.setSelection("");

    	    window.open(php_charts_path + "/charts/allassets/linechart.php?a=" + val + "&title=\"All Assets Indivdual Line Charts\"");
    	   

    	}

   

              
    
        
          function sendAndDraw(queryString) {
	            var options = {};
	        	options.height = 500;
	           	options.width = 1200;
	           	options.title = 'All Time Alert Counts';
				options.hAxis = {};
				options.hAxis.textPosition = 'out';
				options.hAxis.slantedTextAngle = 90;
				options.chartArea = {};
				options.chartArea.left = 50;
         		options.chartArea.top = 50;
         		options.chartArea.width = '50%';
         		options.chartArea.height = '50%';
         		
         		var prop1 = document.getElementById("observationperiod");
         		var prop2 = document.getElementById("entitygroup");
         		var prop3 = document.getElementById("user");

         		queryString = prop1.value + prop2.value + prop3.value;
         		

           	  
              //var container = document.getElementById('chart-div');
              var container2 = document.getElementById('table-div');

              //alert(dataSourceUrl);


              //container.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
              container2.innerHTML="<img src=\"" + php_site_path + "/site/images/spinner3-bluey.gif\" />";
            
              
              //var chart1 = new google.visualization.ColumnChart(container);
              var chart2 = new google.visualization.Table(container2);

              if (window.console && window.console.firebug) {console.log(dataSourceUrl2 + queryString)}
  
             /* query && query.abort();
              //alert(dataSourceUrl + queryString);
              query = new google.visualization.Query(dataSourceUrl+queryString);
              var queryWrapper = new QueryWrapper(query, chart1, options, container);
              queryWrapper.sendAndDraw();*/

              query2 && query2.abort();
              //alert(dataSourceUrl + queryString);
              query2 = new google.visualization.Query(dataSourceUrl2 + queryString);
              var queryWrapper2 = new QueryWrapper(query2, chart2, options2, container2,[10],5);
              google.visualization.events.addListener(chart2, 'select', function(event){
                  genericClickHandler(chart2,queryWrapper2);
         	 	  });
              queryWrapper2.sendAndDraw();

          
            }

          function sendAndDraw2() {
	            var options = {};
	        	options.height = 400;
	           	options.width = 1200;
	           	options.title = 'All Time Alert Counts';
				options.hAxis = {};
				options.hAxis.textPosition = 'out';
				options.hAxis.slantedTextAngle = 90;
				options.chartArea = {};
				options.chartArea.left = 50;
	       		options.chartArea.top = 50;
	       		options.chartArea.width = '50%';
	       		options.chartArea.height = '70%';

	       		var prop1 = document.getElementById("number");
	            var prop2 = document.getElementById("timeframe");
	            var prop3 = document.getElementById("entitygroup2");
	            var prop4 = document.getElementById("user2");
	            var prop5 = document.getElementById("observationperiod2");

	            queryString = prop1.value + prop2.value + prop3.value + prop4.value + prop5.value;
	            
       		

         	  
            var container = document.getElementById('chart-div');
           // var container2 = document.getElementById('table-div');

            //alert(dataSourceUrl);


            container.innerHTML="<img src=\"" + php_site_path + "/site/images/spinner3-bluey.gif\" />";
           // container2.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
          
            
            var chart1 = new google.visualization.ColumnChart(container);
           // var chart2 = new google.visualization.Table(container2);

            
			 if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString)}
            
            
            query && query.abort();
            //alert(dataSourceUrl + queryString);
            query = new google.visualization.Query(dataSourceUrl+queryString);
            var queryWrapper = new QueryWrapper(query, chart1, options, container);
            queryWrapper.sendAndDraw();

          

        
          }
    </script>
<div class="charts chart">

<?php //echo $this->element('actions',array('title'=>'Chart')); ?>
<?php //echo $this->element('chart_actions')?>



	<div style="clear: both;font-size:1.1em;">
	<BR>
	Most Recently Fired Alerts:
	</div>
	<div id="table-div"></div>
	 <form action="">
 
  

  Observation Periods: <BR>
	<select id="observationperiod" style="background-color: #FFFFFF">
	<option value="&obperiod=0">All Periods</option>
	<?php 
	$sql = "select description,id from time_events ";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"&obperiod=".$row1['id']."\">".$row1['description']."</option>";
	}	
	?>
	
	
</select> <BR>
Entity Group: <BR>
<select id="entitygroup" style="background-color: #FFFFFF">
<option value="&entgroup=all">All Groups</option>
<?php 
	$sql = "select description,id from entity_groups where type='b' OR type='d'";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"&entgroup=".$row1['id']."\">".$row1['description']."</option>";
	}	
?>
</select> 
<?php if ($this->getVar('admin')==true) {?>
<BR>
User: <BR>
<select id="user" style="background-color: #FFFFFF">
<?php 
	$sql = "select username,id from users ";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		
		if ($row1['id'] == $this->getVar('userid'))
			echo "<option selected=\"selected\" value=\"&userid=".$row1['id']."\">".$row1['username']."</option>";
		else
			echo "<option value=\"&userid=".$row1['id']."\">".$row1['username']."</option>";
	}	
?>
</select> 
<?php } else {?>
<input type="hidden" id="user"  value=<?php echo "\"&userid=".$this->getVar('userid')."\"";  ?>>
<?php }?>
<BR><BR>
<input type="button" style="color: #000000;background-color: #FFFFFF" value="Refresh Table"	onclick="sendAndDraw();return false;"> <br />
<BR>
</form>
	<div style="clear: both;font-size:1.1em;">
	<BR>
	<!-- All Time Alert Counts:-->
	</div>
	<BR>



    
    <div id="chart-div"></div>
    <form action="">
 
  
  Number of Entities: <BR>
  <select id="number" style="background-color: #FFFFFF">
  	<option value="&limitcount=10">Top 10 Entities</option>   
  	<option value="&limitcount=20">Top 20 Entities</option>
    <option value="&limitcount=30">Top 30 Entities</option>
    <option value="&limitcount=40">Top 40 Entities</option> 
  </select>
  <BR>
  Time Frame: <BR>
	<select id="timeframe" style="background-color: #FFFFFF">
	<option value="&timeframe=all">All Time Frames</option>
	<option value="&timeframe=hour">Last Hour</option>	
	<option value="&timeframe=day">Last Day</option>
	<option value="&timeframe=week">Last Week</option>
	<option value="&timeframe=month">Last Month</option>
	<option value="&timeframe=year">Last Year</option>
	
</select> <BR>
  Alert Observation Period: <BR>
	<select id="observationperiod2" style="background-color: #FFFFFF">
	<option value="&timeeventid=all">All Periods</option>
	<?php 
	$sql = "select description,id from time_events ";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"&timeeventid=".$row1['id']."\">".$row1['description']."</option>";
	}	
	?>
	
	
</select> <BR>
Entity Group: <BR>
	<select id="entitygroup2" style="background-color: #FFFFFF">
	<option value="&entgroup=all">All Groups</option>
<?php 
	$sql = "select description,id from entity_groups where type='b' OR type='d'";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"&entgroup=".$row1['id']."\">".$row1['description']."</option>";
	}	
?>
	
	
</select> 
<?php if ($this->getVar('admin')==true) {?>
<BR>
User: <BR>
<select id="user2" style="background-color: #FFFFFF">
<?php 
	$sql = "select username,id from users ";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		if ($row1['id'] == $this->getVar('userid'))
			echo "<option selected=\"selected\" value=\"&userid=".$row1['id']."\">".$row1['username']."</option>";
		else
			echo "<option value=\"&userid=".$row1['id']."\">".$row1['username']."</option>";
	}	
?>
</select>
<?php } else {?>
<input type="hidden" id="user2"  value=<?php echo "\"&userid=".$this->getVar('userid')."\"";  ?>>
<?php }?>
<BR><BR>
<input type="button" style="color: #000000;background-color: #FFFFFF" value="Refresh Bar Chart"	onclick="sendAndDraw2();return false;"> <br />
<BR>
</form>
    
    <!-- <div id="dialog" title="Google">
    	<IFRAME style="border: 0px;" SRC="http://www.google.com" width="100%" height = "100%" ></IFRAME>
	</div> -->
	
	<!--  <div id="dialog" title="Basic dialog">
	<p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div> -->
	
    
</div>