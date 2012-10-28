<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" />

<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script>

<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/yahoo/yahoo-min.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.8.2r1/build/json/json-min.js"></script>

<script type="text/javascript">
	var treeList;	//Set Global Variable For TreeView

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
			treeDivArr = new Array();
			treeDivArr = document.getElementsByClassName('treeDiv');
			var tree = new Array();

			// Loop For Create A Root Nodes For All Records
			// This is for MultiEdit
			for(i = 0; i < treeDivArr.length; i++ ){			
				var treeDivId = treeDivArr[i].attributes.id.nodeValue;
				tree[i] = new YAHOO.widget.TreeView(treeDivId);
				tmp = new YAHOO.widget.TextNode("&nbsp<B>LOADING TREE...</B>", tree[i].getRoot(), false);
				tree[i].render();
			}	// End Create A Root Node Loop

			// code for IE7+, Firefox, Chrome, Opera, Safari
			if (window.XMLHttpRequest){
				xmlhttp=new XMLHttpRequest();
			}else{// code for IE6, IE5  
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
          	
			xmlhttp.onreadystatechange=function(){
          		if (xmlhttp.readyState==4 && xmlhttp.status==200){
          			var data = YAHOO.lang.JSON.parse(xmlhttp.responseText);

          			var arrayLenght = treeDivArr.length - 1;
          			for(divArr = arrayLenght; divArr >= 0 ; divArr--){			

          				tree[divArr].removeChildren(tree[divArr].getRoot());
          			
	          			//for (i=0;i<1;i++)
	          			for (i = 0; i < data.length; i++){
	              			var label = "&nbsp<B>" + data[i].description + "</B>";
	
	              			label.replace('€','&#8364;');
	              		
	              			if ((data[i].full_name != null) && (data[i].full_name != ''))
	                  			label += ' (' + data[i].full_name + ')';
	
	              			if ((data[i].country != null) && (data[i].country != ''))
	                  			label += ' (' + data[i].country + ')';
	              		
	              			if (i==0){
	              				 var tmpNode = new YAHOO.widget.TextNode(label, tree[divArr].getRoot(), false);
	              			}else{
								nodes = tree[divArr].getNodesByProperty('id',data[i].parent_id);
								for (j=0;j<nodes.length;j++){
									//This if statement is here because the ids between groups and entities can overlap
								 	//we have to loop until we find the group id (this is the parent)
									if (nodes[j].type == 1) 								
										tmpNode = new YAHOO.widget.TextNode(label, nodes[j], false);
									
								}
	          					tmpNode.parent_id = data[i].parent_id;
	              			}
	          				tmpNode.id = data[i].id;
	          				tmpNode.type = data[i].type;
	          			}
	          	
						tree[divArr].subscribe("collapse", function(node) {
							YAHOO.log(node.index + " was collapsed", "info", "example");
	                    });
	
		                tree[divArr].subscribe("expand", function(node) {
		                    YAHOO.log(node.index + " was expanded", "info", "example");
		                });
		
		                // Trees with TextNodes will fire an event for when the label is clicked:
		                tree[divArr].subscribe("labelClick", function(node) {
		                       YAHOO.log(node.index + " label was clicked", "info", "example");
	                    });

		              
	          			tree[divArr].subscribe('clickEvent',tree[divArr].onEventToggleHighlight);		
	          	    	//tree[divArr].setNodesProperty('propagateHighlightUp',true);
	          	    	//tree[divArr].setNodesProperty('propagateHighlightDown',true);
	          	    	tree[divArr].render();

          			}
          			treeList = tree;
          			highlightNodes();
              	}
          	};
          	
          	var currentTime = new Date();
        	var users = document.getElementById("users");

          	// Get Data Through Ajax Request
          	xmlhttp.open("POST",php_site_path + "/site/ajax/cakeajax4.php?q=1002" ,true);
        	xmlhttp.send();
	}

	YAHOO.util.Event.onDOMReady(treeInit);

	} )();
	

	function submitFunc(){
		treeDivArr = new Array();
		treeDivArr = document.getElementsByClassName('treeDiv');

		for(i = 0; i < treeDivArr.length; i++ ){
			var myselect = document.getElementById("Alert"+i+"EntityId");
			myselect.style.display="none";
//			tree[i] = new YAHOO.widget.TreeView(treeDivId);
			var hiLit = treeList[i].getNodesByProperty('highlightState',1);

			/*
			* Have to do an id lookup now. Tried saving the id in the treenode along with the ticker, but the id field wasn't coming through.
			*/
			for ( k = 0; k < hiLit.length; k++ ){
				if (hiLit[k].type == 2){				
					var theOption=document.createElement("OPTION");
					theText=document.createTextNode("OptionText");
					theOption.appendChild(theText);
					theOption.setAttribute("value",hiLit[k].id);
					theOption.setAttribute("selected","selected");
					myselect.appendChild(theOption);
				}
			}
			document.testForm.appendChild(myselect);
		}
		return true;
	}

	function expandBranch(node) {
		node.expanded = true;
		if (node.parent != null)
			expandBranch(node.parent);

	}

	function highlightNode(entityid,treeindex) {
		nodes = treeList[treeindex].getNodesByProperty('id',entityid);
		for (j=0;j<nodes.length;j++){
			//This if statement is here because the ids between groups and entities can overlap
		 	//we have to loop until we find the group id (this is the parent)
			if (nodes[j].type == 2) {							
				nodes[j].highlightState = 1;
				expandBranch(nodes[j].parent);
			}
			

			
			treeList[treeindex].render();
			
		}

		
	}

	</script>







