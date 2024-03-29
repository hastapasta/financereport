package pikefin.hibernate;

// Generated Aug 1, 2012 11:34:38 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;

/**
 * LogNotification generated by hbm2java
 */
public class LogNotification implements java.io.Serializable {

	private Integer logNotificationId;
	private Integer count;
	private Calendar processBegin;
	private Calendar processEnd;

	public LogNotification() {
	}

	public LogNotification(Integer count, Calendar processBegin,
			Calendar processEnd) {
		this.count = count;
		this.processBegin = processBegin;
		this.processEnd = processEnd;
	}

	public Integer getLogNotificationId() {
		return this.logNotificationId;
	}

	public void setLogNotificationId(Integer logNotificationId) {
		this.logNotificationId = logNotificationId;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Calendar getProcessBegin() {
		return this.processBegin;
	}

	public void setProcessBegin(Calendar processBegin) {
		this.processBegin = processBegin;
	}

	public Calendar getProcessEnd() {
		return this.processEnd;
	}

	public void setProcessEnd(Calendar processEnd) {
		this.processEnd = processEnd;
	}

}
