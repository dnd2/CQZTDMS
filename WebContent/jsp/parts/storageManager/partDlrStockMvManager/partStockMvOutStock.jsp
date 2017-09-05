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
//出库
function outStcok() {
    MyConfirm("确定出库?", outStcokAction, []);

}
//
function outStcokAction() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/outStockMvOrder.json?outStcokFlag=1";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("操作成功！", function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/outStockMvInit.do';
            });
        } else {
            MyAlert(json.Exception.message);
        }
    }
}

//返回
function goBack() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/outStockMvInit.do';
}

function selAll2(obj) {
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (obj.checked) {
            cb[i].checked = true;
        } else {
            cb[i].checked = false;
        }
    }
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
			<input type="hidden" name="CHG_CODE" id="CHG_CODE" value="${main.CHG_CODE }" />
			<input type="hidden" name="orgId" id="orgId" value="${main.ORG_ID }" />
			<input type="hidden" name="orgCode" id="orgCode" value="${main.ORG_CODE }" />
			<input type="hidden" name="orgName" id="orgName" value="${main.ORG_NAME }" />
			<input type="hidden" name="fromOrgId" id="fromOrgId" value="${main.ORG_ID }" />
			<input type="hidden" name="whId" id="whId" value="${main.WH_ID }" />
			<input type="hidden" name="toWhId" id="toWhId" value="${main.TOWH_ID }" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨出库 &gt; 出库
			</div>
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 移库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">移库单号：</td>
							<td>${main.CHG_CODE}</td>
							<td class="right">制单人：</td>
							<td>${main.NAME}</td>
							<td class="right">制单日期：</td>
							<td>
								<fmt:formatDate value="${main.CREATE_DATE}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
						<tr>
							<td class="right">申请调拨经销商：</td>
							<td>${main.TOORG_NAME}</td>
							<td class="right">移出仓库：</td>
							<td>${main.WH_NAME}</td>
							<td class="right">移入仓库：</td>
							<td>${main.TOWH_NAME}</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">${main.REMARK }</td>
						</tr>
						<tr>
							<td class="right">审核意见：</td>
							<td colspan="5">${main.CHECK_REMARK }</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px;">
				<tr>
					<th colspan="12" align="left">
						<img src="<%=contextPath%>/img/nav.gif" />移库明细
					</th>
				</tr>
				<tr class="table_list_row0">
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>件号</td>
					<td>申请数量</td>
					<td>审核数量</td>
					<td>库存数量</td>
					<td>
						出库数量<font color="red">*</font>
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
						<td align="center" nowrap>
							${_sequenceNum.index+1}
							<input type="hidden" value="${list.PART_IDS}" id="cell_${_sequenceNum.index+1}" name="cb" />
							<input id="idx_${list.PART_IDS}" name="idx_${list.PART_IDS}" value="${_sequenceNum.index+1}" type="hidden" />
						</td>
						<td>
							<input name="PART_OLDCODE${list.PART_IDS}" id="PART_OLDCODE${list.PART_IDS}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td nowrap>
							<input name="PART_CNAME${list.PART_IDS}" id="PART_CNAME${list.PART_IDS}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
						</td>
						<td nowrap>
							<input name="PART_CODE${list.PART_IDS}" id="PART_CODE${list.PART_IDS}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>${list.APPLY_QTY}
							<input class="short_txt" name="APPLY_QTY${list.PART_IDS}" id="APPLY_QTY${list.PART_IDS}" value="${list.APPLY_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>${list.CHECK_QTY}
							<input class="short_txt" name="CHECK_QTY${list.PART_IDS}" id="CHECK_QTY${list.PART_IDS}" value="${list.CHECK_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>
							<input name="ITEM_QTY${list.PART_IDS}" id="ITEM_QTY${list.PART_IDS}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
						</td>
						<td align="center" nowrap>${list.CHECK_QTY}
							<input class="short_txt" name="OUT_QTY${list.PART_IDS}" id="OUT_QTY${list.PART_IDS}" value="${list.CHECK_QTY}" type="hidden" />
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
						<input class="u-button" type="button" value="出 库" id="passButton" name="button1" onclick="outStcok();">
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
