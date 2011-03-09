<?php


function logo()
{
	echo "<a id=\"jq-siteLogo\" href=\"http://www.pikefin.com\" title=\"PikeFin Home\"><img src=\"/PHP/site/images/33pctsizecrop.jpg\"/></a>";
}

function primaryNav($Page)
{
	

		
	
	
	
	
	
	echo "<div id=\"jq-primaryNavigation\">\n";
	echo "<ul>\n";
	echo "<li class=\"jq-home ".($Page=="home"?"jq-current":"")."\"><a href=\"/PHP/site/main/index.php\" title=\"Pikefin Home\">Home</a></li>\n";
	echo "<li class=\"jq-alerts ".($Page=="alerts"?"jq-current":"")."\"><a href=\"/PHP/site/main/alerts.php\" title=\"Pikefin Alert Manager\">Alert Manager</a></li>\n";
	echo "<li class=\"jq-charts ".($Page=="charts"?"jq-current":"")."\"><a href=\"/PHP/charts/fed/balsheet.php\" title=\"Pikefin Charts\">Charts</a></li>";
	//echo "<li class=\"jq-blog ".($Page=="blog"?"jq-current":"")."\"><a href=\"/PHP/site/main/blog.php\" title=\"Pikefin Blog\">Blog</a></li>";
	//echo "<li class=\"jq-about ".($Page=="about"?"jq-current":"")."\"><a href=\"/PHP/site/main/about.php\" title=\"About Pikefin\">About</a></li>";
	echo "</ul>\n";
	echo "</div><!-- /#primaryNavigation -->\n";
	
	
	
	
}

function chartSecondaryNav()
{
	
	echo "<div id=\"jq-secondaryNavigation\">\n";
	echo "<ul>\n";
	echo "<li class=\"jq-fedreserve jq-first\"><a href=\"../../charts/fed/balsheet.php\">Federal Reserve Main</a></li>\n";
	echo "<li class=\"jq-allassets\"><a href=\"../../charts/allassets/tablesample3A.php\">All Assets</a></li>\n";
	echo "<li class=\"jq-forex\"><a href=\"../../charts/forex/forex.php\">Foreign Exchange</a></li>\n";
	echo "<li class=\"jq-commodities\"><a href=\"../../charts/commods/commodities.php\">Commodities</a></li>\n";
	echo "<li class=\"jq-equities jq-last\"><a href=\"../../charts/usequities/usequities.php\">U.S Equities</a></li>\n";
	echo "</ul>\n";
	echo "</div><!-- /#secondaryNavigation -->\n";
	
}




?>