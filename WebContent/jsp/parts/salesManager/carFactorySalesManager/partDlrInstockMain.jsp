<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>配件接收入库-查询主页面</title>
<script type="text/javascript">
var objArr = [];
//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partDlrInstockQuery.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'TRANS_ID', renderer: myLink, align: 'center'},
    {header: "承运物流", dataIndex: 'TRANSPORT_ORG_CN', align: 'center', style: 'text-align: center'},
    {header: "发运单", dataIndex: 'TRANS_CODE', align: 'center'},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "销售单号", dataIndex: 'SO_CODE', align: 'center'},
    /*  {header: "发票号", dataIndex: '', align: 'center'},*/
    {header: "制单人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
    {header: "发运日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "销售单位", dataIndex: 'SELLER_NAME', align: 'center'},
    {header: "金额", dataIndex: 'CONVERSEAMOUNT', align: 'center', style: 'text-align: right'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];
function myLink(value, meta, record) {
	if (record.data.STATE ==<%=Constant.CAR_FACTORY_OUTSTOCK_STATE_05%>) {
		if(record.data.FLAG != null){
			return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='payApplyPrint(\"" + value + "\")'>[打印赔偿申请单]</a>");
		}
        return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='instockPrint(\"" + value + "\")'>[打印入库单]</a>");
    }
    return String.format("<a href=\"#\" onclick='excelPurOrder(\"" + value + "\")'>[导出]</a><a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='instock(" + value + ")'>[接收入库]</a>");
}
function instockPrint(id) {
	document.fm.action="<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/opPrintHtml.do?transId=" + id;
	document.fm.target="_blank"
	document.fm.submit();
	// OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/opPrintHtml.do?transId=" + id, 1100, 400);
}
function detailOrder(value) {
   // window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partOutstockDetail.do?transId=" + value;
   OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partOutstockDetail.do?transId=" + value,1000,500);
}
function instock(value) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partInstock.do?transId=" + value;
}

function payApplyPrint(value){
    document.fm.action="<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/payApplyPrintHtml.do?transId=" + value;
    document.fm.target="_blank"
    document.fm.submit();
}


//获取CHECKBOX
function getCb(partId) {
    document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' onclick='return false' checked value='" + partId + "' />");
}
function doQuery() {
    var TstartDate = document.getElementById("TstartDate").value;
    var TendDate = document.getElementById("TendDate").value;
    var msg = "";
  /*  if (TstartDate == "") {
        if (TendDate != "") {
            msg += "请填写发运开始日期!</br>"
        }
    } else {
        if (TendDate == "") {
            msg += "请填写发运结束日期!</br>"
        }
    }*/
    if (msg != "") {
        MyAlert(msg);
        return;
    }
    __extQuery__(1);
}
function exportExcel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/exportExcel.do";
    fm.submit();
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
            inputArr[i].disabled = false;
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

//明细下载 add zhumingwei 2013-10-27
function excelPurOrder(value) {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/exportExcelDetal.do?transId=" + value;
}
//明细下载 add zhumingwei 2013-10-27(全部)
function exportExcelDetail() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/exportExcelDetail.do";
    fm.submit();
}

function showTrpInfo(trplanCode) {
    return String.format("<a href=\"#\" style='color:red' onclick='showlogistics(\"" + trplanCode + "\")'>"+trplanCode+"</a>");
}

function showlogistics(trplanCode) {
    OpenHtmlWindow("<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/queryLogisticsInfoInit.do?trplanCode=" + trplanCode, 1000, 450);
}

$(function(){
	__extQuery__(1);
});
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" alt="" />&nbsp;当前位置： 配件管理 &gt; 配件采购管理&gt; 配件接收入库
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">发运单号：</td>
							<td>
								<input class="middle_txt" type="text" name="transId" id="transId" />
							</td>
							<td class="right">发运日期：</td>
							<td>
								<input id="TstartDate" style="width: 80px;" class="short_txt" name="TstartDate" datatype="1,is_date,10" maxlength="10" group="TstartDate,TendDate"  value="${old }"/>
								<input class="time_ico" onclick="showcalendar(event, 'TstartDate', false);" value=" " type="button" />
								至
								<input id="TendDate" style="width: 80px;" class="short_txt" name="TendDate" datatype="1,is_date,10" maxlength="10" group="TstartDate,TendDate" value="${now }" />
								<input class="time_ico" onclick="showcalendar(event, 'TendDate', false);" value=" " type="button" />
							</td>
							<td class="right">状态：</td>
							<td>
								<select id="state" name="state" class="u-select">
									<option value="">-清选择</option>
									<option value="<%=Constant.CAR_FACTORY_TRANS_STATE_01%>" selected="selected">已发运</option>
									<option value="<%=Constant.CAR_FACTORY_OUTSTOCK_STATE_05%>">已入库</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">订单号：</td>
							<td>
								<input class="middle_txt" type="text" name="orderId" id="orderId" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="doQuery();" />
								<input type="button" class="u-button" value="导 出" onclick="exportExcel();" />
								<input type="button" class="u-button" value="明细导出" onclick="exportExcelDetail();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
