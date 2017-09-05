<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<title>配件销售单-查询</title>
</head>
<script language="javascript">
//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/partSoQuery.json";
var title = null;
var columns = [
    {header: "序号",width:'5%',  renderer:getIndex},
//    {header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />", align: 'center', dataIndex: 'SO_ID', renderer: checkLink},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'SO_ID', renderer: myLink, align: 'center'},
    {header: "销售单号", dataIndex: 'SO_CODE', align: 'center', renderer: viewSoOrder},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center', renderer: viewOrder},
    {header: "备注", dataIndex: 'REMARK2', style: 'text-align: left;'},
    /*{header: "发运方式", dataIndex: 'FIX_NAME', style: 'text-align: left;'},*/
    {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align: left;'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
    {header: "制单人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
    //{header: "是否铺货", dataIndex: 'IS_BATCHSO', align: 'center', renderer: getItemValue},
    {header: "销售金额", dataIndex: 'AMOUNT', align: 'center', style: 'text-align:right'},
    {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
//    {header: "发票号", dataIndex: 'INVOICE_NO', align: 'center'},
//    {header: "物流单号", dataIndex: 'LOGISTICS_NO', align: 'center'},
    {header: "销售日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "提交日期", dataIndex: 'SUBMIT_DATE', align: 'center'},
    {header: "审核日期", dataIndex: 'FCAUDIT_DATE', align: 'center'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

function myLink(value, meta, record) {
<%--     if (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>" /*+ "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\",\"" + record.data.ORDER_ID + "\",\"" + record.data.SO_CODE + "\")'>[作废]</a>"+"<a href=\"#\" onclick='cancelSub(\"" + value + "\")'>[撤回]</a>"*/);
    }
    if (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_03%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_04%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>");
    }

    if (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_01%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_06%>) {
        if (record.data.IS_BATCHSO ==<%=Constant.PART_BASE_FLAG_YES%> && (null == record.data.WH_ID)) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='modifyOrder(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\",\"" + record.data.ORDER_ID + "\",\"" + record.data.SO_CODE + "\")'>[作废]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='modifyOrder(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\",\"" + record.data.ORDER_ID + "\",\"" + record.data.SO_CODE + "\")'>[作废]</a>" + "<a href=\"#\" onclick='confirmSubmitOrd(\"" + value + "\",\"" + record.data.ORDER_ID + "\",\"" + record.data.SO_CODE + "\")'>[提交]</a>");
        }
    } --%>
    <%-- if (record.data.ORDER_TYPE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%> && (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%> || record.data.STATE ==<%=Constant.CAR_FACTORY_PKG_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_PKG_STATE_01%> || record.data.STATE ==<%=Constant.CAR_FACTORY_TRANS_STATE_01%>)) {
        //MyAlert(record.data.TRANS_PRINT_DATE);
        if (record.data.TRANS_PRINT_DATE != null) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='printDirectOrder(\"" + value + "\")'>[打印发运单]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='printDirectOrder(\"" + value + "\")'><font color='red'>[打印发运单]</font></a>");
        }
    } --%>
    return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>");
}
function cancelSub(soId) {
	MyConfirm("确定撤回?",confirmResult,[soId]);
}

function confirmResult(soId){
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/cancelSub.json?soId=" + soId;
        makeNomalFormCall(url, getResult, 'fm');
}

function ckAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].disabled) {
            continue;
        }
        cb[i].checked = obj.checked;
    }
}

function printDirectOrder(soId) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/checkDirOrderPrint.json?soId=" + soId;
    makeNomalFormCall(url, checkResult, 'fm');
}

