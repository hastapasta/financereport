<?php
class GroupsController extends AppController {

	var $name = 'Groups';


	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}


	function index() {
		$this->Group->recursive = 0;
		$this->set('groups', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid group'));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('group', $this->Group->read(null, $id));
	}

	function add() {
		if (!empty($this->request->data)) {
			$this->Group->create();
			if ($this->Group->save($this->request->data)) {
				$this->Session->setFlash(__('The group has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The group cound not be saved. Please see below for additional information.'));
			}
		}
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->request->data)) {
			if ($this->Group->saveAll($this->request->data['Group'])) {
				$this->Session->setFlash(__('The group has been saved'),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The group cound not be saved. Please see below for additional information.'));
			}
		}
		if (!empty($record)) {
			$this->request->data = $this->toMulti($this->Group->find('all',array('conditions'=>array('Group.id'=>$record))));
				
		}
		if (empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid user'));
			$this->redirect(array('action' => 'index'));
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for group'));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Group->delete($id)) {
			$this->Session->setFlash(__('Group deleted'),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Group was not deleted'));
		$this->redirect(array('action' => 'index'));
	}
}
?>

