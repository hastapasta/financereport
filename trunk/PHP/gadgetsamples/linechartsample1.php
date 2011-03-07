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
      google.load('visualization', '1', {packages:['linechart']});
      google.setOnLoadCallback(drawTable);
      function drawTable() {
        var data = new google.visualization.DataTable();
        alert('here');
        data.addColumn('string', 'year');
        data.addColumn('number', 'Mike');
        data.addColumn('number', 'Susan');
        data.addRows(4);
        data.setCell(0, 0, '2008');
        data.setCell(0, 1, 10000, '$10,000');
        data.setCell(0, 2, 6000,'$6,000');
        data.setCell(1, 0, '2009');
        data.setCell(1, 1, 8000, '$8,000');
        data.setCell(1, 2, 6500,'$6,500');
        data.setCell(2, 0, '2010');
        data.setCell(2, 1, 12500, '$12,500');
        data.setCell(2, 2, 9000,'$9,500');
        data.setCell(3, 0, '2011');
        data.setCell(3, 1, 7000, '$7,000');
        data.setCell(3, 2, 10000,'$10,000');
      
        var options = {};
        options['title'] = 'Test Chart Title';
     	options['height'] = 600;
     	options['width'] = 800;
     	

        var jsondiv = document.getElementById('jsondata');
    	jsondiv.innerHTML =  data.toJSON();
        var linechart = new google.visualization.LineChart(document.getElementById('table_div'));
        //linechart.draw(data, {showRowNumber: true});
        linechart.draw(data,options);
      }
    </script>
  </head>

  <body>
    <div id='table_div'></div>
    This is some text.
    <div id='jsondata'>a</div>
  </body>
</html>
