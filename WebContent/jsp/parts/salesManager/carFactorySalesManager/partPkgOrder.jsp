<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<title>配件装箱</title>
<style type="text/css">
html, body {
    font-size: 12px;
    margin: 0px;
    height: 100%;
}
.mesWindow {
    border: #666 1px solid;
    background: #fff;
}
.mesWindowTop {
    border-bottom: #eee 1px solid;
    margin-left: 4px;
    padding: 3px;
    font-weight: bold;
    text-align: left;
    font-size: 12px;
}
.mesWindowContent {
    margin: 4px;
    font-size: 12px;
}
.mesWindow .close {
    height: 15px;
    width: 28px;
    border: none;
    cursor: pointer;
    text-decoration: underline;
    background: #fff
}
.package-size input {
    width: 40px;
    min-width: 40px;
    margin-left: 2px;
}
fieldset.form-fieldset {
    margin-bottom: 10px
}
table.table_query {margin-bottom: 10px;background-color: transparent}
</style>
</head>
<script language="javascript">
var temp = "";
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/queryUnPkgedPartInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
        dataIndex: 'PART_ID',
        align: 'center',
        width: '33px',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "最小装箱量", dataIndex: 'MIN_PKG', align: 'center'},
    {header: "当前库存", dataIndex: 'NORMAL_QTY_NOW', align: 'center'},
    {header: "出库货位", dataIndex: 'LOC_NAME', align: 'center'},
    {header: "出库批次", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "销售数量", dataIndex: 'SALES_QTY', align: 'center'},
    {header: "已装箱数量", dataIndex: 'PKGEDQTY', align: 'center'},
    {header: "待装箱数量", dataIndex: 'SPKG_QTY', align: 'center', renderer: insertSPkgQtyInput},
    {header: "装箱数量", dataIndex: 'PKG_QTY', align: 'center', renderer: insertPkgQtyInput},
    {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: insertRemarkInput}
];

function seled(value, meta, record) {
    var locId = record.data.LOC_ID;
    var batchNo = record.data.BATCH_NO;
    var ckValue = value + "," + locId+","+batchNo;
    return "<input type='checkbox' value='" + ckValue + "' name='ck' id='ck" + ckValue + "' checked onclick='chkPart()'/>";
}
function insertSPkgQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var newId = partId + "," + locId;
    var salQty = record.data.SALES_QTY;
    var pkgedQty = record.data.PKGEDQTY;
    var sPkgQty = salQty - pkgedQty;
    var output;
    output = '<input type="hidden"  id="PKGEDQTY' + newId + '" name="PKGEDQTY' + newId + '" value="' + sPkgQty + '"/>' + sPkgQty;
    return output;
}

function insertPkgQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var batchNo = record.data.BATCH_NO;
    var newId = "" + partId + "," + locId + "";
    var salQty = record.data.SALES_QTY;
    var pkgedQty = record.data.PKGEDQTY;
    var sPkgQty = salQty - pkgedQty;
    var output;
    output = '<input type="text" class="short_txt" onchange="check(this,' + partId + ',' + locId+ ',' + batchNo  + ',' + sPkgQty + ');"  id="PKG_QTY' + newId + '" name="PKG_QTY' + newId + '" value="' + sPkgQty + '" onkeyup="insertline(' + partId + ',' + locId + ');"/>';
    return output;
}

function insertRemarkInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var newId = partId + "," + locId;
    var output = '<input type="text" class="middle_txt" id="REMARK1' + newId + '" name="REMARK1' + newId + '" value="' + value + '"/>\n';
    return output;
}

function check(value, partId, locId,batchNo, sPkgQty) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (value != "" && value != "undefined" && !pattern1.exec($(value)[0].value)) {
        MyAlert("装箱数量只能输入正整数!");
        $(value)[0].value = $(value)[0].value.replace(/\D/g, '');
        $(value)[0].focus();
    }

    var pkgQty = $(value)[0].value;
    if (parseInt(pkgQty) > parseInt(sPkgQty)) {
        MyAlert("装箱数量不能大于待装箱数量!");
        /* $(value)[0].value = "";  */
        $(value)[0].focus();
        return;
    }
    $("#ck" + partId + "," + locId+ "," + batchNo)[0].checked = true;
    chkPart();
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

