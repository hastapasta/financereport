<?php

include ("../../site/includes/sitecommon.php");

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/PHP/site/includes/style3.css" type="text/css" />


<!-- Combo-handled YUI CSS files: -->
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/combo?2.8.2r1/build/reset-fonts-grids/reset-fonts-grids.css&2.8.2r1/build/menu/assets/skins/sam/menu.css&2.8.2r1/build/button/assets/skins/sam/button.css">
<!-- Combo-handled YUI JS files: -->
<script type="text/javascript" src="http://yui.yahooapis.com/combo?2.8.2r1/build/yuiloader-dom-event/yuiloader-dom-event.js&2.8.2r1/build/container/container_core-min.js&2.8.2r1/build/menu/menu-min.js&2.8.2r1/build/element/element-min.js&2.8.2r1/build/button/button-min.js"></script>


<script type="text/javascript" src="../../site/includes/yuiDropDown.js"></script>

<script type="text/javascript">



YAHOO.util.Event.onContentReady("productsandservices", yuiCallBack);


</script>



</head>

<body>

<div id="jq-siteContain">

<div id="jq-header">
<?php
logo();
primaryNav("charts");
chartSecondaryNav();
?>
</div> <!-- header -->

<!-- <div id="jq-whosUsing"> -->

<div class="yui-skin-sam" id="yahoo-com">
<div id="doc" class="yui-t1" width="30em">
<div id="productsandservices" class="yuimenubar yuimenubarnav">
    <div class="bd">
        <ul class="first-of-type">
            <li class="yuimenubaritem first-of-type">
                <a class="yuimenubaritemlabel" href="#communication">Communication</a>
            </li>
            <li class="yuimenubaritem">
                <a class="yuimenubaritemlabel" href="http://shopping.yahoo.com">Shopping</a>
            </li>
            <li class="yuimenubaritem">
                <a class="yuimenubaritemlabel" href="http://entertainment.yahoo.com">Entertainment</a>
            </li>
            <li class="yuimenubaritem">
                <a class="yuimenubaritemlabel" href="#">Information</a>
            </li>
        </ul>
    </div>
</div>
</div>
</div>

<BR/><BR/>


<!-- </div> --><!-- who's using -->
</div> <!--  siteContain -->
</body>
</html>