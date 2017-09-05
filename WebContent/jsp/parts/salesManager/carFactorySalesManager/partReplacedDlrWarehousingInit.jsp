<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>切换件验收入库</title>
</head>
<script language="javascript">
//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderBackhaulQuery.json?ORDER_TYPE=" +<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>;
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
    {header: "订单号", dataIndex: 'ORDER_CODE', style: 'text-align:center'},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', style: 'text-align:center', renderer: getItemValue},
    {header: "物流单号", dataIndex: 'WULIU_CODE', style: 'text-align:left'},
    {header: "物流公司", dataIndex: 'WULIU_COMPANY', style: 'text-align:left'},
    {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
    {header: "回运人", dataIndex: 'BUYER_NAME', style: 'text-align:left'},
    {header: "提交时间", dataIndex: 'SUBMIT_DATE', style: 'text-align:center'},//应取回运日期
    {header: "订单状态", dataIndex: 'STATE', style: 'text-align:center', renderer: getItemValue}

];

function myLink(value, meta, record) {
    if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07%>) {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
    }
    return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>"
            + "<a href=\"#\" onclick='backhaulOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[验收入库]</a>"
            + "<a href=\"#\" onclick='confirmRebutOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[驳回]</a>"
            + "<a href=\"#\" onclick='confirmCloseOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[关闭]</a>");
}

//验收入库
function backhaulOrder(value, type) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/warehousingDlrOrder.do?orderId=" + value;
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

//查看
function detailOrder(value, code) {
    var buttonFalg = "disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/detailDlrOrder.do?orderId=" + value + "&&orderCode=" + code + "&buttonFalg=" + buttonFalg, 900, 400);
}

//关闭
function confirmCloseOrder(v1,v2) {
    MyConfirm("确定要关闭?", closeOrder, [v1,v2])
}
function closeOrder(value,value2) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/closeOrder.json?orderId=" + value+"&orderCode="+value2;
    sendAjax(url, getResult, 'fm');
}
//驳回
function confirmRebutOrder(v1,v2) {
    MyConfirm("确定要驳回?", reButOrder, [v1,v2])
}
function reButOrder(value,value2) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/reButOrder.json?orderId=" + value+"&orderCode="+value2;
    sendAjax(url, getResult, 'fm');
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
//查询
function doQuery() {
    //执行查询
    __extQuery__(1);
}
function exportEx() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/exportPartExcelIn.do";
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
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}

function doInit() {
    //默认状态
    $('state').value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13%>;
    __extQuery__(1);
}
</script>
<body onload="loadcalendar();doInit();autoAlertException();enableAllClEl()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden"
           value="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>"/>

    <div class="wbox">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理&gt;配件采购管理&gt;切换件验收入库
        </div>
        <table border="0" class="table_query">
            <th colspan="6"><img class="nav"
                                 src="<%=contextPath%>/img/subNav.gif"/>查询条件
            </th>
            <tr>
                <td width="10%" align="right">订单号：</td>
                <td width="20%" align="left"><input class="middle_txt"
                                                    type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                <td width="10%" align="right">订货单位编码：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" id="DEALER_CODE" name="DEALER_CODE"/>
                </td>
                <td width="10%" align="right">订货单位：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">物流单号：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" id="wlNO" name="wlNO"/></td>
                <td width="10%" align="right">物流公司：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" id="wl" name="wl"/></td>
                <td width="10%" align="right">状态：</td>
                <td width="20%" align="left"><select name="state" id="state"
                                                     class="short_sel">
                    <!-- 							<option selected value=''>请选择</option> -->
                    <c:forEach items="${stateMap}" var="stateMap">
                        <option value="${stateMap.key}">${stateMap.value}</option>
                    </c:forEach>
                </select></td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input name="BtnQuery"
                                                      id="queryBtn" class="normal_btn" type="button" value="查 询"
                                                      onclick="doQuery();"/>
                    <!--                     <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/> -->
                    <input class="normal_btn" type="button" value="导 出"
                           onclick="exportEx()"/></td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>

</html>
