<?php
class EntityGroup extends AppModel {
	var $name = 'EntityGroup';
	var $actsAs = array('Tree');
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
	);
	//The Associations below have been created with all possible keys, those that are not needed can be removed

	var $hasAndBelongsToMany = array(
		'Entity' => array(
			'className' => 'Entity',
			'joinTable' => 'entities_entity_groups',
			'foreignKey' => 'entity_group_id',
			'associationForeignKey' => 'entity_id',
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
		'Task' => array(
			'className' => 'Task',
			'joinTable' => 'entity_groups_tasks',
			'foreignKey' => 'entity_group_id',
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
