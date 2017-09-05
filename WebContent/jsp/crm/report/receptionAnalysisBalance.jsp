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
<title>展厅接待效率分析报表</title>
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
	var openUrl='<%=contextPath%>/crm/report/ReceptionAnalysisReport/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>展厅接待效率分析报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
			<th  nowrap class="scrollRowThead scrollCR" title="经销商代码"  >代码</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商简称"  >简称</th>
		<c:if test="${ !empty dutyType}" >
		<th  nowrap  class="scrollRowThead scrollCR" title="销售组" >销售组</th>
		<th  nowrap  class="scrollRowThead scrollCR" title="销售顾问" >销售顾问</th>
		</c:if>
		<th   class="scrollRowThead scrollCR" title='处理客流线索中、集客方式包含‘首次’的数量(线索来源为：来电)'  >首次客流</th>
		<th   class="scrollRowThead scrollCR" title='处理客流线中、集客方式包含‘邀约再次到店’的数量(线索来源为：来电)'   >邀约客流</th>	
		<th   class="scrollRowThead scrollCR" title='首次客流加上邀约客流数'  >混合客流</th>
		<th   class="scrollRowThead scrollCR" title='处理客流线索中、线索来源为‘来店’、并且线索趋前迎接数为‘是’的数量'  >趋前客流</th>
		<th   class="scrollRowThead scrollCR" title='处理客流线索中、线索来源为DCRC录入并且顾问已经确认的数量'  >当日处理客流量</th>
		<th   class="scrollRowThead scrollCR" title='处理客流线索中、线索来源为‘来店’的数量'  >客流量</th>
		<th   class="scrollRowThead scrollCR" title='趋前迎接数除以客流量'  >趋前迎接率</th>	
		<th   class="scrollRowThead scrollCR" title='当日处理客流量数除以客流量'  >当日处理客流率</th>	
		<th   class="scrollRowThead scrollCR" title='线索来源为‘来店’、集客方式包含‘首次’的线索产生的建档数'  >建卡数</th>
		<th   class="scrollRowThead scrollCR" title='建卡数除以首次客流' >建卡率</th>	
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的试乘试驾数量' >试驾数据</th>
		<th   class="scrollRowThead scrollCR" title='试驾数除以混合客流' >试驾率</th>	
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的订单数量' >订单数</th>
		<th   class="scrollRowThead scrollCR" title='订单数除以建卡数' >订单率</th>	
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的交车数量'>交车数</th>
		<th   class="scrollRowThead scrollCR" title='交车数除以建卡数' >交车成交率</th>	
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的未交车数量'>累计未交订单数</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的购置方式为‘换购’的交车数' >置换数</th>	
		<th   class="scrollRowThead scrollCR" title='置换数除以交车数' >置换率</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、产生的购置方式含‘按揭’的交车数' >车贷渗透数</th>
		<th   class="scrollRowThead scrollCR" title='车贷数除以交车数' >车贷渗透率</th>	
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
			<td nowrap >${list.YYCOUNT}&nbsp; </td>
			<td nowrap >${list.KLTOTAL}&nbsp; </td> 
			<td nowrap >${list.YJSCOUNT}&nbsp; </td>
			<td nowrap >${list.DRKLCOUNT}&nbsp; </td>
			<td nowrap >${list.KLCOUNT}&nbsp; </td>
			<td nowrap >${list.KLRATE}&nbsp; </td>
			<td nowrap >${list.DRKLRATE}&nbsp; </td>
			<td nowrap >${list.JKCOUNT}&nbsp; </td>
			<td nowrap >${list.JKRATE}&nbsp; </td>
			<td nowrap >${list.SJCOUNT}&nbsp; </td>
			<td nowrap >${list.SJRATE}&nbsp; </td>
			<td nowrap >${list.DDCOUNT}&nbsp; </td>
			<td nowrap >${list.DDRATE}&nbsp; </td>
			<td nowrap >${list.JCCOUNT}&nbsp; </td>
			<td nowrap >${list.JCRATE}&nbsp; </td>
			<td nowrap >${list.WJCCOUNT}&nbsp; </td>
			<td nowrap >${list.ZHCOUNT}&nbsp; </td>
			<td nowrap >${list.ZHRATE}&nbsp; </td>
			<td nowrap >${list.CDCOUNT}&nbsp; </td>
			<td nowrap >${list.CDRATE}&nbsp; </td>
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
