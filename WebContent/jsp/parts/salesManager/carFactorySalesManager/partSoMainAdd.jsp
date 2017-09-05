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
var columns = [];

var columns1 = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "采购数量",  align: 'center',renderer:getSaleText,dataIndex:'PART_ID'},
    {header: "当前总库存量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
    {header: "订购单价", dataIndex: 'SALE_PRICE1', align: 'center', renderer: getDefaultValue},
    {header: "上级库存量", dataIndex: 'UPORGSTOCK', align: 'center'},
    {header: "是否紧缺", dataIndex: 'IS_LACK', align: 'center', renderer: inputText},
    {header: "是否可替代", dataIndex: 'IS_REPLACED', align: 'center', renderer: inputText},
    {header: "是否大件", dataIndex: 'IS_PLAN', align: 'center', renderer: inputText},
    {header: "是否直发", dataIndex: 'IS_DIRECT', align: 'center', renderer: inputText}

];
var columns2 = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "采购数量",  align: 'center',renderer:getSaleText,dataIndex:'PART_ID'},
    {header: "当前总库存量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
    {header: "是否紧缺", dataIndex: 'IS_LACK', align: 'center', renderer: inputText},
    {header: "是否可替代", dataIndex: 'IS_REPLACED', align: 'center', renderer: inputText},
    {header: "是否大件", dataIndex: 'IS_PLAN', align: 'center', renderer: inputText},
    {header: "是否直发", dataIndex: 'IS_DIRECT', align: 'center', renderer: inputText}

];
function getSaleText(value){
	return "<input name='partSalesQty_"+value+"' id='partSalesQty_"+value+"' type='text' value='0' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >" ;
}
function onchangeVlidateSaleQty(obj){
	if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "0";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        return;
    }
   	var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('myTable');
    if(obj.value!='0'){
    	tbl.rows[idx].cells[1].firstChild.checked=true;
    }else{
    	tbl.rows[idx].cells[1].firstChild.checked=false;
    }
}
//打开、收起配件按钮			  
function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if ($("dealerId").value == "" && $("isBatchSo").value == <%=Constant.PART_BASE_FLAG_NO%>) {
        MyAlert("请选择订货单位!");
        return;;
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
    var info = getItemValue(value);
    return "<input type='hidden' value='" + value + "' />" + info;
}
function seled(value, meta, record) {
    if (record.data.SALE_PRICE1 != null) {
        return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck' />";
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
    if (type == 2 && $('dealerId').value == "") {
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
    if (${isBatchSoFlag}&&document.getElementById("isBatchSo").value ==<%=Constant.PART_BASE_FLAG_YES%> && type == 5){
        type = 4;
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
    if (${isBatchSoFlag}) {
        if ($('isBatchSo').value ==<%=Constant.PART_BASE_FLAG_NO%>) {
            if ($("wh_id").value == "") {
                MyAlert("请选择库房");
                return;
            }
        }
    } else {
        if ($("wh_id").value == "") {
            MyAlert("请选择库房");
            return;
        }
    }
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            if (validateCell(partId)) {
                var partCode = mt.rows[i].cells[2].innerText;  //件号
                var partOldcode = mt.rows[i].cells[3].innerText;  //配件编码
                var partCname = mt.rows[i].cells[4].innerText;  //配件名称
                var unit = mt.rows[i].cells[5].innerText;  //单位
                
                var saleQty = mt.rows[i].cells[6].firstChild.value;  //最小包装量
                
                var itemQty = mt.rows[i].cells[7].innerText;  //当前库存
                var minPackage = mt.rows[i].cells[8].innerText;  //最小包装量
                var buyPrice = "";  //订购单价
                var upOrgStock = "";  //上级库存量
                var isLack = "";  //是否紧缺
                var isReplaced = "";  //是否可替代
                var isPlan = "";  //是否大件
                var isDirect = "";  //是否可替代
                if (${isBatchSoFlag}&&
                document.getElementById("isBatchSo").value ==
                <%=Constant.PART_BASE_FLAG_YES%>)
                {
                    isLack = mt.rows[i].cells[9].firstChild.value;  //是否紧缺
                    isReplaced = mt.rows[i].cells[10].firstChild.value;  //是否可替代
                    isPlan = mt.rows[i].cells[11].firstChild.value;  //是否大件
                    isDirect = mt.rows[i].cells[12].firstChild.value;  //是否可替代
                }
            else
                {
                    buyPrice = mt.rows[i].cells[9].innerText;  //订购单价
                    upOrgStock = mt.rows[i].cells[10].innerText;  //上级库存量
                    isLack = mt.rows[i].cells[11].firstChild.value;  //是否紧缺
                    isReplaced = mt.rows[i].cells[12].firstChild.value;  //是否可替代
                    isPlan = mt.rows[i].cells[13].firstChild.value;  //是否大件
                    isDirect = mt.rows[i].cells[14].firstChild.value;  //是否可替代
                }
                addCell(partId, partCode, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, isPlan, isDirect,saleQty);
            } else {
                MyAlert("第" + (i - 2) + "行配件：" + mt.rows[i].cells[4].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    countAll();
    if (${isBatchSoFlag}&&
    document.getElementById("isBatchSo").value !=
    <%=Constant.PART_BASE_FLAG_YES%>)
    {
        var whId = $("wh_id").value;
        var partId = "";
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            partId += "," + cb[i].value;
        }
        if (${isBatchSoFlag}&&
        document.getElementById("isBatchSo").value ==
        <%=Constant.PART_BASE_FLAG_YES%>)
        {
            getPartItemStock(whId, partId);
        }
    }
    
}

function addCell(partId, partCode, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, isPlan, isDirect,saleQty) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
    //获取折扣率
    var discount = document.getElementById("discount").value;
    var buyPrice1 = (parseFloat(unFormatNum(buyPrice)) * parseFloat(discount) * discount).toFixed(2);
    var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' />";
    var hiddenHtmlIsLack = "<input type='hidden' name='isLack_" + partId + "' id='isLack_" + partId + "' value='" + isLack + "' />";
    var hiddenHtmlIsReplaced = "<input type='hidden' name='isReplaced_" + partId + "' id='isReplaced_" + partId + "' value='" + isReplaced + "' />";
    var hiddenHTMLIsPlan = "<input type='hidden' name='isPlan_" + partId + "' id='isPlan_" + partId + "' value='" + isPlan + "' />";
    var hiddenHTMLIsDirect = "<input type='hidden' name='isDirect_" + partId + "' id='isDirect_" + partId + "' value='" + isDirect + "' />";
    var amount = 0;
    if(saleQty!=""){
    	amount = parseFloat(buyPrice1)*parseFloat(saleQty);
    }
    createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 2)), "cb", "", "", "checkbox", partId, true, false, "");
    createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 2), false, false, "");
    createCells(rowObj, 2, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
    createCells(rowObj, 3, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
    createCells(rowObj, 4, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
    createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
    createCells(rowObj, 6, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, hiddenHtmlIsReplaced);
    createCells(rowObj, 7, ("itemQty_" + partId), ("itemQty_" + partId), "", "", "block", itemQty, false, false, hiddenHTMLIsDirect);
    if (${isBatchSoFlag}&&
    document.getElementById("isBatchSo").value ==
    <%=Constant.PART_BASE_FLAG_YES%>)
    {

        createCells(rowObj, 8, ("buyQty_" + partId), ("buyQty_" + partId), "", "background-color:#FFFFCC;text-align:center", "text", saleQty, false, false, "");
        createCells(rowObj, 9, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, hiddenHtmlIsLack);
        createCells(rowObj, 10, "", "", "", "", "button", "", false, true, hiddenHtml);
    }
else
    {
        createCells(rowObj, 8, ("buyQty_" + partId), ("buyQty_" + partId), "countMoney(this," + buyPrice1 + "," + partId + ")", "background-color:#FFFFCC;text-align:center", "text", saleQty, false, false, "");
        createCells(rowObj, 9, ("buyPrice_" + partId), ("buyPrice_" + partId), "", "", "hidden", buyPrice, false, false, "");
        createCells(rowObj, 10, ("buyPrice1_" + partId), ("buyPrice1_" + partId), "", "", "hidden", buyPrice1, false, false, hiddenHTMLIsPlan);
        createCells(rowObj, 11, ("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", amount, false, false, "");
        createCells(rowObj, 12, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, hiddenHtmlIsLack);
        createCells(rowObj, 13, "", "", "", "", "button", "", false, true, hiddenHtml);
    }
    
}
//校验是否重复生成
function validateCell(value) {
    var flag = true;
    var tbl = document.getElementById('file');
    for (var i = 0; i <= tbl.rows.length - 1; i++) {
        if (value == tbl.rows[i].cells[0].firstChild.value) {
            flag = false;
            break;
        }
    }
    return flag;
}
function formatNum(str) {
    var len = str.length;
    var step = 3;
    var splitor = ",";
    var decPart = ".";
    if ((str+"").indexOf(".") > -1) {
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
    if ((str+"").indexOf(",") > -1) {
        str = str.replace(/\,/g, "");
    }
    return str;
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

//生成CELL
function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    var cell = obj.insertCell(idx);
    cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);

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
            if (!cb[i].checked) {
                flag = false;
            }
        }
    }
    document.getElementById("ckAll").checked = flag;
    document.getElementById("Amount").value = amountCount;
    
    getFreight(amountCount);
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
        inputHtml = '<input  type="text" class="short_txt"  id="' + id + '" value="'+value+'" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'block') {
        inputHtml = '<input  type="text" readonly style="border:0px;text-align:center" id="' + id + '" name="' + name + '" value="' + value + '" />';
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
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        cleanData(partId, idx);
        return;
    }
    if (parseFloat(document.getElementById("itemQty_" + partId)) < parseFloat(obj.value)) {
        MyAlert("销售数量不得小于库存!");
        obj.value = "";
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
    document.getElementById("Amount").value = amountCount;
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
}
function validateFm() {
    var msg = "";
    var flag = (${isBatchSoFlag} && document.getElementById("isBatchSo").value ==
    <%=Constant.PART_BASE_FLAG_YES%>)
    ;
    if (flag) {
        if (document.getElementById("dealerId1").value == "") {
            msg += "请选择订货单位!</br>";
        }
    } else {
        if (document.getElementById("dealerId").value == "") {
            msg += "请选择订货单位!</br>";
        }
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
    }

    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            if (!flag && document.getElementById("itemQty_" + cb[i].value).value == "" || document.getElementById("itemQty_" + cb[i].value).value == "0") {
                msg += "第" + (i + 1) + "行的配件的库存不足!</br>";
            } else {
                if (!flag && parseFloat(document.getElementById("itemQty_" + cb[i].value).value) < parseFloat(document.getElementById("buyQty_" + cb[i].value).value)) {
                    msg += "第" + (i + 1) + "行的配件的库存不足!</br>";
                }
            }
            //需要校验计划量是否填写
            if (document.getElementById("buyQty_" + cb[i].value).value == ""||document.getElementById("buyQty_" + cb[i].value).value == "0") {
                msg += "请填写第" + (i + 1) + "行的销售数量!</br>";
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
    if (cb.length <= 0) {
        msg += "请添加配件明细!</br>";
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
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return false;
    }
    return true;
}
//提报并保存订单确认
function repOrderConfirm() {
    if (!validateFm()) {
        return;
    }
    MyConfirm('确定保存订单并提报到财务?', repOrder, []);
}
//保存订单
function saveOrder() {
    disableAllClEl();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_01%>;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/saveOrder.json";
    sendAjax(url, getResult, 'fm');
}
//提报订单
function repOrder() {
    disableAllClEl();
    document.getElementById("state").value =<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>;
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
function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        var height = 200;
        if (info.split('</br>').length >= 6) {
            height = height + (info.split('</br>').length - 6) * 27;
        }
        if (info.split('</br>').length > 6) {   //如果有6个就会过长
            var infoR = "";
            var infoArr = info.split('</br>');
            for (var i = 0; i < 6; i++) {
                infoR += infoArr[i] + "</br>";
            }
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
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
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
function showUpload() {

    var uploadDiv = document.getElementById("uploadDiv");
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}
function uploadExcel() {
    if ($('dealerId').value == "") {
        MyAlert("请先选择销售单位!");
        return;
    }
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/uploadExcel.json";
    sendAjax(url, getUploadResult, 'fm');
}
function getUploadResult(jsonObj) {
    if (jsonObj != null) {
        var obj = jsonObj.partData;
        if (!obj) {
            MyAlert(jsonObj.error);
            return;

        }
        var error = obj.error;
        var msg = "";
        if (error != "") {
            msg += '<font color="RED">' + error + '</font>';
        }
        var data = obj.dataList;
        for (var i = 0; i < data.length; i++) {
            var tbl = document.getElementById('file');
            var flag = true;
            for (var j = 0; j < tbl.rows.length; j++) {
                if (data[i].PART_ID == tbl.rows[j].cells[0].firstChild.value) {
                    msg += "第" + (j) + "行配件：" + data[i].PART_CNAME + " 已存在!</br>";
                    flag = false;
                    break;
                }
            }
            if (flag) {
                addCell(data[i].PART_ID, data[i].PART_CODE, data[i].PART_OLDCODE, data[i].PART_CNAME, data[i].UNIT == null ? "" : data[i].UNIT, data[i].ITEM_QTY == null ? "" : data[i].ITEM_QTY, data[i].MIN_PACKAGE == null ? "" : data[i].MIN_PACKAGE, data[i].SALE_PRICE1 == null ? "" : data[i].SALE_PRICE1, data[i].UPORGSTOCK == null ? "" : data[i].UPORGSTOCK, data[i].IS_LACK == null ? "" : data[i].IS_LACK, data[i].IS_REPLACED == null ? "" : data[i].IS_REPLACED, data[i].IS_PLAN == null ? "" : data[i].IS_PLAN, data[i].IS_DIRECT == null ? "" : data[i].IS_DIRECT);
                document.getElementById("buyQty_" + data[i].PART_ID).value = data[i].buyQty;
                countMoney(document.getElementById("buyQty_" + data[i].PART_ID), data[i].SALE_PRICE1, data[i].PART_ID);
                countAll();
            }
        }
        if ("" != msg) {
            MyAlert(msg);
        }
    }
}
function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/exportExcelTemplate.do";
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
            if (inputArr[i].id == "rep" &&${isBatchSoFlag} && document.getElementById("isBatchSo").value ==<%=Constant.PART_BASE_FLAG_YES%>) {
                continue;
            }
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
    if ($('isBatchSo').value ==<%=Constant.PART_BASE_FLAG_NO%>) {
        if ($("wh_id").value == "") {
            MyAlert("请选择库房");
            return;
        }
    }
    var whId = $("wh_id").value;
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

function changeIsBatchSo(obj) {
    document.getElementById("dealerName").value = "";
    document.getElementById("dealerId").value = "";
    document.getElementById("dealerCode").value = "";
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var tbl = document.getElementById('file');
    //需要重置下面配件信息
    for (var i = (tbl.rows.length - 1); i >= 2; i--) {
        tbl.deleteRow(i);
    }

    if (obj.value ==<%=Constant.PART_BASE_FLAG_YES%>) {
        document.getElementById("rep").disabled = true;
        document.getElementById('tb2').style.display = "block";
        document.getElementById('tb1').style.display = "none";
        document.getElementById('tb1').disabled = true;
        document.getElementById('tb2').disabled = false;
        //设置配件信息
        columns = columns2;
        addPartViv.value = "增加";
        partDiv.style.display = "none";
        //设置增加配件信息
        if (tbl.rows[1].cells.length == 14) {
            tbl.rows[1].deleteCell(11);
            tbl.rows[1].deleteCell(10);
            tbl.rows[1].deleteCell(9);
        }

    } else {
        document.getElementById('tb1').style.display = "block";
        document.getElementById('tb2').style.display = "none";
        document.getElementById('tb1').disabled = false;
        document.getElementById('tb2').disabled = true;
        document.getElementById("rep").disabled = false;
        //设置配件信息
        columns = columns1;
        addPartViv.value = "增加";
        partDiv.style.display = "none";

        if (tbl.rows[1].cells.length < 14) {
            var cell1 = tbl.rows[1].cells[tbl.rows[1].cells.length - 1];
            var cell2 = tbl.rows[1].cells[tbl.rows[1].cells.length - 2];
            var cellInnerHtml1 = '<td align="center" width="4%" nowrap="nowrap">销售单价(折扣前)</td>';
            var cellInnerHtml2 = '<td align="center" width="4%" nowrap="nowrap">销售单价(折扣后)</td>';
            var cellInnerHtml3 = '<td align="center" width="6%">销售金额</td>';
            //先增加格子
            for (var i = (tbl.rows[1].cells.length); i < 14; i++) {
                tbl.rows[1].insertCell(i);
            }
            //挪动
            tbl.rows[1].cells[tbl.rows[1].cells.length - 1].innerHTML = cell1.innerHTML;
            tbl.rows[1].cells[tbl.rows[1].cells.length - 2].innerHTML = cell2.innerHTML;
            tbl.rows[1].cells[9].innerHTML = cellInnerHtml1;
            tbl.rows[1].cells[10].innerHTML = cellInnerHtml2;
            tbl.rows[1].cells[11].innerHTML = cellInnerHtml3;
        }
    }
}
function doInit() {
    var columns = columns1;
    changeIsBatchSo(<%=Constant.PART_BASE_FLAG_NO%>);
    $('transType').value = 1;
}
function initAccount(id,sum,dj,ky){
	if (id != "" && id != null && id != "null") {
        document.getElementById("DISCOUNTKYTd").innerHTML = "账户可用金额：";
        document.getElementById("DISCOUNTKYTextTd").innerHTML = '<input readonly class="phone_txt" type="text" style="background-color:#6F9" name="accountKy" value="' + ky + '"  id="accountKy" /><font color="RED">*</font>';
        document.getElementById("DISCOUNTSUMTd").innerHTML = "账户总金额:";
        document.getElementById("DISCOUNTSUMTextTd").innerHTML = '<input id="sum" name="sum" type="text" style="border:0px;background-color:#F3F4F8;" value="' + sum + '" class="normal_txt" readonly />';
    } else {
        document.getElementById("DISCOUNTKYTd").innerHTML = "";
        document.getElementById("DISCOUNTKYTextTd").innerHTML = "";
        document.getElementById("DISCOUNTSUMTd").innerHTML = "";
        document.getElementById("DISCOUNTSUMTextTd").innerHTML = "";
    }
	
	getDefaultAddr(id);
}
function getDefaultAddr(){
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/getDefault.json?id="+$('dealerId').value;
    sendAjax(url, getDefaultResult, 'fm');
}
function getDefaultResult(jsonObj){
	
	if (jsonObj != null) {
		var exceptions = jsonObj.Exception;
        if (exceptions) {
            MyAlert(exceptions.message);
            return;
        }
        
        var defaultData = jsonObj.defaultData;
        $('RCV_ORG').value= $('dealerName').value;
        $('RCV_CODE').value= $('dealerCode').value;
        $('RCV_ORGID').value= $('dealerId').value;
        
        $('ADDR_ID').value= defaultData.ADDR_ID;
        $('ADDR').value= defaultData.ADDR;
        $('RECEIVER').value= defaultData.LINKMAN;
        $('TEL').value= defaultData.TEL;
        $('POST_CODE').value= defaultData.POST_CODE;
        $('STATION').value= defaultData.STATION;
	}
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
        if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>||codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>) {
	        if (codeData[i].type == type && flag) {
	            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
	        }
        }
    }
    str += "</select>";
    document.write(str);
}

function getFreight(amountCount){
	
	disableAllBtn();
	var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money="+amountCount;
	sendAjax(getFreightUrl,getFreightResult,'fm');
}
function getFreightResult(jsonObj){
	enableAllBtn();
	if(jsonObj!=null){
		$('freight').value=jsonObj.freight;
		$('Amount').value =formatNum((parseFloat(unFormatNum($('Amount').value))+ parseFloat(unFormatNum(jsonObj.freight))).toFixed(2));
		
	}
}
</script>
</head>
<body onload="loadcalendar();enableAllClEl();doInit()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="soCode" id="soCode" type="hidden" value="${dataMap.soCode}"/>
<input name="accountId" id="accountId" type="hidden"/>
<input name="state" id="state" type="hidden" value=""/>

<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:

    配件管理 > 配件销售管理 &gt;配件订单资源审核>服务商订单审核生成总部销售单
</div>
<table border="0" class="table_query">
    <tr>
        <td width="10%"   align="right">
            <c:if test="${isBatchSoFlag}">
                是否铺货:
            </c:if>
        </td>
        <td width="20%">
            <c:if test="${isBatchSoFlag}">
                <script type="text/javascript">
                    genSelBoxExp("isBatchSo", <%=Constant.PART_BASE_FLAG%>, <%=Constant.PART_BASE_FLAG_NO%>, false, "short_sel", 'onchange="changeIsBatchSo(this)"', "false", '');
                </script>
            </c:if>
        </td>
        <td width="10%"   align="right"></td>
        <td width="20%"></td>
        <td width="10%"   align="right"></td>
        <td width="20%"></td>
    </tr>
</table>
<table id="tb1" class="table_query">
    <tr>
        <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 配件销售信息</th>
    </tr>
    <tr>
        <td width="13%"   align="right">销售日期:</td>
        <td width="20%">${dataMap.now}</td>
        <td width="13%"   align="right">制单人:</td>
        <td width="21%">${dataMap.name}</td>
        <td width="13%"   align="right">


        </td>
        <td width="20%">

        </td>
    </tr>
    <tr>
        <td   align="right">订货单位:</td>
        <td><input name="dealerName" class="SearchInput" id="dealerName" value="" type="text" size="20"
                   readonly="readonly"/>
            <input name="dealerCode" id="dealerCode" value="" type="hidden"/>
            <input name="dealerId" id="dealerId" value="" type="hidden"/>
            <input name='dlbtn1' id='dlbtn1' class='mini_btn' type='button' value='...'
                   onclick="selSales('dealerId','dealerCode','dealerName','','','','',${dataMap.dealerId},'5')"/>
        </td>
        <td   align="right">出库仓库:</td>
        <td>
            <select name="wh_id" id="wh_id" style='width:173px;' onchange="getPartQty()">
               
                <c:forEach items="${wareHouseList}" var="wareHouse">
                    <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                </c:forEach>
            </select>
            <font color="RED">*</font></td>
        <td   align="right">接收单位:</td>
        <td><input name="RCV_ORG" class="SearchInput" id="RCV_ORG" type="text" size="20" readonly="readonly"/>
            <input name="RCV_CODE" class="SearchInput" id="RCV_CODE" type="hidden" size="20" readonly="readonly"/>
            <input name="RCV_ORGID" class="SearchInput" id="RCV_ORGID" type="hidden" size="20" readonly="readonly"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                   onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',$('dealerId').value,'2')"/>
            </span></td>
    </tr>
    <tr>
        <td   align="right">接收地址:</td>
        <td><input name="ADDR" class="SearchInput" id="ADDR" type="text" size="20" readonly="readonly"/>
            <input name="ADDR_ID" class="SearchInput" id="ADDR_ID" type="hidden" size="20" readonly="readonly"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                   onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('RCV_ORGID').value,'3')"/>
            </span></td>
        <td   align="right">接收人：</td>
        <td><input id="RECEIVER" name="RECEIVER" type="text" class="normal_txt"/>
            <font color="RED">*</font></td>
        <td   align="right"><span   align="right">接收人电话：</span></td>
        <td><input id="TEL" name="TEL" type="text" class="normal_txt" readonly/>
            <font color="RED">*</font></td>
    </tr>
    <tr>
        <td   align="right">邮政编码：</td>
        <td><input id="POST_CODE" name="POST_CODE" type="text" class="normal_txt" readonly/>
            <font color="RED">*</font></td>
        <td   align="right">到站名称：</td>
        <td><input id="STATION" name="STATION" type="text" class="normal_txt" readonly/>
            <font color="RED">*</font></td>
        <td   align="right">发运方式：</td>
        <td>
            <select name="transType" id="transType" class="short_sel">
                <option value="">-请选择-</option>
                <c:if test="${transList!=null}">
                    <c:forEach items="${transList}" var="list">
                        <option value="${list.fixValue }">${list.fixName }</option>
                    </c:forEach>
                </c:if>
            </select>
            <font color="RED">*</font>
        </td>
    </tr>
    <tr>
        <td   align="right">运费支付方式:</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("transpayType", <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS%>, <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_02%>, true, "short_sel", "", "false", '');
            </script>

        </td>
        <td   align="right">付款方式:</td>
        <td>
            <script type="text/javascript">
                genSelBoxExp("payType", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, <%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>, true, "short_sel", "", "false", '');
            </script>
            <font color="RED">*</font></td>
        <td width="109"   align="right">订单类型：</td>
        <td width="260">
            <script type="text/javascript">
            	genMySelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>, true, "short_sel", "", "false", '');
            </script>
            <font color="RED">*</font></td>

    </tr>
    <tr>
        <td id="DISCOUNTNameTd"   align="right">折扣率：</td>
        <td id="DISCOUNTTextTd"><input readonly class="phone_txt" type="text"
                                       style="border:0px;background-color:#F3F4F8;" value="${dataMap.discount}"
                                       name="discount" id="discount"/></td>
        <td id="DISCOUNTKYTd"   align="right"></td>
        <td id="DISCOUNTKYTextTd">

        </td>
        <td id="DISCOUNTSUMTd"   align="right"></td>
        <td id="DISCOUNTSUMTextTd"></td>
    </tr>
	<tr>
      <td id="AmountNameTd" class="table_add_3Col_label_6Letter">订货总金额：</td>
      <td id="AmountTextTd" ><input id="Amount" name="Amount" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.ORDER_AMOUNT} "  class="normal_txt" readonly /><font color="blue">元</font></td>
      <td class="table_add_3Col_label_6Letter"><span class="table_add_3Col_label_6Letter">运费：</span></td>
      <td><input id="freight" name="freight" type="text" class="short_txt" value="0" style="background-color: #ffff80" readonly /></td>
    </tr>
