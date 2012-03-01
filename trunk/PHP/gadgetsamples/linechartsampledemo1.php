<html>
  <head>


  <script src="http://www.google.com/jsapi" type="text/javascript"></script>

  <script type="text/javascript">
    /*
     * Call the RegisterOnloadHandler when html is loaded
    */

    var lineChart;
    var options = {};
    Document.onload = loadVisualizationAPI();
  

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
      options['height'] = '600';
      options['width'] = '800';


	   
      


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
 


    
  </script>
  </head>

  <body>
	<div id="loading"><img src="http://www.google.com/ig/images/spinner.gif" /></div>
  	<div id="chart"></div>
  </body>
</html>
