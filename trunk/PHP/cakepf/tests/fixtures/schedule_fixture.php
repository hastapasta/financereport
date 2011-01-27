<?php
/* Schedule Fixture generated on: 2011-01-20 14:03:09 : 1295557389 */
class ScheduleFixture extends CakeTestFixture {
	var $name = 'Schedule';

	var $fields = array(
		'obsolete_data_set' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 45, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'task' => array('type' => 'integer', 'null' => false, 'default' => NULL),
		'Start_Date' => array('type' => 'date', 'null' => true, 'default' => NULL),
		'Repeat_Type' => array('type' => 'integer', 'null' => true, 'default' => NULL, 'length' => 5),
		'last_run' => array('type' => 'datetime', 'null' => true, 'default' => NULL),
		'primary_key' => array('type' => 'integer', 'null' => false, 'default' => NULL, 'key' => 'primary'),
		'run_once' => array('type' => 'boolean', 'null' => true, 'default' => NULL),
		'priority' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'companygroup' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 40, 'collate' => 'latin1_swedish_ci', 'comment' => 'Used for actuall processing', 'charset' => 'latin1'),
		'configure_notification' => array('type' => 'boolean', 'null' => false, 'default' => '0', 'comment' => 'Flag which indicates whether or not an end user can configure notifications against this data_set'),
		'referencegroup' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 40, 'collate' => 'latin1_swedish_ci', 'comment' => 'Used for reference purposed (creating alerts)', 'charset' => 'latin1'),
		'indexes' => array('PRIMARY' => array('column' => 'primary_key', 'unique' => 1)),
		'tableParameters' => array('charset' => 'latin1', 'collate' => 'latin1_swedish_ci', 'engine' => 'MyISAM')
	);

	var $records = array(
		array(
			'obsolete_data_set' => 'Lorem ipsum dolor sit amet',
			'task' => 1,
			'Start_Date' => '2011-01-20',
			'Repeat_Type' => 1,
			'last_run' => '2011-01-20 14:03:09',
			'primary_key' => 1,
			'run_once' => 1,
			'priority' => 1,
			'companygroup' => 'Lorem ipsum dolor sit amet',
			'configure_notification' => 1,
			'referencegroup' => 'Lorem ipsum dolor sit amet'
		),
	);
}
?>