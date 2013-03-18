<?php
class FactDatum extends AppModel {
	var $name = 'FactDatum';
	
	        	        	 
	        		



	        	var $validate = array(
		'id' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'value' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
	        	);
}
?>
