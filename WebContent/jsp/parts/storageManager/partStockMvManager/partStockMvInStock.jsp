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
</head>
<script type=text/javascript>
//审核
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
            var locCode = document.getElementById("LOC_CODE_" + partLocId).value;

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
            if (locCode == '') {
                MyAlert("第" + (i + 1) + "行的入库货位不能为空!");
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
    var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/inStockMvOrder.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("操作成功！", function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/inStockMvInit.do';
            });
        } else {
            MyAlert(json.Exception.message);
        }
    }
}

function goBack() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/inStockMvInit.do';
}

function codeChoice(partLocId, id, code, name) {
    var whId2 = document.getElementById("toWhId").value;
    if (whId2 == '') {
        MyAlert("请先选择库房！")
        return;
    }
    var url = g_webAppName + "/parts/baseManager/partsBaseManager/PartLocation/selectLocationInit.do";
    url += "?partLocId="+partLocId+"&partId="+id+"&partOldcode="+code+"&partCname="+name+"&whId="+whId2;
    OpenHtmlWindow(encodeURI(url), 800, 500);
}
var LOC_ID = null;
function codeSet(i, c, n) {
    var v = i + "," + c + "," + n;
    if ("query" == LOC_ID) {
        $("#LOC_CODE").val(c);
    } else {
        document.getElementById("LOC_CODE_" + LOC_ID).value = c;
        document.getElementById("LOC_ID_" + LOC_ID).value = v;
    }
}
function codeSet2(i, c, pcId) {
    document.getElementById("LOC_CODE_" + pcId).value = c;
    document.getElementById("LOC_ID_" + pcId).value = i;
}
function checkCode(th, partLocId, partId, whId, whName) {
    var loc_code = th.value;
    var url2 = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/checkSeatExist.json";
    var para = "LOC_CODE=" + loc_code + "&partLocId=" + partLocId + "&partId=" + partId + "&whId=" + whId + "&whName=" + whName;
    makeCall(url2, forBack2, para);
}
function forBack2(json) {
    if (json.returnValue != 1) {
        document.getElementById("LOC_CODE_" + json.partLocId).value = "";
        parent.MyAlert("货位【" + json.LOC_CODE + "】在仓库【" + json.whName + "】中不存在,请先维护在操作！");
    } else {
        codeSet2(json.LOC_ID, json.LOC_CODE, json.partLocId);
    }
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
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="CHG_ID" id="CHG_ID" value="${main.CHG_ID }" />
			<input type="hidden" name="CHG_CODE" id="CHG_CODE" value="${main.CHG_CODE }" />
			<input type="hidden" name="orgId" id="orgId" value="${main.ORG_ID }" />
			<input type="hidden" name="orgCode" id="orgCode" value="${main.ORG_CODE }" />
			<input type="hidden" name="orgName" id="orgName" value="${main.ORG_NAME }" />
			<input type="hidden" name="whId" id="whId" value="${main.WH_ID }" />
			<input type="hidden" name="whCode" id="whCode" value="${main.WH_CODE }" />
			<input type="hidden" name="whName" id="whName" value="${main.WH_NAME }" />
			<input type="hidden" name="toWhId" id="toWhId" value="${main.TOWH_ID }" />
			<input type="hidden" name="toWhCode" id="toWhCode" value="${main.TOWH_CODE }" />
			<input type="hidden" name="toWhName" id="toWhName" value="${main.TOWH_NAME }" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨入库 &gt; 入库
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" />调拨信息
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
							<td class="right">移出仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select u-disabled" disabled>
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<c:choose>
												<c:when test="${main.WH_ID eq list.WH_ID}">
													<option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
												</c:when>
												<c:otherwise>
													<option value="${list.WH_ID }">${list.WH_CNAME }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">移入仓库：</td>
							<td>
								<select name="toWhId" id="toWhId" class="u-select u-disabled" disabled>
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<c:choose>
												<c:when test="${main.TOWH_ID eq list.WH_ID}">
													<option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
												</c:when>
												<c:otherwise>
													<option value="${list.WH_ID }">${list.WH_CNAME }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
							</td>
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
				<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>调拨明细</caption>
				<tr>
					<th>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
					</th>
					<th>序号</th>
					<th>配件编码</th>
					<th>配件名称</th>
<!-- 					<th>件号</th> -->
					<th>批次号</th>
					<th>申请数量</th>
					<th>审核数量</th>
					<th>移出数量</th>
					<th>已入数量</th>
					<th>入库数量</th>
					<th>货位</th>
					<th>备注</th>
				</tr>
				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_seq">
						<tr class="table_list_row1">
							<td align="center" nowrap>
								<input type="checkbox" value="${list.PART_IDS}${_seq.index+1}" id="cell_${_seq.index+1}" name="cb" checked="checked" onclick="chkPart();" />
								<input id="idx_${list.PART_IDS}${_seq.index+1}" name="idx_${list.PART_IDS}${_seq.index+1}" value="${_seq.index+1}" type="hidden" />
							</td>
							<td align="center" nowrap>${_seq.index+1}</td>
							<td align="center">
								<input name="PART_OLDCODE${list.PART_IDS}${_seq.index+1}" id="PART_OLDCODE${list.PART_IDS}${_seq.index+1}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
								<input name="PART_CODE${list.PART_IDS}${_seq.index+1}" id="PART_CODE${list.PART_IDS}${_seq.index+1}" value="${list.PART_CODE}" type="hidden" />
							</td>
							<td align="center" nowrap>
								<input name="PART_CNAME${list.PART_IDS}${_seq.index+1}" id="PART_CNAME${list.PART_IDS}${_seq.index+1}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
							</td>
							<td align="center" nowrap>
								<input name="BATCH_NO${list.PART_IDS}${_seq.index+1}" id="BATCH_NO${list.PART_IDS}${_seq.index+1}" value="${list.BAT_ID}" type="hidden" />${list.BAT_ID}
							</td>
							<td align="center" nowrap>${list.APPLY_QTY}
								<input name="APPLY_QTY${list.PART_IDS}${_seq.index+1}" id="APPLY_QTY${list.PART_IDS}${_seq.index+1}" value="${list.APPLY_QTY}" type="hidden" />
							</td>
							<td align="center" nowrap>
								<input name="CHECK_QTY${list.PART_IDS}${_seq.index+1}" id="CHECK_QTY${list.PART_IDS}${_seq.index+1}" value="${list.CHECK_QTY}" type="hidden" />${list.CHECK_QTY}
							</td>
							<td align="center" nowrap>
								<input name="OUT_QTY${list.PART_IDS}${_seq.index+1}" id="OUT_QTY${list.PART_IDS}${_seq.index+1}" value="${list.OUT_QTY}" type="hidden" />${list.OUT_QTY}
							</td>
							<td align="center" nowrap>${list.IN_QTY}
								<input class="short_txt" name="INED_QTY${list.PART_IDS}${_seq.index+1}" id="INED_QTY${list.PART_IDS}${_seq.index+1}" value="${list.IN_QTY}" type="hidden" />
							</td>
							<td align="center" nowrap>
								<input class="short_txt" name="IN_QTY${list.PART_IDS}${_seq.index+1}" id="IN_QTY${list.PART_IDS}${_seq.index+1}" value="${list.REM_QTY}" type="text" style="background-color: #FF9; text-align: center" />
								<input class="short_txt" name="REM_QTY${list.PART_IDS}${_seq.index+1}" id="REM_QTY${list.PART_IDS}${_seq.index+1}" value="${list.REM_QTY}" type="hidden" />
							</td>
							<td align="center" nowrap>
								<input type="hidden" id="OUT_LOC_ID_${list.PART_IDS}${_seq.index+1}" name="OUT_LOC_ID_${list.PART_IDS}${_seq.index+1}" value="${list.LOC_ID}">
								<input name='LOC_CODE_${list.PART_IDS}${_seq.index+1}' id='LOC_CODE_${list.PART_IDS}${_seq.index+1}' class='middle_txt' type='text' 
									onchange="checkCode(this, '${list.PART_IDS}${_seq.index+1}', '${list.PART_ID}','${main.TOWH_ID}','${main.TOWH_NAME}');" />
								<input name='LOC_ID_${list.PART_IDS}${_seq.index+1}' id='LOC_ID_${list.PART_IDS}${_seq.index+1}' type='hidden' value='' />
								<input class='mini_btn' type='button' value='...' onclick="codeChoice('${list.PART_IDS}${_seq.index+1}', '${list.PART_ID}','${list.PART_OLDCODE}', '${list.PART_CNAME}');" />
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
