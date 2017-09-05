<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>BO单明细查看</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
    </style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：当前位置： 报表管理 > 本部销售报表>
        BO风险报表>BO单明细</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="partId" id="partId" value="${partId}"/>
     <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>

                <td width="10%" align="right">BO单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="BO_CODE" id="BO_CODE"/></td>
                <td width="10%"   align="right">BO产生日期：</td>
                <td width="22%">
                    <input name="startDate" id="t1" value="${startDate}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="${endDate}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
                <td width="10%"   align="right">订货单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
            </tr>
            <tr>
                <td width="10%"   align="right">订货单位编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE"/></td>
                <td width="10%"   align="right">订货单位名称：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
                </td>
            </tr>
        </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<script type=text/javascript>

var myPage;

var url = "<%=contextPath%>/report/partReport/partSalesReport/BoRiskReport/queryPartBoDetail.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "BO单号", dataIndex: 'BO_CODE', align: 'center'},
    {header: "订货单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
    //{header: "销售单位", dataIndex: 'SELLER_NAME', style: 'text-align:left'},
    {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "交货数量", dataIndex: 'SALES_QTY', align: 'center'},
    {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
    {header: "BO金额", dataIndex: 'AMOUNT', style: 'text-align:left'},
    {header: "满足数量", dataIndex: 'TOSAL_QTY', align: 'center'},
    {header: "BO生成日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

function goBack() {
	window.location.href = "<%=contextPath%>/report/partReport/partSalesReport/BoRiskReport/boRiskReportInit.do";
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
