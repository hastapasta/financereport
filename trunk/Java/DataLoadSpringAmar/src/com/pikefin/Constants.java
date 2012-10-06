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
}
