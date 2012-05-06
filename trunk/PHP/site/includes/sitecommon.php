<?php


//various include functions for javascript libraries,etc.
class IncFunc {
	
	//static public $PHP_ROOT_PATH="/PHP";
	//static public $PHP_ROOT_PATH="/phpdev";
	static public $PHP_ROOT_PATH="/phptest";
	
	//static public $CAKE_ROOT_PATH="/cakepfdev";
	static public $CAKE_ROOT_PATH="/cakepftest";
	
	
	//static public $JSP_ROOT_PATH="http://devdataload:8080/JSPDataSource/";
	//static public $JSP_ROOT_PATHxx="http://devdataload:8080/JSPDataSourcexx/";
	//static public $JSP_ROOT_PATH="http://testdataload:8080/JSPDataSource/";	
	//static public $JSP_ROOT_PATH="http://www.testpikefin.com/devjsp/JSPDataSource/";
	static public $JSP_ROOT_PATH="http://www.pikefin.com/testjsp/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://192.168.122.133:8080/JSPDataSource/";
	
	
	
	static function icon()	{
		echo "<link href=\"".self::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"icon\" /><link href=\"".self::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"shortcut icon\" />";
		
	}
	
	static function linkStyleCSS() {
		
		echo "<link rel=\"stylesheet\" href=\"".self::$PHP_ROOT_PATH."/site/includes/style.css\" type=\"text/css\" />"; 
		echo "<link rel=\"stylesheet\" href=\"".self::$PHP_ROOT_PATH."/site/includes/shared.css\" type=\"text/css\" />"; 
	}

	static function logo() {
		echo "<a id=\"jq-siteLogo\" href=\"http://www.pikefin.com\" title=\"PikeFin Home\"><img src=\"".self::$PHP_ROOT_PATH."/site/images/33pctsizecrop.jpg\"/></a>";
	}
	
	static function title() {
		echo "<title>PikeFinancial Data Service</title>";
	}
	
	static function jQuery() {
		echo "<link rel=\"stylesheet\" href=\"../../site/includes/jquery-ui-1.8.11.custom.css\" type=\"text/css\" />";
   		//echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-1.5.1.js\"></script>";
   		echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-1.7.2.min.js\"></script>";
    	echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-ui-1.8.11.custom.min.js\"></script>";		
	}
	
	
	static function jQueryDatePicker() {
		echo "<link rel=\"stylesheet\" href=\"../../site/includes/anytimec.css\" type=\"text/css\" />";
		echo "<script type=\"text/javascript\" src=\"../../site/includes/anytimec.js\"></script>";	
	}
	
	static function generalDateFunctions() {
		//Documentation here: http://www.datejs.com/
		echo "<script type=\"text/javascript\" src=\"../../site/includes/date.js\"></script>\n";	
	}
	
	static function dyGraphs() {
		echo "<script type='text/javascript' src='http://dygraphs.com/dygraph-combined.js'></script>\n";
	}
	
	static function checkFlash() {
		echo "<script type='text/javascript' src='../../site/includes/swfobject.js'></script>\n";
		
	?>
	

		<script type='text/javascript'>
		function isFlashEnabled() {
			var playerVersion = swfobject.getFlashPlayerVersion();
	
			//alert(playerVersion.major);
			
			if (playerVersion.major == 0) {
				return false;
			} else {
				return true;
			}
		}
		</script>
		<?php 
	}
	
	
	static function googleGadget() {
		
		//echo "<script type=\"text/javascript\" src=\"../../site/includes/querywrapper.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"".self::$PHP_ROOT_PATH."/site/includes/querywrapper.js\"></script>";
		//echo "<script type=\"text/javascript\" src=\"".IncFunc::$CAKE_ROOT_PATH."/webroot/js/querywrapper.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>";
		
		echo   "<STYLE type=\"text/css\">\n";
		echo "#google-visualization-errors-0 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-1 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-2 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-3 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-4 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-5 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-6 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "#google-visualization-errors-7 span{\n";
		echo "font:1.8em arial,sans-serif;\n";
		echo "}";
		echo "</STYLE>";
	}

	

