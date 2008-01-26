<%@ page import = "java.util.*" %>
<jsp:useBean id="taskBean" scope="request" class="spider.TaskBean"/>
<jsp:setProperty name="taskBean" property="*" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Untitled Document</title>
<link rel="StyleSheet" href="dtree.css" type="text/css" />
	<script type="text/javascript" src="dtree.js"></script>
</head>

<body>

<div class="dtree">

	<p><a href="javascript: d.openAll();">open all</a> | <a href="javascript: d.closeAll();">close all</a></p>

	<script type="text/javascript">
		<!--

		d = new dTree('d');
		d.add(0,-1,'I risultati divisi per siti');
		<%
		int padre = 0;
			for(int i = 0; i<taskBean.getResults().size();i++){
				if(taskBean.getResults().get(i)=="*"){
				 out.println("d.add("+(i+1)+",0,'Sito "+padre+"', '#');");
				 padre++;
				}else{
				 out.println("d.add("+(i+1)+","+padre+",'"+taskBean.getResults().get(i)+"','"+taskBean.getResults().get(i)+"');");
				} 
				
			}
		%>

		document.write(d);

		//-->
	</script>

</div>

</body>
</html>
