<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>活动配件明细设置</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            __extQuery__(1);
        }
    </script>
</head>
<body  onunload='javascript:destoryPrototype()'>
<form method="post" name="fm" id="fm">
    <input type="hidden" name="descId" id="descId" value="${descId}"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            基础信息管理 &gt; 配件基础信息维护 &gt; 活动配件明细设置 &gt; 关闭
        </div>
        <table class="table_query">
            <th colspan="10"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">服务商代码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="dealerCode" id="dealerCode"/></td>
                <td width="10%" align="right">服务商名称：</td>
                <td width="20%"><input class="middle_txt" type="text" name="dealerName" id="dealerName"/></td>
            </tr>
            <tr>
                <td align="center" colspan="10">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" value="关闭所有" onclick="closeAllActivity('${descId}')"/>
                    <input class="normal_btn" type="button" value="打开所有" onclick="openAllActivity('${descId}')"/>
                    <input class="normal_btn" type="button" value="退出页面" onclick="closeWindow()"/>
                </td>
            </tr>
        </table>
    </div>

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetCloseSearch.json";
    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'REL_ID', renderer: getIndex, style: 'text-align:center'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink},
        {header: "服务商代码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
        {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        {
            header: "状态",
            dataIndex: 'STATE',
            style: 'text-align:center',
            renderer: getItemValue,
            style: 'text-align:center'
        }
    ];

    //设置超链接
    function myLink(value, meta, record) {
        var relId = record.data.REL_ID;
        var state = record.data.STATE;
        if (state == '<%=Constant.STATUS_DISABLE%>') {
            return String.format('<a href=\"#\" onclick="closeActivity(\'' + relId + '\',\'' + 1 + '\')"> [打开]</a>');
        }
        else {
            return String.format('<a href=\"#\" onclick="closeActivity(\'' + relId + '\',\'' + 0 + '\')"> [关闭] </a>');
        }
    }

    //关闭单个
    function closeActivity(relId, command) {
        var closeUrl = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/closeActivity.json?relId=' + relId + '&curPage=' + myPage.page + '&flag=' + 1 + '&command=' + command;
        makeNomalFormCall(closeUrl, veiwParts, 'fm');
    }
    function veiwParts(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            MyAlert(success);
            parent.window. __extQuery__(jsonObj.curPage);
        }
    }
    //全部关闭
    function closeAllActivity(descId) {
        var closeUrl = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/closeActivity.json?descId=' + descId + '&curPage=' + myPage.page + '&flag=' + 2;
        makeNomalFormCall(closeUrl, veiwParts, 'fm');
    }

    //全部关闭
    function openAllActivity(descId) {
        var closeUrl = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/closeActivity.json?descId=' + descId + '&curPage=' + myPage.page + '&flag=' + 4;
        makeNomalFormCall(closeUrl, veiwParts, 'fm');
    }

    function closeWindow(){
        _hide();
        parentContainer.__extQuery__(1);
    }
</script>
</body>
</html>