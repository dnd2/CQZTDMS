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
    <title>广宣品物流信息查询</title>

</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 &gt; 本部销售管理 &gt;
            广宣品物流信息查询
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr>
                <td width="10%" align="right" id="deId">订货单位代码：</td>
                <td align="left" nowrap="true" id="deId1">
                    <input class="middle_txt" id="DEALER_CODE" value="${orgCodes}" name="DEALER_CODE" type="text"/>
                    <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;"
                           onclick="showOrgDealer('DEALER_CODE','','true','',true,'','10771001','');" value="..."/>
                    <input name="clrBtn" type="button" class="normal_btn" onclick="clrTxt('DEALER_CODE');" value="清除"/>
                </td>
                <td align="right">订货单位：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
                <%-- <td   align="right">装箱号：</td>
                  <td width="24%"><input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO"/></td>--%>
                <td align="right">发运方式:</td>
                <td>
                    <script type="text/javascript">
                        genSelBox("OUT_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>

            <td align="right">是否入库:</td>
            <td>
                <select name="IS_HAVA" IS_HAVA="whId" class="short_sel">
                    <option selected value=''>-请选择-</option>
                    <option value="92561001">已发运</option>
                    <option value="92541005">已入库</option>
                </select>
            </td>
            <!-- 
            <tr>
            <td   align="right">物流发运日期：</td>
                <td width="24%">
                <input name="CstartDate" type="text" class="short_time_txt" id="CstartDate" value=""
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CstartDate', false);"/>
                至
                <input name="CendDate" type="text" class="short_time_txt" id="CendDate" value=""
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CendDate', false);"/>
                </td>
            </tr> 
             -->
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
    autoAlertException();
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxWLQuery/query.json";
    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "订货单位代码", dataIndex: "DEALER_CODE", align: 'center'},
        {header: "订货单位", dataIndex: "DEALER_NAME", align: 'center', style: 'text-align:left'},
        {header: "发运计划单号", dataIndex: "PLAN_CODE", align: 'center', renderer: viewDtl},
        {header: "订单号", dataIndex: "ORDER_CODE", align: 'center'},
//        {header: "装箱号", dataIndex:"PKG_NO", align:'center'},
        {header: "随车物流名称", dataIndex: "LOGI_NAME", align: 'center'},
        {header: "随车发运单号", dataIndex: "ASS_NO", align: 'center'},
        {header: "随车发运日期", dataIndex: "ASS_DATE", align: 'center'},
        {header: "物流名称", dataIndex: "TRANS_ORG", align: 'center'},
        {header: "物流单号", dataIndex: "WL_NO", align: 'center'},
        {header: "物流发运时间", dataIndex: "WL_DATE", align: 'center'},
        {header: "是否已收货", dataIndex: "STATE", align: 'center', renderer: getItemValue}

    ];

    function doInit() {
        loadcalendar();
        __extQuery__(1);
    }
    //导出
    function expExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxWLQuery/expExcel.do";
        fm.target = "_self";
        fm.submit();
    }

    function clrTxt(value) {
        document.getElementById(value).value = "";
    }

    function viewDtl(value, meta, record) {
        var planId = record.data.PLAN_ID;
        return "<a href=\"#\" onclick='viewPlan(\"" + planId + "\")'>"+value+"</a>";
    }

    function viewPlan(planId) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/viewPlan.do?planId=" + planId, 1000, 400);
    }
</script>

</html>