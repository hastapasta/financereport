/* Generated: 08/06/2012  01:06 PM EST. EW Web Code Version: 18.5.1 */
var allowIframeDetection=false;
var cp1720451="http://cdn.eyewonder.com/100125/775355/1720451/";
var adcp1720451="http://cdn.eyewonder.com/100125/775355/1720451/";

var ew_isIFrame=false; if(window!=top&&self!=parent) ew_isIFrame=true;

var ew1720451_inFIF=false;

if(typeof(inDapIF)!="undefined"&&inDapIF==true) ew1720451_inFIF=true;
if(ew1720451_inFIF){ ew_isIFrame=false; var topWin=parent; }

var ew1720451_startTime=ew_getTime1720451();
if(typeof(ew1720451_cachebuster)=='undefined'||isNaN(ew1720451_cachebuster)) var ew1720451_cachebuster=ew1720451_startTime;

function ew_getTime1720451(){ return (new Date().getTime()); }

function parseParam(name){ try{ var query=window.location.search; var pos=query.indexOf(name+"="); var pos2=0; if(pos!=-1){ pos=pos+name.length+1; pos2=query.indexOf("&", pos); if(pos2!=-1){ return query.substring(pos, pos2); } else { return query.substring(pos); } } else { return null; } } catch(e){ return null; } }

