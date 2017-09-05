<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt"%>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件移库申请修改</title>

<script type=text/javascript>
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/showPartStockBase.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '2%'},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
        dataIndex: 'PART_ID',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center', renderer: returnLocCode},
    {header: "批次号", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "移库数量<font color='red'>*</font>", align: 'center', renderer: insertApplyQtyInput}
];

var tidx = 0;
var tidxn = "";
function seled(value,meta,record){
	tidx++;
	tidxn = record.data.PART_ID + '_RNUM'+tidx;
    return "<input type='checkbox' value='" + tidxn + "' name='ck' id='ck_" + tidxn + "' />";
}
 
function returnLocCode(value, meta, record) {
    var inputVal = record.data.PART_ID + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME;
    inputVal += "," + record.data.BATCH_NO;
    var html = "<input type='hidden' value='" + inputVal + "' name='loc_" + tidxn + "' id='loc_" + tidxn + "'/>" + value;
    html += '<input type="hidden" value="'+record.data.STOCK_VENDER_ID+'" name="venderId_'+tidxn+'"/>';
    return html;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
  var output = '<input type="text" class="short_txt" onchange="check(this,\'' + tidxn + '\', \'#ck_\');" id="APPLY_QTY1' + tidxn + '" name="APPLY_QTY1' + tidxn + '" value="" size ="10" style="background-color:#FF9"/>\n';
  output += '<input name="NORMAL_QTY' + tidxn + '" id="NORMAL_QTY' + tidxn + '" value="' + record.data.NORMAL_QTY + '" type="hidden" />';
  return output;
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

function check(obj, partId, chkName) {
    var pattern1 = /[1-9]/;
    var value = '';
    if (!pattern1.exec(obj.value)) {
        MyAlert("移库数量是正整数且必须大于0！");
    }else{
    	var normalQty = $('#NORMAL_QTY'+partId).val();
    	if(parseInt(normalQty) < parseInt(obj.value)){
            MyAlert("移库数量不能大于可用库存数量！");
    	}else{
	        value = obj.value;
    	}
    }
    if(value != ''){
        $(chkName + partId)[0].checked = true;
    }else{
        $(chkName + partId)[0].checked = false;
    }
    obj.value = value;
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if ($("#whId").val() == "") {
        MyAlert("请选择移出仓库!");
        return;
    }
    if ($("#toWhId").val() == "") {
        MyAlert("请选择移入仓库!");
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
        //改变仓库之后就要删除移库明细,重新选择
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
        if (mt.rows[i].cells[1].firstChild.checked) {
	        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
	        var loc = document.getElementById("loc_" + partId).value;  //货位信息
            if (validateCell(partId, loc)) {
	            cn++;
	            var applyQty1 = $("#APPLY_QTY1" + partId).val();  //移库数量
	            var itemQty = mt.rows[i].cells[7].innerText;  //库存数量
	            var noramlQty = mt.rows[i].cells[8].innerText;  //可用数量
	            var pattern1 = /^[1-9][0-9]*$/;
	            if (applyQty1 == 0 || applyQty1 == "") {
	                MyAlert("第" + i + "行的移库数量是正整数且必须大于0！");
	                return;
	            }
	            if (!pattern1.exec(applyQty1)) {
	                //MyAlert("请录入正整数且必须大于0！");
	                applyQty1 = applyQty1.replace(/\D/g, '');
	                $("#APPLY_QTY1" + partId)	.focus();
	            }
	            if (parseInt(applyQty1) > parseInt(noramlQty)) {
	                MyAlert("第" + (i) + "行的移库数量不能大于可用数量,请重新输入!");
	                return;
	            }
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                
                addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, loc, itemQty);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[4].innerText + " 已存在于移库明细中!");
                $("#ck" + partId)[0].checked = false;
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
        	var loc2 = document.getElementById("LOC_NAME"+partIds[i].value).value;
            if (loc == loc2) {
                return false;
            }
        }
        return true;
    }
    return true;
}


function addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, loc, itemQty) {
    var locArr = loc.split(",");
	var locCode = locArr[2]; //货位编码
    var batchNo = locArr[4]; // 批次号
	
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
//     var cell12 = rowObj.insertCell(11);

    cell1.innerHTML = '<tr><td align="center" nowrap><input type="checkbox" value="' + partId + '" id="cell_' + partId + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>' + (tbl.rows.length - 1);
    cell3.innerHTML = '<td align="center"><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    
    var html = '<td align="center" nowrap><input name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname;
    html += '<input name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/></td>';
    cell4.innerHTML = html;
    // cell5.innerHTML = '<td align="center" nowrap><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell5.innerHTML = '<td align="center" nowrap><input name="LOC_' + partId + '" id="LOC_NAME' + partId + '" value="' + loc + '" type="hidden"/>'+locCode+'</td>';
    cell6.innerHTML = '<td align="center" nowrap><input name="BATCH_NO'+partId+'" value="'+batchNo+'" type="hidden" />'+batchNo+'</td>';
    cell7.innerHTML = '<td align="center" nowrap><input name="ITEM_QTY' + partId + '" id="ITEM_QTY' + partId + '" value="' + itemQty + '" type="hidden" />' + itemQty + '</td>';
    cell8.innerHTML = '<td align="center" nowrap>' + noramlQty + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty1 + '" type="text" class="short_txt" onchange="check(this,\'' + partId + '\', \'#cell_\');"/></td>';
    cell10.innerHTML = '<td align="center" nowrap><input class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell11.innerHTML = '<td><input  type="button" class="normal_btn"  name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i;
        tbl.rows[i].cells[10].innerHTML = '<td><input type="button" class="normal_btn" name="queryBtn4" value="删除" onclick="deleteTblRow(' + i + ');" /></td></tr>';
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

    if ("" == whValue && "收 起" == addPartViv.value) {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    }
    if ("" != whValue && "收 起" == addPartViv.value) {
        __extQuery__(1);
    }
}

function saveAction() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/modSaveMvOrder.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
        } else if (json.success == "success") {
            MyAlert("修改成功！", function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMvInit.do';
            });
        } else {
            MyAlert(json.Exception.message);
        }
    }
}

//保存
function save() {
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

    //切换件移库时必须分开库存件和装车件
    if ($('#whId').val() == '2014092298805416') {
        var zcflag = 0;
        var kcflag = 0;
        for (var i = 0; i < l; i++) {
            if (cb[i].checked) {
                var locCode = document.getElementById("LOC_NAME" + cb[i].value).value;//货位编码
                if (locCode.indexOf('QHJ') == 0) {
                    zcflag = 1;
                } else {
                    kcflag = 1
                }
            }
        }
        if (zcflag == 1 && kcflag == 0) {
            $("#flag").val() = 1;
        } else {
            $("#flag").val() = 0;
        }
        if ((zcflag + kcflag) == 2) {
            $("#flag").val() = 1;
            MyAlert("切换件移库时,库存件和装车件必须分开移库!");
            return;
        }
    }
    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验调整数量是否为空
            if (document.getElementById("APPLY_QTY" + cb[i].value).value == "") {
                msg += "请填写第" + (i + 1) + "行的移库数量!</br>";
                flag = true;
            } else {
                var returnQty = parseInt(document.getElementById("APPLY_QTY" + cb[i].value).value);
                var itemQty = parseInt(document.getElementById("ITEM_QTY" + cb[i].value).value);
                if (itemQty < returnQty) {
                    msg += "第" + (i + 1) + "行的移库数量不能大于库存!</br>";
                    flag = true;
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
        jQuery(".cname_" + ary[i]).parent("td").css({background: ""});
    }
    for (var i = 0; i < ary.length; i++) {
        if (s.replace(ary[i] + ",", "").indexOf(ary[i] + ",") > -1) {
            pflag = true;
            sid = "PART_CNAME" + ary[i];
            nclass = "cname_" + ary[i];
            var partCname = document.getElementById(sid).value;
            MyAlert("配件：" + partCname + " 重复!");
            break;
        }
    }
    if (pflag) {
        jQuery("." + nclass).parent("td").css({background: "red"});
        return false;
    }

    if (flag) {
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
    }
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加移库信息!</br>";
    }
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
            break;
        }
    }
    if (!flag) {
        msg += "请添加移库信息!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return;
    }
    MyConfirm("确定保存修改信息?", saveAction, []);
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}

