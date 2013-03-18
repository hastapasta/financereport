Hello <br/><br/>
Click on the link to confirm your email address.<br/><br/>
<?php $domain = CONFIG_DOMAIN.'/users/confirm/'.$data['id']; ?>
<a href="<?php echo $domain; ?>"><?php echo $domain; ?></a><br/><br/><br/>
<b>Username:</b>&nbsp;<?php echo $data['username']; ?>
<br/><br/>