<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>财务开票导入</title>
</head>
<body>

<script type="text/javascript">
function gopage() {
	document.getElementById("impBtn").disabled=true;
	document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleCostbudget/vehicleInvoiceImport.do";
	document.getElementById('fm').submit();
}
</script>
<form  name="fm" id="fm">
<div style="margin-left: auto; margin-right: auto; width: 100%;">

<table class="table_query" align="center" border="0">
	<thead>
	<tr>
		<td width="80">导入时间：</td>
		<td align="left" colspan="3">
			${invoDate }
		</td>
	</tr>
	<tr>
		<th width="80" style="text-align: center;">行号</th>
		<th width="150" style="text-align: center;">票据单号</th>
		<th width="120" style="text-align: center;">开票时间</th>
		<th width="120" style="text-align: center;">开票号</th>
		<th width="100" style="text-align: center;">发票版本号</th>
		<th width="100" style="text-align: center;">金税金额</th>
		<th width="100" style="text-align: center;">金税税额</th>
		<th width="100" style="text-align: center;">折扣金额</th>
		<th width="100" style="text-align: center;">折扣税额</th>
	</tr>
	</thead>
	  <c:forEach items="${list }" var="A">
	  		<tr class="table_list_row2" style="text-align: center;">
	  			<td>${A.ROW_NUM }</td>
	  			<td>${A.ORDER_NO }</td>
	  			<td>
	  				<fmt:formatDate value="${A.INVOICE_DATE}" pattern="yyyy-MM-dd"/>
	  			</td>
	  			<td>${A.INVOICE_NO}</td>
	  			<td>${A.INVOICE_NO_VER}</td>
	  			<td>${A.G_JSJE}</td>
	  			<td>${A.G_JSSE}</td>
	  			<td>${A.G_ZKJE}</td>
	  			<td>${A.G_ZKSE}</td>
	  		</tr>
	  </c:forEach>
</table>
<table class="table_query" align="center" border="0"  id="roll">
	<tr align="center" >
		<th>
			<div align="center">
				<input class="normal_btn" id="impBtn" type='button' name='saveResButton' onclick='gopage();' value='确认导入' />
				&nbsp;&nbsp;
				<input class="normal_btn" type='button' name='saveResButton' onclick='_hide()' value='取消导入' />
			</div>
		</th>
  	</tr>
</table>
</div>
</form>
</body>
</html>
