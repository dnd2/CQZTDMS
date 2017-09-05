<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>采购计划管理-采购入库确认</title>
<script language="JavaScript">
//初始化方法
function doInit() {
    loadcalendar();  //初始化时间控件
}
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;采购计划管理&gt;采购入库确认0000</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="curPage" id="curPage"/>
<input type="hidden" name="partId" id="partId"/>
<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="allQuery">
    <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
    <tr>
        <td width="10%" align="right">验收单号：</td>
        <td width="20%"><input class="long_txt" type="text" name="CHECK_CODE" id="CHECK_CODE" value="${CHECK_CODE}"/>
        </td>
        <td width="10%" align="right">供应商：</td>
        <td width="25%">
                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                <INPUT class="short_btn" onclick="clearInput();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
        </td>
        <td width="10%"   align="right">
<!--         计划员： -->
        </td>
    <td width="20%">
<!--         <select id="PLANER_ID" name="PLANER_ID" class="short_sel"> -->
<!--             <option value="">-请选择-</option> -->
<%--             <c:forEach items="${planerList}" var="planerList"> --%>
<%-- 				<c:choose>  --%>
<%-- 					<c:when test="${curUserId eq planerList.USER_ID}"> --%>
<%-- 					<option selected="selected" value="${planerList.USER_ID }" >${planerList.USER_NAME }</option> --%>
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<%-- 					<option value="${planerList.USER_ID }" >${planerList.USER_NAME }</option> --%>
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
<%--             </c:forEach> --%>
<!--         </select> -->
    </td>
    </tr>
    <tr>
        <td width="10%" align="right">验收日期：</td>
        <td width="25%">
            <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"
                   group="t1,t2" value="${beginTime}">
            <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                   onclick="showcalendar(event, 't1', false);"/> 至
            <input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"
                   group="t1,t2" value="${endTime}">
            <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                   onclick="showcalendar(event, 't2', false);"/>
        </td>
        <td width="10%" align="right">打印时间：</td>
        <td width="25%">

                <input name="pBeginTime" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t3,t4">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't3', false);"/>  至
                <input name="pEndTime" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t3,t4">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't4', false);"/>
        </td>
        <td width="10%" align="right">是否确认：</td>
        <td width="20%">
            <script type="text/javascript">
                genSelBoxExp("STATE", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", true, "short_sel", "", "false", '');
            </script>
        </td>
    </tr>
    <tr>
    <td width="10%" align="right">配件编码：</td>
        <td width="20%" align="left">
            <input class="long_txt" type="text" name="PART_OLDCODE1" id="PART_OLDCODE1"/>
            </td>
        <td width="10%" align="right">订单来源：</td>
        <td width="20%">
            <script type="text/javascript">
                genSelBoxExp("ORDER_ORIGIN_TYPE", <%=Constant.ORDER_ORIGIN_TYPE%>, "", true, "long_sel", "", "false", '');
            </script>
        </td>
         <td width="10%" align="right">入库状态：</td>
        <td width="20%">
            <select id="STATE1" name="STATE1" class="short_sel" onchange="doCusChange(this.value);">
                <option selected="" value="">-请选择-</option>
                <option value="10041001" title="完全入库">完全入库</option>
                <option value="10041002" title="未入库">未入库</option>
                <option value="10041003" title="部分入库">部分入库</option>
            </select>
           <%-- <script type="text/javascript">
                //genSelBoxExp("STATE1", <%=Constant.PURCHASE_ORDER_STATE%>, "", true, "short_sel", "", "false", '');
                genSelBoxExp("STATE1", <%=Constant.IF_TYPE%>, "", true, "short_sel", "", "false", '');
            </script>--%>
        </td>
    </tr>
    <tr>
        <td width="10%" align="right">配件件号：</td>
        <td width="20%" align="left">
            <input class="long_txt" type="text" name="PART_CODE1" id="PART_CODE1"/>
        </td>
        <td width="10%" align="right">配件名称：</td>
        <td width="20%" align="left">
            <input class="long_txt" type="text" name="PART_NAME1" id="PART_NAME1"/>
        </td>
        <td width="10%" align="right"></td>
        <td width="20%" align="left">
        </td>
    </tr>
    <tr>
        <td colspan="6" align="center">
            <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                   onclick="__extQuery__(1);"/>
            <%-- <input name="BtnPrint" id="BtnPrint" class="normal_btn" type="button" value="打 印" onclick="printBatch();"/>--%>
            <input name="dtlQueryBtn" id="dtlQueryBtn" class="normal_btn" type="button" value="明细查询"
                   onclick="queryDtl();"/>
            <%-- <input name="dtlPrintBtn" id="dtlPrintBtn" class="normal_btn" type="button" value="明细打印"
                   onclick="printDtl();"/>--%>
        </td>
    </tr>
</table>
</table>
<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="dtlQuery" style="display:none">
    <%--<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" width="100%"/>查询条件</th>--%>
    <tr>
        <td width="10%" align="right">验收单号:</td>
        <td width="20%" align="left">
            <INPUT class="middle_txt" type="text" id="CHECK_CODE2" name="CHECK_CODE2"></td>
            <td width="10%" class="table_query_right" align="right">验收日期:</td>
            <td width="20%" align="left">
                <input name="beginTime2" id="t5" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t5,t6">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't5', false);"/> 至
                <input name="endTime2" id="t6" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="t5,t6">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 't6', false);"/>
            </td>
        <td width="10%" class="table_query_right" align="right">订单来源：</td>
        <td width="20%" align="left">
            <script type="text/javascript">
                genSelBoxExp("ORDER_ORIGIN_TYPE1", <%=Constant.ORDER_ORIGIN_TYPE%>, "", true, "short_sel", "", "false", '');
            </script>
        </td>
    </tr>
        <tr>
            <td width="10%" align="right">配件编码:</td>
         <td width="20%" align="left">
             <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/>
         </td>
         <td width="10%" align="right">配件名称:</td>
         <td width="20%" align="left">
             <input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/>
         </td>
            <td width="10%" align="right">配件件号:</td>
         <td width="20%" align="left">
             <input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/>
         </td>
        </tr>
        <tr>
            <td width="10%" align="right">入库状态：</td>
            <td width="30%">
                <select id="STATE2" name="STATE2" class="short_sel" onchange="doCusChange(this.value);">
                    <option selected="" value="">-请选择-</option>
                    <option value="10041001" title="完全入库">完全入库</option>
                    <option value="10041002" title="未入库">未入库</option>
                    <option value="10041003" title="部分入库">部分入库</option>
                </select>
            </td>
        </tr>
    <tr>
        <td align="center" colspan="6">
            <input name="dtlQueryBtn" id="dtlQueryBtn" class="normal_btn" type="button" value="查询" onclick="queryDtl();"/>
            <input name="backBtn" id="backBtn" class="normal_btn" type="button" value="返回" onclick="myBack();"/>
        </td>
    </tr>
    <tr>
        <td align="center" colspan="6">
            <font color="red">本页面是入库确认明细查询页面，可查询配件的验收入库明细信息，如需入库确认或打印验收单请点击[返回]按钮！</font>
        </td>
    </tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
<jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkInstockInfo.json";
var title = null;
//采购入库查询
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    //{header: "<input type='checkbox' name='checkAll' id='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'CHK_ID', renderer: myCheckBox},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'CHK_ID,', renderer: myLink, align: 'center'},
    {header: "验货单号", dataIndex: 'CHK_CODE', style: 'text-align:left'},
    {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
    {header: "打印次数", dataIndex: 'PRINT_TIMES', align: 'center'},
    {header: "是否确认", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
    {header: "入库状态", dataIndex: 'STATE1', align: 'center'},
    {header: "验货总数量", dataIndex: 'GENERATE_QTY', align: 'center'},
    {header: "入库总数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "入库日期", dataIndex: 'IN_DATE', align: 'center', renderer: formatDate},
    {header: "入库人员", dataIndex: 'IN_NAME', align: 'center'},
//     {header: "计划员", dataIndex: 'PLANER', align: 'center'},
    {header: "库管员", dataIndex: 'WHMAN_NAME', align: 'center'},
    {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "采购单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
   /* {header: "直发单号", dataIndex: 'REMARK', style: 'text-align:left'},*/
    {header: "订单来源", dataIndex: 'ORIGIN_TYPE', align: 'center', renderer: getItemValue},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "备注", dataIndex: 'REMARK2',align: 'center',renderer: remarkInput}
];

function queryDtl() {
    $("allQuery").style.display = "none";
    $("dtlQuery").style.display = "block";
    $("STATE").value="";
    url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkInfo.json";
    //采购入库明细查询
    columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left'},
        {header: "采购单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
        /* {header: "直发单号", dataIndex: 'REMARK', style: 'text-align:left'}, */
        {header: "订单来源", dataIndex: 'ORIGIN_TYPE', align: 'center', renderer: getItemValue},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "配件种类", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
        {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
        {header: "验货数量", dataIndex: 'CHECK_QTY', align: 'center'},
        {header: "待入库数量", dataIndex: 'SPAREIN_QTY', align: 'center'},
        {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center',renderer: getInQty},
        {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
        {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
//         {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
        {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
        {header: "备注", dataIndex: 'REMARK1', align: 'center'},
        {header: "预计到货日期", dataIndex: 'FORECAST_DATE', align: 'center', renderer: formatDate},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    ];
    __extQuery__(1);
}

//明细打印
function printDtl(){
	window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderDtlPrintInit.do";
}
var len = columns.length;
//设置超链接  begin

function myLink(value, meta, record) {
    var state = record.data.STATE;
    if(state==<%=Constant.IF_TYPE_NO%>){
    	 return String.format("<a href=\"#\" onclick='printChkOrder(\"" + record.data.CHK_ID + "\")'>[打印]</a><a href=\"#\" onclick='chkDtlHandle(\"" + record.data.CHK_ID + "\")'>[确认]</a><a href=\"#\" onclick='view(\"" + record.data.CHK_ID + "\")'>[查看]</a>");
    }else{
    	return String.format("<a href=\"#\" onclick='printChkOrder(\"" + record.data.CHK_ID + "\")'>[打印]</a><a href=\"#\" onclick='view(\"" + record.data.CHK_ID + "\")'>[查看]</a>");
    }
}

function remarkInput(value, meta, record) {
    var output = '<input type="hidden" id="REMARK' + record.data.CHK_ID + '" name="REMARK' + record.data.CHK_ID + '" value="' + value + '"/>\n'+value;
    return output;
}

//全选checkbox
function myCheckBox(value, meta, record) {
    var state = record.data.STATE;
    return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>"
            + "<input type='hidden' id='STATE" + record.data.CHK_ID + "' name='STATE" + record.data.CHK_ID + "' value='" + state + "'/>\n");
}

function myBack() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderChkQueryInit.do";
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
//打印
function printChkOrder(value) {
	var remark = $("REMARK"+value).value;
	window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintHtml.do?chkId=" + value+"&remark="+remark,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
	__extQuery__(1);
}
//批量打印
function printBatch() {
    var ids = '';
    var chk = document.getElementsByName("ids");
    for (var i = 0; i < chk.length; i++) {
        if (chk[i].checked) {
            ids += chk[i].value + ','
        }
    }
    OpenHtmlWindow("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintListHtml.do?chkIds=" + ids, 1100, 400);
}
//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function clearInput() {
    //清空选定供应商
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}
//确认
function chkDtlHandle(value) {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/orderInStockHandle.do?chkId=" + value;
}
//查看
function view(value) {
    //window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/orderInStockView.do?chkId=" + value;
    OpenHtmlWindow( '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/orderInStockView.do?chkId=' + value, 800, 440);
}
function getInQty(value, meta, record){
    var state = record.data.STATE;
    var spareinQty = record.data.SPAREIN_QTY;
    if(state == <%=Constant.PART_PURCHASE_ORDERCHK_STATUS_03%> &&  spareinQty > 0){
        var output = '<input type="text" readonly style="color: red;border: none" title="强制完成入库"  value="' + value + '"/>';
    }else{
        var output = '<input type="text" readonly style="border: none"  value="' + value + '"/>';
    }
    return output;
}
</script>
</div>
</body>
</html>