<?php
require_once '../../common/functions.php';
include ("../../site/includes/sitecommon.php");

db_utility::db_connect();




?>

<!DOCTYPE html>
<html>
<head>
<?php IncFunc::icon();?>
<link rel="stylesheet" href="/PHP/site/includes/style.css" type="text/css" />
  <?php IncFunc::yuiDropDownJavaScript(); ?>
  <script type="text/javascript" src="https://www.google.com/jsapi"></script>
  <script type="text/javascript" src="/PHP/charts/querywrapper.js"></script>
  <script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['table']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = 'true';

    <?php 
            echo "var dataSourceUrl = '".db_utility::$datasourceurl."mysqldatasource3.jsp';";
    ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query;

    function loadspinner()
    {
    	var chart1 = document.getElementById('orgchart');
        chart1.innerHTML="<img src=\"http://www.google.com/ig/images/spinner.gif\" />";
        //chart1.innerHTML="The chart is loading...";
        chart1.style.display='none';
        chart1.style.display='block';
    }
    



    function sendAndDraw(queryString) {

    	
     
      var users = document.getElementById('users');
      var tasks = document.getElementById('tasks');
      var timeeventid = document.getElementById('timeeventid');
      var userid= users.value;
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);

      if (firstpass=='true')
      {
          userid='0';
          firstpass='false';
      }

      

     	 queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
 

      //alert(dataSourceUrl + queryString1);
      
      var container = document.getElementById('orgchart');
      var tableChart = new google.visualization.Table(container);
      query && query.abort();
      query = new google.visualization.Query(dataSourceUrl + queryString1);
      query.setTimeout(120);
      var queryWrapper = new QueryWrapper(query, tableChart, {'size': 'large'}, container);
      queryWrapper.sendAndDraw();
    }

  </script>
</head>

<body>
<div id="jq-siteContain">

<?php 
	IncFunc::header1("charts"); 
	IncFunc::yuiDropDownMenu();

?>

<!-- <div id="jq-whosUsing"> -->

<div id="pf-form" style="text-align:left;font-size:1.5em">
<form action="">

  <br/><br/>
  		Observation Period: <BR>
    <select id="timeeventid">
   
         <option value="all">all</option>
        <?php 
        $query1 = "select id,name from time_events";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
        
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['name']."&nbsp&nbsp</option>";
			
		}
        
        ?>
        </select>
        
        <BR><BR>
         Users: <BR>
         <select id="users">
     
         <option value="all">all</option>
         
        <?php 
        $query1 = "select id,username from users";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        

		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['username']."&nbsp&nbsp</option>";
			
		}
        
        ?>
        </select>
        
              <BR><BR>
         Task names: <BR>
         <select id="tasks">
         
   
         <option value="all">all</option>
        <?php 
        $query1 = "select id,name from tasks";
        $result1 = mysql_query($query1) or die("Failed Query of " . $query1);
        
  
		for ($j=0;$j<mysql_num_rows($result1);$j++)
		{
			$row1 = mysql_fetch_array($result1);
			echo "<option value=\"".$row1['id']."\">".$row1['name']."&nbsp&nbsp</option>";
			
		}
        
        ?>
        </select>
        
        <br/><br/>
        
        <input type="button" value="Display Table"
	onclick="sendAndDraw();return false;"> <br />
       </form>
       </div> <!-- pf-form -->

<br/>
<div id="orgchart" style="color: #000"></div>
<!-- </div> --> <!-- who's using -->
</div> <!--  siteContain -->
</body>
</html>
