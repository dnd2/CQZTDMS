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
<title>配件领用单修改</title>
<script type=text/javascript>
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/showPartStockBase.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
        dataIndex: 'PART_ID',
        align: 'center',
        width: '33px',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "批次号", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "占用库存", dataIndex: 'BOOKED_QTY', align: 'center'},
    {header: "账面库存", dataIndex: 'ITEM_QTY', align: 'right'},
    {header: "领用单价", dataIndex: 'RES_PRICE', right: 'right;'},
    {header: "领用数量", dataIndex: 'PART_ID', align: 'center', renderer: returnText}
];

var tidx = 0;
var tidxn = "";
function seled(value, meta, record) {
	tidx++;
	tidxn = record.data.PART_ID + '_RNUM'+tidx;
	var loc = value + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME + "," + record.data.BATCH_NO;
	var html = '<input type="checkbox" id="ck_'+tidxn+'" name="ck" value="'+tidxn+'">';
	html += '<input type="hidden" id="loc_'+tidxn+'" value="'+loc+'">';
	html += '<input type="hidden" name="venderId_'+tidxn+'" value="'+record.data.STOCK_VENDER_ID+'">';
    return html;
}

function returnText(value, meta, record) {
    return "<input type='text' value='' class='short_txt' name='Num_" + tidxn + "' id='Num_" + tidxn + "' onchange='dataCheck1(this, \""+record.data.NORMAL_QTY+"\", \"" + tidxn + "\")'/>";
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
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
	        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
	        var loc = $('#loc_'+partId).val();
            if (validateCell(loc)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var normalQty = mt.rows[i].cells[8].innerText;  //可用库存
                var price = mt.rows[i].cells[11].innerText;  //领用价格
                var number = document.getElementById("Num_" + partId).value;  //领用数量
                if ("" == number) {
                    number = 1;
                }
                //addCell(partId, partCode, partOldcode, partCname, normalQty, unit, price, number);
                addCell(partId, partOldcode, partCname, partCode, normalQty, unit, price, number, loc);
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 已存在!</br>");
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件信息!");
    }
}
  

