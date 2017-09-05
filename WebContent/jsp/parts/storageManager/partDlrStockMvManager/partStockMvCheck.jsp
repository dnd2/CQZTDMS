<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件调拨出库</title>
<script type=text/javascript>
//审核
function check() {
    MyConfirm("确定通过?", checkAction, []);

}
function checkAction() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/checkMvOrder.json?checkFlag=1";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("操作成功！", function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/checkMvInit.do';
            });
        } else {
            MyAlert(json.Exception.message);
        }
    }
}


function goBack() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/checkMvInit.do';
}


//驳回
function rejectOrder() {
    if($("checkRemark").value == ""){
        MyAlert("审核意见必填!");
        return;
    }
    MyConfirm("确定驳回?", rejectOrderAction, []);
}

function rejectOrderAction() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/checkMvOrder.json?checkFlag=2";
    sendAjax(url, getResult, 'fm');
}

</script>

</head>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="CHG_ID" id="CHG_ID" value="${main.CHG_ID }" />
			<input type="hidden" name="whId" id="whId" value="${main.WH_ID }" />
			<input type="hidden" name="toWhId" id="toWhId" value="${main.TOWH_ID }" />
			<input type="hidden" name="fromOrgId" id="fromOrgId" value="${main.ORG_ID }" />
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨出库 &gt; 出库
			</div>
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 调拨信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">调拨单号：</td>
							<td>${main.CHG_CODE}</td>
							<td class="right">制单人：</td>
							<td>${main.NAME}</td>
							<td class="right">制单日期：</td>
							<td>
								<fmt:formatDate value="${main.CREATE_DATE}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
						<tr>
							<td class="right">申请调拨经销商：
							</td>
							<td>${main.ORG_NAME}</td>
							<td class="right">移出仓库：
							</td>
							<td>${main.WH_NAME}</td>
							<td class="right">移入仓库：
							</td>
							<td>${main.TOWH_NAME}</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">${main.REMARK }</td>
						</tr>
						<tr>
							<td class="right">审核意见：</td>
							<td colspan="5">
								<textarea class="form-control" style="width: 80%" id="checkRemark" name="checkRemark" cols="4" rows="4"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px;">
				<tr>
					<th colspan="12" align="left"> <img src="<%=contextPath%>/img/nav.gif" />调拨明细 </th>
				</tr>
				<tr class="table_list_row0">
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>件号</td>
					<td>库存数量</td>
					<td>可用数量</td>
					<td>移库数量</td>
					<td>
						审核数量<font color="red">*</font>
					</td>
					<td>备注</td>
				</tr>
				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_sequenceNum">
						<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
						</c:if>
						<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
						</c:if>
						<input type="hidden" value="${list.PART_IDS}" name="cb" checked="checked" />
						<td align="center" nowrap>${_sequenceNum.index+1}</td>
						<td>
							<input name="PART_OLDCODE${list.PART_IDS}" id="PART_OLDCODE${list.PART_IDS}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td nowrap>
							<input name="PART_CNAME${list.PART_IDS}" id="PART_CNAME${list.PART_IDS}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
						</td>
						<td nowrap>
							<input name="PART_CODE${list.PART_IDS}" id="PART_CODE${list.PART_IDS}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>
							<input name="ITEM_QTY${list.PART_IDS}" id="ITEM_QTY${list.PART_IDS}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
						</td>
						<td align="center" nowrap>
							<input name="NORMAL_QTY${list.PART_IDS}" id="NORMAL_QTY${list.PART_IDS}" value="${list.NORMAL_QTY}" type="hidden" />${list.NORMAL_QTY}
						</td>
						<td align="center" nowrap>${list.APPLY_QTY}
							<input class="short_txt" name="APPLY_QTY${list.PART_IDS}" id="APPLY_QTY${list.PART_IDS}" value="${list.APPLY_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="CHECK_QTY${list.PART_IDS}" id="CHECK_QTY${list.PART_IDS}" value="${list.APPLY_QTY}" type="text" style="background-color: #FF9; text-align: center" />
						</td>
						<td align="center" nowrap>${list.REMARK}</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" align="center">
				<tr>
					<td height="2"></td>
				</tr>
				<tr>
					<td align="center">
						<input class="u-button" type="button" value="通 过" id="passButton" name="button1" onclick="check();">
						&nbsp;&nbsp;
						<input class="u-button" type="button" value="驳 回" id="rejectButton" name="button1" onclick="rejectOrder();">
						&nbsp;&nbsp;
						<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">

					</td>
				</tr>
			</table>

			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
