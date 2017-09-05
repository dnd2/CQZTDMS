<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货申请修改</title>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryPartInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
    {header: "单位", dataIndex: 'UNIT', style: 'text-align:left'},
    {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center',renderer: insertNormalQtyInput},
    {header: "销售价格", dataIndex: 'BUY_PRICE', align: 'center'},
    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "退货数量<font color='red'>*</font>",dataIndex: 'BUY_QTY', align: 'center', renderer: insertApplyQtyInput}
    //{header: "备注", align: 'center', renderer: insertRemarkInput}
];
function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck' onclick='chkPart()'/>";
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

function selAll2(obj){
	var cb = document.getElementsByName('cb');
	for(var i =0 ;i<cb.length;i++){
		if(obj.checked){
			cb[i].checked = true;
		}else{
			cb[i].checked = false;
		}
	}
}

function chkPart(){
	var cks = document.getElementsByName('ck');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckbAll").checked = flag;
}

function chkPart1(){
	var cks = document.getElementsByName('cb');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckAll").checked = flag;
}

function insertPartCodeInput(value, meta, record) {
    var output = '<input type="hidden"  id="PART_CODE' + record.data.PART_ID + '" name="PART_CODE' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    return output;
}
function insertPartOldCodeInput(value, meta, record) {

    var output = '<input type="hidden"  id="PART_OLDCODE' + record.data.PART_ID + '" name="PART_OLDCODE' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    return output;
}
function insertPartCnameInput(value, meta, record) {

    var output = '<input type="hidden"  id="PART_CNAME' + record.data.PART_ID + '" name="PART_CNAME' + record.data.PART_ID + '" value="' + value + '"/>' + value
            + '<input type="hidden"  id="UNIT' + record.data.PART_ID + '" name="UNIT' + record.data.PART_ID + '" value="' + record.data.UNIT + '"/>';
    return output;
}

//插入文本框
function insertNormalQtyInput(value, meta, record) {

    var output = '<input type="hidden" id="NORMAL_QTY' + record.data.PART_ID + '" name="NORMAL_QTY' + record.data.PART_ID + '" value="' + value + '"/>\n'+value;
    return output;
}

function insertBuyQtyInput(value, meta, record) {
    var output = "";
    if (record.data.BUY_QTY) {
        output = '<input type="hidden"  id="BUY_QTY' + record.data.PART_ID + '" name="BUY_QTY' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    } else {
        output = '<input type="hidden"  id="BUY_QTY' + record.data.PART_ID + '" name="BUY_QTY' + record.data.PART_ID + '" value=""/>' + 0;
    }
    return output;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {

    var output = '<input type="text" class="short_txt" id="APPLY_QTY1' + record.data.PART_ID + '" name="APPLY_QTY1' + record.data.PART_ID + '" value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}

function insertRemarkInput(value, meta, record) {

    var output = '<input type="text" class="long_txt" id="REMARK' + record.data.PART_ID + '" name="REMARK' + record.data.PART_ID + '" value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
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
    $("#ckAll")[0].checked = true;
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            var applyQty1 = $("#APPLY_QTY1" + partId)[0].value;
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty1)) {
                MyAlert("配件【" + mt.rows[i].cells[2].innerText + "】的退货数量只能输入非零的正整数!");
                return;
            }
            var normalQty = $("#NORMAL_QTY"+partId)[0].value;
            if(parseInt(applyQty1)>parseInt(normalQty)){
            	MyAlert("配件【" + mt.rows[i].cells[2].innerText + "】的退货数量不能大于可用库存!");
                return;
            }
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
				var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var buyPrice = mt.rows[i].cells[7].innerText;  //销售价格
                var buyQty = mt.rows[i].cells[8].innerText;  //采购数量
                //addCell(partId, partCode, partOldcode, partCname, buyQty, buyPrice, applyQty1,normalQty,unit);
                addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty1,normalQty,unit);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[2].innerText + " 已存在于退货明细中!");
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

function addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty1,normalQty,unit) {
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

    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>' + (tbl.rows.length - 2);
    cell3.innerHTML = '<td align="left" nowrap><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="left" nowrap><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="left" nowrap><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="left" nowrap><input   name="UNIT' + partId + '" id="UNIT' + partId + '" value="' + unit + '" type="hidden"/>' + unit + '</td>';
    cell7.innerHTML = '<td align="right" nowrap><input   name="BUY_PRICE' + partId + '" id="BUY_PRICE' + partId + '" value="' + buyPrice + '" type="hidden" />' + buyPrice + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="BUY_QTY' + partId + '" id="BUY_QTY' + partId + '" value="' + buyQty + '" type="hidden" />' + buyQty + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="NORMAL_QTY' + partId + '" id="NORMAL_QTY' + partId + '" value="' + normalQty + '" type="hidden"/>' + normalQty + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input   name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty1 + '" type="text" class="short_txt"/></td>';
    cell11.innerHTML = '<td align="center" nowrap><input  class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell12.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
//         tbl.rows[i].cells[10].innerHTML = "<input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}

function setCheckModel() {
    var remark = document.getElementById("REMARK").value;
    if (remark == "") {
        MyAlert("请填写退货原因！");
        return;
    }

    var chk = document.getElementsByName("cb");
    var l = chk.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var applyQty = document.getElementById("APPLY_QTY" + chk[i].value).value;//退货数量
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty)) {
                MyAlert("第"+(i+1)+"行的退货数量只能输入非零的正整数!");
                return;
            }
            var soCode = document.getElementById("soCode").value;
            if (soCode != null && soCode != "") {//如果有销售单号,那么就要判断采购数量和退货数量
                var buyQty = document.getElementById("BUY_QTY" + chk[i].value).value;//采购数量
                if (parseInt(applyQty) > parseInt(buyQty)) {
                    MyAlert("退货数量不能大于采购数量,请重新输入!");
                    return;
                }
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择退货明细！");
        return;
    }

    MyConfirm("确认保存？", saveApply);
}

function saveApply() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/updateApply.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

// 删除初始化的明细记录
function deleteDtl(dtlId,rowNum,returnId){
	var delDtlIdVal = document.getElementById('delDtlId').value;
	if(delDtlIdVal == ""){
		document.getElementById('delDtlId').value = dtlId;
	}else{
		document.getElementById('delDtlId').value = delDtlIdVal + "," + dtlId;
	}
	var upDtlIdVal = document.getElementById('upDtlId').value;
	if(upDtlIdVal != "" && upDtlIdVal.indexOf(dtlId) != -1){
		upDtlIdVal = upDtlIdVal.replace(dtlId, '');
		upDtlIdVal = upDtlIdVal.replace(',,', ',');
		document.getElementById('upDtlId').value = upDtlIdVal;
	}
	deleteTblRow(rowNum);
}

// 更新初始化的明细记录
function updateDtl(dtlId,rowNum,returnId){
	var upDtlIdVal = document.getElementById('upDtlId').value;
	if(upDtlIdVal == ""){
		document.getElementById('upDtlId').value = dtlId;
	}else if(upDtlIdVal.indexOf(dtlId) == -1){
		document.getElementById('upDtlId').value = upDtlIdVal + "," + dtlId;
	}
}

