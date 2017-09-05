<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<body>
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;配置明细
  <table class="table_query">
   <tr>
     <th width="10%"></th>
     <th>配置代码</th>
     <th>配置名称</th>
     <th>经销商简称</th>
   </tr>
   <c:forEach items="${list}" var="li">
     <tr>
       <td></td>
       <td>${li.PACKAGE_CODE}</td>
       <td>${li.PACKAGE_NAME}</td>
       <td>${li.DEALER_SHORTNAME }</td>
   </tr>
   </c:forEach>
   <tr>
      <td colspan="5" align="center"> 
         <input type="button"  value="关闭" onclick="_hide();"/>
      </td>
   </tr>
  </table>
</body>
</html>