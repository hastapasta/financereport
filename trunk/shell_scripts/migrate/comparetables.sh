#dump two tables to files and compare
tablename1=users
dbname1=findata
hostname1=testdataload
tablename2=users
dbname2=findata
hostname2=10.0.0.14

#mysqldump --host=$hostname1 --user=root --password=madmax1. $dbname1 $tablename1> /tmp/table1.sql

rm /tmp/table1.sql

echo "select * from $tablename1;" > /tmp/table1.sql

mysql --host=$hostname1 --user=root --password=madmax1. $dbname1 < /tmp/table1.sql > /tmp/table1.txt

rm /tmp/table2.sql

echo "select * from $tablename2;" > /tmp/table2.sql

mysql --host=$hostname2 --user=root --password=madmax1. $dbname2 < /tmp/table1.sql > /tmp/table2.txt

diff -Na /tmp/table1.txt /tmp/table2.txt > /tmp/tablediff

#dump table1.txt to make sure there wasn't an error to make sure we're
#not comparing matching error messages
cat /tmp/table1.txt

