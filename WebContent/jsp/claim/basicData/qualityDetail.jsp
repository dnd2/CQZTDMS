<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔配件质保期维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔配件质保期维护</div>
<form id="fm" name="fm">
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
      <th>车型代码</th>
      <th>配件类型</th>
      <th>质保时间(月)</th>
      <th>质保里程(公里)</th>
       <c:forEach var="addlist" items="${MODELTYPE}">
       <tr class="table_list_row1">
          <td> 
		   <c:out value="${addlist.GROUP_CODE}"></c:out>       
          </td>
          <td>
          <c:out value="${addlist.CODE_DESC}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.GURN_MONTH}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.GURN_MILE}"></c:out>
          </td>                    
        </tr>
    </c:forEach>
</table>
<br/>
<table class="table_edit">
      <tr> 
      <td colspan="2" align="center">
		<input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
      </td>
	  </tr>
</table>
</form>
</body>
</html>
