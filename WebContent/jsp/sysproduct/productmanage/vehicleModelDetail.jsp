<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆型号维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 车辆型号维护</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" id="vehicleModelId" name="vehicleModelId" value="${po.vehicleModelId }">
  	<table class="table_query" border="0">
		<tr>
			<td  nowrap="nowrap" align="right">车系：</td>
			<td>
				${seriesName }
			</td>
		    <td  nowrap="nowrap" align="right">车辆型号代码：</td>
		    <td  nowrap="nowrap">
		    	${po.vehicleModelCode }
		    </td>
		    <td  nowrap="nowrap" align="right">车辆型号名称：</td>
		    <td  nowrap="nowrap">
		    	${po.vehicleModelName }
		    </td>
	  	</tr>
	</table>
	<table class="table_query" id="package_list">
				<tr class="table_query_th">
					<th colspan="5"><img class="nav"
						src="/CHDMS/img/subNav.gif" />&nbsp;配置列表
					<!-- 	<input type="button" class="normal_btn" value="增加" onclick="showMaterialGroup('groupCode','','true','4','true')"/>  --></th>
				</tr>
				<tr class="table_list_row1" align="center">
					<td width="18%" nowrap="nowrap">序号</td>
					<td width="35%" nowrap="nowrap">配置代码</td>
					<td width="47%" nowrap="nowrap">配置名称</td>
				<!-- 	<td width="10%" nowrap="nowrap">操作</td>  -->
				</tr>
				<c:forEach items="${list }" var="po" varStatus="s">
					<tr class="table_list_row1" align="center">
						<td width="18%" nowrap="nowrap">${s.index+1 }</td>
						<td width="35%" nowrap="nowrap">${po.GROUP_CODE }</td>
						<td width="47%" nowrap="nowrap">${po.GROUP_NAME}</td>
					
					</tr>
				</c:forEach>
			</table>
	<table align="center">
	  	<tr>
		    <td colspan="4" align="center">
		      <input name="button" type="button" class="normal_btn" onclick="history.back();" value="返回"/>
		    </td>
	  	</tr>
	</table>
</form>
</body>
</html>