function printDirectMethod(soId) {
    window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/printDirectOrder.do?soId=" + soId, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

function checkResult(json) {
    enableAllClEl();
    if (null != json) {
        if (json.success != null) {
            MyConfirm(json.success, printDirectMethod, [json.soId]);
        } else {
            MyAlert("委托打印验证失败，请联系管理员!");
        }
    }
}

function ck(obj) {
    var cb = document.getElementsByName("cb");
    var flag = true;
    for (var i = 0; i < cb.length; i++) {
        if (!cb[i].checked) {
            flag = false;
        }
    }
    $('cbAll').checked = flag;
}
function checkLink(value, meta, record) {
    if (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_03%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_04%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {
        return String.format('<img src="<%=contextPath%>/img/close.gif" />');
    }
    if (record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_01%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_06%>) {
        if (record.data.IS_BATCHSO ==<%=Constant.PART_BASE_FLAG_YES%> && (null == record.data.WH_ID)) {
            return String.format('<img src="<%=contextPath%>/img/close.gif" />');
        } else {
            return String.format("<input id='cb' name='cb' type='checkbox' onclick='ck(this)' value='" + value + "' />");
        }
    }
    return String.format('<img src="<%=contextPath%>/img/close.gif" />');
}
//查看
function detailOrder(value, code) {
    var buttonFalg = "disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder.do?soId=" + value + "&&soCode=" + code + "&buttonFalg=" + buttonFalg, 800, 400);
}
//作废(确认)
function confirmCancelOrder(value, orderId, soCode) {
    MyConfirm("确定要作废订单?", cancelOrder, [value, orderId, soCode])
}
//作废
function cancelOrder(soId, orderId, soCode) {
    if (orderId == null || orderId == 'null') {
        orderId = "";
    }
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/cancelOrder.json?soId=" + soId + "&&orderId=" + orderId + "&&soCode=" + soCode;
    makeNomalFormCall(url, getResult, 'fm');
}
//提交
function confirmSubmitOrd(soId, orderId, soCode) {
    MyConfirm("确定提交?", submitOrd, [soId, orderId, soCode]);
}
//提交(确认)
function submitOrd(soId, orderId, soCode) {
    if (orderId == null || orderId == 'null') {
        orderId = "";
    }
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/commitOrder.json?soId=" + soId + "&&orderId=" + orderId + "&&soCode=" + soCode;
    makeNomalFormCall(url, getResult, 'fm');
}
//修改
function modifyOrder(value) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/modifyOrder.do?soId=" + value;
}
//新增
function addOrder() {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/addOrder.do?";
}

function doQuery() {
    var msg = "";
    if ("" != document.getElementById("SendDate").value) {
        if ("" == document.getElementById("SstartDate").value) {
            msg += "请选择销售开始时间!</br>";
        }
    }
    if ("" != document.getElementById("SstartDate").value) {
        if ("" == document.getElementById("SendDate").value) {
            msg += "请选择销售结束时间!</br>";
        }
    }
    if ("" != document.getElementById("RendDate").value) {
        if ("" == document.getElementById("RstartDate").value) {
            msg += "请选择提交财务开始时间!</br>";
        }
    }
    if ("" != document.getElementById("RstartDate").value) {
        if ("" == document.getElementById("RendDate").value) {
            msg += "请选择提交财务结束时间!</br>";
        }
    }
    if (msg != "") {
        MyAlert(msg);
        return;
    }
    //__extQuery__(1);
    makeNomalFormCall(url, initResult, 'fm');
}

function initResult(json) {
    if (json != null) {
        document.getElementById("sumAmount").value= json.accountSum;
    	document.getElementById("xs").value= json.xs;
    }else{
    	document.getElementById("sumAmount").value= 0;
    	document.getElementById("xs").value= 0;
    }
    __extQuery__(1);
}

function exportExcel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/exportSoOrderExcel.do?";
    fm.submit();
}

function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}

function enableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
    }
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}

function getCbArr() {
    var cbArr = [];
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cbArr.push(cb[i].value);
        }
    }
    return cbArr;
}
function unitPickOrderConfirm() {
    var flag = false;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
        }
    }
    if (!flag) {
        MyAlert("请选择一条记录!");
        return;
    }
    MyConfirm("确认提交?", batchToFinance, []);
}
function batchToFinance() {
    var ar = getCbArr();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/batchToFinance.json?cbAr=" + ar;
    disableAllClEl()
    makeNomalFormCall(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
            __extQuery__(1);
        }
    }
}
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}
function exportDetailExcel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/exportDetailExcel.do?";
    fm.submit();
}

