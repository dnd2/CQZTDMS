<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String contextPath = request.getContextPath();
			String soCode = (String) request.getAttribute("soCode");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购退货申请</title>
<style>
    table.table_query #headTable{background-color: transparent}
    table.table_query .bottom-button,table .bottom-button{padding: 10px 0}
</style>
<script type="text/javascript">

var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryOrderInInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'IN_ID', align: 'center', width: '33px', renderer: seled},
//     {header: "入库单号", dataIndex: 'IN_CODE', align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: center'},
    {header: "配件种类", dataIndex: 'PRODUCE_STATE', style: 'text-align: center', renderer: getItemValue},
    {header: "货位", dataIndex: 'LOC_CODE', style: 'text-align: center'},
    {header: "批次号", dataIndex: 'BATCH_NO', style: 'text-align: center'},
    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center',renderer: insertInQtyInput},
    {header: "库存数量", dataIndex: 'ITEM_QTY', align: 'center'},
    {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center',renderer: insertNormalQtyInput},
    {header: "退货数量<font color='red'>*</font>", align: 'center', renderer: insertApplyQtyInput},
    {header: "已申请退货数量", dataIndex: 'APPLY_QTY2', align: 'center'},
    {header: "已退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', align: 'center'}
];

function seled(value, meta, record) {
	var loc = record.data.PART_ID + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME + "," + record.data.BATCH_NO;
	var html ="<input type='checkbox' value='" + value + "' name='ck' id='ck"+record.data.IN_ID+"' onclick='chkPart(\'ck\')'/>";
	html += '<input type="hidden" value="'+loc+'" id="LOC_H'+record.data.IN_ID+'" />';
    return html;
}

//插入文本框
function insertInQtyInput(value, meta, record) {
    var inId = record.data.IN_ID;
    var output = '<input type="hidden" id="IN_QTYH' + inId + '" value="' + value + '"/>\n'+value;
    output += '<input type="hidden" id="APPLY_QTY2H' + inId + '" value="' + record.data.APPLY_QTY2 + '"/>';
    output += '<input type="hidden" id="RETURN_QTYH' + inId + '" value="' + record.data.RETURN_QTY + '"/>';
    return output;
}

//插入文本框
function insertNormalQtyInput(value, meta, record) {
    var inId = record.data.IN_ID;
    var output = '<input type="hidden" id="NORMAL_QTYH' + inId + '" value="' + value + '"/>\n'+value;
    return output;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
    var inId = record.data.IN_ID;
	var loc = record.data.PART_ID + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME + "," + record.data.BATCH_NO;
    var output = '<input type="text" class="short_txt" onchange="check(this,\''+inId+'\', \'H\');" id="APPLY_QTY1' + inId + '" name="APPLY_QTY1' + loc + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}

function chkPart(chkId) {
    var cks = document.getElementsByName(chkId);
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

function check(obj, inId, h) {
	var pattern1 = /^[1-9][0-9]*$/;
	if (obj.value == 0||obj.value=="") {
        MyAlert("退货数量是正整数且必须大于0！");
        return;
    }
    if (!pattern1.exec(obj.value)) {
        MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
        return;
    }
	var loc = $('#LOC_'+inId).val();
	var countEle = document.getElementsByName(obj.name);
	var count = 0;
	for (var i = 0; i < countEle.length; i++){
		var value = countEle[i].value;
		if(value && value != ""){
			count += parseInt(value);
		}
	}
	var normalQty = $('#NORMAL_QTY'+h+inId).val();
    if(normalQty < count){
    	obj.value = "";
        MyAlert("退货数量不能大于可用库存！");
        return;
    }
    var inQty = $('#IN_QTY'+h+inId).val();
    var returnQty = $('#RETURN_QTY'+h+inId).val();
    var applyQty = $('#APPLY_QTY2'+h+inId).val();
// 	if(parseInt(returnQty) + parseInt(applyQty) + parseInt(obj.value) > parseInt(inQty)){
	if(parseInt(applyQty) + parseInt(obj.value) > parseInt(inQty)){
    	obj.value = "";
        MyAlert("当前退货数量与已退货数量之和不能大于入库数量！");
        return;
	}
    $('#APPLY_QTY'+inId).val(obj.value);
    var chkId = '';
	if(h == 'H'){
		chkId = 'ck';
	    chkPart(chkId);
	}else{
		chkId = 'cb';
	}
    $("#"+chkId+inId)[0].checked=true;
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
    	var inCode = $('#IN_CODE').val();
    	if(!inCode || inCode == ""){
    		MyAlert('请选择入库单！');
    		return;
    	}
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        
        var pageHight = document.documentElement.clientHeight;
        var bodyHeight = document.body.clientHeight + 200;
        var addNum = 2;
        if(bodyHeight > pageHight){
        	addNum += 15;
        }
   		document.getElementById('myGrid').style.width = (document.getElementById('headTable').clientWidth - addNum) + 'px';
        __extQuery__(1);
    }

}


function addCells() {
    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    $("#ckAll")[0].checked = true;
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var inId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            var rValue = mt.rows[i].cells[11].firstChild.value;
        	if (rValue == 0||rValue=="") {
                MyAlert("第" + i + "行的退货数量是正整数且必须大于0！");
                mt.rows[i].cells[1].firstChild.checked=false;
                return;
            }
            if (validateCell(inId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
               	var partCode = mt.rows[i].cells[4].innerText;  //件号
                var locCode = mt.rows[i].cells[6].innerText;  //货位
                var batchNo = mt.rows[i].cells[7].innerText;  //批次号
                var inQty = mt.rows[i].cells[9].innerText;  //入库数量
                var normalQty = mt.rows[i].cells[11].innerText;  //可用数量
                var applyQty1 = $("#APPLY_QTY1"+inId).val();  // 退货数量
                var applyQty = mt.rows[i].cells[13].innerText;  // 已申请退货数量
                var returnQty = mt.rows[i].cells[14].innerText;  // 已退货数量
                addCell(inId, partCode, partOldcode, partCname, locCode, batchNo, inQty, normalQty, applyQty1, applyQty, returnQty);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[4].innerText + " 已存在于退货明细中!");
                mt.rows[i].cells[1].firstChild.checked=false;
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件!");
    }
}
function validateCell(inId) {
    var inIds = document.getElementsByName("cb");
    if (inIds && inIds.length > 0) {
        for (var i = 0; i < inIds.length; i++) {
            if (inId == inIds[i].value) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(inId, partCode, partOldcode, partCname, locCode, batchNo, inQty, normalQty, applyQty1, applyQty, returnQty) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    rowObj.className = "table_list_row2";
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

    var loc = $('#LOC_'+inId).val();
    
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + inId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + inId + '" name="idx_' + inId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>' + (tbl.rows.length - 1);
    cell3.innerHTML = '<td align="center"><input   name="PART_OLDCODE' + inId + '" id="PART_OLDCODE' + inId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input name="PART_CNAME' + inId + '" id="PART_CNAME' + inId + '" value="' + partCname + '" type="hidden" class="cname_' + inId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="center" nowrap><input name="PART_CODE' + inId + '" id="PART_CODE' + inId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input type="hidden" value="'+loc+'" id="LOC_'+inId+'" name="LOC_'+inId+'" />'+locCode+'</td>';
    cell7.innerHTML = '<td align="center" nowrap>'+batchNo+'</td>';
    cell8.innerHTML = '<td align="center" nowrap><input  onchange="check(this,\''+inId+'\', \'\');" name="S_APPLY_QTY' + loc + '" id="S_APPLY_QTY' + inId + '" value="'+applyQty1+'" type="text" class="short_txt"/></td>';
    var html = '<td align="center" nowrap><input type="hidden" id="APPLY_QTY' + inId + '" name="APPLY_QTY' + inId + '" value="'+applyQty1+'" />';
    html += '<input type="hidden" id="IN_QTY' + inId + '" name="IN_QTY' + inId + '" value="' + inQty + '"/>' + inQty + '</td>';
    cell9.innerHTML = html;
    cell10.innerHTML = '<td align="center" nowrap><input type="hidden" id="APPLY_QTY2'+inId+'" name="APPLY_QTY2'+inId+'" value="'+applyQty+'" />' + applyQty + '</td>';
    cell11.innerHTML = '<td align="center" nowrap><input type="hidden" id="RETURN_QTY'+inId+'" name="RETURN_QTY'+inId+'" value="'+returnQty+'" />' + returnQty + '</td>';
    cell12.innerHTML = '<td align="center" nowrap><input type="hidden" id="NORAML_QTY' + inId + '" name="NORAML_QTY' + inId + '" value="' + normalQty + '"/>' + normalQty + '</td>';
    cell13.innerHTML = '<td align="center" nowrap><input class="short_txt" name="REMARK' + inId + '" id="REMARK' + inId + '" type="text"/></td>';
    cell14.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn4" value="删除" onclick="deleteTblRow(this);" /></td></tr>';

}

function deleteTblRow(obj) {
    var tbl = document.getElementById('file');
    //获取tr对象;
    var tr = obj.parentNode.parentNode;
    tr.parentNode.removeChild(tr);
    //重新生成行号;
    for (var i = 1; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i;
    }
}

// 移除所有退货明细
function onChangeInCode(){
    var tbl = document.getElementById('file');
    for (var i = 1; i < tbl.rows.length; i++) {
        tbl.deleteRow(i);
    }
    var partDiv = document.getElementById("partDiv");
    if (partDiv.style.display == "block") {
        __extQuery__(1);
    }else{
    	addPartDiv();	
    }
}

//提交申请
function fmsubmit() {
    if ($("#reason").val() == "") {
        MyAlert("请填写退货原因!");
        return;
    }
    var ck = document.getElementsByName('cb');
    var l = ck.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (ck[i].checked) {
            cnt++;
            var inId = ck[i].value;
            var obj = document.getElementById('APPLY_QTY'+inId);
        	var pattern1 = /^[1-9][0-9]*$/;
        	if (obj.value == 0||obj.value=="") {
                MyAlert("第"+(i+1)+"行退货数量是正整数且必须大于0！");
                return;
            }
            if (!pattern1.exec(obj.value)) {
                MyAlert("第"+(i+1)+"行请录入正整数且必须大于0！");
                obj.value = obj.value.replace(/\D/g, '');
                obj.focus();
                return;
            }
        	var loc = $('#LOC_'+inId).val();
        	var countEle = document.getElementsByName(obj.name);
        	var count = 0;
        	for (var i = 0; i < countEle.length; i++){
        		var value = countEle[i].value;
        		if(value && value != ""){
        			count += parseInt(value);
        		}
        	}
        	var normalQty = $('#NORAML_QTY'+inId).val();
            if(normalQty < count){
            	obj.value = "";
                MyAlert("第"+(i+1)+"行退货数量不能大于可用库存！");
                return;
            }
            var inQty = $('#IN_QTY'+inId).val();
            var returnQty = $('#RETURN_QTY'+inId).val();
            var applyQty = $('#APPLY_QTY2'+inId).val();
//         	if(parseInt(returnQty) + parseInt(applyQty) + parseInt(obj.value) > parseInt(inQty)){
       		if(parseInt(applyQty) + parseInt(obj.value) > parseInt(inQty)){
            	obj.value = "";
                MyAlert("第"+(i+1)+"行当前退货数量与已退货数量之和不能大于入库数量！");
                return;
        	}
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要退货明细！");
        return;
    }

    MyConfirm("确定修改退货信息?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/modReturnOrder.json';
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
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInit.do';
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

function clearInput() {//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInit.do';
}

function showPartVender(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/venderSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,450);
}

// 选择入库单
function showInCode(inCode) {
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partOemReturnApply/inCodeSelect.jsp", 730, 550);
}

$(function() {
    var Fun = Common.FunHelper;
    Fun.sizeArgs = [
        { aEl: '#file', rEl: '.form-panel' },
        { aEl: '#myGrid', rEl: '.form-panel', pos: 20 }
    ];
    Fun.setElSize( 'width', 0 );
});
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件退货管理&gt;采购退货申请&gt;申请
		</div>
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="ids" value="${ids}" />
			<input type="hidden" name="returnId" id="returnId" value="${po.RETURN_ID }" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po.RETURN_CODE }
								<input type="hidden" name="returnCode" id="returnCode" value="${returnCode }" />
							</td>
							<td class="right">制单单位：</td>
							<td>${po.ORG_NAME }</td>
						</tr>
						<tr>
							<td class="right">退货单位：</td>
							<td>${po.ORG_NAME }</td>
							<td class="right">入库单：</td>
							<td>
								<input id="IN_CODE" name="IN_CODE" type="text" class="middle_txt" readonly="readonly" value="${po.CHECK_CODE }"/>
								<input class="mark_btn" type="button" value="&hellip;" onclick="showInCode('IN_CODE');"/>
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">
								<textarea name="reason" id="reason" class="form-control" style="width: 80%; display: inline;" rows="4" datatype="0,is_null,20000">${po.REMARK }</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list">
				<caption>
					<img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" />退货明细
				</caption>
				<tr>
					<th>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
					</th>
					<th>序号</th>
					<th>配件编码</th>
					<th>配件名称</th>
					<th>件号</th>
					<th>货位</th>
					<th>批次号</th>
					<th>
						退货数量<font color="red">*</font>
					</th>
					<th>入库数量</th>
					<th>已申请退货数量</th>
					<th>已退货数量</th>
					<th>可用数量</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
				<c:if test="${dtlList !=null}">
					<c:forEach items="${dtlList}" var="list" varStatus="_seq" step="1">
						<tr class="table_list_row2">
							<td align="center" nowrap>
								<input type="checkbox" value="${list.IN_ID}" id="cb${list.IN_ID}" name="cb" checked="checked" onclick="chkPart1();" />
								<input name="IN_CODE" id="IN_CODE${list.IN_ID}" value="${list.IN_CODE }" type="hidden" />
							</td>
							<td align="center" nowrap>${_seq.index+1}
								<input id="idx_${list.IN_ID}" name="idx_${list.IN_ID}" value="${_seqe.index+1}" type="hidden">
							</td>
							<td align="center">
								<input name="PART_OLDCODE${list.IN_ID}" id="PART_OLDCODE${list.IN_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
							</td>
							<td align="center" nowrap>
								<input name="PART_CNAME${list.IN_ID}" id="PART_CNAME${list.IN_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.IN_ID}" />${list.PART_CNAME}
							</td>
							<td align="center" nowrap>
								<input name="PART_CODE${list.IN_ID}" id="PART_CODE${list.IN_ID}" value="${list.IN_ID}" type="hidden" />${list.PART_CODE}
							</td>
							<td align="center" nowrap>
								<input name="LOC_${list.IN_ID}" id="LOC_${list.IN_ID}" value="${list.LOC}" type="hidden" />
								${list.LOC_CODE}
							</td>
							<td align="center" nowrap>
								${list.BATCH_NO}
							</td>
							<td align="center" nowrap>
								<input name="S_APPLY_QTY${list.LOC}" id="S_APPLY_QTY${list.IN_ID}" value="${list.APPLY_QTY}" type="text" class="short_txt" onchange="check(this,'${list.IN_ID}', '');" />
								<input id="APPLY_QTY${list.IN_ID}" name="APPLY_QTY${list.IN_ID}" value="${list.APPLY_QTY}" type="hidden" />
							</td>
							<td align="center" nowrap>
								<input name="IN_QTY${list.IN_ID}" id="IN_QTY${list.IN_ID}" value="${list.IN_QTY}" type="hidden" />
								${list.IN_QTY}
							</td>
							<td align="center" nowrap>
								<input name="APPLY_QTY2${list.IN_ID}" id="APPLY_QTY2${list.IN_ID}" value="${list.APPLY_QTY2}" type="hidden" />
								${list.APPLY_QTY2}
							</td>
							<td align="center" nowrap>
								<input name="RETURN_QTY${list.IN_ID}" id="RETURN_QTY${list.IN_ID}" value="${list.A_RETURN_QTY}" type="hidden"/>
								${list.A_RETURN_QTY}
							</td>
							<td align="center" nowrap>
								<input name="NORMAL_QTY${list.IN_ID}" id="NORMAL_QTY${list.IN_ID}" value="${list.NORMAL_QTY}" type="hidden" />
								${list.NORMAL_QTY}
							</td>
							<td align="center" nowrap>
								<input class="short_txt" name="REMARK${list.IN_ID}" id="REMARK${list.IN_ID}" type="text" value="${list.REMARK}" />
							</td>
							<td align="center" nowrap>
								<input type="button" class="u-button" name="queryBtn4" value="删除" onclick="deleteTblRow(this);" />
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<table style="width: 100%;">
				<tr>
					<td class="bottom-button" align="center">
						<input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="fmsubmit();" class="u-button" />
						<input type="button" name="saveBtn2" id="saveBtn2" value="返 回" onclick="javascript:goback();" class="u-button" />
					</td>
				</tr>
			</table>
			<FIELDSET class="form-fieldset">
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6" style="background-color: #DAE0EE; font-weight: normal; color: #416C9B; padding: 2px; line-height: 1.5em; border: 1px solid #E7E7E7;">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> <font color="blue">入库信息</font>
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none; heigeht: 5px;" id="partDiv">
					<table id="headTable" class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME" />
							</td>
							<td class="right">配件种类：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("PRODUCE_STATE",
								<%=Constant.PART_PRODUCE_STATE%>
									, "", true, "u-select", "", "false", '');
								</script>
							</td>
						</tr>

						<tr>
							<td class="center bottom-button" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1);" />
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
