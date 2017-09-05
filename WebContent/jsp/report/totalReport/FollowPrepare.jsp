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
<title>集团客户报备跟进表下载</title>



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
     white-space: nowrap;
     text-align: center;
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
	<strong>集团客户报备跟进表下载&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="22" class="scrollRowThead scrollCR juzuo"><strong>集团客户报备跟进表下载</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="22" class="scrollRowThead scrollCR" align="left" style="left: auto"><div align="left">跟进日期：${followDate1}--${followDate2}</div></td>
	</tr>
	
	<tr class="scrollColThead">
			<th class="scrollRowThead scrollCR" width="7%">组织</th>
			<th class="scrollRowThead scrollCR" width="7%">省</th>
			<th class="scrollRowThead scrollCR" width="7%">提报单位</th>
			<th class="scrollRowThead scrollCR" width="7%">提报日期</th>
			<th class="scrollRowThead scrollCR" width="7%">客户名称</th>
			<th class="scrollRowThead scrollCR" width="7%">客户类型</th>
			<th class="scrollRowThead scrollCR" width="7%">主营业务</th>
			<th class="scrollRowThead scrollCR" width="7%">邮编</th>
			<th class="scrollRowThead scrollCR" width="7%">详细地址</th>
			<th class="scrollRowThead scrollCR" width="7%">主要联系人</th>
			<th class="scrollRowThead scrollCR" width="7%">职务</th>
			<th class="scrollRowThead scrollCR" width="7%">电话</th>
			<th class="scrollRowThead scrollCR" width="7%">车系</th>
			<th class="scrollRowThead scrollCR" width="7%">数量</th>
			<th class="scrollRowThead scrollCR" width="7%">备注</th>
			<th class="scrollRowThead scrollCR" width="7%">跟进日期</th>
			<th class="scrollRowThead scrollCR" width="7%">跟进内容</th>
			
	</tr>
	
	<c:forEach items="${list_FollowPrepare}" var="list_FollowPrepare">
	<tr >
		<td>${list_FollowPrepare.ORG_NAME}&nbsp;</td>
		<td>${list_FollowPrepare.REGION_NAME}&nbsp;</td>
		<td>${list_FollowPrepare.COMPANY_NAME}&nbsp;</td>
		<td>${list_FollowPrepare.SUBMIT_DATE}&nbsp;</td>
		<td>${list_FollowPrepare.FLEET_NAME}&nbsp;</td>
		<td>${list_FollowPrepare.FLEET_TYPE}&nbsp;</td>
		<td >${list_FollowPrepare.MAIN_BUSINESS}&nbsp;</td>
		<td >${list_FollowPrepare.ZIP_CODE}&nbsp;</td>
		<td >${list_FollowPrepare.ADDRESS}&nbsp;</td>
		<td >${list_FollowPrepare.MAIN_LINKMAN}&nbsp;</td>
		<td >${list_FollowPrepare.MAIN_JOB}&nbsp;</td>
		<td >${list_FollowPrepare.MAIN_PHONE}&nbsp;</td>
		<td >${list_FollowPrepare.GROUP_NAME}&nbsp;</td>
		<td>${list_FollowPrepare.SERIES_COUNT}&nbsp;</td>
		<td >${list_FollowPrepare.REQ_REMARK}&nbsp;</td>
		<td >${list_FollowPrepare.FOLLOW_DATE}&nbsp;</td>
		<td >${list_FollowPrepare.FOLLOW_REMARK}&nbsp;</td>
		
	
		
	
	</tr>
	
	 </c:forEach>
	 <tr >
	 	<th class="scrollRowThead"  colspan="13" >合计</th>
	 	<th class="scrollRowThead">${series_count}&nbsp;</th>
	 	<th class="scrollRowThead" colspan="3">&nbsp;</th>
	 </tr>
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
