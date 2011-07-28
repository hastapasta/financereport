<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" />

<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script>

<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/yahoo/yahoo-min.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/json/json-min.js"></script>

<div class="alerts none">
<script type="text/javascript">
	function get_type(thing){     
		if(thing===null)return "[object Null]"; 
	// special case   
 		return Object.prototype.toString.call(thing); 
 	}

	function dumpProps(obj, parent) {
//		Go through all the properties of the passed-in object 
		for (var i in obj) {
	      // if a parent (2nd parameter) was passed in, then use that to 
	      // build the message. Message includes i (the object's property name) 
	      // then the object's property value on a new line 
			if (parent) { var msg = parent + "." + i + "\n" + obj[i]; } else { var msg = i + "\n" + obj[i]; }
	      // Display the message. If the user clicks "OK", then continue. If they 
	      // click "CANCEL" then quit this level of recursion 
			if (!confirm(msg)) { return; }
	      // If this property (i) is an object, then recursively process the object 
			if (typeof obj[i] == "object") { 
				if (parent) { dumpProps(obj[i], parent + "." + i); } else { dumpProps(obj[i], i); }
			}
		}
	}

	(function() {

		 function treeInit() {
			
		        buildTree3();
		    }
	function buildTree3() {

	
		
		tree1 = new YAHOO.widget.TreeView("treeDiv1");

		tmp = new YAHOO.widget.TextNode("&nbsp<B>LOADING TREE...</B>", tree1.getRoot(), false);

		tree1.render();

		
    	
    	/*tree1.subscribe('clickEvent',tree1.onEventToggleHighlight);		
    	tree1.setNodesProperty('propagateHighlightUp',true);
    	tree1.setNodesProperty('propagateHighlightDown',true);
    	tree1.render();*/

    	var tmpNode1;
 

        

        /*for (var i = 0; i < Math.floor((Math.random()*4) + 3); i++) {
            var tmpNode = new YAHOO.widget.TextNode("label-" + i, tree.getRoot(), false);
            // tmpNode.collapse();
            // tmpNode.expand();
            // buildRandomTextBranch(tmpNode);
            buildLargeBranch(tmpNode);
        }*/
     

        

       // Expand and collapse happen prior to the actual expand/collapse,
       // and can be used to cancel the operation
  

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
          	
          			var data = YAHOO.lang.JSON.parse(xmlhttp.responseText);
          			var tmpNode;
					/*
					* Build the Group Nodes
					*/
          			//n = tree1.getNodeByProperty('id',data[0].parent_id)
          			
          			tree1.removeChildren(tree1.getRoot());
          			
          			//tree1.removeChildren(n);

          			//for (i=0;i<1;i++)
          			for (i=0;i<data.length;i++)
          			{
              		
              			//n = tree1.getNodeByProperty('id',data[i].parent_id)
              			//tree1.removeChildren(n);
              			
              		
              			if((data[i].description != null) && data[i].description != ''){
							var label = "&nbsp<B>" + data[i].description + "</B>";
						}else{
							var label = "&nbsp<B>" + data[i].id + "</B>";
						}
						

              			label.replace('€','&#8364;');
              		
              			if ((data[i].full_name != null) && (data[i].full_name != ''))
                  			label += ' (' + data[i].full_name + ')';

              			if ((data[i].country != null) && (data[i].country != ''))
                  			label += ' (' + data[i].country + ')';

              			
              		
              			if (i==0)
              				 var tmpNode = new YAHOO.widget.TextNode(label, tree1.getRoot(), false);
              				 
              			else
              			{
							nodes = tree1.getNodesByProperty('id',data[i].parent_id);


							for (j=0;j<nodes.length;j++)
							{
								if (nodes[j].type == 1)
									tmpNode = new YAHOO.widget.TextNode(label, nodes[j], false);
							}
				
                  			
          					
          					tmpNode.parent_id = data[i].parent_id;
              			}
          				tmpNode.id = data[i].id;
          				tmpNode.type = data[i].type;

          	
          				

          			}

          	
          		   tree1.subscribe("collapse", function(node) {
                       YAHOO.log(node.index + " was collapsed", "info", "example");
                     
                    });

	                tree1.subscribe("expand", function(node) {
	                    YAHOO.log(node.index + " was expanded", "info", "example");
	                });
	
	                // Trees with TextNodes will fire an event for when the label is clicked:
	                tree1.subscribe("labelClick", function(node) {
	                       YAHOO.log(node.index + " label was clicked", "info", "example");
                    });

          			tree1.subscribe('clickEvent',tree1.onEventToggleHighlight);		
          	    	tree1.setNodesProperty('propagateHighlightUp',true);
          	    	tree1.setNodesProperty('propagateHighlightDown',true);
          	    	tree1.render();

         
          		
          			
          		}
          	}

          	var currentTime = new Date()
        	var users = document.getElementById("users");
        
        	//xmlhttp.open("POST","http://localhost/PHP/ajaxsample/cakeajax.php?q="+str+"&timestamp="+currentTime,true);
        	//alert("http://localhost/PHP/ajaxsample/cakeajax2.php?q=" + users.value);
        	
        	xmlhttp.open("POST",php_root_path + "/ajaxsample/cakeajax4.php?q=1002" ,true);

        	xmlhttp.send();

     

    

	}

	YAHOO.util.Event.onDOMReady(treeInit);

	} )();
	function buildTree2() {
		var AppPath = "<?php echo $this->base;?>/alerts/getTicker/q:1000";
	
		xmlhttp.open("POST",AppPath,true);
		xmlhttp.send();
		jsondata = YAHOO.lang.JSON.parse(xmlhttp.responseText);
		tree = new YAHOO.widget.TreeView("treeDiv2");
		tmpNode = new YAHOO.widget.TextNode("All Foreign Exchange Rate Entities", tree.getRoot(), false);
		for (i=0;i<jsondata.length;i++){
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
	}

	function submitFunc()
	{
		//alert('here');
		/*if (!validate())
			return false;*/
		
		var myselect = document.getElementById("AlertEntityId");
		myselect.style.display="none";
			//document.createElement("select");
		//myselect.setAttribute("name","data[Alert][entity_id]");
		//var theOption=document.createElement("OPTION");
		//theText=document.createTextNode("OptionText");
		//theOption.appendChild(theText);
		
		var hiLit = tree1.getNodesByProperty('highlightState',1);
		/*
		* Have to do an id lookup now. Tried saving the id in the treenode along with the ticker, but the id field wasn't coming through.
		*/
		for (k=0;k<hiLit.length;k++)
		//for(k=0;k<1;k++)
		{

			if (hiLit[k].type == 2)
			{
				
				var theOption=document.createElement("OPTION");
				theText=document.createTextNode("OptionText");
				theOption.appendChild(theText);
				theOption.setAttribute("value",hiLit[k].id);
				theOption.setAttribute("selected","selected");
	
				myselect.appendChild(theOption);
			}
		}

		//theOption.setAttribute("value",hiLit[0].id);
		//myselect.appendChild(theOption);
		document.testForm.appendChild(myselect);
		/*var divIdName = 'my'+num+'Div';
		newdiv.setAttribute('id',divIdName);
		newdiv.innerHTML = 'Element Number '+num+' has been added! <a href=\'#\' onclick=\'removeElement('+divIdName+')\'>Remove the div "'+divIdName+'"</a>';
		ni.appendChild(newdiv);*/
		//alert(document.testForm.elements.length);
		/*for(i=0; i<document.testForm.elements.length; i++)
		{
		//document.write("The field name is: " + document.FormName.elements[i].name + " and it’s value is: " + document.FormName.elements[i].value + ".<br />");
			if (document.testForm.elements[i].name=="data[Alert][entity_id][]")
			{
				selRef= document.testForm.elements[i];
				//dumpProps(selRef);
				//alert(get_type(selRef));
				for (var z=selRef.options.length-1; z >= 0;z--) 
				{
					document.getElementById("testdiv").innerHTML += "The value name is: " + selRef.options[z].value + ".<br />";	
				}	
			}                                                    				
			else
				document.getElementById("testdiv").innerHTML += "The field name is: " + document.testForm.elements[i].name + " and it’s value is: " + document.testForm.elements[i].value + ".<br />";
		}*/
		return true;
	}

	</script>

<?php //echo $this->element('actions'); ?>
<?php //echo $this->Form->create('Alert');
	echo $this->Form->create('Alert', array (/*'default'=>false,'action'=>'multiAdd',*/'name'=>'testForm','onsubmit'=>'submitFunc();'))
?>
	<fieldset>
 		<legend><?php __('Add Alert'); ?></legend>
	<?php



		echo '<div style="display:none" id="hiddenselect" >';
		echo '<select id="hiddenTicker" name="data[Alert][entity_id]" multiple="multiple" >';
		echo "</select>";
		echo $this->Form->input('Alert.entity_id',array('type'=>'select','multiple'=> true));
		echo "</div>";
		//$group_id = $session->read('Auth.User.group_id');		
		 
		
		//echo $this->Form->input('task_id',array('label'=>'Entity Group','onChange'=>'showUser(this.value)','options' => $this->getVar('entity_groups')));
		//debug($this->Form,true);
		//echo "<div style=\"position:relative; left:0px; top:0px; width:400px; height:0px; background-color:#ffffff; overflow:auto;\"";
		//echo "id=\"treeDiv1\" class=\"ygtv-checkbox\"></div>";
		echo "Entity Group:<BR>";
		echo "<div id=\"treeDiv1\" class=\"ygtv-checkbox treeDiv\"></div>";
		
		echo "<div style=\"position:relative; left:0px; top:0px; width:400px; height:0px; background-color:#ffffff; overflow:auto;\"";
		echo "id=\"treeDiv2\" class=\"ygtv-checkbox\"></div>";
		echo $this->Form->input('Alert.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
		echo $this->Form->input('Alert.time_event_id',array('label'=>'Observation Period','options' => $this->getVar('timeeventnames')));
		echo $this->Form->input('Alert.metric_id',array('label'=>'Metric','options' => $this->getVar('metric_names')));
		echo $this->Form->input('limit_value');
		//echo $this->Form->input('limit_adjustment');
		echo $this->Form->input('limit_adjustment',array('value'=>0,'type'=>'hidden'));
		echo $this->Form->input('type',array('value'=>'LIMIT','type'=>'hidden'));
		//echo $this->Form->input('fact_data_key');
		
		//echo $this->Form->input('alert_count');
		echo $this->Form->input('disabled');
		echo $this->Form->input('auto_reset_fired',array('label'=>'Auto Reset After Fired'));
		echo $this->Form->input('auto_reset_period',array('label'=>'Auto Reset After Observation Period End'));
		echo $this->Form->input('email_alert',array('label'=>'Email Notification'));
		
		
	
		//echo $this->Form->input('user_id');
	?>
	</fieldset>
<?php echo $this->Form->end(__('Submit', true));?>
</div>

