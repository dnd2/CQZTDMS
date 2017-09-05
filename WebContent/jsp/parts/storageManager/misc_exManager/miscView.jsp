<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
    String orderId=(String)request.getAttribute("orderId");
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script language="javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/miscManager/Misc_exManager/getDetailList.json?orderId="+<%=orderId%>;
var title = null;
var columns = [
			{header: "序号", align:'center', renderer:getIndex},
               {header: "配件编码", dataIndex: 'PART_OLDCODE',   style: 'text-class="cneter"'},
               {header: "配件名称", dataIndex: 'PART_CNAME',   style: 'text-class="cneter"'},
               {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-class="cneter"'},
               {header: "单位", dataIndex: 'UNIT', align:'center'},
               {header: "货位", dataIndex: 'LOC_CODE', align:'center'},
               {header: "数量", dataIndex: 'IN_QTY', align:'center'}
	      ];
//获取选择框的值
function getCode(value){
	var str = getItemValue(value);	
	document.write(str);
}
//获取序号
function getIdx(id){
document.write(document.getElementById(id).rows.length-2);
}

//返回
function goBack(){
	window.location.href = '<%=contextPath%>/parts/storageManager/Misc_exManager/MiscManager/init.do';
}   
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:配件管理&gt;配件仓库管理&gt; 杂项出库(查看)</div>
<form method="post" name ="fm" id="fm">
			<div class="form-panel">
				<h2><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 杂项出库单信息
				</h2>
				<div class="form-body">
					<table class="table_query">
<table class="table_query" >
    <tr>
        <td class="right">单号:</td>
        <td>${mainMap.MISC_ORDER_CODE}</td>
        <td class="right">类型:</td>
        <td>${mainMap.B_TYPE}</td>
        <td class="right">制单人:</td>
        <td>${mainMap.NAME}</td>
    </tr>
    <tr>
        <td class="right">制单日期:</td>
        <td>${mainMap.CREATE_DATE}</td>
    	<td class="right">仓库:</td>
        <td>${mainMap.WH_NAME}</td>   

    </tr>
    <tr>
        <td class="right">备注:</td>
        <td colspan="5">${mainMap.REMARK}</td>
    </tr>
    <tr>
    	<td class="center" colspan="6"><input class="u-button" type="button" value="关 闭" onclick="_hide()"/></td>
    </tr>
</table>
</table>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
</form>
</div>


	
</body>
</html>

