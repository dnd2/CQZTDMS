<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/jstl/fn" prefix="fn" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<% 
	String contextPath = request.getContextPath(); 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常规订单监控看板</title>
<script type="text/javascript">
	
	//计数器
	var count = 0;

	/* function doInit(){
		sum_no();
		sum_jidi();
	} */
	
	function selectMonth(month) {
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/OrderReport/orderReportByMonth.do";
		$('fm').submit();
	}
</script>
<style type="text/css">
	.table_list_row3 td {
    /* background-color: #F7FFF7; */
    border: 1px solid #DAE0EE;
    white-space: nowrap;
    font-size:12px;
    text-align: center;
}
</style>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>常规订单监控看板</strong>
</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="refresh();" value="刷新">
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
<div>
	<input type="radio" name="month" <c:if test="${monthStatus == -1}">checked="checked"</c:if> onclick="selectMonth(this.value)" value="${lastMonth}"/>(${lastDate})
	<input type="radio" name="month" <c:if test="${monthStatus == 0}">checked="checked"</c:if> onclick="selectMonth(this.value)" value="${currentMonth}"/>(${currentDate})
	<input type="radio" name="month" <c:if test="${monthStatus == 1}">checked="checked"</c:if> onclick="selectMonth(this.value)" value="${nextMonth}"/>(${nextDate})
