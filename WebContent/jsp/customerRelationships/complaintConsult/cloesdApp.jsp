<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    String CD_ID = request.getParameter("CD_ID");
    String CP_ID = request.getParameter("CP_ID");

%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>原因</title>
</head>
<script type="text/javascript">
    function save() {
        if ($('reason').value == "") {
            MyAlert("请填写原因!");
            return;
        }
        var reason =$('reason').value;
    	var url = encodeURI(encodeURI("<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/kanzenClosedMoshikomi.json?reason=" + reason));
    	sendAjax(url, result, 'fm');
    }

    function result() {
        _hide();
        parentContainer.reLoad_();
    }

    function cancel() {
        _hide();
    }
</script>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" value="<%=CD_ID%>" name="CD_ID"/>
<input type="hidden" value="<%=CP_ID%>" name="CP_ID"/>
    <table border="0" class="table_query">
        <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>完全闭环申请原因</th>
        <tr>
            <td align="center">
                &nbsp;
            </td>
        </tr>
        <tr>
            <td align="center">
                <textarea rows="5" cols="30" id="reason" name="reason"></textarea>
            </td>
        </tr>
        <tr>
            <td align="center">
                <input class="normal_btn" type="button"
                       value="确 定" onclick="save();"/>
                &nbsp;
                <input class="normal_btn" type="button" value="取 消" onclick="cancel();"/>
            </td>
        </tr>
    </table>

    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>

</form>
</body>
</html>
