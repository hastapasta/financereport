<?php

require_once 'env.php';
//various include functions for javascript libraries,etc.
class IncFunc {
	
	/*
	 * For a period of time, we will have environment variables in both sitecommon.php and env.php
	 * but eventually everything needs to be moved over to env.php.
	 */
	
	static public $offline=false;
	
	static public $PHP_ROOT_PATH="/PHP";
	//static public $PHP_ROOT_PATH="/phpdev";
	//static public $PHP_ROOT_PATH="/phptest";
	
	//static public $CAKE_ROOT_PATH="/cakepfdev";
	static public $CAKE_ROOT_PATH="/cakepftest";
	
	
	static public $JSP_ROOT_PATH="http://devdataload:8080/JSPDataSource/";
	//static public $JSP_ROOT_PATHxx="http://devdataload:8080/JSPDataSourcexx/";
	//static public $JSP_ROOT_PATH="http://testdataload:8080/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://www.testpikefin.com/devjsp/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://www.pikefin.com/testjsp/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://192.168.122.133:8080/JSPDataSource/";

	
	
	static function icon()	{
		echo "<link href=\"".Environment::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"icon\" /><link href=\"".Environment::$PHP_ROOT_PATH."/site/images/favicon.ico\" type=\"image/x-icon\" rel=\"shortcut icon\" />";
		
	}
	
	static function linkStyleCSS() {
		
		echo "<link rel=\"stylesheet\" href=\"".Environment::$PHP_ROOT_PATH."/site/includes/style.css\" type=\"text/css\" />"; 
		echo "<link rel=\"stylesheet\" href=\"".Environment::$PHP_ROOT_PATH."/site/includes/shared.css\" type=\"text/css\" />"; 
	}

	static function logo() {
		echo "<a id=\"jq-siteLogo\" href=\"http://www.pikefin.com\" title=\"PikeFin Home\"><img src=\"".Environment::$PHP_ROOT_PATH."/site/images/33pctsizecrop.jpg\"/></a>";
	}
	
	static function title() {
		echo "<title>PikeFinancial Data Service</title>";
	}
	
