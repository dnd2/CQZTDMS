<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>失败原因信息</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;失败原因信息</div>
<form method="post" name="fm" id="fm">
	<table class="table_query">
		<tr>
			<td align="left" width="20%">发运单号：</td>
			<td align="left" width="80%">${deliverNo}</td>
		</tr>
	</table>
	<table class="table_query">
	<tr>
	<td align="left" width="20%">原因说明</td>
	</tr>
	<c:forEach  items="${resonsMap}" var="po">
		<tr>
			<td align="left" width="80%"><c:out value="${po.LOSE_REASON}"/></td>
		</tr>
	</c:forEach>
		<tr>
			<td></td>
			<td align="left"><input type="button" class="cssbutton" name="button" value="关闭" onclick="_hide();"/></td>
		</tr>
	</table>
</form>
<script type="text/javascript">
</script>
</body>
</html>