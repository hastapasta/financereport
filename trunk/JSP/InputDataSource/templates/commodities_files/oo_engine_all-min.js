var OOo={Browser:(function(){var d=navigator.userAgent,c=Object.prototype.toString.call(window.opera)=="[object Opera]",b={IE:!!window.attachEvent&&!c,Opera:c,WebKit:d.indexOf("AppleWebKit/")>-1,Chrome:d.indexOf("Chrome")>-1,Gecko:d.indexOf("Gecko")>-1&&d.indexOf("KHTML")===-1,MobileSafari:/Apple.*Mobile.*Safari/.test(d),PalmPre:d.indexOf("Pre/")>-1,BlackBerry:d.indexOf("BlackBerry")>-1,Fennec:d.indexOf("Fennec")>-1,IEMobile:d.indexOf("IEMobile")>-1,OperaMobile:d.search(/Opera (?:Mobi|Mini)/)>-1},a=0,e,f=false;if(b.IE){e=/msie.(\d\.\d+)/i;a=d.match(e)[1]}else{if(b.Gecko){e=/gecko.(\d+)/i;a=d.match(e)[1]}else{if(b.WebKit){e=/applewebkit\/(\d+)/i;a=d.match(e)[1]}else{if(b.Opera){e=/opera.(\d\.\d+)/i;a=d.match(e)[1]}else{f=true}}}}b.isMobile=(b.MobileSafari||b.PalmPre||b.BlackBerry||b.Fennec||b.IEMobile||b.OperaMobile);b.Version=parseFloat(a);b.isModern=(!(f||(b.IE&&b.Version<6)||(b.Opera&&b.Version<8)||(b.Gecko=="gecko"&&b.Version<20041107)));return b})()};OOo.Cache={};OOo.instanceCount=0;if(!OnlineOpinion){var OnlineOpinion=OOo}(function(){function g(i){return document.getElementById(i)}function a(i,k){for(var j in k){i[j]=k[j]}return i}function f(k,j,l,i){if(k.addEventListener){k.addEventListener(j,l,i)}else{if(k.attachEvent){k.attachEvent("on"+j,l)}}}function e(k,j,l,i){if(k.removeEventListener){k.removeEventListener(j,l,i)}else{if(k.detachEvent){k.detachEvent("on"+j,l)}}}function d(i){var j=[];for(var k in i){j.push(k+"="+(encodeURIComponent(i[k])||""))}return j.join("&")}function h(i){var j=d(i.metrics);j+="&custom_var="+i.tealeafId+"|"+i.clickTalePID+"/"+i.clickTaleUID+"/"+i.clickTaleSID;if(i.legacyVariables){j+="|"+i.legacyVariables}if(i.metrics.type=="OnPage"){j+="|iframe"}j+="&_rev=2";if(i.customVariables){j+="&customVars="+encodeURIComponent(OOo.serialize(i.customVariables))}return j}function c(k,i){var m=document,l=m.createElement("form"),j=m.createElement("input");l.style.display="none";l.method="post";l.target=i||"OnlineOpinion";l.action=k.onPageCard?"https://secure.opinionlab.com/ccc01/comment_card_json_4_0_b.asp?r="+location.href:"https://secure.opinionlab.com/ccc01/comment_card_d.asp";j.name="params";j.value=h(k);l.appendChild(j);m.body.appendChild(l);return l}function b(){return{width:screen.width,height:screen.height,referer:location.href,prev:document.referrer,time1:(new Date()).getTime(),time2:null,currentURL:location.href,ocodeVersion:"5.1.7"}}a(OOo,{extend:a,toQueryString:d,addEventListener:f,$:g,appendOOForm:c,removeEventListener:e,createMetrics:b})})();(function(){function a(f){if(!f){return null}switch(typeof f){case"number":case"boolean":case"function":return f;break;case"string":return"'"+f+"'";break;case"object":var e;if(f.constructor===Array||typeof f.callee!=="undefined"){e="[";var d,b=f.length;for(d=0;d<b-1;d++){e+=a(f[d])+","}e+=a(f[d])+"]"}else{e="{";var c;for(c in f){e+=c+":"+a(f[c])+","}e=e.replace(/\,$/,"")+"}"}return e;break;default:return null}}OOo.extend(OOo,{serialize:a})})();(function(){function a(e,d){var c=location.pathname,b;if(c.search(e[0])!=-1){OOo.createCookie(d,0);return false}else{if(OOo.readCookie(d)){b=parseInt(OOo.readCookie(d));if((c.search(e[b+1])!=-1)&&(b+1!=e.length-1)){OOo.createCookie(d,b+1);return false}else{if(c.search(e[b])!=-1){return false}else{if(b+1==e.length-1){return true}else{OOo.eraseCookie(d);return false}}}}else{return false}}}OOo.extend(OOo,{checkTunnel:a})})();(function(){function b(i){var k="";for(var h=7;h>=0;h--){k+="0123456789abcdef".charAt((i>>(h*4))&15)}return k}function d(k){var h=((k.length+8)>>6)+1,l=new Array(h*16);for(var j=0;j<h*16;j++){l[j]=0}for(var j=0;j<k.length;j++){l[j>>2]|=k.charCodeAt(j)<<(24-(j%4)*8)}l[j>>2]|=128<<(24-(j%4)*8);l[h*16-1]=k.length*8;return l}function c(h,k){var j=(h&65535)+(k&65535),i=(h>>16)+(k>>16)+(j>>16);return(i<<16)|(j&65535)}function f(h,i){return(h<<i)|(h>>>(32-i))}function g(i,h,k,j){if(i<20){return(h&k)|((~h)&j)}if(i<40){return h^k^j}if(i<60){return(h&k)|(h&j)|(k&j)}return h^k^j}function e(h){return(h<20)?1518500249:(h<40)?1859775393:(h<60)?-1894007588:-899497514}function a(u){var z=d(u),A=new Array(80),y=1732584193,v=-271733879,s=-1732584194,r=271733878,q=-1009589776,p,o,n,l,h,B;for(var m=0;m<z.length;m+=16){p=y,o=v,n=s,l=r,h=q;for(var k=0;k<80;k++){if(k<16){A[k]=z[m+k]}else{A[k]=f(A[k-3]^A[k-8]^A[k-14]^A[k-16],1)}B=c(c(f(y,5),g(k,v,s,r)),c(c(q,A[k]),e(k)));q=r;r=s;s=f(v,30);v=y;y=B}y=c(y,p);v=c(v,o);s=c(s,n);r=c(r,l);q=c(q,h)}return b(y)+b(v)+b(s)+b(r)+b(q)}OOo.extend(OOo,{sha1:a})})();(function(){function a(c){var g=c.cookieName||"oo_abandon",f=OOo.readCookie(g),e=c.startPage,b=c.endPage,d=c.middle;if(!f){if(location.pathname.indexOf(e)!=-1){OOo.createCookie(g)}return false}else{if(location.pathname.indexOf(b)!=-1){OOo.eraseCookie(g);return false}else{if(location.pathname.search(d)!=-1){return false}else{OOo.eraseCookie(g);return true}}}}OOo.extend(OOo,{checkAbandonment:a})})();(function(){function b(d){for(var c=d.length-1;c>=0;c--){if(d[c].read){if(!!(cookieValue=OOo.readCookie(d[c].name))&&cookieValue==d[c].value){return true}else{if(typeof d[c].value=="undefined"&&!!OOo.readCookie(d[c].name)){return true}}}}return false}function a(d){for(var c=d.length-1;c>=0;c--){if(d[c].set){OOo.createCookie(d[c].name,d[c].value,d[c].expiration)}}}OOo.extend(OOo,{checkThirdPartyCookies:b,setThirdPartyCookies:a})})();OOo.extend(Function.prototype,(function(){if(typeof(Prototype)!="undefined"){return}var d=Array.prototype.slice;function a(h,e){var g=h.length,f=e.length;while(f--){h[g+f]=e[f]}return h}function b(f,e){f=d.call(f,0);return a(f,e)}function c(g){if(arguments.length<2&&typeof arguments[0]==="undefined"){return this}var e=this,f=d.call(arguments,1);return function(){var h=b(f,arguments);return e.apply(g,h)}}return{bind:c}})());(function(){var c=location.host.split(".").reverse();c="."+c[1]+"."+c[0];function a(g,h,i){var f="",e="";if(i){f=new Date();f.setTime(f.getTime()+(i*1000));e="; expires="+f.toGMTString()}document.cookie=g+"="+h+e+"; path=/; domain="+c+";"}function b(f){var h=f+"=",e=document.cookie.split(";"),j;for(var g=0;g<e.length;g++){j=e[g];while(j.charAt(0)==" "){j=j.substring(1,j.length)}if(j.indexOf(h)===0){return j.substring(h.length,j.length)}}return null}function d(e){a(e,"",-1)}OOo.extend(OOo,{createCookie:a,readCookie:b,eraseCookie:d})})();OOo.Ocode=function(a){if(!OOo.Browser.isModern||(a.disableMobile&&OOo.Browser.isMobile)){return}if(a.disableNoniOS&&OOo.Browser.isMobile&&navigator.userAgent.search("Android")!=-1){return}OOo.instanceCount++;this.options={tealeafCookieName:"TLTSID"};OOo.extend(this.options,a);var b=this.options,c=b.referrerRewrite;if(b.cookie&&this.matchUrl()){return}if(b.thirdPartyCookies&&OOo.checkThirdPartyCookies(b.thirdPartyCookies)){return}if(b.abandonment&&!OOo.checkAbandonment(b.abandonment)){return}if(b.tunnel&&!OOo.checkTunnel(b.tunnel.path,b.tunnel.cookieName)){return}if(b.events&&b.events.onSingleClick){this.singProbability=Math.random()<1-b.events.onSingleClick/100}b.tealeafId=OOo.readCookie(b.tealeafCookieName);this.frameName=b.onPageCard?"OnlineOpinion"+OOo.instanceCount:"OnlineOpinion";b.metrics=OOo.createMetrics();if(c){b.metrics.referer=c.searchPattern?window.location.href.replace(c.searchPattern,c.replacePattern):c.replacePattern}if(b.events){this.setupEvents();if(b.events.disableLinks||b.events.disableFormElements){this.setupDisableElements()}}if(b.floating){this.floating()}else{if(b.bar){this.bar()}else{if(b.tab){this.tab()}}}};OOo.Ocode.prototype={show:function(){var a=this.options;if(this.interruptShow){return}if(!a.floating&&a.events&&this.singProbability){return}if(a.events&&a.events.onSingleClick){this.singProbability=true}if(a.cookie){this.tagUrl()}if(a.thirdPartyCookies){if(OOo.checkThirdPartyCookies(a.thirdPartyCookies)){return}OOo.setThirdPartyCookies(a.thirdPartyCookies)}if(this.floatingLogo&&a.disappearOnClick){this.floatingLogo.style.display="none"}if(typeof arguments[0]=="string"){a.metrics.trigger=arguments[0]}if(a.clickTalePID&&typeof ClickTale=="function"){a.clickTaleUID=ClickTaleGetUID();a.clickTaleSID=ClickTaleGetSID()}if(a.onPageCard){this.setupOnPageCC()}else{this.launchOOPopup()}},tagUrl:function(){if(this.matchUrl()){return}var b=this.options.cookie,a=b.type=="page"?location.href:location.hostname,c=OOo.readCookie(b.name||"oo_r")||"";OOo.createCookie(b.name||"oo_r",c+OOo.sha1(a),b.expiration)},matchUrl:function(){var a=this.options.cookie.type=="page"?location.href:location.hostname,b=OOo.readCookie(this.options.cookie.name||"oo_r");if(!b){return false}return b.search(OOo.sha1(a))!=-1}};(function(){function a(){var b=this.options,d=b.newWindowSize||[545,325],f=[parseInt((b.metrics.height-d[1])/2),parseInt((b.metrics.width-d[0])/2)],b=this.options,c,e;b.metrics.time2=(new Date()).getTime();b.metrics.type="Popup";c=OOo.appendOOForm(b);e=window.open("","OnlineOpinion","location=no,status=no,width="+d[0]+",height="+d[1]+",top="+f[0]+",left="+f[1]);if(e){c.submit()}}OOo.extend(OOo.Ocode.prototype,{launchOOPopup:a})})();(function(){function c(){var o=this.options.events,h=[false,false],e=["onExit","onEntry"],n=OOo.Browser.Opera?"unload":"beforeunload",f,g;for(var k=e.length-1;k>=0;k--){f=e[k];if(o[f] instanceof Array){var m=o[f],l=m.length;while(l--&&!h[k]){if(window.location.href.search(m[l].url)!=-1&&Math.random()>=1-m[l].p/100){h[k]=true}}}else{if(o[f]&&Math.random()>=1-o[f]/100){h[k]=true}}}if(h[0]){OOo.addEventListener(window,n,this.show.bind(this,"onExit"),false)}if(h[1]){if(o.delayEntry){window.setTimeout(function(){this.show()}.bind(this,"onEntry"),o.delayEntry*1000)}else{this.show("onEntry")}}}function b(){OOo.addEventListener(document.body,"mousedown",a.bind(this));if(!this.options.events.disableFormElements){return}var e=document.getElementsByTagName("form");for(var f=e.length-1;f>=0;f--){OOo.addEventListener(e[f],"submit",d.bind(this))}}function a(j){var g=j||window.event,f=j.target||j.srcElement,h=this.options.events,i=f.parentNode,l=5,k=0;while(i&&(f.nodeName!="A"||f.nodeName!="INPUT")&&k!=l){if(i.nodeName=="A"){f=i}i=i.parentNode;k++}if(h.disableFormElements&&f.tagName=="INPUT"&&(f.type=="submit"||f.type=="image")){this.interruptShow=true}if(f.nodeName=="A"&&f.href.substr(0,4)=="http"&&f.href.search(h.disableLinks)!=-1){this.interruptShow=true}}function d(f){this.interruptShow=true}OOo.extend(OOo.Ocode.prototype,{setupEvents:c,setupDisableElements:b})})();OOo.extend(OOo.Ocode.prototype,{floating:function(){var r=document,j=this.floatingLogo=document.createElement("div"),o=r.createElement("div"),a=r.createElement("div"),s=r.createElement("div"),k=r.createElement("span"),f=this.options.floating,i=OOo.$(f.contentId),c="10px",m=false,g=f.id;if(g){j.id=g}j.className="oo_feedback_float";a.className="oo_transparent";o.className="olUp";s.className="olOver";o.tabIndex=0;o.onkeyup=function(u){var d=u||window.event;if(d.keyCode!=13){return}this.show()}.bind(this);o.innerHTML=f.caption||"Feedback";j.appendChild(o);k.innerHTML=f.hoverCaption||"Click here to<br>rate this page";s.appendChild(k);j.appendChild(s);j.appendChild(a);if(OOo.Browser.IE&&OOo.Browser.Version<7){j.style.position="absolute";j.style.bottom="";OOo.addEventListener(window,"scroll",t,false);OOo.addEventListener(window,"resize",t,false);function t(d){j.style.top=(r.documentElement.scrollTop+document.documentElement.clientHeight-j.clientHeight)+"px"}m=true}else{if(OOo.Browser.MobileSafari){var p=window.innerHeight,l;j.style.bottom=null;j.style.top=(pageYOffset+window.innerHeight-60)+"px";OOo.addEventListener(window,"scroll",function(d){l=pageYOffset-(p-window.innerHeight);j.style.webkitTransform="translateY("+l+"px)"},false)}}if(f.position&&f.position.search(/Content/)&&i){var n=this.spacer=r.createElement("div"),e=OOo.Browser.WebKit?r.body:r.documentElement,q;n.id="oo_feedback_fl_spacer";n.style.left=b(i)+"px";r.body.appendChild(n);switch(f.position){case"rightOfContent":q=function(d){j.style.left=(b(i)-e.scrollLeft)+"px";if(m){q=null}};break;case"fixedPreserveContent":q=function(v){var u=OOo.Browser.IE?r.body.clientWidth:window.innerWidth,d=!m?e.scrollLeft:0;if(u<=b(i)+j.offsetWidth+parseInt(c)){j.style.left=(b(i)-d)+"px"}else{j.style.left="";j.style.right=c}};break;case"fixedContentMax":q=function(u){var d=OOo.Browser.IE?r.body.clientWidth:window.innerWidth;if(d<=b(i)+j.offsetWidth+parseInt(c)){j.style.left="";j.style.right=c;if(u&&u.type=="scroll"&&m){j.style.left=(r.documentElement.clientWidth+e.scrollLeft-105)+"px"}}else{j.style.left=(b(i)-e.scrollLeft)+"px";j.style.right=""}};break}q();OOo.addEventListener(window,"scroll",q,false);OOo.addEventListener(window,"resize",q,false);function h(d){n.style.left=b(i)+"px"}OOo.addEventListener(window,"resize",h,false)}else{j.style.right=c}OOo.addEventListener(j,"click",this.show.bind(this,"Floating"),false);OOo.addEventListener(j,"touchstart",this.show.bind(this,"Floating"),false);r.body.appendChild(j);if(OOo.Browser.IE&&OOo.Browser.Version<7){j.style.top=(r.documentElement.clientHeight-j.clientHeight)+"px";a.style.height=j.clientHeight+"px"}function b(d){return d.offsetLeft+d.offsetWidth}},removeFloatingLogo:function(){document.body.removeChild(this.floatingLogo);if(this.spacer){document.body.removeChild(this.spacer)}}});OOo.extend(OOo.Ocode.prototype,{bar:function(){var f=document,g=this.floatingLogo=f.createElement("div"),a=f.createElement("span");g.id="oo_bar";a.innerHTML=this.options.bar.caption||"Feedback";g.appendChild(a);g.tabIndex=0;g.onkeyup=function(h){var d=h||window.event;if(d.keyCode!=13){return}this.show()}.bind(this);OOo.addEventListener(g,"click",this.show.bind(this,"Bar"));document.body.className+=document.body.className<1?"oo_bar":" oo_bar";document.body.appendChild(g);if(OOo.Browser.IE){var e;if(f.compatMode=="CSS1Compat"){e=function(d){if(d&&d.type=="resize"){setTimeout(e,50)}g.style.top=(f.documentElement.scrollTop+document.documentElement.clientHeight-g.clientHeight-1)+"px";g.style.width=(Math.max(f.documentElement.clientWidth,f.body.offsetWidth))+"px"}}else{e=function(d){g.style.top=(f.body.scrollTop+document.body.clientHeight-g.clientHeight-1)+"px";g.style.width=(Math.max(f.documentElement.clientWidth,f.body.offsetWidth)-22)+"px"}}g.style.position="absolute";OOo.addEventListener(window,"scroll",e,false);OOo.addEventListener(window,"resize",e,false);e()}else{if(OOo.Browser.MobileSafari){var c=window.innerHeight,b;g.style.bottom=null;g.style.top=(pageYOffset+window.innerHeight-22)+"px";OOo.addEventListener(window,"scroll",function(d){b=pageYOffset-(c-window.innerHeight);g.style.webkitTransform="translateY("+b+"px)"},false)}}}});OOo.extend(OOo.Ocode.prototype,{tab:function(){var e=document,f=this.floatingLogo=e.createElement("div"),a=e.createElement("a"),c=e.createElement("span"),b=this.options.tab;f.id="oo_tab";f.className="oo_tab_"+(b.position||"right");if(OOo.Browser.IE&&OOo.Browser.Version<7){f.style.position="absolute";if(b.position=="right"){f.className+=" oo_tab_ie_right"}}else{if(OOo.Browser.MobileSafari){f.style.top=(pageYOffset+window.innerHeight/2)+"px";OOo.addEventListener(window,"scroll",function(d){f.style.top=(pageYOffset+window.innerHeight/2)+"px"},false)}}a.href="javascript:void(0)";a.title=b.title||"Feedback";f.tabIndex=0;f.onkeyup=function(g){var d=g||window.event;if(d.keyCode!=13){return}this.show()}.bind(this);a.appendChild(c);f.appendChild(a);OOo.addEventListener(f,"click",this.show.bind(this,"Tab"),false);e.body.appendChild(f)}});OOo.extend(OOo.Ocode.prototype,{setupOnPageCC:function(){var u=document,t=OOo.Cache.overlay||u.createElement("div"),i=this.wrapper=u.createElement("div"),o=u.createElement("a"),q=u.createElement("div"),k=u.createElement("span"),e=this.frameName,n=u.createElement(OOo.Browser.IE?'<iframe name="'+e+'">':"iframe"),s=u.createDocumentFragment(),l=this.options,b=l.onPageCard,w="https://secure.opinionlab.com/ccc01/comment_card_json_4_0_b.asp",c,v,p,f=false;l.metrics.type="OnPage";OOo.Cache.overlay=t;t.id="oo_overlay";t.style.display="block";t.className="";q.className="iwrapper";i.className="oo_cc_wrapper";o.className="oo_cc_close";o.href="javascript:void(0)";o.title=b.closeTitle||"Close Feedback Card";i.style.visibility="hidden";if(OOo.Browser.IE){if(!window.XMLHttpRequest){t.style.position="absolute";t.style.width=Math.max(u.documentElement.clientWidth,u.body.offsetWidth)+"px";t.style.height=Math.max(u.documentElement.clientHeight,u.body.offsetHeight)+"px";i.style.position="absolute"}else{var j=u.createElement("div"),r=u.createElement("div"),m=u.createElement("div"),a=u.createElement("div");a.className="oo_shadows";j.className="oo_body";r.className="oo_top";m.className="oo_bottom";a.appendChild(j);a.appendChild(r);a.appendChild(m);q.appendChild(a)}}OOo.addEventListener(o,"click",h);if(b.closeWithOverlay&&!OOo.Browser.isMobile){i.appendChild(k);k.onclick=h;t.onclick=h}n.src=w;n.name=e;q.appendChild(o);q.appendChild(n);i.appendChild(q);s.appendChild(i);s.appendChild(t);u.body.appendChild(s);p=g.bind(this);window.postMessage?OOo.addEventListener(window,"message",p):v=setInterval(g.bind(this),500);c=OOo.appendOOForm(l,e);l.metrics.time2=(new Date()).getTime();c.submit();function g(B){if((B&&B.origin!="https://secure.opinionlab.com")||(!B&&location.hash.substr(1,3)!="OL=")){return}var z=B?B.data:location.hash.slice(4),x=parseInt(z),C=document;if(!B){location.hash=""}if(x>0){if(f){return}f=true;var A=window.innerHeight||C.documentElement.clientHeight,y=x;if(y>A){y=A-40;n.style.width="555px"}n.style.height=y+"px";if(OOo.Browser.IE&&OOo.Browser.Version<7){k.style.height=i.offsetHeight+"px"}i.style.visibility="visible";t.className="no_loading"}else{if(z=="submitted"){h()}}}function h(){t.style.display="none";t.className="";u.body.removeChild(i);window.postMessage?OOo.removeEventListener(window,"message",p):window.clearInterval(v);f=false}}});OOo.Invitation=function(a){this.options={tunnelCookie:"oo_inv_tunnel",repromptTime:604800,responseRate:50,repromptCookie:"oo_inv_reprompt",promptMarkup:"oo_inv_prompt.html",promptStyles:"oo_inverstitial_style.css",percentageCookie:"oo_inv_percent",pagesHitCookie:"oo_inv_hit",popupType:"popunder",promptDelay:0,neverShowAgainButton:false,loadPopupInBackground:false,tealeafCookieName:"TLTSID"};this.popupShown=false;OOo.extend(this.options,a);var b=this.options,c=parseInt(OOo.readCookie(b.pagesHitCookie))||0;OOo.Invitation.friendlyDomains=b.friendlyDomains||null;if(location.search.search("evs")!=-1){b.loadPopupInBackground=true;this.launchPopup();OOo.createCookie(b.repromptCookie,1,b.repromptTime==-1?0:b.repromptTime)}setTimeout(function(){if(b.area&&location.href.search(b.area)==-1&&window.oo_inv_monitor){this.options.popupType="popup";this.launchPopup()}}.bind(this),1000);if(OOo.readCookie(b.repromptCookie)){return}if(b.thirdPartyCookies&&OOo.checkThirdPartyCookies(b.thirdPartyCookies)){return}if(!OOo.readCookie(b.percentageCookie)){OOo.createCookie(b.percentageCookie,(Math.random()>1-(b.responseRate/100))?"1":"0")}if(typeof(b.promptTrigger)!="undefined"){if(b.promptTrigger instanceof RegExp){if(!window.location.href.match(b.promptTrigger)){return}}else{if(b.promptTrigger instanceof Array){if(!OOo.checkTunnel(b.promptTrigger,b.tunnelCookie)){return}}}}c++;OOo.createCookie(b.pagesHitCookie,c);if(b.pagesHit&&c<b.pagesHit){return}OOo.eraseCookie(b.tunnelCookie);if(OOo.readCookie(b.percentageCookie)=="1"){window.setTimeout(function(){OOo.createCookie(b.repromptCookie,1,b.repromptTime);this.getPrompt()}.bind(this),b.promptDelay*1000)}};OOo.Invitation.prototype={getPrompt:function(){var b=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP"),d=this,c=document.createElement("link"),a;b.onreadystatechange=function(){if(b.readyState!=4){return}d.showPrompt(b.responseText)};b.open("GET",this.options.pathToAssets+this.options.promptMarkup,true);b.send(null)},showPrompt:function(b){var h=document,a=h.createElement("div"),c=OOo.Cache.overlay||h.createElement("div"),g,f,e=this.options;c.id="oo_overlay";a.id="oo_container";a.style.visibility="hidden";a.innerHTML=b;a.appendChild(c);h.body.appendChild(a);if(e.companyLogo){g=new Image();g.src=e.companyLogo;OOo.$("oo_company_logo").appendChild(g)}OOo.addEventListener(OOo.$("oo_launch_prompt"),"click",this.launchPopup.bind(this),false);if(e.neverShowAgainButton){f=OOo.$("oo_never_show");f.style.visibility="visible";OOo.addEventListener(f,"click",this.killPrompt.bind(this),false)}if(OOo.Browser.IE&&!window.XMLHttpRequest){c.style.position="absolute";c.style.width=Math.max(document.documentElement.clientWidth,document.body.offsetWidth)+"px";c.style.height=Math.max(document.documentElement.clientHeight,document.body.offsetHeight)+"px";a.style.position="absolute"}a.style.visibility="visible";c.className="no_loading"},launchPopup:function(){if(this.popupShown){return}this.popupShown=true;var d=this.options,i=window.location.href,b=d.popupType=="popup"?"https://secure.opinionlab.com/ccc01/comment_card.asp?":d.pathToAssets+"oo_inv_monitor.html?",e,f=[],g=d.asm?[555,500]:[545,200],c,a=OOo.readCookie(d.teleafId),h=OOo.createMetrics();g=d.newWindowSize||g;if(d.referrerRewrite){h.referer=d.referrerRewrite.searchPattern?window.location.href.replace(d.referrerRewrite.searchPattern,d.referrerRewrite.replacePattern):d.referrerRewrite.replacePattern}if(d.thirdPartyCookies){OOo.setThirdPartyCookies(d.thirdPartyCookies)}e=OOo.toQueryString(h)+"&type=Invitation";if(d.customVariables){e+="&customVars="+encodeURIComponent(OOo.serialize(d.customVariables))}e+="&custom_var="+a;if(d.clickTalePID&&ClickTaleGetUID&&ClickTaleGetSID){e+="|"+[d.clickTalePID,ClickTaleGetUID(),ClickTaleGetSID()].join("/")}c=window.open(b+e,"OnlineOpinionInvitation","location=no,status=no,width="+g[0]+",height="+g[1]);if(!d.loadPopupInBackground&&OOo.$("oo_container")){OOo.Invitation.hidePrompt()}if(d.popupType=="popunder"){if(!OOo.Browser.Chrome){c.blur();window.focus()}else{alert(d.chromeMainWinPrompt||"Please fill out the form behind this window when you are finished.");c.window.prompt=d.chromeSurveyPrompt}}else{if(window.oo_inv_monitor){window.blur();c.focus()}}},killPrompt:function(){OOo.createCookie(this.options.repromptCookie,1,1825);OOo.Invitation.hidePrompt()}};OOo.extend(OOo.Invitation,{hidePrompt:function(){OOo.$("oo_container").style.display="none"}});new OOo.Invitation({pathToAssets:"/onlineopinionv5/",responseRate:1,repromptTime:7776000,promptDelay:0,companyLogo:"/onlineopinionv5/logo.png",referrerRewrite:{searchPattern:/:\/\/[^\/]*/,replacePattern:"://interstitial.bloomberg.com"},popupType:"popunder",newWindowSize:[535,530],customVariables:{bbp2:BLOOMBERG.bbp2.is_phase_2()}});var oo_feedback=new OOo.Ocode({referrerRewrite:{replacePattern:"http://www.bloomberg.com"}});