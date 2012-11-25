select a.id from alerts a 
LEFT JOIN tasks t ON a.task_id=t.id
LEFT JOIN entities e ON a.entity_id=e.id 
LEFT JOIN users u ON a.user_id=u.id 
LEFT JOIN fact_data f ON (a.initial_fact_data_id=f.id and (((a.calyear is null) and (f.calyear is null)) or (a.calyear=f.calyear))) 
LEFT JOIN time_events te ON a.time_event_id=te.id
LEFT JOIN countries_entities ce ON ce.entity_id=e.id
LEFT JOIN countries c ON c.id=ce.country_id

where a.task_id=12 
order by a.id