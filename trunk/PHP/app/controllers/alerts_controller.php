<?php
class AlertsController extends AppController {

	var $name = 'Alerts';
	var $uses = array('Alert','Task', 'User','EntityGroup','Entity','TimeEvent','Metric','InitialFactDatum');
	var $paginate = array('limit'=>10);

	var $types = array(1 => "Type 1: Percent Change/Walking Time Frame",2 => "Type 2: Fixed Threshold/No Time Frame",3 => "Type 3: Percent Change/Sliding Time Frame");


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

		$filtervalues = $this->data;
		$conditions = $this->Session->read('alertfilterconditions');



		if ($filtervalues != null)
		{
				
			//Filter values were refreshed; need to reset conditions.
				
			$conditions = "";
				
			//debug($filtervalues,true);
			if (!empty($filtervalues))
			{
					
				if ($filtervalues['Alert']['filtersenabled']=='1')
				{
					//if ($filtervalues['Alert']['task_id']!="All")
					//$conditions['Task.id'] = $filtervalues['Alert']['task_id'];
						
					if (!empty($filtervalues['Alert']['metric_id'])) {
						//have to convert from metric_id to task_id
							
						$taskid = $this->Task->find('list',array('fields'=>array('id'),'conditions'=>array('metric_id' => $filtervalues['Alert']['metric_id'])));

						//debug($taskid,'true');
						$string = "(";
							
						$i=0;
						foreach($taskid as $t) {
							$string.=$t.",";
							$tmp[$i] = $t;
							$i++;
						}
							

						$string = substr($string,0,strlen($string)-1);
						$string.=")";
							

						/* Don't need the 'IN' keyword for an 'in' type clause.*/
						$conditions['Task.id'] = $tmp;
							
						//$this->log($conditions['Task.id'],LOG_DEBUG);

					}
						

					if (!empty($filtervalues['Entity']['ticker']))
					$conditions['Entity.ticker LIKE'] = $filtervalues['Entity']['ticker'];
						
					if (!empty($filtervalues['Entity']['full_name']))
					$conditions['Entity.full_name LIKE'] = $filtervalues['Entity']['full_name'];
						

						
					//debug ($conditions['Entity.ticker LIKE']);

					if(!empty($filtervalues['Alert']['user_id'])){
						$conditions['Alert.user_id'] = $filtervalues['Alert']['user_id'];
					}

					if(!empty($filtervalues['Alert']['time_event_id'])){
						$conditions['Alert.time_event_id'] = $filtervalues['Alert']['time_event_id'];
					}

					if($filtervalues['Alert']['filtersdisabled'] == '1'){
						$conditions['Alert.disabled'] = 1;
					}

					if($filtervalues['Alert']['filtersfired'] == 1)
					$conditions['Alert.fired'] = 1;
						
					if($filtervalues['Alert']['filtersinitial'] == 1)
					$conditions['InitialFactDatum.value'] = 0;
						

					$this->Session->write('alertfilterconditions',$conditions);
					$this->Session->write('alertfiltervalues',$filtervalues);
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

		$this->paginate = array_merge( $this->paginate,  array('conditions' => $conditions));

		/*$task_names2 = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		 array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		 )));*/

		$task_names2 = $this->Task->find('list', array('fields'=>array('id', 'name')));

		//Add an all option to the front
		$tmp = array("All"=>"All");
		foreach(array_keys($task_names2) as $key)
		{
				
			$tmp[$key] = $task_names2[$key];
		}

		$task_names2 = $tmp;

		$metricnames = $this->Metric->find('list',array('fields'=>array('id','name')));

		$this->set(compact('metricnames'));

		//$this->set('frequencies',array('All'=>'All','HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));


		$this->set('task_names2', $task_names2);

		//set observation period (time event) dropdown

		$time_event_names = $this->TimeEvent->find('list', array('fields'=>array('id', 'name')));
		$this->set(compact('time_event_names'));



		// set users dropdown

		$users = $this->User->find('list', array('fields'=>array('id', 'username')));
		$this->set(compact('users'));
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

		//debug($this->Alert->data['Schedule']['Task']['metric_id'],true);

		$metric_id = $this->Alert->data['Task']['metric_id'];
		$metric_name = $this->Metric->find('list', array(   'fields'=> 'name',   'conditions'=> array('id' => $metric_id)));

		//debug($metric_name[$metric_id],true);
		$this->set('metric_name',$metric_name[$metric_id]);

		//debug($metric_name,true);
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

		//debug($this->data,true);
			
		if (!empty($this->data)) {
			$this->Alert->set($this->data);
			if (!$this->Alert->validates())
			{
				//debug('here',true);
				$this->Session->setFlash(__('The alert cound not be saved. Please see below for additional information.', true));
			}
			else
			{
					


				//restructure the data
				$multiselect = $this->data['Alert']['entity_id'];
				$tmp2 = array();
					
				foreach ($multiselect as $item){
					$tmp = $this->data['Alert'];
						
					/*
					 *  Need to do a look up of the task_id based off of the entity_id and the metric_id
					 */
						
					$taskid = $this->getTaskId($item,$this->data['Alert']['metric_id']);
						
					$tmp['task_id'] = $taskid;
					$tmp['entity_id'] = $item;
					//debug($tmp,true);
					array_push($tmp2,$tmp);
				}
				//debug($tmp2);
				$this->data['Alert'] = $tmp2;
				$this->Alert->create();


					


				if ($this->Alert->saveAll($this->data['Alert'])) {
					//$this->Session->setFlash(__('The alert(s) have been saved', true),'default',array('class'=>'green_message'));
					//$this->redirect(array('action' => 'index'));
					$num_of_alert = sizeof($this->data['Alert']);
					$this->Session->setFlash(__('The alert(s) have been saved', true),'default',array('class'=>'green_message'));
					$this->redirect(array('action' => 'listview',$num_of_alert));
				} else {
					$this->Session->setFlash(__('The alert cound not be saved. Please see below for additional information.', true));
				}
			}

		}

		/*$task_names2 = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		 array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id','Task.allow_alerts = 1'))
		 )));*/
		//debug($task_names2,true);
		$task_names2 = $this->Task->find('list', array('fields'=> 'name'));
		$this->set('task_names2', $task_names2);

		//$types = array(1 => "Type 1: Percent Change/Walking Time Frame",2 => "Type 2: Fixed Threshold/No Time Frame",3 => "Type 3: Percent Change/Sliding Time Frame");
		$this->set('types',$this->types);

		$entity_groups = $this->EntityGroup->find('list', array('fields'=> 'description'));
		$this->set('entity_groups', $entity_groups);

		$metric_names = $this->Metric->find('list', array('fields'=> 'name'));
		$this->set('metric_names', $metric_names);

		$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		$this->set('task_names', $task_names);

		$time_event_names = $this->TimeEvent->find('list', array('fields'=> 'name'));
		$this->set('timeeventnames', $time_event_names);

		$entity_descs = $this->Entity->find('list', array('fields'=> 'full_name',    'order'=>'Entity.full_name ASC'));
		$this->set('entity_descs', $entity_descs);

		/*$entities = $this->Entity->find('list', array(''));
		 $this->set(compact('entities'));*/
		$userprops = $this->Auth->user();
		$this->set('currentuser',$userprops['User']['id']);
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
		//$this->set('type','LIMIT');

		//		$this->set('user',$userprops['User']['id']);
	}

	// Call After Add A Alert For Display The Detail Of Recently Added Alert
	function listview($totalAlert = null) {
		if (!$totalAlert) {	//If Not Set Total Added Record
			$this->Session->setFlash(__('Invalid alert', true));
			$this->redirect(array('action' => 'index'));
		}

		$this->Alert->recursive = 1;

		//Fetch Recently Added Record Into Alerts Table
		$alertIds = $this->Alert->find('all',array('order'=>'Alert.id desc','limit'=>'0 ,'.$totalAlert));
		$this->set('alerts',$alertIds);
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
				if (sizeof($this->data['Alert'][$x]['entity_id']) != 1 || empty($this->data['Alert'][$x]['entity_id'])) {
					$this->Session->setFlash(__('Please select one and only one entity id.', true));
					//I want to return back to the edit screen.
					$this->redirect(array('action' => 'edit'),$this->data['Alert']['id']);
				}

				//$tmpticker = $this->data['Alert'][$x]['ticker'];
				//$tmpentityid = $this->Entity->find('first', array('fields'=> 'id','conditions'=>array('Entity.ticker'=>$tmpticker)));
				//$this->data['Alert'][$x]['entity_id'] = $tmpentityid['Entity']['id'];
				$this->data['Alert'][$x]['entity_id'] = $this->data['Alert'][$x]['entity_id'][0];

			}
				
				
			if ($this->Alert->saveAll($this->data['Alert'])) {
				$this->Session->setFlash(__('The alert(s) have been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				//$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The alert cound not be saved. Please see below for additional information.', true));
				//debug($this->validationErrors,true);
				//debug($this->Alert->invalidFields(),true);
				
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
			//temporarily commented out
			//$this->redirect(array('action' => 'index'));
		}
		//$task_names = $this->Task->find('list', array('fields'=> 'description',    'order'=>'Task.description ASC',  'conditions'=> array('1' => '1'),'group'=>'description'));
		/*$task_names = $this->Task->find('list', array('fields'=> array('SchedulesAlias.id','description'),'joins'=>array(
		array('table'=>'schedules','alias'=>'SchedulesAlias','type'=>'inner','foreignKey'=>false,'conditions'=>array('SchedulesAlias.task_id = Task.id'))
		)));*/

		$this->set('types',$this->types);

		$task_names = $this->Task->find('list', array('fields'=> array('id','name')));
		$this->set('task_names', $task_names);

		$tickers = $this->Entity->find('list', array('fields'=> array('id','ticker')));
		$this->set('tickers1', $tickers);

		$time_event_names = $this->TimeEvent->find('list', array('fields'=> 'name'));
		$this->set('timeeventnames', $time_event_names);





		$userprops = $this->Auth->user();
		if ($userprops['User']['group_id'] == 1){
			$this->set('show_twitter_alert',true);
		}
		else
		$this->set('show_twitter_alert',false);



		//$this->set('frequencies',array('HOURLY'=>'HOURLY','DAILY'=>'DAILY','WEEKLY'=>'WEEKLY','MONTHLY'=>'MONTHLY','YEARLY'=>'YEARLY','ALLTIME'=>'ALLTIME'));

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
		$sql="SELECT entities.ticker,entities.id FROM entities,entities_entity_groups ";
		$sql.=" where entities.id=entities_entity_groups.entity_id  ";
		$sql.=" and entities_entity_groups.entity_group_id=".$q;
		$data = $this->Alert->query($sql);
		$ticker = array();
		for($i = 0; $i <sizeof($data); $i++){
			$ticker[] = $data[$i]['entities'];
		}
		print(json_encode($ticker));
	}

	function getTaskId($entityid,$metricid) {

		$sql = "select tasks.id ";
		$sql.= " from entities_entity_groups,entity_groups_tasks,tasks ";
		$sql.= " where entities_entity_groups.entity_id=".$entityid;
		$sql.= " and tasks.metric_id=".$metricid;
		$sql.= " and entities_entity_groups.entity_group_id=entity_groups_tasks.entity_group_id ";
		$sql.= " and entity_groups_tasks.task_id=tasks.id ";



		$data = $this->Alert->query($sql);



		//debug($data,true);

		/*
		 * Right now we are just going to return the first result. If there is more than one task/entityid/metricid combo
		 * then we will have to come up with some way to differentiate the source.
		 */


		return($data[0]['tasks']['id']);


	}
}

function enquire() {
	if ( $this->data ) {
		$this->Alerts->set( $this->data );
		if ( $this->Enquiry->validates() ) {
			// .....
		} else {
			$this->set("errors", $this->Enquiry->invalidFields());
		}
	}
}

?>