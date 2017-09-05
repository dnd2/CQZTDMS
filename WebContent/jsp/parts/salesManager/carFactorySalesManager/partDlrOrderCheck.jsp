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
<title>配件订单审核(生成销售单)</title>
<style>fieldset table.table_query{background-color: transparent}textarea#REMARK2{width:610px}textarea#remark{width:410px}.dealer-contact{padding: 15px 0}table.table_query td.wh-name{padding-left: 11px}table.table_query td.remark-memo{padding-left: 12px}table#file{width: 1250px}table.table_query.bottom-buttons{margin-top:10px}table.bottom-buttons td:first-child{padding:5px 0}</style>
<script type="text/javascript">
//销售单类型
var orderType=${mainMap.ORDER_TYPE};

var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getGift.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
    {header: "赠送数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'}
];
function getSaleText(value) {
    return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
}
//为下面TABLE生成数据
// function addCells() {
//     var ck = document.getElementsByName('ck');
//     var mt = document.getElementById("myTable");
//     for (var i = 1; i < mt.rows.length; i++) {
//         var partId = mt.rows[i].cells[1].firstChild.value;  //ID
//         if (mt.rows[i].cells[1].firstChild.checked) {
//             if (validateCell(partId)) {
//                 var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
//                 var partCname = mt.rows[i].cells[3].innerText;  //配件名称
//                 var partCode = mt.rows[i].cells[4].innerText;  //件号
//                 var unit = mt.rows[i].cells[5].innerText;  //单位
//                 var minPkg = mt.rows[i].cells[6].innerText;
//                 var giftQty = mt.rows[i].cells[7].firstChild.value;
//                 addCell(partId, partCode, partOldcode, partCname, unit, minPkg, giftQty);
//             } else {
//                 MyAlert("配件：" + mt.rows[i].cells[3].innerText + "  已存在!</br>");
//                 break;
//             }
//         }
//     }
//     getPartQty();
// }
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
    //$('Amount').value =formatNum((parseFloat(unFormatNum($('Amount').value))+ parseFloat(unFormatNum(obj.value))).toFixed(2));
}

function countAllWithoutFreight() {
    var flag = true;
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
            }
        }
    }
    $('#Amount')[0].value = (parseFloat(unFormatNum(amountCount)) + parseFloat($('#freight')[0].value)).toFixed(2);
    $('#orderA')[0].value = (parseFloat(unFormatNum(amountCount)) ).toFixed(2);
}

