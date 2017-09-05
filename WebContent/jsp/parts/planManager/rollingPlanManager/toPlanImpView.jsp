<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看</title>
<link href="<%=contextPath%>/style/content.css" type="text/css" rel="stylesheet"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
<LINK href="<%=contextPath%>/style/page-info.css" type="text/css"/>

</head>
<body  >
<form name='fm' id='fm'  method="post"  enctype="multipart/form-data">
	<div class="wbox">
	  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：总部采购管理&gt;计划导入</div>
	  <input id="errNum" type="hidden" name="errNum" value="${ecount}" />
	  <input id="errInfo" type="hidden" name="errInfo" value="${errList}" />
	  <input id="planId" type="hidden" name="planId" value="${planId}" />
	  <input id="planNo" type="hidden" name="planNo" value="${planNo}" />
	  <input type="hidden" id="planIdTemp" name="planIdTemp" value="${planId}" />
	  <table  class="table_query">
		<tr>
			<td align="left">导入结果</td>
		</tr>
	  </table>
	</div> 
	<table id="file" class="table_list" style="border-bottom: 1px;">
			<tr>
				<td>序号</td>
				<td>失败原因</td>
			</tr>
			<c:forEach items="${errList}" var="list" varStatus="_sequenceNum" step="1">
			<tr>
				<td>${_sequenceNum.index+1}</td>
				<td>${list.INFO}</td>
			</tr>
			</c:forEach>
			<tr  class="table_list_row1">
				<td colspan="2" align="center">
					<input type="button" onclick="goback();" class="normal_btn" value="返 回"/>
<!-- 					<input type="button" onclick="exportErrorInfoExcel();" class="long_btn" value="导出错误信息"/>
 -->				</td>
			</tr>
	</table>
</form>
<script type="text/javascript">
	function goback(){
		if('${PLAN_TYPES}'=='<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>'){
       		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInit.do';
       	}else{
       		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInitZ.do';
       	}
	}	
	function exportErrorInfoExcel() {
		var err = document.getElementById("errNum").value;
		if(err==0){
			MyAlert("没有错误信息");
			return;
		}
		fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/exportErrorInfoExcel.do";
	    fm.target = "_self";
	    fm.submit();
	}
	
</script>
</body> 
</html>