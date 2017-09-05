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
    <title>验收单查看</title>
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
    当前位置：配件管理&gt; 采购订单管理&gt; 验收单打印&gt;查看
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="chkId" value="${chkId }"/>
    <table class="table_query" bordercolor="#DAE0EE">
        <th colspan="4" width="100%"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td width="10%" class="table_query_right" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" class="table_query_right" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
        </tr>
        <tr>
            <td align="center" colspan="4"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
                                                  onclick="__extQuery__(1)" value="查 询"/>
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

var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryChkOrderDetail.json";

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "验收数量", dataIndex: 'GENERATE_QTY', align: 'center'},
    /*{header: "已验收数量", dataIndex: 'CHECK_QTY', align: 'center'},
    {header: "待验收数量", dataIndex: 'SPARE_QTY', align: 'center'},*/
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

function myBack() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderChkPrintInit.do";
}
</script>
</div>
</body>
</html>