	static function primaryNav($Page) {

	    
		echo "<div id=\"jq-primaryNavigation\">\n";
		
		/*
		 * This is really kludgy. First, the font sizes still don't match up exactly. Second,
		 * the css styling for this tag isn't even in effect (in theory) but in practice it is.
		 * Might be a bug in the browser (firefox).
		 */
		if ($Page=="alerts") {
			echo "<div id=\"cakefontsize\">\n";
		}
		echo "<ul>\n";
		echo "<li class=\"jq-home ".($Page=="home"?"jq-current":"")."\"><a href=\"".self::$PHP_ROOT_PATH."/site/main/index.php\" title=\"Pikefin Home\">Home</a></li>\n";
		echo "<li class=\"jq-alerts ".($Page=="alerts"?"jq-current":"")."\"><a href=\"".self::$CAKE_ROOT_PATH."/charts/chart\" title=\"Pikefin Alert Manager\">Alert Manager</a></li>\n";
		echo "<li class=\"jq-charts ".($Page=="charts"?"jq-current":"")."\"><a href=\"".self::$PHP_ROOT_PATH."/charts/allassets/topchart.php\" title=\"Pikefin Charts\">Charts</a></li>";
		//echo "<li class=\"jq-blog ".($Page=="blog"?"jq-current":"")."\"><a href=\"/PHP/site/main/blog.php\" title=\"Pikefin Blog\">Blog</a></li>";
		//echo "<li class=\"jq-about ".($Page=="about"?"jq-current":"")."\"><a href=\"/PHP/site/main/about.php\" title=\"About Pikefin\">About</a></li>";
		echo "</ul>\n";
		if ($Page=="alerts") {
			echo "</div><!-- /#cakefontsize -->\n";
		}
		echo "</div><!-- /#primaryNavigation -->\n";
		
		
		
		
	}
	
	//header1 is for cake files, header2 non-cake
	
	
	static function header1($context) {
		//require_once '../../common/functions.php';
		echo "<div id=\"jq-header\" >";
		
	
		
		self::logo();
		self::primaryNav($context);
		
		echo "<P style=\"font-size: 10pt;padding-left:50px;\">";
		echo "Note: All times are UTC-07:00.";
		echo "</P>";
		echo "</div> <!-- header -->";
	}
	
	static function header2($context) {
		//self::pageCounter();
		self::header1($context);
		
	}
	
	
	
	static function pageCounter() {
		
		
		$uri = $_SERVER['REQUEST_URI'];
		
		if (strpos($uri,'?')!=false)
			$uri = substr($uri,0,strpos($uri,'?'));
		
		db_utility::db_connect();
		
		$query1 = "select hits from page_counters where uri='".$uri."'";
		
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		
		$row1 = mysql_fetch_array($result1);
		
		if ($row1==null)
		{
			$query2 = "insert into page_counters (uri,hits) values ('".$uri."',1)";
		}
		else 
		{
			$query2 = "update page_counters set hits=".($row1['hits']+1)." where uri='".$uri."'";
		}
		
		mysql_query($query2) or die("Failed Query of " . $query2);
		
		
		
		
	}
	
	static function googleAnalytics () {
		
	}
	
	static function googleAnalyticsTest() {
		echo "<script type=\"text/javascript\">\n";

		  echo "var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-26582307-1']);
		  _gaq.push(['_trackPageview']);
		
		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();\n";
		
		echo "</script>\n";
	}
	
	static function chartSecondaryNav()	{
		
		echo "<div id=\"jq-secondaryNavigation\">\n";
		echo "<ul>\n";
		echo "<li class=\"jq-fedreserve jq-first\"><a href=\"".self::$PHP_ROOT_PATH."/charts/fed/balsheet.php\">Federal Reserve Main</a></li>\n";
		echo "<li class=\"jq-allassets\"><a href=\"".self::$PHP_ROOT_PATH."/charts/allassets/tablesample3A.php\">All Assets</a></li>\n";
		echo "<li class=\"jq-forex\"><a href=\"".self::$PHP_ROOT_PATH."/charts/forex/forex.php\">Foreign Exchange</a></li>\n";
		echo "<li class=\"jq-commodities\"><a href=\"".self::$PHP_ROOT_PATH."/charts/commods/commodities.php\">Commodities</a></li>\n";
		echo "<li class=\"jq-equities jq-last\"><a href=\"".self::$PHP_ROOT_PATH."/charts/usequities/usequities.php\">U.S Equities</a></li>\n";
		echo "</ul>\n";
		echo "</div><!-- /#secondaryNavigation -->\n";
		
	}
	
	static function yuiDropDownJavaScript()	{
		
		/*echo "	<!-- Combo-handled YUI CSS files: -->";
		//echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/combo?2.8.2r1/build/reset-fonts-grids/reset-fonts-grids.css\">";
		echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/combo?2.8.2r1/build/menu/assets/skins/sam/menu.css\">";
		echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/combo?2.8.2r1/build/button/assets/skins/sam/button.css\">";
		echo "<!-- Combo-handled YUI JS files: -->";
		echo "<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/combo?2.8.2r1/build/yuiloader-dom-event/yuiloader-dom-event.js&2.8.2r1/build/container/container_core-min.js&2.8.2r1/build/menu/menu-min.js&2.8.2r1/build/element/element-min.js&2.8.2r1/build/button/button-min.js\"></script>";
		
		echo "<script type=\"text/javascript\" src=\"".self::$PHP_ROOT_PATH."/site/includes/yuiDropDown.js\"></script>";
		
		echo "<script type=\"text/javascript\">";
		
		echo "var rootpath='".self::$PHP_ROOT_PATH."';";
		//echo "var rootpath='/PHP';";
		//echo "var rootpath='/phptest';";
		
		echo "YAHOO.util.Event.onContentReady(\"productsandservices\", yuiCallBack);";
		echo "</script>";*/
		
		
	}
	
