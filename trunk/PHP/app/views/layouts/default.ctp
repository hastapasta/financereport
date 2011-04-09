<?php
include ("/var/www/html/PHP/site/includes/sitecommon.php");
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
<title><?php echo $title_for_layout; ?>
</title>
<?php
echo $this->Html->meta('icon');

echo $this->Javascript->link(array('jquery 1.4.4.js','querywrapper'));
echo $this->Javascript->link('http://www.google.com/jsapi');

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
		//this was the only way I could figure out how to pass JSP_ROOT_PATH into the view.
		<?php echo " var jsp_root_path='".IncFunc::$JSP_ROOT_PATH."';\n"; ?>
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
	<div id="jq-siteContain">
	<?php IncFunc::header1("alerts"); ?>
	<!--	<div id="logo">
			<?php //echo $html->image('logo.jpg',array('url'=>array('controller'=>'alerts','action'=>'index'))); ?>
		</div> -->
	<!-- <div id="jq-primaryNavigation">
			<ul>
			<li class="jq-home jq-current"><a title="Pikefin Home" href="/phptest/site/main/index.php">Home</a></li>
				<li class="jq-alerts "> -->
					<?php
						/*if ($this->Session->check('Auth.User')):
							echo $this->Html->link('Log Out1',array('controller' => 'users','action' => 'logout'));
						else:
							echo $this->Html->link('Log In1',array('controller' => 'users','action' => 'login'));
						endif;*/
					?>
		<!--  		</li>
			</ul>
		</div>		-->
	</div>
</div>
<div id="content"><?php
//display login/logout message
echo $this->Session->flash('auth'); ?> <?php echo $this->Session->flash(); ?>

<?php echo $content_for_layout; 

$this->set('jsp_root_path',IncFunc::$JSP_ROOT_PATH);
$this->set('test','value');


?></div>


<div id="footer"></div>
</div>
<?php echo $this->element('sql_dump'); ?>
</body>
</html>
