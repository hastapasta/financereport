<?php
class RepeatTypesController extends AppController {

	var $name = 'RepeatTypes';
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}

	function index() {
		$this->RepeatType->recursive = 0;
		$this->set('repeatTypes', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid repeat type'));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('repeatType', $this->RepeatType->read(null, $id));
	}

	function add() {
		if (!empty($this->request->data)) {
			$this->RepeatType->create();
			if ($this->RepeatType->save($this->request->data)) {
				$this->Session->setFlash(__('The repeat type has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The repeat type cound not be saved. Please see below for additional information.'));
			}
		}
	}

	function edit($id = null) {
		if (!$id && empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid repeat type'));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->request->data)) {
			if ($this->RepeatType->save($this->request->data)) {
				$this->Session->setFlash(__('The repeat type has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The repeat type cound not be saved. Please see below for additional information.'));
			}
		}
		if (empty($this->request->data)) {
			$this->request->data = $this->RepeatType->read(null, $id);
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for repeat type'));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->RepeatType->delete($id)) {
			$this->Session->setFlash(__('Repeat type deleted'),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Repeat type was not deleted'));
		$this->redirect(array('action' => 'index'));
	}
}
?>
