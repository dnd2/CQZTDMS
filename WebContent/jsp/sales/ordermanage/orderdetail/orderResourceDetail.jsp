<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.jatools.db.af"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 		
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单资源明细</title>
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
       border:1px solid #ccc;
        border-collapse:collapse;
}
</style> 
</head>
<body >
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>订单资源&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable" rules="cols" >

	<tr class="scrollColThead">
	<th colspan="8" class="scrollRowThead scrollCR"><strong>订单资源</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	</tr>
	<tr class="scrollColThead">
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">车系</th>
		<th  nowrap class="scrollRowThead scrollCR" width="25%">物料代码</th>
		<th nowrap  class="scrollRowThead scrollCR" width="25%">物料名称</th>
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">颜色</th>
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">已审核数量</th>
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">可用资源</th>
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">未满足常规订单</th>
		<th  nowrap  class="scrollRowThead scrollCR" width="25%">实际可用数量</th>
	</tr>
	<c:forEach items="${resourceList}" var="list">
		<tr >
			<td nowrap>${list.SERIES_NAME}&nbsp;</td>
			<td nowrap >${list.MATERIAL_CODE}&nbsp;</td>
			<td nowrap>${list.MATERIAL_NAME}&nbsp;</td>
			<td nowrap>${list.COLOR_NAME}&nbsp;</td>
			<td nowrap >${list.CALL_AMOUNT}&nbsp;</td>
			<td nowrap >${list.ACT_STOCK}&nbsp;</td>
			<td nowrap >${list.UNDO_ORDER_AMOUNT}&nbsp;</td>
			<th nowrap >${list.ACT_RESOURCE_AMOUNT}&nbsp;</th>
		</tr>
	</c:forEach>
</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr>
	<td colspan="4">
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td>
	</tr>
</table>
</body>
</html>
