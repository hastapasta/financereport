<?php
class AlertsController extends AppController {

	var $name = 'Alerts';
	var $uses = array('Alert','Schedule','Task', 'User','Entity');

	function beforeFilter() {
	    parent::beforeFilter(); 
	    $this->Auth->allowedActions = array('index', 'view');

		/*
		* Technically we should be pulling a list of all tasks that are referenced in the schedules table, but
		* for now we'll just pull a list straight from the tasks table.
		*/

		//App::import('Model', 'Schedule'); 
		//debug($this,true);
		
	    	    
	    /*$userprops = $this->Auth->user();

		$task_names = $this->Task->find('list', array(   'fields'=> 'name',    'order'=>'Task.name ASC',  'conditions'=> array('1' => '1'),'group'=>'name')); 
		$this->set('task_names', $task_names); 
		
		if ($userprops['User']['group_id'] == 1)
		{
			//put the group condition here
			$emails = $this->User->find('list', array(   'fields'=> 'email',    'order'=>'User.email ASC',  'conditions'=> array('NOT' => array('User.email' => '')))); 
			$this->set('emails', $emails); 
		}
		else 
		{
			$this->set('emails',array('1'=>$userprops['User']['email']));
		}
		
		$frequencies = $this->Alert->find('list', array(   'fields'=> 'frequency',    'group'=>'frequency'));
		$this->set('frequencies',$frequencies);*/

		//debug($task_names,true);
	}


	function index() {
		$this->Alert->recursive = 2;
		$this->set('alerts', $this->paginate());
		
		
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('alert', $this->Alert->read(null, $id));
	}
	
	function multiAdd() {
		debug("begin multiAdd",true);
		if (!empty($this->data)) {
			$this->Alert->create();
			if ($this->Alert->save($this->data)) {
				$this->Session->setFlash(__('The alert has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert could not be saved. Please, try again.', true));
			}
		}
		
		
		//debug($userprops,true);
		
		$markers = $this->Marker->find('all', array('joins' => array(
    array(
        'table' => 'markers_tags',
        'alias' => 'MarkersTag',
        'type' => 'inner',
        'foreignKey' => false,
        'conditions'=> array('MarkersTag.marker_id = Marker.id')
    ),
    array(
        'table' => 'tags',
        'alias' => 'Tag',
        'type' => 'inner',
        'foreignKey' => false,
        'conditions'=> array(
            'Tag.id = MarkersTag.tag_id',
            'Tag.tag' => explode(' ', $this->params['url']['q'])
        )
    )
)));
		
		$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$this->set('task_names', $task_names); 
		
		$entity_descs = $this->Entity->find('list', array('fields'=> 'full_name',    'order'=>'Entity.full_name ASC'));
		$this->set('entity_descs', $entity_descs); 
		
		//$entities = $this->Entity->find('list', array(''))
		$userprops = $this->Auth->user();
		if ($userprops['User']['group_id'] == 1)
		{
			//put the group condition here
			$names = $this->User->find('list', array(   'fields'=> 'username',    'order'=>'User.username ASC',  'conditions'=> array('NOT' => array('User.username' => '')))); 
			$this->set('usernames', $names); 
		}
		else 
		{
			$this->set('usernames',array('1'=>$userprops['User']['username']));
		}
		
		$frequencies = $this->Alert->find('list', array(   'fields'=> 'frequency',    'group'=>'frequency'));
		$this->set('frequencies',array('HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));
		//$this->set('limit_adjustment',.2);
		$this->set('fact_data_key',0);
		$this->set('alert_count',0);
		$this->set('type','LIMIT');
		debug("end multiAdd",true);
		//$this->set('user',$userprops['User']['id']);
		
	}

	function add() {
		//debug("begin add",true);
		debug($this->data, true);
		if (!empty($this->data)) {
			$this->Alert->create();
			if ($this->Alert->save($this->data)) {
				$this->Session->setFlash(__('The alert has been saved', true));
				//$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert could not be saved. Please, try again.', true));
			}
		}
		
		
		//debug($userprops,true);
		$task_names2 = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		)));
		$this->set('task_names2', $task_names2); 
		//debug($task_names2,true);
		$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$this->set('task_names', $task_names); 
		
		$entity_descs = $this->Entity->find('list', array('fields'=> 'full_name',    'order'=>'Entity.full_name ASC'));
		$this->set('entity_descs', $entity_descs); 
		
		//$entities = $this->Entity->find('list', array(''))
		$userprops = $this->Auth->user();
		if ($userprops['User']['group_id'] == 1)
		{
			//put the group condition here
			$names = $this->User->find('list', array(   'fields'=> 'username',    'order'=>'User.username ASC',  'conditions'=> array('NOT' => array('User.username' => '')))); 
			$this->set('usernames', $names); 
		}
		else 
		{
			$this->set('usernames',array('1'=>$userprops['User']['username']));
		}
		
		$frequencies = $this->Alert->find('list', array(   'fields'=> 'frequency',    'group'=>'frequency'));
		$this->set('frequencies',array('HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));
		//$this->set('limit_adjustment',.2);
		$this->set('fact_data_key',0);
		$this->set('alert_count',0);
		$this->set('type','LIMIT');
		//debug("end add",true);
		//$this->set('user',$userprops['User']['id']);
		
	}

	function edit($id = null) {
		if (!$id && empty($this->data)) {
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}
		if (!empty($this->data)) {
			if ($this->Alert->save($this->data)) {
				$this->Session->setFlash(__('The alert has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert could not be saved. Please, try again.', true));
			}
		}
		if (empty($this->data)) {
			$this->data = $this->Alert->read(null, $id);
		}
		
		$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$this->set('task_names', $task_names); 
		
		$this->set('frequencies',array('HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));
		
		$userprops = $this->Auth->user();
		if ($userprops['User']['group_id'] == 1)
		{
			//put the group condition here
			$names = $this->User->find('list', array(   'fields'=> 'username',    'order'=>'User.username ASC',  'conditions'=> array('NOT' => array('User.username' => '')))); 
			$this->set('usernames', $names); 
		}
		else 
		{
			$this->set('usernames',array('1'=>$userprops['User']['username']));
		}
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for alert', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Alert->delete($id)) {
			$this->Session->setFlash(__('Alert deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Alert was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
}
?>
