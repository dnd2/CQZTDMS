<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>BO订单发出数</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件销售报表&gt;BO订单发出数
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td colspan="6" align="center">BO日期：
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10" maxlength="10"
                           value="${old}" style="width:65px"
                           group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${now}"
                           style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
                </td>
            </tr>

        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryboOrderIssue.json";
        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center'},
//            {header: "BO类型", dataIndex: 'BO_TYPE', align: 'center'},
            {header: "产生品种数", dataIndex: 'BOCNT', align: 'center'},
            {header: "处理品种数", dataIndex: 'BOHDCNT', align: 'center'},
            {header: "处理率", dataIndex: 'BORATE', align: 'center'},
            {header: "产生数量", dataIndex: 'BO_QTY', align: 'center'},
            {header: "处理数量", dataIndex: 'CL_QTY', align: 'center'},
            {header: "处理率", dataIndex: 'BOQTYRATE', align: 'center'}
        ];

        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        //导出
        function expPurOrderInExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expBoOrderIssueExcel.do";
            fm.target = "_self";
            fm.submit();
        }

    </script>
</div>
</body>
</html>