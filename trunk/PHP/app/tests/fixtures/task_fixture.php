<?php
/* Task Fixture generated on: 2011-01-20 19:53:27 : 1295578407 */
class TaskFixture extends CakeTestFixture {
	var $name = 'Task';

	var $fields = array(
		'id' => array('type' => 'integer', 'null' => false, 'default' => NULL, 'key' => 'primary'),
		'name' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 40, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'description' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 100, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'job_primary_key1' => array('type' => 'integer', 'null' => false, 'default' => NULL),
		'job_primary_key2' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key3' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key4' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key5' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key6' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key7' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key8' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key9' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'job_primary_key10' => array('type' => 'integer', 'null' => true, 'default' => NULL),
		'indexes' => array('PRIMARY' => array('column' => 'id', 'unique' => 1)),
		'tableParameters' => array('charset' => 'latin1', 'collate' => 'latin1_swedish_ci', 'engine' => 'MyISAM')
	);

	var $records = array(
		array(
			'id' => 1,
			'name' => 'Lorem ipsum dolor sit amet',
			'description' => 'Lorem ipsum dolor sit amet',
			'job_primary_key1' => 1,
			'job_primary_key2' => 1,
			'job_primary_key3' => 1,
			'job_primary_key4' => 1,
			'job_primary_key5' => 1,
			'job_primary_key6' => 1,
			'job_primary_key7' => 1,
			'job_primary_key8' => 1,
			'job_primary_key9' => 1,
			'job_primary_key10' => 1
		),
	);
}
?>