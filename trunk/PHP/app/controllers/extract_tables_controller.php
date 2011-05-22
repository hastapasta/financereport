<?php
class ExtractTablesController extends AppController {

	var $name = 'ExtractTables';

	function index() {
		$this->ExtractTable->recursive = 0;
		$this->set('extractTables', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid extract table', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('extractTable', $this->ExtractTable->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->ExtractTable->create();
			if ($this->ExtractTable->save($this->data)) {
				$this->Session->setFlash(__('The extract table has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The extract table could not be saved. Please, try again.', true));
			}
		}
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->ExtractTable->saveAll($this->data['ExtractTable'])) {
				$this->Session->setFlash(__('The ExtractTable has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The ExtractTable could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->ExtractTable->find('all',array('conditions'=>array('ExtractTable.id'=>$record))));
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for extract table', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->ExtractTable->delete($id)) {
			$this->Session->setFlash(__('Extract table deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Extract table was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>