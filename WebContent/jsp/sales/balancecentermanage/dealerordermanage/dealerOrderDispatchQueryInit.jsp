<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商订单发运指令下达查询</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 经销商订单管理 > 经销商订单发运指令下达查询</div>
<form method="POST" name="fm" id="fm">
<table class="table_query" align=center width="95%">
	<tr>
		<td width="19%" align="right" nowrap>&nbsp;</td>
		<td align="right" nowrap="nowrap">订单周度：</td>
		<td align="left" nowrap="nowrap"><select name="orderYear1">
			<c:forEach items="${years}" var="po">
				<c:choose>
					<c:when test="${po == curYear}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select> 年 <select name="orderWeek1">
			<c:forEach items="${weeks}" var="po">
				<c:choose>
					<c:when test="${po == curWeek}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select> 至 <select name="orderYear2">
			<c:forEach items="${years}" var="po">
				<c:choose>
					<c:when test="${po == curYear}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select> 年 <select name="orderWeek2">
			<c:forEach items="${weeks}" var="po">
				<c:choose>
					<c:when test="${po == curWeek}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select></td>
		<td width="19%" align=left nowrap>&nbsp;</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">&nbsp;</td>
		<td align="right" class="table_list_th">选择业务范围：</td>
		<td class="table_list_th"><select name="areaId">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
		</select></td>
		<td>&nbsp;</td>
	</tr> 
	<tr>
		<td align="right" nowrap>&nbsp;</td>
		<td align="right" nowrap>选择物料组：</td>
		<td align="left" nowrap>
			<input type="text" name="groupCode" size="15" id="groupCode" value="" /> 
			<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td></td>
		<td align="right">选择经销商：</td>
		<td>
			<input type="text" name="dealerCode" size="15" value="" /> 
			<input name="button2" type="button" class="mini_btn" onclick="toQueryDealer();" value="..." />
		</td>
		<td></td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">&nbsp;</td>
		<td align="right" nowrap="nowrap">订单号码：</td>
		<td align="left" nowrap="nowrap">
			<label> <input type="text" id="orderNo" name="orderNo" datatype="1,is_textarea,18" /> </label>
			<input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">	
		</td>
		<td align="left" nowrap="nowrap"> </td>
	</tr>
</table>
<!--分页 begin --> 
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
<!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderDispatchQuery/dealerOrderDispatchQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center'},
				{header: "数量", dataIndex: 'ODRER_NO',renderer:mySelect, align:'center'},
				{header: "总价", dataIndex: 'ALL_PRICE', align:'center'}
		      ];
	function mySelect(value,meta,record){
		var data = record.data;
	  	return String.format("<a href=\"#\" onclick='vehicleInfo("+data.DETAIL_ID+");'>"+value+"</a>");
	}
	function vehicleInfo(detail_id){
		OpenHtmlWindow('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderDispatchQuery/dealerOrderDispatchListQuery.do?detail_id='+detail_id,800,600);
	}
	function toQueryDealer(){
		OpenHtmlWindow('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toQueryDealerList.do',800,500);
	}
</script>
</body>
</html>
