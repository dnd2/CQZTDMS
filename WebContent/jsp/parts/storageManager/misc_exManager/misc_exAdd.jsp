<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type=text/javascript>
$(function(){
	$('#partDiv').css('width', $('#file').width()-40);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/showPartBase.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位编码", dataIndex: 'LOC_CODE', align: 'center', renderer: returnLocCode},
    {header: "批次", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "出库数量", dataIndex: 'PART_ID', align: 'center', renderer: returnText}
];

function seled(value, meta, record) {
    var pl = record.data.PART_ID + "," + record.data.LOC_ID;
    return "<input type='checkbox' value='" + pl + "' name='ck' id='ck_" + pl + "' />";
}

function returnText(value, meta, record) {
    var pl = record.data.PART_ID + "," + record.data.LOC_ID;
    return "<input class='short_txt' type='text' value='' name='Num_" + pl + "' id='Num_" + pl + "' onchange='dataCheck1(this, \"" + pl + "\")'/>";
}
function returnLocCode(value, meta, record) {
    var pl = record.data.PART_ID + "," + record.data.LOC_ID;
    var v = record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME;
    return "<input type='hidden' value='" + v + "' name='loc_" + pl + "' id='loc_" + pl + "'/>" + value;
}
function dataCheck(obj) {
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

}

function dataCheck1(obj, partId) {
    var value = obj.value;
    if (value.trim() == "") {
        document.getElementById("ck_" + partId).checked = false;
        return;
    }
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
    document.getElementById("ck_" + partId).checked = true;
}


function validateCell(value) {
    var flag = true;
    var tbl = document.getElementById('file');
    for (var i = 1; i <= tbl.rows.length - 2; i++) {
        if (value == document.getElementById('cell_' + i).value) {
            flag = false;
            break;
        }
    }
    return flag;
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
        var pl = mt.rows[i].cells[1].firstChild.value;  //ID
        var n = document.getElementById("Num_" + pl).value;
        if (n.trim() != "") {
            mt.rows[i].cells[1].firstChild.checked = true;
        } else {
            mt.rows[i].cells[1].firstChild.checked = false;
        }
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            if (validateCell(pl)) {
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var locCode = mt.rows[i].cells[6].innerText;  //货位编码
                var pc_code = mt.rows[i].cells[7].innerText;  //批次
                var itemQty = mt.rows[i].cells[8].innerText;  //库存数量
                var number = document.getElementById("Num_" + pl).value;  //零售数量
                var loc = document.getElementById("loc_" + pl).value;  //货位信息
                if ("" == number) {
                    number = 1;
                }
                if (Number(itemQty) < Number(number)) {
                    MyAlert("出库数量大于现存库数量！");
                    return;
                }
                addCell(pl, partCode, partOldcode, partCname, unit, locCode, loc, pc_code , itemQty, number);
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[4].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件信息!");
    }
}

function validateCell(spartId) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
            if (spartId == partIds[i].value) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(partId, partCode, partOldcode, partCname, unit, locCode, loc, pc_code , itemQty, inQty) {
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
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true"  /></td>';
    cell2.innerHTML = '<td align="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell5.innerHTML = '<td align="center" nowrap><input   name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell3.innerHTML = '<td align="center"><input   name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input   name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="locCode_' + partId + '" id="locCode_' + partId + '" value="' + loc + '" type="hidden" />' + locCode + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="pcCode_' + partId + '" id="pcCode_' + partId + '" value="' + pc_code + '" type="hidden" />' + pc_code + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="itemQty_' + partId + '" id="itemQty_' + partId + '" value="' + itemQty + '" type="hidden" />' + itemQty + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input class="short_txt" name="inQty_' + partId + '" id="inQty_' + partId + '" value="' + inQty + '" type="text" onchange="dataCheck(this);" /></td>';
    cell11.innerHTML = '<td><input type="button" class="u-button"  name="delBtn" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></TR>';
}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    var count = tbl.rows.length;
    for (var i = rowNum; i <= count; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
        tbl.rows[i].cells[7].innerHTML = "<td><input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删 除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}

//删除所有已添加的明细
function deleteTblAll() {
    var tbl = document.getElementById('file');
    var count = tbl.rows.length;
    for (var i = count - 1; i > 1; i--) {
        tbl.deleteRow(i);
    }
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = document.getElementById("whId").value;
    if ("增 加" == addPartViv.value) {
        if ("" == whValue) {
            MyAlert("请先选择仓库！");
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

    deleteTblAll();

    if ("" == whValue && "收 起" == addPartViv.value) {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    }
    if ("" != whValue && "收 起" == addPartViv.value) {
        __extQuery__(1);
    }
}

//保存确认
function miscSaveConfirm() {
    if (!validateFm()) {
        return;
    }
    MyConfirm('确定保存该单据?', miscSave, []);
}

function validateFm() {
    var msg = "";
    if (document.getElementById("textarea1").value == "") {
        msg += "请填写备注!</br>";
    }
    if (document.getElementById("whId").value == "") {
        msg += "请选择仓库!</br>";
    }
    if ('${dataMap.orgId}' == '${dataMap.OEM}' && document.getElementById("EX_TYPE").value == "") {
        msg += "请选择出库类型!</br>";
    }
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加配件明细!</br>";
    }
    /* if ('${dataMap.orgId}' == '${dataMap.OEM}' && document.getElementById("department").value == "") {
        msg += "请选择部门!</br>";
    } */
    var ary = new Array();
    var l = cb.length;
    for (var i = 0; i < l; i++) {
        if (cb[i].checked) {
            var partId = cb[i].value;
            ary.push(partId);
        }
    }

    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验计划量是否填写
            if (document.getElementById("inQty_" + cb[i].value).value == "") {
                msg += "请填写第" + (i + 1) + "行的出库数量!</br>";
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

    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return false;
    }
    return true;
}

//保存杂项出库单
function miscSave() {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/MiscSave.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;

        if (null != error && error.length > 0) {
            MyAlert(error);
        }
        else if (null != success && success.length > 0) {
            MyAlert(success, function(){
	            window.location = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/init.do";
            });
        }
        else {
            MyAlert(exceptions.message);
        }
    }
}

//返回
function miscBack() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/init.do";
    fm.submit();
}

function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
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
            if (inputArr[i].id == "rep" && document.getElementById("isBatchSo").value ==<%=Constant.PART_BASE_FLAG_YES%>) {
                continue;
            }
            inputArr[i].disabled = false;
        }
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

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/exportExcelTemplate.do";
    fm.submit();
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
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/uploadExcel.do";
    fm.submit();
    ;
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

