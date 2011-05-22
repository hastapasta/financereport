<?php
class ChartsController extends AppController {

	var $name = 'Charts';
	var $uses = array();#this controller uses no models...
	
	
	function chart(){
		
		$userprops = $this->Auth->user();
		$this->set('userid', $userprops['User']['id']);
		
		//debug($userprops,true);
		
	}
	
	function charta(){
		
	}
	function chartb(){
		
	}
	function chartc(){
		
	}
	function chartd(){
	}
	



}
?>
