<?php
/* Entities Test cases generated on: 2011-01-23 11:50:08 : 1295808608*/
App::import('Controller', 'Entities');

class TestEntitiesController extends EntitiesController {
	var $autoRender = false;

	function redirect($url, $status = null, $exit = true) {
		$this->redirectUrl = $url;
	}
}

class EntitiesControllerTest extends CakeTestCase {
	var $fixtures = array('app.entity', 'app.alert', 'app.schedule', 'app.task', 'app.user', 'app.group', 'app.entity_group', 'app.fact_datum');

	function startTest() {
		$this->Entities =& new TestEntitiesController();
		$this->Entities->constructClasses();
	}

	function endTest() {
		unset($this->Entities);
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