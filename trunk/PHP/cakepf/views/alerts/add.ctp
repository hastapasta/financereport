<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" /> 
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>

<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script>

<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/yahoo/yahoo-min.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/json/json-min.js"></script>


<div class="alerts form">
<script type="text/javascript">
	function submitFunc()
	{
	
		var myselect = document.getElementById("hiddenTicker");
			//document.createElement("select");
		myselect.setAttribute("name","data[Alert][entity_id]");
		//var theOption=document.createElement("OPTION");
		

		//theText=document.createTextNode("OptionText");
		//theOption.appendChild(theText);

		
		var hiLit = tree.getNodesByProperty('highlightState',1);

		/*
		* Have to do an id lookup now. Tried saving the id in the treenode along with the ticker, but the id field wasn't coming through.
		*/
		alert(hiLit.length);
		for (k=0;k<hiLit.length;k++)
		//for(k=0;k<1;k++)
		{

			var theOption=document.createElement("OPTION");
			

			theText=document.createTextNode("OptionText");
			theOption.appendChild(theText);

			
			for (i=0;i<jsondata.length;i++)
			{
				if (jsondata[i].ticker==hiLit[k].label)
				{
					theOption.setAttribute("value",jsondata[i].id);
					//alert(jsondata[i].id);
					break;
				}
			}

			myselect.appendChild(theOption);
		}

		alert(myselect.length);

		//theOption.setAttribute("value",hiLit[0].id);

		//myselect.appendChild(theOption);
		document.testForm.appendChild(myselect);
		

		
		
		/*var divIdName = 'my'+num+'Div';
		newdiv.setAttribute('id',divIdName);
		newdiv.innerHTML = 'Element Number '+num+' has been added! <a href=\'#\' onclick=\'removeElement('+divIdName+')\'>Remove the div "'+divIdName+'"</a>';
		ni.appendChild(newdiv);*/
		//alert(document.testForm.elements.length);
		for(i=0; i<document.testForm.elements.length; i++)
		{
			
		//document.write("The field name is: " + document.FormName.elements[i].name + " and it’s value is: " + document.FormName.elements[i].value + ".<br />");
			if (document.testForm.elements[i].name=="data[Alert][entity_id]")
			{
				selRef= document.testForm.elements[i];
				for (var z=selRef.options.length-1; z >= 0;z--) 
				{
					document.getElementById("testdiv").innerHTML += "The value name is: " + selRef.options[z].value + ".<br />";	
				}	
			}                                                    				
			else
				document.getElementById("testdiv").innerHTML += "The field name is: " + document.testForm.elements[i].name + " and it’s value is: " + document.testForm.elements[i].value + ".<br />";
		}

		alert("here");

		
			


		return true;

	}
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

				jsondata = YAHOO.lang.JSON.parse(xmlhttp.responseText);

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

				tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);

				for (i=0;i<jsondata.length;i++)
				{
					nodeObj=new Object();
					nodeObj.label=jsondata[i].ticker;
				
					nodeObj.id=jsondata[i].id;
					//new YAHOO.widget.TextNode(data[i].ticker, tmpNode, false);
					new YAHOO.widget.TextNode(nodeObj, tmpNode, false);
				}
				/*tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("Commodity Futures", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("US Equities", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);
		        tmpNode = new YAHOO.widget.TextNode("US Equity Indexes", tree.getRoot(), false);
		        buildLargeBranch(tmpNode);*/

		        tree.subscribe('clickEvent',tree.onEventToggleHighlight);		
		    	tree.setNodesProperty('propagateHighlightUp',true);
		    	tree.setNodesProperty('propagateHighlightDown',true);
		    	tree.render();
				
				//document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
				
			}
		}
	
	
		var currentTime = new Date();
		alert("http://localhost/PHP/ajaxsample/cakeajax.php?q="+str);
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
		
			//instantiate the tree:
	        tree = new YAHOO.widget.TreeView("treeDiv1");

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
	        //tree.draw();
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

	    

		//Add an onDOMReady handler to build the tree when the document is ready
	    YAHOO.util.Event.onDOMReady(treeInit);

	})();
	</script>
<?php //echo $this->Form->create('Alert');



	echo $this->Form->create('Alert', array (/*'default'=>false,'action'=>'multiAdd',*/'name'=>'testForm','onsubmit'=>'submitFunc();'))?>

	<fieldset>
 		<legend><?php __('Add Alert'); ?></legend>
	<?php

		//debug($this->validationErrors,true);
		//debug($this->data,true);
		//debug($this->getVar('usernames'),true);
		//debug($this->getVar('task_names'),true);
		//debug($this->getVar('frequencies'),true);
		
		

		//echo $this->Form->input('id');
		//echo $this->Form->input('type');
		
		//debug($this);	
		//array('onChange'=>'showFields(this)')
		
		//echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','options' => $this->getVar('task_names')));
		echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','onChange'=>'showUser(this.value)','options' => $this->getVar('task_names2')));
		
		//echo $this->Form->input('ticker',array('label'=>'Financial Enitity','options' => $this->getVar('entity_descs')));
		
		echo "<div style=\"position:relative; left:0px; top:0px; width:400px; height:200px; background-color:#ffffff; overflow:auto;\"";
		echo "id=\"treeDiv1\" class=\"ygtv-checkbox\"></div>";
		//echo $this->Form->input('schedule_id');

		echo "<div style=\"display:none\" id=\"hiddenselect\" >";
		echo "<select id=\"hiddenTicker\" multiple=\"multiple\" >";
		echo "</select>";
		echo "</div>";
		//$group_id = $session->read('Auth.User.group_id');
		
		 
		
		
		//debug($this->Form,true);
		echo $this->Form->input('Alert.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
		echo $this->Form->input('Alert.frequency',array('label'=>'Frequency','options' => $this->getVar('frequencies')));
		echo $this->Form->input('limit_value');
		//echo $this->Form->input('limit_adjustment');
		echo $this->Form->input('limit_adjustment',array('value'=>0,'type'=>'hidden'));
		echo $this->Form->input('type',array('value'=>'LIMIT','type'=>'hidden'));
		//echo $this->Form->input('fact_data_key');
		
		//echo $this->Form->input('alert_count');
		echo $this->Form->input('disabled');
		
		echo "<div id=\"testdiv\">This is some text</div>";
		//echo $this->Form->input('user_id');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>
<div class="actions">
	<h3><?php __('Actions'); ?></h3>
	<ul>

		<li><?php echo $this->Html->link(__('List Alerts', true), array('action' => 'index'));?></li>
	</ul>
</div>
