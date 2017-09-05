<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
    String ssId=(String)request.getAttribute("ssId");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：客户关系管理&gt;基础设定&gt;坐席排班(查看)</div>
<form method="post" name ="fm" id="fm">
<table class="table_query" border="0px" style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;" cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="8"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 坐席排班信息</th>
    </tr>
    <tr>
        <td width="15%" class="table_query_2Col_label_6Letter" >值班日期:</td>
        <td width="15%">${dataMap.DUTY_DATE}</td>
        <td width="15%" class="table_query_2Col_label_6Letter" >员工号:</td>
        <td width="15%">${dataMap.ACNT}</td>        
        <td width="15%" class="table_query_2Col_label_6Letter" >员工姓名:</td>
        <td width="15%">${dataMap.NAME}</td>           
        <td width="15%" class="table_query_2Col_label_6Letter" >状态:</td>
        <td width="15%">${dataMap.STATUS}</td>        
    </tr>
    <tr>
        <td width="15%" class="table_query_2Col_label_6Letter" >班次类型:</td>
        <td width="15%">${dataMap.WT_TYPE}</td>
        <td width="15%" class="table_query_2Col_label_6Letter" >排班人:</td>
        <td width="15%">${dataMap.SS_BY}</td>        
        <td width="15%" class="table_query_2Col_label_6Letter" >排班日期:</td>
        <td width="15%">${dataMap.SS_DATE}</td>           
        <td width="15%" class="table_query_2Col_label_6Letter" >&nbsp;</td>
        <td width="15%">&nbsp;</td>  
    </tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<table border="0" class="table_query">
    <tr align="center">
        <td><input class="normal_btn" type="button" value="返 回" onclick="goBack()"/></td>
    </tr>
</table>
</form>
</div>

<script language="javascript">

	
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortMainInit.do';
	}   
</script>
	
</body>
</html>

