<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车需求查询 > 订做车需求明细</div>
<form method="POST" name="fm" id="fm">
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编码</th>
			<th nowrap="nowrap">状态编码</th>
			<th nowrap="nowrap">状态名称</th>
			<th nowrap="nowrap">需求数量</th>
			<th nowrap="nowrap">预计交付周期</th>
			<th nowrap="nowrap">订做批次号</th>
			<c:if test="${isSecond == 'false' }">
			<!-- <th nowrap="nowrap">标准价格</th> -->
			<th nowrap="nowrap">价格变动</th>
			</c:if>
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.AMOUNT}</td>
		      <td align="center">${po.EXPECTED_PERIOD}天</td>
		      <td align="center">${po.BATCH_NO}</td>
		      <c:if test="${isSecond == 'false' }">
		      <%-- <td align="center">${po.SALES_PRICE}</td> --%>
		      <td align="center">${po.CHANGE_PRICE}</td>
		      </c:if>
		    </tr>
    	</c:forEach>
	</table>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">订单号</th>
			<th nowrap="nowrap">车系名称</th>
			<th nowrap="nowrap">车型名称</th>
			<th nowrap="nowrap">状态编码</th>
			<th nowrap="nowrap">物料名称</th>
			<th nowrap="nowrap">物料编码</th>
			<th nowrap="nowrap">提报数量</th>
		</tr>
    	<c:forEach items="${orderList}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.ORDER_NO}</td>
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_NAME}</td>
		      <td align="center">${po.PACKAGE_CODE}</td>
		      <td align="center">${po.MATERIAL_NAME}</td>
		      <td align="center">${po.MATERIAL_CODE}</td>
		      <td align="center">${po.ORDER_AMOUNT}</td>
		    </tr>
    	</c:forEach>
	</table>
	<br>
	<table class=table_query>
		<tr class=cssTable>
			<td width="19%" align="right">经销商代码：</td>
			<td width="31%" align="left"><c:out value="${dealerPO.dealerCode}"/></td>
			<td width="19%" align="right">经销商名称：</td>
			<td width="31%" align="left"><c:out value="${dealerPO.dealerShortname}"/></td>
		</tr>
		<tr class=cssTable>
			<td width="19%" align="right">集团客户：</td>
			<td width="81%" colspan="3" align="left">${fleetName}</td>
		</tr>
		<tr class=cssTable>
			<td width="19%" align="right">改装说明：</td>
			<td width="81%" colspan="3" align="left"><c:out value="${remark}"/></td>
		</tr>
	</table>
	<br>
	<table class="table_list" style="border-bottom:1px solid #DAE0EE" >
		<tr class="table_list_row1">
			<th  align="center" nowrap="nowrap" >日期</th>
			<th align="center" nowrap="nowrap"  >单位</th>
			<th align="center" nowrap="nowrap"  >操作人</th>
			<th align="center" nowrap="nowrap"  >审核结果</th>
			<th align="center" nowrap="nowrap"  >审核描述</th>
		</tr>
		<c:forEach items="${checkList}" var="po">
			<tr class="table_list_row1">
				<td align="center" nowrap="nowrap" class="table_list_row1" >${po.CHECK_DATE}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.ORG_NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.CHECK_STATUS}</td>
				<td align="center" nowrap="nowrap"  >${po.CHECK_DESC}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<table class=table_query>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="button" name="button2" class="cssbutton" onclick="history.back();" value="返回" id="queryBtn2" /> 
			</td>
		</tr>
	</table>
</form>
</body>
</html>