</table>
<table class="table_query" id="tb2" style="display:none">
    <tr>
        <th colspan="6"><img src="<%=contextPath%>/img/nav.gif"/>铺货信息</th>
    </tr>

    <TR id="tr1" style="display:">
        <td width="98"   align="right">铺货日期:</td>
        <td width="315">${dataMap.now}</td>
        <td width="105"   align="right">制单人:</td>
        <td width="420">${dataMap.name}</td>
        <td width="100"   align="right"></td>
        <td width="223"></td>
    </tr>
    <tr>
        <td   align="right">订货单位:</td>
        <td colspan="5"><input name="dealerName1" class="maxlong_txt" id="dealerName1" value="" type="text" size="20"
                               readonly="readonly"/>
            <input name="dealerCode1" id="dealerCode1" value="" type="hidden"/>
            <input name="dealerId1" id="dealerId1" value="" type="hidden"/>
            <input name='dlbtn1' id='dlbtn1' class='mini_btn' type='button' value='...'
                   onclick="selSales('dealerId1','dealerCode1','dealerName1','','','','',${dataMap.dealerId},'5')"/>
            <font color="RED">*</font>
        </td>
    </tr>
    <tr>
        <td>备注:</td>
        <td colspan="6">
            <textarea name="textarea1" id="textarea1" cols="80" rows="4"></textarea>
        </td>
    </tr>
