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
    <title>当前订单BO查看</title>
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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;总部销售管理&gt;配件订单审核&gt;当前订单BO查看</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="orderId" id="orderId" value="${param.orderId}"/>
    <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件BO信息 &nbsp; &nbsp; 配件订单号：${param.orderCode}</h2>
            <div class="form-body">
		    <table class="table_query">
		        <tr>
		            <td align="right"> 配件编码：</td>
		            <td align="left">
		                <input class="middle_txt" id="PART_OLDCODE"
		                       name="PART_OLDCODE"
		                       type="text"/>
		            </td>
		            <td align="right"> 配件名称： </td>
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
		            <td class="center" colspan="6">
		                <input class="u-button" type="button" name="BtnQuery"
		                       id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
		            </td>
		        </tr>
		    </table>
	</div>
	</div>	    
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <table class="table_query">
        <tr>
            <td  class="center">
                <input class="u-button" type="button" value="关 闭" onclick="_hide();"/>
            </td>
        </tr>
    </table>
</form>
<script type=text/javascript>
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/showPartBo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "销售数量", dataIndex: 'SALES_QTY', align: 'center'},
    {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'}
];
</script>
</div>
</body>
</html>