function chkPart1() {
    var cks = document.getElementsByName('cb');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("allChk").checked = flag;
}

function selAll2(obj) {
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (obj.checked) {
            cb[i].checked = true;
        } else {
            cb[i].checked = false;
        }
    }
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


    function addCells() {
        var ck = document.getElementsByName('ck');
        var mt = document.getElementById("myTable");
        var cn = 0;
        for (var i = 1; i < mt.rows.length; i++) {
            if (mt.rows[i].cells[1].childNodes[0].checked) {
                cn++;
                var pQty = mt.rows[i].cells[13].childNodes[0].value;
                var pattern1 = /^[1-9][0-9]*$/;
                if (!pattern1.exec(pQty)) {
                    MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + "的装箱数量必须是正整数!</r>");
                    break;
                }
                var partId = mt.rows[i].cells[1].childNodes[0].value;  //ID
                if (validateCell(partId)) {
                    var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                    var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                    var partCode = mt.rows[i].cells[4].innerText;  //件号
                    var unit = mt.rows[i].cells[5].innerText;  //单位
                    var locName = mt.rows[i].cells[8].innerText;  //出库货位
                    var batchNo = mt.rows[i].cells[9].innerText;  //出库批次
                    var salQty = mt.rows[i].cells[10].innerText;  //销售数量
                    var pkgedQty = mt.rows[i].cells[11].innerText;  //已装箱数量
                    var spKgQty = mt.rows[i].cells[12].innerText;  //待装箱数量
                    var pkgQty = mt.rows[i].cells[13].childNodes[0].value;  //装箱数量
                    var remark = mt.rows[i].cells[14].childNodes[0].value;  //备注

                    if (parseInt(pkgQty) > parseInt(spKgQty)) {
                        MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + "装箱数量不能大于待装箱数量!</br>");
                        mt.rows[i].cells[1].firstChild.checked = false;
                        return;
                    }
                    addCell(partId, partOldcode, partCname, partCode, unit, locName,batchNo, salQty, pkgedQty, spKgQty, pkgQty, remark);
                } else {
                    MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText+" 批次:"+mt.rows[i].cells[9].innerText+" 货位:"+mt.rows[i].cells[8].innerText + " 已存在!");
                    break;
                }
            }
        }
        if (cn == 0) {
            MyAlert("请选择要添加的配件!");
        }
    }

function addCell(partId, partOldcode, partCname, partCode, unit, locName,batchNo, salQty, pkgedQty, spKgQty, pkgQty, remark) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
    var cell1 = rowObj.insertCell(0);
    var cell2 = rowObj.insertCell(1);
    var cell3 = rowObj.insertCell(2);
    var cell4 = rowObj.insertCell(3);
    var cell5 = rowObj.insertCell(4);
    var cell6 = rowObj.insertCell(5);
    var cell7 = rowObj.insertCell(6);
    var cell8 = rowObj.insertCell(7);
    var cell9 = rowObj.insertCell(8);
    var cell10 = rowObj.insertCell(9);
    var cell11 = rowObj.insertCell(10);
    var cell12 = rowObj.insertCell(11);
    var cell13 = rowObj.insertCell(12);
    var cell14 = rowObj.insertCell(13);
    var cell15 = rowObj.insertCell(14);

    var str = genMySelBoxExp("pkgType_" + partId, <%=Constant.CAR_FACTORY_PKG_TYPE%>, <%=Constant.CAR_FACTORY_PKG_TYPE_01%>, true, "u-select", "style='width:80px'", "false", '');

    cell1.innerHTML = '<tr><td class="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td class="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 1) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left"><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell6.innerHTML = '<td style="text-align: left"><input   name="UNIT' + partId + '" id="UNIT' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell7.innerHTML = '<td style="text-align: left"><input   name="LOC_NAME' + partId + '" id="LOC_NAME' + partId + '" value="' + locName + '" type="hidden" />' + locName + '</td>';
    cell8.innerHTML = '<td style="text-align: left"><input   name="BATCHNO_' + partId + '" id="BATCHNO_' + partId + '" value="' + batchNo + '" type="hidden" />' + batchNo + '</td>';
    cell9.innerHTML = '<td style="text-align: left">' + str + '</td>';
    cell10.innerHTML = '<td class="center"><input   name="salQty_' + partId + '" id="salQty_' + partId + '" value="' + salQty + '" type="hidden" />' + salQty + '</td>';
    cell11.innerHTML = '<td class="center">' + pkgedQty + '</td>';
    cell12.innerHTML = '<td class="center"><input   name="sPkgQty_' + partId + '" id="sPkgQty_' + partId + '" value="' + spKgQty + '" type="hidden" />' + spKgQty + '</td>';
    cell13.innerHTML = '<td class="center"><input   name="pkgQty_' + partId + '" id="pkgQty_' + partId + '" value="' + pkgQty + '" type="text" style="text-align:center" onchange="check(this,' + partId + ',' + spKgQty + ');" class="middle_txt"/></td>';
    cell14.innerHTML = '<td class="center" nowrap><input class="middle_txt"  name="remark_' + partId + '" id="remark_' + partId + '" value="' + remark + '" type="text"/></td>';
    cell15.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

}

