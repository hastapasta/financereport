package org.jdamico.jhu.web.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.jdamico.jhu.components.Controller;
import org.vikulin.utils.Constants;

public class Uploader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		Controller control = new Controller();
		File oPartFile = null;
		String fileMapXmlFileName = null;
		Integer fileIndex = null;
		ServletFileUpload upload = new ServletFileUpload();
		try {
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream is = item.openStream();

				if (name.equals("filecontents"))
					try {
						oPartFile = new File(
								org.vikulin.utils.Constants.conf
										.getFileDirectory()
										+ Constants.PATH_SEPARATOR
										+ item.getName());

						OutputStream out = new FileOutputStream(oPartFile);
						byte[] buf = new byte[8*1024];
						int len;
						while ((len = is.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						out.close();
						is.close();
					} catch (Exception localIOException) {
						localIOException.printStackTrace();
					}
				else if (name.equals("filemap")) {
					fileMapXmlFileName = Streams.asString(is);
				} else if (name.equals("fileindex")){
					fileIndex = Integer.parseInt(Streams.asString(is));
				}
			}

		} catch (FileUploadException e) {
			System.out.println("Unable to process file upload.");
			e.printStackTrace();
		}
		if (fileMapXmlFileName == null)
			return;
		try {
//			if(true){
			String corruptPartsList = control.updateFileMap(fileMapXmlFileName, oPartFile, fileIndex);
			if(corruptPartsList!=null){
				response.setContentType("text/html");
				response.sendError(HttpServletResponse.SC_LENGTH_REQUIRED, corruptPartsList);
			}
		} catch (Exception e) {
			System.out.println("Unable to update FileMap.");
			e.printStackTrace();
		}
	}
}