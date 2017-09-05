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
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:配件管理&gt;配件仓库管理&gt; 杂项入库(查看)</div>
<form method="post" name ="fm" id="fm">
<table class="table_query" border="0px" style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;" cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="8"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 杂项入库单信息</th>
    </tr>
    <tr>
        <td width="10%"   align="right" >入库单号:</td>
        <td width="20%">${mainMap.MISC_ORDER_CODE}</td>
        <td width="10%"   align="right" >制单人:</td>
        <td width="20%">${mainMap.NAME}</td>           
        <td width="10%"   align="right" >制单日期:</td>
        <td width="20%">${mainMap.CREATE_DATE}</td>
    </tr>
    <tr>
    	<td width="10%"   align="right" >仓库:</td>
        <td width="20%">${mainMap.WH_NAME}</td>   
        <td width="10%"  align="right">备注:</td>
        <td colspan=7 width="50%">${mainMap.REMARK}</td>
    </tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<table border="0" class="table_query">
    <tr align="center">
        <td><input class="normal_btn" type="button" value="关 闭" onclick="_hide()"/></td>
    </tr>
</table>
</form>
</div>

<script language="javascript">
	var myPage;
	var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/getDetailList.json?orderId="+<%=orderId%>;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
                {header: "配件编码", dataIndex: 'PART_OLDCODE',   style: 'text-align:left'},
                {header: "配件名称", dataIndex: 'PART_CNAME',   style: 'text-align:left'},
                {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
                {header: "单位", dataIndex: 'UNIT', align:'center'},
                {header: "入库数量", dataIndex: 'IN_QTY', align:'center'}
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
		window.location.href = '<%=contextPath%>/parts/storageManager/miscManager/MiscManager1/MiscMainInit.do';
	}   
</script>
	
</body>
</html>

