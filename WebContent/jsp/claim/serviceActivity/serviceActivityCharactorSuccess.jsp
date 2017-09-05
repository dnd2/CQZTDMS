<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-车辆性质列表</title>
<% String contextPath = request.getContextPath();%>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理</div>
  <table width=95% border=0 class="table_edit">
  	<tr>
  	    <td width="29%" align="center">系统提示：车辆性质设定成功!</td>
    </tr>
    <tr>
	    <td width="26%" colspan=2 align="center">
	  	    <input type="button" name="bt_edit_id" id="bt_edit_id2" class="normal_btn" value="关闭" onclick="_hide();"/>
	  	</td>
    </tr>
  </table>
</body>
</html>