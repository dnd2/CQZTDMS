<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/showPartBase.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "采购数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'},
    {header: "当前库存", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
    {header: "订购单价", dataIndex: 'SALE_PRICE1', style: 'text-align: right;', renderer: getDefaultValue},
    {header: "上级库存量", dataIndex: 'UPORGSTOCK', align: 'center'},
    {header: "是否紧缺", dataIndex: 'IS_LACK', align: 'center', renderer: inputText},
    {header: "是否可替代", dataIndex: 'IS_REPLACED', align: 'center', renderer: inputText},
    {header: "是否大件", dataIndex: 'IS_PLAN', align: 'center', renderer: inputText},
    {header: "是否直发", dataIndex: 'IS_DIRECT', align: 'center', renderer: inputText}

];
function getDefaultValue(value, meta, record) {
    if (record.data.SALE_PRICE1 == null) {
        return "<font color='RED'>无订购单价</font>";
    } else {
        return record.data.SALE_PRICE1;
    }
}
function getSaleText(value) {
    return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
}

function onchangeVlidateSaleQty(obj) {
    if (obj.value == "") {
        return;
    }
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }

    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('myTable');

    if (obj.value != '0') {
        tbl.rows[idx].cells[1].firstChild.checked = true;
    } else {
        tbl.rows[idx].cells[1].firstChild.checked = false;
    }
}
//打开、收起配件按钮			  
function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}
function inputText(value, meta, record) {
    var info = getItemValue(value);
    return "<input type='hidden' value='" + value + "' />" + info;
}
function seled(value, meta, record) {
    if (record.data.SALE_PRICE1 != null) {
        return "<input type='checkbox' value='" + value + "' onclick='cPartCb()' name='ck' id='ck' />";
    } else {
        return "<input type='checkbox' disabled value='" + value + "' name='ck' id='ck' />";
    }
}
function cPartCb() {
    var flag = true;
    var ck = document.getElementsByName("ck");
    for (var i = 0; i < ck.length; i++) {
        if (!ck[i].checked && !ck[i].disabled) {
            flag = false;
        }
    }
    document.getElementById("ckbAll").checked = flag;
}
function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    reNumIdx(idx);
    countAll();
}
function reNumIdx(idx) {
    var tbl = document.getElementById("file");
    for (var i = idx; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerHTML = (i - 2);
    }
}

