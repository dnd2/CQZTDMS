<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库在途车辆明细</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;移库在途车车辆明细</div>
	<table class=table_list style="border-bottom:1px solid #DAE0EE">
  	<tr align=center class="">
	    <th  nowrap="nowrap">序号</th>
	    <th  nowrap="nowrap">VIN</th>
	    <th  nowrap="nowrap">物料编码</th>
	    <th  nowrap="nowrap">物料名称</th>
	    <th  nowrap="nowrap">批次号</th>
  	</tr>
  	<c:forEach items="${vhclList}" var="po" varStatus="step">
	  	<tr class="table_list_row1">
		    <td align="center">${step.index+1}</td>
		    <td align="center" nowrap="nowrap" >${po.VIN}</td>
		    <td align="center" nowrap="nowrap"  >${po.MATERIAL_CODE}</td>
		    <td align="center" nowrap="nowrap" >${po.MATERIAL_NAME}</td>
		    <td align="center" nowrap="nowrap" >${po.BATCH_NO}</td>
	  	</tr>
  	</c:forEach>
</table>
 <table class=table_query align=center width="85%">
	<tr>
		<td align="center"><input type="button" class="normal_btn" value="关闭" class="button" onClick="_hide();" >
	<td>
  </tr> 
</table>
</body>
</html>