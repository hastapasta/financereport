<?php
class ChartsController extends AppController {

	var $name = 'Charts';
	var $uses = array();#this controller uses no models...
	
	
	function chart(){
		
		$userprops = $this->Auth->user();
		$this->set('userid', $userprops['User']['id']);
		if ($userprops['User']['group_id'] == 1){
			//put the group condition here			
			$this->set('admin', true);
		}else{
			$this->set('admin',false);
		}
		
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