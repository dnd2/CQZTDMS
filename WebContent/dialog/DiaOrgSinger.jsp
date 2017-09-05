<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	<frameset id="orgFrame" cols="*"  framespacing="3" frameborder="1" borderCOLOR="#3A5B81" border=0>
		<frame name="inIframe" frameborder="0" src="<%=contextPath %>/common/OrgMng/queryOrg.do" scrolling="auto" marginwidth="0" marginheight="0">
	</frameset>
<body onunload='javascript:destoryPrototype()' >
</body>
</html>