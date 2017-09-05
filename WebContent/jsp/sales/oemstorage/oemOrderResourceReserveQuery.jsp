<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>资源保留查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}

</script>

</head>
<body onunload='javascript:destoryPrototype();'>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 车厂库存管理 &gt; 资源保留查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
			<td align="right" width="13%">&nbsp;</td>
			<td align="right" width="23%">所在仓库：</td>
			<td align="left" width="50%">
				<select name="warehouseId" id="warehouseId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${warehouseList}" var="warehouseList">
						<option value="${warehouseList.WAREHOUSE_ID}"><c:out value="${warehouseList.WAREHOUSE_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td>&nbsp;</td> 
		</tr>
		<tr>
			<td align="right"></td>
			<td align="right"><input type="radio"  name="flag" value="1" checked="checked" onClick="toChangeDis1();"/>&nbsp;物料组：</td>
			<td>
				<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" id="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','3');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('groupCode');" value="清空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"><input type="radio"  name="flag" value="2" onClick="toChangeDis2();"/>&nbsp;&nbsp;&nbsp;&nbsp;物料：</td>
			<td>
				<input type="text" class="middle_txt" name="materialCode" size="15"  value="" id="materialCode" />
				<input name="button3" id="button" type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">批次号：</td>
			<td>
				<select name="batchNo" id="batchNo" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${batchNOList}" var="batchNOList">
						<option value="${batchNOList.BATCH_NO}"><c:out value="${batchNOList.BATCH_NO}"/></option>
					</c:forEach>
				</select>
	  		</td>
	  		<td></td>
		</tr>
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td></td>
			<td align="left">
				<input type="button" name="queryBtn" class="cssbutton" onclick="totalQuery();" value="查询" id="queryBtn1" /> 
				<input type="button" name="button3" class="cssbutton" onclick="totalDownLoad();" value="下载" id="queryBtn1" /> 
			</td>
			<td align="center" nowrap>&nbsp;</td>
		</tr>   
	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<br>
<!-- 查询条件 end -->
<script type="text/javascript">
	var url;
	
	var title = null;
	
	var columns;		
	
	var calculateConfig;
	
	function doInit(){
		document.getElementById("materialCode").disabled="true";
		document.getElementById("button").disabled="true";
	}
	function toChangeDis1(){
		document.getElementById("materialCode").disabled="true";
		document.getElementById("button").disabled="true";
		document.getElementById("groupCode").disabled="";
		document.getElementById("button3").disabled="";
		document.getElementById("materialCode").value="";
	}
	function toChangeDis2(){
		document.getElementById("materialCode").disabled="";
		document.getElementById("button").disabled="";
		document.getElementById("groupCode").disabled="true";
		document.getElementById("button3").disabled="true";
		document.getElementById("groupCode").value="";
	}
	function totalQuery(){
		url = "<%=contextPath%>/sales/oemstorage/OrderResourceReserveQuery/orderResourceReserveQuery.json";
		columns = [
					{header: "仓库名称",dataIndex: 'WAREHOUSE_NAME', align:'center'},
					{header: "物料代码",dataIndex: 'MATERIAL_CODE', align:'center'},
					{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
					{header: "保留量", dataIndex: 'AMOUNT', align:'center'},
					{header: "订单号", dataIndex: 'ORDER_NO', align:'center', renderer:myDetail},
					{header: "开票经销商", dataIndex: 'BEALER_NAME', align:'center'},
					{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center', renderer:getItemValue}
			      ];
		__extQuery__(1);
	}
	function totalDownLoad(){
		$('fm').action="<%=contextPath%>/sales/oemstorage/OrderResourceReserveQuery/orderResourceReserveExport.json";
     	$('fm').submit();
    }
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ record.data.ORDER_ID +"\",\""+ record.data.RESOURCE_TYPE +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(arg1,arg2){ 
		if(arg2 == "sto"){
			OpenHtmlWindow('<%=request.getContextPath()%>/sales/oemstorage/OemStorageQuery/selInfoView.do?id='+arg1,900,500);
		}
		else{
			OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/dlvryReqQueryInfo.do?&reqId='+arg1,900,500);
		}
	}
</script>
</body>
</html>