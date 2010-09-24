package com.roeschter.jsl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomBufferedWriter extends BufferedWriter
{
	CustomBufferedWriter(FileWriter fw)
	{
		super(fw);
	}
	
	public void writeln (String str)
	{
		try
		{
			this.write(str);
			this.newLine();
			this.flush();
		}
		catch (IOException ioe)
		{
			System.out.println("Error in CustomBufferedWriter in generating the log file.");
			ioe.printStackTrace();
		}
	}
	
	public void writeln (Exception e)
	{
	
			try
			{
					if (e!=null)
					{
						if (e.getMessage() != null)
						{
							System.out.println(e.getMessage());
							this.write(e.getMessage());
							this.newLine();
						}
						StackTraceElement[] tmp2 = e.getStackTrace();
						for (int i=(tmp2.length-1);i>=0;i--)
						{
							this.write(tmp2[i].toString());
							this.newLine();
						}
						this.flush();
					}
					else
					{
						this.write("Attempting to write out exception but exception is null.");
						this.newLine();
						this.flush();
					}
					
			}
			catch (IOException ioe)
			{
				System.out.println("Error in CustomBufferedWriter in generating the log file.");
				ioe.printStackTrace();
			}
			
		
		
	}	
}