FLUSH STATUS;
select SQL_NO_CACHE count(id) from fact_data where fact_data.entity_id=@eid;
