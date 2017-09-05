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
<style>
    table.table_noboder {
        border: none;
        background-color: #F3F4F8;
    }
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/showPartBaseForMod.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "当前库存数量", dataIndex: 'ITEM_QTY', align: 'center', renderer: inputText},
    {header: "切换件编码", dataIndex: 'REPART_OLDCODE', align: 'center'},
    {header: "切换件名称", dataIndex: 'REPART_NAME', align: 'center'},
    {header: "切换数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'},
    {header: "配件类型", align: 'center', renderer: getItemValue, dataIndex: 'PART_TYPE'},
    {header: "是否需要回运", align: 'center', renderer: getItemValue, dataIndex: 'ISNEED_FLAG'},
    
    {header: "备注", dataIndex: 'REMARK', style: 'text-align: left;'}
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
    $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value)) + parseFloat(unFormatNum(obj.value))).toFixed(2));
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
	var actCode = '${mainMap.ACTIVITY_CODE}';
	var select = document.getElementById("actCodeMap");
	for (var i = 0; i < select.options.length; i++) {        
        if (select.options[i].value == actCode) {        
        	select.options[i].selected = true;        
            break;        
        }        
	}
	
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if ($('SELLER_ID').value == "") {
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
    var miniPkg = record.data.MIN_PACKAGE;
    var partid = record.data.PART_ID;
    var repartid = record.data.REPART_ID;
    var partCode = record.data.PART_CODE;
    var isneedFlag = record.data.ISNEED_FLAG;
    var activityCode = record.data.ACTIVITY_CODE;
    return value + "<input type='hidden' id='min_package_' name='min_package_" + partid + "' value='" + miniPkg + "' />" +
            "<input type='hidden' id='min_package_' name='part_Code_" + partid + "' value='" + partCode + "' />" +
            "<input type='hidden' id='isneedFlag_' name='ISNEED_FLAG_" + partid + "' value='" + isneedFlag + "' />" +
            "<input type='hidden' id='ACTIVITY_CODE_' name='ACTIVITY_CODE_" + partid + "' value='" + activityCode + "' />" +
            "<input type='hidden' id='repart_id_' name='repart_id_" + partid + "' value='" + repartid + "' />";
}
function seled(value, meta, record) {
    if (${mainMap.ORDER_TYPE} !=
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
//     countAll();
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
                var itemQty = mt.rows[i].cells[4].innerText;  //当前库存
                var repart_oldcode = mt.rows[i].cells[5].innerText;  //切换件编码
                var repart_cname = mt.rows[i].cells[6].innerText;  //切换件名称
                var replaced_num = mt.rows[i].cells[7].firstChild.value;  //切换数量
                var part_type = mt.rows[i].cells[8].innerText;  //配件类型
                var isneed_flag = mt.rows[i].cells[9].innerText;  //是否需要回运
                var remake = mt.rows[i].cells[10].innerText;  //备注
                addCell(partId, partOldcode, partCname, itemQty, repart_oldcode, repart_cname, replaced_num,part_type,isneed_flag, remake)

            } else {
                MyAlert("配件：" + mt.rows[i].cells[3].innerText + "  已存在!</br>");
                break;
            }
        }
    }
}

function addCell(partId, partOldcode, partCname, itemQty, repart_oldcode, repart_cname, replaced_num, part_type,isneed_flag,remake) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }

//     if (upOrgStock == "Y" ) {
//         upOrgStock = "Y";
//     } else {
//         upOrgStock = "N";
//     }
    //获取折扣率
//     var discount = document.getElementById("DISCOUNT").value;