//选择经销商(1) 接收单位(2)地址(3)
function selSales(inputId, inputCode, inputName, inputLinkMan, inputTel, inputPostCode, inputStation, dealerId, type) {
    if (type == 3) {
        if ("" == dealerId) {
            MyAlert("请选择接收单位!");
            return;
        }
    }
    if (!inputId) {
        inputId = null;
    }
    if (!inputCode) {
        inputCode = null;
    }
    if (!inputName) {
        inputName = null;
    }
    if (!inputLinkMan) {
        inputLinkMan = null;
    }
    if (!inputTel) {
        inputTel = null;
    }
    if (!inputPostCode) {
        inputPostCode = null;
    }
    if (!inputStation) {
        inputStation = null;
    }
    if (!dealerId) {
        dealerId = null;
    }
    if (!type) {
        type = null;
    }

    OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/selSales.jsp?dealerId=' + dealerId + '&type=' + type + '&inputName=' + inputName + '&inputId=' + inputId + '&inputCode=' + inputCode + '&inputLinkMan=' + inputLinkMan + '&inputTel=' + inputTel + '&inputPostCode=' + inputPostCode + '&inputStation=' + inputStation, 730, 390);
}
function selAll(obj, selObjId) {
    var cb = document.getElementsByName(selObjId);
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].disabled) {
            continue;
        }
        cb[i].checked = obj.checked;
    }
}
//为下面TABLE生成数据
function addCells() {
    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    if ($("wh_id").value == "") {
        MyAlert("请选择库房");
        return;
    }
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            if (validateCell(partId)) {
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var saleQty = mt.rows[i].cells[6].firstChild.value;  //销售数量
                var itemQty = mt.rows[i].cells[7].innerText;  //当前库存
                var minPackage = mt.rows[i].cells[8].innerText;  //最小包装量
                var buyPrice = mt.rows[i].cells[9].innerText;  //订购单价
                var upOrgStock = mt.rows[i].cells[10].innerText;  //上级库存量
                var isLack = mt.rows[i].cells[11].firstChild.value;  //是否紧缺
                var isReplaced = mt.rows[i].cells[12].firstChild.value;  //是否可替代
                var isPlan = mt.rows[i].cells[13].firstChild.value;  //是否大件
                var isDirect = mt.rows[i].cells[14].firstChild.value;  //是否可替代

                addCell(partId, partCode, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, isPlan, isDirect, saleQty)
            } else {
                MyAlert("第" + i + "行配件" + mt.rows[i].cells[4].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    var whId = $("wh_id").value;
    var partId = "";
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        partId += "," + cb[i].value;
    }
    getPartItemStock(whId, partId);
    countAll();
}

function addCell(partId, partCode, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, isPlan, isDirect, saleQty) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }

    //获取折扣率
    var discount = document.getElementById("discount").value;
    var buyPrice1 = (parseFloat(buyPrice) * parseFloat(discount) * discount).toFixed(2);

    var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' >";
    var hiddenHtmlIsLack = "<input type='hidden' name='isLack_" + partId + "' id='isLack_" + partId + "' value='" + isLack + "' >";
    var hiddenHtmlIsReplaced = "<input type='hidden' name='isReplaced_" + partId + "' id='isReplaced_" + partId + "' value='" + isReplaced + "' >";
    var hiddenHTMLIsPlan = "<input type='hidden' name='isPlan_" + partId + "' id='isPlan_" + partId + "' value='" + isPlan + "' >";
    var hiddenHTMLIsDirect = "<input type='hidden' name='isDirect_" + partId + "' id='isDirect_" + partId + "' value='" + isDirect + "' >";
    createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 3)), "cb", "", "", "checkbox", partId, true, false, "");

    createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 3), false, false, "");
    createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
    createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
    createCells(rowObj, 4, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
    createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
    createCells(rowObj, 6, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, "");
    createCells(rowObj, 7, ("itemQty_" + partId), ("itemQty_" + partId), "", "", "block", itemQty, false, false, "");
    createCells(rowObj, 8, ("buyQty_" + partId), ("buyQty_" + partId), "countMoney(this," + buyPrice1 + "," + partId + ")", "background-color:#FFFFCC;text-align:center", "text", saleQty, false, false, hiddenHtmlIsReplaced);
    createCells(rowObj, 9, ("buyPrice_" + partId), ("buyPrice_" + partId), "", "", "hidden", buyPrice, false, false, hiddenHTMLIsDirect);
    createCells(rowObj, 10, ("buyPrice1_" + partId), ("buyPrice1_" + partId), "", "", "hidden", buyPrice1, false, false, hiddenHTMLIsPlan);
    createCells(rowObj, 11, ("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
    createCells(rowObj, 12, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, hiddenHtmlIsLack);
    createCells(rowObj, 13, "", "", "", "", "button", "", false, true, hiddenHtml);


    if (saleQty != "") {
        countMoney(document.getElementById("buyQty_" + partId), buyPrice1, partId);
    }
    countAll();
}
//校验是否重复生成
function validateCell(value) {
    var flag = true;
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (value == cb[i].value) {
            flag = false;
            break;
        }
    }
    return flag;
}


//生成CELL
function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var cell = obj.insertCell(idx);
    cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);

}

//生成CELL中的HTML
function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var tdStrHead = trHFlag == true ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
    var tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';
    var inputHtml = "";
    if (type == 'button') {
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt"  id="' + id + '" style="' + style + '" value="' + value + '" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'checkbox') {
        inputHtml = '<input  type="checkbox" onclick="countAll()"  id="' + id + '" name="cb" checked="true" value="' + value + '" />';
    }
    if (type == 'idx') {
        inputHtml = value;
    }
    if (type == 'block') {
        inputHtml = '<input  type="text" readonly style="border:0px;text-align:center" id="' + id + '" name="' + name + '" value="' + value + '" />';
    }
    if (hiddenHtml != "") {
        inputHtml += hiddenHtml;
    }

    return tdStrHead + inputHtml + tdStrEnd;
}
//计算金额
function countMoney(obj, price, partId) {
    var amout = 0;
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (value == '') {
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    var money = (parseFloat(price) * parseFloat(value)).toFixed(2);
    tbl.rows[idx].cells[11].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", money, false, false, "");
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(document.getElementById("buyAmount_" + cb[i].value).value)).toFixed(2);
            }
        }
    }

    document.getElementById("partAmount").value = amountCount;
    document.getElementById("Amount").value = amountCount;
    getFreight(amountCount);
}
//清除数据重新计算
function cleanData(partId, idx) {
    var tbl = document.getElementById('file');
    tbl.rows[idx].cells[11].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0.00", false, false, "");
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            amountCount = (parseFloat(amountCount) + parseFloat(document.getElementById("buyAmount_" + cb[i].value).value)).toFixed(2);
        }
    }
    document.getElementById("Amount").value = amountCount;

}

