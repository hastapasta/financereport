<?php
class UsersController extends AppController {

	var $name = 'Users';

	function beforeFilter() {
		/*
		 * Production setup.
		 */
		parent::beforeFilter();

		$this->Auth->allow('logout');

		/*
		 * End Production setup.
		 */

		//$this->Auth->allow(array('*'));
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
				$this->Session->setFlash(__('The user has been saved', true));
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user could not be saved. Please, try again.', true));
			}
		}
		$groups = $this->User->Group->find('list');
		$this->set(compact('groups'));
	}

	function edit($id = null) {
		$record = $this->Session->read('Record');
		if (!empty($this->data)) {
			for($i = 0; $i < sizeof($this->data['User']); $i++){
				$this->data['User'][$i]['password'] = $this->Auth->password($this->data['User'][$i]['password']);
			}
			if ($this->User->saveAll($this->data['User'])) {
				$this->Session->setFlash(__('The user has been saved', true));
				$this->Session->delete('Record');
				$this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user could not be saved. Please, try again.', true));
			}
		}
		if (!empty($record)) {
			//$this->data = $this->User->read(null, $id);
			$this->data = $this->toMulti($this->User->find('all',array('conditions'=>array('User.id'=>$record))));
				
		}
		if (empty($this->data)) {
			$this->Session->setFlash(__('Invalid user', true));
			$this->redirect(array('action' => 'index'));
		}
		$groups = $this->User->Group->find('list');
		$this->set(compact('groups'));
	}

	function delete($id = null) {
		if (!$id) {
			$this->Session->setFlash(__('Invalid id for user', true));
			$this->redirect(array('action'=>'index'));
		}
		if ($this->User->delete($id)) {
			$this->Session->setFlash(__('User deleted', true));
			$this->redirect(array('action'=>'index'));
		}
		$this->Session->setFlash(__('User was not deleted', true));
		$this->redirect(array('action' => 'index'));
	}

	function login() {
		if ($this->Session->read('Auth.User')) {
			$this->Session->setFlash('You are logged in!');
				
			//$this->redirect('/alerts', null, false);
		}
	}

	function logout() {
		$this->Session->setFlash('Good-Bye');
		$this->redirect($this->Auth->logout());
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
		//$this->Acl->allow($group, 'controllers/Alerts/edit');

		//we add an exit to avoid an ugly "missing views" error message
		echo "all done";
		exit;
	}*/


}
?>
