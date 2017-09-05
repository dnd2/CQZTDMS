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
<title>配件调拨入库</title>
<script type=text/javascript>
// 入库
function instock() {
    var ck = document.getElementsByName('cb');
    var l = ck.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (ck[i].checked) {
            cnt++;
            var partLocId = ck[i].value;
            var outQty = document.getElementById("OUT_QTY" + partLocId).value;//出库数量
            var inQty = document.getElementById("IN_QTY" + partLocId).value;//录入验收数量
            var maxQty = document.getElementById("REM_QTY" + partLocId).value;//待验收数量

            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(inQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量只能输入非零的正整数!");
                return;
            }
            if (parseInt(inQty) > parseInt(outQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量不能大于出库数量!");
                return;
            }
            if (parseInt(inQty) > parseInt(maxQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量不能大于待验收数量：" + maxQty + "！");
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要入库的配件！");
        return;
    }

    MyConfirm("确定?", checkAction, []);

}
function checkAction() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/inStockMvOrder.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("操作成功！");
            window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/inStockMvInit.do';
        } else {
            MyAlert(json.Exception.message);
        }
    }
}

function goBack() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/inStockMvInit.do';
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

function chkPart() {
    var cks = document.getElementsByName('cb');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
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
			<input type="hidden" name="toWhId" id="toWhId" value="${main.TOWH_ID }" />
			<input type="hidden" id="toWhName" value="${main.TOWH_NAME }" />
			<input type="hidden" name="whId" id="whId" value="${main.WH_ID }" />
			<input type="hidden" name="toOrgId" id="toOrgId" value="${main.TOORG_ID }" />
			<input type="hidden" name="toOrgCode" id="toOrgCode" value="${main.TOORG_CODE }" />
			<input type="hidden" name="toOrgName" id="toOrgName" value="${main.TOORG_NAME }" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨入库 &gt; 入库
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 移库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
						</tr>
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
							<td class="right">调拨经销商：</td>
							<td>${main.ORG_NAME}</td>
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
					<td>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
					</td>
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>件号</td>
					<td>申请数量</td>
					<td>审核数量</td>
					<td>移出数量</td>
					<td>已入数量</td>
					<td>入库数量</td>
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
							<input type="checkbox" value="${list.PART_LOC_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" onclick="chkPart();" />
							<input id="idx_${list.PART_LOC_ID}" name="idx_${list.PART_LOC_ID}" value="${_sequenceNum.index+1}" type="hidden" />
							<input type="hidden" name="OUT_LOC_ID_${list.PART_LOC_ID}" value="${list.LOC_ID }">
							<input type="hidden" name="IN_LOC_ID_${list.PART_LOC_ID}" value="${list.TOLOC_ID }">
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}</td>
						<td>
							<input name="PART_OLDCODE${list.PART_LOC_ID}" id="PART_OLDCODE${list.PART_LOC_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td nowrap>
							<input name="PART_CNAME${list.PART_LOC_ID}" id="PART_CNAME${list.PART_LOC_ID}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
						</td>
						<td nowrap>
							<input name="PART_CODE${list.PART_LOC_ID}" id="PART_CODE${list.PART_LOC_ID}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>${list.APPLY_QTY}
							<input name="APPLY_QTY${list.PART_LOC_ID}" id="APPLY_QTY${list.PART_LOC_ID}" value="${list.APPLY_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>
							<input name="CHECK_QTY${list.PART_LOC_ID}" id="CHECK_QTY${list.PART_LOC_ID}" value="${list.CHECK_QTY}" type="hidden" />${list.CHECK_QTY}
						</td>
						<td align="center" nowrap>
							<input name="OUT_QTY${list.PART_LOC_ID}" id="OUT_QTY${list.PART_LOC_ID}" value="${list.OUT_QTY}" type="hidden" />${list.OUT_QTY}
						</td>
						<td align="center" nowrap>${list.IN_QTY}
							<input class="short_txt" name="INED_QTY${list.PART_LOC_ID}" id="INED_QTY${list.PART_LOC_ID}" value="${list.IN_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="IN_QTY${list.PART_LOC_ID}" id="IN_QTY${list.PART_LOC_ID}" value="${list.REM_QTY}" type="text" style="background-color: #FF9; text-align: center" />
							<input class="short_txt" name="REM_QTY${list.PART_LOC_ID}" id="REM_QTY${list.PART_LOC_ID}" value="${list.REM_QTY}" type="hidden" />
						</td>
						<td align="center" nowrap>${list.REMARK}</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%">
				<tr>
					<td height="2"></td>
				</tr>
				<tr>
					<td align="center">
						<input class="u-button" type="button" value="入 库" id="passButton" name="button1" onclick="instock();">
						&nbsp;&nbsp;
						<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

		</form>
	</div>
</BODY>
</html>
