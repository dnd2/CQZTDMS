<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <%
        String contextPath = request.getContextPath();
    %>
    <title>配件订单进度查询</title>
    <script language="javascript" type="text/javascript">
        function doInit(){
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
            &nbsp;当前位置： 配件销售管理 &gt; 异常验收明细
            <input type="hidden" name="IN_ID" id="IN_ID" value="${IN_ID}"/>
        </div>

    </div>

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript" >

    var myPage;

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderEXQuery.json";

    var title = null;
    var columns = [
        {header: "序号", dataIndex: 'ORDER_ID', renderer:getIndex,align:'center'},
        {header: "发运单", dataIndex: 'TRANS_CODE', style: 'text-align: center;'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center;'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center;'},
        {header: "发运数量", dataIndex: 'TRANS_QTY', align:'center'},
        {header: "缺件、破损数量", dataIndex: 'EXCEPTION_NUM', style: 'text-align: center;'},
        {header: "原因", dataIndex: 'EXCEPTION_REMARK'}
    ];

</script>
</body>
</html>