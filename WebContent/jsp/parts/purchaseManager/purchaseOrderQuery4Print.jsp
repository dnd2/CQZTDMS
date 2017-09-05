<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>采购订单-查询</TITLE>
<SCRIPT type=text/javascript>
//loadcalendar();  //初始化时间控件
var myPage;
var url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurchaseOrderInfo.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "订单单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "供应商", dataIndex: 'VENDER_NAME', align: 'center'},
//     {header: "计划员", dataIndex: 'BUYER', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
//     {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
//     {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
    {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
    {header: "总金额", dataIndex: 'AMOUNT', style: 'text-align:right'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
    {header: "关闭日期", dataIndex: 'CLOSE_DATE', align: 'center', renderer: getCloseDateValue},
    {header: "关闭原因", dataIndex: 'REMARK1', align: 'center', renderer: getRemark1Value},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'}
];

function queryOrder(){
    var flag = $("flag").value;
    if(flag=="1"){
    	queryDtl();
    }else{
    	query();
    }
}
function myLink(value, meta, record) {
    var state = record.data.STATE;
    if (state ==<%=Constant.PURCHASE_ORDER_STATE_02%>) {//如果已完成就只能打印
        return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a><a href=\"#\" onclick='viewPurOrder(\"" + value + "\")'>[查看]</a>");
    }
    if(state ==<%=Constant.PURCHASE_ORDER_STATE_03%>){//如果已关闭就只能打开和打印
    	return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a>"
            	+"<a href=\"#\" onclick='openPurOrder(\"" + value + "\")'>[打开]</a>"
            	+"<a href=\"#\" onclick='viewPurOrder(\"" + value + "\")'>[查看]</a>");
    }
    return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a>"
            + "<a href=\"#\" onclick='closePurOrder(\"" + value + "\")'>[关闭]</a>"
            +"<a href=\"#\" onclick='viewPurOrder(\"" + value + "\")'>[查看]</a>"
//             +"<a href=\"#\" onclick='updatePurOrder(\"" + value + "\")'>[修改]</a>"
//             +"<a href=\"#\" onclick='productRecvOrder(\"" + value + "\")'>[生成领件订单]</a>"
//             + "<a href=\"#\" onclick='checkDetail(\"" + value + "\",0)'>[生成验收指令]</a>"
            );
}


function getRemark1Value(value, meta, record){
    var state = record.data.STATE;
    var output = "";
    if(state==<%=Constant.PURCHASE_ORDER_STATE_03%>){
    	output = value;
    }else{
    	output = '<input type="text" id="REMARK1' + record.data.ORDER_ID + '" name="REMARK1' + record.data.ORDER_ID + '" value=""/>\n';
    }
    return output;
}

