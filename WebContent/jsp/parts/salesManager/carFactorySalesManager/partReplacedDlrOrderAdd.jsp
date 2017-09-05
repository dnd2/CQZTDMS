<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet"
      type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css"
      rel="stylesheet"/>
<script language="javascript"
        src="<%=request.getContextPath()%>/jsp/parts/salesManager/carFactorySalesManager/waitingTip.js"></script>
<script type="text/javascript">
jQuery.noConflict();

var myPage;

// // var sel=document.getElementById('actCodeMap');
// var sel=document.getElementsByName("actCodeMap")[0];
// // var selvalue= sel.options[sel.options.selectedIndex].value;
// MyAlert(sel);
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/showPartBase.json";

var title = null;

var columns = [

    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "当前库存数量", dataIndex: 'ITEM_QTY', style: 'text-align:center', renderer: inputText},
    {header: "切换件编码", dataIndex: 'REPART_OLDCODE', style: 'text-align:center'},
    {header: "切换件名称", dataIndex: 'REPART_NAME', style: 'text-align:left'},
    {header: "切换数量", style: 'text-align:center', renderer: getSaleText, dataIndex: 'PART_ID'},
    {header: "配件类型", dataIndex: 'PART_TYPE', renderer: getItemValue, style: 'text-align:center'},
    {header: "是否需要回运", dataIndex: 'ISNEED_FLAG', renderer: getItemValue, style: 'text-align:center'},
    {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'}
];

var wt = new WaitingTip({innerHTML: "<img src='<%=request.getContextPath()%>/jsp/parts/salesManager/carFactorySalesManager/waiting.gif' />上传中..."});

function getSaleText(value, meta, record) {
    if (${orderType} !=
    <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>)
    {
        if (record.data.SALE_PRICE1 != null && record.data.IS_SPECIAL != <%=Constant.IF_TYPE_YES%>) {//非特殊配件或无价格配件
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
        } else {
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' readonly='readonly' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt' title='此处不允许订购特殊配件或无价格配件，特殊配件请到特殊配件订单提报处提报，如有其他问题请联系供应中心或配件处!'  >";
        }
    }
else
    {
        if (record.data.SALE_PRICE1 != null) {
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
        } else {
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' readonly='readonly' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'  >";
        }
    }
}

function validateFreight(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        countAll();
        return;
    }
    if (parseFloat(obj.value) < parseFloat(0)) {
        MyAlert("请输入正数!");
        countAll();
        return;
    }
    obj.value = parseFloat(obj.value).toFixed(2);
    countAllWithoutFreight();
    $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value)) + parseFloat(unFormatNum(obj.value))).toFixed(2));
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
    if ($('SELLER_ID').value == "") {
        MyAlert("请先选择销售单位");
        return;
    }

    var uploadDiv = document.myIframe.uploadDiv;
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    }

    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}
function getDefaultValue(value, meta, record) {
    if (record.data.SALE_PRICE1 == null) {
        return "<font color='RED'>无订购单价</font>";
    } else {
        return record.data.SALE_PRICE1;
    }
}
function inputText(value, meta, record) {
    var miniPkg = record.data.MIN_PACKAGE;
    var partid = record.data.PART_ID;
    var repartid = record.data.REPART_ID;
    var partCode = record.data.PART_CODE;
    var isneedFlag = record.data.ISNEED_FLAG;
    var activityCode = record.data.ACTIVITY_CODE;
    return value + "<input type='hidden' id='min_package_' name='min_package_" + partid + "' value='" + miniPkg + "' />" +
            "<input type='hidden' id='min_package_' name='part_Code_" + partid + "' value='" + partCode + "' />" +
            "<input type='hidden' id='isneedFlag_' name='ISNEED_FLAG_" + partid + "' value='" + isneedFlag + "' />" +
            "<input type='hidden' id='ACTIVITY_CODE' name='ACTIVITY_CODE' value='" + activityCode + "' />" +
            "<input type='hidden' id='repart_id_' name='repart_id_" + partid + "' value='" + repartid + "' />";
}
function seled(value, meta, record) {
    if (${orderType} !=
    <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>)
    {
        if (record.data.SALE_PRICE1 != null && record.data.IS_SPECIAL != <%=Constant.IF_TYPE_YES%>) {//非特殊配件或无价格配件
            return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck'  />";
        } else {
            return "<input type='checkbox' disabled value='" + value + "' name='ck' id='ck' title='此处不允许订购特殊配件或无价格配件，特殊配件请到特殊配件订单提报处提报，如有其他问题请联系供应中心或配件处!' />";
        }
    }
else
    {
        if (record.data.SALE_PRICE1 != null) {
            return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck'  />";
        } else {
            return "<input type='checkbox' disabled value='" + value + "' name='ck' id='ck' title='此处不允许订购无价格配件，请联系配件处！' />";
        }
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
    countAll();
    reNumIdx();
}
function reNumIdx() {
    var tbl = document.getElementById("file");
    for (var i = 2; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerHTML = (i - 1);
    }
}

//选择经销商(1) 接收单位(2)地址(3)
function selSales(inputId, inputCode, inputName, inputLinkMan, inputTel, inputPostCode, inputStation, dealerId, type) {
    if (type == 2 && $('SELLER_ID').value == "") {
        MyAlert("请先选择销售单位!");
        return;
    }
    if (type == 3 && $('RCV_ORGID').value == "") {
        MyAlert("请先选择接收单位!");
        return;
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
    OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/selSales.jsp?dealerId=' + dealerId + '&type=' + type + '&inputName=' + inputName + '&inputId=' + inputId + '&inputCode=' + inputCode + '&inputLinkMan=' + inputLinkMan + '&inputTel=' + inputTel + '&inputPostCode=' + inputPostCode + '&inputStation=' + inputStation, 700, 400);
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
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var itemQty = mt.rows[i].cells[4].innerText;  //当前库存
                var repart_oldcode = mt.rows[i].cells[5].innerText;  //切换件编码
                var repart_cname = mt.rows[i].cells[6].innerText;  //切换件名称
                var replaced_num = mt.rows[i].cells[7].firstChild.value;  //切换数量
                var partType = mt.rows[i].cells[8].innerText;  //配件类型
                var isneed_Flag = mt.rows[i].cells[9].innerText;  //是否需要回运
                var remake = mt.rows[i].cells[10].innerText;  //备注
                addCell(partId, partOldcode, partCname, itemQty, repart_oldcode, repart_cname, replaced_num, partType, isneed_Flag, remake)

            } else {
                MyAlert("配件：" + mt.rows[i].cells[3].innerText + "  已存在!</br>");
                break;
            }
        }
    }
}