function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    refreshMtTable('orderLine', 'SEQ');//刷新行号
}
//刷新行号
function refreshMtTable(mtId, strType) {
    if (strType == "SEQ") {
        var oSeq = eval("document.all." + mtId + "_SEQ");
        if (oSeq != null && oSeq.length != null) {
            for (var i = 0; i < oSeq.length; i++) {
                oSeq[i].innerText = (i + 1);
            }
        }
    }
}

function validateCell(value) {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        var val = cb[i].value;
        if (value == val) {
            flag = false;
            break;
        }
    }
    return flag;
}

//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
//获取序号
function getIdx() {
    document.write(document.getElementById("file").rows.length - 2);
}
//savePkg、finishPkg
function savePkgConfirm(code) {
	//判断cb选中数量
	var len=0;
	var cbs = document.getElementsByName("cb");
    for (var i = 0; i < cbs.length; i++) {
        if(cbs[i].checked)
        	len++;
    }
	if(len>0 && code=='finishPkg'){
		MyAlert('您选择的配件，请先点击<font color="red">装箱</font>，再点击<font color="red">装箱完成</font>！');
		return;
	}
	
    var msg = "";
    var hasDtl = $("#hasDtl")[0].value;
    if (code == "pkg") {
        if (hasDtl == "1") {
            if (!$("#PKG_NO")[0].value) {
                MyAlert("请输入装箱号!");
                return;
            }
            if(isNaN($("#PKG_NO")[0].value)){
            	MyAlert("装箱号只能是数字!");
                return;
            }
            if (!$("#BOX_LEN")[0].value) {
	             MyAlert("请输入包装尺寸的长!");
	             return;
            }
             if (!$("#BOX_WID")[0].value) {
	             MyAlert("请输入包装尺寸的宽!");
	             return;
             }
             if (!$("#BOX_HEI")[0].value) {
	             MyAlert("请输入包装尺寸的高!");
	             return;
             }
             if (!$("#BOX_WEI")[0].value) {
	             MyAlert("请输入单箱重量!");
	             return;
             }
        }
        if (!validateData()) {
            return;
        }
        document.getElementById("status").value = "1";
        document.getElementById("code").value = "";
        msg = "确定装箱?";
    } else if (code == "finishPkg") {
        document.getElementById("status").value = "2";
        document.getElementById("code").value = "jump";
        msg += "</br>确定要完成装箱?";
        if (hasDtl == "1") {
            msg = "<font color='red' >未完全装箱的配件将会在出库时产生现场bo！<font>" + msg;
        }
    }
    MyConfirm(msg, savePkg, []);
}

