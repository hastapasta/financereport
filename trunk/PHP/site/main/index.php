<?php

include ("../includes/sitecommon.php");

?>

<!DOCTYPE HTML>
<HTML>
<HEAD>
<link rel="stylesheet" href="../includes/style.css" type="text/css" />
<script src="https://www.google.com/jsapi?key=ABQIAAAAxIClsZ3ToqpAEYJ0xpbYDBQvMn8QNL-nLRnNjyJkuSEqYss18BSxvSrpXIIMYm_A6P2cdVBEmC64UA" type="text/javascript"></script>
<script language="Javascript" type="text/javascript">    
/*
 *  How to use the Feed Control to grab, parse and display feeds.
*/
    
google.load("feeds", "1");
    
function OnLoad() {
	// Create a feed control
	var feedControl = new google.feeds.FeedControl();
	    
	// Add two feeds.
	feedControl.addFeed("http://pikefin.blogspot.com/feeds/posts/default", "Pikefin Blog");
	    
	// Draw it.
	feedControl.draw(document.getElementById("blogcontrol"))
	}
    
google.setOnLoadCallback(OnLoad);
</script>

<script src="http://widgets.twimg.com/j/2/widget.js"></script>





</HEAD>

<BODY>
<div id="jq-siteContain">

<div id="jq-header">



<?php
logo();
primaryNav("home");
?>
</div> <!-- header -->
<!-- <div id="jq-content"> -->
<div id="jq-intro"> 

<BR/><BR/><BR/><BR/>


<BR/><BR/>
<p>Providing financial data analysis and alerting services.</p>
</div> <!-- intro -->
<div id="blogcontrol" style="float:right;border: 2px solid #fff; width: 200px;font-size: 16px;margin: 25px 0 20px 10px">Loading...</div>
<div id="twittercontrol" style="float:right;clear:right">
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
<div id="disclaimer" style="float:left; clear:both; margin: 20px 0 0 0">
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
<!-- </div> --><!-- content -->
</div><!--  siteContain -->
</BODY>
</HTML>
