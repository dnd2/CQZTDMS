<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-VIN列表</title>
<%
	String contextPath = request.getContextPath();
	ExcelErrors ees=(ExcelErrors)request.getAttribute("ees");
%>
</head>
 
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;三包规则维护</div>
<div class="form-panel">
		<h2>系统提示</h2>
			<div class="form-body">
  <table width=95% border=0 class="table_list">
  	<tr>
  	  <td width="29%" align="center">系统提示：三包规则维护--VIN导入失败!</td>
    </tr>
    <tr>
  	  <td width="29%" align="center">VIN导入失败原因：
  	 <%=ees.getErrorDesc()==null?request.getAttribute("sb"):ees.getErrorDesc() %>
  	  </td>
    </tr>
    <tr>
    <td width="26%" colspan=2 align="center">
  	    <input type="button" name="bt_edit_id" id="bt_edit_id2" class="normal_btn" value="关闭" onclick="javascript:history.go(-2);"/>
  	  </td>
    </tr>
  </table>
  </div>
  </div>
  </div>
</body>
</html>