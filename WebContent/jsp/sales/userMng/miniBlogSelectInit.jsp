<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/userMng/blog.js"></script>
<title>微博积分</title>
</head>
<body onload="miniBlogSelectInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 微博管理 &gt; 微博积分查询</div>
 <form method="post" name = "fm" >
<input type="hidden" name="contextPaths" id="curPaths" value="<%=request.getContextPath()%>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_7Letter">微博单号：</td>
		    <td><input type='text'  class="middle_txt" name="blogNo"  id="blogNo" value=""/></td>
		    <td>年:</td>
		     <td><select name="year"><option value="" >--请选择--</option><c:forEach var="y" items="${yearList}"><option>${y}</option></c:forEach></select></td>
		      <td>月:</td>
		     <td><select name="integ_month"><option value="" >--请选择--</option><c:forEach var="m" items="${list}"><option>${m}</option></c:forEach></select></td>
	      </tr>
		  <tr>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td ><center >
        	<input name="searchBtn" type="button" id="queryBtn" class="normal_btn" onclick="blogSelect();" value="查询" />
      		</center></td>
	      </tr>
     </table> 
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</body>
</html>
