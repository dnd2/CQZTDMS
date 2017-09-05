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
<title>展厅留店时间统计表</title>
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
	var openUrl='<%=contextPath%>/crm/report/LiuShopStatisticsTotal/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>展厅留店时间统计表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
		<th  nowrap  class="scrollRowThead scrollCR" rowspan=2 >大区</th>
		<th  nowrap  class="scrollRowThead scrollCR" rowspan=2 >省份</th>
		<th  nowrap class="scrollRowThead scrollCR" rowspan=2  title="经销商代码"  >代码</th>
		<th  nowrap class="scrollRowThead scrollCR" rowspan=2  title="经销商简称"  >简称</th>
		<c:if test="${ !empty dutyType}" >
		<th  nowrap  class="scrollRowThead scrollCR" rowspan=2  title="销售组" >销售组</th>
		<th  nowrap  class="scrollRowThead scrollCR" rowspan=2  title="销售顾问" >销售顾问</th>
		</c:if>
		<th   class="scrollRowThead scrollCR" rowspan=2 title="销售顾问处理过的客流线索中、集客方式包含'首次'的数量(线索来源为：来店)"  >首次客流</th>
		<th   class="scrollRowThead scrollCR" rowspan=2 title="销售顾问处理过的客流线索中、集客方式包含'邀约再次来店'的数量(线索来源为：来店)"  >邀约客流</th>	
		<th   class="scrollRowThead scrollCR" rowspan=2 title="首次客流加上邀约客流"  >混合客流</th>
		<th   class="scrollRowThead scrollCR" colspan=3 title="首次客流中留店数量"  >留店数</th>	
		<th   class="scrollRowThead scrollCR" colspan=3 title="首次客流中留店数量除以首次客流数"  >留店率</th>
		<th   class="scrollRowThead scrollCR" colspan=4 title="留店数中的建卡数"  >建卡数</th>	
		<th   class="scrollRowThead scrollCR" colspan=4 title="留店数中的建卡数除以首次客流数"  >建卡率</th>	
	</tr>
	<tr class="scrollColThead" >
		<th   class="scrollRowThead scrollCR" title="0-15分钟首次客流中留店数量"  >0-15分钟</th>	
		<th   class="scrollRowThead scrollCR" title="15-30分钟首次客流中留店数量"  >15-30分钟</th>
		<th   class="scrollRowThead scrollCR" title="30分钟以上首次客流中留店数量"  >30分钟以上</th>	
		<th   class="scrollRowThead scrollCR" title="0-15分钟首次客流中留店数量除以首次客流数"  >0-15分钟</th>
		<th   class="scrollRowThead scrollCR" title="15-30分钟首次客流中留店数量除以首次客流数"  >15-30分钟</th>	
		<th   class="scrollRowThead scrollCR" title="30分钟以上首次客流中留店数量除以首次客流数"  >30分钟以上</th>
		<th   class="scrollRowThead scrollCR" title="0-15分钟留店数中的建卡数"  >0-15分钟</th>	
		<th   class="scrollRowThead scrollCR" title="15-30分钟留店数中的建卡数"  >15-30分钟</th>	
		<th   class="scrollRowThead scrollCR" title="30分钟以上留店数中的建卡数"  >30分钟以上</th>
		<th   class="scrollRowThead scrollCR" title="留店数中的建卡数合计"  >合计</th>
		<th   class="scrollRowThead scrollCR" title="0-15分钟留店数中的建卡数除以首次客流数"  >0-15分钟</th>	
		<th   class="scrollRowThead scrollCR" title="0-15分钟留店数中的建卡数除以首次客流数"  >15-30分钟</th>	
		<th   class="scrollRowThead scrollCR" title="30分钟以上留店数中的建卡数除以首次客流数"  >30分钟以上</th>
		<th   class="scrollRowThead scrollCR" title="留店数中的建卡数除以首次客流数合计"  >合计</th>
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
			
			<td nowrap >${list.SCCOUNT}&nbsp; </td>
			<td nowrap >${list.YYKL}&nbsp; </td>
			<td nowrap >${list.SCCOUNT+list.YYKL}&nbsp; </td>
			
			<td nowrap >${list.LD15}&nbsp; </td>
			<td nowrap >${list.LD30}&nbsp; </td> 
			<td nowrap >${list.LD35}&nbsp; </td>
			<td nowrap >${list.RATE15}&nbsp; </td>
			<td nowrap >${list.RATE30}&nbsp; </td>
			<td nowrap >${list.RATE35}&nbsp; </td>
			<td nowrap >${list.JK15}&nbsp; </td>
			<td nowrap >${list.JK30}&nbsp; </td>
			<td nowrap >${list.JK35}&nbsp; </td>
			<td nowrap >${list.JKCOUNT}&nbsp; </td>
			<td nowrap >${list.BUILDRATE15}&nbsp; </td>
			<td nowrap >${list.BUILDRATE30}&nbsp; </td>
			<td nowrap >${list.BUILDRATE35}&nbsp; </td>
			<td nowrap >${list.BUILDRATE}&nbsp; </td>
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
