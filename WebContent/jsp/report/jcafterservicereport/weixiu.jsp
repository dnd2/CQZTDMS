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
<title>维修明细</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
<table width="100%">
	<tr>
		<td>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：售后服务管理&gt;维修明细</div>
		</td>
	</tr>
	<tr>
		<td>
		<table class="table_list">
			<tr class="table_list_row2">
				<td>序号</td>
				<td>车型</td>
				<td>VIN</td>
				<td>行驶里程</td>
				<td>发动机号</td>
				<td>单据类型</td>
				<td>购车日期</td>
				<td>用户姓名</td>
				<td>服务站名称</td>
				<td>联系电话</td>
				<td>维修时间</td>
				<td>顾客问题</td>
				<td>配件代码</td>
				<td>配件名称</td>
				<td>工时代码</td>
				<td>工时名称</td>
				<td>是否三包</td>
				<td>供应商</td>
				<td>是否授权</td>
				<td>状态</td>
				<td>故障描述</td>
			</tr>
			<c:forEach var="map" items="${list}" varStatus="st">
			<tr class="table_list_row${st.index%2+1}">
				<td>${map.ROWNUM}</td>
				<td>${map.GROUP_CODE}</td>
				<td>${map.VIN}</td>
				<td>${map.IN_MILEAGE}</td>
				<td>${map.ENGINE_NO}</td>
				<td>${map.CLAIM_TYPE}</td>
				<td>${map.PURCHASED_DATE}</td>
				<td>${map.CTM_NAME}</td>
				<td>${map.DEALER_NAME}</td>
				<td>${map.PHONE}</td>
				<td>${map.RO_DATE}</td>
				<td>${map.TROUBLE_TYPE}</td>
				<td>${map.PART_CODE}</td>
				<td>${map.PART_NAME}</td>
				<td>${map.WR_LABOURCODE}</td>
				<td>${map.WR_LABOURNAME}</td>
				<td>${map.IS_GUA}</td>
				<td>${map.PRODUCER_NAME}</td>
				<td>${map.IS_AGREE}</td>
				<td>${map.STATUS}</td>
				<td>${map.TROUBLE_DESC}</td>
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