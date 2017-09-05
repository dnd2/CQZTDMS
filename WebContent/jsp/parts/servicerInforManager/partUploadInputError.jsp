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
    <title>导入错误信息</title>
</head>
<body>
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;导入错误信息</div>
<form name="fm" method="post" id="fm">
    <table class="table_list">
        <c:if test="${!empty errList}">
            <tr>
                <td width="10%" align="center">序号</td>
                <td width="50%" align="left">错误信息</td>
            </tr>
            <c:forEach items="${errList}" var="list" varStatus="status">
                <c:if test="${(list.index+1)%2==0}">
                    <tr calss="table_list_row2">
                </c:if>
                <c:if test="${(list.index+1)%2!=0}">
                    <tr calss="table_list_row1">
                </c:if>
                <td>${status.index + 1}</td>
                <td align="center" style="text-align: left">${list.err }</td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <table width="95%" align="center" class="table_query">
        <tr>
            <td align="center">
                <c:if test="${flag eq 2}">
                <input class="normal_btn" type="button" value="返回" name="button1" onClick="goBack2();">
                </c:if>
                <c:if test="${flag eq 1}">
                    <input class="normal_btn" type="button" value="返回" name="button1" onClick="goBack1();">
                </c:if>
            </td>
        </tr>
    </table>
</form>
<script type="text/javascript">
    //返回
    function goBack() {
        window.location.href = "<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/queryServicerInfoInit.do";
    }
    function goBack2() {
        window.location.href = "<%=contextPath%>/parts/baseManager/PartBaseQuery/partBaseQueryInit.do";
    }
</script>
</body>
</html>
