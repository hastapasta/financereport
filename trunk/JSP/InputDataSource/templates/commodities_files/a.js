document.write('<!-- Copyright 2008 DoubleClick, a division of Google Inc. All rights reserved. -->\n<!-- Code auto-generated on Mon Apr 23 19:03:07 EDT 2012 -->\n<script src=\"http://s0.2mdn.net/879366/flashwrite_1_2.js\"><\/script>');document.write('\n');

function DCFlash(id,pVM){
var swf = "http://s0.2mdn.net/2530996/Schwab_AT_Q112_Edge-MomentumTool_728x90.swf";
var gif = "http://s0.2mdn.net/2530996/Schwab_AT_Q112_Edge-MomentumTool_728x90.jpg";
var minV = 9;
var FWH = ' width="728" height="90" ';
var url = escape("http://ad.doubleclick.net/click%3Bh%3Dv8/3ccf/3/0/%2a/m%3B259440384%3B8-0%3B0%3B83533205%3B3454-728/90%3B47863113/47878441/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Efdr%3D260345464%3B0-0%3B0%3B62115419%3B3454-728/90%3B49122899/49118317/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Esscs%3D%3fhttp://activetrader.schwab.com/trading-tools/software-trading/overview.aspx?offer=PLU");
var fscUrl = url;
var fscUrlClickTagFound = false;
var wmode = "opaque";
var bg = "";
var dcallowscriptaccess = "always";

var openWindow = "false";
var winW = 0;
var winH = 0;
var winL = 0;
var winT = 0;

var moviePath=swf.substring(0,swf.lastIndexOf("/"));
var sm=new Array();


var defaultCtVal = escape("http://ad.doubleclick.net/click%3Bh%3Dv8/3ccf/3/0/%2a/m%3B259440384%3B8-0%3B0%3B83533205%3B3454-728/90%3B47863113/47878441/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Efdr%3D260345464%3B0-0%3B0%3B62115419%3B3454-728/90%3B49122899/49118317/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Esscs%3D%3fhttp://activetrader.schwab.com/trading-tools/software-trading/overview.aspx?offer=PLU");
var ctp=new Array();
var ctv=new Array();
ctp[0] = "clicktag";
ctv[0] = "";


var fv='"moviePath='+moviePath+'/'+'&moviepath='+moviePath+'/';
for(i=1;i<sm.length;i++){if(sm[i]!=""){fv+="&submovie"+i+"="+escape(sm[i]);}}
for(var ctIndex = 0; ctIndex < ctp.length; ctIndex++) {
  var ctParam = ctp[ctIndex];
  var ctVal = ctv[ctIndex];
  if(ctVal != null && typeof(ctVal) == 'string') {
    if(ctVal == "") {
      ctVal = defaultCtVal;
    }
    else {
      ctVal = escape("http://ad.doubleclick.net/click%3Bh%3Dv8/3ccf/3/0/%2a/m%3B259440384%3B8-0%3B0%3B83533205%3B3454-728/90%3B47863113/47878441/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Efdr%3D260345464%3B0-0%3B0%3B62115419%3B3454-728/90%3B49122899/49118317/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Esscs%3D%3f" + ctVal);
    }
    if(ctParam.toLowerCase() == "clicktag") {
      fscUrl = ctVal;
      fscUrlClickTagFound = true;
    }
    else if(!fscUrlClickTagFound) {
      fscUrl = ctVal;
    }
    fv += "&" + ctParam + "=" + ctVal;
  }
}
fv+='"';
var bgo=(bg=="")?"":'<param name="bgcolor" value="#'+bg+'">';
var bge=(bg=="")?"":' bgcolor="#'+bg+'"';
function FSWin(){if((openWindow=="false")&&(id=="DCF0"))alert('openWindow is wrong.');
var dcw = 800;
var dch = 600;
// IE
if(!window.innerWidth)
{
  // strict mode
  if(!(document.documentElement.clientWidth == 0))
  {
    dcw = document.documentElement.clientWidth;
    dch = document.documentElement.clientHeight;
  }
  // quirks mode
  else if(document.body)
  {
    dcw = document.body.clientWidth;
    dch = document.body.clientHeight;
  }
}
// w3c
else
{
  dcw = window.innerWidth;
  dch = window.innerHeight;
}
if(openWindow=="center"){winL=Math.floor((dcw-winW)/2);winT=Math.floor((dch-winH)/2);}window.open(unescape(fscUrl),id,"width="+winW+",height="+winH+",top="+winT+",left="+winL+",status=no,toolbar=no,menubar=no,location=no");}this.FSWin = FSWin;
ua=navigator.userAgent;
if(minV<=pVM&&(openWindow=="false"||(ua.indexOf("Mac")<0&&ua.indexOf("Opera")<0))){
	var adcode='<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" id="'+id+'"'+FWH+'>'+
		'<param name="movie" value="'+swf+'"><param name="flashvars" value='+fv+'><param name="quality" value="high"><param name="wmode" value="'+wmode+'"><param name="base" value="'+swf.substring(0,swf.lastIndexOf("/"))+'"><PARAM NAME="AllowScriptAccess" VALUE="'+dcallowscriptaccess+'">'+bgo+
		'<embed src="'+swf+'" flashvars='+fv+bge+FWH+' type="application/x-shockwave-flash" quality="high" swliveconnect="true" wmode="'+wmode+'" name="'+id+'" base="'+swf.substring(0,swf.lastIndexOf("/"))+'" AllowScriptAccess="'+dcallowscriptaccess+'"></embed></object>';
  if(('j'!="j")&&(typeof dclkFlashWrite!="undefined")){dclkFlashWrite(adcode);}else{document.write(adcode);}
}else{
	document.write('<a target="_blank" href="'+unescape(url)+'"><img src="'+gif+'"'+FWH+'border="0" alt="Advertisement" galleryimg="no"></a>');
}}
function getFlashVersion(){
// code derived from SWFObject (http://code.google.com/p/swfobject/)
 var vfv = "0,0,0";
 try {
 try {
   var axo = new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");
     try {axo.AllowScriptAccess = "always"; }catch(e) {return "6";}
 }catch(e) {}
 vfv = new ActiveXObject("ShockwaveFlash.ShockwaveFlash").GetVariable("$version");}
 catch(e) {
   try {if(navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin){vfv= navigator.plugins["Shockwave Flash"].description;}}
   catch(e) {}
 }
 return vfv.replace(/\D+/g, ",").match(/^,?(.+),?$/)[1].split(',').shift();
}
var DCid=(isNaN("259440384"))?"DCF2":"DCF259440384";
var pVM=getFlashVersion();
eval("function "+DCid+"_DoFSCommand(c,a){if(c=='openWindow')o"+DCid+".FSWin();}o"+DCid+"=new DCFlash('"+DCid+"',pVM);");
//-->

