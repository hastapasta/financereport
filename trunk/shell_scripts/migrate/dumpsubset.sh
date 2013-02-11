#special flags:
# --where=<where clause>
# --ignore_table=<table name>
# -d   (structure only)
#mysqldump --host=testdataload --user=root --password=madmax1. findata fact_data fact_data_stage_est log_alerts log_tasks repeat_types alerts splits users batches log_notifications log_tweets page_counters retired_alerts time_events excludes | gzip > findata_$(date +%s).sql.gz 
#mysqldump -d --host=localhost --user=root --password=madmax1. findata extract_tables2 | gzip > findata_$(date +%s).sql.gz
#mysqldump --host=localhost --user=root --password=madmax1. findata alerts | gzip > findata_$(date +%s).sql.gz 
#mysqldump --host=localhost --user=root --password=madmax1. --skip-extended-insert findata acos aros aros_acos
mysqldump --host=testdataload --user=root --password findata excludes | gzip > findata_$(date +%s).sql.gz
