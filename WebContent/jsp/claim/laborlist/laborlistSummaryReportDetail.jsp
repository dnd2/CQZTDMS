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
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单汇总报表</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="sum_id" id="sum_id" value="${report.taxableServiceSumId}"/>
    <table  class="table_query">
		<tr>
			<td width="10%" align="right" height="26">汇总单号：</td>
			<td width="20%" align="left">${report.sumParameterNo}</td>
			<td width="10%" align="right">购买方名称：</td>
			<td width="20%" align="left">
				<script>
					document.write(getItemValue('${report.purchaserId}'));
				</script>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">经销商代码：</td>
			<td width="20%" align="left">${dealer.dealerCode}</td>
			<td width="10%" align="right">经销商名称：</td>
			<td width="20%" align="left">${dealer.dealerName}</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">发票号码：</td>
			<td width="20%" align="left">${report.invoiceNo}</td>
			<td width="10%" align="right">税率：</td>
			<td width="20%" align="left">${report.taxRate}</td>
		</tr>
	</table>
	<br />
	<table class="table_list">
		<tr class="table_list_row2">
			<td>序号</td>
			<td>货物(劳务)名称</td>
			<td>类型</td>
			<td>总金额</td>
		</tr>
		<c:forEach var="dtl" items="${list}" varStatus="st">
			<tr class="table_list_row${st.index%2+1}">
				<td>${st.index+1}</td>
				<td>${dtl.type}</td>
				<td>${dtl.modelCode}</td>
				<td>${dtl.balanceAmount}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="返回" class="normal_btn" onclick="goBack();"/>
			</td>
		</tr>
  </table>
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/mainUrlInit.do' ;
}
</script>
</BODY>
</html>