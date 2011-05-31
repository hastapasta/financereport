package org.jdamico.jhu.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jdamico.jhu.components.Controller;
import org.mortbay.log.Log;
import org.vikulin.utils.Constants;

public class CheckFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("In CheckFiles.java doGet()");
		System.out.println("Request parameter " + request.getParameter("fileXml"));
		
		if (request.getParameter("filename")!= null)
		{
			String strFile = Constants.conf.getFileDirectory()
			+ Constants.PATH_SEPARATOR + request.getParameter("filename");
			Controller control = new Controller();
			//String result = control.getFileMapXml(fileXml);
			String result = control.getFileSize(strFile) + "";
			PrintWriter out = response.getWriter();
			out.println(result);
			out.close();
		
		}
		else if (request.getParameter("deletefilename")!=null)
		{
			String strFile = request.getParameter("deletefilename");
			Controller control = new Controller();
			control.deleteFiles(strFile);
			PrintWriter out = response.getWriter();
			out.println("SUCCESS");
			out.close();			
		}
		else
		{
			String fileXml = Constants.conf.getFileDirectory()
					+ Constants.PATH_SEPARATOR + request.getParameter("fileXml");
			Controller control = new Controller();
			String result = control.getFileMapXml(fileXml);
			PrintWriter out = response.getWriter();
			out.println(result);
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}