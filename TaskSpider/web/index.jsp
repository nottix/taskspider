<%@ page import = "java.util.*" %>
<%@ page import = "java.lang.*" %>
<%@ page import = "org.apache.lucene.document.*" %>
<jsp:useBean id="task" scope="request" class="taskspider.bean.TaskBean"/>
<jsp:setProperty name="task" property="*" />

<%
if(request.getParameter("Submit")!=null) {
	out.println(task.doSearch(request.getParameter("taskString"), request.getParameter("query")));
	out.println(task.toString());
	Vector<Document> docs = task.getResults();
	out.println("size: "+docs.size());
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Untitled Document</title>
<link rel="StyleSheet" href="dtree.css" type="text/css" />
	<script type="text/javascript" src="dtree.js"></script>
    <style type="text/css">
<!--
.titolo {font-size: 18px; color:#000066; font-family:Arial, Helvetica, sans-serif; text-decoration:underline;}
.description {font-size:14px; color:#000000; font-family:Arial, Helvetica, sans-serif}
.link{font-size:12px; color:#009900; font-family:Arial, Helvetica, sans-serif}
.footer{font-size:10px; color:#999999; font-family:Arial, Helvetica, sans-serif}
-->
    </style>
</head>

<body>
<table width="500">
  <tr>
    <td width="200"><img src="img/taskspiderlogoricerca.gif" width="200" height="50" /></td>
    <td width="288" align="center" valign="middle"><form id="form1" name="form1" method="post" action="">
      <label>
        <input name="taskString" type="text" size="30" />
        </label>
      <label>
        <input name="query" type="text" size="30" />
        </label>
      <input type="submit" name="Submit" value="Cerca" />
    </form>
    </td>
  </tr>
</table>
<table width="100%" height="10" bgcolor="#ABC8FB">
  <tr>
    <td>		<%
    out.println(System.getProperty("user.dir"));
    %></td>
    <td></td>
  </tr>
</table><br />
<table width="100%">
  <tr>
    <td width="33%" valign="top">
	<div class="dtree">

	<p><a href="javascript: d.openAll();">Espandi</a> | <a href="javascript: d.closeAll();">Chiudi</a></p>

<!--	<script type="text/javascript">-->


	

<!--	</script>-->
</div>
	</td>



	  </td>
  </tr>
</table>
<br/>
<table width="100%" height="10" bgcolor="#ABC8FB">
  <tr>
    <td></td>
    <td>
    <%
		int padre1 = 0;
		int indexof1 = 0;
			for(int i = 0; i<task.getResults().size();i++){
	%>
	<!--Risultato-->
	<a href="<%=task.getResults().get(i).get("url")%>" class="titolo"><%=task.getResults().get(i).get("title")%></a><br />
      <span class="description"><%=task.getResults().get(i).get("description")%></span><br />
      <span class="link"><%=task.getResults().get(i).get("url")%></span> <br />
	  <hr color="#999999" size="1" width="80%" align="left" />
	  <!--fine risultato-->
	 <%
	 }
	 %> 
    </td>
  </tr>
</table><br />
<div align="center" class="footer">TaskSpider - MGRI 2007/2008 - Notargiacomo Simone & Schipani Giuseppe</div>
</body>
</html>