function addCell(partId, partOldcode, partCname, itemQty, repart_oldcode, repart_cname, replaced_num, partType, isneed_Flag, remake) {

    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }

    var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' >";
    createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 2)), "cb", "", "", "checkbox", partId, true, false, "");
    createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 2), false, false, "");
    createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
    createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "left", partCname, false, false, "");
    createCells(rowObj, 4, ("itemQty_" + partId), ("itemQty_" + partId), "", "", "center", itemQty, false, false, "");
    createCells(rowObj, 5, ("buyQty_" + partId), ("buyQty_" + partId), "", "text-align: center", "text", replaced_num, false, false, "");
    createCells(rowObj, 6, ("repart_oldcode_" + partId), ("repart_oldcode_" + partId), "", "", "hidden", repart_oldcode, false, false, "");
    createCells(rowObj, 7, ("repart_cname_" + partId), ("repart_cname_" + partId), "", "", "left", repart_cname, false, false, "");
    createCells(rowObj, 8, ("partType_" + partId), ("partType_" + partId), "", "", "center", partType, false, false, "");
    createCells(rowObj, 9, ("isneed_Flag_" + partId), ("isneed_Flag_" + partId), "", "", "center", isneed_Flag, false, false, "");

    createCells(rowObj, 10, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, "");
    createCells(rowObj, 11, "", "", "", "", "button", "", false, true, hiddenHtml);
}
//校验是否重复生成
function validateCell(value) {
    var flag = true;
    var tbl = document.getElementById('file');
    for (var i = 1; i <= tbl.rows.length - 1; i++) {
        if (value == tbl.rows[i].cells[0].firstChild.value) {
            flag = false;
            break;
        }
    }
    return flag;
}


//生成CELL
function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var cell = obj.insertCell(idx);
    if (type == "left") {
        cell.align = "left";
    } else if (type == "right") {
        cell.align = "right";
    } else {
        cell.align = "center";
    }

    cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);

}


//计算所有CHECK的金额
function countAll() {
    if ($("TRANS_TYPE").value == '2' && $("ORDER_TYPE").value == "<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>") {
        MyAlert("常规订单发运方式不能为快件!");
        $("TRANS_TYPE").value = '1';
        return;
    }
    var flag = true;
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算unFormatNum
            if (cb[i].checked) {
                amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);

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
//生成CELL中的HTML
//&quot;
function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {

    value = (value + "").replace(new RegExp("\"", "gm"), "&quot;");
    var inputHtml = "";
    var tdStrHead = "";
    var tdStrEnd = "";
    if (type == 'left') {
        tdStrHead = trHFlag == true ? '<tr><td align="left" nowrap>' : '<td align="left" nowrap>';
        tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';

    } else {
        tdStrHead = trHFlag == true ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
        tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';
    }
    if (type == 'button') {
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt" value="' + value + '" style="' + style + '"  id="' + id + '" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden' || type == 'left' || type == 'right' || type == 'center') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'checkbox') {
        inputHtml = '<input  type="checkbox" onclick="countAll()"  id="' + id + '" name="cb" checked="true" value="' + value + '" />';
    }
    if (type == 'idx') {
        inputHtml = value;
    }
    if (hiddenHtml != "") {
        inputHtml += hiddenHtml;
    }
    return tdStrHead + inputHtml + tdStrEnd;
}
//计算金额
function countMoney(obj, price, partId) {
    price = unFormatNum(price);
    //获取折扣率
    var discount = document.getElementById("DISCOUNT").value;
    if (discount == null || discount == "") {
        discount = 1;
    }
    var amout = 0;
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (value == '') {
        cleanData(partId, idx);
        return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        cleanData(partId, idx);
        return;
    }
    value = (parseFloat(value)).toFixed(0);
    obj.value = value;
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert(value);
        obj.value = "";
        cleanData(partId, idx);
        return;
    }
    var money = (parseFloat(unFormatNum(price)) * parseFloat(value) * discount).toFixed(2);
    tbl.rows[idx].cells[10].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
            }
        }
    }
    document.getElementById("Amount").value = formatNum(amountCount);
    document.getElementById("partAmount").value = formatNum(amountCount);
    getFreight(amountCount);
}
//清除数据重新计算
function cleanData(partId, idx) {
    var tbl = document.getElementById('file');
    tbl.rows[idx].cells[10].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
        }
    }
    document.getElementById("Amount").value = formatNum(amountCount);
}

//保存订单确认
function saveOrderConfirm() {

    MyConfirm('确定保存订单?', saveOrder, []);
    //enableAllBtn();
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
//check整整数
function check(obj) {
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj)) {
        return false;
    } else {
        return true;
    }
}
function validateFm() {
    var msg = "";
    var remark = document.getElementById("textarea").value;
    if(remark == ''){
        MyAlert("备注必填!");
        return;
    }
    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要切换数量是否填写
            if (document.getElementById("buyQty_" + cb[i].value).value == "" ||
                    parseFloat(document.getElementById("buyQty_" + cb[i].value).value) == parseFloat(0)) {
                msg += "请填写第" + (i + 1) + "行的切换数量!</br>";
                MyAlert(msg);
                return false;
            }
            if (!check(document.getElementById("buyQty_" + cb[i].value).value)) {
                msg += "第" + (i + 1) + "行的切换数量必须是正整数!</br>";
                MyAlert(msg);
                return false;
            }
//             if(parseInt(document.getElementById("buyQty_"+cb[i].value).value) > parseInt(document.getElementById("stockQty_"+cb[i].value).value)){
//             	msg += "第"+ (i + 1)+"行的切换数量必须小于当前库存数量!</br>";
//             	MyAlert(msg);
//                return false;
//             }
        } else {
            cb[i].disabled = true;
        }
    }
    if (cb.length <= 0) {
        msg += "请添加订货明细!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return false;
    }
    return true;
}
//提报并保存订单确认
function repOrderConfirm() {
    var msg = '确定提报并保存订单?';
    if (parseFloat(unFormatNum($('freight').value)) > parseFloat(0)) {
        msg = "<font color='red'>运费：" + $('freight').value + "</font></br>" + msg;
    }
    MyConfirm(msg, repOrder, []);
//    enableAllBtn();
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
//保存订单
function saveOrder() {
    disableAllBtn();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/saveOrder.json";
    sendAjax(url, getResult, 'fm');
}
//提报订单
function repOrder() {
    disableAllBtn();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/saveOrder.json?discount=discount";

    sendAjax(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllBtn();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            goBack();
        } else if (error) {
            MyAlert(error);
            $('file').style.display = "block";
            hideWait();
        } else if (exceptions) {
            MyAlert(exceptions.message);
            $('file').style.display = "block";
            hideWait();
        }
    }
}


