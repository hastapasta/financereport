<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pikefin.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>

<%

ArrayList<String[]> list1 = new ArrayList<String[]>();
ArrayList<String[]> list2 = new ArrayList<String[]>();

String[] blap9 = {"G","1","1G"};
String[] blap1 = {"A","1","1A"};
String[] blap2 = {"B","1","1B"};
String[] blap3 = {"C","1","1C"};
String[] blap4 = {"D","1","1D"};

String[] blap8 = {"G","2","2G"};
String[] blap5 = {"D","2","2D"};
String[] blap6 = {"B","2","2B"};
String[] blap7 = {"A","2","2A"};



/*list1.add(blap1);
list1.add(blap2);*/
list1.add(blap9);
list1.add(blap3);
list1.add(blap4);



list2.add(blap5);
list2.add(blap6);
list2.add(blap7); 
list2.add(blap8);

ArrayList<String[]> outList= PopulateSpreadsheet.joinLists(list1,list2,0);

out.println("<table>");
for (int i=0;i<outList.size();i++)
{
	out.println("<tr>");
	String[] temp = outList.get(i);
	for (int j=0;j<temp.length;j++)
	{
		out.println("<td>" + temp[j] +"</td>");
	}
	out.println("</tr>");
}
out.println("</table>");










%>