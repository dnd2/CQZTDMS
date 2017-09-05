<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>入库单查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partInStockQueManager/inStockQueAction/partInStockDetailSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'INLINE_ID', renderer:getIndex,align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
			{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
			{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
			{header: "单位", dataIndex: 'UNIT'},
			{header: "最小包装量", dataIndex: 'MIN_PACKAGE', align:'center'},
			{header: "订货数量", dataIndex: 'BUY_QTY', align:'center'},
			{header: "审核数量", dataIndex: 'TRANS_QTY', align:'center'},
			{header: "入库数量", dataIndex: 'IN_QTY', align:'center'},
			{header: "验收状态", dataIndex: 'IN_TYPE', align:'center',renderer:getItemValue},
			{header: "备注", dataIndex: 'REMARK', align:'center'}
	      ];

function goBack(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/salesManager/partInStockQueManager/inStockQueAction/inStockQueInit.do";
	fm.submit();
}
</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件销售管理&gt;入库单查询&gt;查看
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" value="${map.IN_ID}" name="inId" id="inId" />
			</div>
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />配件入库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">入库单号：</td>
							<td>${map.IN_CODE}</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">入库仓库：</td>
							<td>${map.WH_NAME}</td>
						</tr>
						<tr>
							<td class="right">订货单号：</td>
							<td>${map.ORDER_CODE}</td>
							<td class="right">订货单位：</td>
							<td>${map.DEALER_NAME}</td>
							<td class="right">到货日期：</td>
							<td>${map.ARRIVAL_DATE}</td>
						</tr>
						<tr>
							<td class="right">发运单号：</td>
							<td>${map.TRANS_CODE}</td>
							<td class="right">订单类型：</td>
							<td>${map.ORDER_TYPE}</td>
							<td class="right">入库时间：</td>
							<td>${map.SALE_DATE}</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td width="80%" colspan="5">${map.REMARK2}</td>
						</tr>
					</table>
				</div>
			</div>
<!-- 			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;"> -->
<!-- 				<h2 style="border-bottom: 0px;"> -->
<%-- 					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件入库明细信息 --%>
<!-- 				</h2> -->
<!-- 			</div> -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

			<table class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="返 回" onclick="goBack()" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>