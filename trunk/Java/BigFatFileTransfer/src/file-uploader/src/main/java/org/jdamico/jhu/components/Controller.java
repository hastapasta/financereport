package org.jdamico.jhu.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.jdamico.jhu.dataobjects.FileMap;
import org.jdamico.jhu.dataobjects.PartialFile;
import org.jdamico.jhu.runtime.Upload;
import org.jdamico.jhu.utils.Helper;
import org.jdamico.jhu.xml.ObjConverter;
import org.vikulin.utils.Constants;

public class Controller {
	private static final Logger log = Logger.getLogger(Controller.class);
	public void writeFileMapXML(FileMap fileMap, String folder)
			throws IOException {
		
		/*
		 * <?xml version="1.0" encoding="UTF-8"?> <filemap> <sourcefile name=""
		 * md5="" offset=""/> <pfiles> <pfile name="" md5="" uploaded=""/>
		 * </pfiles> </filemap>
		 */
		
		

		String sourceFileName = fileMap.getSourceFile().getName();
		
		log.info("Writing control file to location: " + folder + Constants.PATH_SEPARATOR + sourceFileName + ".xml");

		BufferedWriter out = new BufferedWriter(new FileWriter(folder
				+ Constants.PATH_SEPARATOR + sourceFileName + ".xml"));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<filemap>\n" + "<sourcefile name=\"" + sourceFileName
				+ "\" md5=\"" + fileMap.getSourceFile().getMd5()
				+ "\" offset=\"" + fileMap.getSourceFile().getOffset()
				+ "\"/>\n" + "<pfiles>\n");

		for (int j = 0; j < fileMap.getPartialFileList().length; j++) {
			out.write("<pfile name=\""
					+ fileMap.getPartialFileList()[j].getName() + "\" md5=\""
					+ fileMap.getPartialFileList()[j].getMd5()
					+ "\" uploaded=\""
					+ fileMap.getPartialFileList()[j].isUploaded() + "\"/>\n");
		}

		out.write("</pfiles>\n" + "</filemap>");

