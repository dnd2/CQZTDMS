<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明细</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 明细</div>
<form method="POST" name="FRM" id="FRM">
<table class="table_edit">
<tr>
	<td align="right">
	<label>问题分类：</label>
	</td>
	<td align="left">
	<label id="questionClassFy">${qType }</label>
	</td>
</tr>
<tr>
	<td align="right">
	<label>问题ID：</label>
	</td>
	<td align="left">
	<label id="questionClassFy">${QID }</label>
	</td>
</tr>
<tr>
	<td align="right">
	<label>问题描述：</label>
	</td>
	<td align="left">
	<label id="questionDescrs">${qDesc }</label>
	</td>
</tr>
<tr>
	<td id="status" align="right" nowrap="nowrap">
		<label>内容回答：</label>
		</td>
		<td align="left" nowrap="nowrap">
		<label id="answerQuestion">${qAnswer }</label>
	</td>
</tr>
<c:if test="${lists != null}">
<tr>
	<td id="status" align="right" nowrap="nowrap">
		<label>附件：</label>
		</td>
		<td align="left" nowrap="nowrap">
		<label id="fj">
		    <c:forEach items="${lists}" var="item">  
		        <a href="${item.fileurl}">${item.filename} &nbsp;&nbsp;</a>
    		</c:forEach>  
		</label>
	</td>
</tr>
</c:if>
<tr>
	<td align="right">&nbsp;</td>
	<td align="left">
		<input type="button" name="rebackBtn" id="rebackBtn" onclick="javascript:window.close();"  class="cssbutton" value="关闭"/>
	</td>
</tr>
</table>
</form>
</body>
</html>