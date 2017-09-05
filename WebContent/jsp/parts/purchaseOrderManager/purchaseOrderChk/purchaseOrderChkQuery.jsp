<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购订单验收</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
    当前位置：配件管理&gt; 采购计划管理&gt; 采购入库确认&gt;确认
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="flag" id="flag"/>
    <input type="hidden" name="partId" id="partId"/>
    <input type="hidden" name="chkId" id="chkId" value="${chkId}"/>
    <table class="table_query" bordercolor="#DAE0EE">
        <th colspan="6" width="100%"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td width="10%" class="table_query_right" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" class="table_query_right" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" class="table_query_right" align="right">状态：</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("STATE", <%=Constant.PART_PURCHASE_ORDERCHK_STATUS %>, "<%=Constant.PART_PURCHASE_ORDERCHK_STATUS_01 %>", true, "short_sel", "", "false", '');
                </script>
            </td>
        </tr>
        <tr>
            <td width="10%" class="table_query_right" align="right">是否打印：</td>
            <td width="20%">
            <select id="ISPRINT" name="ISPRINT" style="width:152px;" onchange="doCusChange1(this)">
            <option value="">-请选择-</option>
            <option value="<%=Constant.IF_TYPE_YES %>">是</option>
            <option value="<%=Constant.IF_TYPE_NO %>">否</option>
            </select>
            </td>
        </tr>
        <tr>
            <td width="10%" align="right">备注：</td>
            <td colspan="5"><input name="REMARK2" type="text" id="REMARK2"
            style="width:600px;height:14px;line-height:14px;border:1px solid #a6b2c8;padding-left: 2px;"/></td>
        </tr>
        <!-- <tr>
            <td width="10%" align="right">计划员备注：</td>
            <td colspan="5"><input name="PLANER_REMARK" type="text" id="PLANER_REMARK"
            style="width:600px;height:14px;line-height:14px;border:1px solid #a6b2c8;padding-left: 2px;"/></td>
        </tr> -->
        <tr>
            <td align="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
                                                  onclick="__extQuery__(1)" value="查 询"/>
                <%-- &nbsp;<input id="chkBtn" name="button" type="button" class="normal_btn" onclick="setCheckModel();"
                              value="验 收"/>--%>
                &nbsp;<input id="doneBtn" name="button" type="button" class="normal_btn"
                             onclick="updatePurchaseState(1);"
                             value="完 成"/>
                &nbsp;<input id="closeBtn" name="button" type="button" class="normal_btn"
                             onclick="updatePurchaseState(2);"
                             value="关 闭"/>
                <%-- &nbsp;<input name="button" type="button" class="normal_btn" onclick="exportOrderChkExcel();"
                             value="导出"/>--%>
                &nbsp;<input id="printBtn" name="button" type="button" class="normal_btn" disabled="disabled" onclick="printChkOrder();"
       value="打印"/>
       &nbsp;<input name="button" type="button" class="normal_btn" onclick="myBack();"
                             value="返回"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;

var title = null;

