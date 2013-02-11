rm /home/ollie/scripts/backup/findata*.gz
#dump fact_data without locking
mysqldump --host=pikefin3 --user=root --password=madmax1. --lock-tables=false findata fact_data | gzip > /home/ollie/scripts/backup/findata_factdata_$(date +%s).sql.gz
#dump rest of the tables with locking
mysqldump --host=pikefin3 --user=root --password=madmax1. --ignore_table=fact_data findata | gzip > /home/ollie/scripts/backup/findata_$(date +%s).sql.gz
