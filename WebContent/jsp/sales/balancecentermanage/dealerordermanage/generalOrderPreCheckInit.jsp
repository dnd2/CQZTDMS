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
<title>月度常规订单预审核</title>
<script type="text/javascript">
<!--
function clearText() {
	var oText = arguments ;
	var iLen = arguments.length ;
	
	for(var i=0; i<iLen; i++) {
		document.getElementById(oText[i]).value = "" ;
	}
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：订单管理 > 销售订单管理 > 下级经销商订单管理 > 月度常规订单预审核</div>
<form method="POST" name="fm" id="fm">
<table class="table_query" align=center width="95%">
	<tr>
		<td width="19%" align="right" nowrap>&nbsp;</td>
		<td align="right" nowrap="nowrap">订单日期：</td>
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
		</select> 年 <select name="orderMonth1">
			<c:forEach items="${months}" var="po">
				<c:choose>
					<c:when test="${po == curMonth}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select> 月 至 <select name="orderYear2">
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
		</select> 年 <select name="orderMonth2">
			<c:forEach items="${months}" var="po">
				<c:choose>
					<c:when test="${po == curMonth}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select> 月</td>
		<td width="19%" align=left nowrap>&nbsp;</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">&nbsp;</td>
		<td align="right" class="table_list_th">选择业务范围：</td>
		<td class="table_list_th"><select class="short_sel" name="areaId">
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
			<input type="text" class="middle_txt" name="groupCode" size="15" id="groupCode" value="" /> 
			<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
			<input name="clr1" type="button" class="cssbutton" onclick="clearText('groupCode');" value="清 空" />
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td></td>
		<td align="right">选择经销商：</td> 
		<td>
			<input type="text" class="middle_txt" id="dealerCode" name="dealerCode" size="15" value="" /> 
			<input name="button2" type="button" class="mini_btn" onclick="toQueryDealer();" value="..." />
			<input name="clr2" type="button" class="cssbutton" onclick="clearText('dealerCode');" value="清 空" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">&nbsp;</td>
		<td align="right" nowrap="nowrap">销售订单号：</td>
		<td align="left" nowrap="nowrap">
			<label> <input type="text" class="middle_txt" id="orderNo" name="orderNo" datatype="1,is_textarea,18" /> </label>
		</td>
		<td align="left" nowrap="nowrap"><input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">	</td>
	</tr>
</table>

<!--分页 begin --> <jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include page="${contextPath}/queryPage/pageDiv.html" /> <!--分页 end --></form>
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/GeneralOrderPreCheck/dealerOrderCheckInit_Query.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "订货方", dataIndex: 'ODLRID', align:'center'},
				{header: "开票方", dataIndex: 'BDLRID', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "启票日期", dataIndex: 'CALLDATE', align:'center'},
				{header: "订单日期", dataIndex: 'ORDERDATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "申请状态", dataIndex: 'REQ_STATUS', align:'center',renderer:getItemValue},
				{header: "申请数量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'REQ_ID',renderer:myLink}
		      ];
	var areaId = document.getElementById("areaId").value;	     
	function myLink(value,metaDate,record){
	    return String.format(
	    		 "<a href=\"<%=contextPath%>/sales/balancecentermanage/dealerordermanage/GeneralOrderPreCheck/ReqDetailInto.do?reqId=" + value + "\">[审核]</a>");
	}
	function toQueryDealer(){
		OpenHtmlWindow('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toQueryDealerList.do',800,500);
	}
</script>
</body>
</html>
