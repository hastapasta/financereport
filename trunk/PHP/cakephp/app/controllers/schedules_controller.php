<?php
class SchedulesController extends AppController {

	var $name = 'Schedules';
	var $uses = array('Schedule','Task','RepeatType','EntityGroup',
	    'Job','Metric','TimeEvent','RepeatType');


	function beforeFilter() {
		parent::beforeFilter();
	}

	function index() {
		$this->Schedule->recursive = 2;
		$userprops = $this->Auth->user();
		$conditions = null;
		if($userprops['User']['group_id'] != 1)	{
			$tmpuserid = $this->User->find('first', array('fields'=> 'id',
			    'conditions'=>array('User.username'=>
			        $userprops['User']['username'])));
			$conditions = array('Alert.user_id'  => $tmpuserid['User']['id']);
		}
		$filtervalues = $this->data;
		$conditions = $this->Session->read('schedulefilterconditions');
		//debug($filtervalues);
		if ($filtervalues != null) {
				
			//Filter values were refreshed; need to reset conditions.		
			$conditions = "";

			if (!empty($filtervalues)) {
				if ($filtervalues['Schedule']['filtersenabled']=='1') {
					if(isset($filtervalues['Schedule']['custom_verify_mode']) && 
					    $filtervalues['Schedule']['custom_verify_mode'] != ''){
						$conditions['Schedule.verify_mode'] 
						  = $filtervalues['Schedule']['custom_verify_mode'];
					}
					if(!empty($filtervalues['Schedule']['task_id'])) {
					  $conditions['Schedule.task_id']
					  = $filtervalues['Schedule']['task_id'];
					}
					if(!empty($filtervalues['Schedule']['obsolete_data_set'])) {
						$conditions['Schedule.obsolete_data_set LIKE'] 
						  = '%'.$filtervalues['Schedule']['obsolete_data_set'].'%';
					}
					if(!empty($filtervalues['Schedule']['repeat_type_id'])) {
						if ($filtervalues['Schedule']['notrepeattype']=='1') {
						  	$conditions['Schedule.repeat_type_id !='] 
						      = $filtervalues['Schedule']['repeat_type_id'];
						}
						else {
						  $conditions['Schedule.repeat_type_id']
						    = $filtervalues['Schedule']['repeat_type_id'];  
						}
					}
				}
			}
			
			$this->Session->write('schedulefilterconditions',$conditions);
			$this->Session->write('schedulefiltervalues',$filtervalues);
		}

		$task_names2 = $this->Task->find('list', 
		    array('fields'=>array('id', 'name')));
		
		$verifyMode=array('0'=>'0','1'=>'1');
		$this->set(compact('verifyMode'));

		$repeatTypeId = $this->RepeatType->find('list',
		    array('fields'=>array('id','description')));
		$this->set(compact('repeatTypeId'));

		// set users dropdown
		$users = $this->User->find('list', 
		    array('fields'=>array('id', 'username')));
		$this->set(compact('users'));

		$this->set('schedules',$this->paginate(null,$conditions));
	}

	
	function turnoff(){
		$this->Schedule->query('update schedules set repeat_type_id = 10');
		$this->Session->setFlash(__('Set all schedules to DO NOT
		     EXECUTE repeat type', true),'default',
		    array('class'=>'green_message'));
		$this->redirect($this->referer());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid schedule', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('schedule', $this->Schedule->read(null, $id));
	}
	
	function fullview($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->Schedule->recursive = 2;
		$this->set('schedule', $this->Schedule->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			//debug($this->data); exit();
			$this->Schedule->create();
			if ($this->Schedule->save($this->data)) {
				$this->Session->setFlash(__('The schedule has been saved', true),
				    'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The schedule cound not be saved.
				     Please see below for additional information.', true));
			}
		}
		$tasks = $this->Schedule->Task->find('list');
		$repeatTypes = $this->Schedule->RepeatType
		  ->find('list',array('fields'=>array('type')));
		$this->set(compact('tasks','repeatTypes'));
	}

	function edit($id = null) {
		$this->Schedule->recursive = 0;
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Schedule->saveAll($this->data['Schedule'])) {
				$this->Session->setFlash(__('The schedule has been saved', true),
				    'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The schedule cound not be saved. 
				    Please see below for additional information.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Schedule->find('all',
			    array('conditions'=>array('Schedule.id'=>$record))));
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		$repeat_types = $this->RepeatType->find('list', 
		    array('fields'=> array('RepeatType.description')
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
			$this->Session->setFlash(__('Schedule deleted', true),'default',
			    array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Schedule was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
	
	public function set_default_schedules() {
		$this->Schedule->query("TRUNCATE TABLE schedules");
		$this->Schedule
		  ->query("INSERT INTO schedules SELECT * FROM default_schedules");
		$this->Session->setFlash(__('Set all schedules to default configuration', 
		    true),'default',array('class'=>'green_message'));
		$this->redirect(array('action' => 'index'));
	}
}
?>