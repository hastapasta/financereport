//package com.roeschter.jsl;


//import java.io.BufferedWriter; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock; 
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/*Level 	Description
 * FATAL 	Severe errors that cause premature termination. Expect these to be 
 * 			immediately visible on a status console.
 * ERROR 	Other runtime errors or unexpected conditions. Expect these to be immediately 
 * 			visible on a status console.
 * WARN 	Use of deprecated APIs, poor use of API, 'almost' errors, other runtime 
 * 			situations that are undesirable or unexpected, but not necessarily "wrong". Expect 
 * 			these to be immediately visible on a status console.
 * INFO 	Interesting runtime events (startup/shutdown). Expect these to be immediately visible
 * 			on a console, so be conservative and keep to a minimum.
 * DEBUG 	Detailed information on the flow through the system. Expect these to be written to logs only.
 * TRACE 	More detailed information. Expect these to be written to logs only. Was only added in version 1.2.12.
*/



public class CustomBufferedWriter
{
	PrintWriter fullfilewriter;
	PrintWriter errorfilewriter;
	PrintWriter sqlfilewriter;
	PrintWriter threadfilewriter;
	
	static Logger logger = Logger.getLogger(CustomBufferedWriter.class);

	//static enum LOGS {FULL,ERROR,SQL,STATUS};
	
	static boolean bStaticStatus;
	static boolean bStaticSQL;
	static boolean bStaticThread;
	
	final ReentrantLock lock = new ReentrantLock(); 
	
	
	
	CustomBufferedWriter(String strFullLog, String strErrorLog, String strSQLLog, String strThreadLog, boolean bSQL, boolean bStatus, boolean bThread)
	{
		try
		{
			/*fullfilewriter = new BufferedWriter( new FileWriter(strFullLog));
			errorfilewriter = new BufferedWriter( new FileWriter(strErrorLog));
			sqlfilewriter = new BufferedWriter( new FileWriter(strSQLLog));*/
			try
			{
				FileUtils.copyFile(new File(strFullLog), new File("archive_" + strFullLog));
				FileUtils.copyFile(new File(strErrorLog), new File("archive_" + strErrorLog));
				FileUtils.copyFile(new File(strSQLLog), new File("archive_" + strSQLLog));
				FileUtils.copyFile(new File(strThreadLog), new File("archive_" + strThreadLog));
			}
			catch (FileNotFoundException fnfe)
			{
				//ignore
			}
			fullfilewriter = new PrintWriter( new FileWriter(strFullLog,false),true);
			errorfilewriter = new PrintWriter( new FileWriter(strErrorLog,false),true);
			sqlfilewriter = new PrintWriter( new FileWriter(strSQLLog,false),true);
			threadfilewriter = new PrintWriter( new FileWriter(strThreadLog,false),true);
			
		
			
			bStaticStatus = bStatus;
			bStaticSQL = bSQL;
			bStaticThread = bThread;
		}
		catch (IOException ioe)
		{
			System.out.println("Problem creating bufferedwriters for log files.");
			ioe.printStackTrace();
		}
	}
	
	
	public void writeln (String str, Logs type,String code)
	{
		//setWriter(type);
		try
		{
			//fullfilewriter.write(str);
			//lock.lock();
			switch (type)
			{
			case ERROR:
				//System.out.println("ERROR OCCURRED, check error.log");
				//System.out.println(code + ":" + str);
				errorfilewriter.println(code + ":" + str);
				//errorfilewriter.newLine();
				//errorfilewriter.flush();
				fullfilewriter.println("ERROR:" + code + ":" + str);
				logger.error(code + ":" + str);
				//fullfilewriter.newLine();
				//fullfilewriter.flush();
				break;

			case SQL:
				if (bStaticSQL == true)
				{
					//Not sure if this usage of NDCs makes sense here
					NDC.push("SQL");
					sqlfilewriter.println(code + ":" + str);
					//sqlfilewriter.newLine();
					//sqlfilewriter.flush();
					fullfilewriter.println("SQL:" + code + ":" + str);
					logger.debug(code + ":" + str);
					NDC.pop();
					//fullfilewriter.newLine();
					//fullfilewriter.flush();
				}
				break;
				
			case THREAD:
				if (bStaticThread == true)
				{
					NDC.push("Thread");
					threadfilewriter.println(code + ":" + str);
					//sqlfilewriter.newLine();
					//sqlfilewriter.flush();
					fullfilewriter.println("SQL:" + code + ":" + str);
					logger.debug(code + ":" + str);
					NDC.pop();
					//fullfilewriter.newLine();
					//fullfilewriter.flush();
				}
				break;
				
			case STATUS1:
				//System.out.println(str);
				fullfilewriter.println("STATUS:" + code + ":" + str);
				logger.info("STATUS:" + code + ":" + str);
				//fullfilewriter.newLine();
				//fullfilewriter.flush();
				break;
				
			case STATUS2:
				if (bStaticStatus == true)
				{
					fullfilewriter.println("STATUS:" + code + ":" + str);
					//fullfilewriter.newLine();
					//fullfilewriter.flush();
				}
				logger.debug("STATUS:" + code + ":" + str);
				break;
			case NONE:
				fullfilewriter.println(str);
				logger.debug(str);
				break;
				
			}
		}
		catch (Exception ioe)
		{
			System.out.println("Error in CustomBufferedWriter in generating the log file.");
			ioe.printStackTrace();
		}
		/*finally
		{
			lock.unlock();
		}*/
	}
	
	public void writeln (Exception e)
	{
			System.out.println("EXCEPTION THROWN, check error.log");
			try
			{
				lock.lock();
					if (e!=null)
					{
						if (e.getMessage() != null)
						{
							//System.out.println(e.getMessage());
							errorfilewriter.println("EXCEPTION:" + e.getMessage());
							//errorfilewriter.newLine();
							//errorfilewriter.flush();
							fullfilewriter.println("EXCEPTION:" + e.getMessage());
							logger.error(e.getMessage());
							//fullfilewriter.newLine();
							//errorfilewriter.flush();
						}
						StackTraceElement[] tmp2 = e.getStackTrace();
						for (int i=(tmp2.length-1);i>=0;i--)
						{
							errorfilewriter.println(tmp2[i].toString());
							//errorfilewriter.newLine();
							//errorfilewriter.flush();
							fullfilewriter.println(tmp2[i].toString());
							logger.error(tmp2[i].toString());
							//fullfilewriter.newLine();
							//fullfilewriter.flush();
						}
						/* These flushes are probably really inefficient */
						//tmpBuf.flush();
					}
					else
					{
						errorfilewriter.println("EXCEPTION:Attempting to write out exception but exception is null.");
						//errorfilewriter.newLine();
						//errorfilewriter.flush();
						fullfilewriter.println("EXCEPTION:Attempting to write out exception but exception is null.");
						logger.error("EXCEPTION:Attempting to write out exception but exception is null.");
						//fullfilewriter.newLine();
						//fullfilewriter.flush();
						/* These flushes are probably really inefficient */
						//tmpBuf.flush();
					}
					
			}
			catch (Exception ioe)
			{
				System.out.println("Error in CustomBufferedWriter in generating the log file.");
				ioe.printStackTrace();
			}
			/*finally
			{
				lock.unlock();
			}*/
			
		
		
	}	
}