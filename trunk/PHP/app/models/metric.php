<?php
class Metric extends AppModel {
	var $name = 'Metric';
	//var $displayField = 'ticker';
	//The Associations below have been created with all possible keys, those that are not needed can be removed

	var $hasMany = array(
		'Alert' => array(
			'className' => 'Alert',
			'foreignKey' => 'metric_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		)
		
	);




}
?>
