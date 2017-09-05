<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉明细</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理&gt;客户投

诉管理&gt;客户投诉明细&gt;联系结果描述</div>
<table width="80%" align="center">
	<tr>
		<td align="center"><textarea cols="40" rows="6" disabled>${msg}</textarea></td>
	</tr>
</table>
<br />
<table class="table_edit">
	<tr>
		<td align="center">
			<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
		</td>
	</tr>
</table>
</body>
</html>