//校验是否重复生成
function validateCell(value) {
    var flag = true;
    var tbl = document.getElementById('file');
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (value == cb[i].value) {
            flag = false;
            break;
        }
    }
    return flag;
}
//功能暂时屏蔽
// function addCell(partId, partCode, partOldcode, partCname, unit, minPackage, giftQty) {
//     var tbl = document.getElementById('file');
//     var rowObj = tbl.insertRow(tbl.rows.length);
//     rowObj.className = "table_list_row1";
//     var html1 = '<input id="stockQty_' + partId + '" name="stockQty_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
//     var html2 = '<input id="buyQty_' + partId + '" name="buyQty_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
//     var html3 = '<input id="saleQty_' + partId + '" name="saleQty_' + partId + '"  style="background-color: #ffff80;text-align:center;" class="short_txt" type="text" value="' + giftQty + '" /> ';
// 	var html4 = ' 0';
//     var html5 = '<input id="buyAmount_' + partId + '" name="buyAmount_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
//     var html6 = '<input id="gift" name="gift" readonly type="hidden" value="' + partId + '" /> ';
//     createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 2)), "cb", "", "", "checkbox", partId, true, false, "");
//     createCells(rowObj, 1, "", "", "", "", "idx", " " + (tbl.rows.length - 2), false, false, "");
//     createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
//     createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
//     createCells(rowObj, 4, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
//     createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
//     createCells(rowObj, 6, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, "");
//     createCells(rowObj, 7, "", "", "", "", "", "", false, false, html1);
//     createCells(rowObj, 8, "", "", "", "", "", "", false, false, html2);
//     createCells(rowObj, 9, "", "", "", "", "", "", false, false, html3);
//     createCells(rowObj, 10, "", "", "", "", "", "", false, false, html4);
//     createCells(rowObj, 11, "", "", "", "", "", "", false, false, html5);
//     createCells(rowObj, 12, "", "", "", "", "button", "", false, true, html6);
// }
function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    reNumIdx();
}
function reNumIdx() {
    var tbl = document.getElementById("file");
    for (var i = 2; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerHTML = (i - 1);
    }
}
//生成CELL
function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var cell = obj.insertCell(idx);
    cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);
}
function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    value = (value + "").replace(new RegExp("\"", "gm"), "&quot;");
    var tdStrHead = trHFlag == true ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
    var tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';
    var inputHtml = "";
    if (type == 'button') {
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt" value="' + value + '" style="' + style + '"  id="' + id + '" name="' + name + '" ' + onchangeEvent + ' />';
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
    if (hiddenHtml != "") {
        inputHtml += hiddenHtml;
    }
    return tdStrHead + inputHtml + tdStrEnd;
}
function onchangeVlidateSaleQty(obj) {
    if (obj.value == "") {
        return;
    }
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
function openDiv() {
    var orderDiv = document.getElementById("orderDiv");
    var qt = document.getElementById("queryT");

    if (qt.style.display == "block") {
        orderDiv.value = "打开";
        qt.style.display = "none";
    } else {
        orderDiv.value = "收起";
        qt.style.display = "block";
    }
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
function seled(value, meta, record) {
	//委托订单、销售转采购订单直接不让取消选中
    if(orderType=="92151004" || orderType=="92151012"){
    	return "<input type='checkbox' disabled=\"disabled\" value='" + value + "' name='ck' onclick='cPartCb()' id='ck' />";
    }else{
    	return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck' />";
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
//获取序号
function getIdx() {
    document.write(document.getElementById("file").rows.length - 1);
}
//获取CHECKBOX
function getCb(partId) {
    document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' checked onclick='countAll()' value='" + partId + "' />");
}
//计算所有CHECK的金额1
function countAll() {
    var flag = true;
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
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
    $('#orderA')[0].value = formatNum(amountCount);
    if($("#isLock")[0].value == "1"){
        $('#freight')[0].value = $('#lockFreight')[0].value;
        $('#Amount')[0].value = formatNum((parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum($('#lockFreight')[0].value)) ).toFixed(2));
        $('#orderA')[0].value = formatNum((parseFloat(unFormatNum(amountCount))).toFixed(2));
    }else{
        getFreight(amountCount);
    }
}
function getFreight(amountCount) {
    if($('SELLER_ID').value ==<%=Constant.OEM_ACTIVITIES%>){
   
         <%-- if ($('SELLER_ID').value !=<%=Constant.OEM_ACTIVITIES%>) {
         enableAllBtn();
         $('freight').value = "0.00";
         return;
         } --%>
        if ($('#TRANS_TYPE')[0].value == "3") {
            enableAllBtn();
            $('#freight')[0].value = "0.00";
            countAllWithoutFreight();
            return;
        }
        if($('#transpayType')[0].value == <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>){
            $('#freight')[0].value = "0.00";
            countAllWithoutFreight();
            return;
        }
        if($('#transpayType')[0].value != <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%> && "${mainMap.IS_TRANSFREE}"==<%=Constant.IF_TYPE_YES%> ){
            MyAlert("此单免运费!");
            $('#transpayType')[0].value = <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>;
            $('#freight')[0].value = "0.00";
            countAllWithoutFreight();
            return;
        }
        var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
        makeNomalFormCall(getFreightUrl, getFreightResult, 'fm');
    }else {
        $('#Amount')[0].value =formatNum((parseFloat(unFormatNum($('#Amount')[0].value))+ parseFloat(unFormatNum($('#freight')[0].value))).toFixed(2));;
        return;
    }
}
function getFreightResult(jsonObj) {
    enableAllBtn();
    if (jsonObj != null) {
        $('#freight')[0].value = jsonObj.freight;
        $('#Amount')[0].value = formatNum((parseFloat(unFormatNum(jsonObj.amountCount)) + parseFloat(unFormatNum(jsonObj.freight)) ).toFixed(2));
        $('#orderA')[0].value = formatNum((parseFloat(unFormatNum(jsonObj.amountCount))).toFixed(2));
    }
}

function countMoney(obj, price, partId) {
    price = unFormatNum(price);
    //直发订单、销售采购订单不用校验库存
    if (${mainMap.ORDER_TYPE} !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%> &&
        ${mainMap.ORDER_TYPE} !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12%> ) {
    	//校验库存
        var stockQty = document.getElementById("stockQty_" + partId).value;
        if (parseFloat(stockQty) < parseFloat(obj.value) && (document.getElementById('ORDER_TYPE').value !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>)) {
            MyAlert("销售数量不得大于库存数量!");
            obj.value = "0";
            countMoney(obj, price, partId);
            return;
        }
    }
    
    
    //获取折扣率
    var discount = document.getElementById("DISCOUNT").value;
    if (discount == null || discount == "") {
        discount = 1;
    }
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (value == '') {
        obj.value = "0";
        countMoney(obj, price, partId)
        return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "0";
        countMoney(obj, price, partId)
        return;
    }
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        countMoney(obj, price, partId)
        return;
    }

    if (parseFloat(document.getElementById("saleQty_" + partId).value) > parseFloat(document.getElementById("buyQty_" + partId).value)) {
        MyAlert("销售数量不得大于订货数量!");
        obj.value = "0";
        countMoney(obj, price, partId);
        return;
    }

    var money = (parseFloat(price) * parseFloat(value) * discount).toFixed(2);
    if ($("#orderType")[0].value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
        tbl.rows[idx].cells[11].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    } else {
        tbl.rows[idx].cells[11].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    }
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
    document.getElementById("partAmount").value=formatNum(amountCount);
    document.getElementById("Amount").value=formatNum(amountCount);
    $('#orderA')[0].value = formatNum(amountCount);
    //getFreight(amountCount);数据量大时速度太慢
}
//生成CELL中的HTML
function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var tdStrHead = trHFlag ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
    var tdStrEnd = trEFlag ? '</td></TR>' : '</td>';
    var inputHtml = "";
    if (type == 'button') {
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt"  id="' + id + '" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'checkbox') {
        inputHtml = '<input  type="checkbox"   id="' + id + '" name="cb" checked="true" value="' + value + '" />';
    }
    if (type == 'idx') {
        inputHtml = value;
    }
    if (hiddenHtml != "") {
        inputHtml += hiddenHtml;
    }
    return tdStrHead + inputHtml + tdStrEnd;
}
function repToFactoryConfirm() {
    MyConfirm("确定提交给车厂?", repToFactory, []);
}
function repToFactory() {
    disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/repToFactory.json?";
    makeNomalFormCall(url, getResult, 'fm');
}
function createSalesOrderConfirm() {
    countAll();
    var msg = "";
    var uncheckedId = "";
    if ("" == document.getElementById("wh_id").value) {
        msg += "请选择出库仓库!</br>";
    }
    if ("" == document.getElementById("RCV_ORG").value) {
        msg += "请选择接收单位!</br>";
    }
    if ("" == document.getElementById("ADDR").value) {
        msg += "请选择接收地址!</br>";
    }
    if ("" == document.getElementById("RECEIVER").value) {
        msg += "接收人不能为空!</br>";
    }
    if ("" == document.getElementById("TEL").value) {
        msg += "接收人电话不能为空!</br>";
    }
    if ("" == document.getElementById("POST_CODE").value) {
        msg += "邮政编码不能为空!</br>";
    }
    if ("" == document.getElementById("STATION").value) {
        msg += "到站名称不能为空!</br>";
    }
    if ("" == document.getElementById("TRANS_TYPE").value) {
        msg += "请选择发运方式!</br>";
    }
    if ("" == document.getElementById("PAY_TYPE").value) {
        msg += "请选择付款方式!</br>";
    }
    if ("" == document.getElementById("ORDER_TYPE").value) {
        msg += "请选择订单类型!</br>";
    }
    if ("" == document.getElementById("transpayType").value) {
        msg += "请选择运费支付方式!</br>";
    }

    var gift = document.getElementsByName("gift");

    var selFlag = false;
    var zeroFlag = 0;
    var cb = document.getElementsByName("cb");

    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            selFlag = true;
            var giftFlag = false;
            for (var j = 0; j < gift.length; j++) {
                if (gift[j].value == cb[i].value) {
                    giftFlag = true;
                    break;
                }
            }
            //需要校验销售量是否填写
            if (document.getElementById("saleQty_" + cb[i].value).value == "") {
                msg += "请填写第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的销售数量!</br>";
            } else {
                if (parseFloat(document.getElementById("saleQty_" + cb[i].value).value) > parseFloat(document.getElementById("buyQty_" + cb[i].value).value) && (!giftFlag)) {
                    msg += "第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的销售数量不得大于订货数量!</br>";
                }
                var mod = document.getElementById("saleQty_" + cb[i].value).value % document.getElementById("minPackage_" + cb[i].value).value;
                zeroFlag = parseFloat(zeroFlag) + parseFloat(document.getElementById("saleQty_" + cb[i].value).value);
//                 if ('${flag}' == '1') {
//                     var vender = document.getElementById("vender_" + cb[i].value).value;
//                     if (vender == "") {
//                         msg += "第" + (i + 1) + "行的的供应商不能为空!</br>";
//                     }
//                 }
            }
            //不等于 委托订单、销售采购订单时 
            if (${mainMap.ORDER_TYPE} !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%> &&
            	${mainMap.ORDER_TYPE} !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12%> && 
            	parseFloat(document.getElementById("stockQty_" + cb[i].value).value) < parseFloat(document.getElementById("saleQty_" + cb[i].value).value)) {
                msg += "第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行配件的库存不足!</br>";
            }
        } else {
            uncheckedId += "," + cb[i].value;
            document.getElementById("saleQty_" + cb[i].value).value = 0;
            cb[i].disabled = true;
        }
    }
    /* if (parseFloat(zeroFlag) <= parseFloat(0)) {
     MyAlert("销售配件总数不能为0!");
     return;
     }*/

    if (!selFlag) {
        msg += "请选择明细!</br>";
    }
    if (parseFloat(unFormatNum($('#Amount')[0].value)) == parseFloat(0)) {
        var orderId=$('#orderId')[0].value;
        MyConfirm("销售金额为0，将生成一般BO，确定生成一般BO?", closeOrder,[orderId]);
        return;
    }
    if (msg != "") {
        MyAlert(msg);
        enableCb();
        return;
    }
    MyConfirm("确定生成销售单?", createSaleOrder, [uncheckedId]);
    enableCb();
}

