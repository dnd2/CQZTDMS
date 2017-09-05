<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>库存运行情况</title>
   
    <script language="javascript">
        var myObjArr = [];
        //初始化查询TABLE
        var myPage;
        var url = g_webAppName + "/report/partReport/partStockReport/InventoryOperationReport/queryData.json";
        var title = null;
        var columns = [
            //{header: "序号", align: 'center', renderer: getIndex},
            {header: "期初库存</p>品种数", dataIndex: 'QC_PARTTYPES', style: 'text-align:center'},
            {header: "发运品种数", dataIndex: 'FY_PARTTYPES', style: 'text-align:center'},
            {header: "退货品种数", dataIndex: 'TH_PARTTYPES', style: 'text-align:center'},
            {header: "期末库存</p>品种数", dataIndex: 'QM_PARTTYPES', style: 'text-align:center'},
            {header: "期初库存</p>数量", dataIndex: 'QC_PART_QTYS', style: 'text-align:center'},
            {header: "发运数量", dataIndex: 'FY_PART_QTYS', style: 'text-align:center'},
            {header: "退货数量", dataIndex: 'TH_PART_QTYS', style: 'text-align:center'},
            {header: "期末数量", dataIndex: 'QM_PART_QTYS', style: 'text-align:right'},
            {header: "期初库存</p>金额(元)", dataIndex: 'QC_PART_AMOUNTS', style: 'text-align:right'},
            {header: "发出金额(元)", dataIndex: 'FY_PART_AMOUNTS', style: 'text-align:right'},
            {header: "退货金额(元)", dataIndex: 'TH_PART_AMOUNTS', style: 'text-align:right'},
            {header: "期末金额(元)", dataIndex: 'QM_PART_AMOUNTS', style: 'text-align:right'}
        ];

        function exportExcel() {
            document.fm.action = g_webAppName + "/report/partReport/partStockReport/InventoryOperationReport/exportExcel.do";
            document.fm.target = "_self";
            document.fm.submit();
        }


    </script>
</head>

<body onload="__extQuery__(1);loadcalendar();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="TRANSPORT_ORG2" id="TRANSPORT_ORG2"/>
    <input type="hidden" name="TRANS_TYPE2" id="TRANS_TYPE2"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 报表管理 &gt; 配件仓储报表 &gt;库存运行情况
        </div>
        <table border="0" class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">库房：</td>
                <td width="20%">
                    <select id="WH_ID" name="WH_ID" class="short_sel">
                        <c:forEach items="${wareHouses}" var="wareHouse">
                            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                        </c:forEach>
                    </select>
                </td>
                
                <td align="right">日期：</td>
                <td>
                    <input name="startDate" type="text" class="short_time_txt" id="startDate" value="${old}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'startDate', false);"/>
                    至
                    <input name="endDate" type="text" class="short_time_txt" id="endDate" value="${now}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'endDate', false);"/>
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