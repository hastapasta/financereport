rm -f /home/ollie/scripts/backup/findata*.gz
set hostname=localhost
#dump fact_data without locking
mysqldump --host=$hostname --user=root --password=madmax1. --lock-tables=false findata fact_data | gzip > /home/ollie/scripts/backup/findata_factdata_$(date +%s).sql.gz
#dump rest of the tables with locking
mysqldump --host=$hostname --user=root --password=madmax1. --ignore_table=findata.fact_data findata | gzip > /home/ollie/scripts/backup/findata_$(date +%s).sql.gz
