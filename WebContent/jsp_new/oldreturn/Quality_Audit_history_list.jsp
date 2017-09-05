<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>免费保养模板</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

</script>
<!--页面列表 end -->
</head>
<body onload="loadcalendar();">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;审核历史</div>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <tr>
      <th align="center" width="20%" style="padding-left: 5px;">审核角色</th>
      <th align="center" width="20%" style="padding-left: 5px;">审核人</th>
      <th align="center" width="20%" style="padding-left: 5px;">审核时间</th>
      <th align="center" width="20%" style="padding-left: 5px;">审核前状态</th>
      <th align="center" width="20%" style="padding-left: 5px;">审核备注</th>
    </tr>
    
    <c:forEach items="${list}" var="po">
       <tr>
          <td>${po.ROLE_NAME }</td>
          <td>${po.NAME }</td>
          <td>${po.CREATE_DATE }</td>
          <td>${po.CODE_DESC }</td>
          <td>${po.REMARK }</td>
      </tr>
    
    </c:forEach>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="关闭" class="normal_btn" onClick="_hide();"/>
    		&nbsp;&nbsp;&nbsp;
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
</form>
</body>
</html>