<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/orderNumberFormat.js"></script>
<title>经销商余额</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt;经销商余额查看</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>经销商代码</th>
			<th>经销商名称</th>
			<th>账号类型</th>
			<th>账户金额</th>
			<th>锁定金额</th>
			<th>可用余额</th>
		</tr>
		<c:forEach items="${w_List}" var="list1" varStatus="vstatus">
			<tr>
			<td>
					${list1.DEALER_CODE}
				</td>
				<td>
					${list1.DEALER_NAME}
				</td>
				<td>${list1.TYPE_NAME}</td>
				<td> <script>document.write(amountFormat(${list1.BALANCE_AMOUNT}));</script></td>
				<td> <script>document.write(amountFormat(${list1.ASFROZEN_AMOUNT}));</script></td>
				<td> <script>document.write(amountFormat(${list1.ENABLE_AMOUNT}));</script></td>
			</tr>
		</c:forEach>
	</table>
</form>
<form  name="form1" id="form1">
	<table class="table_query" width="85%" align="center">
		<tr class="table_list_row2">
			<td align="center">
				<input type="button" name="button2" class="cssbutton" onclick="_hide();parentMethod();" value="关闭" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function parentMethod(){
		parent.$('inIframe').contentWindow.destory();
	}
</script>
</body>
</html>