function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        var height = 200;
        //     if (info.split('</br>').length <= 12) {
        //         height = height + (info.split('</br>').length - 6) * 27;
        //     }
        if (info.split('</br>').length > 5) {   //如果有5个就会过长
            var infoR = "";
            var infoArr = info.split('</br>');
            for (var i = 0; i < 5; i++) {
                infoR += infoArr[i] + "</br>";
            }
            height = height + 6 * 27;
            infoR += "......</br>";
            info = infoR;
        }
        owner.getElementById('dialog_content_div').innerHTML = '\
            <div style="font-size:12px;">\
             <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
              <b>信息</b>\
             </div>\
             <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
             <div style="padding:2px;text-align:center;background:#D0BFA1;">\
              <input id="dialog_alert_button" type="button" value="确定"  style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
             </div>\
            </div>';
        owner.getElementById('dialog_alert_info').innerHTML = info;
        owner.getElementById('dialog_alert_button').onclick = _hide;

        _setSize(300, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
}
//显示余额
function showAcount(accountId, accountSum, accountKy, accountDj) {
    if (accountId != "") {
        document.getElementById("accountSumNameTd").innerHTML = "账户余额：";
        document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="hidden" class="middle_txt" value="' + accountSum + '" readonly /><font color="blue">元</font>';
        document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
        document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="hidden" value="' + accountKy + '" class="middle_txt" readonly /><font color="blue">元</font>';
        document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
        document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="hidden" style="border:0px;background-color:#F3F4F8;" value="' + accountDj + '" class="middle_txt" readonly /><font color="blue">元</font>';
        document.getElementById("accountId").value = accountId;
    } else {
        document.getElementById("accountSumNameTd").innerHTML = "";
        document.getElementById("accountSumTextTd").innerHTML = "";
        document.getElementById("accountKyNameTd").innerHTML = "";
        document.getElementById("accountKyTextTd").innerHTML = "";
        document.getElementById("accountDjNameTd").innerHTML = "";
        document.getElementById("accountDjTextTd").innerHTML = "";
        document.getElementById("accountId").value = "";
    }
}
//返回
function goBack() {

    if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedEmergencyDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedPlanDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDirectDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedMarketDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDiscountDlrOrderInit.do';
        return;
    } else if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>) {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDlrOrderInit.do';
        return;
    }

}

function showUpload() {
// 	if( $('SELLER_ID').value == ""){
//         MyAlert("请先选择销售单位");
//         return;
//     }

    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");

    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    }

    var uploadDiv = document.myIframe.uploadDiv;
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/exportExcelTemplate.do";
    fm.submit();
}

