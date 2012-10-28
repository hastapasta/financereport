<?php
class Task extends AppModel {
	var $name = 'Task';
	var $validate = array(
		'name' => array(
			'notempty' => array(
				'rule' => array('notempty'),
			//'message' => 'Your custom message here',
			//'allowEmpty' => false,
			//'required' => false,
			//'last' => false, // Stop validation after this rule
			//'on' => 'create', // Limit validation to 'create' or 'update' operations
			),
			),
		'metric_id' => array(
			'notempty' => array(
				'rule' => array('numeric'),
			//'message' => 'Your custom message here',
			//'allowEmpty' => false,
			//'required' => false,
			//'last' => false, // Stop validation after this rule
			//'on' => 'create', // Limit validation to 'create' or 'update' operations
			),
			)
	);
	//The Associations below have been created with all possible keys, those that are not needed can be removed
	var $belongsTo = array("Metric");
	var $hasMany = array(
		'Schedule' => array(
			'className' => 'Schedule',
			'foreignKey' => 'task_id',
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


			var $hasAndBelongsToMany = array(
		'EntityGroup' => array(
			'className' => 'EntityGroup',
			'joinTable' => 'entity_groups_tasks',
			'foreignKey' => 'task_id',
			'associationForeignKey' => 'entity_group_id',
			'unique' => true,
			'conditions' => '',
			'fields' => '',
			'order' => '',
			'limit' => '',
			'offset' => '',
			'finderQuery' => '',
			'deleteQuery' => '',
			'insertQuery' => ''
			),
		'Job' => array(
			'className' => 'Job',
			'joinTable' => 'jobs_tasks',
			'foreignKey' => 'task_id',
			'associationForeignKey' => 'job_id',
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