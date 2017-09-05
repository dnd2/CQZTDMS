<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>库存盘点管理</title>

<script type="text/javascript">
	//返回
	function goBack(){
		btnDisable();
		var actionURL = document.getElementById("actionURL").value;
		fm.action = actionURL;
		fm.submit();
	}
</script>
</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;库存盘点管理导入错误信息</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
<table class="table_list" >  
<% 
	if(request.getAttribute("error")!=null){
		%>
		<tr>
	  	<td  align="center">${ error}</td>
	  </tr>
		<% 
	}else{
%>
	  <tr>
	  	<td width="40%" align="center">行列</td>
	  	<td width="40%" align="center">内容</td>
	  	<td width="40%" align="center">错误信息</td>
	  </tr>
	  <%
	  List<Map<String,String>> list = (List<Map<String,String>>)request.getAttribute("errorInfo");
	  for(Map<String,String> map : list){
	  %>
	  	 <tr>
		  	<td align="center"><%=map.get("1") %></td>
		  	<td align="center"><%=map.get("2") %></td>
		  	<td align="center"><%=map.get("3") %></td>
		  </tr>
	  	
	  <%
	  }
	}
  %>
</table>
<input type="hidden" name="count" value="${count }"/>
<input type="hidden" name="actionURL" id="actionURL" value="${actionURL }"/>
<table width="95%"  align="center" class="table_query">
  <tr>
    <td class="center">
        <input class="normal_btn" type="button" value="返 回" name="button1"  onClick="goBack()">
    </td>
  </tr>
</table>
</form>
</body>
</html>
