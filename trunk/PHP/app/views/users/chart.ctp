<?php //debug($this,true);?>
<script type="text/javascript">
		var count = 0;
		//alert('here .5');
		
		
		
        google.load('visualization', '1', {packages: ['corechart','table']});
        google.setOnLoadCallback(function() { sendAndDraw('') });

        <?php 
        	echo "var dataSourceUrl =  jsp_root_path + '/mysqldatasource9.jsp?userid=".$this->getVar('userid')."';\n";
        	
        	echo "var dataSourceUrl2 = jsp_root_path + '/mysqldatasource17.jsp?userid=".$this->getVar('userid')."';\n";
        	
        	//echo "var dataSourceUrl3 = jsp_root_path + '/mysqldatasource18.jsp?userid=".$this->getVar('userid')."';\n";

        ?>
        var query,query2;
        var options = {};
        var options2 = {};

   

              
    
        
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
         		

           	  
              var container = document.getElementById('chart-div');
              var container2 = document.getElementById('table-div');
             


              container.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
              container2.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
            
              
              var chart1 = new google.visualization.ColumnChart(container);
              var chart2 = new google.visualization.Table(container2);
             
              
              query && query.abort();
              //alert(dataSourceUrl + queryString);
              query = new google.visualization.Query(dataSourceUrl);
              var queryWrapper = new QueryWrapper(query, chart1, options, container);
              queryWrapper.sendAndDraw();

              query2 && query2.abort();
              //alert(dataSourceUrl + queryString);
              query2 = new google.visualization.Query(dataSourceUrl2);
              var queryWrapper2 = new QueryWrapper(query2, chart2, options2, container2);
              queryWrapper2.sendAndDraw();

          
            }
    </script>
<div class="users index">

<?php echo $this->element('actions',array('title'=>'Chart')); ?>
<?php echo $this->element('chart_actions')?>



	<div style="clear: both;font-size:.9em;">
	<BR>
	Most Recently Fired Alerts:
	</div>
	<div id="table-div"></div>
	<BR>
	All Time Alert Counts:
	<BR>

<form action="">
 
  <br /><br />
  <select onChange="sendAndDraw(this.value)">
  	<option value="&limitcount=10">Top 10 Entities</option>   
  	<option value="&limitcount=20">Top 20 Entities</option>
    <option value="&limitcount=30">Top 30 Entities</option>
    <option value="&limitcount=40">Top 40 Entities</option>
   
   
    
  </select>
</form>

    
    <div id="chart-div"></div>
</div>