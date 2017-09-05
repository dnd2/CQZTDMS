<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
			request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>库存状态变更新增</title>
<script type=text/javascript>
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/showPartStockBase.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    //{header: "件号", dataIndex: 'PART_CODE', align: 'center', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位", dataIndex: 'LOC_NAME', align: 'center',  renderer: returnLocCode},
    {header: "批次号", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "占用库存", dataIndex: 'BOOKED_QTY', align: 'center'},
    {header: "正常封存", dataIndex: 'ZCFC_QTY', align: 'center'},
    {header: "盘亏封存", dataIndex: 'PKFC_QTY'},
    {header: "账面库存", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "盘盈封存", dataIndex: 'PDFC_QTY', align: 'center'}
];

var tidx = 0;
var tidxn = "";
function seled(value, meta, record) {
	tidx++;
	tidxn = record.data.PART_ID + '_RNUM'+tidx;
    return "<input type='checkbox' value='" + tidxn + "' name='ck' id='ck_" + tidxn + "' />";
}

function returnLocCode(value, meta, record) {
    var inputVal = record.data.PART_ID + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME + "," + record.data.BATCH_NO;
    var html = "<input type='hidden' value='" + inputVal + "' name='loc_" + tidxn + "' id='loc_" + tidxn + "'/>" + value;
    html += '<input type="hidden" value="'+record.data.STOCK_VENDER_ID+'" name="stockVenderId'+tidxn+'"/>';
    return html;
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

function addCells() {
    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        var loc = document.getElementById("loc_" + partId).value;  //货位信息
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            if (validateCell(partId, loc)) {
                //var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var unit = mt.rows[i].cells[4].innerText;  //单位
                var locName = mt.rows[i].cells[5].innerText;  //货位编码
                var loc = document.getElementById("loc_" + partId).value;  //货位信息
                var normalQty = mt.rows[i].cells[7].innerText;  //当前可用库存
                var bookedQty = mt.rows[i].cells[8].innerText;  //已占用
                var zcQty = mt.rows[i].cells[9].innerText;  //正常封存
                var pkQty = mt.rows[i].cells[10].innerText;  //盘亏封存
                var itemQty = mt.rows[i].cells[11].innerText;  //库存
                var pdQty = mt.rows[i].cells[12].innerText;  //盘盈封存

                //addCell(partId, partCode, partOldcode, partCname, itemQty, normalQty, bookedQty, zcQty, unit, pdQty, pkQty, locName, loc);
                addCell(partId, partOldcode, partCname, itemQty, normalQty, bookedQty, zcQty, unit, pdQty, pkQty, locName, loc);
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[1].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件信息!");
    }
}

function validateCell(spartId, loc) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
        	var loc2 = document.getElementById("locName_"+partIds[i].value).value;
            if (loc == loc2) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function genSelBoxExpStr(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
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
    // end
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

        var bzType = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>;
        if (type == bzType.toString()) {
            var bzType1 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01%>;
            var bzType2 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02%>;
            var bzType3 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03%>;
            var arrType = [bzType1.toString(), bzType2.toString(), bzType3.toString()];
            if (codeData[i].type == type && flag && arrType.indexOf(codeData[i].codeId.toString()) > -1) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }
        else {
            if (codeData[i].type == type && flag) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }

    }
    str += "</select>";
    return str;
}

function genSelBoxExpStr1(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
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
    // end
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

        var bzType = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>;
        if (type == bzType) {
            var bzType1 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01%>;
            var bzType2 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02%>;
            var bzType3 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03%>;
            var arrType = [bzType1.toString(), bzType2.toString(), bzType3.toString()];

            if (codeData[i].type == type && flag && arrType.indexOf(codeData[i].codeId.toString()) > -1) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }
        else {
            if (codeData[i].type == type && flag) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }

    }
    str += "</select>";
    document.write(str);
}

