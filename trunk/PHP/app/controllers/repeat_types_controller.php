<?php
class RepeatTypesController extends AppController {

	var $name = 'RepeatTypes';

	function index() {
		$this->RepeatType->recursive = 0;
		$this->set('repeatTypes', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid repeat type', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('repeatType', $this->RepeatType->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->RepeatType->create();
			if ($this->RepeatType->save($this->data)) {
				$this->Session->setFlash(__('The repeat type has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The repeat type could not be saved. Please, try again.', true));
			}
		}
	}

	function edit($id = null) {
		if (!$id && empty($this->data)) {
			$this->Session->setFlash(__('Invalid repeat type', true));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->data)) {
			if ($this->RepeatType->save($this->data)) {
				$this->Session->setFlash(__('The repeat type has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The repeat type could not be saved. Please, try again.', true));
			}
		}
		if (empty($this->data)) {
			$this->data = $this->RepeatType->read(null, $id);
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for repeat type', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->RepeatType->delete($id)) {
			$this->Session->setFlash(__('Repeat type deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Repeat type was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>