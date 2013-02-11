mysqldump --host=localhost --user=root --password=root nutanix | gzip > nutanix.sql.gz 
#mysqldump --host=localhost --user=root --password=root --ignore_table=findata.fact_data_full --ignore_table=findata.fact_data_save2 findata | gzip > findata.sql.gz
