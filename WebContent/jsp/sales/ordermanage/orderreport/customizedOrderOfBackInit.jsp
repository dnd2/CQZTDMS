<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt;  订做车订单管理 &gt;  订做车订单调整 </div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
			<td align="right" width="20%">销售订单号：</td> 
			<td align="left" width="20%">
				<input type="text" id="orderNo" name="orderNo" class="middle_txt" value="" />
			</td>
			<td align="right" width="20%">业务范围：</td> 
			<td align="left" width="20%">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="4" align="center"></td>
			<td colspan="1" align="left">
				<input type="button" class="cssbutton" id="queryBtn" name="queryBtn" onclick="__extQuery__(1);" value="查 询" />
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedQuery/customizedOrderOfBackQuery.json";
	var title = null;
	var columns = [
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "申请数量", dataIndex: 'SUM_AMOUNT', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var data  = record.data;
  		return String.format("<a href='#' onclick='toReportInit(\"" + value + "\", \"" + data.DEALER_ID + "\")'>[调整]</a>");
	}
	//生成订单链接
	function toReportInit(value, dealerId){
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedQuery/customizedOrderOfBackAdjustmentInit.do?orderId=' + value + '&dealerId=' + dealerId;
		$('fm').submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>