function addCell(partId, partOldcode, partCname, itemQty, normalQty, bookedQty, zcQty, unit, pdQty, pkQty, locName, loc) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    rowObj.className = "table_list_row2";
//     if (tbl.rows.length % 2 == 0) {
//         rowObj.className = "table_list_row2";
//     } else {
//         rowObj.className = "table_list_row1";
//     }
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
    var cell16 = rowObj.insertCell(15);

    var bzType = genSelBoxExpStr("bzType_" + partId, <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>, <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01%>, true, "u-select", "style=\"width: 80px;\"", "false", "").toString();
    //var cgType = genSelBoxExpStr("cgType_"+ partId,<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE%>,<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>,true,"u-select","","false","").toString();
    var cgType = "<input type='hidden' name='cgType_" + partId + "' id='cgType_" + partId + "' value='" + <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%> +"' />封存";
    var remark = document.getElementById("remark").value;

    var locArr = loc.split(",");
    var batchNo = locArr[4];
    
    cell1.innerHTML = '<tr><td><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true" /></td>';
    cell2.innerHTML = '<td align="center">' + (tbl.rows.length - 1) + '<input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>';
    //cell5.innerHTML = '<td><input name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell3.innerHTML = '<td align="center" ><input name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" ><input name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td><input name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell6.innerHTML = '<td><input name="locName_' + partId + '" id="locName_' + partId + '" value="' + loc + '" type="hidden" />' + locName + '</td>';
    cell7.innerHTML = '<td>' + batchNo + '</td>';
    cell8.innerHTML = '<td><input name="normalQty_' + partId + '" id="normalQty_' + partId + '" value="' + normalQty + '" type="hidden" />' + normalQty + '</td>';
    cell9.innerHTML = '<td><input name="zcQty_' + partId + '" id="zcQty_' + partId + '" value="' + zcQty + '" type="hidden" />' + zcQty + '</td>';
    cell10.innerHTML = '<td><input name="pkQty_' + partId + '" id="pkQty_' + partId + '" value="' + pkQty + '" type="hidden" />' + pkQty + '</td>';
    cell11.innerHTML = '<td><input name="pdQty_' + partId + '" id="pdQty_' + partId + '" value="' + pdQty + '" type="hidden" />' + pdQty + '</td>';
    cell12.innerHTML = '<td>' + bzType + '</td>';
    cell13.innerHTML = '<td>' + cgType + '</td>';
    cell14.innerHTML = '<td><input class="short_txt" onchange="dataTypeCheck(\'' + (tbl.rows.length - 1) + '\',\'' + partId + '\',this)" name="returnQty_' + partId + '" id="returnQty_' + partId + '" type="text"/></td>';
    cell15.innerHTML = '<td><input class="short_txt" name="remark_' + partId + '" id="remark_' + partId + '" type="text" value="' + remark + '"/></td>';
    cell16.innerHTML = '<td><input  type="button" class="u-button"  name="deleteBtn" value="删 除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}
