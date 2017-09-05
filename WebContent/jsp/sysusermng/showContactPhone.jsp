<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> maintaimHisList = (List<Map<String,Object>>)request.getAttribute("maintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>站长及三包员联系电话</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;站长及三包员联系电话</div>
		</td></tr>
		<tr><td>
			<table id="itemTableId" border="1" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<tr >
					<td>
						电话
					</td>
				</tr>
				<c:if test="${!empty listPo}">
						<tr>
							<td>${listPo}</td>
						</tr>
				</c:if>
				
				<tbody id="itemTable">
				</tbody>
			</table>
		</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td align="center">
				<input type="button" name="closeBtn" class="normal_btn" value="关闭" onclick="window.close();"/>
			</td>
		</tr>
	</table>
</body>
</html>