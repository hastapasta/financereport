package com.pikefin;

public enum RepeatTypeEnum {
	RUNONCE,HOURLY,WEEKLY,MONTHLY,DAILY,NONE,MINUTE,RUNEVERY;
	
	
	@Override
	public String toString(){
		return this.name();
	}
}