//     var buyPrice1 = (parseFloat(buyPrice) * parseFloat(discount) * discount).toFixed(2);

    var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' >";
    createCells(rowObj, 0, "cb", "cb", "", "", "checkbox", partId, true, false, "");
    createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 2), false, false, "");
    createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
    createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
    createCells(rowObj, 4, ("itemQty_" + partId), ("itemQty_" + partId), "", "", "hidden", itemQty, false, false, "");
    createCells(rowObj, 5, ("buyQty_" + partId), ("buyQty_" + partId), "", "", "text", replaced_num, false, false, "");
    createCells(rowObj, 6, ("repartOldcode_" + partId), ("repartOldcode_" + partId), "", "", "hidden", repart_oldcode, false, false, "");
    createCells(rowObj, 7, ("repartCname_" + partId), ("repartCname_" + partId), "", "", "hidden", repart_cname, false, false, "");
    createCells(rowObj, 8, ("part_type"), ("part_type"), "", "", "hidden", part_type, false, false, "");

    createCells(rowObj, 9, ("isneed_flag_" + partId), ("isneed_flag_" + partId), "", "", "hidden", isneed_flag, false, false, "");
    createCells(rowObj, 10, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, "");
    createCells(rowObj, 11, "", "", "", "", "button", "", false, true, hiddenHtml);

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
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
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
    tbl.rows[idx].cells[10].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    countAll();
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

//check整整数
function check(obj) {
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj)) {
        return false;
    } else {
        return true;
    }
}

//保存订单确认
function saveOrderConfirm() {
    var msg = "";
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
            /* if(document.getElementById("buyQty_"+cb[i].value).value > document.getElementById("stockQty_"+cb[i].value).value){
             msg += "第"+ (i + 1)+"行的切换数量必须小于当前库存数量!</br>";
             MyAlert(msg);
             return false;
             }  */
        } else {
            cb[i].disabled = true;
        }
    }
    if (cb.length <= 0) {
        msg += "请添加计划明细!</br>";
    }
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
    document.getElementById("state").value =
    <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/saveModifyOrder.json";
    sendAjax(url, getResult, 'fm');
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
                window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
                return;
            }
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDlrOrderInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function MyAlert(info) {
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
}

