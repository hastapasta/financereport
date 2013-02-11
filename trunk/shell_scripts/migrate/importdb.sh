#gunzip $1.gz 
mysql --host=localhost --verbose --user=root --password findata < $1
#mysql --host=localhost --verbose --user=root --password=root test < $1
