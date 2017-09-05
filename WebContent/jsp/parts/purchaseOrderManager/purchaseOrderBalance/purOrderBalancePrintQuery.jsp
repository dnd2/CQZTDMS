<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>结算凭证打印</title>
    
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalancePrint.json";

var title = null;

var columns = [
    {header: "序号", width: '5%', renderer: getIndex},
    {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "结算日期", dataIndex: 'BALANCE_DATE', align: 'center', renderer: formatDate},
    {header: "发票号", dataIndex: 'INVO_NO', align: 'center'},
    {header: "项数", dataIndex: 'DTL_NUM', align: 'center'},
    {header: "结算金额（含税）", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
    {header: "结算金额（无税）", dataIndex: 'BAL_AMOUNT_NOTAX', style: 'text-align:right'},
    {header: "结算人员", dataIndex: 'BALANCE_NAME', align: 'center'},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'BALANCE_CODE', renderer: myLink, align: 'center'}
];

function myLink(value,metaDate,record){
	return String.format("<a href='#' onclick='inFor(\""+ value +"\")'>[查看]</a><a href='#' onclick='print(\""+ value +"\")'>[打印]</a>");
}

function inFor(value)
{
	window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalancePrintDtlInit.do?BALANCE_CODE=" + value;
}

function print(value)
{
		window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalancePrint.do?BALANCE_CODE="+value,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
</script>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：配件管理&gt; 采购计划管理&gt; 结算凭证打印
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="allQuery">
        <tr>
            <td  class="right">结算单号：</td>
            <td ><input class="middle_txt" type="text" name="BALANCE_CODE"/></td>
            <td class="right">供应商名称：</td>
            <td >
                <input class="middle_txt" type="text" name="VENDER_NAME"/>
            </td>
            <td class="right">发票号：</td>
            <td >
                <input class="middle_txt" type="text" name="INVO_NO"/>
            </td>
        </tr>
        <tr>
            <td class="center" colspan="6">
            <input name="BtnQuery" id="queryBtn" type="button" class="u-button"
                                                  onclick="__extQuery__(1);" value="查 询"/>
                </td>
        </tr>
    </table>
 </div>
 </div>  
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

</div>
</body>
</html>