<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script src="<%=request.getContextPath()%>/js/notice/dealerNotice/dealerNotice.js" ></script>
<title>消息提醒列表</title>
</head>
<body onload="initQuery();">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:消息提醒列表

<form method="post" name = "fm" id="fm">
<table class="table_query" border="0">
	<tr>
		<td class="tblopt">提醒菜单</td>
		<td>
			<select id="menuId" name="menuId">
				<option value="">--请选择--</option>
				<c:forEach items="${noticeMenuList }" var="tt" varStatus="index">
					<option value="${tt.funcId }">${tt.funcName }</option>
				</c:forEach>
			</select>
		</td>
		<td class="tblopt">状态</td>
		<td>
			<select id="dnHandlestate" name="dnHandlestate">
				<option value="">--请选择--</option>
				<c:forEach items="${noticeStateList }" var="tt" varStatus="index">
					<option value="${tt.id }">${tt.name }</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td class="tblopt">提醒类型</td>
		<td colspan="3">
			<select id="nmNoticetype" name="nmNoticetype">
				<option value="">--请选择--</option>
				<c:forEach items="${noticetypeList }" var="tt" varStatus="index">
					<option value="${tt.id }">${tt.name }</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
            <input class="normal_btn" type="button" name="button1" value="查 询"  onclick="__extQuery__(1);" />			
			<input class="normal_btn" type="button" value="关 闭" onclick="_hide();"/>
		</td>
	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
</script>
</body>
</html>