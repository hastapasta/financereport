<?php
class ExtractSinglesController extends AppController {

	var $name = 'ExtractSingles';

	function index() {
		$this->ExtractSingle->recursive = 0;
		$this->set('extractSingles', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid extract single', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('extractSingle', $this->ExtractSingle->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->ExtractSingle->create();
			if ($this->ExtractSingle->save($this->data)) {
				$this->Session->setFlash(__('The extract single has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The extract single could not be saved. Please, try again.', true));
			}
		}
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->ExtractSingle->saveAll($this->data['ExtractSingle'])) {
				$this->Session->setFlash(__('The ExtractSingle has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The ExtractSingle could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->ExtractSingle->find('all',array('conditions'=>array('ExtractSingle.id'=>$record))));
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid extract single', true));
			$this->redirect(array('action' => 'index'));
		}
		
		/*
		if (!$id && empty($this->data)) {
			$this->Session->setFlash(__('Invalid extract single', true));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->data)) {
			if ($this->ExtractSingle->save($this->data)) {
				$this->Session->setFlash(__('The extract single has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The extract single could not be saved. Please, try again.', true));
			}
		}
		if (empty($this->data)) {
			$this->data = $this->ExtractSingle->read(null, $id);
		}*/
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for extract single', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->ExtractSingle->delete($id)) {
			$this->Session->setFlash(__('Extract single deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Extract single was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>