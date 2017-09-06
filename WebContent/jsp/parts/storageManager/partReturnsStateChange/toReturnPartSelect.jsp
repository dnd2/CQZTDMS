<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/jstl/cout"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换货解封单明细查询</title>
<script type="text/javascript">
$(function(){
	selectQuery();
});
var myPage;
var url = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/getReturnPartSelect.json';
var title = null;
var columns = [
	{header: "序号", renderer: getIndex},
	{header: "退换单号",dataIndex:'RETURN_CODE', style:"text-align: center"},
	{header: "销售单号",dataIndex:'SO_CODE', style:"text-align: center"},
	{header: "入库单号",dataIndex:'IN_CODE', style:"text-align: center"},
	{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
	{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align: center"},
	{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
	{header: "批次号",dataIndex:'BATCH_NO', style:"text-align: center"},
	{header: "申请数量",dataIndex:'APPLY_QTY', style:"text-align: center"},
	{header: "已解封数量",dataIndex:'UNLOC_QTY', style:"text-align: center"},
	{header: "可解封数量",dataIndex:'KY_QTY', style:"text-align: center"}
    ];
     
//操作链接生成
function myLink(value,meta,record){
	
}
  
  
//报表特殊时间控制查询
function selectQuery(){
	__extQuery__(1);
}
 </script>
</head>
<body onload="selectQuery();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓储管理&gt; 配件退换货状态变更&gt; 解封单明细查询
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="unlocId" value="${mains.UNLOC_ID }">
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 主要数据
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">制单单位：</td>
							<td>${mains.DEALER_NAME }</td>
							<td class="right">状态：</td>
							<td>${mains.STATE_DESC}</td>
						</tr>
						<tr>
							<td class="right">制单人：</td>
							<td>${mains.CREATER_NAME}</td>
							<td class="right">制单时间：</td>
							<td>${mains.CREATE_DATE}</td>
						</tr>
						<c:if test="${mains.STATE >= 97231002 }">
							<tr>
								<td class="right">申请人：</td>
								<td>${mains.SUBMITER_NAME}</td>
								<td class="right">申请时间：</td>
								<td>${mains.SUBMIT_DATE}</td>
							</tr>
							<c:if test="${mains.STATE >= 97231003 }">
								<tr>
									<td class="right">审核人：</td>
									<td>${mains.CHECKER_NAME}</td>
									<td class="right">审核时间：</td>
									<td>${mains.CHECK_DATE}</td>
								</tr>
								<c:if test="${mains.STATE >= 97231005 }">
									<tr>
										<td class="right">解封人：</td>
										<td>${mains.UNLOCER_NAME}</td>
										<td class="right">解封时间：</td>
										<td>${mains.UNLOC_DATE}</td>
									</tr>
								</c:if>
								<tr>
									<td class="right">审核意见：</td>
									<td colspan="3">${mains.CHECK_REMARK}</td>
								</tr>
							</c:if>
						</c:if>
						<tr>
							<td class="right">解封原因：</td>
							<td colspan="3">${mains.REMARK}</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="selectQuery()" value="查询" />
								&nbsp;&nbsp;
								<input type="button" class="u-button" onclick="_hide()" value="关闭" />
								&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>