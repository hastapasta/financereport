<?php
class Job extends AppModel {
	var $name = 'Job';
	//var $displayField = 'ticker';
	//The Associations below have been created with all possible keys, those that are not needed can be removed
	
	/*var $belongsTo = array(
	        'Extract_Single' => array( 
	            'className' => 'Extract_Single', 
	            'foreignKey' => 'extract_id' 
	            )
	          );*/

	var $hasMany = array(
		/*'Alert' => array(
			'className' => 'Alert',
			'foreignKey' => 'entity_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		),
		'FactDatum' => array(
			'className' => 'FactDatum',
			'foreignKey' => 'entity_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		),*/
		/*'FactDataStageEst' => array(
			'className' => 'FactDataStageEst',
			'foreignKey' => 'entity_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		),
		'FactDataStageEstSave' => array(
			'className' => 'FactDataStageEstSave',
			'foreignKey' => 'entity_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		),
		'LogAlert' => array(
			'className' => 'LogAlert',
			'foreignKey' => 'entity_id',
			'dependent' => false,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'exclusive' => '',
			'finderQuery' => '',
			'counterQuery' => ''
		)*/
	);


	var $hasAndBelongsToMany = array(
		'Task' => array(
			'className' => 'Task',
			'joinTable' => 'jobs_tasks',
			'foreignKey' => 'job_id',
			'associationForeignKey' => 'task_id',
			'unique' => true,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'finderQuery' => '',
			'deleteQuery' => '',
			'insertQuery' => ''
		)
	);

}
?>
