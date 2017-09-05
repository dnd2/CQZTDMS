<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：经销商实销管理 > 经销商库存管理 > 经销商地址更改审核 > 经销商审核通过地址汇总</div>
<form method="POST" name="fm" id="fm">
<input type=hidden name="dealerId" id="dealerId" value="${dealerId }"/>
<input type=hidden name="areaId" id="areaId" value="${areaId }"/>
<table class="table_query">
	<thead>
		<tr>
			<td>经销商代码:</td>
			<td>${dealerCode }</td>
			<td>经销商名称:</td>
			<td>${dealerShortName }</td>
		</tr>
	</thead>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/DealerAddressCheck/queryPassAddressByDealerId.json?COMMAND=1&dealerId=${dealerId}";
	var title = null;
	var columns = [
		{header: "序号", align:'center', renderer:getIndex,width:'7%'},
		{header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
		{header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
		{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
		{header: "电话", dataIndex: 'TEL', align:'center'},
		{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
		{header: "地级市", dataIndex: 'CITY_NAME', align:'center'},
		{header: "县", dataIndex: 'AREA_NAME', align:'center'},
		{header: "备注", dataIndex: 'REMARK', align:'center'}
			      ];
</script>
</body>
</html>
