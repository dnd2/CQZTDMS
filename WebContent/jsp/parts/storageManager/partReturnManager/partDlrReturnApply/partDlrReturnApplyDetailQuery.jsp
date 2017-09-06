<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配件销售退货申请查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryPartDlrReturnDetail.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    //{header: "件号", dataIndex: 'PART_CODE', align:'center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "销售价格", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
    {header: "退货金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
    {header: "已退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
    {header: "已退货金额", dataIndex: 'RETURN_AMOUNT', style: 'text-align:right'},
    {header: "货位", dataIndex: 'LOC_NAME', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
}
</script>

</head>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}
#_page {
	margin-top: 0px !important;
}
</style>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货申请&gt;查看
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" value="${po['SO_CODE']}" />
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
							<td  class="right">退货单位：</td>
							<td>${po["DEALER_NAME"]}</td>
							<td  class="right">入库单号：</td>
							<td>${po["IN_CODE"]}</td>
						</tr>
						<%-- <tr>
               <td  class="right">销售单位：</td>
               <td >${po["SELLER_NAME"]}</td>
             </tr>--%>
						<tr>
							<td  class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
						</tr>
						<c:if test="${state eq rstate}">
							<tr>
								<td  class="right">驳回原因：</td>
								<td>${po["REMARK1"]}</td>
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
			<table class="table_query">
				<tr>
					<td class="center">
					<c:choose>
						<c:when test="${flag eq '0'}">
								<input class="u-button" type="button" value="返 回" onclick="javascript:goback();" />
								&nbsp;
						</c:when>
						<c:otherwise>
								<input class="u-button" type="button" value="关 闭" onclick="_hide();;" />
								&nbsp;
						</c:otherwise>
					</c:choose>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
