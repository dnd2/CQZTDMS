<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件调拨管理 >
        配件调拨审核
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%"   align="right" align="right">调拨单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
                <td width="10%"   align="right" align="right">调入单位：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
                <td width="10%"   align="right" align="right">调出单位：</td>
                <td width="20%"><input class="middle_txt" type="text" name="SELLER_NAME" id="SELLER_NAME"/></td>
            </tr>
            <tr>
                <td width="10%"   align="right" align="right">制单日期：</td>
                <td width="20%">
                    <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
                <td width="10%"   align="right" align="right">提交时间：</td>
                <td width="20%">
                    <input name="startDate1" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t3,t4">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't3', false);"/>
                    至
                    <input name="endDate1" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t3,t4">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't4', false);"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="__extQuery__(1);"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var flag = ${flag};
        var url = "<%=contextPath%>/parts/partAllotManager/PartTransferChkManager/queryPartTransferChkInfo.json";

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
            {header: "调拨总金额", dataIndex: 'ORDER_AMOUNT', align: 'center'},
            {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center', renderer: formatDate},
            {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'}
        ];

        //设置超链接  begin


        //设置超链接
        function myLink(value, meta, record) {
            if (!flag) {
                return String.format("<a href=\"#\" onclick='return myclick();'>[审核]</a>");
            }
            return String.format("<a href=\"#\" onclick='chkTransfer(\"" + value + "\")'>[审核]</a>");
        }

        function myclick() {
            MyAlert('只有车厂才能审核!');
            return false;
        }
        //审核
        function chkTransfer(value) {
            window.location.href = '<%=contextPath%>/parts/partAllotManager/PartTransferChkManager/partTransferChkInit.do?orderId=' + value;
        }

        //查看
        function view(value) {
            window.location.href = '<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferDetailInit.do?orderId=' + value;
        }
        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }
        //设置超链接 end
    </script>
</div>
</body>
</html>