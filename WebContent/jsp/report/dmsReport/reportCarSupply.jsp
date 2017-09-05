<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportCarSupply.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;零部件车申报汇总报表
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >零部件代码：</td>
		<td width="15%"><input name="partCode" class="middle_txt" id="partCode"> </td>
		
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">零部件名称：</td>
		<td width="15%"><input name="partName" class="middle_txt" id="partName"> </td>
	</tr>
	<tr>
		
				<td width="10%"  class="table_query_2Col_label_8Letter"  nowrap="true" >开始时间：</td>
				<td width="15%"><input name="startDate" onclick="calendar();" readonly="readonly" class="middle_txt" id="startDate"> </td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">结束时间：</td>
				<td width="15%"><input name="endDate"  onclick="calendar();" readonly="readonly" class="middle_txt" id="endDate"> </td>
			<td width="35%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum(1)" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
  
    
    
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	function checkSum(){
			__extQuery__(1);
		
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryCarSupply.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "零部件代码", dataIndex: 'FIRST_PROBLEM_CODE', align:'center'},
		{header: "零部件名称", dataIndex: 'FIRST_PROBLEM_NAME', align:'center'},
		{header: "部件厂名称", dataIndex: 'PART_CNAME', align:'center'},
		{header: "部件厂代码", dataIndex: 'MAKER_CODE', align:'center'},
		{header: "日期", dataIndex:     'REPORT_DATE_BTN', align:'center',renderer:formatDate1},
		{header: "更换频率", dataIndex: 'CHANGENUM', align:'center'}
	];

	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
</script>
<!--页面列表 end -->
</html>