	static function yuiDropDownMenu() {

		/*echo "<div class=\"yui-skin-sam\" id=\"yahoo-com\" style=\"padding: 0 160px;\">";
		echo "	<div id=\"doc\" class=\"yui-t1\" width=\"30em\">";
		echo "		<div id=\"productsandservices\" class=\"yuimenubar yuimenubarnav\">";
		echo "			<div class=\"bd\">";
		echo "				<ul class=\"first-of-type\">";
		echo "				<li class=\"yuimenubaritem first-of-type\">";
		echo "					<a class=\"yuimenubaritemlabel\" >Equities</a>";
		echo "				</li>";
		echo "				<li class=\"yuimenubaritem\">";
		echo "					<a class=\"yuimenubaritemlabel\" >Commodities</a>";
		echo "				</li>";
		echo "				<li class=\"yuimenubaritem\">";
		echo "					<a class=\"yuimenubaritemlabel\" >Foreign Exchange</a>";
		echo "				</li>";
		echo "				<li class=\"yuimenubaritem\">";
		echo "					<a class=\"yuimenubaritemlabel\" >Miscellaneous</a>";
		echo "				</li>";
		echo "				</ul>";
		echo "			</div>";
		echo "		</div>";
		echo "	</div>";
		echo "</div>";*/
		self::dropdownInclude();
		self::dropdownMenu();
		
		
		
		
	
	}
	
	static function dropdownInclude() {
		//echo "<script type=\"text/javascript\" src=\"".self::$PHP_ROOT_PATH."/site/includes/jquery-1.5.1.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"".self::$PHP_ROOT_PATH."/site/includes/dropdown.js\"></script>";
		echo "<link type=\"text/css\" rel=\"stylesheet\"   href=\"".self::$PHP_ROOT_PATH."/site/includes/dropdown.css\"></link>";
	}
	
	static function dropdownMenu()	{
		echo "
			<div style='padding-left:400px;margin-bottom:20px;z-index:10;position:relative;'>
			<ul class='dropdown'>
				<li>
					<a href='#'>Equities</a>
					<ul class='sub_menu'>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=1' >US Equities Top Gainers/Losers</a>
						</li>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/equities/epslinechart3.php?entityid=1'>US Equities Quarterly Earnings</a>
						</li>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=1&group=1&amp;type=4' >US Equities Individual Charts</a>
						</li>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=5' >Global Equity Indexes Table</a>
						</li>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101027&amp;begindate=1293865200000&amp;enddate=current' >Benchmark Global Equity Indexes Table</a>
						</li>
						<li>
						<a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=1008&amp;metricid=11' >Global Equity Futures Table</a>
						</li>
					</ul>
				</li>
				<li>
					<a href='#'>Commodities</a>
					<ul class='sub_menu'>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=4&amp;metricid=11' >Commodity Futures Table</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=673&group=4&amp;type=3' >Commodity Futures Individual Charts</a></li>
					</ul>
				</li>
				<li>
					<a href='#'>Foreign Exchange</a>
					<ul class='sub_menu'>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=3'>Global Forex Table</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=508&group=3&type=2'>Forex Individual Charts</a></li>
					</ul>
				</li>
					<li>
					<a href='#'>Bonds</a>
					<ul class='sub_menu'>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101024&order=DESC&metricid=1'>Global CDS Table</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101023&order=DESC&metricid=1001'>Global Sovereign Bonds Table</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101029&group=101023&type=5'>UK Yield Curve</a></li>			
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101030&group=101023&type=5'>Australian Yield Curve</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101031&group=101023&type=5'>Japanese Yield Curve</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101032&group=101023&type=5'>Brazilian Yield Curve</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101033&group=101023&type=5'>German Yield Curve</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101034&group=101023&type=5'>Hong Kong Yield Curve</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101035&group=101023&type=5'>U.S. Yield Curve</a></li>
					</ul>
				</li>
				<li>
					<a href='#'>Miscellaneous</a>
					<ul class='sub_menu'>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/fed/balsheet.php'>Federal Reserve Balance Sheet</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/other/comingsoon.php'>Federal Reserve Change WOW</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/gdp/motionchart.php'>GDP Growth Estimates</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/countries/linechart.php'>GDP/Equity Index Line Chart</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/alerts/twittertable.php'>Twitter Alerts Table</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/topchart.php'>All Assets - Tables</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/allassets/linechart.php?a=660&amp;type=1'>All Assets - Inidivdual Line Charts</a></li>
						<li><a href='".self::$PHP_ROOT_PATH."/charts/other/gasolinemotionchart.php'>Global Gasoline Prices</a></li>
					</ul>
				</li>
			</ul>
		</div>
		<div style='clear:both'>
		";
	}
	
