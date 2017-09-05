<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件零售/领用单</title>
<script language="javascript" type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMVDetailQuery.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "批次号", dataIndex: 'BAT_ID', align: 'center'},
    {header: "申请数量", dataIndex: 'APPLY_QTY', align: 'center'},
    {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center'},
    {header: "移出数量", dataIndex: 'OUT_QTY', align: 'center'},
    {header: "移入数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "移出货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "移入货位", dataIndex: 'TOLOC_CODE', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

$(function(){__extQuery__(1);});
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
	<form method="post" name="fm" id="fm">
		<div class="wbox">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨&gt; 查看
				<input type="hidden" value="${main.CHG_ID}" name="CHG_ID" id="CHG_ID" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" />移库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">移库单：</td>
							<td>${map.CHG_CODE}</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">制单时间：</td>
							<td>
								<fmt:formatDate value="${map.CREATE_DATE}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
						<tr>
							<td class="right">移出库房：</td>
							<td>${map.WH_NAME}</td>
							<td class="right">移入库房：</td>
							<td>${map.TOWH_NAME}</td>
							<td class="right"></td>
							<td></td>
						</tr>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td width="90%" colspan="5">${map.REMARK}</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 盘点结果明细
				</h2>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
			<table class="table_query">
				<tr align="center">
					<td colspan="4" class="center">
						<input class="u-button" type="button" value="关 闭" onclick="_hide();" />
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>