function checkFormInput(handle) {
    if (validateobjarr.length == 0) {
        return true;
    }
    clearTip();
    //modified by and.ten@tom.com 解决handle参数可能是form id字符串，也可能是form对象情况 begin
    var controlList;
    if (typeof(handle) == "object") {
        controlList = handle.getElementsByTagName("INPUT");
    } else {
        var form = document.getElementById(handle);
        controlList = form.getElementsByTagName("INPUT");
    }
    //end
    var controlObj;
    var rest = true;
    var issele = null; // select标记ID=
    for (var i = 0; i < controlList.length; i++) {
        var tipid = getTip();
        if (tipid == null) {
            return false;
        }
        controlObj = controlList[i];
        if (isControlVisible(controlObj)) {
            if (controlObj.type == 'text' || controlObj.type == 'password' || controlObj.type == 'textarea' || controlObj.type == 'select-one' || controlObj.type == 'file' || controlObj.type == 'checkbox') {
                if (controlObj.id != "uploadFile1") {
                    controlObj.value = controlObj.value.trim();
                    if (controlObj.datatype != null) {
                        if (controlObj.type == 'checkbox' && controlObj.checked == false) {
                            showTip(controlObj, "此处不能为空.", tipid);
                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                            rest = false;
                            continue;
                        } else if (controlObj.type == 'checkbox' && controlObj.checked == true) {
                            rest = true;
                            continue;
                        }

                        tmptypeStr = controlObj.datatype;
                        str = tmptypeStr.split(",");
                        nullAble = str[0];
                        typeStr = str[1];
                        maxLength = str[2];
                        errm = controlObj.errmsg;

                        minLength = "0";
                        if (str.length == 4) {
                            minLength = str[3];
                        }

                        if (controlObj.value == null || controlObj.value == "") {
                            if (nullAble == "0") {
                                hideTip(tipid);
                                if (controlObj.hint == null || controlObj.hint == "") {
                                    if (controlObj.type == 'select-one' && getIEVer() <= 6) {
                                        showTip(controlObj, "此处不能为空.CL30", tipid);
                                    } else {
                                        showTip(controlObj, "此处不能为空.", tipid);
                                    }
                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                    rest = false;
                                    continue;
                                }
                            }
                        } else {
                            if (typeStr == "is_yuan" || typeStr == "is_wan") {
                                maxLength = 1000;
                            }
                            if (typeStr == "is_phone") {
                                maxLength = 15;
                            }

                            if (typeStr == "is_date") {
                                if (controlObj.group) {
                                    var gr = controlObj.group;
                                    var sdd = gr.split(",");
                                    var startd = document.getElementById(sdd[0]).value;
                                    var endd = document.getElementById(sdd[1]).value;
                                    if (startd != null && startd != "" && endd != null && endd != "") {
                                        if (!checkDate(startd, endd)) {
                                            hideTip(tipid);
                                            showTip(document.getElementById(sdd[0]), "开始时间不能大于结束时间.", tipid);
                                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                            rest = false;
                                            continue;
                                        }
                                    }
                                }
                            }
                            //wjb add by 2010-07-12 验证日期不能大于当前日期 begin
                            if (typeStr == "is_date_now") {
                                var valDate = controlObj.value;
                                var nowDate = (new Date()).Format("yyyy-MM-dd");
                                if (valDate != null && valDate != "" && nowDate != null && nowDate != "") {
                                    if (!checkDate(valDate, nowDate)) {
                                        hideTip(tipid);
                                        showTip(controlObj, "不能大于当前日期.", tipid);
                                        issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                        rest = false;
                                        continue;
                                    }
                                }
                            }
                            //wjb add by 2010-07-12 end

                            if (typeStr == "is_textarea") {
                                if (controlObj.value.length > maxLength) {
                                    hideTip(tipid);
                                    showTip(controlObj, "不能超过" + maxLength + "个字符.", tipid);
                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                    rest = false;
                                    continue;
                                }
                            }

                            if (typeStr == "is_double") {
                                if (controlObj.decimal) {
                                    var tempMsg = eval("" + typeStr + "(controlObj);");
                                    if (tempMsg == true) {
                                        var tt2 = checkDecimal(controlObj.value, controlObj.decimal);
                                        if (tt2 != true) {
                                            hideTip(tipid);
                                            showTip(controlObj, "" + tt2, tipid);
                                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                            rest = false;
                                            continue;
                                        }
                                    } else {
                                        hideTip(tipid);
                                        showTip(controlObj, "" + tempMsg, tipid);
                                        issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                        rest = false;
                                        continue;
                                    }
                                }
                            }

                            if (controlObj.value.length < minLength) {
                                hideTip(tipid);
                                showTip(controlObj, "不能少于" + minLength + "个字符.", tipid);
                                issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                rest = false;
                                continue;
                            } else if (controlObj.value.length > maxLength) {
                                hideTip(tipid);
                                showTip(controlObj, "不能超过" + maxLength + "个字符.", tipid);
                                issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                rest = false;
                                continue;
                            } else {
                                var tempMsg = eval("" + typeStr + "(controlObj);");
                                if (tempMsg != true) {
                                    hideTip(tipid);
                                    if (errm != null && errm != "") {
                                        showTip(controlObj, errm, tipid);
                                    } else {
                                        showTip(controlObj, "" + tempMsg, tipid);
                                    }
                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                    rest = false;
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return rest;
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
function selected() {
    var tb = document.getElementById('file');
    var rowNum = tb.rows.length;
    for (i = 1; i < rowNum; i++) {
        if (i > 1) {
            tb.deleteRow(i);
            rowNum = rowNum - 1;
            i = i - 1;
        }
    }
    __extQuery__(1);
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

function genMySelBoxExp(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    str += "  ";
    if (setAll) {
        str += genDefaultOpt();
    }
    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j]) {
                flag = false;
            }
        }

        //过滤
        if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%> || codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>) {
            if (codeData[i].type == type && flag) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }
    }
    str += "</select>";
    document.write(str);
}

function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
    countAll();
}
function changeOrderType(obj) {
    //如果直发
    if (obj.value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
        //如果上级机构不是车厂   就是供应中心    如果上级机构是供应中心  转换
        if (<%=Constant.OEM_ACTIVITIES%>!=
        "${dataMap.parentId}"
    )
        {
            directTransport();
        }
    } else {
        if (<%=Constant.OEM_ACTIVITIES%>!=
        "${dataMap.parentId}"
    )
        {
            //temp如果为直发  如果是直发   还原数据  如果不是   不处理
            if (document.getElementById("temp").value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                document.getElementById("dlbtn1").disabled = false;
                document.getElementById("SELLER_ID").value = "";
                document.getElementById("SELLER_CODE").value = "";
                document.getElementById("SELLER_NAME").value = "";
                document.getElementById("dealerId").value = "${dataMap.dealerId}";
                document.getElementById("dealerCode").value = "${dataMap.dealerCode}";
                document.getElementById("dealerName").value = "${dataMap.dealerName}";
                document.getElementById("RCV_ORG").value = "";
                document.getElementById("RCV_CODE").value = "";
                document.getElementById("RCV_ORGID").value = "";
                document.getElementById("dlbtn2").disabled = false;
                document.getElementById("accountSumNameTd").innerHTML = "";
                document.getElementById("accountSumTextTd").innerHTML = "";
                document.getElementById("accountKyNameTd").innerHTML = "";
                document.getElementById("accountKyTextTd").innerHTML = "";
                document.getElementById("accountDjNameTd").innerHTML = "";
                document.getElementById("accountDjTextTd").innerHTML = "";
                document.getElementById("accountId").value = "";
                changeSeller();
            }
        }
    }
    document.getElementById("temp").value = obj.value;
}
function directTransport() {
    //将销售单位设置为车厂 并设置为不可修改
    document.getElementById("SELLER_ID").value = "${dataMap.oemId}";
    document.getElementById("SELLER_CODE").value = "${dataMap.oemCode}";
    document.getElementById("SELLER_NAME").value = "${dataMap.oemName}";
    document.getElementById("dlbtn1").disabled = true;
    //将订购单位改为 供应中心
    document.getElementById("dealerId").value = "${dataMap.parentId}";
    document.getElementById("dealerCode").value = "${dataMap.parentCode}";
    document.getElementById("dealerName").value = "${dataMap.parentName}";

    //接收单位改为自己 并设置为不可修改
    document.getElementById("RCV_ORG").value = "${dataMap.dealerName}";
    document.getElementById("RCV_CODE").value = "${dataMap.dealerCode}";
    document.getElementById("RCV_ORGID").value = "${dataMap.dealerId}";
    document.getElementById("dlbtn2").disabled = true;
    changeSeller();

    //显示供应中心在车厂的账户
    document.getElementById("accountSumNameTd").innerHTML = "账户余额：";
    document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="middle_txt" value="' + ${acountMap.ACCOUNT_SUM} +'" readonly /><font color="blue">元</font>';
    document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
    document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="' + ${acountMap.ACCOUNT_KY} +'" class="middle_txt" readonly /><font color="blue">元</font>';
    document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
    document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="text" style="border:0px;background-color:#F3F4F8;" value="' + ${acountMap.ACCOUNT_DJ} +'" class="middle_txt" readonly /><font color="blue">元</font>';
    document.getElementById("accountId").value = "${acountMap.ACCOUNT_ID}";

}
function changeSeller() {
    if (document.getElementById("sellerTemp").value != document.getElementById("SELLER_ID").value) {
        var tbl = document.getElementById("file");
        for (var i = (tbl.rows.length - 1); i >= 2; i--) {
            tbl.deleteRow(i);
            //var idx = tbl.rows[i].rowIndex;
        }
    }
    document.getElementById("sellerTemp").value = document.getElementById("SELLER_ID").value;
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");

    if (partDiv.style.display == "block") {
        if ($('SELLER_ID').value == "") {
            MyAlert("请先选择销售单位");
            addPartViv.value = "增加";
            partDiv.style.display = "none";
            return;
        }
        __extQuery__(1);
    }
}
function doInit() {
    $('TRANS_TYPE').value = 1;
    if ($('ORDER_TYPE').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>) {
        $('TRANS_TYPE').value = 2;
    }

    //初始化默认值
    var accountIdInit = $('accountIdInit').value;
    var accountSumInit = $('accountSumInit').value;
    var accountKyInit = $('accountKyInit').value;
    var accountDjInit = $('accountDjInit').value;

    showAcount(accountIdInit, accountSumInit, accountKyInit, accountDjInit);

}
function initDivAndPartInfo() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    addPartViv.value = "增加";
    partDiv.style.display = "none";
    var tbl = document.getElementById('file');
    //需要重置下面配件信息
    for (var i = (tbl.rows.length - 1); i >= 2; i--) {
        tbl.deleteRow(i);
    }
    countAll();
}
function changeOrderType(value) {
    //if(value==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>||value==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>){
    initDivAndPartInfo();
    document.getElementById("Amount").value = "0";
    //}
}

function partReplaceValidate(code) {
//     countAll();
    if (!validateFm()) {
        return;
    }
    if (code == "save") {
        saveOrderConfirm();
    }
    if (code == "rep") {
        repOrderConfirm();
    }
    return;
}

function getFreight(amountCount) {
    disableAllBtn();
    if ($('SELLER_ID').value !=<%=Constant.OEM_ACTIVITIES%>) {
        enableAllBtn();
        $('freight').value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if ($('TRANS_TYPE').value == "3") {
        enableAllBtn();
        $('freight').value = "0.00";
        countAllWithoutFreight();
        return;
    }
    var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
    sendAjax(getFreightUrl, getFreightResult, 'fm');
}
function getFreightResult(jsonObj) {
    enableAllBtn();
    if (jsonObj != null) {
        $('freight').value = jsonObj.freight;
        $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value)) + parseFloat(unFormatNum(jsonObj.freight))).toFixed(2));
    }
}
function changeProduceFac() {
    var tbl = document.getElementById('file');
    var partDiv = document.getElementById("partDiv");
    if (partDiv.style.display == "block") {
        //需要重置下面配件信息
        __extQuery__(1);
    }
    for (var i = (tbl.rows.length - 1); i >= 2; i--) {
        tbl.deleteRow(i);
    }
    countAll();
}
function changeTrans() {
    var tbl = document.getElementById("file");
    var partDiv = $("partDiv");
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
            //只有CHECKED的才能计算unFormatNum
            if (cb[i].checked) {
                amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
            }
        }
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
    document.getElementById("Amount").value = formatNum(amountCount);
}
var saveFlag = false;
function getUploadData(flag) {
    saveFlag = flag;
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/getUploadExcel.do";
    fm.submit();
}

