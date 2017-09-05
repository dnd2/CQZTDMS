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
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportReportMonthUgencyParts.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;月紧急调件汇总报表
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
  	<tr>
  		
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">经销商代码：</td>
		<td width="10%"><input name="dealerCode" class="middle_txt" id="partCode"> </td>
				
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">故障件代码：</td>
		<td width="10%"><input name="partCode" class="middle_txt" id="partName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">下发时间：</td>
		 <td align="left" nowrap="true">
			<input name="publishStartDate" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="publishEndDate" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >经销商名称：</td>
		<td width="10%"><input name="dealerName" class="middle_txt" id="dealerName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >故障件名称：</td>
		<td width="10%"><input name="partName" class="middle_txt" id="supplyCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" >发运时间：</td>
		<td width="10%"> 
			<input name="transStartDate" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="transEndDate" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" >索赔单号：</td>
		<td width="10%"><input name="claimNo" class="middle_txt" id="claimNo"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >延误返件天数>=：</td>
		<td width="10%"><input name="delayDay" class="middle_txt" id="delayDay"> </td>
		 <td align="right" nowrap="nowrap" >入库时间： </td>
         <td align="left" nowrap="true">
			<input name="inStartDate" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="inEndDate" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
	
	
	 <tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" >车辆VIN号：</td>
		<td width="10%"><input name="VIN" class="middle_txt" id="VIN"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >调件情况：</td>
		<td width="10%">
		<select class="short_sel" name="transSituation">
			<option value=""            >=请选择= </option>
			<option value="调件未下发">调件未下发 </option>
			<option value="件未回运">件未回运 </option>
			<option value="货运在途">货运在途 </option>
			<option value="已签收未审核">已签收未审核 </option>
			<option value="已扣件">已扣件 </option>
			<option value="件已入库">件已入库 </option>
			<option value="件已借出">件已借出 </option>
			<option value="件已归还">件已归还 </option>
		</select>
		</td>
		
		 <td align="right" nowrap="nowrap" >借出时间： </td>
         <td align="left" nowrap="true">
			<input name="borrowStartDate" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="borrowEndDate" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
	
	
	 <tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" >货运单号：</td>
		<td width="10%"><input name="tranNo" class="middle_txt" id="claimNo"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >下发人</td>
		<td width="10%"><input name="issued_person" class="middle_txt" id="issued_person"> </td>
		 <td align="right" nowrap="nowrap" >归还时间： </td>
         <td align="left" nowrap="true">
			<input name="returnStartDate" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="returnEndDate" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
	
	 <tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" >是否拒赔：</td>
		<td width="10%">
			<select class="short_sel" name="isDis" id="isDis">
				<option value="" selected="selected">是</option>
				<option value="18041003">否</option>
			</select>
		</td>
		<td width="10%"  class="table_query_2Col_label_8Letter"></td>
		<td width="10%">
		</td>
		 <td align="right" nowrap="nowrap" ></td>
         <td align="left" nowrap="true">
		</td>	
		<td width="20%"></td>
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
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryReportMonthUgencyParts.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "车辆VIN号", dataIndex: 'VIN', align:'center'},
		{header: "故障件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "故障件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "下发人", dataIndex: 'ISSUED_PERSON', align:'center'},
		{header: "下发时间", dataIndex: 'ISSUED_DATE', align:'center'},
		{header: "要求启运时间", dataIndex: 'REQUIRE_SEND_DATE', align:'center'},
		{header: "要求到货时间", dataIndex: 'REQUIRE_ARRIVED_DATE', align:'center'},
		{header: "延误返件天数", dataIndex: 'DELAY_RETURN_DAYS', align:'center'},
		{header: "调件情况", dataIndex: 'STATUS', align:'center'},
		{header: "调件原因", dataIndex: 'BORROW_REASON', align:'center'},
		{header: "服务商返回时间", dataIndex: 'SEND_DATE', align:'center'},
		{header: "货运单号", dataIndex: 'TRANS_NO', align:'center'},
		{header: "货运公司", dataIndex: 'TRANS_COMP', align:'center'},
		{header: "到库时间", dataIndex: 'IN_DATE', align:'center'},
		{header: "借件人", dataIndex:     'BORROW_MAN', align:'center',renderer:formatDate1},
		{header: "借件时间", dataIndex: 'BORROW_DATE', align:'center'},
		{header: "借件确认人", dataIndex: 'BORROW_PERSON', align:'center'},
		{header: "还件人", dataIndex:     'RETURN_MAN', align:'center',renderer:formatDate1},
		{header: "还件时间", dataIndex: 'RETURN_DATE', align:'center'},
		{header: "还件确认人", dataIndex: 'RETURN_PERSION', align:'center'}
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