<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购退货申请明细</title>
<script type="text/javascript" >
$(function() {
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyById.json";
			
var title = null;

var columns = [
			{header: "序号", align:'center',renderer:getIndex},
			{header: "入库单号", dataIndex: 'IN_CODE', align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
            {header: "件号", dataIndex: 'PART_CODE', align:'center'},
			{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
			{header: "货位", dataIndex: 'LOC_CODE', align:'center'},
			{header: "入库数量", dataIndex: 'IN_QTY', align:'center'},
			{header: "库存数量", dataIndex: 'ITEM_QTY', align:'center'},
			{header: "可用数量", dataIndex: 'NORMAL_QTY', align:'center'},
			{header: "申请退货数量", dataIndex: 'APPLY_QTY', align:'center'},
			{header: "审核数量", dataIndex: 'CHECK_QTY', align:'center'},
			{header: "已退货数量", dataIndex: 'RETURN_QTY', align:'center'},
			{header: "备注", dataIndex: 'REMARK', align:'center'}
	      ];

//返回查询页面
function goback(pageFlag){
	if('1' == pageFlag){
		// 采购退货申请
		window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInit.do';
	}else if('2' == pageFlag){
		// 采购退货查询
		window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/queryPartReturnInit.do';
	}
}
</script>
</head>
<body>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
</style>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;采购退货申请&gt;查看
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">制单单位：</td>
							<td>${po["ORG_NAME"]}</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
						</tr>
						<c:if test="${state eq rstate}">
							<tr>
								<td class="right">驳回原因：</td>
								<td colspan="3">${po["REMARK1"]}</td>
							</tr>
						</c:if>
					</table>
				</div>
			</div>
			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="返 回" onclick="javascript:goback('${pageFlag}');" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>

	</div>
</body>
</html>