function getIdx() {
    document.write(document.getElementById("file").rows.length - 2);
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
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderCheckInit.do?flag=true';
        return;
    }

    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partReplacedDlrOrderInit.do';
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
    
    
//     document.getElementById("partAmount").value = formatNum((${mainMap.AMOUNT}-${mainMap.FREIGHT2}).toFixed(2));
//     if (!${dataMap.accountFlag}) {
//         document.getElementById("accountSumNameTd").innerHTML = "";
//         document.getElementById("accountSumTextTd").innerHTML = "";
//         document.getElementById("accountKyNameTd").innerHTML = "";
//         document.getElementById("accountKyTextTd").innerHTML = "";
//         document.getElementById("accountDjNameTd").innerHTML = "";
//         document.getElementById("accountDjTextTd").innerHTML = "";
//         document.getElementById("accountId").value = "";
//     }
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
    if ($('SELLER_ID').value !=<%=Constant.OEM_ACTIVITIES%>) {
        enableAllBtn();
        $('freight').value = "0.00";
        countAllWithoutFreight();
        return;
    }
    if ("${isTransFreeOrg}" == "2") {
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
    document.getElementById("partAmount").value = formatNum(amountCount);
    document.getElementById("Amount").value = formatNum(amountCount);
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
</script>
</head>

<body onload="loadcalendar();doInit()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="dealerCode" id="dealerCode"
       value="${mainMap.DEALER_CODE}"> <input type="hidden"
                                              name="dealerId" id="dealerId" value="${mainMap.DEALER_ID}"> <input
        type="hidden" name="ver" id="ver" value="${mainMap.VER}"> <input
        type="hidden" name="accountId" id="accountId"/> <input type="hidden"
                                                               name="ORDER_TYPE" id="ORDER_TYPE"
                                                               value="${mainMap.ORDER_TYPE}"/> <input
        type="hidden" name="sellerTemp" id="sellerTemp"
        value="${mainMap.SELLER_ID}"/> <input type="hidden" name="state"
                                              id="state" value="${mainMap.STATE}">

<div class="wbox">
<div class="navigation">
    <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;配件采购管理&gt;切换订单&gt;切换订单修改
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
        <!-- 					<td align="right" nowrap>销售单位：</td> -->
        <td><input name="SELLER_NAME" class="middle_txt" id="SELLER_NAME" type="hidden" value="${mainMap.SELLER_NAME}"/>
            <input name="SELLER_CODE" class="middle_txt" id="SELLER_CODE" type="hidden" value="${mainMap.SELLER_CODE}"
                   size="20" readonly="readonly"/>
            <input name="SELLER_ID" class="middle_txt" id="SELLER_ID" type="hidden" value="${mainMap.SELLER_ID}"
                   size="20" readonly="readonly"/>
            <!-- <input name='dlbtn1' id='dlbtn1' class='mini_btn'  type='button' value='...'  onclick="selSales('SELLER_ID','SELLER_CODE','SELLER_NAME','','','','',${mainMap.DEALER_ID},'1')"/> -->
            <font color="RED"></font></td>
        <!-- 					<td align="right" nowrap>付款方式：</td> -->
        <!-- 					<td> -->
        <!-- 					<script type="text/javascript"> -->
        <%--                 genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, ${mainMap.PAY_TYPE}, true, "short_sel", "", "false", ''); --%>
        <!--             </script> -->
        <!--              <font color="RED"></font></td> -->
        <!-- 					<td align="right" nowrap>订单号：</td> -->
        <td><input type="hidden" value="${mainMap.ORDER_ID}"
                   name="orderId" id="orderId"/><input readonly type="hidden"
                                                       value="${mainMap.ORDER_CODE}" name="orderCode" id="orderCode"/>
        </td>
    </tr>
    <tr>
        <td align="right">订货单位：</td>
        <td><input readonly class="middle_txt" type="hidden"
                   value="${mainMap.DEALER_NAME}" name="dealerName" id="dealerName"/>${mainMap.DEALER_NAME}</td>
        <td align="right">订货人：</td>
        <td><input readonly class="middle_txt" type="hidden"
                   value="${mainMap.BUYER_NAME}" name="buyerName" id="buyerName"/>${mainMap.BUYER_NAME}</td>
        <td align="right">订货日期：</td>
        <td><input readonly type="hidden"
                   value="${mainMap.CREATE_DATE}" name="now" id="now"/>${mainMap.CREATE_DATE}</td>
    </tr>
    <tr>
        <td align="right">接收单位：</td>
        <td><input name="RCV_ORG" class="long_txt" id="RCV_ORG"
                   type="text" value="${mainMap.RCV_ORG}" size="20"
                   readonly="readonly" style="border: none;background-color: #F3F4F8"/> <input name="RCV_CODE"
                                                                                               class="middle_txt"
                                                                                               id="RCV_CODE"
                                                                                               type="hidden"
                                                                                               value="${mainMap.RCV_CODE}"
                                                                                               size="20"
                                                                                               readonly="readonly"/>
            <input name="RCV_ORGID" class="middle_txt"
                   id="RCV_ORGID" type="hidden" value="${mainMap.RCV_ORGID}"
                   size="20" readonly="readonly"/> <%--<input name='dlbtn2' id='dlbtn2'
						class='mini_btn' type='button' value='...'
						onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${mainMap.DEALER_ID},'2')" />--%>
        </td>
        <!-- 					<td align="right">发运方式：</td> -->
        <!-- 					<td><select name="TRANS_TYPE" id="TRANS_TYPE" -->
        <!-- 						class="short_sel" onchange="countAll();"> -->

        <%-- 							<c:if test="${transList!=null}"> --%>
        <%-- 								<c:forEach items="${transList}" var="list"> --%>
        <%-- 									<option value="${list.fixValue }">${list.fixName }</option> --%>
        <%-- 								</c:forEach> --%>
        <%-- 							</c:if> --%>
        <!-- 					</select></td> -->
        <!-- 					<td align="right">产地：</td> -->
        <!-- 					<td><script type="text/javascript"> -->
        <%--                 genSelBoxExp("produceFac", <%=Constant.YIELDLY%>, "${mainMap.PRODUCE_FAC}", false, "short_sel", "onchange='changeProduceFac()'", "false", ''); --%>
        <!--             </script> </td> -->
    </tr>
    <tr>
        <td align="right">接收地址：</td>
        <td colspan="3"><input name="ADDR" class="maxlong_txt"
                               id="ADDR" type="text" size="20" value="${mainMap.ADDR}"
                               readonly="readonly" style="border: none;background-color: #F3F4F8"/> <input
                name="ADDR_ID" class="middle_txt"
                id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden" size="20"
                readonly="readonly"/> <%--<input name='dlbtn3' id='dlbtn3'
						class='mini_btn' type='button' value='...'
						onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('RCV_ORGID').value,'3')" />--%>
        </td>
        <td align="right">到站名称：</td>
        <td><input id="STATION" name="STATION" type="text"
                   class="normal_txt" value="${mainMap.STATION}" readonly
                   style="border: none;background-color: #F3F4F8"/>
        </td>
    </tr>
    <tr>
        <td align="right">接收人：</td>
        <td><input id="RECEIVER" name="RECEIVER" type="text"
                   value="${mainMap.RECEIVER}" class="normal_txt" style="border: none;background-color: #F3F4F8"
                   readonly/>
        </td>
        <td align="right"><span align="right">接收人电话：</span></td>
        <td><input id="TEL" name="TEL" type="text" class="normal_txt"
                   readonly value="${mainMap.TEL}" style="border: none;background-color: #F3F4F8"/></td>
        <td align="right">邮政编码：</td>
        <td><input id="POST_CODE" name="POST_CODE" type="text"
                   class="normal_txt" value="${mainMap.POST_CODE}" readonly
                   style="border: none;background-color: #F3F4F8"/>
        </td>
    </tr>
    <!-- 				<tr id="tr1"> -->
    <!-- 					<td id="accountSumNameTd" align="right">账户余额：</td> -->
    <td id="accountSumTextTd"><input id="accountSum"
                                     name="accountSum" style="border: none" type="hidden"
                                     class="normal_txt" value="${dataMap.currAcountMap.ACCOUNT_SUM}"
                                     readonly/></td>
    <!-- 					<td id="accountKyNameTd" align="right">可用金额：</td> -->
    <td id="accountKyTextTd"><input id="accountKy"
                                    style="border: 0px; background-color: #6F9;" name="accountKy"
                                    type="hidden" value="${dataMap.currAcountMap.ACCOUNT_KY}"
                                    class="normal_txt" readonly/><font color="blue"></font></td>
    <!-- 					<td id="accountDjNameTd" align="right"><span align="right">冻结金额：</span></td> -->
    <td id="accountDjTextTd"><input id="accountDj"
                                    name="accountDj" type="hidden" style="border: none"
                                    value="${dataMap.currAcountMap.ACCOUNT_DJ}" class="normal_txt"
                                    readonly/><font color="blue"></font></td>
    <!-- 				</tr> -->
    <!-- 				<tr id="tr2"> -->
    <!-- 					<td align="right">订单总金额：</td> -->
    <td><input id="Amount" name="Amount" type="hidden"
               value="${mainMap.ORDER_AMOUNT} " class="normal_txt" readonly
               style="background-color: #ffff80; border: none"/><font
            color="blue"></font></td>
    <!-- 					<td align="right">订单金额：</td> -->
    <td><input id="partAmount" name="partAmount" type="hidden"
               value="0.00 " class="normal_txt" readonly
               style="background-color: #ffff80; border: none"/><font
            color="blue"></font></td>
    <!-- 					<td align="right">运费金额：</td> -->
    <td><input id="freight" name="freight" type="hidden"
               style="background-color: #ffff80; border: none"
               value="${mainMap.FREIGHT}" readonly/></td>
    <!-- 				</tr> -->
    <!-- 				<tr id="tr3"> -->
    <!-- 					<td id="DISCOUNTNameTd" align="right">折扣率：</td> -->
    <td id="DISCOUNTTextTd"><input readonly class="middle_txt"
                                   type="hidden" style="border: none" value="${mainMap.DISCOUNT}"
                                   name="DISCOUNT" id="DISCOUNT"/></td>
    <!-- 					<td class="checked" colspan="5">提示：订单总金额=订单金额+运费金额</td> -->
    <!-- 				</tr> -->
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
        <td colspan="5"><textarea name="textarea" cols="80" rows="4">${mainMap.REMARK}</textarea></td>
    </tr>