//保存订单确认
function saveOrderConfirm() {
    if (!validateFm()) {
        return;
    }
    MyConfirm('确定保存订单?', saveOrder, []);
    enableCb();

}
function validateFm() {
    var msg = "";
    if (document.getElementById("wh_id").value == "") {
        msg += "请选择出库仓库!</br>";
    }
    if (document.getElementById("RCV_ORG").value == "") {
        msg += "请选择接收单位!</br>";
    }
    if (document.getElementById("ADDR").value == "") {
        msg += "请选择接收地址!</br>";
    }
    if (document.getElementById("transType").value == "") {
        msg += "请选择发运方式!</br>";
    }
    if (document.getElementById("payType").value == "") {
        msg += "请选择付款方式!</br>";
    }
    if (document.getElementById("transpayType").value == "") {
        msg += "请选择运费支付方式!</br>";
    }
    if (document.getElementById("ORDER_TYPE").value == "") {
        msg += "请选择订单类型!</br>";
    }
    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加配件明细!</br>";
    }
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验计划量是否填写
            if (document.getElementById("buyQty_" + cb[i].value).value == "") {
                msg += "请填写第" + (i + 1) + "行的订货数量!</br>";
            }
        } else {
            cb[i].disabled = true;
        }
    }
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
            break;
        }
    }
    if (!flag) {
        msg += "请选择配件明细!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        enableCb();
        return false;
    }
    return true;
}
function enableCb() {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
//提报并保存订单确认
function repOrderConfirm() {
    if (!validateFm()) {
        return;
    }
    MyConfirm('确定保存订单并提报到财务?', repOrder, []);
    enableCb();
}
//保存订单
function saveOrder() {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/saveModify.json";
    sendAjax(url, getResult, 'fm');
}
//提报订单
function repOrder() {
    disableAllClEl();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALE_ORDER_STATE_02%>;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/saveOrder.json";
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
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/partSoManageInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
/* function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        owner.getElementById('dialog_content_div').innerHTML = '\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
        owner.getElementById('dialog_alert_info').innerHTML = info;
        owner.getElementById('dialog_alert_button').onclick = _hide;
        var height = 200;

        if (info.split('</br>').length >= 6) {
            height = height + (info.split('</br>').length - 6) * 27;
        }
        _setSize(300, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
} */
//显示余额
function showAcount(accountId, accountSum, accountKy, accountDj) {
    if (accountId != "") {
        document.getElementById("accountKy").value = accountKy;
        document.getElementById("accountId").value = accountId;
    } else {
        document.getElementById("accountId").value = "";
    }
}
//返回
function goBack() {
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/partSoManageInit.do';
}
//获取CHECKBOX
function getCb(partId) {
    document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' onclick='countAll()' name='cb' checked value='" + partId + "' />");
}
function getIdx() {
    document.write(document.getElementById("file").rows.length - 3);
}
//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
function doInit() {
    document.getElementById("partAmount").value = formatNum((${mainMap.AMOUNT}-${mainMap.FREIGHT}).toFixed(2));
    var selOp = document.getElementById("wh_id");
    for (var i = 0; i < selOp.length; i++) {
        if (selOp[i].value == '${mainMap.WH_ID}') {
            selOp[i].selected = true;
            break;
        }
    }
    var whId = $("#wh_id")[0].value;
    var partId = "";
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        partId += "," + cb[i].value;
    }
    if ("" != whId) {
        getPartItemStock(whId, partId);
    }
    document.getElementById("transType").value = "${mainMap.TRANS_TYPE}";

}
function checkAll(obj) {
    var cb = document.getElementsByName("cb");

    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }

    countAll();
}
//计算所有CHECK的金额
function countAll() {
    var flag = true;
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(document.getElementById("buyAmount_" + cb[i].value).value)).toFixed(2);
            }
        }
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
    document.getElementById("partAmount").value = formatNum(amountCount);
    document.getElementById("Amount").value = formatNum(amountCount);
    getFreight(amountCount);
}

