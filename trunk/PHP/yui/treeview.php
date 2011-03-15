<?php



?>

<HTML>
<head>
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" />
<link rel="stylesheet" type="text/css" href="http://localhost/PHP/yui/cake.generic.css" />
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>

<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script>


</head>

<BODY>
<div id="container">
<div id="content">
<div class="alerts form">


<div id="treeDiv1"></div>
</div>
</div>
</div>
<script type="text/javascript">

//global variable to allow console inspection of tree:
var tree;

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

        var tmpNode = new YAHOO.widget.TextNode("Forex", tree.getRoot(), false);
        buildLargeBranch(tmpNode);
        tmpNode = new YAHOO.widget.TextNode("Commodity Futures", tree.getRoot(), false);
        buildLargeBranch(tmpNode);
        tmpNode = new YAHOO.widget.TextNode("US Equities", tree.getRoot(), false);
        buildLargeBranch(tmpNode);
        tmpNode = new YAHOO.widget.TextNode("US Equity Indexes", tree.getRoot(), false);
        buildLargeBranch(tmpNode);

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

	//Add an onDOMReady handler to build the tree when the document is ready
    YAHOO.util.Event.onDOMReady(treeInit);

})();

</script>







</BODY>
</HTML>