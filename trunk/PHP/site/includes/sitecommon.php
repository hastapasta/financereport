<?php


//various include functions for javascript libraries,etc.
class IncFunc
{
	
	static public $PHP_ROOT_PATH="/PHP";
	//static public $PHP_ROOT_PATH="/phpdev";
	//static public $PHP_ROOT_PATH="/phptest";
	
	//static public $CAKE_ROOT_PATH="/cakepfdev";
	static public $CAKE_ROOT_PATH="/cakepftest";
	
	
	static public $JSP_ROOT_PATH="http://localhost:8080/JSPDataSource/";	
	//static public $JSP_ROOT_PATH="http://www.pikefin.com/devjsp/JSPDataSource/";
	
	static function icon()
	{
		echo "<link href=\"".self::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"icon\" /><link href=\"".self::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"shortcut icon\" />";
		
	}
	
	static function linkStyleCSS()
	{
		
		echo "<link rel=\"stylesheet\" href=\"".self::$PHP_ROOT_PATH."/site/includes/style.css\" type=\"text/css\" />"; 
	}

	static function logo()
	{
		echo "<a id=\"jq-siteLogo\" href=\"http://www.pikefin.com\" title=\"PikeFin Home\"><img src=\"/PHP/site/images/33pctsizecrop.jpg\"/></a>";
	}
	
	static function title()
	{
		echo "<title>PikeFinancial Data Service</title>";
	}
	
	static function jQuery()
	{
		echo "<link rel=\"stylesheet\" href=\"../../site/includes/jquery-ui-1.8.11.custom.css\" type=\"text/css\" />";
   		echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-1.5.1.js\"></script>";
    	echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-ui-1.8.11.custom.min.js\"></script>";		
	}
	
	static function googleGadget()
	{
		
		echo "<script type=\"text/javascript\" src=\"../../site/includes/querywrapper.js\"></script>";
		//echo "<script type=\"text/javascript\" src=\"".IncFunc::$CAKE_ROOT_PATH."/webroot/js/querywrapper.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>";
	}
	
	
	
	static function primaryNav($Page)
	{


		echo "<div id=\"jq-primaryNavigation\">\n";
		echo "<ul>\n";
		echo "<li class=\"jq-home ".($Page=="home"?"jq-current":"")."\"><a href=\"".self::$PHP_ROOT_PATH."/site/main/index.php\" title=\"Pikefin Home\">Home</a></li>\n";
		echo "<li class=\"jq-alerts ".($Page=="alerts"?"jq-current":"")."\"><a href=\"".self::$CAKE_ROOT_PATH."/users/chart\" title=\"Pikefin Alert Manager\">Alert Manager</a></li>\n";
		echo "<li class=\"jq-charts ".($Page=="charts"?"jq-current":"")."\"><a href=\"".self::$PHP_ROOT_PATH."/charts/allassets/topchart.php\" title=\"Pikefin Charts\">Charts</a></li>";
		//echo "<li class=\"jq-blog ".($Page=="blog"?"jq-current":"")."\"><a href=\"/PHP/site/main/blog.php\" title=\"Pikefin Blog\">Blog</a></li>";
		//echo "<li class=\"jq-about ".($Page=="about"?"jq-current":"")."\"><a href=\"/PHP/site/main/about.php\" title=\"About Pikefin\">About</a></li>";
		echo "</ul>\n";
		echo "</div><!-- /#primaryNavigation -->\n";
		
		
		
		
	}
	
	
	static function header1($context)
	{
		//require_once '../../common/functions.php';
		echo "<div id=\"jq-header\" >";
		self::logo();
		self::primaryNav($context);
		//self::pageCounter();
		echo "</div> <!-- header -->";
	}
	
	
	static function pageCounter()
	{
		
		
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
	
	static function chartSecondaryNav()
	{
		
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
	
	static function yuiDropDownJavaScript()
	{
		
		echo "	<!-- Combo-handled YUI CSS files: -->";
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
		echo "</script>";
		
		
	}
	
	static function yuiDropDownMenu()
	{

		echo "<div class=\"yui-skin-sam\" id=\"yahoo-com\" style=\"padding: 0 160px;\">";
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
		echo "</div>";
		
		
		
		
	
	}
	
	static function blogFeedJavaScript()
	{
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

}



?>