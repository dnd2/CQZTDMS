<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@taglib uri="/jstl/cout" prefix="c" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>保养明细</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
<table width="100%">
	<tr>
		<td>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：售后服务管理&gt;保养明细</div>
		</td>
	</tr>
	<tr>
		<td>
		<table class="table_list">
			<tr class="table_list_row2">
				<td>序号</td>
				<td>VIN</td>
				<td>行驶里程</td>
				<td>保养次数</td>
				<td>用户姓名</td>
				<td>发动机号</td>
				<td>服务站名称</td>
				<td>保养时间</td>
				<td>出厂时间</td>
				<td>购车时间</td>
				<td>状态</td>
			</tr>
			<c:forEach var="map" items="${list}" varStatus="st">
			<tr class="table_list_row${st.index%2+1}">
				<td>${map.ROWNUM}</td>
				<td>${map.VIN}</td>
				<td>${map.IN_MILEAGE}</td>
				<td>${map.FREE_TIMES}</td>
				<td>${map.CTM_NAME}</td>
				<td>${map.ENGINE_NO}</td>
				<td>${map.DEALER_NAME}</td>
				<td>${map.RO_DATE}</td>
				<td>${map.PRODUCT_DATE}</td>
				<td>${map.PURCHASED_DATE}</td>
				<td>${map.STATUS}</td>
			</tr>
			</c:forEach>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center"><input type="button" name="closeBtn"
			class="normal_btn" value="关闭" onclick="window.close();" /></td>
	</tr>
</table>
</body>
</html>