function closeOrder(id){
    var closeUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/closeOrderAction.json?orderId=" + id;
    makeNomalFormCall(closeUrl, closeResult, 'fm');
}
function closeResult(jsonObj){
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        var boStr = jsonObj.boStr;
        if (success) {
            MyAlert(success);
            if(boStr=="1"){
                OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/showPartBoInit.do?orderId="+jsonObj.orderId+"&orderCode="+jsonObj.orderCode,730,390);
            }
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function enableCb() {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
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
    disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getPartItemStock.json?whId=" + whId + "&partId=" + partId;
    makeNomalFormCall(url, getPartResult, 'fm');
}
function getPartResult(jsonObj) {
    if (jsonObj != null || jsonObj.list != null) {
        var reAr = jsonObj.list;
        for (var i = 0; i < reAr.length; i++) {
            var partId = reAr[i].PART_ID;
            var qty = reAr[i].NORMAL_QTY;
            document.getElementById("stockQty_" + partId).value = qty;
        }
    }
    enableAllBtn();
}
function getPartQtyOnLoad() {
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
    getPartItemStockOnLoad(whId, partId);
}
//
function getPartQtyOnLoad2() {
    if ($("#wh_id")[0].value == "") {
        MyAlert("请选择库房");
        return;
    }
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        var partId = cb[i].value;
        if (${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
            $('saleQty_' + partId).value = $('buyQty_' + partId).value;
        } else {
            if (parseFloat($('stockQty_' + partId).value) < parseFloat($('buyQty_' + partId).value)) {
                $('saleQty_' + partId).value =  $('stockQty_' + partId).value
            } else {
                $('saleQty_' + partId).value = $('buyQty_' + partId).value;
            }
        }

        countMoney($('saleQty_' + partId), $('buyPrice_' + partId).value, partId);
    }
}
function getPartItemStockOnLoad(whId, partId) {
    disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getPartItemStock.json?whId=" + whId + "&partId=" + partId;
    makeNomalFormCall(url, getPartResultOnLoad, 'fm');
}
function getPartResultOnLoad(jsonObj) {
    if (jsonObj != null || jsonObj.list != null) {
        var reAr = jsonObj.list;
        for (var i = 0; i < reAr.length; i++) {
            var partId = reAr[i].PART_ID;
            var qty = reAr[i].NORMAL_QTY;
            document.getElementById("stockQty_" + partId).value = qty;
            if (${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                $('saleQty_' + partId).value = $('buyQty_' + partId).value;
            } else {
                if (parseFloat(qty) < parseFloat($('buyQty_' + partId).value)) {
                    $('saleQty_' + partId).value = qty;
                } else {
                    $('saleQty_' + partId).value = $('buyQty_' + partId).value;
                }
            }

            countMoney($('saleQty_' + partId), $('buyPrice_' + partId).value, partId);
        }
    } else {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            document.getElementById("stockQty_" + cb[i].value).value = 0;
        }
    }
    enableAllBtn();
}

function createSaleOrder(uncheckedId) {
    disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/createSaleOrder.json?uncheckedId=" + uncheckedId;
    makeNomalFormCall(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllBtn();
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            var replace = jsonObj.replace;
            var boStr = jsonObj.boStr;
            var orderId = jsonObj.orderId;
            var orderCode = jsonObj.orderCode;
            if (replace) {
                MyAlert(replace);
            }
            MyAlert(success);
            if(boStr=="1"){//表示存在bo单
                OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/showPartBoInit.do?orderId="+orderId+"&orderCode="+orderCode,730,390);
            }
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function initAccount() {
    if (${mainMap.accountFlag}) {
        var td1 = document.getElementById("accountTd1");
        var td2 = document.getElementById("accountTd2");
        var td3 = document.getElementById("accountTd3");
        var td4 = document.getElementById("accountTd4");
        var td5 = document.getElementById("accountTd5");
        var td6 = document.getElementById("accountTd6");
        var str1 = '<td>账户余额:</td>';
        var str2 = '<td><input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="middle_txt" value="${mainMap.accountSumNow}" readonly /></td>';
        var str3 = '<td>可用金额:</td>';
        var str4 = '<td><input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="${mainMap.accountKyNow}" class="middle_txt" readonly /></td>';
        var str5 = '<td>冻结金额:</td>';
        var str6 = '<td><input id="accountDj"  name="accountDj" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.accountDjNow}" class="middle_txt" readonly /></td>';
        td1.innerHTML = str1;
        td2.innerHTML = str2;
        td3.innerHTML = str3;
        td4.innerHTML = str4;
//        td5.innerHTML = str5;
//        td6.innerHTML = str6;
    }
}
function ckAllBox(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
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
function unFormatNum(str) {
    str = str + "";
    if (str.indexOf(",") > -1) {
        str = str.replace(/\,/g, "");
    }
    return str;
}
function doInit() {
    document.getElementById("TRANS_TYPE").value = "${mainMap.TRANS_TYPE}";
    document.getElementById("transpayType").value  = ${mainMap.IS_TRANSFREE}==10041001?92381001:92381002;
    if (${flag==2}) {
        //将所有BUTTON disable 除了  goBack  和sub
        var inputArr = document.getElementsByTagName("input");
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                if (inputArr[i].id == "back" || inputArr[i].id == "sub"||inputArr[i].id == "orderDiv") {
                    continue;
                }
                inputArr[i].disabled = true;
            }
        }
        //将所有TEXT disable
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "text") {
                inputArr[i].disabled = true;
            }
        }
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "checkbox") {
                inputArr[i].disabled = true;
            }
        }
        //将所有TEXT select
        var selArr = document.getElementsByTagName("select");
        for (var i = 0; i < selArr.length; i++) {
            selArr[i].disabled = true;
        }
    }

    //如果是计划或者直发
    <%-- if (${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>||${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>) {
     var defaultOption;
     for (var i = 0; i < $('ORDER_TYPE').options.length; i++) {
     if ($('ORDER_TYPE').options[i].value ==${mainMap.ORDER_TYPE}) {
     defaultOption = $('ORDER_TYPE').options[i];
     }
     }
     $('ORDER_TYPE').options.length = 0;
     $('ORDER_TYPE').add(defaultOption);
     } else {
     var defaultOptionArr = [];
     for (var i = 0; i < $('ORDER_TYPE').options.length; i++) {
     if ($('ORDER_TYPE').options[i].value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>
     || $('ORDER_TYPE').options[i].value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>) {
     defaultOptionArr.push($('ORDER_TYPE').options[i]);
     }
     }
     $('ORDER_TYPE').options.length = 0;
     for (var i = 0; i < defaultOptionArr.length; i++) {
     $('ORDER_TYPE').add(defaultOptionArr[i]);
     }
     } --%>

    //默认仓库
    //getPartQtyOnLoad2();
    countAll();
}
function goBack() {
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
}

/*
 function validateFreight(obj){
 var reg=/^-?\d+\.?\d{0,2}$/;
 if(!reg.test(obj.value)){
 obj.value="0";
 MyAlert("请输入正确的金额!");
 return;
 }
 }
 */
//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
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
function rebutConfirm(id) {
	MyConfirm("确定驳回?",confirmResult,[id]);
}
function confirmResult(id){
    rebutReason(id);
}
function rebutReason(id){
    OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/rebutReason.jsp?id=' + id , 300, 200);
}
function rebut(id,reason) {
    var rebutUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/rebut.json?orderId=" + id+"&reason="+reason;
    makeNomalFormCall(rebutUrl, rebutResult, 'fm');
}
function rebutResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            goBack();
        } else if (error) {
            MyAlert(error);
        }/*  else if (exceptions) {
            MyAlert(exceptions.message);
        } */
    }
} 
function confirmCancelOrder(value) {
    MyConfirm("确定作废订单?", cancelOrder, [value]);
}
function cancelOrder(value) {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/cancelOrder.json?orderId=" + value + "&flag=1";
    makeNomalFormCall(url, getResult, 'fm');
}
//初始化
$(document).ready(function(){
	initAccount();//初始化账户
	doInit();
	enableAllBtn();
});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="orderCode" id="orderCode" type="hidden" value="${mainMap.ORDER_CODE}"/>
<input name="dealerId" id="dealerId" type="hidden" value="${mainMap.DEALER_ID}"/>
<input name="dealerCode" id="dealerCode" type="hidden" value="${mainMap.DEALER_CODE}"/>
<input name="dealerName" id="dealerName" type="hidden" value="${mainMap.DEALER_NAME}"/>
<input name="sellerId" id="sellerId" type="hidden" value="${mainMap.SELLER_ID}"/>
<input name="SELLER_ID" id="SELLER_ID" type="hidden" value="${mainMap.SELLER_ID}"/>
<input name="sellerCode" id="sellerCode" type="hidden" value="${mainMap.SELLER_CODE}"/>
<input name="sellerName" id="sellerName" type="hidden" value="${mainMap.SELLER_NAME}"/>
<input name="buyerId" id="buyerId" type="hidden" value="${mainMap.BUYER_ID}"/>
<input name="buyerName" id="buyerName" type="hidden" value="${mainMap.BUYER_NAME}"/>
<input name="orderId" id="orderId" type="hidden" value="${mainMap.ORDER_ID}"/>
<input name="orderAmount" id="orderAmount" type="hidden" value="${mainMap.ORDER_AMOUNT}"/>
<input name="soCode" id="soCode" type="hidden" value="${mainMap.soCode}"/>
<input name="planFlag" id="planFlag" type="hidden" value="${planFlag}"/>
<input name="orderType" id="orderType" type="hidden" value="${mainMap.ORDER_TYPE}"/>
<input name="isLock" id="isLock" type="hidden" value="${mainMap.LOCK_FREIGHT}"/>
<input name="lockFreight" id="lockFreight" type="hidden" value="${mainMap.FREIGHT2}"/>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:配件管理 > 配件销售管理 &gt;配件订单审核 &gt;订单审核生成销售单
</div>
<FIELDSET class="form-fieldset">
    <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
        <th colspan="6" style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;border-color: #859aff;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
            <img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 配件销售信息
            <input type="button" class="normal_btn" name="orderDiv" id="orderDiv" value="打开" onclick="openDiv()"/>
        </th>
    </LEGEND>
    <table id="queryT" class="table_query" border="0" style="display:none"
           style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
           cellSpacing=1 cellPadding=1 width="100%">
        <tr>
            <td align="right" >销售日期:</td>
            <td>${mainMap.CREATE_DATE}</td>
            <td align="right" >销售制单人:</td>
            <td>${mainMap.saleName}</td>
            <td align="right">采购制单人:</td>
            <td>${mainMap.NAME}</td>
        </tr>
        <tr>
            <td align="right">销售单位:</td>
            <td>${mainMap.SELLER_NAME}</td>
            <td align="right">订货单位:</td>
            <td>${mainMap.DEALER_NAME}</td>
            <td align="right">订货单位编码:</td>
            <td>${mainMap.DEALER_CODE}</td>
        </tr>
        <tr>
            <td align="right">采购日期:</td>
            <td>${mainMap.CREATE_DATE}</td>
