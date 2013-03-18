<?php
class ExtractSinglesController extends AppController {

	var $name = 'ExtractSingles';

	function index() {
		$this->ExtractSingle->recursive = 0;
		$this->set('extractSingles', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid extract single'));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('extractSingle', $this->ExtractSingle->read(null, $id));
	}

	function add() {
		if (!empty($this->request->data)) {
			$this->ExtractSingle->create();
			if ($this->ExtractSingle->save($this->request->data)) {
				$this->Session->setFlash(__('The extract single has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The extract single cound not be saved. Please see below for additional information.'));
			}
		}
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->request->data)) {
			if ($this->ExtractSingle->saveAll($this->request->data['ExtractSingle'])) {
				$this->Session->setFlash(__('The ExtractSingle has been saved'),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The ExtractSingle cound not be saved. Please see below for additional information.'));
			}
		}
		if (!empty($record)) {
			$this->request->data = $this->toMulti($this->ExtractSingle->find('all',array('conditions'=>array('ExtractSingle.id'=>$record))));
				
		}
		if (empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid extract single'));
			$this->redirect(array('action' => 'index'));
		}
		
		/*
		if (!$id && empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid extract single'));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->request->data)) {
			if ($this->ExtractSingle->save($this->request->data)) {
				$this->Session->setFlash(__('The extract single has been saved'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The extract single cound not be saved. Please see below for additional information.'));
			}
		}
		if (empty($this->request->data)) {
			$this->request->data = $this->ExtractSingle->read(null, $id);
		}*/
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for extract single'));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->ExtractSingle->delete($id)) {
			$this->Session->setFlash(__('Extract single deleted'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Extract single was not deleted'));
		$this->redirect(array('action' => 'index'));
	}
}
?>