		out.close();

	}

	public byte[] convertByteArray2byteArray(Byte[] source) {
		Byte[] ByteElement = source;
		byte[] byteElement = new byte[ByteElement.length];
		for (int k = 0; k < ByteElement.length; k++) {
			byteElement[k] = ByteElement[k];
		}
		return byteElement;
	}

	public Byte[] convertbyteArray2ByteArray(byte[] source) {
		byte[] byteElement = source;
		Byte[] ByteElement = new Byte[byteElement.length];
		for (int k = 0; k < byteElement.length; k++) {
			ByteElement[k] = byteElement[k];
		}
		return ByteElement;
	}

	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[1024];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;

		long heapMaxSize = Runtime.getRuntime().maxMemory();

		try {
			bytes = new byte[(int) length];
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
				offset += numRead;
			if (offset < bytes.length)
				throw new IOException("Could not completely read file "
						+ file.getName());
		} catch (OutOfMemoryError e) {

			if (heapMaxSize > 1000000000)
				System.out
						.println("There is no enough heap size memory to build part of this file. Check if you have any other java program running at same time.");
			else
				System.out
						.println("There is no enough heap size memory to build part of this file. Increase your heap size memory using. Example -Xmx1024M.");
			System.exit(1);
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public byte[] createChecksumOld(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	public byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);
		/*
		 * OFP - made changes to this function.
		 */
		System.out.println("Processing checksum for file " + filename);
		byte[] buffer = new byte[2048];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		long lCount = 0;
		do {
			numRead = fis.read(buffer);
			lCount += 2048;
			if (lCount % 614400000 == 0)
				System.out.println(". ");
			else if (lCount % 20480000 == 0)
				System.out.print(". ");
				
				
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		System.out.println();
		return complete.digest();
		
	}

	private String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public String MD5(byte[] data) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {

		String text = convertToHex(data);

		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public byte[] createChecksum(byte[] fileByte) throws Exception {

		MessageDigest complete = MessageDigest.getInstance("MD5");
		complete.digest(fileByte);
		return complete.digest();
	}

	public String getFileMD5(String fileName) throws Exception {
		byte[] b = createChecksum(fileName);
		return MD5(b);
	}

	public String getFileMD5(byte[] fileByte) throws Exception {
		byte[] b = createChecksum(fileByte);

		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public String extractExtensionFromFileName(String fileName) {
		return fileName.substring(fileName.length() - 4);
	}

	public boolean uploadFiles(File partialElementFile, String fileMapFile, String host,
			int port, Upload upload, Integer fileIndex) throws Exception {

		boolean ret = false;

		String url = "http://" + host + ":" + port + "/Uploader";

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		FileBody bin = new FileBody(partialElementFile);
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("filecontents", bin);

		if (fileMapFile != null) {
			StringBody filemap = new StringBody(fileMapFile);
			StringBody index = new StringBody(fileIndex+"");
			reqEntity.addPart("filemap", filemap);
			reqEntity.addPart("fileindex", index);
		}
		httppost.setEntity(reqEntity);

		HttpResponse response = null;

		try {
			response = httpclient.execute(httppost);
			
			switch (response.getStatusLine().getStatusCode()) {
			case HttpServletResponse.SC_OK:
				ret = true;
				break;
			case HttpServletResponse.SC_LENGTH_REQUIRED:
				ret = false;
				ObjConverter converter = new ObjConverter();
				FileMap fileMap = converter
				.convertToFileMap(Helper.getInstance()
						.getStringFromFile(
								Constants.conf.getFileDirectory()
										+ Constants.PATH_SEPARATOR
										+ fileMapFile));
				String[] corruptList =  response.getStatusLine().getReasonPhrase().split("&");
				String message="The following parts are corrupted:\n";
				for (int i=0;i<corruptList.length;i++){
					message = message+fileMap.getPartialFileList()[Integer.parseInt(corruptList[i])].getName()+"\n";
				}
				upload.error(message);
			default:
				break;
			}
			
		} catch (ClientProtocolException e) {
			System.out.println("Remote host is unreachable: " + url);
			upload.error("Remote host is unreachable: " + url);
		} catch (IOException ioe) {
			System.out.println("Remote host is unreachable: " + url);
			upload.error("Remote host is unreachable: " + url);
		}
		return ret;

	}

	public PartialFile[] checkSentFiles(String fileName, String remoteHost,
			int port) throws Exception {
		String urlStr = "http://" + remoteHost + ":" + port
				+ "/CheckFiles?fileXml=" + URLEncoder.encode(fileName, "UTF-8");
		String xml = Helper.getInstance().getStringFromUrl(urlStr);

		PartialFile[] pFileArray = null;

		if (xml != null && xml.length() > 20) {
			ObjConverter converter = new ObjConverter();
			FileMap fileMap = converter.convertToFileMap(xml);
			pFileArray = fileMap.getPartialFileList();
		}

		return pFileArray;
	}

	public String updateFileMap(String fileMapXmlFileName, File oPartFile, Integer fileIndex) throws Exception {
		ObjConverter converter = new ObjConverter();
		String corruptedPartsList = "";
		FileMap fileMap = converter
				.convertToFileMap(Helper.getInstance()
						.getStringFromFile(
								Constants.conf.getFileDirectory()
										+ Constants.PATH_SEPARATOR
										+ fileMapXmlFileName));
//		for (int i = 0; i < fileMap.getPartialFileList().length; i++) {
			PartialFile partFile = fileMap.getPartialFileList()[fileIndex];
//			File e = new File(Constants.conf.getServerFileDirectory()
//					+ Constants.PATH_SEPARATOR
//					+ partFile.getName());

			String uploadedMd5 = null;

			if (oPartFile.exists()) {
				uploadedMd5 = getFileMD5(oPartFile.getAbsolutePath());
				partFile.setUploaded(true);
				//check MD5
				if (!uploadedMd5.equals(partFile.getMd5())) {
					log.error("Error! "
							+ partFile + " | "
							+ uploadedMd5);
					corruptedPartsList=corruptedPartsList+fileIndex+"&";					
				} else {
					log.info(partFile.getName() + " | "+uploadedMd5+" [ok]");
				}
			}
//		}
		
		writeFileMapXML(fileMap, Constants.conf.getFileDirectory());
		if (corruptedPartsList.length()>0) return corruptedPartsList;
		else
			return null;
	}

	public String getFileMapXml(String fileXml) {
		return Helper.getInstance().getStringFromFile(fileXml);
	}

	public void consolidateFileAtRemoteHost(String fileName, String host,
			int port, Upload upload) throws UnsupportedEncodingException {
		String urlStr = "http://" + host + ":" + port
				+ "/ConsolidationServlet?fileXml=" +  URLEncoder.encode(fileName, "UTF-8");
		String responseMessage = Helper.getInstance().getStringFromUrl(urlStr);
		if (!responseMessage.equals("ok")) {
			upload.error(responseMessage);
		}
	}

	public void consolidateFile(String fileMapXml) throws Exception {
		ObjConverter converter = new ObjConverter();
		FileMap fileMap = converter.convertToFileMap(Helper.getInstance()
				.getStringFromFile(fileMapXml));
		Joiner joiner = new Joiner();
		File firstFile = new File(
				Constants.conf.getFileDirectory()
						+ Constants.PATH_SEPARATOR
						+ (new File(fileMap.getPartialFileList()[0].getName()))
								.getName());
		//joiner.join(firstFile, true);
		joiner.join(firstFile, false);
		String uploadedMd5 = getFileMD5(Constants.conf.getFileDirectory()
				+ Constants.PATH_SEPARATOR + fileMap.getSourceFile().getName());
		if (uploadedMd5.equals(fileMap.getSourceFile().getMd5())) {
//			System.out.println("Consolidation worked!");
			log.info(fileMap.getSourceFile().getName() + " | "+uploadedMd5 + " [ok]");
			File rmF = new File(Constants.conf.getFileDirectory()
					+ Constants.PATH_SEPARATOR
					+ fileMap.getSourceFile().getName() + ".xml");
			rmF.delete();
		} else {
			throw new Exception("Accempled file ("
					+ fileMap.getSourceFile().getName() + ") is corrupted!");
		}
	}
}