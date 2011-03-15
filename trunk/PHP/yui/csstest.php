<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	<title>
		CakePHP: the rapid development php framework:		Alerts	</title>
	<link href="/cakepf/favicon.ico" type="image/x-icon" rel="icon" /><link href="/cakepf/favicon.ico" type="image/x-icon" rel="shortcut icon" /><link rel="stylesheet" type="text/css" href="http://localhost/PHP/yui/cake.generic.css" /></head>
<body>
	<!-- <div id="container"> -->
		<div id="header">
			<h1><a href="http://cakephp.org">CakePHP: the rapid development php framework</a><p align="right">

<a href="/cakepf/users/logout">Log Out</a> 
</p>
</h1>
		</div>
		<!-- <div id="content"> -->
			
						
			<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" /> 
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>

<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script>

<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/yahoo/yahoo-min.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/json/json-min.js"></script>


<!-- <div class="alerts form"> -->
<script type="text/javascript">
	function showUser(str)
	{
	
	
		if (str=="")
		{
		
			document.getElementById("txtHint").innerHTML="";
			return;
		} 
	
		
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5  
	
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	
		xmlhttp.onreadystatechange=function()
		{
			
			
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
				//alert(xmlhttp.responseText);

				var data = YAHOO.lang.JSON.parse(xmlhttp.responseText);

				//alert(data[0].ticker);
				/*var tmpSelect = document.getElementById("AlertTicker");
				
				var options=tmpSelect.getElementsByTagName("option");
			
				var i;
				for (i=options.length-1; i>=0; i--)
				{
					
					tmpSelect.remove(i);
				}
			
				
				for (i=0; i<10;i++)
				{
					
					var y=document.createElement('option');
					y.text=i + "";
									
				 	tmpSelect.add(y,null);

				 	
				 	
				}*/

				//alert("here 1");

				tree = new YAHOO.widget.TreeView("treeDiv1");

		        	  
		        //makeBranch(tree.getRoot());
		       
		        
					

				tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);

				for (i=0;i<data.length;i++)
				{
					new YAHOO.widget.TextNode(data[i].ticker, tmpNode, false);
				}

				 tree.subscribe('clickEvent',tree.onEventToggleHighlight);		
			     tree.setNodesProperty('propagateHighlightUp',true);
			     tree.setNodesProperty('propagateHighlightDown',true);
			     tree.render();
				/*tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("Commodity Futures", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("US Equities", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("US Equity Indexes", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);*/
		       
		        //tree.draw();
				
				//document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
				
			}
		}
	
	
		var currentTime = new Date();
		
		xmlhttp.open("POST","http://localhost/PHP/ajaxsample/cakeajax.php?q="+str+"&timestamp="+currentTime,true);
		xmlhttp.send();
	}

	var tree;

    function buildLargeBranch(node) {
    	new YAHOO.widget.TextNode(node.label + "-A", node, false);
    	new YAHOO.widget.TextNode(node.label + "-B", node, false);
    	new YAHOO.widget.TextNode(node.label + "-C", node, false);
        
        /*if (node.depth < 10) {
            YAHOO.log("buildRandomTextBranch: " + node.index, "info", "example");
            for ( var i = 0; i < 10; i++ ) {
                new YAHOO.widget.TextNode(node.label + "-" + i, node, false);
            }
        }*/
    }

   
        

	//anonymous function wraps the remainder of the logic:
	(function() {

		//function to initialize the tree:
	    function treeInit() {
	        buildRandomTextNodeTree();
	    }
	    
		//Function  creates the tree and 
		//builds between 3 and 7 children of the root node:
	    function buildRandomTextNodeTree() {
		
			
	        	        

	        /*tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);
	        buildLargeBranch(tmpNode);
	        tmpNode = new YAHOO.widget.TextNode("Commodity Futures", tree.getRoot(), false);
	        buildLargeBranch(tmpNode);
	        tmpNode = new YAHOO.widget.TextNode("US Equities", tree.getRoot(), false);
	        buildLargeBranch(tmpNode);
	        tmpNode = new YAHOO.widget.TextNode("US Equity Indexes", tree.getRoot(), false);
	        buildLargeBranch(tmpNode);*/

	        /*for (var i = 0; i < Math.floor((Math.random()*4) + 3); i++) {
	            var tmpNode = new YAHOO.widget.TextNode("label-" + i, tree.getRoot(), false);
	            // tmpNode.collapse();
	            // tmpNode.expand();
	            // buildRandomTextBranch(tmpNode);
	            buildLargeBranch(tmpNode);
	        }*/

	       // Expand and collapse happen prior to the actual expand/collapse,
	       // and can be used to cancel the operation
	       tree.subscribe("expand", function(node) {
	              YAHOO.log(node.index + " was expanded", "info", "example");
	              // return false; // return false to cancel the expand
	           });

	       tree.subscribe("collapse", function(node) {
	              YAHOO.log(node.index + " was collapsed", "info", "example");
	           });

	       // Trees with TextNodes will fire an event for when the label is clicked:
	       tree.subscribe("labelClick", function(node) {
	              YAHOO.log(node.index + " label was clicked", "info", "example");
	           });

			//The tree is not created in the DOM until this method is called:
	        tree.draw();
	    }

		//function builds 10 children for the node you pass in:
	    function buildLargeBranch(node) {
	    	new YAHOO.widget.TextNode(node.label + "-A", node, false);
	    	new YAHOO.widget.TextNode(node.label + "-B", node, false);
	    	new YAHOO.widget.TextNode(node.label + "-C", node, false);
	        
	        /*if (node.depth < 10) {
	            YAHOO.log("buildRandomTextBranch: " + node.index, "info", "example");
	            for ( var i = 0; i < 10; i++ ) {
	                new YAHOO.widget.TextNode(node.label + "-" + i, node, false);
	            }
	        }*/
	    }

	    YAHOO.util.Event.on('logHilit','click',function() {
	    	var hiLit = tree1.getNodesByProperty('highlightState',1);
	    	if (YAHOO.lang.isNull(hiLit)) { 
	    		YAHOO.log("None selected");
	    	} else {
	    		var labels = [];
	    		for (var i = 0; i < hiLit.length; i++) {
	    			labels.push(hiLit[i].label);
	    		}
	    		YAHOO.log("Highlighted nodes:\n" + labels.join("\n"), "info", "example");
	    	}
	    });

		//Add an onDOMReady handler to build the tree when the document is ready
	    YAHOO.util.Event.onDOMReady(treeInit);

	})();
	</script>
