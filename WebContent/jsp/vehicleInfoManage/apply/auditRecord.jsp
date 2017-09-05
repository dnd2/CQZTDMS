<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>审核历史</title>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;审核历史
</div>
	<div>
		<table width=100% border="0"  cellpadding="1" cellspacing="1" class="table_query" >
			<tr>
				<th width="10%">
				</th>
				<th width="10%">
					序号
				</th>
				<th width="10%">
					审核部门
				</th>
				<th width="10%">
					审核人
				</th>
				<th width="10%">
					审核操作
				</th>
				<th width="30%">
					审核记录
				</th>
				<th width="10%">
					审核时间
				</th>
				<th width="10%">
				</th>
			</tr>
			<c:forEach var="po" items="${list}" varStatus="status">
				<tr>
					<td>
					</td>
					<td>
						${status.index+1}
					</td>
					<td>
						${po.AUDIT_DEPT }
					</td>
					
					<td>
						${po.AUDIT_PERSON }
					</td>
					<td>
						<change:change type="9553" val="${po.OPERATOR }"/>
					</td>
					
					<td>
						<textarea rows="3" cols="43" >${po.AUDIT_DESC }</textarea> 
					</td>
					<td>
						${po.AUDIT_DATE }
					</td>
					<td>
					</td>
				</tr>		
			</c:forEach>
		</table>
	</div>
</html>