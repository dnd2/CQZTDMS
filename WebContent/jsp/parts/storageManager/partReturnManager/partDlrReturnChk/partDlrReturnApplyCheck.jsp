<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货申请明细</title>
<script type="text/javascript">
$(function(){
	checkLevel();
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/queryPartDlrReturnApplyDetail.json";
var title = null;
var columns = null;
function checkLevel() {
	columns = [
	    {header: "序号", align: 'center', renderer: getIndex},
	    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
	    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
	    {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
	    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
	    {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'}
	];
	var remarkIndex = 7;
    var msg = "审核数量不能大于申请退货数量";
	if('${chkLevel}' == '1'){
		columns[6] =  {header: "审核数量", dataIndex: 'APPLY_QTY', align: 'center', renderer: insertCheckQtyInput};
	}else if('${chkLevel}' == '2'){
		columns[6] = {header: "一级审核数量", dataIndex: 'CHECK_QTY', align: 'center'};
		columns[7] = {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center', renderer: insertCheckQtyInput};
		remarkIndex = 8;
		msg = "审核数量不能大于一级申审核数量";
	}else if('${chkLevel}' == '3'){
		columns[6] = {header: "一级审核数量", dataIndex: 'CHECK_QTY', align: 'center'};
		columns[7] = {header: "二级审核数量", dataIndex: 'CHECK_TWO_QTY', align: 'center'};
		columns[8] = {header: "审核数量", dataIndex: 'CHECK_TWO_QTY', align: 'center', renderer: insertCheckQtyInput};
		remarkIndex = 9;
		msg = "审核数量不能大于二级申审核数量";
	}
	columns[remarkIndex] = {header: "备注", dataIndex: 'REMARK', align: 'center'};
	$('#chkMsg').val(msg);
	__extQuery__(1);
}



function insertCheckQtyInput(value, meta, record) {
    var dtlId = record.data.DTL_ID;
    var maxNum = 0;
	if('${chkLevel}' == '1'){
		maxNum = record.data.APPLY_QTY;
	}else if('${chkLevel}' == '2'){
		maxNum = record.data.CHECK_QTY;
	}else if('${chkLevel}' == '3'){
		maxNum = record.data.CHECK_TWO_QTY;
	}
    var output = '<input type="text" onchange="check(this, \''+dtlId+'\')" class="short_txt" id="CHECK_QTY' + dtlId + '" name="CHECK_QTY' + dtlId + '" value="' + value + '" />\n'
	output += '<input type="hidden"  id="APPLY_QTY' + dtlId + '" name="APPLY_QTY' + dtlId + '" value="' + maxNum + '"/>';
	output += '<input type="hidden"  name="dtlId" value="' + dtlId + '"/>';
    return output;
}

function check(obj, dtlId){
	var value = obj.value;
	var pattern1 = /^[1-9][0-9]*$/;
	if (value == 0||value=="") {
        MyAlert("审核数量是正整数且必须大于0！");
        obj.value = "";
        return;
    }
    if (!pattern1.exec(obj.value)) {
        MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
        return;
    }
    
    var applyQty = $('#APPLY_QTY'+dtlId).val();
    if(parseInt(value) > parseInt(applyQty)){
    	MyAlert($('#chkMsg').val());
    	obj.value="";
    	return;
    }
    
}

//同意
function agreeApply() {
    var dtlIds = document.getElementsByName("dtlId");
    for (var i = 0; i < dtlIds.length; i++) {
        var dtlId = dtlIds[i].value;
        var pattern1 = /^[1-9][0-9]*$/;
        var checkQty = $("#CHECK_QTY" + dtlId).val();
        var applyQty = $("#APPLY_QTY" + dtlId).val();
        if (!pattern1.exec(checkQty)) {
            MyAlert("审核数量只能输入非零的正整数!");
            return;
        }
        if (parseInt(checkQty) > parseInt(applyQty)) {
            MyAlert("审核数量不能大于申请退货数量!");
            return;
        }
    }
    
    MyConfirm("确定通过?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/agreePartDlrReturnApply.json';
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
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/rejectPartDlrReturnApply.json';
        makeNomalFormCall(url, getResult, 'fm');
    });
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, goback);
        }
        if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回查询页面
function goback() {
	var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/';
	if('${chkLevel}' == '1'){
		url += 'queryPartReturnChkOneInit.do';
	}else if('${chkLevel}' == '2'){
     url += 'queryPartReturnChkTwoInit.do';
	}else{
     url += 'queryPartReturnChkThreeInit.do';
	}
	window.location.href = url;
}
</script>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
.form-panel {margin-bottom: 10px}
table.table_query .bottom-button{padding-bottom: 10px}
</style>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货审核&gt;审核
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" id="chkMsg"/>
			<input type="hidden" name="chkLevel" value="${chkLevel}" />
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" value="${po['SO_CODE']}" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" />退货信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">申请单位：</td>
							<td a>${po["CREATE_DEALER"]}</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
						</tr>
						<c:if test="${chkLevel eq '2' or chkLevel eq '3'}">
							<tr>
								<td class="right">一级审核人：</td>
								<td>${po["VL_ONE_BY_NAME"]}</td>
								<td class="right">一级审核时间：</td>
								<td>${po["VL_ONE_DATE"]}</td>
							</tr>
							<tr>
								<td class="right">一级审核意见：</td>
								<td colspan="3">${po["VL_ONE_REMARK"]}</td>
							</tr>
						</c:if>
						<c:if test="${chkLevel eq '3'}">
							<tr>
								<td class="right">二级审核人：</td>
								<td>${po["VL_TWO_BY_NAME"]}</td>
								<td class="right">二级审核时间：</td>
								<td>${po["VL_TWO_DATE"]}</td>
							</tr>
							<tr>
								<td class="right">二级审核意见：</td>
								<td colspan="3">${po["VL_TWO_REMARK"]}</td>
							</tr>
						</c:if>
						<tr>
							<td class="right">审核意见：</td>
							<td colspan="3">
								<textarea class="form-control align" name="rejectReason" id="rejectReason" style="width: 80%" rows="4"></textarea>
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
					<td class="center bottom-button">
						<input name="agreeBtn" id="agreeBtn" class="u-button" type="button" value="通 过" onclick="agreeApply();" />
						&nbsp;
						<input name="rejectBtn" id="rejectBtn" class="u-button" type="button" value="驳 回" onclick="rejectApply();" />
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="javascript:goback();" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
