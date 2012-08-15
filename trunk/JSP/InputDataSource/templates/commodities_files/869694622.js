/* AG-develop 12.7.1-514 (2012-06-13 10:58:12 PDT) */
rsinetsegs=['K05539_10518','K05539_10284','K05539_10561','K05539_10698','K05539_10629','K05539_10737','K05539_10635','K05539_10645','K05539_10646','K05539_10149','K05539_10024','K05539_10384','K05539_10686','K05539_10761','K05539_10766','K05539_10872','K05539_10874','K05539_10751','K05539_10756','K05539_10758','K05539_10891'];
var rsiExp=new Date((new Date()).getTime()+2419200000);
var rsiDom=location.hostname;
rsiDom=rsiDom.replace(/.*(\.[\w\-]+\.[a-zA-Z]{3}$)/,'$1');
rsiDom=rsiDom.replace(/.*(\.[\w\-]+\.\w+\.[a-zA-Z]{2}$)/,'$1');
rsiDom=rsiDom.replace(/.*(\.[\w\-]{3,}\.[a-zA-Z]{2}$)/,'$1');
var rsiSegs="";
var rsiPat=/.*_5.*/;
for(x=0;x<rsinetsegs.length;++x){if(!rsiPat.test(rsinetsegs[x]))rsiSegs+='|'+rsinetsegs[x];}
document.cookie="rsi_segs="+(rsiSegs.length>0?rsiSegs.substr(1):"")+";expires="+rsiExp.toGMTString()+";path=/;domain="+rsiDom;
if(typeof(DM_onSegsAvailable)=="function"){DM_onSegsAvailable(['K05539_10518','K05539_10284','K05539_10561','K05539_10698','K05539_10629','K05539_10737','K05539_10635','K05539_10645','K05539_10646','K05539_10149','K05539_10024','K05539_10384','K05539_10686','K05539_10761','K05539_10766','K05539_10872','K05539_10874','K05539_10751','K05539_10756','K05539_10758','K05539_10891'],'k05539');}