<form id="AlertAddForm" method="post" action="/cakepf/alerts/add" accept-charset="utf-8"><div style="display:none;"><input type="hidden" name="_method" value="POST" /></div>
	<fieldset>
 		<legend>Add Alert</legend>

	<div class="input select required"><label for="AlertScheduleId">Schedule Name</label><select name="data[Alert][schedule_id]" onChange="showUser(this.value)" id="AlertScheduleId">
<option value="12">Commodity Futures, Source: www.bloomberg.com</option>
<option value="11">EPS Estimates, Source: www.marketwatch.com</option>
<option value="9">EPS Estimates, Source: www.nasdaq.com</option>
<option value="1">Forex rates, Source: www.exchangerates.org</option>
<option value="6">Global Equity Indexes, Source: www.bloomberg.com</option>
<option value="10">Stock Quotes, Source: www.yahoo.com</option>
</select></div><div style="position:relative; 
 left:0px;
 top:0px; 
 width:400px;
 height:200px;
 background-color:#ffffff;
 overflow:auto;" id="treeDiv1" class="ygtv-checkbox"></div>



 
 
 
 <div class="input select required"><label for="AlertUserId">User Name</label><select name="data[Alert][user_id]" id="AlertUserId">

<option value="6">dpike</option>
<option value="5">epike</option>
<option value="4">opike</option>
<option value="1">user1</option>
<option value="2">user2</option>
<option value="3">user3</option>
</select></div><div class="input select"><label for="AlertFrequency">Frequency</label><select name="data[Alert][frequency]" id="AlertFrequency">
<option value="HOURLY">HOURLY</option>
<option value="DAILY">DAILY</option>

