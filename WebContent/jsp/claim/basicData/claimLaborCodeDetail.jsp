<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
HashMap hm = (HashMap)request.getAttribute("SELMAP"); //参数对应的值
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
<form id="fm" name="fm">
 <table class="table_edit">
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">索赔车型组：</td>
         <td><%=hm.get("WRGROUP_CODE")==null?"":hm.get("WRGROUP_CODE")%></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">工时代码：</td>
         <td><%=hm.get("LABOUR_CODE")==null?"":hm.get("LABOUR_CODE")%></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">工时名称：</td>
         <td><%=hm.get("CN_DES")==null?"":hm.get("CN_DES")%></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">工时大类名称：</td>
         <td><%=hm.get("P_CN_DES")==null?"":hm.get("P_CN_DES")%></td>
       </tr>       
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">工时系数：</td>
         <td><%=hm.get("LABOUR_QUOTIETY")==null?"":hm.get("LABOUR_QUOTIETY")%></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_6Letter">索赔工时：</td>
         <td><%=hm.get("LABOUR_HOUR")==null?"":hm.get("LABOUR_HOUR")%></td>
       </tr>              
</table>
<br/>
<br/>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
      <th>附加工时代码</th>
      <th>附加工时名称</th>
      <th>工时系数</th>
      <th>索赔工时</th>
       <c:forEach var="addlist" items="${ADDLIST}">
       <tr class="table_list_row1">
          <td> 
			<c:out value="${addlist.LABOUR_CODE}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.CN_DES}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.LABOUR_QUOTIETY}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.LABOUR_HOUR}"></c:out>
          </td>
        </tr>
    </c:forEach>
</table>
<br/>
 <table class="table_list" style="border-bottom:1px solid #DAE0EE">
      <th>故障代码</th>
      <th>故障名称</th>       
     <c:forEach var="buslist" items="${BUSCODELIST}">
       <tr class="table_list_row1">
          <td> 
			<c:out value="${buslist.CODE}"></c:out>
          </td>
          <td>
          <c:out value="${buslist.CODE_NAME}"></c:out>
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
