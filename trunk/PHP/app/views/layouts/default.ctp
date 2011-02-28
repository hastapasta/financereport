<?php
/**
 *
 * PHP versions 4 and 5
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright 2005-2010, Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2005-2010, Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @package       cake
 * @subpackage    cake.cake.libs.view.templates.layouts
 * @since         CakePHP(tm) v 0.10.0.1076
 * @license       MIT License (http://www.opensource.org/licenses/mit-license.php)
 */
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php echo $this->Html->charset(); ?>
<title><?php __('CakePHP: the rapid development php framework:'); ?> <?php echo $title_for_layout; ?>
</title>
<?php
echo $this->Html->meta('icon');

echo $this->Javascript->link('jquery 1.4.4.js');

echo $this->Html->css('cake.generic');

if(isset($jsIncludes))
{     
	foreach($jsIncludes as $js)
	{         
		//debug($js,true);
		echo $javascript->codeBlock($js,array("inline" => false));     
	}
} 

echo $scripts_for_layout;
?>
<script type="text/javascript">
		$(function(){
			$('.deleteButton').click(function(){
				$('#actionValue').val('1');
				var answer = confirm('Are you sure you want to delete');
				if(answer) $('.recordForm').submit();
			});
			$('.editButton').click(function(){
				//alert('here 2');
				$('#actionValue').val('2');
				$('.recordForm').submit();
				//var answer = confirm('Are you sure you want to edit');
				//if(answer) $('.recordForm').submit();
			});
			$('.filterButton').click(function(){
				alert('here 3');
				//$('#actionValue').val('3');
				$tmp = document.getElementById('actionValue');
				if ($tmp==0)
					$('#actionValue').val('0');
				else
					$('#actionValue').val('1');
				//alert($tmp.value);
				$('.filterForm').submit();
				//var answer = confirm('Are you sure you want to edit');
				//if(answer) $('.recordForm').submit();
			});
		});
	</script>
</head>
<body>
<div id="container">
<div id="header">
<h1><?php echo $this->Html->link(__('CakePHP: the rapid development php framework', true), 'http://cakephp.org'); ?>
<p align="right"><?php
//Display the logout link
if ($this->Session->check('Auth.User')):
echo $this->Html->link(
                        'Log Out',
array(
                                'controller' => 'users',
                                'action' => 'logout'
                                )
                                );
                                else:
                                echo $this->Html->link(
                        'Log In',
                                array(
                                'controller' => 'users',
                                'action' => 'login'
                                )
                                );
                                endif;
                                //end display logout link
                                ?></p>
</h1>
</div>
<div id="content"><?php
//display login/logout message
echo $this->Session->flash('auth'); ?> <?php echo $this->Session->flash(); ?>

<?php echo $content_for_layout; ?></div>


<div id="footer"><?php echo $this->Html->link(
$this->Html->image('cake.power.gif', array('alt'=> __('CakePHP: the rapid development php framework', true), 'border' => '0')),
					'http://www.cakephp.org/',
array('target' => '_blank', 'escape' => false)
);
?></div>
</div>
<?php echo $this->element('sql_dump'); ?>
</body>
</html>
