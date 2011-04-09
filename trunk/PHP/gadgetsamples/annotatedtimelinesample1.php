<?php
//echo "google.visualization.Query.setResponse({version:'0.6',status:'ok',sig:'1950104442',table:{cols:[{id:'A',label:'',type:'string',pattern:''},{id:'B',label:'2011-01-26',type:'number',pattern:'#0.###############'},{id:'C',label:'2011-01-19',type:'number',pattern:'#0.###############'}],rows:[{c:[{v:'Capital paid in'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Surplus'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Other capital'},{v:0.0,f:'0'},{v:0.0,f:'0'}]}]}});";

//echo "google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"eps\",type:'number'}],rows: [{c:[{v:\"ADSK\"},{v:20091,f:\"20091\"},{v:0,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20091,f:\"20091\"},{v:800,f:\"1\"}]},{c:[{v:\"BIG\"},{v:20091,f:\"20091\"},{v:10000,f:\"1\"}]},{c:[{v:\"ADSK\"},{v:20092,f:\"20092\"},{v:-1.36,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20092,f:\"20092\"},{v:-0.56,f:\"1\"}]}]},\"version\":'0.5'});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{\"cols\":[{\"id\":\"Col1\",\"label\":\"BLAP\",\"type\":\"date\"}],\"rows\":[{\"c\":[{\"v\":\"a\"},{\"v\":\"Date(2010,10,6)\"}]},{\"c\":[{\"v\":\"b\"},{\"v\":\"Date(2010,10,7)\"}]}]}});";



?> 
<html>
  <head>
    <script type='text/javascript' src='https://www.google.com/jsapi'></script>
    <script type='text/javascript'>
      google.load('visualization', '1', {'packages':['annotatedtimeline']});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('date', 'Date');
        data.addColumn('number', 'Sold Pencils');
        data.addColumn('string', 'title1');
        data.addColumn('string', 'text1');
        data.addColumn('number', 'Sold Pens');
        data.addColumn('string', 'title2');
        data.addColumn('string', 'text2');
        data.addRows([
          [new Date(2008, 1 ,1), 30000, undefined, undefined, 40645, undefined, undefined],
          [new Date(2008, 1 ,2), 14045, undefined, undefined, 20374, undefined, undefined],
          [new Date(2008, 1 ,3), 55022, undefined, undefined, 50766, undefined, undefined],
          [new Date(2008, 1 ,4), 75284, undefined, undefined, 14334, 'Out of Stock','Ran out of stock on pens at 4pm'],
          [new Date(2008, 1 ,5), 41476, 'Bought Pens','Bought 200k pens', 66467, undefined, undefined],
          [new Date(2008, 1 ,6), 33322, undefined, undefined, 39463, undefined, undefined]
        ]);

        var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart_div'));
        chart.draw(data, {displayAnnotations: true});
      }
    </script>
  </head>

  <body>
    // Note how you must specify the size of the container element explicitly!
    <div id='chart_div' style='width: 700px; height: 240px;'></div>
  </body>
</html>