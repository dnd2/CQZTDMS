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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>销售采购订单提报-修改</title>
<script type="text/javascript">
 var myPage;
 var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/showPartBase.json";
 var title = null;
 var columns = [
     {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
     {
         header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />",
         dataIndex: 'PART_ID',
         align: 'center',
         width: '33px',
         renderer: seled
     },
     {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
     {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
     {header: "单位", dataIndex: 'UNIT', align: 'center'},
     {header: "采购数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'},
     {header: "服务商库存", dataIndex: 'ITEM_QTY', align: 'center'},
     {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
     {header: "订购单价", dataIndex: 'SALE_PRICE1', align: 'center', renderer: getDefaultValue},
     {header: "上级库存量", dataIndex: 'UPORGSTOCK', align: 'center'},
     {header: "是否紧缺", dataIndex: 'IS_LACK', align: 'center', renderer: inputText},
     {header: "是否可替代", dataIndex: 'IS_REPLACED', align: 'center', renderer: inputText}

 ];

function getDefaultValue(value, meta, record) {
    if (record.data.SALE_PRICE1 == null) {
        return "<font color='RED'>无订购单价</font>";
    } else {
        return record.data.SALE_PRICE1;
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
    $('#Amount')[0].value = formatNum((parseFloat(unFormatNum($('#Amount')[0].value)) + parseFloat(unFormatNum(obj.value))).toFixed(2));
}
function getSaleText(value, meta, record) {
    if (record.data.SALE_PRICE1 != null && record.data.IS_SPECIAL != <%=Constant.IF_TYPE_YES%>) {//非特殊配件或无价格配件
        return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
    } else {
        return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' readonly='readonly' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt' title='此处不允许订购特殊配件或无价格配件，特殊配件请到特殊配件订单提报处提报，如有其他问题请联系供应中心或配件处!'  >";
    }
}
function onchangeVlidateSaleQty(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "0";
        return;
    }
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
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
    if ($('#SELLER_ID')[0].value == "") {
        MyAlert("请先选择销售单位");
        return;
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
function inputText(value, meta, record) {
    var info = getItemValue(value);
    return "<input type='hidden' value='" + value + "' />" + info;
}
function seled(value, meta, record) {
    if (${mainMap.ORDER_TYPE} != <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>){
        if (record.data.SALE_PRICE1 != null && record.data.IS_SPECIAL != <%=Constant.IF_TYPE_YES%>) {//非特殊配件或无价格配件
            return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck'  />";
        } else {
            return "<input type='checkbox' disabled value='" + value + "' name='ck' id='ck' title='此处不允许订购特殊配件或无价格配件，特殊配件请到特殊配件订单提报处提报，如有其他问题请联系供应中心或配件处!' />";
        }
    }else{
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
        tbl.rows[i].cells[1].innerHTML = i - 1;
    }
}
//选择经销商(1) 接收单位(2)地址(3)
function selSales(inputId, inputCode, inputName, inputLinkMan, inputTel, inputPostCode, inputStation, dealerId, type) {
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
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var unit = mt.rows[i].cells[4].innerText;  //单位
                var saleQty = mt.rows[i].cells[5].firstChild.value;  //配件数量
                var itemQty = mt.rows[i].cells[6].innerText;  //当前库存
                var minPackage = mt.rows[i].cells[7].innerText;  //最小包装量
                var buyPrice = mt.rows[i].cells[8].innerText;  //订购单价
                var upOrgStock = mt.rows[i].cells[9].innerText;  //上级库存量
                var isLack = mt.rows[i].cells[10].firstChild.value;  //是否紧缺
                var isReplaced = mt.rows[i].cells[11].firstChild.value;  //是否可替代
                addCell(partId, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, saleQty)
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    countAll();
}

function addCell(partId, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, saleQty) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }

    if (upOrgStock == "Y") {
        upOrgStock = "Y";
    } else {
        upOrgStock = "N";
    }
    //获取折扣率
    var discount = document.getElementById("DISCOUNT").value;


    var buyPrice1 = (parseFloat(unFormatNum(buyPrice)) * parseFloat(discount)).toFixed(2);

    var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' >";
    var hiddenHtmlIsLack = "<input type='hidden' name='isLack_" + partId + "' id='isLack_" + partId + "' value='" + isLack + "' >";
    var hiddenHtmlIsReplaced = "<input type='hidden' name='isReplaced_" + partId + "' id='isReplaced_" + partId + "' value='" + isReplaced + "' >";
    createCells(rowObj, 0, "cb", "cb", "", "", "checkbox", partId, true, false, "");
    createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 1), false, false, "");
    createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
    createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
    //createCells(rowObj, 4, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
    createCells(rowObj, 4, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, "");
    createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
    createCells(rowObj, 6, ("buyQty_" + partId), ("buyQty_" + partId), "countMoney(this,'" + buyPrice + "'," + partId + ")", "background-color:#FFFFCC;text-align:center", "text", saleQty, false, false, "");
    createCells(rowObj, 7, ("buyPrice_" + partId), ("buyPrice_" + partId), "", "", "hidden", buyPrice, false, false, "");
    createCells(rowObj, 8, ("buyPrice1_" + partId), ("buyPrice1_" + partId), "", "", "hidden", buyPrice1, false, false, "");
    createCells(rowObj, 9, ("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
    createCells(rowObj, 10, ("upOrgStock_" + partId), ("upOrgStock_" + partId), "", "", "hidden", upOrgStock, false, false, hiddenHtmlIsReplaced);
    createCells(rowObj, 11, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, hiddenHtmlIsLack);
    createCells(rowObj, 12, "", "", "", "", "button", "", false, true, hiddenHtml);

    if (saleQty != "") {
        var money = (parseFloat(buyPrice) * parseFloat(saleQty) * discount).toFixed(2);
        rowObj.cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    }
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
    var tdStrHead = trHFlag ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
    var tdStrEnd = trEFlag ? '</td></TR>' : '</td>';
    var inputHtml = "";
    if (type == 'button') {
        inputHtml = '<input  type="button" class="normal_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt"  id="' + id + '" value="' + value + '" style="' + style + '" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'checkbox') {
        inputHtml = '<input  type="checkbox" onclick="countAll()"   id="' + id + '" name="cb" checked="true" value="' + value + '" />';
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
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    var money = (parseFloat(price) * parseFloat(value) * discount).toFixed(2);
    tbl.rows[idx].cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    countAll();
}
//清除数据重新计算
function cleanData(partId, idx) {
    var tbl = document.getElementById('file');
    tbl.rows[idx].cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
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
    var msg = "";
    if (document.getElementById("ORDER_TYPE").value == "") {
        msg += "请选择订单类型!</br>";
    }
    if (document.getElementById("SELLER_ID").value == "") {
        msg += "请选择销售单位!</br>";
    }
    if (document.getElementById("RCV_ORGID").value == "") {
        msg += "请选择接收单位!</br>";
    }
    if (document.getElementById("ADDR_ID").value == "") {
        msg += "请选择接收地址!</br>";
    }
    if (document.getElementById("TRANS_TYPE").value == "") {
        msg += "请选择发运方式!</br>";
    }
    if (document.getElementById("PAY_TYPE").value == "") {
        msg += "请选择付款方式!</br>";
    }

    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加计划明细!</br>";
    }
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //校验订货单价
            if (document.getElementById("buyPrice_" + cb[i].value).value == "" || document.getElementById("buyPrice_" + cb[i].value).value == '0.00') {
                msg += "第" + (i + 1) + "行配件【" + document.getElementById("partOldcode_" + cb[i].value).value + "】,单价不能为0!</br>";
                flag = true;
                break;
            }
            //需要校验计划量是否填写
            if (document.getElementById("buyQty_" + cb[i].value).value == "" || document.getElementById("buyQty_" + cb[i].value).value == "0") {
                msg += "请填写第" + (i + 1) + "行的订货数量!</br>";
                flag = true;
            } else {
                var mod = document.getElementById("buyQty_" + cb[i].value).value % document.getElementById("minPackage_" + cb[i].value).value;
                if (mod != 0) {
                    msg += "第" + (i + 1) + "行的订货数量必须为最小包装量的整数倍!</br>";
                    flag = true;
                }
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
        msg += "请选择计划明细!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return;
    }
    MyConfirm('确定保存订单?', saveOrder, []);
}

//保存订单
function saveOrder() {
    disableAllBtn();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/saveModifyOrder.json";
    makeNomalFormCall(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllBtn();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        var ulrFlg = jsonObj.ulrFlg;
        if (success) {
            MyAlert(success);
            if (ulrFlg == "2") {
                window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
                return;
            }
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partSalePurOrderInit.do';
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
function getIdx() {
    document.write(document.getElementById("file").rows.length - 1);
}
//获取选择框的值
function getCode(value) {
    if (value == <%=Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01%>) {
        document.write("Y");
    } else {
        document.write("N");
    }
}
//返回
function goBack() {
    if ($('state').value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>) {
       window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
       return;
   }

   window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partSalePurOrderInit.do';
}
//显示余额
function showAcount(accountId, accountSum, accountKy, accountDj) {
    if (accountId != "") {
        document.getElementById("accountSumNameTd").innerHTML = "账户余额：";
        document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum"   name="accountSum" type="text" class="normal_txt" value="' + accountSum + '" readonly /><font color="blue">元</font>';
        document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
        document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="' + accountKy + '" class="normal_txt" readonly /><font color="blue">元</font>';
        document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
        document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="text"  value="' + accountDj + '" class="normal_txt" readonly /><font color="blue">元</font>';
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
function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
    countAll();
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
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
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
     if (str.indexOf(".") > -1) {
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
     if (decPart != ".") {
         str += decPart;
     }

return str;
 }
function unFormatNum(str) {
    str = str + "";
    if (str.indexOf(",") > -1) {
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
        //过滤调拨
        if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05%>) {
            continue;
        }
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";
    document.write(str);
}
function changeOrderType(obj) {
    if (obj.value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%> || obj.value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
        initDivAndPartInfo();
        document.getElementById("Amount").value = "0";
    }
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
    document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum"   name="accountSum" type="text" class="middle_txt" value="' + ${acountMap.ACCOUNT_SUM} +'" readonly /><font color="blue">元</font>';
    document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
    document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="' + ${acountMap.ACCOUNT_KY} +'" class="middle_txt" readonly /><font color="blue">元</font>';
    document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
    document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="text"  value="' + ${acountMap.ACCOUNT_DJ} +'" class="middle_txt" readonly /><font color="blue">元</font>';
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
function getFreight(amountCount) {
    disableAllBtn();
    if ($('#SELLER_ID')[0].value !=<%=Constant.OEM_ACTIVITIES%>) {
        enableAllBtn();
        $('#freight')[0].value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if ("${isTransFreeOrg}" == "2") {
        enableAllBtn();
        $('#freight')[0].value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if ($('#TRANS_TYPE')[0].value == "3") {
        enableAllBtn();
        $('#freight')[0].value = "0.00";
        countAllWithoutFreight();
        return;
    }
    var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
    makeNomalFormCall(getFreightUrl, getFreightResult, 'fm');
}
function getFreightResult(jsonObj) {
    enableAllBtn();
    if (jsonObj != null) {
        $('#freight')[0].value = jsonObj.freight;
        $('#Amount')[0].value = formatNum((parseFloat(unFormatNum($('#Amount')[0].value)) + parseFloat(unFormatNum(jsonObj.freight))).toFixed(2));
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
function changeTrans(obj) {
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
$(document).ready(function(){
    document.getElementById("TRANS_TYPE").value = "${mainMap.TRANS_TYPE}";
    document.getElementById("partAmount").value = formatNum((${mainMap.AMOUNT}-${mainMap.FREIGHT2}).toFixed(2));
    if (!${dataMap.accountFlag}) {
        document.getElementById("accountSumNameTd").innerHTML = "";
        document.getElementById("accountSumTextTd").innerHTML = "";
        document.getElementById("accountKyNameTd").innerHTML = "";
        document.getElementById("accountKyTextTd").innerHTML = "";
        document.getElementById("accountDjNameTd").innerHTML = "";
        document.getElementById("accountDjTextTd").innerHTML = "";
        document.getElementById("accountId").value = "";
    }
});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="dealerCode" id="dealerCode" value="${mainMap.DEALER_CODE}">
<input type="hidden" name="dealerId" id="dealerId" value="${mainMap.DEALER_ID}">
<input type="hidden" name="ver" id="ver" value="${mainMap.VER}">
<input type="hidden" name="accountId" id="accountId"/>
<input type="hidden" name="ORDER_TYPE" id="ORDER_TYPE" value="${mainMap.ORDER_TYPE}"/>
<input type="hidden" name="sellerTemp" id="sellerTemp" value="${mainMap.SELLER_ID}"/>
<input type="hidden" name="state" id="state" value="${mainMap.STATE}">
<input type="hidden" name="produceFac" id="produceFac" value="${mainMap.PRODUCE_FAC}">
<div class="wbox">
 <div class="navigation">
 	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;配件采购管理&gt;销售采购订单提报&gt;修改
</div>
<div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>采购订单信息</h2>
     <div class="form-body">
	<table class="table_add" border="0" style="line-height:30px;" cellSpacing=1 cellPadding=1 width="100%">
	    <tr>
	        <td align="right" >订单号：</td>
	        <td>
	        	${mainMap.ORDER_CODE}
	        	<input type="hidden" value="${mainMap.ORDER_ID}" name="orderId"
	                   id="orderId"/>
	            <input readonly type="hidden" value="${mainMap.ORDER_CODE}" name="orderCode"
	                   id="orderCode"/>
	        </td>
	        <td align="right" nowrap>销售单位：</td>
	        <td>
	        	${mainMap.SELLER_NAME}
	            <input name="SELLER_NAME" class="middle_txt" id="SELLER_NAME" type="hidden"
	                   value="${mainMap.SELLER_NAME}"/> 
	            <input name="SELLER_CODE" class="middle_txt" id="SELLER_CODE" type="hidden"
	                   value="${mainMap.SELLER_CODE}" size="20" readonly="readonly"/>
	            <input name="SELLER_ID" class="middle_txt" id="SELLER_ID" type="hidden" 
	            	   value="${mainMap.SELLER_ID}" size="20" readonly="readonly"/>
	        </td>
	        <td align="right" nowrap>付款方式：</td>
	        <td>
	            <script type="text/javascript">
	                genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, ${mainMap.PAY_TYPE}, true, "u-select", "disabled", "false", '');
	            </script>
	        </td>
	    </tr>
	    <tr>
	        <td align="right">订货单位：</td>
	        <td>
	        	${mainMap.DEALER_NAME}
	        	<input readonly="readonly" class="middle_txt" type="hidden"
	                   value="${mainMap.DEALER_NAME}" name="dealerName" id="dealerName"/>
			</td>
	        <td align="right">订货人：</td>
	        <td>
		        ${mainMap.BUYER_NAME}
		        <input readonly="readonly" class="middle_txt" type="hidden"
	                   value="${mainMap.BUYER_NAME}" name="buyerName" id="buyerName"/>
	        </td>
	        <td align="right">订货日期：</td>
	        <td>
		        ${mainMap.CREATE_DATE}
		        <input type="hidden" value="${mainMap.CREATE_DATE}"
	                   name="now" id="now" readonly="readonly" />
	        </td>
	    </tr>
	    <tr>
	        <td align="right">接收单位：</td>
	        <td colspan="3"> ${mainMap.RCV_ORG}
	            <input name="RCV_ORG" class="long_txt" id="RCV_ORG" type="hidden" value="${mainMap.RCV_ORG}"
	                   size="20"
	                   readonly="readonly"/>
	            <input name="RCV_CODE" class="middle_txt" id="RCV_CODE" type="hidden" value="${mainMap.RCV_CODE}"
	                   size="20"
	                   readonly="readonly"/>
	            <input name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden" value="${mainMap.RCV_ORGID}"
	                   size="20" readonly="readonly"/>
	            <font color="RED">*</font>
	        </td>
	        <td align="right" style="display: none">发运方式：</td>
	        <td>
	            <select name="TRANS_TYPE" id="TRANS_TYPE"  class="u-select" onchange="countAll();"
	                    style="display: none">
	                <c:if test="${transList!=null}">
	                    <c:forEach items="${transList}" var="list">
	                        <option value="${list.fixValue }">${list.fixName }</option>
	                    </c:forEach>
	                </c:if>
	            </select>
	        </td>
	        <td align="right" ></td>
	        <td></td>
	    </tr>
	    <tr>
	        <td align="right">接收地址：</td>
	        <td colspan="3">
	            <input name="ADDR" class="middle_txt" style="border: none;width:450px;" id="ADDR" type="text" size="20"
	                   value="${mainMap.ADDR}" readonly="readonly"/>
	            <input name="ADDR_ID" class="middle_txt" id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden"
	                   size="20" readonly="readonly"/>
	            <input name='dlbtn3' id='dlbtn3' class='normal_btn' type='button' value='其他地址'
	                   onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('RCV_ORGID').value,'3')"/>
	            <font color="RED">*</font>
	        </td>
	        <td align="right">到站名称：</td>
	        <td>
	        	<input id="STATION" name="STATION" type="text" class="middle_txt" style="border: none"
	                   value="${mainMap.STATION}" readonly="readonly"/>
	            <font color="RED">*</font>
	        </td>
	    </tr>
	    <tr>
	        <td align="right">接收人：</td>
	        <td><input id="RECEIVER" name="RECEIVER" type="text" value="${mainMap.RECEIVER}" class="middle_txt"
	                   style="border: none"/>
	            <font color="RED">*</font></td>
	        <td align="right"><span align="right">接收人电话：</span></td>
	        <td><input id="TEL" name="TEL" type="text" class="middle_txt" style="border: none" readonly="readonly"
	                   value="${mainMap.TEL}"/>
	            <font color="RED">*</font></td>
	        <td align="right">邮政编码：</td>
	        <td><input id="POST_CODE" name="POST_CODE" type="text" class="middle_txt" style="border: none"
	                   value="${mainMap.POST_CODE}"
	                   readonly="readonly"/>
	            <font color="RED">*</font></td>
	    </tr>
	    <tr id="tr1">
	        <td id="accountSumNameTd" align="right">账户余额：</td>
	        <td id="accountSumTextTd">
	            <input id="accountSum" name="accountSum" style="border: none"
	                   type="text" class="middle_txt"
	                   value="${dataMap.currAcountMap.ACCOUNT_SUM}"
	                   readonly="readonly"/></td>
	        <td id="accountKyNameTd" align="right">可用金额：</td>
	        <td id="accountKyTextTd">
	            <input id="accountKy" style="border:0px;background-color:#6F9;"
	                   name="accountKy"
	                   type="text" value="${dataMap.currAcountMap.ACCOUNT_KY}"
	                   class="middle_txt"
	                   readonly="readonly"/><font color="blue">元</font></td>
	        <td id="accountDjNameTd" align="right"><span
	                align="right">冻结金额：</span></td>
	        <td id="accountDjTextTd">
	            <input id="accountDj" name="accountDj" type="text" style="border: none"
	                   value="${dataMap.currAcountMap.ACCOUNT_DJ}" class="middle_txt"
	                   readonly="readonly"/><font
	                color="blue">元</font></td>
	    </tr>
	    <tr id="tr2">
	        <td align="right">订单总金额：</td>
	        <td><input id="Amount" name="Amount" type="text"
	                   value="${mainMap.ORDER_AMOUNT} " class="middle_txt" readonly="readonly"
	                   style="background-color: #ffff80;border:none"/><font
	                color="blue">元</font></td>
	        <td align="right">订单金额：</td>
	        <td><input id="partAmount" name="partAmount" type="text"
	                   value="0.00 " class="middle_txt" readonly="readonly"
	                   style="background-color: #ffff80;border:none"/><font
	                color="blue">元</font></td>
	        <td align="right">运费金额：</td>
	        <td><input id="freight" name="freight" type="text" class="middle_txt" style="background-color: #ffff80;border:none"
	                   value="${mainMap.FREIGHT}" readonly="readonly"/></td>
	    </tr>
	    <tr id="tr3">
	        <td id="DISCOUNTNameTd" align="right">折扣率：</td>
	        <td id="DISCOUNTTextTd">
	        	<input type="text" name="DISCOUNT" id="DISCOUNT" style="border: none" 
	                   value="${mainMap.DISCOUNT}" readonly="readonly" class="middle_txt"/>
	        </td>
	        <td class="checked" colspan="5">提示：订单总金额=订单金额+运费金额</td>
	    </tr>
	    <tr>
	        <td align="right">备注：</td>
	        <td colspan="5"><textarea name="textarea" cols="80" rows="4">${mainMap.REMARK}</textarea></td>
	    </tr>
	</table>
	</div>
</div>
<table id="file" class="table_list" >
    <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
    <tr>
        <th><input type="checkbox" onclick="checkAll(this)" id="ckAll" name="ckAll" checked/></th>
        <th>序号</th>
		<th>配件编码</th>
		<th>配件名称</th>
		<th>单位</th>
		<th>最小包装量</th>
		<th>订货数量</th>
		<th>订货单价(折扣前)<font color="blue">(元)</font></th>
		<th>订货单价(折扣后)<font color="blue">(元)</font></th>
		<th>订货金额<font color="blue">(元)</font></th>
		<th>上级是否有库存</th>
		<th>备注</th>
		<th>操作</th>
     </tr>
     <c:forEach items="${detailList}" var="data">
        <tr class="table_list_row1">
            <td align="center">
                <input type='checkbox' id='cb' name='cb' onclick='countAll()' checked value='${data.PART_ID}'/>
            </td>
            <td align="center">
                <script type="text/javascript">
                    getIdx();
                </script>
            </td>
            <td align="left">
                <c:out value="${data.PART_OLDCODE}"/>
                <INPUT id="partOldcode_${data.PART_ID}" value="${data.PART_OLDCODE}" type=hidden
                       name="partOldcode_${data.PART_ID}"/>
            </td>
            <td align="left">
                <c:out value="${data.PART_CNAME}"/>
                <INPUT id="partCname_${data.PART_ID}" value="${data.PART_CNAME}" type=hidden
                       name="partCname_${data.PART_ID}"/>
            </td>
            <td>
                <c:out value="${data.UNIT}"/>
                <INPUT id="unit_${data.PART_ID}" value="${data.UNIT}" type=hidden name="unit_${data.PART_ID}"/>
            </td>
            <td>
                <c:out value="${data.MIN_PACKAGE}"/>
                <INPUT id="minPackage_${data.PART_ID}" value="${data.MIN_PACKAGE}" type=hidden
                       name="minPackage_${data.PART_ID}"/>
            </td>
            <td>
                <INPUT id="buyQty_${data.PART_ID}" value="${data.BUY_QTY}" type="text"
                       style="background-color:#FFFFCC;text-align:center" class="short_txt"
                       onchange="countMoney(this,'${data.BUY_PRICE}','${data.PART_ID}')"
                       name="buyQty_${data.PART_ID}"/>
            </td>
            <td align="right">
                <c:out value="${data.SALE_PRICE1}"/>
            </td>
            <td align="right">
                <c:out value="${data.BUY_PRICE}"/>
                <INPUT id="buyPrice_${data.PART_ID}" type="hidden" value="${data.BUY_PRICE}"
                       name="buyPrice_${data.PART_ID}"/>
            </td>
            <td align="right">
                <c:out value="${data.BUY_AMOUNT}"/>
                <INPUT id="buyAmount_${data.PART_ID}" type="hidden" value="${data.BUY_AMOUNT}"
                       name="buyAmount_${data.PART_ID}"/>
            </td>
            <td>
                <script type="text/javascript">
                    getCode(${data.IS_HAVA});
                </script>
                <INPUT id="upOrgStock_${data.PART_ID}" type="hidden" value="${data.IS_HAVA}"
                       name="upOrgStock_${data.PART_ID}"/>
                <INPUT id="isReplaced_${data.PART_ID}" value="${data.IS_REPLACED}" type=hidden
                       name="isReplaced_${data.PART_ID}"/>
                <INPUT id="isLack_${data.PART_ID}" value="${data.IS_LACK}" type=hidden
                       name="isLack_${data.PART_ID}"/>
            </td>
            <td>
                <INPUT id="remark_${data.PART_ID}" value="${data.REMARK}" type="text" class="short_txt"
                       name="remark_${data.PART_ID}"/>
            </td>
            <td>
            	<INPUT class="normal_btn" onclick=deleteTblRow(this); value=删除 type=button name=queryBtn>
                <INPUT id="stockQty_${data.PART_ID}" value="${data.STOCK_QTY}" type=hidden
                       name="stockQty_${data.PART_ID}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<table border="0" class="table_query">
    <tr>
        <td  class="center"><input class="normal_btn" type="button" value="保存" name="button1" onclick="saveOrderConfirm();">
           &nbsp;
           <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
        </td>    
    </tr>
</table>
<FIELDSET>
    <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
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
               <td align="right" width="13%">配件编码：</td>
               <td align="left" width="20%">
                   &nbsp;
                   <input class="middle_txt" id="PART_OLDCODE"
                          datatype="1,is_noquotation,30" name="PART_OLDCODE"
                          onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
               </td>
               <td align="right" width="13%">配件名称：</td>
               <td align="left" width="21%">
                   &nbsp;
                   <input type="text"  id="PART_CNAME" name="PART_CNAME" class="middle_txt"
                          datatype="1,is_noquotation,30" onblur="isCloseDealerTreeDiv(event,this,'pan')" />
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
       </table>
       <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
       <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
</div>
</form>
</body>
</html>
