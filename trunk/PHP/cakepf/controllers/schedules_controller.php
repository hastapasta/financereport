<?php
class SchedulesController extends AppController {

	var $name = 'Schedules';
	var $uses = array('Schedule','Task','RepeatType','EntityGroup');

	function beforeFilter() {
		parent::beforeFilter();
		$this->Auth->allowedActions = array('index', 'view');
	}


	function index() {
		$this->Schedule->recursive = 2;
		$this->set('schedules', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid schedule', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('schedule', $this->Schedule->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			//debug($this->data); exit();
			$this->Schedule->create();
			if ($this->Schedule->save($this->data)) {
				$this->Session->setFlash(__('The schedule has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The schedule could not be saved. Please, try again.', true));
			}
		}
		$tasks = $this->Schedule->Task->find('list');
		$repeatTypes = $this->Schedule->RepeatType->find('list',array('fields'=>array('type')));
		$this->set(compact('tasks','repeatTypes'));
	}

	function edit($id = null) {
		$this->Schedule->recursive = 0;
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Schedule->saveAll($this->data['Schedule'])) {
				$this->Session->setFlash(__('The schedule has been saved', true));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The schedule could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Schedule->find('all',array('conditions'=>array('Schedule.id'=>$record))));
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		$repeat_types = $this->RepeatType->find('list', array('fields'=> array('RepeatType.description')
		));
		$task_names = $this->Task->find('list', array('fields'=> array('Task.name')
		));
		$this->set('task_names',$task_names);
		$this->set('repeat_types',$repeat_types);
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for schedule', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Schedule->delete($id)) {
			$this->Session->setFlash(__('Schedule deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Schedule was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>
