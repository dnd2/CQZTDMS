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
    <title>经销商月度目标导入确认</title>
</head>
<body>
<form name="fm" method="post" enctype="multipart/form-data" id="fm">
    <table class="table_list" width="80%">
    <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>经销商月度目标导入确认</caption>
        <tr>
            <th align="center" width="2%">序号</th>
            <th align="center" width="5%">经销商代码</th>
            <th align="center" width="5%">经销商名称</th>
            <th align="center" width="5%">任务金额(元)</th>
        </tr>
        <c:forEach items="${list}" var="list" varStatus="status">
            <c:if test="${status.count%2 == 0}">
                <tr class="table_list_row2" style="BACKGROUND-COLOR: #f7f7f7">
            </c:if>
            <c:if test="${status.count%2 != 0}">
                <tr class="table_list_row1" >
            </c:if>
            <td align="center">${status.count}<input type="hidden" name="dealerId" id="dealerId_${list.dealerId}"
                                                     value="${list.dealerId}"/></td>
            <td align="center">${list.dealerCode}<input type="hidden" id="dealerCode_${list.dealerId}"
                                                        name="dealerCode_${list.dealerId}" value="${list.dealerCode}"/>
            </td>
            <td align="left">${list.dealerName}<input type="hidden" id="dealerName_${list.dealerId}"
                                                      name="dealerName_${list.dealerId}" value="${list.dealerName}"/>
            </td>
            <td align="center">${list.amount}<input type="hidden" id="amount_${list.dealerId}"
                                                   name="amount_${list.dealerId}" value="${list.amount}"/></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <table width="100%">
    	<tr>
            <td colspan="4" align="center">
                <input class="normal_btn" type="button" value="确定" name="button1" onClick="add();">
                <input class="normal_btn" type="button" value="返回" name="button1" onClick="javascript:history.go(-1);">
            </td>
        </tr>
    </table>
    <input type="hidden" name="count" value="${count }"/>
    <input type="hidden" name="taskMonth" value="${taskMonth }"/>
</form>

<script type="text/javascript">
    function add() {
       fm.action = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/monthPlanAdd.do";
        fm.submit(); 
    }
   
</script>
</body>
</html>
