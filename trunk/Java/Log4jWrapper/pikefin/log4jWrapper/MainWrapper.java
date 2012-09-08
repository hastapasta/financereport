package pikefin.log4jWrapper;

//import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock; 
//import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
//import org.apache.log4j.NDC;

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



public class MainWrapper	{
	/*PrintWriter fullfilewriter;
	PrintWriter errorfilewriter;
	PrintWriter sqlfilewriter;
	PrintWriter threadfilewriter;*/
	
	//static Logger logger = Logger.getLogger(CustomBufferedWriter.class);
	static Logger sqllogger; //= Logger.getLogger("SQLLogging");
	static Logger threadlogger;// = Logger.getLogger("ThreadLogging");
	static Logger fulllogger;// = Logger.getLogger("FullLogging");
	static Logger errorlogger;// = Logger.getLogger("ErrorLogging");
	static Logger consolelogger;
	//static Logger loggerfull = Logger.getLogger(CustomBufferedWriter.full.class);

	//static enum LOGS {FULL,ERROR,SQL,STATUS};
	
	static boolean bStaticStatus;
	static boolean bStaticSQL;
	static boolean bStaticThread;
	
	final ReentrantLock lock = new ReentrantLock(); 
	
	
	
	public MainWrapper()	{ 

		sqllogger = Logger.getLogger("SQLLogging");
		//System.out.println("here1");
		threadlogger = Logger.getLogger("ThreadLogging");
		//System.out.println("here2");
		fulllogger = Logger.getLogger("FullLogging");
		errorlogger = Logger.getLogger("ErrorLogging");
		consolelogger = Logger.getLogger("ConsoleLogging");
		this.logStatus1("Log4jWrapper version " + Configuration.getVersion(), "MainWrapper 1");

	}
	
	public void logError(String str, String code) {
		this.writeln(str,Logs.ERROR,code);
	}
	
	public void logWarn(String str, String code) {
		this.writeln(str,Logs.WARN,code);
	}
	
	public void logStatus1(String str, String code) {
		this.writeln(str,Logs.STATUS1,code);
	}
	
	public void logStatus2(String str, String code) {
		this.writeln(str,Logs.STATUS2,code);
	}
	
	public void logThread(String str, String code) {
		this.writeln(str,Logs.THREAD,code);
	}
	
	public void logSQL(String str, String code) {
		this.writeln(str,Logs.SQL,code);
	}
	

	
	
	public void writeln (String str, Logs type,String code) 	{
		//setWriter(type);
		try
		{
			//fullfilewriter.write(str);
			lock.lock();
			switch (type)
			{
			case ERROR:

				//errorfilewriter.println(code + ":" + str);

				//fullfilewriter.println("ERROR:" + code + ":" + str);
				//errorlogger.error(code + ":" + str);
				fulllogger.error(code + ":" + str);
				//consolelogger.error(code + ":" + str);

				break;
				
			case WARN:
				fulllogger.warn(code + ":" +str);
				break;

			case SQL:
				if (bStaticSQL == true)
				{
					//Not sure if this usage of NDCs makes sense here
					//NDC.push("SQL");
					//sqlfilewriter.println(code + ":" + str);

					//fullfilewriter.println("SQL:" + code + ":" + str);
					
					sqllogger.debug(code + ":" + str);
					//fulllogger.debug(code + ":" + str);
					//NDC.pop();

				}
				break;
				
			case THREAD:
				if (bStaticThread == true)
				{
					//NDC.push("Thread");
					//threadfilewriter.println(code + ":" + str);

					//fullfilewriter.println("Thread:" + code + ":" + str);
					threadlogger.debug(code + ":" + str);
					//fulllogger.debug(code + ":" + str);
					//NDC.pop();

				}
				break;
				
			case STATUS1:

				//fullfilewriter.println("STATUS:" + code + ";:" + str);
				fulllogger.info(code + ":" + str);
				//consolelogger.info(str);

				break;
				
			case STATUS2:
				/*if (bStaticStatus == true)
				{
					//fullfilewriter.println("STATUS:" + code + ":" + str);

				}*/
				fulllogger.debug(code + ":" + str);
				break;
			//type NONE is obsolete, should remove it eventually
			/*case NONE:
				//fullfilewriter.println(str);
				fulllogger.debug(str);
				break;*/
				
			}
		}
		catch (Exception ioe)
		{
			System.out.println("Error in CustomBufferedWriter in generating the log file.");
			ioe.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	
	public void wrapperNDCPush(String strInput) {
		NDC.push(strInput);
	}
	
	public void wrapperNDCPop() {
		NDC.pop();
	}
	
	public void writeln (Exception e)	{
			fulllogger.error("EXCEPTION THROWN, check logs");
			try
			{
				lock.lock();
					if (e!=null)
					{
						
						fulllogger.error(e);
						/*if (e.getMessage() != null)
						{

							//errorfilewriter.println("EXCEPTION:" + e.getMessage());

							//fullfilewriter.println("EXCEPTION:" + e.getMessage());
							//errorlogger.error(e.getMessage());
							fulllogger.error(e.getMessage());

						}
						fulllogger.error(e.getLocalizedMessage());
					    fulllogger.error(e.getCause());*/
						
						StackTraceElement[] tmp2 = e.getStackTrace();
						for (int i=(tmp2.length-1);i>=0;i--)
						{
							//errorfilewriter.println(tmp2[i].toString());

							//fullfilewriter.println(tmp2[i].toString());
							//errorlogger.error(tmp2[i].toString());
							fulllogger.error(tmp2[i].toString());

						}

					}
					else
					{
						//errorfilewriter.println("EXCEPTION:Attempting to write out exception but exception is null.");

						//fullfilewriter.println("EXCEPTION:Attempting to write out exception but exception is null.");
						//errorlogger.error("EXCEPTION:Attempting to write out exception but exception is null.");
						fulllogger.error("EXCEPTION:Attempting to write out exception but exception is null.");

					}
					
			}
			catch (Exception ioe)
			{
				System.out.println("Error in CustomBufferedWriter in generating the log file.");
				ioe.printStackTrace();
			}
			finally
			{
				lock.unlock();
			}
			
		
		
	}	
}