<option value="WEEKLY">WEEKLY</option>
<option value="MONTHLY">MONTHLY</option>
<option value="YEARLY">YEARLY</option>
<option value="ALLTIME">ALLTIME</option>
</select></div><div class="input text"><label for="AlertLimitValue">Limit Value</label><input name="data[Alert][limit_value]" type="text" maxlength="12" id="AlertLimitValue" /></div><input type="hidden" name="data[Alert][limit_adjustment]" value="0" id="AlertLimitAdjustment" /><input type="hidden" name="data[Alert][type]" value="LIMIT" id="AlertType" /><div class="input checkbox required"><input type="hidden" name="data[Alert][disabled]" id="AlertDisabled_" value="0" /><input type="checkbox" name="data[Alert][disabled]" value="1" id="AlertDisabled" /><label for="AlertDisabled">Disabled</label></div>	</fieldset>
<div class="submit"><input type="submit" value="Submit" /></div></form>

<!-- alerts form class end div -->
<!-- </div> -->
<div class="actions">
	<h3>Actions</h3>

	<ul>

		<li><a href="/cakepf/alerts">List Alerts</a></li>
	</ul>
</div>
<!--  end content div -->
	<!--  	</div> -->
		

		<div id="footer">
			<a href="http://www.cakephp.org/" target="_blank"><img src="/cakepf/img/cake.power.gif" alt="CakePHP: the rapid development php framework" border="0" /></a>		</div>
<!--  end container div -->
	<!-- </div> -->
	<table class="cake-sql-log" id="cakeSqlLog_12959885374d3f3739cb2ef8_65399792" summary="Cake SQL Log" cellspacing="0" border = "0"><caption>(default) 8 queries took 56 ms</caption>	<thead>
		<tr><th>Nr</th><th>Query</th><th>Error</th><th>Affected</th><th>Num. rows</th><th>Took (ms)</th></tr>
	</thead>
	<tbody>

	<tr><td>1</td><td>SELECT `Aro`.`id`, `Aro`.`parent_id`, `Aro`.`model`, `Aro`.`foreign_key`, `Aro`.`alias` FROM `aros` AS `Aro` LEFT JOIN `aros` AS `Aro0` ON (`Aro`.`lft` &lt;= `Aro0`.`lft` AND `Aro`.`rght` &gt;= `Aro0`.`rght`)  WHERE `Aro0`.`model` = &#039;User&#039; AND `Aro0`.`foreign_key` = 4   ORDER BY `Aro`.`lft` DESC </td><td></td><td style = "text-align: right">2</td><td style = "text-align: right">2</td><td style = "text-align: right">21</td></tr>
