<?php
/* Groups Test cases generated on: 2011-01-20 14:02:02 : 1295557322*/
App::import('Controller', 'Groups');

class TestGroupsController extends GroupsController {
	var $autoRender = false;

	function redirect($url, $status = null, $exit = true) {
		$this->redirectUrl = $url;
	}
}

class GroupsControllerTestCase extends CakeTestCase {
	var $fixtures = array('app.group', 'app.user');

	function startTest() {
		$this->Groups =& new TestGroupsController();
		$this->Groups->constructClasses();
	}

	function endTest() {
		unset($this->Groups);
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