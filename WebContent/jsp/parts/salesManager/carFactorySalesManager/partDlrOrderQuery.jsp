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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>配件订单审核查询（销售订单）</title>
<script language="javascript">
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderQuery.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
    {header: "订单次数", dataIndex: 'ORDER_NUM', align: 'center'},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left;'},
    {header: "备注", dataIndex: 'REMARK', style: 'text-align:left;',renderer:orderRemaker},
    {header: "订货人", dataIndex: 'BUYER_NAME', style: 'text-align:left;'},
    // {header: "订货日期", dataIndex: 'CREATE_DATE', align: 'center'},
    /*{header: "销售单位", dataIndex: 'SELLER_NAME', align:'center'},*/
    {header: "订货总金额(元)", dataIndex: 'ORDER_AMOUNT', style: 'text-align:right'},
    {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center'},
    {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    /*{header: "驳回原因", dataIndex: 'REBUT_REASON', align: 'center'}*/
    /* {header: "已预审", dataIndex: 'IS_AUTCHK', align: 'center', renderer: getItemValue}*/

];
function myLink(value, meta, record) {
    if ((record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06%>) && record.data.OEM_FLAG != "<%=Constant.IF_TYPE_YES%>") {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" /*+ "<a href=\"#\" onclick='modify(\"" + value + "\")'>[修改]</a>"*/ + "<a href=\"#\" onclick='rebutConfirm(\"" + value + "\")'>[驳回]</a>" + "<a href=\"#\" onclick='checkOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[审核]</a>"/*+"<a href=\"#\" onclick='confirmClose(\"" + value  + "\")'>[强制关闭]</a>"*/);
    }
    if ((record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06%>) && record.data.OEM_FLAG == "<%=Constant.IF_TYPE_YES%>") {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" /*+ "<a href=\"#\" onclick='modify(\"" + value + "\")'>[修改]</a>"*/ + "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\")'>[作废]</a>" + "<a href=\"#\" onclick='checkOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[审核]</a>"/*+"<a href=\"#\" onclick='confirmClose(\"" + value  + "\")'>[强制关闭]</a>"*/);
    }
    return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
}
function orderRemaker(value, meta, record){
    return String.format("<input type='text' style='border:none' id='remark' name='remark' value='"+value+"' onchange='saveRemark(this,\"" + record.data.ORDER_ID + "\")'>");
}
function saveRemark(obj,value){
    $("#remark")[0].value = obj.value;
    var updateUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/updateOrderRemark.json?orderId=" + value;
    makeNomalFormCall(updateUrl, closeResult, 'fm');
}
//查看
function detailOrder(value, code) {
    var buttonFalg = "disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=" + value + "&&orderCode=" + code + "&buttonFalg=" + buttonFalg, 900, 400);
}
function confirmClose(id) {
	MyConfirm("确定关闭?",confirmResult,[id]);
}

function confirmResult(id){
	closeOrder(id)
}

function closeOrder(id) {
    var closeUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/closeOrderAction.json?orderId=" + id;
    makeNomalFormCall(closeUrl, closeResult, 'fm');
}
function closeResult(jsonObj) {
    if (jsonObj != null) {
        __extQuery__(1);
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
        }
    }
}
//审核
function checkOrder(value, type) {
    disableAllClEl();
    //常规订单、紧急订单、促销订单、委托订单、销售采购订单
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/checkDlrOrder.do?orderId=" + value + "&planFlag=" + $('planFlag').value;
}
//导出
function exportEx() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/exportPartPlanExcel.do";
    fm.submit();
}
//查询
function doQuery() {
    var msg = "";
    if (msg != "") {
        //弹出提示
        MyAlert(msg);
        return;
    }
    //执行查询
//     __extQuery__(1);

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
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}

function rebutConfirm(id) {
	MyConfirm("确定驳回?",rebutReason,[id]);
}

function rebutReason(id) {
    OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/rebutReason.jsp?id=' + id, 300, 200);
}
function rebut(id, reason) {
    var rebutUrl = encodeURI(encodeURI("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/rebut.json?orderId=" + id + "&reason=" + reason));
    makeNomalFormCall(rebutUrl, rebutResult, 'fm');
}
function rebutResult(jsonObj) {
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
        }
    }
}
function modify(id) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/modifyDlrOrder.do?orderId=" + id;
}
//新增
function addOrder() {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOemDlrOrder/addOrder.do?orderType=" + $('ORDER_TYPE').value;
}

function initCondition() {
    if ($('#condition_orderCode')[0]) {
        document.getElementById("ORDER_CODE").value = document.getElementById("condition_orderCode").value;
    }
    if ($('#condition_dealerName')[0]) {
        document.getElementById("DEALER_NAME").value = document.getElementById("condition_dealerName").value;
    }
    if ($('#condition_state')[0]) {
        $('#state')[0].value = $('#condition_state')[0].value == "" ? <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> : $('#condition_state')[0].value;
    }
    if ($('#condition_orderType')[0]) {
        $('#ORDER_TYPE')[0].value = $('#condition_orderType')[0].value;
    }
    if ($('condition_salerId')) {
        $('#salerId')[0].value = $('#condition_salerId')[0].value == "" ? ${curUserId} : $('#condition_salerId')[0].value;
    }
}

