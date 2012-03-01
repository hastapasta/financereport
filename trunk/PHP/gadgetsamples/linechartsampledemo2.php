<?php
//echo "google.visualization.Query.setResponse({version:'0.6',status:'ok',sig:'1950104442',table:{cols:[{id:'A',label:'',type:'string',pattern:''},{id:'B',label:'2011-01-26',type:'number',pattern:'#0.###############'},{id:'C',label:'2011-01-19',type:'number',pattern:'#0.###############'}],rows:[{c:[{v:'Capital paid in'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Surplus'},{v:26532.0,f:'26532'},{v:26532.0,f:'26532'}]},{c:[{v:'Other capital'},{v:0.0,f:'0'},{v:0.0,f:'0'}]}]}});";

//echo "google.visualization.Query.setResponse({version:'0.6',reqId:'0',status:'ok',sig:'5982206968295329967',table:{cols:[{id:'Col1',label:'label1',type:'number'},{id:'Col2',label:'label2',type:'number'},{id:'Col3',label:'label3',type:'number'}],rows:[{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]},{c:[{v:2.0,f:'2'},{v:3.0,f:'3'},{v:4.0,f:'4'}]},{c:[{v:3.0,f:'3'},{v:4.0,f:'4'},{v:5.0,f:'5'}]},{c:[{v:1.0,f:'1'},{v:2.0,f:'2'},{v:3.0,f:'3'}]}]}});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{cols: [{id:'ticker',label:\"ticker\",type:'string'},{id:'calyear',label:\"calyear\",type:'number'},{id:'value',label:\"eps\",type:'number'}],rows: [{c:[{v:\"ADSK\"},{v:20091,f:\"20091\"},{v:0,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20091,f:\"20091\"},{v:800,f:\"1\"}]},{c:[{v:\"BIG\"},{v:20091,f:\"20091\"},{v:10000,f:\"1\"}]},{c:[{v:\"ADSK\"},{v:20092,f:\"20092\"},{v:-1.36,f:\"1\"}]},{c:[{v:\"ANF\"},{v:20092,f:\"20092\"},{v:-0.56,f:\"1\"}]}]},\"version\":'0.5'});";

//echo "google.visualization.Query.setResponse({\"reqId\":\"0\",status:'ok',table:{\"cols\":[{\"id\":\"Col1\",\"label\":\"BLAP\",\"type\":\"date\"}],\"rows\":[{\"c\":[{\"v\":\"a\"},{\"v\":\"Date(2010,10,6)\"}]},{\"c\":[{\"v\":\"b\"},{\"v\":\"Date(2010,10,7)\"}]}]}});";



?> 

<html>
  <head>


  <script src="http://www.google.com/jsapi" type="text/javascript"></script>

  <script type="text/javascript">
    /*
     * Call the RegisterOnloadHandler when html is loaded
    */

    var lineChart;
    var options = {};
    document.onload = loadVisualizationAPI();
  

    /*
     * Retrieve the user preferences.
     */	
    /*var prefs = new _IG_Prefs();
    var title = prefs.getString('title');
    if (title) {
      _IG_SetTitle(title);
    }   */

   
    /*
     * Call the init() function after our visualization gadget is loaded.
     */
    function loadVisualizationAPI() {
      google.load('visualization', '1', {packages: ['linechart']});
      google.setOnLoadCallback(init);
    };

    var gadgetHelper;

    

    function init() {
      //Added	
      gadgetHelper = new google.visualization.GadgetHelper();
      var container = document.getElementById('chart');
      container.style.width = document.body.clientWidth;
      container.style.height = document.body.clientHeight;
      lineChart = new google.visualization.LineChart(container);
      /*options['title'] = prefs.getString('chartTitle');
      options['titleX'] = prefs.getString('labelx');
      options['titleY'] = prefs.getString('labely');
      options['isStacked'] = prefs.getBool('stacked');
      options['smoothLine'] = prefs.getBool('smoothline');
      options['pointSize'] = prefs.getBool('showpoints') ? null : 0;
      var legendOptions = ['right', 'left', 'top', 'bottom', 'none'];
      options['legend'] = legendOptions[prefs.getInt('legend')];*/
      options['height'] = '400';
      options['width'] = '500';

      option1 = document.getElementById('option1');
      option1.selected=true;


	   
      


      /*if (prefs.getString('min') != '') {
        options['min'] = prefs.getInt('min');
      }
      if (prefs.getString('max') != '') {
        options['max'] = prefs.getInt('max');
      }*/
     

      //added  
      //var query = gadgetHelper.createQueryFromPrefs(prefs);
       var query = new google.visualization.Query('http://www.pikefin.com/testjsp/JSPDataSource/demodatasource.jsp');

      query.send(responseHandler);
 

    };

     //Added
     function responseHandler(response) {
        // Remove the loading message (if exists).

        var loadingMsgContainer = document.getElementById('loading');
        loadingMsgContainer.style.display = 'none';
       
        if (!gadgetHelper.validateResponse(response)) {
          // Default error handling was done, just leave.
          return;
        }

        var dataTable = response.getDataTable();

        lineChart.draw(dataTable, options);
      };

      function changeHandler() {
   
          var datasource = document.getElementById("datasource");

          var loadingMsgContainer = document.getElementById('loading');
          loadingMsgContainer.style.display = 'block';

          var query;
          if (datasource.value == '1') {
        	  query = new google.visualization.Query('http://www.pikefin.com/testjsp/JSPDataSource/demodatasource.jsp');

              query.send(responseHandler);
          }
          else if (datasource.value == '2') {
        	  query = new google.visualization.Query( 'https://docs.google.com/spreadsheet/tq?range=F1%3AH13&key=0AvI-SyuXTqm8dDFwbjd4NmlKTVQwQml0cUJHWVFnRVE&gid=3&headers=-1');

              query.send(responseHandler);
          }
          else {

        	  var data = new google.visualization.DataTable();
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
              lineChart.draw(data, options);
              loadingMsgContainer.style.display = 'none';
          }

          


      }
 


    
  </script>
  </head>

  <body>
    <form id="formid" name="form" action="">
    <select id="datasource" style="background-color: #FFFFFF" onChange="changeHandler();">
    <option value="1" selected="true" id="option1">Custom Data Source</option>
    <option value="2">Google Spreadsheet Data Source</option>
    <option value="3">Hardcoded table</option>
    </select>
    </form>
	<div id="loading"><img src="http://www.google.com/ig/images/spinner.gif" /></div>
  	<div id="chart"></div>

  </body>
</html>
