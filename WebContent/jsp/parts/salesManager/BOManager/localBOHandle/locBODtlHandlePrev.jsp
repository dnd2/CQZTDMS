<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>现场BO审核</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt; 配件销售管理 &gt; 现场BO审核 &gt;审核</div>
<form name='fm' id='fm'>
    <input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderId}"/>
    <input type="hidden" name="soCode" id="soCode" value="${soCode}"/>
    <input type="hidden" name="flag" id="flag" value="${flag}"/>
    <table class="tab_list" style="border: 0px">
        <tr>
            <td width="25%" align="right">拣货单：</td>
            <td width="25%">${pickOrderId}</td>
            <td width="25%" align="right">流水号：</td>
            <td width="25%"> ${soCode}</td>
        </tr>
    </table>
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/showBoDetlQuery.json";
        var title = null;
        var columns = [
            {header: "序号", dataIndex: '', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left;'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left;'},
            {header: "货位", dataIndex: 'LOC_NAME', style: 'text-align:center;'},
            {header: "批次", dataIndex: 'BATCH_NO', style: 'text-align:center;'},
            {header: "审核数量", dataIndex: 'SALES_QTY', style: 'text-align:center;'},
            {header: "装箱数量", dataIndex: 'PKG_QTY', style: 'text-align:center;'},
            {header: "BO数量", dataIndex: 'LOC_BO_QTY', style: 'text-align:center;'}
        ];
        //刷新父页面
        function reflashQuery(){
        	__parent().checkAction('${pickOrderId}','${flag}');
            _hide();
        }
    </script>
</form>
<table  class="table_query" style="border: 0px">
    <tr>
        <td colspan="6" class="center">
            <c:if test="${flag eq '1'}">
                <input class="normal_btn" type="button" value="强制关闭" name="BtnQuery" id="queryBtn"
                       onclick="reflashQuery();"/>
            </c:if>
            <c:if test="${flag ne '1'}">
                <input class="normal_btn" type="button" value="确 定" name="BtnQuery" id="queryBtn"
                       onclick="reflashQuery();"/>
            </c:if>
            <input class="normal_btn" type="button" name="button1" value="关 闭" onclick="_hide();"/>
        </td>
    </tr>
</table>
</body>
</html>