function dataTypeCheck(loc, partId, obj) {
    if ("" == obj.value) {
        return;
    }
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
    var normalQty = parseInt(document.getElementById("normalQty_" + partId).value);
    var zcQty = parseInt(document.getElementById("zcQty_" + partId).value);
    var pdQty = parseInt(document.getElementById("pdQty_" + partId).value);//盘盈
    var pkQty = parseInt(document.getElementById("pkQty_" + partId).value);//盘亏

    var cgType = document.getElementById("cgType_" + partId).value;
    var bzType = document.getElementById("bzType_" + partId).value;

    var cgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>;
    var cgType2 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_02%>;
    var bzType1 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01%>;
    var bzType2 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02%>;
    var bzType3 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03%>;

    if (bzType2 != bzType && cgType1 == cgType && normalQty < parseInt(obj.value)) {
        MyAlert("封存数量不能大于可用库存数!");
        obj.value = "";
        return;
    }
    else if (cgType2 == cgType && bzType1 == bzType && zcQty < parseInt(obj.value)) {
        MyAlert("解封数量不能大于正常封存数!");
        obj.value = "";
        return;
    }
    else if (cgType2 == cgType && (bzType2 == bzType || bzType3 == bzType)) {
        if (bzType2 == bzType && pdQty < parseInt(obj.value)) {
            MyAlert("解封数量不能大于盘盈封存数!");
            obj.value = "";
            return;
        }
        else if (bzType3 == bzType && (pkQty < parseInt(obj.value))) {
            MyAlert("解封数量不能大于盘亏封存数!");
            obj.value = "";
            return;
        }
    }
}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    var count = tbl.rows.length;
    for (var i = rowNum; i < count; i++) {
        tbl.rows[i].cells[1].innerText = i;
        tbl.rows[i].cells[15].innerHTML = "<td><input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删 除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = document.getElementById("whId").value;
    if ("增 加" == addPartViv.value) {
        if ("" == whValue) {
            MyAlert("请先选择仓库!");
            return false;
        }
    }

    if (partDiv.style.display == "block") {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收 起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

//仓库变化
function WHChanged() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = document.getElementById("whId").value;

    if ("" == whValue && "收 起" == addPartViv.value) {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    }
    if ("" != whValue && "收 起" == addPartViv.value) {
        __extQuery__(1);
    }
}

//上传检查和确认信息
function confirmUpload() {
    if (fileVilidate()) {
        MyConfirm("确定上传选中的文件?", uploadExcel, []);
    }
}

//上传
function uploadExcel() {
    btnDisable();
    fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockSettingUpload.do";
    fm.submit();
}

function fileVilidate() {
    var msg = "";
    if (document.getElementById("whId").value == "") {
        msg += "请先选择仓库!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        return false;
    }
    var importFileName = $("uploadFile").value;
    if (importFileName == "") {
        MyAlert("请选择上传文件!");
        return false;
    }
    var index = importFileName.lastIndexOf(".");
    var suffix = importFileName.substr(index + 1, importFileName.length).toLowerCase();
    if (suffix != "xls" && suffix != "xlsx") {
        MyAlert("请选择Excel格式文件!");
        return false;
    }
    return true;
}

function savePlan() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/saveStockInfos.json";
    makeNomalFormCall(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
            btnDisable();
            MyAlert("保存成功!", function(){
	            window.location.href = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockSettingInit.do";
            });
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
    }
}

//下载上传模板
function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/exportExcelTemplate.do";
    fm.submit();
}

