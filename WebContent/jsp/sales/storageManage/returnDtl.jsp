<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>经销商退车明细</title>
<script type="text/javascript">
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
</head>
<body> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 经销商退车明细</div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="reason" id="reason"/>
		<table class="table_list" border="0">
			<tr>
				<th nowrap>VIN</th>
				<th nowrap>车型名称</th>
				<!-- <th nowrap>配置</th> -->
				<th nowrap>物料代码</th>
				<th nowrap>物料名称</th>
				<th nowrap>审核时间</th>
				<th nowrap>审核状态</th>
				<th nowrap>区域审核描述</th>
				<th nowrap>销售审核描述</th>
				<th nowrap>储运审核描述</th>
				<th nowrap>财务审核描述</th>
			</tr>
			<c:forEach var="dtlList" items="${dtlList }">
				<tr>
					<td nowrap>${dtlList.VIN }</td>
					<td nowrap>${dtlList.MODEL_NAME }</td>
					<%-- <td nowrap>${dtlList.PACKAGE_NAME }</td> --%>
					<td nowrap>${dtlList.MATERIAL_CODE }</td>
					<td nowrap>${dtlList.MATERIAL_NAME }</td>
					<td nowrap>${dtlList.CHECK_DATE }</td>
					<td nowrap><script>document.write(getItemValue(${dtlList.STATUS }));</script></td>
					<td nowrap>${dtlList.CHECK_REMARK }</td>
					<td nowrap>${dtlList.CHECK_SALE_REMARK }</td>
					<td nowrap>${dtlList.CHECK_STORAGE_REMARK }</td>
					<td nowrap>${dtlList.CHECK_FINANCE_REMARK }</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</body>
</html>