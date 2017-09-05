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
<title>配件零售/领用单</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/partOrderDetailSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
			{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
			{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
			{header: "单位", dataIndex: 'UNIT', align:'center'},
			{header: "货位", dataIndex: 'LOC_CODE', align:'center'},
			{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
			{header: "可用库存", dataIndex: 'STOCK_QTY', align:'center'},
//			{header: "占用库存", dataIndex: 'BOOKED_QTY', align:'center'},
			{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
			{header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>数量', dataIndex: 'QTY', align:'center'},
			{header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>单价', dataIndex: 'SALE_PRICE', align:'center'},
			{header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>金额(元)', dataIndex: 'SALE_AMOUNT', align:'center'},
			{header: "已出库", dataIndex: 'OUT_QTY', align:'center'},
			{header: "可出库", dataIndex: 'OUTABLE_QTY', align:'center'},
			{header: "备注", dataIndex: 'REMARK', align:'center'}
	      ];

function goBack(){
	btnDisable();
	var actionURL = document.getElementById("actionURL").value;
	fm.action = actionURL;
	fm.submit();
}

function ConformCommit(result)
{
	if("pass" == result)
	{
		MyConfirm("确定通过订单?",commitOrder,[result]);
	}
	else
	{
		MyConfirm("确定<font color='red'>驳回</font>订单?",commitOrder,[result]);
	}
	
}

function commitOrder(result){
	btnDisable();
	var retailId = document.getElementById("changeId").value;
	var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/commitOrderInfos.json?retailId="+ retailId +"&result=" + result;	
	sendAjax(url,getResult,'fm');
}

function getResult(json){
	btnEnable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	 MyAlert(json.errorExist);
        	 __extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("订单审核成功!", goBack);
        } else {
            MyAlert("订单审核失败，请联系管理员!");
        }
	}
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
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理 &gt; 内部领用管理 &gt; 内部${map.CHG_TYPE}单 &gt; 审核
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="actionURL" id="actionURL" value="${actionURL}" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">
								<c:if test="${map.CHG_TYPE != null}">
									<c:choose>
										<c:when test="${map.CHG_TYPE  eq '领用' }">
										    领用单号：
										</c:when>
										<c:otherwise>
										  零售单号：
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>
								${map.RETAIL_CODE}
								<input type="hidden" value="${map.RETAIL_ID}" name="changeId" id="changeId" />
							</td>
							<td class="right">类型：</td>
							<td width="20%">${map.CHG_TYPE}</td>
							<td class="right">仓库：</td>
							<td width="20%">${map.WH_CNAME}</td>
						</tr>
						<tr>
							<td class="right">
								<c:if test="${map.CHG_TYPE != null}">
									<c:choose>
										<c:when test="${map.CHG_TYPE  eq '领用' }">
										    领用人：
										</c:when>
										<c:otherwise>
										  采购单位：
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>${map.LINKMAN}</td>
							<td class="right">联系电话：</td>
							<td>${map.TEL}</td>
							<td class="right">用途：</td>
							<td>${map.PURPOSE}</td>
						</tr>
						<tr>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">制单单位：</td>
							<td>${map.SORG_CNAME}</td>
							<td class="right">制单时间：</td>
							<td>${map.CREATE_DATE}</td>
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
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 明细信息
				</h2>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
			<table class="table_query">
				<tr>
					<td colspan="4" class="center">
						<input class="u-button" type="button" value="通 过" onclick="ConformCommit('pass')" />
						<input class="u-button" type="button" value="驳 回" onclick="ConformCommit('reject')" />
						<input class="u-button" type="button" value="返 回" onclick="goBack()" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>