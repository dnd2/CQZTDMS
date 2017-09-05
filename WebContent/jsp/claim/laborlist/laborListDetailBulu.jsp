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
	应税劳务清单汇总报表参数设置</div>
<form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>

			<td width="10%" align="right" height="26">单据编码：</td>
			<td width="20%" align="left">${labor.reportCode}</td>
			<td width="10%" align="right">制单人姓名：</td>
			<td width="20%" align="left">${labor.makeMan}</td>
			 <input type="hidden" id= "reportId" name="reportId" value="${labor.reportId}"/>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">制单单位名称：</td>
			<td width="20%" align="left">${dealer.dealerName}</td>
			<td width="10%" align="right">制单单位编码：</td>
			<td width="20%" align="left">${dealer.dealerCode}</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">生产厂商：</td>
			<td width="20%" align="left">
				<input type="hidden" value="${labor.yieldly}" id="yyyy"/>
				<script>
					document.write(getItemValue($('yyyy').value));
				</script>
			</td>
			<td width="10%" align="right">发票号：</td>
			<td width="20%" align="left">
				<input type="text" id="Invoice" name="Invoice" value="${labor.invoiceCode}" />
			</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">制单日期：</td>
			<td width="20%" align="left">
				<fmt:formatDate value="${labor.reportDate}" pattern="yyyy-MM-dd"/>
			</td>
			<td width="10%" align="right">总金额：</td>
			<td width="20%" align="left">
				<input type="text" id="amount" value="${labor.amount}" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26"></td>
			<td width="20%" align="left"></td>
			<td width="10%" align="right">发票金额：</td>
			<td width="20%" align="left">
				<input type="text" id="InvoiceAmount" name="InvoiceAmount" value="${labor.authAmount}" />
			</td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">接收时间：</td>
			<td width="20%" align="left">
				${labor.receiveDate}
			</td>
			<td width="10%" align="right">接收人：</td>
			<td width="20%" align="left">
				${labor.receiveMan}
			</td>
		</tr>
	</table>
	<br />
	
	<table class="table_list">
		<tr class="table_list_row2">
			<td>行号</td>
			<td>开票通知单</td>
			<td>开票金额</td>
			<td>维修站编码</td>
			<td>维修站名称</td>
		</tr>
		<c:forEach var="dtl" items="${ps.records}" varStatus="vs">
			<tr class="table_list_row${vs.index%2+1}">
				<td>${vs.index+1}</td>
				<td>${dtl.balanceNo}</td>
				<td><fmt:formatNumber value="${dtl.invoiceAmount}" pattern="##,###.##"/></td>
				<td>${dtl.dealerCode}</td>
				<td>${dtl.dealerName}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="保存" class="normal_btn" onclick="save();"/>
				<input type="button" value="返回" class="normal_btn" onclick="goBack();"/>
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
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/firstUrlInit1.do' ;
}
function save(){
	if(Number($('amount').value)>=Number($('InvoiceAmount').value)){
		fm.action = '<%=contextPath%>/claim/laborlist/LaborListAction/saveBulu.do' ;
		fm.submit();
	}else{
	MyAlert("发票金额不能大于开票总金额");
	}
	
}
</script>
</BODY>
</html>