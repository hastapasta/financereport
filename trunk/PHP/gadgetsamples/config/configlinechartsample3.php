<?php
//require_once 'init2.php';
include ("../../site/includes/sitecommon.php");





?>
<html>
<head>
    <title>Config Line Chart Sample</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript" src="../../site/includes/querywrapper.js"></script>
    <?php IncFunc::incJquery(); ?>
    <script type="text/javascript">
		var count = 0;
		//alert('here .5');

 
        google.load('visualization', '1', {packages: ['corechart']});
        google.setOnLoadCallback(function() { sendAndDraw('') });
        //google.setOnLoadCallback(loadChart);
        line_chart = null;
        var options = {};
        var query;
        var firstpass = true;
        
        
        
        function setValues()
        {
          	var prop1 = document.getElementById("prop1");
          	$("#prop1").val("in");
          	      
          	//Select_Value_Set(prop1,'in');
                	
            var prop2 = document.getElementById("prop2");
            var prop3 = document.getElementById("prop3");
            var prop4 = document.getElementById("prop4");
            var prop5 = document.getElementById("prop5");
            var prop6 = document.getElementById("prop6");
            var prop7 = document.getElementById("prop7");
            var prop8 = document.getElementById("prop8");
            var prop9 = document.getElementById("prop9");
            var prop10 = document.getElementById("prop10");
            var prop11 = document.getElementById("prop11");
            var prop12 = document.getElementById("prop12");
            var prop13 = document.getElementById("prop13");
            var prop14 = document.getElementById("prop14");
            var prop15 = document.getElementById("prop15");
            var prop16 = document.getElementById("prop16");
            var prop17 = document.getElementById("prop17");
            var prop18 = document.getElementById("prop18");
            var prop19 = document.getElementById("prop19");
            var prop20 = document.getElementById("prop20");
            var prop21 = document.getElementById("prop21");
            var prop22 = document.getElementById("prop22");
            var prop23 = document.getElementById("prop23");
            var prop24 = document.getElementById("prop24");
            var prop25 = document.getElementById("prop25");
            var prop26 = document.getElementById("prop26");
            var prop27 = document.getElementById("prop27");
            var prop28 = document.getElementById("prop28");
            var prop29 = document.getElementById("prop29");
            var prop30 = document.getElementById("prop30");
            var prop31 = document.getElementById("prop31");
            var prop32 = document.getElementById("prop32");
            var prop33 = document.getElementById("prop33");
            var prop34 = document.getElementById("prop34");
            var prop35 = document.getElementById("prop35");
            var prop36 = document.getElementById("prop36");
        
        
        
        
        }


              
    
        
        function sendAndDraw() {

        	   if (firstpass==true)
               {
                   //taskid='0';
                   firstpass=false;
                   return;
               }
	
             var container1 = document.getElementById('chart-div');
             container1.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
            
            var prop1 = document.getElementById("prop1");
            var prop2 = document.getElementById("prop2");
            var prop3 = document.getElementById("prop3");
            var prop4 = document.getElementById("prop4");
            var prop5 = document.getElementById("prop5");
            var prop6 = document.getElementById("prop6");
            var prop7 = document.getElementById("prop7");
            var prop8 = document.getElementById("prop8");
            var prop9 = document.getElementById("prop9");
            var prop10 = document.getElementById("prop10");
            var prop11 = document.getElementById("prop11");
            var prop12 = document.getElementById("prop12");
            var prop13 = document.getElementById("prop13");
            var prop14 = document.getElementById("prop14");
            var prop15 = document.getElementById("prop15");
            var prop16 = document.getElementById("prop16");
            var prop17 = document.getElementById("prop17");
            var prop18 = document.getElementById("prop18");
            var prop19 = document.getElementById("prop19");
            var prop20 = document.getElementById("prop20");
            var prop21 = document.getElementById("prop21");
            var prop22 = document.getElementById("prop22");
            var prop23 = document.getElementById("prop23");
            var prop24 = document.getElementById("prop24");
            var prop25 = document.getElementById("prop25");
            var prop26 = document.getElementById("prop26");
            var prop27 = document.getElementById("prop27");
            var prop28 = document.getElementById("prop28");
            var prop29 = document.getElementById("prop29");
            var prop30 = document.getElementById("prop30");
            var prop31 = document.getElementById("prop31");
            var prop32 = document.getElementById("prop32");
            var prop33 = document.getElementById("prop33");
            var prop34 = document.getElementById("prop34");
            var prop35 = document.getElementById("prop35");
            var prop36 = document.getElementById("prop36");


            
            alert(prop34.value);

			//these work
         	options.title = prop5.value;
         	options.height = prop2.value;
         	options.width = prop3.value;
         	options.lineWidth = parseInt(prop4.value);   
         	if (prop7.value == 'false')
             	options.reverseCategories = false;
         	else
         		options.reverseCategories = true;
     		
        	options.hAxis = {};
         	options.hAxis.title = prop6.value;
         	options.hAxis.titleTextStyle = {};
         	options.hAxis.titleTextStyle.color = '#FF0000';
         	options.hAxis.direction = prop20.value;
         	options.hAxis.textPosition = prop21.value;
         	options.hAxis.slantedText = prop22.value;
         	options.hAxis.slantedTextAngle = parseInt(prop23.value);
         	options.hAxis.maxAlteration = parseInt(prop24.value);
         	options.hAxis.showTextEvery = parseInt(prop25.value);

         	options.vAxis = {};
         	options.vAxis.title = prop8.value;
         	options.vAxis.titleTextStyle = {};
         	options.vAxis.titleTextStyle.color = '#FF0000';
         	options.vAxis.baseline = prop27.value;
         	options.vAxis.baselineColor = prop28.value;
         	options.vAxis.direction = prop29.value;
         	options.vAxis.format = prop30.value;
         	options.vAxis.logScale = prop31.value;
         	options.vAxis.textPosition = prop32.value;
         	options.vAxis.maxValue = prop33.value;
         	options.vAxis.minValue = prop34.value;
         	
         	

         	options.axisTitlesPosition = prop1.value;
         	options.titlePosition = prop9.value;

         	options.backgroundColor = {};
         	options.backgroundColor.fill = prop10.value;
         	options.backgroundColor.stroke = prop35.value;
         	options.backgroundColor.strokeWidth = prop36.value;

         	options.chartArea = {};
         	options.chartArea.left = prop11.value;
         	options.chartArea.top = prop12.value;
         	options.chartArea.width = prop13.value;
         	options.chartArea.height = prop14.value;


         	options.curveType = prop15.value;
         	
         	options.fontSize = prop16.value;
         	options.fontName = prop17.value;
         	
         	options.legend = prop18.value;
         	
         	options.pointSize = prop19.value;
         	
         	if (prop26.value == 'false')
             	options.interpolateNulls = false;
         	else
         		options.interpolateNulls = true;
10
         	
         	

     		//these don't        	    	
         	
         	//options.hAxis =  {title: 'Hello', titleTextStyle: {color: '#FF0000'}};
         


         	
         	
   


         	
         	
         	//options['legend'] = 'none';
         	
         	
		     
		     
		      
		    var lineChart1 = new google.visualization.LineChart(container1);


            query && query.abort();
            query = new google.visualization.Query('http://localhost:8080/JSPDataSource/mysqldatasource1.jsp?entityid=660,661&begindate=2011-01-01');
            query.setTimeout(120);
            var queryWrapper1 = new QueryWrapper(query, lineChart1, options, container1);
            queryWrapper1.sendAndDraw();

            
        }
    </script>
