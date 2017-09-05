<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>

<head>
    <style type="text/css">
        tr.repQueryTrClass_3L td:first-child {
            padding-left: 100px;
        }
    </style>
    <script type="text/javascript">
        jQuery.noConflict();
        var baseUrl = "<%=contextPath%>/report/partReport/partInStockReport/PartInStockReport/";
        var myPage;
        var url = baseUrl + "query.json";
        var title = null;
        var columns = [
            {header: "序号", align: "center", renderer: getIndex},
            {header: "入库单号", dataIndex: "MISC_ORDER_CODE", align: "center"},
            {header: "服务商编码", dataIndex: "DEALER_CODE", align: "center"},
            {header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
            {header: "入库类型", dataIndex: "IN_TYPE", align: "center"},
            {header: "配件编码", dataIndex: "PART_OLDCODE", style: 'text-align:left'},
            {header: "配件名称", dataIndex: "PART_CNAME", style: 'text-align:left'},
//            {header: "配件件号", dataIndex: "PART_CODE", style: 'text-align:left'},
//            {header: "配件类型", dataIndex: "PART_TYPE", align: "center"},
            {header: "单位", dataIndex: "UNIT", align: "center"},
            {header: "入库数量", dataIndex: "IN_QTY", align: "center"},
            {header: "单价", dataIndex: "IN_PRICE", align: "center"},
            {header: "入库金额", dataIndex: "IN_AMOUNT", align: "center"},
            {header: "入库日期", dataIndex: "CREATE_DATE", align: "center"}
        ];
        //ready事件
        jQuery(function () {
            jQuery("#queryBtn").click(function () {
                __extQuery__(1);
            });
            jQuery("#exportBtn").click(function () {
                exportExcel();
            });
            if (jQuery('#oemFlag').val() ==<%=Constant.IF_TYPE_NO%>) {
                jQuery('#orgCode').parent().parent().hide();
                jQuery('#orgCode').val(jQuery('#dealerCode').val());
                jQuery('#org').val(jQuery('#dealerName').val());
            }
            loadcalendar();
            autoAlertException();
            __extQuery__(1);
        })
        function query() {
            __extQuery__(1);
        }
        function exportExcel() {
            fm.action = baseUrl + "export.do";
            fm.submit();
        }
        function showChildOrg(RETURN_DEALER, childorgName, childorgId, WH_ID, WH_NAME) {
            if (!RETURN_DEALER) {
                RETURN_DEALER = null;
            }
            if (!childorgName) {
                childorgName = null;
            }
            if (!childorgId) {
                childorgId = null;
            }
            if (!WH_ID) {
                WH_ID = null;
            }
            if (!WH_NAME) {
                WH_NAME = null;
            }
            OpenHtmlWindow("<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/returnDealerSelect.jsp?RETURN_DEALER=" + RETURN_DEALER + "&childorgName=" + childorgName + "&childorgId=" + childorgId + "&WH_ID=" + WH_ID + "&WH_NAME=" + WH_NAME, 730, 390);
        }
    </script>
</head>
<body enctype="multipart/form-data">
<form name="fm" id="fm" method="post">
    <input name="oemFlag" id="oemFlag" type="hidden" value="${oemFlag}"/>
    <input name="dealerName" id="dealerName" type="hidden" value="${dealerName}"/>
    <input name="dealerCode" id="dealerCode" type="hidden" value="${dealerCode}"/>

    <div id="div1" class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置: 报告管理&gt;配件报表&gt;服务商报表&gt;入库明细（服务商）
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td align="right"> 配件代码：</td>
                <td><input name="partOldCode" id="partOldCode" type="text" class="middle_txt"/></td>
                <td align="right"> 配件名称：</td>
                <td><input name="partCName" id="partCName" type="text" class="middle_txt"/> </td>
                <td align="right"> 入库日期:</td>
                <td>
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                           datatype="1,is_date,10" maxlength="10" value="${preDay}" style="width:65px"
                           group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${toDay}"
                           style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
            </tr>
            <tr>
                <td align="right"> 服务商代码：</td> <td><input name="orgCode" id="orgCode" type="text" class="middle_txt"/> </td>
                <td align="right"> 服务商名称:</td>
                <td>
                    <input name="org" id="org" type="text" class="middle_txt"/>
                    <input onclick="showChildOrg('','org','','','');" type="button" value="..." id="orgBtn" class="mini_btn">
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>&nbsp;
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导 出"/>
                </td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>