document.write('\n<noscript><a target=\"_blank\" href=\"http://ad.doubleclick.net/click%3Bh%3Dv8/3ccf/3/0/%2a/m%3B259440384%3B8-0%3B0%3B83533205%3B3454-728/90%3B47863113/47878441/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Efdr%3D260345464%3B0-0%3B0%3B62115419%3B3454-728/90%3B49122899/49118317/1%3Bu%3Dsz%3D728x90|rsi%3D10284%2C10629%2C10737%2C10645%2C10646%2C10149%2C10384%2C10686%2C10761%2C10766%2C10872%2C10874%2C10751%2C10756%2C10758|status%3Dmarketstatus1|position%3Dtop%3B%7Esscs%3D%3fhttp://activetrader.schwab.com/trading-tools/software-trading/overview.aspx?offer=PLU\"><img src=\"http://s0.2mdn.net/2530996/Schwab_AT_Q112_Edge-MomentumTool_728x90.jpg\" width=\"728\" height=\"90\" border=\"0\" alt=\"Advertisement\" galleryimg=\"no\"></a></noscript>\n<img src=\"http://secure-us.imrworldwide.com/cgi-bin/m?ci=mccann-ca&at=view&rt=banner&st=flash&ca=6771126&cr=83533205_259440384_47863113&pc=930032&ce=DART&rnd=6024212\"/>');
