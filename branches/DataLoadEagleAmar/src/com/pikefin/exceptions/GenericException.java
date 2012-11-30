package com.pikefin.exceptions;


import java.lang.reflect.Field;
import java.sql.BatchUpdateException;
import java.sql.SQLException;

import com.pikefin.ErrorCode;

public class GenericException extends Exception {
	private ErrorCode errorCode=ErrorCode.UNKNOWN_ERROR;
	private String errorMessage;
	private String errorDescription;
private int loopCounter=0;
	public GenericException() {
		super();
	}

	public GenericException(ErrorCode errorCode, String errorMessage,
			Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
if(cause!=null){
		this.errorDescription = getStackTrace(cause).toString();
}
	}

	public GenericException(String message) {
		super(message);
		this.errorMessage=message;
	}

	public GenericException(String message, Throwable cause) {
		super(message, cause);
		this.errorMessage=message;
		this.errorDescription = getStackTrace(cause).toString();

	}

	public GenericException(Throwable cause) {
		super(cause);
		this.errorDescription = getStackTrace(cause).toString();
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	private StringBuilder getStackTrace(Throwable cause) {
		StringBuilder error = new StringBuilder();
		StackTraceElement[] stackElements = cause.getStackTrace();
		try {

			Field fileld = Throwable.class.getField("next");
		} catch (Exception e) {

		}
		error.append(cause.getMessage());
		if (stackElements != null) {
			for (StackTraceElement stackElement : stackElements) {
				error.append(stackElement.toString()).append("\n");
				
			}
			Throwable ourCause = cause.getCause();
			if (ourCause != null) {
			//	error = printStackTraceAsCause(error, stackElements,
				//		ourCause.getStackTrace(), ourCause);
				error=printCause(error,ourCause);
			}
		}
		return error;
	}

	private StringBuilder printCause(StringBuilder error, Throwable ourCause) {
		BatchUpdateException batchException;
		StackTraceElement[] causedTrace=ourCause.getStackTrace();
		error.append("\n\tCaused By:"+ourCause.getMessage());
		if(ourCause instanceof BatchUpdateException){
		batchException=(BatchUpdateException)ourCause;
		SQLException sql=batchException.getNextException();
			error.append("SQL State::" ).append(sql.getSQLState()).append("\t Reason::").append(sql.getMessage());
		}
		if(causedTrace!=null){
		for (StackTraceElement stackElement : causedTrace) {
			error.append(stackElement.toString()).append("\n");
			
		}
		}
		Throwable cause=ourCause.getCause();
		if (cause != null) {
			error = printCause(error, cause);

		}
		return error;
	}
	private StringBuilder printStackTraceAsCause(StringBuilder error,
			StackTraceElement[] causedTrace, StackTraceElement[] traces,
			Throwable ourCause) {
		// assert Thread.holdsLock(s);

		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = traces;
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		error.append("\nCaused by: ").append(ourCause);
		for (int i = 0; i <= m; i++)
			error.append("\tat ").append(trace[i]);
		if (framesInCommon != 0)
			error.append("\t... ").append(framesInCommon).append(" more");

		// Recurse if we have a cause
		Throwable newCause = getCause();
		if (trace != null && loopCounter<10){
			loopCounter++;
			error = printStackTraceAsCause(error, trace,
					newCause.getStackTrace(), newCause);
		}
		return error;
	}

}
