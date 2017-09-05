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
<title>线索首次处理及时率统计表</title>
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
     /*   white-space: nowrap; */
}
</style> 
</head>
<script type="text/javascript">
function getDealerList(dealer_id,flag){
	var startDate=document.getElementById("startDate").value;
	var endDate=document.getElementById("endDate").value;
	var dealerCode=document.getElementById("dealerCode").value;
	var seriesId=document.getElementById("seriesId").value;
	var parameter="?dealer_id="+dealer_id+"&flag="+flag+"&startDate="+startDate+"&endDate="+endDate+"&seriesId="+seriesId;
	var openUrl='<%=contextPath%>/crm/report/ReturnVisitReport/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>线索首次处理及时率统计表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="hidden" id="dealerCode" value="${dealerCode}" />
		<input type="hidden" id="startDate" value="${startDate}" />
		<input type="hidden" id="endDate" value="${endDate}" />
		<input type="hidden" id="seriesId" value="${seriesId}" />
		</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	</tr>
	<tr class="scrollColThead" align="left">
	</tr>
	<tr class="scrollColThead">
		<th  nowrap  class="scrollRowThead scrollCR" >大区</th>
		<th  nowrap  class="scrollRowThead scrollCR" >省份</th>
			<th  nowrap class="scrollRowThead scrollCR" title="经销商代码"  >经销商代码</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商简称"  >经销商名称</th>
		<c:if test="${ !empty dutyType}" >
		<th  nowrap  class="scrollRowThead scrollCR" title="销售组" >销售组</th>
		<th  nowrap  class="scrollRowThead scrollCR" title="销售顾问" >销售顾问</th>
		</c:if>
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的线索总数量' >线索总量</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的有效线索总数量'>有效线索总量</th>
		<th   class="scrollRowThead scrollCR" title='是指符合指定时间内、经销商首次处理的所有线索量' >≤首次处理量</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、经销商首次处理的所有线索量在线索总量中的占比' >首次处理率</th>	
		<th   class="scrollRowThead scrollCR" title='是指符合指定时间内、经销商首次未处理的所有线索量' >≤首次未处理量</th>	
		<th   class="scrollRowThead scrollCR" title='首次处理时长≤12工作时' >≤12H</th>	
		<th   class="scrollRowThead scrollCR" title='首次处理时长＞12工作时' >>12H</th>
		<th   class="scrollRowThead scrollCR" title='12工作时及以内的首次线索处理率' >12H处理率</th>
	</tr>
		<c:forEach items="${balanceList}" var="list">
		 <tr >
			<td nowrap >${list.ROOT_ORG_NAME}&nbsp; </td>
			<td nowrap >${list.PQ_ORG_NAME}&nbsp; </td>
			<td nowrap >${list.DEALER_CODE}&nbsp; </td>
			<td nowrap >
			<c:choose>
				<c:when test="${!empty list.DEALER_ID}"> 
					    <a href="#" onclick="getDealerList('${list.DEALER_ID}','1')">${list.DEALER_SHORTNAME}&nbsp;</a>
				</c:when>
				<c:otherwise>
						  ${list.DEALER_SHORTNAME}&nbsp; 
				</c:otherwise>
			</c:choose>
			 </td>
			<c:if test="${ !empty dutyType}" >
				<td nowrap >${list.GROUP_NAME}&nbsp; </td> 
				<td nowrap >${list.NAME}&nbsp; </td> 
			</c:if>
			<td nowrap >${list.XSZLCOUNT}&nbsp; </td>
			<td nowrap >${list.YXZLCOUNT}&nbsp; </td>
			<td nowrap >${list.SCZLCOUNT}&nbsp; </td>
			<td nowrap >${list.SCHFRATE}&nbsp; </td>
			<td nowrap >${list.WCZLCOUNT}&nbsp; </td>
			<td nowrap >${list.XDZLCOUNT}&nbsp; </td>
			<td nowrap >${list.DYZLCOUNT}&nbsp; </td>
			<td nowrap >${list.HFRATE}&nbsp; </td>
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