function formatNum(str) {
    var len = str.length;
    var step = 3;
    var splitor = ",";
    var decPart = ".";
    if ((str + "").indexOf(".") > -1) {
        var strArr = str.split(".");
        str = strArr[0];
        decPart += strArr[1];
    }
    if (len > step) {
        var l1 = len % step, l2 = parseInt(len / step), arr = [], first = str.substr(0, l1);
        if (first != '') {
            arr.push(first);
        }
        ;
        for (var i = 0; i < l2; i++) {
            arr.push(str.substr(l1 + i * step, step));
        }
        ;
        str = arr.join(splitor);
        str = str.substr(0, str.length - 1);
    }
    ;
    if (decPart != ".") {
        str += decPart;
    }
　　return str;
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
function getPartQty() {
    if ($("#wh_id")[0].value == "") {
        MyAlert("请选择库房");
        return;
    }
    var whId = $("#wh_id")[0].value;
    var partId = "";
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        partId += "," + cb[i].value;
    }
    getPartItemStock(whId, partId);
}
function getPartItemStock(whId, partId) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/getPartItemStock.json?whId=" + whId + "&partId=" + partId;
    sendAjax(url, getPartResult, 'fm');
}
function getPartResult(jsonObj) {

    if (jsonObj != null || jsonObj.list != null) {
        var reAr = jsonObj.list;
        if (reAr) {
            for (var i = 0; i < reAr.length; i++) {
                var partId = reAr[i].PART_ID;
                var qty = reAr[i].NORMAL_QTY;
                document.getElementById("itemQty_" + partId).value = qty;
            }
        }
    } else {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            document.getElementById("itemQty_" + cb[i].value).value = 0;
        }
    }
    enableAllClEl();
}
function getFreight(amountCount) {
    disableAllClEl();
    if (${mainMap.SELLER_ID}!= <%=Constant.OEM_ACTIVITIES%>)
    {
        enableAllClEl();
        $('freight').value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if ($('#transType')[0].value == "3") {
        enableAllClEl();
        $('#freight')[0].value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if($("#transpayType")[0].value ==<%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>){
        enableAllClEl();
        $('#freight')[0].value = "0.00";
        countAllWithoutFreight();
        return;
    }
    var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
    sendAjax(getFreightUrl, getFreightResult, 'fm');
}
function getFreightResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        $('freight').value = jsonObj.freight;
        $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value)) + parseFloat(unFormatNum(jsonObj.freight))).toFixed(2));
    }
}
function formatNum(str) {
    var len = str.length;
    var step = 3;
    var splitor = ",";
    var decPart = ".";
    if ((str + "").indexOf(".") > -1) {
        var strArr = str.split(".");
        str = strArr[0];
        decPart += strArr[1];
    }
    if (len > step) {
        var l1 = len % step, l2 = parseInt(len / step), arr = [], first = str.substr(0, l1);
        if (first != '') {
            arr.push(first);
        }

        for (var i = 0; i < l2; i++) {
            arr.push(str.substr(l1 + i * step, step));
        }

        str = arr.join(splitor);
        str = str.substr(0, str.length - 1);
    }

    if (decPart != ".") {
        str += decPart;
    }
    return str;
}
function unFormatNum(str) {
    str = str + "";
    if ((str + "").indexOf(",") > -1) {
        str = str.replace(/\,/g, "");
    }
    return str;
}
function changeTrans() {
    var tbl = document.getElementById("file");
    var partDiv = document.getElementById("partDiv");
    for (var i = (tbl.rows.length - 1); i >= 2; i--) {
        tbl.deleteRow(i);
    }
    if (partDiv.style.display == "block") {
        var partDiv = document.getElementById("partDiv");
        __extQuery__(1);
    }
    countAll();
}
function countAllWithoutFreight() {
    var flag = true;
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(document.getElementById("buyAmount_" + cb[i].value).value)).toFixed(2);
            }
        }
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
    document.getElementById("Amount").value = amountCount;
}
//初始化
$(document).ready(function(){
	doInit()
});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="state" id="state" type="hidden" value="${mainMap.STATE}"/>
<input name="soId" id="soId" type="hidden" value="${mainMap.SO_ID}"/>
<input name="dealerId" id="dealerId" type="hidden" value="${mainMap.DEALER_ID}"/>
<input name="ORDER_TYPE" id="ORDER_TYPE" type="hidden" value="${mainMap.ORDER_TYPE}"/>
<input name="SELLER_ID" id="SELLER_ID" type="hidden" value="${mainMap.SELLER_ID}"/>
<input name="SO_FROM" id="SO_FROM" type="hidden" value="${mainMap.SO_FROM}"/>

