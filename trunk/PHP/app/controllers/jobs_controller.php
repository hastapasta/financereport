<?php
class JobsController extends AppController {

	var $name = 'Jobs';

	function index() {
		$this->Job->recursive = 0;
		$this->set('jobs', $this->paginate());
	}
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}
	
	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid job', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('job', $this->Job->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->Job->create();
			if ($this->Job->save($this->data)) {
				$this->Session->setFlash(__('The job has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The job could not be saved. Please, try again.', true));
			}
		}
	}

	function edit($id = null) {
		debug('here',true);
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Job->saveAll($this->data['Job'])) {
				$this->Session->setFlash(__('The Job has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The Job could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Job->find('all',array('conditions'=>array('Job.id'=>$record))));
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid Job', true));
			$this->redirect(array('action' => 'index'));
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for job', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Job->delete($id)) {
			$this->Session->setFlash(__('Job deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Job was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>