//作废
function confirmCancelOrder(value) {
    MyConfirm("确定作废订单?", cancelOrder, [value]);
}
function cancelOrder(value) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/cancelOrder.json?orderId=" + value + "&flag=1";
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
        }
    }
}
//初始化
$(document).ready(function(){ 
	if (${planFlag=="plan"}) {
        document.getElementById("planType").style.display = "none";
        document.getElementById("planTypeSel").style.display = "none";
    }
    initCondition();
    //__extQuery__(1);
    doQuery() ;
    enableAllClEl();

});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="planFlag" name="planFlag" value="${planFlag}" type="hidden"/>
    <input id="condition_orderCode" name="condition_orderCode" value="${condition.orderCode}" type="hidden"/>
    <input id="condition_dealerName" name="condition_dealerName" value="${condition.dealerName}" type="hidden"/>
    <input id="condition_sellerName" name="condition_sellerName" value="${condition.sellerName}" type="hidden"/>
    <input id="condition_state" name="condition_state" value="${condition.state}" type="hidden"/>
    <input id="condition_orderType" name="condition_orderType" value="${condition.orderType}" type="hidden"/>
    <input id="condition_salerId" name="condition_salerId" value="${condition.salerId}" type="hidden"/>
    <input id="condition_CstartDate" name="condition_CstartDate" value="${condition.CstartDate}" type="hidden"/>
    <input id="condition_CendDate" name="condition_CendDate" value="${condition.CendDate}" type="hidden"/>
    <input id="condition_SstartDate" name="condition_SstartDate" value="${condition.SstartDate}" type="hidden"/>
    <input id="condition_SendDate" name="condition_SendDate" value="${condition.SendDate}" type="hidden"/>
    <input id="remark" name="remark"  type="hidden"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 &gt; 配件销售管理 &gt;配件订单审核</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">订单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
	                <td class="right">订货单位编码：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="DEALER_CODE" name="DEALER_CODE"/></td>
	                <td class="right">订货单位：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
	                <%--  <td   class="right">销售单位：</td>
	                  <td width="24%"><input class="middle_txt" type="text" id="SELLER_NAME" name="SELLER_NAME"/></td>--%>
	            </tr>
	            <tr>
	                <td class="right" style="display: none">订单状态：</td>
	                <td width="24%" style="display: none">
	                    <script type="text/javascript">
	                        genSelBox("state", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE%>, "<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right" id="planType">订单类型：</td>
	                <td  id="planTypeSel">
	<!--                     <script type="text/javascript"> -->
	<%--                         genMySelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "u-select", "", "false", ''); --%>
	<!--                     </script> -->
	                    
	                     <select name="ORDER_TYPE" id="ORDER_TYPE"  class="u-select">
	                        <option selected="selected" value=''>-请选择-</option>
	                        <c:forEach items="${orderType}" var="orderType">
	                            <option value="${orderType.key}">${orderType.value}</option>
	                        </c:forEach>
	                    </select>
	                    
	                </td>
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
	                <td class="right">排序方式：</td>
	                <td>
	                    <select id="order_By" name="order_By"  class="u-select">
	                        <option value="">-请选择-</option>
	                        <option value="1">订单号</option>
	                        <option value="2">提报日期</option>
	                        <option value="3">服务商编码</option>
	                        <option value="4">订单类型</option>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	              <%--  <td class="right">制单日期：</td>
	                <td width="24%">
	                    <input name="CstartDate" type="text" class="short_time_txt" id="CstartDate" value="${old}"
	                           style="width:80px"/>
	                    <input name="button2" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'CstartDate', false);"/>
	                    至
	                    <input name="CendDate" type="text" class="short_time_txt" id="CendDate" value="${now}"
	                           style="width:80px"/>
	                    <input name="button2" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'CendDate', false);"/>--%>
	                <td class="right">提交时间：</td>
	                <td width="24%" colspan="2">
	                	<input name="SstartDate" type="text" class="short_txt calendar-input-long" id="SstartDate"  style="width:80px" value="${old }"/>
	                    <input name="button2" value=" " type="button" class="time_ico"/>
	                    	至
	                    <input name="SendDate" type="text" class="short_txt calendar-input-long" id="SendDate" style="width:80px" value="${now }"/>
	                    <input name="button2" value=" " type="button" class="time_ico" />
	                </td>
	                <td width="24%"></td>
	 				<td class="right"></td>
	                <td width="24%"></td>
	            </tr>
	            <tr>
	                <td colspan="3" class="right">
	                	<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="doQuery();"/>
	                    &nbsp;
	<!--                     <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/> -->
	<!--                     &nbsp; -->
	                    <input class="u-button" type="button" value="导 出" onclick="exportEx()"/>
	                </td>
	                <td colspan="3"  class="center">
	                    <font color="red">总金额(含税)：</font> 
	                    <input type="text" class="short_txt txt-left" id="sumAmount" name="sumAmount" readonly style="width: 100px;"/>
	                    <font color="red">总项数：</font> 
	                    <input type="text" class="short_txt txt-left" id="xs" name="xs" readonly />
	                </td>
	            </tr>
	        </table>
	    	</div>    
	    </div>    
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
    </div>
</form>
</body>

</html>