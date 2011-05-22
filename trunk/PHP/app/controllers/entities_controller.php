<?php
class EntitiesController extends AppController {

	var $name = 'Entities';
	var $uses = array('Entity','Alert','User','EntityGroup');
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}

	function index() {
		$this->urlToNamed();
		$this->Entity->recursive = 0;
		$conditions = array();
		
		
		if(isset($this->passedArgs['enable']) && $this->passedArgs['enable'] == 1){
			if( isset($this->passedArgs['ticker']) && !empty($this->passedArgs['ticker']))
				$conditions['Entity.ticker LIKE'] = "%".$this->passedArgs['ticker']."%";
			if(isset($this->passedArgs['name']) && !empty($this->passedArgs['name']))
				$conditions['Entity.full_name LIKE'] = "%".$this->passedArgs['name']."%";
			if(isset($this->passedArgs['country_id']) && !empty($this->passedArgs['country_id']))
				$conditions['Entity.country_id'] = $this->passedArgs['country_id']; 
		}
			
		//debug($conditions);
		$this->set('entities', $this->paginate($conditions));
	}

	function view($id = null) {
		
		$userprops = $this->Auth->user();
		
		if($userprops['User']['group_id'] != 1)
		{ 
			$tmpuserid = $this->User->find('first', array('fields'=> 'id','conditions'=>array('User.username'=>$userprops['User']['username'])));
			$conditions['Alert.user_id'] = $tmpuserid['User']['id'];
		}
		
		if (!$id) {
			$this->Session->setFlash(__('Invalid entity', true));
			$this->redirect(array('action' => 'index'));
		}
		/*
		 * Making it recursive causes a retrieval of all of the alerts; don't need that data.
		 */
		//$this->Entity->recursive = 1;
		$this->set('entity', $this->Entity->read(null, $id));
		
	}

	function add() {
		if (!empty($this->data)) {
			$this->Entity->create();
			if ($this->Entity->save($this->data)) {
				$this->Session->setFlash(__('The entity has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity could not be saved. Please, try again.', true));
			}
		}
	}

	function edit($id = null) {
		$this->Entity->recursive = 0;
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Entity->saveAll($this->data['Entity'])) {
				$this->Session->setFlash(__('The Entities has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The Entities could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Entity->find('all',array('conditions'=>array('Entity.id'=>$record))));
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid Entities', true));
			$this->redirect(array('action' => 'index'));
		}	
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for entity', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Entity->delete($id)) {
			$this->Session->setFlash(__('Entity deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Entity was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
	
	function getCountry(){
		$this->autoRender = false;
		$this->layout = null;
		$term  =  $_REQUEST['term'];
		$this->loadModel("Country");
		$conditions = array();
		$conditions['Country.name LIKE'] = $term."%";
		$countries = $this->Country->find('all', array('conditions'=>$conditions, 'order'=>'name ASC', 'fields'=>array('id','name')));
		$this->set("data", $countries);
		$this->render('json');	
	}
}
?>