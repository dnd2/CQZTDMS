<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
			String error = request.getParameter("error");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件结算确认</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});

var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalancePrint.json?isConf=1";

var title = null;

var columns = [
               {header: "序号", width: '5%', renderer: getIndex},
               {
                   header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
                   dataIndex: 'BALANCE_CODE',
                   align: 'center',
                   width: '33px',
                   renderer: seled
               },
               {id: 'action', header: "操作", sortable: false, dataIndex: 'BALANCE_CODE', renderer: myLink, align: 'center'},
               {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left',renderer:changeColor},
               {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
               {header: "结算日期", dataIndex: 'BALANCE_DATE', align: 'center', renderer: formatDate},
               {header: "发票号", dataIndex: 'INVO_NO', align: 'center', renderer: invInput},
               {header: "项数", dataIndex: 'DTL_NUM', align: 'center'},
               {header: "入库金额（含税）", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
               {header: "入库金额（无税）", dataIndex: 'IN_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "结算金额（含税）", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
               {header: "结算金额（无税）", dataIndex: 'BAL_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "结算差异\n金额（无税）", dataIndex: 'DIFF_AMOUNT', style: 'text-align:right'},
               {header: "开票金额（无税）", dataIndex: 'BAL_AMOUNT_FINAL', style: 'text-align:right'},
               {header: "临时结算与正式结算差异金额", dataIndex: 'DIFF_AMOUNT_FINAL', style: 'text-align:right'},
               {header: "发票金额（无税）", dataIndex: 'INVO_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "发票差异金额（无税）", dataIndex: 'INVO_DIFF_AMOUNT', style: 'text-align:right'},
               {header: "结算人员", dataIndex: 'BALANCE_NAME', align: 'center'}
           ];

function queryDif() {
    $("#allQuery")[0].style.display = "none";
    $("#dtlQuery")[0].style.display = "table";
    url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
        {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "计划价", dataIndex: 'PLAN_PRICE', style: 'text-align:right'},
        {header: "采购价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
        {header: "价格差异", dataIndex: 'DIF_PRICE', style: 'text-align:right'}
    ];
}

var len = columns.length;

function myLink(value,metaDate,record){
	return String.format("<a href='#' onclick='inFor(\""+ value +"\")'>[查看]</a>");
}

function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck' onclick='chkPart()'/>";
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

function changeColor(value, meta, record) {
    var DIFF_AMOUNT = record.data.DIFF_AMOUNT;
    if (DIFF_AMOUNT!='0') {
        return String.format("<font color='red'>"+value+"</font>");
    } else {
        return String.format(value);
    }
}
function invInput(value, metaDate, record) {
    var balCode = record.data.BALANCE_CODE;
    return String.format("<input type='hidden' name='inv_" + balCode + "' id='inv_" + balCode + "'  value='" + value + "'/>" + value);
}

function displayInvoice() {
    if ($("#uploadTable")[0].style.display == "none") {
        $("#uploadTable")[0].style.display = "";
    } else {
        $("#uploadTable")[0].style.display = "none";
    }
}

function inFor(value)
{
	window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalancePrintDtlInit.do?BALANCE_CODE=" + value+"&flag=1";
}

function excelPurOrder(value) {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportPurOrder.do?BALANCE_CODE="+value;
}
function exportExcel(){
    fm.action="<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportPurOrder.do";
    fm.submit();
}
function myConfirm(){
	 var chk = document.getElementsByName("ck");
	    var l = chk.length;
	    var cnt = 0;
	    for (var i = 0; i < l; i++) {
	        if (chk[i].checked) {
	            cnt++;
	            var invoNo = $("#inv_"+chk[i].value)[0].value;
	            if(!invoNo){
	            	MyAlert("第"+(i+1)+"行没有发票号,不能确认!");
	            	return;
	            }
	        }
	    }
	    if (cnt == 0) {
	        MyAlert("请选择要确认的结算单!");
	        return;
	    }

	    confirmBalance();
}

function confirmBalance(){
	MyConfirm("是否确认？",function(){
		var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/confirmBalance.json';
	    sendAjax(url, handleControl, 'fm');
	})
}

//驳回
function rejectBal() {
	var chk = document.getElementsByName("ck");
    var l = chk.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要驳回的结算单!");
        return;
    }
    var rejectReason = document.getElementById("rejectReason").value;
    if ((rejectReason.trim()) == "") {
        MyAlert("请输入驳回原因!");
        return;
    }
    MyConfirm("确定驳回？", setRejectBal);
}

//驳回
function setRejectBal() {
    btnDisable();
    var curPage = myPage.page;
    document.getElementById("curPage").value = curPage;
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/rejectBalanceOrder.json';
    makeNomalFormCall(url, handleControl, 'fm');
}

function handleControl(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (error) {
            MyAlert(error);
            __extQuery__(1);
        } else if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (exceptions) {
            MyAlert(exceptions.message);
            __extQuery__(1);
        }
    }
}

function queryAll() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalAllQueryInit.do";
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

//导出
function exportOrderBalDifExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalDifExcel.do";
    fm.target = "_self";
    fm.submit();
}


function myback() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalanceConfirmQueryInit.do";
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 当前位置：配件管理&gt; 配件财务管理&gt; 配件结算审核
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="curPage" id="curPage" />
			<input type="hidden" name="partId" id="partId" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query"  id="allQuery">
						<tr>
							<td class="right">结算单号：</td>
							<td>
								<input class="middle_txt" type="text" name="BALANCE_CODE" />
							</td>
							<td class="right">供应商名称：</td>
							<td>
								<input class="middle_txt" type="text" name="VENDER_NAME" />
							</td>
							<td class="right">发票号：</td>
							<td>
								<input class="middle_txt" type="text" name="INVO_NO" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询" />
								<!--          &nbsp;<input class="u-button" type="button" value="汇总查询"
                       onclick="queryAll();"/> -->
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
								&nbsp;
								<input name="dtlQueryBtn" id="dtlQueryBtn" class="u-button" type="button" value="结算差异查询" onclick="queryDif();" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="myConfirm();" value="确认" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="displayInvoice();" value="驳回" />
								<!--  &nbsp;<input name="button" type="button" class="u-button" onclick="exportExcel();"
                                 value="明细导出"/> -->
							</td>
						</tr>
						<tr id="uploadTable" style="display: none">
							<td class="right">驳回原因：</td>
							<td colspan="3">
								<textarea name="rejectReason" id="rejectReason" style="width: 90%" rows="4"></textarea>
							</td>
							<td>
								<input type="button" class="u-button" onclick="rejectBal();" value="确定驳回" />
							</td>
						</tr>
					</table>

					<table class="table_query" id="dtlQuery" style="display: none">
						<tr>
							<td class="right">结算单号：</td>
							<td>
								<input class="middle_txt" type="text" name="BALANCE_CODE1" />
							</td>
							<td class="right">结算日期：</td>
							<td>
								<input name="balBeginTime" id="balBeginTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'balBeginTime', false);" />
								至
								<input name="balEndTime" id="balEndTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'balEndTime', false);" />
							</td>
							<td class="right">配件种类：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES%>, "", true, "", "", "false", '');
								</script>
							</td>
						</tr>

						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME" />
							</td>
							<td class="right">配件件号：</td>
							<td>
								<input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE" />
							</td>
						</tr>

						<tr>
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn2" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询" />
								<%--      &nbsp;<input name="button" type="button" class="long_btn" onclick="setCheckModel();" value="设置发票号"/>--%>
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="exportOrderBalDifExcel();" value="导出" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="myback();" value="返回" />
							</td>
						</tr>
					</table>

				</div>
			</div>

			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
	</div>
</body>
</html>