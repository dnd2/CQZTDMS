<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	<frameset id="orgFrame" cols="170,*"  framespacing="3" frameborder="1" borderCOLOR="#3A5B81" border=0>
		<frame name="leftFrame" frameborder="0" src="<%=contextPath %>/common/OrgMng/queryOrgs.do" scrolling="auto" marginwidth="0" marginheight="0">
		<frame id="mainFrame" name="mainFrame" src="<%=contextPath %>/dialog/DiaOrgRight.jsp" frameborder=0  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" style="margin-right: 0px;margin-bottom: 0px;">		
	</frameset>
<body onunload='javascript:destoryPrototype()' >
</body>
</html>