function getUploadResult(jsonObj) {
    var data = jsonObj.excelData;
    enableAllBtn();
    if (saveFlag) {
        $('file').style.display = "none";
    }

    for (var i = 0; i < data.length; i++) {
        var tbl = document.getElementById('file');
        var flag = true;
        for (var j = 0; j < tbl.rows.length; j++) {
            if (data[i].PART_ID == tbl.rows[j].cells[0].firstChild.value) {
                error += "第" + (j) + "行配件：" + data[i].PART_CNAME + " 已存在!</br>";
                flag = false;
                break;
            }

        }
        if (flag) {
            addCell(data[i].PART_ID, data[i].PART_OLDCODE, data[i].PART_CNAME, data[i].ITEMQTY, data[i].REPART_OLDCODE, data[i].REPART_NAME, data[i].PART_CNAME, data[i].replaced_num == null ? "" : data[i].ITEM_QTY, data[i].BUYQTY == null ? "0" : data[i].BUYQTY, data[i].REMARK);
            if (data[i].BUYQTY != "") {
                document.getElementById("buyQty_" + data[i].PART_ID).value = parseFloat(data[i].BUYQTY).toFixed(0);
                countAllWithoutFreight(document.getElementById("buyQty_" + data[i].PART_ID), data[i].SALE_PRICE1, data[i].PART_ID);
            }
        }
    }
    if (data.length > 0) {
        countAll();
    }
    if (saveFlag) {
        saveOrder();
    }
}

function loadWait() {
    var div1El = document.getElementById("div1");
    wt.show(div1El, "center");
}

function hideWait() {
    wt.hide();
}

//设置配件的属性
jQuery(function () {
    //不是计划类型的订单 需要将两个都remove掉
    if (jQuery('#ORDER_TYPE').val() !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>) {
        jQuery('.table_add tr')[1].style.display = 'none';
    } else {
        jQuery('#isSpecialOrPlan').on("change", function () {
            if (jQuery('#SELLER_ID').val() != "" && jQuery('#partDiv').css('display') == "block") {
                __extQuery__(1);
            }
            if (jQuery('#file tr').length > 2) {
                for (var i = 0; i <= jQuery('#file tr').length - 2; i++) {
                    jQuery('#file tr')[2].remove();
                }
            }
            countAll();
        })
    }
})

