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
      feedControl.addFeed("http://pikefin.blogspot.com/feeds/posts/default", "Pikefin");
    
      // Draw it.
      feedControl.draw(document.getElementById("blogcontrol"));
    }
    
    google.setOnLoadCallback(OnLoad);
    </script>

</HEAD>

<BODY>
<div id="jq-siteContain">

<div id="jq-header">
<?php
logo();
primaryNav("blog");
?>
</div> <!-- header -->

<div id="jq-intro" class="jq-clearfix">

<BR/><BR/><BR/><BR/>
<h2>Pike Financial Data Services</h2>
<BR/><BR/>
<!-- <p>Blog goes here.</p> -->
<div id="blogcontrol" style="border: 2px solid #fff; width: 200px;font-size: 16px">Loading...</div> 
</div> <!-- intro -->
</div> <!--  siteContain -->
</BODY>
</HTML>