//保存
function confirmSubmit() {
    var cb = document.getElementsByName("cb");
    var msg = "";
    if (document.getElementById("whId").value == "") {
        msg += "请先选择仓库!</br>";
    }

    var maxLineNum = <%=Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM%>;
    if (maxLineNum < cb.length) {
        msg += "添加的数据不能超过 " + maxLineNum + " 行!</br>";
    }

    var ary = new Array();
    var l = cb.length;
    for (var i = 0; i < l; i++) {
        if (cb[i].checked) {
            var partId = cb[i].value;
            ary.push(partId);
        }
    }

    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验业务类型是否选择
            if (document.getElementById("bzType_" + cb[i].value).value == "") {
                msg += "请选择第" + document.getElementById("idx_" + cb[i].value).value + "行的业务类型!</br>";
                flag = true;
            }
            //需要校验调整类型是否选择
            if (document.getElementById("cgType_" + cb[i].value).value == "") {
                msg += "请选择第" + document.getElementById("idx_" + cb[i].value).value + "行的调整类型!</br>";
                flag = true;
            }
            //需要校验调整数量是否为空
            if (document.getElementById("returnQty_" + cb[i].value).value == "") {

                msg += "请填写第" + document.getElementById("idx_" + cb[i].value).value + "行的调整数量!</br>";
                flag = true;
            } else {
                var returnQty = parseInt(document.getElementById("returnQty_" + cb[i].value).value);
                var normalQty = parseInt(document.getElementById("normalQty_" + cb[i].value).value);
                var zcQty = parseInt(document.getElementById("zcQty_" + cb[i].value).value);
                var pdQty = parseInt(document.getElementById("pdQty_" + cb[i].value).value);//盘盈
                var pkQty = parseInt(document.getElementById("pkQty_" + cb[i].value).value);//盘亏

                var cgType = document.getElementById("cgType_" + cb[i].value).value
                var bzType = document.getElementById("bzType_" + cb[i].value).value

                var cgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>;
                var cgType2 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_02%>;
                var bzType1 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01%>;
                var bzType2 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02%>;
                var bzType3 = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03%>;

                if (bzType2 != bzType && cgType1 == cgType && normalQty < returnQty) {
                    msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的封存数量不能大于可用库存数!</br>";
                    flag = true;
                }
                else if (cgType2 == cgType && bzType1 == bzType && zcQty < returnQty) {
                    msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的解封数量不能大于正常封存数!</br>";
                    flag = true;
                }
                else if (cgType2 == cgType && (bzType2 == bzType || bzType3 == bzType)) {
                    if (bzType2 == bzType && pdQty < returnQty) {
                        msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的解封数量不能大于盘盈封存数!</br>";
                        flag = true;
                    }
                    else if (bzType3 == bzType && (pkQty < returnQty)) {
                        msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的解封数量不能大于盘亏封存数!</br>";
                        flag = true;
                    }
                }
            }
        } else {
            cb[i].disabled = true;
        }
    }

    var s = ary.join(",") + ",";
    var pflag = false;
    var nclass = "";
    var sid = "";
    for (var i = 0; i < ary.length; i++) {
        $(".cname_" + ary[i]).parent("td").css({background: ""});
    }
    for (var i = 0; i < ary.length; i++) {
        if (s.replace(ary[i] + ",", "").indexOf(ary[i] + ",") > -1) {
            pflag = true;
            sid = "partCname_" + ary[i];
            nclass = "cname_" + ary[i];
            var partCname = document.getElementById(sid).value;
            MyAlert("配件：" + partCname + " 被重复上传!");
            break;
        }
    }
    if (pflag) {
        $("." + nclass).parent("td").css({background: "red"});
        return false;
    }

    if (flag) {
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
    }
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加配件库存信息!</br>";
    }
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
            break;
        }
    }
    if (!flag) {
        msg += "请选择配件库存信息!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return;
    }
    //document.getElementById("planState").value=<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_01%>;
    MyConfirm("确定保存新增信息?", savePlan, []);
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}


function goBack() {
    btnDisable();
    fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockSettingInit.do";
    fm.submit();
}
function validateNum(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
}
function showUpload() {
    var uploadDiv = document.getElementById("uploadDiv");
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}

