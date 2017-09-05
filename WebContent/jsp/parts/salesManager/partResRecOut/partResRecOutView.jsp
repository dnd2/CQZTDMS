<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件零售/领用单</title>
<style>.form-panel{margin-bottom: 10px}</style>
<script type="text/javascript">
$(function(){
	 __extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecOutAction/partOrderDetailSearch.json";

var title = null;

var columns = [
    {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    //{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "批次号", dataIndex: 'BATCH_NO', align: 'center'},
 	{header: "可用库存", dataIndex: 'STOCK_QTY', align:'center'},
    //			{header: "占用库存", dataIndex: 'BOOKED_QTY', align:'center'},
    {header: "账面库存", dataIndex: 'ITEM_QTY', align: 'center'},
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>数量',
        dataIndex: 'QTY',
        align: 'center'
    },
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>单价',
        dataIndex: 'SALE_PRICE',
        align: 'center'
    },
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>金额(元)',
        dataIndex: 'SALE_AMOUNT',
        align: 'center'
    },
    {header: "已出库", dataIndex: 'OUT_QTY', align: 'center'},
    {header: "出库数量", dataIndex: 'OUTABLE_QTY', align: 'center', renderer: returnText},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

//返回文本框
function returnText(value, meta, record) {
	var dtlId = record.data.DTL_ID;
    var qty = record.data.QTY;
    var outQty = record.data.OUT_QTY;
    var str = "<input type='hidden' name='" + dtlId + "' value='" + dtlId + "' />"
    + "<input type='hidden' name='dtlIds' value='" + dtlId + "' />"
    + "<input type='hidden' name='qty_" + dtlId + "' id='qty_" + dtlId + "' value='" + qty + "' />"
    + "<input type='hidden' name='outQty_" + dtlId + "' id='outQty_" + dtlId + "' value='" + outQty + "' />"
    str += "<input type='text' class='short_txt' onchange= 'dataCheck(\"" + dtlId + "\",this)' name='outableQty_" + dtlId + "' id='outableQty_" + dtlId + "' value='" + value + "' />";
    return String.format(str);
}


function dataCheck(partId, obj) {
    if ("" == obj.value) {
        return;
    }
    var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }

    var qty = parseInt(document.getElementById("qty_" + partId).value); //零售/领用数量
    var outQty = parseInt(document.getElementById("outQty_" + partId).value); //已出库

    if (qty < (parseInt(obj.value) + outQty)) {
        MyAlert("总出库数量不能大于零售/领用数量!");
        obj.value = "";
        return;
    }
}

//保存
function confirmSubmit() {
    var dtlId = document.getElementsByName("dtlIds");
    var msg = "";

    if (dtlId.length == 0) {
        document.getElementById("saveButton").disabled = "disabled";
        msg += "出库已全部完成,请点击 【返回】按钮返回主界面!";
    }

    for (var i = 0; i < dtlId.length; i++) {
        //需要校验可出库数量是否为空
        if (document.getElementById("outableQty_" + dtlId[i].value).value == "") {

            msg += "请填写第" + (i + 1) + "行的可出库数量!</br>";
        } else {
            var qty = parseInt(document.getElementById("qty_" + dtlId[i].value).value);//零售/领用数量
            var outQty = parseInt(document.getElementById("outQty_" + dtlId[i].value).value);//已出库
            var outableQty = parseInt(document.getElementById("outableQty_" + dtlId[i].value).value);//可出库

            if (qty < (outQty + outableQty)) {
                msg += "第" + (i + 1) + "行的总出库数量不能大于零售/领用数量!</br>";
            }
        }
    }
    if (msg != "") {
        MyAlert(msg);
        return false;
    }
    MyConfirm("确定出库?", commitOrder, []);
}

function commitOrder() {
    btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecOutAction/saveOrderInfos.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist, function(){
	            __extQuery__(json.curPage);
            });
        } else if (json.success != null && json.success.length > 0) {
            if ("all" != json.success) {
                MyAlert("出库成功!", function(){
	                __extQuery__(json.curPage);
                });
            }
            else {
                btnDisable();
                MyAlert("出库成功!", function(){
	                window.location.href = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecOutAction/partResRecOutInit.do";
                });
            }

        } else {
            MyAlert("出库失败,请联系管理员!");
        }
    }
}

//返回
function goBack() {
    btnDisable();
    fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecOutAction/partResRecOutInit.do";
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
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理 &gt; 零售领用管理 &gt; 配件零售领用出库 &gt; 出库
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }" />
				<input type="hidden" name="whId" id="whId" value="${map.WH_ID}">
			</div>
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息
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
							</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">制单时间：</td>
							<td>${map.CREATE_DATE}</td>
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
							<td class="right">备注：</td>
							<td width="84%" colspan="5">${map.REMARK}</td>
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
					<td colspan="4" class="center">
						<input class="u-button" type="button" value="确定" id="saveButton" onclick="confirmSubmit();">
						<input class="u-button" type="button" value="返 回" onclick="goBack()" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>