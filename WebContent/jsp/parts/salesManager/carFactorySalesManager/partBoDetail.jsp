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
    <title>配件BO查看</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
        .panel-bottom{margin-bottom: 5px}
    </style>
</head>
<body onload="__extQuery__(1);"> <!--  onunload='javascript:destoryPrototype()' -->
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt; BO单查询 &gt; 查看</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="boId" id="boId" value="${po['BO_ID'] }"/>
    <input type="hidden" name="boCode" id="boCode" value="${po['BO_CODE'] }"/>
    <input type="hidden" name="orderIdE" id="orderIdE" value="${orderId}"/>
    <input type="hidden" name="flag" id="flag" value="${flag}"/>
    <div class="form-panel panel-bottom">
        <h2><img class="nav" src="<%=contextPath%>/img/nav.gif"/>BO单信息</h2>
        <div class="form-body">
            <table class="table_query">
                <tr>
                    <td width="10%" class="right">BO单号：</td>
                    <td width="20%">
                        ${po['BO_CODE'] }
                    </td>
                    <td width="10%" class="right">配件订单号：</td>
                    <td width="20%">
                        ${po['ORDER_CODE'] }
                    </td>
                    <td width="10%" class="right">制单人：</td>
                    <td width="20%">
                        ${po['NAME'] }
                    </td>
                </tr>
                <tr>
                    <td class="right">制单日期：</td>
                    <td>
                        ${po['CREATE_DATE'] }
                    </td>
                    <td class="right">销售单位：</td>
                    <td>
                        ${po['SELLER_NAME'] }
                    </td>
                    <td class="right">订货单位：</td>
                    <td>
                        ${po['DEALER_NAME'] }
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="form-panel">
        <h2><img src="<%=contextPath%>/img/subNav.gif" alt="" class="panel-icon nav"/>配件信息</h2>
        <div class="form-body">
            <table class="table_query">
                <th colspan="6" width="100%"></th>
                <tr>
                    <td class="right"> 配件编码：</td>
                    <td align="left">
                        <input class="middle_txt" id="PART_OLDCODE"
                            name="PART_OLDCODE"
                            type="text"/>
                    </td>
                    <td class="right">  配件名称： </td>
                    <td align="left">
                        <input class="middle_txt" id="PART_CNAME" name="PART_CNAME" type="text"/>
                    </td>
                    <!-- 
                    <td class="right">件号：</td>
                    <td align="left">
                        <input class="middle_txt" id="PART_CODE"
                            datatype="1,is_noquotation,30" name="PART_CODE"
                            type="text"/>
                    </td>
                    -->
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
                <c:choose>
                    <c:when test="${'disabled' eq buttonFalg}">
                    	<input class="u-button" type="button" value="明细下载" onclick="exportDetl()"/>
                        <input class="u-button" type="button" value="关 闭" onclick="_hide();"/>
                    </c:when>
                    <c:otherwise>
                        <input class="u-button u-cancel" type="button" value="返 回" onclick="goBack()"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</form>
<script type=text/javascript>
var myPage;
var orderId = ${orderId};
var flag =${flag};
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetail.json?orderId=" + orderId;
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left',renderer:spe},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
   // {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "订货单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "满足数量", dataIndex: 'SALES_QTY', align: 'center'},
    {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
    {header: "转销售数量", dataIndex: 'TOSAL_QTY', align: 'center'},
    {header: "关闭数量", dataIndex: 'CLOSE_QTY', align: 'center'},
    {header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align: 'center'},
    {header: "库存数量", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "生成日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "处理状态", dataIndex: 'STATUS', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

function goBack() {
    if (flag == 1) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do";
    } else {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boQueryInit.do";
    }
}

//明细下载
function exportDetl()
{
	document.fm.action="<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/exportOrderExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
function spe(value, meta, record) {
    var normal_qty = record.data.NORMAL_QTY;
    if (normal_qty > 0) {
        return String.format("<input style='background-color: chartreuse;border: none' class='middle_txt' type='text' value=\"" + value + "\"   readonly/> ");
    }else{
        return String.format("<input style='border: none'  class='middle_txt'  type='text' value=\"" + value + "\"   readonly/> ");
    }
}
</script>
</div>
</body>
</html>