	static function googleTableWorkaround() {
		
		?>
		$(document).ready(function() {
			/*
			 * OFP 5/27/2012 - Workaround for issue with google tables where the tables
			act as if the mouse button is being held down after a row in a table is clicked.
			*/
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
		
		<?php 
	}
	
	static function jQuery() {
		echo "<link rel=\"stylesheet\" href=\"../../site/includes/jquery/css/overcast/jquery-ui-1.8.21.custom.css\" type=\"text/css\" />";
   		//echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery-1.5.1.js\"></script>";
   		echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery/js/jquery-1.7.2.min.js\"></script>";
    	echo "<script type=\"text/javascript\" src=\"../../site/includes/jquery/js/jquery-ui-1.8.21.custom.min.js\"></script>";		
	}
	
	
	static function jQueryDatePicker() {
		echo "<link rel=\"stylesheet\" href=\"../../site/includes/anytimec.css\" type=\"text/css\" />";
		echo "<script type=\"text/javascript\" src=\"../../site/includes/anytimec.js\"></script>";	
	}
	
	static function generateUrlCode() {
		/*
		 * This code is just for the dialog. The actual construction of the url is currently
		 * contained in a function in the chart/table file itself and has to be
		 * called generateURL.
		 */
	?>
		<script>
		$(function(){
		
		/*
		* Create the dialog div.
		*/
		var newdiv = document.createElement('div');
		newdiv.setAttribute("id","dialog-form");
		newdiv.setAttribute("title","Url");
		var newdiv2 = document.createElement('div');
		newdiv2.setAttribute("id","urllabel");
		newdiv2.setAttribute("class","selected");
		var form = document.createElement('form');
		var fieldset = document.createElement('fieldset');
		form.appendChild(fieldset);
		newdiv2.appendChild(form);
		newdiv.appendChild(newdiv2);
		document.body.appendChild(newdiv);
		
		jQuery.fn.selText = function() {
		    var obj = this[0];
		    if ($.browser.msie) {
		        var range = obj.offsetParent.createTextRange();
		        range.moveToElementText(obj);
		        range.select();
		    } 
			//webkit is chrome
		    else if ($.browser.mozilla || $.browser.opera || $.browser.webkit) {
		        var selection = obj.ownerDocument.defaultView.getSelection();
		        var range = obj.ownerDocument.createRange();
		        range.selectNodeContents(obj);
		        selection.removeAllRanges();
		        selection.addRange(range);
		    } else if ($.browser.safari) {
		        var selection = obj.ownerDocument.defaultView.getSelection();
		        selection.setBaseAndExtent(obj, 0, obj, 1);
		    }
		    return this;
		};
		$( "#dialog-form" ).dialog({
    			autoOpen: false,
    			height: 150,
    			width: 350,
    			modal: true,
    			open: function() {
    				//alert(generateURL());
    				$( "#urllabel" ).html(generateURL());
    				$(this).selText().addClass("selected");
    				
    			},
    			buttons: {
    				/*"CopyClipboard": function() {
    					var bValid = true;
    					allFields.removeClass( "ui-state-error" );
    					

    				
    				},*/
    				Close: function() {
    					$( this ).dialog( "close" );
    				}
    			},
    			close: function() {
    				//allFields.val( "" ).removeClass( "ui-state-error" );
    			}
    		});

    		$( "#generateurl" )
    			.button()
    			.click(function() {
    				//tmp = $("#generateurl").attr("name");
    				//alert(tmp);
    				$( "#dialog-form" ).dialog( "open" );
    			});
    	});
		</script>

	<?php 
	}
	
	static function generalDateFunctions() {
		//Documentation here: http://www.datejs.com/
		echo "<script type=\"text/javascript\" src=\"../../site/includes/date.js\"></script>\n";	
	}
	
	static function dyGraphs() {
		if (self::$offline)
			echo "<script type='text/javascript' src='".Environment::$PHP_ROOT_PATH."/site/includes/offline/dygraph-combined.js'></script>\n";
		else
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
		echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/querywrapper.js\"></script>";
		//echo "<script type=\"text/javascript\" src=\"".IncFunc::$CAKE_ROOT_PATH."/webroot/js/querywrapper.js\"></script>";
		
		if (self::$offline)
			echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/offline/jsapi.js\"></script>";
		else
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
		echo "<li class=\"jq-home ".($Page=="home"?"jq-current":"")."\"><a href=\"".Environment::$PHP_ROOT_PATH."/site/main/index.php\" title=\"Pikefin Home\">Home</a></li>\n";
		echo "<li class=\"jq-alerts ".($Page=="alerts"?"jq-current":"")."\"><a href=\"".Environment::$CAKE_ROOT_PATH."/charts/chart\" title=\"Pikefin Alert Manager\">Alert Manager</a></li>\n";
		echo "<li class=\"jq-charts ".($Page=="charts"?"jq-current":"")."\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/allassets/topchart.php\" title=\"Pikefin Charts\">Charts</a></li>";
		//echo "<li class=\"jq-blog ".($Page=="blog"?"jq-current":"")."\"><a href=\"/PHP/site/main/blog.php\" title=\"Pikefin Blog\">Blog</a></li>";
		//echo "<li class=\"jq-about ".($Page=="about"?"jq-current":"")."\"><a href=\"/PHP/site/main/about.php\" title=\"About Pikefin\">About</a></li>";
		echo "</ul>\n";
		if ($Page=="alerts") {
			echo "</div><!-- /#cakefontsize -->\n";
		}
		echo "</div><!-- /#primaryNavigation -->\n";
		
		
		
		
	}
	
	static function mobileCompatibility() {
		?>
		$(document).ready(function() {
			var isMobile = navigator.userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry)/);
			if (isMobile) {
				$("#pf-custom-body").css("padding-left","20px").css("padding-right","30px");
				//$("#pf-body").css("padding-left","20px").css("padding-right","30px");		
			}	
		});
		
		<?php 
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
	
	static function obsolete_header2($context) {
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
		echo "<li class=\"jq-fedreserve jq-first\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/fed/balsheet.php\">Federal Reserve Main</a></li>\n";
		echo "<li class=\"jq-allassets\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/allassets/tablesample3A.php\">All Assets</a></li>\n";
		echo "<li class=\"jq-forex\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/forex/forex.php\">Foreign Exchange</a></li>\n";
		echo "<li class=\"jq-commodities\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/commods/commodities.php\">Commodities</a></li>\n";
		echo "<li class=\"jq-equities jq-last\"><a href=\"".Environment::$PHP_ROOT_PATH."/charts/usequities/usequities.php\">U.S Equities</a></li>\n";
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
		
		echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/yuiDropDown.js\"></script>";
		
		echo "<script type=\"text/javascript\">";
		
		echo "var rootpath='".Environment::$PHP_ROOT_PATH."';";
		//echo "var rootpath='/PHP';";
		//echo "var rootpath='/phptest';";
		
		echo "YAHOO.util.Event.onContentReady(\"productsandservices\", yuiCallBack);";
		echo "</script>";*/
		
		
	}
	
	static function apycomDropDownMenu() {
		/* from http://apycom.com/menus/4-steel-blue.html */
		self::dropdownInclude();
		self::dropdownMenu();
	}
	
	static function obsoleteyuiDropDownMenu() {

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
	
	static function obsoletedropdownInclude() {
		//echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/jquery-1.5.1.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/dropdown.js\"></script>";
		echo "<link type=\"text/css\" rel=\"stylesheet\"   href=\"".Environment::$PHP_ROOT_PATH."/site/includes/dropdown.css\"></link>";
	}
	
	static function dropdownInclude() { 
		echo "<link type=\"text/css\" href=\"".Environment::$PHP_ROOT_PATH."/site/includes/menucustom/menu.css\" rel=\"stylesheet\" />";
		//echo "<script type=\"text/javascript\" src=\"jquery.js\"></script>";
		echo "<script type=\"text/javascript\" src=\"".Environment::$PHP_ROOT_PATH."/site/includes/menucustom/menu.js\"></script>";
	}
	
	static function dropdownMenu() {
	
		/*<script>
		  function removeApyLink() {
				var tmp = $("body>div:last-child");
				tmp.empty();
				//alert("here");
		    }
			    if(window.attachEvent) {
				    window.attachEvent('onload', removeApyLink);
				} else {
				    if(window.onload) {
				        var curronload = window.onload;
				        var newonload = function() {
				            curronload();
				            removeApyLink();
				        };
				        window.onload = newonload;
				    } else {
				        window.onload = removeApyLink;
				    }
				}

		</script>*/
		?>
		<div id="menu" style='margin-left:100px;margin-bottom:20px;z-index:10;position:relative;'>
		<ul class="menu">
			<li><a href="#" class="parent"><span>Equities</span></a>
				<div><ul>
					<li><a href="#" class="parent"><span>United States</span></a>
						<div><ul>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=1"><span>● Top Gainers/Losers</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/equities/epslinechart3.php?t=ge"><span>● Quarterly Earnings</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?e=1&type=4"><span>● Individual Charts</span></a></li>
						</ul></div>
					</li>
					<li><a href="#" class="parent"><span>Global</span></a>
						<div><ul>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=5"><span>● Global Equity Indexes Table</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=101027&amp;begindate=1293865200000&amp;enddate=current"><span>● Benchmark Global Equity Indexes Table</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=1008&amp;metricid=11"><span>● Global Equity Futures Table</span></a></li>
						</ul></div>
					</li>					
				</ul></div>
			</li>
			<li><a href="#" class="parent"><span>Commodities</span></a>
				<div><ul>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=4&amp;metricid=11"><span>● Complete Futures Table</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?e=673&type=3"><span>● Futures Individual Charts</span></a></li>
				</ul></div>
			</li>
			<li><a href="#" class="parent"><span>Forex</span></a>
				<div><ul>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=3"><span>● Global Forex Table</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?e=508&type=2"><span>● Forex Individual Charts</span></a></li>
				</ul></div>
			</li>
			<li><a href="#" class="parent"><span>Bonds</span></a>
				<div><ul>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=101023&order=DESC&metricid=1001"><span>● Global Sovereign Bonds Table</span></a></li>
					<li><a href="#" class="parent"><span>CDS</span></a>
						<div><ul>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=101024&order=DESC&metricid=1"><span>● Global CDS Table</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=101038&order=DESC&metricid=1"><span>● 5 Year Soverign CDS Table</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/directtable.php?entitygroupid=101036&order=DESC&metricid=1"><span>● 5 Year EU CDS Table</span></a></li>
						</ul></div>
					</li>
					<li><a href="#" class="parent"><span>Yield Curves (Full) </span></a>
						<div><ul>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101029&type=5"><span>● U.K.</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101030&type=5"><span>● Australia</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101031&type=5"><span>● Japan</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101032&type=5"><span>● Brazil</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101033&type=5"><span>● Germany</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101034&type=5"><span>● Hong Kong</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101035&type=5"><span>● U.S.</span></a></li>
						</ul></div>
					</li>
					<li><a href="#" class="parent"><span>Yield Curves (Short End) </span></a>
						<div><ul>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101042&type=5"><span>● U.K.</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101043&type=5"><span>● Australia</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101041&type=5"><span>● Japan</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101044&type=5"><span>● Brazil</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101039&type=5"><span>● Germany</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101045&type=5"><span>● Hong Kong</span></a></li>
						<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?eg=101046&type=5"><span>● U.S.</span></a></li>
						</ul></div>
					</li>
				</ul></div>
			</li>
			<li class="last"><a href="#" class="parent"><span>Miscellaneous</span></a>
				<div><ul>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/topchart.php"><span>● All Assets - Tables</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/allassets/linechart.php?e=508&type=1"><span>● All Assets - Inidivdual Line Charts</span></a></li>					
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/countries/linechart.php"><span>● GDP/Equity Index Line Chart</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/gdp/motionchart.php"><span>● GDP Growth Estimates</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/alerts/twittertable.php"><span>● Twitter Alerts Table</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/other/gasolinemotionchart.php"><span>● Global Gasoline Prices</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/fed/balsheet.php"><span>● Federal Reserve Balance Sheet</span></a></li>
					<li><a href="<?php echo Environment::$PHP_ROOT_PATH;?>/charts/other/comingsoon.php"><span>● Federal Reserve Change WOW</span></a></li>
				</ul></div>
			</li>
		</ul>
	</div>
		
		
	<?php }
	
	static function obsoletedropdownMenu()	{
		echo "
			<div style='padding-left:400px;margin-bottom:20px;z-index:10;position:relative;'>
			<ul class='dropdown'>
				<li>
					<a href='#'>Equities</a>
					<ul class='sub_menu'>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=1' >US Equities Top Gainers/Losers</a>
						</li>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/equities/epslinechart3.php?t=ge'>US Equities Quarterly Earnings</a>
						</li>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=1&type=4' >US Equities Individual Charts</a>
						</li>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=5' >Global Equity Indexes Table</a>
						</li>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101027&amp;begindate=1293865200000&amp;enddate=current' >Benchmark Global Equity Indexes Table</a>
						</li>
						<li>
						<a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=1008&amp;metricid=11' >Global Equity Futures Table</a>
						</li>
					</ul>
				</li>
				<li>
					<a href='#'>Commodities</a>
					<ul class='sub_menu'>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=4&amp;metricid=11' >Commodity Futures Table</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=673&type=3' >Commodity Futures Individual Charts</a></li>
					</ul>
				</li>
				<li>
					<a href='#'>Foreign Exchange</a>
					<ul class='sub_menu'>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=3'>Global Forex Table</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?e=508&type=2'>Forex Individual Charts</a></li>
					</ul>
				</li>
					<li>
					<a href='#'>Bonds</a>
					<ul class='sub_menu'>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101024&order=DESC&metricid=1'>Global CDS Table</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/directtable.php?entitygroupid=101023&order=DESC&metricid=1001'>Global Sovereign Bonds Table</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101029&type=5'>UK Yield Curve</a></li>			
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101030&type=5'>Australian Yield Curve</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101031&type=5'>Japanese Yield Curve</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101032&type=5'>Brazilian Yield Curve</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101033&type=5'>German Yield Curve</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101039&type=5'>German Yield Curve (short duration only)</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101034&type=5'>Hong Kong Yield Curve</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?eg=101035&type=5'>U.S. Yield Curve</a></li>
					</ul>
				</li>
				<li>
					<a href='#'>Miscellaneous</a>
					<ul class='sub_menu'>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/fed/balsheet.php'>Federal Reserve Balance Sheet</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/other/comingsoon.php'>Federal Reserve Change WOW</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/gdp/motionchart.php'>GDP Growth Estimates</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/countries/linechart.php'>GDP/Equity Index Line Chart</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/alerts/twittertable.php'>Twitter Alerts Table</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/topchart.php'>All Assets - Tables</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/allassets/linechart.php?a=660&amp;type=1'>All Assets - Inidivdual Line Charts</a></li>
						<li><a href='".Environment::$PHP_ROOT_PATH."/charts/other/gasolinemotionchart.php'>Global Gasoline Prices</a></li>
					</ul>
				</li>
			</ul>
		</div>
		<div style='clear:both'>
		";
	}
	
	/*static function blogFeedJavaScript() {
		echo "<script src=\"https://www.google.com/jsapi?key=ABQIAAAAxIClsZ3ToqpAEYJ0xpbYDBQvMn8QNL-nLRnNjyJkuSEqYss18BSxvSrpXIIMYm_A6P2cdVBEmC64UA\" type=\"text/javascript\"></script>\n";
		echo "<script language=\"Javascript\" type=\"text/javascript\">    \n";*/
		//echo "/*\n";
		//echo " *  How to use the Feed Control to grab, parse and display feeds.\n";
		//echo "*/\n";
		
		//echo "google.load(\"feeds\", \"1\");\n";
		
		//echo "function OnLoad() {\n";
		//echo "/* Create a feed control */\n";
		//echo "var feedControl = new google.feeds.FeedControl();\n";
		
		//echo "/* Add two feeds. */\n";
		//echo "feedControl.addFeed(\"http://pikefin.blogspot.com/feeds/posts/default\", \"Pikefin Blog\");\n";
		
		//echo "/* Draw it.*/\n";
		/*echo "feedControl.draw(document.getElementById(\"blogcontrol\"))\n";
		echo "}\n";
		
		echo "google.setOnLoadCallback(OnLoad);\n";
		echo "</script>\n";
	
	}*/
	
	static function blogFeedJS2() {
		echo "<script src=\"../../site/includes/recent-posts-with-snippets.js\" />\n";
	}
	
	
	static function dateSelect() {
		?>
	 	<select id="timeframe" style="background-color: #FFFFFF">
	 		<option value=""></option>
		    <option value="YTD">Year To Date</option>
		    <option value="MTD">Month To Date</option>
		    <option value="WTD">Week To Date</option>
			<option value="YEAR">1 Year Back</option>
			<option value="MONTH">1 Month Back</option>
			<option value="DAY">1 Day Back</option>
			<option value="HOUR">1 Hour Back</option>	
			<!-- <option value="Custom">Custom</option> -->
		</select>
		<?php 
		
		
		
	}
	
	
	static function dateSelectNew() {
		?>
		 	<select id="timeframe" style="background-color: #FFFFFF;margin-top:10px;" >
		 		<option value="CURRENT_BLANK"></option>
			    <option value="CURRENT_YTD" >Year To Date Using Current Date & Time</option>
			    <option value="CURRENT_MTD">Month To Date Using Current Date & Time</option>
			    <option value="CURRENT_WTD">Week To Date Using Current Date & Time</option>
				<option value="CURRENT_YEAR">1 Year Back Using Current Date & Time</option>
				<option value="CURRENT_MONTH">1 Month Back Using Current Date & Time</option>
				<option value="CURRENT_DAY">1 Day Back Using Current Date & Time</option>
				<option value="CURRENT_HOUR">1 Hour Back Using Current Date & Time</option>
				<option value="FINISH_YTD">Year To Date Using Finish Value</option>
			    <option value="FINISH_MTD">Month To Date Using Finish Value</option>
			    <option value="FINISH_WTD">Week To Date Using Finish Value</option>
				<option value="FINISH_YEAR">1 Year Back Using Finish Value</option>
				<option value="FINISH_MONTH">1 Month Back Using Finish Value</option>
				<option value="FINISH_DAY">1 Day Back Using Finish Value</option>
				<option value="FINISH_HOUR">1 Hour Back Using Finish Value</option>	
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
				    	else if (tmp == 'YEAR')
				        	begindate.setMonth(enddate.getMonth() - 12);
				    	/*else if (tmp == 'custom1')
					    	begindate = Date.parseExact("1/20/2011", "M/d/yyyy"); */
				    	else if (tmp == 'MONTH')
				        	begindate.setMonth(enddate.getMonth() - 1);
				    	else if (tmp == 'WEEK')
				        	begindate.setDate(enddate.getDate() - 7);
				    	else if (tmp == 'DAY')
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
			
			
   
	
	static function jqueryTimeFrameNew() {
		?>
		$(function(){
			rangeDemoFormat = "%e-%b-%Y %H:%i:%s";
		  	rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});
			$("#rangeDemoClear").click( function(e) {
		    $("#rangeDemoStart").val("").change();
		    $("#rangeDemoFinish").val("").change(); } );
			$("#rangeDemoStart").AnyTime_picker({format:rangeDemoFormat,placement: "popup"});
			$("#rangeDemoFinish").AnyTime_picker({format:rangeDemoFormat,placement: "popup"});
			$("#timeframe").change( function(e) {
			/* Called when the 'preset' drop down box is changed. */
		    	
		    	
		    	
		    	var code = $("#timeframe").val();
				var prefix = code.substring(0,code.indexOf("_"));
				var suffix = code.substring(code.indexOf("_")+1,code.length);
				
				var enddate,begindate;
				if (prefix === "CURRENT") {
					enddate = new Date();
					//begindate = new Date();				 		
				}
				else if (prefix === "FINISH") {
				 	enddate = Date.parse($("#rangeDemoFinish").val());
				 	if (!enddate) {
				 		alert("Please set a 'Finish' date value.");
				 		 $("#timeframe").val("CURRENT_BLANK");
				 		return;
				 	}
				 	//begindate =  new Date();
				}
				 
				begindate = enddate.clone();
				
		    	switch (suffix) {
		    		case "YEAR":
		    			//begindate = enndate.clone();
		    			begindate.setYear(enddate.getFullYear() - 1);
		    			break;
		    		case "MONTH":
		    			//begindate = enddate.clone();
		    			begindate.setMonth(enddate.getMonth() - 1);
		    			break;
		    		case "WEEK":
		    			//begindate = enddate.clone();
		    			begindate.setDate(enddate.getDate() - 7);
		    			break;
		    		case "DAY":
		    		    //alert("here");
		    			begindate.setDate(enddate.getDate() - 1);
		    			break;
		    		case "YTD":
		    			begindate = Date.parse('January 1st, ' + enddate.getFullYear());  
		    			break;
		    		/* avoiding setTime() calls in the next few options so I don't have to worry about
		    		the day rolling forward or back when setting relative times. */
		    		case "MTD":
		    			//begindate = enddate.clone().moveToFirstDayOfMonth().setHours(0,0,0,0);
		    			begindate = new Date(enddate.getFullYear(),enddate.getMonth(),enddate.getDay()).moveToFirstDayOfMonth();
		    			break;
		    		case "WTD":
		    			//begindate = Date.today().last().sunday();
		    			begindate = new Date(enddate.getFullYear(),enddate.getMonth(),enddate.getDate()).last().sunday();
		    			//begindate = enddate.last().sunday();
		    			break;	    			
		    		case "HOUR":
		    			begindate = new Date(enddate - (3600 * 1000));
		    			break;
		    		default:
		    			//do nothing
		    			return;	
		    	}
		    
	        
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
			              
			    $("#timeframe").val("CURRENT_BLANK");
		        	
		        	    
		        	    
		    });	
		 });
		    <?php 
		
	}

}



?>