<!--             <td align="right">出库仓库:</td> -->
<!--             <td> -->
<!--                 <select name="wh_id" id="wh_id" class="short_sel" onchange="getPartQty();"> -->
<%--                     <c:forEach items="${wareHouseList}" var="wareHouse"> --%>
<%--                         <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option> --%>
<%--                     </c:forEach> --%>
<!--                 </select> -->
<!--                 <font color="RED">*</font></td> -->
            <td align="right">接收单位:</td>
            <td colspan="3"><input name="RCV_ORG" class="middle_txt" id="RCV_ORG" type="text" value="${mainMap.RCV_ORG}" size="20"
                       readonly="readonly" style="width: 200px;"/>
                <input name="RCV_CODE" class="middle_txt" id="RCV_CODE" type="hidden" value="${mainMap.RCV_CODE}"
                       size="20" readonly="readonly"/>
                <input name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden" value="${mainMap.RCV_ORGID}"
                       size="20" readonly="readonly"/>
                <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                       onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${mainMap.DEALER_ID},'2')"/>
            </td>
        </tr>
        <tr>
            <td align="right">接收地址:</td>
            <td colspan="3"><input name="ADDR" class="middle_txt" id="ADDR" type="text" size="20" style="width: 450px;"
                                   value="${mainMap.ADDR}" readonly="readonly"/>
                <input name="ADDR_ID" class="middle_txt" id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden" size="20"
                       readonly="readonly"/>
                <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                       onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',${mainMap.DEALER_ID},'3')"/>
            </td>
            <td align="right">接收人:</td>
            <td><input id="RECEIVER" name="RECEIVER" type="text" value="${mainMap.RECEIVER}" class="middle_txt"/>
                <font color="RED">*</font></td>
        </tr>
        <tr>
            <td align="right">接收人电话:</td>
            <td><input id="TEL" name="TEL" type="text" class="middle_txt" readonly value="${mainMap.TEL}"/>
                <font color="RED">*</font></td>
            <td align="right">邮政编码:</td>
            <td><input id="POST_CODE" name="POST_CODE" type="text" class="middle_txt"
                       value="${mainMap.POST_CODE=='null'?'':mainMap.POST_CODE}" readonly/>
                <font color="RED">*</font></td>
            <td align="right">到站名称:</td>
            <td><input id="STATION" name="STATION" type="text" class="middle_txt" value="${mainMap.STATION}" readonly/>
                <font color="RED">*</font></td>
        </tr>
        <tr>
            <%-- <td align="right" >发运方式:</td>
            <td>
                <select name="TRANS_TYPE" id="TRANS_TYPE" class="u-select" onchange="countAll();" style="background-color: #fffb48">
                    <c:if test="${transList!=null}">
                        <c:forEach items="${transList}" var="list">
                            <option value="${list.fixValue }" selected>${list.fixName }</option>
                            <option value="${list.TV_ID }" selected>${list.TV_NAME }</option>
                        </c:forEach>
                    </c:if>
                </select>
                <font color="RED">*</font>
            </td>     --%>
            <td align="right">付款方式:</td>
            <td>
                <script type="text/javascript">
                    genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, ${mainMap.PAY_TYPE}, false, "u-select", "", "false", '');
                </script>
                <font color="RED">*</font></td>
            <td align="right">订单类型:</td>
            <td>
                <script type="text/javascript">
                    genSelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, ${mainMap.ORDER_TYPE}, false, "u-select", " disabled", "false", '');
                </script>
            </td>
        </tr>
        <tr>
            <td align="right">运费支付方式:</td>
            <td>
                <script type="text/javascript">
                    genSelBoxExp("transpayType", <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS%>, <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_02%>, false, "u-select", "onchange=countAll();" , "false", '');
                </script>
            </td>
            <td align="right">折扣:</td>
            <td><input readonly class="middle_txt" type="text" style="border:0px;background-color:#F3F4F8;"
                       value="${mainMap.DISCOUNT}" name="DISCOUNT" id="DISCOUNT"/>
                <font color="RED">*</font></td>
            <td align="right">订单提报金额:</td>
            <td><input readonly class="middle_txt" type="text" style="border:0px;background-color:#F3F4F8;"
                       value="${mainMap.AMOUNT}" name="orderAmount" id="orderAmount"/>
                <font color="RED">*</font></td>
        </tr>
        <tr>
            <td align="right">销售总金额:</td>
            <td><input id="Amount" name="Amount" type="text" style="border:0px;background-color:#F3F4F8;" value="0.00" onchange="countAll();"
                       class="middle_txt" readonly/><font color="blue">元</font></td>

            <td align="right">销售金额:</td>
            <td><input readonly  type="text" style="border:0px;background-color:#F3F4F8;" class="middle_txt"
                       value="0.00" name="partAmount" id="partAmount"/>
                <font color="blue">元</font></td>
            <td align="right">运费金额:</td>
            <td>
                <input id="freight" name="freight" type="text"  value="0"  class="middle_txt"
                       style="background-color: #ffff80" onchange="validateFreight(this)"/>
                <font color="blue">元</font>
            </td>
        </tr>
        <tr>
            <td align="right" id="accountTd1"></td>
            <td id="accountTd2" align="left"></td>
            <td align="right" id="accountTd3"></td>
            <td id="accountTd4"></td>
            <td align="right" id="accountTd5"></td>
            <td id="accountTd6"></td>
        </tr>
        <tr>
            <td align="right">经销商联系人:</td>
            <td class="dealer-contact" align="left">${linkMan}</td>
            <td align="right">经销商电话:</td>
            <td>${phone}</td>
        </tr>
        <input readonly type="hidden" value="0.00" no_border_txt name="orderA" id="orderA" />
        <tr>
            <td align="right">订单备注:</td>
            <td colspan="5"><textarea name="REMARK" id="remark" class="form-control align" readonly cols="50" rows="3">${mainMap.REMARK}</textarea>
            </td>
        </tr>
    </table>
    <table class="table_query" width=100% border="0" align="center"
          cellSpacing=1 cellPadding=1 width="100%">
        <tr>
        	<td width="8%" align="right">出库仓库:</td>
            <td class="wh-name">
                <select name="wh_id" id="wh_id" class="u-select" onchange="getPartQty();">
                    <c:forEach items="${wareHouseList}" var="wareHouse">
                        <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                    </c:forEach>
                </select><font color="RED">&nbsp;*</font>
            </td>
            <td class="right" align="right" >发运方式:</td>
            <td colspan="3">
                <select name="TRANS_TYPE" id="TRANS_TYPE" class="u-select" onchange="countAll();" >
                    <c:if test="${transList!=null}">
                        <c:forEach items="${transList}" var="list">
