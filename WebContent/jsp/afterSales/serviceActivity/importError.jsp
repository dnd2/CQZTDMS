<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>导入错误</title>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;导入错误信息页面
	</div>
	<table class="table_query" width="85%" align="center" border="0">
		<tr>
			<th>
				<div align="center">行号</div>
			</th>
			<th>
				<div align="center">vin</div>
			</th>
			<th>
				<div align="center">错误信息</div>
			</th>
		</tr>
		<c:forEach items="${errorList}" var="errorList">
			<tr class="table_list_row1">
				<td>
					<div align="center">${errorList.num }</div>
				</td>
				<td>
					<div align="center">${errorList.vin }</div>
				</td>
				<td>
					<div align="center">${errorList.msg }</div>
				</td>
			</tr>
		</c:forEach>
		<tr>
				<td align="center" colspan="4">
					<input class="normal_btn" type='button' onclick="history.back();" value='返 回' />
				</td>
		</tr>
	</table>
</body>
</html>
