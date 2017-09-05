<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>项目执行方维护</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/marketmanage/activity/activityMaker.js"></script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车管理 > 市场活动管理 > 项目执行方新增</div>
<form method="POST" name="fm" id="fm" >
	<input type="hidden" value="<%=request.getContextPath()%>" name="contextPaths" id="contextPaths"/>
  	<table class="table_query" border="0">
		<tr>
	    <td class="table_query_2Col_label_8Letter" nowrap="nowrap">执行方代码：</td>
	    <td class="table_query_4Col_input" nowrap="nowrap">
	    	<input name="makerCode" datatype="0,is_null,30" id="makerCode" type="text" class="middle_txt" />
	    </td>
	    <td class="table_query_2Col_label_8Letter" nowrap="nowrap">执行方名称：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<input name="makerName" datatype="0,is_null,30" id="makerName" type="text" class="middle_txt" />
	    </td>
	  </tr>
	  <tr>
	  <td class="table_query_2Col_label_8Letter" nowrap="nowrap">联系人：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<input name="linkMan" datatype="0,is_null,30" id="linkMan" type="text" class="middle_txt" />
	    </td>
	     <td class="table_query_2Col_label_8Letter" nowrap="nowrap">联系电话：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<input name="phone" datatype="0,is_null,30" id="phone" type="text" class="middle_txt" />
	    </td>
	  </tr>
	  <tr>
	    <td colspan="4" align="center">
	      <input name="button2" type="button" class="normal_btn" onclick="confirmAdd();" value="保存"/>
	      <input name="button" type="button" class="normal_btn" onclick="history.back();" value="返回"/>
	    </td>
	  </tr>
	</table>
</form>
</body>
</html>
