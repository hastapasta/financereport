Hello <br/><br/>
A user with the email <?php echo $data['email']; ?>  and username <?php echo $data['username']; ?> has submitted a registration request.
<br><br>
Click on the following link to finalize the confirmation:
<br/>
<?php $domain = CONFIG_DOMAIN.'/users/finalize/'.$data['id']; ?>
<a href="<?php echo $domain; ?>"><?php echo $domain; ?></a><br/><br/><br/>
<br/><br/>