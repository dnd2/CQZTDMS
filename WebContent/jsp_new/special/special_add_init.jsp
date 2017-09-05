<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function chooseType(obj){
		if(obj.checked){
			if (parent.$('inIframe')) 
			{
				parentContainer.chooseType(obj.value);
			}
		   _hide();
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用类型选择
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
      	<td width="15%" nowrap="true">请先选择类型</td>
      	<td width="15%" nowrap="true"><input name="type" type="radio" onclick="chooseType(this);" value="1" />善意索赔</td>
      	<td width="15%" nowrap="true"><input name="type" type="radio" onclick="chooseType(this);" value="0" />退换车</td>
	</tr>
</table>
</form>
</body>
<!--页面列表 begin -->
</html>