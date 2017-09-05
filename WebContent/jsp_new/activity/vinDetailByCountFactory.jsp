<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/vinDetailByCountFactory.json?query=true";
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "是否维修", dataIndex: 'IS_REPAIR', align:'center'}
	];
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN明细展示
</div>
<form name="fm" id="fm">
<!-- 查询条件 end -->
  <input type="hidden" class="middle_txt" name="activity_code" id="activity_code" value="${activity_code }" maxlength="30"/>
  <input type="hidden" class="middle_txt" name="dealer_code" id="dealer_code" value="${dealer_code }" maxlength="30"/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>