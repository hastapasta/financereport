<?php
require_once 'init2.php';

function writelog($the_string)
{

$fi = fopen("logfile1.txt", 'a+');
$the_string = $the_string."\n";
fputs( $fi, $the_string, strlen($the_string) );
fclose( $fi );

}

if(isset($_GET['tq'])) {
	  writelog('here 5');
		if ($vis == null)
			writelog("vis is null");
		else 
			writelog("vis is not null");
    $vis->addEntity('timeline', array(
        'table' => 'table_eps_est_and_act c',
        'fields' => array(
            'ticker' => array('field' => 'c.ticker', 'type' => 'text'),
            'calyear' => array('field' => 'c.calyear', 'type' => 'number'),
            'value' => array('field' => 'c.value', 'type' => 'number')
            /*,
            'calquarter' => array('field' => 'c.calquarter', 'type' => 'number'),
            'fiscalyear' => array('field' => 'c.fiscalyear', 'type' => 'number'),
            'fiscalquarter' => array('field' => 'c.fiscalquarter', 'type' => 'number')*/
        )/*,
        'joins' => array(
            'finance' => 'INNER JOIN finance f ON f.country_id=c.id',
            'birth' => 'INNER JOIN birth_control b ON b.country_id=f.country_id AND b.year=f.year',
            'mort' => 'INNER JOIN mortality m ON m.country_id=f.country_id AND m.year=f.year'
        )*/
    ));

    $vis->setDefaultEntity('timeline');
    
    $vis->handleRequest();
    die();
}
?>
<html>
<head>
    <title>Complete visualization example</title>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load('visualization', '1', {'packages': ['motionchart']});
        google.setOnLoadCallback(loadChart);
        motion_chart = null;
        
        function loadChart() {
            alert('here 1');
            var metric1 = document.getElementById('metric-1').value;
            var metric2 = document.getElementById('metric-2').value;
            if(metric1 == metric2) return;
            
           // var str = 'select country, year, ' + metric1 + ', ' + metric2 + ' where ' + metric1 + '!=0 AND ' + metric2 + '!=0 group by country, year label country "Country", year "Year", birth_control "Birth Control Penetration", gdp_us "Per-capita GDP (US Dollars)", savings_rate "Savings Rate", investment_rate "Investment Rate", infant_mort "Infant Mortality", life_expect "Life Expectancy" format year "%d"';
           // var str = 'select ticker,calyear,value where (calyear=2010 or calyear=2011 or calyear=2012) AND ticker not in (select ticker from comapny where group like \'%sandp%\' limit 50 offsetgroup by ticker, calyear format calyear "%d"';
           //select ticker,calyear,value where (calyear=2010 or calyear=2011 or calyear=2012) AND ticker in (select ticker from company where groups like '%sandp%' order by ticker limit 50 offset 1) group by ticker, calyear format calyear "%d";
            
            //alert('in complete1.php');
            var query = new google.visualization.Query('complete3.php');
            alert(str);
            query.setQuery(str);
            //alert('here 2');
            query.send(function(res) {
		//alert('here 2.05');
                if(res.isError()) {
		   //alert('here 2.06');
                    //alert(res.getDetailedMessage());
                } else {
                    if(motion_chart === null)
                    {
		     //alert('here 2.1');
                     motion_chart = new google.visualization.MotionChart(document.getElementById('chart-div'));
                     //alert('here 2.5');
                   }
		   //alert('here 2.7');
                    motion_chart.draw(res.getDataTable(), {'height': 600, 'width': 800});
                    //alert('here 3');
                }
            });
            
        }
    </script>
</head>
<body>

    <p>This is a more complete example showing how you can build queries to provide interesting data visualizations.</p>
    <div class="chart-options">
        Show me
        <select id="metric-1" onchange="loadChart();">
            <option value="1">Companies 1-50</option>
            <option value="51">Companies 51-100</option>
            <option value="101">Companies 101-150</option>
            <option value="151">Companies 151-200</option>
            <option value="201">Companies 201-250</option>
            <option value="251">Companies 251-300</option>
            <option value="301">Companies 301-350</option>
            <option value="351">Companies 351-400</option>
            <option value="401">Companies 401-450</option>
            <option value="451">Companies 451-500</option>
        </select>
        against
        <select id="metric-2" onchange="loadChart();">
            <option value="gdp_us">Per-capita GDP</option>
            <option value="birth_control">Birth Control Penetration</option>
            <option value="savings_rate">Savings Rate</option>
            <option value="investment_rate">Investment Rate</option>
            <option value="infant_mort">Infant Mortality</option>
            <option value="life_expect">Life Expectancy</option>
        </select>
    </div>
    <div id="chart-div"></div>
    <?php
    writelog("here");
    ?>
</body>
</html>
