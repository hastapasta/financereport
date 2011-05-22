<?php //debug($alert['Task'],true);?>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
	

 
        google.load('visualization', '1', {packages: ['corechart','table','annotatedtimeline']});
        google.setOnLoadCallback(sendAndDraw);
        line_chart = null;
       
        
        var query1;
        var query2;
        var query3;

        function sendAndDraw() {

            var chart = document.getElementById('chart-div');
            chart.innerHTML="<img src=\"" + php_root_path + "/site/images/spinner3-bluey.gif\" />";

            var chart2 = document.getElementById('barchart-div');
            chart2.innerHTML="<img src=\"" + php_root_path + "/site/images/spinner3-bluey.gif\" />";

            var chart3 = document.getElementById('table-div');
            chart3.innerHTML="<img src=\"" + php_root_path + "/site/images/spinner3-bluey.gif\" />";


            var options = {};
            options.scaleType = "maximized";
            var options2 = {};
            var options3 = {};
        
 
            




           	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
           	<?php 
           		//echo "dataSourceUrl1 = jsp_root_path + '/mysqldatasource1.jsp?entityid=".$alert['Alert']['entity_id']."&metricid=".$alert['Task']['metric_id']."&begindate=2011-01-01';\n";
           		
           		echo "dataSourceUrl1 = jsp_root_path + '/mysqldatasource15.jsp';\n";
           		echo "dataSourceUrl1 += '?entityid=".$alert['Alert']['entity_id']."';\n";
           		echo "dataSourceUrl1 += '&userid=".$alert['Alert']['user_id']."';\n";
           		echo "dataSourceUrl1 += '&metricid=".$alert['Task']['metric_id']."';\n";
           		echo "dataSourceUrl1 += '&begindate=2011-01-01';\n";
           		
           		//echo "console.log(dataSourceUrl1);\n";
           	
           		//echo "options.title='".$alert['Entity']['ticker']." - ".$alert['Entity']['full_name']." - ".$this->getVar('metric_name')."';";

           		echo "dataSourceUrl2 = jsp_root_path + '/mysqldatasource16.jsp?alertid=".$alert['Alert']['id']."';\n";
           		
           		//echo "dataSourceUrl2 = jsp_root_path + '/mysqldatasource16.jsp?alertid=".$alert['Alert']['id']."';\n";
           	
				echo "dataSourceUrl3 = jsp_root_path + '/mysqldatasource17.jsp?alertid=".$alert['Alert']['id']."';\n";
           	
           	?>

           	if (window.console && window.console.firebug) {console.log(dataSourceUrl1)}
           	if (window.console && window.console.firebug) {console.log(dataSourceUrl2)}
           	if (window.console && window.console.firebug) {console.log(dataSourceUrl3)}

 

           	//alert(dataSourceUrl2);

            


         	/*options['fontSize'] = 15;
         	options['curveType'] = 'none';
         	options['pointSize'] = 0;
         	options.interpolateNulls = true;
         	options.chartArea = {};
			options.chartArea.left = 100;
      		options.chartArea.top = 50;*/

      		options.displayAnnotations = true;
      		options.allowHtml = true;
      		options.annotationsWidth = 25;

      		options2.title='Alert Count by Month';
      		options2['height'] = 600;
         	options2['width'] = 900;
         	options2.chartArea = {};
			options2.chartArea.left = 100;
      		options2.chartArea.top = 50;

			/*options.backgroundColor = {};
			options.backgroundColor.fill = 'white';
			options.backgroundColor.stroke = '#000';
         	options.backgroundColor.strokeWidth = 8;*/

			options2.backgroundColor = {};
			options2.backgroundColor.fill = 'white';
			options2.backgroundColor.stroke = '#000';
         	options2.backgroundColor.strokeWidth = 8;
          	 

       

            //alert(dataSourceUrl + queryString1);

            //alert(dataSourceUrl + queryString2);

            //alert(dataSourceUrl + queryString3);
            
            var container1 = document.getElementById('chart-div');
            var container2 = document.getElementById('barchart-div');
            var container3 = document.getElementById('table-div');

           
            
            var chart1 = new google.visualization.AnnotatedTimeLine(container1);
            var chart2 = new google.visualization.ColumnChart(container2);
            var chart3 = new google.visualization.Table(container3);


            /*google.visualization.events.addListener(tableChart1, 'select', function(event){
      			var row = tableChart1.getSelection();
      			alert( "you selected row " + row[0].row + " of first table");
             });
      		
            google.visualization.events.addListener(tableChart2, 'select', function(event){
      	  		var row = tableChart2.getSelection();
      			alert( "you selected row " + row[0].row + " of second table");
         	   });
            google.visualization.events.addListener(tableChart3, 'select', function(event){
      	  		var row = tableChart3.getSelection();
      			alert( "you selected row " + row[0].row + " of third table");
         	  });*/
            
            query1 && query1.abort();
            query1 = new google.visualization.Query(dataSourceUrl1);
            query1.setTimeout(120);
            var queryWrapper1 = new QueryWrapper(query1, chart1, options, container1);
            queryWrapper1.sendAndDraw();

            query2 && query2.abort();
            query2 = new google.visualization.Query(dataSourceUrl2);
            query2.setTimeout(120);
            var queryWrapper2 = new QueryWrapper(query2, chart2, options2, container2);
            queryWrapper2.sendAndDraw();

            query3 && query3.abort();
            query3 = new google.visualization.Query(dataSourceUrl3);
            query3.setTimeout(120);
            var queryWrapper3 = new QueryWrapper(query3, chart3, options3, container3);
            queryWrapper3.sendAndDraw();


          }

        

      
    </script>
<div class="alerts view">
<?php //echo $this->element('actions',array('title'=>'Alert')); ?>

<div style='clear:both'></div>

<dl>
<?php //$this->set('jsIncludes',array('google'));   // this will link to /js/google.js ?>
<?php $i = 0; $class = ' class="altrow"';?>

	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Ticker'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['ticker']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Description'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Entity']['full_name']; ?>
	<?php //echo "<div id='tickerval' style=\"display: none\">".$alert['Entity']['ticker']."</div>"?>
	<?php //echo "<div id='taskidval' style=\"display: none\">".$alert['Schedule']['task_id']."</div>"?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Task Name'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php if(isset($alert['Task']['name']))
	echo $alert['Task']['name'];
	?> &nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Type'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['type']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('User'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['User']['username']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Observation Period'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['TimeEvent']['name']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Limit Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['limit_value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Initial Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Current Value'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['value']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Initial Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['InitialFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Current Value Date'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['CurrentFactDatum']['date_collected']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Notification Count'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['notification_count']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Fired'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['fired']; ?>
	&nbsp;</dd>
	<dt <?php if ($i % 2 == 0) echo $class;?>><?php __('Disabled'); ?></dt>
	<dd <?php if ($i++ % 2 == 0) echo $class;?>><?php echo $alert['Alert']['disabled']; ?>
	&nbsp;</dd>
</dl>
<BR>
 	<STYLE>
	.goog-custom-button-inner-box{
	-moz-box-orient:vertical;
	margin:0 0px;
	border-width:0 0px;
	padding:0px 0px;
	white-space:nowrap;
	}
	table tr td {
	padding:0;
	} </STYLE>
<div id="chart-div" style='width: 700px; height: 500px;'></div>

<BR>
<div id="tmptext" style="font-size:1.2em;margin: 0 0 10px 0;">
Fired Alerts History:
</div>
<div id="table-div"></div>
<BR>
<div id="barchart-div"></div>

</div>

