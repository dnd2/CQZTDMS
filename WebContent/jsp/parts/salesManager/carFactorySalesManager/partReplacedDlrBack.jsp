<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>
</head>
<script language="javascript">

    //初始化查询TABLE
    var myPage;

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderBackhaulQuery.json?ORDER_TYPE=" +<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>;

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', style: 'text-align:center'},
        {header: "活动编码", dataIndex: 'ACTIVITY_CODE', style: 'text-align:left'},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', style: 'text-align:center', renderer: getItemValue},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        {header: "订货人", dataIndex: 'BUYER_NAME', style: 'text-align:left'},
        {header: "制单日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
        {header: "提交日期", dataIndex: 'SUBMIT_DATE', style: 'text-align:center'},
        {header: "订单状态", dataIndex: 'STATE', style: 'text-align:center', renderer: getItemValue}

    ];

    function myLink(value, meta, record) {
        if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13%>) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
        }
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='backhaulOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[回运]</a>");
    }
    function detailOrder(value, code) {
        var buttonFalg = "disabled";
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/orderDetailQuery.do?orderId=" + value + "&&orderCode=" + code + "&buttonFalg=" + buttonFalg, 900, 400);
    }
    //回运
    function backhaulOrder(value, type) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/backauilDlrOrder.do?orderId=" + value;
    }
    //查询
    function doQuery() {
        __extQuery__(1);
    }
    function exportEx() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/exportPartExcelBack.do";
        fm.submit();
    }

    function doInit() {
        //默认状态
        $('state').value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03%>;
        __extQuery__(1);
    }
</script>
<body onload="loadcalendar();doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden"
           value="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理&gt;配件采购管理&gt;切换订单回运</div>
        <table border="0" class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr>
                <td width="10%" align="right">订单号：</td>
                <td width="20%" align="left"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/>
                </td>
                <td width="10%" align="right">订货单位：</td>
                <td width="20%" align="left"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/>
                </td>
                <td width="10%" align="right">订单状态：</td>
                <td width="20%" align="left">
                    <select name="state" id="state" class="short_sel">
                        <%--		                <option selected value=''>请选择</option>--%>
                        <c:forEach items="${stateMap}" var="stateMap">
                            <option value="${stateMap.key}">${stateMap.value}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="doQuery();"/>
                    <!--                     <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/> -->
                    <input class="normal_btn" type="button" value="导 出" onclick="exportEx()"/></td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>

</html>
