<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;紧急调件调出调入查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
  		
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">零部件代码：</td>
		<td width="10%"><input name="partCode" class="middle_txt" id="partCode"> </td>
				
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">零部件名称：</td>
		<td width="10%"><input name="partName" class="middle_txt" id="partName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">经销商代码：</td>
		<td width="10%"><input name="dealerCode" class="middle_txt" id="dealerCode"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >经销商名称：</td>
		<td width="10%"><input name="dealerName" class="middle_txt" id="dealerName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >供应商代码：</td>
		<td width="10%"><input name="supplyCode" class="middle_txt" id="supplyCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" >供应商名称：</td>
		<td width="10%"><input name="supplyName" class="middle_txt" id="supplyName"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >索赔单号：</td>
		<td width="10%"><input name="claimNo" class="middle_txt" id="claimNo"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >VIN：</td>
		<td width="10%"><input name="VIN" class="middle_txt" id="VIN"> </td>
		 <td align="right" nowrap="nowrap" >借出时间： </td>
         <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
	<tr>
		 <td align="right" nowrap="nowrap" >归还时间： </td>
         <td align="left" nowrap="true">
			<input name="back_start_date" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="back_end_date" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="80%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
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
	var url = "<%=contextPath%>/claim/oldPart/EmergencyDevice/listShowAllData.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:mylink},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
		{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
		{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
		{header: "库存", dataIndex: 'ALL_AMOUNT', align:'center'},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
		{header: "借出操作人", dataIndex: 'CREATE_PERSON', align:'center'},
		{header: "借件人", dataIndex: 'BORROW_MAN', align:'center'},
		{header: "借出时间", dataIndex: 'CREATE_DATE', align:'center'},
		{header: "归还时间", dataIndex: 'NEXT_DATE', align:'center'},
		{header: "归还操作人", dataIndex: 'NEXT_PERSON', align:'center'},
		{header: "归件人", dataIndex: 'RETURN_MAN', align:'center'}
	];
	function mylink(value,meta,record){
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var roNo = record.data.RO_NO;
		var ID = record.data.ID;
		var claimNo = record.data.CLAIM_NO;
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
	}s
	function wrapOut(){
		$("dealer_id").value="";
		$("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>