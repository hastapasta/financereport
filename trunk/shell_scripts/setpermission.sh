#script to set the appropriate permissions for production

sudo chmod -R 0775 /var/www/html
sudo chmod -R 0777 /var/www/html/cakepftest/app/tmp/cache
sudo chmod -R 0770 /home/ollie
sudo chmod -R 0777 /tmp/phplogs/*
#following lines because of PHP/viewlogs/displaylog.php
sudo chmod 755 /home
sudo chmod 755 /home/ollie
sudo chmod 644 /home/ollie/.dmrc
sudo chmod 775 /home/ollie/workspace
sudo chmod 775 /home/ollie/workspace/DataLoad
sudo chmod 775 /home/ollie/workspace/DataLoad/*log*

