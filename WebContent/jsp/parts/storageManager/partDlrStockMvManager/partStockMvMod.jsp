<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt"%>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件移库申请修改</title>

<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/showPartStockBase.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '2%'},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
        dataIndex: 'PART_ID',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "调拨数量<font color='red'>*</font>", align: 'center', renderer: insertApplyQtyInput}
];

function seled(value, meta, record) {
    var partId = record.data.PART_ID + "_" + record.data.LOC_ID;
    return "<input type='checkbox' value='" + partId + "' name='ck' id='ck" + partId + "' onclick='chkPart()'/>";
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

function chkPart1() {
    var cks = document.getElementsByName('cb');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
    var partId = record.data.PART_ID + "_" + record.data.LOC_ID;
    var output = '<input type="text" class="short_txt" onchange="check(this,\'' + partId + '\');onchangeVlidateSaleQty(this);" id="APPLY_QTY1' + partId + '" name="APPLY_QTY1' + partId + '" value="" size ="10" style="background-color:#FF9"/>\n';
    output += '<input type="hidden"  id="LOC_ID' + partId + '" name="LOC_ID' + partId + '" value="' + record.data.LOC_ID + '" />\n';
    return output;
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

function check(obj, partId) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (obj.value == 0 || obj.value == "") {
        MyAlert("移库数量是正整数且必须大于0！");
        return;
    }
    if (!pattern1.exec(obj.value)) {
        //MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    $("#ck" + partId)[0].checked = true;
    chkPart();
}

// 点击“增加”按钮
function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if ($("#FROM_WH_ID")[0].value == "") {
        MyAlert("请选择调拨经销商！");
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

function changeDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    }
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;
    if (len > 2) {
        //改变仓库之后就要删除调拨明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 2; i--) {
            tbl.deleteRow(i);
        }
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
            var applyQty1 = $("#APPLY_QTY1" + partId)[0].value;  //调拨数量
            var itemQty = mt.rows[i].cells[5].innerText;  //库存数量
            var noramlQty = mt.rows[i].cells[6].innerText;  //可用数量
            var pattern1 = /^[1-9][0-9]*$/;
            if (applyQty1 == 0 || applyQty1 == "") {
                MyAlert("第" + i + "行的调拨数量是正整数且必须大于0！");
                return;
            }
            if (!pattern1.exec(applyQty1)) {
                //MyAlert("请录入正整数且必须大于0！");
                applyQty1 = applyQty1.replace(/\D/g, '');
                $("#APPLY_QTY1" + partId).focus();
            }
            if (parseInt(applyQty1) > parseInt(noramlQty)) {
                MyAlert("第" + (i) + "行的调拨数量不能大于可用数量,请重新输入!");
                return;
            }
            if (validateCell(partId)) {
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, itemQty);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[4].innerText + " 已存在于调拨明细中!");
                $("#ck" + partId)[0].checked = false;
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

function addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, itemQty) {
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

    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" >'+(tbl.rows.length - 2)+'</td>';
    cell3.innerHTML = '<td align="center"><input name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="center" nowrap><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="ITEM_QTY' + partId + '" id="ITEM_QTY' + partId + '" value="' + itemQty + '" type="hidden" />' + itemQty + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="NORMAL_QTY' + partId + '" id="NORMAL_QTY' + partId + '" value="' + noramlQty + '" type="hidden" />' + noramlQty + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty1 + '" type="text" class="short_txt"/></td>';
    cell9.innerHTML = '<td align="center" nowrap><input class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell10.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></TR>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
        tbl.rows[i].cells[10].innerHTML = "<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}
//保存申请
function save() {
    if ($("#FROM_WH_ID")[0].value == "") {
        MyAlert("请选择移出库房!")
        return;
    }
    if ($("#remark")[0].value == "") {
        MyAlert("请填写调拨原因!");
        return;
    }
    var chk = document.getElementsByName('cb');
    var l = chk.length;
    var cnt = 0;
    
    //切换件调拨时必须分开库存件和装车件
    if ($('#FROM_WH_ID')[0].value == '2014092298805416') {
        var zcflag = 0;
        var kcflag = 0;
        for (var i = 0; i < l; i++) {
            if (chk[i].checked) {
                var locCode = document.getElementById("LOC_NAME" + chk[i].value).value;//货位编码
                if (locCode.indexOf('QHJ') == 0) {
                    zcflag = 1;
                } else {
                    kcflag = 1
                }
            }
        }
        if (zcflag == 1 && kcflag == 0) {
            $("#flag")[0].value = 1;
        } else {
            $("#flag")[0].value = 0;
        }
        if ((zcflag + kcflag) == 2) {
            MyAlert("切换件调拨时,库存件和装车件必须分开调拨!");
            return;
        }
    }

    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var applyQty = document.getElementById("APPLY_QTY" + chk[i].value).value;//调拨数量
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty)) {
                MyAlert("第" + (i + 1) + "行的调拨数量只能输入非零的正整数!");
                return;
            }
            var normalQty = document.getElementById("NORMAL_QTY" + chk[i].value).value;//库存数量
            if (parseInt(applyQty) > parseInt(normalQty)) {
                MyAlert("第" + (i + 1) + "行的调拨数量不能大于可用数量,请重新输入!");
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择调拨明细！");
        return;
    }
    MyConfirm("确定保存?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/modSaveMvOrder.json?';
        sendAjax(url, getResult, 'fm');
    });
}
function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/partMvInit.do';
            });
        }
        else if (error) {
            MyAlert(error);
        }
        else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回查询页面
