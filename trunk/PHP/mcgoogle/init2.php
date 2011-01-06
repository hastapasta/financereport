<?php
ini_set('include_path', realpath(dirname(__FILE__) . '/../lib/'));

function writelog2($the_string)
{

$fi = fopen("logfile1.txt", 'a+');
$the_string = $the_string."\n";
fputs( $fi, $the_string, strlen($the_string) );
fclose( $fi );

}

writelog2(realpath(dirname(__FILE__)));

require_once 'MC/Google/Visualization.php';
$username = 'root';
$password = 'madmax1.';

//$db = new PDO("mysql:host=win-d2sjsg6emdd;dbname=mydb", $username, $password);
$db = new PDO("mysql:host=testdataload;dbname=findata", $username, $password);
//$db = new PDO('sqlite:example.db');
writelog2("before new MC_Google_Vis");
$vis = new MC_Google_Visualization($db);

writelog2("in init.php");
?>
