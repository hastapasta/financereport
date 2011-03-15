<HTML>
	
	<BODY>
	
	<?php
	include("../common/functions.php");
	$url_val = $_COOKIE['web_page'];
	$form_properties = $_COOKIE['form_properties'];
	$returned_content = get_data($url_val,$form_properties);
	wl("in show raw html");
	wl($url_val);
	$myFile = "testFile.txt";
	$fh = fopen($myFile, 'w') or die("can't open file");
	fwrite($fh, $returned_content);
	fclose($fh);
	echo "<BR><A href=\"testFile.txt\">Text File</A><BR>";
	echo htmlspecialchars($returned_content);
	

	
	
	
	?>
	
</BODY>
</HTML>