</div>
<table border="0" align="center" class="table_list" id="activeTable">
	<tr class="table_list_th_report">
		<th rowspan="2"><font style="font-size: 17px">业务<br />范围</font></th>
		<th rowspan="2"><font style="font-size: 17px">大区</font></th>
		<th rowspan="2"><font style="font-size: 17px">车系</font></th>
		<th rowspan="2"><font style="font-size: 17px">月度<br />目标</font></th>
		<th colspan="2"><font style="font-size: 17px">需求计划</font></th>
		<th colspan="2"><font style="font-size: 17px">配额</font></th>
		<th rowspan="2"><font style="font-size: 17px">提报</font></th>
		<th rowspan="2"><font style="font-size: 17px">发运<br />申请</font></th>
		<th rowspan="2"><font style="font-size: 17px">资源<br />保留</font></th>
		<th rowspan="2"><font style="font-size: 17px">开票</font></th>
		<th rowspan="2"><font style="font-size: 17px">发运</font></th>
		<th rowspan="2"><font style="font-size: 17px">提报率</font></th>
		<th rowspan="2"><font style="font-size: 17px">执行率</font></th>
		<th rowspan="2"><font style="font-size: 17px">常规订<br />单占比</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th>经销商</th>
		<th>大区</th>
		<th>下发给大区</th>
		<th>下发给经销商</th>
	</tr>
	
	<!-- 百分比显示格式 -->
	<%  java.text.DecimalFormat   df = new   java.text.DecimalFormat("#"); %>
	
	<!-- 大区小计 -->
	<% 
		Map<String, BigDecimal> orgTotal = new LinkedHashMap<String, BigDecimal>();
		orgTotal.put("SALE_AMOUNT", BigDecimal.ZERO);
		orgTotal.put("DLR_FORECAST", BigDecimal.ZERO);
		orgTotal.put("OEM_FORECAST", BigDecimal.ZERO);
		orgTotal.put("OEM_QUATA", BigDecimal.ZERO);
		orgTotal.put("DLR_QUATA", BigDecimal.ZERO);
		orgTotal.put("TOTALORDER", BigDecimal.ZERO);
		orgTotal.put("TOTALREQ", BigDecimal.ZERO);
		orgTotal.put("TOTALRESERVE", BigDecimal.ZERO);
		orgTotal.put("TOTALDELIVERY", BigDecimal.ZERO);
		orgTotal.put("TOTALDELIVERYB", BigDecimal.ZERO);
		orgTotal.put("TOTALDELIVERYD", BigDecimal.ZERO);
		orgTotal.put("TOTALMATCH", BigDecimal.ZERO);
		request.setAttribute("orgTotal", orgTotal);
	%>
	
	<!-- 业务范围小计 -->
	<% 
		Map<String, BigDecimal> businessAreaTotal = new LinkedHashMap<String, BigDecimal>();
		businessAreaTotal.put("SALE_AMOUNT", BigDecimal.ZERO);
		businessAreaTotal.put("DLR_FORECAST", BigDecimal.ZERO);
		businessAreaTotal.put("OEM_FORECAST", BigDecimal.ZERO);
		businessAreaTotal.put("OEM_QUATA", BigDecimal.ZERO);
		businessAreaTotal.put("DLR_QUATA", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALORDER", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALREQ", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALRESERVE", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALDELIVERY", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALDELIVERYB", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALDELIVERYD", BigDecimal.ZERO);
		businessAreaTotal.put("TOTALMATCH", BigDecimal.ZERO);
		request.setAttribute("businessAreaTotal", businessAreaTotal);
	%>
	<!-- 总合计 -->
	<% 
		Map<String, BigDecimal> total = new LinkedHashMap<String, BigDecimal>();
		total.put("SALE_AMOUNT", BigDecimal.ZERO);
		total.put("DLR_FORECAST", BigDecimal.ZERO);
		total.put("OEM_FORECAST", BigDecimal.ZERO);
		total.put("OEM_QUATA", BigDecimal.ZERO);
		total.put("DLR_QUATA", BigDecimal.ZERO);
		total.put("TOTALORDER", BigDecimal.ZERO);
		total.put("TOTALREQ", BigDecimal.ZERO);
		total.put("TOTALRESERVE", BigDecimal.ZERO);
		total.put("TOTALDELIVERY", BigDecimal.ZERO);
		total.put("TOTALDELIVERYB", BigDecimal.ZERO);
		total.put("TOTALDELIVERYD", BigDecimal.ZERO);
		total.put("TOTALMATCH", BigDecimal.ZERO);
		request.setAttribute("total", total);
	%>
	
	<!-- 计算因小计而增加的行数 -->
	<c:forEach items="${orderBoardList}" var="orderBoard" varStatus="status">
		<c:choose>
			<c:when test="${orderBoardList[status.index-1].AREA_NAME == orderBoard.AREA_NAME && orderBoardList[status.index+1].ORG_NAME != orderBoard.ORG_NAME || orderBoardList[status.index+1].AREA_NAME != orderBoard.AREA_NAME}">
				<c:forEach items="${areaCountList}" var="area" varStatus="areaStatus">
					<c:if test="${area.AREA_NAME == orderBoard.AREA_NAME}">
						<c:set target="${areaCountList[areaStatus.index]}" property="AREACOUNT" value="${area.AREACOUNT+1}" />
					</c:if>
				</c:forEach>
			</c:when>
		</c:choose>
	</c:forEach>
	
	<!-- 计算小计和合计值 -->
	<c:forEach items="${orderBoardList}" var="orderBoard" varStatus="status">
		<c:choose>
			<c:when test="${status.first == true || 
				orderBoardList[status.index-1].AREA_NAME == orderBoard.AREA_NAME && orderBoardList[status.index-1].ORG_NAME == orderBoard.ORG_NAME}">
			
				<c:set target="${businessAreaTotal}" property="SALE_AMOUNT" value="${businessAreaTotal.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${businessAreaTotal}" property="DLR_FORECAST" value="${businessAreaTotal.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_FORECAST" value="${businessAreaTotal.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_QUATA" value="${businessAreaTotal.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${businessAreaTotal}" property="DLR_QUATA" value="${businessAreaTotal.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${businessAreaTotal}" property="TOTALORDER" value="${businessAreaTotal.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${businessAreaTotal}" property="TOTALREQ" value="${businessAreaTotal.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${businessAreaTotal}" property="TOTALRESERVE" value="${businessAreaTotal.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERY" value="${businessAreaTotal.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYB" value="${businessAreaTotal.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYD" value="${businessAreaTotal.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${businessAreaTotal}" property="TOTALMATCH" value="${businessAreaTotal.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<c:set target="${orgTotal}" property="SALE_AMOUNT" value="${orgTotal.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${orgTotal}" property="DLR_FORECAST" value="${orgTotal.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_FORECAST" value="${orgTotal.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_QUATA" value="${orgTotal.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${orgTotal}" property="DLR_QUATA" value="${orgTotal.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${orgTotal}" property="TOTALORDER" value="${orgTotal.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${orgTotal}" property="TOTALREQ" value="${orgTotal.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${orgTotal}" property="TOTALRESERVE" value="${orgTotal.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERY" value="${orgTotal.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYB" value="${orgTotal.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYD" value="${orgTotal.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${orgTotal}" property="TOTALMATCH" value="${orgTotal.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<c:set target="${total}" property="SALE_AMOUNT" value="${total.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${total}" property="DLR_FORECAST" value="${total.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${total}" property="OEM_FORECAST" value="${total.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${total}" property="OEM_QUATA" value="${total.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${total}" property="DLR_QUATA" value="${total.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${total}" property="TOTALORDER" value="${total.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${total}" property="TOTALREQ" value="${total.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${total}" property="TOTALRESERVE" value="${total.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${total}" property="TOTALDELIVERY" value="${total.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${total}" property="TOTALDELIVERYB" value="${total.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${total}" property="TOTALDELIVERYD" value="${total.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${total}" property="TOTALMATCH" value="${total.TOTALMATCH + orderBoard.TOTALMATCH}" />
			</c:when>
			
			<c:when test="${status.last == true}">
			
				<c:set target="${businessAreaTotal}" property="SALE_AMOUNT" value="${businessAreaTotal.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${businessAreaTotal}" property="DLR_FORECAST" value="${businessAreaTotal.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_FORECAST" value="${businessAreaTotal.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_QUATA" value="${businessAreaTotal.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${businessAreaTotal}" property="DLR_QUATA" value="${businessAreaTotal.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${businessAreaTotal}" property="TOTALORDER" value="${businessAreaTotal.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${businessAreaTotal}" property="TOTALREQ" value="${businessAreaTotal.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${businessAreaTotal}" property="TOTALRESERVE" value="${businessAreaTotal.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERY" value="${businessAreaTotal.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYB" value="${businessAreaTotal.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYD" value="${businessAreaTotal.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${businessAreaTotal}" property="TOTALMATCH" value="${businessAreaTotal.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<c:set target="${total}" property="SALE_AMOUNT" value="${total.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${total}" property="DLR_FORECAST" value="${total.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${total}" property="OEM_FORECAST" value="${total.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${total}" property="OEM_QUATA" value="${total.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${total}" property="DLR_QUATA" value="${total.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${total}" property="TOTALORDER" value="${total.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${total}" property="TOTALREQ" value="${total.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${total}" property="TOTALRESERVE" value="${total.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${total}" property="TOTALDELIVERY" value="${total.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${total}" property="TOTALDELIVERYB" value="${total.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${total}" property="TOTALDELIVERYD" value="${total.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${total}" property="TOTALMATCH" value="${total.TOTALMATCH + orderBoard.TOTALMATCH}" />
			</c:when>
			
			<c:when test="${orderBoardList[status.index-1].AREA_NAME == orderBoard.AREA_NAME && orderBoardList[status.index-1].ORG_NAME != orderBoard.ORG_NAME}">
				
				<c:set target="${businessAreaTotal}" property="SALE_AMOUNT" value="${businessAreaTotal.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${businessAreaTotal}" property="DLR_FORECAST" value="${businessAreaTotal.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_FORECAST" value="${businessAreaTotal.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_QUATA" value="${businessAreaTotal.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${businessAreaTotal}" property="DLR_QUATA" value="${businessAreaTotal.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${businessAreaTotal}" property="TOTALORDER" value="${businessAreaTotal.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${businessAreaTotal}" property="TOTALREQ" value="${businessAreaTotal.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${businessAreaTotal}" property="TOTALRESERVE" value="${businessAreaTotal.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERY" value="${businessAreaTotal.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYB" value="${businessAreaTotal.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYD" value="${businessAreaTotal.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${businessAreaTotal}" property="TOTALMATCH" value="${businessAreaTotal.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<c:set target="${total}" property="SALE_AMOUNT" value="${total.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${total}" property="DLR_FORECAST" value="${total.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${total}" property="OEM_FORECAST" value="${total.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${total}" property="OEM_QUATA" value="${total.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${total}" property="DLR_QUATA" value="${total.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${total}" property="TOTALORDER" value="${total.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${total}" property="TOTALREQ" value="${total.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${total}" property="TOTALRESERVE" value="${total.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${total}" property="TOTALDELIVERY" value="${total.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${total}" property="TOTALDELIVERYB" value="${total.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${total}" property="TOTALDELIVERYD" value="${total.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${total}" property="TOTALMATCH" value="${total.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<tr class="table_list_row2">
					<td colspan="2">
						小计
					</td>
					<c:forEach items="${orgTotal}" var="orgItem">
						<c:if test="${orgItem.key != 'TOTALDELIVERYB' && orgItem.key != 'TOTALDELIVERYD'}">
							<td>${orgItem.value}</td>
						</c:if>
					</c:forEach>
					<!-- 提报率 = 提报数/下发给经销商配额-->
					<td>
						<c:if test="${orgTotal.TOTALORDER != 0 }">
							<%= df.format(orgTotal.get("TOTALORDER").doubleValue()*100 / orgTotal.get("DLR_QUATA").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 执行率 = 开票数/提报-->
					<td>
						<c:if test="${orgTotal.TOTALDELIVERY != 0}">
							<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / orgTotal.get("TOTALORDER").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
					<td>
						<c:if test="${orgTotal.TOTALDELIVERY != 0 }">
							<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / (orgTotal.get("TOTALDELIVERY").doubleValue() + orgTotal.get("TOTALDELIVERYB").doubleValue() + orgTotal.get("TOTALDELIVERYD").doubleValue())) %>%
						</c:if>
						<c:set var="index" value="${index+1}"></c:set>
					</td>
				</tr>
				<!-- 初始化大区 -->
				<c:set target="${orgTotal}" property="SALE_AMOUNT" value="${orderBoard.SALE_AMOUNT}" />
				<c:set target="${orgTotal}" property="DLR_FORECAST" value="${orderBoard.DLR_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_FORECAST" value="${orderBoard.OEM_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_QUATA" value="${orderBoard.OEM_QUATA}" />
				<c:set target="${orgTotal}" property="DLR_QUATA" value="${orderBoard.DLR_QUATA}" />
				<c:set target="${orgTotal}" property="TOTALORDER" value="${orderBoard.TOTALORDER}" />
				<c:set target="${orgTotal}" property="TOTALREQ" value="${orderBoard.TOTALREQ}" />
				<c:set target="${orgTotal}" property="TOTALRESERVE" value="${orderBoard.TOTALRESERVE}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERY" value="${orderBoard.TOTALDELIVERY}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYB" value="${orderBoard.TOTALDELIVERYB}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYD" value="${orderBoard.TOTALDELIVERYD}" />
				<c:set target="${orgTotal}" property="TOTALMATCH" value="${orderBoard.TOTALMATCH}" />
			</c:when>
			
			<c:when test="${orderBoardList[status.index-1].AREA_NAME != orderBoard.AREA_NAME}">
				
				<c:set target="${total}" property="SALE_AMOUNT" value="${total.SALE_AMOUNT + orderBoard.SALE_AMOUNT}" />
				<c:set target="${total}" property="DLR_FORECAST" value="${total.DLR_FORECAST + orderBoard.DLR_FORECAST}" />
				<c:set target="${total}" property="OEM_FORECAST" value="${total.OEM_FORECAST + orderBoard.OEM_FORECAST}" />
				<c:set target="${total}" property="OEM_QUATA" value="${total.OEM_QUATA + orderBoard.OEM_QUATA}" />
				<c:set target="${total}" property="DLR_QUATA" value="${total.DLR_QUATA + orderBoard.DLR_QUATA}" />
				<c:set target="${total}" property="TOTALORDER" value="${total.TOTALORDER + orderBoard.TOTALORDER}" />
				<c:set target="${total}" property="TOTALREQ" value="${total.TOTALREQ + orderBoard.TOTALREQ}" />
				<c:set target="${total}" property="TOTALRESERVE" value="${total.TOTALRESERVE + orderBoard.TOTALRESERVE}" />
				<c:set target="${total}" property="TOTALDELIVERY" value="${total.TOTALDELIVERY + orderBoard.TOTALDELIVERY}" />
				<c:set target="${total}" property="TOTALDELIVERYB" value="${total.TOTALDELIVERYB + orderBoard.TOTALDELIVERYB}" />
				<c:set target="${total}" property="TOTALDELIVERYD" value="${total.TOTALDELIVERYD + orderBoard.TOTALDELIVERYD}" />
				<c:set target="${total}" property="TOTALMATCH" value="${total.TOTALMATCH + orderBoard.TOTALMATCH}" />
				
				<tr class="table_list_row2">
					<td colspan="2">
						小计
					</td>
					<c:forEach items="${orgTotal}" var="orgItem">
					<c:if test="${orgItem.key != 'TOTALDELIVERYB' && orgItem.key != 'TOTALDELIVERYD'}">
						<td>${orgItem.value}</td>
					</c:if>
					</c:forEach>
					<!-- 提报率 = 提报数/下发给经销商配额-->
					<td>
						<c:if test="${orgTotal.TOTALORDER != 0 }">
							<%= df.format(orgTotal.get("TOTALORDER").doubleValue()*100 / orgTotal.get("DLR_QUATA").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 执行率 = 开票数/提报-->
					<td>
						<c:if test="${orgTotal.TOTALDELIVERY != 0}">
							<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / orgTotal.get("TOTALORDER").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
					<td>
						<c:if test="${orgTotal.TOTALDELIVERY != 0}">
							<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / (orgTotal.get("TOTALDELIVERY").doubleValue() + orgTotal.get("TOTALDELIVERYB").doubleValue() + orgTotal.get("TOTALDELIVERYD").doubleValue())) %>%
						</c:if>
						<c:set var="index" value="${index+1}"></c:set>
					</td>
				</tr>
				
				<tr class="table_list_row2">
					<td colspan="3">
						小计
					</td>
					<c:forEach items="${businessAreaTotal}" var="businessAreaItem">
						<c:if test="${businessAreaItem.key != 'TOTALDELIVERYB' && businessAreaItem.key != 'TOTALDELIVERYD'}">
							<td>${businessAreaItem.value}</td>
						</c:if>
					</c:forEach>
					<!-- 提报率 = 提报数/下发给经销商配额-->
					<td>
						<c:if test="${businessAreaTotal.TOTALORDER != 0}">
							<%= df.format(businessAreaTotal.get("TOTALORDER").doubleValue()*100 / businessAreaTotal.get("DLR_QUATA").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 执行率 = 开票数/提报-->
					<td>
						<c:if test="${businessAreaTotal.TOTALDELIVERY != 0}">
							<%= df.format(businessAreaTotal.get("TOTALDELIVERY").doubleValue()*100 / businessAreaTotal.get("TOTALORDER").doubleValue()) %>%
						</c:if>
					</td>
					<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
					<td>
						<c:if test="${businessAreaTotal.TOTALDELIVERY != 0}">
							<%= df.format(businessAreaTotal.get("TOTALDELIVERY").doubleValue()*100 / (businessAreaTotal.get("TOTALDELIVERY").doubleValue() + businessAreaTotal.get("TOTALDELIVERYB").doubleValue() + businessAreaTotal.get("TOTALDELIVERYD").doubleValue())) %>%
						</c:if>
						<c:set var="index" value="${index+1}"></c:set>
					</td>
				</tr>
				
				<!-- 初始化大区和业务范围统计 -->
				<c:set target="${orgTotal}" property="SALE_AMOUNT" value="${orderBoard.SALE_AMOUNT}" />
				<c:set target="${orgTotal}" property="DLR_FORECAST" value="${orderBoard.DLR_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_FORECAST" value="${orderBoard.OEM_FORECAST}" />
				<c:set target="${orgTotal}" property="OEM_QUATA" value="${orderBoard.OEM_QUATA}" />
				<c:set target="${orgTotal}" property="DLR_QUATA" value="${orderBoard.DLR_QUATA}" />
				<c:set target="${orgTotal}" property="TOTALORDER" value="${orderBoard.TOTALORDER}" />
				<c:set target="${orgTotal}" property="TOTALREQ" value="${orderBoard.TOTALREQ}" />
				<c:set target="${orgTotal}" property="TOTALRESERVE" value="${orderBoard.TOTALRESERVE}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERY" value="${orderBoard.TOTALDELIVERY}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYB" value="${orderBoard.TOTALDELIVERYB}" />
				<c:set target="${orgTotal}" property="TOTALDELIVERYD" value="${orderBoard.TOTALDELIVERYD}" />
				<c:set target="${orgTotal}" property="TOTALMATCH" value="${orderBoard.TOTALMATCH}" />
				
				<c:set target="${businessAreaTotal}" property="SALE_AMOUNT" value="${orderBoard.SALE_AMOUNT}" />
				<c:set target="${businessAreaTotal}" property="DLR_FORECAST" value="${orderBoard.DLR_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_FORECAST" value="${orderBoard.OEM_FORECAST}" />
				<c:set target="${businessAreaTotal}" property="OEM_QUATA" value="${orderBoard.OEM_QUATA}" />
				<c:set target="${businessAreaTotal}" property="DLR_QUATA" value="${orderBoard.DLR_QUATA}" />
				<c:set target="${businessAreaTotal}" property="TOTALORDER" value="${orderBoard.TOTALORDER}" />
				<c:set target="${businessAreaTotal}" property="TOTALREQ" value="${orderBoard.TOTALREQ}" />
				<c:set target="${businessAreaTotal}" property="TOTALRESERVE" value="${orderBoard.TOTALRESERVE}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERY" value="${orderBoard.TOTALDELIVERY}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYB" value="${orderBoard.TOTALDELIVERYB}" />
				<c:set target="${businessAreaTotal}" property="TOTALDELIVERYD" value="${orderBoard.TOTALDELIVERYD}" />
				<c:set target="${businessAreaTotal}" property="TOTALMATCH" value="${orderBoard.TOTALMATCH}" />
			</c:when>
		</c:choose>
		
		<tr class="table_list_row1">
			
			<c:forEach items="${areaCountList}" var="area" varStatus="areaStatus">
				<c:if test="${orderBoardList[status.index-1].AREA_NAME != orderBoard.AREA_NAME && area.AREA_NAME == orderBoard.AREA_NAME}">
					<!-- 业务范围 -->
					<td rowspan="${area.AREACOUNT}" valign="middle">
						${orderBoard.AREA_NAME}
					</td>
				</c:if>
			</c:forEach>
			
			<!-- 大区 -->
			<td>
				${orderBoard.ORG_NAME}
			</td>
			<!-- 车系 -->
			<td>
				${orderBoard.GROUP_NAME}
			</td>
			<!-- 月度目标 -->
			<td>
				<c:if test="${orderBoard.SALE_AMOUNT != 0}">
					${orderBoard.SALE_AMOUNT}
				</c:if>
			</td>
			<!-- 需求计划 (经销商)-->
			<td>
				<c:if test="${orderBoard.DLR_FORECAST != 0}">
					${orderBoard.DLR_FORECAST}
				</c:if>
			</td>
			<!-- 需求计划 (大区)-->
			<td>
				<c:if test="${orderBoard.OEM_FORECAST != 0}">
					${orderBoard.OEM_FORECAST}
				</c:if>
			</td>
			<!-- 配额 (大区)-->
			<td>
				<c:if test="${orderBoard.OEM_QUATA != 0}">
					${orderBoard.OEM_QUATA}
				</c:if>
			</td>
			<!-- 配额 (经销商)-->
			<td>
				<c:if test="${orderBoard.DLR_QUATA != 0}">
					${orderBoard.DLR_QUATA}
				</c:if>
			</td>
			<!-- 提报数 -->
			<td>
				<c:if test="${orderBoard.TOTALORDER != 0}">
					${orderBoard.TOTALORDER}
				</c:if>
			</td>
			<!-- 发运申请数 -->
			<td>
				<c:if test="${orderBoard.TOTALREQ != 0}">
					${orderBoard.TOTALREQ}
				</c:if>
			</td>
			<!-- 资源保留数 -->
			<td>
				<c:if test="${orderBoard.TOTALRESERVE != 0}">
					${orderBoard.TOTALRESERVE}
				</c:if>
			</td>
			<!-- 开票数 -->
			<td>
				<c:if test="${orderBoard.TOTALDELIVERY != 0}">
					${orderBoard.TOTALDELIVERY}
				</c:if>
			</td>
			<!-- 发运数 -->
			<td>
				<c:if test="${orderBoard.TOTALMATCH != 0}">
					${orderBoard.TOTALMATCH}
				</c:if>
			</td>
			<!-- 提报率 = 提报数/下发给经销商配额-->
			<td>
				<c:if test="${orderBoard.ORDERPERCENT != 0}">
					<fmt:formatNumber type="percent" value="${orderBoard.ORDERPERCENT}" ></fmt:formatNumber>
				</c:if>
			</td>
			<!-- 执行率 = 开票数/提报-->
			<td>
				<c:if test="${orderBoard.RUNPERCENT != 0}">
						<fmt:formatNumber type="percent" value="${orderBoard.RUNPERCENT}"></fmt:formatNumber>
				</c:if>
				<c:set var="index" value="${index+1}"></c:set>
			</td>
			<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
			<td>
				<c:if test="${orderBoard.NORMALPERCENT != 0}">
						<fmt:formatNumber type="percent" value="${orderBoard.NORMALPERCENT}"></fmt:formatNumber>
				</c:if>
				<c:set var="index" value="${index+1}"></c:set>
			</td>
		</tr>
	</c:forEach>
	
	<!-- 最后一条小计 -->
	<tr class="table_list_row2">
		<td colspan="2">
			小计
		</td>
		<c:forEach items="${orgTotal}" var="org">
			<c:if test="${org.key != 'TOTALDELIVERYB' && org.key != 'TOTALDELIVERYD'}">
				<td>${org.value}</td>
			</c:if>
		</c:forEach>
		<!-- 提报率 = 提报数/下发给经销商配额-->
		<td>
			<c:if test="${orgTotal.TOTALORDER != 0}">
				<%= df.format(orgTotal.get("TOTALORDER").doubleValue()*100 / orgTotal.get("DLR_QUATA").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 执行率 = 开票数/提报-->
		<td>
			<c:if test="${orgTotal.TOTALDELIVERY != 0}">
				<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / orgTotal.get("TOTALORDER").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
		<td>
			<c:if test="${orgTotal.TOTALDELIVERY != 0}">
				<%= df.format(orgTotal.get("TOTALDELIVERY").doubleValue()*100 / (orgTotal.get("TOTALDELIVERY").doubleValue() + orgTotal.get("TOTALDELIVERYB").doubleValue() + orgTotal.get("TOTALDELIVERYD").doubleValue())) %>%
			</c:if>
			<c:set var="index" value="${index+1}"></c:set>
		</td>
	</tr>
	
	<tr class="table_list_row2">
		<td colspan="3">
			小计
		</td>
		<c:forEach items="${businessAreaTotal}" var="businessArea">
			<c:if test="${businessArea.key != 'TOTALDELIVERYB' && businessArea.key != 'TOTALDELIVERYD'}">
				<td>${businessArea.value}</td>
			</c:if>
		</c:forEach>
		<!-- 提报率 = 提报数/下发给经销商配额-->
		<td>
			<c:if test="${businessAreaTotal.TOTALORDER != 0}">
				<%= df.format(businessAreaTotal.get("TOTALORDER").doubleValue()*100 / businessAreaTotal.get("DLR_QUATA").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 执行率 = 开票数/提报-->
		<td>
			<c:if test="${businessAreaTotal.TOTALDELIVERY != 0}">
				<%= df.format(businessAreaTotal.get("TOTALDELIVERY").doubleValue()*100 / businessAreaTotal.get("TOTALORDER").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
		<td>
			<c:if test="${businessAreaTotal.TOTALDELIVERY != 0}">
				<%= df.format(businessAreaTotal.get("TOTALDELIVERY").doubleValue()*100 / (businessAreaTotal.get("TOTALDELIVERY").doubleValue() + businessAreaTotal.get("TOTALDELIVERYB").doubleValue() + businessAreaTotal.get("TOTALDELIVERYD").doubleValue())) %>%
			</c:if>
			<c:set var="index" value="${index+1}"></c:set>
		</td>
	</tr>
	
	<!-- 总计 -->
	<tr class="table_list_row2">
		<td colspan="3">
			合计
		</td>
		<c:forEach items="${total}" var="t" varStatus="totalStatus">
			<c:if test="${t.key != 'TOTALDELIVERYB' && t.key != 'TOTALDELIVERYD'}">
				<td>${t.value}</td>
			</c:if>
		</c:forEach>
		<!-- 提报率 = 提报数/下发给经销商配额-->
		<td>
			<c:if test="${total.TOTALORDER != 0 }">
				<%= df.format(total.get("TOTALORDER").doubleValue()*100 / total.get("DLR_QUATA").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 执行率 = 开票数/提报-->
		<td>
			<c:if test="${total.TOTALDELIVERY != 0 }">
				<%= df.format(total.get("TOTALDELIVERY").doubleValue()*100 / total.get("TOTALORDER").doubleValue()) %>%
			</c:if>
		</td>
		<!-- 常规订单占比 = 常规订单开票数/所有订单开票数-->
		<td>
			<c:if test="${total.TOTALDELIVERY != 0 }">
				<%= df.format(total.get("TOTALDELIVERY").doubleValue()*100 / (total.get("TOTALDELIVERY").doubleValue() + total.get("TOTALDELIVERYB").doubleValue() + total.get("TOTALDELIVERYD").doubleValue())) %>%
			</c:if>
			<c:set var="index" value="${index+1}"></c:set>
		</td>
	</tr>
</table>
</form> 
<script type="text/javascript" >
	function refresh(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/OrderReport/orderReportInit.do";
		$('fm').submit();
	}
	<%-- //setTimeout('myrefresh()',1000);
	function dayReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/detailReport.do?flag="+3;
		$('fm').submit();
	}
	function monthReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/detailReport.do?flag="+2;
		$('fm').submit();
	}
	function yearReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/detailReport.do?flag="+1;
		$('fm').submit();
	}
	function round(number,fractionDigits){   
		with(Math){   
	     return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
		}   
	}
	function sum_jidi(){
		var cqnumber1 = document.getElementsByName("cqName1");
		var cqnumber2 = document.getElementsByName("cqName2");
		var cqnumber3 = document.getElementsByName("cqName3");
		var cqnumber4 = document.getElementsByName("cqName4");
		var cqnumber5 = document.getElementsByName("cqName5");
		var cqnumber6 = document.getElementsByName("cqName6");
		var hbnumber1 = document.getElementsByName("hbName1");
		var hbnumber2 = document.getElementsByName("hbName2");
		var hbnumber3 = document.getElementsByName("hbName3");
		var hbnumber4 = document.getElementsByName("hbName4");
		var hbnumber5 = document.getElementsByName("hbName5");
		var hbnumber6 = document.getElementsByName("hbName6");
		var njnumber1 = document.getElementsByName("njName1");
		var njnumber2 = document.getElementsByName("njName2");
		var njnumber3 = document.getElementsByName("njName3");
		var njnumber4 = document.getElementsByName("njName4");
		var njnumber5 = document.getElementsByName("njName5");
		var njnumber6 = document.getElementsByName("njName6");
		var numbers1 = 0;
		var numbers2 = 0;
		var numbers3 = 0;
		var numbers4 = 0;
		var numbers5 = 0;
		var numbers6 = 0;
		
		for(var i=0;i<cqnumber1.length;i++){
			numbers1 += Number(cqnumber1[i].value);
			numbers2 += Number(cqnumber2[i].value);
			numbers3 += Number(cqnumber3[i].value);
			numbers4 += Number(cqnumber4[i].value);
			numbers5 += Number(cqnumber5[i].value);
			numbers6 += Number(cqnumber6[i].value);
		}
		for(var i=0;i<hbnumber1.length;i++){
			numbers1 += Number(hbnumber1[i].value);
			numbers2 += Number(hbnumber2[i].value);
			numbers3 += Number(hbnumber3[i].value);
			numbers4 += Number(hbnumber4[i].value);
			numbers5 += Number(hbnumber5[i].value);
			numbers6 += Number(hbnumber6[i].value);
		}
		for(var i=0;i<njnumber1.length;i++){
			numbers1 += Number(njnumber1[i].value);
			numbers2 += Number(njnumber2[i].value);
			numbers3 += Number(njnumber3[i].value);
			numbers4 += Number(njnumber4[i].value);
			numbers5 += Number(njnumber5[i].value);
			numbers6 += Number(njnumber6[i].value);
		}
		document.getElementById("atodayBillSum").innerHTML = numbers1;
		document.getElementById("amonthBillSum").innerHTML = numbers2;
		document.getElementById("ayearBillSum").innerHTML = numbers3;
		document.getElementById("atodaySalesSum").innerHTML = numbers4;
		document.getElementById("amonthSalesSum").innerHTML = numbers5;
		document.getElementById("ayearSalesSum").innerHTML = numbers6;
		
	}
	function sum_no(){
		var number1 = document.getElementsByName("number1");
		var number2 = document.getElementsByName("number2");
		var number3 = document.getElementsByName("number3");
		var number4 = document.getElementsByName("number4");
		var number5 = document.getElementsByName("number5");
		var number6 = document.getElementsByName("number6");
		var numbers1 = 0;
		var numbers2 = 0;
		var numbers3 = 0;
		var numbers4 = 0;
		var numbers5 = 0;
		var numbers6 = 0;
		
		for(var i=0;i<number1.length;i++){
			numbers1 += Number(number1[i].value);
			numbers2 += Number(number2[i].value);
			numbers3 += Number(number3[i].value);
			numbers4 += Number(number4[i].value);
			numbers5 += Number(number5[i].value);
			numbers6 += Number(number6[i].value);
		}
		
		document.getElementById("todayBillSum").innerHTML = numbers1;
		document.getElementById("monthBillSum").innerHTML = numbers2;
		document.getElementById("yearBillSum").innerHTML = numbers3;
		document.getElementById("todaySalesSum").innerHTML = numbers4;
		document.getElementById("monthSalesSum").innerHTML = numbers5;
		document.getElementById("yearSalesSum").innerHTML = numbers6;
		
		//计算目标达成率=合计/目标
		var monthBillSum = document.getElementById("monthBillSum").innerHTML;		//月度启票合计
		var yearBillSum = document.getElementById("yearBillSum").innerHTML;			//年度启票合计
		var monthSalesSum = document.getElementById("monthSalesSum").innerHTML;		//月度启票合计
		var yearSalesSum = document.getElementById("yearSalesSum").innerHTML;		//年度启票合计
	
		var monthBillPlan = document.getElementById("monthBillPlan").value;			//月度启票目标
		var yearBillPlan = document.getElementById("yearBillPlan").value;			//年度启票目标
		var monthSalesPlan = document.getElementById("monthSalesPlan").value;		//月度销售目标
		var yearSalesPlan = document.getElementById("yearSalesPlan").value;			//月度销售目标
	
		if(monthBillPlan+"" != "0"){
			document.getElementById("rate1").innerHTML = round(100*monthBillSum/monthBillPlan,2)+"%";
		}else{
			document.getElementById("rate1").innerHTML = 0;
		}
		
		if(yearBillPlan+"" != "0"){
			document.getElementById("rate2").innerHTML = round(100*yearBillSum/yearBillPlan,2)+"%";
		}else{
			document.getElementById("rate2").innerHTML = 0;
		}
	
		if(monthSalesPlan+"" != "0"){
			document.getElementById("rate3").innerHTML = round(100*monthSalesSum/monthSalesPlan,2)+"%";
		}else{
			document.getElementById("rate3").innerHTML = 0;
		}
	
		if(yearSalesPlan+"" != "0"){
			document.getElementById("rate4").innerHTML = round(100*yearSalesSum/yearSalesPlan,2)+"%";
		}else{
			document.getElementById("rate4").innerHTML = 0;
		}
	 }	
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+value+"&orderType="+value2;
		$('fm').submit();
	}
//设置超链接 end
	 --%>
</script>
<!--页面列表 end -->


</body>
</html>
