<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>操作流水</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;
当前位置：客户关系管理&gt;投诉咨询管理&gt;操作流水&nbsp;&nbsp;</div>
  
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
   			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
			<tr>
			<th colSpan=5 align=left><img class=nav src="/BQYXDMS/img/subNav.gif"> <span id="up_name_00">操作流水</span></th>
			</tr>
			<tr>
			<td>序号</td>
			<td>状态</td>
			<td>操作人</td>
			<td>操作时间</td>
			</tr>
			<c:forEach items="${info}" var="flow" varStatus="index"><tr>
			<td>${index.index+1 }</td>
			<td>${flow.AUDIT_STATUS }</td>
			<td>${flow.CREATE_BY }</td>
			<td>${flow.CREATE_DATE }</td>
			</tr>
			</c:forEach>
			</table>
  </form> 
<!--页面列表 begin -->
<script type="text/javascript" >
</script>
<!--页面列表 end -->

</body>
</html>