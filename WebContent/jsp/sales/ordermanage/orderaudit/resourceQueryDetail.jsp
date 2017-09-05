<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可利用资源查询</title>
</head>
<body> 
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  销售订单管理  &gt;订单审核 &gt; 可利用资源明细查询</div>
	<table class="table_list">
		<tr>
			<th>经销代码</th>
			<th>经销商名称</th>
			<th>物料代码</th>
			<th>物料名称</th>
			<th>单据类型</th>
			<th>发运申请单号</th>
			<th>资源锁定数量</th>
		</tr>
			<c:forEach items="${list}" var="list">
			<tr class="table_list_row2">
				<td>${list.DEALER_CODE}</td>
				<td>${list.DEALER_SHORTNAME}</td>
				<td>${list.MATERIAL_CODE}</td>
				<td>${list.MATERIAL_NAME}</td>
				<td>${list.TYPES}</td>
				<td>${list.DLVRY_REQ_NO}</td>
				<td>${list.AMOUNT}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4"><input type="button" class="cssbutton" name="button" value="关闭" onclick="_hide();"/></td>
		</tr>
		
	</table>
</div>
</body>
</html>