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
<title>集团客户合同</title>



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
	<strong>集团客户合同&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="22" class="scrollRowThead scrollCR juzuo"><strong>集团客户合同</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="22" class="scrollRowThead scrollCR" align="left" style="left: auto"><div align="left">日期：${checkDate1}--${checkDate2}</div></td>
	</tr>
	
	<tr class="scrollColThead">
			<th class="scrollRowThead scrollCR" width="7%">大区</th>
			<th class="scrollRowThead scrollCR" width="7%">省份</th>
			<th class="scrollRowThead scrollCR" width="7%">提报单位</th>
			<th class="scrollRowThead scrollCR" width="7%">提报日期</th>
			<th class="scrollRowThead scrollCR" width="7%">客户名称</th>
			<th class="scrollRowThead scrollCR" width="7%">客户类型</th>
			<th class="scrollRowThead scrollCR" width="7%">合同编号</th>
			<th class="scrollRowThead scrollCR" width="7%">买方</th>
			<th class="scrollRowThead scrollCR" width="7%">卖方</th>
			<th class="scrollRowThead scrollCR" width="7%">签约日期</th>
			<th class="scrollRowThead scrollCR" width="7%">有效日期起</th>
			<th class="scrollRowThead scrollCR" width="7%">有效日期止</th>
			<th class="scrollRowThead scrollCR" width="7%">签约车系</th>
			<th class="scrollRowThead scrollCR" width="7%">数量</th>
			<th class="scrollRowThead scrollCR" width="7%">标准价</th>
			<th class="scrollRowThead scrollCR" width="7%">合计金额</th>
			<th class="scrollRowThead scrollCR" width="7%">折扣点</th>
			<th class="scrollRowThead scrollCR" width="7%">特殊金额</th>
			<th class="scrollRowThead scrollCR" width="7%">折让金额</th>
			<th class="scrollRowThead scrollCR" width="7%">特殊要求</th>
			<th class="scrollRowThead scrollCR" width="7%">审核日期</th>
	</tr>
	
	<c:forEach items="${list_FLEETCONTRACT}" var="list_FLEETCONTRACT">
	<tr >
		<td>${list_FLEETCONTRACT.ORG_NAME}&nbsp;</td>
		<td>${list_FLEETCONTRACT.REGION_NAME}&nbsp;</td>
		<td>${list_FLEETCONTRACT.COMPANY_SHORTNAME}&nbsp;</td>
		<td>${list_FLEETCONTRACT.SUBMIT_DATE}&nbsp;</td>
		<td>${list_FLEETCONTRACT.FLEET_NAME}&nbsp;</td>
		<td>${list_FLEETCONTRACT.FLEET_TYPE}&nbsp;</td>
		<td>${list_FLEETCONTRACT.CONTRACT_NO}&nbsp;</td>
		<td>${list_FLEETCONTRACT.BUY_FROM}&nbsp;</td>
		<td>${list_FLEETCONTRACT.SELL_TO}&nbsp;</td>
		<td>${list_FLEETCONTRACT.CHECK_DATE}&nbsp;</td>
		<td>${list_FLEETCONTRACT.START_DATE}&nbsp;</td>
		<td>${list_FLEETCONTRACT.END_DATE}&nbsp;</td>
		<td>${list_FLEETCONTRACT.GROUP_NAME}&nbsp;</td>
		<td>${list_FLEETCONTRACT.INTENT_COUNT}&nbsp;</td>
		
		
			<c:if test="${list_FLEETCONTRACT.NORM_AMOUNT==0}">
		<td>  &nbsp; </td>
		</c:if>
		<c:if test="${list_FLEETCONTRACT.NORM_AMOUNT!=0}">
		<td>${list_FLEETCONTRACT.NORM_AMOUNT}&nbsp;</td>
		</c:if>
		
		
		<c:if test="${list_FLEETCONTRACT.COUNT_AMOUNT==0}">
		<td> &nbsp;  </td>
		</c:if>
		<c:if test="${list_FLEETCONTRACT.COUNT_AMOUNT!=0}">
		<td>${list_FLEETCONTRACT.COUNT_AMOUNT}&nbsp;</td>
		</c:if>
		
		<c:if test="${list_FLEETCONTRACT.INTENT_POINT==0}">
		<td>  &nbsp; </td>
		</c:if>
		<c:if test="${list_FLEETCONTRACT.INTENT_POINT!=0}">
		<td>${list_FLEETCONTRACT.INTENT_POINT}&nbsp;</td>
		</c:if>

		<c:if test="${list_FLEETCONTRACT.OTHER_AMOUNT==0}">
		<td> &nbsp;  </td>
		</c:if>
		<c:if test="${list_FLEETCONTRACT.OTHER_AMOUNT!=0}">
		<td>${list_FLEETCONTRACT.OTHER_AMOUNT}&nbsp;</td>
		</c:if>
		
		<c:if test="${list_FLEETCONTRACT.DIS_AMOUNT==0}">
		<td>  &nbsp; </td>
		</c:if>
		<c:if test="${list_FLEETCONTRACT.DIS_AMOUNT!=0}">
		<td>${list_FLEETCONTRACT.DIS_AMOUNT}&nbsp;</td>
		</c:if>
		
		<td>${list_FLEETCONTRACT.OTHER_REMARK}&nbsp;</td>
		<td>${list_FLEETCONTRACT.AUDIT_DATE}&nbsp;</td>
	
		
	
	</tr>
	
	 </c:forEach>
	 <tr >
	 	<th  class="scrollRowThead scrollCR" colspan="13" >合计</th>
	 		<c:if test="${intent_count==0}">
		<th class="scrollRowThead scrollCR" width="7%">  &nbsp; </th>
		</c:if>
		<c:if test="${intent_count!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${intent_count}&nbsp;</th>
		</c:if>
	 	
	 		<c:if test="${norm_amount==0}">
		<th class="scrollRowThead scrollCR" width="7%">  &nbsp; </th>
		</c:if>
		<c:if test="${norm_amount!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${norm_amount}&nbsp;</th>
		</c:if>
	 	
	 	<c:if test="${count_amount==0}">
		<th class="scrollRowThead scrollCR" width="7%">  &nbsp; </th>
		</c:if>
		<c:if test="${count_amount!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${count_amount}&nbsp;</th>
		</c:if>
		
	 		<c:if test="${intent_point==0}">
		<th class="scrollRowThead scrollCR" width="7%">  &nbsp; </th>
		</c:if>
		<c:if test="${intent_point!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${intent_point}&nbsp;</th>
		</c:if>
		
	 		<c:if test="${other_amount==0}">
		<th class="scrollRowThead scrollCR" width="7%">  &nbsp; </th>
		</c:if>
		<c:if test="${other_amount!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${other_amount}&nbsp;</th>
		</c:if>
	 
	 	<c:if test="${dis_amount==0}">
		<th class="scrollRowThead scrollCR" width="7%"> &nbsp;  </th>
		</c:if>
		<c:if test="${dis_amount!=0}">
		<th class="scrollRowThead scrollCR" width="7%">${dis_amount}&nbsp;</th>
		</c:if>
	 	<th colspan="2" class="scrollRowThead scrollCR">
	 		&nbsp;
	 	</th>
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
