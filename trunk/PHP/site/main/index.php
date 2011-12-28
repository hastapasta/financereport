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


<?php IncFunc::blogFeedJavaScript();?>

<?php //IncFunc::iquery(); ?>


<script src="http://widgets.twimg.com/j/2/widget.js" type="text/javascript"></script>

<!--  <link rel="stylesheet" href="../includes/style.css" type="text/css" /> -->
<?php IncFunc::linkStyleCSS();?>

<script language="Javascript" type="text/javascript"> 

/*function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}*/

//var obj= document.getElementById(obj);
//obj.style.visibility = "visible";

//var obj = getElementsByClass("gfc-title",null,null);

//obj[0].style.color = "#FFFFFF";

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
        //echo "var dataSourceUrl = '".IncFunc::$JSP_ROOT_PATH."mysqldatasource2eh2.jsp';";
        
	echo "var dataSourceUrl1 = '".IncFunc::$PHP_ROOT_PATH."/json/forex.html';\n";
	echo "var dataSourceUrl2 = '".IncFunc::$PHP_ROOT_PATH."/json/index.html';\n";
	echo "var dataSourceUrl3 = '".IncFunc::$PHP_ROOT_PATH."/json/commodity.html';\n";
	echo "var dataSourceUrl4 = '".IncFunc::$PHP_ROOT_PATH."/json/futures.html';\n";
?>
   
var query1;
var query2;
var query3;
var query4;


function genericClickHandler(localTableChart,localQueryWrapper) {

	var row = localTableChart.getSelection();
	


	var dt = localQueryWrapper.currentDataTable;
	var val = dt.getValue(row[0].row,8);
	<?php //echo "window.location.href = \"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\";";?>
	<?php echo "window.open(\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=\" + val + \"&title=All Assets Indivdual Line Charts\");";?>

}
	
function sendAndDraw() {

    var chart = document.getElementById('table1');
    chart.innerHTML="<img src=\"../../site/images/spinner3-black.gif\" />";

   // var rangeDemoStart = document.getElementById('rangeDemoStart');
    //var rangeDemoFinish = document.getElementById('rangeDemoFinish');
  
    var taskid='1';
    var queryString1;

 

    if (firstpass==true)
    {
       
        <?php //if (!empty($timeframe)) echo "var timeframe='".$timeframe."';";?>
        firstpass=false;
    }
    
    
    var options = {};
    options['height'] = 220;
    options['width'] = 400;
   
    
   	<?php 
   	//echo "queryString1 = '?topmovers=8&entitygroupid=5&begindate='+ (Date.today().add({ days: -90})).getTime() + '&enddate=' + (Date.today().add({ days: -89})).getTime() + '&metricid=1&order=ASC';\n";
   	?>

    var container1 = document.getElementById('table1');
    var container2 = document.getElementById('table2');
    var container3 = document.getElementById('table3');
    var container4 = document.getElementById('table4');
 
   
    var tableChart1 = new google.visualization.Table(container1);
    var tableChart2 = new google.visualization.Table(container2);
    var tableChart3 = new google.visualization.Table(container3);
    var tableChart4 = new google.visualization.Table(container4);

  
    if (window.console && window.console.firebug) {console.log(dataSourceUrl1);}
    if (window.console && window.console.firebug) {console.log(dataSourceUrl2);}
    if (window.console && window.console.firebug) {console.log(dataSourceUrl3);}
    if (window.console && window.console.firebug) {console.log(dataSourceUrl4);}
   
    
    query1 && query1.abort();
    query1 = new google.visualization.Query(dataSourceUrl1);
    query1.setTimeout(120);

    query2 && query2.abort();
    query2 = new google.visualization.Query(dataSourceUrl2);
    query2.setTimeout(120);

    query3 && query3.abort();
    query3 = new google.visualization.Query(dataSourceUrl3);
    query3.setTimeout(120);

    query4 && query4.abort();
    query4 = new google.visualization.Query(dataSourceUrl4);
    query4.setTimeout(120);
    /*
    * The [8] parameter is what is used to hide the data column index.
    */
    var queryWrapper1 = new QueryWrapper(query1, tableChart1, options, container1,[1,2,4,5,7,8,9],3);
    google.visualization.events.addListener(tableChart1, 'select', function(event){
  	  genericClickHandler(tableChart1,queryWrapper1);

   });
    queryWrapper1.sendAndDraw();

    var queryWrapper2 = new QueryWrapper(query2, tableChart2, options, container2,[1,2,4,5,7,8,9],3);
    google.visualization.events.addListener(tableChart2, 'select', function(event){
  	  genericClickHandler(tableChart2,queryWrapper2);

   });
    queryWrapper2.sendAndDraw();

    var queryWrapper3 = new QueryWrapper(query3, tableChart3, options, container3,[1,2,4,5,7,8,9],3);
    google.visualization.events.addListener(tableChart3, 'select', function(event){
  	  genericClickHandler(tableChart3,queryWrapper3);

   });
    queryWrapper3.sendAndDraw();

    var queryWrapper4 = new QueryWrapper(query4, tableChart4, options, container4,[1,2,4,5,7,8,9],3);
    google.visualization.events.addListener(tableChart4, 'select', function(event){
  	  genericClickHandler(tableChart4,queryWrapper4);

   });
    queryWrapper4.sendAndDraw();

   

  
  }

</script>

</script>

<?php IncFunc::googleAnalytics();?>
</HEAD>

<BODY>
<div id="jq-siteContain">



<?php
IncFunc::header1("home");
?>

<!-- <div id="jq-content"> -->
<div id="custom-body" style="margin: 20px 50px;">
<div id="rightcol" style="float:right;margin: 25px 0 0 0;">
<div style="font-size:1.5em;">
Market Snapshots - Past 24 Hours

<br/>
<?php echo "<a href=\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=3&metricid=1&timeframe=day\">Forex:</a>";?>
</div>
<div id="table1" style="color: #000;margin: 15px 0 0 0;"></div>
<div style="font-size:1.5em;">
<?php echo "<a href=\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=5&metricid=1&timeframe=day\">Global Equity Indexes:</a>";?>
</div>
<div id="table2" style="color: #000;margin: 15px 0 0 0;"></div>
<div style="font-size:1.5em;">
<?php echo "<a href=\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=4&metricid=11&timeframe=day\">Commodity Futures:</a>";?>
</div>
<div id="table3" style="color: #000;margin: 15px 0 0 0;"></div>
<div style="font-size:1.5em;">
<?php echo "<a href=\"".IncFunc::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=1008&metricid=11&timeframe=day\">Global Equity Index Futures:</a>";?>
</div>
<div id="table4" style="color: #000;margin: 15px 0 0 0;"></div>
</div> <!--  rightcol -->




<div id="twittercontrol" style="float:left;margin: 10px 0 0 10px;line-height:1.8;"> 
<script>
new TWTR.Widget({
  version: 2,
  type: 'profile',
  rpp: 4,
  interval: 6000,
  width: 250,
  height: 300,
  theme: {
    shell: {
      background: '#333333',
      color: '#ffffff'
    },
    tweets: {
      background: '#000000',
      color: '#ffffff',
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

<div id="blogcontrol" style="float:left;clear:left;border: 2px solid #fff; background: #000; width: 250px;font-size: 15px;margin: 25px 0 20px 10px">
Loading...</div>
</div><!-- pf-body -->
<div id="pf-footer">
<div id="disclaimer" style="clear:both; margin: 20px 100px 20px 100px">
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


<!-- </div> --><!-- content -->
</div><!--  siteContain -->
</BODY>
</HTML>