var ew_cacheBusterReplaceArray=["\\[timestamp\\]", "\\[cachebuster\\]", "\\[random\\]", "\\[randnum\\]" , "\\[randomnumber\\]", "<randomnumber>"];
function ew_newGuid(){ var chars='0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split(''); var result=''; for(var i=0; i<22; i++){ result+=chars[Math.floor(Math.random()*chars.length)]; }; return result; }
if(typeof(ew1720451_guidVal)=='undefined'||ew1720451_guidVal=='null') var ew1720451_guidVal=ew_newGuid();
var ew1720451_imgArray=[], ew1720451_imgArrayCounter=0, ew1720451_realAdIdStr='160988';

function ew_addImage1720451(img){
	ew1720451_imgArray[ew1720451_imgArrayCounter]=document.createElement("img");
	ew1720451_imgArray[ew1720451_imgArrayCounter].setAttribute("src", img);
	ew1720451_imgArrayCounter++;
	return true;
}
function ew1720451_addEvt(obj,evt,fn,bool){ if(!bool) var bool=false; try{ if(window.addEventListener) obj.addEventListener(evt,fn,bool); else if(window.attachEvent) obj.attachEvent(evt,fn); } catch(e){}}

function ew1720451_otrk(img,extra,newtrk){ if(!extra) extra='';if(img) void(ew_addImage1720451(cp1720451+img+'?ewadid='+ew1720451_realAdIdStr+'&ewbust='+ew1720451_cachebuster+extra+'&guid='+ew1720451_guidVal)); return '';
}
function ew_thirdpartytrackingcachebust1720451(trkURL){ for(var i=0, l=ew_cacheBusterReplaceArray.length; i<l; i++) trkURL=trkURL.replace(new RegExp(ew_cacheBusterReplaceArray[i],"gi"), ew1720451_cachebuster); return trkURL; }
function onLoadTracker1720451(num){ var infoStr="", ew_t=(ew_getTime1720451()-ew1720451_startTime);
	if(!isNaN(ew_t)) infoStr+=('&info='+ew_t); if(typeof(num)!='undefined') infoStr+=("&loads="+num); return ew1720451_otrk('ewtrack_onload.gif',infoStr);
}
function flashTracker1720451(){ return ew1720451_otrk('ewtrack_v.gif',''); }

function impressionTracker1720451(){ var extra=""; if(typeof(ew_epid)!='undefined'){ if(typeof(ew160988_noEpid)!='undefined') extra="&epid="+ew_epid+"&val=bad"; else extra="&epid="+ew_epid+"&val=ok"; } return ew1720451_otrk('ewtrack.gif',extra); }

function failoverTracker1720451(){ return ew1720451_otrk('ewtrack_f.gif',''); }
function failoverReasonBrowserTracker1720451(){ return ew1720451_otrk('ewtrack_rb.gif',''); }
function failoverReasonFlashTracker1720451(){ return ew1720451_otrk('ewtrack_rf.gif',''); }
function flashSupportTracker1720451(flVer){ return ''; }

function weSupportTracker1720451(){ var ew_t=ew_getTime1720451()-ew1720451_startTime; return ew1720451_otrk('ewtrack_wesupport.gif','&info='+ew_t); }

void(impressionTracker1720451());


function ew_thirdpartytracking1720451(){ for(var i=0, l=ew1720451_thirdPartyTrackers.length; i<l; i++){ if(ew1720451_thirdPartyTrackers[i]!="") void(ew_addImage1720451(ew_thirdpartytrackingcachebust1720451(ew1720451_thirdPartyTrackers[i]))); }}
var ew1720451_thirdPartyTrackers=["http://ad.doubleclick.net/ad/N4359.bloomberg.comOX2260/B6044525.279;sz=1x1;ord=[timestamp]?","","","","",""];
ew_thirdpartytracking1720451();
var ew1720451_bannerWidth=300, ew1720451_bannerHeight=600;
var ew1720451_fullWidth=300, ew1720451_fullHeight=600;
var ew1720451_psID='204', ew1720451_sID='204', ew1720451_newFormatId="100";

function ew_getProtocol(cp){ if(cp.indexOf("https://")>-1) return "https://"; else return "http://"; }
var ew1720451_protocol=ew_getProtocol("http://cdn.eyewonder.com/100125/775355/1720451/");
if(parseParam("ew_pubVars")) var ew_pubVars=unescape(parseParam("ew_pubVars"));
if(parseParam("ew_clickPrepend")){ var clickTagFramePrepend1720451=parseParam("ew_clickPrepend")+"[ewclickthru]";
	if(clickTagFramePrepend1720451=="%c[ewclickthru]") clickTagFramePrepend1720451="[ewclickthru]";

}

var rtag="<scr"+"ipt src='http://cdn.doubleverify.com/dvtp_src.js?ctx=2875&cmp=[%tp_CampaignID%]&sid=[%tp_PublisherID%]&plc=[%tp_PlacementID%]&adid=[%tp_AdID%]&crt=&dvtagver=6.1.src&region=30&advid=1493&adsrv=5' type='text/javascript'></scr"+"ipt>";
function ew_rtmRepl(rtag,searchval,replaceval){ if(rtag.indexOf(searchval)>-1) return rtag.split(searchval).join(replaceval); return rtag; }
function ew_modifyResearchtagMacros1720451(rtag){
	rtag=ew_rtmRepl(rtag,'SIT=[SITE]','SIT=Bloomberg');
	rtag=ew_rtmRepl(rtag,'CRE=[AD]','CRE=300x600_insight');
	rtag=ew_rtmRepl(rtag,'PLA=[PLACEMENT]','PLA=Bloomberg_ROC_Markets_Q3_300x600_RM_1x1');
	rtag=ew_rtmRepl(rtag,'ADI=[EXECUTIONID]','ADI=1720451');
	rtag=ew_rtmRepl(rtag,'C4=%epid!','C4=300x600_insight');
	rtag=ew_rtmRepl(rtag,'C5=%ecid!','C5=Bloomberg_ROC_Markets_Q3_300x600_RM_1x1');
	rtag=ew_rtmRepl(rtag,'_imp.creative.%ecid!','_imp.creative.300x600_insight');
	rtag=ew_rtmRepl(rtag,'_imp.placement.%eaid!','_imp.placement.Bloomberg_ROC_Markets_Q3_300x600_RM_1x1');
	rtag=ew_rtmRepl(rtag,'_imp.publisher.%epid!','_imp.publisher.Bloomberg');
	rtag=ew_rtmRepl(rtag,'pageid=%epid!','pageid=1720451');
	rtag=ew_rtmRepl(rtag,'cte=%ecid!','cte=160988');
	rtag=ew_rtmRepl(rtag,'cmpid=%ebuy!','cmpid=775355');
	rtag=ew_rtmRepl(rtag,'stid=%esid!','stid=204');
	rtag=ew_rtmRepl(rtag,'impid=%ecid!','impid=160988');
	rtag=ew_rtmRepl(rtag,'[%tp_PublisherName%]','Bloomberg');
	rtag=ew_rtmRepl(rtag,'[%tp_AdID%]','160988');
	rtag=ew_rtmRepl(rtag,'[%tp_PlacementID%]','1720451');
	rtag=ew_rtmRepl(rtag,'[timestamp]',ew1720451_cachebuster);
	rtag=ew_rtmRepl(rtag,'[%tp_PublisherID%]','204');
	rtag=ew_rtmRepl(rtag,'[%tp_CampaignID%]','775355');
	return rtag;
}

ew1720451_extendedResearchTag=ew_modifyResearchtagMacros1720451(rtag);
function ew1720451_writeResTag(){ document.writeln(ew1720451_extendedResearchTag); }
ew1720451_writeResTag();
if(typeof(clickTagFramePrepend160988)!='undefined'&&clickTagFramePrepend160988!='[ewclickthru]'&&clickTagFramePrepend160988!='%c[ewclickthru]') var clickTagFramePrepend=clickTagFramePrepend160988;
else if(typeof(clickTagFramePrepend1720451)!='undefined'&&clickTagFramePrepend1720451!='[ewclickthru]'&&clickTagFramePrepend1720451!='%c[ewclickthru]') var clickTagFramePrepend=clickTagFramePrepend1720451;
if(typeof(clickTagFramePrepend)=="undefined"||clickTagFramePrepend.toLowerCase().substring(0,4)!="http") var clickTagFramePrepend="[ewclickthru]";
else if(clickTagFramePrepend.indexOf('[ewclickthru]')<0) clickTagFramePrepend+='[ewclickthru]';
if(clickTagFramePrepend=="%c[ewclickthru]") clickTagFramePrepend="[ewclickthru]";
var ew_mpUsedClickthruMagic=false;

if(ew_mpUsedClickthruMagic==false){
var failclickTag1720451=clickTagFramePrepend.replace("[ewclickthru]","http://ad.doubleclick.net/clk;259329184;83089047;v");

var clickTag1=clickTagFramePrepend.replace("[ewclickthru]","http://ad.doubleclick.net/clk;259329184;83089047;v");

var clickTag2=clickTagFramePrepend.replace("[ewclickthru]","http://ad.doubleclick.net/clk;259329395;83089047;z");

}
function ew_clickTagCacheBust(_ct){ if(typeof(_ct)=='undefined'||!_ct) return '';
	for(var i=0, l=ew_cacheBusterReplaceArray.length; i<l; i++) _ct=_ct.replace(new RegExp(ew_cacheBusterReplaceArray[i],"gi"), ew1720451_cachebuster);
	if(_ct.indexOf(";ord=")>=0) _ct=_ct.split(";ord=")[0]+";ord="+ew1720451_cachebuster;
	return _ct;
}
clickTag1=ew_clickTagCacheBust(clickTag1);clickTag2=ew_clickTagCacheBust(clickTag2);
if(typeof(failclickTag1720451)=="undefined") var failclickTag1720451="http://ad.doubleclick.net/clk;259329184;83089047;v";
failclickTag1720451=ew_clickTagCacheBust(failclickTag1720451);

var ua=' '+navigator.userAgent.toLowerCase(), ew1720451_operatingSystem="other", isWinVista=ew_isWin7=false;
var geckoRevisionString=geckoRevision=geckoRevisionMajor=geckoRevisionMinor=webkitVersion=ieVersion=operaVersion=geckoDate=ew_ffVersion=ewChromeVer=ewSafariVer=ew1720451_browserEngineVersion=-1;
var ew_cbFlash="", isIe=ew_isIe=(ua.indexOf('msie')>0), isIe8=ew_isIe8=(ua.indexOf('trident/4')>0||ua.indexOf('msie 8')>0), ew_isIe10=(ua.indexOf("trident/6")>0||ua.indexOf('msie 10')>0), isIe9=ew_isIe9=(ua.indexOf("trident/5")>0||ua.indexOf('msie 9')>0), isIe7=ew_isIe7=(ua.indexOf('msie 7')>0), isIe6=ew_isIe6=(ua.indexOf('msie 6')>0);
var isWin=(ua.indexOf('win')>0), isLinux=(ua.indexOf('linux')>0), isMac=(ua.indexOf('mac')>0), ew_isWebKit=(ua.indexOf('applewebkit')>0);
var isOpera=ew_isOpera=(ua.indexOf('opera')>0), isChrome=ew_isChrome=(ew_isWebKit&&ua.indexOf("chrome")>0), ew_isSafari=(ew_isWebKit&&!ew_isChrome&&ua.indexOf("safari")>0), isKonqueror=(ua.indexOf('konqueror')>0), isGecko=ew_isGecko=(ua.indexOf('gecko/')>0), ew_isCamino=ew_isCamino=(ua.indexOf('camino/')>0), ew_ieMobile=(ua.indexOf('iemobile')>0), ew_isMobile=(ew_isWebKit&&ua.indexOf("mobile")>0);
if(isWin){ ew1720451_operatingSystem="windows"; var isWinVista=(ua.indexOf('windows nt 6.0')>0), ew_isWin7=(ua.indexOf('windows nt 6.1')>0); if(isWinVista) ew1720451_operatingSystem="winvista"; else if(ew_isWin7) ew1720451_operatingSystem="windows7";
} else if(isMac) ew1720451_operatingSystem="mac"; else if(isLinux) ew1720451_operatingSystem="linux";
if(ew_isIe){ ieVersion=ua.substr(ua.indexOf('msie')+5,4); if(ieVersion.indexOf(';')>0) ieVersion=ieVersion.substr(0,3); ew1720451_browserEngineVersion=String(ieVersion); }
else if(ew_isGecko){ ew_ffVersion=ua.substr(ua.indexOf("firefox/")+8,5);
	function ew_getGeckoRevisionString(ua){ var tempStr=ua.split("rv:"); return tempStr[1].split(")")[0]; }
	geckoRevisionString=ew_getGeckoRevisionString(ua); ew1720451_browserEngineVersion=geckoRevisionString; geckoRevision=parseFloat(geckoRevisionString); geckoRevisionMajor=parseFloat(geckoRevisionString.split(".")[0]);
	geckoRevisionMinor=parseFloat(geckoRevisionString.split(".")[1]+"."+geckoRevisionString.split(".")[2]); geckoDate=parseInt(ua.substr(ua.indexOf('gecko/')+6,8));
} else if(ew_isWebKit){ webkitVersion=parseInt(ua.substr(ua.indexOf('applewebkit')+12,4)); ew1720451_browserEngineVersion=String(webkitVersion); if(ew_isSafari) ewSafariVer=parseFloat(ua.substr(ua.indexOf('version/')+8)); else if(ew_isChrome) ewChromeVer=parseFloat(ua.substr(ua.indexOf('chrome/')+7)); }
else if(ew_isOpera){ operaVersion=parseFloat(ua.substr(ua.indexOf('version/')+8,5)); if(isNaN(operaVersion)) operaVersion=9.8; ew1720451_browserEngineVersion=String(operaVersion); }
if(ew_isIe&&parseFloat(ieVersion)>8) ew_cbFlash="?cb="+ew1720451_cachebuster;
function ew1720451_revertWK(){ try{ ew_FlashDiv1720451.style.width="300px"; ew_FlashDiv1720451.style.height="600px"; } catch(e){} }
function ew1720451_fixWK(){
	var ewWin=window; if(typeof(ew1720451_foWin)!='undefined') ewWin=ew1720451_foWin;
	if(typeof(ew_FlashDiv1720451)=='undefined'||!ew_FlashDiv1720451) ew_FlashDiv1720451=ewWin.document.getElementById("ew_FlashDiv1720451"); if(ew_FlashDiv1720451){ ew_FlashDiv1720451.style.width="301px"; ew_FlashDiv1720451.style.height="601px"; }
	else { setTimeout(ew1720451_fixWK,1000); return; }; setTimeout(ew1720451_revertWK,500);
}

var ew1720451_adType='Banner';
var ew1720451_adTypeID='100';
var ew1720451_weSupport=false, failReason="browser";

var isFlash7up=false, isFlash8up=false, isFlash9up=false, isFlash10up=false, ew1720451_flashVersionString="", ew1720451_flashVersionMajor=0, ew1720451_flashVersionMinor=0;
var ew1720451_flashVersion=[0,0,0];

function ew_getObjVer(){ for(var v=7; v<=12; ++v){ try{ var axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash."+v); var version=axo.GetVariable("$version"); return version; } catch (e){ return null; } } }
function ew1720451_getFlashVersion(){ if(ew_isIe){ var swf=ew_getObjVer(); if(swf){ var ver=swf.substring(swf.indexOf(" ")); ew1720451_flashVersion=ver.split(","); } else ew1720451_flashVersion=[0,0,0];
	} else { var ver=[]; var desc=navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin.description; desc=desc.split(" "); ver[0]=desc[2].split(".")[0]; ver[1]=desc[2].split(".")[1]; ver[2]=desc[desc.length-1].split("r")[1]; if(ver[0]>=7) ew1720451_flashVersion=ver; }
	ew1720451_flashVersionMajor=parseFloat(ew1720451_flashVersion[0]+"."+ew1720451_flashVersion[1]);
	ew1720451_flashVersionMinor=ew1720451_flashVersion[2]; if(typeof(ew1720451_flashVersionMinor)=="undefined") ew1720451_flashVersionMinor=0;
	ew1720451_flashVersionString=ew1720451_flashVersionMajor+"."+ew1720451_flashVersionMinor;
}
if(navigator.mimeTypes&&navigator.mimeTypes["application/x-shockwave-flash"]) var navPlugin=(navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin);
if(ew_isIe||navPlugin){ ew1720451_getFlashVersion();
	isFlash10up=(ew1720451_flashVersionMajor>=10); isFlash9up=(ew1720451_flashVersionMajor>=9);
	isFlash8up=(ew1720451_flashVersionMajor>=8); isFlash7up=(ew1720451_flashVersionMajor>=7);
} else isFlash7up=false;

if(ew_isIe||ew_isGecko||ew_isWebKit||ew_isOpera||isKonqueror){ failReason="none"; ew1720451_weSupport=true; if(!isFlash8up){ ew1720451_weSupport=false; failReason="flash"; }
} else { failReason="browser"; ew1720451_weSupport=false; }

var ew1720451_turnOffExecution=false;
if(isFlash9up) void(flashSupportTracker1720451("&fv="+ew1720451_flashVersionString));
var ew1720451_isLeaderboard=false, ew1720451_isSkyscraper=true, ew1720451_isRectangle=false;

if(typeof(ew1720451_forceLoadTimer)=='undefined') var ew1720451_forceLoadTimer=2000;
if(isLinux&&isFlash8up) ew1720451_weSupport=true;
var ew1720451_onLoadWasCalled=false, centerTag1="", centerTag2="", ew1720451_positionCorrect="", ew1720451_wMode="opaque", ew1720451_ieAppendDiv=false, ew1720451_afterLoadTimer=1500, ew1720451_forceCenter=true;

if(ew_isIFrame&&allowIframeDetection) ew1720451_positionCorrect=' position:absolute; top:0px; left:0px; ';

ew1720451_turnOffExecutionBackup=ew1720451_turnOffExecution;

/* ####								BEGIN CUSTOM FUNCTIONS								#### */

/* Site-level custom functions */
//307166//function ew_t307166_1720451()//removed top line because of js errors about ew_site not defined/*function ew_siteCF1720451(){	try	{		var ewTarget1 = top.document.getElementById('leader_board');		if(typeof(ewTarget1) != "undefined" && ewTarget1)		{			ewTarget1.style.zIndex = 1;		}		var ewTarget2 = top.document.getElementById('header');		if(typeof(ewTarget2) != "undefined" && ewTarget2)		{			ewTarget2.style.zIndex = 2;		}		else		{			var dummy = setTimeout('ew_siteCF1720451()',500);		}	}	catch (e1720451){}}if((typeof(ew_isIFrame)!='undefined' && ew_isIFrame==false) || typeof(isIFrameContent)!='undefined'){ ew_siteCF1720451(); }*/function ts977_1720451(){	try	{			if (isIe){		var ewTarget = top.document.getElementById('on_air');		if(typeof(ewTarget) != "undefined" && ewTarget)		{			ewTarget.style.display = 'inline-block';		}		else		{			var dummy = setTimeout('ts977_1720451()',500);		}		}	}	catch (e1720451){}}if((typeof(ew_isIFrame)!='undefined' && ew_isIFrame==false) || typeof(isIFrameContent)!='undefined'){ ts977_1720451(); }
/* Ad-level custom functions */
// Convert pageVars into flahVars
ew1720451_flashvarsExtra = ( ew1720451_flashvarsExtra || '' ) + "&" + location.search.substr ( 1 );
/* ####								END CUSTOM FUNCTIONS								#### */

var ew1720451_localConnectionUUID=0;
function ew_setUpLocalConnectionUUID1720451(){ if(typeof(topWin)=="undefined") topWin=window;
	if(typeof(topWin.ew_localConnectionUUID)=="undefined") topWin.ew_localConnectionUUID=(new Date().getTime());
	ew1720451_localConnectionUUID=topWin.ew_localConnectionUUID;
}
ew_setUpLocalConnectionUUID1720451();

if(typeof(weSupport)=='boolean') ew1720451_weSupport=weSupport;
ew1720451_turnOffExecution=ew1720451_turnOffExecutionBackup;

if(typeof(ew1720451_foWin)=='undefined') var ew1720451_foWin=window;

function ew_getSWFTag1720451(){ try{ if(ew_isIe) return ew1720451_foWin.document.getElementById("ewad1720451");
	else return ew1720451_foWin.document.getElementById("ewembed1720451"); } catch(e){ return null; }
}

function ew_delegate1720451(functionName){ var ew_args=[]; if(arguments.length>1){ for(var i=1; i<arguments.length; i++) ew_args[i-1]=arguments[i]; }
	if(functionName.indexOf("ew_delegate")>-1) functionName=ew_args.shift();
	try{ return window[functionName].apply(window, ew_args); } catch(e){ ew1720451_errorObject=e; return; }
}

var ew1720451_embed=null, ew1720451_bannerDiv=null, ew_FlashDiv1720451=null, ew1720451_errorObject="";
function ew_isAvailableEI1720451(){ ew1720451_embed=ew_getSWFTag1720451();
	try{ if(!ew1720451_embed||typeof(ew1720451_embed.ew_isAvailable)=="undefined") return false; } catch(e){ return false; }
	return true;
}

function ew_callExternalInterface1720451(functionName){ if(!ew_isAvailableEI1720451()) return null;
	var flashObj=ew_getSWFTag1720451();
	if(!flashObj||typeof(flashObj[functionName])=="undefined") return null; var ew_args=[], ew_argsString="";
	for(var i=1; i<arguments.length; i++){ if(i!=1) ew_argsString+=","; ew_argsString+="ew_args["+(i-1)+"]"; ew_args[i-1]=arguments[i]; }
	var execString='flashObj.'+functionName+'('+ew_argsString+')';
	try{ return eval(execString); } catch(e){ ew1720451_errorObject=e; return null; }
}

var ew1720451_swfVersion=7, ew1720451_flvNames="Merrill Lynch - INSIGHTS_The Outlook for European Debt_270x152,Merrill Lynch - INSIGHTS_The Outlook for European Debt - 1280x720,Merrill Lynch - INSIGHTS_EquitiesforGrowthandIncome_270x152,Merrill Lynch - INSIGHTS_EquitiesforGrowthandIncome - 1280x720,Merrill Lynch - INSIGHTS_6 Market Indicators_270x152,Merrill Lynch - INSIGHTS_6 Market Indicators - 1280x720,Merrill Lynch - INSIGHTS_Volatility_270x152,Merrill Lynch - INSIGHTS_Volatility - 1280x720,_Component Test Video 120x90,_Component Test Video 160x120,Merrill Lynch - Weighing_Value_Gold_Whitepaper270x152,Merrill Lynch - Weighing_Value_Gold_Whitepaper1280x720,Merrill Lynch - Next_Chapter_in_Chinas_Growth_270x152,Merrill Lynch - Next_Chapter_in_Chinas_Growth_1280x720,BAC_US_ML WM Advisory 2011 - Jobs_Youtube - 270x152,BAC_US_ML WM Advisory 2011 - Jobs_Youtube - 1280x720,Merrill Lynch - Great Global Shift - 270x151,Merrill Lynch - Great Global Shift - 1280x720,Merrill Lynch - Tax_Moves_Youtube_iTunes - 270x152,Merrill Lynch - Tax_Moves_Youtube_iTunes - 1280x720,BAC_US_ML Innovation Through Gaming 270x152 v2,BAC_US_ML Innovation Through Gaming 1280x720,BAC_US_ML GWMA BBG Insights- Eurozone Update 6-19-12 270x152,BAC_US_ML GWMA BBG Insights- Eurozone Update 6-19-12Option 1280x720,BAC_US_ML GWMA BBG Seeking Growth in a G-Zero World Full Program 270x152 v2,BAC_US_ML GWMA BBG Seeking Growth in a G-Zero World Full Program 1280x720 v2,BAC_US_ML GWMA BBG Defining_the_G_Zero_World 270x152,BAC_US_ML GWMA BBG Defining_the_G_Zero_World 1280x720 v2", ew1720451_creativeName="";

function ew_getfNames(flvvideos,flashBool,h264bool){ for(var i=0, l=flashBool.length; i<l; i++){ if(flashBool[i]=="true") flvvideos[i]="fl8_"+flvvideos[i]; }; return flvvideos.join(","); }

function ew_setupCreative(flash8Creative, flash9Creative, flash10Creative){
	if(isFlash10up&&flash10Creative!=""){ ew1720451_swfVersion=10; return flash10Creative;
	} else if(isFlash9up&&flash9Creative!=""){ ew1720451_swfVersion=9; return flash9Creative;
	} else if(isFlash8up&&flash8Creative!=""){ ew1720451_swfVersion=8; return flash8Creative;
	} else { ew1720451_weSupport=false; failReason="flash"; return ''; }
}

ew1720451_creativeName=ew_setupCreative("","insight_300x600.swf","");
function ew_setupFlash8Videos(){ var flvvideos=new String("Merrill Lynch - INSIGHTS_The Outlook for European Debt_270x152,Merrill Lynch - INSIGHTS_The Outlook for European Debt - 1280x720,Merrill Lynch - INSIGHTS_EquitiesforGrowthandIncome_270x152,Merrill Lynch - INSIGHTS_EquitiesforGrowthandIncome - 1280x720,Merrill Lynch - INSIGHTS_6 Market Indicators_270x152,Merrill Lynch - INSIGHTS_6 Market Indicators - 1280x720,Merrill Lynch - INSIGHTS_Volatility_270x152,Merrill Lynch - INSIGHTS_Volatility - 1280x720,_Component Test Video 120x90,_Component Test Video 160x120,Merrill Lynch - Weighing_Value_Gold_Whitepaper270x152,Merrill Lynch - Weighing_Value_Gold_Whitepaper1280x720,Merrill Lynch - Next_Chapter_in_Chinas_Growth_270x152,Merrill Lynch - Next_Chapter_in_Chinas_Growth_1280x720,BAC_US_ML WM Advisory 2011 - Jobs_Youtube - 270x152,BAC_US_ML WM Advisory 2011 - Jobs_Youtube - 1280x720,Merrill Lynch - Great Global Shift - 270x151,Merrill Lynch - Great Global Shift - 1280x720,Merrill Lynch - Tax_Moves_Youtube_iTunes - 270x152,Merrill Lynch - Tax_Moves_Youtube_iTunes - 1280x720,BAC_US_ML Innovation Through Gaming 270x152 v2,BAC_US_ML Innovation Through Gaming 1280x720,BAC_US_ML GWMA BBG Insights- Eurozone Update 6-19-12 270x152,BAC_US_ML GWMA BBG Insights- Eurozone Update 6-19-12Option 1280x720,BAC_US_ML GWMA BBG Seeking Growth in a G-Zero World Full Program 270x152 v2,BAC_US_ML GWMA BBG Seeking Growth in a G-Zero World Full Program 1280x720 v2,BAC_US_ML GWMA BBG Defining_the_G_Zero_World 270x152,BAC_US_ML GWMA BBG Defining_the_G_Zero_World 1280x720 v2").split(',');
	var flashBool=new String("true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true").split(',');
	ew1720451_flvNames=ew_getfNames(flvvideos,flashBool);
}
if(isFlash8up&&(ew1720451_swfVersion>=8)) ew_setupFlash8Videos();
var ew1720451_adMode="stream";

var ew1720451_videoPath="fms2.eyewonder/video/";
var ew1720451_videoID='68095,68424,68089,68423,68134,68422,68135,68425,6625,6626,71458,71457,71772,71773,72670,72675,73705,73890,74412,74414,85613,85786,86321,86322,86869,86870,86687,87183';
if(typeof(isIFrameContent)!="undefined"&&isIFrameContent) ew_qaReportUUID=topWin.ew_qaReportUUID;

if(typeof(ew_qaReportUUID)=="undefined"){ ew_qaReportUUID=parseParam("qaUUID"); if(!ew_qaReportUUID) ew_qaReportUUID="common"; }


function ew_verifyPrepend(url){ if(typeof(url)=='undefined') return ""; var newUrl=url; if(url.indexOf("http")!=0||url.indexOf("ftp")!=0){ if(url.indexOf("http")>-1) newUrl=url.substr(url.indexOf("http")); else if(url.indexOf("ftp")>-1) newUrl=url.substr(url.indexOf("ftp")); }; return newUrl; }

var ewclicktags1720451=''
+'&clickTag1='+encodeURIComponent(ew_verifyPrepend(clickTag1))
+'&clickTag2='+encodeURIComponent(ew_verifyPrepend(clickTag2));

if(typeof(ew1720451_flashvarsExtra)=="undefined") var ew1720451_flashvarsExtra="";
if(typeof(ew1720451_setVideoMode)!="undefined"){
	if((ew1720451_setVideoMode=='prog')||(ew1720451_setVideoMode=='stream')){
	if(cp1720451.indexOf("cdn1")>-1){ var vp="cdn1"; var fldr="2"; } else { var vp="cdn"; var fldr="1"; }
	ew1720451_adMode=ew1720451_setVideoMode; if(ew1720451_setVideoMode=='prog') ew1720451_videoPath="http://"+vp+".eyewonder.com/"+fldr+"00125/video/"; else ew1720451_videoPath="fms2.eyewonder/video/";
	}
}
var ew1720451_browserEngine="unknown";
if(ew_isIe) ew1720451_browserEngine="ie";
else if(ew_isGecko) ew1720451_browserEngine="gecko";
else if(ew_isOpera||isKonqueror) ew1720451_browserEngine="opera";
else if(ew_isWebKit) ew1720451_browserEngine="webkit";
if(typeof(ew1720451_localConnectionUUID)=="undefined"||(ew_isIFrame&&!ew1720451_inFIF)) ew1720451_localConnectionUUID=3979853562951413; else if(typeof(ew1720451_lcOverride)!='undefined') ew1720451_localConnectionUUID=ew1720451_lcOverride;
var ew1720451_customFlashString='version=14insight_gzeroworld';
if(ew1720451_customFlashString!='') ew1720451_customFlashString='&'+ew1720451_customFlashString.replace(/\|/g, '&');
if(typeof(ew_pubVars)=='undefined'||String(ew_pubVars)=='null'){

	var ew_pubVars="setVideoMode=stream"; ew_pubVars=ew_pubVars.replace(/\|/g, '&');

} else { if(ew_pubVars.charAt(0)=="&") ew_pubVars=ew_pubVars.substring(1); if(ew_pubVars.indexOf("bw=")>-1) ew_pubVars=ew_pubVars.replace("bw=", "bandWidth="); }
if(ew_pubVars.indexOf('setVideoMode=stream')>-1){ ew1720451_adMode='stream'; ew1720451_videoPath="fms2.eyewonder/video/";
} else if(ew_pubVars.indexOf('setVideoMode=prog')>-1){ if(cp1720451.indexOf("cdn1")>-1){ var vp="cdn1"; var fldr="2"; } else { var vp="cdn"; var fldr="1"; }
	ew1720451_adMode='prog'; ew1720451_videoPath="http://"+vp+".eyewonder.com/"+fldr+"00125/video/";
}
var flashvarsClickTagPrepend="[ewclickthru]";
if(typeof(clickTagFramePrepend)!='undefined') flashvarsClickTagPrepend=clickTagFramePrepend;
var ewflashvars1720451='ewbase='+adcp1720451
+'&bwfile=bwtest.swf&creative='+ew1720451_creativeName
+'&vLength=400,400,432,432,244,244,238,238,28.8,29.8,420,420,322,322,520,519,1409,1409,278,278,326,326,670,670,1459,1459,747,747&bw=56,90,135,300,450,600&buf=5,4,3,2,2,2&flv='+ew1720451_flvNames
+'&flvId=0&formatId='+ew1720451_adTypeID
+'&aInit=user&vInit=host&videoID='+ew1720451_videoID
+'&videoPath='+ew1720451_videoPath+'&executionId=1720451&adId=160988&adMode='+ew1720451_adMode
+'&trkUrl=http://cdn.eyewonder.com/100125/&siteID=204&swfVersion='+ew1720451_swfVersion
+'&browserEngine='+ew1720451_browserEngine
+'&browserEngineVersion='+ew1720451_browserEngineVersion
+'&opSys='+ew1720451_operatingSystem
+'&qaReportUUID='+escape(ew_qaReportUUID)
+'&localConnectionUUID='+ew1720451_localConnectionUUID
+'&uuid='+ew1720451_localConnectionUUID
+'&offsetX=&offsetY=&guid='+ew1720451_guidVal
+'&clickTagPrepend='+escape(flashvarsClickTagPrepend)
+'&streamServer=eyewond.fcod.llnwd.net&streamAppName=a119/o1&streamMode=0'
+'&usenewtracking=false'
+ew1720451_flashvarsExtra
+ew1720451_customFlashString
+ewclicktags1720451;
if(ew_pubVars!=""&&typeof(ew_pubVars)!='undefined'&&String(ew_pubVars)!='null') ewflashvars1720451+='&'+ew_pubVars;
if(typeof(setScale1720451)!='undefined') ewflashvars1720451+='&setScale='+setScale1720451;

function ew_findPos(obj){ if(!obj) return; var curleft=curtop=curwidth=curheight=0;
	if(obj.offsetParent){ curleft=obj.offsetLeft; curtop=obj.offsetTop; curwidth=obj.offsetWidth; curheight=obj.offsetHeight; while(obj=obj.offsetParent){ if((ew_isIe||ew_isWebKit||ew_isOpera)&&obj.tagName.toUpperCase()=="BODY") break; curleft+=obj.offsetLeft; curtop+=obj.offsetTop; } }
	return {x:curleft,y:curtop,w:curwidth,h:curheight};
}
function ew_findPosX(obj){ return ew_findPos(obj).x; } function ew_findPosY(obj){ return ew_findPos(obj).y; }

function ew_ctBool(tst){ return(tst?1:0); }
function ew_calcVisInt(t){ if(t<60) return(5); else if(t<120) return(10); else if(t<600) return(30); }
var ew1720451_trkExtra='', pts1720451='', vtAvail1720451=true, hbv1720451=false, hbFlag1720451=0, ttlInt1720451=0, perCent1720451=0, ew1720451_lpInt=5, timeOn1720451=-1, ttlVisTime1720451=-1, ad1720451VisX=false, ad1720451VisY=false, xPct1720451=0, yPct1720451=0, ew_adDiv1720451=null;
var ad1720451Vis=false, ew_winHt=0, ew_winWd=0, ew_currHt=0, ew_currWd=0, ew_currVis1720451=-1, ew_pgDim1720451="", ew1720451_pctStr="", ew_pgTop=0, ew_pgLeft=0, xPos1720451=0, yPos1720451=0, ad1720451_visTime=-1, ew1720451_vtCalled=false, ew1720451_sc=0;
if(typeof(topWin)=='undefined'){ if(ew1720451_inFIF) topWin=parent; else topWin=window; }; if(typeof(topDoc)=='undefined') topDoc=topWin.document;
function ew_getWinWd(){ return(topWin.innerWidth||topDoc.documentElement.clientWidth||topDoc.body.clientWidth); }
function ew_getWinHt(){ return(topWin.innerHeight||topDoc.documentElement.clientHeight||topDoc.body.clientHeight); }
function ew1720451_initVisTrk(n){ if(ew1720451_vtCalled||!vtAvail1720451) return; ew1720451_vtCalled=true; if(!n) vtAvail1720451=false;
if(!ew1720451_inFIF) ew_adDiv1720451=(document.getElementById("ew1720451_bannerDiv"));
else try{ ew_adDiv1720451=window.frameElement.parentNode; } catch(e){ return; }
if(!ew_adDiv1720451){ ew1720451_vtCalled=false; setTimeout(ew1720451_initVisTrk,1000); return; }; ew_calcPos1720451(); }
function ew_calcPos1720451(){ if(vtAvail1720451){ try{ xPos1720451=ew_findPos(ew_adDiv1720451).x; yPos1720451=ew_findPos(ew_adDiv1720451).y; if(xPos1720451<0) xPos1720451=0; if(yPos1720451<0) yPos1720451=0; ew_winWd=ew_getWinWd(); ew_winHt=ew_getWinHt(); ew_pgTop=topWin.scrollY||topDoc.body.scrollTop||topDoc.documentElement.scrollTop;
ew_pgLeft=topWin.scrollX||topDoc.documentElement.scrollLeft||topDoc.body.scrollLeft; } catch(e){ return; }; if(ew_winWd==0||ew_winHt==0) return; ew_winWd+=ew_pgLeft; var visTTl1720451=600; var yPctA1720451=(yPos1720451-ew_pgTop); if(yPctA1720451<0) visTTl1720451+=yPctA1720451; var pgBot=((ew_pgTop+ew_winHt)-(yPos1720451+600)); if(pgBot<0) visTTl1720451+=pgBot; var yPct1720451=visTTl1720451/600; if(yPct1720451>0) yPct1720451=parseInt((yPct1720451*100),10); else yPct1720451=0; if(yPct1720451>100) yPct1720451=100;
var xDiff=ew_winWd-(xPos1720451+300); if(xDiff>0) xPct1720451=1; else xPct1720451=(300+xDiff)/300; if(xPos1720451<ew_pgLeft) xPct1720451=(((300-(ew_pgLeft-xPos1720451))/300)*xPct1720451); if(xPct1720451>0) xPct1720451=parseInt((xPct1720451*100),10); else xPct1720451=0; if(xPct1720451>100) xPct1720451=100; if(xPct1720451>0) ad1720451VisX=true; else ad1720451VisX=false; if(yPct1720451>0) ad1720451VisY=true; else ad1720451VisY=false; if(ad1720451VisY&&ad1720451VisX){ ad1720451_visTime++; ttlVisTime1720451++; if(!hbv1720451) hbFlag1720451=1; else hbFlag1720451=0; hbv1720451=ad1720451Vis=true; } else { ad1720451Vis=false; }; perCent1720451=Math.round((xPct1720451*yPct1720451)/100); }; timeOn1720451++; ew1720451_trkVis(); }
function ew1720451_trkVis(){ if(timeOn1720451>600) return; if(((timeOn1720451%ew1720451_lpInt)==0)||(timeOn1720451==0)){ ew1720451_pctStr='&percent='+perCent1720451+"&visChg="; if(perCent1720451!=ew_currVis1720451){ ew1720451_pctStr+="true"; ew1720451_vc=1; ew_currVis1720451=perCent1720451; } else { ew1720451_pctStr+="false"; ew1720451_vc=0; }; ew_pgDim1720451='&res='+ew_winWd+'x'+ew_winHt+"&bsizeChg="; if(ew_winWd!=ew_currWd||ew_winHt!=ew_currHt){ ew_pgDim1720451+="true"; ew_currWd=ew_winWd; ew_currHt=ew_winHt; ew1720451_sc=1; } else { ew_pgDim1720451+="false"; ew1720451_sc=0; }; var vt=ad1720451_visTime; if(vt<0) vt=0; var ttlVt=ttlVisTime1720451; if(ttlVt<0) ttlVt=0; ew1720451_trkExtra='&ad=1720451&vis='+String(ad1720451Vis)+ew1720451_pctStr+'&vistime='+vt+'&ttlvistime='+ttlVt+'&interactions='+ttlInt1720451+'&timeOnPage='+timeOn1720451+'&yscroll='+ew_pgTop+'&xscroll='+ew_pgLeft+'&yPos='+yPos1720451+'&xPos='+xPos1720451+ew_pgDim1720451;
void(ew1720451_otrk('dot.gif', ew1720451_trkExtra));ew1720451_lpInt=ew_calcVisInt(timeOn1720451); if(timeOn1720451>=5) ad1720451_visTime=0; }; setTimeout(ew_calcPos1720451,1000); }
if(ew1720451_forceCenter){ ew1720451_positionCorrect+=" margin:auto; "; centerTag1="<CENTER>"; centerTag2="</CENTER>"; }
var flashHTMLonLoad1720451='<span id="ew_FlashDiv1720451" style="visibility:visible; display:block; width:300px; height:600px; margin:0px; padding:0px" onmouseout="ew_onmouseout1720451()">'
+'<object type="application/x-shockwave-flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="'+ew1720451_protocol+'fpdownload.adobe.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="300" height="600" id="ewad1720451" style="position:relative; top:0px; left:0px" meta="ewad;2;300x600;http://ad.doubleclick.net/clk;259329184;83089047;v">'
+'<param name="movie" value="'+adcp1720451+ew1720451_creativeName+ew_cbFlash+'" />'
+'<param name="quality" value="high" /><param name="bgcolor" value="#FFFFFF" /><param name="allowScriptAccess" value="always" />'
+'<param name="base" value="'+adcp1720451+'" /><param name="wmode" value="'+ew1720451_wMode+'" /><param name="allowFullScreen" value="true" />'
+'<param name="FlashVars" value="'+ewflashvars1720451+'" />'
+'<embed id="ewembed1720451" src="'+adcp1720451+ew1720451_creativeName+ew_cbFlash+'" base="'+adcp1720451+'" wmode="'+ew1720451_wMode+'" quality="high" bgcolor="#FFFFFF" width="300" height="600" style="position:relative; top:0px; left:0px" name="ewad1720451" FlashVars="'+ewflashvars1720451+'" allowScriptAccess="always" allowFullScreen="true" type="application/x-shockwave-flash" pluginspage="'+ew1720451_protocol+'adobe.com/go/getflashplayer" />'
+'</object></span>';

var ew1720451_failHREF='http://www.eyewonderlabs.com/ct2.cfm?ewbust='+ew1720451_cachebuster+'&guid='+ew1720451_guidVal+'&ewadid=160988&eid=1720451&file=failover.jpg&pnl=MainBanner&type=0&name=Clickthru-failover&num=1&time=0&diff=0&clkX=&clkY=&click='+escape(failclickTag1720451);
var failoverHTML1720451='<a onmouseover="ew_trkScroll()" onmousedown="this.href=ew1720451_clickTrk(ew_findPos(this.parentNode).x, ew_findPos(this.parentNode).y)" href="'+ew1720451_failHREF+'" target="_blank"><img src="'+adcp1720451+'failover.jpg" style="width:300px; height:600px; border:0px"></a>';
var flashHTML1720451=centerTag1+'<span id="ew1720451_bannerDiv" onmouseout="ew_onmouseout1720451()" style="visibility:visible; margin:0px; padding:0px; '+ew1720451_positionCorrect+'overflow:hidden; width:300px; height:600px;">'

+'</span>'+centerTag2;

ew_contract1720451=ew_expand1720451=ew_reset1720451=function(){}

function ew_onmouseout1720451(){ try{ ew_getSWFTag1720451().ew_rolloff(); } catch(e){} }
function ew_fixFlash(){ __flash__removeCallback=function(instance, name){ try{ instance[name]=null; } catch(e){} } }

function ew_onLoad1720451(){
	if(typeof(ew1720451_onLoadWasCalled)!='undefined'&&ew1720451_onLoadWasCalled) return;
	ew1720451_onLoadWasCalled=true; void(onLoadTracker1720451()); setTimeout(ew1720451_fixWK,500);
	try{ ew1720451_bannerDiv=document.getElementById('ew1720451_bannerDiv'); } catch(e){ try{ ew1720451_bannerDiv=parent.document.getElementById('ew1720451_bannerDiv'); } catch(e){} }
	if(ew_isIe){ window.ewad1720451=document.getElementById('ewad1720451'); setTimeout(ew_fixFlash,3000);
	} else if(ew_isCamino||(ew_isGecko&&isMac&&parseFloat(geckoRevisionString)>=1.9)) try{ ew1720451_bannerDiv.addEventListener("mouseout",ew_onmouseout1720451,true); } catch(e){}
	if(!ew_isIFrame||ew1720451_inFIF) var vtp=1; else var vtp=0; ew1720451_initVisTrk(vtp); if(ew1720451_inFIF) document.close();
	if(typeof(ew1720451_afterLoader)!='undefined') setTimeout(ew1720451_afterLoader, ew1720451_afterLoadTimer);
	if(ew1720451_bannerDiv){ if(ew_isIe&&ew1720451_ieAppendDiv){ var bnParentDiv=document.getElementById('ew1720451_wrapper').parentNode; if(bnParentDiv.tagName!="HEAD") ew1720451_bannerDiv=bnParentDiv.appendChild(ew1720451_bannerDiv); };
	ew1720451_bannerDiv.innerHTML=flashHTMLonLoad1720451; }

}

function ew1720451_checkSupport(){
	if(ew1720451_weSupport){
		document.writeln(flashHTML1720451);
		void(weSupportTracker1720451()); void(flashTracker1720451());
		if(ew_isIe){ window.ewad1720451=new Object(); window.ewad1720451.SetReturnValue=function(){} }
		setTimeout(ew_onLoad1720451, ew1720451_forceLoadTimer);
	} else {		ew_xCoord=0, ew_yCoord=0;
		ew_mouseTrk=function(e){ if(ew_isIe){ try{ ew_xCoord=event.clientX+ew_pgLeft; ew_yCoord=event.clientY+ew_pgTop; } catch(e){} } else { ew_xCoord=e.clientX+ew_pgLeft; ew_yCoord=e.clientY+ew_pgTop; } }
		ew_trkScroll=function(){ try{ if(!ew_isIe) window.onmousemove=ew_mouseTrk; else document.body.onmousemove=ew_mouseTrk; ew_pgTop=topWin.scrollY||topDoc.documentElement.scrollTop||topDoc.body.scrollTop; ew_pgLeft=topWin.scrollX||topDoc.documentElement.scrollLeft||topDoc.body.scrollLeft; } catch(e){} }
		ew1720451_clickTrk=function(x,y){ if(isNaN(x)||isNaN(y)){ var x=0, y=0; }
		var posX=parseInt(Math.abs(ew_xCoord-x)), posY=parseInt(Math.abs(ew_yCoord-y)); if(posX>300) posX=0; if(posY>600) posY=0;
		var timeon=(ew_getTime1720451()-ew1720451_startTime);
		try{ var ctURL='http://www.eyewonderlabs.com/ct2.cfm?ewbust='+ew1720451_cachebuster+'&guid='+ew1720451_guidVal+'&ewadid=160988&eid=1720451&file=failover.jpg&pnl=MainBanner&type=0&name=Clickthru-failover&num=1&time='+timeon+'&diff='+timeon+'&clkX='+posX+'&clkY='+posY+'&click='+escape(failclickTag1720451);
		} catch(e){ var ctURL=ew1720451_failHREF; }; return(ctURL); }
		window.onscroll=ew_trkScroll; ew_trkScroll(); try{ ew1720451_initVisTrk(0); } catch(e){}

		document.writeln(failoverHTML1720451);
		void(failoverTracker1720451());
		if(failReason=="browser") void(failoverReasonBrowserTracker1720451()); else if(failReason=="flash") void(failoverReasonFlashTracker1720451());

	}
}
ew1720451_checkSupport();

