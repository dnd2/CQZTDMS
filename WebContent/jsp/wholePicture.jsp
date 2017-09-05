<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%
	String url = request.getParameter("url");
%>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body onunload='javascript:destoryPrototype()' onunload="returnVal()">
<form action="">
<img src="<%=url %>" width="800" height="600">
</form>
</body>
<script type="text/javascript">
</script>
</html>