<script type="text/javascript">

		
		
		
        google.load('visualization', '1', {packages: ['table']});
        google.setOnLoadCallback(function() { sendAndDraw('') });
        
        var dataSourceUrl = jsp_root_path + '/mysqldatasource14.jsp';
        var query1;
        
        var firstpass = true;

 function sendAndDraw() {

	  if (firstpass == true)
	  {
	  	firstpass = false;
	  	return;
	  }

      var chart = document.getElementById('chart-div');
      chart.innerHTML="<img src=\"" + php_site_path + "/site/images/spinner3-black.gif\" />";

      /*var chart2 = document.getElementById('orgchart2');
      chart2.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";*/
      
  	
     
      //var users = document.getElementById('users');
      //var tasks = document.getElementById('tasks');
      // var timeeventid = document.getElementById('timeeventid');
      var userselect = document.getElementById('userselect');
      var entitygroupselect = document.getElementById('entitygroupselect');
      var teselect = document.getElementById('teselect');
      var metricselect = document.getElementById('metricselect');
      var firedcheck="";
      if (document.getElementById('fired').checked == true)
          firedcheck = "true";
      else
          firedcheck = "false";
      var rowlimit = document.getElementById('rows');


      //alert(firedcheck);
      //var userid= users.value;
      var taskid='1';
      var queryString1;

      //alert("here 1");
	  //alert("firstpass: " + firstpass);


      
      var options = {};
      options['height'] = 600;
      options['width'] = 1200;
      
      queryString1 = '?entitygroupid=' + entitygroupselect.value;
      queryString1 += '&timeeventid=' + teselect.value;
      queryString1 += '&userid=' + userselect.value;
      queryString1 += '&fired=' + firedcheck;
      queryString1 += '&rows=' + rowlimit.value;
      queryString1 += '&metricid=' + metricselect.value;
   
      
      
      




     	 //queryString1 = '?userid='+userid+'&taskid='+tasks.value+'&timeeventid='+timeeventid.value;
     	<?php 
     	//echo "queryString1 = '?taskid='+taskselect.value

     //	echo "queryString2 = '?taskid=".$taskid."&timeframe='+timeframe.value + '&order=DESC';\n";
     	
     	?>
    	 

 

      //alert(dataSourceUrl + queryString1);

   
      
      var container1 = document.getElementById('chart-div');
      
      container1.innerHTML="<img src=\"" + php_site_path + "/site/images/spinner3-bluey.gif\" />";
      //var container2 = document.getElementById('orgchart2');
     
      
      var tableChart1 = new google.visualization.Table(container1);
      //var tableChart2 = new google.visualization.Table(container2);
     
      
      query1 && query1.abort();
      query1 = new google.visualization.Query(dataSourceUrl + queryString1);
      query1.setTimeout(120);
      var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1,[],7);
      queryWrapper1.sendAndDraw();

      /*query2 && query2.abort();
      query2 = new google.visualization.Query(dataSourceUrl + queryString2);
      query2.setTimeout(120);
      var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2);
      queryWrapper2.sendAndDraw();*/
    }

  </script>
<div class="charts chart">

<?php //echo $this->element('actions',array('title'=>'Chart')); ?>

<?php //echo $this->element('chart_actions')?>

<BR>

<div id="pf-form" style="text-align:left;font-size:.9em;clear: both;">

<?php if($this->Session->read('Auth.User.group_id') == 1) {?>
<BR>
User: <BR>
<select id="userselect" style="background-color: #FFFFFF">
<option value="all">All Users</option>
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
	
</select> <BR>
<?php } else { ?>
<input type="hidden" id="userselect" <?php echo "value=\"".$this->Session->read('Auth.User.id')."\"";?> />
<?php } ?>

<BR>
Observation Period: <BR>

<select id="teselect" style="background-color: #FFFFFF">
<option value="all">All Observation Periods</option>
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
	
</select> <BR>
<BR>
Entity Group: <BR>
<select id="entitygroupselect" style="background-color: #FFFFFF">
<!-- force a user to select at least one entity group or else duplicates will show up -->
<!-- <option value="all">All Entity Groups</option> -->
<?php
	
	$sql = "select description,id from entity_groups where type='b' OR type='d'";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"".$row1['id']."\">".$row1['description']."</option>";
	}	
?>
	
	<!-- <option value="Custom">Custom</option> -->
	
</select> <BR>

<BR>
Metric: <BR>
<select id="metricselect" style="background-color: #FFFFFF">
<?php
	$sql = "select name,id from metrics";
	$result = mysql_query($sql);
	for ($j=0;$j<mysql_num_rows($result);$j++)
	{
		$row1 = mysql_fetch_array($result);
		echo "<option value=\"".$row1['id']."\">".$row1['name']."</option>";
	}	
?>
	
	<!-- <option value="Custom">Custom</option> -->
	
</select> <BR>
<BR>

Row Limit:
<BR>
<input type="text" name="rows" id="rows" value="100">

<BR><BR>
Include Fired:
<BR>
<input type="checkbox" name="fired" value="Fired" id="fired">

<BR><BR>


<input type="button" style="color: #000000;background-color: #FFFFFF" value="Display Table"	onclick="sendAndDraw();return false;"> <br />
<br />
<br />
</div><!-- pf-form -->
    <div id="chart-div" style='clear:both'>

    </div>
</div>