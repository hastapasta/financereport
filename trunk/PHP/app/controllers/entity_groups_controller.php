<?php
class EntityGroupsController extends AppController {

	var $name = 'EntityGroups';
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}

	function index() {
		$this->EntityGroup->recursive = 0;
		$this->set('entityGroups', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid entity group', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('entityGroup', $this->EntityGroup->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->EntityGroup->create();
			if ($this->EntityGroup->save($this->data)) {
				$this->Session->setFlash(__('The entity group has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity group could not be saved. Please, try again.', true));
			}
		}
		$entities = $this->EntityGroup->Entity->find('list');
		$tasks = $this->EntityGroup->Task->find('list');
		$entityGroupsTasks = $this->EntityGroup->EntityGroupsTask->find('list');
		$this->set(compact('entities', 'tasks', 'entityGroupsTasks'));
	}

	function edit($id = null) {
		if (!$id && empty($this->data)) {
			$this->Session->setFlash(__('Invalid entity group', true));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->data)) {
			if ($this->EntityGroup->save($this->data)) {
				$this->Session->setFlash(__('The entity group has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity group could not be saved. Please, try again.', true));
			}
		}
		if (empty($this->data)) {
			$this->data = $this->EntityGroup->read(null, $id);
		}
		$entities = $this->EntityGroup->Entity->find('list');
		$tasks = $this->EntityGroup->Task->find('list');
		$entityGroupsTasks = $this->EntityGroup->EntityGroupsTask->find('list');
		$this->set(compact('entities', 'tasks', 'entityGroupsTasks'));
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for entity group', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->EntityGroup->delete($id)) {
			$this->Session->setFlash(__('Entity group deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Entity group was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>