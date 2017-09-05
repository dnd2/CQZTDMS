
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

Real Path: <%=application.getRealPath(request.getRequestURI())%>
<%=new File(".").getAbsolutePath()%>
<%="now is"+new Date()%>