</head>
<body>


	height: 
	<input type="text" id="prop2" size="10" value="800">
	<BR>
	
	width: 
	<input type="text" id="prop3" size="10" value="800">
	<BR><BR>
	
	main title position: 
	<select id="prop9" style="background-color: #FFFFFF">
	<option value="in">in</option>
	<option value="out">out</option>
	<option value="none">none</option>	
	</select>
    <BR><BR>
	
	
    global axis titles position: 
	<select id="prop1" name="prop1" style="background-color: #FFFFFF">
	<option value="in">in</option>
	<option value="out">out</option>
	<option value="none">none</option>	
	</select>
    <BR><BR>
    
    line width: 
	<input type="text" id="prop4" size="10" value="2">
	<BR><BR>
	
	title: 
	<input type="text" id="prop5" size="30" value="This is the title">
	<BR><BR>
	
	Horizontal Axis: <BR>
	hAxis title: 
	<input type="text" id="prop6" size="30" value="hAxis title">
	<BR>
	hAxis direction: 
	<select id="prop20" style="background-color: #FFFFFF">
	<option value="1">1</option>
	<option value="-1">-1</option>
	</select>
	<BR>
	hAxis text position: 
	<select id="prop21" style="background-color: #FFFFFF">
	<option value="out">out</option>
	<option value="in">in</option>
	<option value="none">none</option>	
	</select>
	<BR>
	hAxis slant text: 
	<select id="prop22" style="background-color: #FFFFFF">
	<option value="true">true</option>
	<option value="false">false</option>
	<option value="automatic">automatic</option>	
	</select>
	<BR>
	h Axis slanted text angle (1 - 90):
	<input type="text" id="prop23" size="10" value="30">
	<BR>
	h Axis max text levels (aka max alteration):
	<input type="text" id="prop24" size="10" value="2">
	<BR>
	h Axis show text every (number or automatic):
	<input type="text" id="prop25" size="10" value="1">
	<BR><BR>
	
	Vertical Axis: <BR>
	vAxis title: 
	<input type="text" id="prop8" size="30" value="vAxis title">
	<BR>
	vAxis baseline (number or automatic):
	<input type="text" id="prop27" size="10" value="automatic">
	<BR>
	vAxis baseline color:
	<select id="prop28" style="background-color: #FFFFFF">
	<option value="black">black</option>
	<option value="red">red</option>
	<option value="white">white</option>
	<option value="blue">blue</option>
	<option value="green">green</option>
	<option value="yellow">yellow</option>
	</select>
	<BR>
	vAxis direction: 
	<select id="prop29" style="background-color: #FFFFFF">
	<option value="1">1</option>
	<option value="-1">-1</option>
	</select>
	<BR>
	vAxis format (e.g. #,###% or blank for auto. <A HREF='http://icu-project.org/apiref/icu4c/classDecimalFormat.html#_details'>ICU Pattern</A>):
	<input type="text" id="prop30" size="10" value="">
	<BR>
	vAxis log scale:
	<select id="prop31" style="background-color: #FFFFFF">
	<option value="false">false</option>
	<option value="true">true</option>
	</select>
	<BR>
	vAxis text position: 
	<select id="prop32" style="background-color: #FFFFFF">
	<option value="out">out</option>
	<option value="in">in</option>
	<option value="none">none</option>	
	</select>
	<BR>
	The max/min values are restricted by the vAxis baseline value.
	<BR>
	vAxis max value (number or automatic):
	<input type="text" id="prop33" size="10" value="automatic">
	<BR>
	vAxis min value (number or automatic):
	<input type="text" id="prop34" size="10" value="automatic">
	<BR><BR>
	
	reverse Categories (takes priority over hAxis direction): 
	<select id="prop7" style="background-color: #FFFFFF">
	<option value="false">false</option>
	<option value="true">true</option>
	</select>
	<BR><BR>
	
	background color properties:<BR>
	fill: 
	<select id="prop10" style="background-color: #FFFFFF">
	<option value="white">white</option>
	<option value="red">red</option>
	<option value="blue">blue</option>
	<option value="green">green</option>
	<option value="black">black</option>
	<option value="yellow">yellow</option>
	</select>
	<BR>
	border (as an html color string - #333):
	<input type="text" id="prop35" size="10" value="#000">
	<BR>
	border width:
	<input type="text" id="prop36" size="10" value="10">
	<BR><BR>
	
	chart area:<BR>
	left (e.g 20 or auto):
	<input type="text" id="prop11" size="10" value="100">
	<BR>
	top (e.g. 20 or auto):
	<input type="text" id="prop12" size="10" value="100">
	<BR>
	width (e.g. 75% or auto):
	<input type="text" id="prop13" size="10" value="60%">
	<BR>
	height (e.g. 75% or auto):
	<input type="text" id="prop14" size="10" value="60%">
	<BR><BR>
	
	curve type: 
	<select id="prop15" style="background-color: #FFFFFF">
	<option value="none">none</option>
	<option value="function">function</option>
	</select>
	<BR><BR>
	
	global font size:
	<input type="text" id="prop16" size="10" value="15">
	<BR>
	global font name:
	<input type="text" id="prop17" size="30" value="Arial">
	<BR><BR>
	
	legend position: 
	<select id="prop18" style="background-color: #FFFFFF">
	<option value="right">right</option>
	<option value="top">top</option>
	<option value="bottom">bottom</option>
	<option value="none">none</option>
	</select>
	<BR><BR>
	
	point size: 
	<input type="text" id="prop19" size="10" value="0">
	<BR><BR>
	
	interpolate nulls: 
	<select id="prop26" style="background-color: #FFFFFF">
	<option value="true">true</option>
	<option value="false">false</option>
	</select>
	<BR><BR>
	
    
 	<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Chart"	onclick="sendAndDraw();return false;"> <br />
 	
 	<input type="button" style="color: #000000;background-color: #FFFFFF" value="Set Values"	onclick="setValues();return false;"> <br />
    <BR>
  

    <div id="chart-div"></div>
  

 
    

  
</body>
</html>
