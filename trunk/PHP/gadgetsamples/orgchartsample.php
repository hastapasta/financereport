<?php
//echo "google.visualization.Query.setResponse({version:'0.6',status:'ok',sig:'1950104442',table:{cols:[{id:'A',label:'',type:'string',pattern:''},{id:'B',label:'2011-01-26',type:'number',pattern:'#0.###############'},{id:'C',label:'2011-01-19',type:'number',pattern:'#0.###############'}],rows:[{c:[{v:'Capital paid in'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Surplus'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Other capital'},{v:0.0,f:'0'},{v:0.0,f:'0'}]}]}});";

//echo "google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"eps\",type:'number'}],rows: [{c:[{v:\"ADSK\"},{v:20091,f:\"20091\"},{v:0,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20091,f:\"20091\"},{v:800,f:\"1\"}]},{c:[{v:\"BIG\"},{v:20091,f:\"20091\"},{v:10000,f:\"1\"}]},{c:[{v:\"ADSK\"},{v:20092,f:\"20092\"},{v:-1.36,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20092,f:\"20092\"},{v:-0.56,f:\"1\"}]}]},\"version\":'0.5'});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{\"cols\":[{\"id\":\"Col1\",\"label\":\"BLAP\",\"type\":\"date\"}],\"rows\":[{\"c\":[{\"v\":\"a\"},{\"v\":\"Date(2010,10,6)\"}]},{\"c\":[{\"v\":\"b\"},{\"v\":\"Date(2010,10,7)\"}]}]}});";



?> 

<!DOCTYPE html>
<html>
<head>
  <title>Query Wrapper Example</title>
  <script type="text/javascript" src="https://www.google.com/jsapi"></script>
  <script type="text/javascript" src="querywrapper.js"></script>
  <script type="text/javascript">
    google.load('visualization', '1', {'packages' : ['orgchart']});
    google.setOnLoadCallback(function() { sendAndDraw('') });

    var dataSourceUrl = 'https://spreadsheets.google.com/tq?key=rCaVQNfFDMhOM6ENNYeYZ9Q&pub=1';
    var query;

    function sendAndDraw(queryString) {
      var container = document.getElementById('orgchart');
      var orgChart = new google.visualization.OrgChart(container);
      query && query.abort();
      query = new google.visualization.Query(dataSourceUrl + queryString);
      var queryWrapper = new QueryWrapper(query, orgChart, {'size': 'large'}, container);
      queryWrapper.sendAndDraw();
    }

  </script>
</head>

<body>
<h1>Query Wrapper Example</h1>
<form action="">
  <span> This example uses the following spreadsheet: <br />
    <a href="https://spreadsheets.google.com/pub?key=rCaVQNfFDMhOM6ENNYeYZ9Q">
      https://spreadsheets.google.com/pub?key=rCaVQNfFDMhOM6ENNYeYZ9Q
    </a></span>
  <br /><br />
  <select onChange="sendAndDraw(this.value)">
    <option value="">No query string</option>
    <option value="&tq=limit 3">query=limit 3</option>
    <option value="&tq=select G,H">(Error) query=select G,H</option>
  </select>
</form>
<br />
<div id="orgchart"></div>
</body>
</html>
