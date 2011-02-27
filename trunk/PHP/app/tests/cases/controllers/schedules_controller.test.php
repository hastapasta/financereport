<?php
/* Schedules Test cases generated on: 2011-01-20 14:03:09 : 1295557389*/
App::import('Controller', 'Schedules');

class TestSchedulesController extends SchedulesController {
	var $autoRender = false;

	function redirect($url, $status = null, $exit = true) {
		$this->redirectUrl = $url;
	}
}

class SchedulesControllerTestCase extends CakeTestCase {
	var $fixtures = array('app.schedule');

	function startTest() {
		$this->Schedules =& new TestSchedulesController();
		$this->Schedules->constructClasses();
	}

	function endTest() {
		unset($this->Schedules);
		ClassRegistry::flush();
	}

	function testIndex() {

	}

	function testView() {

	}

	function testAdd() {

	}

	function testEdit() {

	}

	function testDelete() {

	}

}
?>