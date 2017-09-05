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
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function download(){
		var fsm = document.getElementById('fm');
		fsm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesVehicleReturn/download.json';
		fsm.target="_self";
		fsm.submit();
	}
	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/queryReturnCheckList.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "车系代码", dataIndex: 'SERIES_CODE', class:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', class:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', class:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', class:'center'},
				{header: "VIN", dataIndex: 'VIN', class:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', class:'center'},
				{header: "实销日期", dataIndex: 'SALES_DATE', class:'center'},
				{header: "审核日期", dataIndex: 'CHK_DATE', class:'center'},
				{header: "申请日期", dataIndex: 'REQ_DATE', class:'center'},
				{header: "退车原因", dataIndex: 'RETURN_REASON', class:'center'},
//				{header: "审核时间", dataIndex: 'CHK_DATE', class:'center'},
				{header: "审核状态", dataIndex: 'AUDIT_STATUS', class:'center',renderer:getItemValue},
				{header: "审核记录", dataIndex: 'RETURN_ID', class:'center',renderer:myLink}
		      ];

	//function getItemValue(value){
	//	var TO_OTD_AUDIT = <%= Constant.TO_OTD_AUDIT %>+"";
	//	var TO_SMALL_AUDIT = <%= Constant.TO_SMALL_AUDIT %>+"";
	//	var SMALL_AUDIT_THROUGH = <%= Constant.SMALL_AUDIT_THROUGH %>+"";
	//	var OTD_AUDIT_THROUGH = <%= Constant.OTD_AUDIT_THROUGH %>+"";
	//	var REGECT_STATUS = <%= Constant.REGECT_STATUS %>+"";
	//	var TO_SSEVICE_AUDIT = <%= Constant.TO_SSEVICE_AUDIT %>+"";
	//	if(TO_OTD_AUDIT==value){
	//		return "待车厂审核"
	//	}
	//	else if(TO_SMALL_AUDIT==value){
	//		return '待小区审核';
	//	}
	//	else if(SMALL_AUDIT_THROUGH==value){
	//		return '小区审核通过';
	//	}
	//	else if(OTD_AUDIT_THROUGH==value){
	//		return '车厂审核通过';
	//	}
	//	else if(REGECT_STATUS==value){
	//		return '驳回';
	//	}
	//	else if(TO_SSEVICE_AUDIT==value){
	//		return "待售后审核";
	//	}
	//	else{
	//		return '售后审核通过';
	//	}
	//}
	
	function myLink(value){
	   return String.format("<a href='#' class='u-anchor' onclick='openCheckDesc("+value+")'>查看</a>");
	}
	function openCheckDesc(value){
	        OpenHtmlWindow("<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/openCheckDesc.do?returnId="+value,600,400);
	}
</script>
<title>实销退车审核查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销退车审核查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>实销退车审核查询</h2>
		<div class="form-body">
			<table class="table_query">
			<tr>
					<td rowspan="2" class="tblopt right">VIN：</td>
					<td rowspan="2" >
	      				<textarea name="vin" rows="3"  class="form-control" style="width:150px;"></textarea>
	    			</td>
	    			<td  class="right">实销日期：</td>
			      	<td  class="left">
			      		<div class="left">
		            		<input name="salstartDate" id="t1" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
		            		&nbsp;至&nbsp;
		            		<input name="salendDate" id="t2" value="" type="text" class="short_txt" 
		            		onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
	            		</div>
			      	</td>
			</tr>
			<tr>
				<td class="right">审核日期：</td>
				<td class="left">
					<div class="left">
	            		<input name="checkstartDate" id="t3" value="" type="text" class="short_txt" 
	            			onFocus="WdatePicker({el:$dp.$('t3'), maxDate:'#F{$dp.$D(\'t4\')}'})"  style="cursor: pointer;width: 80px;"/>
	            		&nbsp;至&nbsp;
	            		<input name="checkendDate" id="t4" value="" type="text" class="short_txt" 
	            		onFocus="WdatePicker({el:$dp.$('t4'), minDate:'#F{$dp.$D(\'t3\')}'})" style="cursor: pointer;width: 80px;"/>
	           		</div>
	           	</td>
			</tr>
			<tr>
				<td  class="right"><!-- 选择基地：--> </td>
		      	<td  class="left">
		      		<!--  <select name="entity" id="entity" class="short_sel">
		      			<option value="">--请选择--</option>
			      		<c:forEach var="entityList" items="${entityList }">
			      			<option value="${entityList.PRODUCE_BASE }">${entityList.CODE_DESC }</option>
			      		</c:forEach>
			      	</select>-->
				</td>
				<td  class="right">申请日期：</td>
		      	<td  class="left">
		      		<div class="left">
	            		<input name="reqStartDate" id="t5" value="" type="text" class="short_txt" 
	            			onFocus="WdatePicker({el:$dp.$('t5'), maxDate:'#F{$dp.$D(\'t6\')}'})"  style="cursor: pointer;width: 80px;"/>
	            		&nbsp;至&nbsp;
	            		<input name="reqEndDate" id="t6" value="" type="text" class="short_txt" 
	            			onFocus="WdatePicker({el:$dp.$('t6'), minDate:'#F{$dp.$D(\'t5\')}'})" style="cursor: pointer;width: 80px;"/>
	           		</div>
		      	</td>
			</tr>
			
			<tr>
	    			<td  class="right">状态：</td>
			      	<td  class="left">
						<script type="text/javascript">
							genSelBoxExp("status",1994,"",true,"",'',"false",'');
						</script>
					</td>
	    			<td class="right">选择经销商：
	    			</td>
	    			<td class="left">
	    				<input type="text" class="middle_txt" style="width:100px" name="dealerCode" id="dealerCode" size="15" value="" onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
		    			<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
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
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>