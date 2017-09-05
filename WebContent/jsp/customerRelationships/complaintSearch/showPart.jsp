<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉明细</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理&gt;客户投诉管理&gt;客户投诉明细&gt;联系结果描述</div>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />备件信息</th>
     <tr>
	    <td class="table_query_2Col_label_5Letter">备件编码：</td>
	    <td><c:out value="${partCode}"/></td>
	    <td class="table_query_2Col_label_6Letter">上级保供单位：</td>
	    <td><c:out value="${supplier}"/></td>
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
