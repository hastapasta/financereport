#Script to get the version #s of components

sudo cat /etc/redhat-release
echo "----------------------------"
mysql -V -h localhost -u root -pmadmax1.
echo "----------------------------"
php -v
echo "----------------------------"
/usr/sbin/apachectl -v
echo "----------------------------"
/usr/share/apache-tomcat-7.0.11/bin/version.sh
