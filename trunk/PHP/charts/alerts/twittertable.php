<?php

/* This file is set up intentionally to not return any data on the first pass until a 
 * user has had the chance to select the parameters.
 */


require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();




?>

<!DOCTYPE html>
<html>
<head>
<?php IncFunc::jQuery();?>
	<?php IncFunc::icon();?>
	<?php IncFunc::title();?>
<?php IncFunc::linkStyleCSS();?>
<?php IncFunc::googleGadget()?>
<script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['table']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
            echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource17.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query;

    function loadspinner()
    {
    	var chart1 = document.getElementById('orgchart');
        chart1.innerHTML="<img src=\"http://www.google.com/ig/images/spinner.gif\" />";
        //chart1.innerHTML="The chart is loading...";
        //chart1.style.display='none';
        //chart1.style.display='block';
    }
    



    function sendAndDraw() {

      var chart1 = document.getElementById('orgchart');
      chart1.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";
    	
     
      //var users = document.getElementById('users');
      var tasks = document.getElementById('tasks');
      var timeeventid = document.getElementById('timeeventid');
      //var userid= users.value;
      var userid='16';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass==true)
      {
          userid='0';
          firstpass=false;
      }


      

     	//queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	
     	queryString1 = '?pholder=val&obperiod=0&entgroup=all&userid=19';


		if (window.console && window.console.firebug) {console.log(dataSourceUrl + queryString1)}
 

      //alert(dataSourceUrl + queryString1);
      
      var container = document.getElementById('orgchart');
      var tableChart = new google.visualization.Table(container);
      query && query.abort();
      query = new google.visualization.Query(dataSourceUrl + queryString1);
      query.setTimeout(120);
      var queryWrapper = new QueryWrapper(query, tableChart, {'size': 'large'}, container,[10],4);
      queryWrapper.sendAndDraw();
    }

  </script>
</head>

<body>
<div id="jq-siteContain">
<?php 
	IncFunc::header2("charts"); 
	IncFunc::yuiDropDownMenu();

?>
<!-- header --> <!-- <div id="jq-whosUsing"> --></div>
<!--  siteContain -->




<div id="pf-body">
<BR/><BR/>
<div style="font-size:2.2em;font-weight:bold;margin-left:260px;">
Complete List of Twitter Alerts
</div>
<BR>


<br />
<div id="fontblack"><br />
<div id="orgchart" style="float: left;padding-bottom: 20px;"></div>
</div><!-- pf-body -->
</div> <!-- fontblack -->

</body>
</html>
