<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%
	Map<String, Object> tempMap = (Map<String, Object>) request
			.getAttribute("complaintConsult");
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>抵扣通知单明细</title>
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;抵扣通知单明细
		</div>
	</div>
	<FORM name="FRM2" METHOD=POST>
		<table width="100%" class="tab_edit" border="0" >
				<tr >
					<th style="text-align:center" >序号</th>
					<th style="text-align:center" >索赔申请单号</th>
					<th style="text-align:center" >VIN</th>
					<th style="text-align:center" >项目类型</th>
					<th style="text-align:center" >项目代码</th>
					<th style="text-align:center" >项目名称</th>
					<th style="text-align:center" >抵扣费用</th>
					<th style="text-align:center" >抵扣原因</th>
					<th style="text-align:center" >备注</th>
				</tr>
				<c:forEach items="${list}" var="applyR" varStatus="num">
					<tr class="table_list_row2">
						<td align="center">${num.index+1}</td>
						<td align="center">${applyR.CLAIM_NO}</td>
						<td align="center">${applyR.VIN}</td>
						<td align="center">${applyR.NAMES}</td>
						<td align="center">${applyR.LABOUR_CODE}</td>
						<td align="center">${applyR.LABOUR_NAME}</td>
						<td align="center">${applyR.LABOUR_PRICE}</td>
						<td align="center">${applyR.DEDUCT_REMARK}</td>
						<td align="center">${applyR.REMARK}</td>
					</tr>
				</c:forEach>
			</table>
		<br/>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align="center" >
				<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" /></td>
          	</td>
		</tr>
</table>
	</FORM>
</body>
</html>