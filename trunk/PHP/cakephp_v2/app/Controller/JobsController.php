<?php
class JobsController extends AppController {

	var $name = 'Jobs';

	function index() {
		$this->Job->recursive = 0;
		$this->set('jobs', $this->paginate());
	}
	
	function beforeFilter() {
		parent::beforeFilter();
		$this->Auth->allow('*');
	}
	
	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid job'));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('job', $this->Job->read(null, $id));
	}

	function add() {
		if (!empty($this->request->data)) {
			$this->Job->create();
			if ($this->Job->save($this->request->data)) {
				$this->Session->setFlash(__('The job has been saved'),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The job cound not be saved. Please see below for additional information.'));
			}
		}
	}

	function edit($id = null) {
		//debug($this->Session->read());exit;
		if($id != null){
			$conditions[] = $id;
			$this->Session->write('Record',$conditions);
		}
		
		$record = $this->Session->read('Record');
		if (!empty($this->request->data)) {
		
			if ($this->Job->saveAll($this->request->data['Job'])) {
				//debug($this->request->data['Job']);exit;	
				$this->Session->setFlash(__('The Job has been saved'),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The Job cound not be saved. Please see below for additional information.'));
			}
		}
		if (!empty($record)) {
			$this->request->data = $this->toMulti($this->Job->find('all',array('conditions'=>array('Job.id'=>$record))));
				
		}
		if (empty($this->request->data)) {
			$this->Session->setFlash(__('Invalid Job'));
			$this->redirect(array('action' => 'index'));
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for job'));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Job->delete($id)) {
			$this->Session->setFlash(__('Job deleted'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Job was not deleted'));
		$this->redirect(array('action' => 'index'));
	}
}
?>
