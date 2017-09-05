<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="login-html">
<head>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/vnd.microsoft.icon">
<link rel="icon" href="<%=request.getContextPath()%>/img/favicon.ico"  type="image/vnd.microsoft.icon">
<%
String path = request.getContextPath();
String errorMessage = (String)request.getAttribute("ERROR_MESSAGE");
%>
<title>君马新能源DCS系统</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script>
$(document).ready(
		function(){
			MyAlert("访问权限受限", toRefresh) ;
		}
);

function toRefresh() {
	top.document.location.reload(true) ;
}
</script>
</head>
<h2>访问权限受限</h2>
<body>
</body>
</html>