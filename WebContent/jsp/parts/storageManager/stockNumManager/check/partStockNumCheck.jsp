<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>库存调整申请审核</title>

<script type=text/javascript>
// 返回
function goBack(){
	fm.action = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumChkAction/queryStockNumChkInit.do";
	fm.submit();
}

//单个审核
function checkSimple(dtlId, dealStatus, msg){
	document.getElementById('dtlId').value = dtlId;
	document.getElementById('dealStatus').value = dealStatus;
    MyConfirm("确定"+msg+"申请?", submitCheck, ['simple']);
}
// 审核全部
function checkAll(dealStatus, msg){
	document.getElementById('dealStatus').value = dealStatus;
    MyConfirm("确定"+msg+"申请?", submitCheck, ['all']);
}
function submitCheck(actionType){
	var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumChkAction/partsStockNumCheck.json?actionType="+actionType;
	sendAjax(url, submitCheckBack, 'fm');
}
function submitCheckBack(json) {
    if (json != null) {
    	if(json.returnCode == 1){
	        MyAlert(json.msg, goBack);
    	}else{
	        MyAlert(json.msg);
    	}
    }
}
</script>
</head>
<body>
<form name="fm" id="fm" method="post">
	<input type="hidden" id="abjustmentId" name="abjustmentId" value="${mainMap.ABJUSTMENT_ID }">
	<input type="hidden" id="abjustmentType" name="abjustmentType" value="${mainMap.ABJUSTMENT_TYPE }">
	<input type="hidden" id="dealStatus" name="dealStatus" >
	<input type="hidden" id="dtlId" name="dtlId" >
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件仓库管理&gt;库存调整&gt;库存调整申请&gt;查看详情</div>
		<div>
			<table id="file" class="table_list" >
				<tr>
					<th colspan="12" align="left">
						<img src="<%=contextPath%>/img/nav.gif" />库存调整信息
					</th>
				</tr>
			</table>
			<table class="table_query" style="border-top: 0px;">
			    <tr>
			      <td class="right">调整编码：</td>
			      <td>${mainMap.ABJUSTMENT_CODE }</td>
			      <td class="right">调整类型：</td>
			      <td>${mainMap.ABJUSTMENT_TYPE_DESC } </td>
			    </tr>
			    <c:if test="${mainMap.APPLY_STATE eq 95741002}">
				    <tr>
				      <td class="right">申请人：</td>
				      <td>${mainMap.APPLY_NAME }</td>
				      <td class="right">申请时间：</td>
				      <td>${mainMap.APPLY_DATE } </td>
				    </tr>
			    </c:if>
			    <c:if test="${mainMap.APPLY_STATE eq 95751002}">
				    <tr>
				      <td class="right">审核人：</td>
				      <td>${mainMap.CHECK_NAME }</td>
				      <td class="right">审核时间：</td>
				      <td>${mainMap.CHECK_DATE } </td>
				    </tr>
			    </c:if>
			    <tr>
			      <td class="right">调整状态：</td>
			      <td >${mainMap.STATE_DESC }</td>
			      <td class="right">调整仓库：</td>
			      <td >${mainMap.WH_CNAME }</td>
			    </tr>
			    <tr>
			      <td class="right">备注：</td>
			      <td colspan="3">
			      	<textarea class="form-control" style="width:80%" id="remark" name="remark"></textarea>
			      </td>
			    </tr>
					<td class="center"  colspan="4">
						<input class="u-button" type="button" value="通过" name="button1" onclick="checkAll('1', '通过');">
						<input class="u-button" type="button" value="驳回" name="button1" onclick="checkAll('2', '驳回');">
						<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">
					</td>
				</tr>
		    </table>
	    	<div id="main_body">
				<table id="file" class="table_list">
					<caption>
						<img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" /> 配件信息
					</caption>
					<tr class="table_list_row0">
						<th>序号</th>
						<th>配件编码</th>
						<th>配件名称</th>
						<th>件号</th>
						<th>单位</th>
						<th>货位</th>
						<th>批次号</th>
						<th>账面库存</th>
						<th>可用数量</th>
						<th>调整数量</th>
						<th>备注</th>
					</tr>
					<c:if test="${dtlList !=null}">
						<c:forEach items="${dtlList}" var="list" varStatus="_sequenceNum" step="1">
							<tr class="table_list_row1">
								<td align="center" nowrap>${_sequenceNum.index+1}</td>
								<td align="center" nowrap>${list.PART_OLDCODE}</td>
								<td align="center" nowrap>${list.PART_CNAME}</td>
								<td align="center" nowrap>${list.PART_CODE}</td>
								<td align="center" nowrap>${list.UNIT}</td>
								<td align="center" nowrap>${list.LOC_NAME}</td>
								<td align="center" nowrap>${list.BATCH_CODE}</td>
								<td align="center" nowrap>${list.ITEM_QTY}</td>
								<td align="center" nowrap>${list.NORMAL_QTY}</td>
								<td align="center" nowrap>${list.ABJUSTMENT_NUM}</td>
								<td align="center" nowrap>${list.REMARK}</td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
	  		</div>
		</div>
	</div>
</form>
</dody>
</html>
