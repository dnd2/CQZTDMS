<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆维修历史</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理 &gt; 公共信息管理 &gt;客户信息查询</div>
<form method="post" name='fm' id='fm'>

	<%if(((java.util.List)request.getAttribute("parts")).size()>0){%>
		<table class="table_list">
			<tr class="table_list_row2">
				<td>序号</td>
				<td>配件代码</td>
				<td>配件名称</td>
				<td>单位</td>
				<td>数量</td>
				<td>收费区分</td>
			</tr>
			<c:forEach var="part" items="${parts}" varStatus="st">
				<tr class="table_list_row${st.index%2+1}">
					<td>${st.index+1}</td>
					<td>${part.partNo}</td>
					<td>${part.partName}</td>
					<td>${part.unitCode}</td>
					<td>${part.partQuantity}</td>
					<td>${part.chargePartitionCode}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
	<%}%>
	
	<%if(((java.util.List)request.getAttribute("items")).size()>0){%>
		<table class="table_list">
			<tr class="table_list_row2">
				<td>序号</td>
				<td>工时名称</td>
				<td>问题描述</td>
				<td>问题原因</td>
				<td>收费区分</td>
			</tr>
			<c:forEach var="item" items="${items}" varStatus="st">
				<tr class="table_list_row${st.index%2+1}">
					<td>${st.index+1}</td>
					<td>${item.labourName}</td>
					<td>${item.troubleDesc}</td>
					<td>${item.troubleReason}</td>
					<td>${item.chargeMode}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
	<%}%>
	
	<%if(((java.util.List)request.getAttribute("addItems")).size()>0){%>
		<table class="table_list">
			<tr class="table_list_row2">
				<td>序号</td>
				<td>附加项目代码</td>
				<td>附加项目名称</td>
				<td>活动编号</td>
				<td>附加项目费用</td>
				<td>收费区分</td>
				<td>备注</td>
			</tr>
			<c:forEach var="item" items="${items}" varStatus="st">
				<tr class="table_list_row${st.index%2+1}">
					<td>${st.index+1}</td>
					<td>${item.addItemCode}</td>
					<td>${item.addItemName}</td>
					<td>${item.activityCode}</td>
					<td>${item.addItemAmount}</td>
					<td>${item.chargePartitionCode}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
	<%}%>
	
	
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input class="normal_btn" type="button" value="返回" name="recommit" id="queryBtn" onclick="history.go(-1);" />
       		</td>
		</tr>
	</table>

</form>
</body>
</html>