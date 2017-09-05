<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>财务开票导入</title>
</head>
<body>

<form  name="fm" id="fm">
<div style="margin-left: auto; margin-right: auto; width: 100%;">

<table class="table_query" align="center" border="0">
	<thead>
	<tr>
		<td>${message }</td>
	</tr>
	</thead>
</table>
<table class="table_query" align="center" border="0"  id="roll">
	<tr align="center" >
		<th>
			<div align="center">
				<input class="normal_btn" type='button' name='saveResButton' onclick='_hide()' value='确  定' />
			</div>
		</th>
  	</tr>
</table>
</div>
</form>
</body>
</html>
