#mysqldump --host=localhost --user=root --password=root nutanix | gzip > nutanix.sql.gz 
mysqldump --host=testdataload --user=root --password=root --verbose --ignore_table=findata.fact_data_full --ignore_table=findata.fact_data_save2 findata | gzip > findata.sql.gz
