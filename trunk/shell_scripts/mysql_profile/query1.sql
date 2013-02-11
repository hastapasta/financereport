FLUSH STATUS;
select SQL_NO_CACHE now(),date_format(fact_data.date_collected,'%m-%d-%Y') as date_col, date_format(fact_data.date_collected,'%H:%i:%s') as time_col, fact_data.batch_id,fact_data.value as fdvalue from fact_data where date_format(fact_data.date_collected,'%Y-%m-%d')>'2011-01-01' AND ((fact_data.entity_id=550 and fact_data.metric_id=1));