function savePkg() {
    disabledCb();
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/savePkg.json?";
    makeNomalFormCall(url, getResult, 'fm');
}
function validateBo() {
    var tb = document.getElementById("file");
    for (var i = 2; i < tb.rows.length; i++) {
        var salesQty = tb.rows[i].cells[8].lastChild.value;
        var pkgedQty = tb.rows[i].cells[11].lastChild.value;
        var pkgQty = tb.rows[i].cells[12].firstChild.value;
        if (pkgQty == "") {
            pkgQty = 0;
        }
        if (parseFloat(salesQty) > (parseFloat(pkgedQty) + parseFloat(pkgQty))) {
            return false;
        }
    }
    return true;
}
    function getResult(jsonObj) {
        enableAllClEl();
        unDisabledCb();
        var soId = document.getElementById("pickOrderId").value;
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            var pkgIds = jsonObj.pkgIds;
            if (success) {
                MyAlert(success);
                /* if (document.getElementById("code").value == "jump") {
                    var pkgIds = jsonObj.pkgIds;
                    if (pkgIds != "") {
//                    openDetailPrintOrder(pkgIds);
                    }
                    pageAction();
                    return;
                }
                if ("" == pkgIds) {
                    return;
                }
                 if(confirm("打印去向头?")){
                 openDetailPrintOrder(pkgIds);
                 } */
                pageAction();
            } else if (error) {
                unDisabledCb();
                MyAlert(error);
            } else if (exceptions) {
                unDisabledCb();
                MyAlert(exceptions.message);
            }
        }
    }
function pageAction() {
    var pickOrderId = document.getElementById("pickOrderId").value;
    if (document.getElementById("code").value == "jump") {
        window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=trues';
    } else {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do?pickOrderId=" + pickOrderId + "&flag=true";
    }
}
function openDetailPrintOrder(pkgIds) {
    var pickOrderId = document.getElementById("pickOrderId").value;
    window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/printPkg.do?pkgIds=" + pkgIds + "&pickOrderId=" + pickOrderId, "", 'edge: Raised; center: Yes; help: Yes; resizable: Yes; status: No;dialogHeight:540px;dialogWidth:850px');
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do?pickOrderId=" + pickOrderId + "&flag=true";
}

    function validateData() {
        var cbs = document.getElementsByName("cb");
        var msg = "";
        var flag = false;
        for (var i = 0; i < cbs.length; i++) {
            if (cbs[i].checked) {
                flag = true;
                var pkgQty = document.getElementById(("pkgQty_" + cbs[i].value)).value;
                //if(pkgQty!=""&&pkgQty!="0"){
                if (pkgQty != "") {
                    var re = /^[1-9][0-9]*$/;
                    if (!re.test(pkgQty)) {
                        msg += "第" + (i + 1) + "行的<font color='RED'>装箱数量只能输入正整数</font>!</br>";
                    }
                    var salesQty = document.getElementById(("salQty_" + cbs[i].value)).value;
                    var sPkgQty = document.getElementById(("sPkgQty_" + cbs[i].value)).value
                    if (salesQty == "") {
                        msg += "第" + (i + 1) + "行的<font color='RED'>销售数量</font>错误!</br>";
                    } else {
                        if (parseInt(sPkgQty) < parseInt(pkgQty)) {
                            msg += "第" + (i + 1) + "行的<font color='RED'>装箱数量</font>不能大于<font color='RED'>待装箱数量</font>!</br>";
                        }
                    }
                } else {
                    msg += "请填写第" + (i + 1) + "行的<font color='RED'>装箱数量</font>!</br>";
                }
                if (document.getElementById(("pkgType_" + cbs[i].value)).value == "" || document.getElementById(("pkgType_" + cbs[i].value)).value == "0") {
                    msg += "请填写第" + (i + 1) + "行的<font color='RED'>包装方式</font>!</br>";
                }
            }
        }
        if (!flag) {
            msg += "请选择要装箱的配件!</br>";
        }
        if (msg != "") {
            MyAlert(msg);
            return false;
        }
        return true;
    }
