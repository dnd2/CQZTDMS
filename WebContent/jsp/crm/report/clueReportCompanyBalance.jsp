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
<title>公司线索总量效率分析报表</title>
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
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>公司线索总量效率分析报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
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
		<c:if test="${ !empty dutyType}" >
			<th  nowrap class="scrollRowThead scrollCR" >销售组</th>
			<th  nowrap  class="scrollRowThead scrollCR" >销售顾问</th>
	    </c:if>
		<th   class="scrollRowThead scrollCR"  >总量</th>
		<th   class="scrollRowThead scrollCR"  >处理总量</th>	
		<c:forEach items="${codeList}" var="clist">
			<th   class="scrollRowThead scrollCR" >${clist.CODE_DESC}</th>	
			<th   class="scrollRowThead scrollCR" >占比</th>
		</c:forEach>
		<th   class="scrollRowThead scrollCR"  >重复</th>
	</tr>
	<% List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("balanceList");  %>
		<% for(int i=0;i<list.size();i++){ %>
		<tr >
			<td nowrap ><%=list.get(i).get("ROOT_ORG_NAME")%>&nbsp; </td>
			<td nowrap ><%=list.get(i).get("PQ_ORG_NAME")%>&nbsp;</td>
			<td nowrap><%=list.get(i).get("DEALER_CODE")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("DEALER_SHORTNAME")%>&nbsp;</td>
			<c:if test="${ !empty dutyType}" >
				<td nowrap ><%=list.get(i).get("GROUP_NAME")%>&nbsp;</td>
				<td nowrap ><%=list.get(i).get("DNAME")%>&nbsp;</td>
			</c:if>
			<td nowrap ><%=list.get(i).get("LYTOTAL")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("XSTOTAL")%>&nbsp;</td>
<% List<Map<String,Object>> cList = (List<Map<String,Object>>)request.getAttribute("codeList");  %>
		<% for(int j=0;j<cList.size();j++){ %>
				<td nowrap ><%=list.get(i).get("COUNT"+cList.get(j).get("CODE_ID")) %> </td>
				<td nowrap > <%=list.get(i).get("B"+cList.get(j).get("CODE_ID")) %> </td>
		  <%}%>
		 	<td nowrap ><%=list.get(i).get("CFTOTAL")%>&nbsp; </td>
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
