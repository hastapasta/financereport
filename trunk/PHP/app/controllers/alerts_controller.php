<?php
class AlertsController extends AppController {

	var $name = 'Alerts';
	var $uses = array('Alert','Schedule','Task', 'User','EntityGroup','Entity','TimeEvent');
	var $paginate = array('limit'=>10);

	function beforeFilter() {
		$this->log('in alerts_controller beforeFilter', LOG_DEBUG);
		parent::beforeFilter();
	
		//$this->log(debug_backtrace(), LOG_DEBUG);
	  
		//$this->log($this,LOG_DEBUG);
		//$this->set('urlrequest',$this->referer());
		//$this->Auth->allowedActions = array('index', 'view');

		/*
		 * Technically we should be pulling a list of all tasks that are referenced in the schedules table, but
		 * for now we'll just pull a list straight from the tasks table.
		 */

		//App::import('Model', 'Schedule');
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


	}


	function index() {
		
		$this->Alert->recursive = 2;
		$userprops = $this->Auth->user();
		$conditions = null;
		/*
		 * 2 issues:
		 * 
		 * 1) If I move the session property login in here, I need a way to conclusively determine if the user
		 * clicked on the fitler button vs arriving at this page through another mechanism.
		 * 
		 * 2) If I keep the logic in app_controller.php, then I need to pass the form data with the redirect.
		 * 
		 */
		/*
		 * If not in the administrators group, filter the data based off of user.
		 */
		
		if($userprops['User']['group_id'] != 1)
		{ 
			$tmpuserid = $this->User->find('first', array('fields'=> 'id','conditions'=>array('User.username'=>$userprops['User']['username'])));
			$conditions = array('Alert.user_id'  => $tmpuserid['User']['id']);
		}
		
		if ($this->Session->read('FilterValues') != null)
		{
			
				$filtervalues = $this->Session->read('FilterValues');
				//debug($filtervalues,true);
				if (!empty($filtervalues))
				{
					if ($filtervalues['Alert']['filtersenabled']=='1')
					{
						if ($filtervalues['Alert']['schedule_id']!="All")
							$conditions['Schedule.id'] = $filtervalues['Alert']['schedule_id'];
							
						if ($filtervalues['Alert']['frequency']!="All")
							$conditions['Alert.frequency'] = $filtervalues['Alert']['frequency'];
			
						if (!empty($filtervalues['Entity']['ticker']))
							$conditions['Entity.ticker LIKE'] = $filtervalues['Entity']['ticker'];
			
					}
				}
			/*if ($th['Alert']['schedule_id']!="All")
			{
				//debug('Filter schedules',true);
				$conditions['Schedule.id'] = $this->data['Alert']['schedule_id'];
			}
			
			if (!empty($filtervalues['Entity']['ticker']))
				$conditions['Entity.ticker LIKE'] = $filtervalues['Entity']['ticker'];*/
		}
		

		$this->paginate = array('conditions' => $conditions);

		$task_names2 = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		)));

		//Add an all option to the front
		$tmp = array("All"=>"All");
		foreach(array_keys($task_names2) as $key)
		{
			
			$tmp[$key] = $task_names2[$key];
		}
		
		$task_names2 = $tmp;
		
		$this->set('frequencies',array('All'=>'All','HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));

		$this->set('task_names2', $task_names2);
		//exit();
		$this->set('alerts', $this->paginate());

	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->Alert->recursive = 2;
		$this->set('alert', $this->Alert->read(null, $id));
	}
	
	function fullview($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->Alert->recursive = 2;
		$this->set('alert', $this->Alert->read(null, $id));
	}



	function add() {
		if (!empty($this->data)) {
			//restructure the data
			$multiselect = $this->data['Alert']['entity_id'];
			//$i=0;
			$tmp2 = array();
				
			foreach ($multiselect as $item){
				$tmp = $this->data['Alert'];
				$tmp['entity_id'] = $item;
				array_push($tmp2,$tmp);
				//$i++;
			}
			$this->data['Alert'] = $tmp2;
			$this->Alert->create();
				
			

			if ($this->Alert->saveAll($this->data['Alert'])) {
				$this->Session->setFlash(__('The alert has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert could not be saved. Please, try again.', true));
			}
				
		}
		
		$task_names2 = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		)));
		$this->set('task_names2', $task_names2);
	
		$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$this->set('task_names', $task_names);
		
		$time_event_names = $this->TimeEvent->find('list', array('fields'=> 'name'));
		$this->set('timeeventnames', $time_event_names);

		$entity_descs = $this->Entity->find('list', array('fields'=> 'full_name',    'order'=>'Entity.full_name ASC'));
		$this->set('entity_descs', $entity_descs);

		/*$entities = $this->Entity->find('list', array(''));
		$this->set(compact('entities'));*/
		$userprops = $this->Auth->user();
		if ($userprops['User']['group_id'] == 1){
			//put the group condition here
			$names = $this->User->find('list', array(   'fields'=> 'username',    'order'=>'User.username ASC',  'conditions'=> array('NOT' => array('User.username' => ''))));
			$this->set('usernames', $names);
		}else{
			$this->set('usernames',array('1'=>$userprops['User']['username']));
		}

		//$frequencies = $this->Alert->find('list', array(   'fields'=> 'obsolete_frequency',    'group'=>'obsolete_frequency'));
		//$this->set('frequencies',array('HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));
		//$this->set('limit_adjustment',.2);
		$this->set('fact_data_key',0);
		$this->set('alert_count',0);
		$this->set('type','LIMIT');
		
