<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>采购订单明细</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理&gt;采购订单查询&gt; 查看
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
		<div class="form-body">
        <table class="table_query">
            <tr>
                <td class="right">配件编码：</td>
                <td >
                	<input class="middle_txt" type="hidden" id="ORDER_CODE"  name="ORDER_CODE" value="${orderCode}"/>
                	<input class="middle_txt" type="text" id="PART_OLDCODE"  name="PART_OLDCODE" value=""/>
                </td>
                <td class="right">配件名称：</td>
                <td >
                    <input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME" value=""/>
                </td>
                
                <td class="right">配件件号：</td>
                <td >
                	<input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE" value=""/>
                </td>
            </tr>
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="u-button u-query" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="_hide();" value="关闭"/>&nbsp;
                </td>
            </tr>
        </table>
	 </div>
	 </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
    <script type="text/javascript">
        var myPage;
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getPurcharOrderMx.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				//{header: "操作",dataIndex:'PO_ID',renderer: myLink, style:"text-align: center"},
				{header: "采购单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
				{header: "计划单号",dataIndex:'PLAN_CODE', style:"text-align: center"},
				{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align:center"},
				{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
				{header: "采购数量",dataIndex:'BUY_QTY', style:"text-align: center"},
// 				{header: "验收数量",dataIndex:'CHECK_QTY', style:"text-align: center"},
				{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
				{header: "采购日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
				{header: "备注",dataIndex:'REMARK', style:"text-align: center"},
 				{header: "计划员",dataIndex:'PLANER', style:"text-align: center"},
 				{header: "采购员",dataIndex:'BUYER', style:"text-align: center"},
				{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue}
            ];
    </script>
</div>
</body>
</html>