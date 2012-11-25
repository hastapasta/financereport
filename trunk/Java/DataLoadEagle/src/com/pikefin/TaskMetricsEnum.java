package com.pikefin;

public enum TaskMetricsEnum {
	JOB_START(0,"JobStart"),
	JOB_END(1,"JobEnd"),
	ALERT_START(2,"AlertStart"),
	ALERT_END(3,"AlertEnd"),
	STAGE1_START(4,"Stage1Start"),
	STAGE1_END(5,"Stage1End"),
	STAGE2_START(6,"Stage2Start"),
	STAGE2_END(7,"Stage2End");
	
	
	private int code;
	private String metric;
	
	private TaskMetricsEnum(int c, String m) {
		code = c;
		metric = m;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMetric() {
		return metric;
	}
	
}
