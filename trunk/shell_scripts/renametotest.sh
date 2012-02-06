#sudo su
sed -e 's,devdataload,testdataload,' /etc/sysconfig/network > /tmp/network && sudo mv /tmp/network /etc/sysconfig/network 
sed -e 's,devdataload,testdataload,' /etc/hosts > /tmp/hosts && sudo mv /tmp/hosts /etc/hosts
sed -e 's,devdataload,testdataload,' /etc/httpd/conf/httpd.conf > /tmp/httpd.conf && sudo mv /tmp/httpd.conf /etc/httpd/conf/httpd.conf
chcon -v --type=httpd_config_t /etc/httpd/conf/httpd.conf
sudo hostname testdataload
sudo rm /var/lib/logmein-hamachi/*
sudo cp /home/ollie/scripts/logmeintest/* /var/lib/logmein-hamachi/