function setRemark() {
    var cb = document.getElementsByName("cb");
    var remark = document.getElementById("remark").value;
    for (var i = 0; i < cb.length; i++) {
        document.getElementById("remark_" + cb[i].value).value = remark;
    }
}
</script>
</head>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
<body>
	<div class="wbox" style="min-width: 1030px;">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="planState" id="planState" />
			<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
			<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
			<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }" />
			<input type="hidden" name="actionURL" id="actionURL" value="${actionURL }" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件仓库管理&gt;配件状态变更&gt;配件库存状态变更&gt;新增
			</div>
			<div>
				<div class="form-panel">
					<h2>
						<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
					</h2>
					<div class="form-body">
						<table class="table_query">
							<tr>

								<td class="right">变更单号：</td>
								<td>${changeCode}
									<input type="hidden" name="changeCode" id="changeCode" value="${changeCode}" />
								</td>
								<td class="right">制单单位：</td>
								<td>${companyName }</td>
							</tr>
							<tr>
								<td class="right">制单人：</td>
								<td>${marker}</td>
								<td class="right">仓库：</td>
								<td>
									<select name="whId" id="whId" style="width: 150px;" class="u-select" onchange="WHChanged()">
										<c:if test="${WHList!=null}">
											<c:forEach items="${WHList}" var="list">
												<c:choose>
													<c:when test="${selectedWhId eq list.WH_ID}">
														<option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
													</c:when>
													<c:otherwise>
														<option value="${list.WH_ID }">${list.WH_CNAME }</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:if>
									</select> <font color="#FF000">*</font>
								</td>
							</tr>
							<tr>
								<td class="right">备注：</td>
								<td colspan="3">
									<textarea class="form-control" style="width: 80%" id="remark" name="remark" onchange="setRemark()"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<table id="file" class="table_list" style="border-bottom: 1px;">
					<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>库存变更详细</caption>
					<tr class="table_list_row0">
						<th nowrap>
							<input type="checkbox" onclick="selAll2(this)" />
						</th>
						<th nowrap>序号</th>
						<th nowrap>配件编码</th>
						<th nowrap>配件名称</th>
						<!-- 
        <th>
            件号
        </th>
         -->
						<th nowrap>单位</th>
						<th nowrap>货位</th>
						<th nowrap>批次号</th>
						<th nowrap>可用库存</th>
						<th nowrap>正常封存</th>
						<th nowrap>盘亏封存</th>
						<th nowrap>盘盈封存</th>
						<th nowrap>
							业务类型<font color="red">*</font>
						</th>
						<th nowrap>
							调整类型<font color="red">*</font>
						</th>
						<th nowrap>
							调整数量 <font color="red">*</font>
						</th>
						<th nowrap>备注</th>
						<th nowrap>操作</th>
					</tr>
				</table>
				<table width="100%" align="center">
					<tr>
						<td height="2"></td>
					</tr>
					<tr>
						<td align="center">
<!-- 							<input class="u-button" type="button" value="上传文件" name="button1" onclick="showUpload();"> -->
							<input class="u-button" type="button" value="保 存" id="saveButton" name="button1" onclick="confirmSubmit();">
							<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">
						</td>
					</tr>
					<tr>
						<td height="1"></td>
					</tr>
					<tr>
						<td valign="top">
							<br>
						</td>
					</tr>
				</table>
				<div style="display: none; heigeht: 5px" id="uploadDiv">
					<table>
						<tr>
							<td>
								<font color="red"> <input type="button" class="u-button" value="下载模版" onclick="exportExcelTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
								</font>
								<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
								&nbsp;
								<input type="button" id="upbtn" class="u-button" value="确 定" onclick="confirmUpload()" />
							</td>
						</tr>
					</table>
				</div>
				<FIELDSET>
					<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
						<th colspan="6">
							<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />配件库存查询
							<input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
						</th>
					</LEGEND>
					<div style="display: none; heigeht: 5px" id="partDiv">
						<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
							<tr>
								<td class="right">配件编码：</td>
								<td>
									<input class="middle_txt" id="partOldcode" datatype="1,is_noquotation,30" name="partOldcode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
								<td class="right">配件名称：</td>
								<td>
									<input class="middle_txt" id="partCname" datatype="1,is_noquotation,30" name="partCname" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>

								<!-- 
                <td class="right">
                    配件件号：
                </td>
                <td>
                    <input class="middle_txt" id="partCode"
                           datatype="1,is_noquotation,30" name="partCode"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                 -->
								<td class="right">盘点结果：</td>
								<td>
									<select name="pdResult" id="pdResult" class="u-select" onchange="__extQuery__(1)">
										<option value="">-请选择-</option>
										<option value="1">盘盈</option>
										<option value="0">盘亏</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="center" colspan="6">
									<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
									<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添 加" onclick="addCells()" />
								</td>
							</tr>
						</table>
						<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
						<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
					</div>
				</FIELDSET>
				</from>
			</div>
</body>
</html>
