<?php
class PagelimitController extends AppController {

	var $name = 'Pagelimit';
	var $uses = 'User';
	
	function change( $limit = 10)
	{
		
		$this->User->query("update users set pagination_limit = $limit where id = ". $this->Auth->user('id'));
		$this->redirect($this->referer());	
	}
	
}
?>