var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkInfo.json";

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "<input type='checkbox' name='checkAll' id='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'PO_ID', renderer: myCheckBox},
    /* {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left', renderer: checkCodeInput},*/
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    /*   {header: "验货数量", dataIndex: 'SPARE_QTY', align: 'center', renderer: realQtyInput},
     {header: "待验货数量", dataIndex: 'SPARE_QTY', align: 'center', renderer: spareQtyInput},*/
    /*    {header: "验货日期", dataIndex: 'CHECK_DATE', align: 'center', renderer: checkDateInput},*/
    /*  {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: remarkInput},*/
    {header: "配件种类", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
    {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center', renderer: buyQtyInput},
    {header: "验货数量", dataIndex: 'CHECK_QTY', align: 'center', renderer: checkQtyInput},
    {header: "待入库数量", dataIndex: 'SPAREIN_QTY', align: 'center'},
    {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center', renderer: inQtyInput},
    {header: "备注", dataIndex: 'REMARK1', align: 'center', renderer: remark1Input},
    {header: "打印时间", dataIndex: 'PRINT_DATE2', align: 'center'},
    {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'/*, renderer: makerSelect*/},
    /* {header: "采购订单号", dataIndex: 'ORDER_CODE', align: 'center', renderer: orderCodeInput},*/
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    /* {header: "采购员", dataIndex: 'BUYER', align: 'center'},*/
    {header: "预计到货日期", dataIndex: 'FORECAST_DATE', align: 'center', renderer: formatDate},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}/*,
    {id: 'action', header: "操作", sortable: false, dataIndex: 'PO_ID', renderer: myLink, align: 'center'}*/
];

var len = columns.length;
//设置超链接  begin      

function myLink(value, meta, record) {
    var state = record.data.STATE;
    if (state == <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_01%>) {
        return String.format("<a href=\"#\" onclick='closePo(\"" + value + "\")' id='sel'>[关闭]</a>");
    } else {
        return String.format("<input type='hidden'/>");
    }
}

//打印
function printChkOrder(){
	    var ids = '';
	    var chk = document.getElementsByName("ids");
	    var n=0;
	    for(var i = 0 ; i < chk.length ; i++){
	        if(chk[i].checked){
	           ids += chk[i].value+',';
	           n++;
	        }
	    }
	    if(n==0){
	        MyAlert("请选择要打印的配件!");
	        return;
	    }
	    window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/printConfirmDtl.do?ids="+ids,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

function doCusChange1(obj){
	var val = obj.value;
	if(val){
		$("STATE").disabled = true;
		$("doneBtn").disabled = true;
		$("closeBtn").disabled = true;
		$("printBtn").disabled = false;
	}else{
		$("STATE").disabled = false;
		$("printBtn").disabled = true;
		$("doneBtn").disabled = false;
		$("closeBtn").disabled = false;
	}
	__extQuery__(1);
}

function closePo(value) {
	/* var remark1 = $("REMARK1"+value).value;
	if(!remark1){
		MyAlert("没有备注,不能关闭!");
		return;
	} */
    if (confirm("确定关闭?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/closePo.json?poId=' + value + '&curPage=' + myPage.page;
        sendAjax(url, getResult, 'fm');
    }
}

function openPo(value) {
    if (confirm("确定打开?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/openPo.json?poId=' + value + '&curPage=' + myPage.page;
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
function myCheckBox(value, meta, record) {
    var state = record.data.STATE;
    var isPrint = $("ISPRINT").value;
    if(isPrint){
    	return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>");
    }else{
    	if (state != <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%> && state != <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_02%>) {
            return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>"
                    + "<input type='hidden' id='STATE" + record.data.PO_ID + "' name='STATE" + record.data.PO_ID + "' value='" + state + "'/>\n");
        } else
            return String.format("<input type='hidden'/>")
    }
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

//在已验货量中插入文本框
function checkQtyInput(value, meta, record) {
    var output = '<input type="hidden" id="CHECK_QTY' + record.data.PO_ID + '" name="CHECK_QTY' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

//插入文本框
function remark1Input(value, meta, record) {
    var output = '<input type="hidden" id="REMARK1' + record.data.PO_ID + '" name="REMARK1' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}
//在订货量中插入文本框
function buyQtyInput(value, meta, record) {
    var output = '<input type="hidden" id="BUY_QTY' + record.data.PO_ID + '" name="BUY_QTY' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}
//在入库数量量中插入文本框
function inQtyInput(value, meta, record) {
    var output = '<input type="hidden" id="IN_QTY' + record.data.PO_ID + '" name="IN_QTY' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}
function spareQtyInput(value, meta, record) {
    var output = '<input type="hidden" id="SPARE_QTY' + record.data.PO_ID + '" name="SPARE_QTY' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

//在实际验货量中插入文本框
function realQtyInput(value, meta, record) {
    var state = record.data.STATE;
    var output = "";
    if (state ==<%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%>) {
        output = '<input type="text" align="right" id="REL_QTY' + record.data.PO_ID + '" name="REL_QTY' + record.data.PO_ID + '" value="' + value + '" size ="5"/>\n';
    } else {
        output = '<input type="text" align="right" id="REL_QTY' + record.data.PO_ID + '" name="REL_QTY' + record.data.PO_ID + '" value="' + value + '" onchange="checkInput(this);" size ="5"/>\n';
    }

    return output;
}

function checkCodeInput(value, meta, record) {
    var output = '<input type="hidden" align="right" id="CHK_CODE' + record.data.PO_ID + '" name="CHK_CODE' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

//在采购订单号中插入隐藏域
function orderCodeInput(value, meta, record) {
    var output = '<input type="hidden" align="right" id="ORDER_CODE' + record.data.PO_ID + '" name="ORDER_CODE' + record.data.PO_ID + '" value="' + value + '"/>\n' + value;
    return output;
}

function remarkInput(value, meta, record) {
    /*var state = record.data.STATE;
     if(state==
    <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%>){
     return value;
     }*/
    var output = '<input type="text" id="REMARK' + record.data.PO_ID + '" name="REMARK' + record.data.PO_ID + '" value="' + value + '" size ="10"/>\n';
    return output;
}
//在验货时间中插入时间选择器
function checkDateInput(value, meta, record) {
    /*var state = record.data.STATE;
     if(state==
    <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%>){
     return value.substr(0,10);
     }*/
    var rinput = '<input name="CHECK_DATE' + record.data.PO_ID + '" size=20 type="text" class="short_time_txt" id="CHECK_DATE' + record.data.PO_ID + '" value="' + value.substr(0, 10) + '"/>';
    var output = rinput + "<input name='button3' type='button' class='time_ico' value=' ' onclick='showcalendar(event,&quot;CHECK_DATE" + record.data.PO_ID + "&quot;, false);'/>";
    return output;
}
//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}


var poIdArr = new Array();
//插入制造商下拉选择框
function makerSelect(value, metaDate, record) {

    var makerOutput;
    var poId = record.data.PO_ID;
    var curVenderId = record.data.VENDER_ID;
    var curMakerId = record.data.MAKER_ID;
    var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId;
    if (curMakerId) {//如果当前制造商id存在
        makerOutput = "<select class='short_sel' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "' onmouseover='insertMaker(" + poId + "," + curVenderId + "," + curMakerId + ")'>"
                + "<option value='" + curMakerId + "'>" + value + "</option>";
        +"</select>";
    } else {
        makerOutput = "<select class='short_sel' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "'>"
                + "<option value=''>--请选择--</option>"
                + "</select>";
        sendAjax(url1, getMaker, 'fm');
    }

    return makerOutput;
}

function insertMaker(poId, curVenderId, curMakerId) {
    if (poIdArr.length > 0) {
        for (var i = 0; i < poIdArr.length; i++) {
            if (poIdArr[i] == poId) {//如果数组里面已经包含了当前采购订单ID
                return;
            }
        }
    }
    var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId + "&curMakerId=" + curMakerId;
    sendAjax(url1, getMaker1, 'fm');
}

function getMaker(jsonObj) {
    var poId = jsonObj.poId;
    var arry = jsonObj.makers;
    if (arry.length > 0) {
        var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
        for (var i = 0; i < arry.length; i++) {
            obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
        }
    }
}

function getMaker1(jsonObj) {
    var poId = jsonObj.poId;
    poIdArr.push(poId);
    var arry = jsonObj.makers;
    if (arry.length > 0) {
        var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
        obj.options.length = 0;
        for (var i = 0; i < arry.length; i++) {
            obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
            if (jsonObj.curMakerId == arry[i].MAKER_ID) {
                obj.selectedIndex = i;
            }
        }
    }
}

function updatePurchaseState(value) {
    var chk = document.getElementsByName("ids");
    var l = chk.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var buy_qty = $("BUY_QTY" + chk[i].value).value; //订货数量
            var check_qty = $("CHECK_QTY" + chk[i].value).value; //验货数量
            var in_qty = $("IN_QTY" + chk[i].value).value; //入库数量
            if (value == 1 && (buy_qty != check_qty || check_qty != in_qty || buy_qty != in_qty)) {
                MyAlert("第" + (i + 1) + "行未完全入库不能完成!");
                chk[i].checked = false;
                return;
            }
            if (value == 2 && (buy_qty == check_qty && check_qty == in_qty && buy_qty == in_qty)) {
                MyAlert("第" + (i + 1) + "行已完全入库不用关闭!");
                chk[i].checked = false;
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择明细！");
        return;
    }
    if (value == 1) {
        MyConfirm("确认完成？", checkJob, [value]);
    } else {
        MyConfirm("确认关闭？", checkJob, [value]);
    }
}
//验货
function checkJob(value) {
    btnDisable();
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    document.getElementById("flag").value = value;
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/checkPurchaseUpdateOrderState.json';
    sendAjax(url, handleControl, 'fm');
}
function setCheckModel() {
    var chk = document.getElementsByName("ids");
    var l = chk.length;
    var cnt = 0;
    var chkCodeArr = new Array();
    var makerIdArr = new Array();
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var state = $("STATE" + chk[i].value).value;
            var orderCode = $("ORDER_CODE" + chk[i].value).value;
            var checkCode = $("CHK_CODE" + chk[i].value).value;
            var makerId = $("MAKER_ID" + chk[i].value).value;

            if (state ==<%=Constant.PART_PURCHASE_ORDERCHK_STATUS_02%>) {
                MyAlert("采购订单【" + orderCode + "】已经关闭,不能验收!");
                return;
            }
            if (state ==<%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%>) {
                MyAlert("采购订单【" + orderCode + "】已经验收完成,请重新选择!");
                return;
            }
            var buyQty = $("BUY_QTY" + chk[i].value).value;
            var checkQty = $("CHECK_QTY" + chk[i].value).value;
            var realQty = $("REL_QTY" + chk[i].value).value;

            var makerId = $("MAKER_ID" + chk[i].value).value;

            if (realQty == null || realQty == "") {
                MyAlert("采购订单【" + orderCode + "】的验货数量不能为空!");
                return;
            }
            if (makerId == null || makerId == "") {
                MyAlert("请选择采购订单【" + orderCode + "】的制造商!");
                return;
            }
            if (parseInt(realQty) > (parseInt(buyQty) - parseInt(checkQty))) {
                MyAlert("采购订单【" + orderCode + "】的验货数量不能大于待验货数量!");
                return;
            }
            var check_date = document.getElementById("CHECK_DATE" + chk[i].value).value;//用户选择的验货日期
            if (!checkSys_Sel_Date(check_date, "yyyy-MM-dd")) {
                MyAlert("采购订单【" + orderCode + "】的验货日期不能大于当前日期!");
                return;
            }
            chkCodeArr.push(checkCode);
            makerIdArr.push(makerId);
        }
    }
    if (cnt == 0) {
        MyAlert("请选择验收单！");
        return;
    }

    if (chkCodeArr.length > 1) {
        for (var i = 0; i < chkCodeArr.length - 1; i++) {
            if (chkCodeArr[i] != chkCodeArr[i + 1]) {
                MyAlert("请选择验收单号相同的配件验收!");
                return;
            }
        }
    }
    if (makerIdArr.length > 1) {
        for (var i = 0; i < makerIdArr.length - 1; i++) {
            if (makerIdArr[i] != makerIdArr[i + 1]) {
                MyAlert("请选择制造商相同的配件验收!");
                return;
            }
        }
    }

    MyConfirm("确认验收？", checkOrder);
}

//验货
function checkOrder() {
    btnDisable();
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/checkPurchaseOrder.json';
    sendAjax(url, handleControl, 'fm');
}
function handleControl(jsonObj) {
    btnEable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        var toF = jsonObj.toF;
        if (error) {
            MyAlert(error);
        } else if (success&&toF) {
            myBack();
        } else if (success) {
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        }else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function checkInput(obj) {
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("验货数量只能输入非0的正整数!");
        obj.value = "";
        return;
    }
}
function clearInput() {
    //清空选定供应商
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}

//导出
function exportOrderChkExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/exportOrderChkExcel.do";
    fm.target = "_self";
    fm.submit();
}

function callBack(json) {
    var ps;
    //设置对应数据
    if (Object.keys(json).length > 0) {
        keys = Object.keys(json);
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == "ps") {
                ps = json[keys[i]];
                break;
            }
        }
    }

    //生成数据集
    if (ps.records != null) {
        $("_page").hide();
        $('myGrid').show();
        new createGrid(title, columns, $("myGrid"), ps).load();
        //分页
        myPage = new showPages("myPage", ps, url);
        myPage.printHtml();
        hiddenDocObject(2);
        $("queryBtn").disabled = "";
    } else {
        $("_page").show();
        $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
        $("myPage").innerHTML = "";
        removeGird('myGrid');
        $('myGrid').hide();
        hiddenDocObject(1);
        $("queryBtn").disabled = "";
    }
}
function myBack() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderChkQueryInit.do";
}
</script>
</div>
</body>
</html>