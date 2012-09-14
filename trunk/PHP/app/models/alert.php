<?php
class Alert extends AppModel {
	var $name = 'Alert';
	/*var $validate = array(
		'value' => array(
		'numeric' => array(
		'rule' => 'numeric',
		'required' => true,
		'message' => 'Number required (up to 2 decimal places).'
		)
		)
		);*/
	var $belongsTo = array(
	        'Task' => array( 
	            'className' => 'Task', 
	            'foreignKey' => 'task_id' 
	            ),
	        'User' => array(
	        	'className' => 'User',
	        	'foreignKey' => 'user_id'
	        	),
	        'Entity' => array(
	        	'className' => 'Entity',
	        	'foreignKey' => 'entity_id'),
	        'TimeEvent' => array(
	        	'className' => 'TimeEvent',
	        	'foreignKey' => 'time_event_id'),
	        'InitialFactDatum' => array(
	        	'className' => 'FactDatum',
	        	'foreignKey' => 'initial_fact_data_id'),
	        'CurrentFactDatum' => array(
	        	'className' => 'FactDatum',
	        	'foreignKey' => 'current_fact_data_id'),

	        	);
	        	 
	        	 
	        		



	        	var $validate = array(
		'id' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Number required',
	        	//'allowEmpty' => false,
	        	//'required' => true
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
	        	
	     'limit_value'=> array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	'message' => 'Number required.',
	        	//'allowEmpty' => false,
	        	'required' => true
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
	        'calyear'=> array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Number required.',
	        	'allowEmpty' => true,
	        	'required' => false
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
	     	'time_event_id' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
		'initial_fact_data_id' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	//'message' => 'Your custom message here',
	        	//'allowEmpty' => false,
	        	//'required' => false,
	        	//'last' => false, // Stop validation after this rule
	        	//'on' => 'create', // Limit validation to 'create' or 'update' operations
	        	),
	        	),
	       'current_fact_data_id' => array(
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
	    'type' => array(
			'numeric' => array(
				'rule' => array('numeric'),
	        	'message' => 'Value required',
	        	//'allowEmpty' => false,
	        	'required' => true,
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
