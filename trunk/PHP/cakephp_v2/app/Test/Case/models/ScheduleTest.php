<?php
/* Schedule Test cases generated on: 2011-01-20 14:03:09 : 1295557389*/
App::import('Model', 'Schedule');

class ScheduleTest extends CakeTestCase {
	var $fixtures = array('app.schedule');

	function startTest() {
		$this->Schedule =& ClassRegistry::init('Schedule');
	}

	function endTest() {
		unset($this->Schedule);
		ClassRegistry::flush();
	}

}
?>