function uploadExcelssss() {
    if ($('whId').value == "") {
        MyAlert("请先选择仓库!");
        return;
    }
    var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/uploadExcel.json";
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
                addCell(data[i].PART_ID, data[i].PART_CODE, data[i].PART_OLDCODE, data[i].PART_CNAME, data[i].UNIT == null ? "" : data[i].UNIT, data[i].IN_QTY == null ? "" : data[i].IN_QTY);
                document.getElementById("inQty_" + data[i].PART_ID).value = data[i].inQty;
            }
        }
        if ("" != msg) {
            MyAlert(msg);
        }
    }
}
function check(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请录入正整数！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
}

</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input name="orderId" id="orderId" type="hidden" value="${dataMap.orderId}" />
			<input name="orderCode" id="orderCode" type="hidden" value="${dataMap.orderCode}" />
			<input name="orgId" id="orgId" type="hidden" value="${dataMap.orgId}" />
			<input name="orgCode" id="orgCode" type="hidden" value="${dataMap.orgCode}" />
			<input name="orgName" id="orgName" type="hidden" value="${dataMap.orgName}" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓库管理 &gt; 杂项出库(新增)
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 杂项出库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">杂项出库单号：</td>
							<td>${dataMap.orderCode}</td>
							<td class="right">出库日期：</td>
							<td>${dataMap.now}</td>
							<td class="right">制单人：</td>
							<td>${dataMap.userName}</td>
						</tr>
						<tr>
							<td class="right">仓库：</td>
							<td align="left">
								<select name="whId" id="whId" style='width: 150px;' onchange="WHChanged()" class="u-select">
									<c:forEach items="${wareHouseList}" var="wareHouse">
										<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
									</c:forEach>
									<font color="RED">*</font>
							</td>
						</tr>
						<c:choose>
							<c:when test="${dataMap.orgId eq dataMap.OEM}">
								<tr>
									<td class="right">出库类型：</td>
									<td>
										<script type="text/javascript">
											genSelBoxExp("EX_TYPE",
										<%=Constant.MISC_EX_TYPE%>
											, "", "true", "", "", "false", '');
										</script>
										<font color="RED">*</font>
									</td>
									<%-- <td class="right">部门:</td>
        <td>
            <select name="department" id="department" style='width:150px;'>
                <option selected value=''>-请选择-</option>
                <c:forEach items="${departmentList}" var="departmentList">
                    <option value="${departmentList.DEPARTMENT_CODE}">${departmentList.DEPARTMENT_NAME}</option>
                </c:forEach>
            </select><font color="RED">*</font>
        </td> --%>
									<td></td>
									<td></td>
							</c:when>
						</c:choose>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">
								<textarea class="form-control" style="width: 80%;" name="textarea1" id="textarea1" rows="3"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="15" align="left">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息 <span class="checked" style="text-align: left"></span>
					</th>
				</tr>
				<tr bgcolor="#FFFFCC">
					<td align="center" width="2%">
						<input type="checkbox" checked name="ckAll" id="ckAll" onclick="selectAll()" />
					</td>
					<td align="center" width="4%">序号</td>
					<td align="center" width="12%">配件编码</td>
					<td align="center" width="11%">配件名称</td>
					<td align="center" width="10%">配件件号</td>
					<td align="center" width="11%">单位</td>
					<td align="center" width="11%">货位编码</td>
					<td align="center" width="11%">批次</td>
					<td align="center" width="11%">库存数量</td>
					<td align="center" width="7%">
						出库数量<font color="RED">*</font>
					</td>
					<td align="center" width="8%">操作</td>
				</tr>
				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_sequenceNum">
						<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
						</c:if>
						<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
						</c:if>
						<td align="center" nowrap>
							<input type="checkbox" value="${list.PART_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" />
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}
							<input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNum.index+1}" type="hidden">
						</td>
						<td align="center">
							<input name="partOldcode_${list.PART_ID}" id="partOldcode_${list.PART_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td align="center" nowrap>
							<input name="partCname_${list.PART_ID}" id="partCname_${list.PART_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.PART_ID}" />${list.PART_CNAME}
						</td>
						<td align="center" nowrap>
							<input name="partCode_${list.PART_ID}" id="partCode_${list.PART_ID}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>
							<input name="unit_${list.PART_ID}" id="unit_${list.PART_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
						</td>
						<td align="center" nowrap>
							<input name="locCode_${list.PART_ID}" id="locCode_${list.PART_ID}" value="${list.LOC_CODE}" type="hidden" />${list.LOC_CODE}
						</td>
						<td align="center" nowrap>
							<input name="pcCode_${list.PART_ID}" id="pcCode_${list.PART_ID}" value="${list.PC_CODE}" type="hidden" />${list.PC_CODE}
						</td>
						<td align="center" nowrap>
							<input name="itemQty_${list.PART_ID}" id="itemQty_${list.PART_ID}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
						</td>
						<td align="center" nowrap>
							<input name="inQty_${list.PART_ID}" id="inQty_${list.PART_ID}" value="${list.INQTY}" type="hidden" />${list.INQTY}
						</td>
						<td>
							<input type="button" class="u-button" name="deleteBtn" value="删  除" onclick="deleteTblRow('${_sequenceNum.index+2}');" />
						</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="保 存" onclick="miscSaveConfirm();">
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="miscBack()" />
						&nbsp;
					</td>
				</tr>
			</table>
			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件库存查询
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
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
							<td class="right">件号：</td>
							<td>
								<input class="middle_txt" id="partCode" datatype="1,is_noquotation,30" name="partCode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
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
		</form>
	</div>
</body>
</html>
