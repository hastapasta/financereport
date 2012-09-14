<?php
class AppController extends Controller {
    var $components = array('Acl', 'Auth', 'Session','Mymail');
    var $helpers = array('Html', 'Form', 'Session','Javascript');

    function beforeFilter() {
        //Configure AuthComponent
        

       $this->Auth->authError = 'Please Login For Access';
        $this->Auth->authorize = 'actions';
		$this->Auth->actionPath = 'controllers/';
        $this->Auth->loginAction = array('controller' => 'users', 'action' => 'login');
        $this->Auth->logoutRedirect = array('controller' => 'users', 'action' => 'login');
        $this->Auth->loginRedirect = array('controller' => 'alerts', 'action' => 'index');
        //$this->Auth->loginRedirect = array('controller' => 'alerts', 'action' => 'add');
        //$this->Auth->loginRedirect = array('controller' => 'schedules', 'action' => 'index');
		$this->Auth->allowedActions = array('display');
		
		$this->_pageCounter();
		
		$this->_setPageSize();
		
		$userprops = $this->Auth->user();
		
		$this->set('user_props',$userprops);
		
	

	
    }
    
	function _setPageSize(){
    	$this->loadModel('User');
    	$this->User->id = $this->Auth->user('id');
    	$limit = $this->User->field('pagination_limit');
    	$this->Session->write('Page.pagesize', $limit);
    	
    	$this->paginate = array('limit'=>$limit);
    }
    
	function _pageCounter()
	{
		//debug("debug value",true);
		$uri = $_SERVER['REQUEST_URI'];
		
		if (strpos($uri,'?')!=false)
			$uri = substr($uri,0,strpos($uri,'?'));
		
		
		$query1 = "select hits from page_counters where uri='".$uri."'";
		
		$result1 = mysql_query($query1) or die("Failed Query of " . $query1);
		
		$row1 = mysql_fetch_array($result1);
		
		//debug($row1);
		
		if ($row1==null)
		{
			$query2 = "insert into page_counters (uri,hits) values ('".$uri."',1)";
		}
		else 
		{
			$query2 = "update page_counters set hits=".($row1['hits']+1)." where uri='".$uri."'";
		}
		
		mysql_query($query2) or die("Failed Query of " . $query2);
		
	}
	
	/*function urlToNamed() {
        $urlArray = $this->params['url'];
        unset($urlArray['url']);
        if(!empty($urlArray)){
            $this->redirect($urlArray, null, true);
        }
    }*/

	/*
	* Remove this from the production instance.
	*/

	/*function build_acl() {
		if (!Configure::read('debug')) {
			return $this->_stop();
		}
		$log = array();

		$aco =& $this->Acl->Aco;
		$root = $aco->node('controllers');
		if (!$root) {
			$aco->create(array('parent_id' => null, 'model' => null, 'alias' => 'controllers'));
			$root = $aco->save();
			$root['Aco']['id'] = $aco->id; 
			$log[] = 'Created Aco node for controllers';
		} else {
			$root = $root[0];
		}   

		App::import('Core', 'File');
		$Controllers = Configure::listObjects('controller');
		$appIndex = array_search('App', $Controllers);
		if ($appIndex !== false ) {
			unset($Controllers[$appIndex]);
		}
		$baseMethods = get_class_methods('Controller');
		$baseMethods[] = 'build_acl';

		$Plugins = $this->_getPluginControllerNames();
		$Controllers = array_merge($Controllers, $Plugins);

		// look at each controller in app/controllers
		foreach ($Controllers as $ctrlName) {
			$methods = $this->_getClassMethods($this->_getPluginControllerPath($ctrlName));

			// Do all Plugins First
			if ($this->_isPlugin($ctrlName)){
				$pluginNode = $aco->node('controllers/'.$this->_getPluginName($ctrlName));
				if (!$pluginNode) {
					$aco->create(array('parent_id' => $root['Aco']['id'], 'model' => null, 'alias' => $this->_getPluginName($ctrlName)));
					$pluginNode = $aco->save();
					$pluginNode['Aco']['id'] = $aco->id;
					$log[] = 'Created Aco node for ' . $this->_getPluginName($ctrlName) . ' Plugin';
				}
			}
			// find / make controller node
			$controllerNode = $aco->node('controllers/'.$ctrlName);
			if (!$controllerNode) {
				if ($this->_isPlugin($ctrlName)){
					$pluginNode = $aco->node('controllers/' . $this->_getPluginName($ctrlName));
					$aco->create(array('parent_id' => $pluginNode['0']['Aco']['id'], 'model' => null, 'alias' => $this->_getPluginControllerName($ctrlName)));
					$controllerNode = $aco->save();
					$controllerNode['Aco']['id'] = $aco->id;
					$log[] = 'Created Aco node for ' . $this->_getPluginControllerName($ctrlName) . ' ' . $this->_getPluginName($ctrlName) . ' Plugin Controller';
				} else {
					$aco->create(array('parent_id' => $root['Aco']['id'], 'model' => null, 'alias' => $ctrlName));
					$controllerNode = $aco->save();
					$controllerNode['Aco']['id'] = $aco->id;
					$log[] = 'Created Aco node for ' . $ctrlName;
				}
			} else {
				$controllerNode = $controllerNode[0];
			}

			//clean the methods. to remove those in Controller and private actions.
			foreach ($methods as $k => $method) {
				if (strpos($method, '_', 0) === 0) {
					unset($methods[$k]);
					continue;
				}
				if (in_array($method, $baseMethods)) {
					unset($methods[$k]);
					continue;
				}
				$methodNode = $aco->node('controllers/'.$ctrlName.'/'.$method);
				if (!$methodNode) {
					$aco->create(array('parent_id' => $controllerNode['Aco']['id'], 'model' => null, 'alias' => $method));
					$methodNode = $aco->save();
					$log[] = 'Created Aco node for '. $method;
				}
			}
		}
		if(count($log)>0) {
			debug($log);
		}
	}*/
	
