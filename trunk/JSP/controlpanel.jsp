<%@ page import="java.sql.*,com.roeschter.jsl.UtilityFunctions" %>
<HTML>
<!--
 Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->

<% String strFrame = request.getParameter("frame"); 
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


<FORM TYPE=POST ACTION=check.jsp?frame=form>
<BR>
<font size=5 color="red">
Check all Favorite fruits: <br>

<input TYPE=checkbox name=fruit VALUE=apples> Apples <BR>
<input TYPE=checkbox name=fruit VALUE=grapes> Grapes <BR>
<input TYPE=checkbox name=fruit VALUE=oranges> Oranges <BR>
<input TYPE=checkbox name=fruit VALUE=melons> Melons <BR>


<br> <INPUT TYPE=submit name=submit Value="Submit">

</font>
</FORM>
</BODY>

<%

}
else if (strFrame.compareTo("stdout") == 0)
{
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
