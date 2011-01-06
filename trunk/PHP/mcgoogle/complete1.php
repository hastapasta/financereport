<?php
require_once 'init1.php';

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
        'table' => 'testtable c',
        'fields' => array(
            'country' => array('field' => 'c.name', 'type' => 'text'),
            'year' => array('field' => 'c.year', 'type' => 'number'),
            'birth_control' => array('field' => 'c.all_methods', 'type' => 'number'),
            'gdp_us' => array('field' => 'c.gdp_us', 'type' => 'number'),
            'savings_rate' => array('field' => 'c.savings_rate', 'type' => 'number'),
            'investment_rate' => array('field' => 'c.investment_rate', 'type' => 'number'),
            'infant_mort' => array('field' => 'c.infant_both', 'type' => 'number'),
            'life_expect' => array('field' => 'c.life_both', 'type' => 'number')
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
            var metric1 = document.getElementById('metric-1').value;
            var metric2 = document.getElementById('metric-2').value;
            if(metric1 == metric2) return;
            
           // var str = 'select country, year, ' + metric1 + ', ' + metric2 + ' where ' + metric1 + '!=0 AND ' + metric2 + '!=0 group by country, year label country "Country", year "Year", birth_control "Birth Control Penetration", gdp_us "Per-capita GDP (US Dollars)", savings_rate "Savings Rate", investment_rate "Investment Rate", infant_mort "Infant Mortality", life_expect "Life Expectancy" format year "%d"';
            var str = 'select country, year, ' + metric1 + ', ' + metric2 + ' group by country, year format year "%d"';
            
            alert('in complete1.php');
            var query = new google.visualization.Query('complete1.php');
            alert('here 1');
            query.setQuery(str);
            //alert('here 2');
            query.send(function(res) {
                if(res.isError()) {
                    alert(res.getDetailedMessage());
                } else {
                    if(motion_chart === null)
                    {
                     motion_chart = new google.visualization.MotionChart(document.getElementById('chart-div'));
                     //alert('here 2.5');
                   }
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
            <option value="birth_control">Birth Control Penetration</option>
            <option value="gdp_us">Per-capita GDP</option>
            <option value="savings_rate">Savings Rate</option>
            <option value="investment_rate">Investment Rate</option>
            <option value="infant_mort">Infant Mortality</option>
            <option value="life_expect">Life Expectancy</option>
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
