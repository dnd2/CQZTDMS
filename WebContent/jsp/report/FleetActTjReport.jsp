<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%
	String contextPath = request.getContextPath(); 
	
	

 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团客户实销信息审核查询
</title>



<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<style type="text/css"> 
body,table, td, a { 
font:9pt; 
} 

/*固定行头样式*/
.scrollRowThead 
{
     position: relative; 
     left: expression(this.parentElement.parentElement.parentElement.scrollLeft);
     z-index:0;
}

/*固定表头样式*/
.scrollColThead {
     position: relative; 
     top: expression(this.parentElement.parentElement.parentElement.scrollTop);
     z-index:2;
}

/*行列交叉的地方*/
.scrollCR {
     z-index:3;
} 
 
/*div外框*/
.scrollDiv {
height:480px;
clear: both; 
border: 1px solid #EEEEEE;
OVERFLOW: scroll;
width: 100%; 
}

/*行头居中*/
.scrollColThead td,.scrollColThead th
{
     text-align: center ;
}

/*行头列头背景*/
.scrollRowThead,.scrollColThead td,.scrollColThead th
{
background-color:EEEEEE;
}

/*表格的线*/
.scrolltable
{
border-bottom:1px solid #CCCCCC; 
border-right:1px solid #CCCCCC; 
}

/*单元格的线等*/
.scrolltable td,.scrollTable th
{
     border-left: 1px solid #CCCCCC; 
     border-top: 1px solid #CCCCCC; 
     padding: 5px; 
     text-align: center;
     white-space: nowrap;
}
.juzuo td
{
line-height: 1px solid #CCCCCC;
height: 1px solid #CCCCCC;
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>集团客户实销信息审核查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="22" class="scrollRowThead scrollCR juzuo"><strong>集团客户实销信息审核查询</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="22" class="scrollRowThead scrollCR" align="left" style="left: auto"><div align="left">上报日期：${startDate}--${endDate}</div></td>
	</tr>
	
	<tr class="scrollColThead">
			<th class="scrollRowThead scrollCR" width="7%">大区</th>
			<th class="scrollRowThead scrollCR" width="7%">省</th>
			<th class="scrollRowThead scrollCR" width="7%">市</th>
			<th class="scrollRowThead scrollCR" width="7%">县</th>
			<th class="scrollRowThead scrollCR" width="7%">A/B网</th>
			<th class="scrollRowThead scrollCR" width="7%">销售日期</th>
			<th class="scrollRowThead scrollCR" width="7%">生产日期</th>
			<th class="scrollRowThead scrollCR" width="7%">期票日期</th>
			<th class="scrollRowThead scrollCR" width="7%">上级单位</th>
			<th class="scrollRowThead scrollCR" width="7%">销售单位</th>
			<th class="scrollRowThead scrollCR" width="7%">客户名称</th>
			<th class="scrollRowThead scrollCR" width="7%">一级客户类型</th>
			<th class="scrollRowThead scrollCR" width="7%">二级客户类型</th>
			<th class="scrollRowThead scrollCR" width="7%">合同编号</th>
			<th class="scrollRowThead scrollCR" width="7%">车型类别</th>
			<th class="scrollRowThead scrollCR" width="7%">车型系列</th>
			<th class="scrollRowThead scrollCR" width="7%">车型编号</th>
			<th class="scrollRowThead scrollCR" width="7%">状态</th>
			<th class="scrollRowThead scrollCR" width="7%">颜色</th>
			<th class="scrollRowThead scrollCR" width="7%">底盘号</th>
	</tr>
	
	<c:forEach items="${list_FleetActTj}" var="list_FleetActTj">
	<tr >
		<td >${list_FleetActTj.ROOT_ORG_NAME}&nbsp;</td>
		<td >${list_FleetActTj.REGION_NAME}&nbsp;</td>
		<td >${list_FleetActTj.CITY_NAME}&nbsp;</td>
		<td >${list_FleetActTj.TOWN}&nbsp;</td>
	<td >${list_FleetActTj.AREA_NAME}&nbsp;</td>
		<td >${list_FleetActTj.SALES_DATE}&nbsp;</td>
		<td >${list_FleetActTj.PRODUCT_DATE}&nbsp;</td>
		<td >${list_FleetActTj.INVOICE_DATE}&nbsp;</td>
		<td >${list_FleetActTj.ROOT_DEALER_NAME}&nbsp;</td>
		<td >${list_FleetActTj.DEALER_SHORTNAME}&nbsp;</td>
		<td >${list_FleetActTj.CTM_NAME}&nbsp;</td>
		<td >${list_FleetActTj.PARTFLEET_TYPE}&nbsp;</td>
		<td >${list_FleetActTj.FLEET_TYPE}&nbsp;</td>
		<td >${list_FleetActTj.CONTRACT_NO}&nbsp;</td>
		<td >${list_FleetActTj.SERNAME}&nbsp;</td>
		<td >${list_FleetActTj.MODNAME}&nbsp;</td>
		<td >${list_FleetActTj.MODCODE}&nbsp;</td>
		<td >${list_FleetActTj.PACKNAME}&nbsp;</td>
		<td >${list_FleetActTj.COLOR}&nbsp;</td>
		<td >${list_FleetActTj.VIN}&nbsp;</td>
	</tr>
	
	 </c:forEach>
	
	</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
</body>
</html>
