<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>配件订单查询</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            __extQuery__(1);
        }
    </script>
</head>
<body>
<form method="post" name="fm" id="fm">
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>
            &nbsp;当前位置： 配件销售管理 &gt; 配件订单查询 &gt; 查看
            <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
            <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
            <input type="hidden" value="${map.ORDER_ID}" name="orderId" id="orderId"/>
        </div>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/>订单信息</th>
            <tr>
                <td width="10%" align="right">订单号：</td>
                <td width="20%">
                    ${map.ORDER_CODE}
                </td>
                <td width="10%" align="right">订货单位：</td>
                <td width="20%">
                    ${map.DEALER_NAME}
                </td>
                <td width="10%" align="right">订货日期：</td>
                <td width="20%">
                    ${map.CREATE_DATE}
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">订货人：</td>
                <td width="20%">
                    ${map.BUYER_NAME}
                </td>
                <td width="10%" align="right">销售单位：</td>
                <td width="20%">
                    ${map.SELLER_NAME}
                </td>
                <td width="10%" align="right">接收单位：</td>
                <td width="20%">
                    ${map.RCV_ORG}
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">接收人：</td>
                <td width="20%">
                    ${map.BUYER_NAME}
                </td>
                <td width="10%" align="right">接收地址：</td>
                <td width="20%">
                    ${map.ADDR}
                </td>
                <td width="10%" align="right">接收人电话：</td>
                <td width="20%">
                    ${map.TEL}
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">发运方式：</td>
                <td width="20%">
                    ${map.TRANS_TYPE}
                </td>
                <td width="10%" align="right">到站名称：</td>
                <td width="20%">
                    ${map.STATION}
                </td>
                <td width="10%" align="right">邮政编码：</td>
                <td width="20%">
                    ${map.POST_CODE}
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">订单类型：</td>
                <td width="20%">
                    ${map.ORDER_TYPE}
                </td>
                <td width="10%" align="right">付款方式：</td>
                <td width="20%">
                    ${map.PAY_TYPE}
                </td>
                <td width="10%" align="right">订单总金额：</td>
                <td width="20%">
                    ${map.ORDER_AMOUNT}
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">当前状态：</td>
                <td width="20%">
                    ${map.STATE}
                </td>
                <td width="10%" align="right">备注：</td>
                <td width="50%" colspan="3">
                    ${map.REMARK}
                </td>
            </tr>
        </table>
    </div>
    <th colspan="4"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/>配件信息</th>

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->

    <div id="main_body">
        <table id="file" class="table_list" style="border-bottom: 1px;">
            <tr>
                <th colspan="6" align="left">
                    <img src="<%=contextPath%>/img/nav.gif"/>操作信息
            </tr>
            <tr class="table_list_row0" style="background-color: #FFFFCC;">
                <td>
                    序号
                </td>
                <td>
                    操作人
                </td>
                <td>
                    操作时间
                </td>
                <td>
                    环节
                </td>
                <td>
                    状态
                </td>
                <td>
                    备注
                </td>
            </tr>
            <c:if test="${list !=null}">
                <c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
                    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
                        <tr class="table_list_row1">
                    </c:if>
                    <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
                        <tr class="table_list_row2">
                    </c:if>
                    <td align="center" nowrap>
                            ${_sequenceNum.index+1}
                    </td>
                    <td align="center" nowrap>
                            ${list.OPT_NAME}
                    </td>
                    <td align="center" nowrap>
                            ${list.OPT_DATE}
                    </td>
                    <td align="center" nowrap>
                            ${list.WHAT}
                    </td>
                    <td align="center" nowrap>
                            ${list.STATUS}
                    </td>
                    <td align="center" nowrap>
                            ${list.REMARK}
                    </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </div>


    <div class="wbox">
        <table class="table_query">
            <tr>
                <td align="center">
                    <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
                </td>
            </tr>
        </table>
    </div>
</form>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/partOrderDetailSearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'LINE_ID', renderer: getIndex, align: 'center'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
        {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
        {header: "单位", dataIndex: 'UNIT'},
        {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
        {header: "订单单价", dataIndex: 'BUY_PRICE', style: 'text-align: right;'},
        {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
        {header: "订货金额(元)", dataIndex: 'BUY_AMOUNT', style: 'text-align: right;'},
        {header: "备注", dataIndex: 'REMARK', align: 'center'}
    ];

    function getOrgStock(value, meta, record) {
        var str = "N";
        if (1 < parseInt(value)) {
            str = "Y";
        }
        return String.format(str);
    }

    function goBack() {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/orderQueInit.do";
        fm.submit();
    }

    //失效按钮
    function btnDisable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = true;
        });

    }

    //有效按钮
    function btnEnable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = "";
        });

    }
</script>
</body>
</html>