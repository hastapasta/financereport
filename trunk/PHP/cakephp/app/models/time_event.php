<?php
class TimeEvent extends AppModel {
	var $name = 'TimeEvent';
	
	        	        	 
	        		



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
		
	        	);
}
?>