	static function blogFeedJavaScript() {
		echo "<script src=\"https://www.google.com/jsapi?key=ABQIAAAAxIClsZ3ToqpAEYJ0xpbYDBQvMn8QNL-nLRnNjyJkuSEqYss18BSxvSrpXIIMYm_A6P2cdVBEmC64UA\" type=\"text/javascript\"></script>\n";
		echo "<script language=\"Javascript\" type=\"text/javascript\">    \n";
		echo "/*\n";
		echo " *  How to use the Feed Control to grab, parse and display feeds.\n";
		echo "*/\n";
		
		echo "google.load(\"feeds\", \"1\");\n";
		
		echo "function OnLoad() {\n";
		echo "/* Create a feed control */\n";
		echo "var feedControl = new google.feeds.FeedControl();\n";
		
		echo "/* Add two feeds. */\n";
		echo "feedControl.addFeed(\"http://pikefin.blogspot.com/feeds/posts/default\", \"Pikefin Blog\");\n";
		
		echo "/* Draw it.*/\n";
		echo "feedControl.draw(document.getElementById(\"blogcontrol\"))\n";
		echo "}\n";
		
		echo "google.setOnLoadCallback(OnLoad);\n";
		echo "</script>\n";
			
		
		
	}
	
	static function dateSelect() {
		?>
	 	<select id="timeframe" style="background-color: #FFFFFF">
	 		<option value=""></option>
		    <option value="YTD">Year To Date</option>
		    <option value="MTD">Month To Date</option>
		    <option value="WTD">Week To Date</option>
			<option value="year">1 Year Back</option>
			<option value="month">1 Month Back</option>
			<option value="day">1 Day Back</option>
			<option value="hour">1 Hour Back</option>	
			<!-- <option value="Custom">Custom</option> -->
		</select>
		<?php 
		
		
		
	}
	
	static function jqueryTimeFrame() {
		?>
			 $("#timeframe").change( function(e) {
			 /* Called when the 'preset' drop down box is changed. */
		    	enddate = new Date();
		    	begindate = new Date();
		    	var tmp = $("#timeframe").val();
		    	if (tmp == '')
		    		//do nothing
		    		return;
		    	else if (tmp == 'year')
		        	begindate.setMonth(enddate.getMonth() - 12);
		    	/*else if (tmp == 'custom1')
			    	begindate = Date.parseExact("1/20/2011", "M/d/yyyy"); */
		    	else if (tmp == 'month')
		        	begindate.setMonth(enddate.getMonth() - 1);
		    	else if (tmp == 'week')
		        	begindate.setDate(enddate.getDate() - 7);
		    	else if (tmp == 'day')
		        	begindate.setDate(enddate.getDate() - 1);
		    	else if (tmp == 'YTD') {
		    		begindate = Date.parse('January 1st');          // July 4th of this year.
			    	//year = begindate.getYear();
			    	//begindate = Date.parseExact("1/1/" + year,"M/d/yyyy");
		    	}
		    	else if (tmp == 'MTD') {	
		    		begindate = Date.parse('1'); // 1st of the current month and year
					//year = begindate.getYear();
					//month = begindate.getMonth();
					//begindate = Date.parseExact("1/" + month + "/" + year,"M/d/yyyy");
		    	}
		    	else if (tmp == 'WTD') {
					begindate = Date.today().last().sunday();
		    	}
		    	else //tmp should == hour
		        	begindate = new Date(enddate - (3600 * 1000));
	        
		    	$("#rangeDemoStart").
			  	AnyTime_noPicker().
			  	//removeAttr("disabled").
			  	val(rangeDemoConv.format(begindate)).
			    AnyTime_picker(
			              { //earliest: dayEarlier,
			                format: rangeDemoFormat
			                //latest: ninetyDaysLater
			              } );
		    	$("#rangeDemoFinish").
			  	AnyTime_noPicker().
			  	//removeAttr("disabled").
			  	val(rangeDemoConv.format(enddate)).
			    AnyTime_picker(
			              { //earliest: dayEarlier,
			                format: rangeDemoFormat
			                //latest: ninetyDaysLater
			              } );
		        	
		        	    
		        	    
		    });	
		    <?php 
		
	}

}



?>