//		$this->set('user',$userprops['User']['id']);
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		
		/*
		 * Get parameter overrides Post parameter
		 */
		if (array_key_exists('alert',$this->params['url']) && !empty($this->params['url']['alert']))
		{
			$record = "";
			$this->Session->delete('Record');
			$record= array(0 => $this->params['url']['alert']);
		}
	
		
		
		
		if (!empty($this->data)) {
			
			/*
			 * Here we need to reformat the data and covert the string ticker to an entity_id.
			 */
		
			for($x=0;$x<sizeof($this->data['Alert']);$x++)
			{
				$tmpticker = $this->data['Entity'][$x]['ticker'];
				$tmpentityid = $this->Entity->find('first', array('fields'=> 'id','conditions'=>array('Entity.ticker'=>$tmpticker)));
				$this->data['Alert'][$x]['entity_id'] = $tmpentityid['Entity']['id'];
			}
			
			if ($this->Alert->saveAll($this->data['Alert'])) {
				$this->Session->setFlash(__('The alert has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				//$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			/*
			 * This find() call gathers the alert data that is passed to the edit.ctp view.
			 */
			
			$this->data = $this->toMulti($this->Alert->find('all',array('conditions'=>array('Alert.id'=>$record),'recursive'=>2)));
		}
		
		if (empty($this->data)) {
			$this->log('in edit @ invalid user',LOG_DEBUG);
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		//$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$task_names = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		)));
		$this->set('task_names', $task_names);
		
		$tickers = $this->Entity->find('list', array('fields'=> array('id','ticker')));
		$this->set('tickers1', $tickers);
		
	

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
			$this->Session->setFlash(__('Alert deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Alert was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
	
	function getTicker(){
		$this->autoRender = false;
		$q='';
		$q = $this->params['named']['q'];
		$sql="SELECT entities.ticker,entities.id FROM entities,entity_groups,entities_entity_groups,entity_groups_tasks,schedules";
		$sql.=" where entities.id=entities_entity_groups.entity_id and entity_groups.id=entities_entity_groups.entity_group_id and entity_groups.id=entity_groups_tasks.entity_group_id";
		$sql.=" and entity_groups_tasks.task_id=schedules.task_id and schedules.id=".$q;
		$data = $this->Alert->query($sql);
		$ticker = array();
		for($i = 0; $i <sizeof($data); $i++){
			$ticker[] = $data[$i]['entities'];	
		}
		print(json_encode($ticker));
	}
}
?>
