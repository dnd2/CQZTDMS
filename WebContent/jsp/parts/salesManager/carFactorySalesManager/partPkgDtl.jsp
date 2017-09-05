<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>装箱明细查询</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "箱号", dataIndex: 'PKG_NO', align: 'center'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
        {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
        {header: "批次", dataIndex: 'BATCH_NO', align: 'center'},
        {header: "数量", dataIndex: 'PKG_QTY', align: 'center'}
    ];

    function exportExcel(){
        var url_t = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/exportPkgDtlToExcel.do?pickOrderId="+${pickOrderId};
        document.fm.action=url_t;
        document.fm.submit();
    }
    $(document).ready(function(){
    	__extQuery__(1);
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;装箱单管理&gt;装箱明细查询</div>
        <input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderId}"/>
        <input type="hidden" name="trplanCode" id="trplanCode" value="${trplanCode}"/>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
        	<table   class="table_query">
	            <tr>
	                <td class="right"><span class="right">箱号</span>：</td>
	                <td ><input type="text" id="PKG_NO" name="PKG_NO" class="middle_txt">
	                <td class="right"><span class="right">配件编码</span>：</td>
	                <td ><input type="text" id="PART_OLDCODE" name="PART_OLDCODE" class="middle_txt">
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="__extQuery__(1);"/>&nbsp;
	                    <input name="expBtn" id="expBtn" class="normal_btn" type="button" value="导 出"
	                           onclick="exportExcel(1);"/>&nbsp;
	                    <input name="closeBtn" id="closeBtn" class="normal_btn" type="button" value="关 闭"
	                           onclick="_hide();"/>
	                </td>
	            </tr>
	        </table>
	        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>
</html>