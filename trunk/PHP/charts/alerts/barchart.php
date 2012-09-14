<?php
//require_once 'init2.php';

require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");





?>
<html>
<head>
	<?php IncFunc::icon();?>
	<?php IncFunc::title();?>
    
    <?php IncFunc::linkStyleCSS();?>
    <?php //IncFunc::yuiDropDownJavaScript(); ?>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
		var count = 0;
	

 
        google.load('visualization', '1', {packages: ['corechart']});
        google.setOnLoadCallback(loadChart);
        bar_chart = null;
        var options = {};

        

        
        function loadChart() {
        	
         	//alert('here 1');
            //var metric1 = document.getElementById('metric-1').value;
      		count++;
         	var str = '' + count;

         	<?php //echo "options['title']='".$row['full_name']."';";?>
         	
   
         	options['height'] = 600;
         	options['width'] = 900;
         	/*options['fontSize'] = 1;
         	options['curveType'] = 'function';
         	options['pointSize'] = 0;*/
         	//options['legend'] = 'none';
         
 			
           
            <?php echo "var query = new google.visualization.Query('".IncFunc::$JSP_ROOT_PATH."/mysqldatasource9.jsp?userid=16');"; ?>
            
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
                    if(bar_chart === null)
                    {
		     			//alert('here 2.1');
                    	 bar_chart = new google.visualization.ColumnChart(document.getElementById('chart-div'));
                    	 //alert('here 2.5');
                   	}
		   			
                    
                     
                    
                     bar_chart.draw(res.getDataTable(), options);
                   
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>
<div id="jq-siteContain">

<?php 
	IncFunc::header1("charts"); 
	IncFunc::apycomDropDownMenu();
  
?>
	<div id="pf-form">
	<form action="">

 	<select id="limitcount">
 	<option value="50">50</option>
 	<option value="40">40</option>
 	<option value="30">30</option>
 	<option value="20">20</option>	
 	<option value="10">10</option>
 	</select>
 	
 	<input type="button" value="Redraw Chart"	onclick="window.location.reload();return false;">
    </form>
 

  
	<div style="font-size:20" id="chart-title"></div>
	<BR/>
	<div style="font-size:15" id="chart-description"></div>
    <div id="chart-div"></div>

 
    

</div> <!--  siteContain -->  
</body>
</html>