</table>
<FIELDSET>
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
                <td   align="right" width="13%">
                    件号：
                </td>
                <td width="20%" align="left">
                    &nbsp;
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td   align="right" width="13%">
                    配件编码：
                </td>
                <td align="left" width="20%">
                    &nbsp;
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td   align="right" width="13%">
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
</FIELDSET>
<table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
        <th colspan="15" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span class="checked"
                                                                                                         style="text-align:left">
      </span></th>
    </tr>
    <tr bgcolor="#FFFFCC">
        <td align="center" width="2%"><input type="checkbox" checked name="ckAll" id="ckAll" onclick="selectAll()"/>
        </td>
        <td align="center" width="4%">序号</td>
        <td align="center" width="10%">件号</td>
        <td align="center" width="12%">配件编码
            <font color="RED">*</font></td>
        <td align="center" width="11%">配件名称</td>
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

</table>

<table border="0" class="table_query">
    <tr align="center">
        <td>
            <input class="cssbutton" type="button" value="上传文件" name="button1"
                   onclick="showUpload();"> &nbsp;
            <input class="cssbutton" type="button" value="保存" name="button1" onclick="saveOrderConfirm();">
            &nbsp;
            <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
            &nbsp;
            <input class="normal_btn" type="button" id="rep" name="rep" style="width:65px" value="提交到财务"
                   onclick="repOrderConfirm();"/></td>
    </tr>
</table>
<div style="display:none ; heigeht: 5px" id="uploadDiv">

    <tr>
        <td><font color="red">
            <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
            文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
            <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                   value=""/>
            &nbsp;
            <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
    </tr>

</div>
</div>
</form>
</body>
</html>
