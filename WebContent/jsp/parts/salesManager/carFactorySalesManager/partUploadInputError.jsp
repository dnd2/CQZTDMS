<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <title>采购配件导入错误信息</title>

</head>
<body onload="autoAlertException();btnEable();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;采购配件导入错误信息</div>
<form name="fm" method="post" id="fm">
    <table class="table_list">
       <c:if test="${!empty errorInfo}">
	        <tr>
	            <td width="40%" align="center">行列</td>
	            <td width="40%" align="center">内容</td>
	            <td width="40%" align="center">错误信息</td>
	        </tr>
	        <c:forEach items="${errorInfo}" var="list">
	        	<tr>
		            <td align="center">${list.one }
		            </td>
		            <td align="center">${list.two }
		            </td>
		            <td align="center">${list.three}
		            </td>
        		</tr>
	        </c:forEach>
        </c:if>
        
        <c:if test="${!empty repeatErorr}">
	        <tr>
	            <td width="40%" align="center">配件编码</td>
	            <td width="40%" align="center">重复次数</td>
	        </tr>
	        <c:forEach items="${repeatErorr}" var="list">
	        	<tr>
		            <td align="center">${list.partOldcode }
		            </td>
		            <td align="center">${list.repeatQty }
		            </td>
        		</tr>
	        </c:forEach>
        </c:if>
    </table>
    <input type="hidden" name="count" value="${count }"/>

    <table width="95%" align="center" class="table_query">
        <tr>
            <td align="center">
                <input class="normal_btn" type="button" value="返回" name="button1" onClick="goBack()">
            </td>
        </tr>
    </table>
</form>
<script type="text/javascript">
    //返回
    function goBack() {
        window.parent.frames.location.reload();
    }
</script>
</body>
</html>
