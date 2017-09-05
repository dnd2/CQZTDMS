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
    <title>配件结算确认明细</title>
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
    当前位置：配件管理&gt; 配件财务管理&gt; 配件结算确认
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="allQuery">
        <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td width="10%" align="right">结算单号：</td>
            <td width="20%"><input class="middle_txt" type="text" name="BALANCE_CODE"/></td>
            <td width="10%" class="table_query_right" align="right">供应商名称：</td>
            <td width="20%">
                <input class="middle_txt" type="text" name="VENDER_NAME"/>
            </td>
            <td width="10%" class="table_query_right" align="right">发票号：</td>
            <td width="20%">
                <input class="middle_txt" type="text" name="INVO_NO"/>
            </td>
        </tr>
        <tr>
                <td align="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
                                                  onclick="__extQuery__(1);" value="查 询"/>
               &nbsp;<input class="normal_btn" type="button" value="导 出" onclick="expBalanceConfDtlExcel();"/>
               &nbsp;<input name="button" type="button" class="normal_btn" onclick="myback();"
                             value="返回"/></td>
                </td>
        </tr>
    </table>
   
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryBalanceConfirmDtl.json";

var title = null;

var columns = [
    {header: "序号", width: '5%', renderer: getIndex},
    {header: "验收单号", dataIndex: 'CHECK_CODE',  style: 'text-align:left'},
    {header: "入库单号", dataIndex: 'IN_CODE',  style: 'text-align:left'},
    {header: "结算单号", dataIndex: 'BALANCE_CODE',  style: 'text-align:left'},
    {header: "发票号", dataIndex: 'INVO_NO',  style: 'text-align:right'},
    {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
    {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME',  style: 'text-align:left'},
    {header: "结算金额（含税）", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
    {header: "结算金额（无税）", dataIndex: 'IN_AMOUNT_NOTAX', style: 'text-align:right'},
    {header: "结算人员", dataIndex: 'BALANCE_NAME', align: 'center'},
    {header: "结算时间", dataIndex: 'BALANCE_DATE', align: 'center', renderer: formatDate}
];

//导出
function expBalanceConfDtlExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/expBalanceConfDtlExcel.do";
    fm.target = "_self";
    fm.submit();
}

function myback() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalanceConfirmQueryInit.do";
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
</script>
</div>
</body>
</html>