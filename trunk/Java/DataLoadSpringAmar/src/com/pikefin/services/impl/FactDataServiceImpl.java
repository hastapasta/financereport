package com.pikefin.services.impl;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pikefin.log4jWrapper.Logs;
import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.PikefinUtil;
import com.pikefin.businessobjects.Batches;
import com.pikefin.businessobjects.Entity;
import com.pikefin.businessobjects.FactData;
import com.pikefin.businessobjects.MetaSets;
import com.pikefin.businessobjects.Metric;
import com.pikefin.businessobjects.Task;
import com.pikefin.dao.inter.FactDataDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.EntityService;
import com.pikefin.services.inter.FactDataService;
import com.pikefin.services.inter.MetaSetService;
@Service
public class FactDataServiceImpl implements FactDataService{

	@Autowired
	private FactDataDao factDataDao;
	@Autowired
	private MetaSetService metaSetService;
	@Autowired
	private EntityService entityService;

	private Integer FACT_DATA_VALUE_INDEX=-1; 
	private Integer FACT_DATA_SCALE_INDEX=-1;
	private Integer FACT_DATA_MANUAL_CORRECTION_INDEX=-1;
	private Integer FACT_DATA_DATE_COLLECTED_INDEX=-1;
	private Integer FACT_DATA_ID_INDEX=-1;
	private Integer FACT_DATA_ENTITY_ID_INDEX=-1;
	private Integer FACT_DATA_METRIC_ID_INDEX=-1;
	private Integer FACT_DATA_GROUP_INDEX=-1;
	private Integer FACT_DATA_FISCAL_YEAR_INDEX=-1;
	private Integer FACT_DATA_FISCAL_QUARTER_INDEX=-1;
	private Integer FACT_DATA_CAL_QUARTER_INDEX=-1;
	private Integer FACT_DATA_CAL_YEAR_INDEX=-1;
	private Integer FACT_DATA_CAL_MONTH_INDEX=-1;
	private Integer FACT_DATA_DAY_INDEX=-1;
	private Integer FACT_DATA_BATCH_ID_INDEX=-1;
	private Integer FACT_DATA_RAW_INDEX=-1;
	private Integer FACT_DATA_GARBAGE_COLLECT_INDEX=-1;
	private Integer FACT_DATA_META_SET_ID_INDEX=-1;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void importFactDataInBatch(ArrayList<String[]> tabledata,
			Batches currentBatch, Metric metric)
			throws GenericException {
		
			if (tabledata.size() < 2)
			{
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Not enough rows of data passed into importTableIntoDB\nThis may be a custom import and the flag needs to be set",Logs.ERROR,"DBF4.5");
				return;
			}
			
			String[] columnnames = tabledata.get(0);
			tabledata.remove(0);
			for(int i=0;i<columnnames.length;i++){
				if(Constants.FactDataColumn.FACT_DATA_COLUMN_BATCH_ID.equalsIgnoreCase(columnnames[i])){
					FACT_DATA_BATCH_ID_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_CAL_MONTH.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_CAL_MONTH_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_CAL_QUARTER.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_CAL_QUARTER_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_CAL_YEAR.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_CAL_YEAR_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_DATA_GROUP.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_GROUP_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_DATE_COLLECTED.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_DATE_COLLECTED_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_DAY.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_DAY_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_ENTITY_ID.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_ENTITY_ID_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_FISCAL_QUARTER.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_FISCAL_QUARTER_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_FISCAL_YEAR.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_FISCAL_YEAR_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_GARBAGE_COLLECT.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_GARBAGE_COLLECT_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_ID.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_ID_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_MANUAL_CORRECTION.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_MANUAL_CORRECTION_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_META_SET_ID.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_META_SET_ID_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_RAW.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_RAW_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_METRIC_ID.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_METRIC_ID_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_SCALE.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_SCALE_INDEX=i;
				}else if (Constants.FactDataColumn.FACT_DATA_COLUMN_VALUE.equalsIgnoreCase(columnnames[i])) {
					FACT_DATA_VALUE_INDEX=i;
				}else{
					continue;
				}
				
			}
			int rowNo=0;
			int count=0;
			for(String[] row:tabledata){
				rowNo++;
				FactData fact=new FactData();
						fact.setBatch(currentBatch);
						if(FACT_DATA_CAL_MONTH_INDEX!=-1 && FACT_DATA_CAL_MONTH_INDEX<=row.length-1)
						fact.setCalmonth (Integer.parseInt(row[FACT_DATA_CAL_MONTH_INDEX]));
						if(FACT_DATA_CAL_QUARTER_INDEX!=-1 && FACT_DATA_CAL_QUARTER_INDEX<=row.length-1)
						fact.setCalquarter(Integer.parseInt(row[FACT_DATA_CAL_QUARTER_INDEX]));
						if(FACT_DATA_CAL_YEAR_INDEX!=-1 &&  FACT_DATA_CAL_YEAR_INDEX<=row.length-1)
						fact.setCalyear(Integer.parseInt(row[FACT_DATA_CAL_YEAR_INDEX]));
						if(FACT_DATA_GROUP_INDEX!=-1 &&  FACT_DATA_GROUP_INDEX<=row.length-1)
						fact.setDataGroup(row[FACT_DATA_GROUP_INDEX]);
						if(FACT_DATA_DATE_COLLECTED_INDEX!=-1 &&  FACT_DATA_DATE_COLLECTED_INDEX<=row.length-1)
						{
							Date date=null;
							try{
							 date=PikefinUtil.formatter.parse(row[FACT_DATA_DATE_COLLECTED_INDEX]);
									 fact.setDateCollected(date!=null?date:new Date());
							}catch (Exception e) {
								fact.setDateCollected(new Date());
							}
						}
						if(FACT_DATA_DAY_INDEX!=-1 &&  FACT_DATA_DAY_INDEX<=row.length-1)
						fact.setDay(Integer.parseInt(row[FACT_DATA_DAY_INDEX]));
						if(FACT_DATA_ENTITY_ID_INDEX!=-1 &&  FACT_DATA_ENTITY_ID_INDEX<=row.length-1)
						{
							Integer entityId=Integer.parseInt(row[FACT_DATA_ENTITY_ID_INDEX]);
							if(entityId!=null && entityId!=0){
								Entity entity=entityService.loadEntityInfo(entityId);
								fact.setEntity(entity);
							}
						}
						if(FACT_DATA_FISCAL_QUARTER_INDEX!=-1 &&  FACT_DATA_FISCAL_QUARTER_INDEX<=row.length-1)
						fact.setFiscalquarter(Integer.parseInt(row[FACT_DATA_FISCAL_QUARTER_INDEX]));
						if(FACT_DATA_FISCAL_YEAR_INDEX!=-1 &&  FACT_DATA_FISCAL_YEAR_INDEX<=row.length-1)
						fact.setFiscalyear(Integer.parseInt(row[FACT_DATA_FISCAL_YEAR_INDEX]));
						if(FACT_DATA_GARBAGE_COLLECT_INDEX!=-1 &&  FACT_DATA_GARBAGE_COLLECT_INDEX<=row.length-1)
						fact.setGarbageCollect(Boolean.parseBoolean(row[FACT_DATA_GARBAGE_COLLECT_INDEX]));
						if(FACT_DATA_MANUAL_CORRECTION_INDEX!=-1 &&  FACT_DATA_MANUAL_CORRECTION_INDEX<=row.length-1)
						fact.setManualCorrection(Boolean.parseBoolean(row[FACT_DATA_MANUAL_CORRECTION_INDEX]));
						if(FACT_DATA_META_SET_ID_INDEX!=-1 &&  FACT_DATA_META_SET_ID_INDEX<=row.length-1)
						{
							Integer metaSetsId=Integer.parseInt(row[FACT_DATA_META_SET_ID_INDEX]);
							if(metaSetsId!=null && metaSetsId!=0){
							MetaSets metaSet=metaSetService.loadMetaSetsInfo(metaSetsId);
							fact.setMetaSet(metaSet);
							}
						}
						fact.setMetric(metric);
						if(FACT_DATA_RAW_INDEX!=-1 &&  FACT_DATA_RAW_INDEX<=row.length-1)
						fact.setRaw(Boolean.parseBoolean(row[FACT_DATA_RAW_INDEX]));
						if(FACT_DATA_SCALE_INDEX!=-1 &&  FACT_DATA_SCALE_INDEX<=row.length-1)
						fact.setScale(Integer.parseInt(row[FACT_DATA_SCALE_INDEX]));
						if(FACT_DATA_VALUE_INDEX!=-1 &&  FACT_DATA_VALUE_INDEX<=row.length-1)
						fact.setValue(Double.parseDouble(row[FACT_DATA_VALUE_INDEX]));
				
						try{
							factDataDao.saveFactDataInfo(fact);
							count++;
							}catch (GenericException e) {
								ApplicationSetting.getInstance().getStdoutwriter().writeln("Failed to import the fact data for Row number" + rowNo,Logs.ERROR,"DBF9");
								ApplicationSetting.getInstance().getStdoutwriter().writeln(e);
							}
						
			}
			
			
		
			ApplicationSetting.getInstance().getStdoutwriter().writeln(count + " records inserted in db.",Logs.STATUS2,"DBF10");
		}
		
	
}
