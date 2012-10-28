<?php
class Entity extends AppModel {
	var $name = 'Entity';

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
		)
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
		),
		'Country' => array(
			'className' => 'Country',
			'joinTable' => 'countries_entities',
			'foreignKey' => 'entity_id',
			'associationForeignKey' => 'country_id',
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
