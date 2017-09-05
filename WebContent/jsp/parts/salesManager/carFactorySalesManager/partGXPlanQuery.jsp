<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<head>
    <title>广宣品发运计划</title>
</head>
<script language="javascript">
    jQuery.noConflict();
    autoAlertException();
    loadcalendar();
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/queryPartGxPlan.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', align: 'center', renderer: getActionLink},
        {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style:'text-align:left'},
        {header: "制单人", dataIndex: 'NAME', align: 'center'},
        {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
        {header: "出库类型", dataIndex: 'OUT_TYPE', align: 'center', renderer: getItemValue},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    ];
    function getActionLink(value, meta, record) {
        var state = record.data.STATE;
        var orderId = record.data.ORDER_ID;
        var link_1 = "<a href=\"#\" onclick='modPlan(\"" + value + "\")'>[修改]</a>";
        var link_2 = "<a href=\"#\" onclick='submitPlan(\"" + value + "\")'>[提交]</a>";
        var link_3 = "<a href=\"#\" onclick='delPlan(\"" + value + "\",\"" + orderId + "\")'>[删除]</a>";
        var link_4 = "<a href=\"#\" onclick='viewPlan(\"" + value + "\")'>[查看]</a>";
        if ((state ==<%=Constant.PART_GX_ORDER_STATE_02 %>) || (state ==<%=Constant.PART_GX_ORDER_STATE_03 %>) || (state ==<%=Constant.PART_GX_ORDER_STATE_04 %>)) {
            return link_4;
        }
        return link_1 + link_2 + link_3 + link_4;
    }

    function modPlan(planId) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/modPlanInit.do?planId=" + planId;
    }

    function viewPlan(planId) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/viewPlan.do?planId=" + planId,1000,400);
    }

    function submitPlan(planId) {
        if (confirm("确定提交?")) {
            var rollbackUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/submitPlan.json?planId=" + planId;
            sendAjax(rollbackUrl, ajaxResult, 'fm');
        }
    }

    function delPlan(planId, orderId) {
        if (confirm("确定删除?")) {
            var rollbackUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/delPlan.json?planId=" + planId + "&orderId=" + orderId;
            sendAjax(rollbackUrl, ajaxResult, 'fm');
        }
    }

    function ajaxResult(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
        __extQuery__(1);
    }
    jQuery(function () {
        __extQuery__(1);
        jQuery(document).on('click', '#queryBtn', function () {
            __extQuery__(1);
        })
    })

    function exportPartCarExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartCarConfirm/exportPartCarExcel.do";
        fm.target = "_self";
        fm.submit();
    }

    function queryCarOrder() {
        OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/initOrderFollowQueryPage.do', 1000, 400);
    }

    //新增
    function addOrder() {
        btnDisable();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/addGxPlanOrder.do?";
    }
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 > 配件销售管理 > 广宣品发运计划
        </div>
        <table border="0" class="table_query">
            <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="24%" align="right">计划单号：</td>
                <td>
                    <input type="text" id="planCode" name="planCode" class="middle_txt">
                </td>
                <td align="right">订货单位：</td>
                <td><input type="text" id="dealerName" name="dealerName" class="middle_txt">
                </td>
            </tr>
            <tr>
                <td width="24%" align="right">出库仓库：</td>
                <td>
                    <select name="whId" id="whId" class="short_sel">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select>
                </td>
                <td align="right">出库类型：</td>
                <td>
                    <script type="text/javascript">
                        genSelBox("OUT_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td width="24%" align="right">制单时间：</td>
                <td>
                    <input name="startDate" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
                <td align="right">单据状态：</td>
                <td>
                    <script type="text/javascript">
                        genSelBox("STATE", <%=Constant.PART_GX_ORDER_STATE%>, <%=Constant.PART_GX_ORDER_STATE_01%>, true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="4" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/>
                    &nbsp;
                    <input name="expButton" id="expButton" class="long_btn" type="button" value="查询整车提车单"
                           onclick="queryCarOrder();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</html>
</form>
</body>
</html>