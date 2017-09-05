<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>未装箱明细查询</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partNonePkgDtlQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
//        {header: "操作", dataIndex: 'PKG_NO', align: 'center', renderer: optLink},
//        {header: "箱号", dataIndex: 'PKG_NO', align: 'center'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
        {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
        {header: "销售数量", dataIndex: 'BOOKED_QTY', align: 'center'},
        {header: "已装箱数量", dataIndex: 'PKG_QTY', align: 'center'},
        {header: "未装箱数量", dataIndex: 'DIFF_QTY', align: 'center'},
        {header: "未装箱原因", dataIndex: 'RESSON', align: 'center'}
    ];

    function optLink(value, meta, record) {
        return "<a href=\"#\"  onclick='canclePkg(\"" + value + "\")'>[取消]</>";
    }
    function canclePkg(value) {
//        var pickOrderId = $("pickOrderId1").value;
        <%--window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrder.do?pkgNo=" + pkgNo + "&pickOrderId=" + pickOrderId;--%>
    }
    $(document).ready(function(){
    	__extQuery__(1);
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;装箱单管理
            &gt;未装箱明细查询
        </div>
        <input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderId}"/>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td align="right"><span align="right">箱号</span>：</td>
	                <td><input type="text" id="PKG_NO" name="PKG_NO" class="middle_txt">
	                <td align="right"><span align="right">配件编码</span>：</td>
	                <td><input type="text" id="PART_OLDCODE" name="PART_OLDCODE" class="middle_txt">
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="__extQuery__(1);"/>&nbsp;
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
</html>
</form>
</body>
</html>