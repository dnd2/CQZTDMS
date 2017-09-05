<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>发运单查询</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partTransDtlQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "发运单号", dataIndex: 'TRPLAN_CODE', align: 'center', renderer: link},
        {header: "发运时间(出库)", dataIndex: 'SUBMIT_DATE', align: 'center'},
        {header: "发运箱数", dataIndex: 'PKG_NUMS', align: 'center'},
        {header: "发运箱号", dataIndex: 'PKG_NO', align: 'center'},
        {header: "发运金额", dataIndex: 'SALE_AMOUNT', align: 'center'},
        {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
        {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
        {header: "验收日期", dataIndex: 'IN_DATE', align: 'center'},
        {header: "验收人", dataIndex: 'NAME', align: 'center'},
        {header: "是否有差异", dataIndex: 'IF_HS', align: 'center', renderer: isHava}
    ];
    function isHava(value, meta, record) {
        if (value != "0") {
            return "有";
        } else {
            return "无";
        }
    }
    function link(value, meta, record) {
        return String.format("<a href=\"#\" title='查看明细' onclick='viewSoDtl(\"" + value + "\")' >" + value + "</a>");
    }
    function viewSoDtl(value) {
        $("TRPLAN_CODE").value = value;
        url = "<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partOutstockDetail.do";
//        window.showModalDialog(url, "dialogWidth:400px;dialogHeight:400px;status:0;help:0;resizable:0;center:1;");
        fm.action =url;
        fm.target = "_blank";
        fm.submit();
    }
</script>
</head>
<body onload="loadcalendar();__extQuery__(1);">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;发运明细查询
        </div>
        <input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderId}"/>
        <input type="hidden" name="TRPLAN_CODE" id="TRPLAN_CODE" value=""/>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
    ${x11}
    <table border="0" class="table_query">
        <tr>
            <td colspan="6" align="center">
                <%-- <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                        onclick="__extQuery__(1);"/>&nbsp;--%>
                <input name="closeBtn" id="closeBtn" class="normal_btn" type="button" value="关 闭"
                       onclick="_hide();"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>