<?php
class EntitiesController extends AppController {

	var $name = 'Entities';
	var $uses = array('Entity','Alert','User','EntityGroup','Metric','Country','CountriesEntity');
	
	function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('*');
	}

	function index() {
		//	$this->urlToNamed();
		$this->Entity->recursive = 1;
		//$conditions = array();
		//debug($this->passedArgs);
		
		$filtervalues = $this->data;
		//debug($filtervalues);
		$conditions = $this->Session->read('entityfilterconditions');
		//debug($conditions);
		/*
		 * $this->Recipe->bindModel(array('hasOne' => array('RecipesTag',
    'FilterTag' => array(
        'className' => 'Tag',
        'foreignKey' => false,
        'conditions' => array('FilterTag.id = RecipesTag.tag_id')
))));
$this->Recipe->find('all', array(
    'fields' => array('Recipe.*'),
    'conditions' => array('FilterTag.name' => 'Dessert')
));

		 * 
		 * 
		 * 
		 */
		
		/*
		 * This returns the data, now need to get the contain working.
		 */
		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78)));
		//var_dump($blap);
		/*
		 * 
		 */
		
		/*'contain' => array('Amenity'
=> array('UsersAmenities')));*/
		
		//$this->Entity->Country->contain(array('fields'=>'Entity.ticker'));
		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78),'contain'=> array('Entity'=>array('ticker'))));
		//var_dump($blap);
		
		//$this->Entity->Country->contain(array('fields' => 'ticker'));
		//$this->Entity->contain('ticker');
		//$blap = $this->Entity->find('all', array('fields'=> 'Entity.ticker','conditions'=>array('Entity.id'=>78)));
		
		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78),'contain'=>array('fields'=>'Entity.ticker')));
		//var_dump($blap);
		//$countyent=$this->
		//debug($filtervalues);exit;
		if(!empty($filtervalues)){
			$conditions="";
			if(isset($filtervalues['Entity']['enable']) && $filtervalues['Entity']['enable'] == 1){
				if( isset($filtervalues['Entity']['ticker']) && !empty($filtervalues['Entity']['ticker']))
					$conditions['Entity.ticker LIKE'] = $filtervalues['Entity']['ticker'];
				if(isset($filtervalues['Entity']['full_name']) && !empty($filtervalues['Entity']['full_name']))
					$conditions['Entity.full_name LIKE'] = $filtervalues['Entity']['full_name'];
				if(isset($filtervalues['Country']['id']) && !empty($filtervalues['Country']['id'])) {
					$conditions['Entity.obsolete_country_id'] = $filtervalues['Country']['id'];
					//debug($conditions);exit;
					//$entityId=$this->CountriesEntity->find(
					//	'list',
					//	array(
					//		'conditions' => array(
					//			'Country.country_id' => $conditions1
					//		)
					//	)
					//);
					//var_dump($entityId);
					//debug($entityId);exit;
					//$conditions['Entity.id'] = $entityId;
					//var_dump('here');
				}
				if(isset($filtervalues['EntityGroup']['id']) && !empty($filtervalues['EntityGroup']['id'])) {
					$group_id = $filtervalues['EntityGroup']['id'];
					$this->loadModel('EntitiesEntityGroup');
					$entity_ids = $this->EntitiesEntityGroup->find(
						'list',
						array(
							'fields' => array('entity_id','entity_id'),
							'conditions' => array('EntitiesEntityGroup.entity_group_id' => $group_id)
						)
					);
					$conditions['Entity.id'] = $entity_ids;
				}
				//debug($entity_ids);
				
				
			}	
			
			$this->Session->write('entityfilterconditions',$conditions);
			$this->Session->write('entityfiltervalues',$filtervalues);
		}
		

		$this->paginate = array_merge( $this->paginate,  array('conditions' => $conditions));
		$countries = $this->Country->find('list',array('fields'=>array('id','name'),'order'=>'Country.name asc'));
		$entity_groups = $this->EntityGroup->find('list',array('fields'=>array('id','name'),'order'=>'EntityGroup.name asc'));
		$this->set(compact('countries', 'entity_groups'));
		$this->set('entities', $this->paginate(null,$conditions));
	}

	function view($id = null) {
		
		$userprops = $this->Auth->user();
		
		if($userprops['User']['group_id'] != 1)
		{ 
			$tmpuserid = $this->User->find('first', array('fields'=> 'id','conditions'=>array('User.username'=>$userprops['User']['username'])));
			$conditions['Alert.user_id'] = $tmpuserid['User']['id'];
		}
		
		if (!$id) {
			$this->Session->setFlash(__('Invalid entity', true));
			$this->redirect(array('action' => 'index'));
		}
		/*
		 * Making it recursive causes a retrieval of all of the alerts; don't need that data.
		 */
		//$this->Entity->recursive = 1;
		$this->set('entity', $this->Entity->read(null, $id));
		
	}

	function add() {
		if (!empty($this->data)) {
			$this->Entity->create();
			if ($this->Entity->save($this->data)) {
				$this->Session->setFlash(__('The entity has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The entity cound not be saved. Please see below for additional information.', true));
			}
		}
	}

	function edit($id = null) {
		$this->Entity->recursive = 0;
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			if ($this->Entity->saveAll($this->data['Entity'])) {
				$this->Session->setFlash(__('The Entities has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The Entities cound not be saved. Please see below for additional information.', true));
			}
		}
		if (!empty($record)) {
			$this->data = $this->toMulti($this->Entity->find('all',array('conditions'=>array('Entity.id'=>$record))));
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid Entities', true));
			$this->redirect(array('action' => 'index'));
		}	
		
		$metric_names = $this->Metric->find('list', array('fields'=> 'name'));
		$this->set('metric_names', $metric_names);
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for entity', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->Entity->delete($id)) {
			$this->Session->setFlash(__('Entity deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('Entity was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}
	
	function getCountry(){
		$this->autoRender = false;
		$this->layout = null;
		$term  =  $_REQUEST['term'];
		//$this->loadModel("Country");
		$conditions = array();
		$conditions['Country.name LIKE'] = $term."%";
		$countries = $this->Country->find('all', array('conditions'=>$conditions, 'recursive'=>0,'order'=>'name ASC', 'fields'=>array('Country.id','Country.name')));
		$this->set("data", $countries);
		//var_dump($countries);
		//var_dump($countries);
		$this->render('json');	
	}
	
	function split(){
		
		
		$ratio = $this->params['url']['ratio'];
		$entity_id = $this->params['url']['entity_id'];
		$datecutoff = $this->params['url']['datecutoff'];
		
		//$sql1 -> insert split operation into splits table for tracking purposes
		//$sql2 -> update metric_id=1 (price) values in fact_data table
		//$sql3 -> update metric_id=4,5,7 & 8 (eps actual & estimate, annual and quarterly) in fact_data
		//$sql4 -> update shares outstanding in entities table
		
		$array = split(":",$ratio);
		
		$sql1 = "insert into splits (date_issued,entity_id,ratio,cutoff_date) ";
		$sql1.= " values (NOW(),".$entity_id.",'".$ratio."','".$datecutoff."')";
		
		$sql2 = "update fact_data set value = round(value * ".$array[1]." / ".$array[0].",3) ";
		$sql2.= " where entity_id=".$entity_id." and metric_id=1 ";
		$sql2.= " AND date_collected < '".$datecutoff."' ";
		
		$sql3 = "update fact_data set value = round(value * ".$array[1]." / ".$array[0].",3) ";
		$sql3.= " where entity_id=".$entity_id." and metric_id in (4,5,7,8) ";
		$sql3.= " AND date_collected < '".$datecutoff."' ";
		
		$sql4 = "update entities set shares_outstanding = round(shares_outstanding * ".$array[0]." / ".$array[1].",0) ";
		$sql4.= " where id=".$entity_id;
		
		
		//debug($sql1,true);
		//debug($sql2,true);
		//debug($sql3,true);
		//debug($sql4,true);	
	
		$data1 = $this->Entity->query($sql1);
		$this->set('sql1', $data1);
		//debug($data1);
		
		$data2 = $this->Alert->query($sql2);
		$this->set('sql2', $data2);
		//debug($data2);
		
		
		$data3 = $this->Alert->query($sql3);
		$this->set('sql3', $data3);
		//debug($data3);
		
		
		
		$data4 = $this->Alert->query($sql4);
		$this->set('sql4', $data4);
		//debug($data4);

	}
}
?>
<?php
//class EntitiesController extends AppController {
//
//	var $name = 'Entities';
//	var $uses = array('Entity','Alert','User','EntityGroup','Metric','Country','CountriesEntity');
//	
//	function beforeFilter() {
//		parent::beforeFilter();
//		//$this->Auth->allow('*');
//	}
//
//	function index() {
//		//$this->urlToNamed();
//		$this->Entity->recursive = 1;
//		//$conditions = array();
//		//debug($this->passedArgs);
//		
//		$filtervalues = $this->data;
//		$conditions = $this->Session->read('entityfilterconditions');
//		
//		/*
//		 * $this->Recipe->bindModel(array('hasOne' => array('RecipesTag',
//    'FilterTag' => array(
//        'className' => 'Tag',
//        'foreignKey' => false,
//        'conditions' => array('FilterTag.id = RecipesTag.tag_id')
//))));
//$this->Recipe->find('all', array(
//    'fields' => array('Recipe.*'),
//    'conditions' => array('FilterTag.name' => 'Dessert')
//));
//
//		 * 
//		 * 
//		 * 
//		 */
//		
//		/*
//		 * This returns the data, now need to get the contain working.
//		 */
//		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78)));
//		//var_dump($blap);
//		/*
//		 * 
//		 */
//		
//		/*'contain' => array('Amenity'
//=> array('UsersAmenities')));*/
//		
//		//$this->Entity->Country->contain(array('fields'=>'Entity.ticker'));
//		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78),'contain'=> array('Entity'=>array('ticker'))));
//		//var_dump($blap);
//		
//		//$this->Entity->Country->contain(array('fields' => 'ticker'));
//		//$this->Entity->contain('ticker');
//		//$blap = $this->Entity->find('all', array('fields'=> 'Entity.ticker','conditions'=>array('Entity.id'=>78)));
//		
//		//$blap = $this->Entity->Country->find('first', array('conditions' => array('Country.id' => 78),'contain'=>array('fields'=>'Entity.ticker')));
//		//var_dump($blap);
//		//$countyent=$this->
//		//debug($filtervalues);exit;
//		if(!empty($filtervalues)){
//			$conditions="";
//			if(isset($filtervalues['Entity']['enable']) && $filtervalues['Entity']['enable'] == 1){
//				if( isset($filtervalues['Entity']['ticker']) && !empty($filtervalues['Entity']['ticker']))
//					$conditions['Entity.ticker LIKE'] = $filtervalues['Entity']['ticker'];
//				if(isset($filtervalues['Entity']['name']) && !empty($filtervalues['Entity']['name']))
//					$conditions['Entity.full_name LIKE'] = $filtervalues['Entity']['name'];
//				if(isset($filtervalues['Country']['name']) && !empty($filtervalues['Country']['name'])) {
//					$conditions1['CountriesEntity.CountryId'] = $filtervalues['Country']['name'];
//					
//					$entityId=$this->CountriesEntity->findAllByCountryId($conditions1,array('CountriesEntity.entity_id'));
//					var_dump($entityId);
//					$conditions['Entity.id']=$entityId;
//					//var_dump('here');G
//				}
//				//var_dump($conditions);
//				//var_dump($this->data);
//					
//				
//			}	
//			
//			$this->Session->write('entityfilterconditions',$conditions);
//			$this->Session->write('entityfiltervalues',$filtervalues);
//		}
//		
//
//		$this->paginate = array_merge( $this->paginate,  array('conditions' => $conditions));
//
//		$countries=$this->Country->find('list',array('fields'=>array('id','name'),'order'=>'Country.name asc'));
//		$this->set(compact('countries'));
//		$this->set('entities', $this->paginate(null,$conditions));
//	}
//
//	function view($id = null) {
//		
//		$userprops = $this->Auth->user();
//		
//		if($userprops['User']['group_id'] != 1)
//		{ 
//			$tmpuserid = $this->User->find('first', array('fields'=> 'id','conditions'=>array('User.username'=>$userprops['User']['username'])));
//			$conditions['Alert.user_id'] = $tmpuserid['User']['id'];
//		}
//		
//		if (!$id) {
//			$this->Session->setFlash(__('Invalid entity', true));
//			$this->redirect(array('action' => 'index'));
//		}
//		/*
//		 * Making it recursive causes a retrieval of all of the alerts; don't need that data.
//		 */
//		//$this->Entity->recursive = 1;
//		$this->set('entity', $this->Entity->read(null, $id));
//		
//	}
//
//	function add() {
//		if (!empty($this->data)) {
//			$this->Entity->create();
//			if ($this->Entity->save($this->data)) {
//				$this->Session->setFlash(__('The entity has been saved', true),'default',array('class'=>'green_message'));
//				$this->redirect(array('action' => 'index'));
//			} else {
//				$this->Session->setFlash(__('The entity cound not be saved. Please see below for additional information.', true));
//			}
//		}
//	}
//
//	function edit($id = null) {
//		$this->Entity->recursive = 0;
//		$record = $this->Session->read('Record');
//		if (!empty($this->data)) {
//			if ($this->Entity->saveAll($this->data['Entity'])) {
//				$this->Session->setFlash(__('The Entities has been saved', true),'default',array('class'=>'green_message'));
//				$this->Session->delete('Record');
//				$this->redirect(array('action' => 'index'));
//			} else {
//				$this->Session->setFlash(__('The Entities cound not be saved. Please see below for additional information.', true));
//			}
//		}
//		if (!empty($record)) {
//			$this->data = $this->toMulti($this->Entity->find('all',array('conditions'=>array('Entity.id'=>$record))));
//		}
//		if (empty($this->data)) {
//			$this->Session->setFlash(__('Invalid Entities', true));
//			$this->redirect(array('action' => 'index'));
//		}	
//		
//		$metric_names = $this->Metric->find('list', array('fields'=> 'name'));
//		$this->set('metric_names', $metric_names);
//	}
//
//	function delete($id = null) {
//		if (!$id) {
//			$this->Session->setFlash(__('Invalid id for entity', true));
//			$this->redirect(array('action'=>'index'));
//		}
//		if ($this->Entity->delete($id)) {
//			$this->Session->setFlash(__('Entity deleted', true),'default',array('class'=>'green_message'));
//			$this->redirect(array('action'=>'index'));
//		}
//		$this->Session->setFlash(__('Entity was not deleted', true));
//		$this->redirect(array('action' => 'index'));
//	}
//	
//	function getCountry(){
//		$this->autoRender = false;
//		$this->layout = null;
//		$term  =  $_REQUEST['term'];
//		//$this->loadModel("Country");
//		$conditions = array();
//		$conditions['Country.name LIKE'] = $term."%";
//		$countries = $this->Country->find('all', array('conditions'=>$conditions, 'recursive'=>0,'order'=>'name ASC', 'fields'=>array('Country.id','Country.name')));
//		$this->set("data", $countries);
//		//var_dump($countries);
//		//var_dump($countries);
//		$this->render('json');	
//	}
//	
//	function split(){
//		
//		
//		$ratio = $this->params['url']['ratio'];
//		$entity_id = $this->params['url']['entity_id'];
//		$datecutoff = $this->params['url']['datecutoff'];
//		
//		//$sql1 -> insert split operation into splits table for tracking purposes
//		//$sql2 -> update metric_id=1 (price) values in fact_data table
//		//$sql3 -> update metric_id=4,5,7 & 8 (eps actual & estimate, annual and quarterly) in fact_data
//		//$sql4 -> update shares outstanding in entities table
//		
//		$array = split(":",$ratio);
//		
//		$sql1 = "insert into splits (date_issued,entity_id,ratio,cutoff_date) ";
//		$sql1.= " values (NOW(),".$entity_id.",'".$ratio."','".$datecutoff."')";
//		
//		$sql2 = "update fact_data set value = round(value * ".$array[1]." / ".$array[0].",3) ";
//		$sql2.= " where entity_id=".$entity_id." and metric_id=1 ";
//		$sql2.= " AND date_collected < '".$datecutoff."' ";
//		
//		$sql3 = "update fact_data set value = round(value * ".$array[1]." / ".$array[0].",3) ";
//		$sql3.= " where entity_id=".$entity_id." and metric_id in (4,5,7,8) ";
//		$sql3.= " AND date_collected < '".$datecutoff."' ";
//		
//		$sql4 = "update entities set shares_outstanding = round(shares_outstanding * ".$array[0]." / ".$array[1].",0) ";
//		$sql4.= " where id=".$entity_id;
//		
//		
//		//debug($sql1,true);
//		//debug($sql2,true);
//		//debug($sql3,true);
//		//debug($sql4,true);	
//	
//		$data1 = $this->Entity->query($sql1);
//		$this->set('sql1', $data1);
//		//debug($data1);
//		
//		$data2 = $this->Alert->query($sql2);
//		$this->set('sql2', $data2);
//		//debug($data2);
//		
//		
//		$data3 = $this->Alert->query($sql3);
//		$this->set('sql3', $data3);
//		//debug($data3);
//		
//		
//		
//		$data4 = $this->Alert->query($sql4);
//		$this->set('sql4', $data4);
//		//debug($data4);
//
//	}
//}
?>
