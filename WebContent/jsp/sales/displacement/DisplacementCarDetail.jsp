<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style media=print> 
.Noprint{display:none;} 
.PageNext{page-break-after: always;} 
</style>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE></TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：客户信息管理&gt;总部汇总打印&gt;</div>
<form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>
			<td width="10%" align="right" height="26">单据编码：</td>
			<td width="20%" align="left">${mymap.DJ_CODE}</td>
			<td width="10%" align="right">制单人姓名：</td>
			<td width="20%" align="left">${mymap.ORDER_CREATE_NAME}</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">制单日期：</td>
			<td width="20%" align="left">
				<fmt:formatDate value="${mymap.ORDER_CREATE_DATE}" pattern="yyyy-MM-dd"/>
			</td>
		<td width="10%" align="right" height="26">生产厂商：</td>
			<td width="20%" align="left">
				<input type="hidden" value="${mymap.ORDER_AREA}" id="yyyy"/>
				<script>
					document.write(getItemValue($('yyyy').value));
				</script>
			</td>
		</tr>
		<tr>
	</table>
	<br />
	<table class="table_list">
		<tr class="table_list_row2">
			<td>行号</td>
			<td>新车底盘号</td>
			<td>新车生成基地</td>
			<td>新车购车日期</td>
			<td>新车型号名称</td>
		</tr>
		<c:forEach var="dtl" items="${mylist}" varStatus="vs">
			<tr class="table_list_row${vs.index%2+1}">
				<td>${vs.index+1}</td>
				<td>${dtl.NEW_VIN}</td>
				<td>${dtl.NEW_AREA}</td>
				<td>${dtl.NEW_SALES_DATE}</td>
				<td>${dtl.NEW_MODEL_NAME}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table class="table_list" id="tt"> 
	<tr>
		<td>
			<input type="button" id="btn" value="打印" onclick="doprint()"/>
		</td>
	</tr>
</table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/firstUrlInit.do' ;
}

	function doprint()
	{
		document.getElementById("tt").style.display = "none";
		window.print();
	}
</script>
</BODY>
</html>