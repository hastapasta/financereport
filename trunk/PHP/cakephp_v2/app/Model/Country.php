<?php
class Country extends AppModel {
	var $name = 'Country';

	var $hasAndBelongsToMany = array(
		'Entity' => array(
			'className' => 'Entity',
			'joinTable' => 'countries_entities',
			'foreignKey' => 'country_id',
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
		)
	);

}
?>
