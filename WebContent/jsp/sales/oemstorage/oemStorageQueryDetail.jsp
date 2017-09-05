<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车厂库存查询</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 车厂库存管理&gt; 车厂库存查询</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>物料代码</th>
			<th>物料名称</th>
			<th>批次</th>
			<th>数量</th>
		</tr>
		<c:forEach items="${list}" var="list1" varStatus="vstatus">
			<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td>
					${list1.MATERIAL_CODE}
				</td>
				<td>${list1.MATERIAL_NAME}</td>
				<td>${list1.BATCH_NO}</td>
				<td>${list1.AMOUNT}</td>
			</tr>
		</c:forEach>
		<!--<tr align="center" class="table_list_row2">
			<td></td>
			<td></td>
			<td></td>
			<td>合计</td>
			<td>
				<input type="text" class="mini_txt" id="reserveTotal" name="reserveTotal" value="0" readonly="readonly">
			</td>
		</tr> 
	--></table>
</form>
<form  name="form1" id="form1">
	<table class="table_query" width="85%" align="center">
		<tr class="table_list_row2">
			<td align="center">
				<input type="button" name="button2" class="cssbutton" onclick="_hide();" value="关闭" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
</script>
</body>
</html>