<?php

/*
 * The charts in here are linked to this google docs document:
 * 
 * /ztest/fedbalsheet - yearly
 */

require_once ("../../site/includes/sitecommon.php");
require_once '../../common/functions.php';

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php IncFunc::icon();?>
<?php IncFunc::title();?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />




<?php IncFunc::linkStyleCSS(); ?>
<?php //IncFunc::yuiDropDownJavaScript(); ?>





</head>

<body>

<div id="jq-siteContain">

<?php 
	IncFunc::header1("charts"); 
?>

<div id="pf-body">
<?php 
	IncFunc::apycomDropDownMenu();
?>
<div id="chartTitle" style="border-bottom-style: solid; border-width: 2px;margin: 50px 0 0 0;font-size: medium;font-weight:bold;"><?php echo strtoupper('US FEDERAL RESERVE BALANCE SHEET'); ?></div>




<BR/><BR/>
<table width="500" height="331" border="1" bordercolordark="#999999">
  <tr>
    <td><table width="360" height="66" border="1">
      <tr>

        <td colspan="2">  <script src="https://spreadsheets.google.com/gpub?url=http%3A%2F%2Ff62ed6fg3rppptjtj5ia5idjichr25gm-ss-opensocial.googleusercontent.com%2Fgadgets%2Fifr%3Fup_title%26up_chartTitle%3DFederal%2520Reserve%2520Balance%2520Sheet%2520-%2520Assets%26up_labelx%26up_labely%3D(%2524%2520millions)%26up_legend%3D0%26up_stacked%3D1%26up_showpoints%3D1%26up_min%26up_max%26up__table_query_url%3Dhttps%253A%252F%252Fspreadsheets.google.com%252Ftq%253Fgid%253D1%2526range%253DA1%25253AV9%2526key%253D0AvI-SyuXTqm8dDc3OUU5ZzBRSVVhR2VNRkxJTmhlM3c%2526pub%253D1%26url%3Dhttp%253A%252F%252Fwww.google.com%252Fig%252Fmodules%252Farea-chart.xml%26spreadsheets%3Dspreadsheets&height=300&width=700"></script></td>
        </tr>
      <tr>
        <td><a href="test4.php">Full size chart.</a></td>
        <td><a href="http://www.federalreserve.gov/releases/h41/">Link to source data.</a></td>

      </tr>
    </table>
    </td>
    </tr>
    <tr>
    <td><table width="360" height="66" border="1">
      <tr>
        <td colspan="2">  <script src="https://spreadsheets0.google.com/gpub?url=http%3A%2F%2Ff62ed6fg3rppptjtj5ia5idjichr25gm-ss-opensocial.googleusercontent.com%2Fgadgets%2Fifr%3Fup_title%26up_chartTitle%3DFederal%2520Reserve%2520Balance%2520Sheet%2520-%2520Liabilities%26up_labelx%26up_labely%3D(%2524%2520millions)%26up_legend%3D0%26up_stacked%3D1%26up_showpoints%3D1%26up_min%26up_max%26up__table_query_url%3Dhttps%253A%252F%252Fspreadsheets.google.com%252Ftq%253Fgid%253D3%2526range%253DA1%25253AL9%2526key%253D0AvI-SyuXTqm8dDc3OUU5ZzBRSVVhR2VNRkxJTmhlM3c%2526pub%253D1%26url%3Dhttp%253A%252F%252Fwww.google.com%252Fig%252Fmodules%252Farea-chart.xml%26spreadsheets%3Dspreadsheets&height=300&width=700"></script></td>
        </tr>
      <tr>
        <td><a href="test5.php">Full size chart.</a></td>
        <td><a href="http://www.federalreserve.gov/releases/h41/">Link to source data.</a></td>

      </tr>
    </table></td>

  </tr>
  <tr>
    <td><table width="360" height="66" border="1">
      <tr>
        <td colspan="2">  <script src="https://spreadsheets.google.com/gpub?url=http%3A%2F%2Ff62ed6fg3rppptjtj5ia5idjichr25gm-ss-opensocial.googleusercontent.com%2Fgadgets%2Fifr%3Fup_title%26up_chartTitle%3DFederal%2520Reserve%2520Balance%2520Sheet%2520-%2520Capital%26up_labelx%26up_labely%3D(%2524%2520millions)%26up_legend%3D0%26up_stacked%3D1%26up_showpoints%3D1%26up_min%26up_max%26up__table_query_url%3Dhttps%253A%252F%252Fspreadsheets.google.com%252Ftq%253Fgid%253D5%2526range%253DA1%25253AD9%2526key%253D0AvI-SyuXTqm8dDc3OUU5ZzBRSVVhR2VNRkxJTmhlM3c%2526pub%253D1%26url%3Dhttp%253A%252F%252Fwww.google.com%252Fig%252Fmodules%252Farea-chart.xml%26spreadsheets%3Dspreadsheets&height=300&width=700"></script></td>
        </tr>
      <tr>
        <td><a href="test6.php">Full size chart.</a></td>
        <td><a href="http://www.federalreserve.gov/releases/h41/">Link to source data.</a></td>

      </tr>
    </table></td>

  </tr>
  
</table>

<!-- </div> --><!-- who's using -->
</div> <!--  pf-body -->
</div> <!--  siteContain -->
</body>
</html>