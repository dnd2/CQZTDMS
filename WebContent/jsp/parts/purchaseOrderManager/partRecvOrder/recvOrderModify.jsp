<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>配件领件订单修改</TITLE>
<style type="text/css">
    .table_list_row0 td {
        background-color: #FFFFCC;
        border: 1px solid #DAE0EE;
        white-space: nowrap;
    }
</style>

<SCRIPT type=text/javascript>
jQuery.noConflict();
var myPage;

var url = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/showPartStockBase.json";

var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "室", dataIndex: 'ROOM',  style: 'text-align: left;'},
		        {header: "保管员", dataIndex: 'WHMAN',  style: 'text-align: left;'},
				{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "在途数量", dataIndex: 'ORDER_QTY'},
		        {header: "安全库存", dataIndex: 'SAFETY_STOCK'},
				{header: "计划价(元)", dataIndex: 'PLAN_PRICE', style: 'text-align: right;'},
				{header: "领件数量", dataIndex: 'PART_ID', align:'center',renderer:returnText}
			  ];
function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck' />";
}

function returnText(value,meta,record) 
{
	var planQty = record.data.PLAN_QTY;
	return "<input type='text' name='Num_"+value+"' id='Num_"+value+"' value="+planQty+" onchange='dataCheck(this)'/>";
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
				var normalQty = mt.rows[i].cells[8].innerText;  //当前可用库存
				var price = mt.rows[i].cells[11].innerText;  //领件价格
				var number = document.getElementById("Num_"+partId).value;  //领件数量
				if("" == number)
				{
					number = 1;
				}
				addCell(partId, partCode, partOldcode, partCname,normalQty,unit, price, number);
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

function addCell(partId, partCode, partOldcode, partCname, normalQty, unit, price, number) {
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

	var orgId = document.getElementById("parentOrgId").value;
	var saleAmount = (parseFloat(price) * parseInt(number)).toFixed(2);
	saleAmount = addKannma(saleAmount);

    cell1.innerHTML = '<tr><td align="center" nowrap><input type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" /></td>';
    cell2.innerHTML = '<td align="center" nowrap>' + (tbl.rows.length - 2) + '<input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell5.innerHTML = '<td align="center" nowrap><input name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell3.innerHTML = '<td align="center" nowrap><input name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input name="normalQty_' + partId + '" id="normalQty_' + partId + '" value="' + normalQty + '" type="hidden" />' + normalQty + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input class="short_txt" onchange="dataTypeCheck(' + partId + ',this)" name="returnQty_' + partId + '" id="returnQty_' + partId + '" type="text" value="'+number+'"/></td>';
    if(orgId == <%=Constant.OEM_ACTIVITIES%>)
	{
		cell9.innerHTML = '<td align="center" nowrap><input name="salePrice_'+partId+'" id="salePrice_'+partId+'" value="'+price+'" type="hidden" />'+price+'</td>';
	}
	else
	{
		cell9.innerHTML = '<td align="center" nowrap><input name="salePrice_'+partId+'"  onchange="priceCheck('+partId+',this)" id="salePrice_'+partId+'" value="'+price+'" type="text" /></td>';
	}
	
	
	cell10.innerHTML = '<td align="center" nowrap><input name="saleAmount_'+partId+'" id="saleAmount_'+partId+'" value="'+saleAmount+'" type="text" disabled /></td>';   
	cell11.innerHTML = '<td align="center" nowrap><input class="middle_txt" name="remark_'+partId+'" id="remark_'+partId+'" type="text"/></td>';                                                            
	cell12.innerHTML = '<td><input  type="button" class="short_btn"  name="deleteBtn" value="删 除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';  

}
function dataTypeCheck(partId, obj) {
    var salePrice = document.getElementById("salePrice_"+partId).value.replace(new RegExp(",","g"),"");
	var saleAmountObj = document.getElementById("saleAmount_"+partId);
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

    var saleAmount = (((parseFloat(salePrice)*100) * parseInt(value))/100).toFixed(2);
	saleAmountObj.value = addKannma(saleAmount);
}

function dataCheck(obj)
{
	var value = obj.value;
	if(isNaN(value)){
		MyAlert("请输入数字!");
		obj.value = "";
		return;
	}
	var re = /^[1-9]+[0-9]*]*$/;
	if(!re.test(obj.value)){
		MyAlert("请输入正整数!");
		obj.value = "";
		return;
	}
}

