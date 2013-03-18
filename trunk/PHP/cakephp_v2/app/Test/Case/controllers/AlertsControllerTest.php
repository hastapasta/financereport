<?php
/* Alerts Test cases generated on: 2011-01-20 14:02:29 : 1295557349*/
App::import('Controller', 'Alerts');

class TestAlertsController extends AlertsController {
	var $autoRender = false;

	function redirect($url, $status = null, $exit = true) {
		$this->redirectUrl = $url;
	}
}

class AlertsControllerTest extends CakeTestCase {
	var $fixtures = array('app.alert');

	function startTest() {
		$this->Alerts =& new TestAlertsController();
		$this->Alerts->constructClasses();
	}

	function endTest() {
		unset($this->Alerts);
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