function deleteResult(jsonObj){
	if(jsonObj){
		var success = jsonObj.success;
		var exceptions = jsonObj.Exception;
		var returnId = jsonObj.returnId;
	    if(success){
	    	MyAlert(success);
	    	window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/updateRInfoInit.do?returnId=' + returnId;
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件退货管理&gt;销售退货申请&gt;修改申请
		</div>
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" id="soCode" value="${po['IN_CODE']}" />
			<input type="hidden" name="saleOrgId" id="saleOrgId" value="${po['SELLER_ID']}" />
			<input type="hidden" name="childorgId" id="childorgId" value="${po['DEALER_ID']}">
			<input type="hidden" name="delDtlId" id="delDtlId">
			<input type="hidden" name="upDtlId" id="upDtlId">

			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">制单单位：</td>
							<td>${po["ORG_NAME"]}</td>
						</tr>
						<tr>
							<td class="right">退货单位：</td>
							<td>${po["DEALER_NAME"]}</td>

							<td class="right">销售单位：</td>
							<td>${po["SELLER_NAME"]}</td>

						</tr>
						<tr style="display: none">
							<td class="right">仓库：</td>
							<td width="30%">
								<input id="WH_ID" name="WH_ID" type="hidden" class="middle_txt" value="${po['STOCK_OUT']}" />
							</td>
						</tr>
						<tr>
							<td class="right">入库单号：</td>
							<td>${po["IN_CODE"]}</td>

						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">
								<textarea class="form-control" name="REMARK" id="REMARK" style="width: 80%" rows="4">${po["REMARK"]}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>


			<table id="file" class="table_list" style="border-bottom: 1px;">
				<tr>
					<th colspan="11" align="left">
						<img src="<%=contextPath%>/img/nav.gif" />退货明细
				</tr>
				<tr class="table_list_row0">
					<td>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" checked="checked" />
					</td>
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>件号</td>
					<td>单位</td>
					<td>销售价格</td>
					<td>采购数量</td>
					<td>可用库存</td>
					<td>退货数量<font color="red">*</font></td>
					<td>备注</td>
					<td>操作</td>
				</tr>

				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
						<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
						</c:if>
						<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
						</c:if>
						<td align="center" nowrap>
							<input type="checkbox" value="${list.PART_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" onclick="chkPart1();" />
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}
							<input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNume.index+1}" type="hidden">
							<input id="dtlId_${list.PART_ID}" name="dtlId_${list.PART_ID}" value="${list.DTL_ID}" type="hidden">
						</td>
						<td align="center">
							<input name="PART_OLDCODE${list.PART_ID}" id="PART_OLDCODE${list.PART_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td align="center" nowrap>
							<input name="PART_CNAME${list.PART_ID}" id="PART_CNAME${list.PART_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.PART_ID}" />${list.PART_CNAME}
						</td>
						<td align="center" nowrap>
						  <input   name="PART_CODE${list.PART_ID}" id="PART_CODE${list.PART_ID}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>
							<input name="UNIT${list.PART_ID}" id="UNIT${list.PART_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
						</td>
						<td align="center" nowrap>
							<input name="BUY_PRICE${list.PART_ID}" id="BUY_PRICE${list.PART_ID}" value="${list.BUY_PRICE}" type="hidden" />${list.BUY_PRICE}
						</td>
						
						<td align="center" nowrap>
							<input name="NORMAL_QTY${list.PART_ID}" id="NORMAL_QTY${list.PART_ID}" value="${list.NORMAL_QTY}" type="hidden" />${list.NORMAL_QTY}
						</td>
						
						<td align="center" nowrap>
							<input name="BUY_QTY${list.PART_ID}" id="BUY_QTY${list.PART_ID}" value="${list.BUY_QTY}" type="hidden" />${list.BUY_QTY}
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="APPLY_QTY${list.PART_ID}" id="APPLY_QTY${list.PART_ID}" value="${list.APPLY_QTY}" type="text"
								onchange="updateDtl('${list.DTL_ID}','${_sequenceNum.index+2}','${list.RETURN_ID}')" />
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="REMARK${list.PART_ID}" id="REMARK${list.PART_ID}" value="${list.REMARK}" type="text"
								onchange="updateDtl('${list.DTL_ID}','${_sequenceNum.index+2}','${list.RETURN_ID}')" />
						</td>
						<td>
							<input type="button" class="u-button" value="删除" onclick="deleteDtl('${list.DTL_ID}','${_sequenceNum.index+2}','${list.RETURN_ID}');" />
						</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table style="width: 100%;">
				<tr>
					<td align="center">
						<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="setCheckModel();" class="u-button" />
						<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="javascript:goback();" class="u-button" />
					</td>
				</tr>
			</table>

			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="4" style="background-color: #DAE0EE; font-weight: normal; color: #416C9B; padding: 2px; line-height: 1.5em; border: 1px solid #E7E7E7;">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> <font color="blue">配件信息</font>
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none; heigeht: 5px" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">编码：</td>
							<td>
								<input class="middle_txt" id="PART_OLDCODE" datatype="1,is_noquotation,30" name="PART_OLDCODE" type="text" />
							</td>

							<td class="right">名称：</td>
							<td>
								<input class="middle_txt" id="PART_CNAME" datatype="1,is_noquotation,30" name="PART_CNAME" type="text" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添加" onclick="addCells()" />
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
