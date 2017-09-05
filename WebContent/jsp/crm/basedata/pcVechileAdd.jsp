<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>竞品添加</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/competVechile.js"></script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 客户管理 &gt; 竞品添加</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">竞品代码：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="competCode" id=competCode" value="" datatype="0,is_textarea,30" />	
			</td>
			<td align="right">竞品名称：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="competName" id="competName" value="" datatype="0,is_textarea,30" />	
			</td>
			
		</tr>
		<tr>
			<td align="right">竞品上级：</td>
			<td align="left">
				 <input id="par_name" name="par_name" type="text"  class="mix_type"  size="30"  readonly="readonly" value="${tpcv1.competName}"/> 
				<input id="parId" name="parId"  type="hidden"   class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toCompetVechileList(1);" />
				<input type="button" value="清空" class="normal_btn" onclick="clrCompetTxt();" />
			</td>
			<td align="right">&nbsp;</td>
			<td align="left">
				&nbsp;
			</td>
			
		</tr>
		<tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="addSubmit();" value="保  存" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
	</table>
</form>
</div>
</body>
</html>
