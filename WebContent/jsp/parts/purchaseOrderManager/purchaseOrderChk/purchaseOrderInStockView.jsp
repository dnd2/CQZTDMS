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
    当前位置：配件管理&gt; 采购计划管理&gt; 采购入库确认&gt;查看
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
        <td width="10%" class="table_query_right" align="right">配件件号：</td>
        <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
    </tr>
    <tr>
        <td align="center" colspan="6">
	        <input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
	               onclick="__extQuery__(1)" value="查 询"/>
			&nbsp;
			<input name="button" type="button" class="normal_btn" onclick="_hide();" value="关闭"/>
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
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "配件种类", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
    {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "验货数量", dataIndex: 'CHECK_QTY', align: 'center'},
    {header: "待入库数量", dataIndex: 'SPAREIN_QTY', align: 'center'},
    {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "备注", dataIndex: 'REMARK1', align: 'center'},
    {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
//     {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "预计到货日期", dataIndex: 'FORECAST_DATE', align: 'center', renderer: formatDate}
];

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function myBack() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderChkQueryInit.do";
}
</script>
</div>
</body>
</html>