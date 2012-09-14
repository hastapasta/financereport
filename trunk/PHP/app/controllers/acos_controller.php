<?php
class AcosController extends AppController {

	var $name = 'Acos';
	
	function beforeFilter() {
		parent::beforeFilter();
		
		//$this->Auth->allow('*');
	}
	
	function recoverTree(){
		$this->Aco->recover('parent');
	}

	
}
?>

