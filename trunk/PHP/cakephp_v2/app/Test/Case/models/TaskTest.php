<?php
/* Task Test cases generated on: 2011-01-20 19:53:27 : 1295578407*/
App::import('Model', 'Task');

class TaskTest extends CakeTestCase {
	var $fixtures = array('app.task');

	function startTest() {
		$this->Task =& ClassRegistry::init('Task');
	}

	function endTest() {
		unset($this->Task);
		ClassRegistry::flush();
	}

}
?>