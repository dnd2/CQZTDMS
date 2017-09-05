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
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportCarFix.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;车辆维修履历报表
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	<tr>
		
				<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="15%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">服务商简称：</td>
				<td width="15%"><input name="FaShortName" class="middle_txt" id="FaShortName"> </td>
				<td width="45%"></td>
		</tr>
		<tr>
				<td width="10%"  class="table_query_2Col_label_3Letter" nowrap="true">VIN：</td>
				<td width="15%"><input name="VIN" class="middle_txt" id="VIN"> </td>
				
				<td align="right" nowrap="true">开始时间：</td>
				<td align="left" nowrap="true" width="25%">
				  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10" readonly="readonly"
		                 maxlength="10" group="startDate,endDate" onclick="calendar();"/>
		             	 结束时间
		          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
		                 maxlength="10" group="startDate,endDate"/>
				</td>
				<td width="45%"></td>
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
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryCarFix.json";
	var title = null;
	var columns = [

		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "服务商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
		{header: "工单开据日期", dataIndex: 'RO_CREATE_DATE', align:'center',renderer:formatDate1},
		{header: "工单结算日期", dataIndex: 'FOR_BALANCE_TIME', align:'center',renderer:formatDate1},
		{header: "工单类型", dataIndex: 'REPAIR_TYPE_CODE', align:'center'}
	];
	function getYear(value,meta,record){
		return value.substring(0,4);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
	}

	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
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