<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>库存盘点调整处理</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/partStockDetailSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
			{header: "件号", dataIndex: 'PART_CODE', align:'center'},
			{header: "单位", dataIndex: 'UNIT', align:'center'},
			{header: "批次号", dataIndex: 'BATCH_CODE', align:'center'},
			{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
			{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
			{header: "盘点库存", dataIndex: 'CHECK_QTY', align:'center'},
			{header: "盈亏库存", dataIndex: 'DIFF_QTY', align:'center'},
			{header: "盘点结果", dataIndex: 'CHECK_RESULT', align:'center',renderer:getItemValue},
			{header: "备注", dataIndex: 'REMARK', align:'center'},
			{header: "处理方式", dataIndex: 'HANDLE_TYPE', align:'center',renderer:resolveTypes}
	      ];

function resolveTypes(value,meta,record)
{
	var detailId = record.data.DTL_ID;
	var checkResult = record.data.CHECK_RESULT;
	var inveProfile = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 %>;
	var inveLosses = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 %>;
	
	var resolveType = document.getElementById("resolveType").value;
	var str = "";

	if("处理" != resolveType)
	{
		str = "封 存";
	}
	else if(inveProfile == checkResult)
	{
		str = "入 库";
	}
	else if(inveLosses == checkResult)
	{
		str = "出 库";
	}
	
	return String.format(str);
}

function goBack(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/stockInvReqSolveInit.do";
	fm.submit();
	// window.history.back(-1);
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
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件仓库管理&gt;库存盘点管理&gt;库存盘点调整处理&gt;查看
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" value="${map.HANDLE_TYPE}" name="resolveType" id="resolveType" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">盘点单号：</td>
							<td>${map.CHANGE_CODE}</td>
							<td class="right">盘点仓库：</td>
							<td>${map.WH_CNAME}</td>
							<td class="right"></td>
							<td></td>
						</tr>
						<tr>
							<td class="right">申请单号：</td>
							<td>
								${map.RESULT_CODE}
								<input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId" />
							</td>
							<td class="right">导入人：</td>
							<td>${map.IMP_NAME}</td>
							<td class="right">导入日期：</td>
							<td>${map.CREATE_DATE}</td>
						</tr>
						<tr>
							<td class="right">单据状态：</td>
							<td>${map.STATE}</td>
							<td class="right">提交人：</td>
							<td>${map.COMM_NAME}</td>
							<td class="right">提交日期：</td>
							<td>${map.COMMIT_DATE}</td>
						</tr>
						<tr>
							<td class="right">盘点类型：</td>
							<td>${map.CHECK_TYPE}</td>
							<td class="right">审核人：</td>
							<td>${map.CHE_NAME}</td>
							<td class="right">审核日期：</td>
							<td>${map.CHECK_DATE}</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 盘点结果信息
				</h2>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
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