package com.pikefin.services;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.pikefin.PikefinUtil;
import com.pikefin.businessobjects.RepeatType;
import com.pikefin.businessobjects.Schedule;
import com.pikefin.businessobjects.Task;

public class QueuedJob implements Comparable<QueuedJob>{
	  
		 
		 private Schedule schedule;
		 private Task task;
		 private RepeatType repeatType;
		 private int priority;
		 private Calendar queuedInTime;
		 private boolean verifyMode;
		 
		 
		 
		public QueuedJob(Schedule schedule, Task task, RepeatType repeatType,
				int priority, Calendar queuedInTime, boolean verifyMode) {
			super();
			this.schedule = schedule;
			this.task = task;
			this.repeatType = repeatType;
			this.priority = priority;
			this.queuedInTime = queuedInTime;
			this.verifyMode = verifyMode;
		}



		public Schedule getSchedule() {
			return schedule;
		}



		public Task getTask() {
			return task;
		}



		public RepeatType getRepeatType() {
			return repeatType;
		}



		public int getPriority() {
			return priority;
		}



		public Calendar getQueuedInTime() {
			return queuedInTime;
		}



		public boolean isVerifyMode() {
			return verifyMode;
		}


		/**
		 * Sort in descending order. Higher priority tasks get executed first.
	     * @author Amar_Deep_Singh
	  	 */
		@Override
		public int compareTo(QueuedJob o) {
			if(this.priority > o.priority)
				return -1;
			else if(o.priority > this.priority)
				return 1;
			else {
				Calendar calFirst= Calendar.getInstance();
				Calendar calSecond= Calendar.getInstance();
				try {
					Date d1 = PikefinUtil.formatter.parse(this.queuedInTime.getTime().toString());
					Date d2 = PikefinUtil.formatter.parse(o.queuedInTime.getTime().toString());
					calFirst.setTime(d1);
					calSecond.setTime(d2);
					if (calFirst.before(calSecond))
						return -1;
					else if (calSecond.before(calFirst))
						return 1;
					else
						return 0;
				}
				catch (ParseException pe) {
					return 0;
				}
				
			    
			}
		
		  
			
		}
		  
		 
		  
		  

}
