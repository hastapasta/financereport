<?php
include ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
   <head>
	  <?php IncFunc::jQuery();?>
	  <?php IncFunc::generalDateFunctions();?>
	  <?php IncFunc::googleGadget(); ?>
	  <?php IncFunc::icon();?>
	  <?php IncFunc::title();?>
	  <script type="text/javascript" src="/PHP/site/includes/pftable.js" ></script>
	  
	  <?php //IncFunc::iquery(); ?>
	  <script src="http://widgets.twimg.com/j/2/widget.js" type="text/javascript"></script>
	  <?php IncFunc::linkStyleCSS();?>
	  <script language="Javascript" type="text/javascript"> 

		 $(document).ready(function() {
			 $("#tabs").tabs();
			 window.addEventListener("focus", function(event) { 
				 var sel = window.getSelection ? window.getSelection() : document.selection;
				 if (sel) {
				     if (sel.removeAllRanges) {
				         sel.removeAllRanges();
				     } else if (sel.empty) {
				         sel.empty();
				     }
				 }
				 				 
				 //var td = new Date();
				 //document.getElementById('message').innerHTML = "window has focus " + td;
			 }, false);
		 });
		 function findFirstDescendant(parent, tagname)
		 {
			parent = document.getElementById(parent);
			var descendants = parent.getElementsByTagName(tagname);
			if ( descendants.length )
			   return descendants[0];
			return null;
		 }
		 var element = null;
		 function get_type(thing)
		 {
			 if(thing===null)return "[object Null]"; 
			 return Object.prototype.toString.call(thing); 
		 }
		 google.load('visualization', '1', {'packages' : ['table']});
		 /*
		 * The following line will result in the table being loaded on document load. 
		 */
		 google.setOnLoadCallback(function() { sendAndDraw('') });
		 var firstpass = true;
		 <?php 
			echo "var dataSourceRoot = '".IncFunc::$PHP_ROOT_PATH."/json/';\n";
		 ?>
		 var query1;
		 var query2;
		 var query3;
		 var query4;
		 /*
		 * OFP 5/27/2012 - Because of a scoping issue with using an array of objects, you have to create the handler
		 outside of the local function.
		 */
		 function createHandler(a,b) {
				return(function(event) { genericClickHandler(a,b); });
		 }
		 function genericClickHandler(localTableChart,localQueryWrapper) {
			 var row = localTableChart.getSelection();
			 var dt = localQueryWrapper.currentDataTable;
			 var val = dt.getValue(row[0].row,8);
			 <?php //echo "window.location.href = \"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\";";?>
			 <?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val);";?>
		 }
		 function sendAndDraw() { 
			var taskid='1';
			var queryString1;
			var tables = [1,2,3,4];
			//var tables = [1];
			var assets = ["forex","index", "commodity", "futures"];
			var timeframes = ["day","week","month","year"];
			var tabs = [1,2,3,4];
			//var tabs = [1];
			if (firstpass==true){
			  firstpass=false;
			}
			var options = {};
			options['height'] = 220;
			options['width'] = 420;
			window.tObjects = new Array();
			for (h=0;h<tabs.length;h++) {
			  for (i=0;i<tables.length;i++) {
				  var currentindex = (h*tables.length) + i;
				  window.tObjects[currentindex] = new TableObject();
				  //tObjects[currentindex].container = document.getElementById('table' + tables[currentindex]);
				  //var x = '#tabs-' + tabs[h] + ' #table' + tables[i];
				  var x = '#tab' + tabs[h] + 'table' + tables[i];
				  /*
				  jQuery call to get the underlying element.
				  */
				  window.tObjects[currentindex].container = $(x).get(0);
				  /*
				  * Display the spinner for the first table only.
				  */
				  if (i==0)
					  $(x).html("<img src=\"../../site/images/spinner3-bluey.gif\" />");
				  window.tObjects[currentindex].tableChart = new google.visualization.Table(window.tObjects[currentindex].container);
				  window.tObjects[currentindex].dataSourceUrl = dataSourceRoot + timeframes[h] + assets[i] + ".html";
				  if (window.console && window.console.firebug) {console.log(window.tObjects[currentindex].dataSourceUrl);}
				  window.tObjects[currentindex].query = new google.visualization.Query(window.tObjects[currentindex].dataSourceUrl);
				  window.tObjects[currentindex].query.setTimeout(120);
				  window.tObjects[currentindex].queryWrapper = new QueryWrapper(window.tObjects[currentindex].query, window.tObjects[currentindex].tableChart, options, window.tObjects[currentindex].container,[1,2,4,5,7,8,9],3);
				  /*google.visualization.events.addListener(window.tObjects[currentindex].tableChart, 'select', function(event){
						genericClickHandler(window.tObjects[currentindex].tableChart,window.tObjects[currentindex].queryWrapper);
				   });*/
				   /* Because of scoping issue, need to create handler outside of the local function.*/
				  google.visualization.events.addListener(window.tObjects[currentindex].tableChart, 'select',createHandler(window.tObjects[currentindex].tableChart,window.tObjects[currentindex].queryWrapper));
				  window.tObjects[currentindex].queryWrapper.sendAndDraw();
			   }
			}
		 }
	  </script>
	  <?php IncFunc::googleAnalytics();?>
	   <!-- OFP 5/27/2012: Workaround for issue with the width and height options being passed to the google
	  tables that aren't the top ones.  -->
	  <style type="text/css">
	  .google-visualization-table-table {
	  	width: 400px;
	  	height: 220px;
	  }
	  </style>
   </head>

   <body>
	  <div id="jq-siteContain">
		 <?php
		 IncFunc::header1("home");
		 ?>
		 
		 <!-- <div id="jq-content"> -->
		 <div id="pf-custom-body">
			<div id="rightcol_index2">
			   <div class="rightcol_index2_font">
				  <div class="heading_index2">Market Snapshots</div>
				 <div id="tabs">
	<ul>
		<li><a href="#tabs-1">Past 24 Hours</a></li>
		<li><a href="#tabs-2">Week to Date</a></li>
		<li><a href="#tabs-3">Month to Date</a></li>
		<li><a href="#tabs-4">Year to Date</a></li>
	</ul>
	
