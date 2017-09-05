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
<title>潜客分析报表</title>
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
	var openUrl='<%=contextPath%>/crm/report/PotentialCustomerAnalysisReport/getDealerList.do'+parameter;
 	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>潜客分析报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
	<tr  class="scrollColThead">
		<c:if test="${ !empty dutyType}" >
		 <th colspan="6" class="scrollRowThead scrollCR">信息</th>
		</c:if>
		<c:if test="${ empty dutyType}" >
		 <th colspan="4" class="scrollRowThead scrollCR">信息</th>
		 </c:if>
		 <th colspan="5" class="scrollRowThead scrollCR">H级</th>
		 <th colspan="5" class="scrollRowThead scrollCR">A级</th>
		 <th colspan="5" class="scrollRowThead scrollCR">B级</th>
		 <th colspan="5" class="scrollRowThead scrollCR">C级</th>
		 <th colspan="4" class="scrollRowThead scrollCR">O级</th>
		 <th colspan="4" class="scrollRowThead scrollCR">E级</th>
		 <th colspan="4" class="scrollRowThead scrollCR">L级</th>
		 
		 <th colspan="3" class="scrollRowThead scrollCR">合计(HAB)</th>
		 <th colspan="3" class="scrollRowThead scrollCR">合计(HABC)</th>
		
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

		<th   class="scrollRowThead scrollCR" title='新增的H级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的H级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的H级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的H级客户历史数据客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='H级客户占总的HABC客户数的百分比'  >当月新增占比</th>
		
		<th   class="scrollRowThead scrollCR" title='新增的A级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的A级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的A级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的A级客户历史数据客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='A级客户占总的HABC客户数的百分比'  >当月新增占比</th>
		
		<th   class="scrollRowThead scrollCR" title='新增的B级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的B级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的B级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的B级客户历史数据客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='B级客户占总的HABC客户数的百分比'  >当月新增占比</th>

		<th   class="scrollRowThead scrollCR" title='新增的C级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的C级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的C级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的C级客户历史数据客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='C级客户占总的HABC客户数的百分比'  >当月新增占比</th>
		
		<th   class="scrollRowThead scrollCR" title='新增的O级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的O级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的O级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的O级客户历史数据客户平均卡龄'  >平均卡龄</th>
		
		
		<th   class="scrollRowThead scrollCR" title='新增的E级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的E级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的E级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的E级客户历史数据客户平均卡龄'  >平均卡龄</th>
		
		<th   class="scrollRowThead scrollCR" title='新增的L级客户数'  >当月新增</th>
		<th   class="scrollRowThead scrollCR" title='新增的L级客户平均卡龄'  >平均卡龄</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的L级客户历史数据'  >前期留存</th>
		<th   class="scrollRowThead scrollCR" title='本月已前的L级客户历史数据客户平均卡龄'  >平均卡龄</th>
		
		
		<th   class="scrollRowThead scrollCR" title='新增的HAB级客户总数'  >当月新增</th>
	    <th   class="scrollRowThead scrollCR" title='本月已前的HAB级客户历史总数据'  >前期留存</th>
	    <th   class="scrollRowThead scrollCR" title='新增的HAB级客户总数+本月已前的HAB级客户历史总数据'  >小计</th>
	    
	    <th   class="scrollRowThead scrollCR" title='新增的HABC级客户总数'  >当月新增</th>
	    <th   class="scrollRowThead scrollCR" title='本月已前的HABC级客户历史总数据'  >前期留存</th>
	    <th   class="scrollRowThead scrollCR" title='新增的HABC级客户总数+本月已前的HABC级客户历史总数据'  >小计</th>
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
			<td nowrap >${list.HXZ}&nbsp; </td>
			<td nowrap >${list.HPJKL}&nbsp; </td>
			<td nowrap >${list.HLC}&nbsp; </td> 
			<td nowrap >${list.HLCKL}&nbsp; </td>
			<td nowrap >${list.HXZRATE}&nbsp; </td>
			<td nowrap >${list.AXZ}&nbsp; </td>
			<td nowrap >${list.APJKL}&nbsp; </td>
			<td nowrap >${list.ALC}&nbsp; </td>
			<td nowrap >${list.ALCKL}&nbsp; </td>
			<td nowrap >${list.AXZRATE}&nbsp; </td>
			<td nowrap >${list.BXZ}&nbsp; </td>
			<td nowrap >${list.BPJKL}&nbsp; </td>
			<td nowrap >${list.BLC}&nbsp; </td>
			<td nowrap >${list.BLCKL}&nbsp; </td>
			<td nowrap >${list.BXZRATE}&nbsp; </td>
			<td nowrap >${list.CXZ}&nbsp; </td>
			<td nowrap >${list.CPJKL}&nbsp; </td>
			<td nowrap >${list.CLC}&nbsp; </td>
			<td nowrap >${list.CLCKL}&nbsp; </td>
			<td nowrap >${list.CXZRATE}&nbsp; </td>
			
			<td nowrap >${list.OXZ}&nbsp; </td>
			<td nowrap >${list.OPJKL}&nbsp; </td>
			<td nowrap >${list.OLC}&nbsp; </td>
			<td nowrap >${list.OLCKL}&nbsp; </td>
			
			
			<td nowrap >${list.EXZ}&nbsp; </td>
			<td nowrap >${list.EPJKL}&nbsp; </td>
			<td nowrap >${list.ELC}&nbsp; </td>
			<td nowrap >${list.ELCKL}&nbsp; </td>
			
			
			<td nowrap >${list.LXZ}&nbsp; </td>
			<td nowrap >${list.LPJKL}&nbsp; </td>
			<td nowrap >${list.LLC}&nbsp; </td>
			<td nowrap >${list.LLCKL}&nbsp; </td>
			
			
			<td nowrap >${list.HABXZ}&nbsp; </td>
			<td nowrap >${list.HABLC}&nbsp; </td>
			<td nowrap >${list.HABXZLC}&nbsp; </td>
			<td nowrap >${list.HABCXZ}&nbsp; </td>
			<td nowrap >${list.HABCLC}&nbsp; </td>
			<td nowrap >${list.HABCXZLC}&nbsp; </td>
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
