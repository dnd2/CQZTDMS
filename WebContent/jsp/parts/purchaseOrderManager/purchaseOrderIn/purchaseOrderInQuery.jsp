<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购订单入库</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<OBJECT ID="tecPrint" style="display:none"
        CLASSID="CLSID:AAB8479E-F25B-4C99-944C-5D9AEE4591A1"
        CODEBASE="<%=request.getContextPath()%>/jsp/ocx/tecBPrint.CAB#version=1,0,0,0">
</OBJECT>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
    当前位置：配件管理&gt; 采购订单管理&gt; 采购订单入库
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <input type="hidden" name="flag" id="flag" value=""/>
    <table class="table_query" bordercolor="#DAE0EE">
        <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td width="10%" align="right">验收单号：</td>
            <td width="20%">
                <input class="middle_txt" type="text" id="CHECK_CODE" name="CHECK_CODE"/>
            </td>
            <td width="10%" align="right">采购订单号：</td>
            <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE"/></td>
            <td width="10%" align="right">制单时间：</td>
            <td width="20%">
                <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t1,t2">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't1', false);"/>
                &nbsp;至&nbsp;
                <input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t1,t2">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't2', false);"/>
            </td>
        </tr>
        <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
        </tr>
        <tr>
            <td width="10%" align="right">库房：</td>
            <td width="20%">
                <select id="WH_ID" name="WH_ID" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${wareHouses}" var="wareHouse">
                        <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                    </c:forEach>
                </select>
            </td>
            <td width="10%" align="right">配件种类：</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                </script>
            </td>
            <td align="right">供应商：</td>
            <td width="20%">
                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                <INPUT class=mini_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </td>
        </tr>

        <tr>
            <td align="right">库管员：</td>
            <td>
                <select id="WHMAN_ID" name="WHMAN_ID" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${whmans}" var="whmans">
                        <option value="${whmans.WHMAN_ID }" ${whmans.WHMAN_ID eq userId?"selected":""}>${whmans.WHMAN_NAME }</option>
                    </c:forEach>
                </select>
            </td>
            <td width="10%" align="right">计划员：</td>
            <td width="20%">
                <%--  <input class="phone_txt" type="text" id="PLANER_NAME" name="PLANER_NAME" /></td>--%>
                <select id="PLANER_ID" name="PLANER_ID" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                        <option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                    </c:forEach>
                </select>
            </td>
            <td width="10%" align="right">单据状态：</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("STATE", <%=Constant.PART_PURCHASE_ORDERIN_STATUS %>, "", true, "short_sel", "", "false", '');
                </script>
            </td>
        </tr>
        <!--<tr>
            <td width="10%" align="right">备注：</td>
            <td colspan="5"><input name="REMARK1" type="text" id="REMARK1"
            style="width:600px;height:14px;line-height:14px;border:1px solid #a6b2c8;padding-left: 2px;"/></td>
        </tr>-->
        <tr>
            <td align="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
                                                  onclick="query();" value="查 询"/>
                &nbsp;<input id="chkBtn" name="button" type="button" class="normal_btn" onclick="setCheckModel();"
                             value="入库"/>
                &nbsp;<input name="button" type="button" class="normal_btn" onclick="exportOrderInExcel();"
                             value="导出"/>
                &nbsp;<input id="printBtn" name="printBtn" type="button" class="normal_btn" onclick="setCheckPrint();"
                             value="条码打印"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var barPrint = document.getElementById("tecPrint");
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/queryOrderInInfo.json";
var title = null;

var len = columns.length;
//设置超链接  begin      

function myLink(value, meta, record) {
    var poId = record.data.PO_ID;
    var state = record.data.STATE;
    if (state ==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>) {
        return String.format("<a href=\"#\" disabled='disabled'>[关闭]</a>");
    }
    return String.format("<a href=\"#\" onclick='deletePo(" + value + "," + poId + ")'>[关闭]</a>");
}

function printBar(value, meta, record) {
	var barCode = record.data.BARCODE;
    return String.format("<a href=\"#\" onclick='printOrder(\"" + value + "\",\"" + barCode + "\")'>[条码打印]</a>");
}
function printOrder(partId,barCode) {
    OpenHtmlWindow('<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/barcodePrint.jsp?partId=' + partId+'&barCode='+barCode, 500, 200);
}