function validateCell(loc) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
            var loc2 = document.getElementById("locInfo_" + partIds[i].value).value;
            if (loc == loc2) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(partId, partOldcode, partCname, partCode, normalQty, unit, price, number, loc) {
	var tbl = document.getElementById('file');
	var rowObj = tbl.insertRow(tbl.rows.length);
    rowObj.className = "table_list_row1";
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
	
	var orgId = document.getElementById("parentOrgId").value;
	var saleAmount = (parseFloat(price) * parseInt(number)).toFixed(2);
	saleAmount = addKannma(saleAmount);
	
	var locArr = loc.split(",");
	var locCode = locArr[2];
	var batchNo = locArr[4];
	
	cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true" /></td>';
	cell2.innerHTML = '<td align="center" nowrap>' + (tbl.rows.length - 1) + '<input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>';
	cell3.innerHTML = '<td nowrap style="text-align: left"><input name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
	cell4.innerHTML = '<td nowrap style="text-align: left"><input name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
	cell5.innerHTML = '<td align="center" nowrap><input name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
	cell6.innerHTML = '<td align="center" nowrap><input name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
	cell7.innerHTML = '<td align="center" nowrap><input type="hidden" id="locInfo_'+partId+'" name="locInfo_'+partId+'" value="'+loc+'"/>'+locCode+'</td>';
	cell8.innerHTML = '<td>'+batchNo+'</td>';
    cell9.innerHTML = '<td align="center" nowrap><input name="normalQty_' + partId + '" id="normalQty_' + partId + '" value="' + normalQty + '" type="hidden" />' + normalQty + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input class="short_txt" onchange="dataTypeCheck(\'' + partId + '\',this)" name="returnQty_' + partId + '" id="returnQty_' + partId + '" type="text" value="' + number + '"/></td>';
    if (orgId == <%=Constant.OEM_ACTIVITIES%>) {
        cell11.innerHTML = '<td align="center" nowrap><input name="salePrice_' + partId + '" id="salePrice_' + partId + '" value="' + price + '" type="hidden" />' + price + '</td>';
    }else {
        cell11.innerHTML = '<td align="center" nowrap><input name="salePrice_' + partId + '"  onchange="priceCheck(' + partId + ',this)" id="salePrice_' + partId + '" value="' + price + '" type="text" class="short_txt" style="text-align: right" /></td>';
    }
    cell12.innerHTML = '<td class="right" nowrap><input name="saleAmount_' + partId + '" id="saleAmount_' + partId + '" value="' + saleAmount + '" type="text" readonly class="short_txt" style="border:none;text-align: right"/></td>';
    cell13.innerHTML = '<td align="center" nowrap><input class="short_txt" style="width: 100px;" name="remark_' + partId + '" id="remark_' + partId + '" type="text"/></td>';
    cell14.innerHTML = '<td><input  type="button" class="u-button"  name="deleteBtn" value="删 除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}
function dataTypeCheck(partId, obj) {
    var salePrice = document.getElementById("salePrice_" + partId).value.replace(new RegExp(",", "g"), "");
    var saleAmountObj = document.getElementById("saleAmount_" + partId);
    var value = obj.value;
    if (isNaN(value) || "" == obj.value) {
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

    if (normalQty < parseInt(obj.value)) {
        MyAlert("领用数量不能大于可用库存数!");
        obj.value = "";
        return;
    }

    var saleAmount = (((parseFloat(salePrice) * 100) * parseInt(value)) / 100).toFixed(2);
    saleAmountObj.value = addKannma(saleAmount);
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

function dataCheck1(obj, maxNum, partId) {
	if(!maxNum || maxNum == ''){
		maxNum = 0;
	}
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
    if(parseInt(value) > parseInt(maxNum)){
        MyAlert("领用数量不能大于可用库存数!");
        obj.value = "";
        return;
    }

    document.getElementById("ck_" + partId).checked = true;
}

function priceCheck(partId, obj) {
    var returnQty = document.getElementById("returnQty_" + partId).value;
    var saleAmountObj = document.getElementById("saleAmount_" + partId);
    var salePrice = obj.value.replace(new RegExp(",", "g"), "");
    if (isNaN(salePrice)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var saleAmount = (((parseFloat(salePrice) * 100) * parseInt(returnQty)) / 100).toFixed(2);
    saleAmountObj.value = addKannma(saleAmount);
}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    var count = tbl.rows.length;
    for (var i = rowNum; i < count; i++) {
        tbl.rows[i].cells[1].innerText = i;
        tbl.rows[i].cells[13].innerHTML = "<td><input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删 除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
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
    for (var i = count - 1; i > 0; i--) {
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
function WHChanged(obj) {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var whValue = obj.value;
	var whName = obj.selectedOptions[0].text;
	$("#whName").val(whName);
    
    deleteTblAll();

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
    fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partResRecUpload.do";
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

function savePlan() {
    btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/updateOrderInfos.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
            btnDisable();
            MyAlert("修改成功！", function() {
	            window.location.href = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partRecInit.do";
			});
        } else {
            MyAlert(json.Exception.message);
        }
    }
}

// 下载上传模板
function exportExcelTemplate() {
	fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/exportExcelTemplate.do";
    fm.submit();
}

//保存
function confirmSubmit() {
    var cb = document.getElementsByName("cb");
    var msg = "";
    if (document.getElementById("whId").value == "") {
        msg += "请先选择仓库</br>";
    }

    var maxLineNum = <%=Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM%>;
    if (maxLineNum < cb.length) {
        msg += "添加的数据不能超过 " + maxLineNum + " 行</br>";
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
            //需要校验调整数量是否为空
            var returnQty = document.getElementById("returnQty_" + cb[i].value).value;
            if (returnQty == "") {
                msg += "请填写第" + document.getElementById("idx_" + cb[i].value).value + "行的领用数量!</br>";
                flag = true;
            } else if(returnQty < 1){
                msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的领用数量不能小于1!</br>";
                flag = true;
            } else {
                var normalQty = parseInt(document.getElementById("normalQty_" + cb[i].value).value);
                if (normalQty < returnQty) {
                    msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的领用数量不能大于可用库存!</br>";
                    flag = true;
                }
            }
        } else {
            cb[i].disabled = true;
        }
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

    var linkMan = document.getElementById("linkMan").value;
    var telPhone = document.getElementById("telPhone").value;

    if ("" != linkMan) {
        var recName = /^[a-zA-Z\u4E00-\u9FA5]{0,200}$/;
        if (!recName.test(linkMan)) {
            MyAlert("采购单位输入格式有误!");
            return false;
        }
    }

    if ("" != telPhone) {
        var recTel = /^[-,0-9]{0,20}$/;
        if (!recTel.test(telPhone)) {
            MyAlert("联系电话输入格式有误!");
            return false;
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
    MyConfirm("确定保存新增信息?", savePlan, []);
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}


function goBack() {
    btnDisable();
    fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partRecInit.do";
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

//千分格式
function addKannma(number) {
    var num = number + "";
    num = num.replace(new RegExp(",", "g"), "");
    // 正负号处理
    var symble = "";
    if (/^([-+]).*$/.test(num)) {
        symble = num.replace(/^([-+]).*$/, "$1");
        num = num.replace(/^([-+])(.*)$/, "$2");
    }

    if (/^[0-9]+(\.[0-9]+)?$/.test(num)) {
        var num = num.replace(new RegExp("^[0]+", "g"), "");
        if (/^\./.test(num)) {
            num = "0" + num;
        }

        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/, "$1");
        var integer = num.replace(/^([0-9]+)(\.[0-9]+)?$/, "$1");

        var re = /(\d+)(\d{3})/;

        while (re.test(integer)) {
            integer = integer.replace(re, "$1,$2");
        }
        return symble + integer + decimal;

    } else {
        return number;
    }
}

function selOrg() {
    OpenHtmlWindow('<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partInnerOrgSearch.do', 700, 520);
}
</script>

</head>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
table.table_query{background-color: transparent}
table.table_query td.bottom-button{padding-bottom: 10px}
</style>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="planState" id="planState" />
			<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
			<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
			<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }" />
			<input type="hidden" name="actionURL" id="actionURL" value="${actionURL }" />
			<input type="hidden" name="orderType" id="orderType" value="<%=Constant.PART_SALE_STOCK_REMOVAL_TYPE_02%>" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件零售领用管理 &gt; 配件领用单 &gt; 修改
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 基本信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">领用单号：</td>
							<td>${map.RETAIL_CODE}
								<input type="hidden" name="changeCode" id="changeCode" value="${map.RETAIL_CODE}" />
								<input type="hidden" name="changeId" id="changeId" value="${map.RETAIL_ID}" />
							</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">仓库：</td>
							<td width="28%">
								<input type="hidden" name="whId" id="whId" value="${map.WH_ID}" />
								${map.WH_CNAME}
							</td>
						</tr>
						<tr>

						</tr>
						<tr>
							<td class="right">采购单位：</td>
							<td>
								<input class="middle_txt" type="text" name="linkMan" id="linkMan" value="${map.LINKMAN }" />
								<input name="BUTTON" type="button" class="mini_btn" onclick="selOrg()" value="..." />
							</td>
							<td class="right">联系电话：</td>
							<td>
								<input class="middle_txt" type="text" name="telPhone" id="telPhone" value="${map.TEL }" />
							</td>
							<td class="right">用途：</td>
							<td>
								<input class="middle_txt" type="text" name="purpose" id="purpose" value="${map.PURPOSE }" />
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">
								<textarea class="form-control align" style="width: 84%" id="remark" name="remark" cols="4" rows="4">${map.REMARK }</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px;">
				<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>领用明细</caption>
				<tr class="table_list_row0">
					<th><input type="checkbox" onclick="selAll2(this)" /></th>
					<th>序号</th>
					<th>配件编码</th>
					<th>配件名称</th>
                   	<th>件号</th>
					<th>单位</th>
					<th>货位</th>
					<th>批次号</th>
					<th>可用库存</th>
					<th>领用数量 <font color="red">*</font></th>
					<th>领用单价</th>
					<th>领用金额(元)</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_seq">
						<tr class="table_list_row1">
							<td align="center" nowrap>
								<input type="checkbox" value="${list.PART_ID}_RNUM${_seq.index}" id="cell_${_seq.index+1}" name="cb" checked="checked" />
								<input id="idx_${list.PART_ID}_RNUM${_seq.index}" name="idx_${list.PART_ID}_RNUM${_seq.index}" value="${_seq.index+1}" type="hidden" />
							</td>
							<td align="center" nowrap>${_seq.index+1}</td>
							<td align="center">
								<input name="partOldcode_${list.PART_ID}_RNUM${_seq.index}" id="partOldcode_${list.PART_ID}_RNUM${_seq.index}" value="${list.PART_OLDCODE}" type="hidden" />
								${list.PART_OLDCODE}
							</td>
							<td align="center" nowrap>
								<input name="partCname_${list.PART_ID}_RNUM${_seq.index}" id="partCname_${list.PART_ID}_RNUM${_seq.index}" value="${list.PART_CNAME}" type="hidden" />
								${list.PART_CNAME}
							</td>
							<td align="center" nowrap>
								<input name="partCode_${list.PART_ID}_RNUM${_seq.index}" id="partCode_${list.PART_ID}_RNUM${_seq.index}" value="${list.PART_CODE}" type="hidden"/>
								${list.PART_CODE}
							</td>
							<td align="center" nowrap>
								<input name="unit_${list.PART_ID}_RNUM${_seq.index}" id="unit_${list.PART_ID}_RNUM${_seq.index}" value="${list.UNIT}" type="hidden" />
								${list.UNIT}
							</td>
							<td align="center" nowrap>
								<input name="locInfo_${list.PART_ID}_RNUM${_seq.index}" id="locInfo_${list.PART_ID}_RNUM${_seq.index}" value="${list.PART_ID},${list.LOC_ID},${list.LOC_CODE},${list.LOC_NAME},${list.BATCH_NO}" type="hidden" />
								${list.LOC_NAME}
							</td>
							<td align="center" nowrap>
								${list.BATCH_NO}
							</td>
							<td align="center" nowrap>
								<input name="normalQty_${list.PART_ID}_RNUM${_seq.index}" id="normalQty_${list.PART_ID}_RNUM${_seq.index}" value="${list.STOCK_QTY}" type="hidden" />
								${list.STOCK_QTY}
							</td>
							<td align="center" nowrap>
								<input class="short_txt" name="returnQty_${list.PART_ID}_RNUM${_seq.index}" id="returnQty_${list.PART_ID}_RNUM${_seq.index}" value="${list.QTY}"
									onchange="dataTypeCheck('${list.PART_ID}_RNUM${_seq.index}',this)" type="text" />
							</td>
							<td align="center" nowrap>
								<c:choose>
									<c:when test="${parentOrgId eq oemOrgId}">
										<input name="salePrice_${list.PART_ID}_RNUM${_seq.index}" id="salePrice_${list.PART_ID}_RNUM${_seq.index}" style="text-align: right" value="${list.SALE_PRICE}" type="hidden" />
										${list.SALE_PRICE}
	                            	</c:when>
									<c:otherwise>
										<input name="salePrice_${list.PART_ID}_RNUM${_seq.index}" id="salePrice_${list.PART_ID}_RNUM${_seq.index}" style="text-align: right"
											onchange="priceCheck('${list.PART_ID}_RNUM${_seq.index}',this)" value="${list.SALE_PRICE}" type="text" class="short_txt" />
									</c:otherwise>
								</c:choose>
							</td>
							<td align="center" nowrap>
								<input name="saleAmount_${list.PART_ID}_RNUM${_seq.index}" id="saleAmount_${list.PART_ID}_RNUM${_seq.index}" value="${list.SALE_AMOUNT}" type="text" readonly class="short_txt"
									style="border: none; text-align: right" />
							</td>
							<td align="center" nowrap>
								<input class="middle_txt" style="width: 100px;" name="remark_${list.PART_ID}_RNUM${_seq.index}" id="remark_${list.PART_ID}_RNUM${_seq.index}" value="${list.REMARK}" type="text" />
							</td>
							<td>
								<input type="button" class="u-button" name="deleteBtn" value="删 除" onclick="deleteTblRow('${_seq.index+1}');" />
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" align="center">
				<tr>
					<td height="2"></td>
				</tr>
				<tr>
					<td align="center">
						<input class="u-button" type="button" value="保 存" id="saveButton" name="button1" onclick="confirmSubmit();">
						<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">

					</td>
				</tr>
			</table>
			<FIELDSET class="form-fieldset">
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件查询
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none;" id="partDiv" class="upload-divider grid-resize">
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
						</tr>
						<tr>
							<td class="center bottom-button" colspan="6">
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
