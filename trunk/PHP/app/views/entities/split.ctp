<div class="entities split">

<?php 


$sql1 = $this->getVar('sql1')."\n";
if ($sql1==1)
	echo "Successfully inserted row into splits table.<BR><BR>\n";

$sql2 = $this->getVar('sql2')."\n";
if ($sql2==1)
	echo "Successfully updated price metric in fact_data table.<BR><BR>\n";

$sql3 = $this->getVar('sql3')."\n";
if ($sql3==1)
	echo "Successfully updated eps metrics in fact_data table.<BR><BR>\n";

$sql4 = $this->getVar('sql4')."\n";
if ($sql4==1)
	echo "Successfully updated shares outstanding in entities table.<BR><BR>\n";



?>
</div>

