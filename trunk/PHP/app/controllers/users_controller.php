<?php
class UsersController extends AppController {

	var $name = 'Users';

	function beforeFilter() {
		/*
		 * Production setup.
		 */
		parent::beforeFilter();

		$this->Auth->allow(array('logout','login'));

		/*
		 * End Production setup.
		 */

		//$this->Auth->allow('*');
	}


	function index() {
		$this->User->recursive = 0;
		$this->set('users', $this->paginate());
	}

	function view($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		$this->set('user', $this->User->read(null, $id));
	}

	function add() {
		if (!empty($this->data)) {
			$this->User->create();
			if ($this->User->save($this->data)) {
				$this->Session->setFlash(__('The user has been saved', true),'default',array('class'=>'green_message'));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user could not be saved. Please, try again.', true));
			}
		}
		$groups = $this->User->Group->find('list');
		$this->set(compact('groups'));
	}

	function edit($id = null) {
		/* Distinguish between administrators and regular users */
	
		$userprops = $this->Auth->user();
		
		
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			for($i = 0; $i < sizeof($this->data['User']); $i++){
				$this->data['User'][$i]['password'] = $this->Auth->password($this->data['User'][$i]['password']);
			}
			if ($this->User->saveAll($this->data['User'])) {
				$this->Session->setFlash(__('The user has been saved', true),'default',array('class'=>'green_message'));
				$this->Session->delete('Record');
				//$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			//$this->data = $this->toMulti($this->User->find('all',array('conditions'=>array('User.id'=>$record))));
			//$this->data = $this->User->read(null, $id);
			if($userprops['User']['group_id'] == 1)
			{
				$this->data = $this->toMulti($this->User->find('all',array('conditions'=>array('User.id'=>$record))));
				$groups = $this->User->Group->find('list');
				$this->set(compact('groups'));
				$this->set('administrator', true);
			}
			else 
			{
				$this->data = $this->toMulti($this->User->find('all',array('conditions'=>array('User.id'=>$userprops['User']['id']))));
				$this->set('administrator', false);
			}	
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		//$groups = $this->User->Group->find('list');
		//$this->set(compact('groups'));
		
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for user', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->User->delete($id)) {
			$this->Session->setFlash(__('User deleted', true),'default',array('class'=>'green_message'));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('User was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}

	function login() {
		if ($this->Session->read('Auth.User')) {
			$this->Session->setFlash('You are logged in!','default',array('class'=>'green_message'));
				
			//$this->redirect('/alerts', null, false);
		}
	}

	function logout() {
		$this->Session->setFlash('Good-Bye','default',array('class'=>'green_message'));
		$this->redirect($this->Auth->logout());
	}
	
	function change_password(){
		if(!empty($this->data)){
			$cansave = true;
			$user = $this->User->findByUsername($this->Auth->user('username'));
			if(!$user){
				$this->User->validationErrors['username'] = 'Username not found';
				$cansave = false;
			}
			if($user && $user['User']['password'] != $this->Auth->password($this->data['User']['old_password']) ){
				$this->User->validationErrors['old_password'] = 'Password is incorrect';
				$cansave = false;
			}
			if($this->data['User']['new_password'] != $this->data['User']['confirm_password']){
				$this->User->validationErrors['confirm_password'] = "Password didn't match";
				$cansave = false;
			}
			if($cansave){
				$user['User']['password' ] = $this->Auth->password($this->data['User']['new_password']);
				if($this->User->save($user)){
					$this->Session->setFlash(__('Password is changed successfully' , true),'default',array('class'=>'green_message'));					
				}else{
					$this->Session->setFlash(__("password could not be changed",true));
				}
			}
			
		}
	}
	
	function chart(){
		
	}
/*
	function initDB() {
		$group =& $this->User->Group;
		//Allow admins to everything
		$group->id = 1;
		$this->Acl->allow($group, 'controllers');

		//allow managers to posts and widgets
		$group->id = 2;
		$this->Acl->deny($group, 'controllers');
		$this->Acl->allow($group, 'controllers/Alerts');
		$this->Acl->allow($group, 'controllers/Schedules');

		//allow users to only add and edit on posts and widgets
		$group->id = 3;
		$this->Acl->deny($group, 'controllers');
		$this->Acl->allow($group, 'controllers/Alerts');
		$this->Acl->allow($group, 'controllers/users/change_password');

		//we add an exit to avoid an ugly "missing views" error message
		echo "all done";
		exit;
	}*/


}
?>
