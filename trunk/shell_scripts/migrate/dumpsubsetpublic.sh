#Use this file for dumping out a database that is meant to be shared 
#with other developers.

#special flags:
# --where=<where clause>
# --ignore-table=<table name>
# -d   (structure only)
#mysqldump --host=testdataload --user=root --password=madmax1. findata fact_data fact_data_stage_est log_alerts log_tasks repeat_types alerts splits users batches log_notifications log_tweets page_counters retired_alerts time_events excludes | gzip > findata_$(date +%s).sql.gz 
#mysqldump -d --host=localhost --user=root --password=madmax1. findata extract_tables2 | gzip > findata_$(date +%s).sql.gz
mysqldump --host=localhost --user=root --password=madmax1. --ignore-table=findata.fact_data_orig --ignore-table=findata.extract_tables2 --ignore-table=findata.extract_tables_export --ignore-table=findata.extract_tables_orig --ignore-table=findata.extract_tables_orig2 --ignore-table=findata.jobs_tasks_export --ignore-table=findata.jobs_tasks_orig --ignore-table=findata.jobs_export --ignore-table=findata.jobs_orig --ignore-table=findata.tasks_export --ignore-table=findata.fact_data_main findata | gzip > findata_$(date +%s).sql.gz 
#mysqldump --host=localhost --user=root --password=madmax1. --skip-extended-insert findata acos aros aros_acos
#mysqldump --host=testdataload --user=root --password=madmax1. --where="(entity_id=580 OR entity_id=781) AND metric_id=1" findata fact_data | gzip > findata_$(date +%s).sql.gz
