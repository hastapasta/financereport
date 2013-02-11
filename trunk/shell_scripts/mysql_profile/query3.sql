FLUSH STATUS;
select SQL_NO_CACHE count(id) from fact_data where date_format(fact_data.date_collected,'%Y-%m-%d')>'2011-01-01' AND ((fact_data.entity_id=@eid));
