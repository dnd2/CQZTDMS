<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    String addr = request.getParameter("addr");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
 <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display:none">
 </OBJECT>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <title></title>
 <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
 <link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
 <style media=print>
     .Noprint {
         display: none;
     }

     .p_next {
         page-break-after: always;
     }
 </style>
</head>
<body>
<c:forEach items="${list}" var="data">
    <jsp:include page="${contextPath}/jsp/parts/financeManager/partInvoiceQuery/print.jsp">
        <jsp:param value="${dataMap.year}" name="year"/>
        <jsp:param value="${dataMap.month}" name="month"/>
        <jsp:param value="${dataMap.day}" name="day"/>
        <jsp:param value="${dataMap.hour}" name="hour"/>
        <jsp:param value="${data.TEL}" name="TEL"/>
        <jsp:param value="${data.DEALER_NAME}" name="DEALER_NAME"/>
        <jsp:param value="${data.ADDR}" name="ADDR"/>
        <jsp:param value="${data.POST_CODE}" name="POST_CODE"/>
        <jsp:param value="${data.AMOUNT}" name="AMOUNT"/>
    </jsp:include>
</c:forEach>

<table border=0 cellpadding=0 cellspacing=0 class="Noprint">
    <tr>
        <td width=90%>
        </td>
        <td width=5%>
            <table border=0 width=100%>
                <tr>
                    <td>
                        <input type=button id="printBtn1" class="txtToolBarButton" value="打印" onClick="printOrder()">
                        <%--<input type=button id="printBtn2" class="txtToolBarButton" value="预览" onClick="printpreview()">--%>
                        <input type=button id="printBtn3" class="txtToolBarButton" value="关闭" onClick="window.close();">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
<script type="text/javascript">
    function printpreview() {
        WebBrowser.execwb(7, 1);   // 打印页面预览
    }
    function printOrder() {
        window.print()
    }
</script>
</html>
