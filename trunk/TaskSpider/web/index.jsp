<%@ page import = "java.util.*" %>
<%@ page import = "java.lang.*" %>
<%@ page import = "org.apache.lucene.document.*" %>
<%@ page import = "taskspider.bean.TaskBean" %>

<%
String query;
//out.println("do: "+TaskBean.getArg("do", request.getQueryString()));
//out.println("type: "+TaskBean.getArg("type", request.getQueryString()));
if((query=request.getQueryString())!=null && TaskBean.getArg("index", request.getQueryString()).equals("0") && TaskBean.getArg("do", request.getQueryString())!=null && TaskBean.getArg("type", request.getQueryString())!=null) {
	//out.println("SIZE: "+TaskBean.doSearch( TaskBean.getArg("task", query), TaskBean.getArg("query", query), TaskBean.getArg("do", request.getQueryString()).equals("1") ));
	TaskBean.doSearch( TaskBean.getArg("task", query), TaskBean.getArg("query", query), TaskBean.getArg("do", request.getQueryString()), TaskBean.getArg("type", request.getQueryString()), TaskBean.getArg("exp", request.getQueryString()) );
	
	//Thread.sleep(1000);
}
else if(request.getParameter("Submit")!=null) {
	TaskBean.doSearch(request.getParameter("taskString"), request.getParameter("query"), "1", "1", "normal");
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>TaskSpider</title>
<link rel="StyleSheet" href="dtree.css" type="text/css" />
	<script type="text/javascript" src="dtree.js"></script>
    <style type="text/css">
<!--
.titolo {font-size: 18px; color:#000066; font-family:Arial, Helvetica, sans-serif; text-decoration:underline;}
.description {font-size:14px; color:#000000; font-family:Arial, Helvetica, sans-serif}
.field {font-size:12px; color:#000000; font-family:Arial, Helvetica, sans-serif}
.link{font-size:12px; color:#009900; font-family:Arial, Helvetica, sans-serif}
.footer{font-size:10px; color:#999999; font-family:Arial, Helvetica, sans-serif}
-->
    </style>
</head>

<body>
<table width="500">
  <tr>
    <td width="200"><img src="img/taskspiderlogoricerca.gif" width="200" height="50" /></td>
    <td width="200" align="center" valign="baseline"><form id="form1" name="form1" method="post" action="">
      <label class="field">Task: </label><input name="taskString" type="text" size="30" />
      <label class="field">Query: </label><input name="query" type="text" size="30" />
      <br />
      <input type="submit" name="Submit" value="Cerca" />
    </form>
    </td>
  </tr>
</table>
<table width="100%" height="10">
  <tr>
  <%
  
    	if( request.getQueryString()==null || (request.getQueryString()!=null && TaskBean.getArg("frame", request.getQueryString()).equals("1")) ) {
    		out.println(" <td width=\"20%\" valign=\"top\"> "+
    				"<div class=\"dtree\">"+
    		"<p><a href=\"javascript: d.openAll();\">Espandi</a> | <a href=\"javascript: d.closeAll();\">Chiudi</a></p>"+
    		"<script type=\"text/javascript\">"+
    		""+
    		"d = new dTree('d');"+
    		"d.add(0,-1,'I risultati divisi per siti');");

		int padre = 0;
		int indexof = 0;
		//if( (request.getQueryString()!=null && request.getQueryString().substring(query.lastIndexOf("frame=")+6).equals("1")) ) {
			Vector<Document> docs = TaskBean.getTree();
			if(docs!=null) {
				out.println(docs.size());
				for(int i = 0; i<docs.size(); i++) {
					if(((i+1)<docs.size()) && (docs.get(i)==null)) {
						String s = docs.get(i+1).get("url");
						if((indexof = s.indexOf("/",7))<0) {
							s = "unknown";
						}
						else {
							s = s.substring(7,indexof);
						}
					
						out.println("d.add("+(i+1)+",0,'Risultati da:  "+s+"','','','','img/globe.gif');");
						padre=i+1;
					}
					else if(docs.get(i)!=null){ 
						out.println("d.add("+(i+1)+","+padre+",'"+docs.get(i).get("url")+"','"+docs.get(i).get("url")+"');");
					} 
					
				}
			}
		//}

	//	if( (request.getQueryString()!=null && request.getQueryString().substring(query.lastIndexOf("frame=")+6).equals("1")) ) {
			out.println("	document.write(d); "+
					"</script>"+
					"</div>"+
					"</td>");
		//}
    	}
		%>

	<td>
		<br>
			<table width="100%" height="10" bgcolor="#ABC8FB">
			  <tr>
			    <td>
			    <%
					int padre1 = 0;
					int indexof1 = 0;
					//int start = 0;
					//int end = TaskBean.getResults().size();
					//if(TaskBean.getArg("start", request.getQueryString())!=null && TaskBean.getArg("end", request.getQueryString())!=null ) {
					//	start = Integer.parseInt(TaskBean.getArg("start", request.getQueryString()));
					//	end = Integer.parseInt(TaskBean.getArg("end", request.getQueryString()));
					//}
					Vector<Document> res;
					if(request.getQueryString()==null)
						res = TaskBean.getResults("0");
					else
						res = TaskBean.getResults(TaskBean.getArg("index", request.getQueryString()));
						for(int i = 0; i<res.size();i++){
				%>
				<!--Risultato-->
				<a href="<%=res.get(i).get("url")%>" class="titolo"><%=res.get(i).get("title")%></a><br />
			      <span class="description"><%=res.get(i).get("description")%></span><br />
			      <span class="link"><%=res.get(i).get("url")%></span> <br />
				  <hr color="#999999" size="1" width="80%" align="left" />
				  <!--fine risultato-->
				 <%
				 }
				 %> 
			    </td>
			  </tr>
			  			  <tr>
			  	<td>
			  	<div align="center" class="description">
			  	<%
			  	if(request.getQueryString()!=null && TaskBean.getArg("index", request.getQueryString())!=null)
			  		out.println(TaskBean.printTail(TaskBean.getArg("index", request.getQueryString())));
			  	else
			  		out.println(TaskBean.printTail(null));
			  	%>
			  	</div>
			  	</td>
			  </tr>
			</table>
		<br/>
	</td>
  </tr>

</table>
<div align="center" class="field">Number of matches: <%=TaskBean.getTotal()%></div>
<div align="center" class="footer">TaskSpider - MGRI 2007/2008 - Notargiacomo Simone & Schipani Giuseppe </div>
</body>
</html>
