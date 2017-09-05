<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style>fieldset.form-fieldset{margin-top: 10px;}#partDiv .table_query{background-color:transparent}</style>
<script type=text/javascript>
var myPage;
var url = g_webAppName+"/parts/baseManager/partSalePrice/PartSalePriceChange/queryPartList.json";
var title = null;
var	columns = [
        {header: "序号", align:'center', renderer:getIndex},
     	{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
     	{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
     	{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
     	{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
     	{header: "变更前服务站价格(元)", dataIndex: 'SALE_PRICE1', align:'center'},
     	{header: "变更后服务站价格(元)", dataIndex: 'SALE_PRICE1', align:'center',renderer:returnPrice1},
     	{header: "变更前零售价(元)", dataIndex: 'SALE_PRICE2', align:'center'},
     	{header: "变更后零售价(元)", dataIndex: 'SALE_PRICE2', align:'center',renderer:returnPrice2}
     ];


function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck_" + value + "' />";
}
function returnPrice1(value, meta, record) {
    var partId = record.data.PART_ID;
    var text = "<input name='price1_" + partId + "' id='price1_" + partId + "' type='text' value='"+value+"' onblur='dataCheck1(this,\""+partId+"\");' style='text-align: right; background-color: rgb(255, 255, 153);' class='short_txt' size='10'datatype='1,is_double,10' decimal='2'/>";
    return String.format(text);
}
function returnPrice2(value, meta, record) {
    var partId = record.data.PART_ID;
    var text = "<input name='price2_" + partId + "' id='price2_" + partId + "' type='text' value='"+value+"' onblur='dataCheck1(this,\""+partId+"\");' style='text-align: right; background-color: rgb(255, 255, 153);' class='short_txt' size='10'datatype='1,is_double,10' decimal='2' />";
    return String.format(text);
}
function dataCheck1(obj, partId) {
    var value = obj.value;
    if(value.trim() == ""){
    	document.getElementById("ck_" + partId).checked = false;
    	return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        document.getElementById("ck_" + partId).checked = false;
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
    //var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            if (validateCell(partId)) {
                var partCode = mt.rows[i].cells[2].innerText;  //配件编码
                var partOldcode = mt.rows[i].cells[3].innerText;  //件号
                var partCname = mt.rows[i].cells[4].innerText;  //配件名称
                var price1 = mt.rows[i].cells[5].innerText;  //变更前服务站价格(元)
                var price1After = document.getElementById("price1_" + partId).value;  //变更后服务站价格(元)
                var price2 = mt.rows[i].cells[7].innerText;  //变更前零售价(元)
                var price2After = document.getElementById("price2_" + partId).value;  //变更后零售价(元)
                addCellAppend(partId, partCode,partOldcode, partCname, price1, price1After,price2,price2After);
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

function addCellAppend(partId, partCode,partOldcode, partCname, price1, price1After,price2,price2After){
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
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true"  /></td>';
    cell2.innerHTML = '<td align="center" nowrap><span>' + (tbl.rows.length - 1) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td align="center" nowrap><input   name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input   name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell5.innerHTML = '<td align="center" nowrap><input   name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="price1_' + partId + '" id="price1_' + partId + '" value="' + price1 + '" type="hidden" />' + price1 + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="price1After_' + partId + '" id="price1After_' + partId + '" value="' + price1After + '" type="hidden" />'+price1After+'</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="price2_' + partId + '" id="price2_' + partId + '" value="' + price2 + '" type="hidden" />' + price2 + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="price2After_' + partId + '" id="price2After_' + partId + '" value="' + price2After + '" type="hidden" />'+price2After+'</td>';
    cell10.innerHTML = '<td align="center" nowrap><input type="button" class="u-button"  name="delBtn" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';
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
function SaveConfirm() {
	var tb_file = document.getElementById("file");
	var len = tb_file.rows.length;
	if(len < 2){
		MyAlert("请选择配件");return;
	}
	var dateFrom = document.getElementById("dateFrom").value;
	var dateTo = document.getElementById("dateTo").value;
	if(dateFrom.trim()=="" || dateTo.trim()==""){
		MyAlert("请选择有效日期");return;
	}
    MyConfirm('确定保存?', miscSave, []);
}


//保存杂项入库单
function miscSave() {
    disableAllClEl();
    var url = g_webAppName+"/parts/baseManager/partSalePrice/PartSalePriceChange/savePartApply.json";
    makeNomalFormCall(url, getResult, 'fm');
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
            MyAlert(success);
            window.history.back();
        }
        else {
            MyAlert(exceptions.message);
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
function check(value) {
    if (isNaN($(value).value)) {
        MyAlert("请录入正整数！");
        $(value).value = $(value).value.replace(/\D/g, '');
        $(value).focus();
    }
}

$(function(){
    __extQuery__(1);
    
    var form = $('#fm'),
        grid = $('#myGrid');
    grid.width(form.width() - 40);    
    $(window).resize(function() {
        grid.width(form.width() - 40);
    });
});

function isCloseDealerTreeDiv() {}
</script>

</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 技术相关信息维护 &gt; 配件价格变更申请 (新增)
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">有效期：</td>
							<td style="width: 350px;">
								<input style="height: 30px;" id="dateFrom" name="dateFrom" datatype="1,is_date,10" class="Wdate middle_txt" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'dateTo\')}'})"/>
								至 
								<input style="height: 30px;" id="dateTo" name="dateTo" datatype="1,is_date,10" class="Wdate middle_txt" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'dateFrom\')}'})"/>
							</td>
							<td class="right">备注：</td>
							<td>
								<textarea class="form-control" style="width: 80%;" name="textarea1" id="textarea1" rows="3"></textarea>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button" type="button" value="保 存" onclick="SaveConfirm();">
								&nbsp;
								<input class="u-button" type="button" value="返 回" onclick="window.history.back();" />
								&nbsp;
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list">
                <caption><img class="panel-icon" src="<%=contextPath%>/img/nav.gif" />配件价格变更详细</caption>
				<tr>
					<th align="center" width="2%">
						<input type="checkbox" checked name="ckAll" id="ckAll" onclick="selectAll()" />
					</th>
					<th align="center" width="40">序号</th>
					<th align="center">配件件号</th>
					<th align="center">配件编码</th>
					<th align="center">配件名称</th>
					<th align="center">变更前服务站价格(元)</th>
					<th align="center">变更后服务站价格(元)</th>
					<th align="center">变更前零售价(元)</th>
					<th align="center">变更后零售价(元)</th>
					<th align="center">操作</th>
				</tr>
			</table>
			<FIELDSET class="form-fieldset">
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件查询
						<input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="heigeht: 5px; display: none;" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">件号：</td>
							<td width="20%" align="left">
								<input class="middle_txt" id="partCode" datatype="1,is_noquotation,30" name="partCode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
							</td>
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
							<td class="center" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
								<input class="normal_btn" type="button" name="BtnQuery" id="u-button" value="添 加" onclick="addCells()" />
							</td>
						</tr>
					</table>
					<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
					<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			</FIELDSET>
		</form>
	</div>
</body>
</html>
