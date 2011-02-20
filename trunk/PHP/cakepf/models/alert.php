<?php
class Alert extends AppModel {
	var $name = 'Alert';
	var $belongsTo = array(
	        'Schedule' => array( 
	            'className' => 'Schedule', 
	            'foreignKey' => 'schedule_id' 
	            ),
	        'User' => array(
	        	'className' => 'User',
	        	'foreignKey' => 'user_id'
	        	),
	        'Entity' => array(
	        	'className' => 'Entity',
	        	'foreignKey' => 'entity_id')

	        	);
	        	 
	        	 
	        		



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
		'type' => array(
			'notempty' => array(
				'rule' => array('notempty'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'schedule_id' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'fact_data_key' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'alert_count' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'disabled' => array(
			'boolean' => array(
				'rule' => array('boolean'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'user_id' => array(
			'notempty' => array(
				'rule' => array('notempty'),
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
