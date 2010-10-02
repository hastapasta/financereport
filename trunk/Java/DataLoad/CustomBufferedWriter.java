package com.roeschter.jsl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomBufferedWriter
{
	BufferedWriter fullfilewriter;
	BufferedWriter errorfilewriter;
	BufferedWriter sqlfilewriter;
	BufferedWriter tmpBuf;
	//static enum LOGS {FULL,ERROR,SQL,STATUS};
	
	static boolean bStaticStatus;
	static boolean bStaticSQL;
	
	CustomBufferedWriter(String strFullLog, String strErrorLog, String strSQLLog, boolean bSQL, boolean bStatus)
	{
		try
		{
			fullfilewriter = new BufferedWriter( new FileWriter(strFullLog));
			errorfilewriter = new BufferedWriter( new FileWriter(strErrorLog));
			sqlfilewriter = new BufferedWriter( new FileWriter(strSQLLog));
			bStaticStatus = bStatus;
			bStaticSQL = bSQL;
		}
		catch (IOException ioe)
		{
			System.out.println("Problem creating bufferedwriters for log files.");
			ioe.printStackTrace();
		}
	}
	
	
	public void writeln (String str, Logs type)
	{
		//setWriter(type);
		try
		{
			fullfilewriter.write(str);
			
			switch (type)
			{
			case ERROR:
				System.out.println("ERROR OCCURRED, check error.log");
				errorfilewriter.write(str);
				errorfilewriter.newLine();
				errorfilewriter.flush();
				fullfilewriter.write(str);
				fullfilewriter.newLine();
				fullfilewriter.flush();
				break;

			case SQL:
				if (bStaticSQL == true)
				{
					sqlfilewriter.write(str);
					sqlfilewriter.newLine();
					sqlfilewriter.flush();
					fullfilewriter.write(str);
					fullfilewriter.newLine();
					fullfilewriter.flush();
				}
				break;
			case STATUS1:
				System.out.println(str);
				fullfilewriter.write(str);
				fullfilewriter.newLine();
				fullfilewriter.flush();
				break;
				
			case STATUS2:
				if (bStaticStatus == true)
				{
					fullfilewriter.write(str);
					fullfilewriter.newLine();
					fullfilewriter.flush();
				}
				break;
				
			}
		}
		catch (IOException ioe)
		{
			System.out.println("Error in CustomBufferedWriter in generating the log file.");
			ioe.printStackTrace();
		}
	}
	
	public void writeln (Exception e)
	{
			System.out.println("EXCEPTION THROWN, check error.log");
			try
			{
					if (e!=null)
					{
						if (e.getMessage() != null)
						{
							//System.out.println(e.getMessage());
							errorfilewriter.write(e.getMessage());
							errorfilewriter.newLine();
							errorfilewriter.flush();
							fullfilewriter.write(e.getMessage());
							fullfilewriter.newLine();
							errorfilewriter.flush();
						}
						StackTraceElement[] tmp2 = e.getStackTrace();
						for (int i=(tmp2.length-1);i>=0;i--)
						{
							errorfilewriter.write(tmp2[i].toString());
							errorfilewriter.newLine();
							errorfilewriter.flush();
							fullfilewriter.write(tmp2[i].toString());
							fullfilewriter.newLine();
							fullfilewriter.flush();
						}
						/* These flushes are probably really inefficient */
						//tmpBuf.flush();
					}
					else
					{
						errorfilewriter.write("Attempting to write out exception but exception is null.");
						errorfilewriter.newLine();
						errorfilewriter.flush();
						fullfilewriter.write("Attempting to write out exception but exception is null.");
						fullfilewriter.newLine();
						fullfilewriter.flush();
						/* These flushes are probably really inefficient */
						//tmpBuf.flush();
					}
					
			}
			catch (IOException ioe)
			{
				System.out.println("Error in CustomBufferedWriter in generating the log file.");
				ioe.printStackTrace();
			}
			
		
		
	}	
}