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
<title>库存状态变更查询</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		
	}
</script>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockWrongAction/partStockDetailSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
			{header: "件号", dataIndex: 'PART_CODE', style:'text-align: left;'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
			{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: left;'},
			{header: "当前库存", dataIndex: 'STOCK_QTY', align:'center'},
			{header: "业务类型", dataIndex: 'CHANGE_REASON', align:'center',renderer:getItemValue},
			{header: "调整类型", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue},
			{header: "调整数量", dataIndex: 'RETURN_QTY', align:'center'},
			{header: "备注", dataIndex: 'REMARK', style:'text-align: left;'}
	      ];

function goBack(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgQueryAction/partStockCgQueryInit.do";
	fm.submit();
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
			<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
			<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件仓库管理&gt;库存状态变更&gt;库存状态变更查询&gt;查看
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" />信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">变更单号：</td>
							<td>${map.CHANGE_CODE} <input type="hidden" value="${map.CHANGE_ID}" name="changeId" id="changeId" />
							</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">制单单位：</td>
							<td>${map.CHGORG_CNAME}</td>
						</tr>
						<tr>
							<td class="right">仓库：</td>
							<td>${map.WH_CNAME}</td>
							<td class="right">备注：</td>
							<td width="50%" colspan="3">${map.REMARK}</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
			<table class="table_query">
				<tr>
					<td class="center"><input class="u-button" type="button" value="返 回" onclick="goBack()" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>