<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:
    配件管理 > 配件销售管理 &gt;销售单>销售单修改
</div>
<table class="table_add" border="0"
       style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
       cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 配件销售信息</th>
    </tr>
    <tr>
        <td align="right">销售单号:</td>
        <td width="24%">${mainMap.SO_CODE}</td>
        <td align="right">销售日期:</td>
        <td width="24%">${mainMap.CREATE_DATE}</td>
        <td align="right">制单人:</td>
        <td width="24%">${mainMap.CREATE_BY_NAME}</td>
    </tr>
    <tr>
        <td align="right">订货单位:</td>
        <td><input name="dealerName" class="SearchInput" id="dealerName" value="${mainMap.DEALER_NAME}" type="hidden"
                   size="20" disabled readonly="readonly"/>${mainMap.DEALER_NAME}
            <input name="dealerCode" class="SearchInput" id="dealerCode" value="${mainMap.DEALER_CODE}" type="hidden"
                   size="20" disabled readonly="readonly"/>
            <input name="dealerId" class="SearchInput" id="dealerId" value="${mainMap.DEALER_ID}" type="hidden"
                   size="20" disabled readonly="readonly"/>
            <%--<input name='dlbtn2' id='dlbtn2' class='mini_btn' disabled  type='button' value='...'  onclick="selSales('sellerId','sellerCode','sellerName','','','','',${dataMap.dealerId},'1')"/>--%>
            </span></td>
        <td align="right">出库仓库:</td>
        <td>
            <select name="wh_id" id="wh_id" class="short_sel" onchange="getPartQty()">
                <c:forEach items="${wareHouseList}" var="wareHouse">
                    <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                </c:forEach>
            </select>

            <font color="RED">*</font></td>
        <td align="right">接收单位:</td>
        <td><input name="RCV_ORG" class="SearchInput" id="RCV_ORG" value="${mainMap.CONSIGNEES}" type="text" size="20"
                   readonly="readonly"/>
            <input name="RCV_CODE" class="SearchInput" id="RCV_CODE" type="hidden" size="20" readonly="readonly"/>
            <input name="RCV_ORGID" class="SearchInput" id="RCV_ORGID" type="hidden" value="${mainMap.CONSIGNEES_ID}"
                   size="20" readonly="readonly"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                   onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${dealerId},'2')"/>
            </span></td>
    </tr>
    <tr>
        <td align="right">接收地址:</td>
        <td><input name="ADDR" class="SearchInput" id="ADDR" type="text" value="${mainMap.ADDR}" size="20"
                   readonly="readonly"/>
            <input name="ADDR_ID" class="SearchInput" id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden" size="20"
                   readonly="readonly"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                   onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('RCV_ORGID').value,'3')"/>
            </span></td>
        <td align="right">接收人:</td>
        <td><input id="RECEIVER" name="RECEIVER" type="text" value="${mainMap.RECEIVER}" class="normal_txt"/>
            <font color="RED">*</font></td>
        <td align="right"><span align="right">接收人电话</span></td>
        <td><input id="TEL" name="TEL" type="text" value="${mainMap.TEL}" class="normal_txt" readonly/>
            <font color="RED">*</font></td>
    </tr>
    <tr>
        <td align="right">邮政编码:</td>
        <td><input id="POST_CODE" name="POST_CODE" type="text" value="${mainMap.POST_CODE}" class="normal_txt"
                   readonly/>
            <font color="RED">*</font></td>
        <td align="right">到站名称:</td>
        <td><input id="STATION" name="STATION" type="text" class="normal_txt" value="${mainMap.STATION}" readonly/>
            <font color="RED">*</font></td>
        <td align="right">发运方式:</td>
        <td>
            <select name="transType" id="transType" class="u-select" <%--onchange="changeTrans()"--%>>
                <option value="">-请选择-</option>
                <c:if test="${transList!=null}">
                    <c:forEach items="${transList}" var="list">
                        <option value="${list.fixValue }" selected>${list.fixName }</option>
                    </c:forEach>
                </c:if>
            </select>
            <font color="RED">*</font>
    </tr>
    <tr>
        <td align="right">运费支付方式:</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("transpayType", <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS%>, '${mainMap.TRANSPAY_TYPE}', true, "", "onchange=countAll();", "false", '');
            </script>
        </td>
        <td align="right">付款方式:</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("payType", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, '${mainMap.PAY_TYPE}', true, "", "", "false", '');
            </script>
            <font color="RED">*</font></td>
        <td width="109" align="right">订单类型:</td>
        <td width="260">
            <script type="text/javascript">
                genSelBoxExp("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, '${mainMap.ORDER_TYPE}', true, "", " disabled", "false", '');
            </script>
            <font color="RED">*</font></td>
    </tr>
    <tr>
        <td id="DISCOUNTNameTd" align="right">折扣率:</td>
        <td id="DISCOUNTTextTd">
        	<input readonly class="phone_txt" type="text"
                   style="border:0px;background-color:#F3F4F8;" value="${mainMap.discount}"
                   name="discount" id="discount"/>
        </td>
        <td align="right">可用金额:</td>
        <td>
        	<input readonly class="phone_txt" type="text" style="background-color:#6F9" name="accountKy"
                   value="${accountMap.ACCOUNT_KY}" id="accountKy"/>
            <font color="RED">*</font></td>
    </tr>
    <tr>
        <td align="right">销售总金额:</td>
        <td><input id="Amount" name="Amount" type="text" style="border:0px;background-color:#F3F4F8;"
                   value="${mainMap.AMOUNT}" onchange="countAll();"
                   class="normal_txt" readonly/><font color="blue">元</font>
        </td>
        <td align="right">销售金额:</td>
        <td><input readonly type="text" style="border:0px;background-color:#F3F4F8;"
                   value="0.00" name="partAmount" id="partAmount"/>
            <font color="blue">元</font></td>
        <td align="right">运费金额:</td>
        <td>
            <input id="freight" name="freight" type="text" value="${mainMap.FREIGHT}"
                   style="background-color: #ffff80;border: none" readonly/>
            <font color="blue">元</font>
        </td>
    </tr>
    <tr>
        <td align="right">备注:</td>
        <td colspan="5"><textarea id="REMARK2" name="REMARK2" cols="50" rows="3">${mainMap.REMARK2}</textarea></td>
    </tr>
</table>

<%-- <FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6"
            style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            配件查询
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
            <tr>
                <td align="right" width="13%">
                    件号
                </td>
                <td width="20%" align="left">
                    &nbsp;
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="13%">
                    配件编码
                </td>
                <td align="left" width="20%">
                    &nbsp;
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="13%">
                    配件名称
                </td>
                <td align="left" width="21%">
                    &nbsp;
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>

        </table>
        <table width="100%">
            <tr>

                <td align="center">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="添加" onclick="addCells()"/>
                </td>
            </tr>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET> --%>
<table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
        <th colspan="15" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息 
        <span class="checked" </span>
    </th>
    </tr>
    <tr bgcolor="#FFFFCC">
        <td align="center" width="2%"><input type="checkbox" id="ckAll" name="ckAll" checked onclick="checkAll(this)"/>
        </td>
        <td align="center" width="4%">序号</td>
        <td align="center" width="12%">配件编码</td>
        <td align="center" width="11%">配件名称</td>
        <td align="center" width="10%">件号</td>
        <td align="center" width="7%">最小包装量</td>
        <td width="4%" align="center">单位</td>
        <td width="7%%" align="center">当前库存</td>
        <td width="8%" align="center">销售数量
            <font color="RED">*</font></td>
        <td align="center" width="4%" nowrap="nowrap">销售单价(折扣前)</td>
        <td align="center" width="4%" nowrap="nowrap">销售单价(折扣后)</td>
        <td align="center" width="6%">销售金额</td>
        <td align="center" width="8%">备注</td>
        <td align="center" width="8%">操作</td>
    </tr>
    <tr class="table_list_row0">
        <c:forEach items="${detailList}" var="data">
    <tr class="table_list_row1">
        <td align="center">
            <script type="text/javascript">
                getCb(${data.PART_ID});
            </script>
        </td>
        <td align="center">&nbsp;
            <script type="text/javascript">
                getIdx();
            </script>
        </td>
        <td align="center">
            <c:out value="${data.PART_OLDCODE}"/>
            <INPUT id="partOldcode_${data.PART_ID}" value="${data.PART_OLDCODE}" type=hidden
                   name="partOldcode_${data.PART_ID}">
        </td>
        <td align="center">
            <c:out value="${data.PART_CNAME}"/>
            <INPUT id="partCname_${data.PART_ID}" value="${data.PART_CNAME}" type=hidden
                   name="partCname_${data.PART_ID}">
        </td>
        <td align="center">
            <c:out value="${data.PART_CODE}"/>
            <INPUT id="partCode_${data.PART_ID}" value="${data.PART_CODE}" type=hidden name="partCode_${data.PART_ID}">
        </td>
        <td>
            <c:out value="${data.MIN_PACKAGE}"/>
            <INPUT id="minPackage_${data.PART_ID}" value="${data.MIN_PACKAGE}" type=hidden
                   name="minPackage_${data.PART_ID}">
        </td>
        <td>
            <c:out value="${data.UNIT}"/>
            <INPUT id="unit_${data.PART_ID}" value="${data.UNIT}" type=hidden name="unit_${data.PART_ID}">
        </td>
        <td>
            <INPUT id="itemQty_${data.PART_ID}" value="${data.STOCK_QTY}" style="border:0px;text-align:center" type=text
                   name="itemQty_${data.PART_ID}">
        </td>

        <td>
            <INPUT id="buyQty_${data.PART_ID}" value="${data.SALES_QTY}"
                   style="background-color:#FFFFCC;text-align:center" class="short_txt" type="text"
                   onchange="countMoney(this,'${data.BUY_PRICE}','${data.PART_ID}')" name="buyQty_${data.PART_ID}">
        </td>
        <td>
            <c:out value="${data.price1}"/>
            <INPUT id="buyPrice1_${data.PART_ID}" type="hidden" value="${data.price1}" name="buyPrice1_${data.PART_ID}">
        </td>
        <td>
            <c:out value="${data.BUY_PRICE}"/>
            <INPUT id="buyPrice_${data.PART_ID}" type="hidden" value="${data.BUY_PRICE}"
                   name="buyPrice_${data.PART_ID}">
        </td>
        <td>
            <c:out value="${data.BUY_AMOUNT}"/>
            <INPUT id="buyAmount_${data.PART_ID}" type="hidden" value="${data.BUY_AMOUNT}"
                   name="buyAmount_${data.PART_ID}">
        </td>
        <td><INPUT id="remark_${data.PART_ID}" value="${data.REMARK}" type="text" class="short_txt"
                   name="remark_${data.PART_ID}"></td>
        <td><INPUT class=short_btn onclick=deleteTblRow(this); value=删除 type=button name=queryBtn>
            <INPUT id="stockQty_${data.PART_ID}" value="${data.STOCK_QTY}" type=hidden name="stockQty_${data.PART_ID}">

            <INPUT id="isPlan_${data.PART_ID}" value="${data.IS_PLAN}" type=hidden name="isPlan_${data.PART_ID}"/>
            <INPUT id="isLack_${data.PART_ID}" value="${data.IS_LACK}" type=hidden name="isLack_${data.PART_ID}"/>
            <INPUT id="stockQty_${data.PART_ID}" value="${data.STOCK_QTY}" type=hidden name="stockQty_${data.PART_ID}"/>
            <INPUT id="isDirect_${data.PART_ID}" value="${data.IS_DIRECT}" type=hidden name="isDirect_${data.PART_ID}"/>
            <INPUT id="isReplaced_${data.PART_ID}" value="${data.IS_REPLACED}" type=hidden
                   name="isReplaced_${data.PART_ID}"/>
        </td>
    </tr>
    </c:forEach>
    </tr>
</table>
<table border="0" class="table_query">
    <tr align="center">
        <td><input class="cssbutton" type="button" value="保存" name="button1" onclick="saveOrderConfirm();">
            &nbsp;
            <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
    </tr>
</table>
</div>
</form>
</body>
</html>
