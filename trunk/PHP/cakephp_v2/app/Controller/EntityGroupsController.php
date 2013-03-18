<?php
class EntityGroupsController extends AppController {

	var $name = 'EntityGroups';
	
	function beforeFilter() {
		parent::beforeFilter();
		
		//$this->Auth->allow('*');
	}
	
	/*function recoverTree(){
		$this->EntityGroup->recover('parent');
	}*/

	function index() {
		$this->EntityGroup->recursive = 0;
		$this->set('entityGroups', $this->paginate());
		$this->set('entityList', $this->EntityGroup->find('list'));
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid entity group'));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('entityGroup', $this->EntityGroup->read(null, $id));
	}
	
	function recoverTree(){
		$return = "";
		$return = $this->EntityGroup->recover('parent');
		debug($return,true);
		
	}

	function add() {
		if (!empty($this->request->data)) {
			$this->EntityGroup->create();
			if ($this->EntityGroup->save($this->request->data)) {
				$this->Session->setFlash(__('The entity group has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity group cound not be saved. Please see below for additional information.'));
			}
		}
		$parents = $this->EntityGroup->find('list');
		$this->set(compact('parents'));
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->request->data)) {
			if ($this->EntityGroup->saveAll($this->request->data['EntityGroup'])) {
				$this->Session->setFlash(__('The entity group has been saved'),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity group cound not be saved. Please see below for additional information.'));
			}
		}
		if (!empty($record)) {
			$this->request->data = $this->toMulti($this->EntityGroup->find('all',array('conditions'=>array('EntityGroup.id'=>$record))));
				
		}
		if (empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid user'));
			$this->redirect(array('action' => 'index'));
		}
		$parents = $this->EntityGroup->find('list');
		$this->set(compact('parents'));
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for entity group'));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->EntityGroup->delete($id)) {
			$this->Session->setFlash(__('Entity group deleted'),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Entity group was not deleted'));
		$this->redirect(array('action' => 'index'));
	}
}
?>
