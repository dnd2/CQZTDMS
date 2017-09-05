<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>差异率</title>
</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件销售报表&gt;发运差异率
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr>
                <td colspan="6" align="center">发运日期(出库)：
                    <input class="time_txt" id="startTime" name="startTime"
                           datatype="1,is_date,10" maxlength="10" value="${start}" style="width:65px"
                           group="startTime,endTime"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'startTime', false);" type="button"/>
                    至
                    <input class="time_txt" id="endTime" name="endTime" datatype="1,is_date,10" value="${end}"
                           style="width:65px"
                           maxlength="10" group="startTime,endTime"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'endTime', false);" type="button"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="导 出" onclick="expExcel();"/>
                </td>
            </tr>

        </table>
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
    </div>
</form>
</body>

<script language="javascript">
    autoAlertException();//输出错误信息
    //初始化查询TABLE
    var myPage;

    var url = "<%=contextPath%>/report/partReport/partDifferenceRatioReport/PartDifferenceRatioReportAction/query.json";

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "类型", dataIndex: 'PN', align: 'center'},
        {header: "发出数", dataIndex: 'TRCNT', align: 'center'},
        {header: "差异数", dataIndex: 'ECNT', align: 'center'},
        {header: "差异率", dataIndex: 'WLRATE', align: 'center'}

    ];

    function doInit() {
        loadcalendar();//时间初始化
        //initCondition();
        __extQuery__(1);

    }

    //导出
    function expExcel() {
        fm.action = "<%=contextPath%>/report/partReport/partDifferenceRatioReport/PartDifferenceRatioReportAction/expExcel.do";
        fm.target = "_self";
        fm.submit();
    }

</script>
</html>