</script>
</head>

<body
        onload="loadcalendar();autoAlertException();enableAllBtn();doInit();countAll();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="dealerCode" id="dealerCode"
       value="${dataMap.dealerCode}"/> <input type="hidden" name="dealerId"
                                              id="dealerId" value="${dataMap.dealerId}"/> <input type="hidden"
                                                                                                 name="accountId"
                                                                                                 id="accountId"/> <input
        type="hidden" name="temp"
        id="temp"/> <input type="hidden" name="sellerTemp" id="sellerTemp"/>
<input type="hidden" name="state" id="state"> <input
        type="hidden" name="key" id="key" value="0"> <input
        type="hidden" name="seq" id="seq" value=""> <input
        type="hidden" name="ORDER_TYPE" id="ORDER_TYPE" value="${orderType}">
<input type="hidden" name="accountIdInit" id="accountIdInit"
       value="${dataMap.accountId}"> <input type="hidden"
                                            name="accountSumInit" id="accountSumInit"
                                            value="${dataMap.accountSum}"> <input type="hidden"
                                                                                  name="accountKyInit"
                                                                                  id="accountKyInit"
                                                                                  value="${dataMap.accountKy}">
<input type="hidden" name="accountDjInit" id="accountDjInit"
       value="${dataMap.accountDj}">

<div id="div1" class="wbox">
<div class="navigation">
    <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;配件采购管理&gt;切换订单提报&gt;新增
