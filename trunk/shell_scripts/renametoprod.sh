#sudo su
sed -e 's,testdataload,proddataload,' /etc/sysconfig/network > /tmp/network && sudo mv /tmp/network /etc/sysconfig/network 
sed -e 's,testdataload,proddataload,' /etc/hosts > /tmp/hosts && sudo mv /tmp/hosts /etc/hosts
sed -e 's,testdataload,proddataload,' /etc/httpd/conf/httpd.conf > /tmp/httpd.conf && sudo mv /tmp/httpd.conf /etc/httpd/conf/httpd.conf
chcon -v --type=httpd_config_t /etc/httpd/conf/httpd.conf
sudo hostname proddataload
sudo rm /var/lib/logmein-hamachi/*
sudo cp /home/ollie/scripts/logmeinprod/* /var/lib/logmein-hamachi/
