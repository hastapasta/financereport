<div class="alerts form"><script type="text/javascript">
	function showUser(str)
	{
	
	
		if (str=="")
		{
		
			document.getElementById("txtHint").innerHTML="";
			return;
		} 
	
		
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5  
	
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	
		xmlhttp.onreadystatechange=function()
		{
			
			
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
				alert(xmlhttp.responseText);
				var tmpSelect = document.getElementById("AlertTicker");
				
				var options=tmpSelect.getElementsByTagName("option");
			
				var i;
				for (i=options.length-1; i>=0; i--)
				{
					
					tmpSelect.remove(i);
				}
			
				
				for (i=0; i<10;i++)
				{
					
					var y=document.createElement('option');
					y.text=i + "";
									
				 	tmpSelect.add(y,null);
				 	
				}
				
				//document.getElementById("txtCompany").innerHTML=xmlhttp.responseText;
				
			}
		}
	
	
		var currentTime = new Date();
		
		xmlhttp.open("POST","http://localhost/PHP/ajaxsample/cakeajax.php?q="+str+"&timestamp="+currentTime,true);
		xmlhttp.send();
	}
	</script> <?php echo $this->Form->create('Alert');?>

<fieldset><legend><?php echo __('Add Alert'); ?></legend> <?php

//debug($this->validationErrors,true);
//debug($this->request->data,true);
//debug($this->getVar('usernames'),true);
//debug($this->getVar('task_names'),true);
//debug($this->getVar('frequencies'),true);



//echo $this->Form->input('id');
//echo $this->Form->input('type');

//debug($this);
//array('onChange'=>'showFields(this)')

//echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','options' => $this->getVar('task_names')));
echo $this->Form->input('schedule_id',array('label'=>'Schedule Name','onChange'=>'showUser(this.value)','options' => $this->getVar('task_names')));

echo $this->Form->input('ticker',array('label'=>'Financial Enitity','options' => $this->getVar('entity_descs')));
//echo $this->Form->input('schedule_id');


//$group_id = $this->Session->read('Auth.User.group_id');

//debug($this->Form,true);
echo $this->Form->input('Alert.user_id',array('label'=>'User Name','options' => $this->getVar('usernames')));
echo $this->Form->input('Alert.frequency',array('label'=>'Frequency','options' => $this->getVar('frequencies')));
echo $this->Form->input('limit_value');
//echo $this->Form->input('limit_adjustment');
echo $this->Form->input('limit_adjustment',array('value'=>0,'type'=>'hidden'));
echo $this->Form->input('type',array('value'=>'LIMIT','type'=>'hidden'));
//echo $this->Form->input('fact_data_key');

//echo $this->Form->input('alert_count');
echo $this->Form->input('disabled');
//echo $this->Form->input('user_id');
?></fieldset>
<?php echo $this->Form->end(__('Submit'));?></div>
<div class="actions">
<h3><?php echo __('Actions'); ?></h3>
<ul>

	<li><?php echo $this->Html->link(__('List Alerts'), array('action' => 'index'));?></li>
</ul>
</div>
