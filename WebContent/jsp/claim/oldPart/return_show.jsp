<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath();
%>
</head>
<BODY >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;关联件</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td  nowrap="nowrap">索赔单号：</td>            
         <td  nowrap="nowrap">关联件代码：</td>
         <td  nowrap="nowrap">关联件名称</td>
         <td  nowrap="nowrap">主因件</td>
       </tr>
       <c:forEach var="list" items="${list}" >
        <td  nowrap="nowrap">${list.CLAIM_NO}</td>            
         <td  nowrap="nowrap">${list.DOWN_PART_CODE}</td>
         <td  nowrap="nowrap">${list.DOWN_PART_NAME}</td>
         <td  nowrap="nowrap">${list.MAIN_PART_CODE}</td>
       </c:forEach>
      
  </table>
</form> 
<br>
</BODY>
</html>