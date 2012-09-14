<?php
require_once '../../common/functions.php';
require_once ("../../site/includes/sitecommon.php");

db_utility::db_connect();



/*if (isset($_GET['taskid']))
	$taskid = $_GET['taskid'];
else
	die("No task id parameter in url. Unable to render chart.");
	

if (isset($_GET['userid']))
	$taskid = $_GET['userid'];
else
	die("No user id parameter in url. Unable to render chart.");
	

if (isset($_GET['timeeventid']))
	$taskid = $_GET['timeeventid'];
else
	die("No time event id parameter in url. Unable to render chart.");*/




?>

<!DOCTYPE html>
<html>
<head>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<?php IncFunc::linkStyleCSS();?>
<?php //IncFunc::yuiDropDownJavaScript(); ?>
<?php IncFunc::googleGadget()?>
<script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['table']});
    google.setOnLoadCallback(function() { sendAndDraw('') });
    var firstpass = true;

    <?php 
            echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."/mysqldatasource14.jsp';";
            ?>
        // var dataSourceUrl = 'http://www.pikefin.com/phpdev/gadgetsamples/echodatasource2.php';
           

   // var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query1;
   // var query2;
 


    



    function sendAndDraw() {

        if (firstpass==true)
        {
            //taskid='0';
            firstpass=false;
            return;
        }

      var chart = document.getElementById('table1');
      chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

      /*var chart2 = document.getElementById('orgchart2');
      chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
      
  	
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
      var userselect = document.getElementById('userselect');
      var taskselect = document.getElementById('taskselect');
      var teselect = document.getElementById('teselect');
      var firedcheck="";
      if (document.getElementById('fired').checked == true)
          firedcheck = "true";
      else
          firedcheck = "false";


      //alert(firedcheck);
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);


      
      var options = {};
      options['height'] = 600;
      options['width'] = 1000;
      
      queryString1 = '?taskid=' + taskselect.value + '&timeeventid=' + teselect.value + '&userid=' + userselect.value + '&fired=' + firedcheck;
      
      
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	//echo "queryString1 = '?taskid='+taskselect.value

     //	echo "queryString2 = '?taskid=".$taskid."&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

 

      alert(dataSourceUrl + queryString1);

     // alert(dataSourceUrl + queryString2);
      
      var container1 = document.getElementById('table1');
      //var container2 = document.getElementById('orgchart2');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      //var tableChart2 = new google.visualization.Table(container2);
     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1);
      queryWrapper1.sendAndDraw();

      /*query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2);
      queryWrapper2.sendAndDraw();*/
    }

  </script>
</head>

<body>
<div id="jq-siteContain" >
<?php 
	IncFunc::header2("charts"); 
	IncFunc::apycomDropDownMenu();

?>




<br/>


<div id="pf-form">

<BR>
User: <BR>
<select id="userselect" style="background-color: #FFFFFF">

<?php
	$sql = "select username,id from users";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"".$row1['id']."\">".$row1['username']."</option>";
	}	
?>
	
	<!-- <option value="Custom">Custom</option> -->
	<option value="all">All</option>
</select> <BR>
<BR>
Time Frame: <BR>
<select id="teselect" style="background-color: #FFFFFF">
<?php
	$sql = "select name,id from time_events";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"".$row1['id']."\">".$row1['name']."</option>";
	}	
?>
	
	<!-- <option value="Custom">Custom</option> -->
	<option value="all">All</option>
</select> <BR>
<BR>
Task: <BR>
<select id="taskselect" style="background-color: #FFFFFF">
<?php
	$sql = "select name,id from tasks";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"".$row1['id']."\">".$row1['name']."</option>";
	}	
?>
	
	<!-- <option value="Custom">Custom</option> -->
	<option value="all">All</option>
</select> <BR>
<BR>
Fired:
<BR>
<input type="checkbox" name="fired" value="Fired" id="fired">
<BR><BR>


<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Table"	onclick="sendAndDraw();return false;"> <br />
<br />
<br />
</div><!-- pf-form -->

<div id="tmp1" style="float: left;margin-bottom: 20px">
<?php echo "<div id=\"chartTitle\" style=\"font-size: small\">".$title."</div>";?>
<div id="table1" style="color: #000;"> </div>



</div> <!--  font-black -->


</div> <!--  siteContain -->  
</body>
</html>
