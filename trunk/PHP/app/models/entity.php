<?php
class Entity extends AppModel {
	var $name = 'Entity';
	//var $displayField = 'ticker';
	//The Associations below have been created with all possible keys, those that are not needed can be removed

	var $hasMany = array(
		'Alert' => array(
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
		/*'FactDatum' => array(
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
		'EntityGroup' => array(
			'className' => 'EntityGroup',
			'joinTable' => 'entities_entity_groups',
			'foreignKey' => 'entity_id',
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
		)
	);

}
?>
