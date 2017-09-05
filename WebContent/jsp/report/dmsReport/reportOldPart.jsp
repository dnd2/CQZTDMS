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
		if($("date").value==""){
			MyAlert("请选择年月");
		}else{
			fm.action="<%=contextPath%>/report/dmsReport/Application/expotReportOldPart.do";
			fm.submit();
		}
		
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;索赔结算清单
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	<tr>
		<td width="10%"  class="table_query_2Col_label_8Letter"  nowrap="true" >选择年月：</td>
		<td width="15%"><input name="date" onclick="calendar();" readonly="readonly" class="middle_txt" id="date"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >部件厂代码：</td>
				<td width="15%"><input name="FaCode" class="middle_txt" id="FaCode"> </td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">部件厂简称：</td>
				<td width="15%"><input name="FaShortName" class="middle_txt" id="FaShortName"> </td>
		
	</tr>
	<tr>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >旧件编码：</td>
				<td width="15%"><input name="detailCode" class="middle_txt" id="detailCode"> </td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">旧件名称：</td>
				<td width="15%"><input name="detailName" class="middle_txt" id="detailName"> </td>
		<td width="15%"></td>
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
		if($("date").value==""){
			MyAlert("请选择年月");
		}else{
			__extQuery__(1);
		}
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryReportOldPart.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "部件厂代码", dataIndex: 'SUPPLY_CODE', align:'center'},
		{header: "部件厂名称", dataIndex: 'SUPPLY_NAME', align:'center'},
		{header: "零部件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "零部件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "期初结存", dataIndex: 'QC_AMOUNT', align:'center'},
		{header: "本期收入", dataIndex: 'IN_AMOUNT', align:'center'},
		{header: "本期发出", dataIndex: 'OUT_AMOUNT', align:'center'},
		{header: "本期结存", dataIndex: 'QM_AMOUNT', align:'center'},
		{header: "备注", dataIndex: 'REMARK', align:'center'}
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