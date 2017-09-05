<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>发运信息查询</title>
</head>
<body onload="__extQuery__(1);">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div id="div1" class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置;发运信息
        </div>
    </div>
    <input type="hidden" id="trplanCode" name="trplanCode" value="${trplanCode}" />
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/queryLogisticsInfo.json";
    var title = null;
    var len = 0;
    columns = [
        {header: "序号", align: "center", renderer: getIndex},
        {header: "发运单", dataIndex: "LOGISTICS_NO", align: "center"},
        {header: "实际物流单号", dataIndex: "LOGISTICS_NO2", align: "center"},
        {header: "日期", dataIndex: "LOGISTICS_DATE", style: 'text-align:center',renderer:formatDate},
        {header: "物流信息", dataIndex: "LOGISTICS_INFO", align: "center"}
    ];

    function formatDate(value){
        return value.substring(0,10);
    }
</script>

</body>
</html>