function initCondition() {
    if ($('#condition_orderCode')[0]) {
        $('#orderCode')[0].value = $('#condition_orderCode')[0].value;
    }
    if ($('#condition_ORDER_TYPE')[0]) {
        $('#ORDER_TYPE')[0].value = $('#condition_ORDER_TYPE')[0].value;
    }
    if ($('#condition_salesOrderId')[0]) {
        $('#salesOrderId')[0].value = $('#condition_salesOrderId')[0].value;
    }
    if ($('#condition_whId')[0]) {
        $('#whId')[0].value = $('#condition_whId')[0].value;
    }
    if ($('#condition_sellerName')) {
        $('#sellerName')[0].value = $('#condition_sellerName')[0].value;
    }
    /*if($('#condition_isBatchso')[0]){
     $('#isBatchso')[0].value=$('#condition_isBatchso')[0].value;
     }*/
    if ($('#condition_SstartDate')[0]) {
        $('#SstartDate')[0].value = $('#condition_SstartDate')[0].value == "" ? $('#SstartDate')[0].value : $('#condition_SstartDate')[0].value;
    }
    if ($('condition_SendDate')[0]) {
        $('#SendDate')[0].value = $('#condition_SendDate')[0].value == "" ? $('#SendDate')[0].value : $('#condition_SendDate')[0].value;
    }
    if ($('#condition_RstartDate')[0]) {
        $('#RstartDate')[0].value = $('#condition_RstartDate')[0].value;
    }
    if ($('#condition_RendDate')[0]) {
        $('#RendDate')[0].value = $('#condition_RendDate')[0].value;
    }
    if ($('#condition_buyerName')[0]) {
        $('#buyerName')[0].value = $('#condition_buyerName')[0].value;
    }
    if ($('#condition_buyerCode')[0]) {
        $('#buyerCode')[0].value = $('#condition_buyerCode')[0].value;
    }
    if ($('#condition_finStat')[0] &&　$('#finStat')[0]) {
        $('#finStat')[0].value = $('#condition_finStat')[0].value == "" ? "0" : $('#condition_finStat')[0].value;
    }
    /*if ($('#condition_outFlag')[0]) {
     $('#outFlag')[0].value = $(#condition_outFlag')[0].value;
     }*/
    /*    if ($('#condition_repFlag')[0]) {
     $('#repFlag')[0].value = $('#condition_repFlag')[0].value==""?"1":$('#condition_repFlag')[0].value;
     }*/
    /*if ($('#condition_invoiceFlag')[0]) {
     $('#invoiceFlag')[0].value = $('#condition_invoiceFlag')[0].value;
     }*/
    /*   if ($('#condition_pickOrderFlag')[0]) {
     $('#pickOrderFlag')[0].value = $('#condition_pickOrderFlag')[0].value;
     }*/
    if ($('#condition_salerId')[0]) {
        $('#salerId')[0].value = $('#condition_salerId')[0].value == "" ? ${curUserId} : $('#condition_salerId')[0].value;
    }

}

//zhumingwei 2013-09-16
function viewSoOrder(value, meta, record) {
    var soId = record.data.SO_ID;
    var soCode = record.data.SO_CODE;
    if (soCode != null) {
        return String.format("<a href=\"#\" title='查看销售单明细' onclick='detailOrder(\"" + soId + "\",\"" + soCode + "\")' >" + soCode + "</a>");
    } else {
        return String.format("");
    }
}
function viewOrder(value, meta, record) {
    var ORDER_ID = record.data.ORDER_ID;
    var ORDER_CODE = record.data.ORDER_CODE;
    if (ORDER_CODE != null) {
        return String.format("<a href=\"#\" title='查看订单明细' onclick='viewOrderDtl(\"" + ORDER_ID + "\",\"" + ORDER_CODE + "\")' >" + ORDER_CODE + "</a>");
    } else {
        return String.format("");
    }
}
function viewOrderDtl(ORDER_ID, ORDER_CODE) {
    var buttonFalg = "disabled";//用于判断跳转页面是返回还是关闭
    OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=' + ORDER_ID + '&orderCode=' + ORDER_CODE + '&buttonFalg=' + buttonFalg, 800, 440);
}

//zhumingwei add 2013-10-23 添加销售快报
function detailOrder1(value, code) {
    var SstartDate = $('SstartDate').value;
    var SendDate = $('SendDate').value;
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder1.do?SstartDate=" + SstartDate + "&&SendDate=" + SendDate, 800, 400);
}