</div>
<table class="table_add" border="0"
       style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP: #859aff 1px solid; BORDER-LEFT: #859aff 1px solid; BORDER-BOTTOM: #859aff 1px solid; border-color: #859aff;"
       cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="6" width="100%"><img class="nav"
                                          src="<%=contextPath%>/img/subNav.gif"/> 采购订单信息
        </th>
    </tr>
    <tr>
        <td align="right" nowrap style="display: none">配件类型：</td>
        <td style="display: none"><select id="isSpecialOrPlan"
                                          name="isSpecialOrPlan" style="width: 150px">
            <option selected value="1">特殊配件</option>
            <option value="2">计划配件</option>
        </select></td>
        <td colspan="4" align="right" nowrap></td>
    </tr>
    <tr style="display: none">
        <td align="right" nowrap>销售单位：</td>
        <td><input name="SELLER_NAME" class="middle_txt"
                   id="SELLER_NAME" type="text" value="${dataMap.SELLER_NAME}"
                   size="20" readonly="true"/>
            <input name="SELLER_CODE"
                   class="middle_txt" id="SELLER_CODE" type="hidden"
                   value="${dataMap.SELLER_CODE}" readonly="true"/>
            <input
                    name="SELLER_ID" class="middle_txt" id="SELLER_ID" type="hidden"
                    value="${dataMap.SELLER_ID}" readonly="true"/> <input
                    name='dlbtn1' id='dlbtn1' class='mini_btn' type='button'
                    value='...'
                    onclick="selSales('SELLER_ID','SELLER_CODE','SELLER_NAME','','','','',${dataMap.dealerId},'1')"/>
        </td>
        <td align="right" nowrap>付款方式：</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, "${dataMap.PAY_TYPE}", true, "short_sel", "", "false", '');
            </script>
        </td>
        <td align="right">配件品牌：</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("produceFac", <%=Constant.YIELDLY%>, "${dataMap.produceFac}", false, "short_sel", "onchange='changeProduceFac()'", "false", '');
            </script>
        </td>
    </tr>
    <tr>
        <td align="right">订货单位：</td>
        <td>${dataMap.dealerName}<input type="hidden"
                   value="${dataMap.dealerName}" name="dealerName" id="dealerName"/></td>
        <td align="right">订货人：</td>
        <td><input readonly type="hidden" value="${dataMap.buyerName}"
                   name="buyerName" id="buyerName"/>${dataMap.buyerName}</td>
        <td align="right">订货日期：</td>
        <td><input readonly type="text" value="${dataMap.now}"
                   name="now" id="now" style="border: none;background-color: #F3F4F8"/></td>
    </tr>
    <tr>
        <td align="right">接收单位：</td>
        <td><input name="RCV_ORG" class="long_txt" id="RCV_ORG"
                   type="text" value="${defaultValueMap.defaultRcvOrg}" size="20"
                   readonly="true" style="border: none;background-color: #F3F4F8"/>
            <input name="RCV_CODE"
                   class="middle_txt"
                   id="RCV_CODE" type="hidden"
                   size="20" readonly="true"/>
            <input
                    name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden"
                    value="${defaultValueMap.defaultRcvOrgid}" size="20"
                    readonly="true"/> <%--<input name='dlbtn2' id='dlbtn2'
						class='mini_btn' type='button' value='...'
						onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${dataMap.dealerId},'2')" />--%>
        </td>
        <td align="right" style="display: none">发运方式：</td>
        <td style="display: none"><select name="TRANS_TYPE"
                                          id="TRANS_TYPE" class="short_sel" onchange="countAll();">
            <%--<option value="">-请选择-</option>--%>
            <c:if test="${transList!=null}">
                <c:forEach items="${transList}" var="list">
                    <option value="${list.fixValue }">${list.fixName }</option>
                </c:forEach>
            </c:if>
        </select></td>
        <td align="right">接收地址：</td>
        <td colspan="3"><input name="ADDR" class="maxlong_txt"
                               id="ADDR" type="text" size="20"
                               value="${defaultValueMap.defaultAddr}" readonly="readonly"
                               style="border: none;background-color: #F3F4F8"/> <input
                name="ADDR_ID" class="middle_txt" id="ADDR_ID" type="hidden"
                size="20" value="${defaultValueMap.defaultAddrId}"
                readonly="readonly"/> <%--<input name='dlbtn3' id='dlbtn3'
						class='mini_btn' type='button' value='...'
						onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('RCV_ORGID').value,'3')" />--%>
        </td>
    </tr>
    <tr>

        <td align="right" style="display: none">到站名称：</td>
        <td style="display: none"><input id="STATION" name="STATION" type="text"
                                         class="no_border_txt" value="${defaultValueMap.defaultStation}"
                                         readonly style="border: none;background-color: #F3F4F8"/></td>
    </tr>
    <tr>
        <td align="right">接收人：</td>
        <td><input id="RECEIVER" name="RECEIVER" readonly type="text"
                   value="${defaultValueMap.defaultLinkman}" style="border: none;background-color: #F3F4F8"/>
        </td>
        <td align="right"><span align="right">接收人电话：</span></td>
        <td><input id="TEL" name="TEL" type="text"
                   style="border: none;background-color: #F3F4F8" value="${defaultValueMap.defaultTel}"
                   readonly/></td>
        <td align="right" style="display: none">邮政编码：</td>
        <td style="display: none"><input id="POST_CODE" name="POST_CODE" type="text"
                                         style="border: none;background-color: #F3F4F8"
                                         value="${defaultValueMap.defaultPostCode}"
                                         readonly/></td>
    </tr>
    <tr id="tr2" style="display: none">
        <td id="accountSumNameTd" align="right"><c:if
                test="${defaultValueMap.flag=='upload'}">
            账户余额：
        </c:if></td>
        <td id="accountSumTextTd"><c:if
                test="${defaultValueMap.flag=='upload'}">
            <input id="accountSum"
                   style="border: 0px; background-color: #F3F4F8;"
                   name="accountSum" type="text" class="middle_txt"
                   value="${defaultValueMap.accountSum}" readonly/>
            <font color="blue">元</font>
        </c:if></td>
        <td id="accountKyNameTd" align="right"><c:if
                test="${defaultValueMap.flag=='upload'}">
            可用金额：
        </c:if></td>
        <td id="accountKyTextTd"><c:if
                test="${defaultValueMap.flag=='upload'}">
            <input id="accountKy"
                   style="border: 0px; background-color: #F3F4F8;" name="accountKy"
                   type="text" class="middle_txt"
                   value="${defaultValueMap.accountKy}" readonly/>
            <font color="blue">元</font>
        </c:if></td>
        <td id="accountDjNameTd" align="right"><c:if
                test="${defaultValueMap.flag=='upload'}">
            冻结金额：：
        </c:if></td>
        <td id="accountDjTextTd"><c:if
                test="${defaultValueMap.flag=='upload'}">
            <input id="accountDj"
                   style="border: 0px; background-color: #F3F4F8;" name="accountDj"
                   type="text" class="middle_txt"
                   value="${defaultValueMap.accountDj}" readonly/>
            <font color="blue">元</font>
        </c:if></td>
        <td id="AmountNameTd" align="right"></td>
        <td id="AmountTextTd"></td>
    </tr>
    <tr id="tr3" style="display: none">

        <td align="right" valign="middle">订单总金额：</td>
        <td valign="middle"><input readonly type="text" value="0.00"
                                   no_border_txt name="Amount" id="Amount"
                                   style="background-color: #ffff80; border: none"/><font
                color="blue">元</font></td>
        <td align="right"><span align="right">订单金额：</span></td>
        <td><input id="partAmount" name="partAmount" type="text"
                   value="0.00" readonly
                   style="background-color: #ffff80; border: none"/> <font
                color="blue">元</font></td>
        <td align="right"><span align="right">运费金额：</span></td>
        <td><input id="freight" name="freight" type="text"
                   value="0.00" readonly
                   style="background-color: #ffff80; border: none"/> <font
                color="blue">元</font></td>
    </tr>
    <tr style="display: none">
        <td id="DISCOUNTNameTd" align="right">折扣率：</td>
        <td id="DISCOUNTTextTd"><input readonly type="text"
                                       class="no_border_txt" value="${dataMap.discount}" name="DISCOUNT"
                                       id="DISCOUNT"/></td>
        <td class="checked" colspan="5">提示：订单总金额=订单金额+运费金额</td>
    </tr>
    <tr>
        <td width="10%" align="right">仓库：</td>
        <td colspan="5">
            <select name="whId" id="whId" class="short_sel" onchange="stockSearch()">
                <%-- <option value="">-请选择-</option>--%>
                <c:if test="${wareHouseList!=null}">
                    <c:forEach items="${wareHouseList}" var="wareHouseList">
                        <option value="${wareHouseList.WH_ID }">${wareHouseList.WH_NAME }</option>
                    </c:forEach>
                </c:if>
            </select>
        </td>
    </tr>
    <tr>
        <td align="right">备注：</td>
        <td colspan="5"><textarea name="textarea" id="textarea" cols="80" rows="4"
                                  value="${dataMap.textarea}" class="middle_txt"></textarea>
            <font style="color: red">*</font>
        </td>
    </tr>
