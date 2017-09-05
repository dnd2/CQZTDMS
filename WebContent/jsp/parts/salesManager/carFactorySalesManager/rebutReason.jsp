<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    String id = request.getParameter("id");
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>驳回原因</title>
</head>
<script type="text/javascript">
function save() {
    if ($('#reason')[0].value == "") {
        MyAlert("请填写原因!");
        return;
    }
    rebut(<%=id%>, $('#reason')[0].value);
}

function rebut(id, reason) {
    var rebutUrl = encodeURI(encodeURI("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/rebut.json?orderId=" + id + "&reason=" + reason));
    makeNomalFormCall(rebutUrl, result, 'fm');
}
function result(jsonObj) {
    if (jsonObj != null) {
        parentContainer.rebutResult(jsonObj);
        window.location.href="<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDiscountDlrOrderInit.do";
    }
    _hide();
}
function cancel() {
    _hide();
}
</script>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <table border="0" class="table_query">
        <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>驳回原因</th>
        <tr>
            <td class="center">&nbsp;</td>
        </tr>
        <tr>
            <td class="center">
                <textarea rows="5" cols="30" id="reason" name="reason"></textarea>
            </td>
        </tr>
        <tr>
            <td class="center">
                <input class="normal_btn" type="button" value="确 定" onclick="save();"/>
                &nbsp;
                <input class="normal_btn" type="button" value="取 消" onclick="cancel();"/>
            </td>
        </tr>
    </table>
<%--     <jsp:include page="${contextPath}/queryPage/orderHidden.html"/> --%>
<%--     <jsp:include page="${contextPath}/queryPage/pageDiv.html"/> --%>
</form>
</body>
</html>
