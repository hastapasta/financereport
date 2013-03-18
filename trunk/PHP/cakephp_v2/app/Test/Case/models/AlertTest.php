<?php
/* Alert Test cases generated on: 2011-01-20 14:02:28 : 1295557348*/
App::import('Model', 'Alert');

class AlertTest extends CakeTestCase {
	var $fixtures = array('app.alert');

	function startTest() {
		$this->Alert =& ClassRegistry::init('Alert');
	}

	function endTest() {
		unset($this->Alert);
		ClassRegistry::flush();
	}

}
?>