//function printBar(value, meta, record) {
 //  var vpoldcode = record.data.PART_OLDCODE;
   // var vpCname = record.data.PART_CNAME;
   // var vpEname = record.data.PART_ENAME==null?"":record.data.PART_ENAME;
   // var vpCode = record.data.PART_CODE;
  //  var vQty = 1;
   // var vpNum = padLeft(record.data.RELIN_QTY,4);
   // var vBarCode = record.data.PART_CODE;
    //return String.format("<a href=\"#\" onclick='doPrint(\""+ vBarCode + "\",\"" + vpoldcode + "\",\"" + vpCname + "\",\"" + vpEname + "\",\"" + vpCode + "\",\"" + vQty + "\",\""  + vpNum + "\")'>[条码打印]</a>");
//}

function doPrint(vBarCode, vpoldcode, vpCname, vpEname, vpCode, vQty, vpNum) {
    //MyAlert(vpoldcode + ":" + vpCname + ":" + vpEname + ":" + vpCode + ":" + vQty + ":" + vpNum + ":" + vBarCode);
    try {
        // 注意：打印份数以0001,0002,0003的方式输入
        //s.bPrint('条码号','编号','中文名称','英文名称','图号','数量','打印份数');
        barPrint.bPrint(vBarCode, vpoldcode, vpCname, vpEname, vpCode, vQty, vpNum);
        //barPrint.bPrint('7899555','66666','中文名称','55','1222','333','0002');
    } catch (e) {
    	//MyAlert(vpoldcode + ":" + vpCname + ":" + vpEname + ":" + vpCode + ":" + vQty + ":" + vpNum + ":" + vBarCode);
        MyAlert('控件调用失败!');
    }
}
function padLeft(str,lenght){
    if(str.length >= lenght)
        return str;
    else
        return padLeft("0" +str,lenght);
}
function query() {
    $("queryBtn").disabled = true;
    url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/queryOrderInInfo.json";
    var state = $("STATE").value;
    if (state ==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>) {//如果已经入库完成
        columns = [
            {header: "序号", width: '5%', renderer: getIndex},
            {header: "验收单号", dataIndex: 'CHECK_CODE', style: 'text-align:left'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "当前库存", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "验货数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            {header: "入库库房", dataIndex: 'WH_NAME', style: 'text-align:left'},
            {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
            {header: "包装规格", dataIndex: 'PKG_SIZE', style: 'text-align:left'},
            {header: "采购订单号", dataIndex: 'ORDER_CODE', align: 'center'},
            {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
            {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
            {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
            {header: "验货人员", dataIndex: 'CHECK_NAME', align: 'center'},
            {header: "验货日期", dataIndex: 'CHECK_DATE', align: 'center', renderer: formatDate},
            {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
        ];
    } else {
        columns = [
            {header: "序号", width: '5%', renderer: getIndex},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: printBar, align: 'center'},
            {header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'CHECK_ID', renderer: myCheckBox},
            {header: "验收单号", dataIndex: 'CHECK_CODE', align: 'center', renderer: checkCodeInput},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left',renderer:partColor},
            {header: "预验货数量", dataIndex: 'CHECK_QTY', align: 'center', renderer: checkQtyInput},
            {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center', renderer: inQtyInput},
            {header: "待入库数量", dataIndex: 'RELIN_QTY', style: 'text-align:left', renderer: realQtyInput},
            {header: "当前库存", dataIndex: 'NORMAL_QTY', align: 'center', renderer: normalQtyInput},
            {header: "入库库房", dataIndex: 'WH_NAME', style: 'text-align:left', renderer: whNameSelect},
            {header: "备注", dataIndex: 'REMARK', style: 'text-align:left', renderer: remarkInput},
            {header: "包装规格", dataIndex: 'PKG_SIZE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            /*  {header: "订购数量", dataIndex: 'BUY_QTY', align: 'center'},*/
            {header: "采购订单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
            {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
            {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
            {header: "制造商名称", dataIndex: 'MAKER_NAME', align: 'center'},
            {header: "验货人员", dataIndex: 'CHECK_NAME', align: 'center'},
            {header: "验货日期", dataIndex: 'CHECK_DATE', align: 'center', renderer: formatDate},
            {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
            /*{id: 'action', header: "操作", sortable: false, dataIndex: 'CHECK_ID', renderer: myLink, align: 'center'}*/
        ];
    }
    __extQuery__(1);
}


function deletePo(value, poId) {
    if (confirm("确定关闭?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/deletePo.json?poId=' + poId + '&curPage=' + myPage.page + '&checkId=' + value;
        sendAjax(url, getResult, 'fm');
    }
}

function getResult(jsonObj) {
    btnEable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//全选checkbox
function myCheckBox(value, metaDate, record) {
    var state = record.data.STATE;
    return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>"
            + "<input type='hidden' id='STATE" + record.data.CHECK_ID + "' name='STATE" + record.data.CHECK_ID + "' value='" + state + "'/>\n");

}

function chkPart() {
    var cks = document.getElementsByName('ids');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("checkAll").checked = flag;
}

/*function doCusChange(value){
 if(value==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>){
 $("chkBtn").disabled="disabled";
 columns[0]={header: "序号", width: '5%', renderer: getIndex};
 columns.splice(columns.length-1,1);
 }else{
 columns[0]={header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />",sortable: false,dataIndex: 'CHECK_ID',renderer:myCheckBox};
 if(columns.length<len){
 columns.push({id: 'action', header: "操作", sortable: false, dataIndex: 'CHECK_ID', renderer: myLink, align: 'center'});
 }
 $("chkBtn").disabled="";
 }
 __extQuery__(1);
 }*/
//插入入库库房下拉框
function whNameSelect(value, metaDate, record) {
    /*var state = record.data.STATE;
     if(state==
    <%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>){
     return value;
     }*/
    var whId = record.data.WH_ID;
    var partId = record.data.PART_ID;
    var checkId = record.data.CHECK_ID;
    var whName = record.data.WH_NAME;
    var whNameOutPut = "<select class='min_sel' id='WH_ID" + checkId + "' name='WH_ID" + checkId + "' onmouseover='insertWh(" + whId + "," + checkId + ")' onchange='changeQty(" + partId + "," + checkId + ")'>"
            + "<option value='" + whId + "'>" + whName + "</option>";
    +"</select>";
    return whNameOutPut;
}

function insertWh(whId, checkId) {
    var obj = document.getElementById('WH_ID');
    var obj1 = document.getElementById('WH_ID' + checkId);//根据id查找对象
    if (obj1.options.length < 2) {//当选项只有默认选中项的时候
        for (var i = 1; i < obj.options.length; i++) {
            if (obj.options[i].value != obj1.value) {
                obj1.options.add(new Option(obj.options[i].text, obj.options[i].value)); //兼容IE与firefox
            }
        }
    }
}

function changeQty(partId, checkId) {
    var whId = $("WH_ID" + checkId).value;
    btnDisable();
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/getNormalQty.json?partId=' + partId + '&whId=' + whId + '&checkId=' + checkId;
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    btnEable();
    if (jsonObj != null) {
        var normalQty = jsonObj.normalQty;
        var checkId = jsonObj.checkId;
        var exceptions = jsonObj.Exception;
        if (normalQty >= 0) {
            $("NORMAL_QTY" + checkId).value = normalQty;
        } else {
            MyAlert(exceptions.message);
        }
    }
}

//在入库数量中插入文本框
function realQtyInput(value, meta, record) {
    var state = record.data.STATE;
    var output = "";
    if (state ==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>) {
        output = '<input type="text" align="right" id="RELIN_QTY' + record.data.CHECK_ID + '" name="RELIN_QTY' + record.data.CHECK_ID + '" value="' + value + '" size ="5"/>\n';
    } else {
        output = '<input type="text" align="right" id="RELIN_QTY' + record.data.CHECK_ID + '" name="RELIN_QTY' + record.data.CHECK_ID + '" value="' + value + '" size ="5" onblur="inputCheck(this);"/>\n';
    }
    return output;
}

//在已入库数量中插入文本框
function inQtyInput(value, meta, record) {
    var output = '<input type="hidden" align="right" id="IN_QTY' + record.data.CHECK_ID + '" name="IN_QTY' + record.data.CHECK_ID + '" value="' + value + '" />\n' + value;
    return output;
}

//在验货数量中插入文本框
function checkQtyInput(value, meta, record) {
    var output = '<input type="hidden" align="right" id="CHECK_QTY' + record.data.CHECK_ID + '" name="CHECK_QTY' + record.data.CHECK_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

//在可用库存中插入文本框
function normalQtyInput(value, meta, record) {
    var output = '<input type="text" class="short_txt" align="right" id="NORMAL_QTY' + record.data.CHECK_ID + '" name="NORMAL_QTY' + record.data.CHECK_ID + '" value="' + value + '" readonly  style="border:0;background:transparent;"/>\n';
    return output;
}

//在验收单号中插入文本框
function checkCodeInput(value, meta, record) {
    var output = '<input type="hidden" align="right" id="CHECK_CODE' + record.data.CHECK_ID + '" name="CHECK_CODE' + record.data.CHECK_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

function inputCheck(obj) {
    var pattern = /^[1-9][0-9]*$/;
    if (!pattern.exec(obj.value)) {
        MyAlert("入库数量只能输入非零的正整数!");
        obj.value = "";
    }
}

function remarkInput(value, meta, record) {
    var state = record.data.STATE;
    if (state ==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>) {
        return value;
    }
    var output = '<input type="text" id="REMARK' + record.data.CHECK_ID + '" name="REMARK' + record.data.CHECK_ID + '" value="' + value + '" size ="10"/>\n';
    return output;
}


//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function setCheckPrint(){
	fm.action = "<%=contextPath%>/parts/baseManager/partBarCodePrt/partBarCodePrtAction/partBarCodePrtInit.do";
    //fm.target = "_self";
    fm.submit();
}

function setCheckModel() {
    var chk = document.getElementsByName("ids");
    var l = chk.length;
    var cnt = 0;
    var chkCodeArr = new Array();
    var whArr = new Array();
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            var checkId = chk[i].value;
            var state = $("STATE" + chk[i].value).value;
            var checkCode = $("CHECK_CODE" + checkId).value;
            var whId = $("WH_ID" + checkId).value;
            if (state ==<%=Constant.PART_PURCHASE_ORDERCIN_STATUS_02%>) {
                MyAlert("验收单【" + checkCode + "】已经入库,请重新选择!");
                return;
            }
            var relInQty = $("RELIN_QTY" + checkId).value;
            var inQty = $("IN_QTY" + checkId).value;
            var checkQty = $("CHECK_QTY" + checkId).value;
            if (relInQty == null || relInQty == "") {
                MyAlert("验收单【" + checkCode + "】的入库数量不能为空!");
                return;
            }
            if (parseInt(relInQty) > (parseInt(checkQty) - parseInt(inQty))) {
                MyAlert("验收单【" + checkCode + "】的入库数量不能大于验货数量与已入库数量之差!");
                return;
            }
            cnt++;
            chkCodeArr.push(checkCode);
            whArr.push(whId);
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要入库的订单！");
        return;
    }

    /* if (chkCodeArr.length > 1) {
     for (var i = 0; i < chkCodeArr.length - 1; i++) {
     if (chkCodeArr[i] != chkCodeArr[i + 1]) {
     MyAlert("请选择验收单号相同的配件入库!");
     return;
     }
     }
     }*/
    if (whArr.length > 1) {
        for (var i = 0; i < whArr.length - 1; i++) {
            if (whArr[i] != whArr[i + 1]) {
                MyAlert("同一验收单号下的配件,入库库房必须一致!");
                return;
            }
        }
    }

    MyConfirm("确认入库？", inOrder);
}

//入库
function inOrder() {
    btnDisable();
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/inPurchaseOrder.json';
    sendAjax(url, handleControl, 'fm');
}

function handleControl(jsonObj) {
    btnEable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (error) {
            MyAlert(error);
            __extQuery__(jsonObj.curPage);
        } else if (success) {
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        } else {
            MyAlert(exceptions.message);
        }
    }
}

function clearInput() {
    //清空选定供应商
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}

//导出
function exportOrderInExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/exportOrderInExcel.do";
    fm.target = "_self";
    fm.submit();
}
function MyAlert(info) {

    var owner = getTopWinRef();
    try {
        _dialogInit();
        var height = 200;
        if (info.split('<br>').length >= 6) {
            height = height + (info.split('<br>').length - 6) * 5;
        }
        if (info.split('<br>').length > 6) {   //如果有6个就会过长
            var infoR = "";
            var infoArr = info.split('<br>');
            for (var i = 0; i < 6; i++) {
                infoR += infoArr[i] + "<br>";
            }

            infoR += "......<br>";
            info = infoR;
        }
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

        _setSize(500, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
}
function partColor(value, meta, record) {
    var flag = record.data.FLAG;
    var output = "";
    if (flag=="1") {
        output = '<input type="text" style="color: red;font-size: 15px;border: none;" readonly title="库存已不足，需优先入库" value="' + value + '" />\n';
    } else {
        output = '<input type="text" readonly  style="border: none;"  value="' + value + '" />\n';
    }
    return output;
}
</script>
</div>
</body>
</html>