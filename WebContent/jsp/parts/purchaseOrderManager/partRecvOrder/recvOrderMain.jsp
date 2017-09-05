<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>配件领件订单</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            loadcalendar();  //初始化时间控件
            pageSearch('normal');
        }
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>
        &nbsp;当前位置：配件管理 &gt; 采购计划管理 &gt; 配件领件订单
    </div>
    <form method="post" name="fm" id="fm">
        <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
        <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
        <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
        <input type="hidden" name="actionURL" id="actionURL" value=""/>
        <input type="hidden" name="searchType" id="searchType" value=""/>
        <input type="hidden" name="remark" id="remark" value=""/>
        <table class="table_query" id="normalTable">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">领件单号：</td>
                <td width="25%">
                    <input class="long_txt" type="text" id="orderCode" name="orderCode"></td>
                <td width="10%" align="right">采购订单号：</td>
                <td width="20%"><input class="middle_txt" type="text" id="purOrderCode" name="purOrderCode"></td>
                <td width="10%" align="right">制单人：</td>
                <td width="20%">
                    <select  name="buyer" id="buyer" style="width:150px;">
			      		<option value="">-请选择-</option>
			  			<c:if test="${plannersList!=null}">
							<c:forEach items="${plannersList}" var="list">
							  <c:choose> 
								<c:when test="${currUserId eq list.USER_ID}">
								  <option selected="selected" value="${list.NAME }">${list.NAME }</option>
								</c:when>
								<c:otherwise>
								  <option value="${list.NAME }">${list.NAME }</option>
								</c:otherwise>
							  </c:choose>
							</c:forEach>
						</c:if>
			      	</select>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">制单日期：</td>
                <td width="25%">
                    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" "
                           type="button"/>
                    至
                    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" "
                           type="button"/>
                </td>
                <td width="10%" align="right">仓库：</td>
                <td width="20%">
                    <select name="whId" id="whId" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:if test="${WHList!=null}">
                            <c:forEach items="${WHList}" var="list">
                                <option value="${list.WH_ID }">${list.WH_CNAME }</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
                <td width="10%" align="right">状态：</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        genSelBoxExp("state", <%=Constant.PURCHASE_ORDER_STATE%>, <%=Constant.PURCHASE_ORDER_STATE_01%>, true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="25%">
                    <input class="long_txt" type="text" name="partOldcode" id="partOldcode"/>
                </td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" name="partName" id="partName"/>
                </td>
                <td width="10%" align="right">配件件号：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="partCode" name="partCode"></td>
            </tr>
            <tr>
                <td width="10%" align="right">打印日期：</td>
                <td width="25%">
                    <input id="printSDate" class="short_txt" name="printSDate" datatype="1,is_date,10" maxlength="10"
                           group="printSDate,printEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'printSDate', false);" value=" "
                           type="button"/>
                    至
                    <input id="printEDate" class="short_txt" name="printEDate" datatype="1,is_date,10" maxlength="10"
                           group="printSDate,printEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'printEDate', false);" value=" "
                           type="button"/>
                </td>
                <td width="10%" align="right"></td>
                <td width="20%">
                </td>
                <td width="10%" align="right"></td>
                <td width="20%" align="left">
                </td>
            </tr>
            <tr>
                <td align="center" colspan="64">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="pageSearch('normal')"/>
                    <input class="normal_btn" type="button" value="明细查询" name="dtlQueryBtn" id="dtlQueryBtn"
                           onclick="pageSearch('detail')"/>
                    <input class="normal_btn" type="button" value="新 增" onclick="addRec()"/>
           <!--     <input class="normal_btn" type="button" value="汇总打印" onclick="groupPrint()"/>
                    <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>  -->
                </td>
            </tr>
        </table>
        <table class="table_query" id="detailTable" style="display: none;">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">领件单号：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="orderCode1" name="orderCode1"></td>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" name="partOldcode1" id="partOldcode1"/>
                </td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" name="partName1" id="partName1"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="64">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="pageSearch('normal')"/>
                    <input class="normal_btn" type="button" value="明细查询" name="dtlQueryBtn" id="dtlQueryBtn"
                           onclick="pageSearch('detail')"/>
                    <input class="normal_btn" type="button" value="新 增" onclick="addRec()"/>
              <!--  <input class="normal_btn" type="button" value="汇总打印" onclick="groupPrint()"/>
                    <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>  -->
                </td>
            </tr>
        </table>
</div>

<!-- 查询条件 end -->
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
<jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
<!--分页 end -->
</form>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partRecvOrderSearch.json";

var title = null;
var columns = null;

function pageSearch(parms) {
    var normTbObj = document.getElementById("normalTable");
    var dtllTbObj = document.getElementById("detailTable");
    var schTypeObj = document.getElementById("searchType")

    if ("normal" == parms) {
        dtllTbObj.style.cssText = "display: none;";
        normTbObj.style.cssText = "display: block;";
        schTypeObj.value = parms;

        var state = document.getElementById("state").value;
        var state2 = <%=Constant.PURCHASE_ORDER_STATE_02%>;//已完成
        var state3 = <%=Constant.PURCHASE_ORDER_STATE_03%>;//已关闭

        if (state2 == state) {
            columns = [
                {header: "序号", dataIndex: 'ORDER_ID', align: 'center', renderer: getIndex},
   //             {header: '<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selAll(this)\" />', dataIndex: 'ORDER_ID', renderer: myCheckBox},
   				{id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
   				{header: "采购订单号", dataIndex: 'PUR_ORDER_CODE', style: 'text-align: left;'},
                {header: "领件单号", dataIndex: 'ORDER_CODE', style: 'text-align: left;',renderer: viewDetail},
                {header: "制单人", dataIndex: 'BUYER', align: 'center'},
                {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
                {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
                {header: "仓库", dataIndex: 'WH_NAME', align: 'center'},
                {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center'},
                {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
                {header: "总金额(元)", dataIndex: 'AMOUNT', style: 'text-align: right;'},
                {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
            ];
        }
        else if(state3 == state)
        {
        	columns = [
                       {header: "序号", dataIndex: 'ORDER_ID', align: 'center', renderer: getIndex},
          //             {header: '<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selAll(this)\" />', dataIndex: 'ORDER_ID', renderer: myCheckBox},
          				{id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
                       {header: "采购订单号", dataIndex: 'PUR_ORDER_CODE', style: 'text-align: left;'},
                       {header: "领件单号", dataIndex: 'ORDER_CODE', style: 'text-align: left;',renderer: viewDetail},
                       {header: "制单人", dataIndex: 'BUYER', align: 'center'},
                       {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
                       {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
                       {header: "仓库", dataIndex: 'WH_NAME', align: 'center'},
                       {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center'},
                       {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
                       {header: "总金额(元)", dataIndex: 'AMOUNT', style: 'text-align: right;'},
                       {header: "关闭原因", dataIndex: 'REMARK1',style: 'text-align: left;'},
                       {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
                   ];
        }
        else {
            columns = [
                {header: "序号", dataIndex: 'ORDER_ID', align: 'center', renderer: getIndex},
     //           {header: '<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selAll(this)\" />', dataIndex: 'ORDER_ID', renderer: myCheckBox},
                {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
                {header: "关闭原因", dataIndex: 'REMARK1', align: 'center', renderer: getRemark1Value},
                {header: "采购订单号", dataIndex: 'PUR_ORDER_CODE', style: 'text-align: left;'},
                {header: "领件单号", dataIndex: 'ORDER_CODE', style: 'text-align: left;',renderer: viewDetail},
                {header: "制单人", dataIndex: 'BUYER', align: 'center'},
                {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
                {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
                {header: "仓库", dataIndex: 'WH_NAME', align: 'center'},
                {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center'},
                {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
                {header: "总金额(元)", dataIndex: 'AMOUNT', style: 'text-align: right;'},
                {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
            ];
        }

    }
    else {
        normTbObj.style.cssText = "display: none;";
        dtllTbObj.style.cssText = "display: block;";
        schTypeObj.value = parms;

        columns = [
            {header: "序号", dataIndex: 'ORDER_ID', align: 'center', renderer: getIndex},
            {header: "领件单号", dataIndex: 'ORDER_CODE',style: 'text-align: left;'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            {header: "领件数量", dataIndex: 'PLAN_QTY', align: 'center'},
            {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "已生成数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
        ];
    }


    __extQuery__(1);
}


function getRemark1Value(value, meta, record) {
    var output = '<input type="text" id="REMARK1' + record.data.ORDER_ID + '" name="REMARK1' + record.data.ORDER_ID + '" value="' + value + '"/>\n';
    return output;
}

function myCheckBox(value, meta, record) {
    return String.format("<input type='checkbox' name='orders' value='" + value + "' />");
}

//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    var purOrderCode = record.data.PUR_ORDER_CODE;
    if (state == <%=Constant.PURCHASE_ORDER_STATE_02%> || state == <%=Constant.PURCHASE_ORDER_STATE_03%>) {//如果已完成/已关闭就只能打印
        return String.format("<a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a>");
    }
//    if (state ==<%=Constant.PURCHASE_ORDER_STATE_03%>) {//如果已关闭就只能打开和打印
//        return String.format("<a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a>";
//                + "<a href=\"#\" onclick='openPurOrder(\"" + value + "\")'>[打开]</a>");
//    }
    return String.format("<a href=\"#\" onclick='printPurOrder(\"" + value + "\")'>[打印]</a>"
            + "<a href=\"#\" onclick='closePurOrder(\"" + value + "\",\"" + purOrderCode + "\")'>[关闭]</a>"
            + "<a href=\"#\" onclick='checkDetail(\"" + value + "\")'>[生成验收指令]</a>");
}

function viewDetail(value, meta, record)
{
	var orderId = record.data.ORDER_ID;
	return String.format("<a href=\"#\" onclick='viewDetailFunc(\"" + orderId + "\")'>"+value+"</a>");
}

function selAll(obj) {
    var cks = document.getElementsByName('orders');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
    }
}


function viewDetailFunc(value) {
    var optionType = "detail";
    OpenHtmlWindow("<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/viewOrderDeatilInint.do?orderId=" + value + "&optionType=" + optionType, 950, 500);
}

//生成验收指令
function checkDetail(value,flag) {
	var actUrl = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partRecInit.do";
	if(confirm("制造商是否按默认供应商生成验收单?")){
		window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderView2.do?orderId=" + value+"&flag=" + flag + "&actUrl=" + actUrl;
	}else{
		window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderView.do?orderId=" + value+"&flag=" + flag + "&actUrl=" + actUrl;
	}
}

//打开
function openPurOrder(orderId) {
    var option = "open";
    var purOrderCode = "";
    var remark = document.getElementById("REMARK1" + orderId).value;
    if ("" == remark) {
        MyAlert("请填写打开原因!");
        return false;
    }
    document.getElementById("remark").value = remark;
    MyConfirm("确定打开?", commitOrder, [orderId, option, purOrderCode]);
}

//关闭
function closePurOrder(orderId, purOrderCode) {
    var option = "close";
    var purOrderCodeTmp = "";
    if(null != purOrderCode && "null" != purOrderCode && "" != purOrderCode)
    {
    	purOrderCodeTmp = purOrderCode; 
    }
    var remark = document.getElementById("REMARK1" + orderId).value;
    if ("" == remark) {
        MyAlert("请填写关闭原因!");
        return false;
    }
    document.getElementById("remark").value = remark;
    MyConfirm("确定关闭?", commitOrder, [orderId, option, purOrderCodeTmp]);
}

function commitOrder(orderId, option, purOrderCode) {
    btnDisable();
    var url = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/closeOrOpenOrder.json?orderId=" + orderId + "&option=" + option + "&purOrderCode=" + purOrderCode + "&curPage=" + myPage.page;
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
            __extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
            MyAlert("操作成功!");
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }
}

//修改
function moifyPg(parms) {
    btnDisable();
    var optionType = "modify";
    var actionURL = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partResInit.do";
    document.getElementById("actionURL").value = actionURL;
    document.fm.action = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/viewOrderDeatilInint.do?changeId=" + parms + "&optionType=" + optionType;
    document.fm.target = "_self";
    document.fm.submit();
}


//打印(单)
function printPurOrder(value) {
    window.open("<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/opPrintHtml.do?selOrders=" + value, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//汇总打印
function groupPrint() {
    var cks = document.getElementsByName('orders');
    var selOrders = "";
    var count = 0;
    for (var i = 0; i < cks.length; i++) {
        if (cks[i].checked) {
            selOrders += cks[i].value + ",";
            count++;
        }
    }
    if (count > 0) {
        window.open("<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/opPrintHtml.do?selOrders=" + selOrders, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
    }
    else {
        MyAlert("请先选择领件单号!");
        return;
    }
}

//新增领件
function addRec() {
    var parentOrgId = document.getElementById("parentOrgId").value;
    var companyName = document.getElementById("companyName").value;
    var actionURL = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partOrderAddInit.do";
    document.getElementById("actionURL").value = actionURL;
    btnDisable();
    document.fm.action = actionURL;
    document.fm.target = "_self";
    document.fm.submit();
}


//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/exportSaleOrdersExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

//失效按钮
function btnDisable() {

    $$('input[type="button"]').each(function (button) {
        button.disabled = true;
    });

}

//有效按钮
function btnEnable() {

    $$('input[type="button"]').each(function (button) {
        button.disabled = "";
    });

}
</script>
</body>
</html>