<?php 
$tabs = Array(1,2,3,4);
$tables = Array(1,2,3,4);
$tabletitles = Array("Forex","Global Equity Indexes","Commodity Futures","Global Equity Index Futures");
$entitygroups = Array(3,5,4,1008);
$metricids = Array(1,1,11,11);
/*
 * These values need to match the values in sitecommon:dateselect. Should define a commond set of
 * values, but blowing off for now.
 */
$timeframes = Array("day","WTD","MTD","YTD");



for($i=0;$i<sizeof($tabs);$i++) {
	echo "<div id=\"tabs-".$tabs[$i]."\">\n";
	for($j=0;$j<sizeof($tables);$j++) {
		$anchor = "<a style=\"font-size:1.0em;\" target=\"_new\" href=\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/directtable.php";
		$anchor.="?entitygroupid=".$entitygroups[$j];
		$anchor.="&metricid=".$metricids[$j];
		$anchor.="&timeframe=".$timeframes[$i];
		$anchor.="\">".$tabletitles[$j].":</a>\n";
		echo $anchor;
		echo "<div id=\"tab".$tabs[$i]."table".$tables[$j]."\" style=\"color: #000;margin: 15px 0 15px 0;\"></div>\n";

		
	}
	echo "</div><!-- tab ".$tabs[$i]." -->\n";

}


?>

</div> <!--  tabs  -->
			   </div> <!--  font -->
			</div> <!--  rightcol -->
			<div id="twittercontrol"> 
			   <script>
				  new TWTR.Widget({
					version: 2,
					type: 'profile',
					rpp: 10,
					interval: 6000,
					width: 250,
					height: 400,
					theme: {
					  shell: {
						background: '#666666',
						color: '#ffffff'
					  },
					  tweets: {
						background: '#ffffff',
						color: '#000000',
						links: '#079feb'
					  }
					},
					features: {
					  scrollbar: true,
					  loop: false,
					  live: true,
					  hashtags: true,
					  timestamp: true,
					  avatars: false,
					  behavior: 'all'
					}
				  }).render().setUser('pikefindotcom').start();
			   </script> 
			</div>
			<div id="jq-intro">
			   <BR/><BR/>
			   <BR/><BR/>
			   <p>We provide financial data analysis and alerting services.</p>
			   <br/><br/>
			   <p>We monitor different asset classes and financial metrics for significant changes in value. Alert notifications 
			   are then sent out in a variety of formats such as email, twitter, SMS, facebook and google+. 
			   Some of the specific items 
			   that we track are currency exchange rates, equity indexes, commodity prices, CDS prices, bond prices and gdp estimates.  
			   </p>
			   <br/>
			   <p>For questions or requests please contact <a href="mailto:pikefin1@gmail.com">pikefin1@gmail.com</a></p>
			</div> <!-- jq-intro -->
			<!-- <div id="blogcontrol">Loading...</div> -->
			<div id="blogcontrol2" >
			<script src="../../site/includes/recent-posts-with-snippets.js">
			</script>
<script>
var numposts = 5;var showpostdate = true;var showpostsummary = true;var numchars = 100;var standardstyling = true;
</script>
<script src="http://pikefin.blogspot.com/feeds/posts/default?orderby=published&amp;alt=json-in-script&amp;callback=showrecentposts">
</script></div>
<!-- <div id="rpdr" style="font-family: arial, sans-serif; font-size: 9px;"> -->
<!-- <a href="http://helplogger.blogspot.com/2012/04/recent-posts-widget-for-bloggerblogspot.html" target="_blank" title="Grab this Recent Posts Widget">
Recent Posts Widget</a> by <a href="http://helplogger.blogspot.com" title="Recent Posts Widget">Helplogger</a></div>
<noscript>Your browser does not support JavaScript!</noscript> -->
<style type="text/css">
#rpdr {background: url(http://3.bp.blogspot.com/-WM-QlPmHc6Y/T5wJV58qj9I/AAAAAAAACAk/1kULxdNyEyg/s1600/blogger.png) 0px 0px no-repeat; 
padding: 1px 0px 0px 19px; height:14px; margin: 5px 0px 0px 0px;line-height:14px;}
#rpdr, #rpdr a {color:#808080;}
.hlrps a {font-weight:bold; }
.hlrpssumm {}
</style>
		 </div><!-- pf-body -->
		 <div id="message">
		 </div>
		 <div id="pf-footer">
			<div id="disclaimer">
			   Disclaimer:
			   "The content on this site is provided as general information only and
			   should not be taken as investment advice. All site content, including
			   advertisements, shall not be construed as a recommendation to buy or
			   sell any security or financial instrument, or to participate in any particular
			   trading or investment strategy. The ideas expressed on this site are solely
			   the opinions of the author(s) and do not necessarily represent the opinions
			   of sponsors or firms affiliated with the author(s). The author
			   may or may not have a position in any company or advertiser
			   referenced above. Any action that you take as a result of
			   information, analysis, or advertisement on this site is
			   ultimately your responsibility. Consult your investment adviser
			   before making any investment decisions. "
			</div>
		 </div>
	  </div><!--  siteContain -->
   </body>
</html>
