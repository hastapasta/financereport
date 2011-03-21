<script type="text/javascript">
		var count = 0;
		//alert('here .5');
		
        google.load('visualization', '1', {packages: ['corechart']});
        google.setOnLoadCallback(function() { sendAndDraw('') });
        line_chart = null;
        var dataSourceUrl = 'http://www.pikefin.com/testjsp/JSPDataSource/mysqldatasource9.jsp?userid=16';
        var query;
        var options = {};

       /* function init() {
            gadgetHelper = new google.visualization.GadgetHelper();
            var container = document.getElementById('chart');
            container.style.width = document.body.clientWidth;
            container.style.height = document.body.clientHeight;
            columnChart = new google.visualization.ColumnChart(container);
            options['title'] = prefs.getString('chartTitle');
            options['titleX'] = prefs.getString('labelx');
            options['titleY'] = prefs.getString('labely');
            options['isStacked'] = prefs.getBool('stacked');
            options['is3D'] = prefs.getBool('3d');
            var legendOptions = ['right', 'left', 'top', 'bottom', 'none'];
            options['legend'] = legendOptions[prefs.getInt('legend')];
            if (prefs.getString('min') != '') {
              options['min'] = prefs.getInt('min');
            }
            if (prefs.getString('max') != '') {
              options['max'] = prefs.getInt('max');
            }
            var query = gadgetHelper.createQueryFromPrefs(prefs);
            query.send(responseHandler);
          };*/

              
    
        
          function sendAndDraw(queryString) {
              var options = {};
        	  options['height'] = 600;
           	  options['width'] = 800;
              var container = document.getElementById('chart-div');
              var orgChart = new google.visualization.ColumnChart(container);
              query && query.abort();
              //alert(dataSourceUrl + queryString);
              query = new google.visualization.Query(dataSourceUrl + queryString);
              var queryWrapper = new QueryWrapper(query, orgChart, options, container);
              queryWrapper.sendAndDraw();
            }
    </script>
<div class="users index">
<h2><?php __('Actions');?></h2>
<?php echo $this->element('actions',array('title'=>'Chart')); ?>
<form action="">
 
  <br /><br />
  <select onChange="sendAndDraw(this.value)">
    <option value="&limitcount=40">40</option>
    <option value="&limitcount=30">30</option>
    <option value="&limitcount=20">20</option>
    <option value="&limitcount=10">10</option>   
  </select>
</form>

  

    <div id="chart-div"></div>
</div>