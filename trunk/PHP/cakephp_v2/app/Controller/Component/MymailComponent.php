<?php
class MymailComponent extends Component {

    var $controller = null;
  	var $components = array("Email");

  	//function initialize(&$controller, $config) {
  	function initialize(Controller $controller) {
        $this->controller = $controller;
    }

	/*
	 * $options['to] : A string value where to send mail
	 * $options['from] : A string value from where to send mail
	 * $options['subject] : A string value defining the title of
	 * $options['content] : A array used to set the data of the template
	 * $options['contentTemplate]: the template to be used for sending mail
	 */
    function sendEmail($options = array()){
		$this->Email->to = $options['to'];
		$this->Email->from = $options['from'];
		$this->Email->subject = $options['subject'];

		$this->Email->smtpOptions = array(
        'port'=>'465',
        'timeout'=>'30',
        //'host' => 'ssl://smtp.gmail.com',
        'host' => 'ssl://smtp.gmail.com',
        //'username'=>'pikefin1@gmail.com',
        //'password'=>'ginger1.',
        'username'=>'pikefin1',
		'password'=>'ginger1.',
  		 );
  		 $this->Email->delivery = 'smtp';

		//$this->Email->delivery = 'debug';

		if(isset($options['contentTemplate']))
		   $this->Email->template = $options['contentTemplate'];
		else
		  $this->Email->template = "default";

		$this->Email->sendAs = "both";
		$this->controller->set("data", $options['content']);
		if($this->Email->send()){
			$this->log('email true',LOG_DEBUG);
		   return true;
		}else{
			$this->log('email false',LOG_DEBUG);
		  return false;
		}
	}
}
?>