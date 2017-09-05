<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" 	src="<%=request.getContextPath()%>/js/crm/testDriveRoute/testDriveRoute.js"></script>

<title>试驾路线添加</title>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 字典管理  &gt; 试驾路线添加
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" id="curPaths" value="<%=contextPath%>" />
			<table class="table_query" border="0">
				<tr>
					<td align="right">
						路线名称：
					</td>
					<td align="left">
						<input type="text" class="middle_txt" name="routeName" id="routeName" value="" datatype="0,is_textarea,200" />
					</td>
					<td align="right">
						路线里程数：
					</td>
					<td align="left">
						<input type="text" class="middle_txt" name="mileage" id="mileage" value="" datatype="0,is_textarea,200" />
					</td>
				</tr>
				<tr>
					<td align="right">
						开始路线名：
					</td>
					<td align="left">
						<input type="text" class="middle_txt" name="startLine" id="startLine" value="" datatype="0,is_textarea,200" />
					</td>
					<td align="right">
						结束路线名：
					</td>
					<td align="left">
						<input type="text" class="middle_txt" name="endLine" id="endLine" value="" datatype="0,is_textarea,200" />
					</td>
				</tr>
				<tr>
					<td align="right">
						是否显示：
					</td>
					<td align="left">
						<script type="text/javascript">
							genSelBoxExp("status", <%=Constant.IF_TYPE%>, "", false, "short_sel", '', "false", '');
						</script>
					 </td>
					<td align="right">
						备注：
					</td>
					<td align="left">
						<input type="text" size="40" class="middle_txt" name="remarks" id="remarks" value="${po.remarks}" />
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