function disabledCb() {
    var cbs = document.getElementsByName("cb");
    var disabledSlineId = "";
    for (var i = 0; i < cbs.length; i++) {
        cbs[i].disabled = (!cbs[i].checked);
        disabledSlineId += "," + cbs[i].value;
    }
    //将未选择的传后台生成BO使用
    document.getElementById("disabledSlineId").value = disabledSlineId;
}
function unDisabledCb() {
    var cbs = document.getElementsByName("cb");
    for (var i = 0; i < cbs.length; i++) {
        cbs[i].disabled = false;
    }
}
//返回
function goBack() {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=true";
}
function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }

}
function clickCheckBox() {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
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
/* function changePkgQty(obj) {
    var re = /^\d+$/;
    var tbl = $('#file')[0];
    var idx = obj.parentElement.parentElement.rowIndex;
    var needPkgQty = tbl.rows[idx].cells[13].innerText;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = needPkgQty;
    }
    if (parseFloat(obj.value) > parseFloat(needPkgQty)) {
    	MyAlert("装箱数量不能大于待装箱数量!");
        obj.value = needPkgQty;
    }
    countAllPkg();
} */
function countAllPkg() {
    var cb = document.getElementsByName('cb');
    var tbl = $('#file')[0];
    for (var i = 0; i < cb.length; i++) {
        var idx = cb[i].parentElement.parentElement.rowIndex;
        var minPkg = tbl.rows[idx].cells[6].innerText;
        var boxQty = $('#boxQty_' + cb[i].value)[0];
        var pkgQty = $('#pkgQty_' + cb[i].value)[0].value;
        if (minPkg.trim() != "" && pkgQty.trim() != "") {
            boxQty.value = Math.floor(pkgQty / minPkg);
        } else {
            boxQty.value = "1";//1;
        }

    }
}

function checkSize(obj) {
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("包装尺寸无效,请重新输入!");
        obj.value = "";
        $("#VOLUME")[0].value = "";
        $("#CH_WEIGHT")[0].value = "";
        $("#EQ_WEIGHT")[0].value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("包装尺寸整数部分不能超过8位,且保留精度最大为2位!");
                obj.value = "";
                $("#VOLUME")[0].value = "";
                $("#CH_WEIGHT")[0].value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,8}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("包装尺寸整数部分不能超过8位!");
                obj.value = "";
                $("#VOLUME")[0].value = "";
                $("#CH_WEIGHT")[0].value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        }
    }

    var box_len = $("#BOX_LEN")[0].value;
    var box_wid = $("#BOX_WID")[0].value;
    var box_hei = $("#BOX_HEI")[0].value;
    //体积（VOLUME） = 长*宽*高/1000000
    //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
    //单箱重量(BOX_WEI)
    //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
    if (box_len && box_wid && box_hei) {
        $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
        var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
        $("#EQ_WEIGHT")[0].value = eqWeght;
        var box_weight = $("#BOX_WEI")[0].value;//单箱重量
        if (box_weight) {
            calWeight(box_len, box_wid, box_hei, box_weight, eqWeght);
        }
    }
}

function checkWeight(obj) {
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("单箱重量无效,请重新输入!");
        obj.value = "";
        $("#EQ_WEIGHT")[0].value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("单箱重量整数部分不能超过8位,且保留精度最大为2位!");
                obj.value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,8}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("单箱重量整数部分不能超过8位!");
                obj.value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        }
    }

    var box_len = $("#BOX_LEN")[0].value;
    var box_wid = $("#BOX_WID")[0].value;
    var box_hei = $("#BOX_HEI")[0].value;
    //体积（VOLUME） = 长*宽*高/1000000
    //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
    //单箱重量(BOX_WEI)
    //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
    if (box_len && box_wid && box_hei) {
        $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
        var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
        $("#EQ_WEIGHT")[0].value = eqWeght;
        calWeight(box_len, box_wid, box_hei, $("#BOX_WEI")[0].value, eqWeght);
    }

}
/**
 * 计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边有一边大于60CM 取折合重量和重量取最大值。三边有都小于60CM，取单箱重量。
 * @param c 长
 * @param k 宽
 * @param g 高
 * @param dxzl  单箱重量
 * @param zhzl  折合重量
 */
