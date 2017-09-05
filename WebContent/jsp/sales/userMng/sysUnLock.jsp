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

<title>锁定资源查询</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/unlock.js"></script>
</head>
<body onload="unlockQuery();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 销售订单管理 &gt;订单审核&gt;锁定资源查询</div>
 <form method="post" name = "fm" >
<input type="hidden" name="contextPaths" id="curPaths" value="<%=request.getContextPath()%>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_7Letter">用户账号：</td>
		    <td><input type='text'  class="middle_txt" name="userAccount"  id="userAccount" value=""/></td>
		    <td class="table_query_2Col_label_7Letter">发运申请单号：</td>
		    <td><input type='text'  class="middle_txt" name="reqNo"  id="reqNo" value=""/></td>
		    <td class="table_query_2Col_label_7Letter">销售订单号：</td>
		    <td><input type='text'  class="middle_txt" name="orderNo"  id="orderNo" value=""/></td>
	      </tr>
		  <tr>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td ><center >
        	<input name="searchBtn" type="button" id="queryBtn" class="normal_btn" onclick="unlockQuery();" value="查询" />
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