<div class="alerts form">
<?php 
	echo $this->Form->create('Alert',array('onsubmit'=>'return submitFunc();','name'=>'testForm'));	
	for($i = 0; $i < sizeof($this->data['Alert']); $i++){
?>
	<fieldset>
		<legend><?php __('Edit Alert'); ?></legend> 
		<?php
			echo '<div style="display:none" id="hiddenselect" >';
			echo '<select id="hidden'.$i.'Ticker" name="data[Alert]['.$i.'][entity_id]" multiple="multiple" >';
			echo "</select>";
			echo $this->Form->input('Alert.'.$i.'.entity_id',array('type'=>'select','multiple'=> true));
			echo "</div>";
			echo "Entity Group:<BR>";
			//echo '<div id="treeDiv'.$i.'" class="ygtv-checkbox treeDiv"></div>';
			echo '<div id="treeDiv'.$i.'" class="ygtv-checkbox treeDiv"></div>';
			
			
			
//			echo "<div style=\"position:relative; left:0px; top:0px; width:400px; height:0px; background-color:#ffffff; overflow:auto;\"";
//			echo "id='treeDiv2' class='ygtv-checkbox'></div>";
						
			
			//echo $this->Form->hidden('id');
			//echo $this->Form->input('type');
			
			//echo $this->Form->input('schedule');
			
			echo $this->Form->input('Alert.'.$i.'.id', array('type' => 'hidden'));
			echo $this->Form->input('Alert.'.$i.'.task_id',array('label'=>'Task Name',
																'options' => $this->getVar('task_names')));
			
			//$ticker1 = $tickers['Alert.'.$i.'.entity_id'];
			
			
//			$tickers1 = $this->getVar('tickers1');
//			echo $this->Form->input('Entity.'.$i.'.ticker',array('label'=>'Ticker','default'=>$tickers1[$this->data['Alert'][$i]['entity_id']]));
			echo $this->Form->input('Alert.'.$i.'.type',array('label'=>'Type','options' => $this->getVar('types')));
			echo $this->Form->input('Alert.'.$i.'.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
			//echo $this->Form->input('user');
			//echo $this->Form->input('names');
			echo $this->Form->input('Alert.'.$i.'.time_event_id',array('label'=>'Observation Period','options' => $this->getVar('timeeventnames')));
			//echo $this->Form->input('frequency');
			echo $this->Form->input('Alert.'.$i.'.limit_value');
			
			//echo $this->Form->input('fact_data_key');
			
			//echo $this->Form->input('alert_count');
			
			echo $this->Form->input('Alert.'.$i.'.disabled');
			/*if ($this->data['Alert'][$i]['fired'] == 0)
				echo $this->Form->label($this->data['Alert'][$i]['fired']);
			else
				echo $this->Form->input('Alert.'.$i.'.fired', array("label"=> 'Fired'));*/
			echo $this->Form->input('Alert.'.$i.'.auto_reset_fired', array("label"=> 'Auto Reset Fired'));
			echo $this->Form->input('Alert.'.$i.'.auto_reset_period', array("label"=> 'Auto Reset Period'));
			echo $this->Form->input('Alert.'.$i.'.email_alert', array("label"=> 'Email Notification'));
			if ($this->getVar('show_twitter_alert') == true)
				echo $this->Form->input('Alert.'.$i.'.twitter_alert', array("label"=> 'Twitter Notification'));

		?>
	</fieldset>
	<?php 
		}
		echo $this->Form->end(__('Submit', true));
	?>
	<script>
	function highlightNodes() {
		<?php for($i = 0; $i < sizeof($this->data['Alert']); $i++){ 
			echo 'highlightNode('.$this->data['Alert'][$i]['entity_id'].','.$i.');';
		
		}?>
	}
	
	</script>
</div>
