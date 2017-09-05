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
var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/showPartBase.json";
var title = null;
var columns = [
        {header: "序号", align:'center', renderer:getIndex},
     	{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
     	{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
     	{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
     	{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
     	{header: "单位", dataIndex: 'UNIT', align:'center'},
     	{header: "货位选择", dataIndex: 'UNIT', align:'center',renderer:returnCode},
     	{header: "批次", dataIndex: 'PART_ID', align:'center',renderer:returnPCCode},
     	{header: "入库数量", dataIndex: 'PART_ID', align:'center',renderer:returnText}
     ];

function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck_" + value + "' />";
}
function returnCode(value, meta, record) {
    var partId = record.data.PART_ID;
    var partOldcode = record.data.PART_OLDCODE;
    var partCname = record.data.PART_CNAME;
    var text = '<input name="LOC_CODE_'+partId+'" id="LOC_CODE_'+partId+'" class="middle_txt" type="text" readonly />';
    text += '<input name="LOC_ID_'+partId+'" id="LOC_ID_'+partId+'" type="hidden" value="" />';
    text += '<input class="mini_btn" type="button" value="..." onclick=\'codeChoice("'+partId+'", "'+partId+'","'+partOldcode+'", "'+partCname+'")\' />';
    return String.format(text);
}
function returnPCCode(value, meta, record) {
    return "<input type='text' class='short_txt' style='width: 100px;' value='' name='PC_CODE_" + value + "' id='PC_CODE_" + value + "' onchange='dataCheck2(this, " + value + ")'/>";
}
function checkCode(th,partId){
	var loc_code = th.value;
	var whId = $("#whId").find("option:selected").val();
	var whName = $("#whId").find("option:selected").text();
	var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
	var para = "LOC_CODE="+loc_code+"&PART_ID="+partId+"&whId="+whId+"&whName="+whName;
	makeCall(url2,forBack2,para);
}
function forBack2(json){
	var partId = json.PART_ID;
	if(json.returnValue != 1){
		if(partId!=""){
			document.getElementById("LOC_CODE_" + partId).value = "";
			document.getElementById("LOC_ID_" + partId).value = "";
		}
		parent.MyAlert("该货位编码不存在！");
	}else if(json.returnValue == 1){
		var i = json.LOC_ID;
		var c = json.LOC_CODE;
		var n = json.LOC_NAME;
		var v = i + "," + c + "," + n;
	    document.getElementById("LOC_ID_" + partId).value = v;
	}
}
function returnText(value, meta, record) {
    return "<input type='text' class='short_txt' value='' name='Num_" + value + "' id='Num_" + value + "' onchange='dataCheck1(this, " + value + ")'/>";
}

function codeChoice(partLocId, id, code, name) {
    var whId2 = document.getElementById("whId").value;
    if (whId2 == '') {
        MyAlert("请先选择库房！")
        return;
    }
    var url = g_webAppName + "/parts/baseManager/partsBaseManager/PartLocation/selectLocationInit.do";
    url += "?partLocId="+partLocId+"&partId="+id+"&partOldcode="+code+"&partCname="+name+"&whId="+whId2;
    OpenHtmlWindow(encodeURI(url), 800, 500);
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

function dataCheck2(obj, partId) {
    var value = obj.value;
    if(value.trim() == ""){
    	document.getElementById("ck_" + partId).checked = false;
    	return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^\d{10}$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入十位数字!");
        obj.value = "";
        return;
    }
    document.getElementById("ck_" + partId).checked = true;
}

function dataCheck1(obj, partId) {
    var value = obj.value;
    if(value.trim() == ""){
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
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            if (validateCell(partId)) {
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var number = document.getElementById("Num_" + partId).value;  //零售数量
                if ("" == number) {
                    number = 1;
                }
                var locCode = document.getElementById("LOC_CODE_"+partId).value; //货位信息
				if("" == locCode){
					MyAlert("第 "+i+" 行，请选择货位信息！");return;
				}
				var locId = document.getElementById("LOC_ID_"+partId).value; //货位信息
				pc_code = document.getElementById("PC_CODE_"+partId).value; //批次信息
				if("" == pc_code){
					MyAlert("第 "+i+" 行，请输入批次信息！");return;
				}else{
					pc_code = document.getElementById("PC_CODE_"+partId).value; //货位信息
				}
                addCell(partId, partCode, partOldcode, partCname, unit, locId, locCode , pc_code, number);
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

function addCell(partId, partCode, partOldcode, partCname, unit, locId, locCode , pc_code, inQty) {
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
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true"  /></td>';
    cell2.innerHTML = '<td align="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell5.innerHTML = '<td align="center" nowrap><input   name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell3.innerHTML = '<td align="center"><input   name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input   name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    if(locCode==""){
	    cell7.innerHTML = '<td align="center" nowrap><input   name="inQty_' + partId + '" id="inQty_' + partId + '" value="' + inQty + '" type="text" onchange="dataCheck(this);" /></td>';
	    cell8.innerHTML = '<td><input type="button" class="u-button"  name="delBtn" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';
	}else{
	    var cell10 = rowObj.insertCell(9);
	    var cell7Html = '<td align="center" nowrap><input name="locId_' + partId + '" id="locId_' + partId + '" value="' + locId + '" type="hidden" />';
    	cell7Html += '<input name="locCode_' + partId + '" id="locCode_' + partId + '" value="' + locCode + '" type="hidden" />' + locCode + '</td>';
	    cell7.innerHTML = cell7Html;
	    cell8.innerHTML = '<td align="center" nowrap><input   name="pc_' + partId + '" id="pc_' + partId + '" value="' + pc_code + '" type="hidden" />' + pc_code + '</td>';
	    cell9.innerHTML = '<td align="center" nowrap><input class="short_txt" name="inQty_' + partId + '" id="inQty_' + partId + '" value="' + inQty + '" type="text" onchange="dataCheck(this);" /></td>';
	    cell10.innerHTML = '<td><input type="button" class="u-button"  name="delBtn" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';
	}
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
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加配件明细!</br>";
    }
    if ('${dataMap.orgId}' == '${dataMap.OEM}' && document.getElementById("EI_TYPE").value == "") {
        msg += "请选择入库类型!</br>";
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
                msg += "请填写第" + (i + 1) + "行的入库数量!</br>";
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

//保存杂项入库单
function miscSave() {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/MiscSave.json";
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
	            fm.action = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/MiscMainInit.do";
	            fm.submit();
            });
        }
        else {
            MyAlert(exceptions.message);
        }
    }
}

//返回
function miscBack() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/MiscMainInit.do";
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
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/exportExcelTemplate.do";
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
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/uploadExcel.do";
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
    if ($('#whId')[0].value == "") {
        MyAlert("请先选择仓库!");
        return;
    }
    var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/uploadExcel.json";
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
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓库管理 &gt; 杂项入库(新增)
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 选择仓库
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">入库单号:</td>
							<td>${dataMap.orderCode}</td>
							<td class="right">入库日期:</td>
							<td>${dataMap.now}</td>
							<td class="right">制单人:</td>
							<td>${dataMap.userName}</td>
						</tr>
						<tr>
							<td class="right">仓库:</td>
							<td colspan="5">
								<select name="whId" id="whId" style='width: 200px;' class="u-select" onchange="WHChanged()">
									<%--   <option selected value=''>-请选择-</option>--%>
									<c:forEach items="${wareHouseList}" var="wareHouse">
										<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
									</c:forEach>
								</select><font color="RED">*</font>
							</td>
						</tr>
						<c:choose>
							<c:when test="${dataMap.orgId eq dataMap.OEM}">
								<tr>
									<td class="right">入库类型:</td>
									<td>
										<script type="text/javascript">
											genSelBoxExp("EI_TYPE",
										<%=Constant.MISC_EI_TYPE%>
											, "", true, "", "", "false", "");
										</script>
										<font color="RED">*</font>
									</td>
									<%--  <td class="right">部门:</td>
                        <td>
                            <select name="department" id="department" style='width:200px;'>
                                <option selected value=''>-请选择-</option>
                                <c:forEach items="${departmentList}" var="departmentList">
                                    <option value="${departmentList.DEPARTMENT_CODE}">${departmentList.DEPARTMENT_NAME}</option>
                                </c:forEach>
                            </select><font color="RED">*</font>
                        </td> --%>
								</tr>
							</c:when>
						</c:choose>
						<tr>
							<td class="right">备注:</td>
							<td colspan="5">
								<textarea class="form-control" name="textarea1" id="textarea1" style="width: 50%; display: inline;" rows="3"></textarea>
								<font color="RED">*</font>
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
					<c:if test="${dataMap.dealerId==null}">
						<td align="center" width="11%">货位</td>
						<td align="center" width="7%">批次</td>
					</c:if>
					<td align="center" width="7%">
						入库数量<font color="RED">*</font>
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
						<c:if test="${dataMap.dealerId==null}">
							<td align="center" nowrap>
								<input name="loc_${list.PART_ID}" id="loc_${list.PART_ID}" value="${list.LOC_ID},${list.LOC_CODE},${list.LOC_NAME}" type="hidden" />${list.LOC_CODE}
							</td>
							<td align="center" nowrap>
								<input name="pc_${list.PART_ID}" id="pc_${list.PART_ID}" value="${list.PC}" type="hidden" />${list.pc}
							</td>
						</c:if>
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


			<table class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="保 存" onclick="miscSaveConfirm();">
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="miscBack()" />
						&nbsp;
					</td>
				</tr>
			</table>
			<!-- <div style="display: none; heigeht: 5px" id="uploadDiv">
            <table>
                <tr>
                    <td><font color="red"> <input type="button"
                                                  class="u-button" value="下载模版" onclick="exportExcelTemplate()"/>
                        文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font> <input type="file"
                                                                            name="uploadFile" id="uploadFile"
                                                                            style="width: 250px"
                                                                            datatype="0,is_null,2000" value=""/> &nbsp;
                        <input type="button"
                               id="upbtn" class="u-button" value="确 定" onclick="confirmUpload()"/></td>
                </tr>
            </table>
        </div> -->
			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件库存查询
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none;" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">配件编码：</td>
							<td align="left" width="20%">
								<input class="middle_txt" id="partOldcode" datatype="1,is_noquotation,30" name="partOldcode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
							</td>
							<td class="right">配件名称：</td>
							<td align="left" width="20%">
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
