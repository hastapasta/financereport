<?php
class UsersController extends AppController {

	var $name = 'Users';
	var $components =array('Mymail','Email');
	//var $uses = array('User');

	function beforeFilter() {
		/*
		 * Production setup.
		 */
		parent::beforeFilter();

		$this->Auth->allow(array('logout','login','registration','confirm','received'));
		$this->Auth->userScope = array(
	        'User.active' => array(
	            'expected' => 1,
	            'message' => __("Your account is not active yet. Click the Link in our Mail.", true)
	        )
	    );

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
				$this->Session->setFlash(__('The user cound not be saved. Please see below for additional information.', true));
			}
		}
		$groups = $this->User->Group->find('list');
		$this->set(compact('groups'));
	}
	
	function emailtest()
	{
		//$this->Email->from    = 'Somebody <hastapasta99@gmail.com>';
		//$this->Email->to      = 'Somebody Else <opike@yahoo.com>';
		$this->Email->from    = 'pikefin1@gmail.com';
		$this->Email->to      = 'opike@yahoo.com';
		//$this->Email->delivery = 's';
		$this->Email->delivery = 'smtp';
		$this->Email->subject = 'Test';
		
		
		$this->Email->smtpOptions = array(
        'port'=>'465', 
        'timeout'=>'30',
        //'host' => 'ssl://smtp.gmail.com',
        'host' => 'ssl://smtp.gmail.com',
        'username'=>'pikefin1',
        'password'=>'ginger1.',
  		 );
  		 
  		$this->Email->subject = 'Welcome to our really cool thing';
		$this->Email->send('Hello message body!');
		
		$this->set('smtp_errors', $this->Email->smtpError);
		
		
		
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
				$this->Session->setFlash(__('The user cound not be saved. Please see below for additional information.', true));
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
	
function registration() {
		if(!empty($this->data)){
		
			/*if (sizeof($this->User->find('all',array('conditions'=>array('User.email'=>$this->data['User']['email'])))))
			{
				$this->Session->setFlash('An account for this email address already exists.');
			}*/
			if (sizeof($this->User->find('all',array('conditions'=>array('User.username'=>$this->data['User']['username'])))))
			{
				$this->Session->setFlash('Username already exists.');
			}			
			else if(!($this->data['User']['password'] == $this->Auth->password($this->data['User']['confirm_password']))){
				$this->Session->setFlash('Passwords do not match.');
			}
			else 
			{
				$this->data['User']['group_id'] = 3;
				if($this->User->save($this->data)){
					$data = array();
					$data['id'] = $this->User->id;
					$data['username'] = $this->data['User']['username'];
					$data['password'] = $this->data['User']['confirm_password'];
					
					$options['to'] = $this->data['User']['email'];
					$options['from'] = CONFIG_ADMIN_MAIL;
					$options['contentTemplate'] = 'confirmation_mail';
					$options['subject'] = 'PikeFin Account Confirmation';
					$options['content'] = $data ;
					$this->Mymail->sendEmail($options);
					$this->Session->write('id', $this->User->id);
					
					//$this->Session->setFlash('Please check your email for a confirmation link','default',array('class'=>'green_message'));
					$this->redirect(array('controller'=>'users','action'=>'received'));
				}
			}
		}
	}
	
	function received() {
	
	$id = $this->Session->read('id');
	
	if($id){
			$user = array();
			$user['User']['id'] = $id;
			$user['User']['active'] = -1;
			if($this->User->save($user)){
				$msg = 'Your registration request has been submitted.
						<BR> Please check you email and and click on the confirmation link.';
			}else{
				$msg = 'Sorry! Please try to submit the form again or contact the administrator at pikefin1@gmail.com';
			}
			$this->set(compact('msg'));
		}else{
			debug('here',true);
			//$this->redirect(array('controller'=>'users','action'=>'login'));
		}
		
		
		
		
		
	}
	
	function confirm($id = null) {
		
		$user1 = $this->User->find('all',array('conditions'=>array('User.id'=>$id)));
		//debug($user1,true);
		if($id){
			$user = array();
			$user['User']['id'] = $id;
			$user['User']['active'] = -2;
			if($this->User->save($user)){
				$msg = 'Your email address has been successfully confirmed. 
						<BR> The final step is for an administrator to enable the account.
						<BR> You will receive an email informing you when the account has been enabled.';
				
				$data = array();
				$data['id'] = $user1['0']['User']['id'];
				$data['username'] = $user1['0']['User']['username'];
				$data['email'] = $user1['0']['User']['email'];
				
				$options['to'] = CONFIG_ADMIN_MAIL;
				$options['from'] = CONFIG_ADMIN_MAIL;
				$options['contentTemplate'] = 'administration_mail';
				$options['subject'] = 'PikeFin Administration: Account Confirmation';
				$options['content'] = $data ;
				$this->Mymail->sendEmail($options);
				
				
			}else{
				$msg = 'Sorry! Please try clicking on the link again or contact the administrator at pikefin1@gmail.com';
			}
			$this->set(compact('msg'));
		}else{
			$this->redirect(array('controller'=>'users','action'=>'login'));
		}
	}
	
	function finalize($id = null) {
		$user1 = $this->User->find('all',array('conditions'=>array('User.id'=>$id)));
		if($id){
			$user = array();
			$user['User']['id'] = $id;
			$user['User']['active'] = 1;
			if($this->User->save($user)){
				$msg = 'User: '.$user1['0']['User']['username'].
						'<BR>Email: '.$user1['0']['User']['email'].
						'<BR>Account has been finalized.';
				
		
				
				
			}else{
				$msg = 'Finalization link failed.';
			}
			$this->set(compact('msg'));
		}else{
			$msg = 'Error with finalizing account. No $id variable passed.';
			$this->set(compact('msg'));
		}
	}
	
	function chart(){
		
		$userprops = $this->Auth->user();
		$this->set('userid', $userprops['User']['id']);
		
		//debug($userprops,true);
		
	}
	
	function charta(){
		
	}
	function chartb(){
		
	}
	function chartc(){
		
	}
	function chartd(){
	}
	
	function recover_tree() {
		$this->Acl->recover('parent');
	}
	

	/*function initDB() {
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