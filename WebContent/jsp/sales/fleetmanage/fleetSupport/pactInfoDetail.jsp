<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>批售项目信息</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;批售项目信息</div>
<form method="post" name = "fm" >
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 批售项目信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">项目代码：</td>
			<td><c:out value="${fleetMap.PACT_NO}"/></td>
			<td class="table_query_2Col_label_4Letter">项目名称：</td>
			<td><c:out value="${fleetMap.PACT_NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">备注：</td>
			<td colspan="3"><c:out value="${fleetMap.REMART}"/></td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>
				<input type="button" value="关闭" name="backBtn" class="cssbutton" onclick="closeWindow()"/>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function closeWindow()
	{
		_hide();
	}
</script>
</body>
</html>
