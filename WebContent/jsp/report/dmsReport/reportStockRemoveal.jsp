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
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportStockRemoveal.do";
		fm.submit();
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;出库查询
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	
	
	<tr>
		
			<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >部件厂代码：</td>
				<td width="15%"><input name="FaCode" class="middle_txt" id="FaCode"> </td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">部件厂简称：</td>
				<td width="15%"><input name="FaShortName" class="middle_txt" id="FaShortName"> </td>
			<td width="35%"></td>
	</tr>
	
	<tr>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true" >零部件代码：</td>
		<td width="15%"><input name="partCode" class="middle_txt" id="partCode"> </td>
		
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">零部件名称：</td>
		<td width="15%"><input name="partName" class="middle_txt" id="partName"> </td>
		<td width="35%"></td>
	</tr>
	
	<tr>
		
				<td width="10%"  class="table_query_2Col_label_8Letter"  nowrap="true" >退订单号：</td>
				<td width="15%"><input name="rangeNo" class="middle_txt" id="rangeNo"> </td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">索赔单号：</td>
				<td width="15%"><input name="claimNo" class="middle_txt"  id="claimNo"> </td>
			<td width="35%"></td>
	</tr>
	
	<tr>
				<td width="10%"  class="table_query_2Col_label_8Letter"  nowrap="true" >出库时间起：</td>
				<td width="15%"> 
					<input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/>
				</td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">出库时间止：</td>
				<td width="15%">
					<input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/>
				 </td>
			<td width="35%"></td>
	</tr>
	
	<tr>
				<td width="10%"  class="table_query_2Col_label_8Letter"  nowrap="true" >出库类型：</td>
				<td width="15%"> 
					<script type="text/javascript">
		              genSelBoxExp("out_type",<%=Constant.OUT_CLAIM_TYPE%>,"",true,"short_sel","","false",'');
		            </script>
				</td>
				
				<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">是否切换件</td>
				<td width="15%">
					<select id="is_qhj" name="is_qhj">
						<option value="">--请选择--</option>
						<option value="10561005">是</option>
						<option value="">否</option>
					</select>
				 </td>
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
	
	function doInit()
	{
		__extQuery__(1);
	}   
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryStockRemoveal.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
		{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
		{header: "退赔单号", dataIndex: 'RANGE_NO', align:'center'},
		{header: "零件号", dataIndex: 'OUT_PART_CODE', align:'center'},
		{header: "零件名称",dataIndex: 'OUT_PART_NAME',align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "车架号", dataIndex: 'VIN', align:'center'},
		{header: "数量", dataIndex: 'OUT_AMOUT', align:'center'},
		{header: "切换件", dataIndex: 'IS_QHJ', align:'center'},
		{header: "出库时间", dataIndex: 'OUT_DATE', align:'center'}
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