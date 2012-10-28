<?php
class TasksController extends AppController {

	var $name = 'Tasks';
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}

	function index() {
		$this->Task->recursive = 0;
		$this->set('tasks', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid task', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('task', $this->Task->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->Task->create();
			if ($this->Task->save($this->data)) {
				$this->Session->setFlash(__('The task has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The task cound not be saved. Please see below for additional information.', true));
			}
		}
		$metrics = $this->Task->Metric->find('list');
		$this->set(compact('metrics'));
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Task->saveAll($this->data['Task'])) {
				$this->Session->setFlash(__('The Task has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The Task cound not be saved. Please see below for additional information.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Task->find('all',array('conditions'=>array('Task.id'=>$record))));
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid Task', true));
			$this->redirect(array('action' => 'index'));
		}
		
		$metrics = $this->Task->Metric->find('list');
		$this->set(compact('metrics'));
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for task', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Task->delete($id)) {
			$this->Session->setFlash(__('Task deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Task was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>
