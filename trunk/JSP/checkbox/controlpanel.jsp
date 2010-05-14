<%@ page import="java.sql.*,java.io.*,com.roeschter.jsl.UtilityFunctions" %>
<head>
<script type="text/javascript">
function loadXMLDoc()
{

if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
    }
  }
xmlhttp.open("GET","ajax_info.txt",true);
xmlhttp.send();

}
</script>
</head> 
<HTML>
	
	


<% 
String test = "";
class PrintStream
{
	String test;

	void println(String x)
	{
		test = test + x;
	}
	
		


}




String strFrame = request.getParameter("frame"); 
if (strFrame.compareTo("parent") == 0)
{
 %>
 
 <frameset rows="50%,*">
	<frame src="http://win-d2sjsg6emdd/examples/jsp/checkbox/controlpanel.jsp?frame=panel" name=form>
	<frameset cols="50%,*">
 	<frame src="http://win-d2sjsg6emdd/examples/jsp/checkbox/controlpanel.jsp?frame=stdout" name=stdout>
  <frame src="http://win-d2sjsg6emdd/examples/jsp/checkbox/controlpanel.jsp?frame=stderr" name=stderr>
 </frameset>
</frameset>

<%
}
else if (strFrame.compareTo("panel") == 0)
{

%>


<BODY bgcolor="white">


<form name="SelectFactData" action="controlpanel.jsp?frame=stdout" method=POST target=stdout> 	
					<table>
						<tr><td>
							<!-- <select name="data_set" onchange="alert(this.value);" onchange="UpdateField(this.selectedIndex);"> -->
							<select name="data_set" multiple size=20>
								<%
								  String path = request.getRealPath(request.getServletPath());
								  String reverseString = ((new StringBuffer(path)).reverse()).toString();
								  reverseString = reverseString.substring(reverseString.indexOf("\\"),reverseString.length());
								  path = ((new StringBuffer(reverseString)).reverse()).toString();
								   
									UtilityFunctions uf = new UtilityFunctions("mydb","root","madmax1.",path + "\\stdout.log",path + "\\stderr.log");
									String query = "select Data_Set,run_once from schedule order by run_once DESC,Data_Set";
					
									ResultSet rs = uf.db_run_query(query);
									String row;
									String selected = "";
								
									
									while(rs.next())
									{
										
										if (rs.getInt("run_once") != 0)
										{
											selected = "selected";
										}
										else
										{
											selected = "";
										}
										
										row = rs.getString("Data_Set");
										out.println("<option value=\"" + row + "\" " + selected + " >" + row + "</option>");
									}
								
								%>
							</select>
						</td>
						<td>
							<input type="submit" value="Submit" name="submit_internal">
						</td></tr>
							</table>
							
</form>

<div id="myDiv"><h2>Let AJAX change this text</h2></div>
<button type="button" onclick="loadXMLDoc()">Change Content</button>

</BODY>

<%

}
else if (strFrame.compareTo("stdout") == 0)
{



	if (request.getParameter("submit_internal") != null)
	{
		
	
	
%>
	<textarea name="stdoutlog" cols="40" rows="5">
<%
		String nTmp;
		BufferedReader in = new BufferedReader(new FileReader("stdout.log"));
		
		

		while ((nTmp = in.readLine()) != null)
			out.println(nTmp);
	%>
</textarea>
<%
	
	}
	
	
	
%>

This is the stdout frame.

<%
}
else if (strFrame.compareTo("stderr") == 0)
{
%>

This is the stderr frame.
<%
}

%>



</HTML>
