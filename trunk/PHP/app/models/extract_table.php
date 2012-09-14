<?php
class ExtractTable extends AppModel {
	var $name = 'ExtractTable';
	var $validate = array(
		'rowinterval' => array(
		'numeric' => array(
		'rule' => 'numeric',
		'required' => true,
		'message' => 'Please enter a row interval.'
		)
		)
		);
	//var $useTable = 'extract_tables';
}
?>