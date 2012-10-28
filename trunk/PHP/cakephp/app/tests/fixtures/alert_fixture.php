<?php
/* Alert Fixture generated on: 2011-01-20 14:02:28 : 1295557348 */
class AlertFixture extends CakeTestFixture {
	var $name = 'Alert';

	var $fields = array(
		'primary_key' => array('type' => 'integer', 'null' => false, 'default' => NULL, 'key' => 'primary'),
		'type' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 20, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'schedule' => array('type' => 'integer', 'null' => false, 'default' => '-1'),
		'email' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 40, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'frequency' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 20, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'limit_value' => array('type' => 'float', 'null' => true, 'default' => NULL, 'length' => '8,3'),
		'limit_adjustment' => array('type' => 'float', 'null' => true, 'default' => '0.000', 'length' => '8,3'),
		'fact_data_key' => array('type' => 'integer', 'null' => false, 'default' => '-1'),
		'ticker' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 30, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'alert_count' => array('type' => 'integer', 'null' => false, 'default' => '0'),
		'disabled' => array('type' => 'boolean', 'null' => false, 'default' => '0'),
		'user' => array('type' => 'string', 'null' => false, 'default' => 'ollie', 'length' => 20, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'indexes' => array('PRIMARY' => array('column' => 'primary_key', 'unique' => 1)),
		'tableParameters' => array('charset' => 'latin1', 'collate' => 'latin1_swedish_ci', 'engine' => 'MyISAM')
	);

	var $records = array(
		array(
			'primary_key' => 1,
			'type' => 'Lorem ipsum dolor ',
			'schedule' => 1,
			'email' => 'Lorem ipsum dolor sit amet',
			'frequency' => 'Lorem ipsum dolor ',
			'limit_value' => 1,
			'limit_adjustment' => 1,
			'fact_data_key' => 1,
			'ticker' => 'Lorem ipsum dolor sit amet',
			'alert_count' => 1,
			'disabled' => 1,
			'user' => 'Lorem ipsum dolor '
		),
	);
}
?>