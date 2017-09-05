<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请创建</title>
	</head>
	<body>
		<table width="100%">
		<tr><td>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：售后服务管理&gt;车辆维护历史资料</div>
		<br />
		<!-- 维修项目细则 - 维修项目  -->
		<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />维修项目细则 - 维修项目
				</th>
			</tr>
		</table>
		<table class="table_list">
			<tbody>
			<tr class="table_list_th">
				<th>项目名称</th>
				<th>标准工时</th>
				<th>派工工时</th>
				<th>收费区分</th>
			</tr>
			<c:forEach var="item" items="${repairs}" varStatus="addStatus">
				<tr class="<c:choose><c:when test="${addStatus.index%2==0}">table_list_row1</c:when><c:otherwise>table_list_row2</c:otherwise></c:choose>">
					<td>${item.wrLabourname}</td>
					<td>${item.stdLabourHour}</td>
					<td>${item.assignLabourHour}</td>
					<td>${item.chargePartitionCode}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<br />
		<!-- 维修项目细则 - 维修零件  -->
		<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />维修项目细则 - 维修零件
				</th>
			</tr>
		</table>
		<table class="table_list">
			<tbody>
			<tr class="table_list_th">
				<th>零件名称</th>
				<th>数量</th>
				<th>单位</th>
				<th>收费区分</th>
			</tr>
			<c:forEach var="part" items="${parts}" varStatus="addStatus">
				<tr class="<c:choose><c:when test="${addStatus.index%2==0}">table_list_row1</c:when><c:otherwise>table_list_row2</c:otherwise></c:choose>">
					<td>${part.partName}</td>
					<td>${part.partQuantity}</td>
					<td>${part.unitCode}</td>
					<td>${part.chargePartitionCode}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<br />
		<!-- 维修项目细则 - 附加项目  -->
		<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />维修项目细则 - 附加项目
				</th>
			</tr>
		</table>
		<table class="table_list">
			<tbody>
			<tr class="table_list_th">
				<th>项目代码</th>
				<th>项目名称</th>
				<th>收费区分</th>
			</tr>
			<c:forEach var="item" items="${items}" varStatus="addStatus">
				<tr class="<c:choose><c:when test="${addStatus.index%2==0}">table_list_row1</c:when>
							<c:otherwise>table_list_row2</c:otherwise></c:choose>">
					<td>${item.addItemCode}</td>
					<td>${item.addItemName}</td>
					<td>${item.chargePartitionCode}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<br />
		<table class="table_edit">
			<tr>
				<td align="center">
					<input type="button" class="normal_btn" value="关闭" onclick="window.close();"/>
				</td>
			</tr>
		</table>
		</td></tr>
		</table>
	</body>
</html>
