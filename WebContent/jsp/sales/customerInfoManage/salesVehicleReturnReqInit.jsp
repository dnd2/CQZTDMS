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
var myPage;
var url = "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/querySalesedVehicleList.json?COMMAND=1";
var title = null;
var columns = [
	{id:'action',header: "操作", wclass:'center',idth:70,sortable: false,dataIndex: 'ORDER_ID',renderer:myLink},
			{header: "物料代码", dataIndex: 'MATERIAL_CODE', class:'center'},
			{header: "物料名称", dataIndex: 'MATERIAL_NAME', class:'center'},
			{header: "客户名称", dataIndex: 'CTM_NAME', class:'center'},
			{header: "VIN", dataIndex: 'VIN', class:'center'},
			{header: "实销时间", dataIndex: 'SALES_DATE', class:'center'}
			
	      ];
function myLink(value,metaDate,record){
	var data = record.data;
    return String.format("<a href=\"<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/returnVehicleInfo.do?order_id="+data.ORDER_ID+"&vehicle_id="+data.VEHICLE_ID+"\" class='u-anchor'>[退车]</a>");
}
</script>
<title>实销退车申请</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销退车申请</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>实销退车申请</h2>
		<div class="form-body">
			<table class="table_query" >
			<c:if test="${returnValue==2}">
			<tr>
			<td  colspan="6"><font color="red">注意事项:</font></td>
			</tr>
			<tr>
			<td  colspan="6"><font color="red">1、实销变更和实销退车审核流程：实销信息变更——由区域销售主管在dms系统的“实销信息变更审核”中进行审核,非质量问题实销退车申请--dms系统做“实销退车申请”后，将区域经理、大区经理签字后的工单照片、错误发票、正确发票照片交售后服务部陈力审核,因质量问题实销退车申请--dms系统做“实销退车申请”后，将现场工程师签字的工单照片交售后服务部陈力审核。</font></td>
			</tr>
			<tr>
			<td  colspan="6"><font color="red">2、实销变更和实销退车适用范围：客户名称变更、大客户错录成一般零售、vin号变更、客户退换车必须走实销退车申请流程，其他信息变更走实销变更申请流程。</font></td>
			</tr>
			</c:if>
			<tr>
					<td width="20%" class="tblopt right">VIN：</td>
					<td width="39%" >
	      				<textarea name="vin" cols="18" rows="3" class="form-control" style="width:176px; height:50px"></textarea>
	    			</td>
	    			<td><!--选择业务范围：<select name="areaId" class="short_sel">
						<c:forEach items="${areaList}" var="po">
							<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
						</c:forEach>
					</select>
					<input type="hidden" name="dealerId" id="dealerId" />
					<input type="hidden" name="area_id" id="area_id" value="" />--></td>
			</tr>
			<tr>
					<td class="center" colspan="3" ><input type="button"  class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> </td>
		

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