<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%
	String contextPath = request.getContextPath();
	Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
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
	应税劳务清单汇总报表收票</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="sum_id" id="sum_id" value="<%=map.get("SUM_PARAMETER_ID")%>"/>
    <table  class="table_query">
		<tr>
			<td width="10%" align="right" height="26">汇总单号：</td>
			<td width="20%" align="left"><%=map.get("SUM_PARAMETER_NO")%></td>
			<td width="10%" align="right">购买方名称：</td>
			<td width="20%" align="left">
				<script>
					document.write(getItemValue('<%=map.get("PURCHASER_ID")%>'));
				</script>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">经销商代码：</td>
			<td width="20%" align="left"><%=map.get("DEALER_CODE")%></td>
			<td width="10%" align="right">经销商名称：</td>
			<td width="20%" align="left"><%=map.get("DEALER_NAME")%></td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">发票号码：</td>
			<td width="20%" align="left"><%=map.get("INVOICE_NO")%></td>
			<td width="10%" align="right">税率：</td>
			<td width="20%" align="left"><%=map.get("TAX_RATE")%></td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">总金额：</td>
			<td width="20%" align="left"><%=map.get("AMOUNT")%></td>
			<td width="10%" align="right">发票金额：</td>
			<td width="20%" align="left">${authAmount }</td>
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
	location = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/mainUrlInitOem11.do' ;
}
function getInvoice(value){
	var id = $('sum_id').value ;
	var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/getInvoice.json?id='+id+'&flag='+value ;
	makeNomalFormCall(url,getCallback,'fm');
}
function getCallback(json){
	if(json.flag){
		MyAlert('操作已成功！');
		$('_btn2').disabled = false ;
		$('_btn'+json.type).disabled = 'disabled' ;
	}
}
function btnSet(){
	var num = '${count}' ;
	if(num==0)
		$('_btn2').disabled = 'disabled' ;
	if(num==1){
		$('_btn1').disabled = 'disabled' ;
		$('_btn2').disabled = false ;
	}
	else if(num==2){
		$('_btn1').disabled = 'disabled' ;
		$('_btn2').disabled = 'disabled' ;
	}
}
btnSet();
</script>
</BODY>
</html>