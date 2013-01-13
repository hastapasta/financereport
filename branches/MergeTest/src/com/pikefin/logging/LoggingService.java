package com.pikefin.logging;


import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.exceptions.GenericException;

/**
 * This Class is responsible for logging the messages using the log 4 j 
 * 
 * @author Amar_Deep_Singh
 *
 */
//@Service
//@Aspect
public class LoggingService {
	Logger log=Logger.getLogger(LoggingService.class);
	
	//@Before("execution(* com.pikefin.services.impl.*.*(..)) || execution(* com.pikefin.dao.*.*(..))")
	public void logBefore(JoinPoint joinPoint){
		log.info("========================\nStarting Execution of "+joinPoint.getSignature().getName()+"\n");
		if(ApplicationSetting.getInstance().isDebugMode()){
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Starting Execution of "+joinPoint.getSignature().getName(),Logs.NONE, "INFO");	
	}
	}
	
	
	//@After("execution(* com.pikefin.services.impl.*.*(..)) || execution(* com.pikefin.dao.*.*(..))")
	public void logAfter(JoinPoint joinPoint){
		log.info("========================\nReturning from Execution of "+joinPoint.getSignature().getName());
		if(ApplicationSetting.getInstance().isDebugMode()){
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Returning from Execution of  "+joinPoint.getSignature().getName(),Logs.NONE, "INFO");	
	}
	}
	
	//@AfterReturning(pointcut ="execution(* com.pikefin.services.impl.*.*(..)) || execution(* com.pikefin.dao.*.*(..))",returning= "result")
	public void logAfterRetun(JoinPoint joinPoint,Object result){
		
		log.info("========================\nReturned from method "+joinPoint.getSignature().getName());
		log.info("========================\nReturned value is   "+result);
		if(ApplicationSetting.getInstance().isDebugMode()){
			ApplicationSetting.getInstance().getStdoutwriter().writeln("========================\nReturned from method  "+joinPoint.getSignature().getName(),Logs.NONE, "INFO");	
			ApplicationSetting.getInstance().getStdoutwriter().writeln("========================\nReturned value is   "+result,Logs.NONE, "INFO");	
		}
	}
	
	//@AfterThrowing(pointcut ="execution(* com.pikefin.services.impl.*.*(..)) || execution(* com.pikefin.dao.*.*(..))",throwing="error")
	public void logAfterError(JoinPoint joinPoint,Throwable error){
		
		log.error("========================\nException thrown from  method "+joinPoint.getSignature().getName());
		log.error("========================\nException is   "+error);
		if(error instanceof GenericException){
			GenericException exception =(GenericException) error;
			log.error("ERROR CODE="+exception.getErrorCode());	
			log.error("ERROR MESSAGE="+exception.getErrorMessage());	
			log.error("ERROR DESCRIPTION="+exception.getErrorDescription());	
			log.error("ERROR REASOn="+exception.getCause());	

		}
		if(ApplicationSetting.getInstance().isDebugMode()){
			ApplicationSetting.getInstance().getStdoutwriter().writeln("========================\nException thrown from  method  "+joinPoint.getSignature().getName(),Logs.ERROR, "INFO");	
			ApplicationSetting.getInstance().getStdoutwriter().writeln("========================\nError is   "+error,Logs.ERROR, "INFO");	
			if(error instanceof GenericException){
				GenericException exception =(GenericException) error;
				System.out.println("ERROR CODE="+exception.getErrorCode());	
				System.out.println("ERROR MESSAGE="+exception.getErrorMessage());	
				System.out.println("ERROR DESCRIPTION="+exception.getErrorDescription());	
				System.out.println("ERROR REASOn="+exception.getCause());	

			}
		}
	}
	
	//@Around("execution(* com.pikefin.services.impl.*.*(..)) || execution(* com.pikefin.dao.*.*(..))")
	   public void logAroundMethod(ProceedingJoinPoint  joinPoint) throws Throwable {
	 
		log.info("========================\nInterceptiong the  method call: " + joinPoint.getSignature().getName());
		log.info("========================\nInterceptiong the  method arguments : " + Arrays.toString(joinPoint.getArgs()));
	 
		if(ApplicationSetting.getInstance().isDebugMode()){
			ApplicationSetting.getInstance().getStdoutwriter().writeln("========================\nInterceptiong the  method call:  "+joinPoint.getSignature().getName(),Logs.NONE, "INFO");	
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Interceptiong the  method arguments : " + Arrays.toString(joinPoint.getArgs()),Logs.NONE, "INFO");
			
		}
		joinPoint.proceed(); //continue on the intercepted method
		
	 
	   }
	 

}
