<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="com.jatools.server.*" %>
<%@ page import="com.jatools.core.JobCacher" %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印...</title>
<script LANGUAGE="JavaScript"><!-- 
function checkStatus() {
      if(printer.isPrinted())
	     command.value = "确定";
      else
	     command.value = "取消";
	  setTimeout("checkStatus()",1000);
} 
function closePrinter() {
      printer.cancel();
	  window.close();
} 

--></script>
</head>
<%
            String call_cache = request.getParameter("call_cache");
            String fl = ReportWriter.getJgoFiles(request,JobCacher.callJob(request.getSession(),call_cache));
            String root_url = request.getRequestURL().toString();
            root_url = root_url.substring(0, root_url.lastIndexOf("/") + 1)+"temp/";
%>
   <BODY onload="checkStatus()" bgcolor=#CCCCCC>

   <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="116">
     <tr>
       <td width="100%" height="75">
       <p align="center">

<applet code="com.jatools.viewer.ZPrinter.class" name="printer" width="200" height="75" align="baseline" archive= "printer.jar" id="printer">
    <param name="root" value="<%=root_url%>">
    <param name="files" value="<%=fl%>">
</applet></td>
     </tr>
     <tr>
       <td width="100%" height="41">
       <p align="center">
<input name="command" type="submit" onclick="closePrinter()" id="command" value="取消"></td>
     </tr>
</table>
   </BODY>
   </HTML>