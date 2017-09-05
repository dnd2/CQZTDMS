<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单明细信息</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;订单明细信息</div>
<form method="post" name="fm" id="fm">
	<table class="table_query">
		<tr>
			<td align="left" width="20%">销售订单号：</td>
			<td align="left" width="80%">${map.ORDER_NO}<input type="hidden" name="dealerLevel" id="dealerLevel" value="${map.DEALER_LEVEL}"/></td>
		</tr>
	</table>
	<table class="table_list">
		<tr>
			<th>车系</th>
			<th>物料编号</th>
			<th>物料名称</th>
			<th>提报数量</th>
			<th>审核数量</th>
			<th>发运数量</th><!--
			<th>单价</th>
			<th>合计</th>
		--></tr>
		<c:forEach items="${list}" var="list">
			<tr class="table_list_row2">
				<td>${list.GROUP_NAME}</td>
				<td>${list.MATERIAL_CODE}</td>
				<td>${list.MATERIAL_NAME}</td>
				<td>${list.ORDER_AMOUNT}</td>
				<td>${list.CHECK_AMOUNT}</td>
				<td>${list.MATCH_AMOUNT}</td><!--
				<td>${list.SINGLE_PRICE}</td>
				<td>${list.TOTAIL_PRICE}</td>
			--></tr>
		</c:forEach>
		<tr class="table_list_row2">
			<td align="right" colspan="3"><strong>总计：</strong></td>
			<td><strong><span id="amounts"></span></strong></td>
			<td><strong><span id="camounts"></span></strong></td>
			<td><strong><span id="damounts"></span></strong></td><!--
			<td></td>
			<td><strong><span id="sumPrices"></span></strong></td>
		--></tr>
	</table>
	<br />
	<table class="table_list">
		<tr>
			<th>发运申请单号</th>
			<th>申请日期</th>
			<th>状态</th>
			<th>采购单位代码</th>
			<th>采购单位简称</th>
			<th>备注</th>
		</tr>
		<c:forEach items="${remarkList}" var="remarkList">
			<tr class="table_list_row2">
				<td>${remarkList.DLVRY_REQ_NO}</td>
				<td>${remarkList.REQ_DATE}</td>
				<td><script>document.write(getItemValue(${remarkList.REQ_STATUS}))</script></td>
				<td>${remarkList.DCODE}</td>
				<td>${remarkList.DSNAME}</td>
				<td>${remarkList.REQ_REMARK}</td>
			</tr>
		</c:forEach>
	</table>
	<table class="table_query">
		<c:if test="${map.ORDER_TYPE==10201003}">
			<tr>
				<td align="left" width="20%">改装说明：</td>
				<td align="left" width="80%"><c:out value="${map.REFIT_REMARK}"/></td>
			</tr>
		</c:if>
		<tr>
			<td align="left" width="20%">付款信息备注：</td>
			<td align="left" width="80%"><c:out value="${map.PAY_REMARK}"/></td>
		</tr>
		<tr>
			<td></td>
			<td align="left"><input type="button" class="cssbutton" name="button" value="关闭" onclick="_hide();"/></td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function doInit(){
	showAmount();
	//showAccount();
}
function showAmount(){
	var amounts=0;
	var camounts=0;
	var damounts=0;
	//var sumPrices=0;
	<c:forEach items="${list}" var="list">
	    var amount=<c:out value="${list.ORDER_AMOUNT}"/>;
	    var camount=<c:out value="${list.CHECK_AMOUNT}"/>;
	    var damount=<c:out value="${list.MATCH_AMOUNT}"/>;
	    //var totailPrice=<c:out value="${list.TOTAIL_PRICE}"/>;
	    amounts = Number(amounts)+Number(amount);
	    camounts = Number(camounts)+Number(camount);
	    damounts = Number(damounts)+Number(damount);
	   // sumPrices = Number(sumPrices)+Number(totailPrice);
	</c:forEach>
	document.getElementById("amounts").innerText = amounts;
	document.getElementById("camounts").innerText = camounts;
	document.getElementById("damounts").innerText = damounts;
	//document.getElementById("sumPrices").innerText = amountFormat(sumPrices);
	//document.getElementById("allPrices").innerText = amountFormat(sumPrices);
}
function showAccount(){
	var dealerLevel = document.getElementById("dealerLevel").value;
	var account =0;
	if(dealerLevel==<%=Constant.DEALER_LEVEL_01%>){
		account =<c:out value="${map.AVAILABLE_AMOUNT}"/>;
		document.getElementById("account").innerText = amountFormat(account);
	}else{
		document.getElementById('aaa').style.display="none";
		document.getElementById('bbb').style.display="none";
		document.getElementById('ccc').style.display="none";
		document.getElementById('account').style.display="none";
		document.getElementById('myprice').style.display="none";
		document.getElementById('mypricevalue').style.display="none";
		document.getElementById('myuser').style.display="none";
		document.getElementById('myuservalue').style.display="none";
		document.getElementById('otherPrice').style.display="none";
	}
}
</script>
</body>
</html>