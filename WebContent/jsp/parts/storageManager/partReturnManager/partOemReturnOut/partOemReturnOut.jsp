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
<title>配件采购退货出库</title>
<script type="text/javascript">
$(function(){
	queryDtl();
});

var myPage;

var url;

var title = null;

var columns;

function queryDtl() {
    if (<%=flag%>==0)
    {
        url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/queryPartOemReturnChkInfo.json";
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'DTL_ID', align: 'center', width: '33px', renderer: seled},
//             {header: "入库单号", dataIndex: 'IN_CODE', align: 'center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: center'},
            {header: "货位", dataIndex: 'LOC_CODE', style: 'text-align: center'},
            {header: "批次号", dataIndex: 'BATCH_NO', style: 'text-align: center'},
            {header: "供应商", dataIndex: 'VENDER_NAME', align: 'center'},
            {header: "入库库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "出库库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
            {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
            {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "是否已结算", dataIndex: 'IS_BALANCES', align: 'center', renderer: isBalanceInput},
            {header: "备注", dataIndex: 'REMARK', align: 'center'}
        ];
    }
else
    {
        url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/queryPartOemReturnChkInfo1.json";
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'DTL_ID', align: 'center', width: '33px', renderer: seled},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "货位", dataIndex: 'LOC_CODE', style: 'text-align:left'},
            {header: "出库库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
            {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "备注", dataIndex: 'REMARK', align: 'center'}
        ];
    }
    __extQuery__(1);
}

function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck' onclick='chkPart()'/>";
}

function chkPart() {
    var cks = document.getElementsByName('ck');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("ckbAll").checked = flag;
}

function selAll(obj) {
    var cks = document.getElementsByName('ck');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
    }
}

function isBalanceInput(value, meta, record) {
    var dtlId = record.data.DTL_ID;
    return String.format("<input type='hidden' id='isBalance" + dtlId + "' value='" + value + "'/>" + value);
}

//出库
function outPartDlrReturn() {
    var ck = document.getElementsByName('ck');
    var l = ck.length;
    var cnt = 0;
    var isBalance;
    if (<%=flag%>=='0')
    {
        isBalance = $("isBalance" + ck[0].value).value;
    }
    for (var i = 0; i < l; i++) {
        if (ck[i].checked) {
            cnt++;
        }
    }
    if (cnt == 0) {
    	MyAlert("请选择要出库的配件！");
        return;
    }
    if (isBalance == "是") {
    	MyAlert("该退货单中的配件已经结算,不能出库!");
        return;
    }
    
    MyConfirm("确定出库?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/outPartOemReturn.json';
        sendAjax(url, getResult, 'fm');
    });
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var errors = jsonObj.errors;
        var dtlError = jsonObj.dtlError;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/queryPartReturnInit.do';
            });
        } else if (errors && errors != "") {
        	MyAlert(errors, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/queryPartReturnInit.do';
        	});
        } else if (dtlError && dtlError != "") {
            MyAlert(dtlError);
            __extQuery__(1);
        } else if (exceptions) {
        	MyAlert(exceptions.message);
        }

    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnOutManager/queryPartReturnInit.do';
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
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;采购退货出库&gt;出库
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
						<%-- <tr>
               <td class="table_query_right"  align="right">退货单位：</td>
               <td >${po["ORG_NAME"]}</td>
             </tr>--%>
						<tr>
							<td class="right">入库单号：</td>
							<td colspan="3">${po["IN_CODE"]}</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
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
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="出库" onclick="outPartDlrReturn();" />
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
