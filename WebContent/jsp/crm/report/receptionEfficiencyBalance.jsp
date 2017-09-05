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
<title>潜客跟踪统计表</title>
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
	var openUrl='<%=contextPath%>/crm/report/ReceptionEfficiencyReport/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>潜客跟踪统计表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
		<th  nowrap  class="scrollRowThead scrollCR" rowspan=2>省份</th>
		<th  nowrap class="scrollRowThead scrollCR" rowspan=2  title="经销商代码"  >代码</th>
		<th  nowrap class="scrollRowThead scrollCR" rowspan=2  title="经销商简称"  >简称</th>
		<c:if test="${ !empty dutyType}" >
			<th  nowrap  class="scrollRowThead scrollCR" rowspan=2  title="销售组" >销售组</th>
			<th  nowrap  class="scrollRowThead scrollCR" rowspan=2  title="销售顾问" >销售顾问</th>
		</c:if>
		 <th   class="scrollRowThead scrollCR" colspan=3 title='跟踪数量' >跟踪</th>
		 <th   class="scrollRowThead scrollCR" colspan=6 title='邀约数量' >邀约</th>
		 <th   class="scrollRowThead scrollCR" colspan=3 title='保有客户回访 '>保有客户回访</th>
	</tr>
	<tr class="scrollColThead">
		<th   class="scrollRowThead scrollCR" title='指定时间内、有效的跟进任务数量' >计划跟踪数</th>
		<th   class="scrollRowThead scrollCR" title='计划跟踪数中实际完成的任务数' >实际跟踪数</th>	
		<th   class="scrollRowThead scrollCR" title='实际跟踪数除以计划跟踪数' >占比</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、所有线索数量' >计划线索邀约数</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、完成到店的线索邀约任务数' >实际线索邀约数</th>
		<th   class="scrollRowThead scrollCR" title='实际线索邀约数除以计划线索邀约数' >占比</th>	
		<th   class="scrollRowThead scrollCR" title='指定时间内、有效的再次邀约任务数量' >计划再次邀约数</th> 
		<th   class="scrollRowThead scrollCR" title='计划再次邀约数中完成了实际到店的任务数' >实际再次邀约数</th>	
		<th   class="scrollRowThead scrollCR" title='实际再次邀约数除以计划再次邀约数' >占比</th>
		<th   class="scrollRowThead scrollCR" title='指定时间内、有效的回访任务数量' >保有客户计划回访数</th>	
		<th   class="scrollRowThead scrollCR" title='计划回访数中完成了的任务数' >保有客户实际回访数</th>
		<th   class="scrollRowThead scrollCR" title='保有客户实际回访数除以保有客户计划回访数' >占比</th>	
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
			
			<td nowrap >${list.JHSCOUNT}&nbsp; </td>
			<td nowrap >${list.SJSCOUNT}&nbsp; </td>
			<td nowrap >${list.GZRATE}&nbsp; </td>
			<td nowrap >${list.XSZLCOUNT}&nbsp; </td>
			<td nowrap >${list.SJXSCOUNT}&nbsp; </td> 
			<td nowrap >${list.XSYYRATE}&nbsp; </td>
			<td nowrap >${list.ZYYCOUNT}&nbsp; </td>
			<td nowrap >${list.ZDDCOUNT}&nbsp; </td>
			<td nowrap >${list.ZYYRATE}&nbsp; </td>
			<td nowrap >${list.HFCOUNT}&nbsp; </td>
			<td nowrap >${list.HFWCCOUNT}&nbsp; </td>
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