$(document).ready(function(){
	initCondition();
	//__extQuery__(1);
	doQuery();
	enableAllClEl();
});

</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="condition_orderCode" name="condition_orderCode" value="${condition.orderCode}" type="hidden"/>
    <input id="condition_ORDER_TYPE" name="condition_ORDER_TYPE" value="${condition.ORDER_TYPE}" type="hidden"/>
    <input id="condition_salesOrderId" name="condition_salesOrderId" value="${condition.salesOrderId}" type="hidden"/>
    <input id="condition_whId" name="condition_whId" value="${condition.whId}" type="hidden"/>
    <input id="condition_sellerName" name="condition_sellerName" value="${condition.sellerName}" type="hidden"/>
    <%--<input id="condition_isBatchso" name="condition_isBatchso" value="${condition.isBatchso}" type="hidden" />--%>
    <input id="condition_SstartDate" name="condition_SstartDate" value="${condition.SstartDate}" type="hidden"/>
    <input id="condition_SendDate" name="condition_SendDate" value="${condition.SendDate}" type="hidden"/>
    <input id="condition_RstartDate" name="condition_RstartDate" value="${condition.RstartDate}" type="hidden"/>
    <input id="condition_RendDate" name="condition_RendDate" value="${condition.RendDate}" type="hidden"/>
    <input id="condition_buyerName" name="condition_buyerName" value="${condition.buyerName}" type="hidden"/>
    <input id="condition_buyerCode" name="condition_buyerCode" value="${condition.buyerCode}" type="hidden"/>
    <input id="condition_finStat" name="condition_finStat" value="${condition.finStat}" type="hidden"/>
    <input id="condition_outFlag" name="condition_outFlag" value="${condition.outFlag}" type="hidden"/>
    <input id="condition_repFlag" name="condition_repFlag" value="${condition.repFlag}" type="hidden"/>
    <input id="condition_invoiceFlag" name="condition_invoiceFlag" value="${condition.invoiceFlag}" type="hidden"/>
    <input id="condition_pickOrderFlag" name="condition_pickOrderFlag" value="${condition.pickOrderFlag}" type="hidden"/>
    <input id="condition_salerId" name="condition_salerId" value="${condition.salerId}" type="hidden"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;
        	当前位置: 配件管理 > 配件销售管理 >配件销售单
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">订单号:</td>
	                <td><input class="middle_txt" name="orderCode" id="orderCode" type="text"/></td>
	                <td class="right">订货单位编码:</td>
	                <td><input class="middle_txt" id="buyerCode" name="buyerCode" type="text"/></td>
	                <td class="right">订货单位:</td>
	                <td><input class="middle_txt" id="buyerName" name="buyerName" type="text"/></td>
	            </tr>
	            <tr>
	                <td  class="right">销售单号:</td>
	                <td ><input class="middle_txt" id="salesOrderId" name="salesOrderId" type="text"/></td>
	                <td  class="right">销售单位:</td>
	                <td ><input class="middle_txt" id="sellerName" name="sellerName" type="text"/></td>
	                <td  class="right">订单类型：</td>
	                <td id="planTypeSel">
	                    <script type="text/javascript">
	                    	genSelBox("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td  class="right">销售日期:</td>
	                <td><input name="SstartDate" type="text" class="short_txt" style="width:80px"
	                                       id="SstartDate" value="${old}"/>
	                    <input name="button" value=" " type="button" class="time_ico"/>
	                  	  至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" style="width:80px" value="${now}"/>
	                    <input name="button" value=" " type="button" class="time_ico"/>
	                </td>
	                <td class="right">提交日期:</td>
	                <td><input name="RstartDate" type="text" class="short_txt" style="width:80px"
	                                       value="${old}" id="RstartDate"/>
	                    <input name="button" value=" " type="button" class="time_ico"/>
	                    	至
	                    <input name="RendDate" type="text" class="short_txt" id="RendDate" value="${now}"
	                           style="width:80px"/>
	                    <input name="button" value=" " type="button" class="time_ico"/></td>
	
	                <td class="right">销售人员：</td>
	                <td>
	                    <select name="salerId" id="salerId"  class="u-select">
	                        <option value="">--请选择--</option>
	                        <c:if test="${salerFlag}">
	                            <c:forEach items="${salerList}" var="saler">
	                                <c:choose>
	                                    <c:when test="${curUserId eq saler.USER_ID}">
	                                        <option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
	                                    </c:when>
	                                    <c:otherwise>
	                                        <option value="${saler.USER_ID}">${saler.NAME}</option>
	                                    </c:otherwise>
	                                </c:choose>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <%-- <td class="right">是否生成提货单:</td>
	                 <td>
	                     <select name="pickOrderFlag" id="pickOrderFlag"  class="u-select">
	                         <option selected value=''>-全部-</option>
	                         <option value='0'>-未生成-</option>
	                         <option value='1'>-已生成-</option>
	                     </select>
	                 </td>--%>
	                <%-- <td class="right">是否开票:</td>
	                 <td>
	                     <select name="invoiceFlag" id="invoiceFlag"  class="u-select">
	                         <option selected value=''>-全部-</option>
	                         <option value='0'>-未开票-</option>
	                         <option value='1'>-已开票-</option>
	                     </select>
	                 </td>--%>
	                <td class="right">排序方式：</td>
	                <td>
	                    <select id="order_By" name="order_By" class="u-select">
	                        <option value="">-请选择-</option>
	                        <option value="1">订单号</option>
	                        <option value="2">提报日期</option>
	                        <option value="3">服务商编码</option>
	                        <option value="4">订单类型</option>
	                    </select>
	                </td>
	                <td class="right">出库仓库:</td>
	                <td >
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                
	                <!-- <td class="right">审核状态:</td>
	                <td>
	                    <select name="finStat" id="finStat"  class="u-select">
	                        <option value=''>-全部-</option>
	                        <option selected value='0'>-未审核-</option>
	                        <option value='1'>-已审核-</option>
	                        <option value='2'>-驳回-</option>
	                    </select>
	                </td> -->
	            </tr>
	            <tr>
	                <%-- <td class="right">提交状态:</td>
	                 <td>
	                     <select name="repFlag" id="repFlag"   class="u-select">
	                         <option selected value=''>-全部-</option>
	                         <option value='0'>-未提交-</option>
	                         <option value='1'>-已提交-</option>
	                     </select>
	                 </td>--%>
	                <%--<td class="right">财务审核状态:</td>
	                <td>
	                    <select name="finStat" id="finStat"  class="u-select">
	                        <option value=''>-全部-</option>
	                        <option selected value='0'>-未审核-</option>
	                        <option value='1'>-已审核-</option>
	                        <option value='2'>-驳回-</option>
	                    </select>
	                </td>--%>
	                <%-- <td class="right">出库状态:</td>
	                 <td>
	                     <select name="outFlag" id="outFlag"  class="u-select">
	                         <option selected value=''>-全部-</option>
	                         <option value='0' title="财务审核通过未出库的销售单，状态包括财务审核通过、装箱中、已装箱">-未出库-</option>
	                         <option value='1' title="完成装箱出库的销售单，状态为已发运">-已出库-</option>
	                     </select>
	                 </td>--%>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                	<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
	                                                      value="查 询" onclick="doQuery();"/>
	                    &nbsp;
	                    <!--      <input class="normal_btn" type="button" value="新增" onclick="addOrder()"/>-->
	                    &nbsp;
	                    <input type="button" class="normal_btn" value="导出" onclick="exportExcel();"/>
	                   <%-- &nbsp;
	                    <input type="button" class="normal_btn" value="提交" onclick="unitPickOrderConfirm();"/>--%>
	                    &nbsp;
	                    <input type="button" class="normal_btn" value="导出明细" onclick="exportDetailExcel();"/>
	                    &nbsp;
	                  <%--  <input type="button" class="normal_btn" value="销售快报" onclick="detailOrder1();"/>
	                    &nbsp; &nbsp; &nbsp; &nbsp;--%>
	                    <font color="red">总金额(含税)：</font> <input type="text" class="short_txt" style="border: none;width:100px;"
	                                                             id="sumAmount" name="sumAmount" readonly/>
	                    <font color="red">总项数：</font> <input type="text" class="short_txt" id="xs"
	                                                         name="xs" readonly style="border: none;width:100px;"/>
	                </td>
	            </tr>
	        </table>
	        </div>
	    </div>    
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>