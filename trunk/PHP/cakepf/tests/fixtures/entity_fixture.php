<?php
/* Entity Fixture generated on: 2011-01-23 11:39:27 : 1295807967 */
class EntityFixture extends CakeTestFixture {
	var $name = 'Entity';

	var $fields = array(
		'id' => array('type' => 'integer', 'null' => false, 'default' => NULL, 'key' => 'primary'),
		'ticker' => array('type' => 'string', 'null' => false, 'default' => NULL, 'length' => 30, 'key' => 'unique', 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'shares_outstanding' => array('type' => 'float', 'null' => true, 'default' => NULL, 'length' => 20),
		'begin_fiscal_calendar' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 12, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'last_reported_quarter' => array('type' => 'integer', 'null' => true, 'default' => NULL, 'length' => 3),
		'next_report_date' => array('type' => 'date', 'null' => true, 'default' => NULL),
		'groups' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 100, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'actual_fiscal_year_end' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 8, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'full_name' => array('type' => 'string', 'null' => true, 'default' => NULL, 'length' => 50, 'collate' => 'latin1_swedish_ci', 'charset' => 'latin1'),
		'depricated' => array('type' => 'date', 'null' => true, 'default' => NULL),
		'indexes' => array('PRIMARY' => array('column' => 'id', 'unique' => 1), 'ticker' => array('column' => 'ticker', 'unique' => 1)),
		'tableParameters' => array('charset' => 'latin1', 'collate' => 'latin1_swedish_ci', 'engine' => 'MyISAM')
	);

	var $records = array(
		array(
			'id' => 1,
			'ticker' => 'Lorem ipsum dolor sit amet',
			'shares_outstanding' => 1,
			'begin_fiscal_calendar' => 'Lorem ipsu',
			'last_reported_quarter' => 1,
			'next_report_date' => '2011-01-23',
			'groups' => 'Lorem ipsum dolor sit amet',
			'actual_fiscal_year_end' => 'Lorem ',
			'full_name' => 'Lorem ipsum dolor sit amet',
			'depricated' => '2011-01-23'
		),
	);
}
?>