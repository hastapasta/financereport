<script type="text/javascript">

		
		
		
        google.load('visualization', '1', {packages: ['motionchart']});
        google.setOnLoadCallback(function() { loadChart()});
        
        var dataSourceUrl1 = jsp_root_path + '/mysqldatasource19.jsp?type=total';


        motion_chart1 = null;
        motion_chart2 = null;
        motion_chart3 = null;
        
        var query1;
        var query2;
        var query3;
        var count = 0;
        
        var firstpass =false;

        function loadChart() {

        	 var container1 = document.getElementById('chart1-div');
   

             container1.innerHTML="<img src=\"../../site/images/spinner3-bluey.gif\" />";
 

      	

      	
      
      
          //var str = 'select ticker,calyear,value where ((calyear=2010 or calyear=2011 or calyear=2012) and ticker=\'WMT\') group by ticker, calyear limit 30 format calyear "%d"';
         // alert('count:' + count);
			
         
          //var query = new google.visualization.Query('http://localhost:8080/JSPDataSource/mysqldatasource7.jsp?taskid=22');
          
          query1 = new google.visualization.Query(dataSourceUrl1);
          
    

          
   

          var options = {};

          options['height'] = 600;
          options['width'] = 800;

          options.state = {};

          options['state'] = '{"showTrails":false,"iconType":"VBAR","orderedByY":false,"dimensions":{"iconDimensions":["dim0"]},"yZoomedDataMin":-7,"xZoomedDataMin":0,"xZoomedIn":false,"yAxisOption":"2","xLambda":1,"duration":{"timeUnit":"Y","multiplier":1},"time":"2015","yLambda":1,"xZoomedDataMax":182,"iconKeySettings":[],"colorOption":"_UNIQUE_COLOR","orderedByX":true,"yZoomedIn":false,"playDuration":15000,"sizeOption":"_UNISIZE","xAxisOption":"2","uniColorForNonSelected":false,"nonSelectedAlpha":0.4,"yZoomedDataMax":120}';
          	            

          
      
          query1.send(
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
                  if(motion_chart1 === null)
                  {
		     		//	alert('here 2.1');
                  	 motion_chart1 = new google.visualization.MotionChart(container1);
                  	 //alert('here 2.5');
                 	}
		   			//alert('here 2.7');
                  //motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800, 'interpolateNulls': false});
                  motion_chart1.draw(res.getDataTable(), options);
                  //alert('here 3');
              }
          });

         /* query2.send(
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
                        if(motion_chart2 === null)
                        {
      		     			//alert('here 2.1');
                        	 motion_chart2 = new google.visualization.MotionChart(container2);
                        	 //alert('here 2.5');
                       	}
      		   			//alert('here 2.7');
                        //motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800, 'interpolateNulls': false});
                        motion_chart2.draw(res.getDataTable(), options);
                        //alert('here 3');
                    }
                });

          query3.send(
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
                        if(motion_chart3 === null)
                        {
      		     			//alert('here 2.1');
                        	 motion_chart3 = new google.visualization.MotionChart(container1);
                        	 //alert('here 2.5');
                       	}
      		   			//alert('here 2.7');
                        //motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800, 'interpolateNulls': false});
                        motion_chart3.draw(res.getDataTable(), options);
                        //alert('here 3');
                    }
                });*/

        }

 function sendAndDraw() {

	  if (firstpass == true)
	  {
	  	firstpass = false;
	  	return;
	  }

    




      //alert("here 1");
	  //alert("firstpass: " + firstpass);


      
      var options = {};
		options.height = 600;
		options.width = 800;
		options.chartArea = {};
		options.chartArea.left = 100;
		options.chartArea.top = 50;
		options.backgroundColor = {};
		options.backgroundColor.fill = 'white';
		options.backgroundColor.stroke = '#000';	
		options.backgroundColor.strokeWidth = 8;
		options.hAxis = {};
		options.hAxis.slantedText = true;
     	options.hAxis.slantedTextAngle = 90;
     	options.pointSize = 5;
     	options.interpolateNulls = true;
     	options.title = 'Total Task Execution Time'; 
     	options.vAxis = {};  
     	options.vAxis.title = 'Time in Minutes';
     	//options.vAxis.baseline = 900;
     	//options.vAxis.baselineColor = "red";
     	
     	   var options2 = {};
		options2.height = 600;
		options2.width = 800;
		options2.chartArea = {};
		options2.chartArea.left = 100;
		options2.chartArea.top = 50;
		options2.backgroundColor = {};
		options2.backgroundColor.fill = 'white';
		options2.backgroundColor.stroke = '#000';	
		options2.backgroundColor.strokeWidth = 8;
		options2.hAxis = {};
		options2.hAxis.slantedText = true;
     	options2.hAxis.slantedTextAngle = 90;
     	options2.pointSize = 5;
     	options2.interpolateNulls = true;
     	options2.title = 'Data Capture Time'; 
     	options2.vAxis = {}; 
     	options2.vAxis.title = 'Time in Minutes';

 	   var options3 = {};
		options3.height = 600;
		options3.width = 800;
		options3.chartArea = {};
		options3.chartArea.left = 100;
		options3.chartArea.top = 50;
		options3.backgroundColor = {};
		options3.backgroundColor.fill = 'white';
		options3.backgroundColor.stroke = '#000';	
		options3.backgroundColor.strokeWidth = 8;
		options3.hAxis = {};
		options3.hAxis.slantedText = true;
    	options3.hAxis.slantedTextAngle = 90;
    	options3.pointSize = 5;
    	options3.interpolateNulls = true;
    	options3.title = 'Alert Processing Time'; 
    	options3.vAxis = {}; 
    	options3.vAxis.title = 'Time in Minutes';

     	
     
   
      
      
      


   
      
      var container1 = document.getElementById('chart1-div');
      var container2 = document.getElementById('chart2-div');
      var container3 = document.getElementById('chart3-div');
      
      container1.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
      container2.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
      container3.innerHTML="<img src=\"/PHP/site/images/spinner3-bluey.gif\" />";
      //var container2 = document.getElementById('orgchart2');
     
      
      var lineChart1 = new google.visualization.LineChart(container1);
      var lineChart2 = new google.visualization.LineChart(container2);
      var lineChart3 = new google.visualization.LineChart(container3);
      //var tableChart2 = new google.visualization.Table(container2);
      
      //alert(dataSourceUrl1);
     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, lineChart1, options, container1);
      queryWrapper1.sendAndDraw();

     
      query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl2);
      query2.setTimeout(220);
      var queryWrapper2 = new QueryWrapper(query2, lineChart2, options2, container2);
      queryWrapper2.sendAndDraw();

   
      query3 && query3.abort();
      query3 = new google.visualization.Query(dataSourceUrl3);
      query3.setTimeout(320);
      var queryWrapper3 = new QueryWrapper(query3, lineChart3, options3, container3);
      queryWrapper3.sendAndDraw();

      /*query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2);
      queryWrapper2.sendAndDraw();*/
    }

  </script>
<div class="users index">

<?php echo $this->element('actions',array('title'=>'Chart')); ?>

<?php echo $this->element('chart_actions')?>

<BR>


    <div id="chart1-div" style='clear:both'> </div>
 
</div>