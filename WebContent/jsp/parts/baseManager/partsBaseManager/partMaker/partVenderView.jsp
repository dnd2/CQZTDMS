<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件制造商明细设置</title>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="wbox">
  <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		基础数据管理 &gt;配件基础数据维护>配件制造商设置</div>
	<table class="table_query">
	  <tr>
        <td width="25%"   align="right" >制造商编码：</td>
        <td width="25%"  ><input class="middle_txt" type="text"  /></td>
        <td width="25%"   align="right"  >制造商名称：</td>
        <td width="25%"  ><input class="middle_txt" type="text"  /></td>
      </tr>
    <tr>
    	<td  align="center" colspan="4" ><input class="normal_btn" type="button" value="新增明显" onclick="partAdd();"/>
   	      <input class="normal_btn" type="button" value="返 回" onclick="partAdd();"/></td>    
</tr>
	</table>
    <div id="tl" style="display:">
  <table class="table_list" style="border-bottom:1px solid #DAE0EE;">	
  
  <tr>
		  <th width="10%">序号</th>
	    <th width="25%">件号</th>
		  <th width="25%">配件编码</th>
		  <th width="25%">配件名称</th>
	    <th width="15%">操作</th>
	  <tr class="table_list_row1">
			<td>1</td>
		<td>件号112</td>
			<td>配件编码1</td>
			<td>配件名称1</td>
			<td><a href="javascript:void(0)" onclick="formod()" >[修改]</a></td>
	  </tr>
		<tr class="table_list_row2"> 
			<td>2</td>
			<td>件号113</td>
			<td>配件编码2</td>
			<td>配件名称2</td>
			<td><a href="javascript:void(0)" onclick="formod()" >[修改]</a><a href="javascript:void(0)" onclick="view()" ></a></td>
		</tr>
	  </table>	
	<div class="page_info">
		<b class="page_info_title">总数：200</b>
		<a href="#">上一页</a>
		<a href="#">1</a>	
		<a href="#">2</a>
		<a href="#">3</a>
		<a href="#">4</a>
		<a href="#">5</a>
		<a href="#">6</a>
		<a href="#">7</a>
		<a href="#">8</a>
		<a href="#">9</a>
		<a href="#">10</a>
		<a href="#">..200</a>
		<a href="#">下一页</a>
		<input class="_txt" type="text" />
	</div>
  </div>
</div>
</body>
</html>
