
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;







public class Checksum {

	/**
	 * @param args
	 */
	
	
	/*
	 * 
	 * arg0 -> SEND or JOIN
	 * arg1 -> Directory of files to split/send
	 * arg2 -> Directory for split files NOTE: TERMINATE THIS PARAMETER WITH A DIRECTORY CHARACTER!
	 * arg3 -> path and name of csv file NOTE: TERMINATE THIS PARAMETER WITH A DIRECTORY CHARACTER!
	 * 
	 * 
	 * 
	 * 
	 */
	
	static ArrayList<String> fileList;
	//static final int Chunk_Size = 500000000;
	static final int Chunk_Size = 100000;
	static final boolean bRecursive = false;
	//static String strInputDir = "/home/ollie/scripts/logmeintest";
	//static String strOutputDir = "/tmp/split";
	//static String strOutputCSV = "/tmp/output.csv";
	
	
	
	public static void main(String[] args) {
		
		
		UtilityFunctions.createCSV(null, args[3] + "unsplitchecksum.csv", false);
		UtilityFunctions.createCSV(null, args[3] + "splitchecksum.csv", false);
		
		if (args[0].toUpperCase().compareTo("SEND")==0)
		{
			if (bRecursive)
				fileList = generateFileList(new File(args[1]));
			else
			{
				fileList = new ArrayList<String>();
				File[] listOfFiles = (new File(args[1])).listFiles();
				for (File file : listOfFiles)
				{				
					if (!file.isDirectory())
						fileList.add(file.getPath());							
				}

			}
			
			
			for (String strFile : fileList)
			{
				generateChecksums(strFile,args[3] + "unsplitchecksum.csv");
				ArrayList<File> listFile = split(strFile,args[2]);
				for (File tmpFile : listFile)
				{
					generateChecksums(tmpFile.getPath(),args[3] + "splitchecksum.csv");	
				}
				
				
			}
		} 
		else {
			
			
			
			
			
			
		}
		
		
		
		
	}
	
	public static void generateChecksums(String strInputFile, String strCSVFile) {
		
		ArrayList<String[]> outputList = new ArrayList<String[]>();
		
		
		try
		{
			//String plaintext = "your text here";
			
			MessageDigest m = MessageDigest.getInstance("MD5");
			
			
			File aFile = new File(strInputFile);
			
			
			//for (int i=0;i<fileList.size();i++)
			//{
				//InputStream is = new FileInputStream("/tmp/grepoutput");
				InputStream is = new FileInputStream(aFile);
				
				System.out.println("Processing: " + strInputFile);
				
				//BigDecimal bdLength = new BigDecimal(aFile.length());
				double dLength = aFile.length();
				
				try {
				  is = new DigestInputStream(is, m);
				  // read stream to EOF as normal...
				  int nTmp;	
				  //BigDecimal bdCount = new BigDecimal("0");
				  double dCount = 0;
				  String returned_content="";
				  while ((nTmp = is.read()) != -1)
				  {
					  //bdCount.add(new BigDecimal("1"));
					  dCount++;
					  if (dCount % 600000000 == 0)
					  {
						  System.out.println(". ");
					  }
					  else if (dCount % 20000000 == 0)
					  {
						  System.out.print(". ");
						  
					  }
					  

					  
				  }
					  
						//returned_content = returned_content + (char)nTmp;
				  
				}
				finally {
				  is.close();
				}
				byte[] digest = m.digest();
		
				m.reset();
				//m.update(plaintext.getBytes());
				//byte[] digest = m.digest();
				BigInteger bigInt = new BigInteger(1,digest);
				String hashtext = bigInt.toString(16);
				// Now we need to zero pad it if you actually want the full 32 chars.
				while(hashtext.length() < 32 ){
				  hashtext = "0"+hashtext;
				}
				
				String[] arrayTmp = new String[2];
				
				arrayTmp[0] = strInputFile;
				arrayTmp[1] = hashtext;
				
				outputList.add(arrayTmp);
				
				System.out.println("Hash Code: " + hashtext);
			//}
			
			UtilityFunctions.createCSV(outputList, strCSVFile, true);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			System.out.println(nsae.getMessage());
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println(fnfe.getMessage());
		}
		catch (IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
	}
	
	public static BigDecimal modBD(BigDecimal bd1, BigDecimal bd2)
	{

		BigDecimal bdReturn = bd1.divide(bd2, BigDecimal.ROUND_DOWN).multiply(bd2);
		return((bdReturn.subtract(bd1)));
		
	}
	
	public static ArrayList<String> generateFileList(File aFile) {
	    //spc_count++;
		
		if (aFile.isDirectory())
		{
			File[] listOfFiles = aFile.listFiles();
			
			ArrayList<String> mainlist = new ArrayList<String>();
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				ArrayList<String> returnlist = generateFileList(listOfFiles[i]);
				
				if (returnlist != null)
				{
					for (int j=0;j<returnlist.size();j++)
					{
						mainlist.add(returnlist.get(j));
					}
				}
							
			}
			return(mainlist);
	
			
			
		}
		else
		{
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(aFile.getPath());
			return tmp;
		}
	}	
	
	public static void join(ArrayList<File> files, String fname)
	{
	
		File ofile = new File(fname);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		long bytesRead = 0;
		try {
		    fos = new FileOutputStream(ofile,true);             
		    for (File file : files) {
		        fis = new FileInputStream(file);
		        fileBytes = new byte[(int) file.length()];
		        bytesRead = fis.read(fileBytes, 0,(int)  file.length());
		        assert(bytesRead == fileBytes.length);
		        assert(bytesRead == (int) file.length());
		        fos.write(fileBytes);
		        fos.flush();
		        fileBytes = null;
		        fis.close();
		        fis = null;
		    }
		    fos.close();
		    fos = null;
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());			
		}
	}
		
		public static ArrayList<File> split(String fname,String strOutputDir)
		{
		
			ArrayList<File> returnList = new ArrayList<File>();
			
			File ifile = new File(fname); 
			FileInputStream fis;
			String newName;
			FileOutputStream chunk;
			long fileSize = (long) ifile.length();
			int nChunks = 0, read = 0, readLength = Chunk_Size;
			byte[] byteChunk;
			try {
			    fis = new FileInputStream(ifile);
			    //StupidTest.size = (int)ifile.length();
			    while (fileSize > 0) {
			        if (fileSize <= Checksum.Chunk_Size) {
			            readLength = (int)fileSize;
			        }
			        byteChunk = new byte[readLength];
			        read = fis.read(byteChunk, 0, readLength);
			        fileSize -= read;
			        assert(read==byteChunk.length);
			        nChunks++;
			        newName = strOutputDir + ifile.getName() + ".part" + Integer.toString(nChunks - 1);
			        File tmpFile = new File(newName);
			        chunk = new FileOutputStream(tmpFile);
			        chunk.write(byteChunk);
			        chunk.flush();
			        chunk.close();
			        returnList.add(tmpFile);
			        byteChunk = null;
			        chunk = null;
			    }
			    fis.close();
			    fis = null;
			}
			catch (IOException ioe)
			{
				System.out.println(ioe.getMessage());
				return(null);
			}
			
			return(returnList);
		}


}