<%--                             <option value="${list.fixValue }" selected>${list.fixName }</option> --%>
                            <option value="${list.TV_ID }" selected>${list.TV_NAME }</option>
                        </c:forEach>
                    </c:if>
                </select>
                <font color="RED">*</font>
            </td>
        </tr>   
        <tr>
            <td align="right">备注:</td>
            <td class="remark-memo" colspan="5"><textarea id="REMARK2" class="form-control align" name="REMARK2" cols="50" rows="3">${mainMap.REMARK}</textarea></td>
        </tr>
    </table>
</FIELDSET>
<%-- <FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6"
            style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;border-color: #859aff;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            赠品查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="打开" onclick="addPartDiv()"/>
        </th>
    </LEGEND>

    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
            <tr>
                <td class="table_add_3Col_label_6Letter" width="13%">
                    件号：
                </td>
                <td width="20%" align="left">
                    &nbsp;
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td class="table_add_3Col_label_6Letter" width="13%">
                    配件编码：
                </td>
                <td align="left" width="20%">
                    &nbsp;
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td class="table_add_3Col_label_6Letter" width="13%">
                    配件名称：
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
                    <input class="normal_btn" align="right" type="button" name="queryBtn"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input class="normal_btn" type="button" name="BtnQuery2"
                           id="BtnQuery2" value="添加" onclick="addCells()"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET> --%>
