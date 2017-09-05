<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
int i = 0 ;
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车型组信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 车型组信息</div>
<form id="fm" name="fm">
<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询结果</h2>
			<div class="form-body">
  <table class="table_list" >
  		<th>序号</th>
      <th>车型代码</th>
      <th>车型名称</th>
       <c:forEach var="addlist" items="${ADDLIST}">
       <tr class="table_list_row1">
       		<td> 
			<c:out value="<%=++i %>"></c:out>
          </td>
          <td> 
			<c:out value="${addlist.GROUP_CODE}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.GROUP_NAME}"></c:out>
          </td>
        </tr>
    </c:forEach>
</table>
</div>
</div>
<br/>
<table class="table_query">
      <tr> 
      <td colspan="2" style="text-align:center">
		<input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
      </td>
	  </tr>
</table>
</form>
</div>
</body>
</html>