function calWeight(c, k, g, dxzl, zhzl) {
    //MyAlert(c + ":" + k + "：" + g + ":" + dxzl + ":" + zhzl);
    if (c >= 60 || k >= 60 || g > 60) {
        if (parseFloat(dxzl) >= parseFloat(zhzl)) {
            $("#CH_WEIGHT")[0].value = dxzl;
        } else {
            $("#CH_WEIGHT")[0].value = zhzl;
        }
    }
    if (c < 60 && k < 60 && g < 60) {
        $("#CH_WEIGHT")[0].value = dxzl;
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
    str += " onChange=doCusChange(this.value);> ";
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
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";
    return str;
}

function queryBoxInfo(obj) {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/queryBoxInfo.json?pkgNo=" + obj.value;
    makeNomalFormCall(url, getBoxResult, 'fm');
}

function queryPartInfo() {
    disabledCb();
    disableAllClEl();
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do";
    fm.submit();
}

function getBoxResult(jsonObj) {
    if (jsonObj) {
        if (jsonObj.isOpen == "1") {
            if (!jsonObj.flag) {
                MyAlert("箱号：<font style='color: red'>" + $("#PKG_NO")[0].value + '</font>不属于当前服务商!');
                clrPkg("1");
                return;
            }
        }
        if (jsonObj.pkgNo) {
            if (jsonObj.outId) {
                MyAlert("箱号：<font style='color: red'>" + $("#PKG_NO")[0].value + '</font>已经出库不能重复使用!');
                clrPkg("1");
                return;
            } else {
                if (jsonObj.pickOrderId !=${mainMap.PICK_ORDER_ID}) {
                    MyAlert("输入的箱号：<font style='color: red'>" + $("#PKG_NO")[0].value + "</font>属于另一个拣货单<font style='color: red'>" + jsonObj.pickOrderId + '</font>!');
                } else {
                    MyAlert("输入的箱号：<font style='color: red'>" + $("#PKG_NO")[0].value + "</font>在本装箱单中<font style='color: red'>" + jsonObj.pickOrderId + '</font>已使用!');
                }
				$("#BOX_LEN")[0].value = jsonObj.boxLen;
                $("#BOX_WID")[0].value = jsonObj.boxWid;
                $("#BOX_HEI")[0].value = jsonObj.boxHei;
                $("#VOLUME")[0].value = jsonObj.boxVol;
                $("#BOX_WEI")[0].value = jsonObj.boxWei;
                $("#CH_WEIGHT")[0].value = jsonObj.boxCHWei;
                $("#EQ_WEIGHT")[0].value = jsonObj.boxEQWei;
          }
        } else {
            clrPkg("2");
        }
    }
}
function clrPkg(flag) {
    //1:已出库清理箱号。2：未出库不清理箱号
    if (flag == "1") {
        $("#PKG_NO").value = "";
    }
    $("#BOX_LEN")[0].value = "";
    $("#BOX_WID")[0].value = "";
    $("#BOX_HEI")[0].value = "";
    $("#VOLUME")[0].value = "";
    $("#BOX_WEI")[0].value = "";
    $("#CH_WEIGHT")[0].value = "";
    $("#EQ_WEIGHT")[0].value = "";
    $("#PICK_ORDER_ID")[0].value = "";

    $("#BOX_LEN")[0].readOnly = false;
    $("#BOX_WID")[0].readOnly = false;
    $("#BOX_HEI")[0].readOnly = false;
    $("#VOLUME")[0].readOnly = false;
    $("#BOX_WEI")[0].readOnly = false;
    $("#CH_WEIGHT")[0].readOnly = false;
    $("#EQ_WEIGHT")[0].readOnly = false;
}
function viewPkgDtl() {
    OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlInit.do?pickOrderID=" +${mainMap.PICK_ORDER_ID}, 600, 400);
}

function checkPkgNo() {
    var iKeyCode = window.event.keyCode;
    if (iKeyCode == 13) {
        var pkgNo = $("#PKG_NO")[0];
        queryBoxInfo(pkgNo);
    }
}

function scan() {
    var iKeyCode = window.event.keyCode;
    var partCode = document.getElementById("PART_OLDCODE");
    if (iKeyCode == 40) {
        addCells();
        partCode.value = "";
        partCode.focus();
        partCode.select();
    }
    if (iKeyCode == 37) {
        partCode.value = "";
    }

    /*  var ck = document.getElementsByName("ck");
     if (ck.length > 1){
     if(isNav){
     document.getElementById("PKG_QTY"+ck[1].value).focus();
     }else{
     setFocus.call(document.getElementById("PKG_QTY"+ck[1].value))
     }
     }*/
}
function insertline(partId, locId) {
    $("ck" + partId + "," + locId).checked = true;
    var iKeyCode = window.event.keyCode;
    var partCode = document.getElementById("PART_OLDCODE");
    if (iKeyCode == 40) {
        addCells();
        partCode.value = "";
        partCode.focus();
        partCode.select();
    }
}
function viewDlrPkgNoDtlxx() {
    OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/dlrPKginit.do?dealerId=' +${mainMap.DEALER_ID}, 800, 500);
}
</script>

</head>
<body onload="countAllPkg();enableAllClEl()">
<div class="wbox">  
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="pickOrderId" id="pickOrderId" value="${mainMap.PICK_ORDER_ID}"/>
        <input type="hidden" name="dealerId" id="dealerId" value="${mainMap.DEALER_ID}"/>
        <input type="hidden" name="PICK_ORDER_ID" id="PICK_ORDER_ID"/>
        <input type="hidden" name="code" id="code"/>
        <input type="hidden" name="status" id="status"/>
        <input type="hidden" name="whId" id="whId" value="${mainMap.WH_ID}"/>
        <input type="hidden" name="disabledSlineId" id="disabledSlineId"/>
        <input type="hidden" name="hasDtl" id="hasDtl" value="${hasDtl }"/>
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件销售管理 &gt;装箱单管理 &gt;装箱</div>
        <div>
        <div class="form-panel">
        <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件装箱信息
            &nbsp;&nbsp;<font style="color: red;font-weight: bold">拣货单号：${mainMap.PICK_ORDER_ID}</font>
        </h2>
        <div class="form-body">
            <table class="table_query">
                <tr>
                    <td class="right">制单日期：</td>
                    <td width="25%"><fmt:formatDate type="date" value="${mainMap.CREATE_DATE}"/></td>
                    <td class="right">制单人：</td>
                    <td width="25%">${CREATE_BY_NAME}</td>
                    <td class="right">出库仓库：</td>
                    <td width="25%">${mainMap.whName}
                        <input type="hidden" name="whId" value="${mainMap.whId }"/>
                    </td>
                </tr>
                <tr>
                    <td class="right">订单类型：</td>
                    <td width="25%">
                        <script type="text/javascript">getCode('${mainMap.ORDER_TYPE}');</script>
                    </td>
                    <td class="right">订货单位：</td>
                    <td width="24%">${mainMap.DEALER_NAME}</td>
                    <td class="right">销售单位：</td>
                    <td width="25%">${mainMap.SELLER_NAME}</td>
                </tr>
                <tr>
                    <td class="right">箱号：</td>
                    <td width="25%">
                        <input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO" value="${pkgNo }"/>
                        <font color="red">*</font>
                        <!-- <input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO" value="${pkgNo }"
                            onchange="queryBoxInfo(this);" onkeyup="checkPkgNo();"/><font color="red">*</font>
                        <input type="button" class="mini_btn" value="View" onclick="viewDlrPkgNoDtlxx();"/> -->
                    </td>
                    <td class="right">包装尺寸：</td>
                    <td width="25%" class="package-size">
                        长<input type="text" id="BOX_LEN" class="middle_txt" name="BOX_LEN" 
                            value="${box_len }" onchange="checkSize(this)"/>
                        宽<input type="text" id="BOX_WID" class="middle_txt" name="BOX_WID" 
                            value="${box_wid }" onchange="checkSize(this)"/>
                        高<input type="text" id="BOX_HEI" class="middle_txt" name="BOX_HEI" 
                            value="${box_hei }" onchange="checkSize(this)"/>CM
                    </td>
                    <td class="right">体积：</td>
                    <td width="24%">
                        <input type="text" style="border:0px;background-color:#F3F4F8;" id="VOLUME" class="middle_txt" name="VOLUME" value="${volume }"/>M<sup/>3
                    </td>
                </tr>
                <tr>
                    <td class="right">单箱重量：</td>
                    <td width="25%">
                        <input class="middle_txt" type="text" id="BOX_WEI" name="BOX_WEI" onchange="checkWeight(this)"
                            value="${box_wei }"/>KG
                    </td>
                    <td class="right">折合重量：</td>
                    <td width="24%">
                        <input type="text" style="border:0px;background-color:#F3F4F8;" id="EQ_WEIGHT" class="middle_txt" name="EQ_WEIGHT" value="${eq_weight }"/>KG
                    </td>
                    <td class="right">计费重量：</td>
                    <td width="25%">
                        <input type="text" style="border:0px;background-color:#F3F4F8;" id="CH_WEIGHT" name="CH_WEIGHT" class="middle_txt" value="${ch_weight }"/>KG
                    </td>
                </tr>
            </table>
            </div>
        </div>
        <FIELDSET class="form-fieldset sel-buttons">
            <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
                <th colspan="6">
                    <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/> 配件信息
                    <input type="button" class="normal_btn" name="addPartViv"
                        id="addPartViv" value="增加" onclick="addPartDiv()"/>
                </th>
            </LEGEND>
            <div style="display: none;" id="partDiv" class="grid-resize">
                <table class="table_query" width=100% border="0" class="center"
                    cellpadding="1" cellspacing="1">
                    <tr>
                        <td class="right" width="10%"> 配件编码：</td>
                        <td width="20%">
                            <input class="middle_txt" id="PART_OLDCODE"
                                datatype="1,is_noquotation,30" name="PART_OLDCODE"
                                onblur="isCloseDealerTreeDiv(event,this,'pan')" onkeyup="scan();" type="text"/>
                            <span></span>
                        </td>
                        <td class="right" width="10%">配件名称：</td>
                        <td width="22%">
                            <input class="middle_txt" id="PART_CNAME"
                                datatype="1,is_noquotation,30" name="PART_CNAME"
                                onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                        </td>
                        <td width="10%" class="right">件号：</td>
                        <td width="22%">
                            <input class="middle_txt" id="PART_CODE"
                                datatype="1,is_noquotation,30" name="PART_CODE"
                                onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="center" colspan="6">
                            <input class="normal_btn" type="button" name="BtnQuery"
                                id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                            <input class="normal_btn" type="button" name="BtnQuery"
                                id="queryBtn2" value="添加" onclick="addCells()"/>
                        </td>
                    </tr>
                </table>
                <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
                <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
            </div>
        </FIELDSET>
        <table id="file" class="table_list" scrolling="auto">
            <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>装箱信息</caption>
            <tr class="table_list_row1" >
                <th class="center"><input type="checkbox" name="allChk" id="allChk" onclick="checkAll(this)" checked/></th>
                <th class="center">序号</th>
                <th class="center">配件编码</th>
                <th class="center">配件名称</th>
                <th class="center">件号</th>
                <th class="center">单位</th>
                <th class="center">出库货位</th>
                <th class="center">出库批次</th>
                <th class="center">包装方式</th>
                <th class="center">销售数量</th>
                <th class="center">已装箱数量</th>
                <th class="center">待装箱数量</th>
                <th class="center">装箱数量<font color="RED">*</font></th>
                <th class="center">备注</th>
                <th class="center">操作</th>
            </tr>
        </table>
        <table border="0" class="table_query">
            <tr>
                <td  class="center">
    <!--                 <input class="normal_btn" type="button" name="BtnQuery" id="queryBtn2" value="添加" onclick="addCells()"/> -->
                    &nbsp; <input class="u-button" type="button" value="装 箱" onclick="savePkgConfirm('pkg');"/>
                    &nbsp; <input class="u-button" type="button" value="完 成 装 箱 " onclick="savePkgConfirm('finishPkg');"/>
                    &nbsp;<input class="u-button" type="button" value="装 箱 明 细" name="printPrintInfo" id="printPrintInfo" onclick="viewPkgDtl();"/>
                    &nbsp;<input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
                </td>
            </tr>
        </table>
        </div>
    </form>
</div>    
</body>
</html>