<div class="table-wrap">
    <table id="file" class="table_list" border="0" cellSpacing=1 cellPadding=1 width="100%">
        <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
        <tr>
            <th><input type="checkbox" checked onclick="ckAllBox(this)" id="ckAll" name="ckAll"/></th>
            <th>序号</th>
            <th>配件编码</th>
            <th>配件名称</th>
            <th>件号</th>
            <th>最小包装量</th>
            <th>单位</th>
            <th>当前库存</th>
            <th>订货数量</th>
            <c:if test="${flag==1}">
    <!--             <th>供应商</th> -->
            </c:if>
            <th>销售数量<font color="RED">*</font></th>
            <th>销售单价<font color="blue">(元)</font></th>
            <th>销售金额<font color="blue">(元)</font></th>
            <th>备注</th>
        </tr>
        <c:forEach items="${detailList}" var="data">
            <c:if test="${data.OF_FLAG eq 10041001}">
                <tr class="table_list_row1" style="background-color:greenyellow">
            </c:if>
            <c:if test="${data.OF_FLAG ne 10041001}">
                <tr class="table_list_row1">
            </c:if>
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
                <td align="center"><c:out value="${data.PART_OLDCODE}"/></td>
                <td align="center"><c:out value="${data.PART_CNAME}"/></td>
                <td align="center"><c:out value="${data.PART_CODE}"/></td>
                <td>
                    <c:out value="${data.MIN_PACKAGE}"/>
                    <input id="minPackage_${data.PART_ID}" name="minPackage_${data.PART_ID}" type="hidden" value="${data.MIN_PACKAGE}"/>
                </td>
                <td><c:out value="${data.UNIT}"/></td>
                <td> 
                    <input id="stockQty_${data.PART_ID}" name="stockQty_${data.PART_ID}" readonly value="${data.STOCK_QTY}"
                                style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text"/>
                </td>
                <td>
                    <input id="buyQty_${data.PART_ID}" name="buyQty_${data.PART_ID}" readonly value="${data.BUY_QTY}"
                                style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text"/>
                </td>
                <c:if test="${flag==1}">
    <!--                 <td> -->
    <%--                     <input class="middle_txt" type="text" readonly="readonly" id="vender_${data.PART_ID}"  name="vender_${data.PART_ID}" /> --%>
    <%--                     <input class="mark_btn" type="button" value="&hellip;" onclick="showPartMaker('vender_${data.PART_ID}','VENDER_ID_${data.PART_ID}','false')"/> --%>
    <%--                     <input id="VENDER_ID_${data.PART_ID}" name="VENDER_ID_${data.PART_ID}" type="hidden" value=""> --%>
    <!--                     <font color="red">*</font> -->
    <!--                 </td> -->
                </c:if>
                
                <td>
                    <input id="saleQty_${data.PART_ID}" name="saleQty_${data.PART_ID}" type="text" class="short_txt"
                                value="${data.PER_SALE_QTY}" style="background-color: #ffff80;text-align:center"
                                onchange="countMoney(this,'${data.BUY_PRICE}',${data.PART_ID})"></td>
                <td>
                    <input id="buyPrice_${data.PART_ID}" name="buyPrice_${data.PART_ID}" readonly
                        style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text"
                        value="${data.BUY_PRICE}"/>
                </td>
                <td>
                    <input id="buyAmount_${data.PART_ID}" name="buyAmount_${data.PART_ID}" readonly
                        style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value=""/>
                    <script type="text/javascript">
                        $('#buyAmount_${data.PART_ID}')[0].value = formatNum((parseFloat(unFormatNum(${data.PER_SALE_QTY})) * parseFloat(unFormatNum('${data.BUY_PRICE}'))).toFixed(2));
                    </script>
                </td>
                <td>
                    <c:out value="${data.REMARK}"/>
                    <c:if test="${flag==2}">
                        <script type="text/javascript">
                            document.getElementById("saleQty_" +${data.PART_ID}).value = '${data.BUY_QTY}';
                            countMoney(document.getElementById("saleQty_" +${data.PART_ID}), '${data.BUY_PRICE}', ${data.PART_ID});
                        </script>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>    

<table border="0" class="table_query bottom-buttons">
    <tr>
        <td  class="center">&nbsp;
<%--             <c:if test="${flag!=2}"> --%>
                <input class="normal_btn" type="button" value="生成销售单" onclick="createSalesOrderConfirm();"/>
<%--             </c:if> --%>
<%--             <c:if test="${flag==2}"> --%>
<!--                 <input class="long_btn" type="button" name="sub" id="sub" value="提交给车厂" -->
<!--                        onclick="repToFactoryConfirm();"/> -->
<%--             </c:if> --%>
            &nbsp;&nbsp;
            <c:choose>
                <c:when test="${oemFlag == 10041001}">
                    <input class="normal_btn" type="button" value="作 废" name="reject" id="back" onclick="confirmCancelOrder(${mainMap.ORDER_ID})"/>
                </c:when>
                <c:otherwise>
                    <input class="normal_btn" type="button" value="驳 回" name="reject" id="back" onclick="rebutConfirm(${mainMap.ORDER_ID})"/>
                </c:otherwise>
            </c:choose>
            &nbsp;&nbsp;
            <input class="normal_btn" type="button" value="返 回" name="back" id="back" onclick="goBack()"/>
        </td>
    </tr>
</table>
</div>
</form>
</body>
</html>