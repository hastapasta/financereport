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
<?php IncFunc::title();?>
<?php IncFunc::linkStyleCSS();?> 
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget(); ?>
<?php IncFunc::jQuery();?>   
<script type="text/javascript">

	$(function(){
		$( "#a_c" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
					url: "../../site/includes/getTicker.php",
					dataType: "json",
					data: {
						maxRows: 12,
						term: request.term,
						group: 1
					},
					success: function( data ) {
						//console.log(data);
						response( $.map( data, function( item ) {
							//alert(item.id);
							return {
								
								value: item.ticker,
								label: item.ticker,
								id: item.id,
								full_name: item.full_name
		
							}
	
						}));
					}
				});
			},
			minLength: 1,
			select: function( event, ui ) {
				if(ui.item)
				{
					//loadChart("",  ui.item.value);
					//alert('value:' + ui.item.value);
					//var tmp[] = split(ui.item.value,'|');
					//$( "#a_c" ).val(ui.item.value.split("|")[1]);
					//sendAndDraw(ui.item.value.split("|")[0],ui.item.value.split("|")[1],ui.item.value.split("|")[2]);
					sendAndDraw(ui.item.id,ui.item.label,ui.item.full_name);
				}
			}
		});
	});
	
    google.load('visualization', '1', {'packages' : ['corechart']});
    google.setOnLoadCallback(function() { sendAndDraw(null) });


    <?php 
            //echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2.jsp';";
            
            echo "var dataSourceUrl1 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource11.jsp';"; 
            
            echo "var dataSourceUrl2 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource12.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
    var query2;



    function sendAndDraw(id,ticker,fullname) {

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

  
      var options = {};
      options.height = 400;
      options.width = 800;
      options.colors = ['red','blue'];
      //colors:['red','#004411'].
      options.hAxis = {};
   	  options.hAxis.title = 'Calendar Quarter (format YYYYQ)';

   	 var options2 = {};
     options2.height = 400;
     options2.width = 800;
     options2.colors = ['orange','red','blue'];
     options2.hAxis = {};
  	  options2.hAxis.title = 'Calendar Quarter (format YYYYQ)';
      
      var title = document.getElementById('page-title');
      var description = document.getElementById('page-description');

     	if(id != undefined){

			queryString1 = '?entityid=' + id;

			queryString2 = '?entityid=' + id;

			title.innerHTML = 'Ticker: ' + ticker;

			description.innerHTML = fullname;

			

			//alert(ticker);
		}
		else
		{
			/*queryPath += <?php //echo "'&entityid=".$row[id]."';"; ?>*/
			<?php //echo "options.title='".$row['ticker']." - ".$row['full_name']."';"; ?>
					
			<?php echo "queryString1 = '?entityid=".$entityid."';\n";?>

	     	<?php echo "queryString2 = '?entityid=".$entityid."';\n"; ?>


	  	  <?php echo "title.innerHTML ='Ticker: ".$row['ticker']."';"; ?> 

	        <?php echo "description.innerHTML ='".$row['full_name']."';"; ?> 
		}

     	
  
    	 

 

     	 if (window.console && window.console.firebug) {console.log(dataSourceUrl1 + queryString1)}

      //alert(dataSourceUrl2 + queryString2);



      
      var chart1 = new google.visualization.LineChart(container1);
      var chart2 = new google.visualization.LineChart(container2);



     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl1 + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, chart1, options, container1);
      queryWrapper1.sendAndDraw();

      query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl2 + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, chart2, options2, container2);
      queryWrapper2.sendAndDraw();

    }

  </script>
</head>

<body style="text-align:left;">
<div id="jq-siteContain" >
<?php 
	IncFunc::header2("charts"); 
	//echo "<div id=\"yuipadding\" style=\"paddin>";
	IncFunc::yuiDropDownMenu();
	//echo "</div>";

?>




<br/>
	<div id="pf-form" style="margin:20px 0 0 0;font-size:15px;">
    Enter stock ticker:
    <BR>
  	<input type='text' id='a_c' /><br/>
  
  
	<!-- <div style="font-size:30;margin: 10px 0 0 0;" id="chart-title"></div> -->
	</div>
	<BR>



<div style="font-size:25px;margin: 10px 0 0 0;" id="page-title"></div>
<BR>
<div style="font-size:15px" id="page-description"></div>
<BR><BR>
<div id="tmp1" style="float: left;margin-bottom: 20px">
<div id="tmp1A" style="font-size: small">EPS actuals and estimates (values):</div>
<div id="chart1" style="margin: 10px 0 0 0"> </div>
</div>

<div id="tmp2" style="float: left;clear: left;margin-bottom: 20px">
<div id="tmp2A" style="font-size: small">EPS actuals, EPS estimates and share price (% change):</div>
<div id="chart2" style="margin: 10px 0 0 0"> </div>
</div>

<div id="atl1" style="margin: 10px 0 0 0"> </div>


</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