</table>
<table id="file" class="table_list" border="1"
       style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP: #859aff 1px solid; BORDER-LEFT: #859aff 1px solid; BORDER-BOTTOM: #859aff 1px solid; border-color: #859aff;"
       cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="15" align="left">
            <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息
            <span class="checked" tyle="text-align: left"> </span>
        </th>
    </tr>


    <tr bgcolor="#FFFFCC">
        <td><input type="checkbox" id="ckAll" name="ckAll" onclick="checkAll(this)"/></td>
        <td>序号</td>
        <td>配件编码</td>
        <td>配件名称</td>
        <td>当前库存数量</td>
        <td>切换数量</td>
        <td>切换件编码</td>
        <td>切换件名称</td>
        <td>配件类型</td>
        <td>是否需要回运</td>
        <td>备注</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${detailList}" var="data">
        <tr>
            <td>
                <script type="text/javascript">
                    document.write('<input  type="checkbox" onclick="countAll()"  id="cell_' + (document.getElementById('file').rows.length - 2) + '"  name="cb"  checked="true" value="'+${data.PART_ID}+'" />');
                </script>
            </td>
            <td>
                <script type="text/javascript">
                    document.write((document.getElementById('file').rows.length - 2));
                </script>
            </td>
            <td nowrap>${data.PART_OLDCODE}</td>
            <input type="hidden" id="partOldcode_${data.PART_ID}"
                   name="partOldcode_${data.PART_ID}" value="${data.PART_OLDCODE}"/>
            <td text-align:left
            ;>${data.PART_CNAME}<input type="hidden"
                                       id="partCname_${data.PART_ID}" name="partCname_${data.PART_ID}"
                                       value="${data.PART_CNAME}"/>
            </td>
            <td>${data.ITEM_QTY}<input type="hidden"
                                       id="item_qty_${data.PART_ID}" name="item_qty_${data.PART_ID}"
                                       value="${data.ITEM_QTY}"/>
                <input type="hidden"
                       id="repart_id_${data.PART_ID}" name="repart_id_${data.PART_ID}"
                       value="${data.REPART_ID}"/>
            </td>

            <td><input type="text" class="short_txt"
                       value="${data.BUYQTY}"
                       style="background-color: #FFFFCC; text-align: center"
                       id="buyQty_${data.PART_ID}" name="buyQty_${data.PART_ID}"
                       onchange="countMoney(this,${data.SALE_PRICE1},${data.PART_ID})"/>

            </td>
            <td>${data.REPART_OLDCODE}<input type="hidden"
                                             class="short_txt" value="${data.REPART_OLDCODE}" style=""
                                             id="repart_oldcode_${data.PART_ID}"
                                             name="repart_oldcode_${data.PART_ID}"/>
            </td>
            <td>${data.REPART_NAME}<input type="hidden"
                                          class="short_txt" value="${data.REPART_NAME}" style=""
                                          id="repart_cname_${data.PART_ID}"
                                          name="repart_cname_${data.PART_ID}"/>

            </td>
            <input type="hidden" id="buyAmount_${data.PART_ID}"
                   name="buyAmount_${data.PART_ID}" value="${data.BUY_AMOUNT}"/>

            <input type="hidden" id="upOrgStock_${data.PART_ID}"
                   name="upOrgStock_${data.PART_ID}" value="${data.UPORGSTOCK}"/>
            <input type='hidden' name='isReplaced_${data.PART_ID}'
                   id='isReplaced_${data.PART_ID}' value='${data.IS_REPLACED}'/>
            <td><input type="text" class="short_txt"
                       value="${data.REMARK}" style="" id="remark_${data.PART_ID}"
                       name="remark_${data.PART_ID}"/><input type='hidden'
                                                             name='isLack_${data.PART_ID}' id='isLack_${data.PART_ID}'
                                                             value='${data.IS_LACK}'></td>
            <td><input type="button" class="short_btn" name="queryBtn"
                       value="删除" onclick="deleteTblRow(this);"/><input type='hidden'
                                                                        name='stockQty_${data.PART_ID}'
                                                                        id='stockQty_${data.PART_ID}'
                                                                        value='${data.ITEM_QTY}'></td>

        </tr>
    </c:forEach>
</table>

<table border="0" class="table_query">
    <tr align="center">
        <td>
            <!-- 					<input class="cssbutton" type="button" value="上传文件" -->
            <!-- 						name="button1" onclick="showUpload();"> -->
            <!-- 						 &nbsp;  -->
            <input
                    class="cssbutton" type="button" value="保存" name="button1"
                    onclick="partReplaceValidate('save');"> &nbsp; <input
                class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
            &nbsp; <input class="normal_btn" type="button" value="提 报"
                          onclick="partReplaceValidate('rep');"/></td>
    </tr>
</table>
<FIELDSET>
    <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6"
            style="background-color: #DAE0EE; font-weight: normal; color: #416C9B; border-color: #859aff; padding: 2px; line-height: 1.5em; border: 1px solid #E7E7E7;">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            配件查询 <input type="button" class="normal_btn" name="addPartViv"
                        id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
            <tr>
                <td width="10%" align="right">活动编码：</td>
                <td>
                    &nbsp;&nbsp;<select name="actCodeMap" id="actCodeMap" class="short_sel" onchange="selected()">
                    <c:if test="${actCodeMap!=null}">
                        <c:forEach items="${actCodeMap}" var="actCodeMap">
                            <option value="${actCodeMap.ACTIVITY_CODE }">${actCodeMap.ACTIVITY_CODE }</option>
                        </c:forEach>
                    </c:if>
                </select>
                </td>

                <td align="right" width="13%">活动描述：</td>
                <td width="20%" align="left">&nbsp; <input
                        class="middle_txt" id="DESCRIBE" datatype="1,is_noquotation,30"
                        name="DESCRIBE" onblur="isCloseDealerTreeDiv(event,this,'pan')"
                        type="text"/>
                </td>
                <!-- 
                <td align="right" width="13%">件号：</td>
                <td width="20%" align="left">&nbsp; <input
                        class="middle_txt" id="PART_CODE" datatype="1,is_noquotation,30"
                        name="PART_CODE" onblur="isCloseDealerTreeDiv(event,this,'pan')"
                        type="text"/>
                </td>
                 -->
            </tr>
            <tr>
                <td align="right" width="13%">配件编码：</td>
                <td align="left" width="20%">&nbsp; <input
                        class="middle_txt" id="PART_OLDCODE"
                        datatype="1,is_noquotation,30" name="PART_OLDCODE"
                        onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="13%">配件名称：</td>
                <td align="left" width="20%">&nbsp; <input
                        class="middle_txt" id="PART_CNAME"
                        datatype="1,is_noquotation,30" name="PART_CNAME"
                        onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>

            </tr>

        </table>
        <table width="100%">
            <tr>

                <td align="center"><input class="normal_btn" align="right"
                                          type="button" name="queryBtn" id="queryBtn" value="查 询"
                                          onclick="__extQuery__(1)"/>&nbsp;&nbsp;&nbsp;&nbsp; <input
                        class="normal_btn" type="button" name="BtnQuery2" id="BtnQuery2"
                        value="添加" onclick="addCells()"/></td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
<iframe frameborder="0" name="myIframe" id="myIframe"
        src="<%=request.getContextPath()%>/jsp/parts/salesManager/carFactorySalesManager/ReplacedUploadFile.jsp"
        height="100%" width="100%" scrolling="auto" align="middle">

</iframe>
</div>
</form>
</body>
<script type="text/javascript">
    document.getElementById("PAY_TYPE").value =<%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>;
</script>
</html>
