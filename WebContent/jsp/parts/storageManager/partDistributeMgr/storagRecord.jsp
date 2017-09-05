<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件BO查看</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
    </style>
     
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理>配件仓储管理>配件接收入库>配件接收入库>入库记录查询</div>
<form name="fm" id="fm" method="post">
    <table class="table_query">
        <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>配件信息</th>
        <tr>
            <td align="right">
                配件编码：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_CODE"
                       name="PART_CODE"
                       type="text"/>
            </td>
            <td align="right">
                配件名称：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_NAME"
                       name="PART_NAME"
                       type="text"/>
            </td>
            <td align="right">日期：</td>
            <td align="left">
                <input name="TstartDate" type="text" class="short_time_txt" id="TstartDate" value=""/>
                <input name="button2" type="button" class="time_ico"
                       onclick="showcalendar(event, 'TstartDate', false);" value=" "/>
                至
                <input name="TendDate" type="text" class="short_time_txt" id="TendDate" value="${now}"/>
                <input name="button2" type="button" class="time_ico"
                       onclick="showcalendar(event, 'TendDate', false);" value=" "/>
            </td>
           
        </tr>
       
    </table>
    
    <table class="table_edit">
        <tr align="center">
         	<td>
                <input class="normal_btn" type="button" name="BtnQuery"
                       id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                <c:choose>
                    <c:when test="${'disabled' eq buttonFalg}">
                        <input class="normal_btn" type="button" value="关 闭" onclick="_hide();"/>
                    </c:when>
                    <c:otherwise>
                        <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<script type=text/javascript>

var myPage;
//查询路径           
var url = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/queryStorageRecord.json?";
var title = null;
var flag =${flag};
var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "仓库",dataIndex: 'WH_NAME',style: 'text-align: left;'},
			{header: "配件编码",dataIndex: 'PART_CODE',style: 'text-align: left;'},
			{header: "配件名称",dataIndex: 'PART_NAME',style: 'text-align: left;'},
			//{header: "最小包装量",dataIndex: 'OEM_MIN_PKG',style: 'text-align:center;'},
			{header: "数量",dataIndex: 'PART_NUM',style: 'text-align:center;'},
			{header: "货位",dataIndex: 'LOC_CODE',style: 'text-align:center;'},
			{header: "日期",dataIndex: 'STORAGE_DATE',style: 'text-align:center;'}			
	      ];



function goBack() {
    if (flag == 1) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do";
    } else {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boQueryInit.do";
    }
}

</script>
</div>
</body>
</html>
