<?php
require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();

/* 
 * This is pulled from the beginning of allassets/linechart.php which is now the model for how 
 * to handle initialization & processing for using url parameters with the auto-complete control.
 */

$ticker="";

if (isset ($_GET['e']))
	$entityid = $_GET['e'];
elseif (isset ($_GET['t']))
	$ticker = $_GET['t'];


if (empty($ticker) && !empty($entityid)) {

	$sql2 = "select distinct ticker from entities ";
	if (!empty($entityid)) {
		$sql2.=" where entities.id in (".$entityid.") ";
		$tmperror = "No entity ids were found.";
	}
	

	$result2 = mysql_query($sql2);
	$count=0;
	while ($row1 = mysql_fetch_array($result2)) {
		$ticker.=$row1['ticker'].",";
		$count++;
	}

	if ($count==0) {
		$error = $tmperror;
	}

	$ticker=substr($ticker,0,strlen($ticker)-1);


}
	





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
		document.firstPass = true;
		<?php 
			if (!empty($ticker))
				echo "document.ticker= '".$ticker."';\n";
		?>

		$("#a_c").val('<?php echo $ticker; ?>');
		
		$( "#a_c" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
					url: "../../site/ajax/getTicker.php",
					dataType: "json",
					data: {
						maxRows: 12,
						term: request.term,
						group: 1
					},
					success: function( data ) {
						//console.log(data);
						response( $.map( data, function( item ) {
							//alert(item.ticker);
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
			focus: function() {
				// prevent value inserted on focus
				return false;
			},
			select: function( event, ui ) {

				//alert($('#a_c').val());

				/*
					This next line is needed here or otherwise $('a_c').val() only returns the first letter.
				*/
				this.value = ui.item.value;
				document.ticker = ui.item.value;
				sendAndDraw();

			}
		});
		$("#a_c").keypress(function (e,ui) { 
        	if (e.which == 13) { 
            	//yourFunction($('#yourAutoComplete').val()); return false;
            	value = $("#a_c").val();
            	document.ticker = ui.item.value;
            	//window.id = ui.item.id;
      
            } 
        }); 
		

		<?php  	
	      /* If $ticker is set then url value (either e,t,eg or a) was passed in. */
	      //echo "//ticker: ".$ticker."\n";
			if (!empty($ticker)) {
				echo "google.setOnLoadCallback(function() { sendAndDraw(null) });\n";
			}
			        	
		?>
	        	  	
       	
		document.firstpass = false;
	});
	
    google.load('visualization', '1', {'packages' : ['corechart']});
    //google.setOnLoadCallback(function() { sendAndDraw(null) });

    





    function sendAndDraw() {
    	  var queryString1,queryString2;
    	  var title = document.getElementById('page-title');
          var description = document.getElementById('page-description');

          var query1;
          var query2;

          <?php 
                 
                  
          echo "var dataSourceUrl1 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource11.jsp';"; 
                  
          echo "var dataSourceUrl2 ='".IncFunc::$JSP_ROOT_PATH."/mysqldatasource12.jsp';";
          ?>


           //alert($('#a_c').val());
    
			
				

				var cleantickers = document.ticker;

				//alert(cleantickers);

	        	 if (cleantickers.charAt(cleantickers.length - 1) == ',')
	                 cleantickers = cleantickers.substr(0,cleantickers.length-1);


				var url = "../../site/ajax/getEntityId.php?tickers=" + encodeURIComponent(cleantickers);
		   $.ajax({
				url: url,
				dataType: "json",
				success: function(data) {
					$.map( data, function( item ) {
	
					var tmptext = item.ticker;
					if (item.full_name) {
						tmptext += " - " + item.full_name;
					}
					$('<li>').text(tmptext).appendTo('#chart-legend');
				

				});
					
				}

            });
      var container1 = document.getElementById('chart1');
      var container2 = document.getElementById('chart2');

      queryString1 = queryString2 = '?ticker=' + encodeURIComponent(cleantickers);

      container1.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";


      container2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

  	  
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
     //var timeframe = document.getElementById('timeframe');
      //var userid= users.value;
      //var taskid='1';
    

  
		var options = {};
	    options.height = 400;
	    options.width = 800;
	    options.colors = ['red','blue'];
	    //colors:['red','#004411'].
	    options.hAxis = {};
	   	options.hAxis.title = 'Calendar Quarter (format YYYYQ)';
		options.backgroundColor = {};
		options.backgroundColor.fill = 'white';
		options.backgroundColor.stroke = '#000';
     	options.backgroundColor.strokeWidth = 6;
     	options.enableInteractivity = true;
     	options.pointSize = 4;

		var options2 = {};
		options2.height = 400;
		options2.width = 800;
		options2.colors = ['orange','red','blue'];
		options2.hAxis = {};
		options2.hAxis.title = 'Calendar Quarter (format YYYYQ)';
		options2backgroundColor = {};
		options2backgroundColor.fill = 'white';
		options2backgroundColor.stroke = '#000';
     	options2backgroundColor.strokeWidth = 6;
     	options2.enableInteractivity = true;
     	options2.pointSize=4;
     	options2.vAxis = {};
     	options2.vAxis.format = '#%';
      
      var chart1 = new google.visualization.LineChart(container1);
      var chart2 = new google.visualization.LineChart(container2);

      //if (window.console) {console.log(dataSourceUrl1 + queryString1);}
      //if (window.console) {console.log(dataSourceUrl2 + queryString2);}


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

    function viewDataTable() {

		var url = "<?php echo IncFunc::$PHP_ROOT_PATH;?>";
		url += "/charts/equities/epsdetailtable.php?t=" + document.ticker;
		window.open(url);
    	<?php //echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/equities/epsdetailtable.php\");";?>
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
<div id="pf-body">




<br/>
	<div id="pf-form">
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
<input type="button"  value="View Data Table" style="float: left;clear: both;margin-bottom: 10px;"	onclick="viewDataTable();return false;"> <br />

</div> <!--  pf-body -->

</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
