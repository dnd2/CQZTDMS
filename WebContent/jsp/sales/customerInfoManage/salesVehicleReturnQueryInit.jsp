<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function download(){
		var fsm = document.getElementById('fm');
		fsm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesVehicleReturn/downloadBySVRQ.json';
		fsm.target="_self";
		fsm.submit();
	}
	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/queryReturnReqList_DLR.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "车系代码", dataIndex: 'SERIES_CODE', class:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', class:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', class:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', class:'center'},
				{header: "VIN", dataIndex: 'VIN', class:'center'},
				{header: "实销时间", dataIndex: 'SALES_DATE', class:'center'},
//				{header: "审核时间", dataIndex: 'CHK_DATE', class:'center'},
//				{header: "申请时间", dataIndex: 'REQ_DATE', class:'center'},
				{header: "申请时间", dataIndex: 'REQ_DATE', class:'center'},
				{header: "退车原因", dataIndex: 'RETURN_REASON', class:'center'},
//				{header: "审核人", dataIndex: 'NAME', class:'center'},
				{header: "审核状态", dataIndex: 'AUDIT_STATUS', class:'center',renderer:getItemValue},
                {header: "审核记录", dataIndex: 'RETURN_ID', class:'center',renderer:myLink}
		      ];
function myLink(value){
   return String.format("<a href='#' class='u-anchor' onclick='openCheckDesc("+value+")'>查看</a>");
}
function openCheckDesc(value){
        OpenHtmlWindow("<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/openCheckDesc.do?returnId="+value,600,400);
}
</script>
<title>实销退车申请查询</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销退车申请查询</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" id="dlrId" name="dlrId" value="" />
<div class="form-panel">
<h2>实销退车申请查询</h2>
<div class="form-body">
	<table class="table_query">
		<tr>
			<td width="20%" class="tblopt right">
				VIN：
			</td>
			<td width="39%"><textarea name="vin" cols="18" rows="3" class="form-control" style="width:176px; height:50px"></textarea></td>
			<td  class="right">申请日期：</td>
	      	<td  class="left">
	      		<div class="left">
	           		<input name="reqStartDate" id="t5" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t5'), maxDate:'#F{$dp.$D(\'t6\')}'})"  style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't5', false);" value="&nbsp;" /> -->
	           		&nbsp;至&nbsp;
	           		<input name="reqEndDate" id="t6" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t6'), minDate:'#F{$dp.$D(\'t5\')}'})" style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't6', false);" value="&nbsp;" /> -->
	          		</div>
	      	</td>
		</tr>
		
		<tr>
			<td class="right">审核日期：</td>
			<td class="left">
				<div class="left">
	           		<input name="checkstartDate" id="t3" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t3'), maxDate:'#F{$dp.$D(\'t4\')}'})"  style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't3', false);" value="&nbsp;" /> -->
	           		&nbsp;至&nbsp;
	           		<input name="checkendDate" id="t4" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t4'), minDate:'#F{$dp.$D(\'t3\')}'})" style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't4', false);" value="&nbsp;" /> -->
	          		</div>
	        </td>
	        <td  class="right">实销日期：</td>
	      	<td  class="left">
	      		<div class="left">
	           		<input name="salstartDate" id="t1" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" /> -->
	           		&nbsp;至&nbsp;
	           		<input name="salendDate" id="t2" value="" type="text" class="short_txt" 
	           			onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
	           		<!-- <input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" /> -->
	          		</div>
	      	</td>
		</tr>
		<tr>
			<td colspan="4" class="center">
				<input type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"  class="u-button u-submit" onclick="download()" value="下 载" id="downloadBtn" />
			</td>
		</tr>
	</table>
</div>
</div>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /></div>
</body>
</html>