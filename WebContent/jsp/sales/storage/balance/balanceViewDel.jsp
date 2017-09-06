<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运费挂账</title>
</head>
<body>
	<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置：储运管理&gt;结算管理&gt;运费挂账&gt;明细查看</div>
	<form method="POST" name="fm" id="fm">
	<div class="form-panel">
		<h2>挂账信息</h2>
		<div class="form-body">
	  <TABLE class=table_query id="moneyTable">
	  	<tr>
	  		<td class="right">挂账单号：</td>
			<td><span id="orderNo_span"><c:out value="${map.BAL_NO}" default="0"></c:out></span></td>
			<td class="right">承运商：</td>
			<td><span id="orderNo_span"><c:out value="${map.LOGI_NAME}" default="0"></c:out></span></td>
			<td class="right">挂账月份：</td>
			<td><span id="expectDate_span"><c:out value="${map.BAL_MONTH}"></c:out></span></td>	
		</tr>
		<tr>
			<td class="right">运输总量：</td>
			<td><span id="expectLastDate_span">${map.BAL_COUNT}</span></td>
			<td class="right">挂账合计：</td>
			<td><span id="dealer_code_span">${map.BAL_AMOUNT}</span></td>
			<td class="right"></td>
			<td></td>
	  </TABLE>
	  </div>
	 </div>
	 <TABLE class=table_query style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif">运费明细 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		      <th nowrap="nowrap">交接单号</th>
		      <th nowrap="nowrap">经销商/收货仓库</th>
		      <th nowrap="nowrap">订单收货地</th>
		      <th nowrap="nowrap">发运结算地</th>
		      <th nowrap="nowrap">最后交车日期</th>
		      <th nowrap="nowrap">发运方式</th>
		      <th nowrap="nowrap">挂账合计</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${flist}" var="list" varStatus="status">
    				<tr class="table_list_row2">
    					<td>
    						${list.BILL_NO}
    					</td>
    					<td>${list.DEALER_NAME}</td>
    					<td align="left">${list.ADDRESS_INFO}</td>
    					<td align="left">${list.BAL_ADDR}</td>
    					<td>${list.LAST_CAR_DATE}</td>
    					<td>${list.DLV_SHIP_TYPE}</td>
    					<td>${list.BILL_AMOUNT}</td>
    				</tr>
    			</c:forEach>
    		</tbody>
  		</table>
	  <TABLE class=table_query style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif">车辆明细 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr>
		      <th nowrap="nowrap">交接单号</th>
		      <th nowrap="nowrap">是否散单</th>
		      <th nowrap="nowrap">最后交车日期</th>
		      <th nowrap="nowrap">发运仓库</th>
			  <th nowrap="nowrap">是否中转</th>
			  <th nowrap="nowrap">中转地</th>
			  <th nowrap="nowrap">车系</th>
			  <th nowrap="nowrap">车型</th>
			  <th nowrap="nowrap">配置</th>
			  <th nowrap="nowrap">颜色</th>
			  <th nowrap="nowrap">车架号</th>
			  <th nowrap="nowrap">里程(段1)</th>
			  <th nowrap="nowrap">单价(段1)</th>
			  <th nowrap="nowrap">里程(段2)</th>
			  <th nowrap="nowrap">单价(段2)</th>
		      <th nowrap="nowrap">挂账运费</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${vlist}" var="list" varStatus="status">
    				<tr>
    					<td>${list.BILL_NO}</td>
    					<td>
    						${list.DLV_IS_SD}
    					</td>
    					<td>${list.LAST_CAR_DATE}</td>
    					<td>${list.WAREHOUSE_NAME}</td>
    					<td>${list.DLV_IS_ZZ}</td>
    					<td align="left">${list.ZZ_ADDR}</td>
    					<td>${list.SERIES_NAME}</td>
    					<td>${list.MODEL_NAME}</td>
    					<td>${list.PACKAGE_NAME}</td>
    					<td>${list.COLOR_NAME}</td>
    					<td>${list.VIN}</td>
    					<!--<c:if test="${list.IS_ZZ==10041002}">
	    					<td>${list.MILEAGE}</td>
	    					<td>${list.PRICE}</td>
    					</c:if>
    					<c:if test="${list.IS_ZZ==10041001}">
	    					<td>${list.MILEAGE_ZZ}</td>
	    					<td>${list.PRICE_ZZ}</td>
    					</c:if>-->
    					<td>${list.MILEAGE}</td>
	    				<td>${list.PRICE}</td>
	    				<td>${list.MILEAGE_ZZ}</td>
	    				<td>${list.PRICE_ZZ}</td>
    					<td>${list.ONE_BILL_AMOUNT}</td>
    				</tr>
    			</c:forEach>
    		</tbody>
  		</table>	
</form>
</body>
</html>
