<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运查询</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：整车物流管理&gt;发运管理&gt;运单发票录入</div>
	<form id="fm" name="fm" method="post" >
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="vehicle_id" name="VEHICLE_ID" value="${data.VEHICLE_ID}"/>
		<table class="table_query" border="0">
			<th colspan="11">&nbsp;基本信息</th>
			<tr>
				<td class="right">产地：</td>
				<td>${data.AREA_NAME}</td>
				<td class="right">发运单号：</td>
				<td>${data.BILL_NO}</td>
			</tr>
			<tr>
				<td class="right">VIN：</td>
				<td>${data.VIN}</td>
				<td class="right">车系：</td>
				<td>${data.SERIES_NAME}</td>
			</tr>
			<tr>
				<td class="right">车型：</td>
				<td>${data.MODEL_NAME}</td>
				<td class="right">物料名称：</td>
				<td>${data.MATERIAL_NAME}</td>
				
			</tr>
			<tr>
				<td class="right">颜色：</td>
				<td>${data.COLOR_NAME}</td>
				<td class="right">发运时间：</td>
				<td>${data.BILL_CRT_DATE}</td>
			</tr>
			<tr>
				<td class="right">发票号：</td>
				<td>
				<input name="INVOICE_NO"   id="INVOICE_NO"  type="text" maxlength="20"  class="middle_txt" datatype="0,is_null,20"  maxlength="20" value="${data.INVOICE_NO}"/></td>
			    <td  id="judeposition" style="display: none;"></td>
			</tr>
		</table>
		<table class="table_query" width="90%" border="0" align="center">
			<tr>
				<td align="center" >
					<input id="queren" name="button" type="button" class="normal_btn" onclick="addBillInvoince();" value="确认" />
					<input id="fanhui" name="button" type="button" class="normal_btn" onclick="window.history.back()" value="返回" />
				</td>
			</tr>
		</table>
		<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
	</form>
	<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" style="position:relative;
	clear:both;
	overflow-x:scroll;
	overflow-y:hidden;
	display:inline;
	border:solid 1px #C2C2C2;
	scrollbar-3dlight-color:#595959;
	scrollbar-arrow-color:#CCCCCC;
	scrollbar-base-color:#CFCFCF;
	scrollbar-darkshadow-color:#FFFFFF;
	scrollbar-face-color:#F3F4F8;
	scrollbar-highlight-color:#FFFFFF;
	scrollbar-shadow-color:#595959;"></div>
<div id="myPage" class="pages"></div>

  </body>
</html>

<script type="text/javascript" >

//发票添加到车辆信息中
function addBillInvoince(){
	var invoiceNo =  document.getElementById('INVOICE_NO').value;
 	if(invoiceNo.length == 0) 
 	{
 		document.getElementById('judeposition').style.display  = '';
 		return;
 	}
 	
 	 MyConfirm("是否录入发票？",addBackcomit);
	
}
//提交
function addBackcomit(){
	document.getElementById('fm').action = "<%=contextPath%>/sales/storage/sendmanage/WaybillIn/saveInvoiceNo.do"
   	document.getElementById('fm').submit();
}

</script>  