</table>

<table id="file" class="table_list"
       style="border-bottom: 1px solid #DAE0EE">
    <tr>
        <th colspan="15" align="left"><img class="nav"
                                           src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span class="checked"
                                                                                             style="text-align: left"> </span>
        </th>
    </tr>
    <tr bgcolor="#FFFFCC">
        <td><input type="checkbox" onclick="checkAll(this)" id="ckAll"
                   name="ckAll" checked/></td>
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
                    document.write('<input  type="checkbox" onclick="countAll()"  id="cell_' + (document.getElementById('file').rows.length - 2) + '"  name="cb"  checked="true" value="'+${data.PART_ID}+
                    '" />'
                    )
                    ;
                </script>
            </td>
            <td>
                <script type="text/javascript">
                    document.write((document.getElementById('file').rows.length - 2));
                </script>
            </td>
            <td nowrap><input type="hidden"
                              id="partOldcode_${data.PART_ID}"
                              name="partOldcode_${data.PART_ID}" value="${data.PART_OLDCODE}"/>
                    ${data.PART_OLDCODE}</td>
            <td>${data.PART_CNAME}
                <input type="hidden"
                       id="partCname_${data.PART_ID}" name="partCname_${data.PART_ID}"
                       value="${data.PART_CNAME}"/>
                <input type="hidden"
                       id="repart_id_${data.PART_ID}" name="repart_id_${data.PART_ID}"
                       value="${data.REPART_ID}"/>
            </td>
            <td>${data.STOCK_QTY}</td>
            <td><input type="text" class="short_txt"
                       value="${data.BUY_QTY}" style="" id="buyQty_${data.PART_ID}"
                       name="buyQty_${data.PART_ID}"/></td>
            <td>${data.REPART_OLDCODE} <input type="hidden"
                                              id="repartOldcode_${data.PART_ID}"
                                              name="repartOldcode_${data.PART_ID}"
                                              value="${data.REPART_OLDCODE}"/>
            </td>
            <td>${data.REPART_NAME} <input type="hidden"
                                           id="partCname_${data.PART_ID}" name="partCname_${data.PART_ID}"
                                           value="${data.REPART_NAME}"/>
                <input type="hidden"
                       id="ISNEED_FLAG_${data.PART_ID}" name="ISNEED_FLAG_${data.PART_ID}"
                       value="${data.ISNEED_FLAG}"/>
            </td>
            <td>${PART_TYPE}</td>
            <c:if test="${data.ISNEED_FLAG==10041001}">
            <td>是</td>
            </c:if>
            <c:if test="${data.ISNEED_FLAG==10041002}">
            <td>否</td>
            </c:if>
            <td><input type="text" class="short_txt"
                       value="${data.REMARK}" style="" id="remark_${data.PART_ID}"
                       name="remark_${data.PART_ID}"/><input type='hidden'
                                                             name='isLack_${data.PART_ID}' id='isLack_${data.PART_ID}'
                                                             value='${data.IS_LACK}'></td>
            <td><input type="button" class="short_btn" name="queryBtn"
                       value="删除" onclick="deleteTblRow(this);"/><input type='hidden'
                                                                        name='stockQty_${data.PART_ID}'
                                                                        id='stockQty_${data.PART_ID}'
                                                                        value='${data.STOCK_QTY}'></td>

        </tr>
    </c:forEach>

</table>

<table border="0" class="table_query">
    <tr align="center">
        <td><input class="cssbutton" type="button" value="保存"
                   name="button1" onclick="saveOrderConfirm();"> &nbsp; <input
                class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
    </tr>
</table>
<FIELDSET>
    <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6"
            style="background-color: #DAE0EE; font-weight: normal; color: #416C9B; padding: 2px; line-height: 1.5em; border: 1px solid #E7E7E7;">
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
                <td><select name="actCodeMap" id="actCodeMap" class="short_sel" onchange="selected()">
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
                        type="text" />
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
                <td align="left" width="21%">&nbsp; <input
                        class="middle_txt" id="PART_CNAME"
                        datatype="1,is_noquotation,30" name="PART_CNAME"
                        onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>

            </tr>
        </table>
        <table width="100%">
            <tr>
                <td align="center"><input class="normal_btn" type="button"
                                          name="BtnQuery" id="queryBtn" value="查 询"
                                          onclick="__extQuery__(1)"/>&nbsp;&nbsp;&nbsp;&nbsp; <input
                        class="normal_btn" type="button" name="BtnQuery" id="queryBtn"
                        value="添加" onclick="addCells()"/></td>
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
