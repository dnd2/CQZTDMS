<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>配件交货跟踪表</title>

</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 &gt; 本部销售管理 &gt;
            配件交货跟踪表
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr>
                <td width="10%" align="right">捡货日期：</td>
                <td width="22%" align="left">
                    <input class="time_txt" id="SSUBMIT_DATE" name="SSUBMIT_DATE" value="${pastDate}"
                           style="width:65px"
                           datatype="1,is_date,10" maxlength="10"
                           group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SSUBMIT_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ESUBMIT_DATE" name="ESUBMIT_DATE" datatype="1,is_date,10"  value="${nowDate}"
                           style="width:65px"
                           maxlength="10" group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ESUBMIT_DATE', false);" type="button"/></td>
             <%--   <td align="right">订单类型：</td>
                <td width="24%">
                    <script type="text/javascript">
                        genSelBox("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>--%>
                <td align="right">订单号：</td>
                <td width="24%"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                <td align="right">拣配单：</td>
                <td width="24%"><input class="middle_txt" type="text" id="PICK_ORDER_ID" name="PICK_ORDER_ID"/></td>
            </tr>
            <tr>
                <td align="right">服务商代码：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_CODE" name="DEALER_CODE"/></td>
                <td align="right">服务商：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
                <td align="right">排序方式：</td>
                <td width="24%">
                    <select name="sort" id="sort" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${stateMap}" var="stateMap">
                            <option value="${stateMap.key}">${stateMap.value}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">阶段筛选：</td>
                <td width="24%">
                    <select name="sort2" id="sort2" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${stateMap2}" var="stateMap2">
                            <option value="${stateMap2.key}">${stateMap2.value}</option>
                        </c:forEach>
                    </select>
                </td>
                <td align="right">阶段状态：</td>
                <td width="24%">
                    <select name="state" id="state" class="short_sel">
                        <option value="">--请选择--</option>
                        <option value="Y">Y</option>
                        <option value="N">N</option>
                    </select>
                </td>
                <td align="right">是否已交货：</td>
                <td width="24%">
                    <select name="is_over" id="is_over" class="short_sel">
                        <option value="">--请选择--</option>
                        <option value="Y">Y</option>
                        <option value="N">N</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="query();"/>
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
    autoAlertException();
    //初始化查询TABLE
    var myPage;

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDeliveryFollow/partDeliveryFollowQuery.json";

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "BO单号", dataIndex: 'BO_CODE', align: 'center'},
        {header: "销售单", dataIndex: 'SO_CODE', align: 'center'},
        {header: "销售金额", dataIndex: 'AMOUNT', style: 'text-align:right;'},
        {header: "拣配单", dataIndex: 'PICK_ORDER_ID', align: 'center'},
        {header: "服务商代码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "服务商", dataIndex: 'DEALER_NAME', style: 'text-align:left;'},
        {header: "捡货日期", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "行数", dataIndex: 'ROW_NUM', style: 'text-align:center;'},
        {header: "发货数量", dataIndex: 'ROW_SQTY', style: 'text-align:center;'},
        {header: "拣配打印", dataIndex: 'PICK_PRINT', align: 'center'},
        {header: "包装状态", dataIndex: 'PKG_STATUS', align: 'center'},
        {header: "包装打印", dataIndex: 'PKG_PRINT', align: 'center'},
        {header: "发运状态", dataIndex: 'TRANS_STATUS', align: 'center'},
        {header: "发运打印", dataIndex: 'TRANS_PRINT', align: 'center'},
        {header: "发运单", dataIndex: 'TRPLAN_CODE', align: 'center'},
        {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
        {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
        {header: "出库日期", dataIndex: 'OUT_DATE', align: 'center'},
        {header: "现场BO行数", dataIndex: 'BO_LINES', align: 'center'},
        {header: "现场BO数量", dataIndex: 'BO_QTY', align: 'center'},
        {header: "是否完成", dataIndex: 'IS_OVER', align: 'center'}
    ];

    function doInit() {
        loadcalendar();
        __extQuery__(1);
    }

    //导出
    function expExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDeliveryFollow/expExcel.do";
        fm.target = "_self";
        fm.submit();
    }

    function query(){
        var sort = document.getElementById("sort2").value;
        var state = document.getElementById("state").value;
        if(sort != "" || state != ""){
            if(sort == ""){
                MyAlert("阶段状态和阶段筛选为组合查询,阶段筛选必填!");
                return;
            }
            if(state == ""){
                MyAlert("阶段状态和阶段筛选为组合查询,阶段状态必填!");
                return;
            }
        }
        __extQuery__(1);

    }
</script>

</html>