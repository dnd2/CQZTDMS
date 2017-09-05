<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportRefuseQuestions.do";
		fm.submit();
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;服务站拒赔问题汇总
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	<tr>
		<td width="6%"  class="table_query_2Col_label_4Letter"  nowrap="true" >入库时间：</td>
		<td width="10%"><input name="startDate" onclick="calendar();" readonly="readonly" class="middle_txt" id="date"></td>
		<td width="2%"  class="table_query_2Col_label_1Letter"  nowrap="true"  align="left">到：</td>
		<td width="15%"> <input name="endDate" onclick="calendar();" readonly="readonly" class="middle_txt" id="date"></td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >服务站编号：</td>
		<td width="15%"><input name="FaCode" class="middle_txt" id="FaCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">服务站名称：</td>
		<td width="15%"><input name="FaShortName" class="middle_txt" id="FaShortName"> </td>
		
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
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryRefuseQuestions.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "服务站号", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "服务站简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "货运票号", dataIndex: 'TRANSPORT_NO', align:'center'},
		{header: "到货方式", dataIndex: 'TRAN_PERSON', align:'center'},
		{header: "总箱数", dataIndex: 'REAL_BOX_NO', align:'center'},
		{header: "拒赔数量", dataIndex: 'RETURN_AMOUNT', align:'center'},
		{header: "鉴定员", dataIndex: 'IN_WARHOUSE_BY', align:'center'},
		{header: "旧件月份", dataIndex: 'RETURN_CYCLE', align:'center'},
		{header: "入库编号", dataIndex: 'IN_NO', align:'center'},
		{header: "存在问题", dataIndex: 'DEDUCT_REMARK', align:'center'},
		{header: "验收评价", dataIndex: 'EVALUATION', align:'center'}
	];

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