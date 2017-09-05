<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <title>服务商季度返利导入确认</title>
</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置&gt;财务管理&gt;返利管理&gt;配件季度返利导入&gt;确认导入
</div>
<form name="fm" method="post" enctype="multipart/form-data" id="fm">
    <table class="table_grid" width="80%">
        <tr>
            <td align="center" width="2%">序号</td>
            <td align="center" width="5%">服务商代码</td>
            <td align="center" width="20%">服务商名称</td>
            <td align="center" width="20%">返利类型</td>
            <td align="center" width="5%">返利金额</td>
        </tr>
        <c:forEach items="${list}" var="list" varStatus="status">
            <c:if test="${status.count%2 == 0}">
                <tr class="table_list_row2" style="BACKGROUND-COLOR: #f7f7f7">
            </c:if>
            <c:if test="${status.count%2 != 0}">
                <tr class="table_list_row1" >
            </c:if>
            <td align="center">${status.count}
                <input type="hidden" name="dealerId" id="dealerId_${list.dealerId}"
                       value="${list.dealerId}"/></td>
            <td align="center">${list.dealerCode}
                <input type="hidden" id="dealerCode_${list.dealerId}"
                       name="dealerCode_${list.dealerId}" value="${list.dealerCode}"/>
            </td>
            <td align="left">${list.dealerName}
                <input type="hidden" id="dealerName_${list.dealerId}"
                       name="dealerName_${list.dealerId}" value="${list.dealerName}"/>
            </td>
            <td align="center">${list.rebateType}
                <input type="hidden" id="rebateType_${list.dealerId}"
                       name="rebateType_${list.dealerId}" value="${list.rebateType}"/></td>
            <td align="center">${list.amount}
                <input type="hidden" id="amount_${list.dealerId}"
                       name="amount_${list.dealerId}" value="${list.amount}"/></td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5" align="center">
                <input class="normal_btn" type="button" value="确定" name="button1" id="button1" onClick="imprt();">
                <input class="normal_btn" type="button" value="返回" name="button2" id="button2"
                       onClick="javascript:history.go(-1);">
            </td>
        </tr>
    </table>
    <input type="hidden" name="count" value="${count }"/>
    <input type="hidden" name="taskMonth" value="${taskMonth }"/>

</form>
</div>
<script type="text/javascript">
    function imprt() {
        if (confirm("确认导入?")) {
            document.getElementById("button1").disabled = true;
            document.getElementById("button1").disabled = true;
            fm.action = "<%=request.getContextPath()%>/parts/financeManager/dealerRateManager/RebateManager/monthPlanAdd.do";
            fm.submit();
        }
    }
</script>
</body>
</html>