function goBack() {
    btnDisable();
    window.location.href = '<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMvInit.do';
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
        //改变仓库之后就要删除移库明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 2; i--) {
            tbl.deleteRow(i);
        }
    }
}
</script>
</head>
<body>
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
						<td class="right">移出仓库：</td>
						<td>
							<select name="whId" id="whId" class="u-select" onchange="changeDiv();">
								<c:if test="${WHList!=null}">
									<c:forEach items="${WHList}" var="list">
										<c:choose>
											<c:when test="${main.WH_ID eq list.WH_ID}">
												<option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
											</c:when>
											<c:otherwise>
												<option value="${list.WH_ID }">${list.WH_CNAME }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
						</td>
						<td class="right">移入仓库：</td>
						<td>
							<select name="toWhId" id="toWhId" class="u-select"<%--onchange="changeDiv();"--%>>
								<c:if test="${WHList!=null}">
									<c:forEach items="${WHList}" var="list">
										<c:choose>
											<c:when test="${main.TOWH_ID eq list.WH_ID}">
												<option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
											</c:when>
											<c:otherwise>
												<option value="${list.WH_ID }">${list.WH_CNAME }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td class="right">备注：</td>
						<td colspan="5">
							<textarea style="width: 75%" id="remark" name="remark" cols="4" rows="4">${main.REMARK }</textarea>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="file" class="table_list" style="border-bottom: 1px;">
			<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>调拨明细</caption>
			<tr>
				<th>
					<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
				</th>
				<th>序号</th>
				<th>配件编码</th>
				<th>配件名称</th>
<!-- 					<th>件号</th> -->
				<th>货位</th>
				<th>批次号</th>
				<th>库存</th>
				<th>可用数量</th>
				<th>
					移库数量<font color="red">*</font>
				</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
			<c:if test="${list !=null}">
				<c:forEach items="${list}" var="list" varStatus="_seq">
					<tr class="table_list_row1">
						<td align="center" nowrap>
							<input type="checkbox" value="${list.PART_IDS}${_seq.index+1}" id="cell_${_seq.index+1}" name="cb" checked="checked" />
							<input id="idx_${list.PART_IDS}${_seq.index+1}" name="idx_${list.PART_IDS}${_seq.index+1}" value="${_seq.index+1}" type="hidden" />
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}</td>
						<td align="center" nowrap>
							<input name="PART_OLDCODE${list.PART_IDS}${_seq.index+1}" id="PART_OLDCODE${list.PART_IDS}${_seq.index+1}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
							<input name="PART_CODE${list.PART_IDS}${_seq.index+1}" id="PART_CODE${list.PART_IDS}${_seq.index+1}" value="${list.PART_CODE}" type="hidden" />
						</td>
						<td align="center" nowrap>
							<input name="PART_CNAME${list.PART_IDS}${_seq.index+1}" id="PART_CNAME${list.PART_IDS}${_seq.index+1}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
						</td>
						<td align="center" nowrap>
							<input name="LOC_${list.PART_IDS}${_seq.index+1}" id="LOC_NAME${list.PART_IDS}${_seq.index+1}" value="${list.PART_ID},${list.LOC_ID},${list.LOC_CODE},${list.LOC_NAME},${list.BAT_ID}" type="hidden" />
							<input type="hidden" value="${list.VENDER_ID}" name="venderId_${list.PART_IDS}${_seq.index+1}"/>
							${list.LOC_CODE}
						</td>
						<td align="center" nowrap>
							${list.BAT_ID}
						</td>
						<td align="center" nowrap>
							<input name="ITEM_QTY${list.PART_IDS}${_seq.index+1}" id="ITEM_QTY${list.PART_IDS}${_seq.index+1}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
						</td>
						<td align="center" nowrap>
							<input name="NORMAL_QTY${list.PART_IDS}${_seq.index+1}" id="NORMAL_QTY${list.PART_IDS}${_seq.index+1}" value="${list.NORMAL_QTY}" type="hidden" />${list.NORMAL_QTY}
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="APPLY_QTY${list.PART_IDS}${_seq.index+1}" id="APPLY_QTY${list.PART_IDS}${_seq.index+1}" value="${list.APPLY_QTY}" onchange="check(this,'" + ${list.PART_IDS}${_seq.index+1} + "', '#cell_');" type="text" />
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="REMARK${list.PART_IDS}${_seq.index+1}" id="REMARK${list.PART_IDS}${_seq.index+1}" value="${list.REMARK}" type="text" />
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
					<input class="u-button" type="button" value="保 存" id="saveButton" name="button1" onclick="save();">
					<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">

				</td>
			</tr>
		</table>
		<fieldset>
			<legend style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
				<th colspan="6">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件查询
					<input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
				</th>
			</legend>
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
						</td class="right">
						件号：
						</td>
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
		</fieldset>
	</form>
	</div>
</body>
</html>
