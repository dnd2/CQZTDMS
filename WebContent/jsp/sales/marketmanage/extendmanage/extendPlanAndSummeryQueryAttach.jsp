<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>区域外拓计划及总结查询</title>
    <%
        String contextPath = request.getContextPath();
        List attachList = (List) request.getAttribute("attachList");
    %>


</head>
<body>
<div class="navigation">
    <img src="<%=request.getContextPath()%>/img/nav.gif"/>
    &nbsp;当前位置： 市场活动管理 &gt; 区域外拓计划管理 &gt; 区域外拓计划及总结查询
</div>
<form method="post" name="fm" id="fm">

<!-- 添加附件start -->
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
    &nbsp;附件
</div>
<table class="table_info" border="0" id="file">
    <tr>
        <th>
            附件列表：
            <input type="hidden" id="fjids" name="fjids" />
        </th>
    </tr>
</table>
<table id="attachTab" class="table_info">
    <%
        if (attachList != null && attachList.size() != 0) {
    %>
    <c:forEach items="${attachList}" var="attls">
        <tr class="table_list_row1" id="${attls.FJID}">
            <td>
                <a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
            </td>
        </tr>
    </c:forEach>
    <%
        }
    %>
</table>
<!-- 添加附件end -->

<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_query">
    <tr>
        <td colspan="4" align="center">
            <input type="button" class="cssbutton" name="backBtn"
                   onClick="_hide()" value="关闭"/>
        </td>
    </tr>
</table>
</form>
</body>
</html>