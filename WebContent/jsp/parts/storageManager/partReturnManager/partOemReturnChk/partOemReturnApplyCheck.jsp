<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
    int flag = (Integer) request.getAttribute("flag");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购退货审核</title>
<script type="text/javascript">
$(function(){
	queryDtl();
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryPartOemReturnApplyDetail.json";
var title = null;
var columns;
function queryDtl() {
    if (<%=flag%>=='0'){
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left', renderer: insertIdInput},
            {header: "货位", dataIndex: 'LOC_CODE', style: 'text-align:left'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', align: 'center'},
            {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
            {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
            {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
            {header: "审核数量", dataIndex: 'APPLY_QTY', align: 'center', renderer: insertCheckQtyInput},
            {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: insertRemarkInput}
        ];
    }else{
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left', renderer: insertIdInput},
            {header: "货位", dataIndex: 'LOC_CODE', style: 'text-align:left'},
            {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
            {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
            {header: "审核数量", dataIndex: 'APPLY_QTY', align: 'center', renderer: insertCheckQtyInput},
            {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: insertRemarkInput}
        ];
    }
    __extQuery__(1);
}


function insertIdInput(value, meta, record) {
    var inId = record.data.IN_ID;
    var partId = record.data.DTL_ID;
    var output;
    if (inId) {
        output = '<input type="hidden" name="inId" value="' + inId + '"/>' + value;
    } else {
        output = '<input type="hidden" name="partId" value="' + partId + '"/>' + value;
    }
    return output;
}

function insertCheckQtyInput(value, meta, record) {
    var inId = record.data.IN_ID;
    var dtlId = record.data.DTL_ID;
    var output;
    if (inId) {
        output = '<input type="text" class="short_txt" id="CHECK_QTY' + record.data.IN_ID + '" name="CHECK_QTY' + record.data.IN_ID + '" value="' + value + '" onchange="check(this, \''+inId+'\');" "/>\n'
        + '<input type="hidden"  id="APPLY_QTY' + record.data.IN_ID + '" name="APPLY_QTY' + record.data.IN_ID + '" value="' + record.data.APPLY_QTY + '"/>'
        + '<input type="hidden"  id="NORMAL_QTY' + record.data.IN_ID + '" name="NORMAL_QTY' + record.data.IN_ID + '" value="' + record.data.NORMAL_QTY + '"/>';
    } else {
        output = '<input type="text" class="short_txt" id="CHECK_QTY' + dtlId + '" name="CHECK_QTY' + dtlId + '" value="' + value + '"/>\n'
        + '<input type="hidden"  id="APPLY_QTY' + dtlId + '" name="APPLY_QTY' + dtlId + '" value="' + record.data.APPLY_QTY + '"/>'
        + '<input type="hidden"  id="NORMAL_QTY' + dtlId + '" name="NORMAL_QTY' + dtlId + '" value="' + record.data.NORMAL_QTY + '"/>';
    }
    return output;
}

function insertRemarkInput(value, meta, record) {
    var output = '<input type="text" class="middle_txt" id="REMARK' + record.data.IN_ID + '" name="REMARK" value="' + value + '"/>\n';
    return output;
}

function check(obj, inId){
    var pattern1 = /^[1-9][0-9]*$/;
    var checkQty = obj.value;
    var applyQty = document.getElementById("APPLY_QTY" + inId).value;
    var normalQty = document.getElementById("NORMAL_QTY" + inId).value;
    if (!pattern1.exec(checkQty)) {
        MyAlert("审核数量不能为空且只能输入非零的正整数!");
        obj.value = "";
        return;
    }
    if (parseInt(checkQty) > parseInt(applyQty)) {
        MyAlert("审核数量不能大于申请退货数量!");
        obj.value = "";
        return;
    }
    if (parseInt(checkQty) > (parseInt(normalQty) + parseInt(applyQty))) {
        MyAlert("审核数量不能大于可用库存数量!");
        obj.value = "";
        return;
    }
}

//同意
function agreeApply() {
    var inIds = document.getElementsByName("inId");
    var partIds = document.getElementsByName("partId");
    if (inIds.length > 0) {
        for (var i = 0; i < inIds.length; i++) {
            var pattern1 = /^[1-9][0-9]*$/;
            var obj = document.getElementById("CHECK_QTY" + inIds[i].value);
            var checkQty = obj.value;
            var applyQty = document.getElementById("APPLY_QTY" + inIds[i].value).value;
            var normalQty = document.getElementById("NORMAL_QTY" + inIds[i].value).value;
            if (!pattern1.exec(checkQty)) {
                MyAlert("第" + (i + 1) + "行，审核数量不能为空且只能输入非零的正整数!");
                obj.value = "";
                return;
            }
            if (parseInt(checkQty) > parseInt(applyQty)) {
                MyAlert("第" + (i + 1) + "行，审核数量不能大于申请退货数量!");
                obj.value = "";
                return;
            }
            if (parseInt(checkQty) > (parseInt(normalQty) + parseInt(applyQty))) {
                MyAlert("第" + (i + 1) + "行，审核数量不能大于库存数量!");
                obj.value = "";
                return;
            }
        }
    }else if (partIds.length) {
        for (var i = 0; i < partIds.length; i++) {
            var pattern1 = /^[1-9][0-9]*$/;
            var checkQty = document.getElementById("CHECK_QTY" + partIds[i].value).value;
            var applyQty = document.getElementById("APPLY_QTY" + partIds[i].value).value;
            var normalQty = document.getElementById("NORMAL_QTY" + partIds[i].value).value;
            if (!pattern1.exec(checkQty)) {
                MyAlert("第" + (i + 1) + "行，审核数量不能为空且只能输入非零的正整数!");
                return;
            }
            if (parseInt(checkQty) > parseInt(applyQty)) {
                MyAlert("第" + (i + 1) + "行，审核数量不能大于申请退货数量!");
                return;
            }
            if (parseInt(checkQty) > (parseInt(normalQty) + parseInt(applyQty))) {
                MyAlert("第" + (i + 1) + "行，审核数量不能大于库存数量!");
                return;
            }
        }
    } else {
        MyAlert("请选择明细!");
        return;
    }

    MyConfirm("确定审核通过?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/agreePartOemReturnApply.json';
        makeNomalFormCall(url, getResult, 'fm');
    });
}

//驳回
function rejectApply() {
    var rejectReason = $("#rejectReason")[0].value;
    if (!rejectReason) {
        MyAlert("请填写驳回原因!");
        return;
    }
    MyConfirm("确定驳回?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/rejectPartOemReturnApply.json';
        makeNomalFormCall(url, getResult, 'fm');
    });
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryPartOemReturnApplyInit.do';
            });
        } else if (error) {
            MyAlert(error);
            // window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryPartOemReturnApplyInit.do';
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }

    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryPartOemReturnApplyInit.do';
}
</script>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
</style>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 &gt;配件退货管理&gt;采购退货审核&gt;审核
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">制单单位：</td>
							<td>${po["ORG_NAME"]}</td>
						</tr>
						<tr>
							<td class="right">入库单号：</td>
							<td colspan="3">${po["IN_CODE"]}</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
						</tr>
						<tr>
							<td class="right">驳回原因：</td>
							<td colspan="3">
								<textarea class="form-control" name="rejectReason" id="rejectReason" style="width: 80%" rows="4"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table class="table_query">
			<tr>
				<td class="center"> 
					<input name="agreeBtn" id="agreeBtn" class="u-button" type="button" value="通 过" onclick="agreeApply();" /> &nbsp; 
					<input name="rejectBtn" id="rejectBtn" class="u-button" type="button" value="驳 回" onclick="rejectApply();" /> &nbsp; 
					<input class="u-button" type="button" value="返 回" onclick="javascript:goback();" />&nbsp;
				</td>
			</tr>
			</table>
		</form>
	</div>
</body>
</html>
