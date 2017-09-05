<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>库存查询</title>
<script type="text/javascript">
var myPage;
var title = null;
var  url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/oemReturnQuery.json";
var  columns = [
			{header: "退车号", dataIndex: 'RETURN_VEHICLE_NO', align:'center'},
			{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
			//{header: "资金类型", dataIndex: 'ACCOUNT_TYPE_NAME', align:'center'},
			{header: "申请数量", dataIndex: 'APPLY_AMOUNT', align:'center'},
			{header: "已审核数量", dataIndex: 'COU', align:'center'},
			{header: "申请原因", dataIndex: 'REASON', align:'center'},
			{header: "申请日期", dataIndex: 'APPLAY_CREATE', align:'center'},
			/* {header: "审核描述", dataIndex: 'CHECK_REMARK', align:'center'}, */
			{header: "审核日期", dataIndex: 'CHECK_DATE', align:'center'},
			{header: "审核状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
			{header: "详细", dataIndex: 'HEAD_ID', align:'center',renderer:myOpera}
	      ];

function myOpera(value,metaDate,record) {
	return String.format("<a href=\"#\" onclick=\"operaIt(" + value + ");\">[明细]</a>") ;
}

function operaIt(value) {
	OpenHtmlWindow('<%=contextPath%>/sales/storageManage/ReturnVehicleReq/dtlInit.do?headId=' + value,850,500);
}

function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
function download(){
	var fm = document.getElementById('fm');
	fm.action='<%=request.getContextPath()%>/sales/storageManage/ReturnVehicleReq/download.json';
	fm.target="_self";
	fm.submit();
}
</script>
</head>
<body> 
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 经销商退车查询</div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="reason" id="reason"/>
	<div class="form-panel">
		<h2>经销商退车查询</h2>
		<div class="form-body">
			<table class="table_query">
				<tr>
					<td width="20%" class="tblopt right" >退车单号：</td>
					<td width="39%" >
	      				<input type="text" class="middle_txt" id="returnNo" name="returnNo" />
	    			</td>
	    			<td class="right">账户类型：</td>
					<td>
						<select name="accountType" class="u-select">
							<option value="">-请选择-</option>
							<c:forEach items="${typeList}" var="list">
									<option value="<c:out value="${list.TYPE_ID}"/>"><c:out value="${list.TYPE_NAME}"/></option>
							</c:forEach>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="right">申请日期：</td>
					<td>
						<!-- <input name="reqStartDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
		            		&nbsp;至&nbsp;
		            		<input name="reqEndDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" /> -->
						<input class="short_txt" readonly="readonly"  type="text" id="reqStartDate" name="reqStartDate"   
							onFocus="WdatePicker({el:$dp.$('reqStartDate'), maxDate:'#F{$dp.$D(\'reqEndDate\')}'})"  style="cursor: pointer;width: 80px;"/>
						&nbsp;至&nbsp;
						<input class="short_txt" readonly="readonly"  type="text" id="reqEndDate" name="reqEndDate"  
								onFocus="WdatePicker({el:$dp.$('reqEndDate'), minDate:'#F{$dp.$D(\'reqStartDate\')}'})"  style="cursor: pointer;width: 80px;"/>
					</td>
					<td class="right">审核状态：</td>
					<td>
						<script type="text/javascript">
	 				 		genSelBoxExp("status",<%=Constant.RETURN_CAR_STATUS%>,"",true,"","","false",'');
				  		 </script>
					</td>
				</tr>
				<tr>
					<td class="right">审核日期：</td>
					<td>
						<!-- <input name="checkstartDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't3', false);" />
	            		&nbsp;至&nbsp;
	            		<input name="checkendDate" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't4', false);" /> -->
						<input class="short_txt" readonly="readonly"  type="text" id="checkstartDate" name="checkstartDate"  
							onFocus="WdatePicker({el:$dp.$('checkstartDate'), maxDate:'#F{$dp.$D(\'checkendDate\')}'})"  style="cursor: pointer;width: 80px;"/>
						&nbsp;至&nbsp;
						<input class="short_txt" readonly="readonly"  type="text" id="checkendDate" name="checkendDate"  
							onFocus="WdatePicker({el:$dp.$('checkendDate'), minDate:'#F{$dp.$D(\'checkstartDate\')}'})"  style="cursor: pointer;width: 80px;"/>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4" class="center">
						<input type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" />
						<input type="hidden" name="status" id="status" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button"  class="u-button u-submit" onclick="download()" value="下 载" id="downloadBtn" />
					</td>
				</tr>
			</table>
		</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>  
</div> 
</body>
</html>