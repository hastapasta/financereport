<?php
require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();

//$taskid = $_GET['taskid'];

if (isset($_GET['entityid']))
	$entityid = $_GET['entityid'];
else
	die("No entity id parameter in url. Unable to render chart.");
	
$sql = "select entities.* from entities where id=".$entityid;
$result = mysql_query($sql);

$row = mysql_fetch_array($result);




?>

<!DOCTYPE html>
<html>
<head>
<?php IncFunc::icon();?>
<link rel="stylesheet" href="../../site/includes/style.css"	type="text/css" />
<?php IncFunc::yuiDropDownJavaScript(); ?>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="../../site/includes/querywrapper.js"></script>
<script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['linechart']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
            //echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2.jsp';";
            
            echo "var dataSourceUrl1 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource11.jsp';"; 
            
            echo "var dataSourceUrl2 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource12.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
    var query2;



    function sendAndDraw() {

      var container1 = document.getElementById('chart1');
      var container2 = document.getElementById('chart2');

    
      container1.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";


      container2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

  	  
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
     //var timeframe = document.getElementById('timeframe');
      //var userid= users.value;
      //var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true)
      {
          //taskid='0';
          //timeframe.value='week';
          firstpass=false;
      }
      
      var options = {};
      options['height'] = 400;
      options['width'] = 800;
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	echo "queryString1 = '?entityid=".$entityid."';\n";

     	echo "queryString2 = '?entityid=".$entityid."';\n";
     	
     	
     	?>
    	 

 

      //alert(dataSourceUrl1 + queryString1);

      //alert(dataSourceUrl2 + queryString2);



      
      var chart1 = new google.visualization.LineChart(container1);
      var chart2 = new google.visualization.LineChart(container2);

      var title = document.getElementById('page-title');
	  <?php echo "title.innerHTML ='Ticker: ".$row['ticker']."';"; ?> 
      var description = document.getElementById('page-description');
      <?php echo "description.innerHTML ='".$row['full_name']."';"; ?> 

     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl1 + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, chart1, options, container1);
      queryWrapper1.sendAndDraw();

      query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl2 + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, chart2, options, container2);
      queryWrapper2.sendAndDraw();

    }

  </script>
</head>

<body style="text-align:left;">
<div id="jq-siteContain" >
<?php 
	IncFunc::header1("charts"); 
	//echo "<div id=\"yuipadding\" style=\"paddin>";
	IncFunc::yuiDropDownMenu();
	//echo "</div>";

?>




<br/>
<div id="pf-form" style="text-align:left;font-size:1.5em;">
</div><!-- pf-form -->



<div style="font-size:25px;margin: 10px 0 0 0;" id="page-title"></div>
<BR>
<div style="font-size:15px" id="page-description"></div>
<BR><BR>
<div id="tmp1" style="float: left;margin-bottom: 20px">
<div id="tmp1A" style="font-size: small">EPS acutals and estimates (values):</div>
<div id="chart1" style="margin: 10px 0 0 0"> </div>
</div>

<div id="tmp2" style="float: left;clear: left;margin-bottom: 20px">
<div id="tmp2A" style="font-size: small">EPS actuals, EPS estimates and share price (% change):</div>
<div id="chart2" style="margin: 10px 0 0 0"> </div>
</div>




</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