function getCloseDateValue(value, meta, record){
    var state = record.data.STATE;
    var output = "";
    if(state==<%=Constant.PURCHASE_ORDER_STATE_03%>){
    	output = value;
    }
    return output;
}
//查询
function query() {
    $("allQuery").style.display = "block";
    $("dtlQuery").style.display = "none";
    var state = $("STATE").value;
    url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurchaseOrderInfo.json";
    columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
        {header: "关闭原因", dataIndex: 'REMARK1', align: 'center', renderer: getRemark1Value},
//         {header: "是否紧急入库", dataIndex: 'IS_URGENT_IN', align: 'center', renderer: getItemValue},
        {header: "订单单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
//         {header: "计划员", dataIndex: 'BUYER', align: 'center'},
        {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
//         {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
//         {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
        {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
        {header: "总金额", dataIndex: 'AMOUNT', style: 'text-align:right'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
        {header: "备注", dataIndex: 'REMARK', align: 'center'},
        {header: "关闭日期", dataIndex: 'CLOSE_DATE', align: 'center', renderer: getCloseDateValue}
    ];
    
    if (state ==<%=Constant.PURCHASE_ORDER_STATE_02%>) {//如果已完成
    	columns.splice(9,2);
    }
    if (state ==<%=Constant.PURCHASE_ORDER_STATE_01%>||state=="") {//如果未完成
    	columns.splice(9,1);
    }
    __extQuery__(1);
    btnDisable();
}

//明细查询
function queryDtl() {
    $("allQuery").style.display = "none";
    $("dtlQuery").style.display = "block";
    url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurOrderDtlInfo.json";
    columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "订单单号", dataIndex: 'ORDER_CODE',  style: 'text-align:left', renderer: orderLink},
        {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
        {header: "单位", dataIndex: 'UNIT', align: 'center'},
        {header: "供应商名称", dataIndex: 'VENDER_NAME',  style: 'text-align:left'},
        {header: "计划数量", dataIndex: 'PLAN_QTY', align: 'center'},
        {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
        {header: "验收数量", dataIndex: 'CHECK_QTY', align: 'center'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    ];
    __extQuery__(1);
    btnDisable();
}

function orderLink(value, meta, record){
    var spareQty = record.data.SPARE_QTY;
	var orderId = record.data.ORDER_ID;
	var state = record.data.STATE;
	if(spareQty>0 ){
        if(state == <%=Constant.PURCHASE_ORDER_STATE_04%> ){
            return "<a href=\"#\" style='color: red' title='订单已超期!' onclick='checkDetail(\"" + orderId + "\",1)'>"+value+"</a>";
        }
		return "<a href=\"#\" style='color: yellow;background-color: green' title='有效订单!' onclick='checkDetail(\"" + orderId + "\",1)'>"+value+"</a>";
	}
    return value;
}

function checkDetail(value,flag) {
	if(confirm("制造商是否按默认供应商生成验收单?")){
		window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderView2.do?orderId=" + value+"&flag="+flag;
	}else{
		window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderView.do?orderId=" + value+"&flag="+flag;
	}
}

//生成领件订单
function productRecvOrder(orderId)
{
	window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/productRecvOrderInit.do?orderId=" + orderId;
}
//查看
function viewPurOrder(value) {
    window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderView1.do?orderId=" + value;
}
//修改
function updatePurOrder(value) {
    window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/updatePurOrderInit.do?orderId=" + value;
}

//打印
function printPurOrder(value) {
	window.open("<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/opPrintHtml.do?orderId=" + value,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//导出excel add zhumingwei 2013-09-22
function excelPurOrder(value) {
    window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/exportPurOrder.do?orderId="+value;
}

//关闭
function closePurOrder(value) {
    var remark1 = $("REMARK1"+value).value;
    if(!remark1){
        MyAlert("请填写关闭原因!");
        return;
    }
    if (confirm("确定关闭?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/closePo.json?orderId=' + value + '&curPage=' + myPage.page;
        sendAjax(url, getResult, 'fm');
    }
}

//打开
function openPurOrder(value) {
    if (confirm("确定打开?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/openPo.json?orderId=' + value + '&curPage=' + myPage.page;
        sendAjax(url, getResult, 'fm');
    }
}

function getResult(jsonObj) {
    btnEnable();
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

function callBack(json) {
    btnEnable();
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
    } else {
        $("_page").show();
        $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
        $("myPage").innerHTML = "";
        removeGird('myGrid');
        $('myGrid').hide();
        hiddenDocObject(1);
    }
}

function myBack(){
    window.location.href="<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do";
}
function initCondition(){
    if($('condition_orderCode')){
        $('ORDER_CODE').value=$('condition_orderCode').value;
    }
    if($('condition_planType')){
        $('PLAN_TYPE').value=$('condition_planType').value;
    }
    if($('condition_planerName')){
        MyAlert($('condition_planerName').value==""?${curUserId}:$('condition_planerName').value);
        $('PLANER_NAME').value=$('condition_planerName').value==""?${curUserId}:$('condition_planerName').value;;
    }
    if($('condition_state')){
        $('STATE').value=$('condition_state').value;
    }
    if($('condition_whId')){
        $('WH_ID').value=$('condition_whId').value;
    }
    if($('condition_beginTime')){
        $('beginTime').value=$('condition_beginTime').value;
    }
    if($('condition_endTime')){
        $('endTime').value=$('condition_endTime').value;
    }
    __extQuery__(1);
}
</script>
</HEAD>
<BODY onload="queryOrder();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置>配件管理>采购计划管理>配件采购订单</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" id="flag" name="flag" value="${flag}"/>
    <input id="planFlag" name="planFlag" value="${planFlag}" type="hidden"/>
    <input id="condition_orderCode" name="condition_orderCode" value="${condition.orderCode}" type="hidden"/>
    <input id="condition_planType" name="condition_planType" value="${condition.planType}" type="hidden"/>
    <input id="condition_planerName" name="condition_planerName" value="${condition.planerName}" type="hidden"/>
    <input id="condition_state" name="condition_state" value="${condition.state}" type="hidden"/>
    <input id="condition_whId" name="condition_whd" value="${condition.whId}" type="hidden"/>
    <input id="condition_beginTime" name="condition_beginTime" value="${condition.beginTime}" type="hidden"/>
    <input id="condition_endTime" name="condition_endTime" value="${condition.endTime}" type="hidden"/>
    <table class="table_query" bordercolor="#DAE0EE" id="allQuery">
        <tr>
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
        </tr>
        <tr>
            <td    align="right">订单单号:</td>
            <td   align="left">
                <INPUT class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"></td>
            <td    align="right">制单时间:</td>
            <td   align="left">
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
            <td  align="right">状态:</td>
            <td  align="left">
                <script type="text/javascript">
                    genSelBoxExp("STATE", <%=Constant.PURCHASE_ORDER_STATE%>, "", true, "short_sel", "", "false", '');
                </script>
            </td>
        </tr>
        <tr>
        	<td width="10%"   align="right">配件编码:</td>
            <td width="20%" align="left">
                <input class="middle_txt" type="text" name="DPARTOLD_CODE" id="DPARTOLD_CODE"/>
            </td>
            <td width="10%"   align="right">配件名称:</td>
            <td width="20%" align="left">
                <input class="long_txt" type="text" name="DPART_CNAME" id="DPART_CNAME"/>
            </td>
            <td width="10%"   align="right">配件件号:</td>
            <td width="20%" align="left">
                <input class="middle_txt" type="text" name="DPART_CODE" id="DPART_CODE"/>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="6">
                <input name="BtnQuery" id="queryBtn" type="button" class="normal_btn" value="查 询"
                       onclick="query();">
                <input name="dtlQueryBtn" id="dtlQueryBtn" class="normal_btn" type="button" value="明细查询"
                       onclick="queryDtl();"/>
            </td>
        </tr>
    </table>

    <table class="table_query" bordercolor="#DAE0EE" id="dtlQuery" style="display:none">
      <%--  <tr>
            <th width="100%" align="right" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件
            </th>
        </tr>--%>
        <tr>
            <td   align="right">配件编码:</td>
            <td width="20%" align="left">
                <input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/>
            </td>
            <td   align="right">配件名称:</td>
            <td  align="left">
                <input class="long_txt" type="text" name="PART_CNAME" id="PART_CNAME"/>
            </td>
            <td   align="right">配件件号:</td>
            <td  align="left">
                <input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/>
            </td>
        </tr>
        <tr>
             <td width="10%"   align="right">订单单号:</td>
             <td width="20%" align="left">
                 <INPUT class="middle_txt" type="text" id="DORDER_CODE" name="DORDER_CODE"></td>
             <td width="10%"   align="right">制单时间：</td>
             <td width="20%">
                 <input name="beginTime2" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10"
                        group="t3,t4">
                 <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                        onclick="showcalendar(event, 't3', false);"/>至
                 <input name="endTime2" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10"
                        group="t3,t4">
                 <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                        onclick="showcalendar(event, 't4', false);"/>
             </td>
             <td width="10%"   align="right">状态:</td>
             <td width="20%" align="left">
                 <script type="text/javascript">
                     genSelBoxExp("STATE2", <%=Constant.PURCHASE_ORDER_STATE%>, "", true, "short_sel", "", "false", '');
                 </script>
             </td>
          </tr>
        <tr>
            <td align="center" colspan="6">
                <%--<input name="BtnQuery" id="queryBtn" type="button" class="normal_btn" value="查 询"
                       onclick="query();">--%>
                <input name="dtlQueryBtn2" id="dtlQueryBtn2" class="normal_btn" type="button" value="查询"
                       onclick="queryDtl();"/><!-- 明细查询下的查询按钮 -->
                <input name="backBtn" id="backBtn" class="normal_btn" type="button" value="返回"
                       onclick="myBack();"/>
            </td>
        </tr>
       <tr>
           <td align="center" colspan="6">
               <font color="red">本页面是订单明细查询页面，可查询配件的订单明细信息，如需验收配件或对订单进行修改调整请点击[返回]按钮！</font>
           </td>
       </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</BODY>
</html>
