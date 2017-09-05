<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
   String userName = (String)request.getAttribute("backUser");
   String backUrl = (String)request.getAttribute("backUrl");
%>

<title>Insert title here</title>
</head>
<body>
   <form name="fm" action="<%=backUrl %>" method="post">
   	  <input type=hidden name="userName" value="<%=userName%>" />

   </form>
</body>
<script>
fm.submit();
</script>
</html>