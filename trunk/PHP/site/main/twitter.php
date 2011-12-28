<?php
include ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';


?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<?php IncFunc::jQuery();?>
<?php IncFunc::generalDateFunctions();?>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>



<?php IncFunc::linkStyleCSS();?>




<?php IncFunc::googleAnalytics();?>
<style type="text/css">
table.sample {
	border-width: 2px;
	border-spacing: 0px;
	border-color: white;
	border-style: solid;
}
table.sample td {
	border-width: 1px;
	border-spacing: 0px;
	border-color: white;
	border-style: solid;
}
</style>
</head>

<body>
<div id="jq-siteContain">



<?php
IncFunc::header1("home");
?>

<!-- <div id="jq-content"> -->
<div id="custom-body" style="margin: 20px 50px;">

<!-- 
To do: 
-Put a white background on the text.
-Add the twitter control to the page.
-finish the table with the alert parameters.
-set up the page and link to a table of all of the twitter alerts.




 -->




<div id="jq-intro">
<h1>Info Regarding Our pikefindotcom Twitter Feed</h1>
<p>1) Our overall primary goal is to provide timely and meaningful financial information through a variety of delivery formats, one of which is twitter. 
<br/><br/>
2) We monitor prices of several different asset classes and send out alerts when price movements of a certain magnitude occur over specific time frames. See below for the detailed breakdown.
<br/><br/>
3) Because of twitter restrictions, we place an overall limit on the rate of tweets at 2 tweets per every 15 minutes.
 You can see the full list of generated alerts by going to <a href="<?php echo IncFunc::$PHP_ROOT_PATH.'/charts/alerts/twittertable.php'?>">this page.</a>
<br/><br/>
4) We try to add hash tags that are meaningful in describing the nature of the tweet content.
<br/><br/>
5) If there is a particular financial entity you would like to see us monitor, or for any other questions, comments or requests, please do not hesitate to contact us at <a href="mailto:pikefin1@gmail.com">pikefin1@gmail.com</a>.
</p>

<br/><br/>

<table class="sample">
<tr><td>Global Equity Indexes</td></tr>
<tr><td></td><td>Hour</td><td>+/- 2%</td></tr>
<tr><td></td><td>Day</td><td>+/- 3%</td></tr>
<tr><td></td><td>Month</td><td>+/- 10%</td></tr>
<tr><td></td><td>Year</td><td>+/- 30%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 75%</td></tr>
<tr><td>Foreign Exchange Rates</td></tr>
<tr><td></td><td>Hour</td><td>+/- 2%</td></tr>
<tr><td></td><td>Day</td><td>+/- 3%</td></tr>
<tr><td></td><td>Month</td><td>+/- 5%</td></tr>
<tr><td></td><td>Year</td><td>+/- 10%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 20%</td></tr>
<tr><td>Commodity Futures</td></tr>
<tr><td></td><td>Hour</td><td>+/- 2%</td></tr>
<tr><td></td><td>Day</td><td>+/- 3%</td></tr>
<tr><td></td><td>Month</td><td>+/- 5%</td></tr>
<tr><td></td><td>Year</td><td>+/- 25%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 50%</td></tr>
<tr><td>Soverign Credit Default Swaps</td></tr>
<tr><td></td><td>Hour</td><td>+/- 3%</td></tr>
<tr><td></td><td>Day</td><td>+/- 5%</td></tr>
<tr><td></td><td>Month</td><td>+/- 10%</td></tr>
<tr><td></td><td>Year</td><td>+/- 50%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 100%</td></tr>
<!-- <tr><td>Sovereign Bond Prices</td></tr>
<tr><td></td><td>Hour</td><td>+/- </td></tr>
<tr><td></td><td>Day</td></tr>
<tr><td></td><td>Month</td></tr>
<tr><td></td><td>Year</td></tr>
<tr><td></td><td>All Time</td></tr> -->
<tr><td>Global Equity Index Futures</td></tr>
<tr><td></td><td>Hour</td><td>+/- 2%</td></tr>
<tr><td></td><td>Day</td><td>+/- 3%</td></tr>
<tr><td></td><td>Month</td><td>+/- 10%</td></tr>
<tr><td></td><td>Year</td><td>+/- 30%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 75%</td></tr>
<tr><td>S&amp;500 500 Individual Stocks</td></tr>
<tr><td></td><td>Hour</td><td>+/- 3%</td></tr>
<tr><td></td><td>Day</td><td>+/- 5%</td></tr>
<tr><td></td><td>Month</td><td>+/- 10%</td></tr>
<tr><td></td><td>Year</td><td>+/- 50%</td></tr>
<tr><td></td><td>All Time</td><td>+/- 90%</td></tr>
</table>

</div> <!-- jq-intro -->




</div><!--  custom-body -->



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
</body>
</html>
