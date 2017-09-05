<%@page import="com.infodms.dms.util.CommonUtils"%>
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
<title>来店契机及构成情况分析表</title>
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
	var openUrl='<%=contextPath%>/crm/report/CallOpportunityBuyReport/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>来店契机及构成情况分析表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
		<th  nowrap class="scrollRowThead scrollCR" >代码</th>
		<th  nowrap class="scrollRowThead scrollCR" >简称</th>
		<th  nowrap  class="scrollRowThead scrollCR" >来电契机</th>
		<c:forEach items="${seriesList}" var="clist">
			<th   class="scrollRowThead scrollCR" >${clist.SERIES_NAME}</th>	
			<th   class="scrollRowThead scrollCR" >占比</th>
		</c:forEach>
		<th   class="scrollRowThead scrollCR"  >合计</th>
		<th   class="scrollRowThead scrollCR"  >占比</th>
	</tr>
	<%	List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("balanceList");  %>
		<% for(int i=0;i<list.size();i++){ %>
		<tr >
			<td nowrap ><%=list.get(i).get("ROOT_ORG_NAME")%>&nbsp; </td>
			<td nowrap ><%=list.get(i).get("PQ_ORG_NAME")%>&nbsp;</td>
			<td nowrap><%=list.get(i).get("DEALER_CODE")%>&nbsp;</td>
			<td nowrap >
			<c:choose>
				<c:when test="${empty dutyType}">
				  	<%
					if(!"".equals(list.get(i).get("DEALER_ID")==null?"":list.get(i).get("DEALER_ID").toString())){
					  %>
					    <a href="#" onclick="getDealerList('<%=list.get(i).get("DEALER_ID") %>','1')"><%=list.get(i).get("DEALER_SHORTNAME")%>&nbsp;</a>
					 <% 
					 }else{
						 out.print(list.get(i).get("DEALER_SHORTNAME"));
					 }
					%>
				</c:when>
				<c:otherwise>
						  <%=list.get(i).get("DEALER_SHORTNAME")%>&nbsp;
				</c:otherwise>
			</c:choose>
		 
			</td>
		    <td nowrap ><%=list.get(i).get("CODE_DESC")%>&nbsp;</td>
<% List<Map<String,Object>> cList = (List<Map<String,Object>>)request.getAttribute("seriesList");  %>
		<% 
		for(int j=0;j<cList.size();j++){ %>
				<td nowrap > <%=list.get(i).get("COUNT"+cList.get(j).get("SERIES_ID")) %> </td>
				<td nowrap > <%=list.get(i).get("RATE"+cList.get(j).get("SERIES_ID")) %> </td>
		  <%}%>
		 	<td nowrap ><%=list.get(i).get("TOTAL")%>&nbsp; </td>
		 	<td nowrap > 
		 	<%=CommonUtils.getPercent(new Object[]{list.get(i).get("TOTAL"),request.getAttribute("total")})%>
		 	&nbsp; </td>
		</tr>
  <%}%>
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
