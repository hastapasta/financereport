#sudo su
sed -e 's,testdataload,devdataload,' /etc/sysconfig/network > /tmp/network && sudo mv /tmp/network /etc/sysconfig/network 
sed -e 's,testdataload,devdataload,' /etc/hosts > /tmp/hosts && sudo mv /tmp/hosts /etc/hosts
sed -e 's,testdataload,devdataload,' /etc/httpd/conf/httpd.conf > /tmp/httpd.conf && sudo mv /tmp/httpd.conf /etc/httpd/conf/httpd.conf
chcon -v --type=httpd_config_t /etc/httpd/conf/httpd.conf
sudo hostname devdataload
sudo rm /var/lib/logmein-hamachi/*
sudo cp /home/ollie/scripts/logmeindev/* /var/lib/logmein-hamachi/
