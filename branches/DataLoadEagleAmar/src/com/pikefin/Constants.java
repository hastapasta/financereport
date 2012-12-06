package com.pikefin;

public interface Constants {
 public static final Integer BATCH_SIZE=20;
	interface ExcludeType{
		public static final Integer EXCLUDE_TYPE_ONE=1;
		public static final Integer EXCLUDE_TYPE_TWO=0;
	}
	interface AlertTargetType{
		public static final Integer ALERT_TARGET_TYPE_ONE=1;
		public static final Integer ALERT_TARGET_TYPE_TWO=2;
	}
	interface NotificationData{
		public static final String NOTIFICATION_DATE_TIME_FIRED="DateTimeFired";
		public static final String NOTIFICATION_TICKER="Ticker";
		public static final String NOTIFICATION_TIME_EVENT_NAME="TimeEventName";
		public static final String NOTIFICATION_DIFF="Diff";
		public static final String NOTIFICATION_BEFORE_VALUE="BeforeValue";
		public static final String NOTIFICATION_AFTER_VALUE="AfterValue";
		public static final String NOTIFICATION_BEFORE_DATE_COLLECTED="BeforeDateCollected";
		public static final String NOTIFICATION_AFTER_DATE_COLLECTED="AfterDateCollected";
		public static final String NOTIFICATION_CAKE_EDIT_ALERT_URL="CakeEditAlertURL";
		public static final String NOTIFICATION_CAKE_EDIT_ALERT_SUB_URL="alerts/edit?alert";
	}
	
	interface EnhancedTableSection{
		public static final String ENHANCED_TABLE_BODY_SECTION="body";
		public static final String ENHANCED_TABLE_COL_HEAD_SECTION="colhead";
		public static final String ENHANCED_TABLE_ROW_HEAD_SECTION="rowhead";

	}
	
	interface Ticker{
		public static final String TICKER_BF_B="BF-B";
		public static final String TICKER_BRK_A="BRK-A";
		public static final String TICKER_BF_BY_B="BF/B";
		public static final String TICKER_BRK_BY_A="BRK/A";
		public static final String TICKER_NYSE_COLLON_BF_B="NYSE:BF.B";
		public static final String TICKER_NYSE_COLLON_BRK_A="NYSE:BRK.A";

		public static final String ENHANCED_TABLE_COL_HEAD_SECTION="colhead";
		public static final String ENHANCED_TABLE_ROW_HEAD_SECTION="rowhead";

	}
	
	interface FactDataColumn{
		public static final String FACT_DATA_COLUMN_VALUE="value";
		public static final String FACT_DATA_COLUMN_SCALE="scale";
		public static final String FACT_DATA_COLUMN_MANUAL_CORRECTION="manual_correction";
		public static final String FACT_DATA_COLUMN_DATE_COLLECTED="date_collected";
		public static final String FACT_DATA_COLUMN_ID="id";
		public static final String FACT_DATA_COLUMN_ENTITY_ID="entity_id";
		public static final String FACT_DATA_COLUMN_METRIC_ID="metric_id";
		public static final String FACT_DATA_COLUMN_DATA_GROUP="data_group";
		public static final String FACT_DATA_COLUMN_FISCAL_QUARTER="fiscalquarter";
		public static final String FACT_DATA_COLUMN_FISCAL_YEAR="fiscalyear";
		public static final String FACT_DATA_COLUMN_CAL_QUARTER="calquarter";
		public static final String FACT_DATA_COLUMN_CAL_YEAR="calyear";
		public static final String FACT_DATA_COLUMN_CAL_MONTH="calmonth";
		public static final String FACT_DATA_COLUMN_DAY="day";
		public static final String FACT_DATA_COLUMN_BATCH_ID="batch_id";
		public static final String FACT_DATA_COLUMN_RAW="raw";
		public static final String FACT_DATA_COLUMN_GARBAGE_COLLECT="garbage_collect";
		public static final String FACT_DATA_COLUMN_META_SET_ID="meta_set_id";


	}
	
	interface HttpSetting{
		public static final String PARAM_USER_AGENT="http.protocol.user-agent";
		public static final String PARAM_COKKIE_DATE_PATTERN="http.protocol.cookie-datepatterns";
		public static final String COKKIE_DATE_PATTERN_1="EEE, dd MMM-yyyy-HH:mm:ss z";
		public static final String COKKIE_DATE_PATTERN_2="EEE, dd MMM yyyy HH:mm:ss z";

		public static final String BROWSER_MOZILA="Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)";
		
		
	}
	
	interface Urls{
		public static final String SURVEY_MOST_URL="http://data.bls.gov/cgi-bin/surveymost";
	}
}
