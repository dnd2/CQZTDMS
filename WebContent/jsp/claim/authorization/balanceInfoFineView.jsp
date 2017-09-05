<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>开票管理</title>
	</head>
<body >
<center>
<form id="fm" name="fm">
			<div style="width: 500px;">
				<table width="100%">
					<tr>
						<td align="center"><span
							style="font-size: 24px; font-weight: bold;">昌河汽车质量保证费用结算明细表</span>
						</td>
					</tr>
					<tr>
						<td class="boldTd">结算编号：&nbsp; &nbsp; ${REMARK}</td>
					</tr>
					<tr>
						<td class="boldTd">上报批号:&nbsp; &nbsp; ${sbNO}</td>
					</tr>
				</table>
				<table width="100%" class="sepTab">
					<tr>
						<td>费用类型</td>
						<td>劳务费</td>
						<td>材料费</td>
						<td>&nbsp</td>
					</tr>
					<tr>
						<td>正负激励</td>
						<td>${map.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${map.PLUS_MINUS_DATUM_SUM}</td>
						<td>&nbsp</td>
					</tr>
					<tr>
						<td>总计</td>
						<td>${map.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${map.PLUS_MINUS_DATUM_SUM}</td>
						<td>&nbsp</td>
					</tr>
					<tr>
					<td colspan="4" align="center" style="font-size:18">服务商各站点结算费用明细</td>
					</tr>
					<tr>
						<td colspan="2">经销商代码</td>
						<td colspan="1">劳务费</td>
						<td colspan="1">材料费</td>
					</tr>
					<tr>
							<td colspan="2">${map.DEALER_CODE}</td>
							<td colspan="1">${map.PLUS_MINUS_LABOUR_SUM}</td>
							<td colspan="1">${map.PLUS_MINUS_DATUM_SUM}</td>
						</tr>
				</table>
			</div>
			</center>
			<table class="table_edit">
			<tr>
				<td colspan="6" align="center">
				<div id="buttontype">
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
				</div>
				</td>
			</tr>
		</table>
			
</form>
</center>
<script type="text/javascript">
</script>
</body>
</html>