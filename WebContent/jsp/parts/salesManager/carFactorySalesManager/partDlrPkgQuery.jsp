<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>发运标签打印</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/dlrPkgNoquery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
//        {id: 'action', header: "操作", sortable: false, dataIndex: 'DEALER_ID', renderer: myLink, align: 'center'},
        {header: "经销商编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        {header: "箱号", dataIndex: 'PKG_NO', style: 'text-align:center'},
        {header: "打印日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
        {header: "打印人", dataIndex: 'NAME', style: 'text-align:center'}
    ];

</script>
</head>
<body onload="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="dealerId" id="dealerId" value="${dealerId}"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理&gt;本部销售管理&gt;有效发运标签查询
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td class="right">经销商编码：</td>
	                <td><input class="middle_txt" type="text" id="dealerCode" name="dealerCode"/></td>
	                <td class="right">经销商名称：</td>
	                <td><input class="middle_txt" type="text" id="dealerName" name="dealerName"/></td>
	                <td class="right">箱号：</td>
	                <td><input class="middle_txt" type="text" id="pkgNo" name="pkgNo"/></td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
	                           value="查 询" onclick="__extQuery__(1);"/>
	                    <input name="closeBtn" id="closeBtn" class="normal_btn" type="button"
	                           value="关闭" onclick="_hide();"/>
	                </td>
	            </tr>
	        </table>
	    </div>
	    </div>    
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>

    </div>
</html>
</form>
</body>
</html>