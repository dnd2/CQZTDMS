<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>已开票资金详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javascript" type="text/javascript">
        function doInit() {
            loadcalendar();  //初始化时间控件
//            getDate();
            __extQuery__(1);
        }
    </script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件财务管理 &gt; 服务商账户查询 &gt; 已开票金额详情</div>
<form name='fm' id='fm'>
    <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
    <input type="hidden" name="ACCOUNT_ID" id="ACCOUNT_ID" value="${ACCOUNT_ID }"/>
    <table class="table_query">
        <tr>
            <td width="10%" align="right">服务商名称：
            </td>
            <td width="20%">
                <input class="long_txt" id="dealerNameSelect" name="dealerNameSelect" type="text" value="${dealerName}"
                       disabled="disabled" readonly="readonly"/>
                <input type="hidden" name="dealerId" id="dealerId" value="${dealerId }"/>
            </td>
            <td width="10%" align="right">开票日期：</td>
            <td width="25%">
                <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10"
                       group="checkSDate,checkEDate"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button"/>
                至
                <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10"
                       group="checkSDate,checkEDate"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button"/>
            </td>
            <!--
            <td width="10%" align="right">销售单号：
            </td>
            <td width="20%">
                <input  class="middle_txt" id="orderCode"  name="orderCode" type="text" />
            </td>
             -->
            <td width="10%" align="right">发票号：
            </td>
            <td width="20%">
                <input class="middle_txt" id="invNo" name="invNo" type="text"/>
            </td>
        </tr>
        <tr>
            <td colspan="6" align="center">
                <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                       onclick="__extQuery__(1)"/>&nbsp;
                <input class="normal_btn" type="button" value="导 出" onclick="exportPartPreeDetailExcel()"/>&nbsp;
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
        var url = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerInvAccDetail2.json?query=1";
        var title = null;
        var columns = [
            {header: "序号", dataIndex: 'RECORD_ID', renderer: getIndex},
            {header: "订单号", dataIndex: 'ORDER_CODE'},
            {header: "销售号", dataIndex: 'SO_CODE'},
            {header: "发运单", dataIndex: 'TRPLAN_CODE'},
            {header: "开票金额", dataIndex: 'INVO_AMOUNT', style: 'text-align:right; padding-right:3%;'},
            {header: "发票号", dataIndex: 'INVOICE_NO'},
            {header: "开票日期", dataIndex: 'CREATE_DATE', renderer: shortDate}
        ];

        //导出
        function exportPartPreeDetailExcel() {
            document.fm.action = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/exportDLRInvoDtl.do";
            document.fm.target = "_self";
            document.fm.submit();
        }
        function
        shortDate(value) {
            return value.substring(0, 10);
        }
    </script>
</form>
</body>
</html>