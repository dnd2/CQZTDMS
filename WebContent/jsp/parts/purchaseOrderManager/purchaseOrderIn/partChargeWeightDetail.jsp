<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>配件计费重量明细</title>
    <script language="javascript">
        var myObjArr = [];
        //初始化查询TABLE
        var myPage;
        var url = g_webAppName + "/report/partReport/partStockReport/PartChargeWeightReport/queryData.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "省份", dataIndex: 'PROVINCE', style: 'text-align:left'},
            {header: "城市", dataIndex: 'CITY', style: 'text-align:left'},
            {header: "经销商编号", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
            {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "发运单号", dataIndex: 'TRPLAN_CODE', style: 'text-align:left'},
            {header: "承运商", dataIndex: 'TRANSPORT_ORG', style: 'text-align:center'},
            {header: "运输方式", dataIndex: 'TRANS_TYPE', style: 'text-align:center'},
            {header: "箱号", dataIndex: 'PKG_NO', style: 'text-align:right'},
            {header: "包装尺寸</p>长(cm)*宽(cm)*高(cm)", dataIndex: 'LENGTH', style: 'text-align:center', renderer: setSize},
            {header: "体积", dataIndex: 'VOLUME', style: 'text-align:center'},
            {header: "实际重量(kg)", dataIndex: 'WEIGHT', style: 'text-align:center'},
            {header: "折合重量(kg)", dataIndex: 'EQ_WEIGHT', style: 'text-align:center'},
            {header: "计费重量(kg)", dataIndex: 'CH_WEIGHT', style: 'text-align:center'},
            {header: "备注", dataIndex: 'REMARK', align: 'center'}
        ];
        function setSize(value, meta, record) {
            var l = record.data.LENGTH;
            var w = record.data.WIDTH;
            var h = record.data.HEIGHT;
            var text = l + " * " + w + " * " + h;
            return String.format(text);
        }

        function exportExcel() {
            document.fm.action = g_webAppName + "/report/partReport/partStockReport/PartChargeWeightReport/exportExcel.do";
            document.fm.target = "_self";
            document.fm.submit();
        }
    </script>
</head>

<body onload="loadcalendar();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 报表管理 &gt; 配件仓储报表 &gt;配件计费重量明细
        </div>
        <table border="0" class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td align="right">经销商编号：</td>
                <td><input type="text" id="DEALER_CODE" name="DEALER_CODE" class="middle_txt">
                </td>
                <td align="right">经销商名称：</td>
                <td><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
                <td align="right">发运日期：</td>
                <td>
                    <input name="fstartDate" type="text" class="short_time_txt" id="fstartDate" value="${old}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'fstartDate', false);"/>
                    至
                    <input name="fsendDate" type="text" class="short_time_txt" id="fsendDate" value="${now}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'fsendDate', false);"/>
                </td>
            </tr>
            <tr>
                <td align="right">发运单号：</td>
                <td><input class="middle_txt" type="text" id="TRPLAN_CODE" name="TRPLAN_CODE"/></td>
                <td width="10%" align="right">承运物流：</td>
                <td width="20%">
                    <select name="transportOrg" id="transportOrg" onclick="" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${listc}" var="obj">
                            <option value="${obj.fixName}">${obj.fixName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td width="10%" align="right">发运方式：</td>
                <td width="20%">
                    <select name="transType" id="transType" onclick="" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${listf}" var="obj">
                            <option value="${obj.fixName}">${obj.fixName}</option>
                        </c:forEach>
                    </select>
                </td>

            </tr>

            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导出"
                           onclick="exportExcel();"/>
                </td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</html>
</form>
</body>
</html>