function priceCheck(partId,obj)
{
	var returnQty = document.getElementById("returnQty_"+partId).value;
	var saleAmountObj = document.getElementById("saleAmount_"+partId);
	var salePrice = obj.value.replace(new RegExp(",","g"),"");
	if(isNaN(salePrice)){
		MyAlert("请输入数字!");
		obj.value = 0.00;
		return;
	}
	obj.value = parseFloat(salePrice).toFixed(2);
	var saleAmount = (((parseFloat(salePrice)*100) * parseInt(returnQty))/100).toFixed(2);
	saleAmountObj.value = addKannma(saleAmount);
}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    var count = tbl.rows.length;
    for (var i = rowNum; i <= count; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
        tbl.rows[i].cells[11].innerHTML = "<td><input type=\"button\" class=\"short_btn\"  name=\"deleteBtn\" value=\"删 除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
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
    var vdValue = document.getElementById("venderId").value;
    
    if ("增 加" == addPartViv.value) {
        
    	if("" == vdValue)
		{
			MyAlert("请先选择供应商!");
			return false;
		}
		
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
    var vdValue = document.getElementById("venderId").value;

    if ("" == whValue && "收 起" == addPartViv.value) {
        addPartViv.value = "增 加";
        partDiv.style.display = "none";
    }
    if ("" != whValue && "" != whValue && "收 起" == addPartViv.value) {
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
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partResRecUpload.do";
    fm.submit();
    ;
}

function fileVilidate() {
    var msg = "";
    if("" == document.getElementById("venderId").value)
	{
		MyAlert("请先选择供应商!");
		return false;
	}
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
    var url = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/updateOrderInfos.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
            btnDisable();
            MyAlert("修改成功！");
            window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partRecInit.do";
        } else {
            MyAlert("修改失败，请联系管理员！");
        }
    }
}

//修改提示框超出6行会超出框的BUG
function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        owner.getElementById('dialog_content_div').innerHTML = '\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
        owner.getElementById('dialog_alert_info').innerHTML = info;
        owner.getElementById('dialog_alert_button').onclick = _hide;
        var height = 200;

        if (info.split('</br>').length >= 6) {
            height = height + (info.split('</br>').length - 6) * 27;
        }
        _setSize(300, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
}

//下载上传模板
function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/exportExcelTemplate.do";
    fm.submit();
}

//保存
function confirmSubmit() {
    var cb = document.getElementsByName("cb");
    var msg = "";

    if(document.getElementById("venderId").value == ""){
		msg += "请先选择供应商</br>";
	}
	
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
            if (document.getElementById("returnQty_" + cb[i].value).value == "") {

                msg += "请填写第" + document.getElementById("idx_" + cb[i].value).value + "行的领件数量!</br>";
                flag = true;
            } else {
                var returnQty = parseInt(document.getElementById("returnQty_" + cb[i].value).value);
                var normalQty = parseInt(document.getElementById("normalQty_" + cb[i].value).value);
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
            sid = "partCname_" + ary[i];
            nclass = "cname_" + ary[i];
            var partCname = document.getElementById(sid).value;
            MyAlert("配件：" + partCname + " 被重复上传!");
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
    MyConfirm("确定保存新增信息?", savePlan, []);
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}


function goBack() {
    btnDisable();
    window.history.back(-1);
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
    num = num.replace(new RegExp(",","g"),"");   
    // 正负号处理   
    var symble = "";   
    if(/^([-+]).*$/.test(num)) {   
        symble = num.replace(/^([-+]).*$/,"$1");   
        num = num.replace(/^([-+])(.*)$/,"$2");   
    }   
  
    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
        var num = num.replace(new RegExp("^[0]+","g"),"");   
        if(/^\./.test(num)) {   
        num = "0" + num;   
        }   
  
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
  
        var re=/(\d+)(\d{3})/;  
  
        while(re.test(integer)){   
            integer = integer.replace(re,"$1,$2");  
        }   
        return symble + integer + decimal;   
  
    } else {   
        return number;   
    }   
}


function btnDisable() {

    $$('input[type="button"]').each(function (button) {
        button.disabled = true;
    });
}

