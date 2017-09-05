<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@taglib uri="/jstl/cout" prefix="c"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆维修历史明细表（索赔）</title>

</head>
<body onLoad="tran()">	

	<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：车辆维修历史明细（索赔）</div>
	<table class="table_list">
		<tr class="table_list_row1">
			<th>维修时间</th>
			<th>经销商</th> 
			<th>行驶里程</th>
			<th>购车日期</th>
			<th>客户名称</th>
			<th>作业名称</th>
			<th>零件件号</th>
			<th>零件名称</th>
		</tr>
		<c:if test="${lists!=null}"><c:forEach var="list" items="${lists}">
		<tr class="table_list_row1">
			<td><fmt:formatDate value="${list.roCreateDate}" pattern="yyyy-MM-dd"/></td>
			<td>${list.dealerCode}</td>
			<td>${list.totalMileage }</td>
			<td><fmt:formatDate value="${list.purchasedDate}" pattern="yyyy-MM-dd"/></td>
			<td>${list.ownerName }</td>
			<td>${list.wrLabourcode }</td>
			<td>${list.partNo}</td>
			<td>${list.partName}</td>
		</tr>
		</c:forEach></c:if>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
	</table>
<TABLE class="table_edit" >
  <tr>
  	<td align="center">

		<input type="button" class="normal_btn" value="返回" onclick="history.go(-1)"/>
  	
  
  	</td>
  </tr>
  
</TABLE>

<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 

</body>
</html>