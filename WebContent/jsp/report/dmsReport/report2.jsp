<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后报表</title>
<script type="text/javascript">
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData2.do";
       fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;保养台次查询报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">单据类型：</td>
		<td width="15%">
			<select class="short_sel" name="claim_type">
				<option value="">--请选择--</option>
				<option value="10661011">PDI</option>
				<option value="10661002">强保定检</option>
			</select>
		</td>
		<td width="15%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum()" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
     <tr class="table_list_row1">
		<td >首保总金额：<span id="a1"></span></td>
		<td >PDI金额：<span id="a2"></span></td>
		<td ><font color="red">总金额：<span id="a3"></span></font></td>
		<td >商家数量：<span id="a4"></span></td>
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
	
	
	function checkSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		
		makeNomalFormCall("<%=contextPath%>/report/dmsReport/Application/report2Sum.json",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.FREE_M_PRICE == null ? '0' : json.valueMap.FREE_M_PRICE;//工时费
			document.getElementById("a2").innerHTML = json.valueMap.AMOUNT == null ? '0' : json.valueMap.AMOUNT;
			document.getElementById("a3").innerHTML = json.valueMap.AMOUNT+json.valueMap.FREE_M_PRICE;
			document.getElementById("a4").innerHTML = json.valueMap.DEALER_NUM == null?'0':json.valueMap.DEALER_NUM;
		},'fm');
		__extQuery__(1);
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/report2.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "车架号", dataIndex: 'VIN', align:'center'},
		{header: "单据类型", dataIndex: 'CLAIM_TYPE', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
		{header: "保养商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "用户姓名", dataIndex: 'NAME', align:'center'},
		{header: "开单时间", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "首保金额", dataIndex: 'FREE_M_PRICE', align:'center'},
		{header: "PDI金额", dataIndex: 'AMOUNT', align:'center'}
	];
	function myLink(value,meta,record){
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
	}
</script>
<!--页面列表 end -->
</html>