function btnEnable() {

    $$('input[type="button"]').each(function (button) {
        button.disabled = "";
    });
}
</script>
</HEAD>

<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="planState" id="planState"/>
<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }"/>

<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件领件领件管理 &gt; 配件领件单 &gt; 查看明细</div>
<div>
<table class="table_query" bordercolor="#DAE0EE">

    <tr>
        <th colspan="6" align="left" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 基本信息</th>
    </tr>
    <tr>
        <td width="10%" align="right">领件单号：</td>
        <td width="20%" align="left">${map.ORDER_CODE}
            <input type="hidden" name="orderCode" id="orderCode" value="${map.ORDER_CODE}"/>
            <input type="hidden" name="orderId" id="orderId" value="${map.ORDER_ID}"/>
        </td>
        <td width="10%" align="right">采购订单号：</td>
        <td width="20%" align="left">
            ${map.PUR_ORDER_CODE }
        </td>
        <td width="10%" align="right">制单日期：</td>
        <td width="20%" align="left" >
        	${map.CREATE_DATE}
        </td>
    </tr>
    <tr>
        <td width="10%" align="right">计划类型：</td>
        <td width="20%" align="left" >领件计划</td>
         <td width="10%" align="right">制单人：</td>
        <td width="20%" align="left">
            ${map.BUYER }
        <td width="10%" align="right">仓库：</td>
        <td width="20%" align="left">
          ${map.WH_NAME}
        </td>
    </tr>
    <tr>
        <td width="10%" align="right">备注：</td>
        <td align="left" colspan="5">
            ${map.REMARK }</td>
    </tr>
</table>
<table id="file" class="table_list" style="border-bottom: 1px;">
    <tr>
        <th colspan="12" align="left">
            <img src="<%=contextPath%>/img/nav.gif"/>领件明细
    </tr>
    <tr class="table_list_row0">
        <td>
            序号
        </td>
        <td>
            配件编码
        </td>
        <td>
            配件名称
        </td>
        <td>
            件号
        </td>
        <td>
            单位
        </td>
        <td>
            可用库存
        </td>
        <td>
            领件数量
        </td>
        <td>
            实际领件数量
        </td>
        <td>
	计划价(元)
		</td>
		<td>
	领件金额(元)
		</td>
        <td>
            备注
        </td>
    </tr>
    <c:if test="${list !=null}">
        <c:forEach items="${list}" var="list" varStatus="_sequenceNum">
            <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
                <tr class="table_list_row1">
            </c:if>
            <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
                <tr class="table_list_row2">
            </c:if>
            <td align="center" nowrap>${_sequenceNum.index+1}
                <input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNum.index+1}" type="hidden">
            </td>
            <td align="center">
                <input name="partOldcode_${list.PART_ID}" id="partOldcode_${list.PART_ID}" value="${list.PART_OLDCODE}"
                       type="hidden"/>${list.PART_OLDCODE}
            </td>
            <td align="center" nowrap>
                <input name="partCname_${list.PART_ID}" id="partCname_${list.PART_ID}" value="${list.PART_CNAME}"
                       type="hidden" class="cname_${list.PART_ID}"/>${list.PART_CNAME}
            </td>
            <td align="center" nowrap>
                <input name="partCode_${list.PART_ID}" id="partCode_${list.PART_ID}" value="${list.PART_CODE}"
                       type="hidden"/>${list.PART_CODE}
            </td>
            <td align="center" nowrap>
                <input name="unit_${list.PART_ID}" id="unit_${list.PART_ID}" value="${list.UNIT}"
                       type="hidden"/>${list.UNIT}
            </td>
            <td align="center" nowrap>
                <input name="normalQty_${list.PART_ID}" id="normalQty_${list.PART_ID}" value="${list.NORMAL_QTY}"
                       type="hidden"/>${list.NORMAL_QTY}
            </td>
            <td align="center" nowrap>
                ${list.BUY_QTY}
            </td>
            <td align="center" nowrap>
                ${list.CHECK_QTY}
            </td>
            <td align="center" nowrap>
		      ${list.BUY_PRICE}
		    </td>
		    <td align="center" nowrap>
		      ${list.BUY_AMOUNT}
		    </td>
            <td align="center" nowrap>
                ${list.REMARK}
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
            <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>

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
</div>
</form>
</BODY>
</html>
