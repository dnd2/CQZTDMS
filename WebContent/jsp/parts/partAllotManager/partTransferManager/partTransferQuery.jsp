<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件调拨单查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();removeState();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件调拨管理 >
        配件调拨单
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right"   align="right">调拨单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
                <td width="10%" align="right"   align="right">调入单位：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
                <td width="10%"   align="right" align="right">调出单位：</td>
                <td width="20%"><input class="middle_txt" type="text" name="SELLER_NAME" id="SELLER_NAME"/></td>
            </tr>
            <tr>
                <td width="10%"   align="right" align="right">制单日期：</td>
                <td width="22%">
                    <input name="startDate" id="t1" value="" type="text" class="short_time_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="" type="text" class="short_time_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
                <td width="10%"   align="right" align="right">提交时间：</td>
                <td width="22%">
                    <input name="startDate1" id="t3" value="" type="text" class="short_time_txt" datatype="1,is_date,10"
                           group="t3,t4">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't3', false);"/>
                    至
                    <input name="endDate1" id="t4" value="" type="text" class="short_time_txt" datatype="1,is_date,10"
                           group="t3,t4">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't4', false);"/>
                </td>
                <td width="10%"   align="right" align="right">订单状态：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBoxExp("STATE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="__extQuery__(1);"/>
                    <c:if test="${flag==false}">
                        <input class="normal_btn" type="button" value="新 增" onclick="addInit();"/>
                    </c:if>
                    <input class="normal_btn" type="button" value="导 出" onclick="expPartTransferExcel();"/></td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferInfo.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "调拨号", dataIndex: 'ORDER_CODE', align: 'center'},
            {header: "调入单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'center'},
            {header: "调出单位编码", dataIndex: 'SELLER_CODE', align: 'center'},
            {header: "调出单位", dataIndex: 'SELLER_NAME', align: 'center'},
            {header: "制单人", dataIndex: 'NAME', align: 'center'},
            {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
            {header: "调拨总金额", dataIndex: 'ORDER_AMOUNT', style: 'text-align:right', renderer: formatAmount},
            {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center', renderer: formatDate},
            {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'}
        ];

        //设置超链接  begin


        //设置超链接
        function myLink(value, meta, record) {
            var state = record.data.STATE;
            var flag = ${flag};
            if (flag == true) {
                return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>");
            }
            if ((state ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>) || (state ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05%>)) {
                var output = "<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>"
                        + "<a href=\"#\" onclick='updateTransfer(\"" + value + "\")'>[修改]</a>"
                        + "<a href=\"#\" onclick='submitTransfer(\"" + value + "\")'>[提交]</a>"
                        + "<a href=\"#\" onclick='cancelTransfer(\"" + value + "\")'>[作废]</a>";
                return output;
            }
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>");
        }

        function formatAmount(value, meta, record) {
            return formatNum(parseFloat(value.toString()).toFixed(2));
        }

        function formatNum(str) {
            var len = str.length;
            var step = 3;
            var splitor = ",";
            var decPart = ".";
            if (str.indexOf(".") > -1) {
                var strArr = str.split(".");
                str = strArr[0];
                decPart += strArr[1];
            }
            if (len > step) {
                var l1 = len % step, l2 = parseInt(len / step), arr = [], first = str.substr(0, l1);
                if (first != '') {
                    arr.push(first);
                }

                for (var i = 0; i < l2; i++) {
                    arr.push(str.substr(l1 + i * step, step));
                }

                str = arr.join(splitor);
                str = str.substr(0, str.length - 1);
            }

            if (decPart != ".") {
                str += decPart;
            }

            return str;
        }


        //导出
        function expPartTransferExcel() {
            fm.action = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/expPartTransferExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        //查看
        function view(value) {
            window.location.href = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferDetailInit.do?orderId=' + value;
        }
        //提交
        function submitTransfer(value) {
            if (confirm("确定要提交?")) {
                btnDisable();
                var url1 = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/submitTransfer.json?orderId=' + value + '&curPage=' + myPage.page;
                sendAjax(url1, handleControl, 'fm');
            }
        }
        //修改
        function updateTransfer(value) {
            window.location.href = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/updatePartTransferInit.do?orderId=' + value;
        }
        //作废
        function cancelTransfer(value) {
            if (confirm("确定要作废?")) {
                btnDisable();
                var url = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/cancelTransfer.json?orderId=' + value + '&curPage=' + myPage.page;
                sendAjax(url, handleControl, 'fm');
            }
        }
        function handleControl(jsonObj) {
            btnEable();
            if (jsonObj != null) {
                var success = jsonObj.success;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (success) {
                    MyAlert(success);
                    __extQuery__(jsonObj.curPage);
                } else if (error) {
                    MyAlert(error);
                    __extQuery__(jsonObj.curPage);
                } else if (exceptions) {
                    MyAlert(exceptions.message);
                }
            }
        }
        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        function addInit() {
            window.location.href = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/addPartTransferInit.do';
        }

        function removeState() {
            var stateObj = $("STATE");
            for (var i = 0; i < stateObj.options.length; i++) {
                if (stateObj.options[i].value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03%>) {
                    stateObj.options.remove(i);
                }
            }
        }
        //设置超链接 end
    </script>
</div>
</body>
</html>