	function _getClassMethods($ctrlName = null) {
		App::import('Controller', $ctrlName);
		if (strlen(strstr($ctrlName, '.')) > 0) {
			// plugin's controller
			$num = strpos($ctrlName, '.');
			$ctrlName = substr($ctrlName, $num+1);
		}
		$ctrlclass = $ctrlName . 'Controller';
		$methods = get_class_methods($ctrlclass);

		// Add scaffold defaults if scaffolds are being used
		$properties = get_class_vars($ctrlclass);
		if (array_key_exists('scaffold',$properties)) {
			if($properties['scaffold'] == 'admin') {
				$methods = array_merge($methods, array('admin_add', 'admin_edit', 'admin_index', 'admin_view', 'admin_delete'));
			} else {
				$methods = array_merge($methods, array('add', 'edit', 'index', 'view', 'delete'));
			}
		}
		return $methods;
	}

	function _isPlugin($ctrlName = null) {
		$arr = String::tokenize($ctrlName, '/');
		if (count($arr) > 1) {
			return true;
		} else {
			return false;
		}
	}

	function _getPluginControllerPath($ctrlName = null) {
		$arr = String::tokenize($ctrlName, '/');
		if (count($arr) == 2) {
			return $arr[0] . '.' . $arr[1];
		} else {
			return $arr[0];
		}
	}

	function _getPluginName($ctrlName = null) {
		$arr = String::tokenize($ctrlName, '/');
		if (count($arr) == 2) {
			return $arr[0];
		} else {
			return false;
		}
	}

	function _getPluginControllerName($ctrlName = null) {
		$arr = String::tokenize($ctrlName, '/');
		if (count($arr) == 2) {
			return $arr[1];
		} else {
			return false;
		}
	}

/**
 * Get the names of the plugin controllers ...
 * 
 * This function will get an array of the plugin controller names, and
 * also makes sure the controllers are available for us to get the 
 * method names by doing an App::import for each plugin controller.
 *
 * @return array of plugin names.
 *
 */
	function _getPluginControllerNames() {
		App::import('Core', 'File', 'Folder');
		$paths = Configure::getInstance();
		$folder =& new Folder();
		$folder->cd(APP . 'plugins');

		// Get the list of plugins
		$Plugins = $folder->read();
		$Plugins = $Plugins[0];
		$arr = array();

		// Loop through the plugins
		foreach($Plugins as $pluginName) {
			// Change directory to the plugin
			$didCD = $folder->cd(APP . 'plugins'. DS . $pluginName . DS . 'controllers');
			// Get a list of the files that have a file name that ends
			// with controller.php
			$files = $folder->findRecursive('.*_controller\.php');

			// Loop through the controllers we found in the plugins directory
			foreach($files as $fileName) {
				// Get the base file name
				$file = basename($fileName);

				// Get the controller name
				$file = Inflector::camelize(substr($file, 0, strlen($file)-strlen('_controller.php')));
				if (!preg_match('/^'. Inflector::humanize($pluginName). 'App/', $file)) {
					if (!App::import('Controller', $pluginName.'.'.$file)) {
						debug('Error importing '.$file.' for plugin '.$pluginName);
					} else {
						/// Now prepend the Plugin name ...
						// This is required to allow us to fetch the method names.
						$arr[] = Inflector::humanize($pluginName) . "/" . $file;
					}
				}
			}
		}
		return $arr;
	}

	/*
	* End 'Remove this from the production instance'
	*/
	
	function action_process(){
		//debug($this->data);
		$this->autoRender = false;
		/*
		 * code of 1 for delete
		 */
		if(1 == $this->data[$this->modelClass]['action_value']){
			unset($this->data[$this->modelClass]['action_value']);
			$this->delete_multiple($this->data);
		}
		/*
		 * code of 2 for edit
		 */
		if(2 == $this->data[$this->modelClass]['action_value']){
			
			unset($this->data[$this->modelClass]['action_value']);			
			$ids = array();
			//debug($record,true);
			 foreach($this->data[$this->modelClass] as $record) {
				if($record != 0)
					$ids[] = $record;
			}
			$this->Session->write('Record',$ids);
			$this->redirect(array('action'=>'edit'));
		}
		/*
		 *  code 3 for filter
		 */
		if(3 == $this->data[$this->modelClass]['action_value_search']){
	
			unset($this->data[$this->modelClass]['action_value_search']);
			debug('in action_process', true);
			
			$this->Session->write('FilterValues', $this->data);
			
			//$this->redirect(array('action'=>'index'));
		}
	}
	function delete_multiple($id = null){
        foreach($this->data[$this->modelClass] as $record) {
			if($record != 0)
				$this->{$this->modelClass}->delete($record);
		}
		$this->Session->setFlash('The records were successfully deleted','default',array('class'=>'green_message'));
		$this->redirect($this->referer());
    }
	function toMulti($data){
		$this->log('in toMulti 1',LOG_DEBUG);
        $result = array();
        	if (is_array($data)){
        		$this->log('in toMulti 2',LOG_DEBUG);
        		//debug($data,true);
        		$i =  0;
                foreach ($data as $record) {
                	
                    if ($ar = @each($record)){
                    	$result[$ar["key"]][$i] = $ar["value"];
                    	$i++;
                    }
                }
            }
        return $result;
   	} 

	/*function edit(){
		//Abstract method
	}*/

}
?>
