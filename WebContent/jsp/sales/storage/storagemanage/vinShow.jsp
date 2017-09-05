<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>详细车籍查询</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;详细车籍查询</div>
<div class="form-panel">
	<h2>车辆属性</h2>
	<div class="form-body">
	<table class="table_query">
		<tr>
			<th class="right">车系代码：</th>
			<td align="left">${vinInfo.SERIES_CODE }</td>
			<th class="right">车系名称：</th>
			<td align="left">${vinInfo.SERIES_NAME }</td>
		</tr>
		<tr>
			<th class="right">车型：</th>
			<td align="left">${vinInfo.MODEL_CODE }</td>
			<th class="right">车型名称：</th>
			<td align="left">${vinInfo.MODEL_NAME }</td>
		</tr>
		<tr>
			<th class="right">配置代码：</th>
			<td align="left">${vinInfo.PACKAGE_CODE }</td>
			<th class="right">配置名称：</th>
			<td align="left">${vinInfo.PACKAGE_NAME }</td>
		</tr>
		<tr>
			<th class="right">物料代码：</th>
			<td align="left">${vinInfo.MATERIAL_CODE }</td>
			<th class="right">物料名称：</th>
			<td align="left">${vinInfo.MATERIAL_NAME }</td>
		</tr>
		<tr>
			<th class="right">颜色：</th>
			<td align="left" >${vinInfo.COLOR_NAME }</td>
			<th class="right">VIN：</th>
			<td align="left">${vinInfo.VIN }</td>
		</tr>
		<tr>
			<th class="right">发动机号：</th>
			<td align="left">${vinInfo.ENGINE_NO }</td>
			<th class="right">变速箱号：</th>
			<td align="left">${vinInfo.GEARBOX_NO }</td>
		</tr>
		<tr>
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
		      	<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();" />
		    </td>
		</tr>
	</table>
	</div>
</div>
</div>
</body>
</html>