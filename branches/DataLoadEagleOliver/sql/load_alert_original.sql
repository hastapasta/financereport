select count(alerts.id)
			from alerts 
			LEFT JOIN entities ON alerts.entity_id=entities.id 
			LEFT JOIN users ON alerts.user_id=users.id 
			LEFT JOIN fact_data ON (alerts.initial_fact_data_id=fact_data.id and (((alerts.calyear is null) and (fact_data.calyear is null)) or (alerts.calyear=fact_data.calyear))) 
			LEFT JOIN time_events ON alerts.time_event_id=time_events.id
			LEFT JOIN tasks ON alerts.task_id=tasks.id
			LEFT JOIN countries_entities ON countries_entities.entity_id=entities.id
			LEFT JOIN countries ON countries.id=countries_entities.country_id
			where alerts.task_id=12
			and (countries_entities.default_country=1 OR countries_entities.default_country is null)
			order by time_events.id