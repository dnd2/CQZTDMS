<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>出库现场BO查看</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
    </style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;本部销售管理&gt;配件出库单&gt;出库现场BO查看</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="boId" id="boId" value="${param.boId}"/>
    <table class="table_query">
        <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>配件BO信息</th>
        <tr>
            <td align="right">
                配件编码：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_OLDCODE"
                       name="PART_OLDCODE"
                       type="text"/>
            </td>
            <td align="right">
                配件名称：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_CNAME"
                       name="PART_CNAME"
                       type="text"/>
            </td>
            <td align="right">件号：</td>
            <td align="left">
                <input class="middle_txt" id="PART_CODE"
                       datatype="1,is_noquotation,30" name="PART_CODE"
                       type="text"/>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="6">
                <input class="normal_btn" type="button" name="BtnQuery"
                       id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <table class="table_edit">
        <tr align="center">
            <td>

                <input class="normal_btn" type="button" value="关 闭" onclick="doConfirm();"/>
            </td>
        </tr>
    </table>
</form>
<script type=text/javascript>

var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/showPartLocBo.json";

var title = null;

var columns = [
		{header: "序号", align: 'center', renderer: getIndex},
		{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
		{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
		{header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
		{header: "货位", dataIndex: 'LOC_NAME', align: 'center'},
		{header: "销售数量", dataIndex: 'BUY_QTY', align: 'center'},
		{header: "装箱数量", dataIndex: 'SALES_QTY', align: 'center'},
		{header: "现场BO数量", dataIndex: 'BO_QTY', align: 'center'}
];

function doConfirm(){
	if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
		top.queryOutStockMain();
	}else{
		parentContainer.queryOutStockMain();
	}
	parent._hide();
}
</script>
</div>
</body>
</html>