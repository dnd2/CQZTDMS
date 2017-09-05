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
<title>线索来源效率分析报表</title>
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
height:650px;
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
	var openUrl='<%=contextPath%>/crm/report/ClueReportTotal/getDealerList.do'+parameter;
	//var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>线索来源效率分析报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
		<input type="hidden" id="dealerCode" value="${dealerCode}" />
		<input type="hidden" id="startDate" value="${startDate}" />
		<input type="hidden" id="endDate" value="${endDate}" />
		<input type="hidden" id="seriesId" value="${seriesId}" />
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	 
	<tr class="scrollColThead">
		<th  nowrap  class="scrollRowThead scrollCR" >大区</th>
		<th  nowrap  class="scrollRowThead scrollCR" >省份</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商代码"  >代码</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商简称"  >简称</th>
		<c:if test="${ !empty dutyType}" >
			<th  nowrap  class="scrollRowThead scrollCR" title="销售组" >销售组</th>
			<th  nowrap  class="scrollRowThead scrollCR" title="销售顾问" >销售顾问</th>
		</c:if>
		<th   class="scrollRowThead scrollCR"  title='线索中顾问处理过数据加顾问未处理过数据的合计'  >线索总量</th>
		<th   class="scrollRowThead scrollCR"  title="线索中顾问未处理过的数据总量" >线索未处理量</th>
		<th   class="scrollRowThead scrollCR"  title="线索中顾问处理过的数据总量(24小时内及外顾问处理过的数据)" >线索处理总量</th>	
		<c:forEach items="${codeList}" var="clist">
		   
			<th   class="scrollRowThead scrollCR" title="${ clist.CODE_DESC }"   > 
			 <c:choose>
			<c:when test="${clist.CODE_ID == 60151007}">
			  	         车巡展</br>/路演 
			</c:when>
			<c:when test="${clist.CODE_ID == 60151008}">
					品牌</br>体验</br>活动  
			</c:when>
			<c:when test="${clist.CODE_ID == 60151016}">
					 转介绍</br>及其他  
			</c:when>
			<c:when test="${clist.CODE_ID == 60151020}">
					  新媒体  
			</c:when>
			<c:when test="${clist.CODE_ID == 60151021}">
					社会化</br>媒体 
			</c:when>
			<c:otherwise>
					  ${ clist.CODE_DESC } 
			</c:otherwise>
		</c:choose>
			</th>	
			<th   class="scrollRowThead scrollCR" title="占比" >占比</th>
		</c:forEach>
	</tr>
	<% List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("balanceList");  %>
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
			<c:if test="${ !empty dutyType}" >
			<td nowrap ><%=list.get(i).get("GROUP_NAME")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("DNAME")%>&nbsp;</td>
		   </c:if>
			<td nowrap ><%=list.get(i).get("XSTOTAL")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("WCLTOTAL")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("LYTOTAL")%>&nbsp;</td>
<% List<Map<String,Object>> cList = (List<Map<String,Object>>)request.getAttribute("codeList");  %>
		<% for(int j=0;j<cList.size();j++){ %>
				<td nowrap  ><%=list.get(i).get("COUNT"+cList.get(j).get("CODE_ID")) %>&nbsp; </td>
				<td nowrap    > <%=list.get(i).get("B"+cList.get(j).get("CODE_ID")) %> </td>
		  <%}%>
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
