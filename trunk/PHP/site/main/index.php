<?php
include ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';


?>

<!DOCTYPE HTML>
<HTML>
<HEAD>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>

<?php IncFunc::blogFeedJavaScript();?>

<?php //IncFunc::iquery(); ?>


<script src="http://widgets.twimg.com/j/2/widget.js" type="text/javascript"></script>

<link rel="stylesheet" href="../includes/style.css" type="text/css" />

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

//special case 





//var element = $('.twtr-hdr h3');
//alert(element.type);
//alert(get_type(element));
//element.style.fontSize="17px";

</script>

</HEAD>

<BODY>
<div id="jq-siteContain">



<?php
IncFunc::header1("home");
?>

<!-- <div id="jq-content"> -->
<div id="jq-intro"> 

<BR/><BR/><BR/><BR/>


<BR/><BR/>
<p>Providing financial data analysis and alerting services.</p>
<br/><br/>
<p>Note: This website is an alpha release. Please bear with us as we expand the functionality of the site over the coming days and weeks. Thank you for your patience.</p>
<br/>
<p>For questions or requests please contact <a href="mailto:pikefin1@gmail.com">pikefin1@gmail.com</a></p>
</div> <!-- intro -->

<div id="twittercontrol" style="float:right;margin: 25px 0 0 10px"> 
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
<div id="blogcontrol" style="float:right;clear:right;border: 2px solid #fff; background: #000; width: 250px;font-size: 15px;margin: 25px 0 20px 10px">Loading...</div>
<div id="disclaimer" style="float:left; clear:both; margin: 20px 0 20px 0">
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