<tr><td>2</td><td>SELECT `Aco`.`id`, `Aco`.`parent_id`, `Aco`.`model`, `Aco`.`foreign_key`, `Aco`.`alias` FROM `acos` AS `Aco` LEFT JOIN `acos` AS `Aco0` ON (`Aco0`.`alias` = &#039;controllers&#039;) LEFT JOIN `acos` AS `Aco1` ON (`Aco1`.`lft` &gt; `Aco0`.`lft` AND `Aco1`.`rght` &lt; `Aco0`.`rght` AND `Aco1`.`alias` = &#039;Alerts&#039; AND `Aco0`.`id` = `Aco1`.`parent_id`) LEFT JOIN `acos` AS `Aco2` ON (`Aco2`.`lft` &gt; `Aco1`.`lft` AND `Aco2`.`rght` &lt; `Aco1`.`rght` AND `Aco2`.`alias` = &#039;add&#039; AND `Aco1`.`id` = `Aco2`.`parent_id`)  WHERE ((`Aco`.`lft` &lt;= `Aco0`.`lft` AND `Aco`.`rght` &gt;= `Aco0`.`rght`) OR (`Aco`.`lft` &lt;= `Aco2`.`lft` AND `Aco`.`rght` &gt;= `Aco2`.`rght`))   ORDER BY `Aco`.`lft` DESC </td><td></td><td style = "text-align: right">3</td><td style = "text-align: right">3</td><td style = "text-align: right">3</td></tr>

<tr><td>3</td><td>SELECT `Permission`.`id`, `Permission`.`aro_id`, `Permission`.`aco_id`, `Permission`.`_create`, `Permission`.`_read`, `Permission`.`_update`, `Permission`.`_delete`, `Aro`.`id`, `Aro`.`parent_id`, `Aro`.`model`, `Aro`.`foreign_key`, `Aro`.`alias`, `Aro`.`lft`, `Aro`.`rght`, `Aco`.`id`, `Aco`.`parent_id`, `Aco`.`model`, `Aco`.`foreign_key`, `Aco`.`alias`, `Aco`.`lft`, `Aco`.`rght` FROM `aros_acos` AS `Permission` LEFT JOIN `aros` AS `Aro` ON (`Permission`.`aro_id` = `Aro`.`id`) LEFT JOIN `acos` AS `Aco` ON (`Permission`.`aco_id` = `Aco`.`id`)  WHERE `Permission`.`aro_id` = 7 AND `Permission`.`aco_id` IN (18, 15, 1)   ORDER BY `Aco`.`lft` desc </td><td></td><td style = "text-align: right">0</td><td style = "text-align: right">0</td><td style = "text-align: right">1</td></tr>
<tr><td>4</td><td>SELECT `Permission`.`id`, `Permission`.`aro_id`, `Permission`.`aco_id`, `Permission`.`_create`, `Permission`.`_read`, `Permission`.`_update`, `Permission`.`_delete`, `Aro`.`id`, `Aro`.`parent_id`, `Aro`.`model`, `Aro`.`foreign_key`, `Aro`.`alias`, `Aro`.`lft`, `Aro`.`rght`, `Aco`.`id`, `Aco`.`parent_id`, `Aco`.`model`, `Aco`.`foreign_key`, `Aco`.`alias`, `Aco`.`lft`, `Aco`.`rght` FROM `aros_acos` AS `Permission` LEFT JOIN `aros` AS `Aro` ON (`Permission`.`aro_id` = `Aro`.`id`) LEFT JOIN `acos` AS `Aco` ON (`Permission`.`aco_id` = `Aco`.`id`)  WHERE `Permission`.`aro_id` = 1 AND `Permission`.`aco_id` IN (18, 15, 1)   ORDER BY `Aco`.`lft` desc </td><td></td><td style = "text-align: right">1</td><td style = "text-align: right">1</td><td style = "text-align: right">1</td></tr>
<tr><td>5</td><td>SELECT `Task`.`id`, `Task`.`description` FROM `tasks` AS `Task`   WHERE 1  GROUP BY description  ORDER BY `Task`.`description` ASC </td><td></td><td style = "text-align: right">6</td><td style = "text-align: right">6</td><td style = "text-align: right">0</td></tr>

<tr><td>6</td><td>SELECT `Entity`.`id`, `Entity`.`full_name` FROM `entities` AS `Entity`   WHERE 1 = 1   ORDER BY `Entity`.`full_name` ASC </td><td></td><td style = "text-align: right">964</td><td style = "text-align: right">964</td><td style = "text-align: right">26</td></tr>
<tr><td>7</td><td>SELECT `User`.`id`, `User`.`username` FROM `users` AS `User`   WHERE NOT (`User`.`username` = &#039;&#039;)   ORDER BY `User`.`username` ASC </td><td></td><td style = "text-align: right">6</td><td style = "text-align: right">6</td><td style = "text-align: right">1</td></tr>
<tr><td>8</td><td>SELECT `Alert`.`id`, `Alert`.`frequency` FROM `alerts` AS `Alert`   WHERE 1 = 1  GROUP BY frequency  </td><td></td><td style = "text-align: right">5</td><td style = "text-align: right">5</td><td style = "text-align: right">3</td></tr>

	</tbody></table>
	</body>
</html>