function goback() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/partMvInit.do';
}
   
   // 获取经销商
function getDealer() {
   	OpenHtmlWindow(g_webAppName+'/jsp/parts/storageManager/partDlrStockMvManager/selectWhDealer.jsp',730,390);
   	changeDiv();
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
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="orgId" id="orgId" value="${main.ORG_ID }" />
			<input type="hidden" name="orgName" id="orgName" value="${main.ORG_NAME }" />
			<input type="hidden" name="orgCode" id="orgCode" value="${main.ORG_CODE }" />
			<input type="hidden" name="CHG_ID" id="CHG_ID" value="${main.CHG_ID }" />
			<input type="hidden" name="flag" id="flag" value="${main.FLAG }" />

			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件仓储管理 &gt; 配件调拨申请 &gt; 修改
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" />移库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">移库单号：</td>
							<td>${main.CHG_CODE}</td>
							<td class="right">制单人：</td>
							<td>${main.NAME}</td>
							<td class="right">制单日期：</td>
							<td>
								<fmt:formatDate value="${main.CREATE_DATE}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
						<tr>
							<td class="right">调拨经销商：</td>
							<td colspan="5">
								<input type="hidden" id="FROM_WH_ID" name="fromWhId" value="${main.WH_ID }">
								<input type="hidden" id="FROM_ORG_ID" name="fromOrgId" value="${main.ORG_ID }">
								<input type="hidden" id="FROM_ORG_CODE" name="fromOrgCode" value="${main.ORG_CODE }">
								<input type="text" class="middle_txt" id="FROM_ORG_NAME" name="fromOrgName" value="${main.ORG_NAME }" style="width: 230px;" readonly>
								<input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...' onclick="getDealer();" />
								<font style="color: red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="5">
								<textarea class="form-control" name="remark" id="remark" style="width: 80%; display: inline;" rows="4">${main.REMARK }</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px;">
				<tr>
					<th colspan="12" align="left">
						<img src="<%=contextPath%>/img/nav.gif" />移库明细
					</th>
				</tr>
				<tr class="table_list_row0">
					<td>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
					</td>
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>件号</td>
					<td>库存数量</td>
					<td>可用数量</td>
					<td>
						移库数量<font color="red">*</font>
					</td>
					<td>备注</td>
					<td>操作</td>
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
							<input type="checkbox" value="${list.PART_IDS}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" />
							<input id="idx_${list.PART_IDS}" name="idx_${list.PART_IDS}" value="${_sequenceNum.index+1}" type="hidden" />
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}</td>
						<td>
							<input name="PART_OLDCODE${list.PART_IDS}" id="PART_OLDCODE${list.PART_IDS}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}</td>
						<td nowrap>
							<input name="PART_CNAME${list.PART_IDS}" id="PART_CNAME${list.PART_IDS}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}</td>
						<td nowrap>
							<input name="PART_CODE${list.PART_IDS}" id="PART_CODE${list.PART_IDS}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}</td>
						<td align="center" nowrap>
							<input name="ITEM_QTY${list.PART_IDS}" id="ITEM_QTY${list.PART_IDS}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}</td>
						<td align="center" nowrap>
							<input name="NORMAL_QTY${list.PART_IDS}" id="NORMAL_QTY${list.PART_IDS}" value="${list.NORMAL_QTY}" type="hidden" />${list.NORMAL_QTY}</td>
						<td align="center" nowrap>
							<input class="short_txt" name="APPLY_QTY${list.PART_IDS}" id="APPLY_QTY${list.PART_IDS}" value="${list.APPLY_QTY}" onchange="dataTypeCheck('${list.PART_IDS}',this)" type="text" />
						</td>
						<td align="center" nowrap>
							<input class="middle_txt" name="REMARK${list.PART_IDS}" id="REMARK${list.PART_IDS}" value="${list.REMARK}" type="text" />
						</td>
						<td>
							<input type="button" class="u-button" name="deleteBtn" value="删 除" onclick="deleteTblRow('${_sequenceNum.index+2}');" />
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
						<input class="u-button" type="button" value="保 存" id="saveButton" name="button1" onclick="save();">
						<input class="u-button" type="button" value="返 回" name="button1" onclick="goback();">
					</td>
				</tr>
			</table>
			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件查询
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none; heigeht: 5px" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" id="partOldcode" name="partOldcode" type="text" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" id="partCname" name="partCname" type="text" />
							</td>
							<td class="right">件号：</td>
							<td>
								<input class="middle_txt" id="partCode" name="partCode" type="text" />
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
