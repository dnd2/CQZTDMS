<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>备件显示</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javascript" type="text/javascript">
        function doInit() {
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
    基础信息管理 &gt;计划相关信息维护 &gt; 供应商管理 &gt;合同管理&gt;备件显示
</div>
<form name='fm' id='fm'>
    <input type="hidden" name="VENDER_ID" id="VENDER_ID" value="${VENDER_ID}" />
    <table class="table_query">
        <tr>
            <td colspan="4" align="center">
                <input class="normal_btn" type="button" name="button1" value="关 闭" onclick="_hide();"/>
            </td>
        </tr>
    </table>
    <br/>
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractDeatilList.json";
        var title = null;
        var columns = [
            {header: "选择", sortable: false, dataIndex: 'PART_ID', align: 'center'},
            {header: "备件编码", sortable: false, dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "备件名称", sortable: false, dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "备件件号", sortable: false, dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "采购员", sortable: false, dataIndex: 'P_NAME', style: 'text-align:left'},
            {header: "计划员", sortable: false, dataIndex: 'B_NAME', style: 'text-align:left'}
        ];
    </script>
</form>
</body>
</html>